package com.efuture.omdmain.common;

/**
 * 商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分隔商品/8-箱码商品/0-ERP同步商品
 * @author yihaitao
 *
 */
public enum GoodsType {
	ZP(1,"子品"),
	XNMP(2,"虚拟母品"),
	ZBM(3,"组包码"),
	CP(4,"菜谱"),
	BCSP(5,"步长商品"),
	SX(6,"生鲜商品等级"),
	FG(7,"分隔商品"),
	XM(8,"箱码商品"),
	ERPTB(9,"ERP同步商品");
	
	private final Integer value;
	private final String describe;
	
	public Integer getValue() {
		return value;
	}

	public String getDescribe() {
		return describe;
	}

	private GoodsType(Integer value, String describe) {
		this.value = value;
		this.describe = describe;
	}
	
	public static GoodsType getByValue(Integer value) {  
        for(GoodsType goodsType : GoodsType.values()) {  
            if(goodsType.value == value) {  
                return goodsType;  
            }  
        }  
        throw new IllegalArgumentException("No element matches " + value);  
	}  
	
	
}
