package com.efuture.omdmain.component;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class SyncFrontService extends CommonServiceImpl<Object,SyncFrontService> {
    public SyncFrontService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    public static final Integer DEFUAL_PAGESIZE = 20;

    @Autowired
    private Environment environment;

    //重试
    public ServiceResponse retryFrontData(ServiceSession session, JSONObject paramsObject) throws Exception {
        Object tableNameParam = paramsObject.get("tableName");
        if(tableNameParam instanceof String){
            int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.AdvancedQueryMapper.updateRetryDoFlag",paramsObject);
            log.info("==============================>>>>> 【retryFrontFailData】------> doFlag 2 -> 0 表【{}】更新数量【{}】",tableNameParam,count);
            return syncFrontData(session,paramsObject);
        }else if(tableNameParam instanceof List){
            List<String> tableNames = (List<String>)paramsObject.get("tableName");
            if(tableNames!=null && !tableNames.isEmpty()){
                for(int i=0; i < tableNames.size();i++){
                    paramsObject.put("tableName",tableNames.get(i));
                    int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.AdvancedQueryMapper.updateRetryDoFlag",paramsObject);
                    log.info("==============================>>>>> 【retryFrontFailData】------> doFlag 2 -> 0 表【{}】更新数量【{}】",tableNames.get(i),count);
                    syncFrontData(session,paramsObject);
                }
            }
            return ServiceResponse.buildSuccess("已执行");
        }
        return ServiceResponse.buildFailure(session,ResponseCode.FAILURE,"tableName入参不正确:"+paramsObject);
    }

    public ServiceResponse syncUpsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.AdvancedQueryMapper.upsert",paramsObject);
        return ServiceResponse.buildSuccess(count);
    }

    public ServiceResponse syncDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deletedUniqueKeyBase",paramsObject);
        return ServiceResponse.buildSuccess(count);
    }

    //异步启动线程发送数据到前置机
    public void asyncSendFrontData(ServiceSession session, JSONObject paramsObject){
        CompletableFuture.runAsync(()->{
            try{
                log.info("==============================>>>>>【syncFrontData】同步开始 Start 【{}】",paramsObject.getString("tableName"));
                syncFrontData(session, paramsObject);
                log.info("==============================>>>>>【syncFrontData】同步结束 End 【{}】",paramsObject.getString("tableName"));
            }catch (Exception e){
                log.error("==============================>>>>>【syncFrontData】同步异常 Error 【{}】 \n {}",paramsObject.getString("tableName"),e.getMessage());
                e.printStackTrace();
            }
        });
    }

    //获取IDC同步前置机的数据 add by yihaitao 2024-06-22
    public ServiceResponse syncFrontData(ServiceSession session, JSONObject paramsObject) throws Exception {
//        System.out.println("========================== >>>>> "+Thread.currentThread().getId()+" --- "+Thread.currentThread().getName());
//        Thread.sleep(5000);
//        System.out.println("========================== >>>>> "+Thread.currentThread().getId()+" --- "+Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(3);
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"tableName");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

        String tableName = paramsObject.getString("tableName");
        Integer pageSize = paramsObject.getInteger("pageSize") == null ? DEFUAL_PAGESIZE : paramsObject.getInteger("pageSize");
        List<JSONObject> changeData = null;
        do{
            //查询增量数据的uniqueKey
            changeData = getChangeUniqueKey(tableName,pageSize);
            log.info("==============================>>>>>【syncFrontData】------> 增量表:{}_change, changeData.size = {}",tableName,changeData == null ? 0 : changeData.size());
            if (changeData == null || changeData.isEmpty()){
                return ServiceResponse.buildSuccess("no change data");
            }
            //获取表的主键和唯一索引字段
            List<String> uniqueKeyField = uniqueKeyField(tableName);
            if(uniqueKeyField == null || uniqueKeyField.isEmpty()){
                log.error("==============================>>>>>【syncFrontData】------> 同步的表没有主键或者唯一索引:{},",tableName);
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE,"同步的表没有主键或者唯一索引:"+tableName);
            }
            Map<String,JSONObject> changeMap = changeData.stream().collect(Collectors.toMap(uniqueSQLKeyValue(uniqueKeyField), p -> p));

            paramsObject = execSeleteOrDeleteParam(tableName,uniqueKeyField,changeData);
            //获取增量数据
            List<JSONObject> syncData = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.selectUniqueKeyBase",paramsObject);
            Map<String,JSONObject> syncMap = syncData.stream().collect(Collectors.toMap(uniqueSQLKeyValue(uniqueKeyField),p -> p));

            //删除uniqueKey
            Set<String> differenceKeys = changeMap.keySet().stream().filter(key ->!syncMap.containsKey(key)).collect(Collectors.toSet());
            //新增或更新uniqueKey
            Set<String> commonKeys = changeMap.keySet().stream().filter(syncMap::containsKey).collect(Collectors.toSet());

            String request = environment.getProperty("omdmain.front.request");
            //处理删除数据
            if(!differenceKeys.isEmpty()){
                List<JSONObject> differenceData = new ArrayList<>();
                differenceKeys.forEach(action->{
                    differenceData.add(changeMap.get(action));
                });
                paramsObject = execSeleteOrDeleteParam(tableName,uniqueKeyField,differenceData);
                String doFlag = sendFrontData(uniqueKeyField,paramsObject);
                List<Long> ids = differenceData.stream().map(action -> action.getLong("trigger_change_id")).collect(Collectors.toList());
                paramsObject.clear();
                paramsObject.put("tableName",tableName);
                paramsObject.put("list",ids);
                paramsObject.put("doFlag",doFlag);
                this.updateDoFlag(session,paramsObject);
            }
            //处理新增或者更新数据
            if(!commonKeys.isEmpty()){
                List<JSONObject> commonData = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                commonKeys.forEach(action->{
                    JSONObject data = syncMap.get(action);
                    Date createDate = data.getDate("createDate");
                    if(createDate!=null){
                      data.put("createDate",dateFormat.format(createDate));
                    }
                    Date updateDate = data.getDate("updateDate");
                    if(updateDate!=null){
                        data.put("updateDate",dateFormat.format(updateDate));
                    }
                    //通用框架无法解决，特殊处理：Time类型的处理
                    if("cateringtime".equals(tableName)){
                      specialData(data);
                    }
                    commonData.add(data);
                });
                paramsObject = execUpsertParam(tableName,uniqueKeyField,commonData);
                String doFlag = sendFrontData(uniqueKeyField,paramsObject);
                Map<String,JSONObject> commonMap = changeMap.entrySet().stream()
                        .filter(entry -> commonKeys.contains(entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                List<Long> ids = commonMap.values().stream().map(action->action.getLong("trigger_change_id")).collect(Collectors.toList());
                paramsObject.clear();
                paramsObject.put("tableName",tableName);
                paramsObject.put("list",ids);
                paramsObject.put("doFlag",doFlag);
                this.updateDoFlag(session,paramsObject);
            }

        }while (changeData!=null && changeData.size() == pageSize);

        return ServiceResponse.buildSuccess("sync success");
    }

    //特殊处理：通用框架无法处理
    private void specialData(JSONObject data) {
        if(data.getDate("startTime") !=null){
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = outputFormat.format(data.getDate("startTime"));
            data.put("startTime",formattedTime);
        }
        if(data.getDate("endTime") !=null){
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = outputFormat.format(data.getDate("endTime"));
            data.put("endTime",formattedTime);
        }
    }

    //返回标志位success：1表示数据发送前置机成功或者表示数据已处理。2表示发送前置机失败，或者部分前置机失败。3表示IDC没有配置前置机地址
    public String sendFrontData(List<String> uniqueKeyField,JSONObject paramsObject) {
        String tableName = paramsObject.getString("tableName");
        AtomicReference<String> success = new AtomicReference<>("1");
        List<JSONObject> fronts = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.selectFrontAddress",null);
        if(fronts == null || fronts.isEmpty()){
            log.error("============================>>>>>【sendFrontData】 前置机地址为空，请在syncfrontaddress配置前置机的地址");
            success.set("3");
            return success.get();
        }
        String request = environment.getProperty("omdmain.front.request");

        if(!uniqueKeyField.contains("shopCode")){//数据全门店同步
            for(int i = 0; i< fronts.size(); i ++){
                JSONObject front = fronts.get(i);
                String frontURL = request.replace("{frontIP}", front.getString("ip")).replace("{method}",paramsObject.getString("method"));
                log.info("============================>>>>>【sendFrontData】全shopCode:{}, tableName:{}, frontURL:{}",front.getString("shopCode"),tableName,frontURL);
                boolean requestFlag = httpRequestData(frontURL,paramsObject);
                if(!requestFlag){//简单处理：只要有一个门店处理失败就返回失败。后期可精细优化
                    success.set("2");
                }
            };
        }else{//区分门店数据同步
            Map<String,String> shopFrontMap = fronts.stream().collect(Collectors.toMap(action -> action.getString("shopCode"),action -> action.getString("ip")));
            List<JSONObject> data = (List<JSONObject>)paramsObject.get("list");
            Map<String,List<JSONObject>> shopDataMap = data.stream().collect(Collectors.groupingBy(action -> action.getString("shopCode")));
            shopDataMap.forEach((shopCode, shopData) -> {
                if(shopFrontMap.containsKey(shopCode)){
                    String method = paramsObject.getString("method");
                    String frontURL = request.replace("{frontIP}", shopFrontMap.get(shopCode)).replace("{method}",method);
                    log.info("============================>>>>> 【sendFrontData】单shopCode:{}, tableName:{}, frontURL:{}",shopCode,tableName,frontURL);
                    String syncMethod = method.split("\\.")[method.split("\\.").length-1];
                    JSONObject shopParam = null;
                    if("syncDelete".equals(syncMethod)){
                        shopParam = execSeleteOrDeleteParam(tableName,uniqueKeyField,shopData);
                    }else if("syncUpsert".equals(syncMethod)){
                        shopParam = execUpsertParam(tableName,uniqueKeyField,shopData);
                    }else{
                        log.error("============================>>>>>【sendFrontData】单shopCode:{}, tableName:{}, 方法没匹配成功【syncDelete、syncUpsert】 syncMethod:{}",shopCode,tableName,syncMethod);
                        return;
                    }
                    boolean requestFlag = httpRequestData(frontURL,shopParam);
                    if(!requestFlag){//简单处理：只要有一个门店处理失败就返回失败。后期可精细优化
                        success.set("2");
                    }
                }else{
                    log.error("============================>>>>>【sendFrontData】 发送的门店【单shopCode:{}, tableName:{}】数据没有匹配到门店前置机地址，如需发送请检查syncfrontaddress配置了该门店前置机地址",shopCode,tableName);
                }
            });
        }
        return success.get();
    }

    //调用HTTP请求，增加重试机制 add by yihaitao 2024-06-22
    public boolean httpRequestData(String frontURL, JSONObject paramsObject) {
        int maxRetries = 3;
        int retryCount = 0;
        boolean flag = false;
        int delay = 1000; // 初始延迟为1秒

        while (!flag && retryCount < maxRetries) {
            try {
                // 执行操作
                String frontResponse = HttpUtil.post(frontURL,paramsObject.toString());
                JSONObject responseData = JSONObject.parseObject(frontResponse);
                if(!"0".equals(responseData.getString("returncode"))){
                    log.error("============================>>>>>【httpRequestData】 请求失败。frontURL:{},\n 请求入参：{},\n 请求结果:{}",frontURL,paramsObject,responseData);
                    return flag;
                }
                // 如果成功，将flag设置为true
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("============================>>>>>【httpRequestData】 出现异常。retryCount:{},frontURL:{}",retryCount,frontURL);
                // 处理异常
                retryCount++;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                delay *= 2; // 延迟指数增加
            }
        }
        return flag;
    }

    //更新增量表状态 add by yihaitao 2024-06-22
    public ServiceResponse updateDoFlag(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateDoFlag",paramsObject);
        return ServiceResponse.buildSuccess(count);
    }

    //获取增量数据UniqueKey add by yihaitao 2024-06-22
    public List<JSONObject> getChangeUniqueKey(String tableName, Integer pageSize) {
        JSONObject param = new JSONObject() {{
            put("tableName", tableName);
            put("pageSize", pageSize);
        }};
        return this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.getChangeUniqueKey",param);
    }

    //封装唯一主键,拼接查询，更新，删除的批量SQL参数 add by yihaitao 2024-06-22
    public JSONObject execSeleteOrDeleteParam(String tableName, List<String> uniqueKeyField, List<JSONObject> execData) {
        return execSeleteOrDeleteParam(tableName,uniqueKeyField,execData,"omdmain.syncfront.syncDelete");
    }

    public JSONObject execSeleteOrDeleteParam(String tableName, List<String> uniqueKeyField, List<JSONObject> execData, String method) {
        return new JSONObject(){{
            put("tableName",tableName);
            put("uniqueKeyField",String.join(",", uniqueKeyField));
            put("list", execData);
            put("keys", mybatisKeyValue(uniqueKeyField));
            put("method", method);
        }};
    }

    public JSONObject execUpsertParam(String tableName, List<String> uniqueKeyField, List<JSONObject> execData) {
        return execUpsertParam(tableName,uniqueKeyField,execData,"omdmain.syncfront.syncUpsert");
    }

    public JSONObject execUpsertParam(String tableName, List<String> uniqueKeyField, List<JSONObject> execData, String method) {
        return new JSONObject(){{
            put("tableName",tableName);
            put("uniqueKeyField",String.join(",", uniqueKeyField));
            put("list", execData);
            put("method", method);
            Set<String> fields = new HashSet<>();
            for (int i = 0; i < execData.size(); i++) {
                JSONObject jsonObject = execData.get(i);
                fields.addAll(jsonObject.keySet());
            }
            put("fields", String.join(",", fields));
            put("keys", mybatisKeyValue(new ArrayList<>(fields)));
        }};
    }

    //获取表的UniqueKey: ['pCode','erpCode','entId'] add by yihaitao 2024-06-22
    public List<String> uniqueKeyField (String tableName){
        JSONObject param = new JSONObject() {{
            put("tableName", tableName);
        }};
//        List<String> uniqueKeyField = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.uniqueKeyField",param);
//        if(uniqueKeyField == null || uniqueKeyField.isEmpty()){
//            uniqueKeyField = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.primaryKey",param);
//        }
        return this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.AdvancedQueryMapper.uniqueKeyField",param);
    }

    //获取唯一主键的值 add by yihaitao 2024-06-22
    public Function<JSONObject,String> uniqueSQLKeyValue(List<String> keys) {
        return json -> {
            StringBuffer uniqueKeyValue = new StringBuffer();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (i != 0) {
                    uniqueKeyValue.append(",");
                }
                uniqueKeyValue.append(json.getString(key));
            }
            return uniqueKeyValue.toString();
        };
    }

    //MyBatis拼接多个Key的Value:#{item.pCode}","#{item.erpCode}","#{item.entId} add by yihaitao 2024-06-22
    public List<String> mybatisKeyValue (List <String> uniqueKeyField) {
        List<String> forList = new ArrayList<>();
        uniqueKeyField.forEach(action -> {
            StringBuffer sb = new StringBuffer();
            sb.append("#{item.").append(action).append("}");
            forList.add(sb.toString());
        });
        return forList;
    }

}
