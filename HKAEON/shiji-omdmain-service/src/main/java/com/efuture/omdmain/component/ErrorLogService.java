package com.efuture.omdmain.component;

import com.efuture.omdmain.model.ErrorLogModel;
import com.product.component.CommonServiceImpl;
import com.product.storage.template.FMybatisTemplate;

public class ErrorLogService extends CommonServiceImpl<ErrorLogModel, ErrorLogService> {

	public ErrorLogService(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

}
