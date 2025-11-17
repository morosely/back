package com.efuture.omdmain.component;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.omdmain.common.GoodsType;
import com.efuture.omdmain.common.MapResultHandler;
import com.efuture.omdmain.model.*;
import com.efuture.omdmain.service.SaleGoodsService;
import com.efuture.omdmain.utils.DateUtils;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.JSONSerializeUtill;
import com.mongodb.DBObject;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.*;
import com.product.service.AnnotationService;
import com.product.service.OperationFlag;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.product.util.UniqueID;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import static com.efuture.omdmain.utils.DefaultParametersUtils.transformParam;
import static com.product.util.ParamValidateUtil.checkParam;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * 商品销售表
 * 
 * @author Administrator
 *
 */
public class SaleGoodsServiceImpl extends CommonServiceImpl<SaleGoodsModel,SaleGoodsServiceImpl> implements SaleGoodsService {

	private static final Logger logger = LoggerFactory.getLogger(SaleGoodsServiceImpl.class);
	@Autowired
	private GoodsServiceImpl goodsServiceImpl;
	@Autowired
	private ExtGoodsServiceImpl extGoodsServiceImpl;
	@Autowired
	private GoodsShopRefServiceImpl goodsShopRefServiceImpl;
	@Autowired
	private ExtGoodsShopRefServiceImpl extGoodsShopRefServiceImpl;
	@Autowired
	private AeonMoreBarNoServiceImpl aeonMoreBarNoServiceImpl;
	@Autowired
	private ExtAeonMoreBarNoServiceImpl extAeonMoreBarNoServiceImpl;
	@Autowired
	private GoodsUpAndDownServiceImpl goodsUpAndDownService;
	@Autowired
	private SaleGoodsItemsServiceImpl saleGoodsItemsService;
	@Autowired
	private SaleGoodsService2Impl saleGoodsService2Impl;
	@Autowired
	private ShopServiceImpl shopService;
	@Autowired
	private StallInfoServiceImpl stallInfoService;
	@Autowired
	private ChannelInfoServiceImpl channelInfoService;
	@Autowired
	private CategoryServiceImpl categoryService;
	@Autowired
	private CategoryPropertyServiceImpl categoryPropertyServiceImpl;
	@Autowired
	private PackageAttCateService packageAttCateService;
	@Autowired
	private PackageAttDictService packageAttDictService;
	@Autowired
	private PackageAttShopGoodsRefService packageAttShopGoodsRefService;
	@Autowired
	private SaleGoodsPropertyService saleGoodsPropertyService;

