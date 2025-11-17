package com.efuture.omdmain.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.DecomposeRecipeDetailModel;
import com.efuture.omdmain.model.DecomposeRecipeModel;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.service.DecomposeRecipeService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.FileProcessorUtils;

/**
 * 分解配方单Service
 *
 */
public class DecomposeRecipeServiceImpl extends JDBCCompomentServiceImpl<DecomposeRecipeModel> implements DecomposeRecipeService {

	public DecomposeRecipeServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	@Autowired
	private GoodsServiceImpl goodsService;
	@Autowired
	private DecomposeRecipeDetailServiceImpl decomposeRecipeDetailServiceImpl;
	
	// 查询-分解配方单
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);

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

		List decomposeRecipeList = template.getSqlSessionTemplate().selectList("beanmapper.DecomposeRecipeModelMapper.getDecomposeRecipeList",
				paramsObject);
		long total_results = 0;
		if (decomposeRecipeList != null && decomposeRecipeList.size() > 0) {
			total_results = template.getSqlSessionTemplate().selectOne("beanmapper.DecomposeRecipeModelMapper.countDecomposeRecipeList",
					paramsObject);
		}
		JSONObject result = new JSONObject();
		result.put("decomposeRecipe", decomposeRecipeList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
	// 根据商品编码查分解配方单明细-by
	public ServiceResponse getDRDetailByGoodsCode1(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("goodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "goodsCode"));
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
		}

		ServiceResponse dRQuery = this.search(session, paramsObject);// 分解配方单
		JSONObject dRData = (JSONObject) dRQuery.getData();
		JSONArray dRList = dRData.getJSONArray(this.getCollectionName());
//		if (dRList.size() == 0) {
//			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "选中的分解配方单可能被删除，请刷新页面再试");
//		}
		
		if(dRList.size() > 0){
			JSONObject dRModel = (JSONObject) dRList.get(0);
			JSONObject paramMap = new JSONObject();
			paramMap.put("drid", dRModel.getLong("drid"));          // 分解配方单ID
			ServiceResponse dRDQuery = decomposeRecipeDetailServiceImpl.onQuery(session, paramMap);
			
			JSONObject dRDData = (JSONObject) dRDQuery.getData();
			JSONArray dRDList = dRDData.getJSONArray(decomposeRecipeDetailServiceImpl.getCollectionName());
			if (dRDList.size() > 0) {
				int size = dRDList.size();
				for (int i = 0; i < size; i++) {
					JSONObject json1 = (JSONObject) dRDList.get(i);
					Float recipeRate1 = Float.parseFloat(json1.get("recipeRate").toString()); // 配方比
					DecimalFormat decimalFormat = new DecimalFormat("##.##");
					String p = decimalFormat.format(100 * recipeRate1);
					json1.put("recipeRate", p);
//					json1.put("recipeRate", 100*recipeRate1); 
				}
			}
//			long total_results = dRDList.size();
			long total_results = dRDData.getLong("total_results"); // 总数
			JSONObject decomposeRecipeDetailList = new JSONObject();
			decomposeRecipeDetailList.put(decomposeRecipeDetailServiceImpl.getCollectionName(), dRDList);
			decomposeRecipeDetailList.put("dRD_total_results", total_results);
			
			dRData.put(decomposeRecipeDetailServiceImpl.getCollectionName(), decomposeRecipeDetailList); // 分解配方单明细
		}
		
		return dRQuery;
//		return dRDQuery;
	}
	
	// 商品编码查分解配方单明细
	public ServiceResponse getDRDetailByGoodsCode(ServiceSession session, JSONObject paramsObject) throws Exception {

		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("goodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "goodsCode"));
		}
		if (!paramsObject.containsKey("erpCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
		}

		ServiceResponse dRQuery = this.search(session, paramsObject);// 分解配方单
		if (!ResponseCode.SUCCESS.equals(dRQuery.getReturncode())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "查询分解配方单失败" + dRQuery.getData());
		}
		JSONObject dRData = (JSONObject) dRQuery.getData();
		JSONArray dRList = dRData.getJSONArray(this.getCollectionName());
		
		JSONObject result = new JSONObject();
		if(dRList.size() > 0){
			
			JSONObject dRModel = (JSONObject) dRList.get(0);
			result.put(this.getCollectionName(), dRModel);
			
			Long entId = session.getEnt_id();
			Long drid = dRModel.getLong("drid");  // 分解配方单ID
			Criteria criteria1 = Criteria.where("entId").is(entId).and("drid").is(drid);
			Query query1 = new Query(criteria1);
			List<DecomposeRecipeDetailModel> decomposeRecipeDetailList1 = this.getTemplate().select(query1, DecomposeRecipeDetailModel.class, "decomposeRecipeDetail");
			
			if (decomposeRecipeDetailList1.size() > 0) {
				int size = decomposeRecipeDetailList1.size();
				for (int i = 0; i < size; i++) {
					DecomposeRecipeDetailModel json1 = decomposeRecipeDetailList1.get(i);
					Float recipeRate1 = json1.getRecipeRate(); // 配方比
					DecimalFormat decimalFormat = new DecimalFormat("##.##");
					String p = decimalFormat.format(100 * recipeRate1);
					json1.setRecipeRate(Float.parseFloat(p));
				}
			}
			
			long total_results = decomposeRecipeDetailList1.size(); // 总数
			result.put(decomposeRecipeDetailServiceImpl.getCollectionName(), decomposeRecipeDetailList1);
			result.put("detail_total", total_results);
		}else{
			result.put(this.getCollectionName(), "");
		}
		
		return ServiceResponse.buildSuccess(result);
	}
	
	// 新增--保存/修改--保存
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject) throws Exception {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (StringUtils.isEmpty(paramsObject)) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		}
		if (!paramsObject.containsKey("goodsCode")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "goodsCode"));
		}
