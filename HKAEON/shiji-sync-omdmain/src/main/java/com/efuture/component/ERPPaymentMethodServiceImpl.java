package com.efuture.component;

import java.util.List;

import com.efuture.common.CommonSyncService;
import com.efuture.model.ErpPaymentMethodModel;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author yihaitao
 * @time 2018年5月14日 下午5:07:17
 */
public class ERPPaymentMethodServiceImpl extends CommonSyncService<ErpPaymentMethodModel, ERPPaymentMethodServiceImpl> {

	public ERPPaymentMethodServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
}
