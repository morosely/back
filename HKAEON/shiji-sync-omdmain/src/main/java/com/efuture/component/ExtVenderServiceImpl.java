package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtShopModel;
import com.efuture.model.ExtVenderModel;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

public class ExtVenderServiceImpl extends CommonExtSyncService<ExtVenderModel,ExtVenderServiceImpl> {

	public ExtVenderServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}

}
