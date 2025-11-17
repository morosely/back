package com.efuture.component;

import com.efuture.common.CommonExtSyncService;
import com.efuture.model.ExtErpPaymentMethodModel;
import org.mybatis.spring.SqlSessionTemplate;

public class ExtERPPaymentMethodServiceImpl extends CommonExtSyncService<ExtErpPaymentMethodModel,ExtERPPaymentMethodServiceImpl> {


    public ExtERPPaymentMethodServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
}
