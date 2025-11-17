package com.efuture.omdmain.model;

import java.util.List;

public class PaymentMethodTreeBean extends PaymentMethodModel {
	// 构建树形结构
	private List<PaymentMethodTreeBean> children;

	public List<PaymentMethodTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<PaymentMethodTreeBean> children) {
		this.children = children;
	}
}
