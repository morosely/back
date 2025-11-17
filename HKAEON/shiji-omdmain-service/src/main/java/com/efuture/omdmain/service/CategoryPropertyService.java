package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/3.
 *
 * @Desciption: 查询工业分类的属性
 */
public interface CategoryPropertyService {

    /*
    * @Description: 根据工业分类的id分类属性
     * @param session
   * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject);

    /* 
    * @Description: 批量的插入 ，先查询工业表分类表，先把所有的子节点查询出来，然后联合所有的子节点和参数进行插入
     * @param session
 * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse bulkInsert(ServiceSession session, JSONObject paramsObject);
    
    /* 
    * @Description: 启用/停用， 根据属性编码，查询出所有工业分类的id然后批量的更新 
     * @param session
 * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse bulkUpdate(ServiceSession session, JSONObject paramsObject);
    

}
