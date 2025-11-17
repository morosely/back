package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface PaymentMethodRefService {
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;

	/**
	 * 查询所有支付方式关系
	 */
	public ServiceResponse searchAllPaymentMethodRef(ServiceSession session, JSONObject paramsObject);
}
