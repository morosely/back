package com.efuture.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey UID_GID = new OrderKey("uid_gid");
}
