package com.efuture.omdmain.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ERPPaymentMethodModel;
import com.efuture.omdmain.service.ERPPaymentMethodService;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/** 
* @author yihaitao
* @time 2018年5月14日 下午5:07:17 
* 
*/
public class ERPPaymentMethodServiceImpl extends JDBCCompomentServiceImpl<ERPPaymentMethodModel> implements ERPPaymentMethodService{

	public ERPPaymentMethodServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}
	
	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
		//25422 主数据-支付方式：变更支付方式的状态为删除-1，有正确接收，但是后台支付方式设置时，还能选择被删除的erp支付方式，应不能选择
		if(paramsObject.getShort("status") == null ){
			paramsObject.put("status",1);
		}
		ServiceResponse result = this.onQuery(session, paramsObject);

		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Map<String, Object> config = new HashMap<String, Object>();
			config.put("paymentModeId", "payCode");
			config.put("paymentModeName", "payName");
			Object data = JSONSerializeUtill.toNameJSONByValue(result.getData(),config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
	}

}
