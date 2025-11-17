package com.efuture.omdmain.component;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.efuture.omdmain.model.GoodsPropertyExtValueModel;
import com.efuture.omdmain.service.GoodsPropertyExtService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;

/** 
* @author yihaitao
* @time 2018年5月16日 下午5:48:04 
* 
*/
public class GoodsPropertyExtServiceImpl extends JDBCCompomentServiceImpl<GoodsPropertyExtValueModel> implements GoodsPropertyExtService{

	public GoodsPropertyExtServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
