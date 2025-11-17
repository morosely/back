package com.efuture.omdmain.component.out;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.PaymentMethodRefOutModel;
import com.efuture.omdmain.service.out.PaymentDeamonOutService;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class PaymentDeamonOutServiceImpl extends JDBCCompomentServiceImpl<PaymentMethodRefOutModel> implements PaymentDeamonOutService{
	
	public PaymentDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName){
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
		long count = template.getSqlSessionTemplate().selectOne("beanmapper.out.PaymentDeamonOutMapper.count",paramsObject);
		System.out.println("------------>>>");
		System.out.println(count);
		if(count!=0L){
			System.out.println("=============");
			list = template.getSqlSessionTemplate().selectList("beanmapper.out.PaymentDeamonOutMapper.search",paramsObject);
		}else{
			result.put("paymode", list);
			result.put("total_results", 0);
			return ServiceResponse.buildSuccess(result);
		}
		result.put("paymode", list);
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
		config.put("code", "payCode");
		config.put("name", "payName");
		config.put("jygs", "erpCode");
		config.put("paytype", "payType");
		config.put("parent_id", "parentCode");
		config.put("pmhl", "rate");//汇率
		config.put("pmlx", "virtualPayType");//1-券支付，2-积分支付
		config.put("ismj", "leafFlag");//是否末级
		config.put("paymode", "paymentMethodRef");
		
//		Map<String, String> config1 = new HashMap<String, String>();
//		config1.put("mattype", "true:1,false:0");
		
		
//		DefaultParametersUtils.transformParam(session, paramsObject);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Object d = result.getData();
			Object ds = null;
			if(d instanceof JSONObject){
				ds = JSONSerializeUtill.distinctByKey(((JSONObject) d).get(this.getCollectionName()));
				if(ds instanceof List){
					int num = ((List) ds).size();
					((JSONObject) d).put("total_results", num);
				}
				((JSONObject) d).put(this.getCollectionName(), ds);
				
			}
			Object data = JSONSerializeUtill.toNameJSONByValue(d,config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
	}

}
