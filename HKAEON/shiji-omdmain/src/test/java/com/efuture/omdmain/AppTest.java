package com.efuture.omdmain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.component.MyUserServiceImpl;
import com.efuture.omdmain.config.DataConfiger;
import com.efuture.omdmain.model.MyUserModel;
import com.product.model.BeanConstant;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.SpringContext;
import com.product.util.UniqueID;

@EnableConfigurationProperties({DataConfiger.class})
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	@Autowired
	private MyUserServiceImpl service;
	
	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	@Test
	public void onTest01_Insert() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		MyUserModel model = new MyUserModel();
		model.setEnt_id(session.getEnt_id());
		model.setSeqno(UniqueID.getUniqueID(true));
		model.setCode("00001");
		model.setName("0001-name");
		model.setPointvalue(12.1);
		model.setCreatedate(new Date());
		
		String dataInsert = JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd");
		JSONObject params = JSON.parseObject(dataInsert);
		ServiceResponse response = service.onInsert(session, params);
		System.out.println(JSON.toJSON(response));
	}

	@Test
	public void onTest01_InsertBatch() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<MyUserModel> dataList = Arrays.asList(new MyUserModel(),new MyUserModel(),new MyUserModel(),new MyUserModel(),new MyUserModel());
		
		paramMap.put(service.getCollectionName(), dataList);
		for(int i=0;i<dataList.size();i++) {
			MyUserModel model = dataList.get(i);
			model.setEnt_id(session.getEnt_id());
			model.setSeqno(UniqueID.getUniqueID(true));
			model.setCode(String.format("%1$05d", i));
			model.setName(String.format("%1$s-name", model.getCode()));
			model.setPointvalue(12.1);
			model.setCreatedate(new Date());
		}
		
		String dataInsert = JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd");
		JSONObject params = JSON.parseObject(dataInsert);
		ServiceResponse response = service.onInsert(session, params);
		System.out.println(JSON.toJSON(response));
	}
	
	@Test
	public void onTest02_Delete() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		MyUserModel model = new MyUserModel();
		model.setSeqno(UniqueID.getUniqueID(true));
		
		String dataDelete = JSON.toJSONString(model);
		JSONObject params = JSON.parseObject(dataDelete);
		ServiceResponse response = service.onDelete(session, params);
		System.out.println(JSON.toJSON(response));
	}
	
	@Test
	public void onTest02_DeleteBatch() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<MyUserModel> dataList = Arrays.asList(new MyUserModel(),new MyUserModel(),new MyUserModel(),new MyUserModel(),new MyUserModel());
		
		paramMap.put(service.getCollectionName(), dataList);
		for(int i=0;i<dataList.size();i++) {
			MyUserModel model = dataList.get(i);
			model.setSeqno(UniqueID.getUniqueID(true));
		}
		
		String dataDelete = JSON.toJSONString(paramMap);
		JSONObject params = JSON.parseObject(dataDelete);
		ServiceResponse response = service.onDelete(session, params);
		System.out.println(JSON.toJSON(response));
	}

	@Test
	public void onTest03_Update() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		MyUserModel model = new MyUserModel();
		model.setEnt_id(session.getEnt_id());
		model.setSeqno(UniqueID.getUniqueID(true));
		model.setCode("00001");
		model.setName("0001-name1");
		//model.setPointvalue(12.1);
		//model.setCreatedate(new Date());
		
		String dataUpdate = JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd");
		JSONObject params = JSON.parseObject(dataUpdate);
		
		ServiceResponse response = service.onUpdate(session, params);
		System.out.println(JSON.toJSON(response));
	}
	
	@Test
	public void onTest03_UpdateBatch() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<MyUserModel> dataList = Arrays.asList(new MyUserModel(),new MyUserModel(),new MyUserModel(),new MyUserModel(),new MyUserModel());
		
		paramMap.put(service.getCollectionName(), dataList);
		for(int i=0;i<dataList.size();i++) {
			MyUserModel model = dataList.get(i);
			model.setEnt_id(session.getEnt_id());
			model.setSeqno(UniqueID.getUniqueID(true));
			model.setCode("00001");
			model.setName("0001-name1");
			//model.setPointvalue(12.1);
			//model.setCreatedate(new Date());
		}
		
		String dataUpdate = JSON.toJSONStringWithDateFormat(paramMap, "yyyy-MM-dd");
		JSONObject params = JSON.parseObject(dataUpdate);
		
		ServiceResponse response = service.onUpdate(session, params);
		System.out.println(JSON.toJSON(response));
	}

	@Test
	public void onTest04_Query() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		MyUserModel model = new MyUserModel();
		model.setCode("00001");
		
		String dataQuery = JSON.toJSONString(model);
		JSONObject params = JSON.parseObject(dataQuery);
		
		ServiceResponse response = service.onQuery(session, params);
		System.out.println(JSON.toJSON(response));
	}
	
	@Test
	public void onTest04_QueryCustom() {
    	// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		// 设置返回字段         ——可无此参数，缺省返回全部字段
		paramsMap.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "code,name,pointvalue");
		// 设置分页参数(请求分页) ——可无此参数，缺省第一页
		paramsMap.put(BeanConstant.QueryField.PARAMKEY_PAGENO, 1);
		// 设置分页参数(分页大小) ——可无此参数，缺省40行
		paramsMap.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, 50);
		// 设置排序参数(排序字段) ——可无此参数
		paramsMap.put(BeanConstant.QueryField.PARAMKEY_ORDERFLD, "seqno,name");
		// 设置排序参数(排序方式) ——可无此参数
		paramsMap.put(BeanConstant.QueryField.PARAMKEY_ORDERDIR, "asc,desc");

		// 设置等于参数 ——可无此参数，缺省查询全部
		paramsMap.put("code", "00001");
		
