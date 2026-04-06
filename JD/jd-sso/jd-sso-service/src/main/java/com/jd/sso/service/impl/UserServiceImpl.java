package com.jd.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.jd.common.pojo.JDReturnResult;
import com.jd.common.redis.JedisClient;
import com.jd.common.utils.JsonUtils;
import com.jd.mapper.TbUserMapper;
import com.jd.pojo.TbUser;
import com.jd.pojo.TbUserExample;
import com.jd.pojo.TbUserExample.Criteria;
import com.jd.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public JDReturnResult getUserByToken(String token) {
		// 2、根据token查询redis。
		String json = jedisClient.get(USER_SESSION + ":" + token);
		if (StringUtils.isBlank(json)) {
			// 3、如果查询不到数据。返回用户已经过期。
			return JDReturnResult.build(400, "用户登录已经过期，请重新登录。");
		}
		// 4、如果查询到数据，说明用户已经登录。
		// 5、需要重置key的过期时间。
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		// 6、把json数据转换成TbUser对象，然后使用TaotaoResult包装并返回。
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return JDReturnResult.success(user);
	}
	
	@Override
	public JDReturnResult login(String username, String password) {
		// 1、判断用户名密码是否正确。
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//查询用户信息
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return JDReturnResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//校验密码
		if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return JDReturnResult.build(400, "用户名或密码错误");
		}
		// 2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
		String token = UUID.randomUUID().toString();
		// 3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
		// 4、使用String类型保存Session信息。可以使用“前缀:token”为key
		user.setPassword(null);
		jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
		// 5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		// 6、返回TaotaoResult包装token。
		return JDReturnResult.success(token);
	}
	
	@Override
	public JDReturnResult createUser(TbUser user) {
		// 1、使用TbUser接收提交的请求。
		
		if (StringUtils.isBlank(user.getUsername())) {
			return JDReturnResult.build(400, "用户名不能为空");
		}
		if (StringUtils.isBlank(user.getPassword())) {
			return JDReturnResult.build(400, "密码不能为空");
		}
		//校验数据是否可用
		JDReturnResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return JDReturnResult.build(400, "此用户名已经被使用");
		}
		//校验电话是否可以
		if (StringUtils.isNotBlank(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return JDReturnResult.build(400, "此手机号已经被使用");
			}
		}
		//校验email是否可用
		if (StringUtils.isNotBlank(user.getEmail())) {
			result = checkData(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return JDReturnResult.build(400, "此邮件地址已经被使用");
			}
		}
		// 2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 3、密码要进行MD5加密。
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		// 4、把用户信息插入到数据库中。
		userMapper.insert(user);
		// 5、返回TaotaoResult。
		return JDReturnResult.success();
	}
	
	@Override
	public JDReturnResult checkData(String param, int type) {
		// 1、从tb_user表中查询数据
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 2、查询条件根据参数动态生成。
		//1、2、3分别代表username、phone、email
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {
			criteria.andEmailEqualTo(param);
		} else {
			return JDReturnResult.build(400, "非法的参数");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		// 3、判断查询结果，如果查询到数据返回false。
		if (list == null || list.size() == 0) {
			// 4、如果没有返回true。
			return JDReturnResult.success(true);
		} 
		// 5、使用JDReturnData包装，并返回。
		return JDReturnResult.success(false);
	}
	
}
