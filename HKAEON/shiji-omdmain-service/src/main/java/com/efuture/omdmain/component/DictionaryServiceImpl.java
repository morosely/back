package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.product.component.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.DictionaryDataModel;
import com.efuture.omdmain.model.DictionaryModel;
import com.efuture.omdmain.model.DictionaryTypeModel;
import com.efuture.omdmain.service.DictionaryService;
import com.mongodb.DBObject;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.FileProcessorUtils;

public class DictionaryServiceImpl extends CommonServiceImpl<DictionaryModel,DictionaryServiceImpl> implements DictionaryService {

	@Autowired
	private DictionaryDataServiceImpl dictionaryDataServiceImpl;
	
	public DictionaryServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}
	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	//分类字典-列表查询
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
	
	
	//分类字典-编辑
	@Override
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		return super.wrapUpdate(session, paramsObject);
	}

	//批量增加
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		return super.wrapInsert(session, paramsObject);
	}

	//删除
	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null)
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject))
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if (!paramsObject.containsKey(getCollectionName())) {
			if(!paramsObject.containsKey(getKeyfieldName())){
				return ServiceResponse.buildFailure(session, ResponseCode.Exception.PRIMARY_IS_EMPTY);
			}
			
			Criteria criteria = Criteria.where(getKeyfieldName()).is(paramsObject.get(getKeyfieldName())).and("entId")
					.is(session.getEnt_id());
			Query query = new Query(criteria);
			DictionaryModel bean = this.onFindOne(session, this.getTemplate(), query, DictionaryModel.class,
					this.getCollectionName());
			
			if(bean==null){
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.NOT_EXIST);
			}
			
			if(bean.getSystemFlag()){//是否系统级 1:是，0:否
				return ServiceResponse.buildFailure(session, ResponseCode.Failure.FAIL_DELETE,"系统参数不能删除该信息！");
			}
		}
		
		return this.onDelete(session, paramsObject);
	}
	
	
	@Override
    public List<Map<String, Object>> onBeforeImportData(String params, MultipartFile file) throws Exception{
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> dataMap = new HashMap<String,Object>(); 
		JSONObject jsonparam = JSONObject.parseObject(params);
		String type = jsonparam.getString("type");
		if (type.equals("excel")) {
			if (file == null) {
				throw new Exception("缺少上传文件");
			}
			dataMap.put("dictionary", FileProcessorUtils.parseFile(jsonparam, file.getInputStream()));
			jsonparam.put("sheetIndex", 1);
			jsonparam.put("cols", "entId,dictCode,dictDataCode,dictDataenname,dictDatacnname,status,orderNum,levelNum,leafFlag,parentId,systemFlag");
			dataMap.put("dictionaryData", FileProcessorUtils.parseFile(jsonparam, file.getInputStream()));
			dataList.add(dataMap);
		}
		return dataList;
	}
	
	@Transactional
	@Override
	public ServiceResponse onImportData(ServiceSession session, String params, MultipartFile file) throws Exception {
		ServiceResponse response = null;
		  List<Map<String, Object>> dataList = null;
		  FMybatisTemplate template = this.getTemplate();
		  try{
			  dataList = onBeforeImportData(params, file);
		  }catch(Exception e){
			  response = new ServiceResponse();
			  response.setReturncode(ResponseCode.EXCEPTION);
			  response.setData(e.getMessage());
			  return response;
		  }
		  Criteria criteria = null;
		  Query query = null;
		  DictionaryModel dict = null;
		  DictionaryDataModel dictData = null;
		  DictionaryTypeModel dictType = null;
		  if(!dataList.isEmpty()){
			  Map<String, Object> dataMap = dataList.get(0);
			  List<Map<String,Object>> dictMapList = (List<Map<String, Object>>) dataMap.get("dictionary");
			  List<Map<String,Object>> dictDataMapList = (List<Map<String, Object>>) dataMap.get("dictionaryData");
			  JSONObject paramsObject = null;
			  Map<String,Long> dictIdMap = new HashMap<String,Long>();
			  for(Map<String,Object> dictMap : dictMapList){
				  paramsObject = JSONObject.parseObject(JSON.toJSONString(dictMap));
				  criteria = Criteria.where("entId").is(paramsObject.get("entId"))
						.and("dictTypeCode").is(paramsObject.get("dictTypeCode"));
				  query = new Query(criteria);
				  dictType = template.selectOne(query, DictionaryTypeModel.class, "dictionarytype");
				  if(dictType == null){
					  throw new Exception("字典类别CODE"+paramsObject.get("dictTypeCode")+"不存在");
				  }
				  
				  criteria = Criteria.where("entId").is(paramsObject.get("entId"))
							.and("dictCode").is(paramsObject.get("dictCode"))
							.and("dictTypeCode").is(paramsObject.get("dictTypeCode"));
				  query = new Query(criteria);
				  dict = template.selectOne(query, DictionaryModel.class, "dictionary");
			      if(dict == null){
			    	  paramsObject.put("creator", session.getUser_id());
			    	  paramsObject.put("createDate", new Date());
			    	  this.onInsert(session, paramsObject);
			      }else{
			    	  paramsObject.put("modifier", session.getUser_id());
			    	  paramsObject.put("updateDate", new Date());
			    	  paramsObject.put(this.getKeyfieldName(), dict.getDictId());
			    	  this.onUpdate(session, paramsObject);
			      }
			      dictIdMap.put(paramsObject.getString("dictCode"), paramsObject.getLong("dictId"));
			  }
			  for(Map<String, Object> dictDataMap : dictDataMapList){
				  paramsObject = JSONObject.parseObject(JSON.toJSONString(dictDataMap));
				  Long dictId = dictIdMap.get(paramsObject.getString("dictCode"));
				  if(dictId == null){
					  throw new Exception("字典代码"+paramsObject.get("dictCode")+"不存在");
				  }
				  paramsObject.put("dictId", dictId);
				  
				  query = new Query(Criteria.where("entId").is(dictDataMap.get("entId"))
						  .and("dictCode").is(dictDataMap.get("dictCode"))
						  .and("dictDataCode").is(dictDataMap.get("dictDataCode")));
				  dictData = template.selectOne(query, DictionaryDataModel.class, "dictionarydata");
				  
				  if(dictData == null){
			    	  paramsObject.put("creator", session.getUser_name());
			    	  paramsObject.put("createDate", new Date());
			    	  dictionaryDataServiceImpl.onInsert(session, paramsObject);
				  }else{
					  paramsObject.put("modifier", session.getUser_name());
			    	  paramsObject.put("updateDate", new Date());
			    	  paramsObject.put(dictionaryDataServiceImpl.getKeyfieldName(), dictData.getDictDataId());
			    	  dictionaryDataServiceImpl.onUpdate(session, paramsObject);
				  }
			  }
		  }
		return ServiceResponse.buildSuccess("");
	}
}
