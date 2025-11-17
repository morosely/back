package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ExtBrandInfoModel;
import com.efuture.omdmain.service.ExtBrandInfoService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public class ExtBrandInfoServiceImpl extends JDBCCompomentServiceImpl<ExtBrandInfoModel> implements ExtBrandInfoService {
    public ExtBrandInfoServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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

}
