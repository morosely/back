package com.efuture.eshop.inventory.request;

public interface Request {
	void process();
	Long getGoodsId();
	boolean isForceRefresh();
}
