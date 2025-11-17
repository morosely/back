package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.*;

import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.product.model.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ChannelInfoModel;
import com.efuture.omdmain.model.ShowCategoryTreeBean;
import com.efuture.omdmain.model.ShowCategoryModel;
import com.efuture.omdmain.service.ShowCategoryService;
import com.google.common.collect.ImmutableMap;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;

/**
 * 
 * @author Administrator
 * 展示分类
 */
public class ShowCategoryServiceImpl  extends JDBCCompomentServiceImpl<ShowCategoryModel> implements ShowCategoryService{

	public ShowCategoryServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
	
	@Autowired
	private ShopChannelCategoryGoodsRefServiceImpl shopChannelCategoryGoodsRefServiceImpl;

	//展示分类删除
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse onDelete(ServiceSession session, JSONObject params){
		//1.删除展示分类表
		super.onDelete(session, params);
		//2.删除关联表
		params.put(shopChannelCategoryGoodsRefServiceImpl.getCollectionName(),params.get(this.getCollectionName()));
		params.remove(this.getCollectionName());
		shopChannelCategoryGoodsRefServiceImpl.onDelete(session, params);
		return ServiceResponse.buildSuccess("success");
	}
    
	@Override
	public ServiceResponse search(ServiceSession session,JSONObject paramsObject) {
		Object showCategoryId = paramsObject.get("showCategoryId");
		//树形结构的当前节点返回信息中带父节点名称
		if(showCategoryId != null){
			ServiceResponse response = this.onQuery(session, paramsObject);
			List<RowMap> data = (List<RowMap>)((JSONObject) response.getData()).get(this.getCollectionName());
			RowMap showCategoryMap = null;
			if(!data.isEmpty()){
				showCategoryMap = data.get(0);
				Long parentId = (Long)showCategoryMap.get("parentId");
				paramsObject.clear();
				paramsObject.put("showCategoryId", parentId);
				ServiceResponse parentResponse = this.onQuery(session, paramsObject);
				List<RowMap> parentData = (List<RowMap>)((JSONObject) parentResponse.getData()).get(this.getCollectionName());
				if(!parentData.isEmpty()){
					String parentName = (String)parentData.get(0).get("showCategoryName");
					showCategoryMap.put("parentName", parentName);
				}else{
					showCategoryMap.put("parentName", "");
				}
			}
			paramsObject.clear();
			paramsObject.put("showcategory", showCategoryMap);
			return ServiceResponse.buildSuccess(paramsObject);
		}else{
			return this.onQuery(session, paramsObject);
		}
	}
	
	private Map<String,Object> onFieldMap(Map<String,Object> dataMap,Map<String,String> fieldMap) {
		Map<String,Object> responseMap = new LinkedHashMap<String,Object>();
		Set<String> keySet = fieldMap.keySet();
		for (String key:keySet) {
			if (dataMap.containsKey(key)) {
				responseMap.put(fieldMap.get(key), dataMap.get(key));
			}
		}
		return responseMap;
	}

