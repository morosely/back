package com.efuture.omdmain.model;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

import java.util.Date;
import javax.persistence.*;

@Table(name = "brandinfo")
public class BrandInfoModel {
    /**
     * 品牌编码
     */
    private String brandCode;

    /**
     * 品牌描述
     */
    private String brandDesc;

    /**
     * 英文名称
     */
    private String brandEnName;

    /**
     * 品牌ID
     */
    @Id
    private Long brandId;

    /**
     * 品牌等级编码
     */
    private String brandLevelCode;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌简码
     */
    private String brandSCode;

    /**
     * 品牌分类编码
     */
    private String brandTypeCode;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 设计师
     */
    private String designer;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 经营公司名称
     */
    @ReferQuery(table="businessCompany",query="{erpCode:'$erpCode', entId:'$entId'}",set="{erpName:'erpName'}",operationFlags={OperationFlag.afterQuery})
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
     * 状态
     */
    private Short status;

    /**
     * 修改日期
     */
    private Date updateDate;
    
    /**
         * 品牌国
     */
    private String brandOriginCountryCode;

    public String getBrandOriginCountryCode() {
		return brandOriginCountryCode;
	}

	public void setBrandOriginCountryCode(String brandOriginCountryCode) {
		this.brandOriginCountryCode = brandOriginCountryCode;
	}

	/**
     * 获取品牌编码
     *
     * @return brandCode - 品牌编码
     */
    public String getBrandCode() {
        return brandCode;
    }

    /**
     * 获取品牌描述
     *
     * @return brandDesc - 品牌描述
     */
    public String getBrandDesc() {
        return brandDesc;
    }

    /**
     * 获取英文名称
     *
     * @return brandEnName - 英文名称
     */
    public String getBrandEnName() {
        return brandEnName;
    }

    /**
     * 获取品牌ID
     *
     * @return brandId - 品牌ID
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * 获取品牌等级编码
     *
     * @return brandLevelCode - 品牌等级编码
     */
    public String getBrandLevelCode() {
        return brandLevelCode;
    }

    /**
     * 获取品牌名称
     *
     * @return brandName - 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 获取品牌简码
     *
     * @return brandSCode - 品牌简码
     */
    public String getBrandSCode() {
        return brandSCode;
    }

    /**
     * 获取品牌分类编码
     *
     * @return brandTypeCode - 品牌分类编码
     */
    public String getBrandTypeCode() {
        return brandTypeCode;
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

    /**
     * 获取设计师
     *
     * @return designer - 设计师
     */
    public String getDesigner() {
        return designer;
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
     * 设置品牌编码
     *
     * @param brandCode 品牌编码
     */
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    /**
     * 设置品牌描述
     *
     * @param brandDesc 品牌描述
     */
    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    /**
     * 设置英文名称
     *
     * @param brandEnName 英文名称
     */
    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName;
    }

    /**
     * 设置品牌ID
     *
     * @param brandId 品牌ID
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * 设置品牌等级编码
     *
     * @param brandLevelCode 品牌等级编码
     */
    public void setBrandLevelCode(String brandLevelCode) {
        this.brandLevelCode = brandLevelCode;
    }

    /**
     * 设置品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 设置品牌简码
     *
     * @param brandSCode 品牌简码
     */
    public void setBrandSCode(String brandSCode) {
        this.brandSCode = brandSCode;
    }

    /**
     * 设置品牌分类编码
     *
     * @param brandTypeCode 品牌分类编码
     */
    public void setBrandTypeCode(String brandTypeCode) {
        this.brandTypeCode = brandTypeCode;
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
     * 设置设计师
     *
     * @param designer 设计师
     */
    public void setDesigner(String designer) {
        this.designer = designer;
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