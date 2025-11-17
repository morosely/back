package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ChannelInfoService {
	
	ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;

	ServiceResponse searchByPosMode(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
