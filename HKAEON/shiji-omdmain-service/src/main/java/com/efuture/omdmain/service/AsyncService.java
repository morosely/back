package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceSession;

public interface AsyncService {
	 void onCallback(ServiceSession session,JSONObject params,AsyncCallback callback);

}
