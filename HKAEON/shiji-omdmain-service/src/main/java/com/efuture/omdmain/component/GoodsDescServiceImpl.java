package com.efuture.omdmain.component;

import com.product.component.CommonServiceImpl;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsDescModel;
import com.efuture.omdmain.service.GoodsDescService;
import com.mongodb.DBObject;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class GoodsDescServiceImpl extends CommonServiceImpl<GoodsDescModel,GoodsDescServiceImpl> implements GoodsDescService {

	public GoodsDescServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
		return this.onQuery(session, paramsObject);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject) {
		return this.onInsert(session, paramsObject);
	}
  
  	//根据商品ID查商品文描信息
	public ServiceResponse getGoodsDescBySgid(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,
					String.format("请求参数必须包含参数[%1$s]", "sgid"));
		}

		return this.onQuery(session, paramsObject);

	}
	
	// 根据sgid修改文描信息
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse saveBySgid(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
		}
		if (!paramsObject.containsKey("showTerm")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "showTerm"));
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
		}

		JSONObject goodsDesc = new JSONObject();
		goodsDesc.put("sgid", paramsObject.get("sgid"));
		goodsDesc.put("showTerm", paramsObject.get("showTerm"));
		ServiceResponse gDQuery = this.onQuery(session, goodsDesc);
		JSONObject gDData = (JSONObject) gDQuery.getData();
		JSONArray gDList = gDData.getJSONArray(this.getCollectionName());
		
		this.setUpsert(false);
		if (gDList.size() > 0) {// 有的话，先删除旧的
			goodsDesc.clear();
			goodsDesc.put("sgid", paramsObject.get("sgid"));
			goodsDesc.put("showTerm", paramsObject.get("showTerm"));
			this.onDelete(session, goodsDesc);
		}
		
		// 新增新的
		goodsDesc.put("goodsDesc", paramsObject.get("goodsDesc"));
		goodsDesc.put("erpCode", paramsObject.get("erpCode"));
		goodsDesc.put("goodsCode", paramsObject.get("goodsCode"));
		
		ServiceResponse result = this.onInsert(session, goodsDesc);
		
		return ServiceResponse.buildSuccess("success");
	}

}
