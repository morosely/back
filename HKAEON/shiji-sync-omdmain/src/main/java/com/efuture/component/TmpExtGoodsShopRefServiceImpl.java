package com.efuture.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.common.CommonExtSyncService;
import com.efuture.common.SpringUtil;
import com.efuture.model.TmpExtGoodsShopRefModel;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.UniqueID;

public class TmpExtGoodsShopRefServiceImpl extends CommonExtSyncService<TmpExtGoodsShopRefModel,TmpExtGoodsShopRefServiceImpl> {

	public TmpExtGoodsShopRefServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
		super(mapperNamespace, tableName, keyName, template);
	}
	
	public ServiceResponse truncateTmp(ServiceSession session, JSONObject param) throws Exception {
		logger.info("----------"+mapperNamespace + ".truncateTmp");
		this.getOmdTemplate().delete(mapperNamespace + ".truncateTmp");
		return ServiceResponse.buildSuccess("success");
	}
	
	//处理增量接口
	@Override
    public ServiceResponse receiveChangeData(ServiceSession session, JSONObject param) throws Exception {
    	logger.info("------------------------------ EXT 开始处理【增量数据】");
        List<TmpExtGoodsShopRefModel> paramList = JSON.parseArray(JSON.toJSONString(param.get(tableName)),modelClass);
        if (paramList == null || paramList.isEmpty()) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
        
        Map<String, Long> collect = paramList.stream().collect(Collectors.groupingBy(TmpExtGoodsShopRefModel::getUniqueKeyValue,Collectors.counting()));
        List<String> duplicateKey = null;
        for (Map.Entry<String, Long> entry : collect.entrySet()) {
        	Long count = entry.getValue();
            if(count > 1) {//表明有过程数据，必须单条单条处理数据
            	logger.info("key ----- " + entry.getKey()+"   count ----- " + count);
            	duplicateKey = duplicateKey == null ? new ArrayList<String>() : duplicateKey;
            	duplicateKey.add(entry.getKey());
//            	String result = receiveSingle(paramList);
//            	return ServiceResponse.buildSuccess(result);
            }
        }
        
        String batchResult = null;
        String singleResult = null;
        List<String> duplicate = duplicateKey;//不能拿直接使用duplicateKey。filter报错
        if(duplicate!=null && !duplicate.isEmpty()) {//表明过程数据，需要排除单独处理
        	List<TmpExtGoodsShopRefModel> duplicateList = paramList.stream().filter(t -> duplicate.contains(t.getUniqueKeyValue())).collect(Collectors.toList());
        	singleResult =  receiveSingle(duplicateList);//处理单个
        	paramList.removeAll(duplicateList);
        }
        
        if(paramList!=null && !paramList.isEmpty()) {
       	 	batchResult = receiveBatch(paramList);
        }
      
        String result = (StringUtils.isEmpty(batchResult) ? "" : batchResult) + (StringUtils.isEmpty(singleResult) ? "" : singleResult);
   		return ServiceResponse.buildSuccess(result);
    }
	
	public String receiveSingle(List<TmpExtGoodsShopRefModel> paramList) {
		long startTime = System.currentTimeMillis();
        ErrorLogServiceImpl errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
        //0.处理过程数据：初始化数据
        initData(paramList);
        
        try {
	        //1.批量插入EXT接口表
	    	long time1 = System.currentTimeMillis();
	        template.insert("ExtGoodsShopRefModelMapper.insertAll", paramList);
	        long time2 = System.currentTimeMillis();
	        logger.info("---------- 批量【插入EXT接口表】时间 = "+(time2 - time1));
        }catch (Exception e) {
        	logger.error("******************** 批量插入【插入EXT接口表】失败,逐一单条数据处理 start ********************");
        	logger.error("批量插入失败错误信息[{}]", e);
	       	for (TmpExtGoodsShopRefModel model : paramList) {
	    		 try {
	    			 this.getOmdTemplate().insert("ExtGoodsShopRefModelMapper.insert", model);
	             } catch (Exception ex) {
	            	 logger.error("单条插入【插入EXT接口表】失败, 错误数据:{}",JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
	             }
			 }
	       	logger.error("******************** 批量插入【插入EXT接口表】失败,逐一单条数据处理 end ********************");
        }
        
        int updateCount = 0,insertCount = 0;
        //2.处理过程数据（正式表数据）：循环处理过程数据
        for (int i = 0; i < paramList.size(); i++) {
        	TmpExtGoodsShopRefModel t = paramList.get(i);
	        try {
	        	Method method = modelClass.getMethod("getUniqueKey");
	            Object uniqueKey =  method.invoke(modelClass.newInstance());
	            JSONObject selectParams = new JSONObject();
	            selectParams.put("key", uniqueKey);
	            selectParams.put("values", Arrays.asList(t.getUniqueKeyValue()));
	            selectParams.put("table","goodsshopref");
	            
	            //处理过程数据：单个查询数据
	            //List<T> dbList = template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
	            List<TmpExtGoodsShopRefModel> dbList = this.queryDataByUnique(selectParams);
	            if(dbList!=null && !dbList.isEmpty()) {//更新数据
	            	if(dbList.size() > 1) {//理论上只会有一条数据
	            		logger.info("----------【脏数据】可能存在脏数据:dbList.size(),{},dbList: "+ dbList.size(),JSON.toJSON(dbList).toString());
	            	}
	            	updateCount+=this.getOmdTemplate().update("GoodsShopRefModelMapper.update", t);
	            }else {//新增数据
	            	insertCount+=this.getOmdTemplate().insert("GoodsShopRefModelMapper.insert", t);
	            }
			 }catch (Exception e) {
				 logger.error("******************** 处理过程数据出现异常  ********************{}", e);
				 errorLogService.insertLog(tableName, JSON.toJSONString(t, SerializerFeature.WriteMapNullValue), e.toString(), "P", "D");
			 }
        }
        
		long endTime = System.currentTimeMillis();
		String result = "【处理过程数据结果】=====>>>update:"+updateCount+"=====>>>insert："+insertCount+"=====>>>time:"+(endTime-startTime);
		logger.info(result);
		return result;
	}
    
	@Override
	public ServiceResponse receive(ServiceSession session, JSONObject param) throws Exception {
        List<TmpExtGoodsShopRefModel> paramList = JSON.parseArray(JSON.toJSONString(param.get(tableName)),modelClass);
        if (paramList == null || paramList.isEmpty()) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
        String result = receiveBatch(paramList);
        return ServiceResponse.buildSuccess(result);
    }

	public String receiveBatch(List<TmpExtGoodsShopRefModel> paramList) {
		ErrorLogServiceImpl errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
        long start = System.currentTimeMillis();
        //1清空数据
        this.getOmdTemplate().delete("TmpExtGoodsShopRefModelMapper.truncateTmp");
        
        //2.初始化新增的数据
        initData(paramList);
        
        try {
	        //3.批量插入临时表
	    	long time1 = System.currentTimeMillis();
	        template.insert(insertAllStmt, paramList);
	        long time2 = System.currentTimeMillis();
	        logger.info("---------- 批量【插入临时表】时间 = "+(time2 - time1));
        }catch (Exception e) {
        	logger.error("******************** 批量插入【插入临时表】失败,逐一单条数据处理 start ********************");
        	logger.error("批量插入失败错误信息[{}]", e);
	       	for (TmpExtGoodsShopRefModel model : paramList) {
	    		 try {
	    			 this.getOmdTemplate().insert(mapperNamespace + ".insert", model);
	             } catch (Exception ex) {
	            	 logger.error("单条插入【插入临时表】失败, 错误数据:{}", JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
	             }
			 }
	       	logger.error("******************** 批量插入【插入临时表】失败, 表[{}],逐一单条数据处理 end ********************", tableName);
        }
        
        try {
	        //4.批量插入EXT接口表
	    	long time1 = System.currentTimeMillis();
	        template.insert("ExtGoodsShopRefModelMapper.insertAll", paramList);
	        long time2 = System.currentTimeMillis();
	        logger.info("---------- 批量【插入EXT接口表】时间 = "+(time2 - time1));
        }catch (Exception e) {
        	logger.error("******************** 批量插入【插入EXT接口表】失败,逐一单条数据处理 start ********************");
        	logger.error("批量插入失败错误信息[{}]", e);
	       	for (TmpExtGoodsShopRefModel model : paramList) {
	    		 try {
	    			 this.getOmdTemplate().insert("ExtGoodsShopRefModelMapper.insert", model);
	             } catch (Exception ex) {
	            	 logger.error("单条插入【插入EXT接口表】失败, 错误数据:{}",JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
	             }
			 }
	       	logger.error("******************** 批量插入【插入EXT接口表】失败,逐一单条数据处理 end ********************");
        }
        
        //5. 关联查询(插入或更新正式表)
        long  time1 = System.currentTimeMillis();
        List<TmpExtGoodsShopRefModel> dealList = this.getOmdTemplate().selectList("TmpExtGoodsShopRefModelMapper.searchData");
        long time2 = System.currentTimeMillis();
        logger.info("---------- Left Join【查询】时间 = "+(time2 - time1));
        List<TmpExtGoodsShopRefModel> insertList = new ArrayList<TmpExtGoodsShopRefModel>();//新增数据
		List<TmpExtGoodsShopRefModel> updateList = new ArrayList<TmpExtGoodsShopRefModel>();//更新数据
        for (TmpExtGoodsShopRefModel tmp : dealList) {
			if(StringUtils.isEmpty(tmp.getGsrid())) {
				//设置主键
				tmp.setGsrid(tmp.getEgsrid());
				insertList.add(tmp);
			}else {
				updateList.add(tmp);
			}
		}
        int insertCount = 0;
        int updateCount = 0;
        //批量插入
        if(!insertList.isEmpty()){
        	time1 = System.currentTimeMillis();
        	try {
        		insertCount = template.insert("GoodsShopRefModelMapper.insertAll", insertList);
        	}catch (Exception e) {
            	logger.error("******************** 批量插入【GoodsShopRef表】失败,逐一单条数据处理 start ********************");
            	logger.error("批量插入失败错误信息[{}]", e);
//    	       	for (TmpExtGoodsShopRefModel model : insertList) {
            	for(int i=insertList.size()-1;i>=0;i--) {
            		TmpExtGoodsShopRefModel model = insertList.get(i);
    	    		 try {
    	    			 this.getOmdTemplate().insert("GoodsShopRefModelMapper.insert", model);
    	             } catch (Exception ex) {
    	            	 logger.error("单条插入【GoodsShopRef表】失败, 错误数据:{}",JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
    	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
    	             }
    			 }
    	       	logger.error("******************** 批量插入【GoodsShopRef表】失败,逐一单条数据处理 end ********************");
            }
            time2 = System.currentTimeMillis();
 	        logger.info("---------- 批量插入【GoodsShopRef表】时间 = "+(time2 - time1));
 	        insertCount = insertList.size();
        }
        //批量更新
        if(!updateList.isEmpty()){
        	time1 = System.currentTimeMillis();
        	try {
        	updateCount = template.update("GoodsShopRefModelMapper.updateAll", updateList);
        	}catch (Exception e) {
            	logger.error("******************** 批量更新【GoodsShopRef表】失败,逐一单条数据处理 start ********************");
            	logger.error("批量更新【GoodsShopRef表】失败错误信息[{}]", e);
//    	       	for (TmpExtGoodsShopRefModel model : updateList) {
            	for(int i=updateList.size()-1;i>=0;i--) {
            		TmpExtGoodsShopRefModel model = updateList.get(i);
    	    		 try {
    	    			 this.getOmdTemplate().insert("GoodsShopRefModelMapper.update", model);
    	             } catch (Exception ex) {
    	            	 logger.error("单条更新【GoodsShopRef表】失败, 错误数据:{}", JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
    	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
    	             }
    			 }
    	       	logger.error("******************** 批量更新【GoodsShopRef表】失败,逐一单条数据处理 end ********************");
            }
            time2 = System.currentTimeMillis();
 	        logger.info("---------- 批量更新【GoodsShopRef表】时间 = "+(time2 - time1));
 	        updateCount = updateList.size();
        }
        
        String result = "【批量处理结果】=====>>>insert:"+insertCount+"=====>>>update:"+updateCount+"=====>>>time:"+(System.currentTimeMillis()-start);
        logger.info(result);
		return result;
	}
	
	//初始化数据
    public List<TmpExtGoodsShopRefModel> initData(List<TmpExtGoodsShopRefModel> list){
    	Date date = new Date();
    	list.forEach(model -> {
       		//t.setDealStatus(0);
           	model.setDealStatus(2);//和以往方式处理不同。同步完后调用同步ID接口
       		model.setCreateDate(date);
       		model.setUpdateDate(model.getCreateDate());
       		//ERP增删改状态1:增加,2:删除,3:更新
       		if("2".equals(model.getDMLstatus())) {
       			model.setGoodStatus(-1);
       		}else {
       			model.setGoodStatus(1);
       		}
       		//处理goodsName的/ 
       		if(!StringUtils.isEmpty(model.getGoodsName())) {
   	    		String goodsName = model.getGoodsName().replaceAll("\\\\", "");
   	    		model.setGoodsName(goodsName);
       		}
       		//初始化柜组
       		String orgCode = model.getShopCode()+"001";
       		model.setOrgCode(orgCode);//门店+001
       		model.setSaleOrgId(Long.valueOf("1"+orgCode));//1+门店+001
       		model.setEgsrid(UniqueID.getUniqueID(true));
       		model.setGsrid(model.getEgsrid());
       	});
    	return list;
    }
}
