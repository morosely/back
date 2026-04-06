package com.efuture.eshop.inventory.model;

public class Goods {

	/**
	 * 商品id
	 */
	private Long goodsId;
	
	/**
	 * 库存数量
	 */
	private Integer stock;

	public Goods(Long goodsId, Integer stock) {
		super();
		this.goodsId = goodsId;
		this.stock = stock;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
    
}
