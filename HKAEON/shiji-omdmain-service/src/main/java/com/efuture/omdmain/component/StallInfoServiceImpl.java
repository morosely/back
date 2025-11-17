package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.product.component.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.common.StallCodeUtil;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.model.StallInfoModel;
import com.efuture.omdmain.service.StallInfoService;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;

/**
 * 档口定义-档口信息Service
 * 
 * @author chenp
 *
 */
public class StallInfoServiceImpl extends CommonServiceImpl<StallInfoModel,StallInfoServiceImpl> implements StallInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(StallInfoServiceImpl.class);

	public StallInfoServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Autowired
	private StallGoodsRefServiceImpl stallGoodsRefServiceImpl;

	@Autowired
	private PackageAttShopGoodsRefService packageAttShopGoodsRefService;
	public ServiceResponse saveStallGoodsNew(ServiceSession session, JSONObject paramsObject) throws Exception{
		// 校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "siid","shopId","stallCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		Long siid = paramsObject.getLong("siid");
		String stallCode = paramsObject.getString("stallCode");
		List<JSONObject> newAttrData = new ArrayList<>();//门店商品关联属性分类(新增)
		List<JSONObject> updateAttrData = new ArrayList<>();//门店商品关联属性分类(更新或删除)：处理将属性、套餐时段 有值全置空的情况
		JSONArray saleGoodsParams = paramsObject.getJSONArray("saleGoods");
		if(saleGoodsParams!=null && !saleGoodsParams.isEmpty()) {
			JSONArray stallParams = new JSONArray();
			// 剔除前端传参明细里面多余的信息
			String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			for(int i = 0; i < saleGoodsParams.size(); i++){
				JSONObject dataMap = saleGoodsParams.getJSONObject(i);
				dataMap.putIfAbsent("siid",siid);
				dataMap.putIfAbsent("stallCode",stallCode);
				if (!StringUtils.isEmpty(dataMap.get("goodsCode"))) {
					if (dataMap.getBoolean("processFlag")) { // 是否加工(档口商品) 1:是 0:否
						dataMap.put("processFlag", 1);
					} else {
						dataMap.put("processFlag", 0);
					}
					dataMap.put("updateDate", updateDateString);
					//定制档口套餐类型
					if(dataMap.getBoolean("TCgoodsType")) {//（勾上）将普通商品设置套餐商品goodsType 0->9
						dataMap.put("goodsType", 9);
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
					}else if ("20".equals(dataMap.getString("goodsType"))){//（未勾）非套餐商品。需要判断本身是否是单品（goodsType:20）和套餐商品取消操作(goodsType:9->0)
						dataMap.put("goodsType", dataMap.getString("goodsType"));//1.单品（goodsType:20）
					}else {
						dataMap.put("goodsType", 0);//2.套餐商品取消操作(goodsType:9->0)
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
					}
					stallParams.add(dataMap);
				}
				/*String pCode = dataMap.getString("pCode");
				String cateringTimeCode = dataMap.getString("cateringTimeCode");
				String selfNotShow = dataMap.getString("selfNotShow");
				if((pCode !=null && !pCode.trim().isEmpty()) || (cateringTimeCode !=null && !cateringTimeCode.trim().isEmpty()) || ("0".equals(selfNotShow))){
					newAttrData.add(dataMap);
				}else{//1.有可能是属性、套餐时段本来就空 2.有可能之前属性、套餐时段 有值全置空的情况
					updateAttrData.add(dataMap);
				}*/
				//因增加自助不显示 更改判断逻辑(自助不显示字段一定会有值) add by yihaitao 2024-12-13
				dataMap.putIfAbsent("selfNotShow","1");
				newAttrData.add(dataMap);
			}

			if (!stallParams.isEmpty()) {
				JSONObject params = new JSONObject();
				params.put("siid", paramsObject.getString("siid"));
				params.put("shopId", paramsObject.getLong("shopId"));
				params.put("stallCode", paramsObject.getString("stallCode")); // 档口编号
				params.put("saleGoods", stallParams);
				// 新增档口商品
				result = stallGoodsRefServiceImpl.add(session, params);
				if (!ResponseCode.SUCCESS.equals(result.getReturncode())) {
					throw new Exception(result.getData().toString());
				}
			}

			//add by yihaitao 2024-06-04 增加属性分类和门店商品关联关系
			if(!newAttrData.isEmpty()){
				JSONObject param = new JSONObject();
				param.put("saleGoods",newAttrData);
				ServiceResponse response = packageAttShopGoodsRefService.upsert(session,param);
				logger.info("==========>>>> StallInfoServiceImpl-saveStallGoodsNew ---> PackageAttShopGoodsRefService-upsert:{}",response!=null ? response.getData():"response is null");
			}

			/*if(!updateAttrData.isEmpty()){//处理将属性、套餐时段 有值全置空的情况
				List<JSONObject> updateData = updateAttrData.stream().map(json -> {
					JSONObject newJson = new JSONObject();
					newJson.put("shopCode", json.getString("shopCode"));
					newJson.put("stallCode", json.getString("stallCode"));
					newJson.put("barNo", json.getString("barNo"));
					newJson.put("goodsCode", json.getString("goodsCode"));
					newJson.put("erpCode", json.getString("erpCode"));
					newJson.put("entId", json.getString("entId"));
					return newJson;
				}).collect(Collectors.toList());
				JSONObject param = new JSONObject(){{
					put("list",updateData);
				}};
				ServiceResponse response = packageAttShopGoodsRefService.syncDeleteBatch(session,param);
			}*/
		}
		paramsObject.remove("saleGoods");
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	
	/**
	 *	i.模式一：只允许定义打印机地址和打印机名称，不允许定义NAS路径
	 *  ii.模式二：打印机地址、打印机名称、NAS路径都必须为空
     *  iii.模式三：打印机地址、打印机名称、NAS路径都必须填写
	 *  iv 模式四：打印机地址、打印机名称必填、NAS路径必须为空
	 *
	 *  “打印机名称”已经被“後廚是否分商品打印”替换：1：是，2:否 add by yihaitao 2024-09-26
	 *  其他模式 可以选是 也可以选否 模式二只能否
	 */
	public boolean stringIsEmpty(String string) {
		return (string == null || string.trim().length() == 0) ? true : false;
	}
	
	public ServiceResponse validatePattern(ServiceSession session, JSONObject paramsObject) {
		String pattern = paramsObject.getString("pattern");
		String printAddress = paramsObject.getString("printAddress");
		String printName = paramsObject.getString("printName");
		String nasPath = paramsObject.getString("nasPath");
		if("1".equals(pattern)) {
			if(stringIsEmpty(printAddress)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式1,打印機地址必填項");
			}
//			if(stringIsEmpty(printName)) {
//				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式1,打印機名稱必填項");
//			}
			if(stringIsEmpty(printName)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式1,後廚是否分商品打印必填項");
			}
			if(!stringIsEmpty(nasPath)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式1,NAS路徑不允許設置");
			}
			return ServiceResponse.buildSuccess("success");
		}else if("2".equals(pattern)) {
			if(!stringIsEmpty(printAddress)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式2,打印機地址必須為空");
			}
//			if(!stringIsEmpty(printName)) {
//				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式2,打印機名稱必須為空");
//			}
			if(!"2".equals(printName)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式2,後廚是否分商品打印應該勾選否");
			}
			if(!stringIsEmpty(nasPath)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式2,NAS路徑必須為空");
			}
			return ServiceResponse.buildSuccess("success");
		}else if("3".equals(pattern)) {
			if(stringIsEmpty(printAddress)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式3,打印機地址必填項");
			}
//			if(stringIsEmpty(printName)) {
//				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式3,打印機名稱必填項");
//			}
			if(stringIsEmpty(printName)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式3,後廚是否分商品打印必填項");
			}
			if(stringIsEmpty(nasPath)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式3,NAS路徑必填項");
			}
			return ServiceResponse.buildSuccess("success");
		}else if("4".equals(pattern)) {
			if(stringIsEmpty(printAddress)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式4,打印機地址必填項");
			}
//			if(stringIsEmpty(printName)) {
//				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式4,打印機名稱必填項");
//			}
			if(stringIsEmpty(printName)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式4,後廚是否分商品打印必填項");
			}
			if(!stringIsEmpty(nasPath)) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式4,NAS路徑必須為空");
			}
			return ServiceResponse.buildSuccess("success");
		}else {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"模式值只是能1,2,3,4");
		}
	}
	
	//更新操作(模式之间的切换)初始化值。由于基类无法把更新字段为空，把字段更新成空字符串
	//i.模式一：只允许定义打印机地址和打印机名称，不允许定义NAS路径
	//ii.模式二：打印机地址、打印机名称、NAS路径都必须为空
    //iii.模式三：打印机地址、打印机名称、NAS路径都必须填写
	//iv 模式四：打印机地址、打印机名称必填、NAS路径必须为空
	public static final String BLANK_VALUE= " ";
	public void initUpdatePatternValue(ServiceSession session, JSONObject paramsObject) {
		String pattern = paramsObject.getString("pattern");
		if ("1".equals(pattern) || "4".equals(pattern) ) {
			if(paramsObject.containsKey("nasPath")){
				paramsObject.put("nasPath", BLANK_VALUE);
			}
		} else if ("2".equals(pattern)) {
			if(paramsObject.containsKey("printAddress")){
				paramsObject.put("printAddress", BLANK_VALUE);
			}
//			if(paramsObject.containsKey("printName")){
//				paramsObject.put("printName", BLANK_VALUE);
//			}
			if(paramsObject.containsKey("nasPath")){
				paramsObject.put("nasPath", BLANK_VALUE);
			}
		}
	}
	
	//查询门店内自定义档口ID不为空的记录
	public ServiceResponse queryIncludeSelfId(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "shopCode","erpCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//2.查询
		Criteria criteria = Criteria.where("erpCode").is(paramsObject.getString("erpCode")).and("shopCode").is(paramsObject.getString("shopCode")).and("entId").is(session.getEnt_id()).and("length(trim(selfId))").gte(1);
		Query query = new Query(criteria);
		List<StallInfoModel> list = this.getTemplate().select(query,StallInfoModel.class,"stallinfo");
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(),list);
		paramsObject.put("total_results",list == null ? 0 : list.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	// 根据组织机构shopId查询档口信息
	@Override
	public ServiceResponse getDataByShopId(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "shopId");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		ServiceResponse ss = this.onQuery(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(ss.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询失败"+ss.getData());
	    }
		return ss;
	}

	// 单行删除
	@Transactional(rollbackFor=Exception.class)
	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"siid", "shopId");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		
