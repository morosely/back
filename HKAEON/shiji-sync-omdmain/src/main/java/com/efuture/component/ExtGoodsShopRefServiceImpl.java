package com.efuture.component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.common.CommonExtSyncService;
import com.efuture.common.SpringUtil;
import com.efuture.model.ExtGoodsShopRefModel;
import com.efuture.model.GoodsShopRefModel;
import com.product.component.DataCompareCallback;
import com.product.component.DataReader;
import com.product.component.DataWriter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.UniqueID;

public class ExtGoodsShopRefServiceImpl extends CommonExtSyncService<ExtGoodsShopRefModel,ExtGoodsShopRefServiceImpl> implements DataCompareCallback{
	public ExtGoodsShopRefServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
	
	private ErrorLogServiceImpl errorLogService;
	private final static String DIR = System.getProperty("user.dir");
	private final static String SEPARATOR = System.getProperty("file.separator");
	private final static Integer WRITER_MAX_SIZE = 1000;
	private final static String dbPath = DIR+SEPARATOR+"omdmain-db";
	private final static String dbPathNew = DIR+SEPARATOR+"omdmain-db"+SEPARATOR+"new";
	private final static String dbPathOld = DIR+SEPARATOR+"omdmain-db"+SEPARATOR+"old";
	private final static Integer PROCESS_DATA_MAX_COUNT = 5000;
	
	private Map<String,DataWriter> shopDataWriter = new HashMap<String,DataWriter>();
	private Map<String,List> cacheShopData = new HashMap<String,List>();
	private List<Map<String, Object>> insertAll;
	private List<Map<String, Object>> updateAll;
	
