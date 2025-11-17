package com.efuture.omdmain.service;


import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface PaymentMethodService {
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	public ServiceResponse onPaymentMethodTree(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//查询支付方式和支付方式关系
	public ServiceResponse detail(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//修改支付方式和支付方式关系
    public ServiceResponse updatePaymentMethodAndRef(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
