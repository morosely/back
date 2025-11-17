package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ColdTransSaleGoodsService {

	// 查询商品列表
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	// 是否设为冷藏
	public ServiceResponse doColdset(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
