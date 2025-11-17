package com.efuture.omdmain.component;

import com.efuture.omdmain.model.AeonMoreBarNoModel;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class AeonMoreBarNoServiceImpl extends JDBCCompomentServiceImpl<AeonMoreBarNoModel> {

    public AeonMoreBarNoServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
        super(mybatisTemplate, collectionName, keyfieldName);
    }

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }
}
