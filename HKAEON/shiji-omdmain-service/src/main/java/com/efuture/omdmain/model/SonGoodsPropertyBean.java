package com.efuture.omdmain.model;

import java.math.BigDecimal;


/** 
* @author yihaitao
* @time 2018年5月16日 下午4:37:02 
* 
*/
public class SonGoodsPropertyBean{

	/**
     * 属性编码
     */
    private String propertyCode;

	/**
     * 属性名称
     */
    private String propertyName;

	/**
     * 属性说明
     */
    private String propertyDesc;

	/**
     * 属性值
     */
    private String propertyValue;
    
    /**
     * 属性值Code
     */
    private String propertyValueCode;

	/**
     * 商品ID
     */
    private Long sgid;

	/**
     * 品类属性模板ID
     */
    private Long cpmid;

	/**
     * 商品名称
     */
    private String goodsName;

	/**
     * 零售价
     */
    private BigDecimal salePrice;

	public Long getCpmid() {
		return cpmid;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getPropertyCode() {
		return propertyCode;
	}

	public String getPropertyDesc() {
		return propertyDesc;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public String getPropertyValueCode() {
		return propertyValueCode;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public Long getSgid() {
		return sgid;
	}

	public void setCpmid(Long cpmid) {
		this.cpmid = cpmid;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

    public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

    public void setPropertyDesc(String propertyDesc) {
		this.propertyDesc = propertyDesc;
	}
    
    public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
    
    public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
    
    public void setPropertyValueCode(String propertyValueCode) {
		this.propertyValueCode = propertyValueCode;
	}
    
    public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
    
    public void setSgid(Long sgid) {
		this.sgid = sgid;
	}

	@Override
	public String toString() {
		return "SonGoodsPropertyBean [propertyCode=" + propertyCode + ", propertyName=" + propertyName
				+ ", propertyDesc=" + propertyDesc + ", propertyValue=" + propertyValue + ", propertyValueCode="
				+ propertyValueCode + ", sgid=" + sgid + ", cpmid=" + cpmid + ", goodsName=" + goodsName
				+ ", salePrice=" + salePrice + "]";
	}
    
}
