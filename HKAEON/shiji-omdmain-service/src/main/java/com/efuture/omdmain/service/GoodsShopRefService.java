package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
* @author yihaitao
* @time 2018年5月18日 上午11:18:06 
* 
*/
public interface GoodsShopRefService {

	public ServiceResponse goodsCodeDC(ServiceSession session, JSONObject paramsObject);
    /*
    * @Description:  箱码经营配置插入
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse goodsInsert(ServiceSession session, JSONObject paramsObject);

    /*
    * @Description: 箱码经营配置删除
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse goodsDelete(ServiceSession session, JSONObject paramsObject);

    /*
    * @Description: 箱码经营配置修改
    * @param session
    * @param paramsObject
    * @return: com.product.model.ServiceResponse
    */
    public ServiceResponse goodsSetting(ServiceSession session, JSONObject paramsObject);

    /*
     * @Description: 经营配置查询
     * @param session
     * @param paramsObject
     * @return: com.product.model.ServiceResponse
     */
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject);
}
