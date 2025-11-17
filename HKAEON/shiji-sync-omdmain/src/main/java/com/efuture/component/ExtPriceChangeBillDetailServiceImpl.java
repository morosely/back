package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtPriceChangeBillDetailModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtPriceChangeBillDetailServiceImpl extends CommonExtSyncService<ExtPriceChangeBillDetailModel,ExtPriceChangeBillDetailServiceImpl> {
	public ExtPriceChangeBillDetailServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
