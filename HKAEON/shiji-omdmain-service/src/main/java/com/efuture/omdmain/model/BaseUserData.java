package com.efuture.omdmain.model;

/**
 * 逻辑变量，用于基类内部传递数据
 * @author qianhb
 */
public class BaseUserData {
	Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}