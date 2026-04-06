package com.jd.service;

import java.util.List;

import com.jd.common.pojo.JDReturnResult;
import com.jd.pojo.TbContent;

public interface ContentService {

	JDReturnResult addContent(TbContent content);

	List<TbContent> getContentList(long cid);

}
