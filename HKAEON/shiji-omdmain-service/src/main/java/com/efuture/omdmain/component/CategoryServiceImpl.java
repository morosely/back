package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.product.component.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BusinessCompanyModel;
import com.efuture.omdmain.model.CategoryLabelModel;
import com.efuture.omdmain.model.CategoryManageLevelModel;
import com.efuture.omdmain.model.CategoryModel;
import com.efuture.omdmain.model.CategoryTreeBean;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.service.CategoryService;
import com.efuture.omdmain.utils.CommonUtils;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.AuthorityBaseServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:
 */
public class CategoryServiceImpl extends CommonServiceImpl<CategoryModel,CategoryServiceImpl> implements CategoryService {

	@Autowired
	AuthorityBaseServiceImpl authorityBaseService;
	@Autowired
	CategoryManageLevelServiceImpl categoryManageLevelService;
	@Autowired
	CategoryRedisServiceImpl categoryRedisServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}
    
    //通过末级查询上级编码和名称
    public ServiceResponse selectLevelCateCode(ServiceSession session, JSONObject paramsObject) throws Exception {
    	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"selectLevel","categoryLevel","categoryCodes");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		
		List list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.selectLevelCateCode",paramsObject);
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(), list);
		paramsObject.put("total_results", list == null ? 0 : list.size());
    	return ServiceResponse.buildSuccess(paramsObject);
    }
    
    
    //营销级次（任意级次）、编码、品称查询品类的AriticleCode，ClassCode的接口
    public ServiceResponse categoryToMarket(ServiceSession session, JSONObject paramsObject) throws Exception {
    	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"inputLevel","returnLevel");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		
		List list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.categoryToMarket",paramsObject);
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(), list);
		paramsObject.put("total_results", list == null ? 0 : list.size());
    	return ServiceResponse.buildSuccess(paramsObject);
    }
    
	//品类通过父级查询所有叶子节点
	public ServiceResponse queryLeafCategoryCodes(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","categoryCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		
		String categoryCode = paramsObject.getString("categoryCode");
		JSONObject param = new JSONObject();
		param.put("categoryCode", categoryCode);
		List<CategoryModel> findCategoryCodeAndLevelList = this.wrapQueryBeanList(session, param);
		if(findCategoryCodeAndLevelList!=null && findCategoryCodeAndLevelList.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"查询的编码不存在");
		}
		//如果是第五级直接返回
		if(findCategoryCodeAndLevelList != null && findCategoryCodeAndLevelList.size() == 1) {
			CategoryModel model = findCategoryCodeAndLevelList.get(0);
			if("5".equals(model.getLevel())) {
				paramsObject.clear();
				paramsObject.put("leafCategoryCode", model.getCategoryCode());
				paramsObject.put("total_results",1);
				return ServiceResponse.buildSuccess(paramsObject);
			}
		}
		
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId);
		Query query = new Query(criteria);
		Field fields = query.fields();
		this.getTemplate().onSetContext(session);
		//profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,restricted,concat(categoryCode,'-',level) as categoryCodeAndLevel,concat(parentCode,'-',level-1) as parentCodeAndLevel");
		List<CategoryTreeBean> categorys  =  this.getTemplate().select(query, CategoryTreeBean.class, "category");
