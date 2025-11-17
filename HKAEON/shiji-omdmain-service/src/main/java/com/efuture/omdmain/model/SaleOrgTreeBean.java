package com.efuture.omdmain.model;

import java.util.List;

public class SaleOrgTreeBean extends SaleOrgModel {
	
	// 构建树形结构
	private List<SaleOrgTreeBean> children;
	
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<SaleOrgTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<SaleOrgTreeBean> children) {
		this.children = children;
	}
}
