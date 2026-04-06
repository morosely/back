package com.jd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.common.pojo.JDReturnResult;
import com.jd.service.ContentService;
import com.jd.pojo.TbContent;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/save")
	@ResponseBody
	public JDReturnResult addContent(TbContent content) {
		JDReturnResult result = contentService.addContent(content);
		return result;
	}
}
