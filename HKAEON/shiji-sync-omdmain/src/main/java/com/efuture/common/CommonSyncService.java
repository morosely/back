package com.efuture.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.component.ErrorLogServiceImpl;
import com.efuture.model.CommonModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("hiding")
public abstract class CommonSyncService<T extends CommonModel,S>{

    protected SqlSessionTemplate template;
    protected String mapperNamespace;
    protected String tableName;
    protected String keyName;
    protected String insertAllStmt;
    protected String updateAllStmt;
    protected Class<T> modelClass;
    public Logger logger;//通用日志
    protected boolean abroadBuyFlag;//海外购标识
    protected ErrorLogServiceImpl errorLogService;
    protected boolean seasonFlag;//增加是否受限标志开关：默认false：不自动更新受限品类下新增的商品，商品受限商品标识从DMC获取

    @SuppressWarnings("unchecked")
	public CommonSyncService(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
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
        errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
    }

    public SqlSessionTemplate getTemplate() {
        return SpringUtil.getBean(SqlSessionTemplate.class);
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return this.template;
    }

	public long receive(CommonExtSyncService service, List<JSONObject> data) throws Exception {
    //public long receive(ServiceSession session, JSONObject param) throws Exception {
        long startTime = System.currentTimeMillis();
        //Ext表的Id赋值给正式表Id
        //JSONArray data = (JSONArray)param.get(tableName);
        for (Object o : data) {
        	JSONObject json = ((JSONObject)o);
            Long idValue = json.getLong(service.getKeyName());
            json.put(this.keyName, idValue);
        }

        //Converter字段转换（EXT ，正式model字段映射转换）
        converter(data);

        List<T> paramList = JSON.parseArray(JSON.toJSONString(data),modelClass);
        if (paramList == null || paramList.isEmpty()) return 0;

        //去重操作 start
        Map<String, Long> collect = paramList.stream().collect(Collectors.groupingBy(T::getUniqueKeyValue,Collectors.counting()));
        List<String> duplicateKey = null;
        for (Map.Entry<String, Long> entry : collect.entrySet()) {
            Long count = entry.getValue();
            if(count > 1) {//表明有重复数据，必须单条单条处理数据
                logger.info("【Sync:{}】1.------------------------------ key = {} , count = {} ",tableName,entry.getKey(),count);
                duplicateKey = duplicateKey == null ? new ArrayList<String>() : duplicateKey;
                duplicateKey.add(entry.getKey());
            }
        }
        Integer singleResult = 0;
        List<Long> errorkeyValues = new ArrayList<>(); // 存ext表中处理失败的数据
        List<String> duplicate = duplicateKey;//不能拿直接使用duplicateKey。filter报错
        if(duplicate!=null && !duplicate.isEmpty()) {//表明过程数据，需要排除单独处理
            List<T> duplicateList = paramList.stream().filter(t -> duplicate.contains(t.getUniqueKeyValue())).collect(Collectors.toList());
            singleResult = singleData(duplicateList,errorkeyValues);//处理单个
            paramList.removeAll(duplicateList);
        }
        //去重操作 end
        
        int updateCount = 0;
        int insertCount = 0;
        List<T> updateList = null;
        List<T> dbList = null;
        JSONObject selectParams = new JSONObject();

        if(paramList!=null && !paramList.isEmpty()) {
            //0.初始化数据
            initData(paramList);
            //1.封装参数
            Method method = modelClass.getMethod("getUniqueKey");
            Object uniqueKey = method.invoke(modelClass.newInstance());
            selectParams.put("key", uniqueKey);
            selectParams.put("values", paramList.stream().map(T::getUniqueKeyValue).collect(Collectors.toList()));
            selectParams.put("table", tableName);

            //2.批量查询数据
            long time1 = System.currentTimeMillis();
            //List<T> dbList = template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
            dbList = this.queryDataByUnique(selectParams);
            long time2 = System.currentTimeMillis();
            logger.info("【Sync:{}】3.------------------------------ 批量进行【查询】时间 = {}",tableName,(time2 - time1));
        }

        //3.初始化更新的数据
        if(dbList!=null && !dbList.isEmpty()) {
        	dbList = JSON.parseArray(JSON.toJSONString(dbList),modelClass);
        	List<String> uniqueDBKeyList = dbList.stream().map(T::getUniqueKeyValue).collect(Collectors.toList());
        	updateList = paramList.stream().filter(model ->uniqueDBKeyList.contains(model.getUniqueKeyValue())).collect(Collectors.toList());
        	Date current = new Date();
	        updateList.forEach(t -> {
	        		t.setUpdateDate(current);
	        		t.setErpUpdateDate(current);
	        	});
	        paramList.removeAll(updateList);//新增集合=全集合-更新集合
	     }

        //4.初始化新增的数据
        if(paramList!=null && !paramList.isEmpty()) {
        	Date current = new Date();
	        paramList.forEach(t -> {
	    		t.setCreateDate(current);
	    		t.setUpdateDate(current);
	    		t.setErpUpdateDate(current);
	    	});
        }
        //批量插入
        insertCount = insertAll(errorkeyValues, insertCount ,paramList);
        //批量更新
        updateCount = updateAll(errorkeyValues, updateCount, updateList);

        //更新EXT表的dealStatus处理标识位
        JSONObject param = new JSONObject();
        param.put("tableName", service.getTableName());
        param.put("keyName", service.getKeyName());
        List<Long> keyValues = data.stream().map(e -> e.getLong(service.getKeyName())).collect(Collectors.toList());
  
		if (!errorkeyValues.isEmpty()) {
			keyValues.removeAll(errorkeyValues); // 减去ext表中异常数据
			param.put("keyValues", errorkeyValues);
			param.put("dealStatus", 3); // ext同步正式表失败
			service.updateDealStatus(param);
		}
        param.put("keyValues", keyValues);
        param.put("dealStatus", 2);
        Long time1 = System.currentTimeMillis();
//        if ((updateCount + insertCount) == data.size()) {
            service.updateDealStatus(param);
//        } else {
//        	logger.error("转换出错, table[{}], 主键集合:", tableName, JSON.toJSONString(keyValues));
//        }
        Long time2 = System.currentTimeMillis();
        logger.info("【Sync:{}】6.------------------------------【批量更新EXT状态】时间 = {}",tableName,(time2 - time1));
        long endTime = System.currentTimeMillis();
        String result = " 【批量处理】==========>>>update:"+updateCount+"==========>>>insert："+insertCount+"==========>>>cost time:"+(endTime-startTime);
        logger.info(result);
        /*//add by yihaitao 2022-12-06 代码貌似从未使用过
        if(abroadBuyFlag) {//海外购标识
        	specialData(paramList,updateList,dbList);//每次同步完成后调用的方法
        }*/
        specialData(paramList,updateList,dbList);//新增，更新，数据库数据
        return updateCount + insertCount + singleResult;
    }

