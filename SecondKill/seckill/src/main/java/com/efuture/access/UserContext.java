package com.efuture.access;

import com.efuture.domain.User;

public class UserContext {
	
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();
	
	public static void setUser(User user) {
		userHolder.set(user);
	}
	
	public static User getUser() {
		return userHolder.get();
	}

}
