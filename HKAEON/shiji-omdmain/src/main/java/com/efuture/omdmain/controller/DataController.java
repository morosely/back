package com.efuture.omdmain.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.product.controller.ProductController;
import com.product.controller.ProductReflect;
import com.product.model.ResponseCode;
import com.product.model.ServiceResponse;
import com.product.model.ServiceSession;
import com.product.util.FileProcessorUtils;
import com.product.util.Utils;

@RestController
public class DataController extends ProductController {
	@Value("${eureka.client.serviceUrl.defaultZone}") String defaultZone;
	
    @RequestMapping("/")
    public String onRoot(HttpServletRequest request){
    		return request.getRequestURI() + " success";
    }
	
    @RequestMapping("/defaultZone")
    public String onDefaultZone(HttpServletRequest request){
    		return defaultZone;
    }

    @RequestMapping({"/rest","/amp-openapi-service/rest"})
    public String onRest(@RequestParam(value="method",required=false) String method, 
			    		 @RequestParam(value="session",required=false) String session, 
			    		 @RequestParam(value="ent_id",required=false) String ent_id, 
			    		 @RequestParam(value="user_id",required=false) String user_id, 
			    		 @RequestParam(value="user_name",required=false) String user_name,
			    		 @RequestParam(value="locale",required=false) String locale,
			    		 @RequestBody String param) {
    	System.out.println("=============>"+param);
		//return this.onRestService(method, session, ent_id, user_id, user_name, locale, param);
		String result = this.onRestService(method, session, ent_id, user_id, user_name, locale, param);
		//解决基类将输出数据中的时间类型强制转换成yyyy-MM-dd HH:mm:ss 将Time类型的数据恢复成HH:mm:ss时分秒格式： "1970-01-01 18:00:00" ->  "18:00:00" add by yihaitao 2024-09-17
		if("omdmain.cateringtime.search".equals(method)){
			try{
				JSONObject response = JSONObject.parseObject(result);
				ValueFilter valueFilter = new ValueFilter() {
					@SneakyThrows
					@Override
					public Object process(Object object, String name, Object value) {
						if("startTime".equals(name) || "endTime".equals(name)){
							if(object != null && object instanceof Map){
								SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = inputFormat.parse(value.toString());
								SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
								String formattedTime = outputFormat.format(date);
								return formattedTime;
							}
						}
						return value;
					}
				};
				return JSON.toJSONString(response,valueFilter);
			}catch (Exception e){
				e.printStackTrace();
				return result;
			}
		}
		return result;
    }
	
	/**
	 * HTTP: HTTP://127.0.0.1:8115/DataAdapter/ServiceSuccess/rest?ent_id=1&method=a.x
	 * POST: {"a":"test"}
	 * 【OK】
	 * {"returncode":"0","data":{"ent_id":"1","method":"a.x","params":"{}"}}
	 */
	@RequestMapping(value={"/DataAdapter/ServiceSuccess/rest"},method=RequestMethod.POST)
	public ServiceResponse onServiceSuccess(@RequestParam(value="ent_id"     ,required=false) long   entID,
			                                @RequestParam(value="method"     ,required=false) String method,
							                @RequestBody String params) {
		Map<String,String> resposneMap = new HashMap<String,String>();
		resposneMap.put("ent_id", Long.toString(entID));
		resposneMap.put("method", method);
		resposneMap.put("params", params);
		ServiceResponse response = ServiceResponse.buildSuccess(resposneMap);
    	System.out.println("onServiceSuccess:"+JSON.toJSONString(response));
		return response;
	}

