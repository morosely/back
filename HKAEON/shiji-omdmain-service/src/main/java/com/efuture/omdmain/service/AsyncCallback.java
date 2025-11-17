package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceSession;

public interface AsyncCallback {
	void onCallback(ServiceSession session,JSONObject params);
}
