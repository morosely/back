package com.efuture.eshop.inventory.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回的类
 * @author yihaitao
 *
 */
public class ResponseData {
	//状态编码 100：成功；200：失败
	private Integer code;
	//提示信息
	private String message;
	//用户要返回给浏览器的数据
	private Map<String,Object> data = new HashMap<String,Object>();
	
	public static ResponseData success(){
		ResponseData rm = new ResponseData();
		rm.setCode(200);
		rm.setMessage("处理成功");
		return rm;
	}
	
	public static ResponseData fail(){
		ResponseData rm = new ResponseData();
		rm.setCode(500);
		rm.setMessage("处理失败");
		return rm;
	}
	
	public ResponseData add(String key,Object value){
		this.getData().put(key, value);
		return this;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
