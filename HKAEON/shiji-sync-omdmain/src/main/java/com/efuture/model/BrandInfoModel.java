package com.efuture.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class BrandInfoModel extends CommonModel{
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "brandCode";
	}
    
    public String getUniqueKeyValue() {
		return this.brandCode;
	}

    
    /**品牌编码*/
    private String brandCode;

    /**品牌描述*/
    private String brandDesc;
    
    /**英文名称*/
    private String brandEnName;

    /**品牌ID*/
    private Long brandId;

    /**品牌等级编码*/
    private String brandLevelCode;

    /**品牌名称*/
    private String brandName;

    /**品牌简码*/
    private String brandSCode;

    /**品牌分类编码*/
    private String brandTypeCode;

    /**创建日期*/
    private Date createDate;

    /**创建人*/
    private String creator;

    /**设计师*/
    private String designer;

    /**零售商ID*/
    private Long entId;

    /**经营公司编码*/
    private String erpCode;

    /**经营公司名称*/
    private String erpName;

    /**语言类型*/
    private String lang;

    /**修改人*/
    private String modifier;

    /**状态*/
    private Integer status;

    /**修改日期*/
    private Date updateDate;
    
    /**品牌国*/
    private String brandOriginCountryCode;

    public String getBrandOriginCountryCode() {
		return brandOriginCountryCode;
	}

	public void setBrandOriginCountryCode(String brandOriginCountryCode) {
		this.brandOriginCountryCode = brandOriginCountryCode;
	}

    public String getBrandCode() {
        return this.brandCode;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public String getBrandEnName() {
        return brandEnName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getBrandLevelCode() {
        return brandLevelCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandSCode() {
        return brandSCode;
    }

    public String getBrandTypeCode() {
        return brandTypeCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreator() {
        return creator;
    }

    public String getDesigner() {
        return designer;
    }

    public Long getEntId() {
        return entId;
    }

    public String getErpCode() {
        return erpCode;
    }

    public String getErpName() {
        return erpName;
    }

    public String getLang() {
        return lang;
    }

    public String getModifier() {
        return modifier;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode == null ? null : brandCode.trim();
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc == null ? null : brandDesc.trim();
    }

    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName == null ? null : brandEnName.trim();
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void setBrandLevelCode(String brandLevelCode) {
        this.brandLevelCode = brandLevelCode == null ? null : brandLevelCode.trim();
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public void setBrandSCode(String brandSCode) {
        this.brandSCode = brandSCode == null ? null : brandSCode.trim();
    }

    public void setBrandTypeCode(String brandTypeCode) {
        this.brandTypeCode = brandTypeCode == null ? null : brandTypeCode.trim();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public void setDesigner(String designer) {
        this.designer = designer == null ? null : designer.trim();
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public void setErpName(String erpName) {
        this.erpName = erpName == null ? null : erpName.trim();
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}