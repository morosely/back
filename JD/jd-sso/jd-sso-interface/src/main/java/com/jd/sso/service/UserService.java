package com.jd.sso.service;

import com.jd.common.pojo.JDReturnResult;
import com.jd.pojo.TbUser;

public interface UserService {

	JDReturnResult checkData(String param, int type);

	JDReturnResult createUser(TbUser user);

	JDReturnResult login(String username, String password);

	JDReturnResult getUserByToken(String token);

}
