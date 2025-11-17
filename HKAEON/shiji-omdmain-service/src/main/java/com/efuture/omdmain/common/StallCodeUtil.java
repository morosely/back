package com.efuture.omdmain.common;

/**
 * 生成档口编号
 * 
 */
public class StallCodeUtil {

	public static String getNumber(int number) {

		String top = "D";
		String res;
		int temp;
		if (number < 9999) {
			temp = number + 1;
		} else {
			temp = 0;
		}
		res = String.format(top + "%04d", temp);

		return res;
	}

}
