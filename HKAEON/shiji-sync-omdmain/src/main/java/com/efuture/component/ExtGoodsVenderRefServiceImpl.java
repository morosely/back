package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtGoodsVenderRefModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtGoodsVenderRefServiceImpl extends CommonExtSyncService<ExtGoodsVenderRefModel,ExtGoodsVenderRefServiceImpl> {

	public ExtGoodsVenderRefServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
