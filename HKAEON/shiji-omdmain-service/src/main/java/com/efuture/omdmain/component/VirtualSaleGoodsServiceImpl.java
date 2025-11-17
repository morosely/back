package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.efuture.omdmain.service.VirtualSaleGoodsService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

/**
 * 虚拟母品Service
 * @author Administrator
 *
 */
public class VirtualSaleGoodsServiceImpl extends JDBCCompomentServiceImpl<GoodsModel> implements VirtualSaleGoodsService{

	public VirtualSaleGoodsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	private SaleGoodsServiceImpl saleGoodsServiceImpl;
	@Autowired
	private SaleGoodsItemsServiceImpl saleGoodsItemsServiceImpl;
	@Autowired
	private GoodsShopRefServiceImpl goodsShopRefServiceImpl;
	@Autowired
	private GoodsServiceImpl goodsServiceImpl;
	
	//查询虚拟母品列表
	@Override
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
	
	//生成虚拟母品编码和条码
	@Override
	public ServiceResponse generate(ServiceSession session, JSONObject paramsObject) throws Exception {
		return saleGoodsServiceImpl.getCodeBT(session, paramsObject);
	}

	//生成虚拟母品和项目项
	@Override
	@Transactional
	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "shopId","goodsName","goodsCode","barNo","erpCode","shopCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;		
		
	    //统一插入时间：保持多个表的插入时间是一致性
		String createDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String creator = Long.toString(session.getUser_id());
		Long groupSgid = UniqueID.getUniqueID(true);//生成虚拟母品的ID
		String groupGoodsCode = paramsObject.getString("goodsCode");//虚拟母品编码
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
		
		Map<String,Object> returnMap = new HashMap<>();//返回前台结果ID集合
		//1.存入商品基础表
		JSONArray saleGoodsItems = (JSONArray)paramsObject.get("saleGoodsItems");//暂存商品信息
		paramsObject.remove("saleGoodsItems");//清除商品项信息。获得虚拟母品信息保存到商品基础表
		paramsObject.put("sgid",groupSgid); 
		paramsObject.put("goodsType",GoodsType.XNMP.getValue());
		paramsObject.put("createDate",createDateString);
		paramsObject.put("creator",creator);
		paramsObject.put("entId",entId);
		paramsObject.put("goodsStatus",1);
		paramsObject.put("minDiscount",1);//最低折扣率
		paramsObject.put("directFromErp",false);//是否是ERP的数据来源
		ServiceResponse response = goodsServiceImpl.onInsert(session, paramsObject);//虚拟子品插入商品基础表
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(goodsServiceImpl.getCollectionName(),response.getData());
		
		//2.存入经营配置表
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
			json.put("ggoodsCode",groupGoodsCode);//虚拟母品的Code（组商品code）
			json.put("gsgid",groupSgid);//虚拟母品的ID（组商品ID）
			json.put("createDate",createDateString);
			json.put("creator",creator);
			json.put("erpCode",erpCode);
			json.put("entId",entId);
		}
		paramsObject.clear();
		paramsObject.put(saleGoodsItemsServiceImpl.getCollectionName(),saleGoodsItems);
		response = saleGoodsItemsServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(saleGoodsItemsServiceImpl.getCollectionName(),response.getData());
		return ServiceResponse.buildSuccess(returnMap);
	}

	//虚拟母品明细
	@Override
	public ServiceResponse detail(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse	saleGoodsResponse = saleGoodsServiceImpl.onQuery(session, paramsObject);
		JSONObject saleGoodsData = (JSONObject) saleGoodsResponse.getData();
		//商品项，查询组商品编码
		Object sgid = paramsObject.getLong("sgid");
//		Criteria criteria = Criteria.where("gsgid").is(sgid);
//		Query query = new Query(criteria);
//		this.getTemplate().onSetContext(session);
//		List<SaleGoodsItemsModel> items = this.getTemplate().select(query, SaleGoodsItemsModel.class, "saleGoodsItems");
		paramsObject.clear();
		paramsObject.put("gsgid", sgid);
		ServiceResponse itemResponse = saleGoodsItemsServiceImpl.onQuery(session, paramsObject);
		saleGoodsData.put(saleGoodsItemsServiceImpl.getCollectionName(),((JSONObject)itemResponse.getData()).get(saleGoodsItemsServiceImpl.getCollectionName()));
		return ServiceResponse.buildSuccess(saleGoodsData);
	}

	//虚拟母品修改
	@Override
	@Transactional
	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject, "ssgid","sgid","shopId","goodsName","goodsCode","barNo","erpCode","shopCode");
		if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
		
		 Date updateDate = new Date();//统一更新时间：保持多个表的更新时间是一致性
		 String updateDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateDate);
		 String modifier = Long.toString(session.getUser_id());
		 Long vsgid = paramsObject.getLong("sgid");//虚拟母品的ID
		 String vgoodsCode = paramsObject.getString("goodsCode");//虚拟母品编码
		 Long entId = session.getEnt_id();
		 String erpCode =  paramsObject.getString("erpCode");
		 String barNo =  paramsObject.getString("barNo");
		
		//0.编辑时校验条码不能有重
		Criteria criteria = Criteria.where("erpCode").is(erpCode).and("barNo").is(barNo).and("sgid").ne(vsgid);
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
		//1.修改母品的商品基础表
		JSONArray saleGoodsItems = (JSONArray)paramsObject.get("saleGoodsItems");//暂存商品信息
		paramsObject.remove("saleGoodsItems");//清除商品项信息。获得虚拟母品信息保存到商品基础表
		goodsServiceImpl.setUpsert(false);
		ServiceResponse response = goodsServiceImpl.onUpdate(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(goodsServiceImpl.getCollectionName(),response.getData());
		
		//2.修改经营配置表（无需修改）
		//3.修改母品的商品销售表
		saleGoodsServiceImpl.setUpsert(false);
		response = saleGoodsServiceImpl.onUpdate(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(saleGoodsServiceImpl.getCollectionName(),response.getData());
		
		//2.商品项更新
		//2.1删除以前数据
		paramsObject.clear();
		paramsObject.put("gsgid",vsgid);//虚拟母品的ID(即：商品项的组编码ID)
		response = saleGoodsItemsServiceImpl.onDelete(session, paramsObject);
		//if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		//2.2插入新数据
		for (int i = 0; i < saleGoodsItems.size(); i++) {
			JSONObject json = (JSONObject) saleGoodsItems.get(i);
			json.put("ggoodsCode",vgoodsCode);//虚拟母品的Code（组商品code）
			json.put("gsgid",vsgid);//虚拟母品的ID（组商品ID）
			json.put("createDate",updateDateString);
			json.put("creator",modifier);
			json.put("entId",entId);
			json.put("erpCode",erpCode);
		}
		paramsObject.clear();
		paramsObject.put(saleGoodsItemsServiceImpl.getCollectionName(),saleGoodsItems);
		response = saleGoodsItemsServiceImpl.onInsert(session, paramsObject);
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())) return response;
		returnMap.put(saleGoodsItemsServiceImpl.getCollectionName(),response.getData());
		return ServiceResponse.buildSuccess(returnMap);
	}
	
}
