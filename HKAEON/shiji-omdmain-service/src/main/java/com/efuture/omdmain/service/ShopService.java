package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ShopService {

	// 查询
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	// 根据门店查询同一个组中的所有门店
	public ServiceResponse searchByGroupCode(ServiceSession session, JSONObject paramsObject);

}
