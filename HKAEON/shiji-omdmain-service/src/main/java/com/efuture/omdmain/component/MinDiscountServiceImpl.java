package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.MinDiscountModel;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class MinDiscountServiceImpl extends CommonServiceImpl<MinDiscountModel, MinDiscountServiceImpl>{

	public MinDiscountServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}
	private static final Logger logger = LoggerFactory.getLogger(MinDiscountServiceImpl.class);
	private static final Integer MAX_COUNT = 20000;

	//查询折扣
	public ServiceResponse queryMinDiscount(ServiceSession session, JSONObject paramsObject) throws Exception{
		//1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","categoryCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//2.查询返回结果
		return this.onQuery(session, paramsObject);
	}

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	public final static String ClassCodeKey = "omdmain:sync:minDiscount:classCode";
	public final static String ArtiCodeKey ="omdmain:sync:minDiscount:artiCode";
	//最低折扣保存
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse saveMinDiscount(ServiceSession session, JSONObject paramsObject) throws Exception{
		logger.info("【saveMinDiscount】=====>>> 请求入参:【{}】",paramsObject);
		//1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","level","minDiscount","categoryCode","refresh");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

		String erpCode = paramsObject.getString("erpCode");
		String level = paramsObject.getString("level");
		String minDiscount = paramsObject.getString("minDiscount");
		String refresh = paramsObject.getString("refresh");
		JSONArray categoryCodes = paramsObject.getJSONArray("categoryCode");

		//校验数量,：避免更新数量太大，对服务器造成压力
		//按artCode更新，每次只允许选择一个
		if("4".equals(level) && categoryCodes != null && categoryCodes.size() > 1){
			//return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "按Artcode設置折扣率，每次只能選擇一個Artcode");
			paramsObject.clear();
			paramsObject.put("message","按Artcode設置折扣率，每次只能選擇一個Artcode");
			paramsObject.put("errorFlag",true);
			return ServiceResponse.buildSuccess(paramsObject);
			//按classCode更新，只选择一个classCode（无论数据多少）放行。如果选择多个classCode，统计对应的商品数量，超过MAX_COUNT,提示减少classCode
		}else if("5".equals(level) && categoryCodes != null && categoryCodes.size() > 1){
			int count = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.MinDiscountModelMapper.countGoodsByClassCode",paramsObject);
			logger.info("【saveMinDiscount】=====>>> ClassCode{}的对应的待更新商品数量count:【{}】",categoryCodes,count);
			if(count > MAX_COUNT){
				//return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, String.format("按Class設置折扣率，您選擇的Class對應待更新的商品數量【%d】過大，請減少Class數量再重試！建議待更新商品數量控製在【%d】範圍內",count,MAX_COUNT));
				paramsObject.clear();
				paramsObject.put("message",String.format("按Class設置折扣率，您選擇的Class對應待更新的商品數量【%d】過大，請減少Class數量再重試！建議待更新商品數量控製在【%d】範圍內",count,MAX_COUNT));
				paramsObject.put("errorFlag",true);
				return ServiceResponse.buildSuccess(paramsObject);
			}
		}

		//2.循环处理折扣
		if(categoryCodes != null && !categoryCodes.isEmpty()) {
			for (Iterator iterator = categoryCodes.iterator(); iterator.hasNext();) {
				Object categoryCode = (Object) iterator.next();
				paramsObject.put("categoryCode", categoryCode);
				singleMinDiscount(session, paramsObject);//该方法修改paramsObject的原始参数，所以循环时页面入参临时保存，参数需要重设
				if(iterator.hasNext()) {
					paramsObject.clear();
					paramsObject.put("erpCode", erpCode);
					paramsObject.put("level", level);
					paramsObject.put("minDiscount", minDiscount);
					paramsObject.put("refresh", refresh);
				}
			}
		}
		paramsObject.clear();
		paramsObject.put("message","保存成功");
		paramsObject.put("errorFlag",false);
		stringRedisTemplate.delete(ClassCodeKey);
		stringRedisTemplate.delete(ArtiCodeKey);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	public void singleMinDiscount(ServiceSession session, JSONObject paramsObject) throws Exception{
		//2.判断ArtiCode（level = 4 ）还是ClassCode（level = 5 ）
		String level = paramsObject.getString("level").trim();
		String categoryCode = paramsObject.getString("categoryCode");
		Float newMinDiscount = paramsObject.getFloat("minDiscount");
		String refresh = paramsObject.getString("refresh");
		//2.1页面设置ArtiCode进行设置
		if("4".equals(level) && categoryCode != null){
			Float oldArticodeMinDiscount = 0f;
			//2.1.1.设置artiCode
			Criteria criteria = Criteria.where("level").is(level).and("categoryCode").is(categoryCode);
			Query query = new Query(criteria);
			MinDiscountModel oldArtiCodeMinDis = this.getTemplate().selectOne(query, MinDiscountModel.class, "minDiscount");
			paramsObject.put("entId",session.getEnt_id());
			if(oldArtiCodeMinDis == null){//插入操作。首次操作
				this.onInsert(session, paramsObject);
			}else{//更新操作
				oldArticodeMinDiscount = oldArtiCodeMinDis.getMinDiscount();
				if(Float.compare(newMinDiscount,oldArticodeMinDiscount) != 0){
					//更新ArtiCode
					int updateMinDiscount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateMinDiscount",paramsObject);
					logger.info("【saveMinDiscount artiCode】=====>>> mindiscount 更新数量:【{}】",updateMinDiscount);
				}
			}
			//2.1.2.设置artiCode下的ClassCode
			paramsObject.put("parentCode", categoryCode);
			//Left Join查询结果分析判断：折扣是新增还是更新
			List<Map> classCategoryList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.MinDiscountModelMapper.selectClassDisc",paramsObject);
			List<Map> addList = null,updateList = null;
			for (int i = 0; i < classCategoryList.size(); i++) {
				Map categoryMap = (Map)classCategoryList.get(i);
				if(categoryMap.get("minDiscCategoryCode") == null){
					addList = addList == null ? new ArrayList() : addList;
					categoryMap.put("minDiscount",newMinDiscount);//设置最新折扣
					addList.add(categoryMap);
				}else{
					updateList = updateList == null ? new ArrayList() : updateList;
					if("1".equals(refresh)){//强制刷新
						categoryMap.put("minDiscount",newMinDiscount);//设置最新折扣
						updateList.add(categoryMap);
					}else{
						//如果ClassCode的折扣与先前的ArtiCode相同时，才去更新新的折扣
						if(Float.compare((Float)categoryMap.get("minDiscount"), oldArticodeMinDiscount) == 0){
							categoryMap.put("minDiscount",newMinDiscount);//设置最新折扣
							updateList.add(categoryMap);
						}
					}
				}
			}
			//处理新增数据
			if(addList !=null && !addList.isEmpty()){
				paramsObject.put(this.getCollectionName(), addList);
				this.onInsert(session, paramsObject);
			}
			//处理更新数据
			if(updateList !=null && !updateList.isEmpty()){
				paramsObject.put(this.getCollectionName(), updateList);
				this.onUpdate(session, paramsObject);
			}
			//2.1.3.设置ArtiCode对应的所有ItemCode
			if(classCategoryList!=null && !classCategoryList.isEmpty()){
				List classCodes = (List) classCategoryList.stream().map(map->map.get("categoryCode")).collect(Collectors.toList());
				//paramsObject.put("classCodes", classCodes);
				paramsObject.put("minDiscount", newMinDiscount);
				paramsObject.put("oldMinDiscount", oldArticodeMinDiscount);
				paramsObject.put("updateDate", new Date());
				paramsObject.put("modifier", session.getUser_code());
				//批量操作锁表，优化单个操作 add by yihaitao 2022-11-22
				//this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateItemCodeFromClassCode",paramsObject);
				//this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateSaleItemCodeFromClassCode",paramsObject);
				for(Object classCode : classCodes){
					paramsObject.put("classCode", classCode);
					int goodsCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateGoodsMinDisc",paramsObject);
					logger.info("【saveMinDiscount artCode】=====>>> 【{}】品类更新goods数量:【{}】",classCode,goodsCount);
					int saleGoodsCount =  this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateSaleGoodsMinDisc",paramsObject);
					logger.info("【saveMinDiscount artCode】=====>>> 【{}】品类更新salegoods数量:【{}】",classCode,saleGoodsCount);
				}
			}
			
		//2.2页面设置ClassCode进行设置	
		}else if("5".equals(level)){
			Float oldClassCodeMinDiscount = 0f;
			//2.2.1 设置ClassCode
			Criteria criteria = Criteria.where("level").is(level).and("categoryCode").is(categoryCode);
			Query query = new Query(criteria);
			MinDiscountModel oldClassCodeMinDis = this.getTemplate().selectOne(query, MinDiscountModel.class, "minDiscount");
			paramsObject.put("entId",session.getEnt_id());
			if(oldClassCodeMinDis == null){//插入操作。首次操作
				this.onInsert(session, paramsObject);
			}else{//更新操作
				oldClassCodeMinDiscount = oldClassCodeMinDis.getMinDiscount();
				if(Float.compare(newMinDiscount,oldClassCodeMinDiscount) != 0){
					//更新ClassCode
					int updateMinDiscount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateMinDiscount",paramsObject);
					logger.info("【saveMinDiscount classCode】=====>>> mindiscount 更新数量:【{}】",updateMinDiscount);
				}
			}
			//2.2.2 设置ClassCode对应的ItemCode
			//paramsObject.put("classCodes", Arrays.asList(categoryCode));
			paramsObject.put("minDiscount", newMinDiscount);
			paramsObject.put("oldMinDiscount", oldClassCodeMinDiscount);
			paramsObject.put("updateDate", new Date());
			paramsObject.put("modifier", session.getUser_code());
			//批量操作锁表，优化单个操作 add by yihaitao 2022-11-22
			//this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateItemCodeFromClassCode",paramsObject);
			//this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateSaleItemCodeFromClassCode",paramsObject);
			for(Object classCode : Arrays.asList(categoryCode)){
				paramsObject.put("classCode", classCode);
				int goodsCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateGoodsMinDisc",paramsObject);
				logger.info("【saveMinDiscount classCode】=====>>> 【{}】品类更新goods数量:【{}】",classCode,goodsCount);
				int saleGoodsCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.MinDiscountModelMapper.updateSaleGoodsMinDisc",paramsObject);
				logger.info("【saveMinDiscount classCode】=====>>> 【{}】品类更新salegoods数量:【{}】",classCode,saleGoodsCount);
			}
		}
	}

}
