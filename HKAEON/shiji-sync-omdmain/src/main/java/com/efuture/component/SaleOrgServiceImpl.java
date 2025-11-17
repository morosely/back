package com.efuture.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.common.CommonSyncService;
import com.efuture.model.SaleOrgModel;
import com.efuture.utils.HttpUtils;
import com.product.model.ServiceSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class SaleOrgServiceImpl extends CommonSyncService<SaleOrgModel,SaleOrgServiceImpl> {

	@Value("${category.sync.redis}")
	String url;

	private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

	public SaleOrgServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}

	public void onSyncRedis() {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0);
		JSONObject param = new JSONObject();
		param.put("entId", 0);
		param.put("erpCode", "002");

		try {
			LOGGER.info("调用主数据接口，同步redis，表:{}", getTableName());
			HttpUtils.onPost(url, session, param.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
