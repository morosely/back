package com.efuture.omdmain.component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.omdmain.common.GoodsStatus;
import com.efuture.omdmain.common.GoodsType;
import com.efuture.omdmain.common.MapResultHandler;
import com.efuture.omdmain.model.BrandInfoModel;
import com.efuture.omdmain.model.BusinessCompanyModel;
import com.efuture.omdmain.model.CategoryModel;
import com.efuture.omdmain.model.CategoryPropertyBean;
import com.efuture.omdmain.model.CategoryPropertyModel;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.GoodsProcessModel;
import com.efuture.omdmain.model.GoodsPropertyExtValueModel;
import com.efuture.omdmain.model.GoodsShopRefModel;
import com.efuture.omdmain.model.GoodsSpecialModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.model.ShopModel;
import com.efuture.omdmain.model.SonGoodsBean;
import com.efuture.omdmain.model.SonGoodsPropertyBean;
import com.efuture.omdmain.service.GoodsService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.Descartes;
import com.efuture.omdmain.utils.QueryAdvanceField;
import com.efuture.omdmain.utils.SpringContextUtil;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.BeanConstant;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.product.util.UniqueID;

/**
 * 商品基础表
 * @author Administrator
 * 
 */
public class GoodsServiceImpl extends JDBCCompomentServiceImpl<GoodsModel> implements GoodsService {
	private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
	
	@Autowired
	private SaleGoodsService2Impl saleGoodsService2Impl;

	public GoodsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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

	@Autowired
	private QueryAdvanceField queryAdvanceField;
	@Autowired
	private GoodsPropertyExtServiceImpl goodsPropertyExtServiceImpl;
	@Autowired
	private SaleGoodsServiceImpl saleGoodsServiceImpl;
	@Autowired
	private GoodsShopRefServiceImpl goodsShopRefServiceImpl;
	@Autowired
	private SaleGoodsImageRefServiceImpl saleGoodsImageRefService;
	@Autowired
	private GoodsDescServiceImpl goodsDescService;
	@Autowired
	GoodsShopRefServiceImpl goodsShopRefService;
	@Autowired
	GoodsMoreBarCodeServiceImpl goodsMoreBarCodeService;
	@Autowired
	GoodsSpecPriceServiceImpl goodsSpecPriceService;
	@Autowired
	GoodsSpecialServiceImpl goodsSpecialServiceImpl;
	
	//导出行送商品模版
	@Override
	public ServiceResponse createExlModel(ServiceSession session, JSONObject paramsObject) throws Exception{
		JSONObject result = new JSONObject();
		JSONArray model = new JSONArray();
		result.put("goods", model);
		result.put("total_results", 0);
		return ServiceResponse.buildSuccess(result);
	}
	
	//导入行送商品
	@Override
	public ServiceResponse importDelivery(ServiceSession session, String params, MultipartFile file) throws Exception {
		List<Map<String, Object>> dataList = onBeforeImportData(params, file);
		//0.校验数据少于1000条
		if(dataList.size() - 1 > 1000) {
			ServiceResponse response = ServiceResponse.buildFailure(session,ResponseCode.FAILURE);
			response.setData("每次只能導入1000條數據，如果超過1000條，請多次導入");
			return response;
		}
		
		//1.导入数据进行数据校验
		int batchDataCount = 1000;
		if(dataList != null && dataList.size() > 1) {
			// 每次处理10条
		    List<String> excelGoodsCodes = new ArrayList<String>();
		    List<String> paramGoodsCodes = new ArrayList<String>();
		    Map<Object,Object> goodsDeliveryFlagMap = new HashMap<>();
		    for (int i = 1; i < dataList.size(); i++) {
				Map<String, Object> excelMap = dataList.get(i);
				goodsDeliveryFlagMap.put(excelMap.get("goodsCode"),excelMap.get("deliveryFlag"));
				//1.1校验商品行送标识是否正确（0或者1）
				Boolean flag = DeliveryFlag.isInclude((String)excelMap.get("deliveryFlag"));
				if(!flag) {
					ServiceResponse response = ServiceResponse.buildFailure(session,ResponseCode.FAILURE);
					String msg = "商品的行送標識有誤(導入的商品行送標識值請輸入0、或1、或2)";
					excelMap.put("errorMessage",msg);
					response.setData(excelMap.toString());
					return response;
				}
				//1.2.校验商品编码是否存在
				String goodsCode = (String)excelMap.get("goodsCode");
				excelGoodsCodes.add(goodsCode);
				paramGoodsCodes.add(goodsCode);
				if(i == dataList.size() - 1 || excelGoodsCodes.size() ==  batchDataCount) {
					JSONObject paramsObject = new JSONObject();
					paramsObject.put("goodsList",excelGoodsCodes);
					List<String> dbGoodsCodes = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.checkGoodsCode", paramsObject);
					//Excel编码对比数据编码
					excelGoodsCodes.removeAll(dbGoodsCodes);
					if(!excelGoodsCodes.isEmpty()) {
						ServiceResponse response = ServiceResponse.buildFailure(session,ResponseCode.Failure.NOT_EXIST);
						String msg = "商品編碼不存在";
						excelMap.clear();
						excelMap.put("errorMessage",msg);
						excelMap.put("goodsCode",excelGoodsCodes);
						response.setData(excelMap.toString());
						return response;
					}
				}
			}
		    
			//2.校验通过afterCheckQuery
			JSONObject paramsObject = new JSONObject();
			paramsObject.put("goodsList",paramGoodsCodes);
			List<Map> list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.afterCheckQuery", paramsObject);
			for (Map map : list) {
				map.put("deliveryFlag",goodsDeliveryFlagMap.get(map.get("goodsCode")));
			}
			paramsObject.clear();
			paramsObject.put("goods", list);
			paramsObject.put("total_results", dataList.size()-1);
			return ServiceResponse.buildSuccess(paramsObject);
		}else {
			ServiceResponse response = ServiceResponse.buildFailure(session,ResponseCode.FAILURE);
			response.setData("數據為空，請在Excel填入導入的數據！");
			return response;
		}

//	    //2.校验通过后，进行数据处理
//		String erpCode = "002";
//		long entId = 0;
//		String modifier = "";
//		if(session != null) {
//			erpCode = session.getErpCode();
//			entId = session.getEnt_id();
//			modifier = session.getUser_code();
//		}
//		for (int i = 1; i < dataList.size(); i++) {
//			Map<String, Object> excelMap = dataList.get(i);
//			excelMap.put("updateDate",new Date());
//			excelMap.put("erpCode",erpCode);
//			excelMap.put("entId",entId);
//			excelMap.put("modifier",modifier);
//		    //2.1更新商品
//			this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.importDeliveryGoods", excelMap);
//		    //2.2更新可售商品
//			this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.importDeliverySaleGoods", excelMap);
//		}
//		return ServiceResponse.buildSuccess("导入成功");
	}
	
