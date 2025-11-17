package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface StallInfoService {

	// 根据组织机构shopId查询档口信息
	public ServiceResponse getDataByShopId(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
