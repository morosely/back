package com.efuture.omdmain.model;

import java.util.List;

/** 
* @author yihaitao
* @time 2018年5月28日 下午7:28:37 
* 
*/
public class CategoryTreeBean extends CategoryModel {
	
	//profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
	private String categoryCodeAndLevel;
	private String parentCodeAndLevel;
	
	public String getCategoryCodeAndLevel() {
		return categoryCodeAndLevel;
	}


	public void setCategoryCodeAndLevel(String categoryCodeAndLevel) {
		this.categoryCodeAndLevel = categoryCodeAndLevel;
	}


	public String getParentCodeAndLevel() {
		return parentCodeAndLevel;
	}


	public void setParentCodeAndLevel(String parentCodeAndLevel) {
		this.parentCodeAndLevel = parentCodeAndLevel;
	}

	// 构建树形结构
	private List<CategoryTreeBean> children;
	
	public List<CategoryTreeBean> getChildren() {
		return children;
	}
    
	
	public void setChildren(List<CategoryTreeBean> children) {
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
		CategoryTreeBean other = (CategoryTreeBean) obj;
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
