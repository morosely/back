package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class ExtErpPaymentMethodModel  extends CommonExtModel{
	
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "payCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.payCode+"','"+this.erpCode+"',"+this.entId;
	}
	
    private Long eepmid;

    private Long entId;

    private String erpCode;

    private Long parentId;

    private String parentCode;

    private Integer payLevel;

    private String payType;

    private Short virtualPayType;

    private String payCode;

    private String paySCode;

    private String payName;

    private String payPattern;

    private Double rate;

    private Double changeRate;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private String roundType;

    private Double roundPrecision;

    private Boolean overflowFlag;

    private Boolean changeFlag;

    private Boolean recordFlag;

    private Boolean invoiceFlag;

    private Integer status;

    private Integer level;

    private Boolean leafFlag;

    private String sourceFromCode;

    private Integer dealStatus;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Integer integralFlag;//是否积分
    private String cardPayType; //银行卡支付类型

    public Integer getIntegralFlag() {
        return integralFlag;
    }

    public void setIntegralFlag(Integer integralFlag) {
        this.integralFlag = integralFlag;
    }

    public String getCardPayType() {
        return cardPayType;
    }

    public void setCardPayType(String cardPayType) {
        this.cardPayType = cardPayType;
    }

    public Long getEepmid() {
        return eepmid;
    }

    public void setEepmid(Long eepmid) {
        this.eepmid = eepmid;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }

    public Integer getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(Integer payLevel) {
        this.payLevel = payLevel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Short getVirtualPayType() {
        return virtualPayType;
    }

    public void setVirtualPayType(Short virtualPayType) {
        this.virtualPayType = virtualPayType;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode == null ? null : payCode.trim();
    }

    public String getPaySCode() {
        return paySCode;
    }

    public void setPaySCode(String paySCode) {
        this.paySCode = paySCode == null ? null : paySCode.trim();
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName == null ? null : payName.trim();
    }

    public String getPayPattern() {
        return payPattern;
    }

    public void setPayPattern(String payPattern) {
        this.payPattern = payPattern == null ? null : payPattern.trim();
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(Double changeRate) {
        this.changeRate = changeRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getRoundType() {
        return roundType;
    }

    public void setRoundType(String roundType) {
        this.roundType = roundType == null ? null : roundType.trim();
    }

    public Double getRoundPrecision() {
        return roundPrecision;
    }

    public void setRoundPrecision(Double roundPrecision) {
        this.roundPrecision = roundPrecision;
    }

    public Boolean getOverflowFlag() {
        return overflowFlag;
    }

    public void setOverflowFlag(Boolean overflowFlag) {
        this.overflowFlag = overflowFlag;
    }

    public Boolean getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(Boolean changeFlag) {
        this.changeFlag = changeFlag;
    }

    public Boolean getRecordFlag() {
        return recordFlag;
    }

    public void setRecordFlag(Boolean recordFlag) {
        this.recordFlag = recordFlag;
    }

    public Boolean getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Boolean invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
    }

    public String getSourceFromCode() {
        return sourceFromCode;
    }

    public void setSourceFromCode(String sourceFromCode) {
        this.sourceFromCode = sourceFromCode == null ? null : sourceFromCode.trim();
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
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
}