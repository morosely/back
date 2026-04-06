package com.efuture.redis;

public class UserKey extends BasePrefix{

	private UserKey(String prefix) {
		super(prefix);
	}
	
	private UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static final int TOKEN_EXPIRE = 3600 * 24;
	
	public static UserKey ID = new UserKey("id");
	public static UserKey NICK_NAME = new UserKey("nickName");
	public static UserKey TOKEN = new UserKey(TOKEN_EXPIRE, "token");
}
