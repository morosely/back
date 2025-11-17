package com.efuture.omdmain.component;

import com.efuture.omdmain.model.GoodsSpecialModel;
import com.product.component.CommonServiceImpl;
import com.product.storage.template.FMybatisTemplate;

public class GoodsSpecialServiceImpl extends CommonServiceImpl<GoodsSpecialModel,GoodsSpecialServiceImpl>{

	public GoodsSpecialServiceImpl(FMybatisTemplate mybatisTemplate, String collectionName, String keyfieldName) {
		super(mybatisTemplate, collectionName, keyfieldName);
	}


}