//		Short level = paramsObject.getShort("level");
//		String findCategoryCodeAndLevel = String.join("-", categoryCode,level.toString());
		List<String> leafCodes = getMapTree(categorys,findCategoryCodeAndLevelList);
		paramsObject.clear();
		paramsObject.put("leafCategoryCode", leafCodes);
		paramsObject.put("total_results", leafCodes.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	public List<String> getMapTree(List<CategoryTreeBean> nodes,List<CategoryModel> findCategoryCodeAndLevelList){
		Map<Object, List<CategoryTreeBean>> nodeMap = new HashMap<Object, List<CategoryTreeBean>>();
		for (CategoryTreeBean node : nodes) {
			// 程序设置顶级节点的父亲节点编码为0
			String parentCodeAndLevel = StringUtils.isEmpty(node.getParentCodeAndLevel()) ? "-0":node.getParentCodeAndLevel();
			List<CategoryTreeBean> childrenNodes;
			if (nodeMap.containsKey(parentCodeAndLevel)) {
				childrenNodes = nodeMap.get(parentCodeAndLevel);
			} else {
				childrenNodes = new ArrayList<CategoryTreeBean>();
				nodeMap.put(parentCodeAndLevel, childrenNodes);
			}
			childrenNodes.add(node);
		}
		List<String> leafCodes = new ArrayList<String>();
		for (CategoryModel find : findCategoryCodeAndLevelList) {
			String findCategoryCodeAndLevel = String.join("-", find.getCategoryCode(),find.getLevel().toString());
			 getLeafNode(nodeMap,findCategoryCodeAndLevel,leafCodes);
		}
		return leafCodes;
    }

	private List<CategoryTreeBean> getLeafNode(Map<Object, List<CategoryTreeBean>> nodeMap, String parentCodeAndLevel,List<String> leafCodes) {
    	List<CategoryTreeBean> childrens = nodeMap.get(parentCodeAndLevel);
        if(childrens == null){
        	String str[] = parentCodeAndLevel.split("-");
        	String categoryCode = str[0];
        	String level = str[1];
        	if("5".equals(level)) {
        		leafCodes.add(categoryCode);
        	}
            return null;
        }
        for(CategoryTreeBean node : childrens){
        	node.setChildren(getLeafNode(nodeMap, node.getCategoryCodeAndLevel(),leafCodes));
        }
        return childrens;
	}
	
    //设置受限品类（保存）
    @Transactional(propagation = Propagation.REQUIRED)
    public ServiceResponse restrictedSave(ServiceSession session, JSONObject paramsObject) throws Exception {
    	//1.校验参数
    	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode");
      	if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
      	JSONArray paramCategoryCodes = paramsObject.getJSONArray("categoryCodes");
		List newArtiCodes = new ArrayList(paramCategoryCodes);
      	List newCloneArtiCodes = new ArrayList(paramCategoryCodes);
      	
      	//2查询数据库的选中原始ArtiCode
      	Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("level").is(4).and("restricted").is(1);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,restricted");
		List<CategoryModel> oldCategorys  =  this.getTemplate().select(query, CategoryModel.class, "category");
		List oldArtiCodes = null;
		if(oldCategorys!=null && !oldCategorys.isEmpty()){
			oldArtiCodes = oldCategorys.stream().map(CategoryModel::getCategoryCode).collect(Collectors.toList());
			newArtiCodes.removeAll(oldArtiCodes);//获取新增的集合
			oldArtiCodes.removeAll(newCloneArtiCodes);//获取删除的集合
		}
		
		//3.处理新增的数据
		if(newArtiCodes!=null && !newArtiCodes.isEmpty()){
	      	//3.1封装参数
	        criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(session.getEnt_id()).and("parentCode").in(newArtiCodes);
	        query = new Query(criteria);
			fields = query.fields();
			fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,restricted");
			List<CategoryModel> newClasses = this.getTemplate().select(query, CategoryModel.class, "category");
			if(newClasses == null || newClasses.isEmpty()) return ServiceResponse.buildSuccess("artiCode对应的classCode为空,没有可更新内容!");
			
			//3.2提取品类编码集合
			List<String> newClassCodes = newClasses.stream().map(CategoryModel::getCategoryCode).collect(Collectors.toList());
			//加上ArtiCode编码
			newClassCodes.addAll(newArtiCodes);
			//（动态SQL使用$）处理SQL中key的值是字符串字段添加单引号，让其走索引
		  	List<String> category = newClassCodes.stream().map(code -> {
		  			return "'" + code + "'";
		  		}).collect(Collectors.toList());
			
		  	String restricted = "1";
		  	paramsObject.put("updateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	      	//3.3.更新category表
	      	paramsObject.put("entId", entId);
	    	paramsObject.put("table","category");
			paramsObject.put("setField","restricted");
			paramsObject.put("setFieldValue",restricted);
			paramsObject.put("key","categoryCode");
			paramsObject.put("values",category);
			int categoryCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
			
			//3.4.更新goods表
			paramsObject.put("table","goods");
			paramsObject.put("setField","season");
			paramsObject.put("setFieldValue",restricted);
			paramsObject.put("key","categoryCode");
			paramsObject.put("values",category);
			int goodsCoutn = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
			
			//3.5.更新salegoods表
			paramsObject.put("table","salegoods");
			paramsObject.put("setField","season");
			paramsObject.put("setFieldValue",restricted);
			paramsObject.put("key","categoryCode");
			paramsObject.put("values",category);
			int saleGoodsCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		}
		
		//4.处理删除的数据
		if(oldArtiCodes!=null && !oldArtiCodes.isEmpty()){
	      	//4.1.封装参数
	        criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(session.getEnt_id()).and("parentCode").in(oldArtiCodes);
	        query = new Query(criteria);
			fields = query.fields();
			fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,restricted");
			List<CategoryModel> oldClasses = this.getTemplate().select(query, CategoryModel.class, "category");
			if(oldClasses == null || oldClasses.isEmpty()) return ServiceResponse.buildSuccess("artiCode对应的classCode为空,没有可更新内容!");
			
			//4.2提取品类编码集合
			List<String> oldClassCodes = oldClasses.stream().map(CategoryModel::getCategoryCode).collect(Collectors.toList());
			//加上ArtiCode编码
			oldClassCodes.addAll(oldArtiCodes);
			//（动态SQL使用$）处理SQL中key的值是字符串字段添加单引号，让其走索引
		  	List<String> category = oldClassCodes.stream().map(code -> {
		  			return "'" + code + "'";
		  		}).collect(Collectors.toList());
			
		  	String restricted = "0";
		  	paramsObject.put("updateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	      	//4.3.更新category表
	      	paramsObject.put("entId", entId);
	    	paramsObject.put("table","category");
			paramsObject.put("setField","restricted");
			paramsObject.put("setFieldValue",restricted);
			paramsObject.put("key","categoryCode");
			paramsObject.put("values",category);
			int deletec = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
			
			//4.4.更新goods表
			paramsObject.put("table","goods");
			paramsObject.put("setField","season");
			paramsObject.put("setFieldValue",restricted);
			paramsObject.put("key","categoryCode");
			paramsObject.put("values",category);
			int deleteg = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
			
			//4.5.更新salegoods表
			paramsObject.put("table","salegoods");
			paramsObject.put("setField","season");
			paramsObject.put("setFieldValue",restricted);
			paramsObject.put("key","categoryCode");
			paramsObject.put("values",category);
			int deletes = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		}
		
		return ServiceResponse.buildSuccess("更新成功");
    }
    
    //设置受限品类（查询功能）
    public ServiceResponse restrictedTree(ServiceSession session, JSONObject paramsObject) throws Exception {
    	//1.校验参数
      	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode");
      	if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
      	//2.查询全量数据(Article级别：第四级数据)
      	Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("level").lte(4);
		Query query = new Query(criteria);
		Field fields = query.fields();
		this.getTemplate().onSetContext(session);
		//profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,restricted,concat(categoryCode,'-',level) as categoryCodeAndLevel,concat(if(parentCode is null or parentCode = '', 0, parentCode),'-',level-1) as parentCodeAndLevel");
		List<CategoryTreeBean> categorys  =  this.getTemplate().select(query, CategoryTreeBean.class, "category");
//		List<CategoryTreeBean> trees = getTreeByNodes(categorys);
		//profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
		List<CategoryTreeBean> trees = getTreeByNodesProfit(categorys);
		//3.虚拟构造树
		criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode"));
		query = new Query(criteria);
		BusinessCompanyModel businessCompanyModel =  this.getTemplate().selectOne(query, BusinessCompanyModel.class, "businesscompany");
		CategoryTreeBean companyCategoryTreeBean = new CategoryTreeBean();
		companyCategoryTreeBean.setCategoryName(businessCompanyModel.getErpName());
		//Bug：经营公司和分类两张表数据构成的树，出现编码重复的情况。经营公司编码前加上字母B
		List<CategoryTreeBean> companyCategoryTrees = new ArrayList<CategoryTreeBean>();
		companyCategoryTreeBean.setCategoryCode("B"+businessCompanyModel.getErpCode());
		companyCategoryTreeBean.setLevel((short)-1);
		companyCategoryTreeBean.setErpCode(businessCompanyModel.getErpCode());
		companyCategoryTreeBean.setChildren(trees);
		companyCategoryTreeBean.setLeafFlag(false);
		companyCategoryTrees.add(companyCategoryTreeBean);
    	return ServiceResponse.buildSuccess(companyCategoryTrees);
    }
    
    //profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
    public List<CategoryTreeBean> getTreeByNodesProfit(List<CategoryTreeBean> nodes){
		Map<Object, List<CategoryTreeBean>> nodeMap = new HashMap<Object, List<CategoryTreeBean>>();
		for (CategoryTreeBean node : nodes) {
			// 程序设置顶级节点的父亲节点编码为0
			String parentCodeAndLevel = StringUtils.isEmpty(node.getParentCodeAndLevel()) ? "0-0":node.getParentCodeAndLevel();
			List<CategoryTreeBean> childrenNodes;
			if (nodeMap.containsKey(parentCodeAndLevel)) {
				childrenNodes = nodeMap.get(parentCodeAndLevel);
			} else {
				childrenNodes = new ArrayList<CategoryTreeBean>();
				nodeMap.put(parentCodeAndLevel, childrenNodes);
			}
			childrenNodes.add(node);
		}
		
//		if(nodeMap.containsKey("1112")){
//			for (CategoryTreeBean categoryTreeBean : nodeMap.get("1112")) {
//				System.out.println(categoryTreeBean.getParentCode()+" --- "+categoryTreeBean.getCategoryCode() +" --- "+categoryTreeBean.getLevel() );
//			}
//		}
		return getTreeNodeByNodeMapProfit(nodeMap, "0-0");
    }
    
    //profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
    private List<CategoryTreeBean> getTreeNodeByNodeMapProfit(Map<Object, List<CategoryTreeBean>> nodeMap, String parentCodeAndLevel) {
    	List<CategoryTreeBean> childrens = nodeMap.get(parentCodeAndLevel);
        if(childrens == null){
            return null;
        }
        for(CategoryTreeBean node : childrens){
        	node.setChildren(getTreeNodeByNodeMapProfit(nodeMap, node.getCategoryCodeAndLevel()));
        }
        return childrens;
	}
    
    //HashMap构建树形结构(根据Code构建树)
    public List<CategoryTreeBean> getTreeByNodes(List<CategoryTreeBean> nodes){
    	Map<Object,List<CategoryTreeBean>> nodeMap = new HashMap<Object, List<CategoryTreeBean>>();
    	 for(CategoryTreeBean node : nodes){
    		 //程序设置顶级节点的父亲节点编码为0
             String parentCode = StringUtils.isEmpty(node.getParentCode())? "0" : node.getParentCode();
             List<CategoryTreeBean> childrenNodes ;
             if(nodeMap.containsKey(parentCode)){
                 childrenNodes = nodeMap.get(parentCode);
             }else{
                 childrenNodes = new ArrayList<CategoryTreeBean>();
                 nodeMap.put(parentCode, childrenNodes);
             }
             childrenNodes.add(node);
         }
    	 return getTreeNodeByNodeMap(nodeMap,"0");
    }
    
    //顶级Code递归构建树
    private List<CategoryTreeBean> getTreeNodeByNodeMap(Map<Object, List<CategoryTreeBean>> nodeMap, String parentCode) {
    	List<CategoryTreeBean> childrens = nodeMap.get(parentCode);
        if(childrens == null){
            return null;
        }
        for(CategoryTreeBean node : childrens){
        	node.setChildren(getTreeNodeByNodeMap(nodeMap, node.getCategoryCode()));
        }
        return childrens;
	}

	//查询某个经营公司所有的叶子节点分类(分页)
    public ServiceResponse getCategoryLeafListPage(ServiceSession session, JSONObject paramsObject) throws Exception {
    	//1.校验参数
      	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","leafFlag");
      	if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
      	//2.查询某个经营公司的所有分类
		paramsObject.put("fields","categoryId,categoryCode,entId,erpCode,categoryName");
		List<CategoryModel> categoryList = wrapQueryBeanList(session,paramsObject);
		for (CategoryModel categoryModel : categoryList) {
			categoryModel.setCategoryName(categoryModel.getCategoryCode()+"-"+categoryModel.getCategoryName());
		}
      	//3.返回数据
    	return ServiceResponse.buildSuccess(categoryList);
    }
    
    //查询某个经营公司所有的叶子节点分类
    public ServiceResponse getCategoryLeafList(ServiceSession session, JSONObject paramsObject) throws Exception {
    	//1.校验参数
      	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode");
      	if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
      	//2.查询某个经营公司的所有分类
      	Criteria criteria = null;
      	//2.1去除空字符串
      	paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
      	//2.2 如果品类名称，品类支持模糊查询
      	String categoryName = paramsObject.getString("categoryName");
      	if(!StringUtils.isEmpty(categoryName)){ 
      		categoryName = "%" + categoryName + "%";
      		criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(session.getEnt_id()).and("leafFlag").is(1)
      				.and("categoryName").regex(escapeExprSpecialWord(categoryName));
      	}else{
      		criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(session.getEnt_id()).and("leafFlag").is(1);
      	}
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("categoryId,categoryCode,entId,erpCode,categoryName");
		List<CategoryModel> categoryList = this.getTemplate().select(query, CategoryModel.class, "category");
      	//3.返回数据
    	return ServiceResponse.buildSuccess(categoryList);
    }
    
    public boolean verifyData(ServiceSession session, JSONObject paramsObject){
        ServiceResponse ssData = onQuery(session, paramsObject);
        JSONObject dataObject = (JSONObject)ssData.getData();
        List<RowMap> dataList = (List<RowMap>) dataObject.get(this.getCollectionName());
        if(dataList.size() == 0){
            return false;
        }
        return true;
    }

    /*
    * @Description: 非子节点查询，首先拿着id 查询是否为一个父节点，若为子节点，提示刷新后再查询
    * @param session
    * @param paramsObject
    * @return: 返回一个列表其中可能包含子节点和非子节点
    */
    @Override
    public ServiceResponse pGet(ServiceSession session, JSONObject paramsObject) {

        paramsObject.put("leafFlag", 0);
        if (!this.verifyData(session, paramsObject)){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "工业分类已修改，请重新刷新页面");
        }
        JSONObject realParams = new JSONObject();
        realParams.put("parentId", paramsObject.get("categoryId"));
        realParams.put("fields", "categoryId, categoryCode, parentCode, leafFlag");
        return onQuery(session, realParams);
    }

    /*
    * @Description: 1 查询子节点，首先判断该节点是否为子节点，然后获取该节点的详细信息 2 需要根据categoryId 查询
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    @Override
    public ServiceResponse sGet(ServiceSession session, JSONObject paramsObject) {
        paramsObject.put("lleafFlage", 1);
        if(!this.verifyData(session, paramsObject)){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "工业分类已修改，请重新刷新页面");
        }
        //需要查询类别标签，

        List<String> catLabelName = new ArrayList<>();
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        List<Map> resultList =  template.getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.getSaleGoodsImg",paramsObject.get("categoryId"));
        for (Map result: resultList){
            catLabelName.add((String) result.get("catLabelName"));
        }
        ServiceResponse rtData = onQuery(session, paramsObject);
        JSONObject rtObject = (JSONObject) rtData.getData();
        rtObject.put("catLabelName", catLabelName);
        return rtData;
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }

    //EPR分类查询
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
		return this.onQuery(session, paramsObject);
	}

	// erpCode 和 categoryCode逐级查类别---pos专用
	public ServiceResponse query(ServiceSession session, JSONObject paramsObject) {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "categoryCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
//		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		paramsObject.put("entId",session.getEnt_id());
		paramsObject.put("parentCode",paramsObject.getString("categoryCode"));

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List categoryList = template.getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.getCategoryList", paramsObject);
		paramsObject.clear();
		paramsObject.put("categoryList",categoryList);
//		paramsObject.put("category_total",categoryList.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//查询当前节点的所有儿子节点
	public ServiceResponse searchSon(ServiceSession session, JSONObject paramsObject) {
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("parentId").is(paramsObject.get("parentId")).and("erpCode").is(paramsObject.get("erpCode")).and("level").is(paramsObject.get("level"));
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,license");
		List<CategoryTreeBean> list  = new ArrayList<>();
		try {
			list = this.getTemplate().select(query, CategoryTreeBean.class, "category_view");
		}catch (Exception e){
			logger.error(e.getMessage());
			list = this.getTemplate().select(query, CategoryTreeBean.class, "category");

		}
//		剪枝
		List<CategoryTreeBean> allCategory = getAllCategory(session, paramsObject);
		paramsObject.clear();
		paramsObject.put("category",compareCategory(list, allCategory));
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	@Override
	public ServiceResponse detail(ServiceSession session, JSONObject paramsObject) {
		//1.查询当前节点的信息
		ServiceResponse nodeInfo = this.onQuery(session, paramsObject);
		
		//2.查询类别标签
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
		List<CategoryLabelModel> checkBoxCategoryLables = template.getSqlSessionTemplate().selectList("beanmapper.CategoryLabelModelMapper.getCategoryLabelCheckbox",paramsObject);
		
		//3.封装返回数据
		Map nodeMap = (Map)nodeInfo.getData();
		nodeMap.put("categorylabel", checkBoxCategoryLables);
		return ServiceResponse.buildSuccess(nodeMap);
	}
	
	//封装返回参数（只返回顶层数据）异步加载
	public ServiceResponse onCategoryTopTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		List<CategoryTreeBean> categoryTree = categoryTopTreeForCompany(session,paramsObject);
		paramsObject.clear();
		paramsObject.put("category", categoryTree);
		return ServiceResponse.buildSuccess(paramsObject);
	}
		
	//增加经营公司的树（只返回顶层数据）异步加载
	public List<CategoryTreeBean> categoryTopTreeForCompany(ServiceSession session, JSONObject paramsObject){
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("status").is("1");
		Query query = new Query(criteria);
		//查询经营公司
		List<BusinessCompanyModel> allBusinessCompany = this.getTemplate().select(query, BusinessCompanyModel.class, "businesscompany");
		//保存虚拟树的List
		List<CategoryTreeBean> companyCategoryTrees = new ArrayList<CategoryTreeBean>();
		for (BusinessCompanyModel businessCompanyModel : allBusinessCompany) {
			paramsObject.put("erpCode", businessCompanyModel.getErpCode());
			List<CategoryTreeBean> categoryTrees = categoryTopTree(session,paramsObject);//获取某个经营公司的顶层树
			//虚拟构造树
			CategoryTreeBean companyCategoryTreeBean = new CategoryTreeBean();
			companyCategoryTreeBean.setCategoryName(businessCompanyModel.getErpName());
			companyCategoryTreeBean.setCategoryCode(businessCompanyModel.getErpCode());
			companyCategoryTreeBean.setChildren(categoryTrees);
			companyCategoryTrees.add(companyCategoryTreeBean);
		}
		return companyCategoryTrees;
	}
	
	//返回树的顶层数据（只返回顶层数据）异步加载
	public List<CategoryTreeBean> categoryTopTree(ServiceSession session, JSONObject paramsObject){
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("level").is(1);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,license");
		List<CategoryTreeBean> topCategorys  = new ArrayList<>();
		try {
			topCategorys = this.getTemplate().select(query, CategoryTreeBean.class, "category_view");
		}catch (Exception e){
			logger.error(e.getMessage());
			topCategorys = this.getTemplate().select(query, CategoryTreeBean.class, "category");

		}
		//做出剪枝两边的树进行对比剪枝
		List<CategoryTreeBean> allCategorys = getAllCategory(session, paramsObject);
		return compareCategory(topCategorys, allCategorys);
	}

	private List<CategoryTreeBean> compareCategory(List<CategoryTreeBean> topCategorys, List<CategoryTreeBean> allCategorys) {
		List<CategoryTreeBean> rtTrees = new ArrayList<>();
		for (CategoryTreeBean topCategory : topCategorys) {
			for (CategoryTreeBean allCategory : allCategorys) {
				if(topCategory.getCategoryId().equals(allCategory.getCategoryId())){
					rtTrees.add(topCategory);
					break;
				}
			}
		}
		return rtTrees;
	}


	//封装返回格式 （工业分类树形结构展示）
	public ServiceResponse onCategoryTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		System.out.println("start------" + new Date().getTime());
		List<CategoryTreeBean> categoryTree = categoryTreeForCompany(session,paramsObject);
		paramsObject.clear();
		paramsObject.put("category", categoryTree);
		System.out.println("end------" + new Date().getTime());
		return ServiceResponse.buildSuccess(paramsObject);

	}
	
	//增加经营公司的树
	public List<CategoryTreeBean> categoryTreeForCompany(ServiceSession session, JSONObject paramsObject){
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("status").is("1");
		//如果传入参数有erpCode，只查当前的erpCode
		String erpCode = paramsObject.getString("erpCode");
		criteria = erpCode != null ? criteria.and("erpCode").is(erpCode) : criteria;
		Query query = new Query(criteria);
		//查询经营公司
		List<BusinessCompanyModel> allBusinessCompany = this.getTemplate().select(query, BusinessCompanyModel.class, "businesscompany");
		//保存虚拟树的List
		List<CategoryTreeBean> companyCategoryTrees = new ArrayList<CategoryTreeBean>();
		for (BusinessCompanyModel businessCompanyModel : allBusinessCompany) {
			paramsObject.put("erpCode", businessCompanyModel.getErpCode());
			List<CategoryTreeBean> categoryTrees = categoryTree(session,paramsObject);//获取某个经营公司的树
			//虚拟构造树
			CategoryTreeBean companyCategoryTreeBean = new CategoryTreeBean();
			companyCategoryTreeBean.setCategoryName(businessCompanyModel.getErpName());
			//Bug：经营公司和分类两张表数据构成的树，出现编码重复的情况。经营公司编码前加上字母B
			companyCategoryTreeBean.setCategoryCode("B"+businessCompanyModel.getErpCode());
			companyCategoryTreeBean.setLevel((short)-1);
			companyCategoryTreeBean.setErpCode(businessCompanyModel.getErpCode());
			companyCategoryTreeBean.setChildren(categoryTrees);
			companyCategoryTrees.add(companyCategoryTreeBean);
		}
		return companyCategoryTrees;
	}
	
	//展示所有工业分类信息 （树形结构）
	public List<CategoryTreeBean> categoryTree(ServiceSession session, JSONObject paramsObject){
		//1.查询所有门店数据
		List<CategoryTreeBean> allCategorys = getAllCategory(session, paramsObject);
		//借助HashMap生成树
		CommonUtils<CategoryTreeBean> commonUtils = new CommonUtils<>();
		HashMap<Long, List<CategoryTreeBean>> pidCategoryTree = commonUtils.getPidEnty(allCategorys);
		List<CategoryTreeBean> topTree = pidCategoryTree.get(-1l);
		if(topTree == null || topTree.size() == 0) return new ArrayList<>();
		System.out.println("开始递归----" + new Date().getTime());
		buildTree(topTree, pidCategoryTree);//从顶级节点开始往下递归
		System.out.println("结束递归----" + new Date().getTime());
		return topTree;
	}

	private List<CategoryTreeBean> getAllCategory(ServiceSession session, JSONObject paramsObject) {
		/**
		 * 得到所有的分类的数据
		 * 权限控制 根据auth判断， 并且redis中有控制的数据范围
		 * 根据levelManager 如果包含levelManager 且 levelManager 为 1 则需要根据部类查询
		 * @param session
		 * @param paramsObject
		 * @return java.util.List<com.efuture.omdmain.model.CategoryTreeBean>
		 * @throws
		 */
		Long entId = session.getEnt_id();
		int level = 2;
		if(paramsObject.containsKey("levelManager") && paramsObject.getInteger("levelManager").equals(1)){
//			得到管理的部类，需要考虑没有设置部类的情况
			CategoryManageLevelModel categoryManageLevelModel = this.getTemplate().selectOne(new Query()
					.addCriteria(Criteria.where("entId").is(entId).and("erpCode").is(paramsObject.getString("erpCode"))),
					CategoryManageLevelModel.class ,"categoryManageLevel");
			if(categoryManageLevelModel != null && categoryManageLevelModel.getLevel() != null){
				level = categoryManageLevelModel.getLevel();
			}
		}
		Criteria criteria = null;
		if (paramsObject.containsKey("auth") || "0".equals(paramsObject.get("auth"))){
			criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId).and("level").lte(level);
		}else {
			criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId);

		}
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode");
		boolean isAuth = false; // 默认不走权限控制
		List<String>categoryIds = null;
		logger.info("redis------start" + new Date().getTime());
		try {
			categoryIds = authorityBaseService.getDataRangeCategory(session);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		logger.info("redis------end" + new Date().getTime());
		if((!paramsObject.containsKey("auth") || "1".equals(paramsObject.get("auth"))) && categoryIds != null && categoryIds.size() > 0){
			//走权限控制
			isAuth = true;
		}
		logger.error(String.valueOf(isAuth));
		List<CategoryTreeBean> allCategorys  = new ArrayList<>();
		logger.info("数据库------start" + new Date().getTime());
		try {
			allCategorys = this.getTemplate().select(query, CategoryTreeBean.class, "category_view");
		}catch (Exception e){
			logger.error(e.getMessage());
			allCategorys = this.getTemplate().select(query, CategoryTreeBean.class, "category");

		}
		logger.info("数据库------end" + new Date().getTime());
		//通过权限认证剪枝
		if(isAuth){
			logger.error("start-----auth");
			allCategorys = getAuthCategory(session, allCategorys);
		}
		return allCategorys;
	}

	//通过权限认证剪枝
	private List<CategoryTreeBean> getAuthCategory(ServiceSession session, List<CategoryTreeBean> allCategorys) {
		List<String>categoryIds = authorityBaseService.getDataRangeCategory(session);
		//List<String> categoryIds = Arrays.asList("3");
		List<CategoryTreeBean> lastCategorys = new ArrayList<>();
		List<CategoryTreeBean> rtcategorys = new ArrayList<>();
		getLastShops(allCategorys, lastCategorys, categoryIds);
		if(lastCategorys.size() < 1) {
			return new ArrayList<>();
		}
		getShopList(lastCategorys, allCategorys, rtcategorys);
		allCategorys.clear();
		return rtcategorys;
	}

	//得到权限范围内的category
	private void getLastShops(List<CategoryTreeBean> allCategorys, List<CategoryTreeBean> lastCategorys, List<String> categoryIds) {
		for (CategoryTreeBean allCategory : allCategorys) {
			if(allCategory.getLeafFlag()){
				for (String categoryId : categoryIds) {
					if(String.valueOf(allCategory.getCategoryId()).equals(categoryId)){
						lastCategorys.add(allCategory);
					}
				}
			}
		}
	}


	private void getShopList(List<CategoryTreeBean> chidList, List<CategoryTreeBean> allCategorys, List<CategoryTreeBean> rtcategorys){
		while (chidList.size() > 0){
			List<CategoryTreeBean> newChidList = new ArrayList<>();
			for (CategoryTreeBean categoryTreeBean : chidList) {
				rtcategorys.add(categoryTreeBean);
				//最顶级 就直接返回
				if(categoryTreeBean.getLevel() == 1 ||categoryTreeBean.getParentCode() == null ||
						"0".equals(categoryTreeBean.getParentCode())) continue;
				for (CategoryTreeBean allCategory : allCategorys) {
					if(categoryTreeBean.getParentId().equals(allCategory.getCategoryId())){
						if(!newChidList.contains(allCategory)){
							newChidList.add(allCategory);
						}
						break;
					}
				}
			}
			chidList = newChidList;
		}
	}

	//从顶级节点开始递归构建树
	private void buildTree(List<CategoryTreeBean> root, HashMap<Long, List<CategoryTreeBean>> pidCategoryTree) {

		for (CategoryTreeBean categoryTreeBean : root) {
			if (pidCategoryTree.get(categoryTreeBean.getCategoryId()) == null) continue;
			categoryTreeBean.setChildren(pidCategoryTree.get(categoryTreeBean.getCategoryId()));
			pidCategoryTree.remove(categoryTreeBean.getCategoryId());
			buildTree(categoryTreeBean.getChildren(), pidCategoryTree);
		}
	}
    
	//查询该节点的所有儿子节点
	private List<CategoryTreeBean> findChildren(Long parentId,String parentName, List<CategoryTreeBean> allCategorys) {
		List<CategoryTreeBean> childrenCategory = new ArrayList<CategoryTreeBean>();
		for (CategoryTreeBean category : allCategorys) {
			if(parentId.equals(category.getParentId())){
				childrenCategory.add(category);
			}
		}
		return childrenCategory;
	}
	
	public ServiceResponse searchByCategoryCodeAndErpCode(ServiceSession session, JSONObject paramsObject){
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "categoryCode","erpCode");
		if(ResponseCode.SUCCESS.equals(result.getReturncode())){
			return this.onQuery(session, paramsObject);
		}
		return result;
	}
	
	public ServiceResponse search4Like(ServiceSession session, JSONObject paramsObject) {
	  ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "leafFlag", "status", "erpCode");
      if(!ResponseCode.SUCCESS.equals(result.getReturncode())){
          return result;
      }
      
      paramsObject.put("entId", session.getEnt_id());
      DefaultParametersUtils.addSplitPageParams(paramsObject);
      if(!paramsObject.containsKey("erpCode")) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "erpCode不能为空.");
      }

      FMybatisTemplate template = this.getTemplate();
      template.onSetContext(session);
      List<CategoryModel> categoryModel = template.getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.search4Like", paramsObject);
      return ServiceResponse.buildSuccess(categoryModel);
	}
	
	
	@Transactional
	@Override
	public ServiceResponse onImportData(ServiceSession session, String params, MultipartFile file) throws Exception {
		ServiceResponse response = null;
		List<Map<String, Object>> dataList = null;
		FMybatisTemplate template = this.getTemplate();
		try{
		  dataList = onBeforeImportData(params, file);
	    }catch(Exception e){
		  response = new ServiceResponse();
		  response.setReturncode(ResponseCode.EXCEPTION);
		  response.setData(e.getMessage());
		  return response;
	    }
		Map<String, Object> map = null;
		JSONObject paramsObject = null;
		Query query = null;
		Map<String, Object> entity = null;
		for (int i = 0; i < dataList.size(); i++) {
			map = dataList.get(i);
			paramsObject = JSONObject.parseObject(JSON.toJSONString(map));
			query = new Query(Criteria.where("entId").is(map.get("entId"))
					.and("erpCode").is(map.get("erpCode"))
					.and("categoryCode").is(map.get("parentCode")));
			entity = template.selectOne(query, this.getCollectionName());
			if(entity == null){
				throw new Exception("上级代码"+map.get("parentCode")+"不存在！");
			}
			
			query = new Query(Criteria.where("entId").is(map.get("entId"))
					.and("erpCode").is(map.get("erpCode"))
					.and("categoryCode").is(map.get("categoryCode")));
			entity = template.selectOne(query, this.getCollectionName());
			if(entity == null){
				paramsObject.put("creator", session.getUser_name());
				paramsObject.put("createDate", new Date());
				this.onInsert(session, paramsObject);
			}else{
				paramsObject.put("modifier", session.getUser_name());
				paramsObject.put("updateDate", new Date());
				paramsObject.put(this.getKeyfieldName(), entity.get(this.getKeyfieldName()));
				this.onUpdate(session, paramsObject);
			}
		}
		return ServiceResponse.buildSuccess("");
	}
	  
    //获取某个经营公司的所有部类（无分页）
    public ServiceResponse levelCategory(ServiceSession session, JSONObject paramsObject) {
    	//1.校验必填字段
    	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
    	
		Long entId = session.getEnt_id();
		//2.查询数据库获取部类级别
    	Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(paramsObject.getString("erpCode"));
    	Query query = new Query(criteria);
    	Field fields = query.fields();
    	fields.include("cml,entId,erpCode,level");
		CategoryManageLevelModel categoryManageLevel = this.getTemplate().selectOne(query, CategoryManageLevelModel.class,"categoryManageLevel");
		//如果为空，设置缺省值为1
		Short level = StringUtils.isEmpty(categoryManageLevel) ? Short.parseShort("1") : (StringUtils.isEmpty(categoryManageLevel.getLevel()) ? Short.parseShort("1"): categoryManageLevel.getLevel());
    	
		//2.查询某个经营公司的所有部类集合
		criteria = Criteria.where("level").is(level).and("entId").is(entId).and("erpCode").is(paramsObject.getString("erpCode"));
    	query = new Query(criteria);
    	List<CategoryModel> levelCategoryList = this.getTemplate().select(query, CategoryModel.class, "category");
    	
    	//3.返回封装结果
    	return ServiceResponse.buildSuccess(levelCategoryList);
    }
    
    //设置所有叶子节点管理部类
    @Transactional(propagation = Propagation.REQUIRED)
    public ServiceResponse resetLeafManageCategoryCode(ServiceSession session, JSONObject paramsObject) {
    	//0.参数校验
    	ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","level");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
    	String erpCode =  paramsObject.getString("erpCode");
    	Short level = paramsObject.getShort("level");
    	Long entId = session.getEnt_id();
		
		//1.保存管理部类表
    	Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode);
    	Query query = new Query(criteria);
    	CategoryManageLevelModel queryLevelModel = this.getTemplate().selectOne(query, CategoryManageLevelModel.class, "categoryManageLevel");
    	ServiceResponse response = null;
    	if(StringUtils.isEmpty(queryLevelModel)){//新增
    		response = categoryManageLevelService.onInsert(session,paramsObject);
    	}else{//更新
    		queryLevelModel.setLevel(level);
    		categoryManageLevelService.setUpsert(false);
    		response = categoryManageLevelService.onUpdate(session, JSON.parseObject(JSON.toJSONString(queryLevelModel)));
    	}
    	if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		//2.获取某个经营公司的所有部类
    	criteria = Criteria.where("level").is(level).and("entId").is(entId).and("erpCode").is(erpCode);
    	query = new Query(criteria);
    	List<CategoryModel> levelCategoryList = this.getTemplate().select(query, CategoryModel.class, "category");
    	
    	//3.获取某个经营公司所有类型
    	criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode);
    	query = new Query(criteria);
    	List<CategoryModel> allList = this.getTemplate().select(query, CategoryModel.class, "category");
    	
    	//4.循环部类级别
    	if(levelCategoryList==null || levelCategoryList.isEmpty()){
    		return ServiceResponse.buildSuccess("部类数据为空");
    	}
    	List<CategoryModel> allLeafList = new ArrayList<>();//存放所有部类的叶子节点的数据
    	for (CategoryModel levelModel : levelCategoryList) {
    		List<CategoryModel> levelLeafNotes = getLeafNodes(allList,levelModel);//某一个部类的叶子节点的数据
    		if(levelLeafNotes != null && !levelLeafNotes.isEmpty()){
    			allLeafList.addAll(levelLeafNotes);
    		}
		}
    	
    	//5.更新所有管理部类的叶子节点(批量更新)
    	if(allLeafList!=null &&!allLeafList.isEmpty())
    		this.getTemplate().getSqlSessionTemplate().update("beanmapper.CategoryModelMapper.batchUpdateManageCategoryCode", allLeafList);
    	return ServiceResponse.buildSuccess("更新部类数量："+allLeafList.size());
    }
    
	//指定父亲节点ID查询所有叶子节点（叶子节点）
	private List<CategoryModel> getLeafNodes(List<CategoryModel> allList, CategoryModel levelModel) {
		if (allList == null || levelModel.getParentId() == null || allList.isEmpty())
			return null;
		//叶子节点结果集
		List<CategoryModel> leafList = new ArrayList<CategoryModel>();
		recursionLeaf(allList, levelModel, levelModel.getCategoryCode(),leafList);
		return leafList;
	}
	
	//递归（叶子节点）
	private void recursionLeaf(List<CategoryModel> allList, CategoryModel node ,String manageCategoryCode,List<CategoryModel> leafList) {
		List<CategoryModel> childList = getChildList(allList, node);// 得到子节点列表
		if (!childList.isEmpty()) {// 判断是否有子节点
			for (CategoryModel childNode : childList) {
				recursionLeaf(allList, childNode,manageCategoryCode,leafList);
			}
		} else {//叶子节点
			node.setManageCategoryCode(manageCategoryCode);//设置叶子节点的管理部类
			leafList.add(node);//将叶子节点加入返回集合中
		}
	}
	
	// 得到子节点列表( 当前节点的下一级所有节点集合)
    private List<CategoryModel> getChildList(List<CategoryModel> allList, CategoryModel node) {
        List<CategoryModel> childList = new ArrayList<CategoryModel>();
        for (CategoryModel allNode : allList) {
        	if(allNode.getParentCode().equals(node.getCategoryCode())){
        		 childList.add(allNode);
        	}
		}
        return childList;
    }

    
    /**
     *  是否打印黄色小票--提供给POS总部      license--1：是  0：否
     * @param session
     * @param paramsObject
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ServiceResponse batchPrintYellowTicket1(ServiceSession session, JSONObject paramsObject) throws Exception {
    	
    	// 1.传参校验
    	ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
    	if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", this.getCollectionName()));
		}
    	JSONArray categoryList = paramsObject.getJSONArray(this.getCollectionName()); // ---页面传来的勾选上的节点数据
    	
		if (!StringUtils.isEmpty(categoryList)) {
			for (int i = 0; i < categoryList.size(); i++) {
				checkParam = ParamValidateUtil.checkParam(session, categoryList.getJSONObject(i), "erpCode");
				if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
					return checkParam;
				}
			}
		}
		
		// 2.查询所选节点的全部经营公司
		int size = categoryList.size();   // 页面传来的勾选节点数
		List<CategoryModel> allList = new ArrayList<>();// 存放查到的全部经营公司数据
		Long entId = session.getEnt_id();
		
		if (size > 0) {
			JSONObject categoryModel = categoryList.getJSONObject(0);
			String ckErpCode = categoryModel.getString("erpCode");   // 参考的经营公司
			List<String> erpCodeList = new ArrayList<>();            // 存放需要查的经营公司
			erpCodeList.add(ckErpCode);
			for (int i = 0; i < size; i++) {
				JSONObject category = categoryList.getJSONObject(i);
				String erpCode = category.getString("erpCode");
				if (!ckErpCode.equals(erpCode)) {
					erpCodeList.add(erpCode);
				}
			}
			
			for (int j = 0; j < erpCodeList.size(); j++) {
				// 获取某个经营公司所有类型
				String erpCode =  erpCodeList.get(j);
				Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode);
				Query query = new Query(criteria);
				Field fields = query.fields();
				fields.include("categoryId,parentId,erpCode,license");
				List<CategoryModel> getallList = this.getTemplate().select(query, CategoryModel.class, "category");
				allList.addAll(getallList);
			}
		}
		// 此时页面是要全部设否操作，先查到全部勾选的节点数据---旧数据--也是备份旧数据
		Criteria criteria = Criteria.where("entId").is(entId).and("license").is(1);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("categoryId,parentId,erpCode,license");
		List<CategoryModel> oldallList = this.getTemplate().select(query, CategoryModel.class, "category");
    	
    	// 3.遍历查所选节点的全部叶子节点
    	List<CategoryModel> levelLeafNotes = new ArrayList<>(); // 存放页面传来的节点进行递归后的数据--即下面进行对比分析的***新数据
		for (int m = 0; m < size; m++) {
			CategoryModel category = categoryList.getObject(m, CategoryModel.class);
			String ErpCode = category.getErpCode(); // 这是勾选节点的经营公司
			
			// 这时候勾选的是经营公司节点
			if(StringUtils.isEmpty(category.getCategoryId())){
				// 子节点从allList中遍历找
				for (CategoryModel categoryModel : allList) {
					if (ErpCode.equals(categoryModel.getErpCode())) {
						levelLeafNotes.add(categoryModel);
					}
				}
			} else {
				// 这时候勾选的是非经营公司节点，递归找子节点
				List<CategoryModel> levelLeafNotes1 = getLeafList(allList, category);// 某一个所选类别的叶子节点的数据
				levelLeafNotes.addAll(levelLeafNotes1); 
			}
		}
		
    	// 4.找出操作前是勾选状态的旧数据
		List<CategoryModel> oldList = new ArrayList<CategoryModel>();  // 操作前的旧数据
		oldList.addAll(oldallList); 
		
		// 5.新旧数据对比分析
		oldList.removeAll(levelLeafNotes);        // 原旧数据  - 新数据            = 需要更新为 0 的数据, 走批量更新***0
		levelLeafNotes.removeAll(oldallList);     // 新数据      - 备份旧数据    = 需要更新为 1的数据, 走批量更新***1
		
		// 6.找出最终需要更新的节点
		List<CategoryModel> updateNodeList = new ArrayList<>();
    	Short license0 = 0;                   
    	for (CategoryModel categoryModel : oldList) {               // ***********设否的数据, 走批量更新--0
    		categoryModel.setLicense(license0);
    	}
    	Short license1 = 1;
    	for (CategoryModel categoryModel : levelLeafNotes) {        // ***********设是的数据, 走批量更新--1
    		categoryModel.setLicense(license1);
    	}
    	
    	// 包装数据
    	if (levelLeafNotes != null && !levelLeafNotes.isEmpty()) {
    		updateNodeList.addAll(levelLeafNotes);
    	}
    	if (oldList != null && !oldList.isEmpty()) {
    		updateNodeList.addAll(oldList);
    	}
    	
    	// 7.更新所选类别的叶子节点(批量更新)
		if (!updateNodeList.isEmpty()) {
			this.getTemplate().getSqlSessionTemplate().update("beanmapper.CategoryModelMapper.batchPrintYellowTicket", updateNodeList);
		}
		return ServiceResponse.buildSuccess("成功设置打印黄色小票类别数量：" + updateNodeList.size() + "条");
    }
  	
	// 查选中节点下面所有叶子节点
	private List<CategoryModel> getLeafList(List<CategoryModel> allList, CategoryModel levelModel) {
		if (allList == null || allList.isEmpty() || StringUtils.isEmpty(levelModel))
			return null;
		// 叶子节点结果集
		List<CategoryModel> leafList = new ArrayList<CategoryModel>();
		leafList = searchLeaf(allList, levelModel, leafList);
		leafList.add(levelModel);
		return leafList;
	}

	// 递归（叶子节点）
	private List<CategoryModel> searchLeaf(List<CategoryModel> allList, CategoryModel node,  List<CategoryModel> leafList) {
		List<CategoryModel> childList = searchChildList(allList, node);// 得到子节点列表
		for (CategoryModel childNode : childList) {
			leafList.add(childNode);
			searchLeaf(allList, childNode, leafList);
		}
		return leafList;
	}
	
	// 得到子节点列表( 当前节点的下一级所有节点集合)---按照ParentId查子节点
    private List<CategoryModel> searchChildList(List<CategoryModel> allList, CategoryModel node) {
        List<CategoryModel> childList = new ArrayList<CategoryModel>();
        for (CategoryModel allNode : allList) {
        	if(node.getCategoryId().equals(allNode.getParentId())){
        		 childList.add(allNode);
        	}
		}
        return childList;
    }
    
    // 递归优化
    private List<CategoryTreeBean> getLeafList1(List<CategoryTreeBean> root, Map<Long, List<CategoryTreeBean>> allList,List<CategoryTreeBean> leafList) {
    	if (allList == null || allList.isEmpty() || root == null || root.isEmpty())
			return null;
		for (CategoryTreeBean categoryTreeBean : root) {
			if (allList.get(categoryTreeBean.getCategoryId()) == null) {
				leafList.add(categoryTreeBean);   //  取最末级节点
				continue;
			}
			categoryTreeBean.setChildren(allList.get(categoryTreeBean.getCategoryId()));
//			leafList.addAll(allList.get(categoryTreeBean.getCategoryId()));
			allList.remove(categoryTreeBean.getCategoryId());
			getLeafList1(categoryTreeBean.getChildren(), allList,leafList);
		}
		return leafList;
	}
    
    // 整理allEnty  将List<CategoryTreeBean> 转换成     Map<Long, List<CategoryTreeBean>>  方便递归
	public static Map<Long, List<CategoryTreeBean>> getPidModel(List<CategoryTreeBean> allEnty) {

		Map<Long, List<CategoryTreeBean>> map = new HashMap<>(); // 存放最终整理结果
		List<CategoryTreeBean> beanList = null;

		for (CategoryTreeBean model : allEnty) {

			Long pid = model.getParentId() == null ? 0 : model.getParentId();
			if (!map.containsKey(pid)) {
				beanList = new ArrayList<>();
				map.put(pid, beanList);
			} else {
				beanList = map.get(pid);
			}
			beanList.add(model);
		}
		return map;
	}
    
	/**
     *  是否打印黄色小票--提供给POS总部      license--1：是  0：否
     * @param session
     * @param paramsObject
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ServiceResponse batchPrintYellowTicket2(ServiceSession session, JSONObject paramsObject) throws Exception {
    	
    	// 1.传参校验
    	ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
    	if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", this.getCollectionName()));
		}
    	JSONArray categoryList = paramsObject.getJSONArray(this.getCollectionName()); // ---页面传来的勾选上的节点数据
    	
		if (!StringUtils.isEmpty(categoryList)) {
			for (int i = 0; i < categoryList.size(); i++) {
				checkParam = ParamValidateUtil.checkParam(session, categoryList.getJSONObject(i), "erpCode");
				if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
					return checkParam;
				}
			}
		}
		
		// 2.查询所选节点的全部经营公司
		int size = categoryList.size();   // 页面传来的勾选节点数
		List<CategoryTreeBean> allList = new ArrayList<>();// 存放查到的全部经营公司数据
		Long entId = session.getEnt_id();
		
		if (size > 0) {
			JSONObject categoryModel = categoryList.getJSONObject(0);
			String ckErpCode = categoryModel.getString("erpCode");   // 参考的经营公司
			List<String> erpCodeList = new ArrayList<>();            // 存放需要查的经营公司
			erpCodeList.add(ckErpCode);
			for (int i = 0; i < size; i++) {
				JSONObject category = categoryList.getJSONObject(i);
				String erpCode = category.getString("erpCode");
				if (!ckErpCode.equals(erpCode)) {
					erpCodeList.add(erpCode);
				}
			}
			
			for (int j = 0; j < erpCodeList.size(); j++) {
				// 获取某个经营公司所有类型
				String erpCode =  erpCodeList.get(j);
				Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode);
				Query query = new Query(criteria);
				Field fields = query.fields();
				fields.include("categoryId,parentId,erpCode,license");
				List<CategoryTreeBean> getallList = this.getTemplate().select(query, CategoryTreeBean.class, "category");
				allList.addAll(getallList);
			}
		}
		// 借助HashMap整理allList
		Map<Long, List<CategoryTreeBean>> pidCategoryTree = getPidModel(allList);
		
		// 此时页面是要全部设否操作，先查到全部勾选的节点数据---旧数据--也是备份旧数据
		Criteria criteria = Criteria.where("entId").is(entId).and("license").is(1);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("categoryId,parentId,erpCode,license");
		List<CategoryTreeBean> oldallList = this.getTemplate().select(query, CategoryTreeBean.class, "category");
    	
    	// 3.遍历查所选节点的全部叶子节点
    	List<CategoryTreeBean> levelLeafNotes = new ArrayList<>(); // 存放页面传来的节点进行递归后的数据--即下面进行对比分析的***新数据
    	List<CategoryTreeBean> levelLeaf = new ArrayList<>();      // 存需要进行递归的节点
		for (int m = 0; m < size; m++) {
			CategoryTreeBean category = categoryList.getObject(m, CategoryTreeBean.class);
			String ErpCode = category.getErpCode(); // 这是勾选节点的经营公司
			// 这时候勾选的是经营公司节点
			if(StringUtils.isEmpty(category.getCategoryId())){
				// 子节点从pidCategoryTree中遍历找
				long a = 0;
				List<CategoryTreeBean> listA = pidCategoryTree.get(a);
				if(listA == null ){ // 传来的经营公司数据库找不到
					listA = new ArrayList<>();
		        }
				System.out.println(listA.size()+"-----------------------------pidCategoryTree---------------");
				for (CategoryTreeBean categoryModel : listA) {
					if (ErpCode.equals(categoryModel.getErpCode())) {
//						levelLeafNotes.add(categoryModel);
						levelLeaf.add(categoryModel);
					}
				}
				System.out.println(levelLeaf.size()+"-----------------------------levelLeaf---------------");
			}else{
				levelLeaf.add(category);
			}
		}
		
		if (!levelLeaf.isEmpty()) {
			// 这时候勾选的是非经营公司节点，递归找子节点
			List<CategoryTreeBean> updateLeafList = new ArrayList<CategoryTreeBean>();  // 存递归找到的需要修改的叶子节点
			updateLeafList = getLeafList1(levelLeaf, pidCategoryTree,updateLeafList); 
			levelLeafNotes.addAll(updateLeafList);
//			levelLeafNotes.addAll(levelLeaf);
		}
		
    	// 4.找出操作前是勾选状态的旧数据
		List<CategoryTreeBean> oldList = new ArrayList<>();  // 操作前的旧数据
		oldList.addAll(oldallList); 
		
		// 5.新旧数据对比分析
		oldList.removeAll(levelLeafNotes);        // 原旧数据  - 新数据            = 需要更新为 0 的数据, 走批量更新***0
		levelLeafNotes.removeAll(oldallList);     // 新数据      - 备份旧数据    = 需要更新为 1的数据, 走批量更新***1
		
		// 6.找出最终需要更新的节点
		List<Long> categoryIds = null;
		JSONObject updateParams = null;
		
		categoryIds = new ArrayList<>();
		updateParams = new JSONObject();
    	for (CategoryModel categoryModel : oldList) {               // ***********设否的数据, 走批量更新--0
    		categoryIds.add(categoryModel.getCategoryId());
    	}
    	
    	// 7.更新所选类别的叶子节点(批量更新)
    	long update0 = 0;
    	if(!categoryIds.isEmpty()){
    		updateParams.put("license", 0);
    		updateParams.put("categoryIds", categoryIds);
    		update0 = this.getTemplate().getSqlSessionTemplate().update("beanmapper.CategoryModelMapper.batchPrintYellowTicket", updateParams);
    	}
    	
    	categoryIds = new ArrayList<>();
		updateParams = new JSONObject();
    	for (CategoryModel categoryModel : levelLeafNotes) {        // ***********设是的数据, 走批量更新--1
    		categoryIds.add(categoryModel.getCategoryId());
    	}
    	
    	long update1 = 0;
		if (!categoryIds.isEmpty()) {
			updateParams.put("license", 1);
    		updateParams.put("categoryIds", categoryIds);
    		update1 = this.getTemplate().getSqlSessionTemplate().update("beanmapper.CategoryModelMapper.batchPrintYellowTicket", updateParams);
		}
		
		return ServiceResponse.buildSuccess("成功设置打印黄色小票类别数量：" + (update0 + update1) + "条");
    }
    
     public ServiceResponse sychCategory(ServiceSession session,JSONObject jsonParam) {
    	this.logger.info("同步【品类】Start----->>> param:"+jsonParam);
		// 参数转换
		CategoryModel category = JSON.toJavaObject(jsonParam, CategoryModel.class);
		if(category==null){
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY,"传入参数格式不对");
		}
		// 查询条件
		try {
			Criteria criteria = Criteria.where("entId").is(jsonParam.getLong("entId")).and("erpCode").is(jsonParam.getString("erpCode")).and("status").is(1);
			Query query = new Query(criteria);
			query.limit(Integer.MAX_VALUE);
			List<CategoryModel> newCategoryList = this.onFind(session, this.getTemplate(), query, CategoryModel.class, this.getCollectionName());
			List<Long> newCategoryIdList = newCategoryList.stream().map(s->s.getCategoryId()).collect(Collectors.toList());
			List<CategoryModel> oldCategoryList = categoryRedisServiceImpl.queryCategory(session, jsonParam);
			List<Long> oldCategoryIdList = oldCategoryList.stream().map(s->s.getCategoryId()).collect(Collectors.toList());
			oldCategoryIdList.removeAll(newCategoryIdList);
			if(oldCategoryIdList!=null&&oldCategoryIdList.size()>0){
				categoryRedisServiceImpl.deleteCategory(session, newCategoryIdList);
			}
			JSONArray array = new JSONArray();
			for(CategoryModel categoryModel : newCategoryList){
				JSONObject json = new JSONObject();
				json.put("categoryId", categoryModel.getCategoryId());
				json.put("entId", categoryModel.getEntId());
				json.put("erpCode", categoryModel.getErpCode());
				json.put("categoryCode", categoryModel.getCategoryCode());
				json.put("parentCode", categoryModel.getParentCode());
				json.put("parentId", categoryModel.getParentId());
				array.add(json);
			}
			categoryRedisServiceImpl.insertCategory(session, array);
			return ServiceResponse.buildSuccess("同步成功");
			// 在这里加入业务处理
		} catch (Exception ex) {
			logger.error(String.format("signInNoEnt->%1$s -> %2$s","返回登录异常",ex.getMessage()));
			return ServiceResponse.buildFailure(session,ex.getMessage());
		}
	 }
     
	 //HashMap构建树--查类别树勾选状态
	 public ServiceResponse getSonTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		 List<CategoryTreeBean> allCategorys = this.getTemplate().select(new Query(), CategoryTreeBean.class, "category");
		 // 权限过滤allCategorys
		 
		 Long categoryId = paramsObject.getLong("categoryId");
		 List<CategoryTreeBean> sonCategorys = getTreeByNodes(allCategorys,categoryId);
		 return ServiceResponse.buildSuccess(sonCategorys);
	 }
	 
	 //查询某个节点的儿子节点以及状态（0:不选，1：全选，2：半选）
	public List<CategoryTreeBean> getTreeByNodes(List<CategoryTreeBean> allCategorys, Long searchParentId) {
			
//	        Map<Object,List<CategoryTreeBean>> nodeMap = new HashMap<Object, List<CategoryTreeBean>>();
//	        for(CategoryTreeBean node : allCategorys){
//	            Object parentId = node.getParentId();
//	            List<CategoryTreeBean> childrenNodes ;
//	            if(nodeMap.containsKey(parentId)){
//	                childrenNodes = nodeMap.get(parentId);
//	            }else{
//	                childrenNodes = new ArrayList<CategoryTreeBean>();
//	                nodeMap.put(parentId, childrenNodes);
//	            }
//	            childrenNodes.add(node);
//	        }
	        
	        //可以递归查询子儿子节点，或者直接查询数据库
	        //1.查询数据库找到儿子节点
//	        Criteria criteria = Criteria.where("parentId").is(searchParentId);
//	        Query query = new Query(criteria);
//	        List<CategoryTreeBean> sons =  this.getTemplate().select(query, CategoryTreeBean.class, "category");
	        
	        // 借助HashMap整理allList
	        Map<Long, List<CategoryTreeBean>> nodeMap = getPidModel(allCategorys);
	        List<CategoryTreeBean> sons = nodeMap.get(searchParentId);
	        if(sons == null ){
	        	sons = new ArrayList<>();
	        }
	        //2.递归找到儿子节点
	        //List<CategoryTreeBean> sons = getTreeNodeByNodeMap(nodeMap,searchParentId,null);
	        
	        for (CategoryTreeBean oneSon : sons) {
	        	List<Long> leafIds = new ArrayList<Long>();
	        	getTreeNodeByNodeMap(nodeMap,oneSon.getCategoryId(),leafIds);
	        	List<CategoryTreeBean> leafNodeList = allCategorys.stream().filter(x -> leafIds.contains(x.getCategoryId())).collect(Collectors.toList());
				List<Short> checked = leafNodeList.stream().filter(x -> Short.valueOf("1").equals(x.getLicense()==null?0:x.getLicense())).map(CategoryTreeBean::getLicense).collect(Collectors.toList());
				List<Short> noChecked = leafNodeList.stream().filter(x -> Short.valueOf("0").equals(x.getLicense()==null?0:x.getLicense())).map(CategoryTreeBean::getLicense).collect(Collectors.toList());
				if(leafNodeList.size() == checked.size()){
					System.out.println(oneSon.getCategoryName()+" ----- > 全选"); 
					oneSon.setTreeStatus("1");
				}else if(leafNodeList.size() == noChecked.size()){
					System.out.println(oneSon.getCategoryName()+" ----- > 全不选");
					oneSon.setTreeStatus("0");
				}else{
					System.out.println(oneSon.getCategoryName()+" ----- > 半选");
					oneSon.setTreeStatus("2");
				}
	        }
	        return sons;
	}
	 
	 //递归子节点
	public List<CategoryTreeBean> getTreeNodeByNodeMap(Map<Long, List<CategoryTreeBean>> nodeMap, Long parentId, List<Long> leafIds) {
		List<CategoryTreeBean> childrens = nodeMap.get(parentId);
		if (childrens == null) {
			if (leafIds != null) leafIds.add(parentId);
			return null;
		}
		for (CategoryTreeBean node : childrens) {
			node.setChildren(getTreeNodeByNodeMap(nodeMap, node.getCategoryId(), leafIds));
		}
		return childrens;
	}
	
	// 异步加载类别树--带勾选状态
	public ServiceResponse getCategoryTopTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		List<CategoryTreeBean> categoryTree = categoryTopTreeForCompany1(session, paramsObject);
		paramsObject.clear();
		paramsObject.put("category", categoryTree);
		return ServiceResponse.buildSuccess(paramsObject);
	}
			
	//增加经营公司的树（只返回顶层数据）异步加载
	public List<CategoryTreeBean> categoryTopTreeForCompany1(ServiceSession session, JSONObject paramsObject){
		this.getTemplate().onSetContext(session);
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("entId").is(entId).and("status").is("1");
		Query query = new Query(criteria);
		//查询经营公司
		List<BusinessCompanyModel> allBusinessCompany = this.getTemplate().select(query, BusinessCompanyModel.class, "businesscompany");
		//保存虚拟树的List
		List<CategoryTreeBean> companyCategoryTrees = new ArrayList<CategoryTreeBean>();
		for (BusinessCompanyModel businessCompanyModel : allBusinessCompany) {
			paramsObject.put("erpCode", businessCompanyModel.getErpCode());
			List<CategoryTreeBean> categoryTrees = categoryTopTree1(session,paramsObject);//获取某个经营公司的顶层树
			
			//虚拟构造树
			CategoryTreeBean companyCategoryTreeBean = new CategoryTreeBean();
			companyCategoryTreeBean.setCategoryName(businessCompanyModel.getErpName());
			companyCategoryTreeBean.setCategoryCode(businessCompanyModel.getErpCode());
			companyCategoryTreeBean.setChildren(categoryTrees);
			
			// 类别状态勾选状态返回
			List<String> checked = categoryTrees.stream().filter(x -> ("1").equals(x.getTreeStatus()==null?"0":x.getTreeStatus())).map(CategoryTreeBean::getTreeStatus).collect(Collectors.toList());
			List<String> noChecked = categoryTrees.stream().filter(x -> ("0").equals(x.getTreeStatus()==null?"0":x.getTreeStatus())).map(CategoryTreeBean::getTreeStatus).collect(Collectors.toList());
			if(categoryTrees.size() == checked.size()){
				System.out.println(companyCategoryTreeBean.getCategoryName()+" ----- > 全选"); 
				companyCategoryTreeBean.setTreeStatus("1");
			}else if(categoryTrees.size() == noChecked.size()){
				System.out.println(companyCategoryTreeBean.getCategoryName()+" ----- > 全不选");
				companyCategoryTreeBean.setTreeStatus("0");
			}else{
				System.out.println(companyCategoryTreeBean.getCategoryName()+" ----- > 半选");
				companyCategoryTreeBean.setTreeStatus("2");
			}
			companyCategoryTrees.add(companyCategoryTreeBean);
		}
		return companyCategoryTrees;
	}
		
	//返回树的顶层数据（只返回顶层数据）异步加载
	public List<CategoryTreeBean> categoryTopTree1(ServiceSession session, JSONObject paramsObject){
		Long entId = session.getEnt_id();
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("entId").is(entId);
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("categoryId,categoryCode,categoryName,parentId,parentCode,level,leafFlag,erpCode,license");
		List<CategoryTreeBean> allList  = new ArrayList<>();
		
		try {
			allList = this.getTemplate().select(query, CategoryTreeBean.class, "category_view");
		}catch (Exception e){
			logger.error(e.getMessage());
			allList = this.getTemplate().select(query, CategoryTreeBean.class, "category");
		}
		
		// 把查到的allList  转成  Map<Long, List<CategoryTreeBean>>形式
		Map<Long, List<CategoryTreeBean>> pidCategoryTree = getPidModel(allList);
		long a = 0;   // 查到第一级
		List<CategoryTreeBean> topCategorys = pidCategoryTree.get(a);
		if(topCategorys == null ){ // 经营公司数据不对应
			topCategorys = new ArrayList<>();
        }
		for (CategoryTreeBean category : topCategorys) {
			Long parentId = category.getCategoryId();
			List<CategoryTreeBean> sonCategorys = getTreeByNodes(allList,parentId);
			
			List<String> checked = sonCategorys.stream().filter(x -> ("1").equals(x.getTreeStatus()==null?"0":x.getTreeStatus())).map(CategoryTreeBean::getTreeStatus).collect(Collectors.toList());
			List<String> noChecked = sonCategorys.stream().filter(x -> ("0").equals(x.getTreeStatus()==null?"0":x.getTreeStatus())).map(CategoryTreeBean::getTreeStatus).collect(Collectors.toList());
			if(sonCategorys.size() == checked.size()){
//				System.out.println(category.getCategoryName()+" ----- > 全选"); 
				category.setTreeStatus("1");
			}else if(sonCategorys.size() == noChecked.size()){
//				System.out.println(category.getCategoryName()+" ----- > 全不选");
				category.setTreeStatus("0");
			}else{
//				System.out.println(category.getCategoryName()+" ----- > 半选");
				category.setTreeStatus("2");
			}
		}
//			做出剪枝两边的树进行对比剪枝
//		List<CategoryTreeBean> allCategorys = getAllCategory(session, paramsObject);
//		List<CategoryTreeBean> compareCategory = compareCategory(topCategorys, allCategorys);
		return topCategorys;
	}
	
	// 根据选中类别查最末级类别--黄色小票设置使用
	public ServiceResponse getNodeByCategory(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		// 1.传参校验
    	ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject,"erpCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
    	if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", this.getCollectionName()));
		}
    	JSONArray categoryList = paramsObject.getJSONArray(this.getCollectionName()); // ---页面传来的勾选上的节点数据
    	if(categoryList == null) categoryList = new JSONArray();
		
		// 2.查询所选经营公司
		List<CategoryTreeBean> allList = new ArrayList<>();// 存放查到的全部经营公司数据
		Long entId = session.getEnt_id();
		String erpCode = paramsObject.getString("erpCode");   // 参考的经营公司
		
		Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("categoryId,parentId,erpCode,license,categoryCode,categoryName");
		List<CategoryTreeBean> getallList = this.getTemplate().select(query, CategoryTreeBean.class, "category");
		allList.addAll(getallList);
		
		// 借助HashMap整理allList
		Map<Long, List<CategoryTreeBean>> pidCategoryTree = getPidModel(allList);
		
		// 3.遍历查所选节点的全部叶子节点
    	List<CategoryTreeBean> levelLeaf = new ArrayList<>();      // 存需要进行递归的节点
    	
    	int size = categoryList.size();   // 页面传来的勾选节点数
		for (int m = 0; m < size; m++) {
			CategoryTreeBean category = categoryList.getObject(m, CategoryTreeBean.class);
			String ErpCode = category.getErpCode(); // 这是勾选节点的经营公司
			// 这时候勾选的是经营公司节点
			if(StringUtils.isEmpty(category.getCategoryId())){
				// 子节点从pidCategoryTree中遍历找
				long a = 0;
				List<CategoryTreeBean> listA = pidCategoryTree.get(a);
				if(listA == null ){ // 传来的经营公司数据库找不到
					listA = new ArrayList<>();
		        }
				System.out.println(listA.size()+"-----------------------------pidCategoryTree---------------");
				for (CategoryTreeBean categoryModel : listA) {
					if (ErpCode.equals(categoryModel.getErpCode())) {
						levelLeaf.add(categoryModel);
					}
				}
				System.out.println(levelLeaf.size()+"-----------------------------levelLeaf---------------");
			}else{
				levelLeaf.add(category);
			}
		}
		
		List<CategoryTreeBean> updateLeafList = new ArrayList<CategoryTreeBean>();  // 存递归找到的需要修改的叶子节点
		if (!levelLeaf.isEmpty()) {
			updateLeafList = getLeafList1(levelLeaf, pidCategoryTree,updateLeafList); 
		}
		
		JSONObject result = new JSONObject();
		result.put("category", updateLeafList);
//		result.put("total_results", updateLeafList.size());
		return ServiceResponse.buildSuccess(result);
	}
	
	// 查类别最末级--黄色小票设置使用
	public ServiceResponse searchByLeafFlag(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject,"erpCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		if (!paramsObject.containsKey("leafFlag")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "leafFlag"));
		}
		
		// 带类别查询全部叶子节点
		List<Long> categoryIdList = null;
		if(paramsObject.containsKey("categoryId") && !StringUtils.isEmpty(paramsObject.get("categoryId"))){
			JSONArray categoryList = paramsObject.getJSONArray("categoryId");
			if(categoryList == null) categoryList = new JSONArray();
			
			List<JSONObject> categoryIdList1 = new ArrayList<>();
			int size = categoryList.size();
			JSONObject param = null;
			for (int i = 0; i < size; i++) {
				param = new JSONObject();
				String categoryId = categoryList.getString(i);
				param.put("categoryId", categoryId);
				categoryIdList1.add(param);
			}
			
			JSONObject search = new JSONObject();
			search.put("erpCode", paramsObject.getString("erpCode"));
			search.put("category", categoryIdList1);
			ServiceResponse categoryArray = getNodeByCategory(session, search);
			if (!ResponseCode.SUCCESS.equals(categoryArray.getReturncode())) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询类别失败" + categoryArray.getData());
			}
			JSONObject categoryData = (JSONObject) categoryArray.getData();
			JSONArray categoryDataList = categoryData.getJSONArray(this.getCollectionName());
			
			if (!StringUtils.isEmpty(categoryDataList)) {
				int temp = categoryDataList.size();
				categoryIdList = new ArrayList<>();
				for (int j = 0; j < temp; j++) {
					JSONObject category = categoryDataList.getJSONObject(j);
					Long categoryId = category.getLong("categoryId");
					categoryIdList.add(categoryId);
				}
			}
		}
		
		if (categoryIdList != null && !categoryIdList.isEmpty()) {
			paramsObject.put("categoryId", categoryIdList);
		}
		paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "categoryId,erpCode,categoryCode,categoryName,leafFlag,license");
		ServiceResponse category = this.onQuery(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(category.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询类别失败" + category.getData());
		}
		return category;
	}
	
	/**
             *  是否打印黄色小票--提供给POS总部      license--1：是  0：否
     * @param session
     * @param paramsObject
     * @return
     */
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse batchPrintYellowTicket(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "license");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", this.getCollectionName()));
		}
		
		Short license = paramsObject.getShort("license");  // 是否打印黄色小票
		// 统一更新时间：保持多个表的修改时间是一致性
