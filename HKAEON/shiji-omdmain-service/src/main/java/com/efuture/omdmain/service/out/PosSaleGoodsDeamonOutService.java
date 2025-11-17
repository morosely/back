package com.efuture.omdmain.service.out;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface PosSaleGoodsDeamonOutService {
	//查询(电子秤资料更新管理)
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	//查询（打印副单）
    public ServiceResponse query(ServiceSession session, JSONObject paramsObject) throws Exception;
	//更新（打印副单）
	public ServiceResponse updatePrtDuplFalg(ServiceSession session, JSONObject paramsObject) throws Exception;
}