//		JSONObject paramMap = new JSONObject();
//		paramMap.put("shopId", paramsObject.getLong("shopId"));
//		paramMap.put("siid", paramsObject.getString("siid"));
//		ServiceResponse stallGoodsQuery = stallGoodsRefServiceImpl.getDataBySiid(session, paramMap);
//		JSONObject stallGoodsData = (JSONObject) stallGoodsQuery.getData();
		
		Long entId = session.getEnt_id();
		Long siid = paramsObject.getLong("siid");
		Long shopId = paramsObject.getLong("shopId");
		Criteria criteria = Criteria.where("entId").is(entId).and("siid").is(siid).and("shopId").is(shopId).and("goodsType").ne("20");
		Query query = new Query(criteria);
		//查询所有档口的商品
		List<SaleGoodsModel> saleGoodsList = this.getTemplate().select(query, SaleGoodsModel.class, "saleGoods");
		
		if (saleGoodsList!=null && !saleGoodsList.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"删除档口，请先清除该档口所经营的商品！");
		}
/*		JSONObject param = new JSONObject();
		param.put("saleGoods",saleGoodsList);
		if(saleGoodsList!=null && !saleGoodsList.isEmpty()){
//			for(int i = 0; i < temp; i++){
//				JSONObject dataMap = stallGoodsList.getJSONObject(i);
//				paramMap = new JSONObject();
//				paramMap.put("ssgid", dataMap.getLong("ssgid")); // 可售商品ID
//				// 清理档口商品表，更新门店销售表-档口信息
//				stallGoodsRefServiceImpl.delete(session, paramMap);
//			}
			// 清理全部档口商品，更新门店销售表-档口信息和经营配置表-档口信息
			ServiceResponse stallGoodsRefResult = stallGoodsRefServiceImpl.deleteAll(session, param);
			if(!ResponseCode.SUCCESS.equals(stallGoodsRefResult.getReturncode())) {
				throw new Exception("删除档口商品失败,"+ stallGoodsRefResult.getData().toString());
			}
		}*/
		// 删除档口信息表
		return this.onDelete(session, paramsObject);
	}
	
	// 新增
	@Transactional(rollbackFor=Exception.class)
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "erpCode", "shopId","stallName","pattern");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
	    //档口模式校验
		ServiceResponse validateResponse = validatePattern(session, paramsObject);
		if (ResponseCode.FAILURE.equals(validateResponse.getReturncode())) return validateResponse;
		 
		// 查询当前门店下全部档口信息
		Long entId = session.getEnt_id();
		String erpCode = paramsObject.getString("erpCode");
		Long shopId = paramsObject.getLong("shopId");
		Criteria criteria = Criteria.where("entId").is(entId).and("erpCode").is(erpCode).and("shopId").is(shopId);
		Query query = new Query(criteria);
		List<StallInfoModel> stallInfoList = this.getTemplate().select(query, StallInfoModel.class, "stallinfo");

		// 校验ID是否重复
		String selfId = paramsObject.get("selfId") == null ? "-1" : paramsObject.getString("selfId").trim();
		if (stallInfoList != null && !stallInfoList.isEmpty()) {
			List<StallInfoModel> filterList = stallInfoList.stream().filter(a -> selfId.equals(a.getSelfId())).collect(Collectors.toList());
			if (filterList.size() > 0) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "输入的ID已存在，换一个吧！");
			}
		}
		
		// 取档口最大编号
		int size = stallInfoList.size();
		int number = 0;
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				StallInfoModel json1 = stallInfoList.get(i);
				String stallCode = json1.getStallCode();
				int a = Integer.parseInt(stallCode.substring(1));
				if (a > number) {
					number = a;
				}
			}
		} else {
			number = 0;
		}
		String stallCode = StallCodeUtil.getNumber(number); // 档口编号
		paramsObject.put("stallCode", stallCode);
		
		//20200708 新增字段nasPath  printAddress为空时候，nas也不能有值，并且新增判断 printAddres为空或者没有传递则赋值" "