	public SaleGoodsServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	//档口复制商品选择框 add by yihaitao 2025-01-02
	public ServiceResponse searchSetMealCopyList(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse response = this.onQuery(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			logger.error(Thread.currentThread().getStackTrace()[1].getMethodName() + " --- "+response.getData());
		}
		Map<String,List<RowMap>> map = (Map<String,List<RowMap>>)response.getData();
		List<RowMap> rowMapList = map.get(this.getCollectionName());

		if(rowMapList !=null && !rowMapList.isEmpty()){
			List<JSONObject> setMeals = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SetMealDetailModelMapper.searchGoods",new JSONObject(){{
				put("shopId",paramsObject.getLong("shopId"));
				put("goodsCode",rowMapList.stream().map(rowMap -> rowMap.get("goodsCode")).collect(Collectors.toSet()));
			}});
			if(setMeals!=null && !setMeals.isEmpty()){
				Map<String,Integer> goodsSetMealMap = setMeals.stream().collect(Collectors.toMap(p -> ((JSONObject)p).getString("goodsCode"), p -> ((JSONObject)p).getInteger("num"),(value1,value2) ->{return value2;}));
				List<JSONObject> data = rowMapList.stream()
						.map(rowMap -> {
							JSONObject json = new JSONObject();
							json.put("ssgid", rowMap.get("ssgid"));
							json.put("goodsCode", rowMap.get("goodsCode"));
							json.put("barNo", rowMap.get("barNo"));
							json.put("goodsName", rowMap.get("goodsName"));
							json.put("salePrice", rowMap.get("salePrice"));
							json.put("shopCode", rowMap.get("shopCode"));
							json.put("stallCode", rowMap.get("stallCode"));
							Integer num = goodsSetMealMap.get(rowMap.get("goodsCode"));
							if(num!=null && num > 0){
								json.put("isSealMeat",1);
							}else{
								json.put("isSealMeat",0);
							}
							return json;
						})
						.collect(Collectors.toList());
				((JSONObject)response.getData()).put(this.getCollectionName(),data);
			}else{
				List<JSONObject> data = rowMapList.stream()
						.map(rowMap -> {
							JSONObject json = new JSONObject();
							json.put("ssgid", rowMap.get("ssgid"));
							json.put("goodsCode", rowMap.get("goodsCode"));
							json.put("barNo", rowMap.get("barNo"));
							json.put("goodsName", rowMap.get("goodsName"));
							json.put("salePrice", rowMap.get("salePrice"));
							json.put("shopCode", rowMap.get("shopCode"));
							json.put("stallCode", rowMap.get("stallCode"));
							json.put("isSealMeat",0);
							return json;
						})
						.collect(Collectors.toList());
				((JSONObject)response.getData()).put(this.getCollectionName(),data);
			}
		}
		return response;
	}

	//档口套餐设置(新)界面新增复制套餐内商品列表功能 add by yihaitao 2024-09-14
	public ServiceResponse copyData(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "fromSsgid","toSsgidList");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;
		JSONArray array = paramsObject.getJSONArray("toSsgidList");
		for(int i = 0; i < array.size(); i++){
			JSONObject param = (JSONObject)array.get(i);
			paramsObject.put("toSsgid",param.getString("toSsgid"));
			paramsObject.put("toGoodsCode",param.getString("toGoodsCode"));
			paramsObject.put("creator",session.getUser_code());
			int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.SetMealTypeRefModelMapper.deleteData", paramsObject);
			logger.info(" 1.==========> 【SaleGoodsServiceImpl - delete()】 setmealtypefef : {}",count);
			count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.SetMealTypeRefModelMapper.copyData", paramsObject);
			logger.info(" 2.==========> 【SaleGoodsServiceImpl - copyData()】 setmealtypefef : {}",count);
			count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.SetMealDetailModelMapper.deleteData", paramsObject);
			logger.info(" 3.==========> 【SaleGoodsServiceImpl - delete()】 setmealdetail : {}",count);
			count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.SetMealDetailModelMapper.copyData", paramsObject);
			logger.info(" 4.==========> 【SaleGoodsServiceImpl - copyData()】 setmealdetail : {}",count);
		}
		return ServiceResponse.buildSuccess("success");
	}

	//查询套餐明细(新) add by yihaitao 2024-06-12
	public ServiceResponse searchMealDetailNew(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "ssgid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;

		Long ssgid = paramsObject.getLong("ssgid");
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);

		// 目前只有主条码设置了套餐明细。如果是从条码的ssgid转换成主条码的ssgid进行查询套餐
		//paramsObject.put("searchPluginFlag", true);
		SaleGoodsModel saleGoods = this.wrapQueryBean(session,paramsObject);//检测是否是主条码
		if(saleGoods == null) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "查询的套餐商品不存在。参数:"+paramsObject);

		if(saleGoods.getMainBarcodeFlag() != null && !saleGoods.getMainBarcodeFlag()) {//如果是从条码：从条码转成主条码的ssgid
			SaleGoodsModel mainSaleGoods =  template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.slaveToMasterId",paramsObject);
			if(mainSaleGoods != null) {//如果主数据根据从条码没有查询到主条码（可能数据原因），就返回当前从条码
				saleGoods = mainSaleGoods;
			}
			ssgid = saleGoods.getSsgid();
		}

		paramsObject.put("ssgid", ssgid);
		List<Map<String, Object>> detailList = template.getSqlSessionTemplate().selectList("beanmapper.SetMealDetailModelMapper.selectByStateNew", paramsObject);
		List<Map<String, Object>> typeMealRef = template.getSqlSessionTemplate().selectList("beanmapper.SetMealTypeRefModelMapper.selectByState4Map", paramsObject);
		paramsObject.put("entId", session.getEnt_id());
		paramsObject.put("shopId", saleGoods.getShopId());

		// 获取明细商品的商品信息
		if(detailList!=null && !detailList.isEmpty()) {
		/*List<Long> categoryIds = new ArrayList<>();
			detailList.forEach(action -> {
				if (action.get("categoryId") != null) {
					categoryIds.add((Long) action.get("categoryId"));
				};
			});*/

			//List<CategoryPropertyModel> categoryProperty = this.getCategoryProperty(session, paramsObject,saleGoods.getEntId(), saleGoods.getErpCode(), categoryIds);
			//add by yihaitao 2024-06-07 添加商品的属性分类和属性值
			JSONArray attData = new JSONArray();
			List<PackageAttShopGoodsRef> attShopGoodsList;
			List<PackageAttCate> attCateList;
			List<PackageAttDict> dictList;
			Map<String,List<PackageAttShopGoodsRef>> attShopGoodsRefMap;
			Map<String,List<PackageAttCate>> attCateMap;
			Map<String,List<PackageAttDict>> dictMap;

			//1.查询packageattshopgoodsref门店商品属性分类信息
			JSONObject queryParam = new JSONObject();
			List<String> goodsCodeList = detailList.stream().map(action -> (String)action.get("goodsCode")).collect(toList());
			queryParam.put("goodsCode",goodsCodeList);
			queryParam.put("erpCode",saleGoods.getErpCode());
			queryParam.put("entId",saleGoods.getEntId());
			queryParam.put("shopCode",saleGoods.getShopCode());
			queryParam.put("stallCode",saleGoods.getStallCode());
			queryParam.put("page_size",Integer.MAX_VALUE);
			attShopGoodsList = packageAttShopGoodsRefService.wrapQueryBeanList(session,queryParam);
			//获取pCode集合
			Map<String,String> pCodeNameMap = attShopGoodsList.stream().collect(Collectors.toMap(PackageAttShopGoodsRef::getPCode,PackageAttShopGoodsRef::getPName,(key1 , key2)-> key2));//重复键对应的后值覆盖前值
			Map<String,String> attRefGoodsCodeMap = attShopGoodsList.stream().collect(Collectors.toMap(PackageAttShopGoodsRef::getGoodsCode,PackageAttShopGoodsRef::getPCode));

			//2.查询packageattcate获取下级数据
			if(pCodeNameMap != null && !pCodeNameMap.isEmpty()){
				pCodeNameMap.forEach((k,v) -> {
					JSONObject data = new JSONObject();
					data.put("pCode",k);
					data.put("pName",v);
					attData.add(data);
				});
				queryParam.clear();
				queryParam.put("level",3);
				queryParam.put("parentCode",attShopGoodsList.stream().map(action -> action.getPCode()).collect(Collectors.toSet()));
				queryParam.put("erpCode",saleGoods.getErpCode());
				queryParam.put("entId",saleGoods.getEntId());
				attCateList = packageAttCateService.wrapQueryBeanList(session,queryParam);

				//3.查询packageattdict数据
				if(attCateList!=null && !attCateList.isEmpty()){
					queryParam.clear();
					queryParam.put("pCode",attCateList.stream().map(action -> action.getPCode()).collect(toList()));
					queryParam.put("erpCode",saleGoods.getErpCode());
					queryParam.put("entId",saleGoods.getEntId());
					queryParam.put("order_field","orderNum");
					queryParam.put("order_direction","asc");
					dictList = packageAttDictService.wrapQueryBeanList(session,queryParam);

					if(dictList!=null && !dictList.isEmpty()){
						dictMap = dictList.stream().collect(Collectors.groupingBy(PackageAttDict::getPCode));
					} else {
						dictMap = null;
					}

					//4.数据挂载
					attCateMap = attCateList.stream().collect(Collectors.groupingBy(PackageAttCate::getParentCode));
					attData.forEach(obj -> {
						JSONObject jsonObj = (JSONObject) obj;
						String attCatePCode = jsonObj.getString("pCode");
						if(attCateMap.containsKey(attCatePCode)){
							JSONArray attCateArray = JSON.parseArray((JSONObject.toJSONString(attCateMap.get(attCatePCode), SerializerFeature.WriteDateUseDateFormat)));
							for (int i = 0; i < attCateArray.size(); i++) {
								JSONObject attCateJSON = attCateArray.getJSONObject(i);
								String pCode = attCateJSON.getString("pCode");
								if (dictMap != null && dictMap.containsKey(pCode)) {
									attCateJSON.put("attdic", JSON.parseArray((JSONObject.toJSONString(dictMap.get(pCode), SerializerFeature.WriteDateUseDateFormat))));
								}
							}
							jsonObj.put("attdetail", attCateArray);
						}
					});
				}
			}

			Map<String,List<Object>> attMap = attData.stream().collect(Collectors.groupingBy(item -> JSON.parseObject(item.toString()).getString("pCode")));
			paramsObject.clear();
			paramsObject.put("shopCode",saleGoods.getShopCode());
			paramsObject.put("stallCode",saleGoods.getStallCode());
			paramsObject.put("goodsCode",goodsCodeList);
			JSONObject dateParam = new JSONObject(){{put(">=", DateUtils.getCurrentDay());}};
			paramsObject.put("updateDate",dateParam);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<SaleGoodsProperty> properties = saleGoodsPropertyService.wrapQueryBeanList(session,paramsObject);
			Map<String,String> goodsCodeSelloutMap = properties.stream().collect(Collectors.toMap(SaleGoodsProperty::getGoodsCode,SaleGoodsProperty::getSellout));
			//循环详情集合，返回的数据赋值
			for (Map<String, Object> map : detailList) {
				String goodsNameD = map.get("goodsName") == null ? "" : map.get("goodsName").toString();
				String goodsDisplayNameD = map.get("goodsDisplayName") == null ? "" : map.get("goodsDisplayName").toString();
				String enFnameD = map.get("enFname") == null ? "" : map.get("enFname").toString();
				String enSnameD = map.get("enSname") == null ? "" : map.get("enSname").toString();
				map.put("goodsNameD", goodsNameD);
				map.put("goodsDisplayNameD", goodsDisplayNameD);
				map.put("enFnameD", enFnameD);
				map.put("enSnameD", enSnameD);

				// 将escaleFlag 转换成 1，0
				if (map.get("escaleFlag") == null || "".equals(map.get("escaleFlag").toString().trim())) {
					map.remove("escaleFlag");
				} else if (Boolean.parseBoolean(map.get("escaleFlag").toString())) {
					map.put("escaleFlag", 1);
				} else {
					map.put("escaleFlag", 0);
				}

				// 添加明细商品工业分类属性
				/* List<Map<String, Object>> subCategoryProperty = new ArrayList<>();
				Long tmpCategoryId = map.get("categoryId") == null ? null : Long.parseLong(map.get("categoryId").toString());
				if(categoryProperty!=null && !categoryProperty.isEmpty()) {
					for (int i = 0; i < categoryProperty.size(); i++) {
						CategoryPropertyModel c = categoryProperty.get(i);
						if (c.getCategoryId().longValue() == tmpCategoryId) {
							Map<String, Object> tmpCategoryProperty = new HashMap<>();
							tmpCategoryProperty.put("propertyName", c.getPropertyName());
							tmpCategoryProperty.put("propertyCode", c.getPropertyCode());
							subCategoryProperty.add(tmpCategoryProperty);
						}
					}
				}
				map.put("categoryProperty", subCategoryProperty);*/
				//增加属性分类 add by yihaitao 2024-06-13
				String goodsCode = map.get("goodsCode") == null ? "" : map.get("goodsCode").toString();
				if(attRefGoodsCodeMap.containsKey(goodsCode)){
					String pCode = attRefGoodsCodeMap.get(goodsCode);
					//logger.info("==========>>>>> goodsCode = {} , pCode = {} ",goodsCode , pCode);
					if(pCode!=null && !pCode.isEmpty()){
						List data = attMap.get(pCode);
						JSONObject attJSON = (JSONObject)data.get(0);
						map.putAll(attJSON);
					}
				}
				//增加售罄标识 add by yihaitao 2024-06-13
				map.put("sellout",goodsCodeSelloutMap.get(map.get("goodsCode")) == null || goodsCodeSelloutMap.isEmpty() ? 0 : goodsCodeSelloutMap.get(map.get("goodsCode")));
			}
		}

		// 整合套餐商品明细数据
		List<Map<String, Object>> transfer = new ArrayList<>();
		if(typeMealRef!=null && !typeMealRef.isEmpty()) {
			for (Map<String, Object> mealRef : typeMealRef) {
				Map<String, Object> map = new HashMap<>();
				map.put("smtrid", mealRef.get("smtrid")); // 档口套餐种类关联ID
				map.put("typeName", mealRef.get("typeName")); // 种类名称
				map.put("optionNum", mealRef.get("optionNum")); // 可选数量
				map.put("goodsCode", mealRef.get("goodsCode")); // 套餐编码
				map.put("goodsName", mealRef.get("goodsName"));

				List<Map<String, Object>> detail = new ArrayList<>();
				for (Map<String, Object> tmp : detailList) {
					String typeName = tmp.get("typeName") == null ? "" : tmp.get("typeName").toString();
					if (typeName.toString().equals(mealRef.get("typeName"))) {
						detail.add(tmp);
					}
				}
				//map.put("detail",detail);
				map.put("detail", JSONObject.parse(JSONArray.toJSONString(detail,SerializerFeature.DisableCircularReferenceDetect)));// 解决JSON循环引用$ref问题);
				transfer.add(map);
			}
		}
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), transfer);
		JSONObject saleJson =JSONObject.parseObject(JSON.toJSONString(saleGoods));
		if(saleJson.get("escaleFlag")!=null && saleJson.getBoolean("escaleFlag")) {
			saleJson.put("escaleFlag",1);
		}else {
			saleJson.put("escaleFlag",0);
		}
		result.put("sumInfo", saleJson);
		result.put("total_results", transfer.size());
		return ServiceResponse.buildSuccess(result);
	}

	// 保存套餐：香港永旺虚拟单品套餐明细保存（新）add by yihaitao 2024-06-08
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse saveMealNew(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.数据校验
		if (!paramsObject.containsKey("entity") || paramsObject.getJSONObject("entity").isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "套餐信息不能为空.");
		}
		JSONObject entity = paramsObject.getJSONObject("entity");
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, entity, "erpCode","shopId", "shopCode", "goodsName", "goodsCode", "salePrice", "categoryId", "categoryCode", "ssgid","stallCode", "siid");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))return result;
		Long mainSsgid = entity.getLong("ssgid");

		// 2.校验套餐编码的唯一性
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		String goodsCodeOld = entity.getString("goodsCode"); // 旧的套餐编码
		JSONArray detail = paramsObject.getJSONArray("detail"); // 明细允许为空
		List<SaleGoodsModel> insertSaleGoods = null;
		List<SaleGoodsModel> updateSaleGoods = null;
		List<SetMealDetailModel> setMealDetails = null;
		List<SetMealTypeRefModel> ref = null;
		List<JSONObject> attRefUpsertList = new ArrayList<>();//属性分类和门店商品关联关系
		JSONObject attRefDelete = new JSONObject();
		attRefDelete.put("shopCode",entity.getString("shopCode"));
		attRefDelete.put("stallCode",entity.getString("stallCode"));
		attRefDelete.put("erpCode",entity.getString("erpCode"));
		attRefDelete.put("entId",session.getEnt_id());
		attRefDelete.put("sGoodsCode",goodsCodeOld);

		if (detail != null) {
			insertSaleGoods = new ArrayList<SaleGoodsModel>();
			updateSaleGoods = new ArrayList<SaleGoodsModel>();
			Date now = new Date();
			setMealDetails = new ArrayList<>();
			ref = new ArrayList<>();
			for (int index = 0; index < detail.size(); index++) {
				JSONObject jsonObj = detail.getJSONObject(index);
				SetMealTypeRefModel refModel = new SetMealTypeRefModel();
				refModel.setSmtrid(UniqueID.getUniqueID(true));
				refModel.setGoodsId(mainSsgid);
				refModel.setGoodsCode(entity.getString("goodsCode"));
				refModel.setOptionNum(Integer.parseInt(jsonObj.getString("optionNum")));
				refModel.setTypeName(jsonObj.getString("typeName"));
				refModel.setShopId(entity.getLong("shopId"));
				refModel.setShopCode(entity.getString("shopCode"));
				refModel.setErpCode(entity.getString("erpCode"));
				refModel.setEntId(session.getEnt_id());
				refModel.setCreateDate(now);
				refModel.setCreator(session.getUser_code());
				refModel.setUpdateDate(now);
				refModel.setModifier(session.getUser_code());
				ref.add(refModel);

				JSONArray goodsInfoArray = jsonObj.getJSONArray("goodsInfoArray");
				for (int i = 0; goodsInfoArray != null && i < goodsInfoArray.size(); i++) {
					JSONObject tmp = goodsInfoArray.getJSONObject(i);
					// 设置属性分类和门店商品关联关系 add by yihaitao 2024-06-05
					String pCode = tmp.getString("pCode");
					if(pCode !=null && !pCode.trim().isEmpty()){
						JSONObject attRef = new JSONObject();
						attRef.put("goodsCode",tmp.getString("goodsCode"));
						attRef.put("barNo",tmp.getString("goodsCode"));//套餐商品的条码就是编码
						attRef.put("shopCode",entity.getString("shopCode"));
						attRef.put("stallCode",entity.getString("stallCode"));
						attRef.put("entId",session.getEnt_id());
						attRef.put("erpCode",entity.getString("erpCode"));
						attRef.put("pCode",pCode);
						attRef.put("sGoodsCode",goodsCodeOld);
						attRefUpsertList.add(attRef);
					}

					// 设置套餐明细参数
					SetMealDetailModel mealDetail = new SetMealDetailModel();
					mealDetail.setSmdid(UniqueID.getUniqueID(true));
					mealDetail.setGoodsCode(tmp.getString("goodsCode"));
					mealDetail.setTypeName(jsonObj.getString("typeName")); // 套餐种类名称
					mealDetail.setSgoodsId(mainSsgid); // 套餐商品ID
					mealDetail.setSgoodsCode(entity.getString("goodsCode")); // 套餐商品编码
					mealDetail.setErpCode(entity.getString("erpCode"));
					mealDetail.setShopId(entity.getLong("shopId"));
					mealDetail.setShopCode(entity.getString("shopCode"));
					mealDetail.setEntId(session.getEnt_id());
					mealDetail.setCreator(session.getUser_code());
					mealDetail.setCreateDate(now);
					mealDetail.setModifier(session.getUser_code());
					mealDetail.setUpdateDate(now);
					// 设置salegoods参数
					SaleGoodsModel saleGoods = new SaleGoodsModel();
					saleGoods.setCategoryId(entity.getLong("categoryId"));
					saleGoods.setCategoryCode(entity.getString("categoryCode"));
					saleGoods.setShopId(entity.getLong("shopId"));
					saleGoods.setShopCode(entity.getString("shopCode"));
					saleGoods.setSiid(entity.getLong("siid"));
					saleGoods.setStallCode(entity.getString("stallCode"));
					saleGoods.setGoodsType((short) 20);// 档口虚拟商品goodsType定义20
					saleGoods.setMainBarcodeFlag(true);
					saleGoods.setGoodsStatus((short) 1);
					saleGoods.setDirectFromErp(false);
					saleGoods.setSalePrice(tmp.getBigDecimal("salePrice"));
					saleGoods.setGoodsName(tmp.getString("goodsName"));
					saleGoods.setGoodsCode(tmp.getString("goodsCode"));
					saleGoods.setBarNo(tmp.getString("goodsCode"));
					saleGoods.setErpCode(entity.getString("erpCode"));
					saleGoods.setEntId(session.getEnt_id());
					saleGoods.setCreateDate(now);
					saleGoods.setUpdateDate(now);
					saleGoods.setParentGoodsCode(entity.getString("goodsCode"));

					Long saleGoodsId = tmp.getLong("goodsId");// 套餐明细表ID和可售商品ID一致
					if (StringUtils.isEmpty(saleGoodsId)) {// 表明新增的数据
						saleGoodsId = UniqueID.getUniqueID(true);
						saleGoods.setSsgid(saleGoodsId);
						saleGoods.setSgid(saleGoodsId);// 设置goodsId和saleGoodsId一致
						insertSaleGoods.add(saleGoods);
					} else {
						saleGoods.setSsgid(saleGoodsId);
						saleGoods.setSgid(saleGoodsId);// 设置goodsId和saleGoodsId一致
						updateSaleGoods.add(saleGoods);
					}
					mealDetail.setGoodsId(saleGoodsId);
					setMealDetails.add(mealDetail);
				}
			}
		}

		// 套餐种类（删除旧数据，重新插入新数据）
		if (!StringUtils.isEmpty(goodsCodeOld)) {
			List<Map<String, Object>> param = new ArrayList<>();
			Map<String, Object> paramMap = new HashMap<>();

			//（因新增套餐明细复制功能，废弃此更新虚拟商品为-1操作）更新套餐明细的虚拟商品状态-1
			/*
			paramMap.put("goodsCode", goodsCodeOld);
			paramMap.put("shopCode", entity.get("shopCode"));
			paramMap.put("entId", session.getEnt_id());
			paramMap.put("erpCode", entity.get("erpCode"));
			List<String> goodsCodeList = template.getSqlSessionTemplate().selectList("beanmapper.SetMealDetailModelMapper.selectGoodsCode20",paramMap);
			if(goodsCodeList!=null && !goodsCodeList.isEmpty()){
				paramMap.put("goodsCode",goodsCodeList);
				int updateCount = template.getSqlSessionTemplate().update("beanmapper.SetMealDetailModelMapper.updateStatus20",paramMap);
				logger.info("updateCount updateStatus20 ==========>>>>> {}",updateCount);
			}*/

			//删除旧的数据
			param.clear();
			paramMap.clear();
			paramMap.put("goodsCode", goodsCodeOld);
			paramMap.put("shopCode", entity.get("shopCode"));
			paramMap.put("entId", session.getEnt_id());
			paramMap.put("erpCode", entity.get("erpCode"));
			param.add(paramMap);
			template.getSqlSessionTemplate().delete("beanmapper.SetMealTypeRefModelMapper.deleteByGoodsCodeShopCode",param);

			param.clear();
			paramMap.clear();
			paramMap.put("sGoodsCode", goodsCodeOld);
			paramMap.put("shopCode", entity.get("shopCode"));
			paramMap.put("entId", session.getEnt_id());
			paramMap.put("erpCode", entity.get("erpCode"));
			param.add(paramMap);
			template.getSqlSessionTemplate().delete("beanmapper.SetMealDetailModelMapper.deleteBySGoodsCodeShopCode",param);
		}

		if (CollectionUtils.isNotEmpty(ref)) {
			template.getSqlSessionTemplate().insert("beanmapper.SetMealTypeRefModelMapper.batchInsert", ref);
		}
		if (CollectionUtils.isNotEmpty(setMealDetails)) {
			template.getSqlSessionTemplate().insert("beanmapper.SetMealDetailModelMapper.batchInsert", setMealDetails);
		}
		if (CollectionUtils.isNotEmpty(insertSaleGoods)) {
			template.getSqlSessionTemplate().insert("beanmapper.SaleGoodsModelMapper.batchInsert", insertSaleGoods);
			template.getSqlSessionTemplate().insert("beanmapper.SaleGoodsModelMapper.batchInsertBaDaTong",
					insertSaleGoods);
		}
		if (CollectionUtils.isNotEmpty(updateSaleGoods)) {
			template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.batchUpdate", updateSaleGoods);
			template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.batchUpdateBaDaTong",updateSaleGoods);
		}
		//设置属性分类和门店商品关联关系 add by yihaitao 2024-06-05
		int deleteCount = template.getSqlSessionTemplate().delete("beanmapper.PackageAttShopGoodsRefMapper.deleteSGoodsCode",attRefDelete);
		logger.info("=========> 【SaleGoodsServiceImpl - saveMealNew ---> PackageAttShopGoodsRefService - delete 】 deleteCount :{}",deleteCount);
		if(CollectionUtils.isNotEmpty(attRefUpsertList)){
			paramsObject.clear();
			paramsObject.put("saleGoods",attRefUpsertList);
			ServiceResponse refResponse = packageAttShopGoodsRefService.upsert(session,paramsObject);
			logger.info("=========> 【SaleGoodsServiceImpl - saveMealNew ---> PackageAttShopGoodsRefService - upsert 】Response:{}",refResponse == null ? null : refResponse.getData());
		}
		return ServiceResponse.buildSuccess("success");
	}

	//中台提供获取档口&前置地址接口 add by yihaitao 20204-05-21
	public ServiceResponse shopStallFront(ServiceSession session,JSONObject paramsObject) throws Exception {
		//1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,  "shopCode", "erpCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

		//2.查询前置地址
		String frontAddress = null;
		List<String> address = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryFrontAddress", paramsObject);
		logger.info("【callNoShopInfo】==========>>> 套餐叫号查询前置机地址 IP Address:{}",address);
		//3.查询门店的档口
		List<Map> stallList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryShopStall", paramsObject);
		//4.查询门店
		List<Map> shopList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryShop", paramsObject);

		//4.封装返回数据
		JSONObject data = new JSONObject();
		if(address!=null && !address.isEmpty()){
			frontAddress = address.get(0);
			data.put("frontAddress",frontAddress);
		}
		if(shopList!=null && !shopList.isEmpty()){
			Map shopInfo = shopList.get(0);
			data.put("shopCode",shopInfo.get("shopCode"));
			data.put("shopName",shopInfo.get("shopName"));
		}
		data.put("stallCodes",stallList);
		paramsObject.clear();
		paramsObject.put("shopStall",data);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//扩展查询Ext接口表
	public ServiceResponse extraExtInfo(ServiceSession session,JSONObject paramsObject) throws Exception {
		logger.info("【extraExtInfo】==========>>> 扩展EXT接口查询:请求参数:{}",paramsObject);
		//1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "goodsCode", "shopCode", "erpCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		String barNo = paramsObject.getString("barNo");
		if(!StringUtils.isEmpty(barNo)){
			result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "groupCode");
			if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		}
		paramsObject.put("page_no",paramsObject.getInteger("page_no") == null ? 1 : paramsObject.getInteger("page_no"));
		paramsObject.put("page_size",paramsObject.getInteger("page_size") == null ? Integer.MAX_VALUE : paramsObject.getInteger("page_size"));
		paramsObject.put("order_field","createDate");
		paramsObject.put("order_direction","desc");
		//2.查询EXT经营配置
		ServiceResponse extRefResp = extGoodsShopRefServiceImpl.onQuery(session,paramsObject);
		//3.查询EXT多条码
		ServiceResponse extBarNoResp = extAeonMoreBarNoServiceImpl.onQuery(session,paramsObject);
		//4.查询EXT商品
		if(!StringUtils.isEmpty(barNo)){
			paramsObject.remove("barNo");
		}
		ServiceResponse extGoodsResp= extGoodsServiceImpl.onQuery(session,paramsObject);
		//处理escaleFlag 和 controlFlag
		if(extGoodsResp.getData() != null && extGoodsResp.getData() instanceof JSONObject){
			List<RowMap> array = (List<RowMap>)((JSONObject)extGoodsResp.getData()).get(extGoodsServiceImpl.getCollectionName());
			if(array!=null && !array.isEmpty()){
				for(RowMap data : array){
					data.put("escaleFlag",data.get("escaleFlag") == null ? 0 : (boolean)data.get("escaleFlag")  ? 1 : 0);
					data.put("controlFlag",data.get("controlFlag") == null ? 0 : (boolean)data.get("controlFlag")  ? 1 : 0);
				}
			}
		}
		//5.封装返回EXT数据
		paramsObject.clear();
		paramsObject.put("extGoodsData",extGoodsResp.getData());
		paramsObject.put("extRefData",extRefResp.getData());
		paramsObject.put("extBarnoData",extBarNoResp.getData());
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//扩展查询
	public ServiceResponse extraInfo(ServiceSession session,JSONObject paramsObject) throws Exception {
		logger.info("【extraInfo】==========>>> 扩展查询:请求参数:{}",paramsObject);
		//1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "goodsCode", "shopCode", "erpCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		String barNo = paramsObject.getString("barNo");
		if(!StringUtils.isEmpty(barNo)){
			result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "groupCode");
			if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		}
		paramsObject.put("page_no",paramsObject.getInteger("page_no") == null ? 1 : paramsObject.getInteger("page_no"));
		paramsObject.put("page_size",paramsObject.getInteger("page_size") == null ? Integer.MAX_VALUE : paramsObject.getInteger("page_size"));
		//2.查询经营配置
		ServiceResponse refResp = goodsShopRefServiceImpl.onQuery(session,paramsObject);
		//3.查询多条码
		ServiceResponse barNoResp = aeonMoreBarNoServiceImpl.onQuery(session,paramsObject);
		//4.查询商品(goods表有barNo字段，排除barNo)
		if(!StringUtils.isEmpty(barNo)){
			paramsObject.remove("barNo");
		}
		ServiceResponse goodsResp= goodsServiceImpl.onQuery(session,paramsObject);
		//处理escaleFlag 和 controlFlag
		if(goodsResp.getData() != null && goodsResp.getData() instanceof JSONObject){
			List<RowMap> array = (List<RowMap>)((JSONObject)goodsResp.getData()).get(goodsServiceImpl.getCollectionName());
			if(array!=null && !array.isEmpty()){
				for(RowMap data : array){
					data.put("escaleFlag",data.get("escaleFlag") == null ? 0 : (boolean)data.get("escaleFlag")  ? 1 : 0);
					data.put("controlFlag",data.get("controlFlag") == null ? 0 : (boolean)data.get("controlFlag")  ? 1 : 0);
				}
			}
		}
		//5.封装返回数据
		paramsObject.clear();
		paramsObject.put(goodsServiceImpl.getCollectionName(),((JSONObject)goodsResp.getData()).getJSONArray(goodsServiceImpl.getCollectionName()));
		paramsObject.put(goodsShopRefServiceImpl.getCollectionName(),((JSONObject)refResp.getData()).getJSONArray(goodsShopRefServiceImpl.getCollectionName()));
		paramsObject.put(aeonMoreBarNoServiceImpl.getCollectionName(),((JSONObject)barNoResp.getData()).getJSONArray(aeonMoreBarNoServiceImpl.getCollectionName()));
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//门店异常商品查询（IDC) + 前置
	private static final Integer defaultTimeOut = 10000;
	public ServiceResponse exceptionGoods(ServiceSession session,JSONObject paramsObject) throws Exception{
		Long start = System.currentTimeMillis();
		//1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "goodsCode", "shopCode", "erpCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		JSONObject returnData = new JSONObject();
		String shopCode = paramsObject.getString("shopCode");
		paramsObject.put("page_no",paramsObject.getInteger("page_no") == null ? 1 : paramsObject.getInteger("page_no"));
		paramsObject.put("page_size",paramsObject.getInteger("page_size") == null ? Integer.MAX_VALUE : paramsObject.getInteger("page_size"));
		Integer timeout = paramsObject.getInteger("timeout") == null ? defaultTimeOut : paramsObject.getInteger("timeout");

		//2.查询前置机数据
		String frontAddress = null;//前置机地址数据库获取
		List<String> address = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryFrontAddress", paramsObject);
		logger.info("【exceptionGoods】1.==========>>> 门店异常商品查询:请求前置机地址 IP Address:{}",address);
		if(address != null && !address.isEmpty() && address.get(0).trim().length() > 0){
			frontAddress = address.get(0).replaceAll("8088","80");
			logger.info("【exceptionGoods】1.Replace Port 【8088->80】==========>>> 门店异常商品查询:请求前置机地址 IP Address 原地址:{},替换后:{}",address.get(0),frontAddress);
		}
		FutureTask<String> futureTask = null;
		ExecutorService executorService = null;
		//2.1 门店有前置机，多线程请求前置服务
		if(frontAddress != null) {
			String url = String.format("%sefuture-epos/amp-openapi-service/rest?ent_id=0&method=omdmain.salegoods.onQuery",frontAddress);
			//String url = "http://127.0.0.1:8156/rest?ent_id=0method=omdmain.salegoods.onQuery";
			logger.info("【exceptionGoods】2.==========>>> 门店:{}异常商品查询:请求前置机地址:{},请求参数:{}",shopCode,url,paramsObject);
			executorService = Executors.newSingleThreadExecutor();
			futureTask = new FutureTask<>(new Callable<String>() {
				@Override
				public String call() throws Exception {
					String frontResult = null;
					logger.info("【exceptionGoods】3.==========>>> 是否含有超时参数timeout:{}",timeout);
					if(timeout != null && timeout > 0){
						frontResult = HttpUtil.post(url, paramsObject.toString(),timeout);
					}else{
						frontResult = HttpUtil.post(url, paramsObject.toString());
					}
					return frontResult;
				}
			});
			executorService.execute(futureTask);
		}else{//门店没配置前置机，直接返回结果
			returnData.put("frontErrorMsg",String.format("門店:%s沒有配置前置機",shopCode));
			returnData.put("frontsalegoods",Arrays.asList());
		}

		//3.查询IDC
		ServiceResponse idcResponse = this.onQuery(session,paramsObject);
		//处理escaleFlag 和 controlFlag
		if(idcResponse.getData() != null && idcResponse.getData() instanceof JSONObject){
			List<RowMap> array = (List<RowMap>)((JSONObject)idcResponse.getData()).get(this.getCollectionName());
			if(array!=null && !array.isEmpty()){
				for(RowMap data : array){
					data.put("escaleFlag",data.get("escaleFlag") == null ? 0 : (boolean)data.get("escaleFlag")  ? 1 : 0);
					data.put("controlFlag",data.get("controlFlag") == null ? 0 : (boolean)data.get("controlFlag")  ? 1 : 0);
				}
			}
		}

		//4.封装返回数据
		//4.1处理IDC数据
		returnData.put("idcsalegoods",((JSONObject)idcResponse.getData()).getJSONArray(this.getCollectionName()));
		//4.2处理前置机数据
		if(frontAddress != null){
			try{
				String frontResult = futureTask.get();
				logger.info("【exceptionGoods】4.==========>>> 门店异常商品查询:前置查询返回结果{}",frontResult);
				if(frontResult != null){
					JSONObject frontResultJson = JSONObject.parseObject(frontResult);
					List<JSONObject> array = (List<JSONObject>)frontResultJson.getJSONObject("data").get(this.getCollectionName());
					//处理escaleFlag 和 controlFlag
					if(array!=null && !array.isEmpty()){
						for(JSONObject data : array){
							data.put("escaleFlag",data.get("escaleFlag") == null ? 0 : (boolean)data.get("escaleFlag")  ? 1 : 0);
							data.put("controlFlag",data.get("controlFlag") == null ? 0 : (boolean)data.get("controlFlag")  ? 1 : 0);
						}
					}
					returnData.put("frontsalegoods",frontResultJson.getJSONObject("data").getJSONArray(this.getCollectionName()));
				}else{
					returnData.put("frontsalegoods",Arrays.asList());
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("【exceptionGoods】5.==========>>> 门店异常商品查询:请求前置机发生异常！{}",e.getMessage());
				returnData.put("frontErrorMsg",String.format("查詢門店:%s前置機失敗！",shopCode));
				returnData.put("frontsalegoods",Arrays.asList());
			}finally {
				if(executorService!=null){
					executorService.shutdown();
				}
			}
		}
		Long end = System.currentTimeMillis();
		logger.info("【exceptionGoods】6.==========>>> 门店异常商品查询完毕！耗时：{}",(end-start));
		return ServiceResponse.buildSuccess(returnData);
	}

//	//门店异常商品查询（Front）
//	public ServiceResponse exceptionGoodsFront(ServiceSession session, JSONObject paramsObject) throws Exception {
//		//1.校验参数
//		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"goodsCode", "shopCode", "erpCode");
//		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
//		//2.查询前置机
//		ServiceResponse frontResponse = this.onQuery(session,paramsObject);
//		return ServiceResponse.buildSuccess(((JSONObject)frontResponse.getData()).getJSONArray(this.getCollectionName()));
//	}

	public ServiceResponse searcheSaleGoodsStatus(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

	// POS总部档口商品查询
	public ServiceResponse queryStallGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"codes", "shopCode", "erpCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		paramsObject.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		// 去除Json串中Value为空字符串的Key-Value键值对
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		List list = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.queryStallGoods", paramsObject);
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(), list);
		paramsObject.put("total_results", list == null ? 0 : list.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 档口套餐生成编码(档口虚拟商品goodsType定义20)
	public ServiceResponse generateStallCode(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"stallCode", "shopCode", "erpCode", "goodsName");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		paramsObject.put("entId", session.getEnt_id());
		paramsObject.put("goodsType", 20);

		// 2.校验名字是否重复
		paramsObject.put("searchPluginFlag", true);
		List<SaleGoodsModel> list = this.wrapQueryBeanList(session, paramsObject);
		if (list != null && !list.isEmpty()) {
			paramsObject.clear();
			paramsObject.put(this.getCollectionName(), list);
			paramsObject.put("exist", true);
			return ServiceResponse.buildSuccess(paramsObject);
		}

		// 3.生成虚拟母品编码:生成编码有可能与数据库编码重复。重复后重新生成编码再查询。
		// 最好不要死循环,循环10次终止即可。
		String goodsCode = null;
		int loopCount = 0;
		do {
			String id = Long.toString(UniqueID.getUniqueID(true));
			goodsCode = "9" + id.substring(id.length() - 8);
			paramsObject.clear();
			paramsObject.put("goodsCode", goodsCode);
			list = this.wrapQueryBeanList(session, paramsObject);
			if (list != null && list.isEmpty())
				break;
		} while (loopCount == 10);

		if (list != null && !list.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "生成档口虚拟编码失败,请重新尝试");
		} else {
			paramsObject.clear();
			paramsObject.put("exist", false);
			paramsObject.put("goodsCode", goodsCode);
			paramsObject.put("barNo", goodsCode);
			return ServiceResponse.buildSuccess(paramsObject);
		}
	}

	// 档口套餐商品删除商品列表的虚拟单品
	public ServiceResponse deleteSetMealDetailOne(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"goodsCode", "shopCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;
		this.getTemplate().getSqlSessionTemplate().delete("beanmapper.SaleGoodsModelMapper.deleteSetMealDetailOne",
				paramsObject);
		return ServiceResponse.buildSuccess("success");
	}

	// 档口套餐设置
	public ServiceResponse searchSetMeal(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

	// 一键收回
	public ServiceResponse saleGoodsRevokeAll(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"shopId", "channelId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;
		// 2.删除
		this.getTemplate().getSqlSessionTemplate().delete("beanmapper.SaleGoodsModelMapper.saleGoodsRevokeAll",
				paramsObject);
		return ServiceResponse.buildSuccess("删除成功");
	}

	// partyTray多门店查询商品
	public ServiceResponse partyTrayQueryGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"goodsCode", "shopIds", "erpCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;

		// 2.查询操作（主条码）
		paramsObject.put("entId", session.getEnt_id());
		List<SaleGoodsModel> list = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.partyTrayQueryGoods", paramsObject);
		if (list == null || list.isEmpty())
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "查询不到该商品编码");

		// 3.处理是否每个门店都有该商品
		List paramShopIds = paramsObject.getJSONArray("shopIds");
		List<String> dbShopIds = list.stream().map(e -> e.getShopId().toString()).collect(Collectors.toList());
		paramShopIds.removeAll(dbShopIds);
		if (paramShopIds == null || paramShopIds.isEmpty()) {// 所有门店都有该商品
			return ServiceResponse.buildSuccess(list.get(0));
		} else {// 部分门店有该商品
			Criteria criteria = Criteria.where("shopId").in(paramShopIds);
			Query query = new Query(criteria);
			Field fields = query.fields();
			fields.include("shopName");
			List<ShopModel> shopList = this.getTemplate().select(query, ShopModel.class, "shop");
			List<String> shopNameList = shopList.stream().map(e -> e.getShopName()).collect(Collectors.toList());
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, shopNameList + "门店查询不到该商品编码");
		}

	}

	// HGO微商城商品查询
	public ServiceResponse skuSearch(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"goodsCode", "shopCode", "erpCode", "channelId", "entId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;
		// 2.检查渠道是否有效状态
		JSONObject params = new JSONObject();
		params.put("channelId", paramsObject.get("channelId"));
		ChannelInfoModel model = channelInfoService.wrapQueryBean(session, params);
		if (model == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION,
					"主数数据没有查询到相关渠道信息:channelId:" + paramsObject.get("channelId"));
		} else if ("0".equals(model.getStatus())) {
			return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION,
					"主数据改渠道的状态是停用：" + model.getChannelName());
		}
		// 3.查询商品信息
		paramsObject.remove("channelId");
		List<SaleGoodsModel> saleGoodList = this.wrapQueryBeanList(session, paramsObject);
		if (saleGoodList == null || saleGoodList.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "主数据没有查询到可售商品");
		}
		// 去重
		List<SaleGoodsModel> cleanList = saleGoodList.stream()
				.collect(Collectors.collectingAndThen(
						Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f.getCategoryId()))),
						ArrayList<SaleGoodsModel>::new));

		// 4.查询商品的属性(通过商品的categoryCode,erpCode,entId查询属性表)
		paramsObject.put("table", "categoryproperty");
		paramsObject.put("key", "categoryId");
		List<Long> values = new ArrayList<Long>();
		for (SaleGoodsModel saleGoodsModel : cleanList) {
			values.add(saleGoodsModel.getCategoryId());
		}
		paramsObject.put("values", values);
		List propertyList = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.AdvancedQueryMapper.selectModel", paramsObject);
		// 数据解析（分组统计）key：categoryId
		Map<Long, JSONArray> attributeMap = null;
		if (propertyList != null && !propertyList.isEmpty()) {
			attributeMap = new HashMap<Long, JSONArray>();
			for (Iterator iterator = propertyList.iterator(); iterator.hasNext();) {
				JSONObject propertyJSON = JSONObject.parseObject(JSON.toJSONString((Map) iterator.next()));
				Long key = propertyJSON.getLong("categoryId");
				JSONArray categoryPropertyArray = attributeMap.containsKey(key) ? attributeMap.get(key)
						: new JSONArray();
				categoryPropertyArray.add(propertyJSON);
				attributeMap.put(key, categoryPropertyArray);
			}
		}
		// 5.封装返回的数据(将商品绑定商品属性)
		JSONArray saleGoodsArray = JSONArray.parseArray(JSON.toJSONString(saleGoodList));
		for (Object obj : saleGoodsArray) {
			JSONObject saleJson = (JSONObject) obj;
			if (attributeMap == null) {
				saleJson.put("attribute", "");
			} else {
				JSONArray categoryPropertyArray = attributeMap.get(saleJson.getLong("categoryId"));
				JSON json = (JSON) JSONObject.parse(JSONArray.toJSONString(categoryPropertyArray,
						SerializerFeature.DisableCircularReferenceDetect));// 解决JSON循环引用$ref问题
				saleJson.put("attribute",
						(categoryPropertyArray == null || categoryPropertyArray.isEmpty()) ? "" : json);
			}
		}
		paramsObject.clear();
		paramsObject.put("total_results", saleGoodList.size());
		paramsObject.put(this.getCollectionName(), saleGoodsArray);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	public ServiceResponse updateShopGoods(ServiceSession session, JSONObject paramsObject) {
		this.setUpsert(false);
		return this.onUpdate(session, paramsObject);
	}

	// 主数据根据业务规则唯一性批量查询(通用接口)
	public ServiceResponse queryDataByUnique(ServiceSession session, JSONObject paramsObject) {
		// 1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"table", "key", "values", "className");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;
		// 2.批量查询
		List list = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.AdvancedQueryMapper.queryDataByUnique", paramsObject);
		if (list == null || list.isEmpty())
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "查询不到数据");
		// 3.执行查询插件
		String className = paramsObject.getString("className");
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
		List<RowMap> jsonList = JSONObject.parseArray(array.toJSONString(), RowMap.class);
		// 执行查询插件处理 钱海兵
		List<AnnotationService> tagPlugins = this.getPlugins();
		for (AnnotationService plugin : tagPlugins) {
			try {
				plugin.onAction(session, jsonList, OperationFlag.afterQuery, Class.forName(className));
			} catch (Exception e) {
				return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "查询前插件执行异常:{0}",
						e.getMessage().toString());
			}
		}
		// 4.返回结果集
		String table = paramsObject.getString("table");
		paramsObject.clear();
		paramsObject.put(table, jsonList);
		paramsObject.put("total_results", jsonList.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 某个部类的所有子类集合
	public ServiceResponse levelCategoryChildren(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"categoryId", "erpCode", "entId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;

		String categoryId = paramsObject.getString("categoryId");
		Long entId = paramsObject.getLong("entId");
		String erpCode = paramsObject.getString("erpCode");
		List<CategoryModel> list = categoryPropertyServiceImpl.getLevelCategoryList(session, categoryId, erpCode,
				entId);
		// List<Long> categoryIdList
		// =list.stream().map(CategoryModel::getCategoryId).collect(Collectors.toList());
		return ServiceResponse.buildSuccess(list);
	}

	// 带部类的可售商品表查询
	public ServiceResponse querySaleGoods(ServiceSession session, JSONObject paramsObject) {
		// 1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"shopCode", "erpCode", "entId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;

		// 2.分页查询SaleGoods（带部类）
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		List<SaleGoodsModel> saleGoodsList = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.selectManageCategoryCodeSaleGoods", paramsObject);
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(saleGoodsList));
		List<RowMap> jsonList = JSONObject.parseArray(array.toJSONString(), RowMap.class);

		long total_results = 0;
		if (saleGoodsList != null && !saleGoodsList.isEmpty()) {
			total_results = this.getTemplate().getSqlSessionTemplate()
					.selectOne("beanmapper.SaleGoodsModelMapper.countManageCategoryCodeSaleGoods", paramsObject);
			// 执行查询插件处理 钱海兵
			List<AnnotationService> tagPlugins = this.getPlugins();
			for (AnnotationService plugin : tagPlugins) {
				try {
					plugin.onAction(session, jsonList, OperationFlag.afterQuery, this.getBeanClass());
				} catch (Exception e) {
					return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "查询前插件执行异常:{0}",
							e.getMessage() + "");
				}
			}
		}

		// 3.封装返回参数
		paramsObject.clear();
		paramsObject.put("saleGoods", jsonList);
		paramsObject.put("total_results", total_results);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 新增套餐
	@Override
	public ServiceResponse addMeal(ServiceSession session, JSONObject paramsObject) throws Exception {
		return getCodeBT(session, paramsObject);
	}

	// 启用或者停用套餐
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse enableOrDisableMeal(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店不能为空");
		}
		if (!paramsObject.containsKey("goodsStatus")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品状态不能为空");
		}
		if (!paramsObject.containsKey("ssgidList")) {
			// 字符串形式，中间以逗号隔开
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "套餐编码数组不能为空");
		}

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<Map<String, Object>> parameter = new ArrayList<>();
		String[] ssgidList = paramsObject.getString("ssgidList").toString().split(",");
		if (ssgidList == null || ssgidList.length <= 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "请选择要操作的套餐.");
		}

		String shopId = paramsObject.getString("shopId");
		for (String s : ssgidList) {
			Map<String, Object> map = new HashMap<>();
			map.put("shopId", shopId);
			map.put("ssgid", s);
			map.put("goodsStatus", paramsObject.getString("goodsStatus"));
			parameter.add(map);
		}

		int affectedRow = template.getSqlSessionTemplate()
				.update("beanmapper.SaleGoodsModelMapper.updateSaleGoodsStatus", parameter);
		if (affectedRow <= 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "操作失败.");
		}

		return ServiceResponse.buildSuccess("操作成功.");
	}

	// 商品查询中心-子品信息、组包码、菜谱、商品分割
	public ServiceResponse getChildGoodsInfo(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse response = ParamValidateUtil.checkParam(session, paramsObject,
				new String[] { "entId", "parentGoodsCode", "goodsType" });
		if (!ResponseCode.SUCCESS.equals(response.getReturncode())) {
			return response;
		}

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		String goodsType = paramsObject.getString("goodsType");
		String parentGoodsCode = paramsObject.getString("parentGoodsCode");
		ServiceResponse rep = null;

		// 商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分割商品/8-箱码商品/0-正常商品
		if ("3".equals(goodsType) || "4".equals(goodsType)) {
			// 3-组包码/4-菜谱
			paramsObject.remove("parentGoodsCode");
			paramsObject.put("goodsCode", parentGoodsCode);
			List<SaleGoodsItemsModel> allGoodsItems = template.getSqlSessionTemplate().selectList(
					"beanmapper.SaleGoodsItemsModelMapper.selectParentGoodsByChildGoodsCodes", paramsObject);
			List<Long> parentGoodsId = new ArrayList<>();
			for (int i = 0; i < allGoodsItems.size(); i++) {
				Long gsgid = allGoodsItems.get(i).getGsgid();
				parentGoodsId.add(gsgid);
			}

			List<Map<String, Object>> saleGoodsInfo = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(parentGoodsId)) {
				JSONObject p = new JSONObject();
				p.put("entId", paramsObject.get("entId"));
				p.put("sgids", parentGoodsId);
				saleGoodsInfo = template.getSqlSessionTemplate()
						.selectList("beanmapper.SaleGoodsModelMapper.selectInSGID", p);
			}

			JSONObject data = new JSONObject();
			data.put("saleGoods", saleGoodsInfo);
			data.put("total_result", saleGoodsInfo.size());
			rep = ServiceResponse.buildSuccess(data);
		} else {
			rep = this.onQuery(session, paramsObject);
		}

		JSONObject data = (JSONObject) rep.getData();
		JSONArray array = data.getJSONArray("saleGoods");
		List<Long> shopIds = new ArrayList<>();
		List<Long> sgids = new ArrayList<>();
		for (int i = 0; array != null && i < array.size(); i++) {
			JSONObject tmp = array.getJSONObject(i);
			if (tmp != null && tmp.getLong("shopId") != null)
				shopIds.add(tmp.getLong("shopId"));

			if (tmp != null && tmp.getLong("sgid") != null)
				sgids.add(tmp.getLong("sgid"));
		}

		// 添加商店
		List<ShopModel> shop = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shop))
			shop = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectIn", shopIds);

		for (int i = 0; array != null && i < array.size(); i++) {
			JSONObject tmp = array.getJSONObject(i);
			tmp.put("shopName", "");
			if (tmp.getString("shopId") == null)
				continue;
			Long shopId = Long.parseLong(tmp.getString("shopId"));
			for (ShopModel s : shop) {
				if (shopId == s.getShopId().longValue()) {
					tmp.put("shopName", s.getShopName());
				}
			}
		}

		// 添加商品信息
		List<GoodsModel> goodsModel = new ArrayList<>();
		List<GoodsDescModel> goodsDescModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sgids)) {
			DefaultParametersUtils.removeRepeateParams4Long(sgids);
			paramsObject.put("sgids", sgids);
			paramsObject.put("sgid", sgids);
			goodsModel = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID",
					paramsObject);
			goodsDescModel = template.getSqlSessionTemplate().selectList("beanmapper.GoodsDescModelMapper.selectInSGID",
					paramsObject);
		}

		for (int i = 0; array != null && i < array.size(); i++) {
			JSONObject tmp = array.getJSONObject(i);
			String goodsDesc = "";
			String measureUnit = "";
			for (GoodsModel g : goodsModel) {
				if (tmp.getLong("sgid").longValue() == g.getSgid().longValue()) {
					measureUnit = g.getMeasureUnit();
					break;
				}
			}

			for (GoodsDescModel s : goodsDescModel) {
				if (tmp.getLong("sgid").longValue() == s.getSgid()) {
					goodsDesc = s.getGoodsDesc();
					break;
				}
			}
			tmp.put("measureUnit", measureUnit);
			tmp.put("goodsDesc", goodsDesc);
		}

		JSONObject result = new JSONObject();
		result.put("saleGoods", array);
		result.put("total_result", data.getLong("total_result"));
		return ServiceResponse.buildSuccess(result);
	}

	// 码定义时获取对应码的商品编码和条码
	public ServiceResponse getCodeBT(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);

		if (!paramsObject.containsKey("goodsType")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品类型不能为空");
		}
		String goodsType = paramsObject.getString("goodsType");
		if (goodsType.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品类型值不能为空");
		} else {
			String regex = "^[0-9]{1}$";
			if (!goodsType.matches(regex)) {
				return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品类型值必须为整数");
			}
		}

		String id = Long.toString(UniqueID.getUniqueID(true));
		String code = goodsType + id.substring(id.length() - 8);

		paramsObject.clear();
		paramsObject.put("goodsCode", code);
		paramsObject.put("barNo", code);
		return ServiceResponse.buildSuccess(paramsObject);

	}

	// 按条件查询门店商品销售表
	public ServiceResponse getSaleGoodsList(ServiceSession session, JSONObject paramsObject) throws Exception {

		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject);
		// 查siid:""的商品
		if (paramsObject.containsKey("siid")) {
			if (StringUtils.isEmpty(paramsObject.get("siid"))) {
				paramsObject.put("siid", -1);
			}
		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);

		// goodsCode处理两边空格
		if (!StringUtils.isEmpty(paramsObject.get("goodsCode"))) {
			paramsObject.put("goodsCode", paramsObject.getString("goodsCode").trim());
		}
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

		// categoryId传空数组校验
		if (paramsObject.containsKey("categoryId")) {
			Object values = paramsObject.get("categoryId");
			if (values instanceof List) {
				// 如果categoryId为空数组，则为查询全部类别
				if (((List) values).isEmpty()) {
					paramsObject.remove("categoryId");
				}
			}
		}
		// 档口定义查询只查询套餐和主条码商品
		List<Map> salegoodsList = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.getSaleGoodsList", paramsObject);
		long total_results = 0;
		if (salegoodsList != null && salegoodsList.size() > 0) {
			total_results = template.getSqlSessionTemplate()
					.selectOne("beanmapper.SaleGoodsModelMapper.countSaleGoodsList", paramsObject);
			// 是否套餐显示前台CheckBox
			salegoodsList.forEach(action -> {
				// 如果商品类型是9.说明是套餐
				if (action.get("goodsType") != null && action.get("goodsType").equals(9)) {
					action.put("TCgoodsType", true);// TCgoodsType:是否是套餐
					action.put("TCdisable", false); // TCdisable:是否可编辑
					// 如果goodsType返回的20.说明是PLU商品（香港虚拟商品）
				} else if (action.get("goodsType") != null && action.get("goodsType").equals(20)) {
					action.put("TCgoodsType", false);
					action.put("TCdisable", true);
				} else {
					action.put("TCgoodsType", false);
					action.put("TCdisable", false);
				}
			});
		}
		JSONObject salegoods = new JSONObject();
		salegoods.put("saleGoods", salegoodsList);
		JSONArray array = salegoods.getJSONArray(this.getCollectionName());
		if (salegoodsList.size() > 0) {
			DefaultParametersUtils.numberFormat(array, "#0.00", "salePrice");
		}
		JSONObject result = new JSONObject();
		result.put("saleGoods", array);
		result.put("total_results", total_results);
		
		//提示查询的编码已经存在 
		String goodsCode = paramsObject.getString("goodsCode");
		if(!StringUtils.isEmpty(goodsCode)) {
			List<Map> isExistSaleGoods = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.isExistSaleGoods", paramsObject);
			if(isExistSaleGoods!=null && !isExistSaleGoods.isEmpty() && !StringUtils.isEmpty(isExistSaleGoods.get(0).get("stallCode").toString().trim()))
				result.put("isExist","该商品编码"+goodsCode + " 已被档口占用："+isExistSaleGoods.get(0).get("stallCode"));
		}
		
		return ServiceResponse.buildSuccess(result);
	}

	// @Override
	// protected FMybatisTemplate getTemplate() {
	// return this.getBean("StorageOperation", FMybatisTemplate.class);
	// }

	// 线上商品发布（关键字段为发布状态）
	// public ServiceResponse searchByState(ServiceSession session, JSONObject
	// paramsObject) throws Exception {

	// }

	// 菜谱新建
	public ServiceResponse menuInsert(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);

		if (!paramsObject.containsKey("goodsType")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品类型不能为空");
		}
		// JSONArray returnarray = new JSONArray();
		// paramsObject.put("sgid", UniqueID.getUniqueID(true));
		// ServiceResponse response = this.onInsert(session, paramsObject);
		// if(response.getReturncode().equals(ResponseCode.SUCCESS)){
		// Object data = response.getData();
		// if(data instanceof JSONObject){
		// Object id = ((JSONObject) data).get(this.getKeyfieldName());
		// paramsObject.put("gsgid", id);
		// }
		// ServiceResponse result = saleGoodsItems.menuInsert(session, paramsObject);
		// //需要判断插入是否成功 需要做事物管理 以后再加
		//
		// returnarray.add(result);
		//
		// }
		// returnarray.add(response);
		// return ServiceResponse.buildSuccess(returnarray);
		String createDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String creator = Long.toString(session.getUser_id());
		Map<String, Object> returnMap = new HashMap<>();
		// 1.存入商品基础表
		JSONArray saleGoodsItems = (JSONArray) paramsObject.get("saleGoodsItems");// 暂存商品信息

		paramsObject.put("goodsType", GoodsType.CP.getValue());
		Long groupSgid = UniqueID.getUniqueID(true);// 生成菜谱的ID
		String groupGoodsCode = paramsObject.getString("goodsCode");// 菜谱编码
		paramsObject.put("sgid", groupSgid);
		paramsObject.put("createDate", createDateString);
		paramsObject.put("creator", creator);
		paramsObject.put("status", 1);// 0:禁用 1：启用
		ServiceResponse response = goodsServiceImpl.onInsert(session, paramsObject);
		returnMap.put(goodsServiceImpl.getCollectionName(), response.getData());

		// 2.存入经营配置表
		response = goodsShopRefServiceImpl.onInsert(session, paramsObject);
		returnMap.put(goodsShopRefServiceImpl.getCollectionName(), response.getData());

		// 3.存入商品销售表
		response = this.onInsert(session, paramsObject);
		returnMap.put(this.getCollectionName(), response.getData());

		// 4.存入商品项表
		for (int i = 0; i < saleGoodsItems.size(); i++) {
			JSONObject json = (JSONObject) saleGoodsItems.get(i);
			json.put("gsgid", groupSgid);// 菜谱的ID（组商品ID）
			json.put("ggoodsCode", groupGoodsCode);// 菜谱母品的Code（组商品code）
			json.put("createDate", createDateString);
			json.put("creator", creator);
			json.put("status", 1);// 0:禁用 1：启用
		}
		response = saleGoodsItemsService.onInsert(session, paramsObject);
		returnMap.put(saleGoodsItemsService.getCollectionName(), response.getData());
		return ServiceResponse.buildSuccess(returnMap);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	// 发布
	public ServiceResponse saleGoodsRelease(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店ID不能为空");
		}
		if (!paramsObject.containsKey("channelId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "线上渠道ID不能为空");
		}
		List<JSONObject> jlist = new ArrayList<JSONObject>();
		if (paramsObject.containsKey(this.getCollectionName())) {
			Object dataObject = paramsObject.get(this.getCollectionName());
			if (dataObject != null && dataObject instanceof List) {
				// 解析格式：{"collectionname":[...]}
				JSONArray paramsArray = paramsObject.getJSONArray(this.getCollectionName());
				for (int i = 0; i < paramsArray.size(); i++) {
					JSONObject params = paramsArray.getJSONObject(i);
					Object ssgid = params.get(this.getKeyfieldName());
					Criteria criteria = Criteria.where("ssgid").is(ssgid).and(getEntname()).is(session.getEnt_id());
					Query query = new Query(criteria);
					SaleGoodsModel goodBean = this.onFindOne(session, this.getTemplate(), query, SaleGoodsModel.class,
							this.getCollectionName());
					if (goodBean != null) {
						long sgid = goodBean.getSgid();
						Criteria c2 = Criteria.where("ssgid").is(ssgid).and(getEntname()).is(session.getEnt_id())
								.and("shopId").is(paramsObject.get("shopId")).and("channelId")
								.is(paramsObject.get("channelId"));
						Query q2 = new Query(c2);
						List<GoodsUpAndDownModel> list = this.onFind(session, this.getTemplate(), q2,
								GoodsUpAndDownModel.class, "goodsUpAndDown");
						if (list == null || list.size() == 0) {
							long guadid = UniqueID.getUniqueID(true);
							JSONObject json = new JSONObject();
							json.put("guadid", guadid);
							json.put("shopId", paramsObject.get("shopId"));
							json.put("channelId", paramsObject.get("channelId"));
							json.put("ssgid", ssgid);
							json.put("updownStatus", 1);
							json.put("creator", session.getUser_id());
							json.put("createDate", new Date());
							jlist.add(json);
						}

					}
				}
			}
		} else {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_ERROR, "传入参数格式错误");
		}
		paramsObject.clear();
		paramsObject.put("goodsUpAndDown", jlist);
		System.out.println(paramsObject);
		return goodsUpAndDownService.onInsert(session, paramsObject);
	}

	/*
	 * @Description: 一键发布，发布该门店所有经营配置的商品
	 * 
	 * @param session  * @param paramsObject
	 * 
	 * @return: com.product.model.ServiceResponse
	 */
	public ServiceResponse saleGoodsReleaseAll(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (!paramsObject.containsKey("shopId") || !paramsObject.containsKey("channelId")
				|| !paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误");
		}
		Map insertParams = new HashMap();
		insertParams.put("shopId", paramsObject.get("shopId"));
		insertParams.put("erpCode", paramsObject.get("erpCode"));
		insertParams.put("channelId", paramsObject.get("channelId"));
		insertParams.put("updownStatus", 1);
		insertParams.put("creator", session.getUser_id());
		insertParams.put("createDate", new Date());
		insertParams.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		template.getSqlSessionTemplate().insert("beanmapper.SaleGoodsModelMapper.saleGoodsReleaseAll", insertParams);
		return ServiceResponse.buildSuccess("success");
	}

	// 收回
	public ServiceResponse saleGoodsTakeBack(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		if (!paramsObject.containsKey("saleGoods")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "数据key不能匹配");
		}
		Object params = paramsObject.get("saleGoods");
		paramsObject.clear();
		paramsObject.put(goodsUpAndDownService.getCollectionName(), params);
		return goodsUpAndDownService.onDelete(session, paramsObject);
	}

	// 保存套餐：香港永旺虚拟单品套餐明细保存
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse saveMeal(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 1.数据校验
		if (!paramsObject.containsKey("entity") || paramsObject.getJSONObject("entity").isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "套餐信息不能为空.");
		}
		/*if (!paramsObject.containsKey("detail") || paramsObject.getJSONArray("detail").isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品列表不能为空.");
		}*/
		JSONObject entity = paramsObject.getJSONObject("entity");
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, entity, "erpCode",
				"shopId", "shopCode", "goodsName", "goodsCode", "salePrice", "categoryId", "categoryCode", "ssgid",
				"stallCode", "siid");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;
		Long mainSsgid = entity.getLong("ssgid");

		// 2.校验套餐编码的唯一性
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		// 此校验代码已注释。永旺编码ERP传过来的，不是系统自建的。
		/*
		 * List<SaleGoodsModel> sgList = template.getSqlSessionTemplate()
		 * .selectList("beanmapper.SaleGoodsModelMapper.selectGoodsCode", entity); if
		 * (!CollectionUtils.isEmpty(sgList)) { return
		 * ServiceResponse.buildFailure(session, ResponseCode.FAILURE,
		 * "套餐编码已经被占用，请重新填写."); }
		 */

		// 3.修改主体部分
		/*
		 * entity.put("entId", session.getEnt_id()); entity.put("goodsType", 9);//
		 * goodsType 9为套餐商品 entity.put("directFromErp", false); // 0:否 1:是
		 * entity.put("directFromErp", true); // 0:否 1:是(永旺套餐是ERP传过来的数据)
		 * entity.put("singleItemFlag", true); // 是否单品 entity.put("mainBarcodeFlag",
		 * true); // 是否主条码 entity.put("canSaleFlag", 1); // 是否能售卖 // 2019-1-25
		 * 统一套餐商品状态：0 停用/ 3 启用 entity.put("goodsStatus", 3); // 目前前端传递的goodsStatus 是 3，
		 * 后台暂且保存1， 后面可能需要改成 3 entity.put("parentGoodsCode",
		 * entity.getString("goodsCode")); // 母品编码 entity.put("goodsDisplayName",
		 * entity.getString("goodsName")); // // 2019-03-01 套餐商品默认：是否加工(档口商品)为： 1:是
		 * entity.put("processFlag", 1); // 是否加工(档口商品) 1:是 0:否
		 * paramsObject.put("goodsType", 9);// 商品类型为9是套餐商品 ServiceResponse codeBT =
		 * this.getCodeBT(session, paramsObject); if
		 * (!ResponseCode.SUCCESS.equals(codeBT.getReturncode())) { return codeBT; }
		 * 
		 * JSONObject codeData = (JSONObject) codeBT.getData(); entity.put("barNo",
		 * codeData.get("barNo"));
		 * 
		 * ShopModel shopModel = template.getSqlSessionTemplate().selectOne(
		 * "beanmapper.ShopModelMapper.selectByState", entity); String erpCode =
		 * shopModel.getErpCode() == null ? "002" : shopModel.getErpCode(); // 获取
		 * JSONObject tmpShopParams = new JSONObject(); tmpShopParams.put("shopId",
		 * entity.getString("shopId")); ServiceResponse tmpRep =
		 * this.shopService.onQuery(session, tmpShopParams); JSONObject shop =
		 * (JSONObject) tmpRep.getData(); JSONArray array = shop.getJSONArray("shop");
		 * Long ssgid = null;
		 */
		String goodsCodeOld = entity.getString("goodsCode"); // 旧有的套餐编码
		/*
		 * if (entity.containsKey("ssgid") &&
		 * !StringUtils.isEmpty(entity.getString("ssgid"))) { // 修改 ssgid =
		 * Long.parseLong(entity.getString("ssgid")); List<JSONObject> entityList = new
		 * ArrayList<>(); entityList.add(entity);
		 * template.getSqlSessionTemplate().update(
		 * "beanmapper.SaleGoodsModelMapper.batchUpdateSaleGoods", entityList);
		 * 
		 * SaleGoodsModel oldModel = template.getSqlSessionTemplate().selectOne(
		 * "beanmapper.SaleGoodsModelMapper.searchByState", entity); goodsCodeOld =
		 * oldModel.getGoodsCode(); paramsObject.put("sgid", oldModel.getSgid());
		 * entity.put("sgid", entity.getString("ssgid"));
		 * this.goodsServiceImpl.onUpdate(session, entity); } else { ssgid =
		 * UniqueID.getUniqueID(true); entity.put("ssgid", ssgid); entity.put("sgid",
		 * ssgid); ServiceResponse response = this.onInsert(session, entity); //
		 * 新增salegoods if(!ResponseCode.SUCCESS.equals(response.getReturncode())) {
		 * throw new Exception(response.getData().toString()); }
		 * entity.put("directFromErp", "0"); entity.put("singleItemFlag", false);
		 * //entity.put("canSaleFlag", true); entity.put("fullName",
		 * entity.getString("goodsName")); response =
		 * this.goodsServiceImpl.onInsert(session, entity); // 新增goods
		 * if(!ResponseCode.SUCCESS.equals(response.getReturncode())) { throw new
		 * Exception(response.getData().toString()); } entity.put("gsrid", ssgid);
		 * response = this.goodsShopRefServiceImpl.onInsert(session, entity); //
		 * 新增goodsshopref if(!ResponseCode.SUCCESS.equals(response.getReturncode())) {
		 * throw new Exception(response.getData().toString()); } }
		 */

		// 4.修改套餐种类关联、套餐明细
		/*
		 * JSONObject tmpObj = new JSONObject(); tmpObj.put("ssgid", mainSsgid);
		 * List<SaleGoodsModel> saleGoodsList =
		 * template.getSqlSessionTemplate().selectList(
		 * "beanmapper.SaleGoodsModelMapper.searchByState", tmpObj); SaleGoodsModel sg =
		 * saleGoodsList.get(0); String creator = Long.toString(session.getUser_id());
		 */
		
		JSONArray detail = paramsObject.getJSONArray("detail"); // 明细允许为空
		List<SaleGoodsModel> insertSaleGoods = null;
		List<SaleGoodsModel> updateSaleGoods = null;
		List<SetMealDetailModel> setMealDetails = null;
		List<SetMealTypeRefModel> ref = null;
		if (detail != null) {
			insertSaleGoods = new ArrayList<SaleGoodsModel>();
			updateSaleGoods = new ArrayList<SaleGoodsModel>();
			Date now = new Date();
			setMealDetails = new ArrayList<>();
			ref = new ArrayList<>();
			for (int index = 0; index < detail.size(); index++) {
				JSONObject jsonObj = detail.getJSONObject(index);
				SetMealTypeRefModel refModel = new SetMealTypeRefModel();
				refModel.setSmtrid(UniqueID.getUniqueID(true));
				refModel.setGoodsId(mainSsgid);
				refModel.setGoodsCode(entity.getString("goodsCode"));
				refModel.setOptionNum(Integer.parseInt(jsonObj.getString("optionNum")));
				refModel.setTypeName(jsonObj.getString("typeName"));
				refModel.setShopId(entity.getLong("shopId"));
				refModel.setShopCode(entity.getString("shopCode"));
				refModel.setErpCode(entity.getString("erpCode"));
				refModel.setEntId(session.getEnt_id());
				refModel.setCreateDate(now);
				refModel.setCreator(session.getUser_code());
				refModel.setUpdateDate(now);
				refModel.setModifier(session.getUser_code());
				ref.add(refModel);
	
				JSONArray goodsInfoArray = jsonObj.getJSONArray("goodsInfoArray");
				for (int i = 0; goodsInfoArray != null && i < goodsInfoArray.size(); i++) {
					JSONObject tmp = goodsInfoArray.getJSONObject(i);
					// 设置套餐明细参数
					SetMealDetailModel mealDetail = new SetMealDetailModel();
					mealDetail.setSmdid(UniqueID.getUniqueID(true));
					mealDetail.setGoodsCode(tmp.getString("goodsCode"));
					mealDetail.setTypeName(jsonObj.getString("typeName")); // 套餐种类名称
					mealDetail.setSgoodsId(mainSsgid); // 套餐商品ID
					mealDetail.setSgoodsCode(entity.getString("goodsCode")); // 套餐商品编码
					mealDetail.setErpCode(entity.getString("erpCode"));
					mealDetail.setShopId(entity.getLong("shopId"));
					mealDetail.setShopCode(entity.getString("shopCode"));
					mealDetail.setEntId(session.getEnt_id());
					mealDetail.setCreator(session.getUser_code());
					mealDetail.setCreateDate(now);
					mealDetail.setModifier(session.getUser_code());
					mealDetail.setUpdateDate(now);
					// 设置salegoods参数
					SaleGoodsModel saleGoods = new SaleGoodsModel();
					saleGoods.setCategoryId(entity.getLong("categoryId"));
					saleGoods.setCategoryCode(entity.getString("categoryCode"));
					saleGoods.setShopId(entity.getLong("shopId"));
					saleGoods.setShopCode(entity.getString("shopCode"));
					saleGoods.setSiid(entity.getLong("siid"));
					saleGoods.setStallCode(entity.getString("stallCode"));
					saleGoods.setGoodsType((short) 20);// 档口虚拟商品goodsType定义20
					saleGoods.setMainBarcodeFlag(true);
					saleGoods.setGoodsStatus((short) 1);
					saleGoods.setDirectFromErp(false);
					saleGoods.setSalePrice(tmp.getBigDecimal("salePrice"));
					saleGoods.setGoodsName(tmp.getString("goodsName"));
					saleGoods.setGoodsCode(tmp.getString("goodsCode"));
					saleGoods.setBarNo(tmp.getString("goodsCode"));
					saleGoods.setErpCode(entity.getString("erpCode"));
					saleGoods.setEntId(session.getEnt_id());
					saleGoods.setCreateDate(now);
					saleGoods.setUpdateDate(now);
	
					Long saleGoodsId = tmp.getLong("goodsId");// 套餐明细表ID和可售商品ID一致
					if (StringUtils.isEmpty(saleGoodsId)) {// 表明新增的数据
						saleGoodsId = UniqueID.getUniqueID(true);
						saleGoods.setSsgid(saleGoodsId);
						saleGoods.setSgid(saleGoodsId);// 设置goodsId和saleGoodsId一致
						insertSaleGoods.add(saleGoods);
					} else {
						saleGoods.setSsgid(saleGoodsId);
						saleGoods.setSgid(saleGoodsId);// 设置goodsId和saleGoodsId一致
						updateSaleGoods.add(saleGoods);
					}
					mealDetail.setGoodsId(saleGoodsId);
					setMealDetails.add(mealDetail);
				}
			}
		}

		// 套餐种类
		if (!StringUtils.isEmpty(goodsCodeOld)) {
			// 删除旧的数据
			List<Map<String, Object>> param = new ArrayList<>();
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("goodsCode", goodsCodeOld);
			paramMap.put("shopCode", entity.get("shopCode"));
			paramMap.put("entId", session.getEnt_id());
			paramMap.put("erpCode", entity.get("erpCode"));
			param.add(paramMap);
			template.getSqlSessionTemplate().delete("beanmapper.SetMealTypeRefModelMapper.deleteByGoodsCodeShopCode",
					param);

			param.clear();
			paramMap.clear();
			paramMap.put("sGoodsCode", goodsCodeOld);
			paramMap.put("shopCode", entity.get("shopCode"));
			paramMap.put("entId", session.getEnt_id());
			paramMap.put("erpCode", entity.get("erpCode"));
			param.add(paramMap);
			template.getSqlSessionTemplate().delete("beanmapper.SetMealDetailModelMapper.deleteBySGoodsCodeShopCode",
					param);
		}

		if (CollectionUtils.isNotEmpty(ref)) {
			template.getSqlSessionTemplate().insert("beanmapper.SetMealTypeRefModelMapper.batchInsert", ref);
		}
		if (CollectionUtils.isNotEmpty(setMealDetails)) {
			template.getSqlSessionTemplate().insert("beanmapper.SetMealDetailModelMapper.batchInsert", setMealDetails);
		}
		if (CollectionUtils.isNotEmpty(insertSaleGoods)) {
			template.getSqlSessionTemplate().insert("beanmapper.SaleGoodsModelMapper.batchInsert", insertSaleGoods);
			template.getSqlSessionTemplate().insert("beanmapper.SaleGoodsModelMapper.batchInsertBaDaTong",
					insertSaleGoods);
		}
		if (CollectionUtils.isNotEmpty(updateSaleGoods)) {
			template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.batchUpdate", updateSaleGoods);
			template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.batchUpdateBaDaTong",
					updateSaleGoods);
		}
		return ServiceResponse.buildSuccess("success");
	}

	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 这里需要加shopName 暂时先放着field中处理
		boolean isShopName = false;
		if (paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_FIELDS)) {
			String fieldsValue = paramsObject.getString(BeanConstant.QueryField.PARAMKEY_FIELDS);
			if (fieldsValue.indexOf("shopName") != -1) {
				isShopName = true;
				fieldsValue = formatShopName(fieldsValue);
				paramsObject.put(BeanConstant.QueryField.PARAMKEY_FIELDS, fieldsValue);
			}
		}

		 if(!paramsObject.containsKey("goodsStatus") ||
		 StringUtils.isEmpty(paramsObject.getString("goodsStatus"))
		 || (paramsObject.get("goodsStatus") instanceof JSONArray &&
		 paramsObject.getJSONArray("goodsStatus").size() <= 0)){
		 int[] goodsStatus = {1,2,3};
		 paramsObject.put("goodsStatus", goodsStatus);
		 }

		ServiceResponse ssResponse = this.onQuery(session, paramsObject);
		// 步长差异范围stepDiff页面是整形，数据库是小数。需要转义
		JSONObject saleGoodsMap = (JSONObject) ssResponse.getData();
		List<RowMap> array = (List<RowMap>) saleGoodsMap.get(this.getCollectionName());
		List<Long> sgids = new ArrayList<>();
		for (RowMap rowMap : array) {
			if (!StringUtils.isEmpty(rowMap.get("stepDiff")))
				rowMap.put("stepDiff", (Float) rowMap.get("stepDiff") * 100);
			if (rowMap.get("sgid") != null)
				sgids.add(Long.parseLong(rowMap.get("sgid").toString()));
		}

		List<GoodsModel> goodsModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sgids)) {
			paramsObject.put("sgids", sgids);
			goodsModel = this.getTemplate().getSqlSessionTemplate()
					.selectList("beanmapper.GoodsModelMapper.selectInSGID", paramsObject);
		}
		for (RowMap map : array) {
			BigDecimal salePrice = new BigDecimal("0");
			for (GoodsModel g : goodsModel) {
				if (map.get("sgid") != null && Long.parseLong(map.get("sgid").toString()) == g.getSgid().longValue()) {
					salePrice = g.getSalePrice();
					break;
				}
			}
			map.put("salePrice", salePrice);
		}

		if (ssResponse.getReturncode().equals("0") && isShopName) {
			formatShopValue(ssResponse, session);
		}
		return ssResponse;
	}

	// 给返回的值加上shopName
	public void formatShopValue(ServiceResponse ssResponse, ServiceSession session) {
		List<Long> shopIdList = new ArrayList();
		JSONObject ssData = (JSONObject) ssResponse.getData();
		List<RowMap> ssMap = (List<RowMap>) ssData.get(this.getCollectionName());
		for (Map tmpMap : ssMap) {
			if (tmpMap.containsKey("shopId")) {
				shopIdList.add((Long) tmpMap.get("shopId"));
			}
		}
		if (shopIdList.size() > 0) {
			JSONObject tmpQueryParams = new JSONObject();
			tmpQueryParams.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "shopName,shopId");
			tmpQueryParams.put("shopId", shopIdList);
			ServiceResponse shopResponse = shopService.onQuery(session, tmpQueryParams);
			// System.out.println();
			if (shopResponse.getReturncode() == "0") {
				JSONObject shopObject = (JSONObject) shopResponse.getData();
				List<RowMap> shopList = (List<RowMap>) shopObject.get(shopService.getCollectionName());
				for (Map ssTmpMap : ssMap) {
					for (Map shopTmpMap : shopList) {
						if (String.valueOf(ssTmpMap.get("shopId")).equals(String.valueOf(shopTmpMap.get("shopId")))) {
							ssTmpMap.put("shopName", shopTmpMap.get("shopName"));
							continue;
						}
					}
				}
			}
		}

	}

	// 根据是否包含shopName 来格式化 fields字段
	public String formatShopName(String fieldsValue) {
		StringBuffer sb = new StringBuffer();
		String[] fieldsArray = fieldsValue.split(",");
		for (int i = 0; i < fieldsArray.length; i++) {
			if (fieldsArray[i].equals("shopName")) {
				continue;
			}
			sb.append(fieldsArray[i]);
			if ((i + 1) != fieldsArray.length) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	// saleGoods 查找，支持模糊查询
	public ServiceResponse search4Like(ServiceSession session, JSONObject paramsObject) {
		String fields = paramsObject.getString("fields");
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);

		paramsObject.put("entId", session.getEnt_id());

		DefaultParametersUtils.removeEmptyParams(paramsObject);
		DefaultParametersUtils.addSplitPageParams(paramsObject);

		if (String.class.isInstance(paramsObject.get("brandId"))
				|| Integer.class.isInstance(paramsObject.get("brandId"))) {
			List<Object> brandIdList = Arrays.asList(paramsObject.get("brandId"));
			paramsObject.put("brandId", brandIdList);
		}

		if (String.class.isInstance(paramsObject.get("categoryId"))
				|| Integer.class.isInstance(paramsObject.get("categoryId"))) {
			List<Object> categoryIdList = Arrays.asList(paramsObject.get("categoryId"));
			paramsObject.put("categoryId", categoryIdList);
		}

		if (String.class.isInstance(paramsObject.get("goodsStatus"))
				|| Integer.class.isInstance(paramsObject.get("goodsStatus"))) {
			List<Object> goodsStatus = Arrays.asList(paramsObject.get("goodsStatus"));
			paramsObject.put("goodsStatus", goodsStatus);
		}

		List<Map<String, Object>> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.search4LikeTC", paramsObject);

		List<Long> saleOrgIDs = new ArrayList<>();
		List<String> erpCodes = new ArrayList<>();
		List<Long> shopIds = new ArrayList<>();
		List<Long> brandIds = new ArrayList<>();
		List<Long> categoryIds = new ArrayList<>();
		List<Long> sgids = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> tmp : list) {
				if (tmp.get("saleOrgId") != null)
					saleOrgIDs.add(Long.parseLong(tmp.get("saleOrgId").toString()));
				if (tmp.get("erpCode") != null)
					erpCodes.add(tmp.get("erpCode").toString());
				if (tmp.get("shopId") != null)
					shopIds.add(Long.parseLong(tmp.get("shopId").toString()));
				if (tmp.get("brandId") != null)
					brandIds.add(Long.parseLong(tmp.get("brandId").toString()));
				if (tmp.get("categoryId") != null)
					categoryIds.add(Long.parseLong(tmp.get("categoryId").toString()));
				if (tmp.get("sgid") != null)
					sgids.add(Long.parseLong(tmp.get("sgid").toString()));
			}
		}

		List<BrandInfoModel> brandInfoModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(brandIds)) {
			paramsObject.put("brandIds", brandIds);
			brandInfoModel = template.getSqlSessionTemplate()
					.selectList("beanmapper.BrandInfoModelMapper.selectInBrandId", paramsObject);
		}

		List<SaleOrgModel> saleOrgModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(saleOrgIDs)) {
			saleOrgModel = template.getSqlSessionTemplate().selectList("beanmapper.SaleOrgModelMapper.selectIn",
					saleOrgIDs);
		}
		Map<Long, SaleOrgModel> saleOrgMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(saleOrgModel)) {
			for (SaleOrgModel s : saleOrgModel) {
				if (s.getSaleOrgId() != null)
					saleOrgMap.put(s.getSaleOrgId(), s);
			}
		}

		List<BusinessCompanyModel> businessCompanies = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(erpCodes)) {
			paramsObject.put("erpCodes", erpCodes);
			businessCompanies = template.getSqlSessionTemplate()
					.selectList("beanmapper.BusinessCompanyModelMapper.selectInErpCodes", paramsObject);
		}

		List<CategoryModel> categoryModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.put("categoryIds", categoryIds);
			categoryModel = template.getSqlSessionTemplate()
					.selectList("beanmapper.CategoryModelMapper.selectInCategoryIds", paramsObject);
		}

		List<ShopModel> shopModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shopIds)) {
			shopModel = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectIn", shopIds);
		}

		List<GoodsModel> goodsModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sgids)) {
			paramsObject.put("sgids", sgids);
			goodsModel = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID",
					paramsObject);
		}

		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> tmp : list) {
				SaleOrgModel model = null;
				if (tmp.get("saleOrgId") != null) {
					model = saleOrgMap.get(Long.parseLong(String.valueOf(tmp.get("saleOrgId"))));
				}
				String orgName = "-";
				String orgEnName = "-";
				if (model != null) {
					orgName = model.getOrgName() == null ? "" : model.getOrgName();
					orgEnName = model.getOrgEnName() == null ? "" : model.getOrgEnName();
				}
				tmp.put("orgName", orgName); // 柜组名称
				tmp.put("orgEnName", orgEnName); // 柜组英文名称

				// 添加经营公司信息
				String erpName = "";
				for (int i = 0; i < businessCompanies.size(); i++) {
					BusinessCompanyModel b = businessCompanies.get(i);
					if (tmp.get("erpCode") != null && tmp.get("erpCode").toString().equals(b.getErpCode())) {
						erpName = b.getErpName();
						break;
					}
				}
				tmp.put("erpName", erpName);

				// 添加门店名称
				String shopName = "";
				for (ShopModel s : shopModel) {
					if (tmp.get("shopId") != null
							&& s.getShopId().longValue() == Long.parseLong(tmp.get("shopId").toString())) {
						shopName = s.getShopName();
						break;
					}
				}
				tmp.put("shopName", shopName);

				// 添加品牌
				String brandName = "";
				for (BrandInfoModel b : brandInfoModel) {
					if (tmp.get("brandId") != null && Long.parseLong(tmp.get("brandId").toString()) == b.getBrandId()) {
						brandName = b.getBrandName();
						break;
					}
				}
				tmp.put("brandName", brandName);

				// 工业品类信息
				String categoryName = "";
				for (CategoryModel c : categoryModel) {
					if (tmp.get("categoryId") != null
							&& Long.parseLong(tmp.get("categoryId").toString()) == c.getCategoryId()) {
						categoryName = c.getCategoryName();
						break;
					}
				}
				tmp.put("categoryName", categoryName);

				// 添加计量单位
				String measureUnit = "";
				for (GoodsModel g : goodsModel) {
					if (tmp.get("sgid") != null
							&& Long.parseLong(tmp.get("sgid").toString()) == g.getSgid().longValue()) {
						measureUnit = g.getMeasureUnit();
						break;
					}
				}
				tmp.put("measureUnit", measureUnit);
			}
		}

		long totalResult = 0;
		if (CollectionUtils.isNotEmpty(list)) {
			totalResult = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.search4LikeCount",
					paramsObject);
		}

		JSONObject result = new JSONObject();
		result.put("saleGoods", DefaultParametersUtils.filterByFields(list, fields));
		result.put("total_results", totalResult);
		return ServiceResponse.buildSuccess(result);
	}

	@Override
	public ServiceResponse search4OnlineChanel(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店不能为空");
		}

		// 查询展示分类参数
		Object shopId = paramsObject.get("shopId");
		Object channelId = paramsObject.get("channelId");
		Set<Long> sgidSet = new HashSet<Long>();

		if (String.class.isInstance(paramsObject.get("shopId"))
				|| Integer.class.isInstance(paramsObject.get("shopId"))) {
			List tmpShopList = Arrays.asList(paramsObject.get("shopId"));
			paramsObject.put("shopId", tmpShopList);
		}

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		DefaultParametersUtils.removeEmptyParams(paramsObject);
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		List<Map<String, Object>> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.selectOnLineChannel", paramsObject);
		long total_results = template.getSqlSessionTemplate()
				.selectOne("beanmapper.SaleGoodsModelMapper.selectOnLineChannelCount", paramsObject);

		List<Long> brandIds = new ArrayList<>();
		List<Long> categoryIds = new ArrayList<>();
		List<Long> vids = new ArrayList<>();
		for (Map<String, Object> map : list) {
			if (map.get("brandId") != null)
				brandIds.add(Long.parseLong(map.get("brandId").toString()));
			if (map.get("categoryId") != null)
				categoryIds.add(Long.parseLong(map.get("categoryId").toString()));
			if (map.get("vid") != null)
				vids.add(Long.parseLong(map.get("vid").toString()));
			// 查询展示分类参数
			if (map.get("sgid") != null)
				sgidSet.add(Long.parseLong(map.get("sgid").toString()));
		}

		// 添加品牌名称
		paramsObject.put("brandIds", brandIds);
		List<BrandInfoModel> brandInfo = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(brandIds)) {
			brandInfo = template.getSqlSessionTemplate().selectList("beanmapper.BrandInfoModelMapper.selectInBrandId",
					paramsObject);
		}
		// 品类
		List<CategoryModel> categoryInfo = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.put("categoryIds", categoryIds);
			categoryInfo = template.getSqlSessionTemplate()
					.selectList("beanmapper.CategoryModelMapper.selectInCategoryIds", paramsObject);
		}
		// 供应商
		List<VenderModel> venderInfo = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(vids)) {
			venderInfo = template.getSqlSessionTemplate().selectList("beanmapper.VenderModelMapper.selectIn", vids);
		}

		for (Map<String, Object> map : list) {
			String brandName = "";
			String categoryName = "";
			String vendersName = "";
			for (BrandInfoModel b : brandInfo) {
				if (map.get("brandId") != null && Long.parseLong(map.get("brandId").toString()) == b.getBrandId()) {
					brandName = b.getBrandName();
					break;
				}
			}
			for (CategoryModel c : categoryInfo) {
				if (map.get("categoryId") != null
						&& Long.parseLong(map.get("categoryId").toString()) == c.getCategoryId()) {
					categoryName = c.getCategoryName();
					break;
				}
			}
			for (VenderModel v : venderInfo) {
				if (map.get("vid") != null && Long.parseLong(map.get("vid").toString()) == v.getVid()) {
					vendersName = v.getVendersName();
					break;
				}
			}
			map.put("brandName", brandName);
			map.put("categoryName", categoryName);
			map.put("vendersName", vendersName);
		}

		// 列表中显示展示分类
		if (!sgidSet.isEmpty()) {
			paramsObject.clear();
			paramsObject.put("shopId", shopId);
			paramsObject.put("channelId", channelId);
			paramsObject.put("sgids", sgidSet);
			MapResultHandler mapResultHandler = new MapResultHandler();
			template.getSqlSessionTemplate().select("beanmapper.AdvancedQueryMapper.selectShowCategoryMap",
					paramsObject, mapResultHandler);
			Map<String, Long> sgidShowCategoryNameMap = mapResultHandler.getMappedResults();
			list.stream().forEach(e -> {
				e.put("showCategoryName", sgidShowCategoryNameMap.get(e.get("sgid")));
			});
		}
		JSONObject result = new JSONObject();
		result.put("saleGoods", list);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}

	// 线上商品发布（关键字段为发布状态）
	@Override
	public ServiceResponse searchByState(ServiceSession session, JSONObject paramsObject) throws Exception {
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		paramsObject = transformParam(session, paramsObject, false, false);
		if (!paramsObject.containsKey("falgState")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "发布状态不能为空");
		}
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);

		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店ID不能为空");
		}
		if (!paramsObject.containsKey("channelId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "线上渠道ID不能为空");
		}
		if (String.class.isInstance(paramsObject.get("shopId"))
				|| Integer.class.isInstance(paramsObject.get("shopId"))) {
			List tmpShopList = Arrays.asList(paramsObject.get("shopId"));
			paramsObject.put("shopId", tmpShopList);
		}
		if (String.class.isInstance(paramsObject.get("channelId"))
				|| Integer.class.isInstance(paramsObject.get("channelId"))) {
			List tmpChannelIdList = Arrays.asList(paramsObject.get("channelId"));
			paramsObject.put("channelId", tmpChannelIdList);
		}
		if (String.class.isInstance(paramsObject.get("categoryId"))
				|| Integer.class.isInstance(paramsObject.get("categoryId"))) {
			List tmpcategoryIdList = Arrays.asList(paramsObject.get("categoryId"));
			paramsObject.put("categoryId", tmpcategoryIdList);
		}

		boolean falgState = paramsObject.getBoolean("falgState");
		long count = 0;
		// 当查询未发布的商品 为查询saleGoods 查询发布商品为saleGoods与goodsUpAndDown的关联查询
		List<SaleGoodsModel> list = new ArrayList();
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		if (falgState) {
			list = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.searchTrueByState",
					paramsObject);
			if (list != null && list.size() > 0) {
				count = template.getSqlSessionTemplate()
						.selectOne("beanmapper.SaleGoodsModelMapper.searchTrueByStateCount", paramsObject);
			}
		} else {
			list = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.searchFalseByState",
					paramsObject);
			if (list != null)
				if (list.size() > 0) {
					count = template.getSqlSessionTemplate()
							.selectOne("beanmapper.SaleGoodsModelMapper.searchFalseByStateCount", paramsObject);
				}
		}

		JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
		List<RowMap> jsonList = JSONObject.parseArray(array.toJSONString(), RowMap.class);
		// 执行查询插件处理 钱海兵
		List<AnnotationService> tagPlugins = this.getPlugins();
		for (AnnotationService plugin : tagPlugins) {
			try {
				plugin.onAction(session, jsonList, OperationFlag.afterQuery, this.getBeanClass());
			} catch (Exception e) {
				return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "查询前插件执行异常:{0}",
						e.getMessage() + "");
			}
		}
		JSONObject result = new JSONObject();
		result.put("saleGoods", jsonList);
		result.put("total_results", count);
		return ServiceResponse.buildSuccess(result);
	}

	// 搜索商品明细
	public ServiceResponse searchDetail(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("ssgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店商品销售ID不能为空");
		}
		return this.onQuery(session, paramsObject);
	}


	//查询商品详细信息（线下）
	@Override
	public ServiceResponse searcheSaleGoodsDetails(ServiceSession session, JSONObject paramsObject) throws Exception {
		String dcms = paramsObject.getString("dcms");//add by yihaitao 2024-06-21{"dcms":"Y"} 需要返回属性分类
		long allStartTime = System.currentTimeMillis();
		long startTime = System.currentTimeMillis();
		String paramCode = paramsObject.getString("code");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:sss");
		logger.error("【paramCode：{}】开始调用 searcheSaleGoodsDetails Start ------- {}",paramCode,formatter.format(new Date()));
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);

		if (!paramsObject.containsKey("entId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "零售商ID不能为空");
		}
		if (!paramsObject.containsKey("shopCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店编码不能为空");
		}
		if (!paramsObject.containsKey("terminalNo")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "收银机号不能为空");
		}
		if (!paramsObject.containsKey("code")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品编码或条码不能为空");
		}

		// 柜组编码，前端有一些不是传递空字符串，而是干脆不传，所以不能添加校验; orgCode 前端传递过来的是有多个的情况，以英文逗号分割开;
		// if (!paramsObject.containsKey("orgCode")) {
		// return ServiceResponse.buildFailure(session,
		// ResponseCode.Exception.SPECDATA_IS_EMPTY,"柜组编码不能为空");
		// }
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "经营公司编码不能为空");
		}

		Long entId = paramsObject.getLong("entId");
		String erpCode = paramsObject.getString("erpCode");
		// String shopCode = paramsObject.getString("shopCode");

		// 当orgCode 传递空字符串的时候不进行过滤
		if (paramsObject.containsKey("orgCode") && !StringUtils.isEmpty(paramsObject.getString("orgCode"))) {
			String orgCode = paramsObject.getString("orgCode");
			String[] orgCodeArray = orgCode.split(",");
			if ("3".equals(paramsObject.getString("searchType"))) {
				paramsObject.put("categoryCode", orgCodeArray); // 当searchType =  3,orgCode是起到categoryCode的作用
			} else {
				paramsObject.put("orgCode", orgCodeArray);
			}
		}

		if (!paramsObject.containsKey("searchType") || !"2".equals(paramsObject.getString("searchType"))) {
			paramsObject.remove("orgCode");// 如果searchType不为2，则不需要验证柜组
		}

		if (!paramsObject.containsKey("goodsStatus") || StringUtils.isEmpty(paramsObject.getString("goodsStatus"))
				|| (paramsObject.get("goodsStatus") instanceof JSONArray
						&& paramsObject.getJSONArray("goodsStatus").size() <= 0)) {
			List<Integer> goodsStatus = new ArrayList<>();
			goodsStatus.add(1);
			goodsStatus.add(2);
			goodsStatus.add(3);
			paramsObject.put("goodsStatus", goodsStatus);
		}

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		DefaultParametersUtils.removeEmptyParams(paramsObject);
		logger.error("A start.【paramCode：{}】searchSaleGoodsDetails start ------- {}",paramCode,formatter.format(new Date()));
		// 先使用条码查询
		paramsObject.put("barNo", paramsObject.get("code"));
		List<java.util.Map<String, Object>> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.searchSaleGoodsDetails", paramsObject);
		if (list == null || list.size() < 1) {
			paramsObject.put("goodsCode", paramsObject.get("code"));
			paramsObject.remove("barNo");
			list = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.searchSaleGoodsDetails",
					paramsObject);
		}
		long endTime = System.currentTimeMillis();
		logger.error("A end.【paramCode：{}】searchSaleGoodsDetails end ------- cost time:{}",paramCode,endTime - startTime);

		// 对商品去重，去重规则 1 根据 mainBarCode字段是否为1 ，然后更加商品的更新时间
		Map<String, List<Map>> tmpHashMap = new HashMap<>();
		for (Map<String, Object> stringObjectMap : list) {
			List<Map> tmpGoods = null;
			String goodsCode = (String) stringObjectMap.get("goodsCode");
			if (!tmpHashMap.containsKey(goodsCode)) {
				tmpGoods = new ArrayList<>();
				tmpHashMap.put(goodsCode, tmpGoods);
			} else {
				tmpGoods = tmpHashMap.get(goodsCode);
			}
			tmpGoods.add(stringObjectMap);
		}
		list = new ArrayList<>();
		for (String goodsCode : tmpHashMap.keySet()) {
			List<Map> goods;
			goods = tmpHashMap.get(goodsCode);
			list.add(compareAndGetMap(goods));
		}

		startTime = System.currentTimeMillis();
		logger.error("B start.【paramCode：{}】decorateResponseData4SaleGoodsDetail  start ------- {}",paramCode,formatter.format(new Date()));
		ServiceResponse response = decorateResponseData4SaleGoodsDetail(session, paramsObject, template, list);
		endTime = System.currentTimeMillis();
		logger.error("B end.【paramCode：{}】decorateResponseData4SaleGoodsDetail  end ------- cost time:{}",paramCode,endTime - startTime);

		if (!ResponseCode.SUCCESS.equals(response.getReturncode())) {
			return response;
		}

		if("Y".equals(dcms) && !list.isEmpty()){//add by yihaitao 2024-06-21 新增属性分类
			List goodsCodes = list.stream().map(action -> action.get("goodsCode")).collect(Collectors.toList());
			erpCode = paramsObject.getString("erpCode") == null ? "002" : paramsObject.getString("erpCode");
			Object shopCode = list.get(0).get("shopCode");
			Object stallCode = list.get(0).get("stallCode");
			JSONArray attData = new JSONArray();
			JSONObject queryParam = new JSONObject();
			List<PackageAttCate> attCateList;
			Map<String,List<PackageAttCate>> attCateMap;
			List<PackageAttDict> dictList;
			Map<String,List<PackageAttDict>> dictMap;

			//1.查询商品属性分类
			Map<String, String> arrMap = new HashMap<>();
			paramsObject.clear();
			paramsObject.put("erpCode",erpCode);
			paramsObject.put("shopCode",shopCode);
			paramsObject.put("stallCode",stallCode);
			paramsObject.put("goodsCode",goodsCodes);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<PackageAttShopGoodsRef> arrGoodsList = packageAttShopGoodsRefService.wrapQueryBeanList(session,paramsObject);
			Map<String, List<PackageAttShopGoodsRef>> arrGoodsMap = arrGoodsList.stream().collect(Collectors.groupingBy(PackageAttShopGoodsRef::getGoodsCode));
			//Map<String,String> pCodeNameMap = arrGoodsList.stream().collect(Collectors.toMap(PackageAttShopGoodsRef::getPCode,PackageAttShopGoodsRef::getPName,(key1 , key2)-> key2));//重复键对应的后值覆盖前值
			//修复空指针异常：数据存在空数据 add by yihaitao 2024-09-26
			Map<String,String> pCodeNameMap = arrGoodsList.stream().collect(Collectors.toMap(PackageAttShopGoodsRef::getPCode,item->item.getPName() == null ? "":item.getPName(),(key1 , key2)-> key2));
			pCodeNameMap.remove(null);
			//查询商品的图片
//			paramsObject.clear();
//			paramsObject.put("goodsCode",goodsCodes);
//			paramsObject.putIfAbsent("imageType",1);//默认查询主图
//			paramsObject.put("page_size",Integer.MAX_VALUE);
//			List<SaleGoodsImageRefModel> imageList = saleGoodsImageRefServiceImpl.wrapQueryBeanList(session,paramsObject);
//			Map<String,String> codeImageTypeMap = imageList.stream().collect(Collectors.toMap(SaleGoodsImageRefModel::getGoodsCode,SaleGoodsImageRefModel::getImageUrl,(key1 , key2)-> key2));

			//2.查询packageattcate获取下级数据
			if(pCodeNameMap != null && !pCodeNameMap.isEmpty()){
				pCodeNameMap.forEach((k,v) -> {
					JSONObject data = new JSONObject();
					data.put("pCode",k);
					data.put("pName",v);
					attData.add(data);
				});
				queryParam.clear();
				queryParam.put("level",3);
				queryParam.put("parentCode",arrGoodsList.stream().map(action -> action.getPCode()).collect(Collectors.toSet()));
				queryParam.put("erpCode",erpCode);
				queryParam.put("entId",session.getEnt_id());
				queryParam.put("page_size",Integer.MAX_VALUE);
				attCateList = packageAttCateService.wrapQueryBeanList(session,queryParam);
				//3.查询packageattdict数据
				if(attCateList!=null && !attCateList.isEmpty()){
					queryParam.clear();
					queryParam.put("pCode",attCateList.stream().map(action -> action.getPCode()).collect(toList()));
					queryParam.put("erpCode",erpCode);
					queryParam.put("entId",session.getEnt_id());
					queryParam.put("page_size",Integer.MAX_VALUE);
					queryParam.put("order_field","orderNum");
					queryParam.put("order_direction","asc");
					dictList = packageAttDictService.wrapQueryBeanList(session,queryParam);
					if(dictList!=null && !dictList.isEmpty()){
						dictMap = dictList.stream().collect(Collectors.groupingBy(PackageAttDict::getPCode));
					} else {
						dictMap = null;
					}
					//4.数据挂载
					attCateMap = attCateList.stream().collect(Collectors.groupingBy(PackageAttCate::getParentCode));
					attData.forEach(obj -> {
						JSONObject jsonObj = (JSONObject) obj;
						String attCatePCode = jsonObj.getString("pCode");
						if(attCateMap.containsKey(attCatePCode)){
							JSONArray attCateArray = JSON.parseArray((JSONObject.toJSONString(attCateMap.get(attCatePCode), SerializerFeature.WriteDateUseDateFormat)));
							for (int i = 0; i < attCateArray.size(); i++) {
								JSONObject attCateJSON = attCateArray.getJSONObject(i);
								String pCode = attCateJSON.getString("pCode");
								if (dictMap != null && dictMap.containsKey(pCode)) {
									attCateJSON.put("attdic", JSON.parseArray((JSONObject.toJSONString(dictMap.get(pCode), SerializerFeature.WriteDateUseDateFormat))));
								}
							}
							jsonObj.put("attdetail", attCateArray);
						}
					});
				}
			}

			Map<String,List<Object>> attMap = attData.stream().collect(Collectors.groupingBy(item -> JSON.parseObject(item.toString()).getString("pCode")));
			paramsObject.clear();
			paramsObject.put("shopCode",shopCode);
			paramsObject.put("stallCode",stallCode);
			paramsObject.put("goodsCode",goodsCodes);
			JSONObject dateParam = new JSONObject(){{put(">=", DateUtils.getCurrentDay());}};
			paramsObject.put("updateDate",dateParam);
			paramsObject.put("page_size",Integer.MAX_VALUE);
			List<SaleGoodsProperty> properties = saleGoodsPropertyService.wrapQueryBeanList(session,paramsObject);
			Map<String,String> goodsCodeSelloutMap = properties.stream().collect(Collectors.toMap(SaleGoodsProperty::getGoodsCode,SaleGoodsProperty::getSellout));

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tmpMap = list.get(i);
				Object goodsCode = tmpMap.get("goodsCode");
				List<PackageAttShopGoodsRef> refList = arrGoodsMap.get(goodsCode);//判断商品是否有属性分类
				if(refList!=null && !refList.isEmpty()){//增加属性分类 add by yihaitao 2024-06-13
					PackageAttShopGoodsRef ref = refList.get(0);
					String pCode = ref.getPCode();
					if(pCode!=null){//修复空指针异常：数据存在空数据 add by yihaitao 2024-09-26
						List data = attMap.get(pCode);
						JSONObject attJSON = (JSONObject)data.get(0);
						tmpMap.putAll(attJSON);
					}
				}
				//增加售罄标识 add by yihaitao 2024-06-21
				tmpMap.put("sellout",goodsCodeSelloutMap.get(tmpMap.get("goodsCode")) == null || goodsCodeSelloutMap.isEmpty() ? 0 : goodsCodeSelloutMap.get(tmpMap.get("goodsCode")));
			}
		}else {
			// 添加工业分类属性
			List<Long> ssgids = new ArrayList<>();
			List<Long> categoryIds = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tmpMap = list.get(i);
				if (tmpMap.get("ssgid") != null)
					ssgids.add(Long.parseLong(tmpMap.get("ssgid").toString()));
				if (tmpMap.get("categoryId") != null)
					categoryIds.add(Long.parseLong(tmpMap.get("categoryId").toString()));
			}

			startTime = System.currentTimeMillis();
			logger.error("C start.【paramCode：{}】categoryProperty  start ------- {}",paramCode,formatter.format(new Date()));
			List<CategoryPropertyModel> categoryProperty = getCategoryProperty(session, paramsObject, entId, erpCode,categoryIds);
			endTime = System.currentTimeMillis();
			logger.error("C end.【paramCode：{}】categoryProperty  end ------- cost time :{}",paramCode,endTime - startTime);

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tmpMap = list.get(i);
				List<Map<String, Object>> subCategoryProperty = new ArrayList<>();
				Long tmpCategoryId = Long.parseLong(tmpMap.get("categoryId").toString());
				for (int j = 0; j < categoryProperty.size(); j++) {
					CategoryPropertyModel model = categoryProperty.get(j);
					if (model.getCategoryId().longValue() == tmpCategoryId) {
						Map<String, Object> tmpCategoryProperty = new HashMap<>();
						tmpCategoryProperty.put("propertyName",
								model.getPropertyName() == null ? "" : model.getPropertyName());
						tmpCategoryProperty.put("propertyCode",
								model.getPropertyCode() == null ? "" : model.getPropertyCode());
						subCategoryProperty.add(tmpCategoryProperty);
					}
				}

				tmpMap.put("categoryProperty", subCategoryProperty);
			}
		}

		JSONObject result = new JSONObject();
		result.put("goods", list);
		result.put("total_results", list.size());
		long allEndTime = new Date().getTime();
		logger.error("【paramCode：{}】结束调用 searcheSaleGoodsDetails End -----> 总耗时：{}" ,paramCode,(allEndTime - allStartTime));
		return ServiceResponse.buildSuccess(result);
	}

	private List<CategoryPropertyModel> getCategoryProperty(ServiceSession session, JSONObject paramsObject, Long entId,
			String erpCode, List<Long> categoryIds) {
		List<CategoryPropertyModel> categoryProperty = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.clear();
			DefaultParametersUtils.removeRepeateParams4Long(categoryIds);
			Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode).and("status").is(1).and("categoryId").in(categoryIds);
			Query query = new Query(criteria);
			Field fields = query.fields();
			fields.include("categoryId,propertyCode,propertyName");
			categoryProperty = this.getTemplate().select(query, CategoryPropertyModel.class, "categoryproperty");
		}

		return categoryProperty;
	}

	public Map<String, Object> compareAndGetMap(List<Map> list) {
		if (list == null || list.size() == 0)
			return null;
		list.sort((Map goods1, Map goods2) -> {
			String mainBarcodeFlag1 = String.valueOf(goods1.get("mainBarcodeFlag")).equals("true") ? "1" : "0";
			String mainBarcodeFlag2 = String.valueOf(goods2.get("mainBarcodeFlag")).equals("true") ? "1" : "0";
			int firstCompare = mainBarcodeFlag2.compareTo(mainBarcodeFlag1);
			if (firstCompare == 0) {
				String time1 = String.valueOf(goods1.get("updateDate")).equals("null")
						? String.valueOf(goods1.get("createDate"))
						: String.valueOf(goods1.get("updateDate"));
				String time2 = String.valueOf(goods2.get("updateDate")).equals("null")
						? String.valueOf(goods2.get("createDate"))
						: String.valueOf(goods2.get("updateDate"));
				return time2.compareTo(time1);
			}
			return firstCompare;

		});
		return list.get(0);
	}

	//查询套餐明细
	@Override
	public ServiceResponse searchMealDetail(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "ssgid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;	
		
		Long ssgid = paramsObject.getLong("ssgid");
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
	
		// 目前只有主条码设置了套餐明细。如果是从条码的ssgid转换成主条码的ssgid进行查询套餐
		//paramsObject.put("searchPluginFlag", true);
		SaleGoodsModel saleGoods = this.wrapQueryBean(session,paramsObject);//检测是否是主条码
		if(saleGoods == null) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "查询的套餐商品不存在。参数:"+paramsObject);
		
		if(saleGoods!=null && saleGoods.getMainBarcodeFlag() == null ? false : !saleGoods.getMainBarcodeFlag()) {//如果是从条码：从条码转成主条码的ssgid
			SaleGoodsModel mainSaleGoods =  template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.slaveToMasterId",paramsObject);
			if(mainSaleGoods != null) {//如果主数据根据从条码没有查询到主条码（可能数据原因），就返回当前从条码
				saleGoods = mainSaleGoods;
			}
			ssgid = saleGoods.getSsgid();
		}
		paramsObject.put("ssgid", ssgid);
		List<Map<String, Object>> detailList = template.getSqlSessionTemplate().selectList("beanmapper.SetMealDetailModelMapper.selectByStateNew", paramsObject);
		List<Map<String, Object>> typeMealRef = template.getSqlSessionTemplate().selectList("beanmapper.SetMealTypeRefModelMapper.selectByState4Map", paramsObject);
		//Map saleGoodsMap = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.searchByStateNew", paramsObject);//增加Artcode返回
		paramsObject.put("entId", session.getEnt_id());
		paramsObject.put("shopId", saleGoods.getShopId());

		// 获取明细商品的商品信息
		if(detailList!=null && !detailList.isEmpty()) {
			List<Long> categoryIds = new ArrayList<>();
			detailList.forEach(action -> {
				if (action.get("categoryId") != null) {
					categoryIds.add((Long) action.get("categoryId"));
				};
			});
			
			List<CategoryPropertyModel> categoryProperty = this.getCategoryProperty(session, paramsObject,saleGoods.getEntId(), saleGoods.getErpCode(), categoryIds);
	
			for (Map<String, Object> map : detailList) {
				String goodsNameD = map.get("goodsName") == null ? "" : map.get("goodsName").toString();
				String goodsDisplayNameD = map.get("goodsDisplayName") == null ? "" : map.get("goodsDisplayName").toString();
				String enFnameD = map.get("enFname") == null ? "" : map.get("enFname").toString();
				String enSnameD = map.get("enSname") == null ? "" : map.get("enSname").toString();
				map.put("goodsNameD", goodsNameD);
				map.put("goodsDisplayNameD", goodsDisplayNameD);
				map.put("enFnameD", enFnameD);
				map.put("enSnameD", enSnameD);
	
				// 将escaleFlag 转换成 1，0
				if (map.get("escaleFlag") == null || "".equals(map.get("escaleFlag").toString().trim())) {
					map.remove("escaleFlag");
				} else if (Boolean.parseBoolean(map.get("escaleFlag").toString())) {
					map.put("escaleFlag", 1);
				} else {
					map.put("escaleFlag", 0);
				}
	
				// 添加明细商品工业分类属性
				List<Map<String, Object>> subCategoryProperty = new ArrayList<>();
				Long tmpCategoryId = map.get("categoryId") == null ? null : Long.parseLong(map.get("categoryId").toString());
				if(categoryProperty!=null && !categoryProperty.isEmpty()) {
					for (int i = 0; i < categoryProperty.size(); i++) {
						CategoryPropertyModel c = categoryProperty.get(i);
						if (c.getCategoryId().longValue() == tmpCategoryId) {
							Map<String, Object> tmpCategoryProperty = new HashMap<>();
							tmpCategoryProperty.put("propertyName", c.getPropertyName());
							tmpCategoryProperty.put("propertyCode", c.getPropertyCode());
							subCategoryProperty.add(tmpCategoryProperty);
						}
					}
				}
				map.put("categoryProperty", subCategoryProperty);
			}
		}
		// 整合套餐商品明细数据
		List<Map<String, Object>> transfer = new ArrayList<>();
		if(typeMealRef!=null && !typeMealRef.isEmpty()) {
			for (Map<String, Object> mealRef : typeMealRef) {
				Map<String, Object> map = new HashMap<>();
				map.put("smtrid", mealRef.get("smtrid")); // 档口套餐种类关联ID
				map.put("typeName", mealRef.get("typeName")); // 种类名称
				map.put("optionNum", mealRef.get("optionNum")); // 可选数量
				map.put("goodsCode", mealRef.get("goodsCode")); // 套餐编码
				map.put("goodsName", mealRef.get("goodsName"));
	
				List<Map<String, Object>> detail = new ArrayList<>();
				for (Map<String, Object> tmp : detailList) {
					String typeName = tmp.get("typeName") == null ? "" : tmp.get("typeName").toString();
					if (typeName.toString().equals(mealRef.get("typeName"))) {
						detail.add(tmp);
					}
				}
				map.put("detail", detail);
				transfer.add(map);
			}
		}
		// 套餐商品总信息		
