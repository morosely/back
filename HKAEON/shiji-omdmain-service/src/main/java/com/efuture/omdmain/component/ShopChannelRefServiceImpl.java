package com.efuture.omdmain.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ShopChannelRefModel;
import com.efuture.omdmain.model.StallInfoModel;
import com.efuture.omdmain.service.ShopChannelRefService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.component.QueryBlankValuePreFilter;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class ShopChannelRefServiceImpl extends JDBCCompomentServiceImpl<ShopChannelRefModel> implements ShopChannelRefService{

	public ShopChannelRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	
	//查询某个渠道下面的所有门店信息
	public ServiceResponse queryShopsByChannel(ServiceSession session, JSONObject paramsObject) throws Exception {
		//1.校验参数
        ServiceResponse result = QueryBlankValuePreFilter.getInstance().checkRequiredField(session, paramsObject,"channelCode");
        if(ResponseCode.Exception.PARAM_IS_EMPTY.equals(result.getReturncode())) return result;
       //2.查询
        List list = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.ShopChannelRefModelMapper.queryShopsByChannel", paramsObject);
		paramsObject.clear();
		paramsObject.put(this.getCollectionName(),list);
		paramsObject.put("total_results",list == null ? 0 : list.size());
		return ServiceResponse.buildSuccess(paramsObject);
	}
	
	public ServiceResponse search(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}

	public ServiceResponse add(ServiceSession session, JSONObject paramsObject) throws Exception {
		this.setUpsert(false);
		paramsObject.put("createDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramsObject.put("creator", session.getUser_name());
		return this.onInsert(session, paramsObject);
	}

	public ServiceResponse delete(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onDelete(session, paramsObject);
	}

	public ServiceResponse update(ServiceSession session, JSONObject paramsObject) throws Exception {
		this.setUpsert(false);
		paramsObject.put("modifier", session.getUser_name());
		paramsObject.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return this.onUpdate(session, paramsObject);
	}
	

}
