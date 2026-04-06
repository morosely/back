package com.efuture.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.efuture.access.UserContext;
import com.efuture.domain.User;
import com.efuture.service.UserService;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	UserService userService;
	
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz == User.class;
	}
	
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return UserContext.getUser();
	}

//	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//		
//		String paramToken = request.getParameter(userService.COOKI_NAME_TOKEN);
//		String cookieToken = getCookieValue(request, userService.COOKI_NAME_TOKEN);
//		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//			return null;
//		}
//		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//		return userService.getByToken(response, token);
//	}
//
//	private String getCookieValue(HttpServletRequest request, String cookiName) {
//		Cookie[]  cookies = request.getCookies();
//		if(cookies!=null && cookies.length > 0) {
//			for(Cookie cookie : cookies) {
//				if(cookie.getName().equals(cookiName)) {
//					return cookie.getValue();
//				}
//			}
//		}
//		return null;
//	}

}
