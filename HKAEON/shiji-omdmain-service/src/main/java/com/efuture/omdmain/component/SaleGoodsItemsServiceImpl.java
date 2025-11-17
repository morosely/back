package com.efuture.omdmain.component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.SaleGoodsItemsModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.model.SetMealTypeRefModel;
import com.efuture.omdmain.service.SaleGoodsItemsService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class SaleGoodsItemsServiceImpl extends JDBCCompomentServiceImpl<SaleGoodsItemsModel>implements SaleGoodsItemsService {

	public SaleGoodsItemsServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	//（组包码查询接口 香港专用）香港永旺组包码当箱码
	public ServiceResponse searchDetail(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","shopCode","gsgid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//2.查询
		Criteria criteria = Criteria.where("gsgid").is(paramsObject.get("gsgid"));
		Query query = new Query(criteria);
		//箱码和单品
		SaleGoodsItemsModel item = this.getTemplate().selectOne(query,SaleGoodsItemsModel.class,"salegoodsitems");
		if(item == null) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "没有查询到箱码记录！gsgid:"+paramsObject.get("gsgid"));
		
		criteria = Criteria.where("erpCode").is(paramsObject.get("erpCode")).and("shopCode")
				.is(paramsObject.get("shopCode")).and("entId").is(session.getEnt_id()).and("goodsCode").is(item.getGoodsCode())
				.and("barNo").is(item.getBarNo());
		query = new Query(criteria);
		
		SaleGoodsModel saleGoods = this.getTemplate().selectOne(query,SaleGoodsModel.class,"salegoods");
		if(saleGoods!=null){
			item.setGoodsName(saleGoods.getGoodsName());
			item.setSalePrice(saleGoods.getSalePrice());
		}
		//3.封装数据
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(),Arrays.asList(item));
		paramsObject.put("total_results",1);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//组包码查询接口
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse saleGoodsItems = this.onQuery(session, paramsObject);// 商品组成表
		JSONObject saleGoodsItemsData = (JSONObject) saleGoodsItems.getData();
		JSONArray saleGoodsItemsList = saleGoodsItemsData.getJSONArray(this.getCollectionName());
		if (saleGoodsItemsList.size() > 0) {
			DefaultParametersUtils.numberFormat(saleGoodsItemsList, "#0.00", "salePrice");
			int size = saleGoodsItemsList.size();
			// 页面展示需要
			for (int i = 0; i < size; i++) {
				JSONObject json1 = (JSONObject) saleGoodsItemsList.get(i);

				if (!StringUtils.isEmpty(json1.get("discountShareRate"))) {
					Float discountShareRate = Float.parseFloat(json1.get("discountShareRate").toString()); // 折扣分摊比例
					DecimalFormat decimalFormat = new DecimalFormat("##.##");
					String p = decimalFormat.format(100 * discountShareRate);
					json1.put("discountShareRate", p);
				}
			}
			saleGoodsItemsData.put(this.getCollectionName(), saleGoodsItemsList);
		}
		return saleGoodsItems;
	}

	public ServiceResponse menuInsert(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);

		if (!paramsObject.containsKey("gsgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_EMPTY, "gsgid主键值为空");
		}

		if (!paramsObject.containsKey("ggoodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_EMPTY, "goodsCode组商码为空");
		}
		JSONArray returnarray = new JSONArray();
		if (paramsObject.containsKey(this.getCollectionName())) {
			Object dataObject = paramsObject.get(this.getCollectionName());
			if (dataObject != null && dataObject instanceof List) {
				JSONArray paramsArray = paramsObject.getJSONArray(this.getCollectionName());
				for (int i = 0; i < paramsArray.size(); i++) {
					JSONObject params = paramsArray.getJSONObject(i);
					params.put("gsgid", paramsObject.get("gsgid"));
					params.put("ggoodsCode", paramsObject.get("ggoodsCode"));
					// params.put("goodsType", paramsObject.get("goodsType"));
					ServiceResponse result = super.onInsert(session, params);
					if (!result.getReturncode().equals(ResponseCode.SUCCESS)) {
						return ServiceResponse.buildFailure(session, ResponseCode.Failure.FAIL_INSERT, "gsgid主键值为空");
					} else {
						returnarray.add(result.getData());
					}
				}
			}
		}

		return ServiceResponse.buildSuccess(returnarray);
	}

	@Override
	public ServiceResponse searchAllMealDetail(ServiceSession session, JSONObject paramsObject) {
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);

		List<Map<String, Object>> detailList = null;
		List<SetMealTypeRefModel> typeMealRefList = null;

		ServiceResponse response = super.onQuery(session, paramsObject);
		if (response.getReturncode() != null && response.getReturncode().equals("0")) {
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			List<SaleGoodsModel> saleGoodsList = JSONArray
					.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), SaleGoodsModel.class);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (SaleGoodsModel saleGoods : saleGoodsList) {
				Map<String, Object> saleGoodsMap = new HashMap<String, Object>();// 套餐表头
				saleGoodsMap.put("ssgid", saleGoods.getSsgid());
				saleGoodsMap.put("shopId", saleGoods.getShopId());
				saleGoodsMap.put("shopCode", saleGoods.getShopCode());
				saleGoodsMap.put("tcCode", saleGoods.getGoodsCode());
				saleGoodsMap.put("tcName", saleGoods.getGoodsName());
				saleGoodsMap.put("tcDisplayName", saleGoods.getGoodsDisplayName());
				saleGoodsMap.put("tcSalePrice", saleGoods.getSalePrice());
				resultList.add(saleGoodsMap);

				JSONObject paramsObj = new JSONObject();
				paramsObj.put("ssgid", saleGoods.getSsgid());
				detailList = template.getSqlSessionTemplate()
						.selectList("beanmapper.SetMealDetailModelMapper.selectByState", paramsObj);
				typeMealRefList = template.getSqlSessionTemplate()
						.selectList("beanmapper.SetMealTypeRefModelMapper.selectByState", paramsObj);

				boolean isSame = false;
				String lastTypeName = null;
				for (SetMealTypeRefModel mealRef : typeMealRefList) {
					String typeName = (String) mealRef.getTypeName();
					if (typeName != null && typeName.equals(lastTypeName)) {
						isSame = true;
					}
					if (!isSame) {
						saleGoodsMap = new HashMap<String, Object>(); // 类型表头
						saleGoodsMap.put("smtrid", mealRef.getSmtrid()); // 档口套餐种类关联ID
						saleGoodsMap.put("typeName", mealRef.getTypeName()); // 种类名称
						saleGoodsMap.put("optionNum", mealRef.getOptionNum()); // 可选数量
						resultList.add(saleGoodsMap);
						lastTypeName = typeName;
					}
					for (Map<String, Object> detail : detailList) {
						if (typeName.equals((String) detail.get("typeName"))) {
							saleGoodsMap = new HashMap<String, Object>();// 商品明细
							saleGoodsMap.put("smdid", detail.get("smdid"));
							saleGoodsMap.put("sgoodsId", detail.get("sgoodsId"));
							saleGoodsMap.put("sgoodsCode", detail.get("sgoodsCode"));
							saleGoodsMap.put("goodsCode", detail.get("goodsCode"));
							saleGoodsMap.put("goodsId", detail.get("goodsId"));
							saleGoodsMap.put("goodsName", detail.get("goodsName"));
							saleGoodsMap.put("goodsDisplayName", detail.get("goodsDisplayName"));
							saleGoodsMap.put("salePrice", detail.get("salePrice"));
							saleGoodsMap.put("barNo", detail.get("barNo"));
							saleGoodsMap.put("skuCode", detail.get("skuCode"));
							saleGoodsMap.put("fullName", detail.get("fullName"));
							saleGoodsMap.put("enSname", detail.get("enSname"));
							saleGoodsMap.put("enFname", detail.get("enFname"));
							resultList.add(saleGoodsMap);
						}
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

}
