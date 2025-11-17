package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class ExtGoodsMoreBarCodeModel extends CommonExtModel{
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getUniqueKey() {
		return "goodsCode,barNo,erpCode,entId";
	}

	@Override
	public String getUniqueKeyValue() {
		return "'"+this.goodsCode+"','"+this.barNo+"','"+this.erpCode+"',"+this.entId;
	}
	
    private Long egsid;

    private Long entId;

    private String erpCode;

    private Long sgid;

    private String goodsCode;

    private String goodsName;

    private Short codeType;

    private String barNo;

    private Double partsNum;

    private String partsUnit;

    private String sortLevel;

    private BigDecimal salePrice;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Boolean priceFromGoods;

    private Short barCodeType;

    private Integer dealStatus;

    private Integer status;

    public Long getEgsid() {
        return egsid;
    }

    public void setEgsid(Long egsid) {
        this.egsid = egsid;
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

    public String getSortLevel() {
        return sortLevel;
    }

    public void setSortLevel(String sortLevel) {
        this.sortLevel = sortLevel == null ? null : sortLevel.trim();
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public Boolean getPriceFromGoods() {
        return priceFromGoods;
    }

    public void setPriceFromGoods(Boolean priceFromGoods) {
        this.priceFromGoods = priceFromGoods;
    }

    public Short getBarCodeType() {
        return barCodeType;
    }

    public void setBarCodeType(Short barCodeType) {
        this.barCodeType = barCodeType;
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}