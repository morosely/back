package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "erppaymentmethod")
public class ERPPaymentMethodModel {
    /**
     * 支付方式ID
     */
    @Id
    private Long epmid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 上级代码
     */
    private String parentCode;

    /**
     * 付款级次
     */
    private Integer payLevel;

    /**
     * 付款类型
     */
    private String payType;

    /**
     * 虚拟支付类型 1-券 2-积分
     */
    private Short virtualPayType;

    /**
     * 付款代码
     */
    private String payCode;

    /**
     * 付款简码
     */
    private String paySCode;

    /**
     * 付款名称
     */
    private String payName;

    /**
     * 付款模式
     */
    private String payPattern;

    /**
     * 付款汇率
     */
    private Double rate;

    /**
     * 找零汇率
     */
    private Double changeRate;

    /**
     * 最小金额
     */
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    private BigDecimal maxAmount;

    /**
     * 舍入方式
     */
    private String roundType;

    /**
     * 舍入精度
     */
    private Double roundPrecision;

    /**
     * 是否允许溢余
     */
    private Boolean overflowFlag;

    /**
     * 是否允许找零
     */
    private Boolean changeFlag;

    /**
     * 是否记收入
     */
    private Boolean recordFlag;

    /**
     * 是否开发票
     */
    private Boolean invoiceFlag;

    /**
     * 状态
     */
    private Short status;

    /**
     * 层级
     */
    private Short level;

    /**
     * 是否叶子结点
     */
    private Boolean leafFlag;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取支付方式ID
     *
     * @return epmid - 支付方式ID
     */
    public Long getEpmid() {
        return epmid;
    }

    /**
     * 设置支付方式ID
     *
     * @param epmid 支付方式ID
     */
    public void setEpmid(Long epmid) {
        this.epmid = epmid;
    }

    /**
     * 获取零售商ID
     *
     * @return entId - 零售商ID
     */
    public Long getEntId() {
        return entId;
    }

    /**
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 获取经营公司编码
     *
     * @return erpCode - 经营公司编码
     */
    public String getErpCode() {
        return erpCode;
    }

    /**
     * 设置经营公司编码
     *
     * @param erpCode 经营公司编码
     */
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    /**
     * 获取上级ID
     *
     * @return parentId - 上级ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级ID
     *
     * @param parentId 上级ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取上级代码
     *
     * @return parentCode - 上级代码
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置上级代码
     *
     * @param parentCode 上级代码
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * 获取付款级次
     *
     * @return payLevel - 付款级次
     */
    public Integer getPayLevel() {
        return payLevel;
    }

    /**
     * 设置付款级次
     *
     * @param payLevel 付款级次
     */
    public void setPayLevel(Integer payLevel) {
        this.payLevel = payLevel;
    }

    /**
     * 获取付款类型
     *
     * @return payType - 付款类型
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 设置付款类型
     *
     * @param payType 付款类型
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 获取虚拟支付类型 1-券 2-积分
     *
     * @return virtualPayType - 虚拟支付类型 1-券 2-积分
     */
    public Short getVirtualPayType() {
        return virtualPayType;
    }

    /**
     * 设置虚拟支付类型 1-券 2-积分
     *
     * @param virtualPayType 虚拟支付类型 1-券 2-积分
     */
    public void setVirtualPayType(Short virtualPayType) {
        this.virtualPayType = virtualPayType;
    }

    /**
     * 获取付款代码
     *
     * @return payCode - 付款代码
     */
    public String getPayCode() {
        return payCode;
    }

    /**
     * 设置付款代码
     *
     * @param payCode 付款代码
     */
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    /**
     * 获取付款简码
     *
     * @return paySCode - 付款简码
     */
    public String getPaySCode() {
        return paySCode;
    }

    /**
     * 设置付款简码
     *
     * @param paySCode 付款简码
     */
    public void setPaySCode(String paySCode) {
        this.paySCode = paySCode;
    }

    /**
     * 获取付款名称
     *
     * @return payName - 付款名称
     */
    public String getPayName() {
        return payName;
    }

    /**
     * 设置付款名称
     *
     * @param payName 付款名称
     */
    public void setPayName(String payName) {
        this.payName = payName;
    }

    /**
     * 获取付款模式
     *
     * @return payPattern - 付款模式
     */
    public String getPayPattern() {
        return payPattern;
    }

