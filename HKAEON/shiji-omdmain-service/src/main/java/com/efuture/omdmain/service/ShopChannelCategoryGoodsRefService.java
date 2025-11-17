package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface ShopChannelCategoryGoodsRefService {

  ServiceResponse save(ServiceSession session, JSONObject paramsObject) throws Exception;

  ServiceResponse batchSave(ServiceSession session, JSONObject paramsObject);

}
