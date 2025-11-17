package com.efuture.omdmain.component.out;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.omdmain.model.out.SaleGoodsOutModel;
import com.efuture.omdmain.service.out.GoodsShopRefDeamonOutService;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GoodsShopRefDeamonOutServiceImpl extends JDBCCompomentServiceImpl<SaleGoodsOutModel> implements  GoodsShopRefDeamonOutService{

	public GoodsShopRefDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	
	/*@SuppressWarnings("rawtypes")
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		DefaultParametersUtils.transformParam(session, paramsObject);
		FMybatisTemplate template = this.getTemplate();
		
		JSONObject result = new JSONObject();
		List<Map> list = new ArrayList<Map>();
		long count = template.getSqlSessionTemplate().selectOne("beanmapper.out.GoodsShopRefDeamonOutMapper.count",paramsObject);
		if(count!=0L){
			list = template.getSqlSessionTemplate().selectList("beanmapper.out.GoodsShopRefDeamonOutMapper.search",paramsObject);
		}else{
			result.put("item_business", list);
			result.put("total_results", 0);
			return ServiceResponse.buildSuccess(result);
		}
		result.put("item_business", list);
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
		config.put("org_code", "shopCode");
		config.put("org_name", "shopName");
		config.put("status", "goodstatus");
		config.put("vendor_id", "venderCode");
		config.put("purchase_price", "contractCost");
		config.put("purchase_tax", "costTaxRate");
		config.put("gross_margin", "deductRate");
		config.put("trading_term", "operateFlag");
		config.put("mattype", "singleItemFlag");
		config.put("spucode", "parentGoodsCode");
		config.put("vendor_name", "venderName");
		config.put("saleGoods", "item_business");
		Map<String, String> config1 = new HashMap<String, String>();
		config1.put("mattype", "true:1,false:0");
		
//		DefaultParametersUtils.transformParam(session, paramsObject,false);
		paramsObject = (JSONObject) JSONSerializeUtill.toNameJSONByKey(JSONSerializeUtill.toValueJSONByKey(paramsObject, config), config);
		JSONObject o = new JSONObject();
		JSON.parseObject(JSON.toJSONString(paramsObject,
				new NameFilter() {
					@Override
					public String process(Object object, String name,Object value) {
							if (name.equals("goodsCode")){
								o.put("mainBarcodeFlag", 1);
								return name;
							}
						return name;
					}
				}, SerializerFeature.WriteDateUseDateFormat));
		
		if(o.containsKey("mainBarcodeFlag")){
			paramsObject.put("mainBarcodeFlag",o.get("mainBarcodeFlag"));
		}
		if(!paramsObject.containsKey("goodsStatus")){
			paramsObject.put("goodsStatus", Arrays.asList(1,2,3,5,6,10));
		}
		
		if(paramsObject.containsKey("barNo")){
			if(StringUtils.isEmpty(paramsObject.get("barNo"))){
				//只有编码的时候只传主条码
				if(paramsObject.containsKey("mainBarcodeFlag"))
					paramsObject.remove("mainBarcodeFlag");
			}
		}
		
		ServiceResponse result =  this.onQueryWithHint(session, paramsObject,true);
		if(result.getReturncode().equals(ResponseCode.SUCCESS)){
			try {
				result = filterData(session, paramsObject, result);
			}catch (Exception e){
				e.printStackTrace();
			}
			Object data = JSONSerializeUtill.toNameJSONByValue(JSONSerializeUtill.toValueJSONByKey(result.getData(),config1,",",":"),config);
			return ServiceResponse.buildSuccess(data);
		}
		return result;
		
	}

	/**
	 * add by yihaitao 2023-07-12
	 * 28536 销售商品验码：验码页面，当查询的商品没有主条码时，查不到商品，需要优化，当没主条码时，要取最新的一行有效数据返回
	 * 1.一个商品存在多个主条码，取时间最新的一条
	 * 2.主条码为空，查询从条码；如果从条码为多条，取最新的一条
	 */
	public ServiceResponse filterData(ServiceSession session,JSONObject paramsObject,ServiceResponse result) throws Exception{
		//1.判断入参是否是单个商品编码查询条件，单个商品查询遵循上述规则
		Set valueSet = searchJSONValueByKey(paramsObject,"goodsCode");
		if(valueSet!=null && !valueSet.isEmpty()){
			Object goodsCodeparam = valueSet.iterator().next();
			//{"goodsCode":"10000,10086,10010"},goodsCode:["10000,10086,10010"]
			if(goodsCodeparam.toString().indexOf(',') == -1 && goodsCodeparam.toString().indexOf('[') == -1 ){
				if(result.getData() instanceof JSONObject){
					JSONObject data = (JSONObject)result.getData();
					JSONArray array = data.getJSONArray(this.getCollectionName());
					if(array != null && !array.isEmpty()){
						//单个主条码,直接返回
						if(array.size() == 1){
							return result;
						}else{//多个主条码，排序后取更新时间最新的那条
							List<JSONObject> jsonList = new ArrayList<>();
							for (Object o : array) {
								jsonList.add((JSONObject) o);
							}
							List sortList = jsonList.stream().sorted(Comparator.comparing(obj -> ((JSONObject) obj).getDate("updateDate"),Comparator.reverseOrder())).collect(Collectors.toList());
							data.put(this.getCollectionName(),Arrays.asList(sortList.get(0)));
							data.put("total_results",1);
						}
					}else{
						//查询从条码
						paramsObject.remove("mainBarcodeFlag");
						result =  this.onQueryWithHint(session,paramsObject,true);
						if(result.getReturncode().equals(ResponseCode.SUCCESS) && result.getData() instanceof JSONObject){
							data = (JSONObject)result.getData();
							array = data.getJSONArray(this.getCollectionName());
							if(array != null && !array.isEmpty()){
								//单个从条码,直接返回
								if(array.size() == 1){
									return result;
								}else{//多个从条码，排序后取更新时间最新的那条
									List<JSONObject> jsonList = new ArrayList<>();
									for (Object o : array) {
										jsonList.add((JSONObject) o);
									}
									List sortList = jsonList.stream().sorted(Comparator.comparing(obj -> ((JSONObject) obj).getDate("updateDate"),Comparator.reverseOrder())).collect(Collectors.toList());
									data.put(this.getCollectionName(),Arrays.asList(sortList.get(0)));
									data.put("total_results",1);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	//通过key查询JSON中value值
	public static Set searchJSONValueByKey(JSONObject data, String searchKey){
		Set result = new HashSet<>();
		Set<String> keys = data.keySet();
		keys.stream().forEach(key->{
			Object value = data.get(key);
			if(value instanceof JSONObject){
				JSONObject valueJsonObject = (JSONObject) value;
				result.addAll(searchJSONValueByKey(valueJsonObject,searchKey));
			}else if(value instanceof JSONArray){
				JSONArray jsonArray = (JSONArray) value;
				if(jsonArray.size() == 0){
					return;
				}
				searchJSONValueByKeyJSONArray(jsonArray,result,searchKey);
			}else{
				if(searchKey.equals(key)){
					//System.out.println("jsonObject = " + jsonObject);
					result.add(data.get(key));
				}
			}
		});
		return result;
	}

	public static void searchJSONValueByKeyJSONArray(JSONArray jsonArray,Set result,String searchKey){
		jsonArray.stream().forEach(json->{
			if(json instanceof JSONObject){
				JSONObject valueJsonObject= (JSONObject) json;
				result.addAll(searchJSONValueByKey(valueJsonObject,searchKey));
			}else if(json instanceof JSONArray){
				JSONArray tmpJsonArray = (JSONArray) json;
				if(tmpJsonArray.size() == 0){
					return;
				}
				searchJSONValueByKeyJSONArray(tmpJsonArray,result,searchKey);
			}
		});
	}
}
