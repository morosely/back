package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "stallinfo")
public class StallInfoModel {
    /**
     * 档口ID
     */
    @Id
    private Long siid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 档口编号
     */
    private String stallCode;

    /**
     * 档口名称
     */
    private String stallName;

    /**
     * 打印机IP地址
     */
    private String printAddress;

    /**
     * 打印机名称
     */
    private String printName;

    /**
     * 档口状态 0-停用/1-正常
     */
    private Short stallStatus;

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
    
    // 档口自定义业务ID
    private String selfId;
    
    //NAS路径 20200708添加
    private String nasPath;
    
    //档口模式
    private String pattern;
    
    public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getNasPath() {
		return nasPath;
	}

	public void setNasPath(String nasPath) {
		this.nasPath = nasPath;
	}

	public String getSelfId() {
		return selfId;
	}

	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	/**
     * 获取档口ID
     *
     * @return siid - 档口ID
     */
    public Long getSiid() {
        return siid;
    }

    /**
     * 设置档口ID
     *
     * @param siid 档口ID
     */
    public void setSiid(Long siid) {
        this.siid = siid;
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
     * 获取门店ID
     *
     * @return shopId - 门店ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置门店ID
     *
     * @param shopId 门店ID
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取门店编码
     *
     * @return shopCode - 门店编码
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * 设置门店编码
     *
     * @param shopCode 门店编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    /**
     * 获取档口编号
     *
     * @return stallCode - 档口编号
     */
    public String getStallCode() {
        return stallCode;
    }

    /**
     * 设置档口编号
     *
     * @param stallCode 档口编号
     */
    public void setStallCode(String stallCode) {
        this.stallCode = stallCode;
    }

    /**
     * 获取档口名称
     *
     * @return stallName - 档口名称
     */
    public String getStallName() {
        return stallName;
    }

    /**
     * 设置档口名称
     *
     * @param stallName 档口名称
     */
    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    /**
     * 获取打印机IP地址
     *
     * @return printAddress - 打印机IP地址
     */
    public String getPrintAddress() {
        return printAddress;
    }

    /**
     * 设置打印机IP地址
     *
     * @param printAddress 打印机IP地址
     */
    public void setPrintAddress(String printAddress) {
        this.printAddress = printAddress;
    }

    /**
     * 获取打印机名称
     *
     * @return printName - 打印机名称
     */
    public String getPrintName() {
        return printName;
    }

    /**
     * 设置打印机名称
     *
     * @param printName 打印机名称
     */
    public void setPrintName(String printName) {
        this.printName = printName;
    }

    /**
     * 获取档口状态 0-停用/1-正常
     *
     * @return stallStatus - 档口状态 0-停用/1-正常
     */
    public Short getStallStatus() {
        return stallStatus;
    }

    /**
     * 设置档口状态 0-停用/1-正常
     *
     * @param stallStatus 档口状态 0-停用/1-正常
     */
    public void setStallStatus(Short stallStatus) {
        this.stallStatus = stallStatus;
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