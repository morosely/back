package com.efuture.omdmain.model;

import java.util.List;

/** 
* @author yihaitao
* @time 2018年5月29日 上午11:06:56 
* 
*/
public class ShowCategoryTreeBean extends ShowCategoryModel {

	// 构建树形结构
	private List<ShowCategoryTreeBean> children;
		
	private String parentName;

	//表示该节点是否被勾选
	private boolean selected;   
	
	public List<ShowCategoryTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<ShowCategoryTreeBean> children) {
		this.children = children;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

    public boolean isSelected() {
      return selected;
    }
  
    public void setSelected(boolean selected) {
      this.selected = selected;
    }
}