//		// 设置字段条件
//		Map<String,Object> paramsGt = new HashMap<String,Object>();
//		paramsGt.put(">", "0001");
//		
//		// 设置字段条件
//		Map<String,Object> paramsGL = new HashMap<String,Object>();
//		paramsGL.put(">", "0001");
//		paramsGL.put("<", "0002");
//
//		// 设置大于参数,说明，如果上面有code参数，上面的code的条件会被覆盖
//		paramsMap.put("code", paramsGt);
//		
//		// 设置大于，且小于参数,说明，如果上面有code参数，上面的code的条件会被覆盖
//		paramsMap.put("code", paramsGL);

		String dataQuery = JSON.toJSONString(paramsMap);
		// {"code":{"<":"0002",">":"0001"},"fields":"code,name,pointvalue","order_direction":"asc,desc","order_field":"seqno,name","page_no":1,"page_size":50}
		JSONObject params = JSON.parseObject(dataQuery);
		
		ServiceResponse response = service.onQuery(session, params);
		System.out.println(JSON.toJSON(response));
	}
	
	@Test
	public void onTest05_MyBatis() {
		DateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");

		// 注入上下文
    	SpringContext.setInstance(applicationContext);
		
		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		session.setUser_id(0L);
		session.setUser_code("efuture");
		session.setUser_name("系统管理员");
		
		JSONObject params = new JSONObject();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		params.put(service.getCollectionName(), dataList);

		Long seqno = UniqueID.getUniqueID(true);
		for(int i=0;i<10;i++) {
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataList.add(dataMap);
			
			// 设置值
			dataMap.put("ent_id"     , session.getEnt_id());
			dataMap.put("code"       , String.format("%1$05d",i));
			dataMap.put("name"       , String.format("%1$s-name", dataMap.get("code")));
			dataMap.put("pointvalue" , 12.1);
			dataMap.put("createdate" , formatdate.format(new Date()));

			if (i>3) {
				seqno = UniqueID.getUniqueID(true);
				dataMap.put("_flag"  , "I");
			} 
			
			if (i == 0) {
				dataMap.put("_flag"  , "I");
			} else if (i == 1) {
				dataMap.put("_flag"  , "D");
			} else if (i == 2) {
				dataMap.put("_flag"  , "I");
			} else if (i == 3) {
				dataMap.put("_flag"  , "U");
				dataMap.put("name"   , String.format("%1$s-update", dataMap.get("code")));
			}
			dataMap.put("seqno"      , seqno);
		}
		
		ServiceResponse response = service.onMyBatisIUD(session, params);
		System.out.println(JSON.toJSON(response));
	}
}
