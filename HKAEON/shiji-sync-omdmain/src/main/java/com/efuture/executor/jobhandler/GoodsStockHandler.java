package com.efuture.executor.jobhandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.SpringUtil;
import com.efuture.utils.HttpUtils;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("goodsStockHandler")
@Component
public class GoodsStockHandler extends IJobHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(GoodsStockHandler.class);
	
	@Value("${stock.oms.stockPush.goodsChangeCategory}")
	private String goodsStockURL;
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		SqlSessionTemplate omdmainTemplate = getOmdmainTemplate();
		
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		//根据经营公司 门店 分组发送数据
		JSONObject paramJson = new JSONObject();
		paramJson.put("startTime", format.format(today)+" 00:00:00");
		List<JSONObject>  shopCount = omdmainTemplate.selectList("GoodsModelMapper.getShopCount",paramJson);
		Long entId ;
		String erpCode;
		String shopCode;
		for (JSONObject shop : shopCount) {
			entId = shop.getLong("entId");
			erpCode = shop.getString("erpCode");
			shopCode = shop.getString("shopCode");
			
			paramJson.put("entId", entId);
			paramJson.put("erpCode", erpCode);
			paramJson.put("shopCode", shopCode);
			List<JSONObject> list = omdmainTemplate.selectList("GoodsModelMapper.getGoodsStock",paramJson);
			System.out.println("获取变更商品数据 ===== 》》》》》 "+list+"参数："+paramJson.toJSONString());
			if(list != null && list.size() >0) {
				//访问接口
				ServiceSession session = new ServiceSession();
			    session.setEnt_id(0);
			    
			    JSONObject goodsJson = new JSONObject();	//去重
			    List<Long> ids = new ArrayList<Long>();
			    String goodsCode;
			    for (JSONObject json : list) {
			    	ids.add(json.getLong("id"));
			    	goodsCode = json.getString("goodsCode");
			    	goodsJson.put(goodsCode, goodsCode);
				}
			    
			    JSONObject postParam = new JSONObject();
			    postParam.put("erpCode", erpCode);
			    postParam.put("shopCode", shopCode);
			    postParam.put("goods", goodsJson.keySet());
			    
				try {
			        LOGGER.info("调用库存中心接口，同步品类安全库存映射");
			        String result = HttpUtils.onPost(goodsStockURL, session, postParam.toJSONString());
			        if(result == null)
			        	throw new Exception("连接efutrue-stock错误");
			        ServiceResponse response = JSON.parseObject(result, ServiceResponse.class);
			        if(response.getReturncode().equals(ResponseCode.SUCCESS)) {
			        	//成功修改状态
			        	JSONObject json = new JSONObject();
			        	json.put("ids", ids);
			        	omdmainTemplate.update("GoodsModelMapper.updGoodsStock", json);
			        }
			     } catch (Exception e) {
			        	e.printStackTrace();
			     }
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
