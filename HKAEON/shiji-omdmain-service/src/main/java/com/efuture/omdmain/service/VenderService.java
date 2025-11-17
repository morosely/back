package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * @Desciption:供应商信息查询
 */
public interface VenderService {
    /*
    * @Description:  根据供应商编码，供应商名称查询
     * @param session
     * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
}
