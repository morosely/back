package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.SaleGoodsProperty;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SaleGoodsPropertyService extends CommonServiceImpl<SaleGoodsProperty,SaleGoodsPropertyService> {
    public SaleGoodsPropertyService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    @Autowired
    private  SyncFrontService syncFrontService;
    //设置售罄标识
    public ServiceResponse sellout(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse validateResult = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "shopCode","stallCode","goodsCode","barNo","erpCode");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(validateResult.getReturncode())) return validateResult;

        Date date = new Date();
        paramsObject.putIfAbsent("id", UniqueID.getUniqueID(true));
        paramsObject.putIfAbsent("sellout", "0");
        paramsObject.put("creator",session.getUser_code());
        paramsObject.put("createDate",date);
        paramsObject.put("modifier",session.getUser_code());
        paramsObject.put("updateDate",date);
        paramsObject.put("entId",session.getEnt_id());
        int count = this.getTemplate().getSqlSessionTemplate().insert("beanmapper.SaleGoodsPropertyMapper.insertOrUpdate",paramsObject);
        paramsObject.clear();
        paramsObject.put(this.getCollectionName(),count);
        ServiceResponse response = ServiceResponse.buildSuccess(paramsObject);
        paramsObject.put("tableName",this.getCollectionName());
        syncFrontService.asyncSendFrontData(session,paramsObject);
        return response;
    }
}
