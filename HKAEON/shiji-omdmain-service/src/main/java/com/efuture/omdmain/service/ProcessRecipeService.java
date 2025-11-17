package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ProcessRecipeService {

  ServiceResponse save(ServiceSession session, JSONObject paramsObject);

  ServiceResponse batchDelete(ServiceSession session, JSONObject paramsObject);

  ServiceResponse searchAllDetail(ServiceSession session, JSONObject paramsObject);

}
