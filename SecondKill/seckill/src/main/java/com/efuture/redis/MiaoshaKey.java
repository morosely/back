package com.efuture.redis;

public class MiaoshaKey extends BasePrefix{

	private MiaoshaKey(String prefix) {
		super(prefix);
	}
	
	private MiaoshaKey( int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static MiaoshaKey isGoodsOver = new MiaoshaKey("goodsOver");
	public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "miaoShaPath");
	public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "verifyCode");

	public static void main(String[] args) {
		System.out.println("MiaoshaKey = " + MiaoshaKey.getMiaoshaVerifyCode.getPrefix());
	}
}
