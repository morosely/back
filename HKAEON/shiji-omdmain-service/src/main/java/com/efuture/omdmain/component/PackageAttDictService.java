package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.PackageAttDict;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PackageAttDictService extends CommonServiceImpl<PackageAttDict,PackageAttDictService> {
    public PackageAttDictService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    @Autowired
    private  SyncFrontService syncFrontService;

    //批量修改排序号
    public ServiceResponse updateOrderNum(ServiceSession session, JSONObject paramsObject) throws Exception {
        //1.更新
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttDictMapper.updateBatchSelective",paramsObject);
        //2.同步前置
        String tableName = this.getCollectionName();
        syncFrontService.asyncSendFrontData(session,new JSONObject(){{
            put("tableName",tableName);
        }});
       //3.返回数据
        paramsObject.clear();
        paramsObject.put(this.getCollectionName(),count);
        return ServiceResponse.buildSuccess(paramsObject);
    }


    //属性值的删除
    public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"id");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

        Long id = paramsObject.getLong("id");
        paramsObject.clear();
        paramsObject.put("id",id);
        ServiceResponse response = this.onDelete(session,paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return response;
    }

    //属性值的新增或更新
    public ServiceResponse upsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"dEnName","pCode","erpCode","dName","orderNum");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
        ServiceResponse response = null;
        Long id = paramsObject.getLong("id");
        Date date = new Date();
        if (id == null){//新增
            id = UniqueID.getUniqueID(true);
            paramsObject.put("id",id);
            paramsObject.put("dCode",id);
            paramsObject.put("creator",session.getUser_code());
            paramsObject.put("createDate",date);
            response = this.onInsert(session,paramsObject);
        }else{//更新
            paramsObject.put("id",id);//解决id是字符串类型基类无法更新更新
            paramsObject.put("modifier",session.getUser_code());
            paramsObject.put("updateDate",date);
            paramsObject.put("dCode",id);
            response = this.onUpdate(session,paramsObject);
        }
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return response;
    }

    public ServiceResponse syncOneDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttDictMapper.delete",paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return ServiceResponse.buildSuccess(count);
    }
//    private static final AtomicLong INCREMENTER = new AtomicLong();
//    public static String generateCode() {
//        long incrementedValue = INCREMENTER.incrementAndGet();
//        return "D" + String.valueOf(System.currentTimeMillis()) +" - "+ incrementedValue;
//    }

}
