package com.efuture.component;

import com.efuture.common.CommonSyncService;
import com.efuture.model.PaymentMethod;
import org.mybatis.spring.SqlSessionTemplate;

public class PaymentMethodService extends CommonSyncService<PaymentMethod,  PaymentMethodService> {

    public PaymentMethodService(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }
}
