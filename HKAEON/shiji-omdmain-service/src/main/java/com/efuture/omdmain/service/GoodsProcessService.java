package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface GoodsProcessService {

	// 查询商品基础表
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	// 根据商品sgid查询档口加工方法
	public ServiceResponse getDataBySgid(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
