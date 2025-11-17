package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.GoodsVenderRefModel;
import org.mybatis.spring.SqlSessionTemplate;

public class GoodsVenderRefServiceImpl extends CommonSyncService<GoodsVenderRefModel,GoodsVenderRefServiceImpl> {

	public GoodsVenderRefServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
