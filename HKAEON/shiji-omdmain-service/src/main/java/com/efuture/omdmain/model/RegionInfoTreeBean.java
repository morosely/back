package com.efuture.omdmain.model;

import java.util.List;

public class RegionInfoTreeBean extends RegionInfoModel{
	

	// 构建树形结构
	private List<RegionInfoTreeBean> children;
	
	public List<RegionInfoTreeBean> getChildren() {
		return children;
	}
    
	
	public void setChildren(List<RegionInfoTreeBean> children) {
		this.children = children;
	}
   
	private String treeStatus;//0:不选，1：全选，2：半选

	public String getTreeStatus() {
		return treeStatus;
	}

	public void setTreeStatus(String treeStatus) {
		this.treeStatus = treeStatus;
	}

	// 解决removeAll()方法失效问题
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((treeStatus == null) ? 0 : treeStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegionInfoTreeBean other = (RegionInfoTreeBean) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (treeStatus == null) {
			if (other.treeStatus != null)
				return false;
		} else if (!treeStatus.equals(other.treeStatus))
			return false;
		return true;
	}




}
