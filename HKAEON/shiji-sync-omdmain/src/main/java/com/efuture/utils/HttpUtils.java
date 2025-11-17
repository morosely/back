package com.efuture.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.RestBaseUtils;

public class HttpUtils {

    protected static Log logger = LogFactory.getLog(HttpUtils.class);

    public static String onPost(String url, ServiceSession session, String jsonString) {
        String traceId = "";
        long traceId1 = Long.valueOf(0);

        try {
            JSONObject paramsObject = JSON.parseObject(jsonString);
            if(jsonString.contains("traceId")) {
                traceId1 = Long.valueOf(paramsObject.getString("traceId"));
            }
            traceId = traceId1+"";
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("session", JSON.toJSONString(session));
            String response = RestBaseUtils.onHttpPost(traceId1, 3000, 3000, null, url, paramsMap, jsonString,
                    String.class, null, null);

            //重试一次
            if(response==null) {
                response = RestBaseUtils.onHttpPost(traceId1, 3000, 3000, null, url, paramsMap, jsonString,
                        String.class, null, null);
            }

            // String response=
            // DiscoveryRestUtils.onHttpPost(RestBaseUtils.getRestTemplate(), traceId, 3000,
            // 3000, null, url,Collections.singletonMap("session",
            // JSON.toJSONString(session)), jsonString, String.class,
            // HttpUtils.class);

            return response;
        } catch (Exception e) {
            logger.error("{traceId:" + traceId + "}", e);
            return null;
        }
    }

    /***
     * 调用接http接口返回1个json数组
     *
     * @param url
     * @param session
     * @param json_param
     * @param tableName
     * @return
     */
    public static JSONArray doPost(String url, ServiceSession session, String json_param, String tableName) {
        ServiceResponse response = null;
        String traceId = "";

        try {
            JSONObject paramsObject = JSON.parseObject(json_param);
            traceId = paramsObject.getString("traceId");

            JSONArray array = new JSONArray();
            String respStr = HttpUtils.onPost(url, session, json_param);
            if (respStr != null) {
                response = JSON.parseObject(respStr, ServiceResponse.class);
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+response);
                String returncode = response.getReturncode();
                if (returncode.equals("0")) {
                    JSONObject data = (JSONObject) response.getData();
                    array = data.getJSONArray(tableName);
                }
            }

            // 记录日志
            HttpLog.log(traceId, url, json_param, response);
            return array;

        } catch (Exception e) {
            HttpLog.log(traceId, url, json_param, response);
            logger.error("{traceId:" + traceId + "}", e);
            return new JSONArray();
        }
    }

}
