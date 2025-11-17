package com.efuture.omdmain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.Mongo;

public class Test3 {

	@Test
	public void test2(){
		 String ip = "192.168.244.65";
		 int port =27170;
		 
		 String dbName = "order";
		 String collName = "order";
		 String collName2 = "order_detail";
		
		 Date date = new Date(2018-1900,6,9);
		 System.out.println(date.toLocaleString());
		 Criteria criteria = Criteria.where("bill_type").is("1").and("timestamp").gte(date);
		 Query query = new Query(criteria);
		 
		 Mongo mongo = new Mongo(ip, port);
		 MongoTemplate template = new MongoTemplate(mongo, dbName);
		 
		 List<Map> orderList = template.find(query, Map.class, collName);
		 List<Map> detailList =null;
		 for(Map orderMap : orderList){
			 Double payable_value = (Double) orderMap.get("payable_value");
			 Double logistics_value = (Double) orderMap.get("logistics_value");
			 String sheetno = (String) orderMap.get("sheetno");
			 criteria = Criteria.where("sheetno").is(orderMap.get("sheetno"));
			 query = new Query(criteria);
			 detailList = template.find(query, Map.class, collName2);
			 Double total = 0.0;
			 for(Map d :detailList){
				 total += (Double)d.get("transaction_value");
			 }
			 if(total + logistics_value != payable_value){
				 System.out.println(sheetno);
			 }
		 }
		 
		 
	}
}
