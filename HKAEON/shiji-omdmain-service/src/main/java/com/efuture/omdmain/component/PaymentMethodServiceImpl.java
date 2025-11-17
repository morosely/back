package com.efuture.omdmain.component;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.DictionaryDataModel;
import com.efuture.omdmain.model.PaymentMethodModel;
import com.efuture.omdmain.model.PaymentMethodRefModel;
import com.efuture.omdmain.model.PaymentMethodTreeBean;
import com.efuture.omdmain.service.PaymentMethodService;
import com.efuture.omdmain.utils.SpringContextUtil;
import com.efuture.omdmain.utils.httpclientUtils;
import com.mongodb.DBObject;
import com.product.component.CommonServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.TypeUtils;
import com.product.util.UniqueID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentMethodServiceImpl extends CommonServiceImpl<PaymentMethodModel,PaymentMethodServiceImpl> implements PaymentMethodService {

	private static Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

	public PaymentMethodServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	@Autowired
	private PaymentMethodRefServiceImpl paymentMethodRefServiceImpl;

	@Autowired
	private ConfigurableEnvironment env;

	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse response = super.onQuery(session, paramsObject);
		return response;
	}

	@Override
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		String userCode = session.getUser_code();
		String updateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		paramsObject.put("modifier",userCode);
		paramsObject.put("updateDate",updateDate);
		this.setUpsert(false);
		return this.onUpdate(session, paramsObject);
	}
	
	public ServiceResponse save(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onSave(session, paramsObject);
	}
	
	@Override
	@Transactional
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		Map<String,Object> returnMap = new HashMap<>();
		String createDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String userCode = session.getUser_code();

		//1.封装支付方式数据
		JSONArray paymentmethodArray = paramsObject.getJSONArray("paymentMethod");
		JSONObject paymentmethod = ((JSONObject)paymentmethodArray.get(0));
		Long pmid = UniqueID.getUniqueID(true);
		paymentmethod.put("createDate", createDateString);
		paymentmethod.put("creator", userCode);
		paymentmethod.put("pmid", pmid);
		paymentmethod.put("virtualPayType", 0);//默认初始值0
		paymentmethod.put("entId", session.getEnt_id());
		ServiceResponse response = this.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		returnMap.put(this.getCollectionName(), response.getData());
		
		//2.封装支付方式关系数据
	    JSONArray paymentmethodrefArray = paramsObject.getJSONArray("paymentmethodref");
	    for (int i = 0; i < paymentmethodrefArray.size(); i++) {
	    	JSONObject refJson = (JSONObject) paymentmethodrefArray.get(i);
	    	refJson.put("pmid", pmid);//设置支付方式和支付方式关系的关联关系
	    	refJson.put("payCode", paymentmethod.get("payCode"));
	    	refJson.put("payName", paymentmethod.get("payName"));
	    	refJson.put("status", 1);
	    	refJson.put("createDate", createDateString);
	    	refJson.put("creator", userCode);
	    	refJson.put("entId", session.getEnt_id());
	    }		
	    response =  paymentMethodRefServiceImpl.onInsert(session, paramsObject);
	    if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			throw new Exception(response.getData().toString());
		}

		//调用营销注入积分
		ServiceResponse Injectionresponse=empInjectPaymethod(session, paramsObject.getJSONArray("paymentMethod").getJSONObject(0));
		if(!ResponseCode.SUCCESS.equals(Injectionresponse.getReturncode())){
			throw new Exception(Injectionresponse.getData().toString());
		}
		returnMap.put(paymentMethodRefServiceImpl.getCollectionName(), response.getData());
		return ServiceResponse.buildSuccess(returnMap);
	}
	
	//查询支付方式和支付方式关系
	@Override
	public ServiceResponse detail(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "pmid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;		
		
		ServiceResponse response = super.onQuery(session, paramsObject);
		//POS总部需要返回virtualPayType字段（虚拟支付类型）
		Map<String,List<RowMap>> returnMap = null;
		if(ResponseCode.SUCCESS.equals(response.getReturncode())) {
			returnMap = (Map<String,List<RowMap>>)response.getData();
			/*List<RowMap> list = returnMap.get(this.getCollectionName());
			for (RowMap rowMap : list) {
				Set set = rowMap.keySet();
				if(!set.contains("virtualPayType")){
					rowMap.pu1t("virtualPayType","");
				}
			}*/
		}
		
		ServiceResponse refResponse = paymentMethodRefServiceImpl.onQuery(session, paramsObject);
		if(ResponseCode.SUCCESS.equals(refResponse.getReturncode())){
			Map<String,List<RowMap>> refMap = (Map<String,List<RowMap>>)refResponse.getData();
			returnMap.put(paymentMethodRefServiceImpl.getCollectionName(),refMap.get(paymentMethodRefServiceImpl.getCollectionName()));
		}
		return ServiceResponse.buildSuccess(returnMap);
	}

	//封装事务回滚的异常信息返回前台
	public ServiceResponse updatePaymentMethodAndRef(ServiceSession session, JSONObject paramsObject){
		ServiceResponse response = null;
		try {
			PaymentMethodServiceImpl proxyService = ((PaymentMethodServiceImpl)SpringContextUtil.getBean(this.getClass()));
			response = proxyService.doUpdatePaymentMethodAndRef(session,paramsObject);
		} catch (Exception e) {
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,e.getMessage());
		}
		return response;
	}
	
	//修改支付方式和支付方式关系
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
	public ServiceResponse doUpdatePaymentMethodAndRef(ServiceSession session, JSONObject paramsObject) throws Exception {
		Map<String,Object> returnMap = new HashMap<>();
		String nowDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Long userId = session.getUser_id();

        JSONObject   InjectionObj= (JSONObject) paramsObject.getJSONArray("paymentMethod").getJSONObject(0).clone();
		//1.封装支付方式数据
		JSONArray paymentmethodArray = paramsObject.getJSONArray("paymentMethod");
		JSONObject paymentmethod = ((JSONObject)paymentmethodArray.get(0));
		paymentmethod.put("updateDate", nowDateString);
		paymentmethod.put("modifier", userId);
		Long pmid = paymentmethod.getLong("pmid");
		this.setUpsert(false);
		ServiceResponse response = this.onUpdate(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		returnMap.put(this.getCollectionName(), response.getData());
		
		//2.封装支付方式关系数据
		//2.1查询支付方式对应ERP关系的数据
		JSONArray deleteParam  = new JSONArray();
		Criteria criteria = Criteria.where("pmid").is(pmid);
		Query query = new Query(criteria);
		List<PaymentMethodRefModel> oldList = this.getTemplate().select(query, PaymentMethodRefModel.class, "paymentmethodref");
		List<Long> oldKey = (oldList==null || oldList.isEmpty())? null : oldList.stream().map(PaymentMethodRefModel::getPmrid).collect(Collectors.toList());
		
		//2.2添加或更新的新数据 
	    JSONArray paymentmethodrefArray = paramsObject.getJSONArray("paymentmethodref");
	    JSONArray insert = new JSONArray();
	    JSONArray update = new JSONArray();
	    List<Long> newKey = new ArrayList<Long>();
	    for (int i = 0; i < paymentmethodrefArray.size(); i++) {
	    	JSONObject refJson = (JSONObject) paymentmethodrefArray.get(i);
	    	Long primd = refJson.getLong("pmrid");
	    	if(StringUtils.isEmpty(primd)){//新增
		    	refJson.put("createDate", nowDateString);
		    	refJson.put("creator", userId);
		    	refJson.put("updateDate", nowDateString);
		    	refJson.put("modifier", userId);
		    	refJson.put("pmid", pmid);//设置支付方式和支付方式关系的关联关系
		    	refJson.put("payCode", paymentmethod.get("payCode"));
		    	//refJson.put("payName", paymentmethod.get("payName"));//用户自定义payName
		    	refJson.put("status", 1);
		    	refJson.put("entId", session.getEnt_id());
		    	insert.add(refJson);
	    	}else{//修改
	    		newKey.add(primd);
	    		refJson.put("updateDate", nowDateString);
		    	refJson.put("modifier", userId);
		    	update.add(refJson);
	    	}
	    }	
	    //插入
	    if(insert!=null && !insert.isEmpty()){
		    paramsObject.clear();
		    paramsObject.put(paymentMethodRefServiceImpl.getCollectionName(),insert);
		    response =  paymentMethodRefServiceImpl.onInsert(session, paramsObject);
		    if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
				throw new Exception(response.getData().toString());
			}
			returnMap.put(paymentMethodRefServiceImpl.getCollectionName(), response.getData());
	    }
		
		//更新
	    if(update!=null && !update.isEmpty()){
		    paramsObject.clear();
		    paramsObject.put(paymentMethodRefServiceImpl.getCollectionName(),update);
		    paymentMethodRefServiceImpl.setUpsert(false);
		    response =  paymentMethodRefServiceImpl.onUpdate(session, paramsObject);
		    if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
				throw new Exception(response.getData().toString());
			}
			returnMap.put(paymentMethodRefServiceImpl.getCollectionName(), response.getData());
	    }
	    
		//2.3删除的数据
		if(oldKey!=null && !oldKey.isEmpty()){
			oldKey.removeAll(newKey);
			paramsObject.put("table",paymentMethodRefServiceImpl.getCollectionName());
			paramsObject.put("key","pmrid");
			if(!oldKey.isEmpty()){
				paramsObject.put("values",oldKey);
				this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
			}
		}

		//调用营销注入积分
		ServiceResponse Injectionresponse=empInjectPaymethod(session, InjectionObj);
		if(!ResponseCode.SUCCESS.equals(Injectionresponse.getReturncode())){
			log.error(JSONArray.toJSONString(Injectionresponse));
			throw  new Exception("调用营销注入支付方式异常");
		}
		return ServiceResponse.buildSuccess(returnMap);
	}

	//字典树关联支付关系树
	private final static String PayMethodCode = "FKLX";
	@Override
	public ServiceResponse onPaymentMethodTree(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.查询字典定义（付款类型：FKLX）
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id()).and("status").is("1").and("dictCode").is(PayMethodCode);
		Query query = new Query(criteria);
		//查询支付方式字典信息
		Long startTime = System.currentTimeMillis();
		List<DictionaryDataModel> allDicData = this.getTemplate().select(query, DictionaryDataModel.class, "dictionarydata");
		Long endTime = System.currentTimeMillis();
		log.info("1. ==========>>>【onPaymentMethodTree】【查询dictionarydata耗时】 {}",(endTime - startTime));
		
		//2.查询支付关系数据
		criteria = Criteria.where("entId").is(session.getEnt_id());
		if(!StringUtils.isEmpty(paramsObject.get("status"))){
			Short status = paramsObject.getShort("status");
			criteria = Criteria.where("entId").is(session.getEnt_id()).and("status").is(status);
		}
	    query = new Query(criteria);
		startTime = System.currentTimeMillis();
		List<PaymentMethodTreeBean> allPaymentMethod = this.getTemplate().select(query, PaymentMethodTreeBean.class, "paymentmethod");
		endTime = System.currentTimeMillis();
		log.info("2. ==========>>>【onPaymentMethodTree】【查询paymentmethod耗时】 {}",(endTime - startTime));

		List<PaymentMethodTreeBean> trees = new ArrayList<PaymentMethodTreeBean>();//保存虚拟树的List
		startTime = System.currentTimeMillis();
		for (DictionaryDataModel dictionaryDataModel : allDicData) {
			paramsObject.put("parentCode", dictionaryDataModel.getDictDataCode());
			paramsObject.put("parentId", dictionaryDataModel.getDictDataId());
			List<PaymentMethodTreeBean> paymentMethodTree = paymentMethodTree(session,paramsObject,allPaymentMethod);//获取某个支付方式的树
			//虚拟构造树
			PaymentMethodTreeBean paymentMethodTreeBean = new PaymentMethodTreeBean();
			paymentMethodTreeBean.setPayCode(dictionaryDataModel.getDictDataCode());
			paymentMethodTreeBean.setPayName(dictionaryDataModel.getDictDatacnname());
			paymentMethodTreeBean.setPmid(dictionaryDataModel.getDictDataId());
			paymentMethodTreeBean.setChildren(paymentMethodTree);
			trees.add(paymentMethodTreeBean);
		}
		endTime = System.currentTimeMillis();
		log.info("3. ==========>>>【onPaymentMethodTree】【构建Tree耗时】 {}",(endTime - startTime));

		return ServiceResponse.buildSuccess(trees);
	}

	//查询所有付款类型树 （树形结构）
	public List<PaymentMethodTreeBean> paymentMethodTree(ServiceSession session, JSONObject paramsObject,List<PaymentMethodTreeBean> allPaymentMethod){
		//1.查询支付关系数据
		Criteria criteria = Criteria.where("entId").is(session.getEnt_id());
		Query query = new Query(criteria);
		//2.获得顶级树数据
		List<PaymentMethodTreeBean> topPaymentMethod = new ArrayList<>();
		for (PaymentMethodTreeBean paymentMethod : allPaymentMethod) {
			//System.out.println(paramsObject.getString("parentCode") +":"+ paymentMethod.getParentCode()+" -- "+paramsObject.getString("parentId")+":"+paymentMethod.getParentId());
			if(paramsObject.getString("parentCode").equals(paymentMethod.getParentCode())&& paramsObject.getString("parentId").equals(paymentMethod.getParentId().toString())){
				topPaymentMethod.add(paymentMethod);
			}
		}
		return buildTree(topPaymentMethod,allPaymentMethod);//从顶级节点开始往下递归
	}

	//从顶级节点开始递归构建树
	private List<PaymentMethodTreeBean> buildTree(List<PaymentMethodTreeBean> root, List<PaymentMethodTreeBean> allPaymentMethod) {
		for (PaymentMethodTreeBean paymentMethod : root) {
			Long parentId = paymentMethod.getPmid();
			String parentName = paymentMethod.getPayName();
			List<PaymentMethodTreeBean> children = findChildren(parentId,parentName,allPaymentMethod);
			buildTree(children,allPaymentMethod);
			paymentMethod.setChildren(children);
		}
		return root;
	}
    
	//查询该节点的所有儿子节点
	private List<PaymentMethodTreeBean> findChildren(Long parentId,String parentName, List<PaymentMethodTreeBean> allPaymentMethod) {
		List<PaymentMethodTreeBean> childrenPaymentMethod = new ArrayList<PaymentMethodTreeBean>();
		for (PaymentMethodTreeBean paymentMethod : allPaymentMethod) {
			if(parentId.equals(paymentMethod.getParentId())){
				childrenPaymentMethod.add(paymentMethod);
			}
		}
		return childrenPaymentMethod;
	}

	@Override
	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "pmid");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;		
		//1.删除支付方式
		ServiceResponse response = this.onDelete(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		//2.删除支付方式关系
		paramsObject.put("table",paymentMethodRefServiceImpl.getCollectionName());
		paramsObject.put("key","pmid");
		JSONArray arrayParam  = new JSONArray(1);
		arrayParam.add(paramsObject.get("pmid"));
		paramsObject.put("values",arrayParam);
		this.getTemplate().getSqlSessionTemplate().delete("beanmapper.AdvancedQueryMapper.deleteModel",paramsObject);
		return ServiceResponse.buildSuccess(response.getData());
	}


	//调用营销接口受限支付方式注入  是否积分
	/*
	   地址：https://sit-srmp.sjhgo.com/omp-model-webin/rest?method=efuture.omp.event.popmodel.exceptpay&ent_id=0
            {
         "ent_id": 0,
         "orgs": [],
         "corp_id": "010",
          "setmode": [
            "ISPOP"
             ],
          "pays": [
            {
            "joinmode": "Y",
            "pay_code": "471",
            "pay_type": "1",
            "pay_name": "编码券471"
            }
            ]
         }
	 */
	public   ServiceResponse  empInjectPaymethod(ServiceSession session, JSONObject paramsObject)
	{
		log.info("开始营销积分注入");
		//检查是否开发支付方式注入
		if(!"on".equals(env.getProperty("emp.inject.switch"))){
			log.error("未开启积分支付方式注入");
			return ServiceResponse.buildSuccess("未开启积分支付方式注入,无需调用营销接口");
		}
		log.info("开始营销积分注入1");

		/*修改 先判断是否积分这个字段是否有变动
		if(!StringUtils.isEmpty(paramsObject.getString("pmid"))){
			JSONObject paymethod=new JSONObject();
			paymethod.put("pmid",paramsObject.getString("pmid"));
			paymethod.put("creditsFalg",paramsObject.getBoolean("creditsFalg"));
			ServiceResponse  result=this.onQuery(session,paymethod);
			JSONObject resultObj = (JSONObject)result.getData();
			if(resultObj.containsKey("total_results") && resultObj.getInteger("total_results")>0){
				log.info("开始营销积分注入2----未修改creditsFalg,无需调用营销接口");
				return ServiceResponse.buildSuccess("未修改creditsFalg,无需调用营销接口");

			}

		}*/
		log.info("开始营销积分注入2session"+ JSON.toJSONString(session));
		JSONObject posParamObject = new JSONObject();
		if(!StringUtils.isEmpty(session.getErpCode())){
			posParamObject.put("corp_id", session.getErpCode());
		}else{
			posParamObject.put("corp_id", "002");
		}
		posParamObject.put("ent_id", session.getEnt_id());
		posParamObject.put("orgs", Collections.emptyList());
		JSONObject pays=new JSONObject();
		pays.put("pay_code",paramsObject.getString("payCode"));
		pays.put("pay_type",paramsObject.getString("payType"));
		pays.put("pay_name",paramsObject.getString("payName"));
		if(paramsObject.containsKey("creditsFalg"))
		{
			if(paramsObject.getBoolean("creditsFalg")){
				pays.put("joinmode","N");
			}else {
				pays.put("joinmode", "Y");
			}
		}else{
			pays.put("joinmode","Y");
		}

		JSONArray arraypays= new JSONArray();
		arraypays.add(pays);
		posParamObject.put("pays", arraypays);
		List<String> setModeList = Arrays.asList("ISPOINT");
		posParamObject.put("setmode", setModeList);
		String ompUrl = env.getProperty("SERVICE_OMP_ROUTE");
		if (StringUtils.isEmpty(ompUrl)) {
			log.error("配置文件缺少支付方式营销调用地址");
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,"配置文件缺少支付方式营销调用地址");

		}
		String ompUrlNew="http://"+ompUrl+"/omp-model-webin/rest?method=efuture.omp.event.popmodel.exceptpay&ent_id=0";
		log.info("开始营销积分注入3");
		String response = null;
		try {
			log.info("请求营销支付方式地址：---------> {} ", ompUrlNew);
			log.info("请求营销支付方式入参：---------> {} ", TypeUtils.castToString(posParamObject));
			response = httpclientUtils.post(ompUrlNew,TypeUtils.castToString(posParamObject));
			log.info("请求营销支付方式:response ---------> {} ", response);
			JSONObject jsonObject = JSONObject.parseObject(response);
			if (ResponseCode.SUCCESS.equals(jsonObject.getString("returncode"))) {
				log.info("请求营销支付方式:调用成功");
				return ServiceResponse.buildSuccess("修改成功");
			} else {
				log.error("请求营销据支付方式:调用异常");
				return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,"请求营销据支付方式:调用异常");
			}
		} catch (Exception e) {
			log.error("请求营销据支付方式:API接口无响应或异常，失败原因：{}", e.getCause());
			return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,e.getMessage());

		}
	}

	/*
	 *订单查询使用
	 */
	public   ServiceResponse  getErpPaymethod(ServiceSession session, JSONObject paramsObject){
		List paymentMethodRefList = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.PaymentMethodModelMapper.getErpPaymethod", paramsObject);
		JSONObject result = new JSONObject();
		result.put("paymentmethodref", paymentMethodRefList);
		result.put("total_results", paymentMethodRefList.size());
		return ServiceResponse.buildSuccess(result);
	}

	/*
	 *pos初始化使用
	 */
	public   ServiceResponse  getPaymethod(ServiceSession session, JSONObject paramsObject){
		List paymentMethodRefList = this.getTemplate().getSqlSessionTemplate()
				.selectList("beanmapper.PaymentMethodModelMapper.posGetPaymethod", paramsObject);
		JSONObject result = new JSONObject();
		result.put("paymentmethodref", paymentMethodRefList);
		result.put("total_results", paymentMethodRefList.size());
		return ServiceResponse.buildSuccess(result);
	}
}
