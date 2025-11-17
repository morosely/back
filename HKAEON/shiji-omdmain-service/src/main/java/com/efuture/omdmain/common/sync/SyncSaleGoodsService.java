package com.efuture.omdmain.common.sync;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.ErrorLogService;
import com.efuture.omdmain.component.SyncDataTimeService;
import com.efuture.omdmain.model.ErrorLogModel;
import com.efuture.omdmain.model.out.SyncDataTimeModel;
import com.efuture.omdmain.model.out.SyncSaleGoodsModel;
import com.product.component.CommonServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

public class SyncSaleGoodsService extends CommonServiceImpl<SyncSaleGoodsModel,SyncSaleGoodsService>{

	public SyncSaleGoodsService(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Autowired
	private ErrorLogService errorLogService;
	@Autowired
	private SyncDataTimeService syncDateTimeService;
	
	private static final String SynGoodsAndRef = "GoodsAndRef";
	private static final String SynMoreBarCode = "MoreBarCode";
	private static final String SynInit = "SynInit";
	private static final String SynSaleGoodsBatchErrorCode = "10000";//批量出错编码
	private static final String SynSaleGoodsOneInertErrorCode = "20001";//单个插入出错编码
	private static final String SynSaleGoodsOneUpdateErrorCode = "20002";//单个更新出错编码
	private static final Integer page_size = 5000; //每次同步数量
	
	
	//方案一 通过1.goods goodsshopref 同步数据到salegoods 2.goodsmorebarcode和goodsshopref,goods 同步数据到salegoods
	public ServiceResponse syncSaleGoods(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse response = null;
		try {
			String syncSql = "beanmapper.out.SyncSaleGoodsModelMapper.searchSaveOrUpdateGoodsShopRef";
			String countSyncSql = "beanmapper.out.SyncSaleGoodsModelMapper.countSaveOrUpdateGoodsShopRef";
			String modelName = SynMoreBarCode;
			response = doSyncSaleGoods(session,paramsObject,syncSql,countSyncSql,modelName);
		} catch (Exception e) {
			JSONObject errorParams = new JSONObject();
			errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
			errorParams.put("operateType", "I");
			errorParams.put("tableName", "salegoods");
			errorParams.put("message", "GoodsMoreBarCode同步SaleGoods有异常");
			errorParams.put("stack", e);
			errorLogService.wrapInsert(session, errorParams);
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "同步SaleGoods有异常");
		}
		return response;
	}
	
