package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.PriceChangeBillModel;
import org.mybatis.spring.SqlSessionTemplate;

public class PriceChangeBillServiceImpl extends CommonSyncService<PriceChangeBillModel,PriceChangeBillServiceImpl> {

	public PriceChangeBillServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
