package com.efuture.omdmain.component.out;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.SaleOrgOutModel;
import com.efuture.omdmain.service.out.SaleOrgDeamonOutService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class SaleOrgDeamonOutServiceImpl extends JDBCCompomentServiceImpl<SaleOrgOutModel> implements SaleOrgDeamonOutService{

	public SaleOrgDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("ent_id", "entId");
		config.put("yjgs", "erpCode");
		config.put("oid", "saleOrgId");
		config.put("code", "orgCode");
		config.put("name", "orgName");
		config.put("parent_id", "parentCode");
		config.put("org_code", "shopCode");
		config.put("manaframe", "saleOrg");
		
//		DefaultParametersUtils.transformParam(session, paramsObject,false);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Object data = JSONSerializeUtill.toNameJSONByValue(result.getData(),config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
		
	}
}
