package com.efuture.executor.jobhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.common.SpringUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("goodsCategoryStockHandler")
@Component
public class GoodsCategoryStockHandler extends IJobHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(GoodsCategoryStockHandler.class);
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		SqlSessionTemplate omdmainTemplate = getOmdmainTemplate();
		//1.删除前7天数据
		omdmainTemplate.delete("GoodsModelMapper.deleteSevenAbroadBuy");
		//2.初始化参数
		String shopCode = "007";//默认缺省值
		Integer pageSize = 1000;//默认缺省值
		Integer id = 0;
		JSONObject executeParam = new JSONObject();
		if(param!=null && param.trim().length()>0) {
			executeParam = JSON.parseObject(param, JSONObject.class);
			String paramShopCode = executeParam.getString("shopCode");
			Integer paramPageSize = executeParam.getInteger("pageSize");
			shopCode = paramShopCode == null ? shopCode : paramShopCode;
			pageSize = paramPageSize == null ? pageSize : paramPageSize;
		}
		executeParam.put("shopCode",shopCode);
		List<Map> list = null;
		boolean loopFlag = true;
		//3.分页批量处理数据
		do {
			executeParam.put("id",id);
			executeParam.put("pageSize",pageSize);
			list = omdmainTemplate.selectList("GoodsModelMapper.broadBuyGoods",executeParam);
			if(list!=null && list.size()>0) {
				List<Map> broadBuyGoodsList = new ArrayList<>();
				for (Map goodsMap : list) {
					String shopCodeRef = (String)goodsMap.get("shopCode");
					if(shopCodeRef!=null && shopCodeRef.trim().length()>0) {
						broadBuyGoodsList.add(goodsMap);
					}
				}
				if(broadBuyGoodsList.size()>0) {
					omdmainTemplate.insert("GoodsModelMapper.abroadBuyGoodsStock",broadBuyGoodsList);
				}
				id = (Integer)((Map)list.get(list.size() - 1)).get("id");
			}else{
				loopFlag = false;
			}
		}while (loopFlag);
		
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
