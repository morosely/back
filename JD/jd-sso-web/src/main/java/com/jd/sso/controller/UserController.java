package com.jd.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.common.pojo.JDReturnResult;
import com.jd.common.utils.CookieUtils;
import com.jd.common.utils.JsonUtils;
import com.jd.pojo.TbUser;
import com.jd.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Value("${TOKEN_COOKIE_KEY}")
	private String TOKEN_COOKIE_KEY;
	@Value("${TOKEN_COOKIE_KEY_EXPIRE}")
	private Integer TOKEN_COOKIE_KEY_EXPIRE;

	//指定返回响应数据的content-type:produces=MediaType.APPLICATION_JSON_UTF8_VALUE
	 @RequestMapping(value="/user/token/{token}", method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	 @ResponseBody 
	 public String getUserByToken(@PathVariable String token, String callback) { 
		 JDReturnResult result = userService.getUserByToken(token); 
		 //判断是否为jsonp请求
		 if(StringUtils.isNotBlank(callback)) {
			 return callback + "(" +
					 JsonUtils.objectToJson(result) + ");"; 
			 } 
		 return JsonUtils.objectToJson(result); 
	}
	 
	// jsonp的第二种方法，spring4.1以上版本使用
	/*@RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		JDReturnData result = userService.getUserByToken(token);
		// 判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
					result);
			// 设置回调方法
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}*/

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public JDReturnResult login(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		// 1、接收两个参数。
		// 2、调用Service进行登录。
		JDReturnResult result = userService.login(username, password);
		// 3、从返回结果中取token，写入cookie。Cookie要跨域。
		if(result.getData()!=null){
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_COOKIE_KEY, token,TOKEN_COOKIE_KEY_EXPIRE);
		}
		// 4、响应数据。Json数据。TaotaoResult，其中包含Token。
		return result;

	}

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public JDReturnResult checkData(@PathVariable String param,
			@PathVariable Integer type) {
		JDReturnResult result = userService.checkData(param, type);
		return result;
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public JDReturnResult register(TbUser user) {
		JDReturnResult result = userService.createUser(user);
		return result;
	}
}
