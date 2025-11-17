 package com.efuture.common;

 import com.alibaba.druid.util.StringUtils;
 import com.alibaba.fastjson.JSON;
 import com.alibaba.fastjson.JSONObject;
 import com.alibaba.fastjson.serializer.SerializerFeature;
 import com.efuture.component.ErrorLogServiceImpl;
 import com.efuture.model.CommonExtModel;
 import com.product.model.ResponseCode;
 import com.product.model.ServiceResponse;
 import com.product.model.ServiceSession;
 import com.product.util.UniqueID;
 import org.mybatis.spring.SqlSessionTemplate;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.transaction.annotation.Transactional;

 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.lang.reflect.ParameterizedType;
 import java.lang.reflect.Type;
 import java.util.*;
 import java.util.stream.Collectors;

public abstract class CommonExtSyncService<T extends CommonExtModel,S> {

	protected String mapperNamespace;
	protected String tableName;
	protected String keyName;
	protected String insertAllStmt;
	protected String updateAllStmt;
	protected Class<T> modelClass;
	protected SqlSessionTemplate template;
    public Logger logger;//通用日志

    @SuppressWarnings("unchecked")
	public CommonExtSyncService(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        this.mapperNamespace = mapperNamespace;
        this.tableName = tableName;
        this.keyName = keyName;
        this.insertAllStmt = mapperNamespace + ".insertAll";
        this.updateAllStmt = mapperNamespace + ".updateAll";
        this.template = template;
        ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		Type[] actualTypeArguments = superclass.getActualTypeArguments();
		modelClass = (Class<T>) actualTypeArguments[0];
		logger = LoggerFactory.getLogger((Class<T>) actualTypeArguments[1]);
    }

    protected SqlSessionTemplate getOmdTemplate() {
        return SpringUtil.getBean("sqlSessionTemplate", SqlSessionTemplate.class);
    }

    //处理增量接口
    public ServiceResponse receiveChangeData(ServiceSession session, JSONObject param) throws Exception {
    	logger.info("【ExtSync:{}】1.------------------------------ EXT 开始处理【增量数据】",tableName);
        List<T> paramList = JSON.parseArray(JSON.toJSONString(param.get(tableName)),modelClass);
        if (paramList == null || paramList.isEmpty()) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
        
        Map<String, Long> collect = paramList.stream().collect(Collectors.groupingBy(T::getUniqueKeyValue,Collectors.counting()));
        List<String> duplicateKey = null;
        for (Map.Entry<String, Long> entry : collect.entrySet()) {
        	Long count = entry.getValue();
            if(count > 1) {//表明有过程数据，必须单条单条处理数据
            	logger.info("【ExtSync:{}】2.------------------------------ key = {} , count = {} ",tableName,entry.getKey(),count);
            	duplicateKey = duplicateKey == null ? new ArrayList<String>() : duplicateKey;
            	duplicateKey.add(entry.getKey());
            	//String result = receiveSingleJavaData(paramList);
            	//return ServiceResponse.buildSuccess(result);
            }
        }
        
        String batchResult = null;
        String singleResult = null;
        List<String> duplicate = duplicateKey;//不能拿直接使用duplicateKey。filter报错
        if(duplicate!=null && !duplicate.isEmpty()) {//表明过程数据，需要排除单独处理
        	List<T> duplicateList = paramList.stream().filter(t -> duplicate.contains(t.getUniqueKeyValue())).collect(Collectors.toList());
        	singleResult = receiveSingleJavaData(duplicateList);//处理单个
        	paramList.removeAll(duplicateList);
        }
        
        if(paramList!=null && !paramList.isEmpty()) {
        	 batchResult = receiveBatchJavaData(param,paramList);
        }
       
        String result = (StringUtils.isEmpty(batchResult) ? "" : batchResult) + (StringUtils.isEmpty(singleResult) ? "" : singleResult);
    	return ServiceResponse.buildSuccess(result);
    }
    
    //处理过程数据（单条数据处理）
    @Transactional
    public ServiceResponse receiveSingle(ServiceSession session, JSONObject param) throws Exception {
    	List<T> paramList = JSON.parseArray(JSON.toJSONString(param.get(tableName)),modelClass);
    	if (paramList == null || paramList.isEmpty()) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
    	String result = receiveSingleJavaData(paramList);
		return ServiceResponse.buildSuccess(result);
    }

