package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface SaleGoodsService {

  public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;

  // 可选商品列表
  public ServiceResponse selectiveGoods(ServiceSession session, JSONObject paramsObject)
      throws Exception;

  ServiceResponse saveMeal(ServiceSession session, JSONObject paramsObject) throws Exception;

  ServiceResponse addMeal(ServiceSession session, JSONObject paramsObject) throws Exception;

  ServiceResponse searchMealDetail(ServiceSession session, JSONObject paramsObject) throws Exception;

  ServiceResponse enableOrDisableMeal(ServiceSession session, JSONObject paramsObject);

  ServiceResponse searcheSaleGoodsDetails(ServiceSession session, JSONObject paramsObject) throws Exception;

  ServiceResponse searchByState(ServiceSession session, JSONObject paramsObject) throws Exception;

  ServiceResponse search4OnlineChanel(ServiceSession session, JSONObject paramsObject);
  
  /**
   * 查询所有套餐及其明细
   */
  ServiceResponse searchAllMealDetail(ServiceSession session, JSONObject paramsObject);
  
  //子品经营配置 - 商品查询列表
  public ServiceResponse searchGoods(ServiceSession session, JSONObject paramsObject);
  //子品经营配置 - 母品查询子品
  public ServiceResponse searchSonGoodsByParentCode(ServiceSession session, JSONObject paramsObject);
  //子品经营配置 - 配置经营配置（保存）
  public ServiceResponse saveSonGoodsRef(ServiceSession session, JSONObject paramsObject);
  
  /**
   * 查询所有组包码商品
   */
  ServiceResponse searchAllZbmsp(ServiceSession session, JSONObject paramsObject);
  /**
   * 查询所有虚拟母品
   */
  ServiceResponse searchAllXnmp(ServiceSession session, JSONObject paramsObject);
  /**
   * 查询所有plu商品
   */
  ServiceResponse searchAllPlusp(ServiceSession session, JSONObject paramsObject) throws Exception;
  
  public ServiceResponse searchSaleGoodsByUniqueCondition(ServiceSession session, JSONObject paramsObject);
  
}
