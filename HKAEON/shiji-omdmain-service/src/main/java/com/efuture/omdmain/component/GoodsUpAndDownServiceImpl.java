package com.efuture.omdmain.component;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.efuture.omdmain.model.GoodsUpAndDownModel;
import com.efuture.omdmain.service.GoodsUpAndDownService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;

public class GoodsUpAndDownServiceImpl  extends JDBCCompomentServiceImpl<GoodsUpAndDownModel> implements GoodsUpAndDownService{

	public GoodsUpAndDownServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
	
}