	@Transactional
	@Override
    public ServiceResponse receive(ServiceSession session, JSONObject param) throws Exception {
		/**
		 * 格式：
		 * 
		 * {"extgoodsshopref": [{...},...,{...}]}
		 * 
		 */
		long start = System.currentTimeMillis();
		errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
		
		// 获取本次列表结果集
		List<ExtGoodsShopRefModel> paramList = JSON.parseArray(JSON.toJSONString(param.get(tableName)),modelClass);
		if (paramList == null || paramList.isEmpty()) {
			return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
		}
		
		File pathNew = new File(dbPathNew);// 获取文件夹路径
		if (!pathNew.exists()) {// 判断文件夹是否创建，没有创建则创建新文件夹
			boolean isSuccess = pathNew.mkdirs();
			this.logger.info("开始创建文件夹路径{}。创建文件夹是否成功:{}",pathNew,isSuccess);
		}
		File pathOld = new File(dbPathOld);// 获取文件夹路径
		if (!pathOld.exists()) {// 判断文件夹是否创建，没有创建则创建新文件夹
			boolean isSuccess = pathOld.mkdirs();
			this.logger.info("开始创建文件夹路径{}。创建文件夹是否成功:{}",pathOld,isSuccess);
		}
		
		//数据根据门店进行分组
		Map<String,List<ExtGoodsShopRefModel>> shopCodeMap = paramList.stream().collect(Collectors.groupingBy(ExtGoodsShopRefModel::getShopCode));
		//---------- 根据Map的Key进行遍历生成门店数据start ----------
		for (String shopCode : shopCodeMap.keySet()) {			
			List dataList = new ArrayList();
			List<ExtGoodsShopRefModel> oneShop = shopCodeMap.get(shopCode);
			for (ExtGoodsShopRefModel model : oneShop) {
				JSONObject data = JSONObject.parseObject(JSON.toJSONString(model));
				dataList.add(data);
			}
			//指定路径
			String fileNewNameDat = dbPathNew+SEPARATOR+"goodsshopref-"+shopCode+".dat";
			String fileNewNameIdx = dbPathNew+SEPARATOR+"goodsshopref-"+shopCode+".idx";
			File fileNewDat = new File(fileNewNameDat);
			File fileNewIdx = new File(fileNewNameIdx);
			
			if (!fileNewDat.exists() && !fileNewIdx.exists()) {//文件不存在，创建
				DataWriter dataWriter = new DataWriter(fileNewNameDat,fileNewNameIdx,1000,500,"entId","erpCode","shopCode","goodsCode");
				shopDataWriter.put(shopCode, dataWriter);
				cacheShopData.put(shopCode, dataList);
			}else {//文件存在
				DataWriter dataWriter = shopDataWriter.get(shopCode);
				List shopCodeData = cacheShopData.get(shopCode);
				shopCodeData.addAll(dataList);
				if(shopCodeData.size() > WRITER_MAX_SIZE) {
					dataWriter.onAppend(shopCodeData);
					shopCodeData.clear();
				}
			}
		}
		// ---------- 根据Map的Key进行遍历生成门店数据end ----------
				
		//批量插入EXT接口表
		Date date = new Date();
        paramList.forEach(t -> {
	        	t.setEgsrid(UniqueID.getUniqueID(true));//设置主键
	        	//t.setDealStatus(0);
	        	t.setDealStatus(2);//和以往方式处理不同。同步完后调用同步正式表接口：更新表中ID字段
	    		t.setCreateDate(date);
	    		t.setUpdateDate(t.getCreateDate());
	    	});

        try {
        		long time1 = System.currentTimeMillis();
	        template.insert("ExtGoodsShopRefModelMapper.insertAll", paramList);
	        long time2 = System.currentTimeMillis();
	        logger.info("------------------------------ 批量【插入EXT接口表: 数量{}】时间 = "+(time2 - time1),paramList.size());
        }catch (Exception e) {
	        	logger.error("******************** 批量插入【EXT接口表】失败,逐一单条数据处理 start ********************");
	        	logger.error("批量插入失败错误信息[{}]", e);
	       	for (ExtGoodsShopRefModel model : paramList) {
		    		 try {
		    			 this.getOmdTemplate().insert("ExtGoodsShopRefModelMapper.insert", model);
	             } catch (Exception ex) {
	            	 logger.error("单条插入【EXT接口表】失败, 错误数据:{}",JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
	             }
			 }
	       	logger.error("******************** 批量插入【EXT接口表】失败,逐一单条数据处理 end ********************");
        }
		long end = System.currentTimeMillis();
		String result = "====================>>> One request cost time is ："+(end-start);
		this.logger.info(result);
		return ServiceResponse.buildSuccess(result);
	}
	
	//开始处理数据
	public ServiceResponse processFileData() throws Exception {
		this.onCloseDataWriter();
		insertAll = new ArrayList<Map<String, Object>>();
		updateAll = new ArrayList<Map<String, Object>>();
		long start = System.currentTimeMillis();
 		List<Map> shops = this.getOmdTemplate().selectList("AdvancedQueryMapper.distinctShopCode");
 		
 		// ---------- 根据门店处理数据 start ----------
		//检查old文件夹是否有文件。没有：新增数据直接插入数据库。有：比较差集，合并数据生成新的全量文件
		for (Map shop : shops) {
			String shopCode = (String)shop.get("shopCode");
			String fileOldNameDat = dbPathOld+SEPARATOR+"goodsshopref-"+shopCode+".dat";
			String fileOldNameIdx = dbPathOld+SEPARATOR+"goodsshopref-"+shopCode+".idx";
			File fileOldDat = new File(fileOldNameDat);
			File fileOldIdx = new File(fileOldNameIdx);
			
			String fileNewNameDat = dbPathNew+SEPARATOR+"goodsshopref-"+shopCode+".dat";
			String fileNewNameIdx = dbPathNew+SEPARATOR+"goodsshopref-"+shopCode+".idx";
			File fileNewDat = new File(fileNewNameDat);
			File fileNewIdx = new File(fileNewNameIdx);
			
			long shopStartTime = System.currentTimeMillis();
			if (fileNewDat.exists() && fileNewIdx.exists() && !fileOldDat.exists() && !fileOldIdx.exists()) {//Old文件夹中文件不存在，第一次新增
				//1.读取New文件夹的
				this.logger.info("<<<========== 处理门店{}数据 start. ==========>>>",shopCode);
				DataReader readerNew = new DataReader(fileNewNameDat,fileNewNameIdx,1000,500,"entId","erpCode","shopCode","goodsCode");
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				List<Integer> hashList = readerNew.getHashList();
				hashList.forEach(hashCode -> {
					try {
						Map<String,Map<String,Object>> dataMap = readerNew.onBlockReader(hashCode);
						dataMap.values().forEach(action -> {
							dataList.add(action);
						});
						if(dataList.size() >= PROCESS_DATA_MAX_COUNT) {
							this.batchAllInsert(dataList);
							dataList.clear();
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				});
				//处理：可能最后一批数据，有可能小于PROCESS_DATA_MAX_COUNT
				if(!dataList.isEmpty()) {
					this.batchAllInsert(dataList);
				}
				readerNew.onClose();
				//将数据移动到Old文件夹(第一次全量文件)
				this.moveTotherFolders(fileNewDat);
				this.moveTotherFolders(fileNewIdx);
			}else if(fileNewDat.exists() && fileNewIdx.exists() && fileOldDat.exists() && fileOldIdx.exists()){//Old文件夹文件存在，比对差集，然后生成新的全量数据
				DataReader readerNew = new DataReader(fileNewNameDat,fileNewNameIdx,1000,500,"entId","erpCode","shopCode","goodsCode");
				DataReader readerOld = new DataReader(fileOldNameDat,fileOldNameIdx,1000,500,"entId","erpCode","shopCode","goodsCode");
				long time1 = System.currentTimeMillis();
				readerNew.onMinus(readerOld,this);//差集回调处理数据
				long time2 = System.currentTimeMillis();
				this.logger.info("<<<========== 门店{}数据比较【差集】时间:{} ==========>>>",shopCode,(time2 - time1));
				
				//处理：可能最后一批数据，有可能小于PROCESS_DATA_MAX_COUNT
				if(!insertAll.isEmpty()) {
					this.batchAllInsert(insertAll);
					insertAll.clear();
				};
				if(!updateAll.isEmpty()) {
					this.batchAllUpdate(updateAll);
					updateAll.clear();
				}
				
				//生成全量数据移动到Old文件夹（删除之前的全量文件）
				String fileGenNameDat = dbPath+SEPARATOR+"goodsshopref-"+shopCode+".dat";
				String fileGenNameIdx = dbPath+SEPARATOR+"goodsshopref-"+shopCode+".idx";
				DataWriter dataGenWriter = new DataWriter(fileGenNameDat,fileGenNameIdx,1000,500,"entId","erpCode","shopCode","goodsCode");
				time1 = System.currentTimeMillis();
				readerNew.onUnion(readerOld,dataGenWriter,null);
				time2 = System.currentTimeMillis();
				this.logger.info("<<<========== 门店{}数据处理【并集】时间:{} ==========>>>",shopCode,(time2 - time1));
				dataGenWriter.onClose();
				readerNew.onClose();
				readerOld.onClose();
				File fileGenDat = new File(fileGenNameDat);
				File fileGenIdx = new File(fileGenNameIdx);
				if (fileGenDat.exists() && fileGenIdx.exists()){
					this.moveTotherFolders(fileGenDat);
					this.moveTotherFolders(fileGenIdx);
				}
			}
			long shopEndTime = System.currentTimeMillis();
			this.logger.info("<<<========== 处理门店{}数据 end.time is {} ==========>>>",shopCode,(shopEndTime - shopStartTime));
			//处理数据后，删除New的文件
			if (fileNewDat.exists() && fileNewIdx.exists()){
				fileNewDat.delete();
				fileNewIdx.delete();
			}
		}
		// ---------- 根据门店处理数据 end ----------
		
		resetIdByCode();//通过Code设置Id
		long end = System.currentTimeMillis();
		return ServiceResponse.buildSuccess("cost time is : "+(end-start));
	}
	
	//关闭所有输出流
	public void onCloseDataWriter() throws IOException {
		for (Entry<String, DataWriter> writer : shopDataWriter.entrySet()) {
			String shopCode = writer.getKey();
			DataWriter dataWriter = writer.getValue();
			List shopCodeData = cacheShopData.get(shopCode);
			if(shopCodeData!=null && !shopCodeData.isEmpty()) {
				dataWriter.onAppend(shopCodeData);
				shopCodeData.clear();
				dataWriter.onClose();
			}
		}
	}
	
	//回调方法（插入）
	@Override
	public void onInsert(List<Map<String, Object>> dataList) {
		if(dataList!=null && !dataList.isEmpty()) {
//this.logger.info("插入回调函数："+dataList.size());
			insertAll.addAll(dataList);
			if(insertAll.size() >= PROCESS_DATA_MAX_COUNT) {
				this.batchAllInsert(insertAll);
				insertAll.clear();
			}
		}
	}
	
	//回调方法（更新）
	@Override
	public void onUpdate(List<Map<String, Object>> dataList) {
		if(dataList!=null && !dataList.isEmpty()) {
//this.logger.info("更新回调函数："+dataList.size());
			updateAll.addAll(dataList);
			if(updateAll.size() >= PROCESS_DATA_MAX_COUNT) {
				this.batchAllUpdate(updateAll);
				updateAll.clear();
			}
		}
	}

	//批量插入
	public void batchAllInsert(List<Map<String, Object>> dataList) {
		if(dataList!=null && !dataList.isEmpty()) {
//this.logger.info("批量插入的【数据】--->>>"+JSON.toJSONString(dataList));			
			for (Map<String, Object> map : dataList) {
				map.put("gsrid",UniqueID.getUniqueID(true));//设置经营配制主键
			}
			List<GoodsShopRefModel> paramList = JSON.parseArray(JSON.toJSONString(dataList),GoodsShopRefModel.class);
			long time1 = System.currentTimeMillis();
        	try {
        		template.insert("GoodsShopRefModelMapper.insertAll", paramList);
        	}catch (Exception e) {
            	logger.error("******************** 批量插入【GoodsShopRef表】失败,逐一单条数据处理 start ********************");
            	logger.error("批量插入失败错误信息[{}]", e);
            	for (GoodsShopRefModel model : paramList) {
    	    		 try {
    	    			 this.getOmdTemplate().insert("GoodsShopRefModelMapper.insert",model);
    	             } catch (Exception ex) {
    	            	 logger.error("单条插入【GoodsShopRef表】失败, 错误数据:{}",JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
    	                 errorLogService.insertLog("goodsshopref", JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
    	             }
    			 }
    	       	logger.error("******************** 批量插入【GoodsShopRef表】失败,逐一单条数据处理 end ********************");
            }
            long time2 = System.currentTimeMillis();
 	        logger.info("------------------------------ 批量插入【GoodsShopRef表：数量{}】时间 = "+(time2 - time1),dataList.size());
		}
	}
	
	//批量更新
	public void batchAllUpdate(List<Map<String, Object>> dataList) {
		if(dataList!=null && !dataList.isEmpty()) {
//this.logger.info("批量更新的【数据】--->>>"+JSON.toJSONString(dataList));				
			long time1 = System.currentTimeMillis();
			List<GoodsShopRefModel> paramList = JSON.parseArray(JSON.toJSONString(dataList),GoodsShopRefModel.class);
        	try {
        	template.update("GoodsShopRefModelMapper.updateAll", paramList);
        	}catch (Exception e) {
            	logger.error("******************** 批量更新【GoodsShopRef表】失败,逐一单条数据处理 start ********************");
            	logger.error("批量更新【GoodsShopRef表】失败错误信息[{}]", e);
            	for (GoodsShopRefModel model : paramList) {
    	    		 try {
    	    			 this.getOmdTemplate().insert("GoodsShopRefModelMapper.update", model);
    	             } catch (Exception ex) {
    	            	 logger.error("单条更新【GoodsShopRef表】失败, 错误数据:{}", JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
    	                 errorLogService.insertLog("goodsshopref", JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
    	             }
    			 }
    	       	logger.error("******************** 批量更新【GoodsShopRef表】失败,逐一单条数据处理 end ********************");
            }
            long time2 = System.currentTimeMillis();
 	        logger.info("------------------------------ 批量更新【GoodsShopRef表：数量{}】时间 = "+(time2 - time1),dataList.size());
		}
	}
	
	//移动下载文件从new文件夹移动到old文件夹
	public void moveTotherFolders(File startFile) {
		String fileName = startFile.getName();
		String endPathFile = dbPathOld + System.getProperty("file.separator") + fileName;
		try {
			File tmpFile = new File(dbPathOld);// 获取文件夹路径
			if (!tmpFile.exists()) {// 判断文件夹是否创建，没有创建则创建新文件夹
				tmpFile.mkdirs();
			}
			File oldFile = new File(endPathFile);
			if(oldFile.exists()){//删除以前文件，将文件命名到old目录
				oldFile.delete();
			}
			if (!startFile.renameTo(oldFile)) {
				logger.error("文件移动失败！文件名：{} 起始路径：{}", fileName, startFile.getPath());
			}/*else{
				logger.info("文件移动Success！文件名：{} 起始路径：{}", fileName, startFile.getPath());
			}*/
		} catch (Exception e) {
			logger.error("文件移动异常！文件名：{},错误信息{} ", fileName, e.getStackTrace());
		}
	}
	
	//设置经营配制表ID
	public void resetIdByCode(){
		//1.商品Id
		Map<String,String> paramMap = new HashMap<String,String>();
    	this.logger.info("==========》》》设置商品Id Start...");
    	long start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "goods");
    	paramMap.put("targetCode", "goodsCode");
    	paramMap.put("sourceCode", "goodsCode");
    	paramMap.put("targetId", "sgid");
    	paramMap.put("sourceId", "sgid");
    	int updateSgidCount = this.getOmdTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	long end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置商品Id End... 更新数量:{},时间:{}",updateSgidCount,(end-start));
		
    	//2.设置门店Id
    	this.logger.info("==========》》》设置门店Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "shop");
    	paramMap.put("targetCode", "shopCode");
    	paramMap.put("sourceCode", "shopCode");
    	paramMap.put("targetId", "shopId");
    	paramMap.put("sourceId", "shopId");
    	int updateShopIdCount = this.getOmdTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置门店Id End... 更新数量:{},时间:{}",updateShopIdCount,(end-start));
    	
    	//3.设置档口Id
    	this.logger.info("==========》》》设置档口Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "stallinfo");
    	paramMap.put("targetCode", "stallCode");
    	paramMap.put("sourceCode", "stallCode");
    	paramMap.put("targetId", "siid");
    	paramMap.put("sourceId", "siid");
    	int updateSiidCount = this.getOmdTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置档口Id End... 更新数量:{},时间:{}",updateSiidCount,(end-start));

    	//4.柜组Id
    	/*this.logger.info("==========》》》设置柜组Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "saleorg");
    	paramMap.put("targetCode", "orgCode");
    	paramMap.put("sourceCode", "orgCode");
    	paramMap.put("targetId", "saleOrgId");
    	paramMap.put("sourceId", "saleOrgId");
    	int updateSaleOrgIdCount = this.getTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置柜组Id End... 更新数量:{},时间:{}",updateSaleOrgIdCount,(end-start));*/
    	
    	//5.供应商Id
    	this.logger.info("==========》》》设置供应商Id Start...");
    	start = System.currentTimeMillis();
    	paramMap.put("targetTable", "goodsshopref");
    	paramMap.put("sourceTable", "vender");
    	paramMap.put("targetCode", "venderCode");
    	paramMap.put("sourceCode", "venderCode");
    	paramMap.put("targetId", "vid");
    	paramMap.put("sourceId", "vid");
    	int updateVidCount = this.getOmdTemplate().update("AdvancedQueryMapper.updateIdByCode", paramMap);
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》设置供应商Id End。更新数量:{},时间:{}",updateVidCount,(end-start));
    	
    	//6.定制化需求：初始化goods的saleprice字段
    	this.logger.info("==========》》》初始化goods的saleprice字段 Start...");
    	start = System.currentTimeMillis();
    	int initGoodsSalePriceCount = this.getOmdTemplate().update("GoodsModelMapper.initGoodsSalePrice");
    	end = System.currentTimeMillis();
    	this.logger.info("==========》》》初始化goods的saleprice字段End... 更新数量:{},时间:{}",initGoodsSalePriceCount,(end-start));
	}

}
