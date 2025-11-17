package com.efuture.omdmain.component;

import com.product.component.CommonServiceImpl;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BusinessCompanyModel;
import com.efuture.omdmain.service.BusinessCompanyService;
import com.mongodb.DBObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/** 
* @author yihaitao
* @time 2018年5月14日 下午4:06:47 
* 
*/
public class BusinessCompanyServiceImpl extends CommonServiceImpl<BusinessCompanyModel,BusinessCompanyServiceImpl> implements BusinessCompanyService{

	public BusinessCompanyServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	/*@Override
	protected FMybatisTemplate getTemplate() {
		return this.getBean("StorageOperation", FMybatisTemplate.class);
	}*/

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

}
