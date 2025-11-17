package com.efuture.executor.jobhandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.SpringUtil;
import com.efuture.utils.HttpUtils;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("stockHandler")
@Component
public class StockHandler extends IJobHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(StockHandler.class);
	
	@Value("${stock.oms.safestockCategory.categoryInit}")
	private String safeStockURL;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		SqlSessionTemplate omdmainTemplate = getOmdmainTemplate();
		
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Calendar tomorrow = Calendar.getInstance();  
		tomorrow.setTime(today);  
		tomorrow.add(Calendar.DAY_OF_MONTH, 1); 
        
		JSONObject paramJson = new JSONObject();
		paramJson.put("startTime", format.format(today)+" 00:00:00");
		paramJson.put("endTime", format.format(tomorrow.getTime())+" 00:00:00");
		List<JSONObject> list = omdmainTemplate.selectList("CategoryModelMapper.getCategoryStock",paramJson);
		System.out.println("获取库存品类初始化数据 ===== 》》》》》 "+list+"参数："+paramJson.toJSONString());
		
		//根据经营公司 分组发送数据
		JSONObject erpJson = new JSONObject();
		Long entId = null ;
		for (JSONObject json : list) {
			if(null == entId && json.containsKey("entId")) 
				entId = json.getLong("entId");
			if(json.containsKey("erpCode")) {
				String erpCode = json.getString("erpCode");
				JSONArray array;
				if(!erpJson.containsKey(erpCode)) {
					array = new JSONArray();
				}else {
					array = erpJson.getJSONArray(erpCode);
				}
				array.add(json);
				erpJson.put(erpCode, array);
			}
		}
		
		ServiceSession session = new ServiceSession();
	    session.setEnt_id(0);
	    
		JSONObject postParam = new JSONObject();
		postParam.put("entId", entId);
		postParam.put("flag", "I");
		JSONArray  array;
		for (String erpCode : erpJson.keySet()) {
			array = erpJson.getJSONArray(erpCode);
			if(array ==null || array.size() == 0)
				continue;
			postParam.put("erpCode", erpCode);
			postParam.put("details", array);
			try {
		        LOGGER.info("调用库存中心接口，同步品类安全库存映射");
		        String result = HttpUtils.onPost(safeStockURL, session, postParam.toJSONString());
		        if(result == null)
		        	throw new Exception("连接efutrue-stock错误");
		        ServiceResponse response = JSON.parseObject(result, ServiceResponse.class);
		        if(response.getReturncode().equals(ResponseCode.SUCCESS)) {
		        	//成功修改状态
		        	JSONObject json;
		        	for (int i = 0; i < array.size(); i++) {
		        		json = array.getJSONObject(i);
		        		omdmainTemplate.update("CategoryModelMapper.updCategoryStock", json);
					}
		        }
		     } catch (Exception e) {
		        	e.printStackTrace();
		     }
		}
		return new ReturnT<String>("success");
	}

	//正式主数据Session
    public SqlSessionTemplate getOmdmainTemplate() {
	   return SpringUtil.getBean("sqlSessionTemplate", SqlSessionTemplate.class);
	}
    
    //正式库存Session
    public SqlSessionTemplate getStockTemplate() {
	   return SpringUtil.getBean("stockSqlSessionTemplate", SqlSessionTemplate.class);
	}
}
