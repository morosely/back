package com.efuture.omdmain.service;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

/**
 * Created by huangzhengwei on 2018/5/7.
 *
 * @Desciption:
 */
public interface SaleGoodsService2 {

    //    商品步长设置查询
    public ServiceResponse goodsStepSizeSearch(ServiceSession session, JSONObject paramsObject);

    //   商品步长设置修改
    public ServiceResponse goodsStepSizeUpdate(ServiceSession session, JSONObject paramsObject);

    //   商品步长设置新增
    public ServiceResponse goodsStepSizeAdd(ServiceSession session, JSONObject paramsObject);
    //商品列表查询
    public ServiceResponse goodsListSearch(ServiceSession session, JSONObject paramsObject);

    //  商品生鲜等级，箱码， 分割明细查询 (查询母品id和商品类型查询)
    public ServiceResponse goodsDetailsSearch(ServiceSession session, JSONObject paramsObject);
    //  商品生鲜等级 商品分割  新增
    public ServiceResponse goodsDetailAdd(ServiceSession session, JSONObject paramsObject);
    //  商品生鲜等级 商品分割  修改
    public ServiceResponse goodsDetailUpdate(ServiceSession session, JSONObject paramsObject);
    //  商品生鲜等级 商品分割  删除
    public ServiceResponse goodsDetaildelete(ServiceSession session, JSONObject paramsObject);
    //    渠道商品维护根据id 查询
//    public ServiceResponse channelGoodsGet(ServiceSession session, JSONObject paramsObject);
    //    渠道商品维护根据id 修改
//    public ServiceResponse channelGoodsUpdate(ServiceSession session, JSONObject paramsObject);

    public ServiceResponse goodsDetailsDelete(ServiceSession session, JSONObject paramsObject);

    public ServiceResponse goodsSettingSearch(ServiceSession session, JSONObject paramsObject);

//    子品查询
    public ServiceResponse subGoodsSearch(ServiceSession session, JSONObject paramsObject);

//    子品经营配置
    public ServiceResponse subGoodsSetting(ServiceSession session, JSONObject paramsObject);

    public  ServiceResponse moreCodeGoodsSave (ServiceSession session, JSONObject paramsObject) throws Exception;

    /**
     * 查询所有箱装商品
     */
	public ServiceResponse searchAllXzsp(ServiceSession session, JSONObject paramsObject) throws Exception;

	/**
	 * 查询所有分割商品
	 */
	public ServiceResponse searchAllFgsp(ServiceSession session, JSONObject paramsObject);




}