//		if (!paramsObject.containsKey("erpCode")) {
//			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "erpCode"));
//		}
		if (!paramsObject.containsKey("decomposeRecipeDetail")) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,String.format("请求参数必须包含参数[%1$s]", "decomposeRecipeDetail"));
		}
		
		ServiceResponse dRQuery = this.onQuery(session, paramsObject);
		JSONObject dRData = (JSONObject) dRQuery.getData();
		JSONArray dRList = dRData.getJSONArray(this.getCollectionName());
		
		Map<String,Object> returnMap = new HashMap<>();// 记录返回信息
		
		ServiceResponse result1 = null;
		String drid = null;
		String goodsCode = null; // 分解配方单-商品编码
		if (dRList.size() == 0) { // 新增
			
			ServiceResponse goodsQuery = goodsService.onQuery(session, paramsObject);
			JSONObject goodsData = (JSONObject) goodsQuery.getData();
			JSONArray goodsList = goodsData.getJSONArray(goodsService.getCollectionName());
			if(goodsList.size() == 0){
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "输入编码有误，该商品不存在配方！");
			}
			JSONObject goods = (JSONObject) goodsList.get(0);
			
			JSONObject paramMap = new JSONObject();
			paramMap.put("erpCode", goods.getString("erpCode"));         // 经营公司编码
			paramMap.put("sgid", goods.getLong("sgid"));                 // 商品ID
			paramMap.put("goodsCode", goods.getString("goodsCode"));     // 商品编码
			paramMap.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));   //  最后修改时间
			paramMap.put("modifier", session.getUser_name());        // 最后修改人
			result1 = this.onInsert(session, paramMap);
			
			JSONObject dR = (JSONObject) result1.getData();
			drid = String.valueOf(dR.get("drid"));// 分解配方单ID
			goodsCode = String.valueOf(dR.get("goodsCode")); // 分解配方单-商品编码
			
		} else { // 有配方，新增明细
			
			JSONObject dRlist = (JSONObject) dRList.get(0);
			drid = dRlist.get("drid").toString(); // 分解配方单ID
			goodsCode = dRlist.get("goodsCode").toString(); // 分解配方单-商品编码
			
			JSONObject updateParam = new JSONObject();
			updateParam.put("drid", drid);    
			updateParam.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));   //  最后修改时间
			updateParam.put("modifier", session.getUser_name());        // 最后修改人
			result1 = this.onUpdate(session, updateParam);		
		}
		
		if (!ResponseCode.SUCCESS.equals(result1.getReturncode())) {
			throw new Exception(result1.getData().toString());
		}
		returnMap.put(this.getCollectionName(), result1.getData());
		
		// 新增分解配方单明细
		JSONArray dRDList = paramsObject.getJSONArray("decomposeRecipeDetail");
		
		if (dRDList.size() > 0) {
			
			Float tempRecipeRate = 0.0f;
			
			JSONObject insertDRD = new JSONObject();
			List<JSONObject> dRDParmList = new ArrayList<>();
			
			int temp1 = dRDList.size();
			for (int j = 0; j < temp1; j++) {
				JSONObject json2 = (JSONObject) dRDList.get(j);
				
				String MXgoodsCode = json2.getString("goodsCode");
				if (goodsCode.equals(MXgoodsCode)) {
					return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "明细中商品编码不能和配方中商品编码重复！");
				}
				
				json2.put("drid", drid);
				if (!StringUtils.isEmpty(json2.get("recipeRate"))) {
					Float recipeRate2 = Float.parseFloat(json2.get("recipeRate").toString()); // 配方比
					// 计算当前分解配方单明细的配方比总和
					BigDecimal a1 = new BigDecimal(Float.toString(tempRecipeRate));
					BigDecimal a2 = new BigDecimal(Float.toString(recipeRate2));
					tempRecipeRate =  a1.add(a2).floatValue();// 解决Float运算精度问题
//					tempRecipeRate = recipeRate2 + tempRecipeRate ;
					json2.put("recipeRate", recipeRate2/100);
				}
				dRDParmList.add(json2);
			}
			
