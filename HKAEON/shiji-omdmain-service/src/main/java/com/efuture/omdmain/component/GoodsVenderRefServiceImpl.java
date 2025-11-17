package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.GoodsVenderRefModel;
import com.efuture.omdmain.model.SaleGoodsModel;
import com.efuture.omdmain.model.ShopModel;
import com.efuture.omdmain.model.VenderModel;
import com.efuture.omdmain.service.GoodsVenderRefService;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

public class GoodsVenderRefServiceImpl extends JDBCCompomentServiceImpl<GoodsVenderRefModel> implements GoodsVenderRefService{

  public GoodsVenderRefServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
    super(mybatisTemplate,collectionName, keyfieldName);
  }

  @Override
  protected DBObject onBeforeRowInsert(Query query, Update update) {
      return this.onDefaultRowInsert(query, update);
  }

//  @Override
//  protected FMybatisTemplate getTemplate() {
//      return this.getBean("StorageOperation", FMybatisTemplate.class);
//  }
  
	public ServiceResponse getVenderGoods(ServiceSession session, JSONObject paramsObject) throws Exception {
		return this.onQuery(session, paramsObject);
	}
  
}
