package com.efuture.omdmain.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.omdmain.model.MyUserModel;
import com.efuture.omdmain.service.MyUserService;
import com.google.common.collect.ImmutableMap;
import com.mongodb.DBObject;
import com.product.component.JDBCCompomentServiceImpl;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.storage.template.FMybatisTemplate;

/**
 * 注意：
 * 1、需要配置【resources/mybatis-config.xml】
 *    参考如下：
 *		<mappers>
 *			<!-- mapper start -->
 *			<mapper resource="beanmapper/DynQuery.xml" /> 这行是配置动态SQL转换用
 *			其它..
 *			<!-- mapper end -->
 *		</mappers>
 *     
 * 2、需要配置【resources/beanmapper/DynQuery.xml文件】
 *    参考如下：
 *	    <mapper namespace="mybatis.sql">	
 *	    	<select id="select" parameterType="String" resultType="MapCase">  <--动态生成语句的查询
 *	        	${value}
 *	    	</select>
 *      
 *	    	<select id="insert" parameterType="String" resultType="MapCase">  <--动态生成语句的插入
 *	        	${value}
 *	    	</select>
 *      
 *	    	<select id="update" parameterType="String" resultType="MapCase">  <--动态生成语句的更新
 *	        	${value}
 *	    	</select>
 *      
 *	    	<select id="delete" parameterType="String" resultType="MapCase">  <--动态生成语句的删除
 *	        	${value}
 *	    	</select>
 *	    </mapper>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * create user mydata identified by efuture;
 * grant dba to mydata;
 * 
 * drop table mydata.myuser;
 * create table mydata.myuser 
 * (
 *    ent_id     NUMBER(20,0),
 *    seqno      NUMBER(20,0), 
 *    code       VARCHAR2(20), 
 *    name       VARCHAR2(50), 
 *    pointvalue DECIMAL, 
 *    createdate DATE
 *  );
 * 
 * insert into mydata.myuser values(1,1,'0001','张三',123.12,TO_DATE('2017-01-03','YYYY-MM-DD'));
 * insert into mydata.myuser values(1,2,'0002','李四',133.39,TO_DATE('2017-09-02','YYYY-MM-DD'));	
 * 
 * commit;
 * 
 * @param collectionName
 * @param keyfieldName
 */
public class MyUserServiceImpl extends JDBCCompomentServiceImpl<MyUserModel> implements MyUserService {
	/**
	 * 构造函数，用于描述此服务保存的表名和关键字，参考【com.efuture.omdmain.config.DataConfiger】中配置
	 * @param collectionName  保存到什么表
	 * @param keyfieldName    保存表关键字【全局唯一主键，long类型字段】
	 */
	public MyUserServiceImpl(FMybatisTemplate mybatisTemplate,String collectionName, String keyfieldName) {
		super(mybatisTemplate,collectionName, keyfieldName);
	}

	// 当Update参数带条件时，此方法需要自行实现，因为mongodb的$set、$setOnInsert，需要处理及Query可能是表达式
	@Override
	protected DBObject onBeforeRowInsert(Query query, Update update) {
		return this.onDefaultRowInsert(query, update);
	}

	// 获取数据库链接，名字是在【com.efuture.omdmain.config.DataConfiger】中配置的
//	@Override
//	protected FMybatisTemplate getTemplate() {
//		return this.getBean("StorageOperation", FMybatisTemplate.class);
//	}
	
	private Map<String,Object> onFieldMap(Map<String,Object> dataMap,Map<String,String> fieldMap) {
		Map<String,Object> responseMap = new LinkedHashMap<String,Object>();
		Set<String> keySet = fieldMap.keySet();
		for (String key:keySet) {
			if (dataMap.containsKey(key)) {
				responseMap.put(fieldMap.get(key), dataMap.get(key));
			}
		}
		return responseMap;
	}
	
	public ServiceResponse onMyBatisIUD(ServiceSession session,JSONObject paramsObject) {
		// 参数检查
		if (session == null) return ServiceResponse.buildFailure(session,ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(paramsObject)) return ServiceResponse.buildFailure(session,ResponseCode.Exception.PARAM_IS_EMPTY);
		
		if (!paramsObject.containsKey(this.getCollectionName())) {
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "onMyBatisIUD请求参数必须包含参数[{1}]", this.getCollectionName());
		}
		
		JSONArray dataList = paramsObject.getJSONArray(this.getCollectionName());
		
