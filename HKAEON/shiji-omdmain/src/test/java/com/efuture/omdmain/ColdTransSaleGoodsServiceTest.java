package com.efuture.omdmain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.ColdTransSaleGoodsServiceImpl;
import com.efuture.omdmain.config.DataConfiger;
import com.efuture.omdmain.model.GoodsModel;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties({ DataConfiger.class })
public class ColdTransSaleGoodsServiceTest {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private ColdTransSaleGoodsServiceImpl coldTransSaleGoodsServiceImpl;

	public ServiceSession getSession() {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		return session;
	}
	
	/**
	 *  查询商品列表
	 * 
	 * @author  chenp
	 */
	@Test
	public void search() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("order_field", "createDate");
		params.put("order_direction", "desc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = coldTransSaleGoodsServiceImpl.search(getSession(), params);
		System.out.println("=========================[商品列表--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[商品列表--查询--test]============================");
	}
	
	/**
	 * 设为冷藏(批量)
	 * 
	 * @author chenp
	 */
	@Test
	public void coldsetY() throws Exception {

		SpringContext.setInstance(applicationContext);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<GoodsModel> dataList = Arrays.asList(new GoodsModel());

		paramMap.put(coldTransSaleGoodsServiceImpl.getCollectionName(), dataList);
		for (int i = 0; i < dataList.size(); i++) {
			GoodsModel goodsModel = dataList.get(i);
			goodsModel.setSgid(10L);
//			goodsModel.setColdTransFlag(true);
		}

		String dataUpdate = JSON.toJSONStringWithDateFormat(dataList, "yyyy-MM-dd");
		List<JSONObject> array = JSONArray.parseArray(dataUpdate, JSONObject.class);

		paramMap.put("goods", array);
		paramMap.put("coldTransFlag", false);
		JSONObject params = JSON.parseObject(JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd"));

		ServiceResponse response = coldTransSaleGoodsServiceImpl.coldset(getSession(), params);
		System.out.println("=========================[设为冷藏(批量)--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[设为冷藏(批量)--test]============================");
	}
	
}
