package com.jd.service;

import java.util.List;

import com.jd.common.pojo.EasyUITreeNode;
import com.jd.common.pojo.JDReturnResult;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCategoryList(long parentId);
	
	JDReturnResult addContentCategory(long parentId, String name);

}
