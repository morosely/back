package com.efuture.eshop.inventory.service;

import com.efuture.eshop.inventory.model.Goods;

public interface GoodsService {

	/**
	 * 更新商品库存
	 */
	void updateGoods(Goods goods);
	
	/**
	 * 删除Redis中的商品库存的缓存
	 */
	void removeGoodsCache(Goods goods);
	
	/**
	 * 根据商品id查询商品库存
	 */
	Goods findGoods(Long goodsId);
	
	/**
	 * 设置商品库存的缓存
	 */
	void setGoodsCache(Goods goods);
	
	/**
	 * 获取商品库存的缓存
	 */
	Goods getGoodsCache(Long goodsId);
}
