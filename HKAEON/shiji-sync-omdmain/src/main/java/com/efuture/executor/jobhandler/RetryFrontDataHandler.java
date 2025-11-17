package com.efuture.executor.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.utils.HttpUtils;
import com.product.model.ServiceSession;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@JobHandler("retryFrontDataHandler")
@Component
public class RetryFrontDataHandler extends IJobHandler {

    @Value("${sync.retry.frontdata}")
    String url;
    @Override
    public ReturnT<String> execute(String param) throws Exception {

        if(param == null || "".equals(param)){
            param = "{}";
        }
        JSONObject paramJson = JSON.parseObject(param);
        JSONArray array = paramJson.getJSONArray("tableName");
        if(array == null || array.isEmpty()){
            ReturnT returnT = ReturnT.FAIL;
            returnT.setMsg("格式不正确!");
            return returnT;
        }
        for(int i=0 ; i < array.size(); i++){
            JSONObject syncParam = new JSONObject();
            syncParam.put("tableName",array.get(i));
            CompletableFuture.supplyAsync(()->{
                    try{
                        HttpUtils.onPost(url,ServiceSession.getSession(),syncParam.toJSONString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        return "error";
                    }
                    return "success";
                }
            );
        }

        ReturnT returnT = ReturnT.SUCCESS;
        returnT.setMsg("请求已发送");
        return returnT;
    }
}
