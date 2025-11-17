package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BaseUserData;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface BaseServiceCallback {
	ServiceResponse onCallback(ServiceSession session,Long requestID,BaseUserData threadObject,JSONObject data);
	long getRowCount();
}
