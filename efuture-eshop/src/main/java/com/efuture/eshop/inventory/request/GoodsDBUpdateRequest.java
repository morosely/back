package com.efuture.eshop.inventory.request;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.efuture.eshop.inventory.model.Goods;
import com.efuture.eshop.inventory.service.GoodsService;

/**
 *  cache aside pattern
 * （1）删除缓存
 * （2）更新数据库
 * @author Administrator
 *
 */
public class GoodsDBUpdateRequest implements Request{

	private Goods goods;
	@Autowired
	private GoodsService goodsService;
	
	public GoodsDBUpdateRequest(Goods goods) {
		super();
		this.goods = goods;
	}

	@Override
	@Transactional
	public void process() {
		// 删除redis中的缓存
		goodsService.removeGoodsCache(goods);
		//修改数据库中的库存
		goodsService.updateGoods(goods);
	}

	@Override
	public Long getGoodsId() {
		return goods.getGoodsId();
	}

	@Override
	public boolean isForceRefresh() {
		return false;
	}

}
