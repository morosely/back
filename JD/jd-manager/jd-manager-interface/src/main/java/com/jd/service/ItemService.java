package com.jd.service;

import com.jd.common.pojo.EasyUIDataGridResult;
import com.jd.common.pojo.JDReturnResult;
import com.jd.pojo.TbItem;
import com.jd.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(Integer page, Integer rows);
	TbItemDesc getItemDescById(long itemId);
	JDReturnResult addItem(TbItem item, String desc, String itemParams);

}
