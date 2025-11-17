package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtPriceChangeBillModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtPriceChangeBillServiceImpl extends CommonExtSyncService<ExtPriceChangeBillModel,ExtPriceChangeBillServiceImpl> {

    public ExtPriceChangeBillServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
}
