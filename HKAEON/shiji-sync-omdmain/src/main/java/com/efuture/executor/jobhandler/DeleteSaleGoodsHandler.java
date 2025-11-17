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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//功能：删除无效的（状态为-1的）salegoods
@JobHandler("deleteSaleGoodsHandler")
@Component
public class DeleteSaleGoodsHandler extends IJobHandler {

    public Logger logger = LoggerFactory.getLogger(DeleteSaleGoodsHandler.class);
    @Autowired
    private ErrorLogServiceImpl service;
    //停止删除操作控制标志位
    public static Boolean stopFlag = false;
    public static void setStopFlag(Boolean stopFlag) {
        DeleteSaleGoodsHandler.stopFlag = stopFlag;
    }

    /**
     * 动态定义参数
     * intervalMonth:查询间隔月份
     * pageSize:每次删除条数
     * shopCodes:参数配置指定门店，不配置读取shop表有效门店
     * stopFlag:true 停止定时任务
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("【deleteSaleGoods 无效状态status:-1】 【param】----->>> {}",param);
        //0.处理参数信息
        if(param == null || "".equals(param)){
            param = "{}";
        }
        JSONObject paramJson = JSON.parseObject(param);
        //传参stopFlag人工干预停止任务
        Boolean stopFlagParam = paramJson.getBoolean("stopFlag");
        if(stopFlagParam!=null && stopFlagParam){
            this.setStopFlag(true);
            logger.info("【deleteSaleGoods 无效状态status:-1】 【设置停止标志】----->>> 执行结束");
            return new ReturnT<String>("停止删除");
        }else{
            this.setStopFlag(false);
        }
        //1.间隔时间没配缺省3个月
        Integer intervalMonth = paramJson.getInteger("intervalMonth") == null ? 3 : paramJson.getInteger("intervalMonth");
        //计算当前时间点的前三个月时间点
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date endDate = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(endDate);
        rightNow.add(Calendar.MONTH,-intervalMonth);
        Date startDate = rightNow.getTime();
        //2.分页条数
        Integer pageSize = paramJson.getInteger("pageSize") == null ? 100 : paramJson.getInteger("pageSize");
        //3.门店编码
        List shopCodes = paramJson.getJSONArray("shopCodes");
        //4.封装查询参数
        paramJson.put("startDate",formatter.format(startDate));
        paramJson.put("pageSize",pageSize);
        paramJson.put("shopCodes",shopCodes);
        //参数没有门店信息，读取数据库门店
        if(shopCodes == null){
            shopCodes = service.queryShopCode();
        }
        if(shopCodes != null){
            for(Object shopCode :shopCodes){
                paramJson.put("shopCode",shopCode);
                Integer deleteSum = service.deleteSaleGoods(paramJson);
                logger.info("【deleteSaleGoods 无效状态status:-1】 【当前总共】----->>> 当前门店【{}】总共删除无效数据条数: {}",shopCode, deleteSum);
            }
        }
        logger.info("【deleteSaleGoods】 【所有门店任务】----->>> 执行结束");
        return new ReturnT<String>(shopCodes==null?"":shopCodes.toString());
    }
}