//	    String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    JSONArray categoryList = paramsObject.getJSONArray(this.getCollectionName());
	    if(categoryList == null) categoryList = new JSONArray();
	    
	    List<Long> categoryIds = new ArrayList<>();
	    int size = categoryList.size(); 
	    for (int i = 0; i < size; i++) {
	    	JSONObject category = categoryList.getJSONObject(i);
	    	Long categoryId = category.getLong("categoryId");
	    	categoryIds.add(categoryId);
	    }
	  
	    long update = 0;
		if (!categoryList.isEmpty()) {
			JSONObject updateParams = new JSONObject();
			updateParams.put("license", license);
    		updateParams.put("categoryIds", categoryIds);
			update = this.getTemplate().getSqlSessionTemplate().update("beanmapper.CategoryModelMapper.batchPrintYellowTicket", updateParams);
		}
		
	    return ServiceResponse.buildSuccess("成功设置打印黄色小票类别数量：" +update + "条");	
	}
	
	/**
	 *  查询某个部类下面的全部叶子节点
     *  传参，如： {"erpCode": "002","categoryCode":"001"}
     * @param session
     * @param paramsObject
     * @return
     */
	public ServiceResponse getBuLeiAllLeaf(ServiceSession session, JSONObject paramsObject) {
		
		// 传参校验
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject,"erpCode","categoryCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		String erpCode = paramsObject.getString("erpCode");
		String categoryCode = paramsObject.getString("categoryCode");
		
		// 找出当前部类
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("categoryCode").is(categoryCode);
    	Query query = new Query(criteria);
    	Field fields = query.fields();
		fields.include("categoryId,erpCode,categoryCode,categoryName,manageCategoryCode,leafFlag");
    	CategoryModel category = this.getTemplate().selectOne(query, CategoryModel.class,"category");
    	if (category == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "该部类不存在！");
		}
    	
    	List<CategoryModel> categoryList = new ArrayList<>();
    	JSONObject result = new JSONObject(); // 存最终查询结果
		if (category.getLeafFlag()) {// 如果当前部类是叶子节点就不用向下找
			categoryList.add(category);
		} else {
			criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("manageCategoryCode").is(categoryCode).and("leafFlag").is(1);
	    	query = new Query(criteria);
	    	fields = query.fields();
			fields.include("categoryId,parentId,erpCode,categoryCode,categoryName,manageCategoryCode,leafFlag");
			categoryList = this.getTemplate().select(query, CategoryModel.class,"category");
		}
		if (categoryList != null && !categoryList.isEmpty()) {
			result.put(this.getCollectionName(), categoryList);
		}
		return ServiceResponse.buildSuccess(result);
	}
	
	@Autowired
	private GoodsServiceImpl goodsService;
	
	/**
	 * 挑选--库存策略例外商品
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse searchShopSheetTypeGoods(ServiceSession session, JSONObject paramsObject) {

		// 传参校验
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "categoryCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		// 根据部类找出全部叶子节点
//        JSONObject params = new JSONObject();
//        params.put("erpCode", paramsObject.getString("erpCode"));
//        params.put("categoryCode", paramsObject.getString("categoryCode"));
        ServiceResponse buLeiAllLeaf = getBuLeiAllLeaf(session, paramsObject);
        if (!ResponseCode.SUCCESS.equals(buLeiAllLeaf.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询部类叶子节点失败" + buLeiAllLeaf.getData());
		}
		JSONObject categorysData = (JSONObject) buLeiAllLeaf.getData();
		JSONArray categoryList = categorysData.getJSONArray(this.getCollectionName());
        
        List<String> categoryCodes = new ArrayList<>(); // 存需要查的商品类别
        int size =  categoryList.size();
        for (int i = 0; i < size; i++) {
	    	JSONObject category = categoryList.getJSONObject(i);
	    	String categoryCode1 = category.getString("categoryCode");
	    	categoryCodes.add(categoryCode1);
	    }
        
        if(!categoryCodes.isEmpty()){
        	paramsObject.put("categoryCode", categoryCodes);
        }
        // 商品门店库存属性  0-单店/0-全店
//		Integer shopSheetType = paramsObject.getInteger("shopSheetType");
//		if (shopSheetType == 1) {
//			paramsObject.put("shopSheetType", 2);
//		} else {
//			paramsObject.put("shopSheetType", 1);
//		}
        paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS,"sgid,erpCode,categoryCode,goodsCode,goodsName,barNo,shopSheetType");
        ServiceResponse searchGoods = goodsService.onQuery(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(searchGoods.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询库存策略例外商品失败" + searchGoods.getData());
		}
		JSONObject goodsData = (JSONObject) searchGoods.getData();
		return ServiceResponse.buildSuccess(goodsData);
	}
	
	/**
	 * 查询--选中管理部类+例外商品
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse getCategoryAndGoods(ServiceSession session, JSONObject paramsObject) {

		// 1.传参校验
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "categoryCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		String erpCode = paramsObject.getString("erpCode");
		String categoryCode = paramsObject.getString("categoryCode");
		
		// 2.找出选择的部类信息
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("categoryCode").is(categoryCode);
    	Query query = new Query(criteria);
    	Field fields = query.fields();
		fields.include("categoryId,erpCode,categoryCode,categoryName,shopSheetType");
    	CategoryModel categoryModel = this.getTemplate().selectOne(query, CategoryModel.class,"category");
		
		JSONObject result = new JSONObject(); // 存最终结果
		if (categoryModel != null) {
			result.put(this.getCollectionName(), categoryModel);
		}
		
		// 3.根据选择的部类找出全部叶子节点
        JSONObject params = new JSONObject();
        params.put("erpCode", paramsObject.getString("erpCode"));
        params.put("categoryCode", paramsObject.getString("categoryCode"));
        ServiceResponse buLeiAllLeaf = getBuLeiAllLeaf(session, params);
        if (!ResponseCode.SUCCESS.equals(buLeiAllLeaf.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询部类叶子节点失败" + buLeiAllLeaf.getData());
		}
		JSONObject categorysData = (JSONObject) buLeiAllLeaf.getData();
		JSONArray categoryList = categorysData.getJSONArray(this.getCollectionName());
        
        List<String> categoryCodes = new ArrayList<>(); // 存需要查的商品类别
        int size =  categoryList.size();
        for (int i = 0; i < size; i++) {
	    	JSONObject category = categoryList.getJSONObject(i);
	    	String categoryCode1 = category.getString("categoryCode");
	    	categoryCodes.add(categoryCode1);
	    }
        
        if(!categoryCodes.isEmpty()){
        	paramsObject.put("categoryCode", categoryCodes);
        }
        // 商品门店库存属性  0-单店/1-全店
		int shopSheetType = categoryModel.getShopSheetType() == null ? 0 : categoryModel.getShopSheetType();
		if (shopSheetType == 0) {
			paramsObject.put("shopSheetType", 1);
		} else {
			paramsObject.put("shopSheetType", 0);
		}
		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)) {
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_PAGENO, 1);
		}
		if (!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, 1000);
		}
        paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS,"sgid,erpCode,categoryCode,goodsCode,goodsName,barNo,shopSheetType");
        ServiceResponse searchGoods = goodsService.onQuery(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(searchGoods.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询库存策略例外商品失败" + searchGoods.getData());
		}
		JSONObject goodsData = (JSONObject) searchGoods.getData();
		JSONArray goodsList = goodsData.getJSONArray(goodsService.getCollectionName());
		if (goodsList != null) {
			result.put(goodsService.getCollectionName(), goodsList);
			result.put("goods_total", goodsData.getLong("total_results"));
		}
		return ServiceResponse.buildSuccess(result);
	}
	
	/**
	 * 库存策略例外商品--保存
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse saveShopSheetTypeGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		// 传参校验
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "categoryId",
				"categoryCode", "shopSheetType");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		// 找出需要更新的部类最末级
		JSONObject params = new JSONObject();
		params.put("erpCode", paramsObject.getString("erpCode"));
		params.put("categoryCode", paramsObject.getString("categoryCode"));
		ServiceResponse buLeiAllLeaf = getBuLeiAllLeaf(session, params); // 查当前部类下面的全部叶子节点
		if (!ResponseCode.SUCCESS.equals(buLeiAllLeaf.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询部类叶子节点失败" + buLeiAllLeaf.getData());
		}
		JSONObject categorysData = (JSONObject) buLeiAllLeaf.getData();
		JSONArray categoryList = categorysData.getJSONArray(this.getCollectionName());
		
		List<Long> categoryIds = new ArrayList<>(); // 存需要操作的类别id
		int size =  categoryList.size();
		for (int i = 0; i < size; i++) {
			JSONObject category = categoryList.getJSONObject(i);
			Long categoryId = category.getLong("categoryId");
			categoryIds.add(categoryId);
		}
		Long buleiId = paramsObject.getLong("categoryId");
		if(!categoryIds.contains(buleiId)) { 
			categoryIds.add(buleiId);
		}
		
		// 商品门店库存属性  0-单店/1-全店
		Integer shopSheetType = paramsObject.getInteger("shopSheetType");
		// 更新类别
		params = new JSONObject();
		params.put("shopSheetType", shopSheetType);
		params.put("categoryIds", categoryIds);
		long updateCategory = this.getTemplate().getSqlSessionTemplate().update("beanmapper.CategoryModelMapper.batchUpdateShopSheetType", params);
		
		JSONArray goodsList = paramsObject.getJSONArray("sgids"); // 页面传来的商品id
	    if(goodsList == null) goodsList = new JSONArray();
	    List<Long> ymsgids = new ArrayList<>(); // 页面传来的商品
	    for (int i = 0; i < goodsList.size(); i++) {
//			JSONObject join = goodsList.getJSONObject(i);
//			Long sgid = join.getLong("sgid");
//	    	ymsgids.add(sgid);
			Object join1 = goodsList.get(i);
			ymsgids.add(Long.parseLong(join1.toString()));
		}
	   
		// 找出当前类别下全部商品
		params = new JSONObject();
		params.put("erpCode", paramsObject.getString("erpCode"));
		if(!categoryIds.isEmpty()){
			params.put("categoryId", categoryIds);
		}
		params.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<GoodsModel> goodsModelList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.getShopSheetTypeGoodsList", params);
		// 数据库对象List<Map>转换List<Model>对象
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(goodsModelList));
		List<GoodsModel> list = JSONArray.parseArray(array.toJSONString(), GoodsModel.class);
		
		List<Long> sgids0 = new ArrayList<>(); // 存数据库查到"0"的商品id
		List<Long> sgids1 = new ArrayList<>(); // 存数据库查到"1"的商品id
		List<Long> sgids = new ArrayList<>();  // 存数据库查到的全部商品id
		if (list != null && !list.isEmpty()) {
			sgids = list.stream().map(GoodsModel::getSgid).collect(Collectors.toList());
			List<GoodsModel> goodsModelList0 = list.stream()
					.filter(a -> ("0").equals(String.valueOf(a.getShopSheetType()))).collect(Collectors.toList());
			if (goodsModelList0 != null && !goodsModelList0.isEmpty()) {
				sgids0 = goodsModelList0.stream().map(GoodsModel::getSgid).collect(Collectors.toList());
			}
			List<GoodsModel> goodsModelList1 = list.stream()
					.filter(a -> ("1").equals(String.valueOf(a.getShopSheetType()))).collect(Collectors.toList());
			if (goodsModelList1 != null && !goodsModelList1.isEmpty()) {
				sgids1 = goodsModelList1.stream().map(GoodsModel::getSgid).collect(Collectors.toList());
			}
		}
		
		// 更新商品
        long update1 = 0;
        long update0 = 0;
		if (shopSheetType == 1) {
			// 处理要更新为"1"的商品
			if (sgids.size() > 0) {
				if (ymsgids.size() > 0) {
					sgids.removeAll(ymsgids); // 全部 - 页面的
				}
				if (sgids1.size() > 0) {
					sgids.removeAll(sgids1); // 全部 - 已经有的
				}
				if(!sgids.isEmpty()) {
					params = new JSONObject();
					params.put("sgids", sgids);
					params.put("shopSheetType", 1);
					update1 = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.batchShopSheetTypeGoods", params);
				}
			}
			// 处理要更新为"0"的商品
			if (ymsgids.size() > 0) {
				if (sgids0.size() > 0) {
					ymsgids.removeAll(sgids0); // 页面的 - 已经有的
				}
				if (!ymsgids.isEmpty()) {
					params = new JSONObject();
					params.put("sgids", ymsgids);
					params.put("shopSheetType", 0);
					update0 = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.batchShopSheetTypeGoods", params);
				}
			}
		} else {
			// 处理要更新为"0"的商品
			if (sgids.size() > 0) {
				if (ymsgids.size() > 0) {
					sgids.removeAll(ymsgids); // 全部 - 页面的
				}
				if (sgids0.size() > 0) {
					sgids.removeAll(sgids0); // 全部 - 已经有的
				}
				if(!sgids.isEmpty()) {
					params = new JSONObject();
					params.put("sgids", sgids);
					params.put("shopSheetType", 0);
					update0 = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.batchShopSheetTypeGoods", params);
				}
			}
			// 处理要更新为"1"的商品
			if (ymsgids.size() > 0) {
				if (sgids1.size() > 0) {
					ymsgids.removeAll(sgids1); // 页面的 - 已经有的
				}
				if (!ymsgids.isEmpty()) {
					params = new JSONObject();
					params.put("sgids", ymsgids);
					params.put("shopSheetType", 1);
					update1 = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.batchShopSheetTypeGoods", params);
				}
			}
        }
		return ServiceResponse.buildSuccess("保存成功！ category: " + updateCategory + " goods: " + (update0+update1));
	}
	
	/**
	 * 导出--库存策略
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse exportCategoryAndGoods(ServiceSession session, JSONObject paramsObject) {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 0);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
		
		List<JSONObject> list = new ArrayList<>();
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		
		// 管理部类查询
        List categoryList = template.getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.shopSheetTypeCategory", paramsObject);
        if (categoryList != null && !categoryList.isEmpty()) {
        	// 数据库对象List<Map>转换List<Model>对象
        	JSONArray array= JSONArray.parseArray(JSON.toJSONString(categoryList));
        	List<JSONObject> listCategory = JSONArray.parseArray(array.toJSONString(), JSONObject.class);
        	list.addAll(listCategory);
        }
		
        // 例外商品查询
        List goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.shopSheetTypeGoods", paramsObject);
        if (goodsList != null && !goodsList.isEmpty()) {
        	// 数据库对象List<Map>转换List<Model>对象
        	JSONArray array1 = JSONArray.parseArray(JSON.toJSONString(goodsList));
        	List<JSONObject> listGoods = JSONArray.parseArray(array1.toJSONString(), JSONObject.class);
        	list.addAll(listGoods);
        }
        
        // 数据排序整理
        List<JSONObject> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
        	List<JSONObject> list1 = list.stream().sorted((j1,j2) ->j1.getString("erpCode").compareTo(j2.getString("erpCode"))).collect(Collectors.toList());
        	list2 = list1.stream().sorted((j1,j2) ->j1.getString("categoryCode").compareTo(j2.getString("categoryCode"))).collect(Collectors.toList());
        }
        
        int temp = list2.size();
		for (int i = 0; i < temp; i++) {
			JSONObject json = list2.get(i);
			int shopSheetType = json.get("shopSheetType") == null ? 0 : json.getInteger("shopSheetType");
			if (shopSheetType == 0) {
				json.put("shopSheetType", "单店");
			} else if (shopSheetType == 1) {
				json.put("shopSheetType", "全店");
			}
		}
        
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), list2);
		result.put("total_results", list2.size());
		return ServiceResponse.buildSuccess(result);
	}
	
	/**
	   *  查某个经营公司全部管理部类--带分页
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse getManageCategory(ServiceSession session, JSONObject paramsObject) {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 0);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
		
		// 查部类
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		// 查询管理部类总数
		long total_results = template.getSqlSessionTemplate().selectOne("beanmapper.CategoryModelMapper.countBLCategory", paramsObject);
		if (total_results == 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有找到管理部类，请到管理部类设置页面设置！");
		}
		// 查询部类
		List categoryList = template.getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.shopSheetTypeCategory", paramsObject);
		if (categoryList == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "管理部类查询失败！");
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), categoryList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
}