    //批量插入
    public int insertAll(List<Long> errorkeyValues, int insertCount,List<T> paramList) {
        try {
	        //5.批量插入
	        if(paramList !=null && !paramList.isEmpty()) {
                Long time1 = System.currentTimeMillis();
	        	template.insert(insertAllStmt, paramList);
                Long time2 = System.currentTimeMillis();
	 	        logger.info("【Sync:{}】4.------------------------------ 批量【插入】时间 = {}",tableName,(time2 - time1));
	        	insertCount = paramList.size();
	        }
        }catch (Exception e) {
//       	 	logger.error("批量插入失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
//       	 	errorLogService.insertLog(tableName, JSON.toJSONString(paramList, SerializerFeature.WriteMapNullValue), e.toString(), "I", "P");
        	logger.error("【Sync:{}】4.1 ******************** 批量【插入】失败, 表[{}],逐一单条数据处理 start ********************",tableName,tableName);
        	logger.error("【Sync:{}】4.2 ******************** 批量【插入】失败错误信息:{}",tableName, e);
        	for (T model : paramList) {
	    		 try {
	    			 insertCount += this.getTemplate().insert(mapperNamespace + ".insert", model);
	             } catch (Exception ex) {
	            	 JSONObject json = (JSONObject) JSON.toJSON(model);
	            	 errorkeyValues.add(json.getLong(this.getKeyName()));
	            	 logger.error("【Sync:{}】4.3 ******************** 单条【插入】失败, 错误数据:{}", tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
	                 errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
	             }
			 }
        	logger.error("【Sync:{}】 4.4 ******************** 批量【插入】失败, 表[{}],逐一单条数据处理 end ********************",tableName,tableName);
        }
        return insertCount;
    }

    //批量更新
    public int updateAll(List<Long> errorkeyValues, int updateCount, List<T> updateList) {
        try {
	        //6.批量更新
	        if(updateList !=null && !updateList.isEmpty()) {
	        	Long time1 = System.currentTimeMillis();
	        	template.update(updateAllStmt, updateList);
                Long time2 = System.currentTimeMillis();
	 	        logger.info("【Sync:{}】5.------------------------------ 批量【更新】时间 = {}",tableName,(time2 - time1));
	        	updateCount = updateList.size();
	        }
        }catch (Exception e) {
//        	 logger.error("批量更新失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
//        	 errorLogService.insertLog(tableName, JSON.toJSONString(updateList, SerializerFeature.WriteMapNullValue), e.toString(), "U", "P");
        	logger.error("【Sync:{}】5.1 ******************** 批量【更新】失败, 表[{}],逐一单条数据处理 start ********************",tableName,tableName);
        	logger.error("【Sync:{}】5.2 ******************** 批量【更新】失败错误信息:{}",tableName,e);
        	 for (T model : updateList) {
        		 try {
        			 updateCount += this.getTemplate().update(mapperNamespace + ".update", model);
                 } catch (Exception ex) {
                	 JSONObject json = (JSONObject) JSON.toJSON(model);
	            	 errorkeyValues.add(json.getLong(this.getKeyName()));
                	 logger.error("【Sync:{}】5.3 ******************** 单条【更新】失败, 错误数据:{}, 错误信息:{}", tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), e);
                     errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "U", "D");
                 }
			 }
        	 logger.error("【Sync:{}】 5.4 ******************** 批量【更新】失败, 表[{}],逐一单条数据处理 end ********************",tableName,tableName);
        }
        return updateCount;
    }

    //单条处理
    public Integer singleData(List<T> paramList,List<Long> errorkeyValues) {
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
                        logger.info("【Sync:{}】2.----------【脏数据】可能存在脏数据:dbList.size() = {},dbList = {} ",tableName,dbList.size(),JSON.toJSON(dbList).toString());
                    }
                    t.setUpdateDate(new Date());
                    updateCount += template.update(mapperNamespace + ".update", t);
                    specialSingleUpdateData(t);
                }else {//新增数据
                    t.setCreateDate(new Date());
                    t.setUpdateDate(t.getCreateDate());
                    //Field f = t.getClass().getDeclaredField(keyName);
                    //f.setAccessible(true);
                    //f.set(t, UniqueID.getUniqueID(true));
                    insertCount += template.insert(mapperNamespace + ".insert", t);
                    specialSingleInsertData(t);
                }
            }catch (Exception e) {
                logger.error("【Sync:{}】 2.1 ******************** 【重复数据单条处理】处理过程数据出现异常:{}",tableName, e);
                errorLogService.insertLog(tableName, JSON.toJSONString(t, SerializerFeature.WriteMapNullValue), e.toString(), "P", "D");
                try {
                    Field field = modelClass.getDeclaredField(keyName);
                    field.setAccessible(true);
                    Long key = (Long)field.get(t);
                    errorkeyValues.add(key);
                }catch (Exception f){
                    logger.error("【Sync:{}】 2.2 ******************** 【重复数据单条处理】反射获取主键值产生异常:{}",tableName, f);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        String result = "【重复数据单条处理】=====>>>update:"+updateCount+"=====>>>insert："+insertCount+"=====>>>time:"+(endTime-startTime);
        logger.info(result);
        return insertCount + updateCount;
    }

    /*
      注：\n 回车(\u000a)
         \t 水平制表符(\u0009)
         \r 换行(\u000d)
     */
    public String replaceBlank(String content) {
        String dest = "";
        try {
            if (content!=null) {
                Pattern p = Pattern.compile("\\t|\r|\n|\\#|\\$|\\\\");
                Matcher m = p.matcher(content);
                dest = m.replaceAll("");
            }
        } catch (Exception e) {
            logger.error(" ==========>>>>> 字符串{}:replaceAll()转换异常:{}",content,e);
            errorLogService.insertLog(tableName, content, e.toString(), "T", "D");
            return content;
        }
        return dest;
    }

    //初始化数据
    public List<T> initData(List<T> list){
    	return list;
    };
    //通过Code设置Id
    public void resetIdByCode(){};
    //有增量数据同步完成后执行的方法
    public void changeDataAfter(){};
    public void onSyncRedis(){};

    //EXT和正式的Model字段映射转换
    public void converter(List<JSONObject> data){};

    public List<T> queryDataByUnique(JSONObject selectParams) {
    	return template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
    }

    //每次同步完成后 调用的数据（批量数据）
    public void specialData(List<T> insertData,List<T> updateData,List<T> DBdata){};

    //每次同步完成后 单独处理新增数据（单一数据）
    public void specialSingleInsertData(T t){};
    //每次同步完成后 单独处理更新数据（单一数据）
    public void specialSingleUpdateData(T t){};

    public boolean isAbroadBuyFlag() {
		return abroadBuyFlag;
	}

	public void setAbroadBuyFlag(boolean abroadBuyFlag) {
		this.abroadBuyFlag = abroadBuyFlag;
	}

	public String getMapperNamespace() {
        return mapperNamespace;
    }

    public void setMapperNamespace(String mapperNamespace) {
        this.mapperNamespace = mapperNamespace;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public boolean isSeasonFlag() {
        return seasonFlag;
    }

    public void setSeasonFlag(boolean seasonFlag) {
        this.seasonFlag = seasonFlag;
    }
}
