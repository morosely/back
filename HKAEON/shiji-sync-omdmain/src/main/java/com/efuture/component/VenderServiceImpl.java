package com.efuture.component;


import com.efuture.common.CommonSyncService;
import com.efuture.model.VenderModel;
import org.mybatis.spring.SqlSessionTemplate;

public class VenderServiceImpl extends CommonSyncService<VenderModel,VenderServiceImpl> {

	public VenderServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