//		if(!paramsObject.containsKey("printAddress")) {
//			paramsObject.put("printAddress", " ");
//			paramsObject.put("nasPath", " ");
//		}else {
//			if(StringUtils.isEmpty(paramsObject.get("printAddress"))){
//				paramsObject.put("printAddress", " ");
//				paramsObject.put("nasPath", " ");
//			}else {
//				if(!paramsObject.containsKey("nasPath")) {
//					paramsObject.put("nasPath", " ");
//				}else {
//					if(StringUtils.isEmpty(paramsObject.get("nasPath"))){
//						paramsObject.put("nasPath", " ");
//					}
//				}
//			}
//		}
		// 新增档口
		ServiceResponse newStallInfoQuery = this.wrapInsert(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(newStallInfoQuery.getReturncode())) {
			return newStallInfoQuery;
		}
		
		/*JSONArray paramsList = paramsObject.getJSONArray("saleGoods");
		if (paramsList == null) paramsList = new JSONArray();
		JSONArray paramsList1 = new JSONArray();
		
		// 剔除前端传参明细里面多余的信息
		for(int i = 0; i < paramsList.size(); i++){
			JSONObject dataMap = paramsList.getJSONObject(i);
			if (!StringUtils.isEmpty(dataMap.get("goodsCode"))) {
				if (dataMap.getBoolean("processFlag")) { // 是否加工(档口商品) 1:是 0:否
					dataMap.put("processFlag", 1);
				}else {
					dataMap.put("processFlag", 0);
				}
				dataMap.put("updateDate", updateDateString);
				//定制档口套餐类型
				if(dataMap.getBoolean("TCgoodsType")) {//新增只存在勾上的情况（勾上）将普通商品设置套餐商品goodsType 0->9
					dataMap.put("goodsType", 9);
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
				}
				paramsList1.add(dataMap);
			}
		}
		
		if (paramsList1.size() > 0) {
			JSONObject newStallInfoData = (JSONObject) newStallInfoQuery.getData();
			String siid = String.valueOf(newStallInfoData.get("siid"));// 档口ID

			JSONObject params = new JSONObject();
			params.put("siid", siid);
			params.put("shopId", paramsObject.getLong("shopId"));
			params.put("stallCode", stallCode); // 档口编号
			params.put("saleGoods", paramsList1);
			// 新增档口商品
			ServiceResponse result2 = stallGoodsRefServiceImpl.add(session, params);
			if (!ResponseCode.SUCCESS.equals(result2.getReturncode())) {
				throw new Exception(result2.getData().toString());
			}
		}
		JSONObject newStallInfoData = (JSONObject) newStallInfoQuery.getData();
		newStallInfoData.put("shopId", paramsObject.getLong("shopId"));*/
		return newStallInfoQuery;
	}

	// 编辑
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 参数校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "siid","erpCode","stallName","pattern");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		
		//档口模式校验
		ServiceResponse validateResponse = validatePattern(session, paramsObject);
		if (ResponseCode.FAILURE.equals(validateResponse.getReturncode())) return validateResponse;
		
		//更新操作(模式之间的切换)初始化值
		initUpdatePatternValue(session, paramsObject);
		
		// 查询当前门店下全部档口信息
		if (!StringUtils.isEmpty(paramsObject.get("selfId"))) {
			Long entId = session.getEnt_id();
			Long shopId = paramsObject.getLong("shopId");
			String selfId = paramsObject.get("selfId") == null ? "-1" : paramsObject.getString("selfId").trim();
			if (!StringUtils.isEmpty(selfId)) {
				Criteria criteria = Criteria.where("entId").is(entId).and("shopId").is(shopId).and("selfId").is(selfId);
				Query query = new Query(criteria);
				List<StallInfoModel> stallInfoList = this.getTemplate().select(query, StallInfoModel.class, "stallinfo");
				Long siid = paramsObject.getLong("siid");
				// 校验ID是否重复使用
				if (stallInfoList != null && !stallInfoList.isEmpty()) {
					List<StallInfoModel> filterList = stallInfoList.stream().filter(a -> !siid.equals(a.getSiid())).collect(Collectors.toList());
					if (filterList.size() > 0) {
						return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "输入的ID已存在，换一个吧！");
					}
				}
			}
		}
	
		//解决页面字段传空，基类屏蔽该字段无法更新数据库
		if (StringUtils.isEmpty(paramsObject.get("selfId"))) {
			paramsObject.put("selfId", " ");
		}
		
		//20200708 新增字段nasPath  printAddress为空时候，nas也不能有值
