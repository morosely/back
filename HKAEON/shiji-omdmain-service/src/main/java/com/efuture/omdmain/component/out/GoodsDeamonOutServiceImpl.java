package com.efuture.omdmain.component.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.out.GoodsOutBakModel;
import com.efuture.omdmain.service.out.GoodsShopRefDeamonOutService;
import com.efuture.omdmain.utils.DefaultParametersUtils;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;


public class GoodsDeamonOutServiceImpl extends JDBCCompomentServiceImpl<GoodsOutBakModel> implements  GoodsShopRefDeamonOutService{

	
	public GoodsDeamonOutServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
	
	@SuppressWarnings("rawtypes")
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		DefaultParametersUtils.transformParam(session, paramsObject);
		FMybatisTemplate template = this.getTemplate();
		IntoParam(paramsObject,"item_code","goodsCode");
		IntoParam(paramsObject,"barcode","barNo");
		if(paramsObject.containsKey("item_name")){
			Object o = paramsObject.get("item_name");
			if(o instanceof JSONObject){
				Set<String> ks = ((JSONObject) o).keySet();
				for (String k : ks) {
					if(k.equalsIgnoreCase("like")){
						Object v = ((JSONObject) o).get(k);
						paramsObject.put("goodsName", v);
						paramsObject.remove("item_name");
					}
				}
			}
		}
		
		JSONObject result = new JSONObject();
		List<Map> list = new ArrayList<Map>();
		long startTime = new Date().getTime();
//		System.out.println("start GoodsDeamonOutServiceImpl -------> " + new Date().getTime());
		long count = template.getSqlSessionTemplate().selectOne("beanmapper.out.GoodsDeamonOutMapper.count",paramsObject);
		if(count!=0L){
			list = template.getSqlSessionTemplate().selectList("beanmapper.out.GoodsDeamonOutMapper.search",paramsObject);
		}else{
			result.put("goods", list);
			result.put("total_results", 0);
			return ServiceResponse.buildSuccess(result);
		}
		long endTime = new Date().getTime();
//		System.out.println("end   GoodsDeamonOutServiceImpl -------> " + new Date().getTime());
		System.out.println("GoodsDeamonOutServiceImpl search Time consuming-------> " + (endTime-startTime));
		result.put("goods", list);
		result.put("total_results", count);
		
		return ServiceResponse.buildSuccess(result);
	}
	
	public void IntoParam(JSONObject paramsObject , String inParam,String outParam){
			if(paramsObject.containsKey(inParam)){
				paramsObject.put(outParam, paramsObject.get(inParam).toString().split(","));
				paramsObject.remove(inParam);
			}
	}
	
	
	
}
