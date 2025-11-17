package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.product.component.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.CategoryModel;
import com.efuture.omdmain.model.CategoryPropertyModel;
import com.efuture.omdmain.service.CategoryPropertyService;
import com.mongodb.DBObject;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

/**
 * Created by huangzhengwei on 2018/5/3.
 *
 * @Desciption:
 */
public class CategoryPropertyServiceImpl extends CommonServiceImpl<CategoryPropertyModel,CategoryPropertyServiceImpl> implements CategoryPropertyService {

    public CategoryPropertyServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

    @Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	/*@Override
	protected FMybatisTemplate getTemplate() {
		return this.getBean("StorageOperation", FMybatisTemplate.class);
	}*/
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	//根据工业分类的id查询
    @Override
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
        if (!paramsObject.containsKey("categoryId")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
        }
        return onQuery(session, paramsObject);
    }

	// 通过部类顶级节点，获取所有子类：level级别的子类
	public List<CategoryModel> getLevelCategoryList(ServiceSession session, String categoryId, String erpCode, Long entId) {
		//查询某个经营公司的所有分类
    	Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode);
    	Query query = new Query(criteria);
    	List<CategoryModel> allCategoryList = this.getTemplate().select(query, CategoryModel.class, "category");
		//查询某个部类的所有子类集合
		if (allCategoryList != null && !allCategoryList.isEmpty()) {
			List<CategoryModel> resultList = getSList(session, categoryId, allCategoryList);
			return resultList;
		}
		return null;
	}
	
	// 工业分类根据parentId递归出所有的子类（1.增加动态参数只是兼容以前的写法。2.递归调用此方法不用多次查询，取参数中分类集合）
	// public List<CategoryModel> getSList(ServiceSession session, String categoryId){
	public List<CategoryModel> getSList(ServiceSession session, String categoryId,List<CategoryModel> ... paramAllCategoryList) {
		// 1.查询所有的记录
		Query query = new Query();
		// 参数中没有传，直接查询数据库
		List<CategoryModel> allCategoryList = paramAllCategoryList.length > 0 ? paramAllCategoryList[0] : this.getTemplate().select(query, CategoryModel.class, "category");
		// 2.统计归类分组,将所有子品放入Key为ParentId的Map
		HashMap<String, List<CategoryModel>> parentIdMap = new HashMap<>();
		List<CategoryModel> rtList = new ArrayList<>();
		for (CategoryModel categoryModel : allCategoryList) {
			if (categoryId != null && categoryId.equals(String.valueOf(categoryModel.getCategoryId()))) {
				rtList.add(categoryModel);
			}
			if (categoryModel.getParentId() != null) {
				List<CategoryModel> parentIdList = parentIdMap.containsKey(categoryModel.getParentId().toString()) ? parentIdMap.get(categoryModel.getParentId().toString()) : new ArrayList<>();
				parentIdList.add(categoryModel);
				parentIdMap.put(categoryModel.getParentId().toString(), parentIdList);
			}
		}
		recursiveGetList(parentIdMap, categoryId, rtList);
		return rtList;
	}

    
    //递归
    public void recursiveGetList(HashMap<String, List<CategoryModel>> hashMap, String categoryId, List<CategoryModel> rtList){
        if (!hashMap.containsKey(categoryId)){
            return;
        }
        for (CategoryModel categoryModel: hashMap.get(categoryId)) {
            rtList.add(categoryModel);
            recursiveGetList(hashMap, categoryModel.getCategoryId().toString(), rtList);
        }
    }

    //批量插入数据,多个id
    @Override
    public ServiceResponse bulkInsert(ServiceSession session, JSONObject paramsObject) {
        JSONObject paramMap = new JSONObject();
        List<CategoryPropertyModel> dataList = new ArrayList<CategoryPropertyModel>();
        List<JSONObject> paramList = (List<JSONObject>) paramsObject.get("data");
        List<CategoryModel> resultList = new ArrayList<CategoryModel>();

        //递归查找出所有的子类
        resultList.addAll(getSList(session, (String) paramList.get(0).getString("categoryId")));
        //组装数据
        Date now = new Date();
//         这里应该反着循环
//        for (CategoryModel categoryModel: resultList) {//遍历工业分类
//            for (JSONObject param: paramList) {//遍历页面传过来的属性值组
		for (JSONObject param: paramList) {
			String propertyCode =  Long.toHexString(UniqueID.getUniqueID(true));
			for (CategoryModel categoryModel: resultList) {
            	CategoryPropertyModel categoryPropertyModel = JSONObject.toJavaObject(param, CategoryPropertyModel.class);
				categoryPropertyModel.setPropertyCode(propertyCode);
				categoryPropertyModel.setCategoryId(categoryModel.getCategoryId());
//            	categoryPropertyModel.setCategoryCode(categoryModel.getCategoryCode());
            	categoryPropertyModel.setCreateDate(now);
				categoryPropertyModel.setUpdateDate(now);
            	categoryPropertyModel.setCreator(Long.toString(session.getUser_id()));
//            	categoryPropertyModel.setStatus(1);//1:启用 0:禁用
            	dataList.add(categoryPropertyModel);
            }
        }
        paramMap.put(this.getCollectionName(), dataList);
        String dataInsert = JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd HH:mm:ss");
		JSONObject params = JSON.parseObject(dataInsert);
        return this.onInsert(session, params);
		
    }
    
    public List<CategoryModel> getAllCategory(ServiceSession session,List<CategoryModel> params,List<CategoryModel> result){
    	
    	if(params==null||params.size()<=0){
    		result.addAll(params);
    		return result;
    	}
    	List<CategoryModel> newparams  = new ArrayList<CategoryModel>();
    	for(CategoryModel model :params){
    		if(model.getLeafFlag()){
    			break;
    		}
    		Criteria criteria = Criteria.where("parentId").is(model.getCategoryId()).and(getEntname()).is(session.getEnt_id());
			Query query = new Query(criteria);
			List<CategoryModel> list = this.onFind(session, this.getTemplate(), query,CategoryModel.class,categoryService.getCollectionName());
			
			if(list!=null){
				if(list.size()>0){
					result.addAll(list);
					newparams.addAll(list);
				}
			}
    	}
    	return getAllCategory(session,newparams,result);
    }
    
    public ServiceResponse bulkAdd(ServiceSession session, JSONObject paramsObject) {
    	if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
    	
		List<JSONObject> plist = new ArrayList<JSONObject>();
		List<CategoryModel> allList = new ArrayList<CategoryModel>();
		if (paramsObject.containsKey(this.getCollectionName())) {
			Object dataObject = paramsObject.get(this.getCollectionName());
			if (dataObject != null && dataObject instanceof List) {
				JSONArray paramsArray = paramsObject.getJSONArray(this.getCollectionName());
				List<CategoryModel> caList = new ArrayList<CategoryModel>();
				
				for (int i = 0; i < paramsArray.size(); i++) {
					JSONObject params = paramsArray.getJSONObject(i);
					if (!params.containsKey(this.getEntname())) {
						params.put(this.getEntname(), session.getEnt_id());
					}
					if(!params.containsKey(categoryService.getKeyfieldName())){
						return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,"缺少关键字段[{1}]",categoryService.getKeyfieldName());
					}
					Object categoryId = params.get(categoryService.getKeyfieldName());
					
					Criteria criteria = Criteria.where("categoryId").is(categoryId).and(getEntname()).is(session.getEnt_id());
					Query query = new Query(criteria);
					CategoryModel bean = this.onFindOne(session, this.getTemplate(), query,CategoryModel.class,categoryService.getCollectionName());
					if(bean==null){
						return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST," 数据不存在");
					}
					caList.add(bean);
					caList = getAllCategory(session,caList,allList);
					String propertyCode = Long.toHexString(UniqueID.getUniqueID(true));
					if(caList.size()>=1){
						for(CategoryModel model :caList){
							CategoryPropertyModel ca = new CategoryPropertyModel();
							ca.setCategoryId(model.getCategoryId());
							ca.setCategoryCode(model.getCategoryCode());
							ca.setCreateDate(new Date());
							ca.setUpdateDate(new Date());
							ca.setPropertyDesc(params.getString("propertyName"));
							ca.setPropertyCode(propertyCode);
							ca.setPropertyName(params.getString("propertyName"));
							ca.setRemark(params.getString("remark"));
							ca.setGeneratesFlag(params.getBoolean("generatesFlag")==null?true:false);
							ca.setStatus(params.getShort("status")==null?1:params.getShort("status"));
							plist.add(JSON.parseObject(JSON.toJSONStringWithDateFormat(ca, "yyyy-MM-dd")));
						}
					}
				}
				
			}
		}
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(), plist);
    	
    	return onInsert(session, paramsObject);
    }

   //批量更新
    @Override
    public ServiceResponse bulkUpdate(ServiceSession session, JSONObject paramsObject) {
        //先验证paramsObject contain categoryId status propertyCode
       if (!paramsObject.containsKey("categoryId") || !paramsObject.containsKey("status")){
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
        }
        //先查询出所有的子节点的id 编码
       
        List<Long> categoryIdList = new ArrayList<Long>();
        List<CategoryModel> resultList = new ArrayList<CategoryModel>();
        //递归查找出所有的子类
        resultList.addAll(getSList(session, paramsObject.getString("categoryId")));
        
        for (CategoryModel categoryModel: resultList) {
            categoryIdList.add(categoryModel.getCategoryId());
        }
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        paramsObject.put("categoryId",categoryIdList);
        if(categoryIdList.size() == 0){
			ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有需要修改的工业分类");
        }
        template.getSqlSessionTemplate().update("beanmapper.CategoryPropertyModelMapper.updateCategoryProperty", paramsObject);

        return ServiceResponse.buildSuccess("success");
    }


}
