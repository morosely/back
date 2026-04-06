package com.efuture.eshop.inventory.request;

import org.springframework.beans.factory.annotation.Autowired;

import com.efuture.eshop.inventory.model.Goods;
import com.efuture.eshop.inventory.service.GoodsService;

/**
 * 重新加载商品库存的缓存
 * @author Administrator
 *
 */
public class GoodsCacheRefreshRequest implements Request {

	private Long goodsId;
	@Autowired
	private GoodsService goodsService;
    //是否强制刷新缓存
	private boolean forceRefresh;
	
	public GoodsCacheRefreshRequest(Long goodsId,boolean forceRefresh) {
		super();
		this.goodsId = goodsId;
		this.forceRefresh = forceRefresh;
	}
	
	@Override
	public void process() {
		// 从数据库中查询最新的商品库存数量
		Goods goods = goodsService.findGoods(goodsId);
		// 将最新的商品库存数量，刷新到redis缓存中去
		goodsService.setGoodsCache(goods); 
	}

	@Override
	public Long getGoodsId() {
		return goodsId;
	}

	@Override
	public boolean isForceRefresh() {
		return forceRefresh;
	}

}
