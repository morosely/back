package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import com.product.annotation.Size;
import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;

@Table(name = "paymentmethod")
@UniqueKey(table="paymentmethod",keys = {"entId","payCode" },operationFlags={OperationFlag.Insert},message="企业【${entId}】及编码【${payCode}】必须唯一")
@UniqueKey(table="paymentmethod",keys = {"entId","payCode" },primaryKey="pmid",operationFlags={OperationFlag.Update},message="企业【${entId}】及编码【${payCode}】必须唯一")
public class PaymentMethodModel {
    //银行卡支付类型
    private String cardPayType;

    /**
     * 是否允许找零
     */
    private Boolean changeFlag;

    /**
     * 找零汇率
     */
    @Size(operationFlags={OperationFlag.Insert,OperationFlag.Update},message="找零汇率(changeRate)数据范围必须在0和99999999.9999之间",min="0",max="99999999.9999")
    private Double changeRate;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    //货币代码
    private String currencyCode;

    //货币标识
    private String currencyFlag;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 是否开发票
     */
    private Boolean invoiceFlag;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 是否叶子结点
     */
    private Boolean leafFlag;

    /**
     * 层级
     */
    private Short level;

    /**
     * 最大金额
     */
    @Size(operationFlags={OperationFlag.Insert,OperationFlag.Update},message="最大金额(maxAmount)数据范围必须在0和99999999999999.9999之间",min="0",max="99999999999999.9999")
    private BigDecimal maxAmount;

    /**
     * 最小金额
     */
    @Size(operationFlags={OperationFlag.Insert,OperationFlag.Update},message="最小金额(minAmount)数据范围必须在0和99999999999999.9999之间",min="0",max="99999999999999.9999")
    private BigDecimal minAmount;

    /**
     * 修改人
     */
    private String modifier;

    //是否计入净收益
    private Boolean netvalueFlag;

    /**
     * 是否允许溢余
     */
    private Boolean overflowFlag;

    /**
     * 上级代码
     */
    private String parentCode;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 付款代码
     */
    private String payCode;

    /**
     * 付款级次
     */
    private Integer payLevel;

    /**
     * 付款名称
     */
    private String payName;
    /**
     * 付款模式
     */
    private String payPattern;

	//支付备注
    private String payRemark;

	/**
     * 付款简码
     */
    private String paySCode;

    /**
     * 付款类型
     */
    private String payType;

    /**
     * 支付方式ID
     */
    @Id
    private Long pmid;

    /**
     * 付款汇率
     */
    @Size(operationFlags={OperationFlag.Insert,OperationFlag.Update},message="付款汇率(rate)数据范围必须在0和99999999.9999之间",min="0",max="99999999.9999")
    private Double rate;

    /**
     * 是否记收入
     */
    private Boolean recordFlag;

    /**
     * 允许退货支付
     */
    private Boolean returnPayFlag;

    /**
     * 舍入精度
     */
    @Size(operationFlags={OperationFlag.Insert,OperationFlag.Update},message=" 舍入精度(roundPrecision)数据范围必须在0和9.9999之间",min="0",max="9.9999")
    private Float roundPrecision;

    /**
     * 舍入方式
     */
    private String roundType;
    
    /**
     * 状态
     */
    private Short status;
    
    /**
     * 修改日期
     */
    private Date updateDate;
    
    /**
     * 虚拟支付类型 1-券 2-积分
     */
    private Short virtualPayType;
    
