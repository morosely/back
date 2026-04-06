package com.efuture.domain;

import java.util.Date;

public class MiaoshaGoods {
	private Long id;
	private Long goodsId;
	private Integer stockCount;
	private Double miaoshaPrice;
	private Date startDate;
	private Date endDate;
	public Date getEndDate() {
		return endDate;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public Long getId() {
		return id;
	}
	public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
}