	//方案二 通过goods goodsshopref goodsmorebarcode 同步数据到salegoods (主从条码全部在多条码表，商品基本表没条码)
	public ServiceResponse syncSaleGoodsAeon(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse response = null;
		try {
			//商品基础和经营配置同步可售商品
			String countSyncSql = "beanmapper.out.SyncSaleGoodsModelMapper.countGoodsShopRefMoreBarNoAeon";
			String syncSql= "beanmapper.out.SyncSaleGoodsModelMapper.searchGoodsShopRefMoreBarNoAeon";
			String modelName = SynGoodsAndRef;
			response = doSyncSaleGoodsAeon(session,paramsObject,syncSql,countSyncSql,modelName);
		} catch (Exception e) {
			JSONObject errorParams = new JSONObject();
			errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
			errorParams.put("operateType", "I");
			errorParams.put("tableName", "salegoods");
			errorParams.put("message", "Goods、GoodsShopRef、GoodsMoreBarCode同步SaleGoods有异常");
			errorParams.put("stack", e);
			errorLogService.wrapInsert(session, errorParams);
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "同步SaleGoods有异常");
		}
		return response;
	}
	
	//方案三（首次新增门店直接插入） 通过goods goodsshopref goodsmorebarcode 同步数据到salegoods (主从条码全部在多条码表，商品基本表没条码)
	public ServiceResponse syncSaleGoodsAeonAdd(ServiceSession session, JSONObject paramsObject) throws Exception{
		ServiceResponse response = null;
		//1. 查询新增新门店的所有商品首次插入的数据
		List<SyncSaleGoodsModel> allShopRef = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.out.SyncSaleGoodsModelMapper.searAllShopRef", paramsObject);
		List<SyncSaleGoodsModel> allSaleShop = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.out.SyncSaleGoodsModelMapper.searchAllSaleShop", paramsObject);
		paramsObject.put("fields",null);
		if(allShopRef!=null && allSaleShop!=null){
			allShopRef.removeAll(allSaleShop);
			if(!allShopRef.isEmpty()){
				List<String> values = new ArrayList<>();
				for (SyncSaleGoodsModel model : allShopRef) {
					StringBuffer paramValue = new StringBuffer();
					paramValue.append("('").append(model.getShopCode()).append("',").append(model.getEntId()).append(",").append("'").append(model.getErpCode()).append("')");
					values.add(paramValue.toString());
				}
				paramsObject.put("fields",values);
			}
		}
		
		/*List<Map> fields = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.out.SyncSaleGoodsModelMapper.searchNewShopGoods", paramsObject);
		if(fields!=null && !fields.isEmpty()){
			List<String> values = new ArrayList<>();
			for (Map field : fields) {
				String shopCode = (String) field.get("shopCode");
				String erpCode = (String) field.get("erpCode");
				Long entId = (Long) field.get("entId");
				StringBuffer paramValue = new StringBuffer();
				paramValue.append("('").append(shopCode).append("',").append(entId).append(",").append("'").append(erpCode).append("')");
				values.add(paramValue.toString());
			}
			paramsObject.put("fields",values);
		}else{
			paramsObject.put("fields",fields);
		}*/
		try {
			//商品基础和经营配置同步可售商品
			String countSyncSql = "beanmapper.out.SyncSaleGoodsModelMapper.countGoodsShopRefMoreBarNoAeonAdd";
			String syncSql= "beanmapper.out.SyncSaleGoodsModelMapper.searchGoodsShopRefMoreBarNoAeonAdd";
			String modelName = SynInit;
			response = doSyncSaleGoodsAeon(session,paramsObject,syncSql,countSyncSql,modelName);
		} catch (Exception e) {
			JSONObject errorParams = new JSONObject();
			errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
			errorParams.put("operateType", "I");
			errorParams.put("tableName", "salegoods");
			errorParams.put("message", "Goods、GoodsShopRef、GoodsMoreBarCode同步SaleGoods有异常");
			errorParams.put("stack", e);
			errorLogService.wrapInsert(session, errorParams);
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "同步SaleGoods有异常");
		}
		return response;
	}
	
	/************************************************************ 同步方案一 分割线*******************************************************/
	//方案一：Goods的barNo是主条码，GoodsMoreBarCode的barNo从条码
	public ServiceResponse doSyncSaleGoods(ServiceSession session, JSONObject paramsObject,String syncSql,String countSyncSql,String modelName) throws Exception{
		int insertCount = 0;//统计新增数据
		int updateCount = 0;//统计修改数据
		int deleteCount = 0;//删除新增数据
		Date batchTime = new Date();//批次时间
		logger.info("     ===================================>>>>>  sync salegoods start ===================================>>>>> " +batchTime);
		//1.设置批量处理数
		//Integer page_size = 2000;
		
		//2.查询上次同步时间
		Criteria criteria = Criteria.where("finishFlag").is(true).and("modelName").is(modelName);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("max(startTime) startTime");
		SyncDataTimeModel syncDataTimeModel = this.getTemplate().selectOne(query, SyncDataTimeModel.class, "syncDataTime");
		
		//3.增量的数据（理论上startTime为空第一次插入，不为空为增量数据）
	    if(syncDataTimeModel != null) paramsObject.put("startTime", syncDataTimeModel.getStartTime());
		
		//4.插入同步时间日志表
		JSONObject syncParam = new JSONObject();
		syncParam.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(batchTime));
		syncParam.put("finishFlag",0);
		syncParam.put("modelName",modelName);
		ServiceResponse reponse = syncDateTimeService.onInsert(session, syncParam);
	
		//5.新增和更新
		Long total_results = this.getTemplate().getSqlSessionTemplate().selectOne(countSyncSql, paramsObject);
		if(!StringUtils.isEmpty(total_results) && total_results > 0){
			List<SyncSaleGoodsModel> insertList = new ArrayList<SyncSaleGoodsModel>();//新增数据
			List<SyncSaleGoodsModel> updateList = new ArrayList<SyncSaleGoodsModel>();//更新数据
			Integer total_page = (total_results.intValue() + page_size - 1) / page_size;
			for (int page_no = 0; page_no < total_page; page_no ++) {	
				paramsObject.put("page_no", page_no * page_size);
				paramsObject.put("page_size",page_size);
				List<SyncSaleGoodsModel> mockSaleGoodsList = this.getTemplate().getSqlSessionTemplate().selectList(syncSql, paramsObject);
				for (SyncSaleGoodsModel syncSaleGoodsModel : mockSaleGoodsList) {
					if(syncSaleGoodsModel.getSsgid() == null){
						syncSaleGoodsModel.setSsgid(UniqueID.getUniqueID(true));
						syncSaleGoodsModel.setCreateDate(batchTime);
						syncSaleGoodsModel.setUpdateDate(batchTime);//处理后插入条码表的脏数据：数据清洗功能(有多个主条码的商品：条码不同)：此字段值不能为空
						syncSaleGoodsModel.setMainBarcodeFlag(true);//是否设置主条码
						//设置可售商品状态（商品状态，经营配置状态）二者其中一个为禁用，可售商品就为禁用
						if(!"0".equals(syncSaleGoodsModel.getStatus4Goods()) && !"0".equals(syncSaleGoodsModel.getStatus4Ref())){
							insertList.add(syncSaleGoodsModel);
						}
					}else{
						syncSaleGoodsModel.setUpdateDate(batchTime);
						syncSaleGoodsModel.setMainBarcodeFlag(true);//是否设置主条码
						//设置可售商品状态（商品状态，经营配置状态）二者其中一个为禁用，可售商品就为禁用
						if("0".equals(syncSaleGoodsModel.getStatus4Goods()) || "0".equals(syncSaleGoodsModel.getStatus4Ref())){
							syncSaleGoodsModel.setGoodsStatus(Short.valueOf("0"));
						}
						updateList.add(syncSaleGoodsModel);
					}
				}
				if(!insertList.isEmpty()){
					insertBatchSaleGoods(session,insertList,batchTime);
					insertCount +=  insertList.size();
				}
				if(!updateList.isEmpty()) {
					updateBatchSaleGoods(session,updateList,batchTime);
					updateCount += updateList.size();
				}
				insertList.clear();
				updateList.clear();
			}
		}
		
		//主条码更新同时，需要更新从条码数据
		//从条码数据：箱码价格来源于单品需要重新计算
		countSyncSql = "beanmapper.out.SyncSaleGoodsModelMapper.countGoodsShopRefMoreBarNo";
		syncSql = "beanmapper.out.SyncSaleGoodsModelMapper.searchGoodsShopRefMoreBarNo";
		total_results = this.getTemplate().getSqlSessionTemplate().selectOne(countSyncSql, paramsObject);
		if(!StringUtils.isEmpty(total_results) && total_results > 0){
			List<SyncSaleGoodsModel> insertList = new ArrayList<SyncSaleGoodsModel>();//新增数据
			List<SyncSaleGoodsModel> updateList = new ArrayList<SyncSaleGoodsModel>();//更新数据
			Integer total_page = (total_results.intValue() + page_size - 1) / page_size;
			for (int page_no = 0; page_no < total_page; page_no ++) {
				paramsObject.put("page_no", page_no * page_size);
				paramsObject.put("page_size",page_size);
				List<SyncSaleGoodsModel> mockSaleGoodsList = this.getTemplate().getSqlSessionTemplate().selectList(syncSql, paramsObject);
				for (SyncSaleGoodsModel syncSaleGoodsModel : mockSaleGoodsList) {
					if(syncSaleGoodsModel.getSsgid() == null){
						syncSaleGoodsModel.setSsgid(UniqueID.getUniqueID(true));
						syncSaleGoodsModel.setCreateDate(batchTime);
						syncSaleGoodsModel.setUpdateDate(batchTime);//处理后插入条码表的脏数据：数据清洗功能(有多个主条码的商品：条码不同)：此字段值不能为空
						syncSaleGoodsModel.setMainBarcodeFlag(false);//设置条码
						//设置可售商品状态（商品状态，经营配置状态，多条码状态）三者其中一个为禁用，可售商品就为禁用
						if(!"0".equals(syncSaleGoodsModel.getStatus4Goods()) && !"0".equals(syncSaleGoodsModel.getStatus4Ref()) && !"0".equals(syncSaleGoodsModel.getStatus4More())){
							insertList.add(syncSaleGoodsModel);
						}
						insertList.add(syncSaleGoodsModel);
					}else{
						syncSaleGoodsModel.setUpdateDate(batchTime);
						syncSaleGoodsModel.setMainBarcodeFlag(false);//设置条码
						//设置可售商品状态（商品状态，经营配置状态，多条码状态）三者其中一个为禁用，可售商品就为禁用
						if("0".equals(syncSaleGoodsModel.getStatus4Goods()) || "0".equals(syncSaleGoodsModel.getStatus4Ref()) || "0".equals(syncSaleGoodsModel.getStatus4More())){
							syncSaleGoodsModel.setGoodsStatus(Short.valueOf("0"));
						}
						updateList.add(syncSaleGoodsModel);
					}
				}
				if(!insertList.isEmpty()) {
					insertBatchSaleGoods(session,insertList,batchTime);
					insertCount +=  insertList.size();
				}
				if(!updateList.isEmpty()){
					updateBatchSaleGoods(session,updateList,batchTime);
					updateCount += updateList.size();
				}
				insertList.clear();
				updateList.clear();
			}
		}
		
		//6.处理错误异常数据
		doErrorData(session,paramsObject,batchTime);
		
		//删除状态为0的可售商品
		paramsObject.clear();
		paramsObject.put("table","saleGoods");
		paramsObject.put("key","goodsStatus");
		paramsObject.put("values",Arrays.asList(0));
		deleteCount += this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
		
		String result = "insertSQL: "+insertCount +" . updateSQL: "+updateCount+" . deleteSQL: "+deleteCount;
		logger.info("     ===================================>>>>> sync salegoods end.  ===================================>>>>>  "+result);
		
		//7.更新同步时间日志表
		syncParam = (JSONObject)reponse.getData();//返回主键
		syncParam.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		syncParam.put("finishFlag",1);
		syncParam.put("returnMsg",result);
		reponse = syncDateTimeService.onUpdate(session, syncParam);
		return ServiceResponse.buildSuccess("");
	}
	
	/************************************************************ 同步方案二  分割线*******************************************************/
	
	//方案二：Goods表没有BarNo，GoodsMoreBarCode存有主条码，从条码
	public ServiceResponse doSyncSaleGoodsAeon(ServiceSession session, JSONObject paramsObject,String syncSql,String countSyncSql,String modelName) throws Exception{
		int insertCount = 0;//统计新增数据
		int updateCount = 0;//统计修改数据
		int deleteCount = 0;//删除新增数据
		Date batchTime = new Date();//批次时间
		logger.info("     ===================================>>>>>  sync salegoods start ===================================>>>>> " +batchTime+" paramsObject ====>"+paramsObject);
		//1.设置批量处理数
		//Integer page_size = 2000;
		/*if(paramsObject.getString("shopCode")!=null){
			String[] shopCodes =  paramsObject.getString("shopCode").split(",");
			List shopCode = Arrays.asList(shopCodes);
			paramsObject.put("shopCode",shopCode);
		}*/
		
		//2.查询上次同步时间
		Criteria criteria = Criteria.where("finishFlag").is(true).and("modelName").is(modelName);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("max(startTime) startTime");
		SyncDataTimeModel syncDataTimeModel = this.getTemplate().selectOne(query, SyncDataTimeModel.class, "syncDataTime");
		
		//3.增量的数据（理论上startTime为空第一次插入，不为空为增量数据）
	    if(syncDataTimeModel != null) paramsObject.put("startTime", syncDataTimeModel.getStartTime());
		
		//4.插入同步时间日志表
		JSONObject syncParam = new JSONObject();
		syncParam.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(batchTime));
		syncParam.put("finishFlag",0);
		syncParam.put("modelName",modelName);
		ServiceResponse reponse = syncDateTimeService.onInsert(session, syncParam);
	
		//5.新增和更新
		Long total_results = this.getTemplate().getSqlSessionTemplate().selectOne(countSyncSql, paramsObject);
		List<SyncSaleGoodsModel> insertList = new ArrayList<SyncSaleGoodsModel>();//新增数据
		List<SyncSaleGoodsModel> updateList = new ArrayList<SyncSaleGoodsModel>();//更新数据
		List<SyncSaleGoodsModel> deleteList = new ArrayList<SyncSaleGoodsModel>();//删除数据
		
		//5.1 处理增量的数据
		if(!StringUtils.isEmpty(total_results) && total_results > 0){
			Integer total_page = (total_results.intValue() + page_size - 1) / page_size;
			for (int page_no = 0; page_no < total_page; page_no ++) {	
				paramsObject.put("page_no", page_no * page_size);
				paramsObject.put("page_size",page_size);
				List<SyncSaleGoodsModel> mockSaleGoodsList = this.getTemplate().getSqlSessionTemplate().selectList(syncSql, paramsObject);
				for (SyncSaleGoodsModel syncSaleGoodsModel : mockSaleGoodsList) {
					if(syncSaleGoodsModel.getSsgid() == null){
						syncSaleGoodsModel.setSsgid(UniqueID.getUniqueID(true));
						syncSaleGoodsModel.setCreateDate(batchTime);
						syncSaleGoodsModel.setUpdateDate(batchTime);//处理后插入条码表的脏数据：数据清洗功能(有多个主条码的商品：条码不同)：此字段值不能为空
						//设置主条码
						syncSaleGoodsModel.setMainBarcodeFlag(syncSaleGoodsModel.getBarCodeType() == 1);
						//设置可售商品状态（商品状态，经营配置状态，多条码状态）三者其中一个为禁用，可售商品就为禁用
						if(!"0".equals(syncSaleGoodsModel.getStatus4Goods()) && !"0".equals(syncSaleGoodsModel.getStatus4Ref()) && !"0".equals(syncSaleGoodsModel.getStatus4More())){
							insertList.add(syncSaleGoodsModel);
						}
					}else{
						//设置主条码
						syncSaleGoodsModel.setMainBarcodeFlag(syncSaleGoodsModel.getBarCodeType() == 1);
						//设置可售商品状态（商品状态，经营配置状态，多条码状态）三者其中一个为禁用，可售商品就为禁用
						if("0".equals(syncSaleGoodsModel.getStatus4Goods()) || "0".equals(syncSaleGoodsModel.getStatus4Ref()) || "0".equals(syncSaleGoodsModel.getStatus4More())){
							syncSaleGoodsModel.setGoodsStatus(Short.valueOf("0"));
						}
						syncSaleGoodsModel.setUpdateDate(batchTime);
						updateList.add(syncSaleGoodsModel);
					}
				}
				if(!insertList.isEmpty()) {
					insertBatchSaleGoods(session,insertList,batchTime);
					insertCount +=  insertList.size();
				}
				if(!updateList.isEmpty()) {
					updateBatchSaleGoods(session,updateList,batchTime);
					updateCount += updateList.size();
				}
				insertList.clear();
				updateList.clear();
			}
		}

		//6.处理错误异常数据
		doErrorData(session,paramsObject,batchTime);
		
		//5.2 处理后插入条码表的脏数据：数据清洗功能(有多个主条码的商品：条码不同)
		updateList.clear();
		deleteList.clear();
		List<SyncSaleGoodsModel> moreMainBarNoList = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.out.SyncSaleGoodsModelMapper.searchDirtyDataBarNoAeon", paramsObject);
		if(!moreMainBarNoList.isEmpty()){
			//1.map根据模板属性进行归类统计
			Map<String,List<SyncSaleGoodsModel>> mainBarNoMap = new HashMap<String,List<SyncSaleGoodsModel>>();
			
			//2.解析数据，归类统计数据
			for (SyncSaleGoodsModel mainBarNoSaleGoods : moreMainBarNoList) {
				StringBuffer keyBuf = new StringBuffer();
				//商品编码，零售商ID，经营公司，柜组，门店编码组成的Key
				String key = keyBuf.append(mainBarNoSaleGoods.getGoodsCode()).append(mainBarNoSaleGoods.getEntId()).
						append(mainBarNoSaleGoods.getErpCode()).append(mainBarNoSaleGoods.getOrgCode()).append(mainBarNoSaleGoods.getShopCode()).toString();
				List<SyncSaleGoodsModel> sameGoodsList = mainBarNoMap.containsKey(key) ? mainBarNoMap.get(key):new ArrayList<SyncSaleGoodsModel>();
				sameGoodsList.add(mainBarNoSaleGoods);
				mainBarNoMap.put(key,sameGoodsList);
			}
			
			//3. 从key中 处理主条码数据（SQL中根据GoodsCode，UpdateDate Desc已经排过序）需要保留最初数据的ID
			for (List<SyncSaleGoodsModel> list : mainBarNoMap.values()) {
				SyncSaleGoodsModel top = null;//最新的主条码商品数据，将数据赋值给最初的商品后需要删除
				SyncSaleGoodsModel old = null;//最初的主条码商品数据
				for(int i=0; i< list.size(); i++){
					if(i == 0 ){ 
						top = list.get(0);
					}else if(i == list.size() - 1 ){ 
						old = list.get(list.size() - 1);
					}else {
						deleteList.add(list.get(i));
					}
				}
				//ID变量交换
				Long ssgidTemp = top.getSsgid();
				top.setSsgid(old.getSsgid());
				old.setSsgid(ssgidTemp);
				
				updateList.add(top);
				deleteList.add(old);
			}
			//调用删除（先删除后更新，否则产生唯一索引报错）批量删除SaleGoods表（通过主键ID删除）
			paramsObject.clear();
			paramsObject.put("table","saleGoods");
			paramsObject.put("key","ssgid");
			List<Long> deleteSsgidList = deleteList.stream().map(SyncSaleGoodsModel::getSsgid).collect(Collectors.toList());
			paramsObject.put("values",deleteSsgidList);
			deleteCount += this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
			//调用更新
			updateBatchSaleGoods(session,updateList,batchTime);
			updateCount += updateList.size();
		}
		
		//5.3删除状态为0的可售商品
		paramsObject.clear();
		paramsObject.put("table","saleGoods");
		paramsObject.put("key","goodsStatus");
		paramsObject.put("values",Arrays.asList(0));
		deleteCount += this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
		
		
		String result = "insertSQL: "+insertCount +" . updateSQL: "+updateCount+" . deleteSQL: "+deleteCount;
		logger.info("     ===================================>>>>> sync salegoods end.  ===================================>>>>>  "+result);
		
		//7.更新同步时间日志表
		syncParam = (JSONObject)reponse.getData();//返回主键
		syncParam.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		syncParam.put("finishFlag",1);
		syncParam.put("returnMsg",result);
		reponse = syncDateTimeService.onUpdate(session, syncParam);
		return ServiceResponse.buildSuccess(result);
	}
	
	
	/************************************************************ 公共方法 分割线*******************************************************/
	
	//处理异常数据(读取错误日志表数据)
	public void doErrorData(ServiceSession session, JSONObject paramsObject, Date batchTime) throws ParseException{
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date batchTime = dateFormat.parse("2018-08-23 14:42:00");
		//1.查询错误数据
		Criteria criteria = Criteria.where("errorCode").is(SynSaleGoodsBatchErrorCode).and("createDate").is(batchTime).and("isProcessData").is(false);
		Query query = new Query(criteria);
		List<ErrorLogModel> list = this.getTemplate().select(query, ErrorLogModel.class, "errorLog");
		if(list!=null && !list.isEmpty()) {
			//解析批量数据，逐一插入
			for (ErrorLogModel errorLogModel : list) {
				if("I".equals(errorLogModel.getOperateType())){// 插入失败的批量数据
					//获取批量Json串集合数据
					String params = errorLogModel.getParams();
					if(!StringUtils.isEmpty(params)){
						JSONArray jsonArray = JSONArray.parseArray(params);
						for (Object object : jsonArray) {
							JSONObject saleGoodsJson =  (JSONObject)object;
							try{
								this.getTemplate().getSqlSessionTemplate().insert("beanmapper.out.SyncSaleGoodsModelMapper.insertOneSaleGoods",saleGoodsJson);
							}catch (Exception e) {
								JSONObject errorParams = new JSONObject();
								errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
								errorParams.put("params", saleGoodsJson.toString());
								errorParams.put("tableName", "salegoods");
								errorParams.put("operateType", "I");
								errorParams.put("stack", e);
								errorParams.put("message", "(单个数据)插入SaleGoods表异常");
								errorParams.put("createDate", batchTime);
								errorParams.put("errorCode", SynSaleGoodsOneInertErrorCode);
								errorParams.put("isProcessData",false);
								errorLogService.onInsert(session, errorParams);
								logger.error("(单个数据)插入异常  ===========================>>>>>>>>>> "+saleGoodsJson);
							}
						}
					}
				}else if("U".equals(errorLogModel.getOperateType())){// 更新失败的批量数据
					String params = errorLogModel.getParams();
					if(!StringUtils.isEmpty(params)){
						JSONArray jsonArray = JSONArray.parseArray(params);
						for (Object object : jsonArray) {
							JSONObject saleGoodsJson =  (JSONObject)object;
							try{
								this.getTemplate().getSqlSessionTemplate().update("beanmapper.out.SyncSaleGoodsModelMapper.updateOneSaleGoods",saleGoodsJson);
							}catch (Exception e) {
								JSONObject errorParams = new JSONObject();
								errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
								errorParams.put("params", saleGoodsJson.toString());
								errorParams.put("tableName", "salegoods");
								errorParams.put("operateType", "U");
								errorParams.put("stack", e);
								errorParams.put("message", "(单个数据)更新SaleGoods表异常");
								errorParams.put("createDate", batchTime);
								errorParams.put("errorCode", SynSaleGoodsOneUpdateErrorCode);
								errorParams.put("isProcessData",false);
								errorLogService.onInsert(session, errorParams);
								logger.error("(单个数据)更新异常  ===========================>>>>>>>>>> "+saleGoodsJson);
							}
						}
					}
				}
				//标记处理过出错批量数据
				paramsObject.clear();
				paramsObject.put("errorId", errorLogModel.getErrorId());
				paramsObject.put("isProcessData", true);
				errorLogService.onUpdate(session, paramsObject);
			}
		}
	}
	
	//批量插入SaleGoods表
	private void insertBatchSaleGoods(ServiceSession session, List<SyncSaleGoodsModel> mockSaleGoodsList,Date createDate) throws Exception{
		try{
			this.getTemplate().getSqlSessionTemplate().insert("beanmapper.out.SyncSaleGoodsModelMapper.batchInsert", mockSaleGoodsList);
		}catch (Exception e) {
			JSONObject errorParams = new JSONObject();
			errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
			String errorData = JSON.toJSONStringWithDateFormat(mockSaleGoodsList,"yyyy-MM-dd HH:mm:ss");
			errorParams.put("params", errorData);
			errorParams.put("tableName", "salegoods");
			errorParams.put("operateType", "I");
			errorParams.put("stack", e);
			errorParams.put("message", "插入SaleGoods表异常");
			errorParams.put("createDate", createDate);
			errorParams.put("errorCode", SynSaleGoodsBatchErrorCode);
			errorParams.put("isProcessData",false);
			errorLogService.onInsert(session, errorParams);
			logger.error("插入异常  ===========================>>>>>>>>>> "+errorData);
		}
	}
	
	//批量更新SaleGoods表
	private void updateBatchSaleGoods(ServiceSession session,List<SyncSaleGoodsModel> mockSaleGoodsList,Date createDate) throws Exception{
		try{
			this.getTemplate().getSqlSessionTemplate().update("beanmapper.out.SyncSaleGoodsModelMapper.batchUpdate", mockSaleGoodsList);
		}
		catch (Exception e) {
			JSONObject errorParams = new JSONObject();
			errorParams.put("className", "com.efuture.omdmain.common.sync.SyncSaleGoodsService");
			String errorData = JSON.toJSONStringWithDateFormat(mockSaleGoodsList,"yyyy-MM-dd HH:mm:ss");
			errorParams.put("params", errorData);
			errorParams.put("tableName", "salegoods");
			errorParams.put("operateType", "U");
			errorParams.put("stack", e);
			errorParams.put("message", "更新SaleGoods表异常");
			errorParams.put("createDate", createDate);
			errorParams.put("errorCode", SynSaleGoodsBatchErrorCode);
			errorParams.put("isProcessData",false);
			errorLogService.onInsert(session, errorParams);
			logger.error("更新异常  ===========================>>>>>>>>>> "+errorData);
		}
	}
 
}
