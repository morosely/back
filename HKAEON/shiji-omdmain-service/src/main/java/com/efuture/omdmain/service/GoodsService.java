package com.efuture.omdmain.service;


import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public interface GoodsService {
	//导入行送商品
	public ServiceResponse importDelivery(ServiceSession session, String params, MultipartFile file) throws Exception;
	//校验通过后，进行数据处理
	public ServiceResponse saveImportDelivery(ServiceSession session, JSONObject paramsObject) throws Exception;
	//行送商品创建导出模版
	public ServiceResponse createExlModel(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	// 商品管理 / 商品资料维护-列表查询
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception;

	// 商品管理 / 商品资料维护-详情查询
	public ServiceResponse get(ServiceSession session, JSONObject paramsObject) throws Exception;

	// 商品管理 / 商品资料维护-编辑
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception;

	//生成子品：显示属性模板
	public ServiceResponse showCategoryProperty(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	// 生成子品：点击下一步的操作
	public ServiceResponse next(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	// 保存子品
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//删除子品，和对应的商品扩展属性
	public ServiceResponse batchDelete(ServiceSession session, JSONObject paramsObject) throws Exception;
	
	//模糊查询
    public ServiceResponse search4Like(ServiceSession session, JSONObject paramsObject) throws Exception;
    
    //属性模板属性值修改
  	public ServiceResponse updateProValue(ServiceSession session, JSONObject paramsObject) throws Exception;
  	
    //通过子品多规格属性值查询子品信息
  	public ServiceResponse searchSonGoods(ServiceSession session, JSONObject paramsObject) throws Exception;
  	
  	//修改子品价格接口
  	public ServiceResponse updateSonGoods(ServiceSession session, JSONObject paramsObject) throws Exception;

  	/**
  	 * 查询所有档口商品加工方法
  	 */
	ServiceResponse searchAllGoodsProcess(ServiceSession session, JSONObject paramsObject);

	/**
	 * 查询所有箱装商品
	 */
	ServiceResponse searchAllXzsp(ServiceSession session, JSONObject paramsObject) throws Exception;

	/**
	 * 查询所有子品
	 */
	ServiceResponse searchAllZp(ServiceSession session, JSONObject paramsObject);

}
