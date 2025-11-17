package com.efuture.omdmain.common;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by huangzhengwei on 2018/5/2.
 *
 * @Desciption:
 */
public class ParamsFormat {

    /*
    * @Description: 组装like参数
     * @param paramsList
 * @param paramsObject
    * @return: from {"name":"jack"} to {"name":{"like":"jack"}}
    */
    public static JSONObject formatLike(List<String> paramsList, JSONObject paramsObject){
        for (String params: paramsList
             ) {
            if (paramsObject.containsKey(params)){

                JSONObject likeParams = new JSONObject();
                likeParams.put("like", paramsObject.get(params));
                paramsObject.put(params, likeParams);
            }
        }
        return paramsObject;
    }
}
