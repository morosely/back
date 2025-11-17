package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.service.AsyncCallback;
import com.efuture.omdmain.service.AsyncService;
import com.product.component.BaseServiceImpl;
import com.product.model.ServiceSession;
import com.product.util.SpringBeanFactory;

public class TaskServiceImpl{
	
	private static TaskServiceImpl service;
	
	private static synchronized void initService() {
		if (service==null) {
			service=new TaskServiceImpl();
		}
	}
	
	public static TaskServiceImpl getInstance() {
		if (service==null) {
			initService();
		}
		return service;
	}
	
	private AsyncService getAsyncService() {
		return SpringBeanFactory.getBean("omdmain.asyn.service",AsyncService.class);
	}
	
	private AsyncService getAsyncService(String method) {
		return SpringBeanFactory.getBean(method,AsyncService.class);
	}
	
	public void onAsync(ServiceSession session,JSONObject params,AsyncCallback callback) {
		//异步执行代码
		this.getAsyncService().onCallback(session, params,callback);
	}
	
	public void onAsync(ServiceSession session,JSONObject params,AsyncCallback callback,String method) {
		//异步执行代码
		this.getAsyncService(method).onCallback(session, params,callback);
	}

}
