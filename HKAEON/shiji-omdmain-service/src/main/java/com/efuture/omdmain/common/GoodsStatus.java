package com.efuture.omdmain.common;

/**
 * Goods的状态只有两个状态
 * 
 * @author Administrator
 *
 */
public enum GoodsStatus {
	//goodStatus` int(11) DEFAULT NULL COMMENT '商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补'
	DQY(0,"待启用"),
	XPSX(1,"新品试销"),
	XPPG(2,"新品评估"),
	ZC(3,"正常"),
	ZTDH(4,"暂停订货"),
	HJCL(5,"换季处理"),
	ZTJY(6,"暂停经营"),
	TZXS(7,"停止销售"),
	DQT(8,"待清退"),
	YQT(9,"已清退"),
	ZTDB(10,"暂停订补");
	
	private final Integer value;
	private final String describe;
	
	public Integer getValue() {
		return value;
	}

	public String getDescribe() {
		return describe;
	}
	
	private GoodsStatus(Integer value, String describe) {
		this.value = value;
		this.describe = describe;
	}
	
	public static GoodsStatus getByValue(Integer value) {  
        for(GoodsStatus goodsStatus : GoodsStatus.values()) {  
            if(goodsStatus.value == value) {  
                return goodsStatus;  
            }  
        }  
        throw new IllegalArgumentException("No element matches " + value);  
	}  
}
