package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class ExtGoodsModel extends CommonExtModel{

	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "goodsCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.goodsCode+"','"+this.erpCode+"',"+this.entId;
	}

    private Long egid;

    private Long entId;

    private String erpCode;

    private Integer goodsType;

    private Boolean directFromErp;

    private Boolean singleItemFlag;

    private Boolean canSaleFlag;

    private String goodsCode;

    private String parentGoodsCode;

    private String goodsName;

    private String goodsDisplayName;

    private String fullName;

    private String enSname;

    private String enFname;

    private String barNo;

    private BigDecimal salePrice;

    private BigDecimal refPrice;

    private BigDecimal minSalePrice;

    private Float minDiscount;

    private BigDecimal memberPrice;

    private BigDecimal primeCost;

    private String goodsFromCode;

    private String measureUnit;

    private String saleUnit;

    private Double partsNum;

    private String partsUnit;

    private Long categoryId;

    private String categoryCode;

    private Long brandId;

    private String brandCode;

    private String artNo;

    private String textture;

    private String originArea;

    private Double nweight;

    private Double rweight;

    private Integer qaDays;

    private Short qaDaysUnit;

    private Float waterDamage;

    private Float lowTemp;

    private Float highTemp;

    private Float inputTax;

    private Float outputTax;

    private Float consumpTax;

    private Integer longScale;

    private Integer wideScale;

    private Integer highScale;

    private String goodsGrade;

    private String orderSpec;

    private String orderUnit;

    private Integer orderNum;

    private Integer safeStockDays;

    private Integer minStockDays;

    private Integer maxStockDays;

    private Boolean isPercious;

    private Boolean coldTransFlag;

    private Boolean escaleFlag;

    private Short timesFlag;

    private String season;

    private Boolean isBatch;

    private Boolean controlFlag;

    private BigDecimal recycleFee;

    private Boolean multiUnitFlag;

    private Short goodsStatus;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Integer dealStatus;

    private String venderCode;
    
    private Short deliveryFlag;

    public Short getDeliveryFlag() {
		return deliveryFlag;
	}

	public void setDeliveryFlag(Short deliveryFlag) {
		this.deliveryFlag = deliveryFlag;
	}

	public Long getEgid() {
        return egid;
    }

    public void setEgid(Long egid) {
        this.egid = egid;
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

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Boolean getDirectFromErp() {
        return directFromErp;
    }

    public void setDirectFromErp(Boolean directFromErp) {
        this.directFromErp = directFromErp;
    }

    public Boolean getSingleItemFlag() {
        return singleItemFlag;
    }

    public void setSingleItemFlag(Boolean singleItemFlag) {
        this.singleItemFlag = singleItemFlag;
    }

    public Boolean getCanSaleFlag() {
        return canSaleFlag;
    }

    public void setCanSaleFlag(Boolean canSaleFlag) {
        this.canSaleFlag = canSaleFlag;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getParentGoodsCode() {
        return parentGoodsCode;
    }

    public void setParentGoodsCode(String parentGoodsCode) {
        this.parentGoodsCode = parentGoodsCode == null ? null : parentGoodsCode.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsDisplayName() {
        return goodsDisplayName;
    }

    public void setGoodsDisplayName(String goodsDisplayName) {
        this.goodsDisplayName = goodsDisplayName == null ? null : goodsDisplayName.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public String getEnSname() {
        return enSname;
    }

    public void setEnSname(String enSname) {
        this.enSname = enSname == null ? null : enSname.trim();
    }

    public String getEnFname() {
        return enFname;
    }

    public void setEnFname(String enFname) {
        this.enFname = enFname == null ? null : enFname.trim();
    }

    public String getBarNo() {
        return barNo;
    }

    public void setBarNo(String barNo) {
        this.barNo = barNo == null ? null : barNo.trim();
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(BigDecimal refPrice) {
        this.refPrice = refPrice;
    }

    public BigDecimal getMinSalePrice() {
        return minSalePrice;
    }

    public void setMinSalePrice(BigDecimal minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    public Float getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Float minDiscount) {
        this.minDiscount = minDiscount;
    }

    public BigDecimal getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(BigDecimal memberPrice) {
        this.memberPrice = memberPrice;
    }

    public BigDecimal getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(BigDecimal primeCost) {
        this.primeCost = primeCost;
    }

    public String getGoodsFromCode() {
        return goodsFromCode;
    }

    public void setGoodsFromCode(String goodsFromCode) {
        this.goodsFromCode = goodsFromCode == null ? null : goodsFromCode.trim();
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit == null ? null : saleUnit.trim();
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode == null ? null : brandCode.trim();
    }

    public String getArtNo() {
        return artNo;
    }

    public void setArtNo(String artNo) {
        this.artNo = artNo == null ? null : artNo.trim();
    }

    public String getTextture() {
        return textture;
    }

    public void setTextture(String textture) {
        this.textture = textture == null ? null : textture.trim();
    }

    public String getOriginArea() {
        return originArea;
    }

    public void setOriginArea(String originArea) {
        this.originArea = originArea == null ? null : originArea.trim();
    }

    public Double getNweight() {
        return nweight;
    }

    public void setNweight(Double nweight) {
        this.nweight = nweight;
    }

    public Double getRweight() {
        return rweight;
    }

    public void setRweight(Double rweight) {
        this.rweight = rweight;
    }

    public Integer getQaDays() {
        return qaDays;
    }

    public void setQaDays(Integer qaDays) {
        this.qaDays = qaDays;
    }

    public Short getQaDaysUnit() {
        return qaDaysUnit;
    }

    public void setQaDaysUnit(Short qaDaysUnit) {
        this.qaDaysUnit = qaDaysUnit;
    }

    public Float getWaterDamage() {
        return waterDamage;
    }

    public void setWaterDamage(Float waterDamage) {
        this.waterDamage = waterDamage;
    }

    public Float getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(Float lowTemp) {
        this.lowTemp = lowTemp;
    }

    public Float getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(Float highTemp) {
        this.highTemp = highTemp;
    }

    public Float getInputTax() {
        return inputTax;
    }

    public void setInputTax(Float inputTax) {
        this.inputTax = inputTax;
    }

    public Float getOutputTax() {
        return outputTax;
    }

    public void setOutputTax(Float outputTax) {
        this.outputTax = outputTax;
    }

    public Float getConsumpTax() {
        return consumpTax;
    }

    public void setConsumpTax(Float consumpTax) {
        this.consumpTax = consumpTax;
    }

    public Integer getLongScale() {
        return longScale;
    }

    public void setLongScale(Integer longScale) {
        this.longScale = longScale;
    }

    public Integer getWideScale() {
        return wideScale;
    }

    public void setWideScale(Integer wideScale) {
        this.wideScale = wideScale;
    }

    public Integer getHighScale() {
        return highScale;
    }

    public void setHighScale(Integer highScale) {
        this.highScale = highScale;
    }

    public String getGoodsGrade() {
        return goodsGrade;
    }

    public void setGoodsGrade(String goodsGrade) {
        this.goodsGrade = goodsGrade == null ? null : goodsGrade.trim();
    }

    public String getOrderSpec() {
        return orderSpec;
    }

    public void setOrderSpec(String orderSpec) {
        this.orderSpec = orderSpec == null ? null : orderSpec.trim();
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit == null ? null : orderUnit.trim();
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getSafeStockDays() {
        return safeStockDays;
    }

    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
    }

    public Integer getMinStockDays() {
        return minStockDays;
    }

    public void setMinStockDays(Integer minStockDays) {
        this.minStockDays = minStockDays;
    }

    public Integer getMaxStockDays() {
        return maxStockDays;
    }

    public void setMaxStockDays(Integer maxStockDays) {
        this.maxStockDays = maxStockDays;
    }

    public Boolean getIsPercious() {
        return isPercious;
    }

    public void setIsPercious(Boolean isPercious) {
        this.isPercious = isPercious;
    }

    public Boolean getColdTransFlag() {
        return coldTransFlag;
    }

    public void setColdTransFlag(Boolean coldTransFlag) {
        this.coldTransFlag = coldTransFlag;
    }

    public Boolean getEscaleFlag() {
        return escaleFlag;
    }

    public void setEscaleFlag(Boolean escaleFlag) {
        this.escaleFlag = escaleFlag;
    }

    public Short getTimesFlag() {
        return timesFlag;
    }

    public void setTimesFlag(Short timesFlag) {
        this.timesFlag = timesFlag;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season == null ? null : season.trim();
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    public Boolean getControlFlag() {
        return controlFlag;
    }

    public void setControlFlag(Boolean controlFlag) {
        this.controlFlag = controlFlag;
    }

    public BigDecimal getRecycleFee() {
        return recycleFee;
    }

    public void setRecycleFee(BigDecimal recycleFee) {
        this.recycleFee = recycleFee;
    }

    public Boolean getMultiUnitFlag() {
        return multiUnitFlag;
    }

    public void setMultiUnitFlag(Boolean multiUnitFlag) {
        this.multiUnitFlag = multiUnitFlag;
    }

    public Short getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Short goodsStatus) {
        this.goodsStatus = goodsStatus;
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

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode == null ? null : venderCode.trim();
    }

}