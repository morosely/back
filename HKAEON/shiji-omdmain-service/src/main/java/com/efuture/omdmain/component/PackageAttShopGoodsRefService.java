package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.PackageAttShopGoodsRef;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PackageAttShopGoodsRefService extends CommonServiceImpl<PackageAttShopGoodsRef,PackageAttShopGoodsRefService> {
    public PackageAttShopGoodsRefService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }
    @Autowired
    private  SyncFrontService syncFrontService;

    //属性与门店商品关系的新增或更改
    public ServiceResponse upsert(ServiceSession session, JSONObject paramsObject) throws Exception {
        JSONArray array = paramsObject.getJSONArray("saleGoods");
        Date date = new Date();
        for(int i=0; i<array.size();i++){
            JSONObject saleGoods = array.getJSONObject(i);

            ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, saleGoods,"goodsCode","barNo","erpCode","shopCode","stallCode","entId");
            if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;

            saleGoods.put("id",UniqueID.getUniqueID(true));
            saleGoods.put("creator",session.getUser_code());
            saleGoods.put("createDate",date);
            saleGoods.put("modifier",session.getUser_code());
            saleGoods.put("updateDate",date);
            saleGoods.put("status",1);
        }

        paramsObject.clear();
        paramsObject.put("list",array);
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.PackageAttShopGoodsRefMapper.upsert",paramsObject);
        ServiceResponse response = ServiceResponse.buildSuccess(count);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return response;
    }

    public ServiceResponse syncOneDelete(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttShopGoodsRefMapper.delete",paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return ServiceResponse.buildSuccess(count);
    }

    public ServiceResponse syncDeleteBatch(ServiceSession session, JSONObject paramsObject) throws Exception {
        int count = this.getTemplate().getSqlSessionTemplate().delete("beanmapper.PackageAttShopGoodsRefMapper.deleteBatch",paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return ServiceResponse.buildSuccess(count);
    }
}
