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
import com.efuture.omdmain.component.StallInfoServiceImpl;
import com.efuture.omdmain.config.DataConfiger;
import com.efuture.omdmain.model.StallInfoModel;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties({ DataConfiger.class })
public class StallInfoServiceTest {

	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private StallInfoServiceImpl stallInfoServiceImpl;

	public ServiceSession getSession() {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		return session;
	}
	
	/**
	 *  根据组织机构shopId查询档口信息
	 * 
	 * @author  chenp
	 */
	@Test
	public void getDataByShopId() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("shopId", 19L);
		params.put("order_field", "siid");
		params.put("order_direction", "asc");
		params.put("page_no", 1);
		params.put("page_size", 50);

		ServiceResponse response = stallInfoServiceImpl.getDataByShopId(getSession(), params);
		System.out.println("=========================[档口信息--查询--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口信息--查询--test]============================");
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
		params.put("siid", 1601323730468864601L);

		ServiceResponse response = stallInfoServiceImpl.delete(getSession(), params);
		System.out.println("=========================[单行删除--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[单行删除--test]============================");
	}
	
	
	/**
	 *  新增
	 * 
	 * @author  chenp
	 */
	@Test
	public void add() throws Exception {
		
		SpringContext.setInstance(applicationContext);
		JSONObject params = new JSONObject();
		params.put("shopId", 19L);
//		params.put("stallCode", "D1223");

		ServiceResponse response = stallInfoServiceImpl.add(getSession(), params);
		System.out.println("=========================[新增--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[新增--test]============================");
	}
	
	
	/**
	 * 档口信息-修改/保存
	 * 
	 * @author chenp
	 */
	@Test
	public void update() throws Exception {

		SpringContext.setInstance(applicationContext);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<StallInfoModel> dataList = Arrays.asList(new StallInfoModel());

		paramMap.put(stallInfoServiceImpl.getCollectionName(), dataList);
		for (int i = 0; i < dataList.size(); i++) {
			StallInfoModel stallInfoModel = dataList.get(i);
			stallInfoModel.setSiid(1601676254380032727L);
			stallInfoModel.setShopId(19L);
			stallInfoModel.setStallName("避风塘");
			stallInfoModel.setPrintAddress("127.0.0.1");
			stallInfoModel.setPrintName("asdfs11");
		}

		String dataUpdate = JSON.toJSONStringWithDateFormat(dataList, "yyyy-MM-dd");
		List<JSONObject> array = JSONArray.parseArray(dataUpdate, JSONObject.class);
//		for (JSONObject o : array) {
//			o.put("_flag", "U");
//		}

		paramMap.put("stallinfo", array);
		JSONObject params = JSON.parseObject(JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd"));

		ServiceResponse response = stallInfoServiceImpl.update(getSession(), params);
		System.out.println("=========================[档口信息修改--test]============================");
		Assert.assertEquals(response.getReturncode(), "0");
		System.out.println(JSON.toJSON(response));
		System.out.println("=========================[档口信息修改--test]============================");
	}
	
}
