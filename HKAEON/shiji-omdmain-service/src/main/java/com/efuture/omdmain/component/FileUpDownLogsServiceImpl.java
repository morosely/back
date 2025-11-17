package com.efuture.omdmain.component;

import com.efuture.omdmain.model.FileUpDownLogsModel;
import com.product.component.CommonServiceImpl;
import com.product.storage.template.FMybatisTemplate;

public class FileUpDownLogsServiceImpl extends CommonServiceImpl<FileUpDownLogsModel, FileUpDownLogsServiceImpl>{

	public FileUpDownLogsServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}



}
