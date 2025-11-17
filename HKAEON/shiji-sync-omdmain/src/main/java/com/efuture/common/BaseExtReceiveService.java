package com.efuture.common;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.efuture.component.ErrorLogServiceImpl;
import com.efuture.model.ErrorLogModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.product.model.ServiceSession;
import com.product.util.UniqueID;

public abstract class BaseExtReceiveService {

    @SuppressWarnings("unused")
	private String mapperNamespace;
    private String tableName;
    private String keyName;
    private String countStmt;
    private String updateStmt;
    private String insertAllStmt;

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

    @SuppressWarnings("unchecked")
	//final Class<T> beanClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), BaseExtReceiveService.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseExtReceiveService.class);

    public BaseExtReceiveService(String mapperNamespace, String tableName, String keyName) {
        this.mapperNamespace = mapperNamespace;
        this.tableName = tableName;
        this.keyName = keyName;
        this.countStmt = mapperNamespace + ".count";
        this.updateStmt = mapperNamespace + ".update";
        this.insertAllStmt = mapperNamespace + ".insertAll";
    }

    protected SqlSessionTemplate getTemplate() {
        return SpringUtil.getBean("sqlSessionTemplate", SqlSessionTemplate.class);
    }

    @Transactional
    public String receive(ServiceSession session, JSONObject param) throws Exception {
    	long startTime = System.currentTimeMillis();
        Object o = param.get(tableName);
        SqlSessionTemplate template = getTemplate();
        ErrorLogServiceImpl errorLogService = SpringUtil.getBean("errorlog", ErrorLogServiceImpl.class);

        List<JSONObject> list = JSON.parseArray(JSON.toJSONString(o, SerializerFeature.WriteMapNullValue), JSONObject.class);

        if (list.size() == 0) {
            return "传入数据空";
        }

        LinkedList<JSONObject> beInsert = new LinkedList<>();
        long updateCount = 0L;
        for (JSONObject t : list) {
            long i = template.selectOne(countStmt, t);

            if (i > 0) {
                t.put("updateDate", new Date());
                t.put("dealStatus", 0);

                try {
                    updateCount += template.update(updateStmt, t);
                } catch (Exception e) {
                    LOGGER.error("更新失败,表[{}], 参数[{}]", tableName, t);
                    errorLogService.insertLog(tableName, JSON.toJSONString(t, SerializerFeature.WriteMapNullValue), e.toString(), "U", "d");
                }
            } else {
                t.put("createDate", new Date());
                t.put("updateDate", new Date());
                t.put(keyName, UniqueID.getUniqueID());
                t.put("dealStatus", 0);

                beInsert.add(t);
            }
        }

        LOGGER.info("更新{}条, 表[{}]", updateCount, tableName);

        if (beInsert.size() == 0) {
            return "success";
        }

        try {
            long insertCount = template.insert(insertAllStmt, beInsert);
            LOGGER.info("插入{}条, 表[{}]", beInsert.size(), this.getTableName());
        } catch (Exception e) {
            LOGGER.error("插入失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
            LOGGER.error("表[{}], 错误数据集:{}", tableName, JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
            LOGGER.info("表[{}], 尝试循环插入---------", tableName);
            Iterator<JSONObject> iterator = beInsert.iterator();

            errorLogService.insertLog(tableName, JSON.toJSONString(param, SerializerFeature.WriteMapNullValue), e.toString(), "I", "p");

            while (iterator.hasNext()) {
                JSONObject obj = iterator.next();
                try {
                    template.insert(mapperNamespace + ".insert", obj);
                } catch (Exception ex) {
                    LOGGER.error("单条插入失败，表[{}], 错误数据:{}", tableName, JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue));
                    errorLogService.insertLog(tableName, JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "d");
                }
            }

        }

        return "success";
    }

    public List<JSONObject> search(JSONObject param) {
        return getTemplate().selectList("CommonMapper.select", param);
    }

    public long dataCount(JSONObject param) {
        return getTemplate().selectOne("CommonMapper.dataCount", param);
    }

    public void updateDealStatus(JSONObject param) {
        getTemplate().update("CommonMapper.updateDealStatus", param);
    }

}
