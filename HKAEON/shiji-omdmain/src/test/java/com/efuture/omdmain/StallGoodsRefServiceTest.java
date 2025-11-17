package com.efuture.omdmain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.StallGoodsRefServiceImpl;
import com.efuture.omdmain.config.DataConfiger;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties({ DataConfiger.class })
public class StallGoodsRefServiceTest {

	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private StallGoodsRefServiceImpl stallGoodsRefServiceImpl;

	public ServiceSession getSession() {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		return session;
	}
	
	/**
	 *  根据档口信息siid查询档口经营商品信息
	 * 
	 * @author  chenp
	 */
	@Test
	public void getDataBySiid() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("siid", 15L);
		params.put("shopId", 19L);
		params.put("order_field", "createDate");
		params.put("order_direction", "desc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = stallGoodsRefServiceImpl.getDataBySiid(getSession(), params);
		System.out.println("=========================[档口经营商品列表--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口经营商品列表--查询--test]============================");
	}
	
	
	/**
	 *  单行删除
	 * 
	 * @author  chenp
	 */
	@Test
	public void delete() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("sgrid", 2L);

		ServiceResponse response = stallGoodsRefServiceImpl.delete(getSession(), params);
		System.out.println("=========================[单行删除--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[单行删除--test]============================");
	}
	
	
	/**
	 * 新增
	 * 
	 * @author chenp
	 */
	@Test
	public void add() throws Exception {

		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("shopId", 1015L);
		params.put("siid", 1601676254380032727L);
		params.put("goodsCode", "764298");
		params.put("ssgid", 1601851788099625698L);

		ServiceResponse response = stallGoodsRefServiceImpl.add(getSession(), params);
		System.out.println("=========================[档口经营商品新增--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口经营商品新增--test]============================");
	}
	
}
