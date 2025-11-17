package com.efuture.omdmain.component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.product.component.CommonServiceImpl;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.VenderModel;
import com.efuture.omdmain.service.VenderService;
import com.mongodb.DBObject;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class VenderServiceImpl extends CommonServiceImpl<VenderModel,VenderServiceImpl> implements VenderService {
	
    public VenderServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
        super(mybatisTemplate,collectionName, keyfieldName);
    }

    @Override
    public ServiceResponse search(ServiceSession session, JSONObject paramsObject) {
        return onQuery(session, paramsObject);
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }

//    @Override
//    protected FMybatisTemplate getTemplate() {
//        return this.getBean("StorageOperation", FMybatisTemplate.class);
//    }
    
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public ServiceResponse onImportData(ServiceSession session, String params, MultipartFile file){
    	ServiceResponse response = null;
    	FMybatisTemplate template = this.getTemplate();
    	List<Map<String, Object>> dataList = null;
		try {
			dataList = onBeforeImportData(params, file);
		} catch (Exception e) {
			response = new ServiceResponse();
			response.setReturncode(ResponseCode.EXCEPTION);
			response.setData(e.getMessage());
			return response;
		}
		Map<String, Object> map = null;
		JSONObject paramsObject = null;
		Query query = null;
		Map<String, Object> entity = null;
		for (int i = 0; i < dataList.size(); i++) {
			map = dataList.get(i);
			paramsObject = JSONObject.parseObject(JSON.toJSONString(map));
			query = new Query(Criteria.where("entId").is(map.get("entId"))
					.and("erpCode").is(map.get("erpCode"))
					.and("venderCode").is(map.get("venderCode")));
			entity = template.selectOne(query, this.getCollectionName());
			if(entity == null){
				paramsObject.put("creator", session.getUser_name());
				paramsObject.put("createDate", new Date());
				this.onInsert(session, paramsObject);
			}else{
				paramsObject.put("modifier", session.getUser_name());
				paramsObject.put("updateDate", new Date());
				paramsObject.put(this.getKeyfieldName(), entity.get(this.getKeyfieldName()));
				this.onUpdate(session, paramsObject);
			}
		}
		return ServiceResponse.buildSuccess("");
    }
    
}