	//校验通过后，进行数据处理
	@Override
	public ServiceResponse saveImportDelivery(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"goods");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		List<Map<String,Object>> dataList = (List<Map<String,Object>>)paramsObject.get("goods");
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> excelMap = dataList.get(i);
			excelMap.put("updateDate",new Date());
			excelMap.put("erpCode",excelMap.get("erpCode"));
			excelMap.put("entId",excelMap.get("entId"));
			excelMap.put("modifier",session == null ? "" : session.getUser_code());
		    //2.1更新商品
			this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.importDeliveryGoods", excelMap);
		    //2.2更新可售商品
			this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.importDeliverySaleGoods", excelMap);
		}
		return ServiceResponse.buildSuccess("保存成功");
	}
	
	//行送商品更新
	public ServiceResponse updateDeliveryGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		//1.校验参数
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"goodsCode","erpCode","entId","deliveryFlag");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//2.更新商品goods表
		paramsObject.put("updateDate",new Date());
		paramsObject.put("modifier",session.getUser_code());
		int goodsCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.updateDeliveryGoods", paramsObject);
		//3.更新salegoods表
		int saleGoodsCount = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.updateDeliverySaleGoods", paramsObject);
		//封装返回参数
		paramsObject.clear();
		paramsObject.put("goodsCount", goodsCount);
		paramsObject.put("saleGoodsCount", saleGoodsCount);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//商品行送查询
	@SuppressWarnings("rawtypes")
	public ServiceResponse deliveryGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		//1.过滤掉参数中的空值
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		//2.设置分页参数
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		//3 查询（支持编码和条码查询）
		String goodsCode = paramsObject.getString("goodsCode");
		//3.1 如果页面查询条件中有编码，区分编码和条码
		int total_results = 0;
		List list = null;
		if(goodsCode != null && goodsCode.trim().length() > 0) {
			//3.1.1 按照goodsCode编码查询
			list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.deliveryGoods4Code", paramsObject);
			if(list !=null && list.size() > 0) {
				//统计按编码查询总数量
				total_results = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.countDeliveryGoods4Code", paramsObject);
			}else{//3.1.2 编码查询为空，按照条码进行查询
				list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.deliveryGoods4BarNo", paramsObject);
				if(list !=null && list.size() > 0) {
					//统计按条码查询总数量
					total_results = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.countDeliveryGoods4BarNo", paramsObject);
				}
			}
		}else {//3.2 页面中没有输入编码条件
			list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.deliveryGoods4Code", paramsObject);
			total_results = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.countDeliveryGoods4Code", paramsObject);
		}
		//4.封装参数
		paramsObject.clear();
		paramsObject.put("goods", list);
		paramsObject.put("total_results", total_results);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//PartyTray商品设置
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse setPartyTrayGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		//1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","goodsCodes","partTray");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		Integer partTray = paramsObject.getInteger("partTray");
		JSONArray goodsArray = paramsObject.getJSONArray("goodsCodes");
		Long entId = session.getEnt_id();
		String erpCode = paramsObject.getString("erpCode");
		//2.更新goods
		paramsObject.put("updateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
      	paramsObject.put("entId", entId);
    	paramsObject.put("table","goods");
		paramsObject.put("setField","goodsType");
		paramsObject.put("setFieldValue",Integer.compare(1, partTray)== 0 ? 17 : 0 );//17：partTray商品 0:常规商品
		paramsObject.put("key","goodsCode");
		paramsObject.put("values", goodsArray);
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		//3.更新salegoods
		paramsObject.put("table","salegoods");
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		//4.处理特殊商品类型表同步数据
		List<GoodsSpecialModel> list = null;
		if(Integer.compare(1, partTray)== 0){//常规商品设置成partTray商品
			list = new ArrayList<GoodsSpecialModel>();
			for (int i = 0; i < goodsArray.size(); i++) {
				GoodsSpecialModel goods = new GoodsSpecialModel();
				goods.setGsid(UniqueID.getUniqueID(true));
				goods.setEntId(entId);
				goods.setErpCode(erpCode);
				goods.setGoodsCode(goodsArray.getString(i));
				goods.setGoodsType(17);
				list.add(goods);
			}
			paramsObject.clear();
			paramsObject.put("values",list);
			this.getTemplate().getSqlSessionTemplate().insert("beanmapper.GoodsSpecialModelMapper.batchInsert",paramsObject);
		}else{//partTray商品设置成常规商品
			paramsObject.clear();
			paramsObject.put("erpCode",erpCode);
			paramsObject.put("entId",entId);
			paramsObject.put("values",goodsArray);
			paramsObject.put("goodsType",17);
			this.getTemplate().getSqlSessionTemplate().delete("beanmapper.GoodsSpecialModelMapper.batchDelete",paramsObject);
		}
		return ServiceResponse.buildSuccess("success");
	}
	
	public ServiceResponse queryPartyTrayGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"erpCode","partTray");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		Integer partTray = paramsObject.getInteger("partTray");
		paramsObject.remove("partTray");
		paramsObject.put("goodsType", Integer.compare(1, partTray)== 0 ? 17 : 0);
		paramsObject.put("entId", session.getEnt_id());
		return this.onQuery(session, paramsObject);
	}
	
	//单店/全店·商品库存基础策略设置（商品查询）
	public ServiceResponse stockGoodsShopSearch(ServiceSession session, JSONObject paramsObject) throws Exception{
		//0.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopSheetType");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		String shopSheetType = paramsObject.getString("shopSheetType");
		//0：单店，1：全店。单店必传门店编码
		if("0".equals(shopSheetType)){
			result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode","erpCode");
			if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		}
		paramsObject.put("entId",session.getEnt_id());
		//1设置默认分页参数
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		//2.查询
		List goodsList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.stockGoodsShopSearch", paramsObject);
		long total_results = 0;
		if(goodsList != null && !goodsList.isEmpty()){
			total_results = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.stockGoodsShopSearchCount", paramsObject);
		}
		//3.返回数据
		paramsObject.clear();
	    paramsObject.put("goods", goodsList);
	    paramsObject.put("total_results", total_results);
	    return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//商品管理 / 商品资料维护-列表查询
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse rep = this.onQuery(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(rep.getReturncode())) {
		  return rep;
		}
		
		JSONObject obj = (JSONObject)rep.getData();
		JSONArray array = obj.getJSONArray(this.getCollectionName());
		DefaultParametersUtils.numberFormat(array, "#0.00", "salePrice");
		if(!CollectionUtils.isEmpty(array)) {
			String key = "goodsStatus";
			for(int i=0; i< array.size(); i++) {
				JSONObject map = array.getJSONObject(i);
				Object value = map.get(key);
	        	if(map.get(key) == null){
	        		continue;
	        	}
	        	if(map.getIntValue(key) == 0){
	        		value = "待启用";
	        	}
	        	if(map.getIntValue(key) == 1){
	        		value = "正常销售";
	        	}
	        	map.put(key, value);
		    }
		}
		obj.put(this.getCollectionName(), array);
		
		return ServiceResponse.buildSuccess(obj);
	}
	
	// 获取商品的图片和文描信息
	public ServiceResponse getImageAndGoodsDesc(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
		}
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(paramsObject.get("sgid"))){
			return ServiceResponse.buildSuccess("");
		}
		
		ServiceResponse gDQuery = goodsDescService.onQuery(session, paramsObject);// 文描信息
		JSONObject goodsDescData = (JSONObject) gDQuery.getData();
		JSONArray goodsDescList = goodsDescData.getJSONArray(goodsDescService.getCollectionName());
		
		ServiceResponse imageQuery = saleGoodsImageRefService.onQuery(session, paramsObject);// 图片
		JSONObject imageData = (JSONObject) imageQuery.getData();
		JSONArray imageList = imageData.getJSONArray(saleGoodsImageRefService.getCollectionName());
		
		result.put(goodsDescService.getCollectionName(), goodsDescList);
		result.put(saleGoodsImageRefService.getCollectionName(), imageList);
		
		return ServiceResponse.buildSuccess(result);
	}
	
	@Override
	public ServiceResponse search4Like(ServiceSession session, JSONObject paramsObject) throws Exception {
		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject);
		// 过滤空的传参
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
//    if(!paramsObject.containsKey("goodsType")) {
//      return  ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"商品类型不能为空");
//    }
		String fields = paramsObject.getString("fields");

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);

		DefaultParametersUtils.removeEmptyParams(paramsObject);
		DefaultParametersUtils.addSplitPageParams(paramsObject);

		List<Map<String, Object>> list = template.getSqlSessionTemplate()
				.selectList("beanmapper.GoodsModelMapper.search4Like", paramsObject);
		long totalResult = 0;
		if (CollectionUtils.isNotEmpty(list)) {
			totalResult = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.search4LikeCount",
					paramsObject);
		}

		List<String> erpCodes = new ArrayList<>();
		List<Long> brandIds = new ArrayList<>();
		List<Long> categoryIds = new ArrayList<>();
		for (int i = 0; list != null && i < list.size(); i++) {
			Map<String, Object> tmp = list.get(i);
			if (tmp.get("brandId") != null)
				brandIds.add(Long.parseLong(tmp.get("brandId").toString()));
			if (tmp.get("erpCode") != null)
				erpCodes.add(tmp.get("erpCode").toString());
			if (tmp.get("categoryId") != null)
				categoryIds.add(Long.parseLong(tmp.get("categoryId").toString()));

			if (tmp.get("goodsDisplayName") == null)
				tmp.put("goodsDisplayName", "-");
			if (tmp.get("vid") == null)
				tmp.put("vid", "-");
			if (tmp.get("venderName") == null)
				tmp.put("venderName", "-");
			if (tmp.get("vendersName") == null)
				tmp.put("vendersName", "-");
			if (tmp.get("stepDiff") == null)
				tmp.put("stepDiff", "-");
			if (tmp.get("venderCode") == null)
				tmp.put("venderCode", "-");
			if (tmp.get("season") == null)
				tmp.put("season", "-");
			if (tmp.get("serviceType") == null)
				tmp.put("serviceType", "-");
			if (tmp.get("serviceTypeName") == null)
				tmp.put("serviceTypeName", "-");
			if (tmp.get("erpCode") == null)
				tmp.put("erpCode", "-");
			if (tmp.get("erpName") == null)
				tmp.put("erpName", "-");
			if (tmp.get("maxSafeStock") == null)
				tmp.put("maxSafeStock", 0);
			if (tmp.get("minSafeStock") == null)
				tmp.put("minSafeStock", 0);
			if (tmp.get("minDiscount") == null)
				tmp.put("minDiscount", 0);
			if (tmp.get("lowTemp") == null)
				tmp.put("lowTemp", 0);
			if (tmp.get("highTemp") == null)
				tmp.put("highTemp", 0);
		}

		paramsObject.put("entId", session.getEnt_id());
		// 查询品牌
		List<BrandInfoModel> brandModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(brandIds)) {
			paramsObject.put("brandIds", brandIds);
			brandModel = template.getSqlSessionTemplate().selectList("beanmapper.BrandInfoModelMapper.selectInBrandId",
					paramsObject);
		}

		List<CategoryModel> categoryModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.put("categoryIds", categoryIds);
			categoryModel = template.getSqlSessionTemplate()
					.selectList("beanmapper.CategoryModelMapper.selectInCategoryIds", paramsObject);
		}

		List<BusinessCompanyModel> businessCompanyModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(erpCodes)) {
			paramsObject.put("erpCodes", erpCodes);
			businessCompanyModel = template.getSqlSessionTemplate()
					.selectList("beanmapper.BusinessCompanyModelMapper.selectInErpCodes", paramsObject);
		}

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> tmp = list.get(i);
			if (tmp.get("brandId") != null) {
				for (BrandInfoModel b : brandModel) {
					if (b.getBrandId().longValue() == Long.parseLong(tmp.get("brandId").toString())) {
						tmp.put("brandName", b.getBrandName());
						break;
					}
				}
			}

			if (tmp.get("erpCode") != null) {
				for (BusinessCompanyModel b : businessCompanyModel) {
					if (tmp.get("erpCode").toString().equals(b.getErpCode())) {
						tmp.put("erpName", b.getErpName());
						break;
					}
				}
			}

			if (tmp.get("categoryId") != null) {
				for (CategoryModel c : categoryModel) {
					if (Long.parseLong(tmp.get("categoryId").toString()) == c.getCategoryId()) {
						tmp.put("categoryName", c.getCategoryName());
						break;
					}
				}
			}
		}

		JSONObject result = new JSONObject();
		result.put("goods", DefaultParametersUtils.filterByFields(list, fields));
		result.put("total_results", totalResult);
		return ServiceResponse.buildSuccess(result);
	}
  
	//商品管理 / 商品资料维护-列表查询
	@Override
	public ServiceResponse get(ServiceSession session, JSONObject paramsObject) throws Exception {
		Object sgid = paramsObject.get("sgid");
		//1.查询商品基本信息
		ServiceResponse goodsRes = this.onQuery(session, paramsObject);
		//2.查询商品图片信息
		paramsObject.clear();
		paramsObject.put("sgid", sgid);
		ServiceResponse image = saleGoodsImageRefService.onQuery(session, paramsObject);
		JSONObject goodsJson = (JSONObject)goodsRes.getData();
		goodsJson.put(saleGoodsImageRefService.getCollectionName(), ((JSONObject)image.getData()).get(saleGoodsImageRefService.getCollectionName()));
		return ServiceResponse.buildSuccess(goodsJson);
	}
	
	// 商品资料维护-详情
	public ServiceResponse getList(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
		}
		
		Object sgid = paramsObject.get("sgid");
		//1.查询商品基本信息
		ServiceResponse goodsQuery = this.onQuery(session, paramsObject);
		//2.查询商品图片信息和文描信息
		paramsObject.clear();
		paramsObject.put("sgid", sgid);
		ServiceResponse imageAndGoodsDesc = this.getImageAndGoodsDesc(session, paramsObject);
		
		JSONObject goodsJson = (JSONObject)goodsQuery.getData();
		JSONArray array = goodsJson.getJSONArray(this.getCollectionName());
		DefaultParametersUtils.numberFormat(array, "#0.00", "salePrice");
		DefaultParametersUtils.numberFormat(array, "#0.00", "primeCost");
		DefaultParametersUtils.numberFormat(array, "#0.00", "refPrice");
		DefaultParametersUtils.numberFormat(array, "#0.00", "memberPrice");
		DefaultParametersUtils.numberFormat(array, "#0.00", "minSalePrice");
		
		JSONObject imageAndGDJson = (JSONObject)imageAndGoodsDesc.getData();
		
		// 商品信息
		goodsJson.put(this.getCollectionName(), array);
		// 图片信息
		goodsJson.put(saleGoodsImageRefService.getCollectionName(), (imageAndGDJson.get(saleGoodsImageRefService.getCollectionName())));
		// 文描信息
		goodsJson.put(goodsDescService.getCollectionName(), (imageAndGDJson.get(goodsDescService.getCollectionName())));
		return goodsQuery;
	}
	
	//商品管理 / 商品资料维护-编辑
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse update1(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.更新商品基本信息
		List <JSONObject> imageList = (List<JSONObject>) paramsObject.get("saleGoodsImageRef");
		Object sgid = paramsObject.get("sgid");
		paramsObject.put("updateDate", new Date());
		paramsObject.put("modifier", Long.toString(session.getUser_id()));
		this.setUpsert(false);
		this.onUpdate(session, paramsObject);
		//2.更新图片
		//2.1删除以前图片
		paramsObject.clear();
		paramsObject.put("sgid", sgid);
		saleGoodsImageRefService.onDelete(session, paramsObject);
		//2.2增加图片
		paramsObject.clear();
		for (JSONObject jsonObject : imageList) {
			jsonObject.put("sgid", sgid);
		}
		
		paramsObject.put(saleGoodsImageRefService.getCollectionName(), imageList);
		saleGoodsImageRefService.onInsert(session, paramsObject);
		
		FMybatisTemplate template = this.getTemplate();
	    template.onSetContext(session);
	    paramsObject.put("sgids", paramsObject.get("sgid"));
		GoodsModel goodsModel = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.selectInSGID", paramsObject);
		
		JSONObject goodsDesc = new JSONObject();
		goodsDesc.put("entId", session.getEnt_id());
		goodsDesc.put("erpCode", goodsModel.getErpCode());
		goodsDesc.put("sgid", paramsObject.get("sgid"));
		goodsDesc.put("goodsCode", goodsModel.getGoodsCode());
		goodsDesc.put("showTerm", 0);
		goodsDesc.put("goodsDescPc", paramsObject.get("goodsDescPc"));
		this.goodsDescService.save(session, goodsDesc);
		
		goodsDesc.put("showTerm", 1);
        goodsDesc.put("goodsDescPc", paramsObject.get("goodsDesc"));
        this.goodsDescService.save(session, goodsDesc);
		return ServiceResponse.buildSuccess("success");
	}

	//商品管理 / 商品资料维护-编辑
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
    	if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "sgid"));
		}
		if (!paramsObject.containsKey("saleGoodsImageRef")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "saleGoodsImageRef"));
		}
		if (!paramsObject.containsKey("goodsdesc")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "goodsdesc"));
		}
		
		//1.更新商品基本信息
		Object sgid = paramsObject.get("sgid");
		paramsObject.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramsObject.put("modifier", session.getUser_name());
		this.setUpsert(false);
		this.onUpdate(session, paramsObject);
		
		// 修改文描
		JSONArray gdList = paramsObject.getJSONArray(goodsDescService.getCollectionName());
		if (gdList.size() > 0) { 
			int temp1 = gdList.size();
			for (int i = 0; i < temp1; i++) {
				JSONObject goodsDesc = (JSONObject) gdList.get(i);
				ServiceResponse goodsDescResult = goodsDescService.saveBySgid(session, goodsDesc);
				if(!ResponseCode.SUCCESS.equals(goodsDescResult.getReturncode())) {
					  return goodsDescResult;
				}
			}
		}
		
		// 更新图片
		List <JSONObject> imageList = (List<JSONObject>) paramsObject.get("saleGoodsImageRef");
		//2.1删除以前图片
		paramsObject.clear();
		paramsObject.put("sgid", sgid);
		saleGoodsImageRefService.onDelete(session, paramsObject);
		if (imageList.size() > 0) { 
			//2.2增加图片
			paramsObject.clear();
			for (JSONObject jsonObject : imageList) {
				jsonObject.put("sgid", sgid);
			}
			paramsObject.put(saleGoodsImageRefService.getCollectionName(), imageList);
			saleGoodsImageRefService.onInsert(session, paramsObject);
		}	
		return ServiceResponse.buildSuccess("success");
	}
	
	//高级查询显示的字段
	public ServiceResponse showAdvanceField(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "table","schema");
		if(ResponseCode.SUCCESS.equals(result.getReturncode())){
			return queryAdvanceField.showAdvanceField(session, paramsObject);
		}
		return result;
	}
	 
	//生成子品：显示属性模板
	public ServiceResponse showCategoryProperty(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "categoryId","psgid","generatesFlag");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;		
		
		//1.查询类别默认的属性(通过工业分类类别)
		Criteria criteria = Criteria.where("categoryId").is(paramsObject.getLong("categoryId")).and("generatesFlag").is(paramsObject.get("generatesFlag")).and("status").is(1);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("cpmid,propertyCode,propertyName,propertyDesc");
		List<CategoryPropertyModel> categoryPropertyList = this.getTemplate().select(query, CategoryPropertyModel.class, "categoryproperty");
		
		//CategoryPropertyBean整理辅助属性propertyValueCode(该属性用于修改)
		List<CategoryPropertyBean> categoryPropertyBeanList = new ArrayList<CategoryPropertyBean>();
		for (CategoryPropertyModel categoryPropertyModel : categoryPropertyList) {
			CategoryPropertyBean categoryPropertyBean = new CategoryPropertyBean();
    		BeanUtils.copyProperties(categoryPropertyModel, categoryPropertyBean);
    		categoryPropertyBeanList.add(categoryPropertyBean);
		}
		
		//2.查询商品保存过的属性
		//2.1查询商品的所有子品的属性集合信息
		Criteria criteria1 = Criteria.where("psgid").is(paramsObject.getString("psgid"));//传过来的母品编码（扩展属性表都是子品编码）
		Query query1 = new Query(criteria1);
		Field fields1 = query1.fields();
		fields1.include("propertyCode,propertyDesc,propertyName,propertyValue,propertyValueCode");
		List<GoodsPropertyExtValueModel> goodsPropertyExtList = this.getTemplate().select(query1, GoodsPropertyExtValueModel.class, "goodspropertyextvalue");
		
		//2.2去重(根据属性Code和属性值Code去重)
		List<GoodsPropertyExtValueModel> uniqueGoodsPropertyExtList = goodsPropertyExtList.stream()
				.collect(
						Collectors.collectingAndThen(
								Collectors.toCollection(
										() -> new TreeSet<>(Comparator.comparing(GoodsPropertyExtValueModel::getPropertyCode)
												.thenComparing(GoodsPropertyExtValueModel::getPropertyValue))),
								ArrayList::new));
		
		//3. 封装返回数据
		//返回属性模板下拉列表
		paramsObject.put("categoryProperty",categoryPropertyBeanList);
		//返回已存的属性
		paramsObject.put("goodsPropertyExt",uniqueGoodsPropertyExtList);
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//生成子品：点击下一步的操作
	public ServiceResponse next(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"psgid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		
		//1.map根据模板属性进行归类统计
		Map<String,List<SonGoodsPropertyBean>> map = new HashMap<String,List<SonGoodsPropertyBean>>();
		
		//2.解析数据，模板属性归类统计数据
		JSONArray jsonArray = paramsObject.getJSONArray("goodsPropertyExt");//子品多规格属性值
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = (JSONObject) jsonArray.get(i);
			String propertyCode = json.getString("propertyCode");
			List<SonGoodsPropertyBean> list = map.containsKey(propertyCode) ? map.get(propertyCode):new ArrayList<SonGoodsPropertyBean>();
			//复制属性
			SonGoodsPropertyBean model = JSONObject.toJavaObject(json, SonGoodsPropertyBean.class);
			if(StringUtils.isEmpty(model.getPropertyValueCode())||"0".equals(model.getPropertyValueCode())){//如果属性值Code为空，自动生成设置属性值Code
				model.setPropertyValueCode(Long.toString(UniqueID.getUniqueID(true)));
			}
			list.add(model);
			map.put(propertyCode,list);
		}
		
		//3.产生笛卡尔积数据集
		List<List<SonGoodsPropertyBean>> originalValue = new ArrayList<List<SonGoodsPropertyBean>>();
		originalValue.addAll(map.values());
		List<List<SonGoodsPropertyBean>> recursiveResult = new ArrayList<List<SonGoodsPropertyBean>>(); 
		Descartes.recursive(originalValue, recursiveResult, 0, new ArrayList<SonGoodsPropertyBean>());
		//recursiveResult.forEach(System.out::println);
		
		//4.解析数据集生成返回数据
		Long psgid = paramsObject.getLong("psgid");//母品的sgid
		Criteria criteria = Criteria.where("sgid").is(psgid);
		Query query = new Query(criteria);
		GoodsModel goods = this.getTemplate().selectOne(query, GoodsModel.class, "goods");
		if(goods == null){
			return ServiceResponse.buildFailure(session, "查询不到商品");
		}
		BigDecimal salePrice = goods.getSalePrice();//母品的价格
		
		List<SonGoodsBean> descartesGoodsList = new ArrayList<SonGoodsBean>();//返回页面的Goods List集合
		
		for (List<SonGoodsPropertyBean> propertyList : recursiveResult) { 
			String goodsName = goods.getGoodsName();//母品名称
			List<String> propertyValueCodes = new ArrayList<String>();//拼接属性值Code
			List<String> propertyValue = new ArrayList<String>();//拼接各子品描述的值
			for (SonGoodsPropertyBean bean : propertyList) {  
				propertyValue.add(bean.getPropertyValue()); 
				propertyValueCodes.add(bean.getPropertyValueCode());
            }
			Collections.sort(propertyValue);//排序
			SonGoodsBean sonGoodBean = new SonGoodsBean();
			String sonGoodsName = goodsName + " "+ String.join(" ", propertyValue);
			sonGoodsName = sonGoodsName.length() > 64 ? sonGoodsName.substring(0, 64) : sonGoodsName;
			sonGoodBean.setGoodsName(sonGoodsName);
			String id = Long.toString(UniqueID.getUniqueID(true));
			String code = GoodsType.ZP.getValue()+id.substring(id.length()-8);
			sonGoodBean.setBarNo(code);//条码需要自动生成
			sonGoodBean.setSalePrice(salePrice);
			sonGoodBean.setPsgid(psgid);//设置母品的ID
			//属性值Code排序后拼接
			Collections.sort(propertyValueCodes);
			sonGoodBean.setPropertyValueCodes(String.join(",", propertyValueCodes));
			sonGoodBean.setGoodsPropertyExt(propertyList);
			descartesGoodsList.add(sonGoodBean);
		}
		
		//5.查询数据库
		//5.1获取母品对应的子品所有属性信息
		Criteria criteria1 = Criteria.where("psgid").is(psgid);//母品ID
		Query query1 = new Query(criteria1);
		List<GoodsPropertyExtValueModel> goodsPropertyExtList = this.getTemplate().select(query1, GoodsPropertyExtValueModel.class, "goodspropertyextvalue");
		
		if(!goodsPropertyExtList.isEmpty()){//编辑
			//6根据子品编码收集属性值Code
			//6.1分组统计（根据子品编码统计属性值Code）
			Map<Long,List<String>> sgidPropertyValueListMap = new HashMap<Long,List<String>>();
			for (GoodsPropertyExtValueModel goodsPropertyExt : goodsPropertyExtList) {
				Long sgid = goodsPropertyExt.getSgid();
				String propertyValueCode = goodsPropertyExt.getPropertyValueCode();
				List<String> propertyValueCodeList = sgidPropertyValueListMap.containsKey(sgid) ? sgidPropertyValueListMap.get(sgid):new ArrayList<String>();
				propertyValueCodeList.add(propertyValueCode);
				sgidPropertyValueListMap.put(sgid,propertyValueCodeList);
			}
			//6.2根据子品编码对应的属性值Code进行排序
			for (Long sgid : sgidPropertyValueListMap.keySet()) {
				List<String> propertyValueCodes = sgidPropertyValueListMap.get(sgid);
				Collections.sort(propertyValueCodes);
				sgidPropertyValueListMap.put(sgid, propertyValueCodes);
			}
			//6.3根据子品编码对应的属性值Code进行拼接
			Map<Long,String> sgidPropertyValueCodesMap = new HashMap<Long,String>();
			for (Long sgid : sgidPropertyValueListMap.keySet()) {
				sgidPropertyValueCodesMap.put(sgid, String.join(",", sgidPropertyValueListMap.get(sgid)));
			}
			 
			//7.生成的笛卡尔积数据匹配赋值子品编码
//			Criteria criteria2 = Criteria.where("parentGoodsCode").is(goods.getGoodsCode());//母品编码
//			Query query2 = new Query(criteria2);
//			this.getTemplate().onSetContext(session);
//			List<GoodsModel> sonGoodsList = this.getTemplate().select(query2, GoodsModel.class, "goods");//查询所有子品信息
			
		  for(int i = descartesGoodsList.size()-1; i >= 0; i--){
			  SonGoodsBean descartesSonGoodsBean = descartesGoodsList.get(i);
			  String propertyValueCodes = descartesSonGoodsBean.getPropertyValueCodes();
			  for (Long sgid : sgidPropertyValueCodesMap.keySet()){
				  if(propertyValueCodes.equals(sgidPropertyValueCodesMap.get(sgid))){//属性值匹配成功
			            //不显示已经存在于记录中的子品，避免误操作
			            descartesGoodsList.remove(i);
			            continue;
		            }
			  }
		   }
		}
		
		paramsObject.clear();
		//解决Java List转换Json出现$ref
		String descartesGoodsStr = JSON.toJSONString(descartesGoodsList, SerializerFeature.DisableCircularReferenceDetect);
		paramsObject.put("goods",JSONObject.parse(descartesGoodsStr));
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//封装事务回滚的异常信息返回前台
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject){
		ServiceResponse response = null;
		try {
			GoodsServiceImpl proxyService = ((GoodsServiceImpl)SpringContextUtil.getBean(this.getClass()));
			response = proxyService.doAdd(session,paramsObject);
		} catch (Exception e) {
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,e.getMessage());
		}
		return response;
	}
		
	//保存子品
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
	public ServiceResponse doAdd(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"goods");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		Map<String,Object> returnMap = new HashMap<>();
		
		//1.查询母品相关信息
		JSONArray jsonParamArray = paramsObject.getJSONArray("goods");
		Long psgid = ((JSONObject) jsonParamArray.get(0)).getLong("psgid");
		Criteria criteria = Criteria.where("sgid").is(psgid);//母品的sgid
		Query query = new Query(criteria);
		GoodsModel goods = this.getTemplate().selectOne(query, GoodsModel.class, "goods");
		
		//2.复制属性给子品
		//2.1查询当前母品的子品编码最大子品序号，以便生成子品编码（此处并发会有问题） 子品编码规则：母品编码+3位数字
		Integer maxNo = 0;
		if(goods != null){
			//校验条码不能有重复（前端同时需要检验）
			List<String> barNoList = new ArrayList<>();
			for (Object json : jsonParamArray) {
				JSONObject sonJsonParam = (JSONObject)json;
				barNoList.add(sonJsonParam.getString("barNo"));
			}
			criteria = Criteria.where("erpCode").is(goods.getErpCode()).and("barNo").in(barNoList);
			query = new Query(criteria);
			Field flds = query.fields();
			flds.include("barNo");
			List<GoodsModel> repeatBarNoGoods  = this.getTemplate().select(query, GoodsModel.class,"goods");
			List<String> repeatBarNos = repeatBarNoGoods.stream().map(GoodsModel::getBarNo).collect(Collectors.toList());
			if(!repeatBarNoGoods.isEmpty()){
				return ServiceResponse.buildFailure(session,ResponseCode.Failure.ALREADY_EXISTS,"数据库重复条码："+repeatBarNos.toString());
			}
			
			//获取子品的最大序号
			criteria = Criteria.where("parentGoodsCode").is(goods.getGoodsCode());
			query = new Query(criteria);
			query.with(new Sort(new Sort.Order(Direction.DESC,"goodsCode")));
			Field fields = query.fields();
			fields.include("right(goodsCode,3) goodsCode");
			List<GoodsModel> sonGoods = this.getTemplate().select(query, GoodsModel.class,"goods");
			//如果存在子品编码取最值+1
			if(!sonGoods.isEmpty()){
				String sonGoodsMax = sonGoods.get(0).getGoodsCode();//获得子品最大的序列号
				maxNo = Integer.parseInt(sonGoodsMax);
			}
		
			//2.2生成子品信息和子品扩展属性，更新母品为单品
			JSONObject goodsJson = JSON.parseObject(JSON.toJSONString(goods));
			JSONArray sonGoodsArray = new JSONArray();//子品集合
			JSONArray sonProExtArray = new JSONArray();//子品扩展属性集合
			String nowDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String userId = Long.toString(session.getUser_id());
			
			for (Object json : jsonParamArray) {
				JSONObject sonJsonParam = (JSONObject)json;
				//1.构造子品信息,母品的数值复制给子品
				JSONObject son = (JSONObject) goodsJson.clone();
				son.remove("sgid");//移除主键信息

				//2.子品的属性赋值(页面中传入值)
				son.put("barNo", sonJsonParam.getString("barNo"));
				son.put("salePrice",sonJsonParam.getBigDecimal("salePrice"));
				String sonGoodsName = sonJsonParam.getString("goodsName");
				//数据库商品名称最大长度64位。超过64位截取64位. 防止存入数据库报异常
				sonGoodsName = sonGoodsName.length() > 64 ? sonGoodsName.substring(0, 64) : sonGoodsName;
				son.put("goodsName",sonGoodsName);
				son.put("parentGoodsCode",goods.getGoodsCode());//子品编码的母码
				son.put("psgid", goods.getSgid());//子品编码的母品ID
				String sonGoodsCode = goods.getGoodsCode()+String.format("%03d",++maxNo);
				son.put("goodsCode", sonGoodsCode);//子品编码规则：母品编码+3位数字
				son.put("sgid",UniqueID.getUniqueID(true));//生成子品的唯一ID
				son.put("goodsType",GoodsType.ZP.getValue());//商品类型
				son.put("directFromErp",false);//是否是ERP的数据来源
				son.put("singleItemFlag",true);//是否是单品
				son.put("createDate",nowDateString);
				son.put("creator", userId);
				sonGoodsArray.add(son);
				
				//构造子品对应的扩展属性信息
				JSONArray properties = sonJsonParam.getJSONArray("goodsPropertyExt");
				for (Object propertity : properties) {
					JSONObject goodsProExt =  (JSONObject)propertity;
					goodsProExt.put("sgid", son.get("sgid"));//子品ID
					goodsProExt.put("goodsCode", son.get("goodsCode"));//子品编码
					goodsProExt.put("psgid",goods.getSgid());//母品的ID
					goodsProExt.put("pgoodsCode",goods.getGoodsCode());//母品编码
					goodsProExt.put("gpeid",goodsProExt.get("propertyValueCode"));//商品扩展属性ID
					goodsProExt.put("createDate",nowDateString);
					goodsProExt.put("creator", userId);
					sonProExtArray.add(goodsProExt);
				}
			}
			//3.更新商品基础表母品信息为非单品
			goodsJson.clear();
			goodsJson.put("sgid",goods.getSgid());
			goodsJson.put("singleItemFlag",false);//是否单品0-否/1- 是
			goodsJson.put("modifier",userId);
			goodsJson.put("updateDate",nowDateString);
			this.setUpsert(false);
			ServiceResponse response = this.onUpdate(session, goodsJson);
			if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
				throw new Exception(response.getData().toString());
			}
			
			paramsObject.clear();
			paramsObject.put(this.getCollectionName(), sonGoodsArray);
			paramsObject.put(goodsPropertyExtServiceImpl.getCollectionName(), sonProExtArray);
			
			//4.批量插入子品信息到商品基础表	
			response = this.onInsert(session, paramsObject);//新增子品
			if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
				throw new Exception(response.getData().toString());
			}
			returnMap.put(this.getCollectionName(),response.getData());
			
			//5.批量插入子品的扩展属性信息到商品扩展属性表
			response = goodsPropertyExtServiceImpl.onInsert(session,paramsObject);
			if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
				throw new Exception(response.getData().toString());
			}
			returnMap.put(goodsPropertyExtServiceImpl.getCollectionName(),response.getData());
			
			//6 插入经营配置信息
			//6.1 查询所有门店母品的经营配置信息
			Criteria criteria1 = Criteria.where("sgid").is(psgid);//传过来的母品编码
			Query query1 = new Query(criteria1);
			List<GoodsShopRefModel> goodsShopRefList = this.getTemplate().select(query1, GoodsShopRefModel.class, "goodsshopref");//母品的经营配置
			
			if(!goodsShopRefList.isEmpty()){
				JSONArray sonShopRefArray = new JSONArray();//子品经营配置集合
				for (GoodsShopRefModel goodsShopRefModel : goodsShopRefList) {// 遍历循环经营配置
					JSONObject goodsShopRefJson = JSON.parseObject(JSON.toJSONString(goodsShopRefModel));//母品经营配置信息
					for (Object son : sonGoodsArray) {//遍历添加门店子品信息
						//添加经营配置
						JSONObject sonGoodsShopRef = (JSONObject)goodsShopRefJson.clone();
						sonGoodsShopRef.remove("gsrid");//移除主键信息
						sonGoodsShopRef.put("sgid", ((JSONObject)son).get("sgid"));
						sonGoodsShopRef.put("goodsCode", ((JSONObject)son).get("goodsCode"));
						sonGoodsShopRef.put("salePrice", ((JSONObject)son).getBigDecimal("salePrice"));//经营配置门店价格
						sonShopRefArray.add(sonGoodsShopRef);
					}
				}
				paramsObject.clear();
				paramsObject.put(goodsShopRefServiceImpl.getCollectionName(),sonShopRefArray);
				response = goodsShopRefServiceImpl.onInsert(session,paramsObject);
				if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
					throw new Exception(response.getData().toString());
				}
				returnMap.put(goodsShopRefServiceImpl.getCollectionName(),response.getData());
			}
			
			//7.插入商品销售表
			List<SaleGoodsModel> saleGoodsList = this.getTemplate().select(query1, SaleGoodsModel.class, "salegoods");//商品销售表的母品信息
			
			if(!saleGoodsList.isEmpty()){
				JSONArray sonSaleGoodsArray = new JSONArray();//子品商品销售集合
				
				for(SaleGoodsModel saleGoods : saleGoodsList){//遍历母品集合
					JSONObject saleGoodsJson = JSON.parseObject(JSON.toJSONString(saleGoods));//母品
					for (Object son : sonGoodsArray) {
						JSONObject sonGoodsJson = (JSONObject)son;
						//添加子品信息(复制母品信息给子品)，然后再覆盖值
						JSONObject sonSaleGoodsJson = (JSONObject)saleGoodsJson.clone();
						sonSaleGoodsJson.remove("ssgid");//移除主键信息
						sonSaleGoodsJson.put("ssgid","");//避免请求条件缺内部ID关键字ssgid"
						sonSaleGoodsJson.put("barNo",sonGoodsJson.getString("barNo"));
						sonSaleGoodsJson.put("salePrice",sonGoodsJson.getBigDecimal("salePrice"));
						sonSaleGoodsJson.put("goodsName",sonGoodsJson.getString("goodsName"));
						sonSaleGoodsJson.put("parentGoodsCode",sonGoodsJson.getString("parentGoodsCode"));//子品编码的母码
						sonSaleGoodsJson.put("psgid",sonGoodsJson.getString("psgid"));//子品编码的母码ID
						sonSaleGoodsJson.put("goodsCode", sonGoodsJson.getString("goodsCode"));//子品编码规则：母品编码+3位数字
						sonSaleGoodsJson.put("sgid",sonGoodsJson.get("sgid"));
						sonSaleGoodsJson.put("goodsType",GoodsType.ZP.getValue());//商品类型
						sonSaleGoodsJson.put("directFromErp",false);//是否是ERP的数据来源
						sonSaleGoodsJson.put("singleItemFlag",true);//是否是单品
						sonSaleGoodsJson.put("createDate",nowDateString);
						sonSaleGoodsJson.put("creator", userId);
						sonSaleGoodsArray.add(sonSaleGoodsJson);//无主键数据（插入）
					}
					saleGoodsJson.put("singleItemFlag", false);//是否单品0-否/1- 是(更新母品为非单品)（有主键数据，更新）
					saleGoodsJson.put("modifier",userId);
					saleGoodsJson.put("updateDate",nowDateString);
					sonSaleGoodsArray.add(saleGoodsJson);
				}
				//7.1 更新母品为非单品、插入子品到商品销售表（更新和插入）
				paramsObject.clear();
				paramsObject.put(saleGoodsServiceImpl.getCollectionName(), sonSaleGoodsArray);
				response = saleGoodsServiceImpl.onUpdate(session, paramsObject);
				if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
					throw new Exception(response.getData().toString());
				}
				returnMap.put(saleGoodsServiceImpl.getCollectionName(),response.getData());
			}
			return ServiceResponse.buildSuccess(returnMap);
		}else{
			return ServiceResponse.buildFailure(session,"查询不到商品");
		}		
	}	
	
	//属性模板属性值修改
	public ServiceResponse updateProValue(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"psgid","goodsPropertyExt");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		
		JSONArray propertiesArray = paramsObject.getJSONArray("goodsPropertyExt");
		String nowDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String userId = Long.toString(session.getUser_id());
		//1.更新属性值
		for (Object propertity : propertiesArray) {
			JSONObject property =  (JSONObject)propertity;
			property.put("modifier",userId);
			property.put("updateDate", nowDateString);
			this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsPropertyExtValueModelMapper.updateProperValue",property);
		}
		
		//2.更新子品名称（商品表，商品销售表）
		Long psgid = paramsObject.getLong("psgid");//母品的sgid
		Criteria criteria = Criteria.where("psgid").is(psgid);//母品ID
		Query query = new Query(criteria);
		List<GoodsPropertyExtValueModel> goodsPropertyExtList = this.getTemplate().select(query, GoodsPropertyExtValueModel.class, "goodspropertyextvalue");
		
		criteria = Criteria.where("sgid").is(psgid);//母品ID
		query = new Query(criteria);
		GoodsModel goods = this.getTemplate().selectOne(query,GoodsModel.class,"goods");
		if( goods == null ){
			return ServiceResponse.buildFailure(session, "查询不到商品");
		}
		
		
		//2.1 解析数据，模板属性归类统计数据
		if(!goodsPropertyExtList.isEmpty()){
			Map<Long,List<String>> sgidProValueMap = new HashMap<Long,List<String>>();
			for (GoodsPropertyExtValueModel goodsPropertyExtValueModel : goodsPropertyExtList) {
				Long sgid = goodsPropertyExtValueModel.getSgid();
				String propertyValue = goodsPropertyExtValueModel.getPropertyValue();
				List<String> list = sgidProValueMap.containsKey(sgid) ? sgidProValueMap.get(sgid) : new ArrayList<String>();
				list.add(propertyValue);
				sgidProValueMap.put(sgid,list);
			}
			
			for(Entry<Long, List<String>> entry : sgidProValueMap.entrySet()){
				paramsObject.clear();
				String goodsName = goods.getGoodsName();//母品的名称
				
				List<String> propertyValue = entry.getValue();
				Collections.sort(propertyValue);//排序
				goodsName = goodsName + " " +String.join(" ", propertyValue);
				//数据库商品名称最大长度64位。超过64位截取64位. 防止存入数据库报异常
				goodsName = goodsName.length() > 64 ? goodsName.substring(0, 64) : goodsName;
				paramsObject.put("sgid", entry.getKey());
				paramsObject.put("goodsName", goodsName);
				//3.1.更新商品表的名称
		    	this.setUpsert(false);
		    	ServiceResponse response = this.onUpdate(session, paramsObject);
		    	if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		    	//3.2.更新商品销售表名称
		    	this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateSaleGoodsNameBySgid",paramsObject);
			}
		}
		return ServiceResponse.buildSuccess("success");
	}
	
	//通过子品多规格属性值查询子品信息
	public ServiceResponse searchSonGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"psgid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		
		List<GoodsModel> sonGoods = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.searchSonGoods",paramsObject);
		return ServiceResponse.buildSuccess(sonGoods);
	}
	
	//修改子品价格接口
	@Transactional
  	public ServiceResponse updateSonGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
  		//1.修改子品基础表
  		Map<String,Object> returnMap = new HashMap<>();
  		this.setUpsert(false);
  		ServiceResponse response = this.onUpdate(session, paramsObject);
  		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
  		returnMap.put(this.getCollectionName(),response.getData());
       
  		//2.修改子品经营配置价格，修改商品销售表
  		JSONArray sonGoodsArray = paramsObject.getJSONArray("goods");
  		for (Object object : sonGoodsArray) {
  			this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateSonGoodsShopRef",(JSONObject)object);
  			this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateSaleGoodsNameBySgid",(JSONObject)object);
		}
  		return ServiceResponse.buildSuccess(returnMap);
  	}
		
    //封装事务回滚的异常信息返回前台
	public ServiceResponse caseInsert(ServiceSession session, JSONObject paramsObject){
		ServiceResponse response = null;
		try {
			GoodsServiceImpl proxyService = ((GoodsServiceImpl)SpringContextUtil.getBean(this.getClass()));
			response = proxyService.doCaseInsert(session,paramsObject);
		} catch (Exception e) {
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,e.getMessage());
		}
		return response;
	}
	
	//箱码新增
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse doCaseInsert(ServiceSession session, JSONObject paramsObject) throws Exception {
//		参数校验
		if(!paramsObject.containsKey("sgid")){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数必须有母品的id");
		}
		List<JSONObject> paramsObjectList = (List<JSONObject>) paramsObject.get("saleGoods");
		List<RowMap> shopList = null;
		JSONObject tmpShopef = new JSONObject();
		List<JSONObject> goodsMoreBarCodeList = new ArrayList<>();
		List<JSONObject> goodsSpecPriceList = new ArrayList<>();
		List<JSONObject> saleGoodsList = new ArrayList<>();
		List<String> mustFields = Arrays.asList("barNo", "goodsType", "salePrice");
		tmpShopef.put("sgid", paramsObject.get("sgid"));
		tmpShopef.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "shopId,shopCode,venderCode,vid");
		ServiceResponse shopRefResponse = goodsShopRefService.onQuery(session, tmpShopef);
		if(shopRefResponse.getReturncode() != "0"){
			logger.error(String.format("查询经营配置错误%s", shopRefResponse.getData()));
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询经营配置错误");
		}
		JSONObject shopRefObject = (JSONObject) shopRefResponse.getData();
		shopList = (List<RowMap>) shopRefObject.get(goodsShopRefService.getCollectionName());
		List<JSONObject> shopListObject = new ArrayList();