    /**
     * 设置付款模式
     *
     * @param payPattern 付款模式
     */
    public void setPayPattern(String payPattern) {
        this.payPattern = payPattern;
    }

    /**
     * 获取付款汇率
     *
     * @return rate - 付款汇率
     */
    public Double getRate() {
        return rate;
    }

    /**
     * 设置付款汇率
     *
     * @param rate 付款汇率
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * 获取找零汇率
     *
     * @return changeRate - 找零汇率
     */
    public Double getChangeRate() {
        return changeRate;
    }

    /**
     * 设置找零汇率
     *
     * @param changeRate 找零汇率
     */
    public void setChangeRate(Double changeRate) {
        this.changeRate = changeRate;
    }

    /**
     * 获取最小金额
     *
     * @return minAmount - 最小金额
     */
    public BigDecimal getMinAmount() {
        return minAmount;
    }

    /**
     * 设置最小金额
     *
     * @param minAmount 最小金额
     */
    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    /**
     * 获取最大金额
     *
     * @return maxAmount - 最大金额
     */
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    /**
     * 设置最大金额
     *
     * @param maxAmount 最大金额
     */
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    /**
     * 获取舍入方式
     *
     * @return roundType - 舍入方式
     */
    public String getRoundType() {
        return roundType;
    }

    /**
     * 设置舍入方式
     *
     * @param roundType 舍入方式
     */
    public void setRoundType(String roundType) {
        this.roundType = roundType;
    }

    /**
     * 获取舍入精度
     *
     * @return roundPrecision - 舍入精度
     */
    public Double getRoundPrecision() {
        return roundPrecision;
    }

    /**
     * 设置舍入精度
     *
     * @param roundPrecision 舍入精度
     */
    public void setRoundPrecision(Double roundPrecision) {
        this.roundPrecision = roundPrecision;
    }

    /**
     * 获取是否允许溢余
     *
     * @return overflowFlag - 是否允许溢余
     */
    public Boolean getOverflowFlag() {
        return overflowFlag;
    }

    /**
     * 设置是否允许溢余
     *
     * @param overflowFlag 是否允许溢余
     */
    public void setOverflowFlag(Boolean overflowFlag) {
        this.overflowFlag = overflowFlag;
    }

    /**
     * 获取是否允许找零
     *
     * @return changeFlag - 是否允许找零
     */
    public Boolean getChangeFlag() {
        return changeFlag;
    }

    /**
     * 设置是否允许找零
     *
     * @param changeFlag 是否允许找零
     */
    public void setChangeFlag(Boolean changeFlag) {
        this.changeFlag = changeFlag;
    }

    /**
     * 获取是否记收入
     *
     * @return recordFlag - 是否记收入
     */
    public Boolean getRecordFlag() {
        return recordFlag;
    }

    /**
     * 设置是否记收入
     *
     * @param recordFlag 是否记收入
     */
    public void setRecordFlag(Boolean recordFlag) {
        this.recordFlag = recordFlag;
    }

    /**
     * 获取是否开发票
     *
     * @return invoiceFlag - 是否开发票
     */
    public Boolean getInvoiceFlag() {
        return invoiceFlag;
    }

    /**
     * 设置是否开发票
     *
     * @param invoiceFlag 是否开发票
     */
    public void setInvoiceFlag(Boolean invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 获取层级
     *
     * @return level - 层级
     */
    public Short getLevel() {
        return level;
    }

    /**
     * 设置层级
     *
     * @param level 层级
     */
    public void setLevel(Short level) {
        this.level = level;
    }

    /**
     * 获取是否叶子结点
     *
     * @return leafFlag - 是否叶子结点
     */
    public Boolean getLeafFlag() {
        return leafFlag;
    }

    /**
     * 设置是否叶子结点
     *
     * @param leafFlag 是否叶子结点
     */
    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
    }

    /**
     * 获取语言类型
     *
     * @return lang - 语言类型
     */
    public String getLang() {
        return lang;
    }

    /**
     * 设置语言类型
     *
     * @param lang 语言类型
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取创建日期
     *
     * @return createDate - 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 获取修改日期
     *
     * @return updateDate - 修改日期
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改日期
     *
     * @param updateDate 修改日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}