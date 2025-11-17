package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ExtVender;
import com.efuture.omdmain.model.VenderModel;
import com.efuture.omdmain.service.ExtSupplierInformationService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:实现类
 */
public class ExtSupplierInformationServiceImpl extends JDBCCompomentServiceImpl<ExtVender> implements ExtSupplierInformationService {
   
	public ExtSupplierInformationServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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
