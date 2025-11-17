package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.PartyTrayGoodsPropertiesModel;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class PartyTrayGoodsPropertiesServiceImpl extends CommonServiceImpl<PartyTrayGoodsPropertiesModel, PartyTrayGoodsPropertiesServiceImpl> {

	public PartyTrayGoodsPropertiesServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName,String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	//查询商品PartyTrayGoods属性
	@SuppressWarnings("unchecked")
	public ServiceResponse queryProperties(ServiceSession session, JSONObject paramsObject) throws Exception{
		//1.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"sgid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		//2.查询属性
		Criteria criteria = Criteria.where("sgid").is(paramsObject.get("sgid"));
		Query query = new Query(criteria);
	    List<PartyTrayGoodsPropertiesModel> list = this.getTemplate().select(query, PartyTrayGoodsPropertiesModel.class, "partytraygoodsproperties");
	    paramsObject.clear();
	    paramsObject.put(this.getCollectionName(), list);
	    @SuppressWarnings("rawtypes")
		Set proGroupSet = list.stream().map(PartyTrayGoodsPropertiesModel::getProGroup).collect(Collectors.toSet());
	    List<JSONObject> proGroupsList = new ArrayList<JSONObject>();
	    proGroupSet.forEach(proGroup->{
	    	JSONObject jo = new JSONObject();
	    	jo.put("proGroup", proGroup);
	    	proGroupsList.add(jo);
	      });
	    paramsObject.put("proGroups",proGroupsList);
	    return ServiceResponse.buildSuccess(paramsObject);
	}
	
	//保存PartyTrayGoods属性
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResponse saveProperties(ServiceSession session, JSONObject paramsObject) throws Exception{
		//0.校验
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"sgid","goodsCode","erpCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;	
		Long sgid = paramsObject.getLong("sgid");
		String goodsCode =  paramsObject.getString("goodsCode");
		String erpCode = paramsObject.getString("erpCode");
	
		//1.删除以前旧数据
		paramsObject.put("table","partytraygoodsproperties");
		paramsObject.put("key","sgid");
		paramsObject.put("values",Arrays.asList(sgid));
		this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
		paramsObject.remove("table");
		paramsObject.remove("key");
		paramsObject.remove("values");
		
		//2.插入新数据
		JSONArray array = paramsObject.getJSONArray(this.getCollectionName());
		if(array!=null && !array.isEmpty()){
			Long entId = session.getEnt_id();
			for(int i=0;i<array.size();i++){
				array.getJSONObject(i).put("entId", entId);
				array.getJSONObject(i).put("goodsCode", goodsCode);
				array.getJSONObject(i).put("erpCode", erpCode);
				array.getJSONObject(i).put("sgid", sgid);
			}
			return this.onInsert(session,paramsObject);
		}
	    return ServiceResponse.buildSuccess(sgid);
	}
}
