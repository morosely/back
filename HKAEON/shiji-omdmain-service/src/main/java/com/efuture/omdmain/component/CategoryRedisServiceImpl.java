package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.CategoryModel;
import com.efuture.omdmain.model.SaleOrgModel;
import com.efuture.redis.component.RedisClient;
import com.product.component.RedisCompomentServiceImpl;
import com.product.model.ServiceSession;

public class CategoryRedisServiceImpl extends RedisCompomentServiceImpl<CategoryModel> {

	private static final Logger logger = LoggerFactory.getLogger(ShopServiceRedisImpl.class);
	
	@Autowired // 操作字符串的template，stringRedisTemplate是RedisTemplate的一个子集
	private RedisClient redisClient;
	
	public CategoryRedisServiceImpl(String collectionName, String keyfieldName) {
		super(collectionName, keyfieldName);
	}
	
	public List<CategoryModel> queryCategory(ServiceSession session,JSONObject jsonParam){
		Criteria criteria = Criteria.where("entId").is(jsonParam.getLong("entId")).and("erpCode").is(jsonParam.getString("erpCode"));
		Query query = new Query(criteria);
		query.limit(Integer.MAX_VALUE);
		this.setContainEntId(false);
		return this.onFind(session, this.getTemplate(), query, CategoryModel.class, this.getCollectionName());
	}
	
	public void insertCategory(ServiceSession session,JSONArray categoryList){
		if(categoryList!=null&&categoryList.size()>0){
			List<String> indexFields = new ArrayList<String>();
			indexFields.add("entId");
			indexFields.add("erpCode");
			this.setIndexFields(indexFields);
			for(int i = 0;i < categoryList.size(); i++){
				this.onInsert(session, this.getTemplate(), categoryList.getJSONObject(i), this.getCollectionName());
			}
		}
	}
	
	public void deleteCategory(ServiceSession session,List<Long> idList){
		for(Long categoryId : idList){
			Criteria criteria = Criteria.where("categoryId").is(categoryId);
			Query query = new Query(criteria);
			this.onRemove(session, this.getTemplate(), query, this.getCollectionName());
		}
	}
	 
	  @Override
	  protected RedisClient getTemplate() {
	    return redisClient;
	  }
	  
	 @Override
	  protected int onUpsertd(ServiceSession session, RedisClient template, Query query, Update update,
	      String collectionName) {
	    // TODO Auto-generated method stub
	    return 0;
	  }
	
	  @Override
	  protected int onRemoved(ServiceSession session, RedisClient template, Query query,
	      String collectionName) {
	    // TODO Auto-generated method stub
	    return 0;
	  }
	
	  @Override
	  protected int onUpdate(ServiceSession session, RedisClient template, Query query, Update update,
	      String collectionName) {
	    // TODO Auto-generated method stub
	    return 0;
	  }
}