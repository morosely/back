package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface GoodsDescService {

  ServiceResponse save(ServiceSession session, JSONObject paramsObject);

}
