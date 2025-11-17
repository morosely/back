package com.efuture.model;

import java.util.Date;

public class ExtBrandInfoModel extends CommonExtModel{
	
	private static final long serialVersionUID = 1L;
    
	public String getUniqueKey() {
		return "brandCode";
	}
    
    public String getUniqueKeyValue() {
		return this.brandCode;
	}
	
    /**品牌ID*/
    private Long ebrandId;

    /**零售商ID*/
    private Long entId;

    /**品牌名称*/
    private String brandName;

    /**品牌简码*/
    private String brandSCode;

    /**品牌编码*/
    private String brandCode;

    /**品牌分类编码*/
    private String brandTypeCode;

    /**品牌等级编码*/
    private String brandLevelCode;

    /**英文名称*/
    private String brandEnName;

    /**设计师*/
    private String designer;

    /**品牌描述*/
    private String brandDesc;

    /**经营公司编码*/
    private String erpCode;

    /**经营公司名称*/
    private String erpName;

    /**状态*/
    private Integer status;

    /**数据来源编码*/
    private String sourceFromCode;

    /**0-未处理/1-处理中/2-已处理*/
    private Integer dealStatus;

    /**语言类型*/
    private String lang;

    /**创建人*/
    private String creator;

    /**创建日期*/
    private Date createDate;

    /**修改人*/
    private String modifier;

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

	public Long getEbrandId() {
        return ebrandId;
    }

    public void setEbrandId(Long ebrandId) {
        this.ebrandId = ebrandId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getBrandSCode() {
        return brandSCode;
    }

    public void setBrandSCode(String brandSCode) {
        this.brandSCode = brandSCode == null ? null : brandSCode.trim();
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode == null ? null : brandCode.trim();
    }

    public String getBrandTypeCode() {
        return brandTypeCode;
    }

    public void setBrandTypeCode(String brandTypeCode) {
        this.brandTypeCode = brandTypeCode == null ? null : brandTypeCode.trim();
    }

    public String getBrandLevelCode() {
        return brandLevelCode;
    }

    public void setBrandLevelCode(String brandLevelCode) {
        this.brandLevelCode = brandLevelCode == null ? null : brandLevelCode.trim();
    }

    public String getBrandEnName() {
        return brandEnName;
    }

    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName == null ? null : brandEnName.trim();
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer == null ? null : designer.trim();
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc == null ? null : brandDesc.trim();
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public String getErpName() {
        return erpName;
    }

    public void setErpName(String erpName) {
        this.erpName = erpName == null ? null : erpName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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