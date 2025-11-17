package com.efuture.omdmain.component;

import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.ExtGoodsModel;
import com.efuture.omdmain.service.ExtGoodsService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.RowMap;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by huangzhengwei on 2018/5/29.
 *
 * @Desciption:
 */
public class ExtGoodsServiceImpl extends JDBCCompomentServiceImpl<ExtGoodsModel> implements ExtGoodsService {
    public ExtGoodsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
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

    @Override
    protected <T> List<T> onFind(ServiceSession session, FMybatisTemplate template, Query query, Class<T> entityClass, String collectionName) {
        List<T> dataList = super.onFind(session,template,query,entityClass,collectionName);
        if(dataList!=null && !dataList.isEmpty()){
            for(int i = 0;i < dataList.size(); i++){
                RowMap data = (RowMap)dataList.get(i);
                //使用ReferQuery：接口表的品类只能通过categoryCode去查询标识商品，但是categoryCode四，五级会有重复!
                data.put("level",5);
            }
        }
        return dataList;
    }
}
