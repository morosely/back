package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public interface ExtGoodsService {

    public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
}