//		是否需要经营配置
			for (RowMap tmpShopObject:shopList
				 ) {
				if(tmpShopObject.get("shopId") != null || tmpShopObject.get("shopCode") != null){
					shopListObject.add(new JSONObject(tmpShopObject));
				}
			}
		if(shopListObject.size() < 1){
			logger.error(String.format("该商品没有做经营配置 %s",paramsObject.get("sgid") ));
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "该商品没有做经营配置");
		}
		JSONObject tmpQuery = new JSONObject();
		tmpQuery.put("sgid", paramsObject.get("sgid"));
		ServiceResponse ssData = onQuery(session, tmpQuery);
		if (ssData.getReturncode() != "0"){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询错误");
		}
		JSONObject tmpData = (JSONObject) ssData.getData();
		List<RowMap> tmpList = (List<RowMap>) tmpData.get(this.getCollectionName());
		if(tmpList.size() < 1 ){
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "需要设置箱码的单品不存在");
		}

		for (JSONObject tmpObject :paramsObjectList
				) {
			for (String tmpField: mustFields
					) {
				if(!tmpObject.containsKey(tmpField)){
					logger.info(String.format( "参数必须包括: %s", tmpField));
					return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, String.format( "参数必须包括: %s", tmpField));
				}
			}
			JSONObject tmpGoodsMoreBarCodeObject = new JSONObject();
			JSONObject tmpgoodsSpecPriceObject = new JSONObject();
			JSONObject tmpsaleGoodsObject = new JSONObject();
			long gsid = UniqueID.getUniqueID(true);
			RowMap tmpMap = tmpList.get(0);
			tmpMap.put("status", 1);
			tmpMap.put("creator", session.getUser_name());
			tmpMap.put("createDate", new Date());
			tmpMap.put("updateDate", new Date());
			tmpMap.put("salePrice", tmpObject.get("salePrice"));
			tmpMap.put("barNo", tmpObject.get("barNo"));
			tmpMap.put("priceFormGoods", tmpObject.get("priceFormGoods"));
			for (String key:tmpObject.keySet()
					) {
				tmpMap.put(key, tmpObject.get(key));

			}
