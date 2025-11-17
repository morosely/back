package com.efuture.omdmain.component;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.efuture.omdmain.service.CategoryManageLevelService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ServiceResponse;
import com.product.storage.template.FMybatisTemplate;
import com.product.util.ParamValidateUtil;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.CategoryManageLevelModel;
import com.efuture.omdmain.model.CategoryModel;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;

public class CategoryManageLevelServiceImpl extends JDBCCompomentServiceImpl<CategoryManageLevelModel> implements CategoryManageLevelService {

	public CategoryManageLevelServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

    @Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

/*	@Override
	protected FMybatisTemplate getTemplate() {
		return this.getBean("StorageOperation", FMybatisTemplate.class);
	}*/
	
	public ServiceResponse selectBuLei(ServiceSession session, JSONObject paramsObject){
		paramsObject.put("entId", session.getEnt_id());
		paramsObject.put("erpCode", "002");  /*erpCode 暂定为002*/
		List<CategoryModel> category = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.selectBuLei", paramsObject);
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), category);
		return ServiceResponse.buildSuccess(result);
	}
	
	public ServiceResponse selectDaLei(ServiceSession session, JSONObject paramsObject){
		ServiceResponse response = ParamValidateUtil.checkParam(session, paramsObject, new String[]{"parentId"});
		if(!ResponseCode.SUCCESS.equals(response.getReturncode())){
			return response;
		}
		
		paramsObject.put("entId", session.getEnt_id());
		paramsObject.put("erpCode", "002"); /*erpCode 暂定为002*/
		List<CategoryModel> category = this.getTemplate().getSqlSessionTemplate().selectList("beanmapper.CategoryModelMapper.selectDaLei", paramsObject);
		
		JSONObject result = new JSONObject();
		result.put(this.getCollectionName(), category);
		return ServiceResponse.buildSuccess(result);
	}

}

















