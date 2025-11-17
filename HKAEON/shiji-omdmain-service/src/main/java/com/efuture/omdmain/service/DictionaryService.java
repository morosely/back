package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface DictionaryService {

	//分类字典 -列表查询
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//分类字典 -编辑
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//批量增加
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//删除
    public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception;
	
}
