package com.efuture.omdmain;

import java.math.BigDecimal;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.GoodsProcessServiceImpl;
import com.efuture.omdmain.config.DataConfiger;
import com.efuture.omdmain.model.GoodsProcessModel;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties({ DataConfiger.class })
public class GoodsProcessServiceTest {

	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private GoodsProcessServiceImpl goodsProcessServiceImpl;

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
		params.put("sgid", 20L);
		params.put("order_field", "createDate");
		params.put("order_direction", "desc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = goodsProcessServiceImpl.search(getSession(), params);
		System.out.println("=========================[商品列表--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[商品列表--查询--test]============================");
	}
	
	/**
	 *  根据商品sgid查询档口加工方法
	 * 
	 * @author  chenp
	 */
	@Test
	public void getDataBySgid() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("sgid", 20L);
		params.put("order_field", "createDate");
		params.put("order_direction", "desc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = goodsProcessServiceImpl.getDataBySgid(getSession(), params);
		System.out.println("=========================[档口加工方法--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口加工方法--查询--test]============================");
	}
	
	
	/**
	 * 档口加工方法-新增
	 * 
	 * @author chenp
	 */
	@Test
	public void add() throws Exception {

		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("sgid",3L);
		params.put("processType","红烧");
		params.put("processFee", 60);
		params.put("minProcessFee", 60);
		
		ServiceResponse response = goodsProcessServiceImpl.add(getSession(), params);
		System.out.println("=========================[档口加工方法新增--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口加工方法新增--test]============================");
	}
	
	
	/**
	 * 档口加工方法-编辑
	 * 
	 * @author chenp
	 */
	@Test
	public void update() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("ssgid", 1601313064353792663L);
		params.put("processFee", 80);
		params.put("minProcessFee", 80);
		ServiceResponse response = goodsProcessServiceImpl.update(getSession(), params);
		System.out.println("=========================[档口加工方法--编辑--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口加工方法--编辑--test]============================");
	}
	
	@Test
	public void update1() throws Exception {

		SpringContext.setInstance(applicationContext);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<GoodsProcessModel> dataList = Arrays.asList(new GoodsProcessModel());

		paramMap.put(goodsProcessServiceImpl.getCollectionName(), dataList);
		for (int i = 0; i < dataList.size(); i++) {
			GoodsProcessModel goodsProcessModel = dataList.get(i);
			goodsProcessModel.setSsgid(1601313064353792663L);
			goodsProcessModel.setProcessType("油炸");
			goodsProcessModel.setProcessFee(new BigDecimal(100));
			goodsProcessModel.setMinProcessFee(new BigDecimal(100));
		}

		String dataUpdate = JSON.toJSONStringWithDateFormat(dataList, "yyyy-MM-dd");
		List<JSONObject> array = JSONArray.parseArray(dataUpdate, JSONObject.class);

		paramMap.put("goodsprocess", array);
		JSONObject params = JSON.parseObject(JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd"));

		ServiceResponse response = goodsProcessServiceImpl.update(getSession(), params);
		System.out.println("=========================[设为冷藏(批量)--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[设为冷藏(批量)--test]============================");
	}
	
}
