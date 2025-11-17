package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/** 
* @author yihaitao
* @time 2018年5月14日 下午5:12:33 
* 
*/
public interface ERPPaymentMethodService {

	public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
}
