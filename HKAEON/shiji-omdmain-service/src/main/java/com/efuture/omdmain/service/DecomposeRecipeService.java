package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface DecomposeRecipeService {

	/**
	 * 查询分解配方明细
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	ServiceResponse searchAllDetail(ServiceSession session, JSONObject paramsObject);

	// 根据档口信息siid查询档口经营商品信息
//	public ServiceResponse getDataBySiid(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
