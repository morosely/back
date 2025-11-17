package com.efuture.omdmain.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.product.annotation.NotNull;
import com.product.annotation.ReferQuery;
import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;

@Table(name = "paymentmethodref")
//@UniqueKey(table="paymentmethodref",keys = { "payCode","paymentModeId","erpCode","entId" },operationFlags={OperationFlag.Insert},message="支付方式【${payName}】及ERP支付方式【${paymentModeName}】必须唯一")
//@UniqueKey(table="paymentmethodref",keys = { "payCode","paymentModeId","erpCode","entId" },primaryKey="pmrid",operationFlags={OperationFlag.Update},message="支付方式【${payName}】及ERP支付方式【${paymentModeName}】必须唯一")
public class PaymentMethodRefModel {
    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 零售商ID
     */
    private Long entId;
    
    /**
     * 经营公司编码
     */
    
    private String erpCode;

    @Transient
    @ReferQuery(table = "businesscompany", query = "{erpCode:'$erpCode'}", set = "{erpName:'erpName'}", operationFlags = { OperationFlag.afterQuery })
    private String erpName;
    
    /**
     * 语言类型
     */
    private String lang;

    /**
     * 修改人
     */
    private String modifier;

	/**
     * 付款代码
     */
    @NotNull(operationFlags = { OperationFlag.Insert })
    private String payCode;

	//辅助字段：PayCode查询Pmid
    @ReferQuery(table = "paymentmethod", query = "{payCode:'$payCode'}", set = "{pmid:'pmid'}", operationFlags = { OperationFlag.Insert })
    private String payCodeToPmid;

    /**
     * ERP付款代码
     */
    private String paymentModeId;

    /**
     * ERP付款名称
     */
    private String paymentModeName;

    /**
	 * ERP付款简码
	 */
	private String paymentSMode;

    /**
     * 付款名称
     */
    @NotNull(operationFlags = { OperationFlag.Insert })
    private String payName;

    /**
     * 支付方式ID
     */
    private Long pmid;

    /**
     * 支付方式关系ID
     */
    @Id
    private Long pmrid;

    /**
     * 描述
     */
    private String remark;

    /**
     * 状态
     */
    private Short status;

    /**
     * 修改日期
     */
    private Date updateDate;

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

	/**
     * 获取零售商ID
     *
     * @return entId - 零售商ID
     */
    public Long getEntId() {
        return entId;
    }

	/**
     * 获取经营公司编码
     *
     * @return erpCode - 经营公司编码
     */
    public String getErpCode() {
        return erpCode;
    }
    
    
    public String getErpName() {
		return erpName;
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
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 获取付款代码
     *
     * @return payCode - 付款代码
     */
    public String getPayCode() {
        return payCode;
    }

    public String getPayCodeToPmid() {
		return payCodeToPmid;
	}

    /**
     * 获取ERP付款代码
     *
     * @return paymentModeId - ERP付款代码
     */
    public String getPaymentModeId() {
        return paymentModeId;
    }

    /**
     * 获取ERP付款名称
     *
     * @return paymentModeName - ERP付款名称
     */
    public String getPaymentModeName() {
        return paymentModeName;
    }

    public String getPaymentSMode() {
		return paymentSMode;
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
     * 获取支付方式ID
     *
     * @return pmid - 支付方式ID
     */
    public Long getPmid() {
        return pmid;
    }

    /**
     * 获取支付方式关系ID
     *
     * @return pmrid - 支付方式关系ID
     */
    public Long getPmrid() {
        return pmrid;
    }

    /**
     * 获取描述
     *
     * @return remark - 描述
     */
    public String getRemark() {
        return remark;
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

    /**
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 设置经营公司编码
     *
     * @param erpCode 经营公司编码
     */
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public void setErpName(String erpName) {
		this.erpName = erpName;
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
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 设置付款代码
     *
     * @param payCode 付款代码
     */
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public void setPayCodeToPmid(String payCodeToPmid) {
		this.payCodeToPmid = payCodeToPmid;
	}

    /**
     * 设置ERP付款代码
     *
     * @param paymentModeId ERP付款代码
     */
    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    /**
     * 设置ERP付款名称
     *
     * @param paymentModeName ERP付款名称
     */
    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public void setPaymentSMode(String paymentSMode) {
		this.paymentSMode = paymentSMode;
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
     * 设置支付方式ID
     *
     * @param pmid 支付方式ID
     */
    public void setPmid(Long pmid) {
        this.pmid = pmid;
    }

    /**
     * 设置支付方式关系ID
     *
     * @param pmrid 支付方式关系ID
     */
    public void setPmrid(Long pmrid) {
        this.pmrid = pmrid;
    }

    /**
     * 设置描述
     *
     * @param remark 描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
    
}