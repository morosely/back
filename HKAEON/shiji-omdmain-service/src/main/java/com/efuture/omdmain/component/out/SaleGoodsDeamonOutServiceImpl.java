package com.efuture.omdmain.component.out;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.GoodsOutModel;
import com.efuture.omdmain.service.out.SaleGoodsDeamonOutService;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class SaleGoodsDeamonOutServiceImpl extends JDBCCompomentServiceImpl<GoodsOutModel> implements SaleGoodsDeamonOutService{
	
	public SaleGoodsDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}


	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
	@Autowired
	GoodsDeamonOutServiceImpl goodsDeamonOutServiceImpl;

	/*@SuppressWarnings("rawtypes")
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		DefaultParametersUtils.transformParam(session, paramsObject);
		FMybatisTemplate template = this.getTemplate();
		
		JSONObject result = new JSONObject();
		List<Map> list = new ArrayList<Map>();
		long count = template.getSqlSessionTemplate().selectOne("beanmapper.out.SaleGoodsDeamonOutMapper.count",paramsObject);
		if(count!=0L){
			list = template.getSqlSessionTemplate().selectList("beanmapper.out.SaleGoodsDeamonOutMapper.search",paramsObject);
		}else{
			result.put("goods", list);
			result.put("total_results", 0);
			return ServiceResponse.buildSuccess(result);
		}
		result.put("goods", list);
		result.put("total_results", count);
		
		return ServiceResponse.buildSuccess(result);
	}*/
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("ent_id", "entId");
		config.put("jygs", "erpCode");
		config.put("item_code", "goodsCode");
		config.put("barcode", "barNo");
		config.put("item_name", "goodsName");
		config.put("typeid", "goodsType");
		config.put("cat_code", "categoryCode");
		config.put("cat_name", "categoryName");
		config.put("brand_code", "brandCode");
		config.put("brand_name", "brandName");
		config.put("spec", "saleSpec");
		config.put("unit", "saleUnit");
		config.put("sale_price", "salePrice");
		config.put("vendor_id", "venderCode");
		config.put("STATUS", "goodsStatus");
//		config.put("goods", "saleGoods");
		if(!paramsObject.containsKey("STATUS")){
			paramsObject.put("STATUS", "1");
		}
		if(paramsObject.containsKey("page_size")){
			int page_size = paramsObject.getInteger("page_size");
			if(page_size>2000){
				return goodsDeamonOutServiceImpl.search(session, paramsObject);
			}
		}
//		DefaultParametersUtils.transformParam(session, paramsObject);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			Object d = result.getData();
			Object ds = null;
//			if(d instanceof JSONObject){
//				ds = JSONSerializeUtill.distinctByKey(((JSONObject) d).get(this.getCollectionName()));
//				if(ds instanceof List){
//					int num = ((List) ds).size();
//					((JSONObject) d).put("total_results", num);
//				}
//				((JSONObject) d).put(this.getCollectionName(), ds);
//				
//			}
			Object data = JSONSerializeUtill.toNameJSONByValue(d,config); 
			return ServiceResponse.buildSuccess(data);
		}
		return result;
	}

}
