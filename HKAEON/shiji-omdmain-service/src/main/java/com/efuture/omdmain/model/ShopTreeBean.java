package com.efuture.omdmain.model;

import java.util.List;

/**
 * @author yihaitao
 * @time 2018年5月26日 下午10:24:09
 * 
 */
public class ShopTreeBean extends ShopModel {
	// 构建树形结构
	private List<ShopTreeBean> children;
	
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<ShopTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<ShopTreeBean> children) {
		this.children = children;
	}
}
