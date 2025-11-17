package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.product.component.CommonServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.BusinessCompanyModel;
import com.efuture.omdmain.model.PaymentMethodRefModel;
import com.efuture.omdmain.service.BeanConstant;
import com.efuture.omdmain.service.PaymentMethodRefService;
import com.mongodb.DBObject;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/** 
* @author yihaitao
* @time 2018年5月14日 下午4:38:13 
* 
*/
public class PaymentMethodRefServiceImpl extends CommonServiceImpl<PaymentMethodRefModel,PaymentMethodRefServiceImpl> implements PaymentMethodRefService{
	
	@Autowired
	private BusinessCompanyServiceImpl businessCompanyService;

	public PaymentMethodRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

//	@Override
//	protected FMybatisTemplate getTemplate() {
//		return this.getBean("StorageOperation", FMybatisTemplate.class);
//	}

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

	//批量更新
	@Override
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		return super.onUpdate(session, paramsObject);
	}

	//批量插入
	@Override
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onInsert(session, paramsObject);
	}
	
	public ServiceResponse search1(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_no")) {
			paramsObject.put("page_no", 0);
		} else {
			paramsObject.put("page_no",
					(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
		}
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
		
		List paymentMethodRefList = template.getSqlSessionTemplate()
				.selectList("beanmapper.PaymentMethodRefModelMapper.getPaymentMethodRefList", paramsObject);
		long total_results = 0;
		if (paymentMethodRefList != null && paymentMethodRefList.size() > 0) {
			total_results = template.getSqlSessionTemplate()
					.selectOne("beanmapper.PaymentMethodRefModelMapper.countPaymentMethodRefList", paramsObject);
		}
		JSONObject result = new JSONObject();
		result.put("paymentmethodref", paymentMethodRefList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
	
	@Override
	  public ServiceResponse searchAllPaymentMethodRef(ServiceSession session, JSONObject paramsObject) {
		  FMybatisTemplate template = this.getTemplate();
		  ServiceResponse response = businessCompanyService.onQuery(session, paramsObject);
		  List<PaymentMethodRefModel> paymentMethodRefList = null;
		  if(response.getReturncode() != null && response.getReturncode().equals("0")){
			  JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			  List<BusinessCompanyModel> bcList = JSONArray.parseArray(JSONObject.toJSONString(data.get("businesscompany")), BusinessCompanyModel.class);
			  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			  for(BusinessCompanyModel bc : bcList){
				  Map<String, Object> paymentmethodrefMap = new HashMap<String,Object>();
				  paymentmethodrefMap.put("erpCode", bc.getErpCode());
				  paymentmethodrefMap.put("erpName", bc.getErpName());
				  paymentmethodrefMap.put("remark", bc.getRemark());
				  paymentmethodrefMap.put("entId", bc.getEntId());
				  resultList.add(paymentmethodrefMap);
				  
				  JSONObject paramsObj = new JSONObject();
				  paramsObj.put("erpCode", bc.getErpCode());
				  paramsObj.put("status", 1);
				  paramsObj.put(BeanConstant.QueryField.PARAMKEY_ORDERFLD, "pmrid");
				  paramsObj.put(BeanConstant.QueryField.PARAMKEY_ORDERDIR, "desc");
				  paramsObj.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, Integer.MAX_VALUE);
				  ServiceResponse detailResponse = super.onQuery(session, paramsObj);
				  if(detailResponse.getReturncode() != null && detailResponse.getReturncode().equals("0")){
					  data =  JSONObject.parseObject(JSONObject.toJSONString(detailResponse.getData()));
					  paymentMethodRefList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), PaymentMethodRefModel.class);
					  for(PaymentMethodRefModel pmr: paymentMethodRefList) {
			    			paymentmethodrefMap = new HashMap<String,Object>();
			    			paymentmethodrefMap.put("payCode", pmr.getPayCode());
			    			paymentmethodrefMap.put("payName", pmr.getPayName());
			    			paymentmethodrefMap.put("paymentModeId", pmr.getPaymentModeId());
			    			paymentmethodrefMap.put("paymentModeName", pmr.getPaymentModeName());
			    			paymentmethodrefMap.put("pmid", pmr.getPmid());
			    			paymentmethodrefMap.put("pmrid", pmr.getPmrid());
			    			paymentmethodrefMap.put("remark", pmr.getRemark());
				    		resultList.add(paymentmethodrefMap);
					  }
				  }
			  }
			  JSONObject result = new JSONObject();
			  result.put(this.getCollectionName(), resultList);
			  result.put("total_results", data.get("total_results"));
			  return ServiceResponse.buildSuccess(result);
			  
		  }
		  return response;
	  }

	public ServiceResponse searchPaymentmethod(ServiceSession session, JSONObject paramsObject) throws Exception {
		if(StringUtils.isEmpty(paramsObject.getString("erpCode"))){
			return ServiceResponse.buildFailure(session,ResponseCode.FAILURE, "请输入经营公司");
		}

		if(StringUtils.isEmpty(paramsObject.getString("paymentModeId"))){
			return ServiceResponse.buildFailure(session,ResponseCode.FAILURE, "请输入erp支付方式");
		}
		paramsObject.put("status", "1");
		return  onQuery(session,paramsObject);
	}

}
