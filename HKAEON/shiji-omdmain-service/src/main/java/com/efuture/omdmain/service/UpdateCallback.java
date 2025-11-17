package com.efuture.omdmain.service;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface UpdateCallback {
	boolean submit(Query query,Update update,String message);
}
