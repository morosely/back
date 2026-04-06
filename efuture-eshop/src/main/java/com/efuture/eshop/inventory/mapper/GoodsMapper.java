package com.efuture.eshop.inventory.mapper;

import org.apache.ibatis.annotations.Param;

import com.efuture.eshop.inventory.model.Goods;

/**
 * 库存数量Mapper
 * @author Administrator
 *
 */
public interface GoodsMapper {

	/**
	 * 更新库存数量
	 * @param inventoryCnt 商品库存
	 */
	void updateGoods(Goods goods);
	
	/**
	 * 根据商品id查询商品库存信息
	 * @param productId 商品id
	 * @return 商品库存信息
	 */
	Goods findGoods(@Param("goodsId") Long goodsId);
}
