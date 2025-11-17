package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface SaleGoodsItemsService {

	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;

	/**
	 * 查询所有套餐及其明细
	 */
	public ServiceResponse searchAllMealDetail(ServiceSession session, JSONObject paramsObject);
}
