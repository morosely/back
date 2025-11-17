package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.AeonCitySaleGoodsModel;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

import java.util.Date;

public class AeonCitySaleGoodsService extends CommonServiceImpl<AeonCitySaleGoodsModel,AeonCitySaleGoodsService> {
    public AeonCitySaleGoodsService(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    //商品API说明
    public ServiceResponse queryGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"shopCode");
        if (ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
        Date searchDate = paramsObject.getDate("searchDate");
        if(paramsObject.getDate("searchDate") != null){
            JSONObject dateParam = new JSONObject();
            dateParam.put("<=",new Date());
            dateParam.put(">=",searchDate);
            paramsObject.put("updateDate",dateParam);
        }
        paramsObject.put("fields","goodsCode,shopCode,barNo,orgCode,salePrice,goodsName,categoryCode,brandCode,erpCode,goodsStatus,coldTransFlag,enSname");
        return this.onQuery(session,paramsObject);
    }
}
