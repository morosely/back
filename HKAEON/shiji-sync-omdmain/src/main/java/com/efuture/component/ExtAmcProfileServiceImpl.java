package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtAmcProfileModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtAmcProfileServiceImpl extends CommonExtSyncService<ExtAmcProfileModel, ExtAmcProfileServiceImpl> {
    public ExtAmcProfileServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
}