	public String receiveSingleJavaData(List<T> paramList) {
		long startTime = System.currentTimeMillis();
        ErrorLogServiceImpl errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
        //0.处理过程数据：初始化数据
        initData(paramList);
        int updateCount = 0,insertCount = 0;
        //1.处理过程数据：循环处理过程数据
        for (int i = 0; i < paramList.size(); i++) {
        	T t = paramList.get(i);
	        try {
		        	Method method = modelClass.getMethod("getUniqueKey");
		            Object uniqueKey =  method.invoke(modelClass.newInstance());
		            JSONObject selectParams = new JSONObject();
		            selectParams.put("key", uniqueKey);
		            selectParams.put("values", Arrays.asList(t.getUniqueKeyValue()));
		            selectParams.put("table",tableName);
		            
		            //2.处理过程数据：单个查询数据
		            //List<T> dbList = template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
		            List<T> dbList = this.queryDataByUnique(selectParams);
		            if(dbList!=null && !dbList.isEmpty()) {//更新数据
		            	if(dbList.size() > 1) {//理论上只会有一条数据
		            		logger.info("【ExtSync:{}】3.----------【脏数据】可能存在脏数据:dbList.size() = {} , dbList = {}",tableName,dbList.size(),JSON.toJSON(dbList).toString());
		            	}
		            	t.setDealStatus(0); 
    	        		t.setUpdateDate(new Date());
		            	updateCount+=this.getOmdTemplate().update(mapperNamespace + ".update", t);
		            }else {//新增数据
		            	t.setDealStatus(0); 
			    		t.setCreateDate(new Date());
			    		t.setUpdateDate(t.getCreateDate());
			    		Field f;
						try {
							f = t.getClass().getDeclaredField(keyName);
							f.setAccessible(true);
					        f.set(t, UniqueID.getUniqueID(true));
						} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
		            	insertCount+=this.getOmdTemplate().insert(mapperNamespace + ".insert", t);
		            }
				}
	        catch (Exception e) {
	       	 logger.error("【ExtSync:{}】3.1 ******************** 处理过程数据出现异常:{}",tableName, e);
	       	 errorLogService.insertLog(tableName, JSON.toJSONString(t, SerializerFeature.WriteMapNullValue), e.toString(), "P", "D");
	        }
        }
        
		long endTime = System.currentTimeMillis();
		String result = "【处理过程数据结果】=====>>>update:"+updateCount+"=====>>>insert："+insertCount+"=====>>>time:"+(endTime-startTime);
		logger.info(result);
		return result;
	}
    
