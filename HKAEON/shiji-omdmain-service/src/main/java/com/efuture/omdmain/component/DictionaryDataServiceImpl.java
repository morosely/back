package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.product.component.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.DictionaryDataModel;
import com.efuture.omdmain.model.DictionaryModel;
import com.efuture.omdmain.service.DictionaryDataService;
import com.efuture.omdmain.utils.SpringContextUtil;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.product.util.UniqueID;

public class DictionaryDataServiceImpl extends CommonServiceImpl<DictionaryDataModel,DictionaryDataServiceImpl> implements DictionaryDataService {

	public DictionaryDataServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Autowired
	private DictionaryServiceImpl dictionaryServiceImpl;
	
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		paramsObject = QueryBlankValuePreFilter.getInstance().trimBlankValue(paramsObject);
		FMybatisTemplate template = this.getTemplate();
        template.onSetContext(session);
        
       //设置默认分页参数
  		if(!paramsObject.containsKey("page_no")){
  			paramsObject.put("page_no", 0);
  		}else{
  			paramsObject.put("page_no", (paramsObject.getInteger("page_no")-1)*paramsObject.getInteger("page_size"));
  		}
  		if(!paramsObject.containsKey("page_size")) {
  			paramsObject.put("page_size", 10);
  		}
  		
        List dicDataList = template.getSqlSessionTemplate().selectList("beanmapper.DictionaryDataModelMapper.selectDicData",paramsObject);
		Integer total_results = 0;
		if(dicDataList != null && dicDataList.size()>0){
			total_results = template.getSqlSessionTemplate().selectOne("beanmapper.DictionaryDataModelMapper.countSelectDicData",paramsObject);
		}
		JSONObject result = new JSONObject();
		result.put("dictionarydata", dicDataList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
	//批量更新操作
	@Override
	@Transactional
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		JSONArray dictionaryDataArray = paramsObject.getJSONArray("dictionarydata");
		String nowDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Long userId = session.getUser_id();
		for (int i = 0; i < dictionaryDataArray.size(); i++) {
			JSONObject json = (JSONObject) dictionaryDataArray.get(i);
			Object dictDataId = json.get("dictDataId");
			if(StringUtils.isEmpty(dictDataId)){//新增
				json.put("createDate", nowDateString);
				json.put("creator", userId);
			}else{//编辑
				json.put("updateDate", nowDateString);
				json.put("modifier", userId);
			}
			json.put("entId", session.getEnt_id());
		}
		return this.onUpdate(session, paramsObject);
	}

	//封装事务回滚的异常信息返回前台
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject){
		ServiceResponse response = null;
		try {
			DictionaryDataServiceImpl proxyService = ((DictionaryDataServiceImpl)SpringContextUtil.getBean(this.getClass()));
			response = proxyService.doAdd(session,paramsObject);
		} catch (Exception e) {
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,e.getMessage());
		}
		return response;
	}
	
	//新增批量保存
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
	public ServiceResponse doAdd(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
		
		Map<String,Object> returnMap = new HashMap<>();
		//1.临时保存字典数据
		JSONArray dictionaryArray = paramsObject.getJSONArray("dictionary");
		JSONArray dictionaryDataArray = paramsObject.getJSONArray("dictionarydata");
	
		//2.保存字典
		Date createDate = new Date();//保持两个表的插入时间是一致性
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createDateString = formatter.format(createDate);
		String userCode = session.getUser_code();
		
		Long dictId = UniqueID.getUniqueID(true);
		JSONObject dic = ((JSONObject)dictionaryArray.get(0));
		String dictCode = dic.getString("dictCode");
		dic.put("dictId", dictId);//设置主键
		dic.put("createDate", createDateString);
		dic.put("updateDate", createDateString);
		dic.put("creator", userCode);
		dic.put("entId", session.getEnt_id());
		dic.put("systemFlag",0);//是否系统级 1:是，0:否
		ServiceResponse response = dictionaryServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			throw new Exception(response.getData().toString());
		}
		
		Map<String,Object> dictionaryMap = new HashMap<>();
		dictionaryMap.put("dictId", dictId);
		dictionaryMap.put("dictCode", dictCode);
		returnMap.put(dictionaryServiceImpl.getCollectionName(), dictionaryMap);
		
		//3.批量插入字典数据信息
		for (int i = 0; i < dictionaryDataArray.size(); i++) {
			JSONObject json = (JSONObject) dictionaryDataArray.get(i);
			json.put("dictId", dictId);//设置字典和字典数据的关联关系
			json.put("dictCode", dictCode);
			json.put("createDate", createDateString);
			json.put("updateDate", createDateString);
			json.put("creator", userCode);
			json.put("entId", session.getEnt_id());
		}
		response = this.onInsert(session,paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			throw new Exception(response.getData().toString());
		}
		returnMap.put(this.getCollectionName(), response.getData());
		return ServiceResponse.buildSuccess(returnMap); 
	}
	
	public ServiceResponse queryGroupData(ServiceSession session, JSONObject paramsObject) throws Exception {
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if(!paramsObject.containsKey("dictCode")){
			return ServiceResponse.buildFailure(session,ResponseCode.Exception.SPECDATA_IS_EMPTY,"关键数据为空！");
		}
		
		if (paramsObject.containsKey(this.getEntname())) {
			try {
				this.onQueryForEntID(session, paramsObject);
			} catch (Exception e) {
				return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, e.getMessage());
			}
		}
		
		Set<String> keySet = paramsObject.keySet();
		Criteria criteria = null;
		for (String keyName : keySet) {
			criteria = this.onParseParamsOne(criteria, keyName, paramsObject.get(keyName));
		}
		Query query = null;
		if (criteria != null) {
			query = new Query(criteria);
		} else {
			query = new Query();
		}
		List<DictionaryModel> lid = this.onFind(session, this.getTemplate(), query, DictionaryModel.class,
				"dictionary");
		
		if(lid == null || lid.isEmpty()){
			return ServiceResponse.buildFailure(session,ResponseCode.Failure.NOT_EXIST,"数据不存在！");
		}
		Long[] dictIds = new Long[lid.size()];
		if(lid.size()>0){
			for(int i =0 ; i<lid.size();i++){
				dictIds[i]= lid.get(i).getDictId();
			}
		}
			
		paramsObject.clear();
		paramsObject.put("dictId", dictIds);
		return super.onQuery(session, paramsObject);
	}

	/**
	 * 根据字典编码查询字典数据
	 * 
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	public ServiceResponse getDataByCode(ServiceSession session, JSONObject paramsObject) {
		
		// 参数校验
		ParamValidateUtil.paramCheck(session, paramsObject, "dictCode");
		Long entId = session.getEnt_id();
		String dictCode = paramsObject.getString("dictCode");
		Criteria criteria = Criteria.where("entId").is(entId).and("status").is(1).and("dictCode").is(dictCode);
		Query query = new Query(criteria);
		Field fields = query.fields();
		fields.include("dictId,entId,dictCode,dictDataCode,dictDatacnname,status");
		List<DictionaryDataModel> dictionarydataList = this.getTemplate().select(query, DictionaryDataModel.class, "dictionarydata");
		int total_results = 0;
		if (dictionarydataList != null && dictionarydataList.size() > 0) {
			total_results = dictionarydataList.size();
		}
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), dictionarydataList);
		result.put("total_results", total_results);
		return ServiceResponse.buildSuccess(result);
	}
	
}
