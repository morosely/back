package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface VirtualSaleGoodsService {

	//查询虚拟母品列表
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	//生成虚拟母品编码和条码
	public ServiceResponse generate(ServiceSession session, JSONObject paramsObject) throws Exception;
	//生成虚拟母品和项目项
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;
	//虚拟母品明细
    public ServiceResponse detail(ServiceSession session, JSONObject paramsObject) throws Exception;
    //虚拟母品修改
    public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;
}
