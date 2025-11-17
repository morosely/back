package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.service.ColdTransSaleGoodsService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.SpringContextUtil;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/**
 * 冷藏商品设置Service
 * @author chenp
 *
 */
public class ColdTransSaleGoodsServiceImpl extends JDBCCompomentServiceImpl<GoodsModel> implements ColdTransSaleGoodsService{

	public ColdTransSaleGoodsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
	
	@Autowired
	private SaleGoodsServiceImpl saleGoodsServiceImpl;
	
	// 查询商品列表
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
//		return this.onQuery(session, paramsObject);
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		
		ServiceResponse goods = this.onQuery(session, paramsObject);// 商品基础表
		
		if (!goods.getReturncode().equals(ResponseCode.SUCCESS)) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询失败");
		}
		
		JSONObject goodsData = (JSONObject) goods.getData();
		JSONArray goodsList = goodsData.getJSONArray(this.getCollectionName());
		if(goodsList.size() > 0){
			DefaultParametersUtils.numberFormat(goodsList, "#0.00", "salePrice");
			goodsData.put(this.getCollectionName(), goodsList);
		}
		return goods;
	}
	
	/**
	 * 是否设为冷藏(批量)
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
	public ServiceResponse doColdset(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if (!paramsObject.containsKey("coldTransFlag")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "coldTransFlag不能为空");
		}
		
	    Boolean coldTransFlag = paramsObject.getBoolean("coldTransFlag");
	    //统一插入时间：保持多个表的插入时间是一致性
	    String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    
		this.setUpsert(false);
		Map<String,Object> returnMap = new HashMap<>();
		
		JSONArray paramList = paramsObject.getJSONArray("goods");
		Iterator<Object> it = paramList.iterator();
		while(it.hasNext()){
			JSONObject obj = (JSONObject)it.next();
			obj.put("coldTransFlag", coldTransFlag);
			obj.put("modifier", session.getUser_name());
			obj.put("updateDate", updateDateString);
		}
		JSONObject goodsparamMap = new JSONObject();
		goodsparamMap.put("goods", paramList);
		// 修改goods
		ServiceResponse goodsUpdate = super.onUpdate(session, goodsparamMap);
		if(!ResponseCode.SUCCESS.equals(goodsUpdate.getReturncode())){
			throw new Exception(goodsUpdate.getData().toString());
		}
		returnMap.put(this.getCollectionName(),goodsUpdate.getData());
		
		List<JSONObject> saleGoodsList = new ArrayList<>();
		
		for (int i = 0; i < paramList.size(); i++) {
			JSONObject dataMap = paramList.getJSONObject(i);
			Long sgid = dataMap.getLong("sgid");

//			JSONObject saleGoodsparamMap = new JSONObject();
//			saleGoodsparamMap.put("sgid", sgid);
//			saleGoodsparamMap.put("order_field", "sgid");// 排序字段
//			saleGoodsparamMap.put("order_direction", "desc");// 排序方法
//			ServiceResponse ss = saleGoodsServiceImpl.onQuery(session, saleGoodsparamMap);
//			JSONObject ssData = (JSONObject) ss.getData();
			
			Long entId = session.getEnt_id();
			Criteria criteria1 = Criteria.where("entId").is(entId).and("sgid").is(sgid);
			Query query1 = new Query(criteria1);
			List<SaleGoodsModel> saleGoodsList1 = this.getTemplate().select(query1, SaleGoodsModel.class, "saleGoods");
			JSONObject param = new JSONObject();
			long total_results = saleGoodsList1.size();
			param.put("saleGoods",saleGoodsList1);
			param.put("total_results",total_results);
			
			JSONArray dataList1 = param.getJSONArray("saleGoods");
//			JSONArray dataList = ssData.getJSONArray("saleGoods");
			for (Object json : dataList1) {
				JSONObject saleGoodsparam = (JSONObject) json;
				saleGoodsparam.put("coldTransFlag", coldTransFlag);
				saleGoodsparam.put("updateDate", updateDateString);
				saleGoodsList.add(saleGoodsparam);
				// saleGoodsServiceImpl.onUpdate(session, saleGoodsparam); // 修改saleGoods表
			}
		}
		
		JSONObject paramMap = new JSONObject();
		paramMap.put(saleGoodsServiceImpl.getCollectionName(), saleGoodsList);
		// 更新salegoods表
		ServiceResponse response1 = saleGoodsServiceImpl.onUpdate(session, paramMap);
		if(!ResponseCode.SUCCESS.equals(response1.getReturncode())){
			throw new Exception(response1.getData().toString());
		}
		returnMap.put(saleGoodsServiceImpl.getCollectionName(),response1.getData());
		
		return ServiceResponse.buildSuccess(returnMap);
	}
	
	//封装事务回滚的异常信息返回前台
	public ServiceResponse coldset(ServiceSession session, JSONObject paramsObject){
		ServiceResponse response = null;
		try {
			ColdTransSaleGoodsServiceImpl proxyService = ((ColdTransSaleGoodsServiceImpl)SpringContextUtil.getBean(this.getClass()));
			response = proxyService.doColdset(session,paramsObject);
		} catch (Exception e) {
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,e.getMessage());
		}
		return response;
	}

}
