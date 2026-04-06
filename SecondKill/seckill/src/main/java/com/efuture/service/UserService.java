package com.efuture.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efuture.dao.UserDao;
import com.efuture.domain.User;
import com.efuture.exception.GlobalException;
import com.efuture.redis.RedisService;
import com.efuture.redis.UserKey;
import com.efuture.result.CodeMsg;
import com.efuture.util.MD5Util;
import com.efuture.util.UUIDUtil;
import com.efuture.vo.LoginVo;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	RedisService redisService;
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	public User getById(long id) {
		//取缓存
		User user = redisService.get(UserKey.ID, ""+id, User.class);
		if(user != null) {
			return user;
		}
		//取数据库
		user = userDao.getById(id);
		if(user != null) {
			redisService.set(UserKey.ID, ""+id, user);
		}
		return user;
	}
	
	public User getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		User user = redisService.get(UserKey.TOKEN, token, User.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}

	public String login(HttpServletResponse response, @Valid LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		User user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return token;
		
	}
	
	private void addCookie(HttpServletResponse response, String token, User user) {
		redisService.set(UserKey.TOKEN, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.TOKEN.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	// http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
	public boolean updatePassword(String token, long id, String formPass) {
		//取user
		User user = getById(id);
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//更新数据库
		User toBeUpdate = new User();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
		userDao.update(toBeUpdate);
		//处理缓存
		redisService.delete(UserKey.ID, ""+id);
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(UserKey.TOKEN, token, user);
		return true;
	}

}
