package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.PriceChangeBillDetailModel;
import org.mybatis.spring.SqlSessionTemplate;

public class PriceChangeBillDetailServiceImpl extends CommonSyncService<PriceChangeBillDetailModel,PriceChangeBillDetailServiceImpl> {

	public PriceChangeBillDetailServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