//          先对多条码商品赋值
			tmpGoodsMoreBarCodeObject = (JSONObject) new JSONObject(tmpMap).clone();
			tmpGoodsMoreBarCodeObject.put("gsid", gsid);
//          对多条码价格表赋值
			tmpgoodsSpecPriceObject = (JSONObject) new JSONObject(tmpMap).clone();
			tmpgoodsSpecPriceObject.put("gspid", gsid);
			//          会员价 批发价 零售价保持一致
			tmpgoodsSpecPriceObject.put("customPrice", tmpgoodsSpecPriceObject.get("salePrice"));
			tmpgoodsSpecPriceObject.put("bulkPrice", tmpgoodsSpecPriceObject.get("salePrice"));
//          codeType 1 条码 (也就是 箱码商品) 2 plu码 (也就是plu称重商品)
			int codeType = 1;
			if(tmpObject.get("goodsType").equals(6) || tmpObject.get("goodsType").equals("6")){
				codeType = 2;
			}
			tmpGoodsMoreBarCodeObject.put("codeType", codeType);
			tmpgoodsSpecPriceObject.put("codeType", codeType);
//            修改sgid barNo salePrice mainBarcodeFlag goodsType
			tmpsaleGoodsObject = (JSONObject) new JSONObject(tmpMap).clone();
			tmpsaleGoodsObject.put("mainBarcodeFlag",0);
			tmpsaleGoodsObject.put("directFromErp",0);//是否来源ERP
			tmpsaleGoodsObject.put("goodsStatus",0);
