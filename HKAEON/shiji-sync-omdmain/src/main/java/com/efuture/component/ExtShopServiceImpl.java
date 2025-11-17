package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtAeonMoreBarNoModel;
import com.efuture.model.ExtShopModel;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

public class ExtShopServiceImpl extends CommonExtSyncService<ExtShopModel,ExtShopServiceImpl>{


	public ExtShopServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
	
}
