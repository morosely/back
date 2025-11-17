package com.efuture.omdmain.component;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.efuture.omdmain.model.CategoryLabelRefModel;
import com.efuture.omdmain.service.CategoryLabelRefService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;

/** 
* @author yihaitao
* @time 2018年5月28日 下午5:22:07 
* 类别标签关联
*/
public class CategoryLabelRefServiceImpl extends JDBCCompomentServiceImpl<CategoryLabelRefModel> implements CategoryLabelRefService{

	public CategoryLabelRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}
	
	@Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }

   /* @Override
    protected FMybatisTemplate getTemplate() {
        return this.getBean("StorageOperation", FMybatisTemplate.class);
    }*/

}
