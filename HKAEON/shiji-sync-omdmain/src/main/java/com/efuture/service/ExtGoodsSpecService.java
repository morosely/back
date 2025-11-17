package com.efuture.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public interface ExtGoodsSpecService {

	public ServiceResponse receive(ServiceSession session, JSONObject paramsObject);
    
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
}
