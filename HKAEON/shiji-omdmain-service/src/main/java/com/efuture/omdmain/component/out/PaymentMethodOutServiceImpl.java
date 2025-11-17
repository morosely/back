package com.efuture.omdmain.component.out;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.SaleGoodsServiceImpl;
import com.efuture.omdmain.model.out.PaymentMethodOutModel;
import com.efuture.omdmain.model.out.PaymentMethodRefOutModel;
import com.efuture.omdmain.service.out.PaymentDeamonOutService;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentMethodOutServiceImpl extends JDBCCompomentServiceImpl<PaymentMethodOutModel>{

	public PaymentMethodOutServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName){
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	@Autowired
	private PaymentDeamonOutServiceImpl paymentDeamonOutServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(PaymentMethodOutServiceImpl.class);

	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if(null != paramsObject.getBoolean("oldMethod")  && paramsObject.getBoolean("oldMethod")){
			logger.info("【paymethod search 营销支付方式查询（Old）】==========>>> 入参：{}",paramsObject);
			return paymentDeamonOutServiceImpl.search(session,paramsObject);
		}
		logger.info("【paymethod search 营销支付方式查询（New）】==========>>> 入参：{}",paramsObject);

		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("ent_id", "entId");
		config.put("code", "payCode");
		config.put("name", "payName");
		config.put("jygs", "erpCode");
		config.put("paytype", "payType");
		config.put("parent_id", "parentCode");
		config.put("pmhl", "rate");//汇率
		config.put("pmlx", "virtualPayType");//1-券支付，2-积分支付
		config.put("ismj", "leafFlag");//是否末级
		config.put("paymode", "paymentmethod");
		
//		Map<String, String> config1 = new HashMap<String, String>();
//		config1.put("mattype", "true:1,false:0");
		
		
//		DefaultParametersUtils.transformParam(session, paramsObject);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		//20221124 陈总要求去掉过滤，后续等营销确认
		if(paramsObject.containsKey("virtualPayType")) {
			paramsObject.remove("virtualPayType");
		}
		
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Object d = result.getData();
			/*Object ds = null;
			if(d instanceof JSONObject){
				ds = JSONSerializeUtill.distinctByKey(((JSONObject) d).get(this.getCollectionName()));
				if(ds instanceof List){
					int num = ((List) ds).size();
					((JSONObject) d).put("total_results", num);
				}
				((JSONObject) d).put(this.getCollectionName(), ds);
				
			}*/
			Object data = JSONSerializeUtill.toNameJSONByValue(d,config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
	}

}
