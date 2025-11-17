package com.efuture.executor.jobhandler;

import com.alibaba.fastjson.JSONObject;
import com.efuture.component.GoodsShopRefServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@JobHandler("maxGoodsSalePriceHandler")
@Component
public class MaxGoodsSalePriceHandler extends IJobHandler {

    private final Logger logger = LoggerFactory.getLogger(MaxGoodsSalePriceHandler.class);
    @Autowired
    private GoodsShopRefServiceImpl service;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        this.logger.info("【定时任务刷新商品最大价格】 ==========》》》初始化goods的saleprice字段 Start...");
		long start = System.currentTimeMillis();
        List<String> distinctGoodsCode = service.getTemplate().selectList("GoodsModelMapper.distinctGoodsCode");
        int  initGoodsSalePriceCount = 0;
        if(distinctGoodsCode!=null && !distinctGoodsCode.isEmpty()){
            JSONObject executeParam = new JSONObject();
            executeParam.put("distinctGoodsCode",distinctGoodsCode);
            initGoodsSalePriceCount = service.getTemplate().update("GoodsModelMapper.initGoodsSalePrice",executeParam);
        }

		long end = System.currentTimeMillis();
		this.logger.info("【定时任务刷新商品最大价格】==========》》》初始化goods的saleprice字段End... 更新数量:{},时间:{}",initGoodsSalePriceCount,(end-start));
        return ReturnT.SUCCESS;
    }

}
