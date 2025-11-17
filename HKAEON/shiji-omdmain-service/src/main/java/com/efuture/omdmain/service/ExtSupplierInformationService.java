package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:供应商信息查询
 */
public interface ExtSupplierInformationService {
    /*
    * @Description:  根据供应商编码，供应商名称查询
     * @param session
     * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
}
