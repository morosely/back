package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtSaleOrgModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtSaleOrgServiceImpl extends CommonExtSyncService<ExtSaleOrgModel,ExtSaleOrgServiceImpl> {

	public ExtSaleOrgServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