//          参考售价 会员价 最低售价 批发价 和零售价保持一致
			List<String> priceKeyList = Arrays.asList("refPrice", "memberPrice", "minSalePrice", "bulkPrice", "primeCost");
			for (String priceKey:priceKeyList
					) {
				tmpsaleGoodsObject.put(priceKey, tmpsaleGoodsObject.get(priceKey));
			}
			for (JSONObject shopObject: shopListObject
				 ) {
				long ssgid = UniqueID.getUniqueID(true);
				JSONObject ttmpgoodsSpecPriceObject = (JSONObject) tmpgoodsSpecPriceObject.clone();
				tmpsaleGoodsObject.put("ssgid", ssgid);
				ttmpgoodsSpecPriceObject.put("shopId", shopObject.get("shopId"));
				ttmpgoodsSpecPriceObject.put("shopCode", shopObject.get("shopCode"));
				goodsSpecPriceList.add(ttmpgoodsSpecPriceObject);
				JSONObject ttmpsaleGoodsObject = (JSONObject) tmpsaleGoodsObject.clone();
				ttmpsaleGoodsObject.put("ssgid", ssgid);
				ttmpsaleGoodsObject.put("shopId",shopObject.get("shopId"));
				ttmpsaleGoodsObject.put("shopCode",shopObject.get("shopCode"));
				if(shopObject.containsKey("venderCode")){
					ttmpsaleGoodsObject.put("venderCode",shopObject.get("venderCode"));
				}
				if(shopObject.containsKey("vid")){
					ttmpsaleGoodsObject.put("vid",shopObject.get("vid"));
				}
				saleGoodsList.add(ttmpsaleGoodsObject);

			}
			goodsMoreBarCodeList.add(tmpGoodsMoreBarCodeObject);
		}

		JSONObject goodsMoreBarCodeObject = new JSONObject();
		goodsMoreBarCodeObject.put(goodsMoreBarCodeService.getCollectionName(), goodsMoreBarCodeList);
		ServiceResponse response1 =  goodsMoreBarCodeService.onInsert(session, goodsMoreBarCodeObject);
		if(!response1.getReturncode().equals(ResponseCode.SUCCESS)){
			throw new RuntimeException(response1.getData().toString());
		}
		JSONObject goodsSpecPriceObject = new JSONObject();
		goodsSpecPriceObject.put(goodsSpecPriceService.getCollectionName(), goodsSpecPriceList);
		ServiceResponse response2 =  goodsSpecPriceService.onInsert(session, goodsSpecPriceObject);
		if(!response2.getReturncode().equals(ResponseCode.SUCCESS)){
			throw new RuntimeException(response2.getData().toString());
		}
		JSONObject saleGoodsObject = new JSONObject();
		saleGoodsObject.put(saleGoodsServiceImpl.getCollectionName(), saleGoodsList);
		ServiceResponse rtResponse =  saleGoodsServiceImpl.onInsert(session, saleGoodsObject);
		if(!rtResponse.getReturncode().equals(ResponseCode.SUCCESS)){
			throw new RuntimeException(rtResponse.getData().toString());
		}
		return  rtResponse;
	}



	//子品删除
	@Override
	@Transactional
	public ServiceResponse batchDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"sgid" ,"goodsStatus");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		
		FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
		//1.删除商品表（逻辑删除）状态1：有效 0
		template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateGoods",paramsObject);
		//2.删除经营配置（逻辑删除）商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补'
		paramsObject.put("goodsStatus",GoodsStatus.TZXS.getValue());
		template.getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateGoodsShopRefGoods",paramsObject);
		//3.删除销售商品表（物理删除）
		paramsObject.put("table",saleGoodsServiceImpl.getCollectionName());
		paramsObject.put("key","sgid");
		paramsObject.put("values",paramsObject.getJSONArray("sgid"));
        template.getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
        //4.删除子品模板属性（物理删除）
        paramsObject.put("table",goodsPropertyExtServiceImpl.getCollectionName());
        template.getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
		return ServiceResponse.buildSuccess("success");
	}

  //商品分割、箱码
  public ServiceResponse searchDetail4Specified(ServiceSession session, JSONObject paramsObject) {
    if (!paramsObject.containsKey("goodsType")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
          "商品类型不能为空");
    }
    if (!paramsObject.containsKey("parentGoodsCode")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
          "母品编码不能为空");
    }
    if (!paramsObject.containsKey("entId")) {
      return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
          "零售商ID不能为空");
    }
    ServiceResponse rep = this.onQuery(session, paramsObject);
    FMybatisTemplate template = this.getTemplate();
    template.onSetContext(session);
    JSONObject jsonObj = (JSONObject) rep.getData();
    JSONArray array = jsonObj.getJSONArray("goods");
    
    for(int i=0; array != null && i<array.size(); i++) {
      JSONObject tmp = array.getJSONObject(i);
      List<ShopModel> shop = template.getSqlSessionTemplate().selectList("beanmapper.ShopModelMapper.selectByState", tmp);
      tmp.put("shopName", shop.get(0).getShopName());
      tmp.put("shopCode", shop.get(0).getShopCode());
    }
    
    JSONObject result = new JSONObject();
    result.put("goods", array);
    result.put("total_result", array == null ? 0: array.size());
    return ServiceResponse.buildSuccess(result);
  }

	/*
   * @Description: 提供给pos的服务，根据商品code 查询 商品名称
   * @param {"goodsCode":"001", "erpCode":"002"}
   * @return:
   */
	public ServiceResponse searchNameByCode(ServiceSession session, JSONObject paramsObject){
		if(!paramsObject.containsKey("goodsCode")){
			logger.error("参数错误，必须含有商品code");
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "参数错误，必须含有商品code");
		}
		if(!paramsObject.containsKey(com.product.model.BeanConstant.QueryField.PARAMKEY_FIELDS)){
			paramsObject.put(com.product.model.BeanConstant.QueryField.PARAMKEY_FIELDS, "goodsCode,erpCode,goodsName");
		}
		return onQuery(session, paramsObject);
	}
  
  
  @Override
  public ServiceResponse searchAllGoodsProcess(ServiceSession session, JSONObject paramsObject) {
	  FMybatisTemplate template = this.getTemplate();
	  ServiceResponse response = super.onQuery(session, paramsObject);
	  List<GoodsProcessModel> goodsprocessList = null;
	  if(response.getReturncode() != null && response.getReturncode().equals("0")){
		  JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
		  List<GoodsModel> goodsList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), GoodsModel.class);
		  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		  for(GoodsModel goods : goodsList){
			  Map<String, Object> saleGoodsMap = new HashMap<String,Object>();//商品表头
			  saleGoodsMap.put("sgid", goods.getSgid());
			  saleGoodsMap.put("barNo", goods.getBarNo());
			  saleGoodsMap.put("goodsCode", goods.getGoodsCode());
			  saleGoodsMap.put("goodsName", goods.getGoodsName());
			  saleGoodsMap.put("categoryId", goods.getCategoryId());
			  saleGoodsMap.put("salePrice", goods.getSalePrice());
			  saleGoodsMap.put("brandId", goods.getBrandId());
			  saleGoodsMap.put("brandName", goods.getBrandName());
			  saleGoodsMap.put("categoryId", goods.getCategoryId());
			  saleGoodsMap.put("categoryName", goods.getCategoryName());
			  resultList.add(saleGoodsMap);
			  
			  Criteria criteria = Criteria.where("sgid").is(goods.getSgid());
			  Query query = new Query(criteria);
			  query.limit(Integer.MAX_VALUE);
			  goodsprocessList = template.select(query, GoodsProcessModel.class, "goodsprocess");
			  
			  for(GoodsProcessModel gp: goodsprocessList) {
	    			saleGoodsMap = new HashMap<String,Object>();//分割商品
	    			saleGoodsMap.put("processType", gp.getProcessType());//加工方式
	    			saleGoodsMap.put("processFee", gp.getProcessFee());//加工费用
	    			saleGoodsMap.put("minProcessFee", gp.getMinProcessFee());//最近加工费用
	    			saleGoodsMap.put("ssgid", gp.getSsgid());//商品加工ID
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

  @Override
  public ServiceResponse searchAllXzsp(ServiceSession session, JSONObject paramsObject) throws Exception {
	  FMybatisTemplate template = this.getTemplate();
	  ServiceResponse response = this.search(session, paramsObject);
	  List<Map<String,Object>> xzspList = null;
	  if(response.getReturncode() != null && response.getReturncode().equals("0")){
		  JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
		  List<GoodsModel> goodsList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), GoodsModel.class);
		  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		  for(GoodsModel goods : goodsList){
			  Map<String, Object> goodsMap = new HashMap<String,Object>();
			  goodsMap.put("sgid", goods.getSgid());
			  goodsMap.put("barNo", goods.getBarNo());
			  goodsMap.put("goodsCode", goods.getGoodsCode());
			  goodsMap.put("goodsName", goods.getGoodsName());
			  goodsMap.put("categoryId", goods.getCategoryId());
			  goodsMap.put("salePrice", goods.getSalePrice());
			  resultList.add(goodsMap);
			  
			  JSONObject paramsObj = new JSONObject();
			  paramsObj.put("goodsType", 8);
			  paramsObj.put("goodsCode", goods.getGoodsCode());
			  paramsObj.put("erpCode", goods.getErpCode());
			  paramsObj.put("order_direction", "desc");
			  paramsObj.put("order_field", "updateDate");
			  ServiceResponse unquieMoreDetailResponse = saleGoodsService2Impl.unquieMoreDetailSearch(session, paramsObj);
			  if(unquieMoreDetailResponse.getReturncode() != null && unquieMoreDetailResponse.getReturncode().equals("0")){
				  JSONObject jsonData = (JSONObject)unquieMoreDetailResponse.getData();
				  xzspList = (List<Map<String,Object>>)jsonData.get(saleGoodsService2Impl.getCollectionName());
				  for(Map<String,Object> xzsp: xzspList) {
		    			goodsMap = new HashMap<String,Object>();//箱装商品
		    			goodsMap.put("xzspBarNo", xzsp.get("barNo"));//箱码
		    			goodsMap.put("xzspGoodsName", xzsp.get("goodsName"));//箱装名称
		    			goodsMap.put("xzspGoodsCode", xzsp.get("goodsCode"));//单品编码
		    			goodsMap.put("xzspPartsNum", xzsp.get("partsNum"));//箱装包装含量
		    			goodsMap.put("xzspSalePrice", xzsp.get("salePrice"));//箱装参考价
		    			goodsMap.put("xzspSsgid", xzsp.get("ssgid"));//箱装商品ID
			    		resultList.add(goodsMap);
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
  public ServiceResponse searchAllZp(ServiceSession session, JSONObject paramsObject) {
	  FMybatisTemplate template = this.getTemplate();
	  //ServiceResponse response = super.onQuery(session, paramsObject);
	  List<GoodsModel> zpList = null;
	  //if(response.getReturncode() != null && response.getReturncode().equals("0")){
		  //JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
//		  List<GoodsModel> goodsList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), GoodsModel.class);
//		  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		  for(GoodsModel goods : goodsList){
//			  Map<String, Object> goodsMap = new HashMap<String,Object>();//子品表头
//			  goodsMap.put("sgid", goods.getSgid());
//			  goodsMap.put("barNo", goods.getBarNo());
//			  goodsMap.put("goodsCode", goods.getGoodsCode());
//			  goodsMap.put("goodsName", goods.getGoodsName());
//			  goodsMap.put("categoryId", goods.getCategoryId());
//			  goodsMap.put("categoryName", goods.getCategoryName());
//			  goodsMap.put("salePrice", goods.getSalePrice());
//			  goodsMap.put("erpCode", goods.getErpCode());
//			  goodsMap.put("erpName", goods.getErpName());
//			  goodsMap.put("categoryCode", goods.getCategoryCode());
//			  goodsMap.put("entId", goods.getEntId());
//			  resultList.add(goodsMap);
//			  
//			  JSONObject paramsObj = new JSONObject();
//			  paramsObj.put("goodsStatus", 1);
//			  paramsObj.put("parentGoodsCode", goods.getGoodsCode());
//			  paramsObj.put(BeanConstant.QueryField.PARAMKEY_ORDERFLD, "createDate");
//			  paramsObj.put(BeanConstant.QueryField.PARAMKEY_ORDERDIR, "desc");
//			  paramsObj.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, Integer.MAX_VALUE);
////			  Criteria criteria = Criteria.where("goodsStatus").is(1).
////					  and("parentGoodsCode").is(goods.getGoodsCode());
////			  Query query = new Query(criteria);
////			  query.with(new Sort(new Order(Direction.DESC,"createDate")));
////			  query.limit(Integer.MAX_VALUE);
////			  zpList = template.select(query, GoodsModel.class, this.getCollectionName());
//			  ServiceResponse detailResponse = super.onQuery(session, paramsObj);
//			  if(detailResponse.getReturncode() != null && detailResponse.getReturncode().equals("0")){
//				  data =  JSONObject.parseObject(JSONObject.toJSONString(detailResponse.getData()));
//				  zpList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), GoodsModel.class);
//				  for(GoodsModel zp: zpList) {
//		    			goodsMap = new HashMap<String,Object>();//子品商品
//		    			goodsMap.put("zpBarNo", zp.getBarNo());//子品条码
//		    			goodsMap.put("zpGoodsCode", zp.getGoodsCode());//子品商品编码
//		    			goodsMap.put("zpGoodsName", zp.getGoodsName());//子品名称
//		    			goodsMap.put("zpParentGoodsCode", zp.getParentGoodsCode());//单品编码
//		    			goodsMap.put("zpSalePrice", zp.getSalePrice());//子品参考价
//		    			goodsMap.put("zpBrandCode", zp.getBrandCode());//子品商品ID
//		    			goodsMap.put("zpBrandId", zp.getBrandId());//子品品牌ID
//		    			goodsMap.put("zpBrandName", zp.getBrandName());//子品品牌名称
//		    			goodsMap.put("zpCategoryCode", zp.getCategoryCode());//子品品类编码
//		    			goodsMap.put("zpCategoryId", zp.getCategoryId());//子品品类ID
//		    			goodsMap.put("zpCategoryName", zp.getCategoryName());//子品品类名称
//			    		resultList.add(goodsMap);
//				  }
//			  }
//		  }
//		  JSONObject result = new JSONObject();
//		  result.put(this.getCollectionName(), resultList);
//		  result.put("total_results", data.get("total_results"));
//		  return ServiceResponse.buildSuccess(result);
		  
	  //}
	  Integer pageno = BeanConstant.QueryPage.DEFAULT_PAGENO;
	  Integer pageSize = BeanConstant.QueryPage.DEFAULT_PAGESIZE;
	  
	  if(paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGENO)) {
		  pageno = paramsObject.getInteger(BeanConstant.QueryField.PARAMKEY_PAGENO) - 1;
	  }
	  if(paramsObject.containsKey(BeanConstant.QueryField.PARAMKEY_PAGESIZE)) {
		  pageSize = paramsObject.getInteger(BeanConstant.QueryField.PARAMKEY_PAGESIZE);
	  }
	  
	  if(pageno < 0) {
		  pageno = 0;
	  }
	  paramsObject.put("offset", pageno * pageSize);
	  paramsObject.put("limit", pageSize);
	  
	  List<GoodsModel> goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.findAllZp", paramsObject);
	  Long total_results = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.findAllZpCount", paramsObject);
	  
	  List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
	  for(GoodsModel goods : goodsList){
		  Map<String, Object> goodsMap = new HashMap<String,Object>();//子品表头
		  goodsMap.put("sgid", goods.getSgid());
		  goodsMap.put("barNo", goods.getBarNo());
		  goodsMap.put("goodsCode", goods.getGoodsCode());
		  goodsMap.put("goodsName", goods.getGoodsName());
		  goodsMap.put("categoryId", goods.getCategoryId());
		  goodsMap.put("categoryName", goods.getCategoryName());
		  goodsMap.put("salePrice", goods.getSalePrice());
		  goodsMap.put("erpCode", goods.getErpCode());
		  goodsMap.put("erpName", goods.getErpName());
		  goodsMap.put("categoryCode", goods.getCategoryCode());
		  goodsMap.put("entId", goods.getEntId());
		  resultList.add(goodsMap);
		  
		  JSONObject paramsObj = new JSONObject();
		  paramsObj.put("goodsStatus", 1);
		  paramsObj.put("parentGoodsCode", goods.getGoodsCode());
		  paramsObj.put(BeanConstant.QueryField.PARAMKEY_ORDERFLD, "createDate");
		  paramsObj.put(BeanConstant.QueryField.PARAMKEY_ORDERDIR, "desc");
		  paramsObj.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, Integer.MAX_VALUE);
		  ServiceResponse detailResponse = super.onQuery(session, paramsObj);
		  if(detailResponse.getReturncode() != null && detailResponse.getReturncode().equals("0")){
			  JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(detailResponse.getData()));
			  zpList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), GoodsModel.class);
			  for(GoodsModel zp: zpList) {
	    			goodsMap = new HashMap<String,Object>();//子品商品
	    			goodsMap.put("zpBarNo", zp.getBarNo());//子品条码
	    			goodsMap.put("zpGoodsCode", zp.getGoodsCode());//子品商品编码
	    			goodsMap.put("zpGoodsName", zp.getGoodsName());//子品名称
	    			goodsMap.put("zpParentGoodsCode", zp.getParentGoodsCode());//单品编码
	    			goodsMap.put("zpSalePrice", zp.getSalePrice());//子品参考价
	    			goodsMap.put("zpBrandCode", zp.getBrandCode());//子品商品ID
	    			goodsMap.put("zpBrandId", zp.getBrandId());//子品品牌ID
	    			goodsMap.put("zpBrandName", zp.getBrandName());//子品品牌名称
	    			goodsMap.put("zpCategoryCode", zp.getCategoryCode());//子品品类编码
	    			goodsMap.put("zpCategoryId", zp.getCategoryId());//子品品类ID
	    			goodsMap.put("zpCategoryName", zp.getCategoryName());//子品品类名称
		    		resultList.add(goodsMap);
			  }
		  }
	  }
	  JSONObject result = new JSONObject();
	  result.put(this.getCollectionName(), resultList);
	  result.put("total_results", total_results);
	  return ServiceResponse.buildSuccess(result);
  }
  
  /**
   * @author liupq
   * @param session
   * @param paramsObject
   * @return
   * 
   * 	门店作业系统获取需要补货的商品id数组，此商品数组已按门店获取，所以这里直接到goods表获取商品信息，根据category和商品状态为1获取
   */
  public ServiceResponse findBySdidsAndCid(ServiceSession session, JSONObject paramsObject) {
	  ServiceResponse check = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "categoryId","goodsIds");
	  if(!check.getReturncode().equals(ResponseCode.SUCCESS)) {
		  return check;
	  }
	  if(paramsObject.containsKey("page_no") && paramsObject.containsKey("page_size")) {
		 int pageNo = paramsObject.getIntValue("page_no");
		 int pageSize = paramsObject.getIntValue("page_size");
		 if(pageNo > 0) {
			 paramsObject.put("page_no", (pageNo-1)*pageSize);
		 }else {
			 paramsObject.put("page_no", 0);
		 }
	  }
	  FMybatisTemplate template = this.getTemplate();
	  template.onSetContext(session);
	  List<Map<String,Object>> list = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.findBySgidAndCid",paramsObject);
	  JSONObject json = new JSONObject();
	  if(null != list) {
		json.put(this.getCollectionName(), list);
		json.put("total_results", list.size());
	  }else {
		  json.put(this.getCollectionName(), "[]");
		  json.put("total_results", 0);
	  }
	  return ServiceResponse.buildSuccess(json);
  }
  
  /**
   *  是否受限商品查询      season--时令属性(永旺占用！受限商品0：否，1：是)
   * @param session
   * @param paramsObject
   * @return
   */
	public ServiceResponse getRestrictedGoods(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "restricted");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 0);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
        
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        // 受限标识(0:否,1:是)
        String restricted = paramsObject.getString("restricted");
        
        long total_results = 0;
    	paramsObject.put("season", restricted);
    	List goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.getRestrictedGoods", paramsObject);
        if (goodsList != null && goodsList.size() > 0) {
            total_results = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.countRestrictedGoods", paramsObject);
            //查询artCode的名称
            JSONObject selectParams = new JSONObject();
            Set artCodeSet = new HashSet();
            goodsList.forEach(action->{
            	Map data = (Map)action;
            	artCodeSet.add(data.get("artiCode"));
            });
            
            selectParams.put("key", "categoryCode");
            selectParams.put("values", artCodeSet);
            selectParams.put("table","category");
            selectParams.put("field","categoryCode as mapkey, categoryName as mapvalue");
            MapResultHandler artiCodeMap = new MapResultHandler();
            template.getSqlSessionTemplate().select("beanmapper.AdvancedQueryMapper.selectMap", selectParams,artiCodeMap);
    		Map<String,String> artiCodeMapper = artiCodeMap.getMappedResults();
    		
    		goodsList.forEach(action ->{
    			Map data = (Map)action;
    			data.put("artiCode",data.get("artiCode")+"-"+artiCodeMapper.get(data.get("artiCode")));
    		});  
        }

		JSONObject result = new JSONObject();
        result.put(this.getCollectionName(), goodsList);
        result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
	/**
	 * 是否受限商品设置 season--时令属性(永旺占用！受限商品0：否，1：是)
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse saveRestrictedGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject, "erpCode", "restricted","goodsCodes");
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		// 受限标识(0:否,1:是)
        String restricted = paramsObject.getString("restricted");
        paramsObject.put("season",restricted);
        paramsObject.put("entId",session.getEnt_id());
        paramsObject.put("updateDate",new Date());
		int g = this.getTemplate().getSqlSessionTemplate().update("beanmapper.GoodsModelMapper.batchRestrictedGoods",paramsObject);
		
		int s = this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.batchRestrictedSaleGoods",paramsObject);
		
		return ServiceResponse.buildSuccess("更新成功！ goods: "+g+" salegoods: "+s);
	}
	
	/**
	 * 导出--全部受限商品 season--时令属性(永旺占用！受限商品0：否，1：是)
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse exportRestrictedGoods(ServiceSession session, JSONObject paramsObject) throws Exception {

		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
        
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 0);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
		
        FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        paramsObject.put("entId", session.getEnt_id());
        List goodsList = new ArrayList<>();
        long total_results = 0;
        
    	goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.exportRestrictedGoods", paramsObject);
    	if (goodsList != null && goodsList.size() > 0) {
            total_results = goodsList.size();
        }
        
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), goodsList);
		result.put("total_results", total_results);
		System.out.println("***************************查询结束**************************************");
		return ServiceResponse.buildSuccess(result);
	}
  
	/**
	 * 库存中心--保存全部的全店商品
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse saveQDGoodsToStock(ServiceSession session, JSONObject paramsObject) {
		
		// 传参校验
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		
		// 找出全部全店商品
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id()).and("shopSheetType").is(1);
    	Query query = new Query(criteria);
    	Field fields = query.fields();
		fields.include("sgid,entId,erpCode,goodsCode,goodsStatus");
		List<GoodsModel> goodsList = this.getTemplate().select(query, GoodsModel.class,this.getCollectionName());
    	
		// 调用库存中心保存接口，写入数据
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), goodsList);
		result.put("total_results", goodsList.size());
		return ServiceResponse.buildSuccess(result);
	}
	
	 /**
	 * 折扣商品--查询
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse getDiscountGoodsList(ServiceSession session, JSONObject paramsObject) {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		
		// 设置默认分页参数
		if (!paramsObject.containsKey("page_size")) {
			paramsObject.put("page_size", 10);
		}
        if (!paramsObject.containsKey("page_no")) {
            paramsObject.put("page_no", 0);
        } else {
            paramsObject.put("page_no",(paramsObject.getInteger("page_no") - 1) * paramsObject.getInteger("page_size"));
        }
        
        FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		// 查询折扣商品总数
		long total_results = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.countDiscountGoods", paramsObject);
		// 查询商品
		List goodsList = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.getDiscountGoodsList", paramsObject);
		if (goodsList == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "商品查询失败！");
		}
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(goodsList));
		DefaultParametersUtils.numberFormat(array, "#0.00", "salePrice");// 价格格式处理
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), array);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
	 /**
	 * 折扣商品--保存
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse saveDiscountGoods(ServiceSession session, JSONObject paramsObject)  throws Exception {
		
		ServiceResponse checkParam = ParamValidateUtil.checkParam(session, paramsObject);
		if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
			return checkParam;
		}
		if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", this.getCollectionName()));
		}
		JSONArray goodsList = paramsObject.getJSONArray(this.getCollectionName());
		if(goodsList == null) 	goodsList = new JSONArray();
		// 统一时间
		String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		// 参数校验
		int goodsSize = goodsList.size();
		for (int i = 0; i < goodsSize; i++) {
			checkParam = ParamValidateUtil.checkParam(session, goodsList.getJSONObject(i), "sgid","minDiscount");
			if (!ResponseCode.SUCCESS.equals(checkParam.getReturncode())) {
				return checkParam;
			}
			JSONObject model = goodsList.getJSONObject(i);
//				BigDecimal temp = new BigDecimal(minDiscount);
			BigDecimal temp = model.getBigDecimal("minDiscount");
			if (temp.compareTo(new BigDecimal(0)) == -1) {
				return ServiceResponse.buildFailure(session,ResponseCode.FAILURE, String.format("保存失败，选中数据第[%d]行最低折扣率不能小于0", i+1));
			}
			if (temp.compareTo(new BigDecimal(1)) == 1) {
				return ServiceResponse.buildFailure(session,ResponseCode.FAILURE, String.format("保存失败，选中数据第[%d]行最低折扣率不能大于1", i+1));
			}
			Float minDiscount = Float.parseFloat(model.get("minDiscount").toString());
			model.put("minDiscount", minDiscount);
			model.put("updateDate", updateDateString);
			model.put("modifier", session.getUser_name());
		}
		
		this.setUpsert(false);
		if (goodsSize > 0) {
			// 更新goods
			ServiceResponse result = this.onUpdate(session, paramsObject);
			if (!ResponseCode.SUCCESS.equals(result.getReturncode())) {
				throw new RuntimeException("更新goods失败，" + result.getData().toString());
			}
			
			// 更新salegoods
			List<Map<String, Object>> updateSaleGoods = new ArrayList<>();
			Map<String, Object> map = null;
			for (int i = 0; i < goodsSize; i++) {
				JSONObject goodsModel = goodsList.getJSONObject(i);
				map = new HashMap<>();
				map.put("updateDate", updateDateString);
				map.put("sgid", goodsModel.get("sgid"));
				map.put("minDiscount", goodsModel.get("minDiscount"));
				updateSaleGoods.add(map);
			}
			if(!updateSaleGoods.isEmpty()) {
				long s = this.getTemplate().getSqlSessionTemplate().update("beanmapper.SaleGoodsModelMapper.updateDiscountSaleGoods",updateSaleGoods);
			}
		} 
		return ServiceResponse.buildSuccess("success");
	}
}

enum DeliveryFlag {  
	COM("0","普通商品"),YES("1","行送商品"),NO("2","非行送商品");
    private String key;
    private String value;

    private DeliveryFlag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
    public static boolean isInclude(String key){
        boolean include = false;
        for (DeliveryFlag e: DeliveryFlag.values()){
            if(e.getKey().equals(key)){
                include = true;
                break;
            }
        }
        return include;
    }
}
