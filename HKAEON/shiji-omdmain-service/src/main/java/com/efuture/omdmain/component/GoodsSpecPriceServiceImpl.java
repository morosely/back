package com.efuture.omdmain.component;

import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsSpecPriceModel;
import com.efuture.omdmain.service.GoodsSpecPriceService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class GoodsSpecPriceServiceImpl extends JDBCCompomentServiceImpl<GoodsSpecPriceModel> implements GoodsSpecPriceService {

  public GoodsSpecPriceServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
    super(mybatisTemplate,collectionName, keyfieldName);
  }

//  @Override
//  protected FMybatisTemplate getTemplate() {
//      return this.getBean("StorageOperation", FMybatisTemplate.class);
//  }

  @Override
  protected DBObject onBeforeRowInsert(Query query, Update update) {
    return this.onDefaultRowInsert(query, update);
  }

  
  /**
   * @Title: 			searchPLUBySGID
   * @Description: 	TODO
   * @param: 			@param session
   * @param: 			@param paramsObject
   * @param: 			@return   
   * @return: 		ServiceResponse   
   * @throws
   */
  public ServiceResponse searchPLUBySGID(ServiceSession session, JSONObject paramsObject) {
    if(!paramsObject.containsKey("entId")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "entId不能为空.");
    }
    if(!paramsObject.containsKey("erpCode")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "erpCode不能为空.");
    }
    if(!paramsObject.containsKey("codeType")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "codeType不能为空.");
    }
    if(!paramsObject.containsKey("sgid")) {
      return  ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "sgid不能为空.");
    }
    
    ServiceResponse res = this.onQuery(session, paramsObject);
    JSONObject data =  (JSONObject)res.getData();
    JSONArray array = data.getJSONArray(this.getCollectionName());
    long total_results = data.getLongValue("total_results");
    for(int i = 0; array != null && i < array.size(); i++) {
      JSONObject obj = array.getJSONObject(i);
      String goodsName = obj.getString("goodsName") == null ? "" : obj.getString("goodsName").toString();
      String pluName = goodsName + obj.getString("sortLevel").toString();
      obj.put("pluName", pluName);
    }
    
    JSONObject result = new JSONObject();
    result.put("total_results", total_results);
    result.put(this.getCollectionName(), array);
    return ServiceResponse.buildSuccess(result);
  }
}
