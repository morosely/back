package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.OrderCallNoHis;
import com.product.component.CommonServiceImpl;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class OrderCallNoServiceHisImpl extends CommonServiceImpl<OrderCallNoHis, OrderCallNoServiceHisImpl> {
    public OrderCallNoServiceHisImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
        return this.onQuery(session,paramsObject);
    }
}