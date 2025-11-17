package com.efuture.omdmain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.efuture.omdmain.component.GoodsServiceImpl;
import com.efuture.omdmain.component.GoodsShopRefServiceImpl;
import com.efuture.omdmain.component.RegionInfoServiceImpl;
import com.efuture.omdmain.component.SaleGoodsServiceImpl;
import com.efuture.omdmain.model.GoodsModel;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;

//@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
public class regionInfoTest {
	
	@Autowired
	private RegionInfoServiceImpl service;
	
	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	GoodsServiceImpl goods;
	
	@Autowired
	SaleGoodsServiceImpl salegoods;
	
	@Autowired
	GoodsShopRefServiceImpl goodshop;
	
	@Test
	public void onTest01_Insert() {
		// 注入上下文
    	/*SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		List list = new ArrayList();
			RegionInfoModel model = new RegionInfoModel();
			model.setEntId(session.getEnt_id());
			model.setCreateDate(new Date());
			model.setLeafFlag(false);
			model.setCreator(session.getUser_code());
			model.setLevel(0);
			long id = UniqueID.getUniqueID(true);
			model.setRegionId(id);
			model.setRegionName("中国");
			model.setStatus(1);
			model.setRegionCode("01");
			String dataInsert = JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd");
			JSONObject params = JSON.parseObject(dataInsert);
			list.add(params);
			
			RegionInfoModel model0 = new RegionInfoModel();
			model0.setEntId(session.getEnt_id());
			model0.setCreateDate(new Date());
			model0.setLeafFlag(false);
			model0.setCreator(session.getUser_code());
			model0.setLevel(1);
			long id0 = UniqueID.getUniqueID(true);
			model0.setRegionId(id0);
			model0.setRegionName("湖北");
			model0.setStatus(1);
			model0.setRegionCode("0105");
			model0.setParentCode("01");
			model0.setParentId(id);
			list.add(JSON.parseObject(JSON.toJSONStringWithDateFormat(model0, "yyyy-MM-dd")));
			
			RegionInfoModel model1 = new RegionInfoModel();
			model1.setEntId(session.getEnt_id());
			model1.setCreateDate(new Date());
			model1.setLeafFlag(false);
			model1.setCreator(session.getUser_code());
			model1.setLevel(2);
			long id1 = UniqueID.getUniqueID(true);
			model1.setRegionId(id1);
			model1.setRegionName("武汉");
			model1.setStatus(1);
			model1.setRegionCode("010510");
			model1.setParentCode("0105");
			model1.setParentId(id0);
			list.add(JSON.parseObject(JSON.toJSONStringWithDateFormat(model1, "yyyy-MM-dd")));
			
			RegionInfoModel model2 = new RegionInfoModel();
			model2.setEntId(session.getEnt_id());
			model2.setCreateDate(new Date());
			model2.setLeafFlag(true);
			model2.setCreator(session.getUser_code());
			model2.setLevel(3);
			long id2 = UniqueID.getUniqueID(true);
			model2.setRegionId(id2);
			model2.setRegionName("武汉区");
			model2.setStatus(1);
			model2.setRegionCode("01051002");
			model2.setParentCode("010510");
			model2.setParentId(id1);
			list.add(JSON.parseObject(JSON.toJSONStringWithDateFormat(model2, "yyyy-MM-dd")));
		
		JSONObject param = new JSONObject();
		param.put("regionInfo", list);
		
		ServiceResponse response = service.onInsert(session, param);
		System.out.println(JSON.toJSON(response));
		*/
		
	}
	
	@Test
	public void onTest02_Insert() {
		// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		long EntId = 0L;
		String erpCode = "002";
		int goodsType = 12;
		boolean directFromErp = true;
		boolean singleItemFlag = true;
		GoodsModel gbean = new GoodsModel();
		
	}

}
