package com.efuture.omdmain.component.out;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.ShopOutModel;
import com.efuture.omdmain.service.out.ShopDeamonOutService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class ShopDeamonOutServiceImpl extends JDBCCompomentServiceImpl<ShopOutModel> implements  ShopDeamonOutService{

	public ShopDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if(!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_ORDERFLD)){
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_ORDERFLD, "shopCode");
		}
		if(!paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_ORDERDIR)){
			paramsObject.put(BeanConstant.QueryField.PARAMKEY_ORDERDIR, "asc");
		}
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("ent_id", "entId");
		config.put("code", "shopCode");
		config.put("name", "shopName");
		config.put("jygs", "erpCode");
		config.put("parent_id", "parentCode");
		config.put("lastflag", "leafFlag");
		config.put("organization", "shop");
		config.put("shopId", "oid");
		
		Map<String, String> config1 = new HashMap<String, String>();
		config1.put("leafFlag", "true:Y,false:N");
		
//		DefaultParametersUtils.transformParam(session, paramsObject);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		if(!paramsObject.containsKey("shopTypex")){
			paramsObject.put("shopTypex", "0");
		}
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
	
	public ServiceResponse queryTestBySleep(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		
		long t = 0L;
		
		if(paramsObject.containsKey("sleepTime")){
			t = paramsObject.getLongValue("sleepTime");
		}
		
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return onQuery(session, paramsObject);
	}
}
