package com.efuture.omdmain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;

public class HttpLog {

	private static final Logger logger = LoggerFactory.getLogger(HttpLog.class);

	/***
	 * http请求日志
	 * 
	 * @param traceId
	 * @param url
	 * @param inParams
	 * @param response
	 */
	public static void log(String traceId, String url, String inParams, ServiceResponse response, long millisecond) {

		logger.info("traceId[{}]-->请求url-->[{}]，入参-->[{}], 耗费时间-->[{}]", traceId, url, inParams, millisecond);
		if (response == null) {
			logger.info("traceId[{}]-->网络异常或接口未正确返回数据", traceId);
		} else {
			if (ResponseCode.SUCCESS.equals(response.getReturncode())) {
				logger.info("traceId[{}]-->成功返回-->{}", traceId, JSON.toJSONString(response));
			} else {
				logger.info("traceId[{}]-->异常返回-->{}", traceId, JSON.toJSONString(response));
			}
		}

	}
	
	/***
	 * 方法请求日志
	 * 
	 * @param traceId
	 * @param url
	 * @param inParams
	 * @param response
	 */
	public static void posLog(String traceId, String method,String inParams, ServiceResponse response) {

		logger.info("traceId[{}]-->请求url-->[{}]，入参-->[{}]", traceId, inParams);
		if (response == null) {
			logger.info("traceId[{}]-[{}]-->未正确返回数据", traceId,method);
		} else {
			if (ResponseCode.SUCCESS.equals(response.getReturncode())) {
				logger.info("traceId[{}]-[{}]-->成功返回-->{}", traceId, method,JSON.toJSONString(response));
			} else {
				logger.info("traceId[{}]-[{}]-->异常返回-->{}", traceId,method, JSON.toJSONString(response));
			}
		}

	}

}
