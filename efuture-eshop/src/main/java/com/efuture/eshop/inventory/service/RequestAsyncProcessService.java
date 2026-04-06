package com.efuture.eshop.inventory.service;

import com.efuture.eshop.inventory.request.Request;

/**
 * 请求异步执行的service
 * @author Administrator
 *
 */
public interface RequestAsyncProcessService {

	void process(Request request);
	
}
