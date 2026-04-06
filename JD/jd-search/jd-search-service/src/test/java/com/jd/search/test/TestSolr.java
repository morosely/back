package com.jd.search.test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {

	private final static String SOLR_URL = "http://192.168.124.4:8080/solr";
	private final static String SOLR_CLUSTER_URL = "192.168.124.4:2191,192.168.124.4:2192,192.168.124.4:2193";
	
	@Test
	public void testSolrCloudAddDocument() throws Exception {
		// 第一步：把solrJ相关的jar包添加到工程中。
		// 第二步：创建一个SolrServer对象，需要使用CloudSolrServer子类。构造方法的参数是zookeeper的地址列表。
		//参数是zookeeper的地址列表，使用逗号分隔
		CloudSolrServer solrServer = new CloudSolrServer(SOLR_CLUSTER_URL);
		// 第三步：需要设置DefaultCollection属性。
		solrServer.setDefaultCollection("collection2");
		// 第四步：创建一SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第五步：向文档对象中添加域
		document.addField("item_title", "测试商品");
		document.addField("item_price", "100");
		document.addField("id", "test001");
		// 第六步：把文档对象写入索引库。
		solrServer.add(document);
		// 第七步：提交。
		solrServer.commit();

	}
	@Test
	public void queryDocumentWithHighLighting() throws Exception {
		// 第一步：创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer(SOLR_URL);
		// 第二步：创建一个SolrQuery对象。
		SolrQuery query = new SolrQuery();
		// 第三步：向SolrQuery中添加查询条件、过滤条件。。。
		query.setQuery("测试");
		//指定默认搜索域
		query.set("df", "item_keywords");
		//开启高亮显示
		query.setHighlight(true);
		//高亮显示的域
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		// 第四步：执行查询。得到一个Response对象。
		QueryResponse response = solrServer.query(query);
		// 第五步：取查询结果。
		SolrDocumentList solrDocumentList = response.getResults();
		System.out.println("查询结果的总记录数：" + solrDocumentList.getNumFound());
		// 第六步：遍历结果并打印。
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = null;
			if (list != null && list.size() > 0) {//搜索结果有高亮
				itemTitle = list.get(0);
			} else {//搜索结果无高亮返还原来的值
				itemTitle = (String) solrDocument.get("item_title");
			}
			System.out.println(itemTitle);
			System.out.println(solrDocument.get("item_price"));
		}
	}
	
	@Test
	public void queryDocument() throws Exception {
		// 第一步：创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer(SOLR_URL);
		// 第二步：创建一个SolrQuery对象。
		SolrQuery query = new SolrQuery();
		// 第三步：向SolrQuery中添加查询条件、过滤条件。。。
		query.setQuery("*:*");
		// 第四步：执行查询。得到一个Response对象。
		QueryResponse response = solrServer.query(query);
		// 第五步：取查询结果。
		SolrDocumentList solrDocumentList = response.getResults();
		System.out.println("查询结果的总记录数：" + solrDocumentList.getNumFound());
		// 第六步：遍历结果并打印。
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
	
	@Test
	public void deleteDocumentById() throws Exception {
		// 第一步：创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer(SOLR_URL);
		// 第二步：调用SolrServer对象的根据id删除的方法。
		solrServer.deleteById("1");
		// 第三步：提交。
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentByQuery() throws Exception {
		SolrServer solrServer = new HttpSolrServer(SOLR_URL);
		solrServer.deleteByQuery("item_title:测试");
		solrServer.commit();
	}
	
	@Test
	public void addDocument() throws Exception {
		// 第一步：把solrJ的jar包添加到工程中。
		// 第二步：创建一个SolrServer，使用HttpSolrServer创建对象。
		SolrServer solrServer = new HttpSolrServer(SOLR_URL);
		// 第三步：创建一个文档对象SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第四步：向文档中添加域。必须有id域，域的名称必须在schema.xml中定义。
		document.addField("id", "test002");
		document.addField("item_title", "测试商品2");
		document.addField("item_price", "299");
		// 第五步：把文档添加到索引库中。
		solrServer.add(document);
		// 第六步：提交。
		solrServer.commit();
	}
	
}
