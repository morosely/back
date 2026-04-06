package com.jd.search.service;

import com.jd.common.pojo.SearchResult;
import com.jd.common.pojo.JDReturnResult;

public interface SearchItemService {

	JDReturnResult importAllItems() throws Exception;
	
	SearchResult search(String queryString, int page, int rows) throws Exception;
	
	JDReturnResult addDocument(long itemId) throws Exception;
}
