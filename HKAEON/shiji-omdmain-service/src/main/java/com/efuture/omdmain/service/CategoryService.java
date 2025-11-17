package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:
 */
public interface CategoryService {

    public ServiceResponse pGet(ServiceSession session, JSONObject paramsObject);

    public ServiceResponse sGet(ServiceSession session, JSONObject paramsObject);
    
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
    
    public ServiceResponse detail(ServiceSession session, JSONObject paramsObject);
}
