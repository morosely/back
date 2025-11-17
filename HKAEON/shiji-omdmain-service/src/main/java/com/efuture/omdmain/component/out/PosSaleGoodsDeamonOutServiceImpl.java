package com.efuture.omdmain.component.out;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.PosSaleGoodsOutModel;
import com.efuture.omdmain.service.out.PosSaleGoodsDeamonOutService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.service.AnnotationService;
import com.product.service.OperationFlag;
import com.product.storage.template.FMybatisTemplate;

public class PosSaleGoodsDeamonOutServiceImpl extends CommonServiceImpl<PosSaleGoodsOutModel,PosSaleGoodsDeamonOutServiceImpl> implements PosSaleGoodsDeamonOutService{

	public PosSaleGoodsDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	//查询(电子秤资料更新管理)
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
	
	//查询（打印副单）
	@Override
	public ServiceResponse query(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.校验
		ServiceResponse response = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "shopId","page_no","page_size");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(response.getReturncode())) return response;
		//2.设置分页参数
		DefaultParametersUtils.addSplitPageParams(paramsObject);
		//3.关联查询
		Long total_results = this.getTemplate().getSqlSessionTemplate().selectOne("beanmapper.PosSaleGoodsOutModelMapper.countSaleGoodsAndProperty", paramsObject);
		List<PosSaleGoodsOutModel> list = null;
		List<RowMap> jsonList = null;
		if(total_results != null && total_results > 0 ){
			list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.PosSaleGoodsOutModelMapper.querySaleGoodsAndProperty", paramsObject);
			JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
		    jsonList = JSONObject.parseArray(array.toJSONString(), RowMap.class);
			// 执行查询插件处理 钱海兵
			List<AnnotationService> tagPlugins = this.getPlugins();
			for (AnnotationService plugin : tagPlugins) {
				try {
					plugin.onAction(session, jsonList, OperationFlag.afterQuery, this.getBeanClass());
				} catch (Exception e) {
					return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "查询前插件执行异常:{0}",e.getMessage() + "");
				}
		    }
		}
		//4.封装结果返回
		paramsObject.clear();
		paramsObject.put("salegoods", jsonList);
		paramsObject.put("total_results", total_results);
		return ServiceResponse.buildSuccess(paramsObject);
	}

	//更新（打印副单）
	@Override
	public ServiceResponse updatePrtDuplFalg(ServiceSession session, JSONObject paramsObject) throws Exception {
		JSONArray array = (JSONArray)paramsObject.get(this.getCollectionName());
		Date now = new Date();
		for (Object object : array) {
			JSONObject obj = (JSONObject)object;
			obj.put("updateDate", now);
		}
		paramsObject.put(this.getCollectionName(), array);
		return this.wrapUpdate(session, paramsObject);
	}

	
}
