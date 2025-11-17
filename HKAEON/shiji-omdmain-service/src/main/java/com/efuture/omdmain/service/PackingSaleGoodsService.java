package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface PackingSaleGoodsService {

	//查询
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	//增加
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;
	//编辑
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;
	//启用停用
	public ServiceResponse startOrStop(ServiceSession session, JSONObject paramsObject) throws Exception;
}