	@Override
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject) {
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "save请求参数必须包含参数[{1}]", this.getCollectionName());
		}
		
		JSONArray dataList = paramsObject.getJSONArray(this.getCollectionName());
		JSONArray returnarray = new JSONArray();
		for (int i=0;i<dataList.size();i++) {
			JSONObject dataMap = dataList.getJSONObject(i);
			
			String dataFlag  = (String) dataMap.get("_flag");
			if ("I".equalsIgnoreCase(dataFlag)) {
				returnarray.add(this.saveInsert(session, dataMap));
			} else if ("U".equalsIgnoreCase(dataFlag)) {
				returnarray.add( this.saveUpdate(session, dataMap));
			} else if ("D".equalsIgnoreCase(dataFlag)) {
				Map<String,Object> dataRow = this.onFieldMap(dataMap,ImmutableMap.of("showCategoryId","showCategoryId"));
				dataMap.clear();
				dataMap.putAll(dataRow);
				returnarray.add( this.onDelete(session, dataMap));
			} else {
				returnarray.add(this.onQuery(session, dataMap));
			}
		}
		return ServiceResponse.buildSuccess(returnarray);
		
	}

	/*
	* @Description: 删除节点,节点状态为停用的才能删除
	* @param session
    * @param paramsObject
	* @return: com.product.model.ServiceResponse
	*/
	@Override
	public ServiceResponse categoryDelete(ServiceSession session, JSONObject paramsObject) {
		if(!paramsObject.containsKey("showCategory")){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
		}
		List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("showCategory");
		List<String> showCategoryIdList = new ArrayList();
		for (JSONObject tmpObject: paramsList
			 ) {
			if(!tmpObject.containsKey("showCategoryId") || tmpObject.get("showCategoryId") == null){
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
			}
			showCategoryIdList.add(String.valueOf(tmpObject.get("showCategoryId")));
		}
		JSONObject updateParams = new JSONObject();
		updateParams.put("status", 0);
		updateParams.put("showCategoryId", showCategoryIdList);
		updateParams = DefaultParametersUtils.transformParam(session, updateParams, false, false);
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.categoryDelete",updateParams);
		return ServiceResponse.buildSuccess("success");
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse saveUpdate(ServiceSession session, JSONObject paramsObject) {
		paramsObject.put("updateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//1.更难主表
		this.onUpdate(session, paramsObject);
		//2.更新关联表数据
		paramsObject.put("entId", session.getEnt_id());
    	paramsObject.put("table","shopchannelcategorygoodsref");
		paramsObject.put("setField","isValid");
		paramsObject.put("setFieldValue",paramsObject.getShort("status"));
		paramsObject.put("key","showCategoryId");
		paramsObject.put("values", Arrays.asList(paramsObject.get("showCategoryId")));
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		return ServiceResponse.buildSuccess("success");
	}

	public ServiceResponse saveInsert(ServiceSession session, JSONObject dataMap) {
		return this.onInsert(session, dataMap);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse updateStatus(ServiceSession session, JSONObject paramsObject) throws Exception{
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if(!paramsObject.containsKey(this.getKeyfieldName())){
			return ServiceResponse.buildFailure(session,ResponseCode.Exception.PRIMARY_IS_EMPTY);
		}
		
		if(!paramsObject.containsKey("status")){
			return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"updateStatus请求参数必须包含参数[{1}]","status");
		}
		
		ShowCategoryModel bean = this.getBeanById(session,getTemplate(),paramsObject);
		
		if(bean==null){
			return ServiceResponse.buildFailure(session,ResponseCode.Failure.NOT_EXIST);
		}
		
		Integer status = paramsObject.getInteger("status");
		
		bean.setStatus(status);
		bean.setModifier(session.getUser_code());
		bean.setUpdateDate(new Date());
		//1.更新主表
		this.onUpdate(session, JSON.parseObject(JSON.toJSONStringWithDateFormat(bean, "yyyy-MM-dd")));
		//2.更新关联表数据
	    paramsObject.put("updateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramsObject.put("entId", session.getEnt_id());
    	paramsObject.put("table","shopchannelcategorygoodsref");
		paramsObject.put("setField","isValid");
		paramsObject.put("setFieldValue",status );
		paramsObject.put("key","showCategoryId");
		paramsObject.put("values", Arrays.asList(paramsObject.get("showCategoryId")));
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		
		return ServiceResponse.buildSuccess("success");
	}

	//封装返回格式 （展示分类树）
	public ServiceResponse onShowCategoryTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		List<ShowCategoryTreeBean> showCategoryTree = showCategoryTreeForChannel(session,paramsObject);
		paramsObject.clear();
		paramsObject.put("showcategory", showCategoryTree);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//增加渠道定义的树
    public List<ShowCategoryTreeBean>  showCategoryTreeForChannel(ServiceSession session, JSONObject paramsObject){
        this.getTemplate().onSetContext(session);
        List<ChannelInfoModel> channelInfoFiltered = getSpecifiedChannel(session, paramsObject);
        //保存虚拟树的List
        List<ShowCategoryTreeBean> channelTrees = new ArrayList<ShowCategoryTreeBean>();
        
        for (ChannelInfoModel channelInfoModel : channelInfoFiltered) {
            paramsObject.put("channelCode", channelInfoModel.getChannelCode());
            List<ShowCategoryTreeBean> categoryTrees = showCategoryTree(session,paramsObject);//获取某个渠道的树
                //虚拟构造树
                ShowCategoryTreeBean showCategoryTreeBean = new ShowCategoryTreeBean();
                showCategoryTreeBean.setShowCategoryCode(channelInfoModel.getChannelCode());
                showCategoryTreeBean.setShowCategoryName(channelInfoModel.getChannelName());
                showCategoryTreeBean.setChannelId(channelInfoModel.getChannelId());
                showCategoryTreeBean.setChildren(categoryTrees);
                channelTrees.add(showCategoryTreeBean);
        }
        return channelTrees;
    }
	
	//若paramsObject包含channelCodes，则返回channelCodes对应的渠道信息
    //若paramsObject不包含channelCodes，则返回所有的渠道信息
    private List<ChannelInfoModel> getSpecifiedChannel(ServiceSession session, JSONObject paramsObject) {
        Criteria criteria = Criteria.where("selfBuildFlag").is(1).and("entId").is(session.getEnt_id()).and("channelType").is(1).and("status").is(1);//是否自建展示分类 1:是 0:否.channelType:1线上渠道
        Query query = new Query(criteria);
        
        //查询渠道定义
        List<ChannelInfoModel> allChannelInfo = this.getTemplate().select(query, ChannelInfoModel.class, "channelinfo");
        if(CollectionUtils.isEmpty(allChannelInfo))
          return new ArrayList<>();
        
        if(!paramsObject.containsKey("channelIds")) {
          return allChannelInfo;
        }
        
        List<ChannelInfoModel> result = new ArrayList<>();
        String[] channelIds = paramsObject.getString("channelIds").split(",");
        for(ChannelInfoModel c: allChannelInfo) {
          for(String s: channelIds) {
            if(s != null && Long.parseLong(s) == c.getChannelId().longValue()) {
              result.add(c);
            }
          }
        }
        
        return result;
    }
    
	//展示分类信息 （树形结构）
	public List<ShowCategoryTreeBean> showCategoryTree(ServiceSession session, JSONObject paramsObject){
		//1.查询所有门店数据
		Long entId = session.getEnt_id();
		Criteria criteria = null;
		if(paramsObject.getBooleanValue("checkedFlag")){
			criteria = Criteria.where("channelCode").is(paramsObject.getString("channelCode")).and("entId").is(entId).and("status").is(1);
		}else{
			criteria = Criteria.where("channelCode").is(paramsObject.getString("channelCode")).and("entId").is(entId);
		}
		Query query = new Query(criteria);
		this.getTemplate().onSetContext(session);
		Field fields = query.fields();
		fields.include("showCategoryId,showCategoryCode,showCategoryName,parentId,parentCode,level,status,channelCode,leafFlag");
		List<ShowCategoryTreeBean> allShowCategorys = this.getTemplate().select(query, ShowCategoryTreeBean.class, "showcategory");
		//2.获得顶级树数据
		List<ShowCategoryTreeBean> topShowCategorys = new ArrayList<>();
		for (ShowCategoryTreeBean showCategory : allShowCategorys) {
			if(StringUtils.isEmpty(showCategory.getParentId()) || "0".equals(showCategory.getParentId().toString())){
				topShowCategorys.add(showCategory);
			}
		}
		return buildTree(topShowCategorys,allShowCategorys);//从顶级节点开始往下递归
	}
	
	//从顶级节点开始递归构建树
	private List<ShowCategoryTreeBean> buildTree(List<ShowCategoryTreeBean> root, List<ShowCategoryTreeBean> allShowCategorys) {
		for (ShowCategoryTreeBean showCategory : root) {
			Long parentId = showCategory.getShowCategoryId();
			String parentName = showCategory.getShowCategoryName();
			List<ShowCategoryTreeBean> children = findChildren(parentId,parentName,allShowCategorys);
			buildTree(children,allShowCategorys);
			showCategory.setChildren(children);
		}
		return root;
	}
	
	//查询该节点的所有儿子节点
	private List<ShowCategoryTreeBean> findChildren(Long parentId,String parentName, List<ShowCategoryTreeBean> allShowCategorys) {
		List<ShowCategoryTreeBean> childrenCategory = new ArrayList<ShowCategoryTreeBean>();
		for (ShowCategoryTreeBean showCategory : allShowCategorys) {
			if(parentId.equals(showCategory.getParentId())){
				childrenCategory.add(showCategory);
				showCategory.setParentName(parentName);//增加父亲节点名称
			}
		}
		return childrenCategory;
	}
	
}
