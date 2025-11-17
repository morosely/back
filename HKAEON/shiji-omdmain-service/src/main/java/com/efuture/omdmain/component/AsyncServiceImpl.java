package com.efuture.omdmain.component;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.service.AsyncCallback;
import com.efuture.omdmain.service.AsyncService;
import com.product.component.BaseServiceImpl;
import com.product.model.ServiceSession;

@Service(value="omdmain.asyn.service")
public class AsyncServiceImpl implements AsyncService {
	
	@Async
	@Override
	public void onCallback(ServiceSession session, JSONObject params,
			AsyncCallback callback) {
		callback.onCallback(session, params);
	}
	
	
	

}
