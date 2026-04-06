package com.jd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.common.pojo.JDReturnResult;
import com.jd.search.service.SearchItemService;

@Controller
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/importall")
	@ResponseBody
	public JDReturnResult importAllItems() {
		try {
			JDReturnResult result = searchItemService.importAllItems();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return JDReturnResult.build(500, "导入数据失败");
		}
	}
}
