package com.efuture.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ExtPriceChangeBillDetailService {

	ServiceResponse receive(ServiceSession session, JSONObject paramsObject);
	
	ServiceResponse search(ServiceSession session, JSONObject paramsObject);
	
}
