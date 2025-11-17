package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ShowCategoryService {
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
	
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject);

	public ServiceResponse categoryDelete(ServiceSession session, JSONObject paramsObject);
}