    @Transactional
    public ServiceResponse receive(ServiceSession session, JSONObject param) throws Exception {
        List<T> paramList = JSON.parseArray(JSON.toJSONString(param.get(tableName)),modelClass);
        if (paramList == null || paramList.isEmpty()) return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST,"传入数据空");
        String result = receiveBatchJavaData(param, paramList);
        return ServiceResponse.buildSuccess(result);
    }

	public String receiveBatchJavaData(JSONObject param, List<T> paramList) throws Exception {
		long startTime = System.currentTimeMillis();
        ErrorLogServiceImpl errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
        List<T> updateList = null;
        //0.初始化数据
        initData(paramList);
        
    	//1.封装参数
        Method method = modelClass.getMethod("getUniqueKey");
        Object uniqueKey =  method.invoke(modelClass.newInstance());
        JSONObject selectParams = new JSONObject();
        selectParams.put("key", uniqueKey);
        selectParams.put("values", paramList.stream().map(T::getUniqueKeyValue).collect(Collectors.toList()));
        selectParams.put("table",tableName);
        
        //2.批量查询数据
        long time1 = System.currentTimeMillis();
        //List<T> dbList = template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
        List<T> dbList = this.queryDataByUnique(selectParams);
        long time2 = System.currentTimeMillis();
        logger.info("【ExtSync:{}】4.------------------------------ EXT 批量进行【查询】时间 = {}",tableName,(time2 - time1));
        
        //3.初始化更新的数据
        if(dbList!=null && !dbList.isEmpty()) {
        	dbList = JSON.parseArray(JSON.toJSONString(dbList),modelClass);
        	List<String> uniqueDBKeyList = dbList.stream().map(T::getUniqueKeyValue).collect(Collectors.toList());
        	updateList = paramList.stream().filter(model ->uniqueDBKeyList.contains(model.getUniqueKeyValue())).collect(Collectors.toList());
        	Date current = new Date();
	        updateList.forEach(t -> {
 	        		t.setDealStatus(0); 
	        		t.setUpdateDate(current);
	        	});
	        paramList.removeAll(updateList);//新增集合=全集合-更新集合
	     }
        
        //4.初始化新增的数据
        if(paramList!=null && !paramList.isEmpty()) {
	        paramList.forEach(t -> {
	    		t.setDealStatus(0); 
	    		t.setCreateDate(new Date());
	    		t.setUpdateDate(t.getCreateDate());
	    		Field f;
				try {
					f = t.getClass().getDeclaredField(keyName);
					f.setAccessible(true);
			        f.set(t, UniqueID.getUniqueID(true));
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
	    	});
        }
        
        int updateCount = 0;
        try {
	        //5.批量更新
	        if(updateList!=null && !updateList.isEmpty()) {
	        	time1 = System.currentTimeMillis();
                template.update(updateAllStmt, updateList);
                time2 = System.currentTimeMillis();
	 	        logger.info("【ExtSync:{}】5.------------------------------ EXT 批量【更新】时间 = ",tableName,(time2 - time1));
	        	updateCount = updateList.size();
	        }
        }catch (Exception e) {
//        	 logger.error("批量更新失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
//        	 errorLogService.insertLog(tableName, JSON.toJSONString(updateList, SerializerFeature.WriteMapNullValue), e.toString(), "U", "P");
        	 logger.error("【ExtSync:{}】5.1 ******************** 批量【更新】失败, 表[{}],逐一单条数据处理 start ********************",tableName,tableName);
        	 logger.error("【ExtSync:{}】5.2 ******************** 批量【更新】失败错误信息:{}",tableName,e);
//        	 for (T model : updateList) {
        	 for(int i=updateList.size()-1;i>=0;i--) {
        		 T model = updateList.get(i);
        		 try {
        			 updateCount += this.getOmdTemplate().update(mapperNamespace + ".update", model);
                 } catch (Exception ex) {
                	 logger.error("【ExtSync:{}】5.3 ******************** 单条【更新】失败，表[{}], 错误数据:{}", tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
                     errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "U", "D");
                 }
			 }
        	 logger.error("【ExtSync:{}】5.4 ******************** 批量【更新】失败, 表[{}],逐一单条数据处理 end ********************",tableName,tableName);
        }
        
        int insertCount = 0;
        try {
	        //6.批量插入
	        if(paramList!=null && !paramList.isEmpty()) {
	        	time1 = System.currentTimeMillis();
                template.insert(insertAllStmt, paramList);
                time2 = System.currentTimeMillis();
	 	        logger.info("【ExtSync:{}】6 ------------------------------ EXT 批量【插入】时间 = ",tableName,(time2 - time1));
	        	insertCount = paramList.size();
	        }
        }catch (Exception e) {
//       	 	logger.error("批量插入失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
//       	 	errorLogService.insertLog(tableName, JSON.toJSONString(paramList, SerializerFeature.WriteMapNullValue), e.toString(), "I", "P");
        	logger.error("【ExtSync:{}】6.1 ******************** 批量【插入】失败, 表[{}],逐一单条数据处理 start ********************",tableName,tableName);
        	logger.error("【ExtSync:{}】6.2 ******************** 批量【插入】失败错误信息:{}",tableName,e);
//	       	for (T model : paramList) {
        	for(int i=paramList.size()-1;i>=0;i--) {
       		 T model = paramList.get(i);
	    		 try {
	    			 insertCount += this.getOmdTemplate().insert(mapperNamespace + ".insert", model);
	             } catch (Exception ex) {
	            	 logger.error("【ExtSync:{}】6.3 ******************** 单条【插入】失败，表[{}],错误数据:{}",tableName,tableName,JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
	             }
			 }
	       	logger.error("【ExtSync:{}】6.4 ******************** 批量【插入】失败, 表[{}],逐一单条数据处理 end ********************",tableName,tableName);
        }
        
        long endTime = System.currentTimeMillis();
        String result = "【批量处理结果】 =====>>>update:"+updateCount+"=====>>>insert："+insertCount+"=====>>>time:"+(endTime-startTime);
        logger.info(result);
		return result;
	}

    public List<JSONObject> search(JSONObject param) {
        //return getTemplate().selectList("CommonMapper.select", param);
        return template.selectList("CommonMapper.select", param);
    }
    
    public long dataCount(JSONObject param) {
        //return getTemplate().selectOne("CommonMapper.dataCount", param);
        return template.selectOne("CommonMapper.dataCount", param);
    }

    public void updateDealStatus(JSONObject param) {
        //getTemplate().update("CommonMapper.updateDealStatus", param);
        template.update("CommonMapper.updateDealStatus", param);
    }

    //初始化数据
    public List<T> initData(List<T> list){
    	return list;
    }
    
    public List<T> queryDataByUnique(JSONObject selectParams) {
    	return template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
    }
    
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMapperNamespace() {
        return mapperNamespace;
    }

    public void setMapperNamespace(String mapperNamespace) {
        this.mapperNamespace = mapperNamespace;
    }
}
