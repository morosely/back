package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/** 
* @author yihaitao
* @time 2018年5月15日 下午1:51:42 
* 
*/
public interface DictionaryTypeService {

	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;

	/**
	 * 查询所有字典数据
	 */
	public ServiceResponse searchAllDictData(ServiceSession session, JSONObject paramsObject) throws Exception;
}
