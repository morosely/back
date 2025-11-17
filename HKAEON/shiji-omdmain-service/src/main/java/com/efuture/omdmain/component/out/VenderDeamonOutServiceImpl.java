package com.efuture.omdmain.component.out;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.VenderOutModel;
import com.efuture.omdmain.service.out.VenderDeamonOutService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class VenderDeamonOutServiceImpl extends JDBCCompomentServiceImpl<VenderOutModel> implements VenderDeamonOutService{
	public VenderDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		
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
		long count = template.getSqlSessionTemplate().selectOne("beanmapper.out.VenderDeamonOutMapper.count",paramsObject);
		if(count!=0L){
			list = template.getSqlSessionTemplate().selectList("beanmapper.out.VenderDeamonOutMapper.search",paramsObject);
		}else{
			result.put("vendor", list);
			result.put("total_results", 0);
			return ServiceResponse.buildSuccess(result);
		}
		result.put("vendor", list);
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
		config.put("code", "venderCode");
		config.put("name", "venderName");
		config.put("jygs", "erpCode");
		config.put("oid", "vid");
		config.put("vendor", "vender");
		
//		DefaultParametersUtils.transformParam(session, paramsObject);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Object data = JSONSerializeUtill.toNameJSONByValue(result.getData(),config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
	}
	

}
