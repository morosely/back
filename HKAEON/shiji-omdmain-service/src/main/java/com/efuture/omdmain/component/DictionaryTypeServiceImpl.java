package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.product.component.CommonServiceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.DictionaryDataModel;
import com.efuture.omdmain.model.DictionaryModel;
import com.efuture.omdmain.model.DictionaryTypeModel;
import com.efuture.omdmain.service.DictionaryTypeService;
import com.mongodb.DBObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/** 
* @author yihaitao
* @time 2018年5月15日 下午1:50:47 
* 字典类别
*/
public class DictionaryTypeServiceImpl extends CommonServiceImpl<DictionaryTypeModel,DictionaryTypeServiceImpl> implements DictionaryTypeService{

	public DictionaryTypeServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	
	}
	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
	
	
	@Override
	public ServiceResponse searchAllDictData(ServiceSession session, JSONObject paramsObject) throws Exception {
		FMybatisTemplate template = this.getTemplate();
		ServiceResponse response = super.onQuery(session, paramsObject);
		if(response.getReturncode() != null && response.getReturncode().equals("0")){
			JSONObject data =  JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
			List<DictionaryTypeModel> dictTypeList = JSONArray.parseArray(JSONObject.toJSONString(data.get(this.getCollectionName())), DictionaryTypeModel.class);
			List<DictionaryModel> dictList = null;
			List<DictionaryDataModel> dictDataList = null;
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			for(DictionaryTypeModel dictType : dictTypeList){
				Map<String, Object> dictMap = new HashMap<String,Object>();//字典类型表头
				dictMap.put("dictTypeCode", dictType.getDictTypeCode());//字典类别CODE
				dictMap.put("dictTypeName", dictType.getDictTypeName());//字典类别名称
				dictMap.put("dtid", dictType.getDtid());//字典类别ID
				resultList.add(dictMap);
				
				Criteria criteria = Criteria.where("dictTypeCode").is(dictType.getDictTypeCode());
				Query query = new Query(criteria);
				query.with(new Sort(new Order(Direction.DESC,"createDate")));
				query.limit(Integer.MAX_VALUE);
				dictList = template.select(query, DictionaryModel.class, "dictionary");
				for(DictionaryModel dict : dictList){
					dictMap = new HashMap<String,Object>();
					dictMap.put("cnName", dict.getCnName());//字典中文名称
					dictMap.put("dictCode", dict.getDictCode());//dictCode
					dictMap.put("dictId", dict.getDictId());//字典ID
					dictMap.put("systemFlag", dict.getSystemFlag());//是否系统级
					resultList.add(dictMap);
					
					criteria = Criteria.where("dictId").is(dict.getDictId());
					query = new Query(criteria);
					query.with(new Sort(new Order(Direction.DESC,"createDate")));
					query.limit(Integer.MAX_VALUE);
					dictDataList = template.select(query, DictionaryDataModel.class, "dictionarydata");
					for(DictionaryDataModel dictData : dictDataList){
						dictMap = new HashMap<String,Object>();
						dictMap.put("dictDataCode", dictData.getDictDataCode());//字典数据代码
						dictMap.put("dictDataId", dictData.getDictDataId());//字典数据ID
						dictMap.put("dictDatacnname", dictData.getDictDatacnname());//字典数据中文名称
						dictMap.put("dictDatacnname", dictData.getDictDatacnname());//字典数据中文名称
						dictMap.put("remark", dictData.getRemark());//描述
						dictMap.put("status", dictData.getStatus());//状态
						resultList.add(dictMap);
					}
				}
			}
			JSONObject result = new JSONObject();
  		    result.put(this.getCollectionName(), resultList);
  		    result.put("total_results", data.get("total_results"));
  		    return ServiceResponse.buildSuccess(result);
		}
		return response;
	}

}