		for (int i=0;i<dataList.size();i++) {
			JSONObject dataMap = dataList.getJSONObject(i);
			
			String dataFlag  = (String) dataMap.get("_flag");
			if ("I".equalsIgnoreCase(dataFlag)) {
				Map<String,Object> dataRow = this.onFieldMap(dataMap,ImmutableMap.of("seqno","seqno", "code","code", "name", "name", "pointvalue", "pointvalue", "createdate", "createdate"));
				dataMap.clear();
				dataMap.putAll(dataRow);

				String result = this.onMyBatisInsert(session, dataMap);
				System.out.println(result);
			} else if ("U".equalsIgnoreCase(dataFlag)) {
				Map<String,Object> dataRow = this.onFieldMap(dataMap,ImmutableMap.of("seqno","seqno", "code","code", "name", "name", "pointvalue", "pointvalue", "createdate", "createdate"));
				dataMap.clear();
				dataMap.putAll(dataRow);

				String result = this.onMyBatisUpdate(session, dataMap);
				System.out.println(result);
			} else if ("D".equalsIgnoreCase(dataFlag)) {
				Map<String,Object> dataRow = this.onFieldMap(dataMap,ImmutableMap.of("seqno","seqno"));
				dataMap.clear();
				dataMap.putAll(dataRow);

				String result = this.onMyBatisDelete(session, dataMap);
				System.out.println(result);
			} else {
				String result = this.onMyBatisQuery(session, dataMap);
				System.out.println(result);
			}
		}
		return ServiceResponse.buildSuccess("");
	}
	
	/**
	 * 插入数据
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	private String onMyBatisInsert(ServiceSession session,JSONObject paramsMap) {
		FMybatisTemplate template = this.getTemplate();
		int resultState = template.getSqlSessionTemplate().insert("insert_myuser", paramsMap);
		return String.format("insert_myuser Result:%1$d",resultState);
	}
	
	private String onMyBatisUpdate(ServiceSession session,JSONObject paramsMap) {
		FMybatisTemplate template = this.getTemplate();
		int resultState = template.getSqlSessionTemplate().update("update_myuser", paramsMap);
		return String.format("update_myuser",resultState);
	}

	private String onMyBatisDelete(ServiceSession session,JSONObject paramsMap) {
		FMybatisTemplate template = this.getTemplate();
		int resultState = template.getSqlSessionTemplate().delete("delete_myuser", paramsMap);
		return String.format("delete_myuser Result:%1$d",resultState);
	}

	private String onMyBatisQuery(ServiceSession session,JSONObject paramsMap) {
		FMybatisTemplate template = this.getTemplate();
		Map<String, Object> responseMap = template.getSqlSessionTemplate().selectOne("query_myuser", paramsMap);
		if (responseMap == null || responseMap.size() == 0 || !responseMap.containsKey("name")) {
			return String.format("你的Jdbc请求参数是:%1$s 记录数据:%2$s",paramsMap.toJSONString(),"query_myuser解析失败！");
		} else {
			return responseMap.get("name").toString();
		}
	}
	
	/**
	 * http://localhost:8112/rest?method=testJdbc1.onTestTransaction&session={"ent_id":0}
	 * {}
	 * @param session
	 * @param paramsObject
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String onTestTransaction(ServiceSession session,JSONObject paramsObject) {
		// 1、先插入，后报异常，确认OK
		JSONObject paramsInsert = new JSONObject();
		
		Random random = new Random(); // 注意：如果Random给参数，产生重复的概率很大
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for (int i=6;i<10;i++) {
			Map<String,Object> paramsMap = new HashMap<String,Object>();
			dataList.add(paramsMap);
			
			paramsMap.put("ent_id", 0L); // 会被session中的ent_id替换
			paramsMap.put("seqno", i);
			paramsMap.put("code", String.format("%1$05d", i));
			paramsMap.put("name", String.format("%1$05d-名称", i));
			paramsMap.put("pointvalue", Math.round(random.nextDouble()*100)/100);
			paramsMap.put("createdate", new Date());
		}
		paramsInsert.put("mydata.myuser", dataList);
		
		if (session == null) {
			session = new ServiceSession();
			session.setEnt_id(0L);
			session.setUser_id(0L);
			session.setUser_name("钱海兵");
		}
		
		System.out.println(JSON.toJSONStringWithDateFormat(paramsInsert, "yyyy-MM-dd hh:mm:ss"));
		ServiceResponse response = null;
		try {
			response = this.onUpdate(session, paramsInsert);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		// 从0到6，其中6重复，应该全回滚掉
		dataList.clear();
		for (int i=0;i<7;i++) {
			Map<String,Object> paramsMap = new HashMap<String,Object>();
			dataList.add(paramsMap);
			
			paramsMap.put("ent_id", 0L); // 会被session中的ent_id替换
			paramsMap.put("seqno", i);
			paramsMap.put("code", String.format("%1$05d", i));
			if (i<4) {
				// 第六个名称为空，报错
				paramsMap.put("name", String.format("%1$05d-名称", i));
			} else {
				// 用于控制数据库出错
				paramsMap.put("name", null);
			}
			paramsMap.put("pointvalue", Math.round(random.nextDouble()*100)/100);
			paramsMap.put("createdate", new Date());
		}

		System.out.println(JSON.toJSONStringWithDateFormat(paramsInsert, "yyyy-MM-dd hh:mm:ss"));
		/**
		 * 由于捕获了，错误，则会存储于数据库中记录为：0、1、2、3、6、7、8、9，缺【4、5】
		 * 如果不捕获错误，则数据库中一条都不会存在
		 */
		try {
			response = this.onUpdate(session, paramsInsert);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return JSON.toJSONString(response);
	}
}