//		if(StringUtils.isEmpty(paramsObject.get("printAddress"))){
//			paramsObject.put("printAddress", " ");
//			paramsObject.put("nasPath", " ");
//		}else {
//			if(StringUtils.isEmpty(paramsObject.get("nasPath"))){
//				paramsObject.put("nasPath", " ");
//			}
//		}
		
//		if(StringUtils.isEmpty(paramsObject.get("printName"))){
//			paramsObject.put("printName", " ");
//		}
		
		result = this.wrapUpdate(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(result.getReturncode())) {
			return result;
		}
/*		returnMap.put(this.getCollectionName(), result1.getData());
		
		JSONArray paramsList = paramsObject.getJSONArray(stallGoodsRefServiceImpl.getCollectionName());
		if(paramsList == null ) paramsList = new JSONArray();
		JSONArray paramsList1 = new JSONArray();
		
		// 剔除前端传参明细里面多余的信息
		for(int i = 0; i < paramsList.size(); i++){
			JSONObject dataMap = paramsList.getJSONObject(i);
			if (!StringUtils.isEmpty(dataMap.get("goodsCode"))) {
				if (dataMap.getBoolean("processFlag")) { // 是否加工(档口商品) 1:是 0:否
					dataMap.put("processFlag", 1);
				} else {
					dataMap.put("processFlag", 0);
				}
				dataMap.put("updateDate", updateDateString);
				//定制档口套餐类型
				if(dataMap.getBoolean("TCgoodsType")) {//（勾上）将普通商品设置套餐商品goodsType 0->9
					dataMap.put("goodsType", 9);
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
				}else if ("20".equals(dataMap.getString("goodsType"))){//（未勾）非套餐商品。需要判断本身是否是单品（goodsType:20）和套餐商品取消操作(goodsType:9->0)
					dataMap.put("goodsType", dataMap.getString("goodsType"));//1.单品（goodsType:20）
				}else {
					dataMap.put("goodsType", 0);//2.套餐商品取消操作(goodsType:9->0)
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
					this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
				}
				paramsList1.add(dataMap);
			}
		}
		
		if (paramsList1.size() > 0) {
			JSONObject params = new JSONObject();
			params.put("siid", paramsObject.getString("siid"));
			params.put("shopId", paramsObject.getLong("shopId"));
			params.put("stallCode", paramsObject.getString("stallCode")); // 档口编号
			params.put("saleGoods", paramsList1);
			// 新增档口商品
			ServiceResponse result2 = stallGoodsRefServiceImpl.add(session, params);
			if (!ResponseCode.SUCCESS.equals(result2.getReturncode())) {
				throw new Exception(result2.getData().toString());
			}
		}*/
		return ServiceResponse.buildSuccess(paramsObject);	
	}
	
	// 档口所选档口经营商品保存
	public ServiceResponse saveStallGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		// 校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "siid","shopId","stallCode");
		if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		
		JSONArray saleGoodsParams = paramsObject.getJSONArray("saleGoods");
	    if(saleGoodsParams!=null && !saleGoodsParams.isEmpty()) {
			JSONArray stallParams = new JSONArray();
			// 剔除前端传参明细里面多余的信息
			String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			for(int i = 0; i < saleGoodsParams.size(); i++){
				JSONObject dataMap = saleGoodsParams.getJSONObject(i);
				if (!StringUtils.isEmpty(dataMap.get("goodsCode"))) {
					if (dataMap.getBoolean("processFlag")) { // 是否加工(档口商品) 1:是 0:否
						dataMap.put("processFlag", 1);
					} else {
						dataMap.put("processFlag", 0);
					}
					dataMap.put("updateDate", updateDateString);
					//定制档口套餐类型
					if(dataMap.getBoolean("TCgoodsType")) {//（勾上）将普通商品设置套餐商品goodsType 0->9
						dataMap.put("goodsType", 9);
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
					}else if ("20".equals(dataMap.getString("goodsType"))){//（未勾）非套餐商品。需要判断本身是否是单品（goodsType:20）和套餐商品取消操作(goodsType:9->0)
						dataMap.put("goodsType", dataMap.getString("goodsType"));//1.单品（goodsType:20）
					}else {
						dataMap.put("goodsType", 0);//2.套餐商品取消操作(goodsType:9->0)
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateGoodsType", dataMap);//更新商品主档表的goodsType
						this.getTemplate().getSqlSessionTemplate().update("beanmapper.StallInfoModelMapper.updateSaleGoodsType", dataMap);//更新可售商品表的goodsType（主从条码）
					}
					stallParams.add(dataMap);
				}
			}
					
			if (stallParams.size() > 0) {
				JSONObject params = new JSONObject();
				params.put("siid", paramsObject.getString("siid"));
				params.put("shopId", paramsObject.getLong("shopId"));
				params.put("stallCode", paramsObject.getString("stallCode")); // 档口编号
				params.put("saleGoods", stallParams);
				// 新增档口商品
				result = stallGoodsRefServiceImpl.add(session, params);
				if (!ResponseCode.SUCCESS.equals(result.getReturncode())) {
					throw new Exception(result.getData().toString());
				}
			}
	    }
		paramsObject.remove("saleGoods");
		return ServiceResponse.buildSuccess(paramsObject);	
	}
	
	/*
	* @Description: 提供给pos的服务，根据商品code 查询 商品名称
	* @param {"stallCode":"001", "erpCode":"002", "shopCode":"001"}
	* @return:
	*/
	public ServiceResponse searchNameByCode(ServiceSession session, JSONObject paramsObject){
		List<String> mustField = Arrays.asList("erpCode", "shopCode", "siid");
		for (String field:mustField
			 ) {
			if(!paramsObject.containsKey(field)){
				logger.error(String.format("参数错误，必须含有 %s", field));
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, String.format("参数错误，必须含有 %s", field));

			}
		}
		if(!paramsObject.containsKey(com.product.model.BeanConstant.QueryField.PARAMKEY_FIELDS)){
			paramsObject.put(com.product.model.BeanConstant.QueryField.PARAMKEY_FIELDS, "siid,stallCode,stallName,erpCode,shopCode");
		}
		return onQuery(session, paramsObject);
	}

	/**
	 * 根据经营公司+门店编码查全部档口 
	 * 传参，如： {"erpCode": "002","shopCode":"002"}
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse searchStallByCode(ServiceSession session, JSONObject paramsObject) {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "shopCode");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}

		Long entId = session.getEnt_id();
		String shopCode = paramsObject.getString("shopCode");
		String erpCode = paramsObject.getString("erpCode");
		Criteria criteria = Criteria.where("entId").is(entId).and("shopCode").is(shopCode).and("erpCode").is(erpCode);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("siid,erpCode,shopId,shopCode,stallCode,stallName");
		query.with(new Sort(new Sort.Order(Sort.Direction.ASC,"stallCode")));
		List<StallInfoModel> stallList = this.getTemplate().select(query, StallInfoModel.class, "stallinfo");
		if (stallList == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询档口失败！");
		}
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), stallList);
		result.put("total_results", stallList.size());
		return ServiceResponse.buildSuccess(result);
	}
	
}
