package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.PackageAttGoodsRef;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PackageAttGoodsRefService extends CommonServiceImpl<PackageAttGoodsRef,PackageAttGoodsRefService> {
    public PackageAttGoodsRefService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    //属性商品删除
    public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"id");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

        Object paramId = paramsObject.get("id");
        if(paramId instanceof List){//批量删除
            List ids = paramsObject.getJSONArray("id");
            ids.forEach(id -> {
                paramsObject.clear();
                paramsObject.put("id",Long.valueOf(id.toString()));
                this.onDelete(session,paramsObject);
            });
        }else{//单个删除
            paramsObject.clear();
            paramsObject.put("id",paramId);
            this.onDelete(session,paramsObject);
        }
        paramsObject.clear();
        paramsObject.put("id",paramId);
        return ServiceResponse.buildSuccess(paramsObject);
    }

    //属性商品新增或更改
    public ServiceResponse upsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        //校验必填字段
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"pCode","goodsCodes","erpCode");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
        Date date = new Date();

        String pCode = paramsObject.getString("pCode");
        String erpCode = paramsObject.getString("erpCode");
        JSONArray array = paramsObject.getJSONArray("goodsCodes");
        List<JSONObject> list = new ArrayList<>();
        for(int i=0; i<array.size(); i++){
            JSONObject data = new JSONObject();
            String goodsCode = array.getString(i);
            data.put("id", UniqueID.getUniqueID(true));
            data.put("pCode",pCode);
            data.put("status",1);
            data.put("erpCode",erpCode);
            data.put("entId",session.getEnt_id());
            data.put("goodsCode",goodsCode);
            data.put("creator",session.getUser_code());
            data.put("createDate",date);
            data.put("modifier",session.getUser_code());
            data.put("updateDate",date);
            list.add(data);
        }
        paramsObject.clear();
        paramsObject.put("list",list);
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttGoodsRefMapper.upsert",paramsObject);
        return ServiceResponse.buildSuccess(count);
    }

    public ServiceResponse syncUpsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttGoodsRefMapper.upsert",paramsObject);
        return ServiceResponse.buildSuccess(count);
    }

    public ServiceResponse syncDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
        //int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttGoodsRefMapper.delete",paramsObject);
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deletedUniqueKeyBase",paramsObject);
        return ServiceResponse.buildSuccess(count);
    }
}
