package com.efuture.omdmain.component;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.CategoryModel;
import com.efuture.omdmain.model.DecomposeRecipeDetailModel;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.ProcessRecipeDetailModel;
import com.efuture.omdmain.model.ProcessRecipeModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.service.ProcessRecipeService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.efuture.omdmain.utils.SpringContextUtil;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.FileProcessorUtils;
import com.product.util.UniqueID;

public class ProcessRecipeServiceImpl extends JDBCCompomentServiceImpl<ProcessRecipeModel>
		implements ProcessRecipeService {

	@Autowired
	private GoodsServiceImpl goodsService;

	@Autowired
	private ProcessRecipeDetailServiceImpl processRecipeDetailService;

	public ProcessRecipeServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);

		DefaultParametersUtils.removeEmptyParams(paramsObject);
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		List<Map<String, Object>> processRecipe = template.getSqlSessionTemplate()
				.selectList("beanmapper.ProcessRecipeModelMapper.selectByState", paramsObject);
		long total_results = template.getSqlSessionTemplate()
				.selectOne("beanmapper.ProcessRecipeModelMapper.selectByStateCount", paramsObject);

		List<Long> categoryIds = new ArrayList<Long>();
		for (int i = 0; i < processRecipe.size(); i++) {
			Map<String, Object> tmp = processRecipe.get(i);
			if (tmp.get("categoryId") != null)
				categoryIds.add(Long.parseLong(tmp.get("categoryId").toString()));
		}

		List<CategoryModel> categorys = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.put("categoryIds", categoryIds);
			categorys = template.getSqlSessionTemplate()
					.selectList("beanmapper.CategoryModelMapper.selectInCategoryIds", paramsObject);
		}

		for (int i = 0; i < processRecipe.size(); i++) {
			Map<String, Object> tmp = processRecipe.get(i);
			for (int j = 0; categorys != null && j < categorys.size(); j++) {
				if (tmp.get("categoryId") != null && Long.parseLong(tmp.get("categoryId").toString()) == categorys
						.get(j).getCategoryId().longValue()) {
					tmp.put("categoryName", categorys.get(j).getCategoryName());
					tmp.put("categoryCode", categorys.get(j).getCategoryCode());
					break;
				}
			}
		}

		JSONObject result = new JSONObject();
		result.put("total_results", total_results);
		result.put(this.getCollectionName(), processRecipe);
		return ServiceResponse.buildSuccess(result);
	}

	/**
	 * @Title: searchByGoodsCode @Description:
	 * 加工配方定义，按照商品编码搜索加工配方 @param: @param session @param: @param
	 * paramsObject @param: @return @return: ServiceResponse @throws
	 */
	public ServiceResponse searchByGoodsCode(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("goodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "goodsCode不能为空");
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "erpCode不能为空");
		}
		String erpCode = paramsObject.getString("erpCode");
		
		paramsObject.put("entId", session.getEnt_id());
		DefaultParametersUtils.removeEmptyParams(paramsObject);
		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		Map<String, Object> processRecipe = template.getSqlSessionTemplate()
				.selectOne("beanmapper.ProcessRecipeModelMapper.selectByState", paramsObject);

		Long prid = null;
		Long categoryId = null;
		ServiceResponse rep = null;
		if (processRecipe == null) {
			// 根据条码返回相关信息
			rep = goodsService.onQuery(session, paramsObject);
			if (!ResponseCode.SUCCESS.equals(rep.getReturncode())) {
				return rep;
			}
			JSONObject data = (JSONObject) rep.getData();
			JSONArray array = data.getJSONArray("goods");

			if (array == null || array.isEmpty())
				data.put("entity", new HashMap<>());
			else
				data.put("entity", array.get(0));

			data.put("detail", new ArrayList<>());
			data.remove("goods");
			return ServiceResponse.buildSuccess(data);
		}

		if (processRecipe.get("prid") != null)
			prid = Long.parseLong(processRecipe.get("prid").toString());
		if (processRecipe.get("categoryId") != null)
			categoryId = Long.parseLong(processRecipe.get("categoryId").toString());

		// 添加品类信息
		CategoryModel category = null;
		if (categoryId != null) {
			paramsObject.clear();
			List<Long> categoryIds = new ArrayList<>();
			categoryIds.add(categoryId);
			paramsObject.put("categoryIds", categoryIds);
			category = template.getSqlSessionTemplate().selectOne("beanmapper.CategoryModelMapper.selectInCategoryIds",
					paramsObject);
		}
		if (category != null) {
			processRecipe.put("categoryCode", category.getCategoryCode());
			processRecipe.put("categoryName", category.getCategoryName());
		}

		// 添加明细信息
		List<Map<String, Object>> detail = new ArrayList<>();
		if (prid != null) {
			paramsObject.clear();
			paramsObject.put("prid", prid);
			paramsObject.put("erpCode", erpCode);
			paramsObject.put("entId", session.getEnt_id());
			detail = template.getSqlSessionTemplate()
					.selectList("beanmapper.ProcessRecipeDetailModelMapper.selectByPrid4Map", paramsObject);
		}

		JSONObject result = new JSONObject();
		result.put("entity", processRecipe);
		result.put("detail", detail);
		return ServiceResponse.buildSuccess(result);
	}

	@Override
	@Transactional
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("entity")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "entity不能为空");
		}
		if (!paramsObject.containsKey("detail")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "detail不能为空");
		}

		ServiceResponse validateResponse = this.validateDetailInput(session, paramsObject.getJSONArray("detail"),
				new String[] { "^\\d{0,9}\\.\\d{0,3}$|^\\d{0,9}$", "^\\d{0,1}\\.\\d{0,4}$|^\\d{0,1}$" },
				new String[] { "weight", "recipeRate" }, new String[] { "重量", "配方比" });
		if (!ResponseCode.SUCCESS.equals(validateResponse.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_ERROR,
					validateResponse.getData().toString());
		}

		JSONObject entity = paramsObject.getJSONObject("entity");
		String erpCode = entity.getString("erpCode");
		if (!entity.containsKey("sgid")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "sgid不能为空");
		}
		if (!entity.containsKey("goodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "sgid不能为空");
		}

		String creator = Long.toString(session.getUser_id());
		entity.put("entId", session.getEnt_id());
		entity.put("creator", creator);
		entity.put("modifier", creator);
		entity.put("createDate", new java.util.Date());
		entity.put("updateDate", new java.util.Date());

		String id = Long.toString(UniqueID.getUniqueID(true));
		String goodsType = "4";
		String sheetNo = goodsType + id.substring(id.length() - 8);
		entity.put("sheetNo", sheetNo);

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		ServiceResponse rep = null;
		Long prid = null;

		if (entity.get("processType") != null && "1".equals(entity.get("processType").toString())) {
			entity.put("processType", true);
		} else {
			entity.put("processType", false);
		}
		if (!entity.containsKey("prid")) {
			rep = this.onInsert(session, entity);
			JSONObject obj = (JSONObject) rep.getData();
			prid = obj.getLong("prid");
		} else {
			prid = entity.getLong("prid");
			rep = this.onUpdate(session, entity);
			template.getSqlSessionTemplate().delete("beanmapper.ProcessRecipeDetailModelMapper.deleteByPrid", entity);
		}
		entity.put("prid", prid);

		if (!ResponseCode.SUCCESS.equals(rep.getReturncode())) {
			return rep;
		}

		JSONArray detail = paramsObject.getJSONArray("detail");
		for (int i = 0; detail != null && i < detail.size(); i++) {
			JSONObject tmp = detail.getJSONObject(i);

			// 添加商品进价、规格
			JSONObject tmpObj = new JSONObject();
			tmpObj.put("entId", session.getEnt_id());
			tmpObj.put("erpCode", erpCode);
			tmpObj.put("goodsCode", tmp.get("goodsCode"));
			ServiceResponse tmpResponse = this.goodsService.onQuery(session, tmpObj);
			if (!ResponseCode.SUCCESS.equals(tmpResponse.getReturncode())) {
				return tmpResponse;
			}
			JSONObject tmpData = (JSONObject) tmpResponse.getData();
			JSONArray tmpArray = tmpData.getJSONArray(this.goodsService.getCollectionName());
			if (tmpArray == null || tmpArray.size() <= 0) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "系统找不到明细商品信息.");
			}
			tmpData = tmpArray.getJSONObject(0);

			tmp.put("goodsSpec", tmpData.get("saleSpec"));
			tmp.put("purPriceAmount", tmpData.get("primeCost"));
			tmp.put("prdid", UniqueID.getUniqueID(true));
			tmp.put("prid", prid);
			ServiceResponse tmpRep = this.processRecipeDetailService.onInsert(session, tmp);
			if (!ResponseCode.SUCCESS.equals(tmpRep.getReturncode())) {
				throw new RuntimeException(tmpRep.getData().toString());
			}
		}

		entity.remove("servicesession");
		return ServiceResponse.buildSuccess(entity);
	}

	private ServiceResponse validateDetailInput(ServiceSession session, JSONArray detail, String[] regex,
			String[] fields, String[] fieldNames) {
		if (detail == null || detail.isEmpty()) {
			return ServiceResponse.buildSuccess("");
		}

		for (int i = 0; i < detail.size(); i++) {
			JSONObject obj = detail.getJSONObject(i);
			for (int j = 0; j < regex.length; j++) {
				if (StringUtils.isNotEmpty(obj.getString(fields[j]))) {
					if (!DefaultParametersUtils.regexValidate(regex[j], obj.getString(fields[j]))) {
						return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY,
								"加工配方单明细" + fieldNames[j] + "输入不符合要求.");
					}
				}
			}
		}

		return ServiceResponse.buildSuccess("");
	}

	/**
	 * @Title: batchDelete @Description: 加工配方删除 @param: @param
	 * session @param: @param paramsObject @param: @return @return:
	 * ServiceResponse @throws
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse batchDelete(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("prids")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "参数prids不能为空.");
		}

		JSONArray array = paramsObject.getJSONArray("prids");
		if (array.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "请勾选要删除的配方.");
		}

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		long rows = template.getSqlSessionTemplate().delete("beanmapper.ProcessRecipeModelMapper.batchDelete",
				paramsObject);
		template.getSqlSessionTemplate().delete("beanmapper.ProcessRecipeDetailModelMapper.batchDeleteByPrid",
				paramsObject);
		return ServiceResponse.buildSuccess("删除成功!");
	}

	/**
	 * @Title: searchByDeatilGoodsCode @Description:
	 * 商品查询中心-加工配方定义 @param: @param session @param: @param
	 * paramsObject @param: @return @return: ServiceResponse @throws
	 */
	public ServiceResponse searchByDeatilGoodsCode(ServiceSession session, JSONObject paramsObject) {
		if (!paramsObject.containsKey("goodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "商品编码不能为空.");
		}

		FMybatisTemplate template = this.getTemplate();
		template.onSetContext(session);
		paramsObject.put("entId", session.getEnt_id());
		List<Map<String, Object>> processRecipeInfo = template.getSqlSessionTemplate()
				.selectList("beanmapper.ProcessRecipeModelMapper.selectByDetailGoodsCode", paramsObject);

		List<Long> sgids = new ArrayList<>();
		for (int i = 0; i < processRecipeInfo.size(); i++) {
			if (processRecipeInfo.get(i).get("sgid") != null)
				sgids.add(Long.parseLong(processRecipeInfo.get(i).get("sgid").toString()));

			if (processRecipeInfo.get(i).get("processType") == null) {
				processRecipeInfo.remove("processType");
			} else if (Boolean.parseBoolean(processRecipeInfo.get(i).get("processType").toString())) {
				processRecipeInfo.get(i).put("processType", 1);
			} else {
				processRecipeInfo.get(i).put("processType", 0);
			}
		}

		// 添加商品名称
		List<GoodsModel> goodsModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sgids)) {
			paramsObject.put("sgids", sgids);
			goodsModel = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID",
					paramsObject);
		}

		List<Long> categoryIds = new ArrayList<>();
		for (int i = 0; i < goodsModel.size(); i++) {
			if (processRecipeInfo.get(i).get("categoryId") != null)
				categoryIds.add(Long.parseLong(processRecipeInfo.get(i).get("categoryId").toString()));
		}

		List<CategoryModel> categoryModel = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			paramsObject.clear();
			paramsObject.put("categoryIds", categoryIds);
			categoryModel = template.getSqlSessionTemplate()
					.selectList("beanmapper.CategoryModelMapper.selectInCategoryIds", paramsObject);
		}

		for (Map<String, Object> map : processRecipeInfo) {
			// 添加商品名称
			for (int i = 0; i < goodsModel.size(); i++) {
				GoodsModel g = goodsModel.get(i);
				if (map.get("sgid") != null && Long.parseLong(map.get("sgid").toString()) == g.getSgid()) {
					map.put("goodsNameM", g.getGoodsName());
					map.put("goodsCodeM", g.getGoodsCode());
					map.put("measureUnit", g.getMeasureUnit());
					map.put("categoryId", g.getCategoryId());
					break;
				}
			}

			// 添加工业分类信息
			for (int i = 0; i < categoryModel.size(); i++) {
				CategoryModel c = categoryModel.get(i);
				if (map.get("categoryId") != null
						&& Long.parseLong(map.get("categoryId").toString()) == c.getCategoryId().longValue()) {
					map.put("categoryName", c.getCategoryName());
					break;
				}
			}
		}

		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), processRecipeInfo);
		result.put("total_results", processRecipeInfo.size());
		return ServiceResponse.buildSuccess(result);
	}

	@Override
	public ServiceResponse searchAllDetail(ServiceSession session, JSONObject paramsObject) {
		ServiceResponse response = this.search(session, paramsObject);
		FMybatisTemplate template = this.getTemplate();
		List<ProcessRecipeDetailModel> processRecipeDetailList = null;
		if (response.getReturncode() != null && response.getReturncode().equals("0")) {
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			List<ProcessRecipeModel> processRecipeList = JSONArray
					.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), ProcessRecipeModel.class);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			int i = 1;
			int j = 1;
			for (ProcessRecipeModel pr : processRecipeList) {
				Map<String, Object> prMap = new HashMap<String, Object>();
				prMap.put("xh", i);
				prMap.put("prid", pr.getPrid());
				prMap.put("sheetNo", pr.getSheetNo());
				prMap.put("sgid", pr.getPrid());
				prMap.put("barNo", pr.getBarNo());
				prMap.put("goodsCode", pr.getGoodsCode());
				prMap.put("goodsName", pr.getGoodsName());
				prMap.put("categoryId", pr.getCategoryId());
				prMap.put("categoryCode", pr.getCategoryCode());
				prMap.put("categoryName", pr.getCategoryName());
				prMap.put("processType", pr.getProcessType());
				prMap.put("entId", pr.getEntId());
				prMap.put("modifier", pr.getModifier());
				prMap.put("updateDate", pr.getUpdateDate());
				prMap.put("createDate", pr.getCreateDate());
				resultList.add(prMap);
				i++;

				Criteria criteria = Criteria.where("prid").is(pr.getPrid());
				Query query = new Query(criteria);
				processRecipeDetailList = template.select(query, ProcessRecipeDetailModel.class, "processrecipedetail");
				j = 1;
				for (ProcessRecipeDetailModel prd : processRecipeDetailList) {
					prMap = new HashMap<String, Object>();
					prMap.put("dxh", j);
					prMap.put("prdid", prd.getPrdid());
					prMap.put("dGoodsCode", prd.getGoodsCode());
					prMap.put("dBarCode", prd.getBarNo());
					prMap.put("dGoodsName", prd.getGoodsName());
					prMap.put("dCategoryCode", prd.getCategoryCode());
					prMap.put("dCategoryName", prd.getCategoryName());
					prMap.put("dPurPriceAmount", prd.getPurPriceAmount());
					prMap.put("dGoodsSepc", prd.getGoodsSpec());
					prMap.put("dWeight", prd.getWeight());
					prMap.put("dRecipeRate", prd.getRecipeRate());
					resultList.add(prMap);
					j++;
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
	public List<Map<String, Object>> onBeforeImportData(String params, MultipartFile file) throws Exception {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		JSONObject jsonparam = JSONObject.parseObject(params);
		String type = jsonparam.getString("type");
		if (type.equals("excel")) {
			if (file == null) {
				throw new Exception("缺少上传文件");
			}
			dataMap.put("processRecipe", FileProcessorUtils.parseFile(jsonparam, file.getInputStream()));
			jsonparam.put("sheetIndex", 1);
			jsonparam.put("cols", "entId,goodsCode,dGoodsCode,recipeRate");
			dataMap.put("processRecipeDetail", FileProcessorUtils.parseFile(jsonparam, file.getInputStream()));
			dataList.add(dataMap);
		}
		return dataList;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public ServiceResponse onImportData(ServiceSession session, String params, MultipartFile file) throws Exception {
		ServiceResponse response = null;
		List<Map<String, Object>> dataList = null;
		FMybatisTemplate template = this.getTemplate();
		try {
			dataList = onBeforeImportData(params, file);
		} catch (Exception e) {
			response = new ServiceResponse();
			response.setReturncode(ResponseCode.EXCEPTION);
			response.setData(e.getMessage());
			return response;
		}
		Criteria criteria = null;
		Query query = null;
		ProcessRecipeModel pr = null;
		ProcessRecipeDetailModel prd = null;
		GoodsModel goods = null;
		if (!dataList.isEmpty()) {
			Map<String, Object> dataMap = dataList.get(0);
			List<Map<String, Object>> prMapList = (List<Map<String, Object>>) dataMap.get("processRecipe");
			List<Map<String, Object>> prDetailMapList = (List<Map<String, Object>>) dataMap.get("processRecipeDetail");
			JSONObject paramsObject = null;
			Map<String, Long> pridMap = new HashMap<String, Long>();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for (Map<String, Object> prMap : prMapList) {
				paramsObject = JSONObject.parseObject(JSON.toJSONString(prMap));
				paramMap.put("entId", paramsObject.get("entId"));
				paramMap.put("goodsCode", paramsObject.get("goodsCode"));
				goods = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.getGoodsByGoodsCode",
						paramMap);
				if (goods == null) {
					throw new Exception("商品编码" + paramsObject.get("goodsCode") + "不存在");
				}

				criteria = Criteria.where("entId").is(paramsObject.get("entId")).and("goodsCode")
						.is(paramsObject.get("goodsCode"));
				query = new Query(criteria);
				pr = template.selectOne(query, ProcessRecipeModel.class, "processrecipe");
				if (pr == null) {
					paramsObject.put("sgid", goods.getSgid());
					paramsObject.put("creator", session.getUser_name());
					paramsObject.put("createDate", new Date());
					this.onInsert(session, paramsObject);
				} else {
					paramsObject.put("sgid", goods.getSgid());
					paramsObject.put("modifier", session.getUser_name());
					paramsObject.put("updateDate", new Date());
					paramsObject.put(this.getKeyfieldName(), pr.getPrid());
					this.onUpdate(session, paramsObject);
				}
				pridMap.put(paramsObject.getString("goodsCode"), paramsObject.getLong("prid"));
			}
			query = new Query(Criteria.where("prid").in(pridMap.values()));
			template.delete(query, ProcessRecipeDetailModel.class, "processrecipedetail");

			Map<String, Double> recipeRateMap = new HashMap<String, Double>();
			for (Map<String, Object> prdMap : prDetailMapList) {
				paramsObject = JSONObject.parseObject(JSON.toJSONString(prdMap));
				String goodsCode = paramsObject.getString("goodsCode");
				Long prid = pridMap.get(paramsObject.getString("goodsCode"));
				if (prid == null) {
					throw new Exception("加工配方单主表商品" + paramsObject.get("goodsCode") + "不存在");
				}

				paramMap.clear();
				paramMap.put("entId", paramsObject.get("entId"));
				paramMap.put("goodsCode", paramsObject.get("dGoodsCode"));
				goods = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.getGoodsByGoodsCode",
						paramMap);
				if (goods == null) {
					throw new Exception("商品编码" + paramsObject.get("goodsCode") + "不存在");
				}

				paramsObject.put("prid", prid);
				paramsObject.put("goodsCode", paramsObject.get("dGoodsCode"));
				paramsObject.put("barNo", goods.getBarNo());
				paramsObject.put("goodsName", goods.getGoodsName());
				paramsObject.put("categoryCode", goods.getCategoryCode());
				paramsObject.put("categoryName", goods.getCategoryName());
				paramsObject.put("goodsSpec", goods.getMeasureUnit());
				paramsObject.put("purPriceAmount", goods.getPrimeCost());
				paramsObject.put("weight", goods.getRweight());
				processRecipeDetailService.onInsert(session, paramsObject);
				if (recipeRateMap.containsKey(goodsCode)) {
					recipeRateMap.put(goodsCode, recipeRateMap.get(goodsCode) + paramsObject.getDouble("recipeRate"));
				} else {
					recipeRateMap.put(goodsCode, paramsObject.getDouble("recipeRate"));
				}
			}
			for (Entry<String, Double> entry : recipeRateMap.entrySet()) {
				if (entry.getValue() != 1) {
					throw new Exception("分解配方单主表商品" + entry.getKey() + "配方比不为1");
				}
			}
		}
		return ServiceResponse.buildSuccess("");
	}
}
