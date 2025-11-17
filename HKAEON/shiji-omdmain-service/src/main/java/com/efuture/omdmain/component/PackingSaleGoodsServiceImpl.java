package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.common.GoodsType;
import com.efuture.omdmain.model.GoodsModel;
import com.efuture.omdmain.model.SaleGoodsItemsModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.service.PackingSaleGoodsService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

/**
 * 组包码Service
 * @author Administrator
 *
 */
public class PackingSaleGoodsServiceImpl extends JDBCCompomentServiceImpl<SaleGoodsModel> implements PackingSaleGoodsService{

	public PackingSaleGoodsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

//	@Override
//	protected FMybatisTemplate getTemplate() {
//		return this.getBean("StorageOperation", FMybatisTemplate.class);
//	}
	
	@Autowired
	private GoodsServiceImpl goodsServiceImpl;
	@Autowired
	private SaleGoodsItemsServiceImpl saleGoodsItemsServiceImpl;
	@Autowired
	private GoodsShopRefServiceImpl goodsShopRefServiceImpl;
	@Autowired
	private SaleGoodsServiceImpl saleGoodsServiceImpl;
	
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

	@Override
	@Transactional
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "shopId","goodsName","salePrice","goodsCode","barNo","categoryId","erpCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;		
		
		//统一插入时间：保持多个表的插入时间是一致性
		String createDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String creator = Long.toString(session.getUser_id());
		Long groupSgid = UniqueID.getUniqueID(true);//生成组包码的ID
		String groupGoodsCode = paramsObject.getString("goodsCode");//组包码编码
		Long entId = session.getEnt_id();
		String erpCode =  paramsObject.getString("erpCode");
		String barNo = paramsObject.getString("barNo");
		
		//0.校验编码和条码不能有重
		Criteria criteria = Criteria.where("erpCode").is(erpCode).and("barNo").is(barNo);
		Query query = new Query(criteria);
		Field flds = query.fields();
		flds.include("barNo");
		List<GoodsModel> repeatBarNoGoods  = this.getTemplate().select(query, GoodsModel.class,"goods");
		
		criteria = Criteria.where("erpCode").is(erpCode).and("goodsCode").is(groupGoodsCode);
		query = new Query(criteria);
		flds = query.fields();
		flds.include("goodsCode");
		List<GoodsModel> repeatGoodsCodeGoods  = this.getTemplate().select(query, GoodsModel.class,"goods");
		
		StringBuffer repeatResult = null;
		if(!repeatBarNoGoods.isEmpty()){
			repeatResult = repeatResult == null ? new StringBuffer():repeatResult;
			List<String> repeatBarNos = repeatBarNoGoods.stream().map(GoodsModel::getBarNo).collect(Collectors.toList());
			repeatResult.append("数据库重复条码：").append(repeatBarNos.toString()).append(" ");
		}
		if(!repeatGoodsCodeGoods.isEmpty()){
			repeatResult = repeatResult == null ? new StringBuffer():repeatResult;
			List<String> repeatGoodsCodes = repeatGoodsCodeGoods.stream().map(GoodsModel::getBarNo).collect(Collectors.toList());
			repeatResult.append("数据库重复商品编码：").append(repeatGoodsCodes.toString()).append(" ");
		}
		if(repeatResult != null){
			return ServiceResponse.buildFailure(session,ResponseCode.Failure.ALREADY_EXISTS,repeatResult.toString());
		}
		
		Map<String,Object> returnMap = new HashMap<>();
		//1.存入商品基础表
		JSONArray saleGoodsItems = (JSONArray)paramsObject.get("saleGoodsItems");//暂存商品信息
		paramsObject.remove("saleGoodsItems");//清除商品项信息。将组包商品保存到商品销售表
		paramsObject.put("sgid",groupSgid); 
		paramsObject.put("goodsType",GoodsType.ZBM.getValue());
		paramsObject.put("createDate",createDateString);
		paramsObject.put("creator",creator);
		paramsObject.put("entId",entId);
		paramsObject.put("goodsStatus",1);//
		paramsObject.put("minDiscount",1);//最低折扣率
		paramsObject.put("directFromErp",false);//是否是ERP的数据来源
		ServiceResponse response = goodsServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(goodsServiceImpl.getCollectionName(),response.getData());
		
