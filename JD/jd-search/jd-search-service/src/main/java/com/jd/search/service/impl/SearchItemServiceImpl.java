package com.jd.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.common.pojo.SearchItem;
import com.jd.common.pojo.SearchResult;
import com.jd.common.pojo.JDReturnResult;
import com.jd.search.dao.SearchDao;
import com.jd.search.mapper.SearchItemMapper;
import com.jd.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public JDReturnResult importAllItems() throws Exception {
		// 1、查询所有商品数据。
		List<SearchItem> itemList = searchItemMapper.getItemList();
		// 2、创建一个SolrServer对象。
		// 3、为每个商品创建一个SolrInputDocument对象。
		for (SearchItem searchItem : itemList) {
			SolrInputDocument document = new SolrInputDocument();
			// 4、为文档添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name",
					searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			// 5、向索引库中添加文档。
			solrServer.add(document);
		}
		// 提交修改
		solrServer.commit();
		// 6、返回TaotaoResult。
		return JDReturnResult.success();
	}

	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {
		// 1、创建一个SolrQuery对象。
		SolrQuery query = new SolrQuery();
		// 2、设置查询条件
		query.setQuery(queryString);
		// 3、设置分页条件
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		// 4、需要指定默认搜索域。
		query.set("df", "item_title");
		// 5、设置高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		// 6、执行查询，调用SearchDao。得到SearchResult
		SearchResult result = searchDao.search(query);
		// 7、需要计算总页数。
		long recordCount = result.getRecordCount();
		long pageCount = recordCount / rows;
		if (recordCount % rows > 0) {
			pageCount++;
		}
		result.setPageCount(pageCount);
		// 8、返回SearchResult
		return result;
	}
	
	/**
	 * 用于消息队列插入索引库
	 */
	@Override
	public JDReturnResult addDocument(long itemId) throws Exception {
		// 1、根据商品id查询商品信息。
		SearchItem searchItem = searchItemMapper.getItemById(itemId);
		// 2、创建一SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 3、使用SolrServer对象写入索引库。
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
		document.addField("item_desc", searchItem.getItem_desc());
		// 5、向索引库中添加文档。
		solrServer.add(document);
		solrServer.commit();
		// 4、返回成功，返回TaotaoResult。
		return JDReturnResult.success();
	}
}
