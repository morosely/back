package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsSpecPriceModel extends CommonModel{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getUniqueKey() {
		return "goodsCode,barNo,shopCode,erpCode,entId";
	}

	@Override
	public String getUniqueKeyValue() {
		return "'"+this.goodsCode+"','"+this.barNo+"','"+this.shopCode+"','"+this.erpCode+"',"+this.entId;
	}
	
	//商品
	public String getGoodsCodeKeyValue(){
		return "'"+this.goodsCode+"','"+this.erpCode+"',"+this.entId;
	}
	
	//门店
    public String getShopCodeKeyValue() {
		return "'"+this.shopCode+"','"+this.erpCode+"',"+this.entId;
	}
	
    private Long gspid;

    private Long entId;

    private String erpCode;

    private Long shopId;

    private String shopCode;

    private Long sgid;

    private String goodsCode;

    private String goodsName;

    private Short codeType;

    private String barNo;

    private Double partsNum;

    private String partsUnit;

    private BigDecimal salePrice;

    private BigDecimal customPrice;

    private BigDecimal bulkPrice;

    private String sortLevel;

    private Short status;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Date erpUpdateDate;

    public Long getGspid() {
        return gspid;
    }

    public void setGspid(Long gspid) {
        this.gspid = gspid;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public Long getSgid() {
        return sgid;
    }

    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Short getCodeType() {
        return codeType;
    }

    public void setCodeType(Short codeType) {
        this.codeType = codeType;
    }

    public String getBarNo() {
        return barNo;
    }

    public void setBarNo(String barNo) {
        this.barNo = barNo == null ? null : barNo.trim();
    }

    public Double getPartsNum() {
        return partsNum;
    }

    public void setPartsNum(Double partsNum) {
        this.partsNum = partsNum;
    }

    public String getPartsUnit() {
        return partsUnit;
    }

    public void setPartsUnit(String partsUnit) {
        this.partsUnit = partsUnit == null ? null : partsUnit.trim();
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    public BigDecimal getBulkPrice() {
        return bulkPrice;
    }

    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
    }

    public String getSortLevel() {
        return sortLevel;
    }

    public void setSortLevel(String sortLevel) {
        this.sortLevel = sortLevel == null ? null : sortLevel.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getErpUpdateDate() {
        return erpUpdateDate;
    }

    public void setErpUpdateDate(Date erpUpdateDate) {
        this.erpUpdateDate = erpUpdateDate;
    }
}