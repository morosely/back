package com.efuture.omdmain.component;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.efuture.omdmain.model.SaleGoodsModel2;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.storage.template.FMybatisTemplate;

/**
 * 修改salegoods时，为了规避UniqueKey的影响，解决同一条码对应多个商品的问题
 */
public class UpdateSaleGoodsServiceImpl extends JDBCCompomentServiceImpl<SaleGoodsModel2>  {
	
    public UpdateSaleGoodsServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

    @Override
    protected DBObject onBeforeRowInsert(Query query, Update update) {
        return this.onDefaultRowInsert(query, update);
    }
}
