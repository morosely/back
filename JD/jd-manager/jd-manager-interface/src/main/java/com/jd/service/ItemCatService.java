package com.jd.service;

import java.util.List;

import com.jd.common.pojo.EasyUITreeNode;
import com.jd.manager.pojo.CatResult;

public interface ItemCatService {
	
	List<EasyUITreeNode> getItemCatList(long parentId);
	
	CatResult getItemCatMenu();
}
