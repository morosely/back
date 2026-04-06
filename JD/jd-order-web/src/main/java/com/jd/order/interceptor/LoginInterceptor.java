package com.jd.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jd.common.pojo.JDReturnResult;
import com.jd.common.utils.CookieUtils;
import com.jd.pojo.TbUser;
import com.jd.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${TOKEN_COOKIE_KEY}")
	private String TOKEN_COOKIE_KEY;
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Autowired
	private UserService userService;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//执行Handler之前执行此方法
		// a)从cookie中取token。
		String token = CookieUtils.getCookieValue(request, TOKEN_COOKIE_KEY);
		if (StringUtils.isBlank(token)) {
			//取当前请求的url
			String url = request.getRequestURL().toString();
			// b)没有token，需要跳转到登录页面。
			response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
			//拦截
			return false;
		}
		// c)有token。调用sso系统的服务，根据token查询用户信息。
		 JDReturnResult result= userService.getUserByToken(token);
		if (result.getStatus() != 200) {
			// d)如果查不到用户信息。用户登录已经过期。需要跳转到登录页面。
			//取当前请求的url
			String url = request.getRequestURL().toString();
			// b)没有token，需要跳转到登录页面。
			response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
			//拦截
			return false;
		}
		// e)查询到用户信息。放行。
		TbUser user = (TbUser)result.getData();
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 执行Handler之后返回ModelAndView之前

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回ModelAndView之后，执行。异常处理。

	}

}
