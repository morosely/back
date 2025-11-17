package com.efuture.omdmain.model;

import java.util.List;


/** 
* @author yihaitao
* @time 2018年5月16日 下午5:11:49 
* 
*/
public class SonGoodsBean extends GoodsModel{
	
	private Long psgid;
	private String propertyValueCodes;
	private List<SonGoodsPropertyBean> goodsPropertyExt;

	public List<SonGoodsPropertyBean> getGoodsPropertyExt() {
		return goodsPropertyExt;
	}
	
	public String getPropertyValueCodes() {
		return propertyValueCodes;
	}
	
	public Long getPsgid() {
		return psgid;
	}

	public void setGoodsPropertyExt(List<SonGoodsPropertyBean> goodsPropertyExt) {
		this.goodsPropertyExt = goodsPropertyExt;
	}

	public void setPropertyValueCodes(String propertyValueCodes) {
		this.propertyValueCodes = propertyValueCodes;
	}

	public void setPsgid(Long psgid) {
		this.psgid = psgid;
	}

	@Override
	public String toString() {
		return "SonGoodsBean [psgid=" + psgid + ", propertyValueCodes=" + propertyValueCodes + ", goodsPropertyExt="
				+ goodsPropertyExt + "]";
	}
	
}