/*		JSONObject sumInfo = new JSONObject();
		sumInfo.put("salePrice", saleGoodsMap.get("salePrice"));
		sumInfo.put("goodsName", saleGoodsMap.get("goodsName") == null ? "" : saleGoodsMap.get("goodsName"));
		sumInfo.put("goodsCode", saleGoodsMap.get("goodsCode") == null ? "" : saleGoodsMap.get("goodsCode"));
		sumInfo.put("goodsDisplayName", saleGoodsMap.get("goodsDisplayName") == null ? "" : saleGoodsMap.get("goodsDisplayName"));
		sumInfo.put("enSname", saleGoodsMap.get("enSname") == null ? "" : saleGoodsMap.get("enSname"));
		sumInfo.put("enFname", saleGoodsMap.get("enFname") == null ? "" : saleGoodsMap.get("enFname"));
		sumInfo.put("prcutMode", saleGoodsMap.get("prcutMode"));
		sumInfo.put("artCode", saleGoodsMap.get("artCode"));
		// 套餐 取值是否打印 20190421 --cj
		sumInfo.put("processFlag", saleGoodsMap.get("processFlag"));
*/
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), transfer);
		JSONObject saleJson =JSONObject.parseObject(JSON.toJSONString(saleGoods));
		if(saleJson.get("escaleFlag")!=null && saleJson.getBoolean("escaleFlag")) {
			saleJson.put("escaleFlag",1);
		}else {
			saleJson.put("escaleFlag",0);
		}
		result.put("sumInfo", saleJson);
		result.put("total_results", transfer.size());
		return ServiceResponse.buildSuccess(result);
	}

	// 可选商品列表
	@Override
	public ServiceResponse selectiveGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		// 动态指定查询字段
		String fields = paramsObject.getString("fields") == null ? "*" : paramsObject.getString("fields");
		paramsObject.put("fields", " " + fields + " ");
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		List<SaleGoodsModel> selectiveGoods = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.selectiveGoods", paramsObject);
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(selectiveGoods));
		List<RowMap> jsonList = JSONObject.parseArray(array.toJSONString(), RowMap.class);

		long total_results = 0;
		if (!selectiveGoods.isEmpty()) {
			total_results = template.getSqlSessionTemplate()
					.selectOne("beanmapper.SaleGoodsModelMapper.countSelectiveGoods", paramsObject);
			// 执行查询插件处理 钱海兵
			List<AnnotationService> tagPlugins = this.getPlugins();
			for (AnnotationService plugin : tagPlugins) {
				try {
					plugin.onAction(session, jsonList, OperationFlag.afterQuery, this.getBeanClass());
				} catch (Exception e) {
					return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "查询前插件执行异常:{0}",
							e.getMessage() + "");
				}
			}
		}
		paramsObject.clear();
		paramsObject.put("saleGoods", jsonList);
		paramsObject.put("total_results", total_results);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse stepAdd(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 对包装数量 进行 0-1 数字校验
		// 对步长范围进行0-100 校验
		// 步长差异范围stepDiff页面是整形，数据库是小数。需要转义
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);

		if (!paramsObject.containsKey("barNo")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品条码不能为空");
		}
		if (!paramsObject.containsKey("shopId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "门店ID不能为空");
		}

		if (!StringUtils.isEmpty(paramsObject.get("stepDiff"))) {
			// 步长差异范围stepDiff页面是整形，数据库是小数。需要转义
			Float stepDiff = Float.parseFloat(paramsObject.get("stepDiff").toString());// 步长差异范围
			paramsObject.put("stepDiff", stepDiff / 100);
		}
		paramsObject.put("entId", session.getEnt_id());
		return super.onUpdate(session, paramsObject);
	}

	// 商品步长设置-删除
	public ServiceResponse stepDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		if (paramsObject.containsKey(this.getCollectionName())) {
			Object dataObject = paramsObject.get(this.getCollectionName());
			if (dataObject != null && dataObject instanceof List) {
				FMybatisTemplate template = this.getTemplate();
				template.onSetContext(session);
				template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.stepDelete", dataObject);
				// 解析格式：{"collectionname":[...]}
			}
			return ServiceResponse.buildSuccess("success");

		} else {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_ERROR, "传入参数格式错误");
		}
	}

	@Transactional
	public ServiceResponse stepUpdate(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 对包装数量 进行 0-1 数字校验
		// 对步长范围进行0-100 校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"ssgid");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode()))
			return result;

		// 步长差异范围stepDiff页面是整形，数据库是小数。需要转义
		if (!StringUtils.isEmpty(paramsObject.get("stepDiff"))) {
			paramsObject.put("stepDiff", paramsObject.getInteger("stepDiff") / 100);
		}
		// 1.更新商品销售表
		this.setUpsert(false);
		this.onUpdate(session, paramsObject);
		// 2.更新经营配置表
		Object ssgid = paramsObject.get("ssgid");
		Criteria criteria = Criteria.where("ssgid").is(ssgid);
		Query query = new Query(criteria);
		SaleGoodsModel saleGoods = this.getTemplate().selectOne(query, SaleGoodsModel.class, "salegoods");
		paramsObject.put("sgid", saleGoods.getSgid());
		paramsObject.put("shopId", saleGoods.getShopId());
		paramsObject.put("erpCode", saleGoods.getErpCode());
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateStepDiff",
				paramsObject);
		return ServiceResponse.buildSuccess("success");
	}

	// pos 总台
	public ServiceResponse getSelfGoodsByShopCode(ServiceSession session, JSONObject paramsObject) {
		List<String> mustkey = Arrays.asList("erpCode", "goods");
		for (String key : mustkey) {

			if (!paramsObject.containsKey(key)) {
				System.out.println(key);
				logger.error(String.format("参数必须包括 %s", key));
				return ServiceResponse.buildFailure(session, String.format("参数必须包括", key));
			}
		}
		if (paramsObject.get("erpCode") == null) {
			logger.error("the erpCode is null");
			return ServiceResponse.buildFailure(session, "the erpCode is null");
		}
		if (!paramsObject.containsKey("imageType")) {
			paramsObject.put("imageType", 1);
		}
		String erpCode = String.valueOf(paramsObject.get("erpCode"));
		if (paramsObject.containsKey("mkt")) {

		}
		List<String> mustField = Arrays.asList("goodsDetail");
		List<String> goodsCodeList = new ArrayList<>();

		List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("goods");
		JSONObject queryParams = new JSONObject();
		for (JSONObject tmpParamsObject : paramsList) {
			for (String field : mustField) {
				if (!tmpParamsObject.containsKey(field)) {
					logger.error(String.format("参数必须包括 %s", field));
					return ServiceResponse.buildFailure(session, String.format("参数必须包括 %s", field));
				}
			}

			List<JSONObject> goodsDetail = (List<JSONObject>) tmpParamsObject.get("goodsDetail");
			for (JSONObject tmpGoodsDetail : goodsDetail) {
				if (!tmpGoodsDetail.containsKey("goodCode")) {
					return ServiceResponse.buildFailure(session, String.format("参数必须包括 %s ", "goodCode"));
				}
				String goodsCodeSrt = tmpGoodsDetail.getString("goodCode");
				String[] goodsCodeArray = goodsCodeSrt.split(",");
				if (goodsCodeArray.length == 0) {
					logger.error("goodsDetail is error");
					return ServiceResponse.buildFailure(session, "goodsDetail is error");
				}
				for (int i = 0; i < goodsCodeArray.length; i++) {
					if (StringUtils.isEmpty(goodsCodeArray[i])) {
						continue;
					}
					if (!goodsCodeList.contains(goodsCodeArray[i])) {
						goodsCodeList.add(goodsCodeArray[i]);
					}
				}
				tmpGoodsDetail.put("goodsCode", goodsCodeArray);

			}
		}
		if (goodsCodeList.size() < 1) {
			logger.error("the goodsCode is null");
			return ServiceResponse.buildFailure(session, "the goodsCode is null");
		}
		queryParams.put("erpCode", erpCode);
		queryParams.put("entId", session.getEnt_id());
		queryParams.put("goodsCode", goodsCodeList);
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<java.util.Map> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.GoodsModelMapper.getGoodsByShopCode", queryParams);
		for (JSONObject tmpResultObject : paramsList) {
			//
			List<JSONObject> goodsDetail = (List<JSONObject>) tmpResultObject.get("goodsDetail");
			for (JSONObject tmpGoodsDetailObject : goodsDetail) {
				List<JSONObject> goodsList = new ArrayList<>();
				String[] tmpGoodsCodeList = (String[]) tmpGoodsDetailObject.get("goodsCode");
				for (String code : tmpGoodsCodeList) {
					JSONObject tmpGoodsObject = new JSONObject();
					List<String> proList = new ArrayList<>();
					tmpGoodsObject.put("goodsCode", code);
					tmpGoodsObject.put("goodsURL", "");
					tmpGoodsObject.put("goodProperty", proList);
					tmpGoodsObject.put("goodsName", "");
					// 待优化
					for (Map tmpResultMap : list) {
						if (tmpResultMap.get("goodsCode").equals(code)) {
							tmpGoodsObject.put("goodsName", tmpResultMap.get("goodsName"));
							tmpGoodsObject.put("goodsUrl", tmpResultMap.get("imageUrl"));
							if (tmpResultMap.get("propertyName") != null) {
								proList.add((String) tmpResultMap.get("propertyName"));
							}
						}
					}
					goodsList.add(tmpGoodsObject);
				}
				tmpGoodsDetailObject.put("goods", goodsList);
				tmpGoodsDetailObject.remove("goodCode");

			}
		}
		JSONObject rtObject = new JSONObject();
		rtObject.put("selfGoods", paramsList);
		return ServiceResponse.buildSuccess(rtObject);
	}

	public ServiceResponse getStallByShopCode(ServiceSession session, JSONObject paramsObject) {
		List<String> mustkey = Arrays.asList("erpCode", "goods", "mkt");
		for (String key : mustkey) {
			if (!paramsObject.containsKey(key)) {
				logger.error(String.format("参数必须包括", key));
				return ServiceResponse.buildFailure(session, String.format("参数必须包括", key));
			}
		}
		if (paramsObject.get("erpCode") == null) {
			logger.error("the erpCode is null");
			return ServiceResponse.buildFailure(session, "the erpCode is null");
		}
		String erpCode = String.valueOf(paramsObject.get("erpCode"));
		if (paramsObject.get("mkt") == null) {
			logger.error("the mkt is null");
			return ServiceResponse.buildFailure(session, "the mkt is null");
		}
		String shopCode = String.valueOf(paramsObject.get("mkt"));
		List<String> mustField = Arrays.asList("goodsDetail", "stallCode");
		List<String> goodsCodeList = new ArrayList<>();
		List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("goods");
		JSONObject queryParams = new JSONObject();
		for (JSONObject tmpParamsObject : paramsList) {
			for (String field : mustField) {
				if (!tmpParamsObject.containsKey(field)) {
					logger.error(String.format("参数必须包括", field));
					return ServiceResponse.buildFailure(session, String.format("参数必须包括", field));
				}
			}

			String goodsDetailStr = (String) tmpParamsObject.get("goodsDetail");
			String[] goodsCodeArray = goodsDetailStr.split(",");
			if (goodsCodeArray.length == 0) {
				logger.error("goodsDetail is error");
				return ServiceResponse.buildFailure(session, "goodsDetail is error");

			}
			for (int i = 0; i < goodsCodeArray.length; i++) {
				if (StringUtils.isEmpty(goodsCodeArray[i])) {
					continue;
				}
				if (!goodsCodeList.contains(goodsCodeArray[i])) {
					goodsCodeList.add(goodsCodeArray[i]);
				}
			}
			tmpParamsObject.put("goodsCode", goodsCodeArray);
		}
		if (goodsCodeList.size() < 1) {
			logger.error("the goodsCode is null");
			return ServiceResponse.buildFailure(session, "the goodsCode is null");
		}
		queryParams.put("erpCode", erpCode);
		queryParams.put("entId", session.getEnt_id());
		queryParams.put("goodsCode", goodsCodeList);
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<java.util.Map> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.GoodsModelMapper.getStallByShopCode", queryParams);
		for (JSONObject tmpResultObject : paramsList) {
			//
			List<JSONObject> goodsList = new ArrayList<>();
			String[] tmpGoodsCodeList = (String[]) tmpResultObject.get("goodsCode");
			JSONObject queryStall = new JSONObject();
			queryStall.put("erpCode", erpCode);
			queryStall.put("stallCode", tmpResultObject.get("stallCode"));
			queryStall.put("shopCode", shopCode);
			getStallNameByCode(session, queryStall, tmpResultObject);
			for (String code : tmpGoodsCodeList) {
				JSONObject tmpGoodsObject = new JSONObject();
				tmpGoodsObject.put("goodsCode", code);
				tmpGoodsObject.put("goodsName", "");
				// 待优化
				for (Map tmpResultMap : list) {
					if (tmpResultMap.get("goodsCode").equals(code)) {
						tmpGoodsObject.put("goodsName", tmpResultMap.get("goodsName"));
					}
				}
				goodsList.add(tmpGoodsObject);
			}
			tmpResultObject.put("goodsDetail", goodsList);
			tmpResultObject.remove("goodsCode");

		}
		JSONObject rtObject = new JSONObject();
		rtObject.put("stallGoods", paramsList);
		return ServiceResponse.buildSuccess(rtObject);
	}

	private void getStallNameByCode(ServiceSession session, JSONObject queryParams, JSONObject tmpResultObject) {
		ServiceResponse ssResponse = stallInfoService.onQuery(session, queryParams);
		if (!ssResponse.getReturncode().equals(ResponseCode.SUCCESS)) {
			logger.error("查询档口失败");
		}
		JSONObject ssData = (JSONObject) ssResponse.getData();
		List<RowMap> ssList = (List<RowMap>) ssData.get(stallInfoService.getCollectionName());
		String stallName = null;
		String stallCode = null;
		if (ssList.size() < 1) {
			logger.error("查询不到档口信息");
		} else {
			stallName = (String) ssList.get(0).get("stallName");
			stallCode = (String) ssList.get(0).get("stallCode");
		}
		tmpResultObject.put("stallName", stallName);
		tmpResultObject.put("stallCode", stallCode);

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
				saleGoodsMap.put("tcShopId", saleGoods.getShopId());
				saleGoodsMap.put("tcShopCode", saleGoods.getShopCode());
				saleGoodsMap.put("tcCode", saleGoods.getGoodsCode());
				saleGoodsMap.put("tcName", saleGoods.getGoodsName());
				saleGoodsMap.put("tcDisplayName", saleGoods.getGoodsDisplayName());
				saleGoodsMap.put("tcSalePrice", saleGoods.getSalePrice());
				saleGoodsMap.put("tcGoodsStatus", saleGoods.getGoodsStatus());
				saleGoodsMap.put("tcCategoryCode", saleGoods.getCategoryCode());
				saleGoodsMap.put("tcCategoryName", saleGoods.getCategoryName());
				saleGoodsMap.put("tcErpCode", saleGoods.getErpCode());
				saleGoodsMap.put("tcErpName", saleGoods.getErpName());
				saleGoodsMap.put("tcEnSname", saleGoods.getEnSname());
				saleGoodsMap.put("tcStallCode", saleGoods.getStallCode());
				resultList.add(saleGoodsMap);

				JSONObject paramsObj = new JSONObject();
				paramsObj.put("sgoodsId", saleGoods.getSsgid());
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

	/**
	 * 导出--档口套餐
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse exportAllMealDetail(ServiceSession session, JSONObject paramsObject) {

		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject, "erpCode", "shopCode", "stallCode", "goodsType");
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);

		// 设置默认分页参数
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
		if (!paramsObject.containsKey("page_no")) {
			paramsObject.put("page_no", 0);
		} else {
			paramsObject.put("page_no",
					(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
		}

		String erpCode = paramsObject.getString("erpCode");
		String shopCode = paramsObject.getString("shopCode");
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());

		// 查询档口套餐
		List<JSONObject> list = new ArrayList<>(); // 存最终结果
		JSONObject result = null;
		List tcgoodsList = template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.getTCGoods",
				paramsObject);
		if (tcgoodsList != null && !tcgoodsList.isEmpty()) {
			// 数据库对象List<Map>转换List<Model>对象
			JSONArray array = JSONArray.parseArray(JSON.toJSONString(tcgoodsList));
			List<JSONObject> listGoods = JSONArray.parseArray(array.toJSONString(), JSONObject.class);
			// list.addAll(listGoods);
			List<SaleGoodsModel> modelList = JSONArray.parseArray(array.toJSONString(), SaleGoodsModel.class);

			// 查询套餐类型
			List<String> goodsCodeList = new ArrayList<>(); // 存放查到的套餐编码
			goodsCodeList = modelList.stream().map(SaleGoodsModel::getGoodsCode).collect(Collectors.toList());
			List<JSONObject> tcTypeList = new ArrayList<>();
			if (!goodsCodeList.isEmpty()) {
				result = new JSONObject();
				result.put("entId", session.getEnt_id());
				result.put("erpCode", erpCode);
				result.put("shopCode", shopCode);
				result.put("goodsCode", goodsCodeList);
				List tcTypegoodsList = template.getSqlSessionTemplate()
						.selectList("beanmapper.SetMealTypeRefModelMapper.selectTCMealType", result);
				if (tcTypegoodsList != null && !tcTypegoodsList.isEmpty()) {
					JSONArray array1 = JSONArray.parseArray(JSON.toJSONString(tcTypegoodsList));
					tcTypeList = JSONArray.parseArray(array1.toJSONString(), JSONObject.class);
				}
			}

			// 查询套餐明细
			List<Long> ssgidList = new ArrayList<>(); // 存放套餐商品id
			ssgidList = modelList.stream().map(SaleGoodsModel::getSsgid).collect(Collectors.toList());
			List<JSONObject> tcDetailList = new ArrayList<>();
			if (!ssgidList.isEmpty()) {
				result = new JSONObject();
				result.put("entId", session.getEnt_id());
				result.put("erpCode", erpCode);
				result.put("shopCode", shopCode);
				result.put("sgoodsId", ssgidList);
				List tcDetailgoodsList = template.getSqlSessionTemplate()
						.selectList("beanmapper.SetMealDetailModelMapper.selectTCMealDetail", result);
				if (tcDetailgoodsList != null && !tcDetailgoodsList.isEmpty()) {
					JSONArray array2 = JSONArray.parseArray(JSON.toJSONString(tcDetailgoodsList));
					tcDetailList = JSONArray.parseArray(array2.toJSONString(), JSONObject.class);
				}
			}

			// 数据整理
			for (JSONObject tcGoods : listGoods) {// 套餐商品
				int goodsStatus = tcGoods.get("goodsStatus") == null ? 3 : tcGoods.getInteger("goodsStatus");
				if (goodsStatus == 0) {
					tcGoods.put("goodsStatus", "禁用");
				} else if (goodsStatus == 3) {
					tcGoods.put("goodsStatus", "启用");
				}
				list.add(tcGoods);

				String tcCode = tcGoods.getString("goodsCode"); // 套餐编码
				for (JSONObject tcType : tcTypeList) { // 套餐商品的种类
					String tcTypeCode = tcType.getString("tcCode"); // 套餐编码
					if (!StringUtils.isEmpty(tcCode) && tcCode.equals(tcTypeCode)) {
						list.add(tcType);
					}
					String typeName = tcType.getString("typeName"); // 套餐种类名称
					for (JSONObject tcDetail : tcDetailList) { // 套餐商品明细
						String tcDetailCode = tcDetail.getString("tcCode"); // 套餐编码
						String tcDetailTypeName = tcDetail.getString("tcTypeName"); // 套餐种类名称
						if (!StringUtils.isEmpty(tcCode) && tcCode.equals(tcDetailCode)
								&& !StringUtils.isEmpty(typeName) && typeName.equals(tcDetailTypeName)) {
							list.add(tcDetail);
						}
					}
				}
			}

		}
		// 最终结果
		result = new JSONObject();
		result.put(this.getCollectionName(), list);
		result.put("total_results", list.size());
		return ServiceResponse.buildSuccess(result);
	}

	// 子品经营配置 - 商品查询列表
	@Override
	public ServiceResponse searchGoods(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse checkResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"shopId", "erpCode", "singleItemFlag");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(checkResult.getReturncode()))
			return checkResult;
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);

		paramsObject = DefaultParametersUtils.transformParam(session, paramsObject, false, false);
		FMybatisTemplate template = this.getTemplate();
		List<SaleGoodsModel> result = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.parentGoodsSearch", paramsObject);
		long count = 0;
		if (result != null && result.size() > 0) {
			count = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.parentGoodsSearchCount",
					paramsObject);
		}
		paramsObject.clear();
		paramsObject.put("saleGoods", result);
		paramsObject.put("total_results", count);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 子品经营配置 - 母品查询子品
	public ServiceResponse searchSonGoodsByParentCode(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse checkResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"parentGoodsCode", "erpCode", "shopId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(checkResult.getReturncode()))
			return checkResult;
		// 经营配置查询子品
		List shopRefGoodsList = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.searchSonGoodsByParentCode", paramsObject);
		paramsObject.clear();
		paramsObject.put("saleGoods", shopRefGoodsList);
		paramsObject.put("total_results", shopRefGoodsList.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}

	// 子品经营配置 - 配置经营配置（保存）1正常/6暂停经营
	@Transactional
	public ServiceResponse saveSonGoodsRef(ServiceSession session, JSONObject paramsObject) {
		String nowDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Long userId = session.getUser_id();

		JSONArray saleGoods = (JSONArray) paramsObject.get("saleGoods");
		for (Object object : saleGoods) {
			JSONObject saleGood = (JSONObject) object;
			String goodsStatus = saleGood.getString("goodsStatus");
			String afterGoodsStatus = saleGood.getString("afterGoodsStatus");
			if (goodsStatus.equals(afterGoodsStatus) && goodsStatus.equals("1")) {// 更新（从选中到选中）
				// 更新经营配置(更新操作只会更新salePrice)
				paramsObject.clear();
				paramsObject.put("gsrid", saleGood.get("gsrid"));
				paramsObject.put("modifier", userId);
				paramsObject.put("updateDate", nowDateString);
				paramsObject.put("salePrice", saleGood.get("salePrice"));
				goodsShopRefServiceImpl.setUpsert(false);
				ServiceResponse response = goodsShopRefServiceImpl.onUpdate(session, paramsObject);
				if (!ResponseCode.SUCCESS.equals(response.getReturncode()))
					return response;
				// 更新可售商品
				this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateSonSaleGoods",
						saleGood);
			} else if (goodsStatus.equals("1") && afterGoodsStatus.equals("6")) {// 删除（从选中到取消）
				// 更新经营配置状态为6暂停经营(只更新状态)
				paramsObject.clear();
				paramsObject.put("goodsStatus", 6);
				paramsObject.put("gsrid", saleGood.get("gsrid"));
				goodsShopRefServiceImpl.setUpsert(false);
				ServiceResponse response = goodsShopRefServiceImpl.onUpdate(session, paramsObject);
				if (!ResponseCode.SUCCESS.equals(response.getReturncode()))
					return response;
				// 删除可售销售表
				this.getTemplate().getSqlSessionTemplate().delete("beanmapper.SaleGoodsModelMapper.deleteSonSaleGoods",
						saleGood);
			} else if (goodsStatus.equals("6") && afterGoodsStatus.equals("1")) {// 增加（从取消到选中）
				// 更新经营配置状态为1可用
				paramsObject.clear();
				paramsObject.put("goodsStatus", 1);
				paramsObject.put("gsrid", saleGood.get("gsrid"));
				paramsObject.put("salePrice", saleGood.get("salePrice"));
				goodsShopRefServiceImpl.setUpsert(false);
				ServiceResponse response = goodsShopRefServiceImpl.onUpdate(session, paramsObject);
				if (!ResponseCode.SUCCESS.equals(response.getReturncode()))
					return response;
				// 增加可售商品表
				Criteria criteria = Criteria.where("goodsCode").is(saleGood.getString("parentGoodsCode")).and("erpCode")
						.is(saleGood.getString("erpCode"));
				Query query = new Query(criteria);
				GoodsModel goods = this.getTemplate().selectOne(query, GoodsModel.class, "goods");// 母品ID
				if (goods == null) {
					return ServiceResponse.buildFailure(session, "查询不到商品");
				}
				JSONObject newSonJson = JSON.parseObject(JSON.toJSONString(goods));
				newSonJson.put("goodsName", saleGood.get("goodsName"));
				newSonJson.put("barNo", saleGood.get("barNo"));
				newSonJson.put("salePrice", saleGood.get("salePrice"));
				newSonJson.put("sgid", saleGood.get("sgid"));
				newSonJson.put("goodsCode", saleGood.get("goodsCode"));
				newSonJson.put("createDate", nowDateString);
				newSonJson.put("parentGoodsCode", saleGood.get("parentGoodsCode"));
				newSonJson.put("psgid", goods.getSgid());
				newSonJson.put("gsrid", saleGood.get("gsrid"));
				newSonJson.put("goodsType", GoodsType.ZP.getValue());// 商品类型
				newSonJson.put("directFromErp", false);// 是否是ERP的数据来源
				newSonJson.put("singleItemFlag", true);// 是否是单品
				response = this.onInsert(session, newSonJson);
				if (!ResponseCode.SUCCESS.equals(response.getReturncode()))
					return response;
			}
		}
		return ServiceResponse.buildSuccess("success");
	}

	@Override
	public ServiceResponse searchAllZbmsp(ServiceSession session, JSONObject paramsObject) {
		FMybatisTemplate template = this.getTemplate();
		List<SaleGoodsItemsModel> saleGoodsItemsList = null;
		ServiceResponse response = super.onQuery(session, paramsObject);
		if (response.getReturncode() != null && response.getReturncode().equals("0")) {
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			List<SaleGoodsModel> saleGoodsList = JSONArray
					.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), SaleGoodsModel.class);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			Criteria criteria = null;
			for (SaleGoodsModel saleGoods : saleGoodsList) {
				criteria = Criteria.where("gsgid").is(saleGoods.getSgid());
				Query query = new Query(criteria);
				query.limit(Integer.MAX_VALUE);
				saleGoodsItemsList = template.select(query, SaleGoodsItemsModel.class, "salegoodsitems");

				if (saleGoodsItemsList.size() > 0) {
					Map<String, Object> saleGoodsMap = new HashMap<String, Object>();// 虚拟母品表头
					saleGoodsMap.put("zbmSgid", saleGoods.getSgid());
					saleGoodsMap.put("zbmCategoryId", saleGoods.getCategoryId());
					saleGoodsMap.put("zbmCategoryName", saleGoods.getCategoryName());
					saleGoodsMap.put("zbmGoodsCode", saleGoods.getGoodsCode());
					saleGoodsMap.put("zbmGoodsName", saleGoods.getGoodsName());
					saleGoodsMap.put("zbmGoodsType", saleGoods.getGoodsType());
					saleGoodsMap.put("zbmBarNo", saleGoods.getBarNo());
					saleGoodsMap.put("erpCode", saleGoods.getErpCode());
					saleGoodsMap.put("zbmSalePrice", saleGoods.getSalePrice());
					saleGoodsMap.put("shopCode", saleGoods.getShopCode());
					saleGoodsMap.put("shopId", saleGoods.getShopId());
					saleGoodsMap.put("shopName", saleGoods.getShopName());
					saleGoodsMap.put("zbmSsgid", saleGoods.getSsgid());
					resultList.add(saleGoodsMap);

					for (SaleGoodsItemsModel sgi : saleGoodsItemsList) {
						saleGoodsMap = new HashMap<String, Object>();// 商品明细
						saleGoodsMap.put("gsgid", sgi.getGsgid());// 组包码ID
						saleGoodsMap.put("ggoodsCode", sgi.getGgoodsCode());// 组包码编码
						saleGoodsMap.put("goodsCode", sgi.getGoodsCode());// 商品编码
						criteria = Criteria.where("ssgid").is(sgi.getSsgid()).and("entId").is(sgi.getEntId());
						query = new Query(criteria);
						query.fields().include("ssgid");
						query.fields().include("goodsName");
						SaleGoodsModel sg = template.selectOne(query, SaleGoodsModel.class, "salegoods");
						if (sg != null) {
							saleGoodsMap.put("goodsName", sg.getGoodsName());// 商品名称
						}
						saleGoodsMap.put("salePrice", sgi.getSalePrice());// 零售价
						saleGoodsMap.put("barNo", sgi.getBarNo());// 条码
						saleGoodsMap.put("num", sgi.getNum());// 数量
						saleGoodsMap.put("discountShareRate", sgi.getDiscountShareRate());// 折扣分摊比例
						saleGoodsMap.put("sgiid", sgi.getSgiid());// 商品项ID
						saleGoodsMap.put("ssgid", sgi.getSsgid());// 可售商品ID
						resultList.add(saleGoodsMap);
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

	@Override
	public ServiceResponse searchAllXnmp(ServiceSession session, JSONObject paramsObject) {
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<SaleGoodsItemsModel> saleGoodsItemsList = null;
		ServiceResponse response = super.onQuery(session, paramsObject);
		if (response.getReturncode() != null && response.getReturncode().equals("0")) {
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			List<SaleGoodsModel> saleGoodsList = JSONArray
					.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), SaleGoodsModel.class);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (SaleGoodsModel saleGoods : saleGoodsList) {
				Map<String, Object> saleGoodsMap = new HashMap<String, Object>();// 组包码商品表头
				saleGoodsMap.put("xnmpSgid", saleGoods.getSgid());
				saleGoodsMap.put("xnmpCategoryId", saleGoods.getCategoryId());
				saleGoodsMap.put("xnmpCategoryName", saleGoods.getCategoryName());
				saleGoodsMap.put("xnmpGoodsCode", saleGoods.getGoodsCode());
				saleGoodsMap.put("xnmpGoodsName", saleGoods.getGoodsName());
				saleGoodsMap.put("xnmpGoodsType", saleGoods.getGoodsType());
				saleGoodsMap.put("xnmpBarNo", saleGoods.getBarNo());
				saleGoodsMap.put("erpCode", saleGoods.getErpCode());
				saleGoodsMap.put("xnmpSalePrice", saleGoods.getSalePrice());
				saleGoodsMap.put("shopCode", saleGoods.getShopCode());
				saleGoodsMap.put("shopId", saleGoods.getShopId());
				saleGoodsMap.put("shopName", saleGoods.getShopName());
				saleGoodsMap.put("xnmpSsgid", saleGoods.getSsgid());
				resultList.add(saleGoodsMap);

				Criteria criteria = Criteria.where("gsgid").is(saleGoods.getSgid());
				Query query = new Query(criteria);
				query.limit(Integer.MAX_VALUE);
				saleGoodsItemsList = template.select(query, SaleGoodsItemsModel.class, "salegoodsitems");

				for (SaleGoodsItemsModel sgi : saleGoodsItemsList) {
					saleGoodsMap = new HashMap<String, Object>();// 商品明细
					saleGoodsMap.put("gsgid", sgi.getGsgid());// 组包码ID
					saleGoodsMap.put("ggoodsCode", sgi.getGgoodsCode());// 组包码编码
					saleGoodsMap.put("goodsCode", sgi.getGoodsCode());// 商品编码
					criteria = Criteria.where("ssgid").is(sgi.getSsgid()).and("entId").is(sgi.getEntId());
					query = new Query(criteria);
					query.fields().include("ssgid");
					query.fields().include("goodsName");
					SaleGoodsModel sg = template.selectOne(query, SaleGoodsModel.class, "salegoods");
					if (sg != null) {
						saleGoodsMap.put("goodsName", sg.getGoodsName());// 商品名称
					}
					saleGoodsMap.put("salePrice", sgi.getSalePrice());// 零售价
					saleGoodsMap.put("barNo", sgi.getBarNo());// 条码
					saleGoodsMap.put("sgiid", sgi.getSgiid());// 商品项ID
					saleGoodsMap.put("ssgid", sgi.getSsgid());// 可售商品ID
					resultList.add(saleGoodsMap);
				}

			}
			JSONObject result = new JSONObject();
			result.put(this.getCollectionName(), resultList);
			result.put("total_results", data.get("total_results"));
			return ServiceResponse.buildSuccess(result);
		}
		return response;
	}

	@Autowired
	JedisPoolConfig jedisPoolConfig;

	// POS总部初始化常购商品
	public ServiceResponse getSelfGoodsByShopCodeTmp(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse check = checkParam(session, paramsObject, "erpCode", "goods", "mkt", "goodsDetail", "goodCode");
		if (!check.getReturncode().equals(ResponseCode.SUCCESS)) {
			return check;
		}
		if (!paramsObject.containsKey("imageType")) {
			paramsObject.put("imageType", 1);
		}
		String erpCode = String.valueOf(paramsObject.get("erpCode"));
		String shopCode = paramsObject.getString("mkt");
		List<String> mustField = Arrays.asList("goodsDetail");
		List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("goods");
		JSONObject queryParams = new JSONObject();
		List<String> goodsCodeList = new ArrayList<>();
		for (JSONObject tmpParamsObject : paramsList) {
			List<JSONObject> goodsDetail = (List<JSONObject>) tmpParamsObject.get("goodsDetail");
			for (JSONObject tmpGoodsDetail : goodsDetail) {
				String goodsCodeSrt = tmpGoodsDetail.getString("goodCode");
				String[] goodsCodeArray = goodsCodeSrt.split(",");
				List<String> goodsTmpList = new ArrayList<>();
				goodsTmpList = Arrays.stream(goodsCodeArray).filter(StringUtils::hasText).distinct().collect(toList());
				goodsCodeList.addAll(goodsTmpList);
				tmpGoodsDetail.put("goodsCode", goodsTmpList);
			}
		}
		goodsCodeList = goodsCodeList.stream().distinct().collect(toList());
		if (goodsCodeList.size() < 1) {
			logger.error("the goodcode is null");
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "the goodcode is null");
		}
		List<java.util.Map> list = new ArrayList<>();
		queryParams.put("erpCode", erpCode);
		queryParams.put("entId", session.getEnt_id());
		queryParams.put("goodsCode", goodsCodeList);
		queryParams.put("shopCode", shopCode);
		queryParams.put("imageType", paramsObject.get("imageType"));
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		// list =
		// template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.getSelfGoodsByGoodsBarNo",
		// queryParams);
		//
		// List<java.util.Map> listGoodsCode =
		// template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.getSelfGoodsByGoodsCode",
		// queryParams);
		// 2019-04-25 需求变更：条码编码都需要查，然后去重，去重规则 取主条码的数据，如果任然有多条，则根据updateDate 取最新的一条
		list = queryByGoodsCodeOrBarNo(session, queryParams, "getSelfGoodsByGoodsBarNo", "getSelfGoodsByGoodsCode");
		for (JSONObject tmpResultObject : paramsList) {
			List<JSONObject> goodsDetail = (List<JSONObject>) tmpResultObject.get("goodsDetail");
			for (JSONObject tmpGoodsDetailObject : goodsDetail) {
				List<JSONObject> goodsList = new ArrayList<>();
				List<String> tmpGoodsCodeList = (List<String>) tmpGoodsDetailObject.get("goodsCode");
				// 如果传参中商品展示名称不为空，则获取展示名称
				String[] tmpGoodsDispNames = new String[] {};
				if (tmpGoodsDetailObject.containsKey("goodDisplayName")) {
					String tmpGoodsStr = tmpGoodsDetailObject.getString("goodDisplayName");
					tmpGoodsDispNames = tmpGoodsStr.split(",");
				}

				int pos = 0;
				for (String code : tmpGoodsCodeList) {
					// 标识 表示为 商品编码为code 是否能查询到商品，如果查询不到， 则给一个空的object
					boolean containGoods = false;
					for (Map tmpResultMap : list) {
						if (code.equals(tmpResultMap.get("goodsCode")) || code.equals(tmpResultMap.get("barNo"))) {
							List<String> proList = new ArrayList<>();
							// 该标识 标识，goods中是否已经有相同条码的商品，如果有相同的商品，忽略该条记录，只需要取其商品属性
							boolean hasSameGoods = false;
							for (JSONObject goodsObject : goodsList) {
								if (goodsObject.get("barNo").equals(tmpResultMap.get("barNo"))
										&& goodsObject.get("goodsCode").equals(tmpResultMap.get("goodsCode"))) {
									hasSameGoods = true;
									containGoods = true;
									if (goodsObject.containsKey("goodProperty")
											&& tmpResultMap.get("propertyName") != null) {
										proList = (List<String>) goodsObject.get("goodProperty");
										proList.add((String) tmpResultMap.get("propertyName"));
									}
								}
							}
							if (hasSameGoods)
								continue;
							JSONObject tmpGoodsObject = new JSONObject();
							tmpGoodsObject.put("goodProperty", proList);
							// 如果查询不到商品名称，则过滤
							if (!tmpResultMap.containsKey("goodsName"))
								continue;
							if (!tmpGoodsDetailObject.containsKey("goodDisplayName")) { // 如果没有传商品展示名称，则直接用主数据的商品名称
								tmpGoodsObject.put("goodsName", tmpResultMap.get("goodsName"));
							} else {
								// 如果pos总部传过来的显示名称设置得有问题，值是'null'（包含三种情况:NULL,'null',空格）, 则还是用主数据的商品名称
								if ("null".equals(tmpGoodsDispNames[pos])) {
									tmpGoodsObject.put("goodsName", tmpResultMap.get("goodsName"));
								} else {
									tmpGoodsObject.put("goodsName", tmpGoodsDispNames[pos]);
								}
							}
							if (tmpResultMap.containsKey("enSname"))
								tmpGoodsObject.put("enSname", tmpResultMap.get("enSname"));
							tmpGoodsObject.put("goodsURL", tmpResultMap.get("imageUrl"));
							tmpGoodsObject.put("salePrice", tmpResultMap.get("salePrice"));
							tmpGoodsObject.put("goodsCode", tmpResultMap.get("goodsCode"));
							tmpGoodsObject.put("barNo", tmpResultMap.get("barNo"));
							tmpGoodsObject.put("isExist", "1");
							// 如果商品没有英文名称，则用其中文名称
							tmpGoodsObject.put("enFname",
									tmpResultMap.containsKey("enFname") ? tmpResultMap.get("enFname")
											: tmpResultMap.get("goodsName"));
							if (tmpResultMap.get("propertyName") != null) {
								proList.add((String) tmpResultMap.get("propertyName"));
							}
							tmpGoodsObject.put("mainBarcodeFlag",
									tmpResultMap.containsKey("mainBarcodeFlag") ? tmpResultMap.get("mainBarcodeFlag")
											: 0);
							if (tmpResultMap.get("goodsType") != null) {
								tmpGoodsObject.put("goodsType", tmpResultMap.get("goodsType"));
							}
							tmpGoodsObject.put("erpCode", erpCode);
							goodsList.add(tmpGoodsObject);
							containGoods = true;

						}
					}
					if (containGoods == false) {
						// 表示该商品编码不存
						JSONObject emptyObject = new JSONObject();
						emptyObject.put("isExist", "0");
						emptyObject.put("goodsCode", code);
						emptyObject.put("barNo", code);
						emptyObject.put("goodsCode", code);
						goodsList.add(emptyObject);
					}
					pos = pos + 1;
				}
				if (goodsCodeList.size() == 1) {
					goodsList = goodsList.stream().map(x -> this.fileterGoods(session, x, shopCode)).distinct()
							.collect(toList());
				}
				tmpGoodsDetailObject.put("goods", goodsList);
				tmpGoodsDetailObject.remove("goodsCode");
			}
		}
		JSONObject rtObject = new JSONObject();
		rtObject.put("selfGoods", paramsList);
		return ServiceResponse.buildSuccess(rtObject);
	}

	public List<Map> queryByGoodsCodeOrBarNo(ServiceSession session, JSONObject queryParams, String sqlId1,
			String sqlId2) {
		sqlId1 = "beanmapper.SaleGoodsModelMapper." + sqlId1;
		sqlId2 = "beanmapper.SaleGoodsModelMapper." + sqlId2;
		List<Map> rtList = new ArrayList<>();
		List<Map> tmpList = new ArrayList<>();
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		rtList = template.getSqlSessionTemplate().selectList(sqlId1, queryParams);
		List<String> goodsCodes = (List<String>) queryParams.get("goodsCode");
		// List<Map> finalRtList = rtList;
		List<Map> finalRtList = rtList;
		List<String> noQueryGoodsCode = goodsCodes.stream().filter(x -> fileterGoodsCodes(x, finalRtList))
				.collect(toList());
		if(noQueryGoodsCode.size() > 0){
            JSONObject queryParams2 = (JSONObject) queryParams.clone();
            queryParams2.put("goodsCode", noQueryGoodsCode);
            tmpList = template.getSqlSessionTemplate().selectList(sqlId2, queryParams2);
            rtList.addAll(tmpList);
        }
		// 先根据 主条码过滤，然后根据 updateDate 过滤
		Map<Object, List<Map>> goodsList = rtList.stream().collect(groupingBy(x -> x.get("goodsCode")));
		List<Object> barNos = goodsList.values().stream().map(x -> {
			Map<String, Object> stringObjectMap = compareAndGetMap(x);
			return stringObjectMap.get("barNo");
		}).collect(Collectors.toList());
		rtList = rtList.stream().filter(x -> barNos.contains(x.get("barNo"))).collect(toList());
		return rtList;
	}

	public static boolean fileterGoodsCodes(String goodsCode, List<Map> goodsList) {
		for (Map goods : goodsList) {
			if (goodsCode.equals(goods.get("goodsCode")) || goodsCode.equals(goods.get("barNo"))) {
				return false;
			}
		}
		return true;
	}

	public JSONObject fileterGoods(ServiceSession session, JSONObject goodsObject, String shopCode) {
		/**
		 * 对商品过滤， 过滤规则，如果存在多个条码相同的商品，则 过滤掉该商品 @param goodsObject @return boolean @throws
		 */
		if ("0".equals(goodsObject.get("isExist")))
			return goodsObject;
		JSONObject paramsObject = new JSONObject();
		paramsObject.put("entId", session.getEnt_id());
		paramsObject.put("erpCode", goodsObject.get("erpCode"));
		paramsObject.put("barNo", goodsObject.get("barNo"));
		paramsObject.put("shopCode", shopCode);
		paramsObject.put("goodsStatus", Arrays.asList(1,2,3,4,5,10));
		ServiceResponse response = this.onQuery(session, paramsObject);
		try {
			if (ResponseCode.SUCCESS.equals(response.getReturncode())) {
				if (response.getData() instanceof JSONObject
						&& (long) ((JSONObject) response.getData()).get("total_results") > 1l) {
					logger.error("barNo---" + String.valueOf(goodsObject.get("barNo")) + "----存在多条");
					// 表示该商品编码不存
					JSONObject emptyObject = new JSONObject();
					emptyObject.put("isExist", "0");
					emptyObject.put("goodsCode", goodsObject.get("goodsCode"));
					return emptyObject;
				}
			}
		} catch (Exception e) {
			logger.error("查询商品错误" + e.getMessage());
		}
		return goodsObject;
	}

	//// POS总部初始化档口信息
	public ServiceResponse getStallByShopCodeTmp(ServiceSession session, JSONObject paramsObject) {
		// 加一个档口code 商品英文名称
		ServiceResponse check = checkParam(session, paramsObject, "erpCode", "goods", "mkt", "goodsDetail", "siid");
		if (!check.getReturncode().equals(ResponseCode.SUCCESS)) {
			return check;
		}
		if (!paramsObject.containsKey("imageType")) {
			paramsObject.put("imageType", 1);
		}
		String erpCode = String.valueOf(paramsObject.get("erpCode"));
		String shopCode = String.valueOf(paramsObject.get("mkt"));
		List<JSONObject> paramsList = (List<JSONObject>) paramsObject.get("goods");
		JSONObject queryParams = new JSONObject();
		List<String> goodsCodeList = new ArrayList<>();
		for (JSONObject tmpParamsObject : paramsList) {
			String goodsDetailStr = (String) tmpParamsObject.get("goodsDetail");
			String[] goodsCodeArray = goodsDetailStr.split(",");
			List<String> goodsTmpCodes = Arrays.stream(goodsCodeArray).filter(StringUtils::hasText).distinct()
					.collect(toList());
			goodsCodeList.addAll(goodsTmpCodes);
			tmpParamsObject.put("goodsCode", goodsTmpCodes);
		}
		goodsCodeList = goodsCodeList.stream().distinct().collect(toList());
		if (goodsCodeList.size() < 1) {
			logger.error("the goodcode is null");
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "the goodcode is null");
		}
		queryParams.put("erpCode", erpCode);
		queryParams.put("entId", session.getEnt_id());
		queryParams.put("goodsCode", goodsCodeList);
		queryParams.put("shopCode", shopCode);
		queryParams.put("imageType", paramsObject.get("imageType"));
		// 先根据条码查询
		// List<java.util.Map> list =
		// template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.getStallByBarNo",
		// queryParams);
		// if(list.size() < 1){
		//// 查询不到，再根据商品编码查询
		// list =
		// template.getSqlSessionTemplate().selectList("beanmapper.SaleGoodsModelMapper.getStallByGoodsCode",
		// queryParams);
		// }
		List<java.util.Map> list = queryByGoodsCodeOrBarNo(session, queryParams, "getStallByBarNo",
				"getStallByGoodsCode");
		for (JSONObject tmpResultObject : paramsList) {
			List<JSONObject> goodsList = new ArrayList<>();
			List<String> tmpGoodsCodeList = (List<String>) tmpResultObject.get("goodsCode");
			// 如果传参中商品展示名称不为空，则获取展示名称
			String[] tmpGoodsDispNames = new String[] {};
			if (tmpResultObject.containsKey("goodsDisplayName")) {
				String tmpGoodsStr = tmpResultObject.getString("goodsDisplayName");
				tmpGoodsDispNames = tmpGoodsStr.split(",");
			}

			JSONObject queryStall = new JSONObject();
			queryStall.put("erpCode", erpCode);
			queryStall.put("siid", tmpResultObject.get("siid"));
			queryStall.put("shopCode", shopCode);
			getStallNameByCode(session, queryStall, tmpResultObject);

			int pos = 0;
			for (String code : tmpGoodsCodeList) {
				JSONObject tmpGoodsObject = new JSONObject();
				tmpGoodsObject.put("goodsCode", code);
				tmpGoodsObject.put("barNo", code);
				tmpGoodsObject.put("goodsName", "");
				tmpGoodsObject.put("imageUrl", "");
				tmpGoodsObject.put("enFname", "");
				for (Map tmpResultMap : list) {
					if (code.equals(tmpResultMap.get("goodsCode")) || code.equals(tmpResultMap.get("barNo"))) {
						if (!tmpResultObject.containsKey("goodsDisplayName")) { // 如果没有传商品展示名称，则直接用主数据的商品名称
							tmpGoodsObject.put("goodsName", tmpResultMap.get("goodsName"));
						} else {
							// 如果pos总部传过来的显示名称设置得有问题，值是'null'（包含三种情况:NULL,'null',空格）, 则还是用主数据的商品名称
							if ("null".equals(tmpGoodsDispNames[pos])) {
								tmpGoodsObject.put("goodsName", tmpResultMap.get("goodsName"));
							} else {
								tmpGoodsObject.put("goodsName", tmpGoodsDispNames[pos]);
							}
						}
						tmpGoodsObject.put("goodsCode", tmpResultMap.get("goodsCode"));
						tmpGoodsObject.put("barNo", tmpResultMap.get("barNo"));
						tmpGoodsObject.put("enFname",
								tmpResultMap.get("enFname") != null ? tmpResultMap.get("enFname") : "");
						tmpGoodsObject.put("imageUrl",
								tmpResultMap.get("imageUrl") == null ? "" : tmpResultMap.get("imageUrl"));
					}
				}
				goodsList.add(tmpGoodsObject);
				pos = pos + 1;
			}
			tmpResultObject.put("goodsDetail", goodsList);
			tmpResultObject.remove("goodsCode");
		}
		JSONObject rtObject = new JSONObject();
		rtObject.put("stallGoods", paramsList);
		return ServiceResponse.buildSuccess(rtObject);
	}

	public ServiceResponse searchMultiSaleGoodsDetails(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("entId")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "entId不能为空");
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "erpCode不能为空");
		}

		if (!paramsObject.containsKey("searchType") || "1".equals(paramsObject.getString("searchType"))) {
			paramsObject.remove("orgCode");// 如果searchType不为2，则不需要验证柜组
		} else if (!paramsObject.containsKey("orgCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "orgCode不能为空");
		}

		if (!paramsObject.containsKey("goodsParamsArray")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
					"goodsParamsArray不能为空");
		}
		String fields = paramsObject.getString("fields");

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		DefaultParametersUtils.removeEmptyParams(paramsObject);
		JSONArray goodsParamsArray = paramsObject.getJSONArray("goodsParamsArray");
		paramsObject.remove("goodsParamsArray");

		List<Map<String, Object>> resultData = new ArrayList<>();
		for (int i = 0; i < goodsParamsArray.size(); i++) {
			JSONObject params = goodsParamsArray.getJSONObject(i);
			params.put("entId", session.getEnt_id());
			params.put("erpCode", paramsObject.get("erpCode"));
			if (!params.containsKey("shopCode")) {
				return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
						"goodsParamsArray.shopCode不能为空");
			}
			if (!params.containsKey("goodsCode") || params.getJSONArray("goodsCode").isEmpty()) {
				return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
						"goodsParamsArray.goodsCode不能为空");
			}

			List<java.util.Map<String, Object>> list = template.getSqlSessionTemplate()
					.selectList("beanmapper.SaleGoodsModelMapper.searchMultiSaleGoodsDetails", params);
			decorateResponseData(paramsObject, template, list);

			Map<String, Object> map = new HashMap<>();
			map.put("shopCode", params.get("shopCode"));
			map.put("info", DefaultParametersUtils.filterByFields(list, fields));
			resultData.add(map);
		}

		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), resultData);
		result.put("total_results", resultData.size());
		return ServiceResponse.buildSuccess(result);
	}

	private void decorateResponseData(JSONObject paramsObject, FMybatisTemplate template,
			List<java.util.Map<String, Object>> list) {
		List<Long> brandIds = new ArrayList<>();
		List<Long> sgids = new ArrayList<>();
		List<Long> categoryIds = new ArrayList<>();

		for (java.util.Map<String, Object> map : list) {
			if (map.get("partsNum") == null)
				map.put("partsNum", 0);

			if (map.get("enSName") == null)
				map.put("enSName", "");

			if (map.get("brandId") != null)
				brandIds.add(Long.parseLong(map.get("brandId").toString()));

			if (map.get("goodsId") != null)
				sgids.add(Long.parseLong(map.get("goodsId").toString()));

			if (map.get("categoryId") != null)
				categoryIds.add(Long.parseLong(map.get("categoryId").toString()));
		}

		// 添加品牌信息
		List<BrandInfoModel> brandInfo = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(brandIds)) {
			JSONObject parameter = new JSONObject();
			parameter.put("brandIds", brandIds);
			brandInfo = template.getSqlSessionTemplate().selectList("beanmapper.BrandInfoModelMapper.selectInBrandId",
					parameter);
		}

		// 添加商品信息
		List<GoodsModel> goodsInfo = new ArrayList<>();
		List<SaleGoodsImageRefModel> saleGoodsImageRef = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sgids)) {
			paramsObject.put("sgids", sgids);
			goodsInfo = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID",
					paramsObject);
			paramsObject.put("imageType", 1);
			saleGoodsImageRef = template.getSqlSessionTemplate()
					.selectList("beanmapper.SaleGoodsImageRefModelMapper.selectSinglePictureInSGID", paramsObject);
		}

		// 添加品类信息
		List<CategoryModel> categoryModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.put("categoryIds", categoryIds);
			categoryModel = template.getSqlSessionTemplate()
					.selectList("beanmapper.CategoryModelMapper.selectInCategoryIds", paramsObject);
		}

		// 添加品类属性信息
		for (Map<String, Object> map : list) {
			// 添加商品信息
			String partsUnit = "";
			String enFName = "";
			if (map.get("sgid") != null) {
				for (GoodsModel g : goodsInfo) {
					if (Long.parseLong(map.get("sgid").toString()) == g.getSgid()) {
						partsUnit = g.getPartsUnit();
						enFName = g.getEnFname();
						break;
					}
				}
			}
			map.put("partsUnit", partsUnit);
			map.put("enFName", enFName);

			String brandName = "";
			// 添加品牌信息
			if (map.get("brandId") != null) {
				for (BrandInfoModel b : brandInfo) {
					if (Long.parseLong(map.get("brandId").toString()) == b.getBrandId()) {
						brandName = b.getBrandName();
						break;
					}
				}
			}
			map.put("brandName", brandName);

			String categoryName = "";
			// 添加品类信息
			if (map.get("categoryId") != null) {
				for (CategoryModel c : categoryModel) {
					if (Long.parseLong(map.get("categoryId").toString()) == c.getCategoryId()) {
						categoryName = c.getCategoryName();
						break;
					}
				}
			}
			map.put("categoryName", categoryName);

			// 添加商品图片
			String imageUrl = "";
			if (map.get("goodsId") != null) {
				for (SaleGoodsImageRefModel r : saleGoodsImageRef) {
					if (Long.parseLong(map.get("goodsId").toString()) == r.getSgid()) {
						imageUrl = r.getImageUrl();
						break;
					}
				}
			}
			map.put("imageUrl", imageUrl);
		}
	}

	private ServiceResponse decorateResponseData4SaleGoodsDetail(ServiceSession session, JSONObject paramsObject,
			FMybatisTemplate template, List<java.util.Map<String, Object>> list) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:sss");
		String paramCode = paramsObject.getString("code");
		String erpCode = paramsObject.getString("erpCode");
		List<Long> sgids = new ArrayList<>();
		List<Long> categoryIdList = new ArrayList<>();

		for (java.util.Map<String, Object> map : list) {
			if (map.get("partsNum") == null)
				map.put("partsNum", 0);

			if (map.get("goodsId") != null)
				sgids.add(Long.parseLong(map.get("goodsId").toString()));

			if (map.get("categoryId") != null)
				categoryIdList.add(Long.parseLong(map.get("categoryId").toString()));
		}
		DefaultParametersUtils.removeRepeateParams4Long(sgids);
		DefaultParametersUtils.removeRepeateParams4Long(categoryIdList);

		// 添加商品信息
		List<SaleGoodsImageRefModel> saleGoodsImageRef = new ArrayList<>();
		List<GoodsDescModel> goodsDescList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sgids)) {
			Criteria criteria = null;
			if (sgids.size() == 1) {
				criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("sgid")
						.is(sgids.get(0)).and("imageType").is(1);
			} else {
				criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("sgid")
						.in(sgids).and("imageType").is(1);
			}
			Query query = new Query(criteria);
			Field fields = query.fields();
			fields.include("sgid,imageUrl");
			long startTime = System.currentTimeMillis();
			logger.error("B1 start.【paramCode：{}】imageRef start ------- {}",paramCode,formatter.format(new Date()));
			saleGoodsImageRef = this.getTemplate().select(query, SaleGoodsImageRefModel.class, "saleGoodsImageRef");
			long endTime = System.currentTimeMillis();
			logger.error("B1 end.【paramCode：{}】imageRef end ------- cost time: {}",paramCode,endTime - startTime);
			if (sgids.size() == 1) {
				criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("sgid")
						.is(sgids.get(0)).and("printFlag").is(1).and("showTerm").is(2);
			} else {
				criteria = Criteria.where("entId").is(session.getEnt_id()).and("erpCode").is(erpCode).and("sgid")
						.in(sgids).and("printFlag").is(1).and("showTerm").is(2);
			}

			query = new Query(criteria);
			fields = query.fields();
			fields.include("sgid,goodsDesc");
			startTime = System.currentTimeMillis();
			logger.error("B2 start.【paramCode：{}】 goodsDesc start ------- {}",paramCode,formatter.format(new Date()));
			goodsDescList = this.getTemplate().select(query, GoodsDescModel.class, "goodsDesc");
			endTime = System.currentTimeMillis();
			logger.error("B2 end.【paramCode：{}】 goodsDesc end ------- cost time: {}",paramCode,endTime - startTime);
		}

		// JSONArray categoryArray = new JSONArray();
		List<CategoryModel> categorys = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIdList)) {
			paramsObject.clear();
			// 优化 1 首先 不用基类查询， 可以不用查询count 第二 指定field
			paramsObject.put("categoryId", categoryIdList.size() == 1 ? categoryIdList.get(0) : categoryIdList);
			// 不用基类查询，可以减少查询count
			Criteria criteria = categoryIdList.size() == 1 ? Criteria.where("categoryId").is(categoryIdList.get(0))
					: Criteria.where("categoryId").in(categoryIdList);
			Query query = new Query(criteria);
			Field fields = query.fields();
			fields.include("categoryId,license");
			long startTime = System.currentTimeMillis();
			logger.error("B3 start.【paramCode：{}】 category start ------- {}",paramCode,formatter.format(new Date()));
			categorys = this.getTemplate().select(query, CategoryModel.class, "category");
			long endTime = System.currentTimeMillis();
			logger.error("B3 end.【paramCode：{}】 category end ------- cost time: {}",paramCode,endTime - startTime);

			// JSONObject categoryObj = (JSONObject) response.getData();
			// categoryArray =
			// categoryObj.getJSONArray(this.categoryService.getCollectionName());
		}

		// 添加商品图片、商品文描信息
		for (Map<String, Object> map : list) {
			Long sgid = Long.parseLong(map.get("goodsId").toString());
			Long categoryId = Long.parseLong(map.get("categoryId").toString());
			// 添加商品图片
			String imageUrl = "";
			for (SaleGoodsImageRefModel r : saleGoodsImageRef) {
				if (sgid == r.getSgid()) {
					imageUrl = r.getImageUrl();
					break;
				}
			}
			map.put("imageUrl", imageUrl);

			String goodsDesc = null;
			for (int i = 0; i < goodsDescList.size(); i++) {
				GoodsDescModel goodsDescObj = goodsDescList.get(i);
				if (sgid == goodsDescObj.getSgid().longValue()) {
					goodsDesc = goodsDescObj.getGoodsDesc() == null ? "" : goodsDescObj.getGoodsDesc();
					break;
				}
			}
			if (goodsDesc != null)
				map.put("goodsDesc", goodsDesc);

			Short license = null;

			if (categorys != null && categorys.size() > 0) {
				for (CategoryModel category : categorys) {
					if (categoryId.equals(category.getCategoryId())) {
						license = category.getLicense();
						break;
					}
				}
			}
			map.put("license", license);
		}
		return ServiceResponse.buildSuccess("");
	}

	@Override
	public ServiceResponse searchAllPlusp(ServiceSession session, JSONObject paramsObject) throws Exception {
		FMybatisTemplate template = this.getTemplate();
		ServiceResponse response = this.search(session, paramsObject);
		List<Map<String, Object>> pluspList = null;
		if (response.getReturncode() != null && response.getReturncode().equals("0")) {
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			List<SaleGoodsModel> saleGoodsList = JSONArray
					.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), SaleGoodsModel.class);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (SaleGoodsModel saleGoods : saleGoodsList) {
				Map<String, Object> saleGoodsMap = new HashMap<String, Object>();
				saleGoodsMap.put("sgid", saleGoods.getSgid());
				saleGoodsMap.put("barNo", saleGoods.getBarNo());
				saleGoodsMap.put("goodsCode", saleGoods.getGoodsCode());
				saleGoodsMap.put("goodsName", saleGoods.getGoodsName());
				saleGoodsMap.put("categoryId", saleGoods.getCategoryId());
				saleGoodsMap.put("salePrice", saleGoods.getSalePrice());
				saleGoodsMap.put("shopId", saleGoods.getShopId());
				saleGoodsMap.put("shopCode", saleGoods.getShopCode());
				saleGoodsMap.put("shopName", saleGoods.getShopName());
				saleGoodsMap.put("erpCode", saleGoods.getErpCode());
				resultList.add(saleGoodsMap);

				JSONObject paramObj = new JSONObject();
				paramObj.put("erpCode", saleGoods.getErpCode());
				paramObj.put("goodsCode", saleGoods.getGoodsCode());
				paramObj.put("goodsStatus", 1);
				paramObj.put("goodsType", "6");
				paramObj.put("order_direction", "desc");
				paramObj.put("order_field", "updateDate");
				ServiceResponse moreCodeGoodsDetailResponse = saleGoodsService2Impl.moreCodeGoodsDetailSearch(session,
						paramObj);

				if (moreCodeGoodsDetailResponse.getReturncode() != null
						&& moreCodeGoodsDetailResponse.getReturncode().equals("0")) {
					JSONObject jsonData = (JSONObject) moreCodeGoodsDetailResponse.getData();
					pluspList = (List<Map<String, Object>>) jsonData.get(this.getCollectionName());
					for (Map<String, Object> plssp : pluspList) {
						saleGoodsMap = new HashMap<String, Object>();
						saleGoodsMap.put("pluspBarNo", plssp.get("barNo"));
						saleGoodsMap.put("pluspGoodsCode", plssp.get("goodsCode"));
						saleGoodsMap.put("pluspGoodsName", plssp.get("goodsName"));
						saleGoodsMap.put("pluspPartsNum", plssp.get("partsNum"));
						saleGoodsMap.put("pluspSalePrice", plssp.get("salePrice"));
						saleGoodsMap.put("pluspSsgid", plssp.get("ssgid"));
						saleGoodsMap.put("pluspShopId", plssp.get("shopId"));
						saleGoodsMap.put("pluspShopCode", plssp.get("shopCode"));
						saleGoodsMap.put("pluspSortLevel", plssp.get("sortLevel"));
						resultList.add(saleGoodsMap);
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

	/**
	 * 查询受管制商品信息（线下） 通过受管制商品的商品编码、商品类型查到
	 * 
	 * @author guowenping
	 * @date 2018-07-24
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse searchControlledGoodsInfo(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse response = ParamValidateUtil.checkParam(session, paramsObject,
				new String[] { "erpCode", "shopCode", "goodsType", "goodsCode" });
		if (!ResponseCode.SUCCESS.equals(response.getReturncode())) {
			return response;
		}

		String goodsType = paramsObject.getString("goodsType");
		paramsObject.remove("goodsType");
		response = this.onQuery(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(response.getReturncode())) {
			return response;
		}

		// 1.查询受管制商品信息;
		JSONObject obj = (JSONObject) response.getData();
		JSONArray array = obj.getJSONArray(this.getCollectionName());
		if (array.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "系统查询不存在该受管制商品.");
		}
		JSONObject controlledGoods = array.getJSONObject(0);
		String goodsFromCode = controlledGoods.getString("goodsFromCode"); // 该管制商品的的来源编码存放其标签商品编码

		// 2.通过受管制商品编码，goodsType = 15 查找标签信息;
		paramsObject.put("goodsCode", goodsFromCode);
		paramsObject.put("goodsType", goodsType);
		response = this.onQuery(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(response.getReturncode())) {
			return response;
		}
		obj = (JSONObject) response.getData();
		array = obj.getJSONArray(this.getCollectionName());
		if (array == null || array.size() <= 0) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "系统不存在该受管制商品的标签信息.");
		}

		// 标签对象
		JSONObject biaoqianModel = array.getJSONObject(0);
		Map<String, String> config1 = new HashMap<String, String>();
		config1.put("escaleFlag", "true:1,false:0");
		Object o = JSONSerializeUtill.toValueJSONByKey(biaoqianModel, config1, ",", ":");

		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), o);
		return ServiceResponse.buildSuccess(result);
	}

	// 通过业务代码唯一性去查询
	public ServiceResponse searchSaleGoodsByUniqueCondition(ServiceSession session, JSONObject paramsObject) {
		paramsObject.put("table", this.getCollectionName());
		List<SaleGoodsModel> saleGoodsList = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.AdvancedQueryMapper.queryDataByUnique", paramsObject);
		if (saleGoodsList == null || saleGoodsList.isEmpty())
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST, "查询的数据不存在");

		JSONArray array = JSONArray.parseArray(JSON.toJSONString(saleGoodsList));
		List<RowMap> jsonList = JSONObject.parseArray(array.toJSONString(), RowMap.class);
		// 执行查询插件处理 钱海兵
		List<AnnotationService> tagPlugins = this.getPlugins();
		for (AnnotationService plugin : tagPlugins) {
			try {
				plugin.onAction(session, jsonList, OperationFlag.afterQuery, this.getBeanClass());
			} catch (Exception e) {
				return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "查询前插件执行异常:{0}",
						e.getMessage() + "");
			}
		}
		return ServiceResponse.buildSuccess(jsonList);
	}

	/**
	 * 门店作业添加商品和提取商品 返回值主要是多了进项税率，物流模式，安全库存天数
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse searchForShop(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse resp = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"shopCode", "erpCode", "categoryCode", "pcategoryCode");
		if (!ResponseCode.SUCCESS.equals(resp.getReturncode())) {
			return resp;
		}
		paramsObject.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		if (paramsObject.containsKey("page_no") && paramsObject.containsKey("page_size")) {
			int pageNo = paramsObject.getIntValue("page_no");
			int pageSize = paramsObject.getIntValue("page_size");
			if (pageNo == 0) {
				pageNo = 1;
			}
			paramsObject.put("page_no", (pageNo - 1) * pageSize);
		}
		List<JSONObject> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.searchForShop", paramsObject);
		JSONArray array = new JSONArray();
		for (JSONObject json : list) {
			if (null == json.get("safeStockDays"))
				json.put("safeStockDays", 1);
			if (null == json.get("logistics"))
				json.put("logistics", 1);
			if (null == json.get("inputTax"))
				json.put("inputTax", 0);
			if (null == json.get("purPrice"))
				json.put("purPrice", 0);
			if (null == json.get("partsNum"))
				json.put("partsNum", 1);
			array.add(json);
		}
		Integer total = template.getSqlSessionTemplate().selectOne("beanmapper.SaleGoodsModelMapper.getTotalForShop",
				paramsObject);
		JSONObject obj = new JSONObject();
		obj.put(this.getCollectionName(), array);
		obj.put("total_results", total);
		return ServiceResponse.buildSuccess(obj);
	}

	// 根据goodsCode查询商品信息(进价、物流模式、进项税率、安全库存天数)
	public ServiceResponse getGoodsInfoForStore(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse resp = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"shopCode", "erpCode", "goodsCode");
		if (!ResponseCode.SUCCESS.equals(resp.getReturncode())) {
			return resp;
		}
		paramsObject.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<JSONObject> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.getGoodsInfoForStore", paramsObject);
		if (null == list) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品不存在");
		}
		JSONArray array = new JSONArray();
		for (JSONObject json : list) {
			if (null == json.get("inputTax"))
				json.put("inputTax", 0);
			if (null == json.get("purPrice"))
				json.put("purPrice", 0);
			if (null == json.get("logistics"))
				json.put("logistics", 1);
			if (null == json.get("safeStockDays"))
				json.put("safeStockDays", 1);
			if (null == json.get("partsNum"))
				json.put("partsNum", 1);
			array.add(json);
		}
		return ServiceResponse.buildSuccess(array);
	}

	public ServiceResponse getGoodsSalePrice(ServiceSession session, JSONObject paramsObject) {
		return getGoodsInfo(session, paramsObject, "beanmapper.SaleGoodsModelMapper.getGoodsSalePrice");
	}

	public ServiceResponse getGoodsInfoByGoodsCodes(ServiceSession session, JSONObject paramsObject) {
		return getGoodsInfo(session, paramsObject, "beanmapper.SaleGoodsModelMapper.getGoodsInfoByGoodsCodes");
	}

	public ServiceResponse getGoodsInfo(ServiceSession session, JSONObject paramsObject, String mapper) {
		if (null == session) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "session不可为空");
		}
		ServiceResponse resp = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,
				"shopCode", "erpCode", "goodsCode");
		if (!ResponseCode.SUCCESS.equals(resp.getReturncode())) {
			return resp;
		}
		paramsObject.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List<JSONObject> list = template.getSqlSessionTemplate().selectList(mapper, paramsObject);
		if (null == list)
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品不存在");
		JSONArray array = new JSONArray();
		for (JSONObject json : list) {
			if (null == json.getString("partsNum")) {
				json.put("partsNum", 1);
			}
			if (null == json.getString("inputTax")) {
				json.put("inputTax", 0);
			}
			array.add(json);
		}
		JSONObject json = new JSONObject();
		json.put(this.getCollectionName(), array);
		return ServiceResponse.buildSuccess(json);
	}

	/**
	 * 门店作业-查询商品属性接口-支持商品编码或条码查询
	 * 
	 * 传参，如： {"erpCode": "002","shopCode":"001","goodsCode":"6752"}
	 */
	public ServiceResponse getStoreGoodsByCode(ServiceSession session, JSONObject paramsObject) {

		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject, "erpCode", "shopCode", "goodsCode");
		// 过滤空的传参
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		paramsObject.put("entId", session.getEnt_id());
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		List goodsList = template.getSqlSessionTemplate()
				.selectList("beanmapper.SaleGoodsModelMapper.getStoreGoodsByCode", paramsObject);
		if (goodsList == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询商品失败！");
		}
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(goodsList));
		List<JSONObject> list = JSONArray.parseArray(array.toJSONString(), JSONObject.class);
		for (JSONObject json : list) {
			if (null == json.get("inputTax"))
				json.put("inputTax", 0);
			if (null == json.get("purPrice"))
				json.put("purPrice", 0);
			if (null == json.get("logistics"))
				json.put("logistics", 1);
			if (null == json.get("safeStockDays"))
				json.put("safeStockDays", 1);
			if (null == json.get("orderNum"))
				json.put("orderNum", 1);
		}
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), list);
		return ServiceResponse.buildSuccess(result);
	}
}