package com.efuture.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.common.CommonSyncService;
import com.efuture.common.SpringUtil;
import com.efuture.convert.AcmProfileConverter;
import com.efuture.model.AmcProfileModel;
import com.efuture.model.ExtAmcProfileModel;
import org.mybatis.spring.SqlSessionTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AmcProfileServiceImpl extends CommonSyncService<AmcProfileModel, AmcProfileServiceImpl> {

    public AmcProfileServiceImpl(String mapperNamespace, String tableName, String keyName, SqlSessionTemplate template) {
        super(mapperNamespace, tableName, keyName, template);
    }

    @Override
    public void converter(List<JSONObject> data) {
        List<ExtAmcProfileModel> extList = JSON.parseArray(JSON.toJSONString(data), ExtAmcProfileModel.class);
        List<AmcProfileModel> list = AcmProfileConverter.INSTANCE.extsToFormals(extList);
        data.clear();
        list.forEach(action -> {
            JSONObject json = (JSONObject) JSONObject.toJSON(action);
            String status = json.getString("status");
            //当接收数据中Status为空时，维持现有处理不变（dealtype为I或U时status按正常处理值为A，dealtype为D时按逻辑删除处理值为C）
            if (status == null || status.trim().length() == 0) {
                //`status` '状态("A" = Active (CAN LOGIN),"E" = Expiry,"C" = Cancel,"P" = Pending)'
                status = "D".equals(json.getString("estamp")) ? "C" : "A";
                json.put("status", status);
            }
            data.add(json);
        });
    }

    //批量插入
    @Override
    public int insertAll(List<Long> errorkeyValues, int insertCount, List<AmcProfileModel> paramList) {
        try {
            //5.批量插入
            if (paramList != null && !paramList.isEmpty()) {
                Long time1 = System.currentTimeMillis();
                List<AmcProfileModel> insertList = new ArrayList<AmcProfileModel>();
                for (AmcProfileModel model : paramList) {
                    String status = model.getStatus();
                    Integer memberLevel = model.getMemberLevel();
                    //新增数据中status有值且memberLevel为空时
                    if (!(status != null && status.trim().length() > 0 && memberLevel == null)) {
                        insertList.add(model);
                    }else{
                        logger.info("【Sync:{}】4.0------------------------------ 【批量】放弃插入 memberId = {},filler2= {}, status = {},memberLevel ={}",tableName,model.getMemberId(),model.getFiller2(),status,memberLevel);
                    }
                }
                if (!insertList.isEmpty()) {
                    template.insert(insertAllStmt, insertList);
                }
                Long time2 = System.currentTimeMillis();
                logger.info("【Sync:{}】4.------------------------------ 批量【插入】时间 = {}", tableName, (time2 - time1));
                insertCount = insertList.size();
            }
        } catch (Exception e) {
//       	 	logger.error("批量插入失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
//       	 	errorLogService.insertLog(tableName, JSON.toJSONString(paramList, SerializerFeature.WriteMapNullValue), e.toString(), "I", "P");
            logger.error("【Sync:{}】4.1 ******************** 批量【插入】失败, 表[{}],逐一单条数据处理 start ********************", tableName, tableName);
            logger.error("【Sync:{}】4.2 ******************** 批量【插入】失败错误信息:{}", tableName, e);
            for (AmcProfileModel model : paramList) {
                try {
                    insertCount += this.getTemplate().insert(mapperNamespace + ".insert", model);
                } catch (Exception ex) {
                    JSONObject json = (JSONObject) JSON.toJSON(model);
                    errorkeyValues.add(json.getLong(this.getKeyName()));
                    logger.error("【Sync:{}】4.3 ******************** 单条【插入】失败, 错误数据:{}", tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue));
                    errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "I", "D");
                }
            }
            logger.error("【Sync:{}】 4.4 ******************** 批量【插入】失败, 表[{}],逐一单条数据处理 end ********************", tableName, tableName);
        }
        return insertCount;
    }

    //批量更新
    @Override
    public int updateAll(List<Long> errorkeyValues, int updateCount, List<AmcProfileModel> updateList) {
        try {
            //6.批量更新
            if (updateList != null && !updateList.isEmpty()) {
                for (AmcProfileModel model : updateList) {
                    String status = model.getStatus();
                    Integer memberLevel = model.getMemberLevel();
                    //当接收数据中status有值且memberLevel为空时，按相应的memberId+ memberCard仅更新会员状态，其他值不做变更
                    if (status != null && status.trim().length() > 0 && memberLevel == null) {
                        model.setMembershipExpireDate(null);
                        model.setBonusPointLastMonth(null);
                        model.setBonusPointThisMonth(null);
                        model.setBonusPointUsed(null);
                        model.setBonusPointExpireDate(null);
                        model.setBonusPoint(null);
                        model.setBonusPointToBeExpired(null);
                        model.setEstamp(null);
                        model.setFiller1(null);
                        model.setFiller3(null);
                        model.setFiller4(null);
                        model.setFiller5(null);
                        model.setFiller6(null);
                        model.setFiller7(null);
                        model.setFiller8(null);
                    }
                }
                Long time1 = System.currentTimeMillis();
                template.update(updateAllStmt, updateList);
                Long time2 = System.currentTimeMillis();
                logger.info("【Sync:AmcProfileServiceImpl:{}】 5.------------------------------ 批量【更新】时间 = {}", tableName, (time2 - time1));
                updateCount = updateList.size();
            }
        } catch (Exception e) {
//        	 logger.error("批量更新失败, 表[{}], 错误信息[{}]", tableName, e.getMessage());
//        	 errorLogService.insertLog(tableName, JSON.toJSONString(updateList, SerializerFeature.WriteMapNullValue), e.toString(), "U", "P");
            logger.error("【Sync:AmcProfileServiceImpl:{}】 5.1 ******************** 批量更新失败, 表[{}],逐一单条数据处理 start ********************", tableName, tableName);
            logger.error("批量更新失败错误信息:", e);
            for (AmcProfileModel model : updateList) {
                try {
                    updateCount += this.getTemplate().update(mapperNamespace + ".update", model);
                } catch (Exception ex) {
                    JSONObject json = (JSONObject) JSON.toJSON(model);
                    errorkeyValues.add(json.getLong(this.getKeyName()));
                    logger.error("单条更新失败，表[{}], 错误数据:{}, 错误信息:{}", tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), e);
                    errorLogService.insertLog(tableName, JSON.toJSONString(model, SerializerFeature.WriteMapNullValue), ex.toString(), "U", "D");
                }
            }
            logger.error("【Sync:AmcProfileServiceImpl:{}】5.2 ******************** 批量更新失败, 表[{}],逐一单条数据处理 end ********************", tableName, tableName);
        }
        return updateCount;
    }

    //单条处理
    @Override
    public Integer singleData(List<AmcProfileModel> paramList,List<Long> errorkeyValues) {
        long startTime = System.currentTimeMillis();
        ErrorLogServiceImpl errorLogService = SpringUtil.getBean("sync.errorlog", ErrorLogServiceImpl.class);
        //0.处理过程数据：初始化数据
        initData(paramList);
        int updateCount = 0,insertCount = 0;
        //1.处理过程数据：循环处理过程数据
        for (int i = 0; i < paramList.size(); i++) {
            AmcProfileModel t = paramList.get(i);
            try {
                Method method = modelClass.getMethod("getUniqueKey");
                Object uniqueKey =  method.invoke(modelClass.newInstance());
                JSONObject selectParams = new JSONObject();
                selectParams.put("key", uniqueKey);
                selectParams.put("values", Arrays.asList(t.getUniqueKeyValue()));
                selectParams.put("table",tableName);

                //2.处理过程数据：单个查询数据
                //List<T> dbList = template.selectList("AdvancedQueryMapper.queryDataByUnique", selectParams);
                List<AmcProfileModel> dbList = this.queryDataByUnique(selectParams);
                if(dbList!=null && !dbList.isEmpty()) {//更新数据
                    if(dbList.size() > 1) {//理论上只会有一条数据
                        logger.info("【Sync:{}】2.----------【脏数据】可能存在脏数据:dbList.size() = {},dbList = {} ",tableName,dbList.size(),JSON.toJSON(dbList).toString());
                    }
                    t.setUpdateDate(new Date());
                    t.setMembershipExpireDate(null);
                    t.setBonusPointLastMonth(null);
                    t.setBonusPointThisMonth(null);
                    t.setBonusPointUsed(null);
                    t.setBonusPointExpireDate(null);
                    t.setBonusPoint(null);
                    t.setBonusPointToBeExpired(null);
                    t.setEstamp(null);
                    t.setFiller1(null);
                    t.setFiller3(null);
                    t.setFiller4(null);
                    t.setFiller5(null);
                    t.setFiller6(null);
                    t.setFiller7(null);
                    t.setFiller8(null);
                    updateCount += template.update(mapperNamespace + ".update", t);
                    specialSingleUpdateData(t);
                }else {//新增数据
                    t.setCreateDate(new Date());
                    t.setUpdateDate(t.getCreateDate());
                    String status = t.getStatus();
                    Integer memberLevel = t.getMemberLevel();
                    //新增数据中status有值且memberLevel为空时
                    if (!(status != null && status.trim().length() > 0 && memberLevel == null)) {
                        insertCount += template.insert(mapperNamespace + ".insert", t);
                        specialSingleInsertData(t);
                    }else{
                        logger.info("【Sync:{}】4.0------------------------------ 【单条】放弃插入 memberId = {},filler2= {}, status = {},memberLevel ={}",tableName,t.getMemberId(),t.getFiller2(),status,memberLevel);
                    }
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

}