	/**
	 * HTTP: HTTP://127.0.0.1:8115/DataAdapter/ServiceMessage/rest?ent_id=1&method=a.x
	 * POST: {"a":"test"}
	 * 【OK】
	 * {"returncode":"50000","data":"Error:{\"ent_id\":\"1\",\"method\":\"a.x\",\"params\":\"{}\"}"}
	 */
	@RequestMapping(value={"/DataAdapter/ServiceMessage/rest"},method=RequestMethod.POST)
	public ServiceResponse onServiceMessage(@RequestParam(value="ent_id"     ,required=false) long   entID,
			                                @RequestParam(value="method"     ,required=false) String method,
							                @RequestBody String params) {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(entID);

		Map<String,String> resposneMap = new HashMap<String,String>();
		resposneMap.put("ent_id", Long.toString(entID));
		resposneMap.put("method", method);
		resposneMap.put("params", params);
		ServiceResponse response = ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, "Error:{0}", JSON.toJSONString(resposneMap));
    	System.out.println("onServiceMessage:"+JSON.toJSONString(response));
		return response;
	}

	/**
	 * HTTP: HTTP://127.0.0.1:8115/DataAdapter/ServiceFaiure/rest?ent_id=1&method=a.x
	 * POST: {"a":"test"}
	 * 【OK】
	 * {"method":"a.x","params":"{}","ent_id":"1"} 429 -> TOO_MANY_REQUESTS
	 */
	@RequestMapping(value={"/DataAdapter/ServiceFaiure/rest"},method=RequestMethod.POST)
	public ResponseEntity<Object> onServiceFaiure(@RequestParam(value="ent_id"     ,required=false) long   entID,
			                                      @RequestParam(value="method"     ,required=false) String method,
							                      @RequestBody String params) {
		ServiceSession session = new ServiceSession();
		session.setEnt_id(entID);

		Map<String,String> resposneMap = new HashMap<String,String>();
		resposneMap.put("ent_id", Long.toString(entID));
		resposneMap.put("method", method);
		resposneMap.put("params", params);
		
		HttpHeaders responseHeaders = new HttpHeaders();  
    	responseHeaders.setContentType(MediaType.TEXT_PLAIN);  		
    	ResponseEntity<Object> response = new ResponseEntity<Object>(JSON.toJSON(resposneMap),responseHeaders, HttpStatus.TOO_MANY_REQUESTS);
    	System.out.println("onServiceFaiure:"+JSON.toJSONString(response));
    	return response;
	}
	
	@RequestMapping(value = { "/rest/import" }, method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String onParse(@RequestParam(value = "method", required = false) String method,
			@RequestParam(value = "session", required = false) String session,
			@RequestParam(required = false) String type, @RequestParam(required = false) String content,
			@RequestParam(required = false) String cols,
			@RequestParam(required = false, defaultValue = "\t") String separator,
			@RequestParam(defaultValue = "0") Integer sheetIndex, @RequestParam(defaultValue = "0") int startRow,
			@RequestParam(required = false) Integer endRow,
			@RequestParam(required = false) String params,
			@RequestParam(name = "file", required = false) MultipartFile file) throws Exception {

		JSONObject jo = new JSONObject();
		jo.put("type", type);
		jo.put("cols", cols);
		jo.put("separator", separator);
		jo.put("sheetIndex", sheetIndex);
		jo.put("startRow", Integer.valueOf(startRow));
		jo.put("endRow", endRow);
		jo.put("content", content);

		if(StringUtils.isNotEmpty(params)){
			JSONObject paramObj = JSONObject.parseObject(params);
			jo.putAll(paramObj);
		}
		
		ProductReflect rcm = new ProductReflect();
		//String param = "{type: \"excel\", cols: \"code,cn_name,remark,en_name,dict_type\", sheetIndex: 0, startRow: 0, endRow: 3}";
		//Object retdata = rcm.executeClassMethodForMultipartFileParam(method, session, jo, file);
		try{
			Object retdata = rcm.executeClassMethodForMultipartFileParam(method,null,session, jo.toJSONString(), file);
			if (retdata == null) {
				return "";
			} else if (retdata instanceof String) {
				return (String) retdata;
			} else {
				return JSON.toJSONString(Utils.toNormalJSONObject(retdata));
			}
		}catch(Exception ex){
			return JSON.toJSONString(ServiceResponse.buildFailure(null,ResponseCode.FAILURE,Utils.getLastExceptionMessage(ex)));
		}

	}

	/***
	 * 
	 * @param method
	 * @param session
	 * @param type
	 * @param url
	 * @param params
	 * @param cols
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/rest/export" }, method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public void onExport(
			@RequestParam(value = "method", required = false) String method,
			@RequestParam(value = "session", required = false) String session, @RequestParam String type,
			@RequestParam String tag, @RequestParam(defaultValue = "{}") String params,
			@RequestParam String token,
			@RequestParam String cols, @RequestParam(defaultValue = "0") String istree, @RequestParam(defaultValue = "children") String childname, HttpServletRequest request, HttpServletResponse response) {

//		params = "{\"url\": \"http://127.0.0.1:8989/rest?method=dict.group.search&ent_id=0"
//				+"&token=1597730034025086448\", \"cols\": \"code=字典代码,cn_name=字典名称,remark=备注,"
//				+"en_name=英文名称,dict_type=退货字典\", \"tname\": \"dictionary\", "
//				+"\"params\": {\"order_field\":\"create_date\",\"order_direction\":\"desc\", \"page_no\":1, \"page_size\":50}"
//				+",\"type\":\"xls\"}";
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStream os = null;
		JSONObject jo = new JSONObject();
		jo.put("type", type);
		jo.put("tag", tag);
		jo.put("token", token);
		jo.put("params", params);
		jo.put("cols", cols);
		jo.put("istree", istree);
		jo.put("childname", childname);
		//jo.put("tname", tname);
		try {
			response.setCharacterEncoding("UTF-8"); // 设置输出流为UTF-8
			response.setContentType("application/zip");
			ProductReflect rcm = new ProductReflect();
			rcm.executeClassMethodForOutputStreamParam(method,null,session, jo.toJSONString(), bos);
			//rcm.executeClassMethodForOutputStreamParam(method, session, params, bos);
			os = response.getOutputStream();
			String dataname = "data." + type;
			String filename = "export.zip";
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(bos.toByteArray()));
			FileProcessorUtils.zip(dataname, os, in);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("export success");
	}

	@RequestMapping(value = { "/rest/exportExcel" }, method = RequestMethod.POST)
	public void onExportExcel(@RequestParam(required = true) String type, @RequestParam(required = true) String url,
			@RequestParam(required = true) String fillName, @RequestParam(defaultValue = "{}") String params,
			@RequestParam String cols, @RequestParam(required = true) String tname,
			@RequestParam(defaultValue = "1") String istree, @RequestParam(defaultValue = "children") String childname,
			@RequestParam String token, @RequestParam(defaultValue = "") String key, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream os = null;

		JSONObject param = new JSONObject();
		param.put("type", type); // 导出文件格式
		param.put("url", url); // 数据获取地址
		param.put("params", params); // 请求参数
		param.put("cols", cols); // 导出数据列与字段对应关系
		param.put("tname", tname); // 数据头,用于提取数据
		param.put("istree", istree); // 是否是树形结果.1 = false, other = true
		param.put("childname", childname); // 子节点名称
		param.put("key", key); // 子节点名称

		ServiceSession session = new ServiceSession();
		session.setEnt_id(0L);
		try {
			String name = fillName + "." + type;
			// 设置输出流格式
			response.reset();
			response.setHeader("Content-Disposition",
					"attachment;filename*=UTF-8''" + URLEncoder.encode(name, "UTF-8"));
			response.setCharacterEncoding("UTF-8");  // 设置输出流为UTF-8
			response.setContentType("application/vnd.ms-excel");
			os = response.getOutputStream();
			ProductReflect rcm = new ProductReflect();
			rcm.executeClassMethodForOutputStreamParam("omdmain.export.onExportDataDirect",null,JSON.toJSONString(session),
					param.toJSONString(), os);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("export success");
	}
	
	/***
	 * 支持导出多种类型数据
	 */
	@RequestMapping(value = {"/rest/exportData"}, method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public void onExportData(@RequestParam(required = true) String type, @RequestParam(required = true) String url,
			@RequestParam(required = true) String fillName, @RequestParam(defaultValue = "{}") String params,
			@RequestParam String cols, @RequestParam(required = true) String tname,
			@RequestParam(defaultValue = "0") String istree, @RequestParam(defaultValue = "children") String childname,
			@RequestParam String token, @RequestParam(defaultValue = "") String key,
			@RequestParam(value = "method", required = false) String method,
			@RequestParam(value = "session", required = false) String session,
			@RequestHeader(value = "session") String newSession, HttpServletRequest request,
			HttpServletResponse response) {

		OutputStream os = null;
		JSONObject jo = new JSONObject();
		jo.put("type", type); // 导出文件格式
		jo.put("url", url); // 数据获取地址
		jo.put("params", params); // 请求参数
		jo.put("cols", cols); // 导出数据列与字段对应关系
		jo.put("tname", tname); // 数据头,用于提取数据
		jo.put("istree", istree); // 是否是树形结果.0 = false, 1 = true
		jo.put("childname", childname); // 子节点名称
		jo.put("key", key); // 子节点
		jo.put("token", token);
		  
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			response.setCharacterEncoding("UTF-8"); // 设置输出流为UTF-8
			response.setContentType("application/zip");
			ProductReflect rcm = new ProductReflect();
			if (StringUtils.isEmpty(session)) {
				session = newSession;
			}
			rcm.executeClassMethodForOutputStreamParam(method,null,session, jo.toJSONString(), bos);
			// rcm.executeClassMethodForOutputStreamParam(method, session, params, bos);
			os = response.getOutputStream();
			String dataname = fillName + "." + type;
			String filename = "export.zip";
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(bos.toByteArray()));
			FileProcessorUtils.zip(dataname, os, in);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("export success");
	  }

}
