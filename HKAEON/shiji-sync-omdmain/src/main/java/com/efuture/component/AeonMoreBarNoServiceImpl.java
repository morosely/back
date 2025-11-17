package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.AeonMoreBarNoModel;
import org.mybatis.spring.SqlSessionTemplate;

public class AeonMoreBarNoServiceImpl extends CommonSyncService<AeonMoreBarNoModel, AeonMoreBarNoServiceImpl> {
    public AeonMoreBarNoServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }

}
