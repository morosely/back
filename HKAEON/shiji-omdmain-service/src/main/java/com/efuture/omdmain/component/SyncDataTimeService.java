package com.efuture.omdmain.component;

import com.efuture.omdmain.model.out.SyncDataTimeModel;
import com.product.component.CommonServiceImpl;
import com.product.storage.template.FMybatisTemplate;

public class SyncDataTimeService extends CommonServiceImpl<SyncDataTimeModel, SyncDataTimeService> {

	public SyncDataTimeService(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

}
