package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class ExtPaymentMethodModel extends CommonExtModel {

    @Override
    public String getUniqueKey() {
        return "payCode,entId";
    }

    @Override
    public String getUniqueKeyValue() {
        return "'" + this.payCode + "'," + this.entId;
    }

    private Long epmid;

    private Long entId;

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

    private Float roundPrecision;

    private Boolean overflowFlag;

    private Boolean changeFlag;

    private Boolean recordFlag;

    private Boolean invoiceFlag;

    private Boolean netvalueFlag;

    private String payRemark;

    private Short status;

    private Short level;

    private Boolean leafFlag;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private String currencyCode;

    private String currencyFlag;

    private String cardPayType;

    private Boolean returnPayFlag;

    private Integer dealStatus;

    public Long getEpmid() {
        return epmid;
    }

    public void setEpmid(Long epmid) {
        this.epmid = epmid;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
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
        this.parentCode = parentCode;
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
        this.payType = payType;
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
        this.payCode = payCode;
    }

    public String getPaySCode() {
        return paySCode;
    }

    public void setPaySCode(String paySCode) {
        this.paySCode = paySCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayPattern() {
        return payPattern;
    }

    public void setPayPattern(String payPattern) {
        this.payPattern = payPattern;
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
        this.roundType = roundType;
    }

    public Float getRoundPrecision() {
        return roundPrecision;
    }

    public void setRoundPrecision(Float roundPrecision) {
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

    public Boolean getNetvalueFlag() {
        return netvalueFlag;
    }

    public void setNetvalueFlag(Boolean netvalueFlag) {
        this.netvalueFlag = netvalueFlag;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public Boolean getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public Date getUpdateDate() {
        return updateDate;
    }

    @Override
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyFlag() {
        return currencyFlag;
    }

    public void setCurrencyFlag(String currencyFlag) {
        this.currencyFlag = currencyFlag;
    }

    public String getCardPayType() {
        return cardPayType;
    }

    public void setCardPayType(String cardPayType) {
        this.cardPayType = cardPayType;
    }

    public Boolean getReturnPayFlag() {
        return returnPayFlag;
    }

    public void setReturnPayFlag(Boolean returnPayFlag) {
        this.returnPayFlag = returnPayFlag;
    }

    @Override
    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }
}