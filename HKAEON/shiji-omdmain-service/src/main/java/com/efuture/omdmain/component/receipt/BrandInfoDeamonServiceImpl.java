package com.efuture.omdmain.component.receipt;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.BrandsServiceImpl;
import com.efuture.omdmain.component.TaskServiceImpl;
import com.efuture.omdmain.model.BaseUserData;
import com.efuture.omdmain.model.ExtBrandInfoModel;
import com.efuture.omdmain.service.AsyncCallback;
import com.efuture.omdmain.service.BrandInfoDeamonService;
import com.efuture.omdmain.service.UpdateCallback;
import com.efuture.omdmain.utils.JDBCCompomentDeamonServiceImpl;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.UniqueID;

public class BrandInfoDeamonServiceImpl extends JDBCCompomentDeamonServiceImpl<ExtBrandInfoModel> implements BrandInfoDeamonService,AsyncCallback{

	public BrandInfoDeamonServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	@Override
	protected boolean onAction(ServiceSession session, Long requestID,
			BaseUserData userData, ExtBrandInfoModel bean) {
		boolean response = this.onValid(session, requestID, userData, bean);
		
		if (response) {
			response = this.onWork(session, requestID, userData, bean); // 在onWork中产生单据编号
			
		}
		
		
		return response;
	}

	@Autowired
	protected  BrandsServiceImpl service;
	
	@Override
	protected boolean onValid(ServiceSession session, Long requestID,
			BaseUserData userData, ExtBrandInfoModel bean) {
		if (StringUtils.isEmpty(bean.getBrandName())) {
			bean.setBrandName("#");
			bean.setMessage("品牌名称[brandName]不允许为空!");
		}else if(StringUtils.isEmpty(bean.getBrandCode())){
			bean.setBrandCode("#");
			bean.setMessage("品牌编码[brandCode]不允许为空!");
		}else if(StringUtils.isEmpty(bean.getErpCode())){
			bean.setErpCode("#");
			bean.setMessage("经营公司编码[erpCode]不允许为空!");
		}else{
			bean.setProcessed(false);
			return true;
		}
		bean.setProcessed(true);
		return false;
	}
	
	public boolean onWork(ServiceSession session, Long requestID,
			BaseUserData userData, ExtBrandInfoModel bean){
		if (bean.getProcessed()) return false;
		this.clearResultData();
		
		return this.onUpdateCallback(session, requestID, userData, bean,
				new UpdateCallback() {

					@Override
					public boolean submit(Query query, Update update,
							String message) {
						Long brandId = UniqueID.getUniqueID();
						
						FMybatisTemplate template = getTemplate();
						long count = template.count(query, "extbrandinfo");
						update.set("creator", requestID);
						if(count==0){
							update.set("ebrandId", brandId);
							update.set("createDate", new Date());
							update.set("status","1");
							addResultData(brandId);
						}else{
							update.set("modifier", session.getUser_id());
							update.set("updateDate", new Date());
						}
						
						
						template.updateOrInsert(query, update, "extbrandinfo");
						
						return true;
					}
			
		});
		
	}
	
	private boolean onUpdateCallback(ServiceSession session, Long requestID,
			BaseUserData userData, ExtBrandInfoModel bean,
			UpdateCallback callback){
		
		Criteria criteria=Criteria.where("entId").is(session.getEnt_id());
		criteria.and("brandCode").is(bean.getBrandCode());
		Query query = new Query(criteria);

		Update update=new Update();
		
		this.onAssist(bean,update,"entId",false,false);   //零售商   
		this.onAssist(bean,update,"brandCode",true,false);
		this.onAssist(bean,update,"brandSCode");
		this.onAssist(bean,update,"brandName");
		this.onAssist(bean,update,"brandTypeCode");
		this.onAssist(bean,update,"brandLevelCode");
		this.onAssist(bean,update,"brandEnName");
		this.onAssist(bean,update,"designer");
		this.onAssist(bean,update,"brandDesc");
		this.onAssist(bean,update,"erpCode");
		this.onAssist(bean,update,"erpName");
		this.onAssist(bean,update,"lang");
		
		
		if (callback!=null) {
			return callback.submit(query,update,bean.getMessage());
		}
		return true;
		
	}
	
	@Override
	protected void onSubmit(ServiceSession session, Long requestID,BaseUserData userData) {
		JSONObject params=new JSONObject();
		params.put("requestID", requestID);
		System.out.println(requestID);
		System.out.println("1、------");
		TaskServiceImpl.getInstance().onAsync(session,params,this);
		System.out.println("2、------");
	}
	
	
	/**
	 * 系统异步处理回调服务
	 */
	public void onCallback(ServiceSession session,JSONObject params) {
		System.out.println("2.5、--------------进入异步");
		//初始化区域库存
		Long requestID  = params.getLong("requestID");
		System.out.println(requestID);
		Criteria criteria=Criteria.where("entId").is(session.getEnt_id());
		criteria.and("creator").is(requestID);
		Query query = new Query(criteria);
		query.fields().include("brandCode");
		query.fields().include("brandName");
		query.fields().include("entId");
		query.fields().include("erpCode");
		
		FMybatisTemplate template = getTemplate();
		List<ExtBrandInfoModel> list = this.onFind(session, template, query, this.getBeanClass(), "extbrandinfo");
		if(list!=null){
			for(ExtBrandInfoModel bean : list){
				JSONObject brandparmas = JSON.parseObject(JSON.toJSONString(bean));
				this.addBrand(session,template,brandparmas);
			}
		}
		
		System.out.println("3、------");
	}
	
	
	public void addBrand(ServiceSession session,FMybatisTemplate template,JSONObject paramsObject ){
		Object brandCode = paramsObject.get("brandCode");
//		Object brandName = parmas.get("brandName");
//		Object entId = map.get("entId");
		Object erpCode = paramsObject.get("erpCode");
		Criteria criteria=Criteria.where("entId").is(session.getEnt_id());
		criteria.and("brandCode").is(brandCode).and("erpCode").is(erpCode);
		Query query = new Query(criteria);
		
		Map<String, Object> bean = template.selectOne(query, "brandinfo");
		if(bean==null){
			service.onInsert(session, paramsObject);
		}else{
			paramsObject.put("brandId", bean.get("brandId"));
			service.onUpdate(session, paramsObject);
		}
	}
}