		//2.存入经营配置表
		paramsObject.put("goodStatus",1);//经营配置的状态
		response = goodsShopRefServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(goodsShopRefServiceImpl.getCollectionName(),response.getData());
				 
		//3.存入商品销售表
		response = saleGoodsServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(saleGoodsServiceImpl.getCollectionName(),response.getData());
		
		//4.存入商品项表
		for (int i = 0; i < saleGoodsItems.size(); i++) {
			JSONObject json = (JSONObject) saleGoodsItems.get(i);
			json.put("ggoodsCode",groupGoodsCode);//组包码的Code（组商品code）
			json.put("gsgid",groupSgid);//组包码的ID（组商品ID）
			json.put("createDate",createDateString);
			json.put("creator",creator);
			json.put("erpCode",erpCode);
			json.put("entId",entId);
			Float discountShareRate1 = Float.parseFloat(json.get("discountShareRate").toString()); // 折扣分摊比例
			json.put("discountShareRate",discountShareRate1/100);
		}
		paramsObject.clear();
		paramsObject.put(saleGoodsItemsServiceImpl.getCollectionName(),saleGoodsItems);
		response = saleGoodsItemsServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(saleGoodsItemsServiceImpl.getCollectionName(),response.getData());
		return ServiceResponse.buildSuccess(returnMap);
	}

	//更新
	@Override
	@Transactional
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "ssgid","sgid","goodsName","goodsCode","barNo","erpCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		Long gsgid = paramsObject.getLong("sgid");//组包码的ID
		String erpCode =  paramsObject.getString("erpCode");
		String barNo =  paramsObject.getString("barNo");
		
		//0.编辑时校验条码不能有重
		Criteria criteria = Criteria.where("erpCode").is(erpCode).and("barNo").is(barNo).and("sgid").ne(gsgid);
		Query query = new Query(criteria);
		Field flds = query.fields();
		flds.include("barNo");
		List<GoodsModel> repeatBarNoGoods  = this.getTemplate().select(query, GoodsModel.class,"goods");
		
		StringBuffer repeatResult = null;
		if(!repeatBarNoGoods.isEmpty()){
			repeatResult = repeatResult == null ? new StringBuffer():repeatResult;
			List<String> repeatBarNos = repeatBarNoGoods.stream().map(GoodsModel::getBarNo).collect(Collectors.toList());
			repeatResult.append("数据库重复条码：").append(repeatBarNos.toString()).append(" ");
		}
		if(repeatResult != null){
			return ServiceResponse.buildFailure(session,ResponseCode.Failure.ALREADY_EXISTS,repeatResult.toString());
		}
		
		Map<String,Object> returnMap = new HashMap<>();
		//1.更新商品销售表
		saleGoodsServiceImpl.setUpsert(false);
		ServiceResponse response = saleGoodsServiceImpl.onUpdate(session, paramsObject);
		returnMap.put(saleGoodsServiceImpl.getCollectionName(), response.getData());
		
		//2.更新商品基本表
		Object saleGoods = paramsObject.get("saleGoods");
		paramsObject.clear();
		paramsObject.put(goodsServiceImpl.getCollectionName(), saleGoods);
		goodsServiceImpl.setUpsert(false);
		response = goodsServiceImpl.onUpdate(session, paramsObject);
		returnMap.put(goodsServiceImpl.getCollectionName(), response.getData());

		return ServiceResponse.buildSuccess(returnMap); 
	}

	//启用或者停用
	@Override
	@Transactional
	public ServiceResponse startOrStop(ServiceSession session, JSONObject paramsObject) throws Exception {
		String date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    paramsObject.put("updateDate",date);
		//1.更新商品基础表
		goodsServiceImpl.setUpsert(false);
		goodsServiceImpl.onUpdate(session, paramsObject);
		//2.更新商品销售表
		paramsObject.put("table",this.getCollectionName());
		paramsObject.put("setField","goodsStatus");
		paramsObject.put("setFieldValue",paramsObject.getString("goodsStatus"));
		paramsObject.put("key",this.getKeyfieldName());
		paramsObject.put("values",Arrays.asList(paramsObject.getString(this.getKeyfieldName())));
		this.getTemplate().getSqlSessionTemplate().update("beanmapper.AdvancedQueryMapper.updateModel",paramsObject);
		return ServiceResponse.buildSuccess("更新成功");
	}
}
