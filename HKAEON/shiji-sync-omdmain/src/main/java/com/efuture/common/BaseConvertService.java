package com.efuture.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.product.util.UniqueID;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class BaseConvertService {

    private static final Object lock = new Object();

    private String mapperNamespace;
    private String tableName;
    private String keyName;
    private String countStmt;
    private String updateStmt;
    private String insertAllStmt;

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



    @SuppressWarnings("unchecked")
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConvertService.class);

    public BaseConvertService(String mapperNamespace, String tableName, String keyName) {
        this.mapperNamespace = mapperNamespace;
        this.tableName = tableName;
        this.keyName = keyName;
        this.countStmt = mapperNamespace + ".count";
        this.updateStmt = mapperNamespace + ".update";
        this.insertAllStmt = mapperNamespace + ".insertAll";
    }

    protected SqlSessionTemplate getTemplate() {
        return SpringUtil.getBean(SqlSessionTemplate.class);
    }

    @SuppressWarnings("rawtypes")
	@Transactional
    public long receive(BaseExtReceiveService service, List<JSONObject> data) throws Exception {
        long startTime = System.currentTimeMillis();
        SqlSessionTemplate sqlSessionTemplate = getTemplate();

        synchronized (lock) {
            for (JSONObject o : data) {
                Long idValue = o.getLong(service.getKeyName());
                o.put(this.keyName, idValue);
            }
        }

        LinkedList<JSONObject> beInsert = new LinkedList<>();
        long updateCount = 0L;

        for (JSONObject t : data) {
            long count = sqlSessionTemplate.selectOne(countStmt, t);
            t.put("erpUpdateDate", new Date());

            if (count > 0) {
                t.put("updateDate", new Date());
                updateCount += sqlSessionTemplate.update(updateStmt, t);
            } else {
            	t.put("updateDate", new Date());
                beInsert.add(t);
            }
        }


        JSONObject param = new JSONObject();
        param.put("tableName", service.getTableName());
        param.put("keyName", service.getKeyName());

        List<Long> bak;

        synchronized (lock) {
            List<Long> keyValues = data.stream().map(e -> e.getLong(service.getKeyName())).collect(Collectors.toList());
            param.put("keyValues", keyValues);
            bak = keyValues;
        }

        LOGGER.info("更新{}条, 表[{}]", updateCount, this.getTableName());

        long insertCount = 0L;
        if (beInsert.size() > 0) {
            insertCount = sqlSessionTemplate.insert(insertAllStmt, beInsert);
        }

        if ((updateCount + insertCount) == data.size()) {
            service.updateDealStatus(param);
        } else {
            LOGGER.error("转换出错, table[{}], 主键集合:", tableName, JSON.toJSONString(bak));
        }


        LOGGER.info("插入{}条, 表[{}]", beInsert.size(), this.getTableName());

        return updateCount + insertCount;
    }

    public void onSyncRedis() {}

}
