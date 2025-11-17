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
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.ShopChannelRefServiceImpl;
import com.efuture.omdmain.component.ShopServiceImpl;
import com.efuture.omdmain.config.DataConfiger;
import com.efuture.omdmain.model.ShopChannelRefModel;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties({ DataConfiger.class })
public class ShopServiceTest {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private ShopServiceImpl shopServiceImpl;
	@Autowired
	private ShopChannelRefServiceImpl shopChannelRefServiceImpl;

	public ServiceSession getSession() {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		return session;
	}

	/**
	 * 查询门店信息
	 * 
	 * @author chenp
	 */
	@Test
	public void selectShopChannel() throws Exception {

		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("shopId", 1015L);
		params.put("order_field", "createDate");
		params.put("order_direction", "desc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = shopServiceImpl.getShopChannelByShopId(getSession(), params);
		System.out.println("=========================[门店信息--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[门店信息--查询--test]============================");
	}

	
	@Test
	public void select() throws Exception {

		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("shopId", 1015L);
		params.put("order_field", "createDate");
		params.put("order_direction", "desc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = shopServiceImpl.search(getSession(), params);
		System.out.println("=========================[门店信息--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[门店信息--查询--test]============================");
	}
	
	
	 /**
	 * 门店信息-编辑
	 *
	 * @author chenp
	 */
	 @Test
	 public void update() throws Exception {

			SpringContext.setInstance(applicationContext);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			List<ShopChannelRefModel> dataList = Arrays.asList(new ShopChannelRefModel());

			paramMap.put(shopChannelRefServiceImpl.getCollectionName(), dataList);
			for (int i = 0; i < dataList.size(); i++) {
				ShopChannelRefModel model = dataList.get(i);
//				model.setOcrid(1601970033917952063L);
				model.setChannelId(1L);
				model.setShopId(1013L);
			}

			String dataUpdate = JSON.toJSONStringWithDateFormat(dataList, "yyyy-MM-dd");
			List<JSONObject> array = JSONArray.parseArray(dataUpdate, JSONObject.class);
			for (JSONObject o : array) {
				o.put("flag", true);
			}

			paramMap.put("shopChannelRef", array);
			paramMap.put("shopId", 1013L);
			paramMap.put("longitude", "114.323646");
			paramMap.put("latitude", "30.557506");
			paramMap.put("serviceRadiu", 3);
			JSONObject params = JSON.parseObject(JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd"));

			ServiceResponse response = shopServiceImpl.update(getSession(), params);
			System.out.println("=========================[档口信息修改--test]============================");
			Assert.assertEquals(response.getReturncode(), "0");
			System.out.println(JSON.toJSON(response));
			System.out.println("=========================[档口信息修改--test]============================");
		}

}