    public String getCardPayType() {
		return cardPayType;
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
     * 获取找零汇率
     *
     * @return changeRate - 找零汇率
     */
    public Double getChangeRate() {
        return changeRate;
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getCurrencyFlag() {
		return currencyFlag;
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
     * 获取是否开发票
     *
     * @return invoiceFlag - 是否开发票
     */
    public Boolean getInvoiceFlag() {
        return invoiceFlag;
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
     * 获取是否叶子结点
     *
     * @return leafFlag - 是否叶子结点
     */
    public Boolean getLeafFlag() {
        return leafFlag;
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
     * 获取最大金额
     *
     * @return maxAmount - 最大金额
     */
    public BigDecimal getMaxAmount() {
        return maxAmount;
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
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    public Boolean getNetvalueFlag() {
		return netvalueFlag;
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
     * 获取上级代码
     *
     * @return parentCode - 上级代码
     */
    public String getParentCode() {
        return parentCode;
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
     * 获取付款代码
     *
     * @return payCode - 付款代码
     */
    public String getPayCode() {
        return payCode;
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
     * 获取付款名称
     *
     * @return payName - 付款名称
     */
    public String getPayName() {
        return payName;
    }

    /**
     * 获取付款模式
     *
     * @return payPattern - 付款模式
     */
    public String getPayPattern() {
        return payPattern;
    }

    public String getPayRemark() {
		return payRemark;
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
     * 获取付款类型
     *
     * @return payType - 付款类型
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 获取支付方式ID
     *
     * @return pmid - 支付方式ID
     */
    public Long getPmid() {
        return pmid;
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
     * 获取是否记收入
     *
     * @return recordFlag - 是否记收入
     */
    public Boolean getRecordFlag() {
        return recordFlag;
    }

    public Boolean getReturnPayFlag() {
		return returnPayFlag;
	}

    /**
     * 获取舍入精度
     *
     * @return roundPrecision - 舍入精度
     */
    public Float getRoundPrecision() {
        return roundPrecision;
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
     * 获取状态
     *
     * @return status - 状态
     */
    public Short getStatus() {
        return status;
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
     * 获取虚拟支付类型 1-券 2-积分
     *
     * @return virtualPayType - 虚拟支付类型 1-券 2-积分
     */
    public Short getVirtualPayType() {
        return virtualPayType;
    }

    public void setCardPayType(String cardPayType) {
		this.cardPayType = cardPayType;
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
     * 设置找零汇率
     *
     * @param changeRate 找零汇率
     */
    public void setChangeRate(Double changeRate) {
        this.changeRate = changeRate;
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
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

    public void setCurrencyFlag(String currencyFlag) {
		this.currencyFlag = currencyFlag;
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
     * 设置是否开发票
     *
     * @param invoiceFlag 是否开发票
     */
    public void setInvoiceFlag(Boolean invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
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
     * 设置是否叶子结点
     *
     * @param leafFlag 是否叶子结点
     */
    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
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
     * 设置最大金额
     *
     * @param maxAmount 最大金额
     */
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
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
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setNetvalueFlag(Boolean netvalueFlag) {
		this.netvalueFlag = netvalueFlag;
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
     * 设置上级代码
     *
     * @param parentCode 上级代码
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
     * 设置付款代码
     *
     * @param payCode 付款代码
     */
    public void setPayCode(String payCode) {
        this.payCode = payCode;
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
     * 设置付款名称
     *
     * @param payName 付款名称
     */
    public void setPayName(String payName) {
        this.payName = payName;
    }

    /**
     * 设置付款模式
     *
     * @param payPattern 付款模式
     */
    public void setPayPattern(String payPattern) {
        this.payPattern = payPattern;
    }

    public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
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
     * 设置付款类型
     *
     * @param payType 付款类型
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 设置支付方式ID
     *
     * @param pmid 支付方式ID
     */
    public void setPmid(Long pmid) {
        this.pmid = pmid;
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
     * 设置是否记收入
     *
     * @param recordFlag 是否记收入
     */
    public void setRecordFlag(Boolean recordFlag) {
        this.recordFlag = recordFlag;
    }

    public void setReturnPayFlag(Boolean returnPayFlag) {
		this.returnPayFlag = returnPayFlag;
	}

    /**
     * 设置舍入精度
     *
     * @param roundPrecision 舍入精度
     */
    public void setRoundPrecision(Float roundPrecision) {
        this.roundPrecision = roundPrecision;
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
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 设置修改日期
     *
     * @param updateDate 修改日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
     * 是否积分
     */
    private Boolean creditsFalg;

    public Boolean getCreditsFalg() {
        return creditsFalg;
    }

    public void setCreditsFalg(Boolean creditsFalg) {
        this.creditsFalg = creditsFalg;
    }
}