//			if (tempRecipeRate < 100) {
//				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "当前分解配方单明细的配方比总和不够100%，请填完整！");
//			}
			if (tempRecipeRate > 100) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "当前分解配方单明细的配方比总和已超过100%，不能再添加商品");
			}
			
			// 删除数据库的旧明细
			FMybatisTemplate template = this.getTemplate();
	        template.onSetContext(session);
	        JSONObject oldParam = new JSONObject();
	        oldParam.put("drid", drid);
	        template.getSqlSessionTemplate().delete("beanmapper.DecomposeRecipeDetailModelMapper.deleteByDrid", oldParam);
		
	        // 新增新的明细
			insertDRD.put(decomposeRecipeDetailServiceImpl.getCollectionName(), dRDParmList);
			ServiceResponse result2 = decomposeRecipeDetailServiceImpl.onInsert(session, insertDRD);
			if (!ResponseCode.SUCCESS.equals(result2.getReturncode())) {
				throw new Exception(result2.getData().toString());
			}
			returnMap.put(decomposeRecipeDetailServiceImpl.getCollectionName(),result2.getData());
		}
		
		return ServiceResponse.buildSuccess(returnMap);	
	}
	
	// 批量删除
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse deleteAll(ServiceSession session, JSONObject paramsObject) {
		
		if (session == null) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if (!paramsObject.containsKey("drids")) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "分解配方单ID不能为空");
		}
		
		JSONArray dRList = paramsObject.getJSONArray("drids");
		if (dRList.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "请勾选要删除的配方.");
		}
		
		FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        long rows = template.getSqlSessionTemplate().delete("beanmapper.DecomposeRecipeModelMapper.decomposeRecipeDelete", paramsObject);
        long rows2 = template.getSqlSessionTemplate().delete("beanmapper.DecomposeRecipeDetailModelMapper.deleteAll", paramsObject);
        
		return ServiceResponse.buildSuccess("删除成功!, 共删除分解配方单" + rows + "条, 同时删除分解配方明细" + rows2 + "条");
	}
	
	//商品查询中心-分解配方
	public ServiceResponse searchByGoodsCode(ServiceSession session, JSONObject paramsObject) {
	  if (session == null) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
      }
	  if(!paramsObject.containsKey("sgid")) {
	    return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "参数sgid不能为空.");
	  }
	  if(!paramsObject.containsKey("erpCode")) {
        return ServiceResponse.buildFailure(session, ResponseCode.Exception.SPECDATA_IS_EMPTY, "参数erpCode不能为空.");
      }
	  
	  FMybatisTemplate template = this.getTemplate();
      template.onSetContext(session);
      paramsObject.put("entId", session.getEnt_id());
      DefaultParametersUtils.removeEmptyParams(paramsObject);
      List<Map<String, Object>> decomposeInfo = template.getSqlSessionTemplate().selectList("beanmapper.DecomposeRecipeModelMapper.selectDetailInfo", paramsObject);
	  
      List<Long> sgids = new ArrayList<>();
      for(Map<String, Object> map: decomposeInfo) {
        if(map.get("sgid") != null)
          sgids.add(Long.parseLong(String.valueOf(map.get("sgid").toString())));
      }
      
      List<GoodsModel> goodsModel = new ArrayList<>();
      if(CollectionUtils.isNotEmpty(sgids)) {
        paramsObject.put("sgids", sgids);
        goodsModel = template.getSqlSessionTemplate().selectList("beanmapper.GoodsModelMapper.selectInSGID", paramsObject);
      }
      for(Map<String, Object> map: decomposeInfo) {
        for(GoodsModel g: goodsModel) {
          if(map.get("sgid") != null && Long.parseLong(map.get("sgid").toString()) == g.getSgid().longValue()) {
            map.put("goodsNameM", g.getGoodsName());
            map.put("goodsCodeM", g.getGoodsCode());
            map.put("measureUnit", g.getMeasureUnit());
            break;
          }
        }
      }
      
      JSONObject result = new JSONObject();
      result.put(this.getCollectionName(), decomposeInfo);
      result.put("total_results", decomposeInfo.size());
      return ServiceResponse.buildSuccess(result);
	}
	
	
	@Override
	public ServiceResponse searchAllDetail(ServiceSession session, JSONObject paramsObject) {
		FMybatisTemplate template = this.getTemplate();
  		List<DecomposeRecipeDetailModel> decomposeRecipeDetailList = null;
  		ServiceResponse response = this.search(session, paramsObject);
  		if(response.getReturncode() != null && response.getReturncode().equals("0")){
  			JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
  			List<Map> decomposeRecipeList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), Map.class);
  			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
  			for(Map<String,Object> drMap : decomposeRecipeList){
  				Map<String, Object> decomposeRecipeMap = new HashMap<String,Object>();
  				decomposeRecipeMap.put("goodsCode", drMap.get("goodsCode"));
  				decomposeRecipeMap.put("goodsName", drMap.get("goodsName"));
  				decomposeRecipeMap.put("barNo", drMap.get("barNo"));
  				decomposeRecipeMap.put("categoryCode", drMap.get("categoryCode"));
  				decomposeRecipeMap.put("categoryName", drMap.get("categoryName"));
  				decomposeRecipeMap.put("modifier", drMap.get("modifier"));
  				decomposeRecipeMap.put("updateDate", drMap.get("updateDate"));
  				decomposeRecipeMap.put("erpCode", drMap.get("erpCode"));
  				decomposeRecipeMap.put("entId", drMap.get("entId"));
  				decomposeRecipeMap.put("sgid", drMap.get("sgid"));
  				decomposeRecipeMap.put("drid", drMap.get("drid"));
  				resultList.add(decomposeRecipeMap);
  				
  				Criteria criteria = Criteria.where("drid").is(drMap.get("drid"));
  				Query query = new Query(criteria);
  				decomposeRecipeDetailList = template.select(query, DecomposeRecipeDetailModel.class, "decomposerecipedetail");
  				
  				for(DecomposeRecipeDetailModel drd: decomposeRecipeDetailList){
  					decomposeRecipeMap = new HashMap<String,Object>();
  					decomposeRecipeMap.put("dGoodsCode", drd.getGoodsCode());
  					decomposeRecipeMap.put("dGoodsName", drd.getGoodsName());
  					decomposeRecipeMap.put("dCategoryCode", drd.getCategoryCode());
  					decomposeRecipeMap.put("dCategoryName", drd.getCategoryName());
  					decomposeRecipeMap.put("dBarCode", drd.getBarNo());
  					decomposeRecipeMap.put("dPurPriceAmount", drd.getPurPriceAmount());
  					decomposeRecipeMap.put("dGoodsSpec", drd.getGoodsSpec());
  					decomposeRecipeMap.put("dRecipeRate", drd.getRecipeRate());
		    		resultList.add(decomposeRecipeMap);
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
    public List<Map<String, Object>> onBeforeImportData(String params, MultipartFile file) throws Exception{
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> dataMap = new HashMap<String,Object>(); 
		JSONObject jsonparam = JSONObject.parseObject(params);
		String type = jsonparam.getString("type");
		if (type.equals("excel")) {
			if (file == null) {
				throw new Exception("缺少上传文件");
			}
			dataMap.put("decomposeRecipe", FileProcessorUtils.parseFile(jsonparam, file.getInputStream()));
			jsonparam.put("sheetIndex", 1);
			jsonparam.put("cols", "entId,goodsCode,dGoodsCode,recipeRate");
			dataMap.put("decomposeRecipeDetail", FileProcessorUtils.parseFile(jsonparam, file.getInputStream()));
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
		  try{
			  dataList = onBeforeImportData(params, file);
		  }catch(Exception e){
			  response = new ServiceResponse();
			  response.setReturncode(ResponseCode.EXCEPTION);
			  response.setData(e.getMessage());
			  return response;
		  }
		  Criteria criteria = null;
		  Query query = null;
		  DecomposeRecipeModel dr = null;
		  DecomposeRecipeDetailModel drd = null;
		  GoodsModel goods = null;
		  if(!dataList.isEmpty()){
			  Map<String, Object> dataMap = dataList.get(0);
			  List<Map<String,Object>> drMapList = (List<Map<String, Object>>) dataMap.get("decomposeRecipe");
			  List<Map<String,Object>> drDetailMapList = (List<Map<String, Object>>) dataMap.get("decomposeRecipeDetail");
			  JSONObject paramsObject = null;
			  Map<String,Long> dridMap = new HashMap<String,Long>();
			  Map<String,Object> paramMap = new HashMap<String,Object>();
			  for(Map<String,Object> drMap : drMapList){
				  paramsObject = JSONObject.parseObject(JSON.toJSONString(drMap));
				  paramMap.put("entId", paramsObject.get("entId"));
				  paramMap.put("goodsCode", paramsObject.get("goodsCode"));
				  goods = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.getGoodsByGoodsCode", paramMap);
				  if(goods == null){
					  throw new Exception("商品编码"+paramsObject.get("goodsCode")+"不存在");
				  }
				  
				  criteria = Criteria.where("entId").is(paramsObject.get("entId"))
							.and("goodsCode").is(paramsObject.get("goodsCode"))
							.and("erpCode").is(paramsObject.get("erpCode"));
				  query = new Query(criteria);
				  dr = template.selectOne(query, DecomposeRecipeModel.class, "decomposeRecipe");
			      if(dr == null){
			    	  paramsObject.put("sgid", goods.getSgid());
			    	  paramsObject.put("creator", session.getUser_name());
			    	  paramsObject.put("createDate", new Date());
			    	  this.onInsert(session, paramsObject);
			      }else{
			    	  paramsObject.put("sgid", goods.getSgid());
			    	  paramsObject.put("modifier", session.getUser_name());
			    	  paramsObject.put("updateDate", new Date());
			    	  paramsObject.put(this.getKeyfieldName(), dr.getDrid());
			    	  this.onUpdate(session, paramsObject);
			      }
			      dridMap.put(paramsObject.getString("goodsCode"), paramsObject.getLong("drid"));
			  }
			  query = new Query(Criteria.where("drid").in(dridMap.values()));
			  template.delete(query, DecomposeRecipeDetailModel.class, "decomposerecipedetail");
			  
			  Map<String,Double> recipeRateMap = new HashMap<String,Double>();
			  for(Map<String, Object> prdMap : drDetailMapList){
				  paramsObject = JSONObject.parseObject(JSON.toJSONString(prdMap));
				  String goodsCode = paramsObject.getString("goodsCode");
				  Long drid = dridMap.get(goodsCode);
				  if(drid == null){
					  throw new Exception("分解配方单主表商品"+paramsObject.get("goodsCode")+"不存在");
				  }
				  
				  paramMap.clear();
				  paramMap.put("entId", paramsObject.get("entId"));
				  paramMap.put("goodsCode", paramsObject.get("dGoodsCode"));
				  goods = template.getSqlSessionTemplate().selectOne("beanmapper.GoodsModelMapper.getGoodsByGoodsCode", paramMap);
				  if(goods == null){
					  throw new Exception("商品编码"+paramsObject.get("goodsCode")+"不存在");
				  }
					
				  paramsObject.put("drid", drid);
				  paramsObject.put("goodsCode", paramsObject.get("dGoodsCode"));
				  paramsObject.put("barNo", goods.getBarNo());
				  paramsObject.put("goodsName", goods.getGoodsName());
				  paramsObject.put("categoryCode", goods.getCategoryCode());
				  paramsObject.put("categoryName", goods.getCategoryName());
				  paramsObject.put("goodsSpec", goods.getMeasureUnit());
				  paramsObject.put("purPriceAmount", goods.getPrimeCost());
				  decomposeRecipeDetailServiceImpl.onInsert(session, paramsObject);
				  if(recipeRateMap.containsKey(goodsCode)){
					  recipeRateMap.put(goodsCode, recipeRateMap.get(goodsCode) + paramsObject.getDouble("recipeRate"));
				  }else{
					  recipeRateMap.put(goodsCode, paramsObject.getDouble("recipeRate"));  
				  }
			  }
			  for(Entry<String,Double> entry : recipeRateMap.entrySet()){
				  if(entry.getValue() != 1){
					  throw new Exception("分解配方单主表商品"+entry.getKey()+"配方比不为1");
				  }
			  }
		  }
		  return ServiceResponse.buildSuccess("");
	}
}
