package com.efuture.omdmain.component.out;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.CategoryOutModel;
import com.efuture.omdmain.service.out.CategoryDeamonOutService;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class CategoryDeamonOutServiceImpl extends JDBCCompomentServiceImpl<CategoryOutModel> implements CategoryDeamonOutService{

	public CategoryDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	/*@SuppressWarnings("rawtypes")
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		DefaultParametersUtils.transformParam(session, paramsObject);
		FMybatisTemplate template = this.getTemplate();
		
		JSONObject result = new JSONObject();
		List<Map> list = new ArrayList<Map>();
		long count = template.getSqlSessionTemplate().selectOne("beanmapper.out.CategoryDeamonOutMapper.count",paramsObject);
		if(count!=0L){
			list = template.getSqlSessionTemplate().selectList("beanmapper.out.CategoryDeamonOutMapper.search",paramsObject);
		}else{
			result.put("category", list);
			result.put("total_results", 0);
			return ServiceResponse.buildSuccess(result);
		}
		result.put("category", list);
		result.put("total_results", count);
		
		return ServiceResponse.buildSuccess(result);
	}*/
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("ent_id", "entId");
		config.put("code", "categoryCode");
		config.put("name", "categoryName");
		config.put("jygs", "erpCode");
		config.put("cid", "categoryId");
		config.put("is_parent", "leafFlag");
		config.put("parent_id", "parentId");
		config.put("parent_code", "parentCode");
		config.put("mcclass", "level");
//		config.put("channel", "category");
		Map<String, String> config1 = new HashMap<String, String>();
		config1.put("leafFlag", "true:0,false:1");
		
//		DefaultParametersUtils.transformParam(session, paramsObject);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		//只返回4级和5级数据                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                4级和5级代码
		JSONObject levelJson = new JSONObject();
		levelJson.put("in", Arrays.asList("4","5"));
		paramsObject.put("level",levelJson);
		if(!paramsObject.containsKey("status")){
			paramsObject.put("status", "1");
		}
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Object data = JSONSerializeUtill.toNameJSONByValue(JSONSerializeUtill.toValueJSONByKey(result.getData(),config1,",",":"),config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
	}

}
