package com.efuture.executor.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.component.ErrorLogServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 删除无效商品goods，商品经营配置goodsshopref，多条码aeonmorebarno，可售商品salegoods
 */
@JobHandler("deleteGoodsHandler")
@Component
public class DeleteGoodsHandler extends IJobHandler {

    public Logger logger = LoggerFactory.getLogger(DeleteGoodsHandler.class);
    @Autowired
    private ErrorLogServiceImpl service;
    //停止删除操作控制标志位
    public static Boolean stopFlag = false;
    public static void setStopFlag(Boolean stopFlag) {
        DeleteGoodsHandler.stopFlag = stopFlag;
    }
    public static final Integer DEFAULT_TOTAL_COUNT = 1000;//缺省总数量
    public static final Integer DEFAULT_PAGE_SIZE = 50;//缺省每次处理数据
    public static final Integer DEFAULT_SLEEP_TIME = 0;//每处理一批数据后程序休眠时间(单位秒)
    /** 动态定义参数
     * totalCount:需要处理的总数量
     * pageSize:每次删除数量
     * stopFlag:true 停止定时任务
     * sleepTime：每处理一批数据后程序休眠时间(单位秒)
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("【deleteGoods】 -----> 【start】 -----> param:{}",param);
        long start = System.currentTimeMillis();
        //0.处理参数信息
        if(param == null || "".equals(param)){
            param = "{}";
        }
        JSONObject paramJson = JSON.parseObject(param);
        //传参stopFlag人工干预停止任务
        Boolean stopFlagParam = paramJson.getBoolean("stopFlag");
        if(stopFlagParam!=null && stopFlagParam){
            this.setStopFlag(true);
            long end = System.currentTimeMillis();
            logger.info("【deleteGoods】 -----> 【end】 -----> 设置停止标志！costTime:{}",end-start);
            return new ReturnT<String>("停止删除");
        }else{
            this.setStopFlag(false);
        }
        Integer totalCount = paramJson.getInteger("totalCount") == null ? DEFAULT_TOTAL_COUNT : paramJson.getInteger("totalCount");
        Integer pageSize = paramJson.getInteger("pageSize") == null ? DEFAULT_PAGE_SIZE : paramJson.getInteger("pageSize");
        Integer sleepTime = paramJson.getInteger("sleepTime") == null ? DEFAULT_SLEEP_TIME : paramJson.getInteger("sleepTime");
        paramJson.put("pageSize",pageSize);
        paramJson.put("totalCount",totalCount);
        paramJson.put("sleepTime",sleepTime);
        service.deleteGoods(paramJson);
        long end = System.currentTimeMillis();
        logger.info("【deleteGoods】 -----> 【end】 -----> costTime:{}",end-start);
        return new ReturnT("success");
    }
}
