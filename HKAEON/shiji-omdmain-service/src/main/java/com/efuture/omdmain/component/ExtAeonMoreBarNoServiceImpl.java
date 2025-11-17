package com.efuture.omdmain.component;


import com.efuture.omdmain.model.ExtAeonMoreBarNoModel;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ExtAeonMoreBarNoServiceImpl extends JDBCCompomentServiceImpl<ExtAeonMoreBarNoModel> {

    public ExtAeonMoreBarNoServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }
}
