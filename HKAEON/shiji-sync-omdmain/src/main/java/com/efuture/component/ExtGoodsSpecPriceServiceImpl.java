package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtGoodsSpecPriceModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtGoodsSpecPriceServiceImpl extends CommonExtSyncService<ExtGoodsSpecPriceModel,ExtGoodsSpecPriceServiceImpl>{


	public ExtGoodsSpecPriceServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
