package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:供应商商品查询
 */
public interface ExtSupplierGoodsService {
    

    /* 
    * @Description: 查询ext表中的供应商 商品查询
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject); 
}
