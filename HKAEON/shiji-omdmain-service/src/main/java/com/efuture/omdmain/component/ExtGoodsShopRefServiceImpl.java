package com.efuture.omdmain.component;

import com.efuture.omdmain.model.ExtGoodsShopRefModel;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ExtGoodsShopRefServiceImpl extends JDBCCompomentServiceImpl<ExtGoodsShopRefModel> {

	public ExtGoodsShopRefServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}

	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}
}
