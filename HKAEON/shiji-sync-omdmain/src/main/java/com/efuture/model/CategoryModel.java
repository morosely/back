package com.efuture.model;

import java.util.Date;

public class CategoryModel extends CommonModel{
	
	private static final long serialVersionUID = 1L;

	/*public String getUniqueKey() {
		return "categoryCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.categoryCode+"','"+this.erpCode+"',"+this.entId;
	}*/
	
	//profit ERP系统品类编码允许重复的（不同层次品类编码可以重复），判断唯一性 编码+层级
    public String getUniqueKey() {
    	return "categoryCode,erpCode,entId,level";
    }
    
    public String getUniqueKeyValue() {
    	return "'"+this.categoryCode+"','"+this.erpCode+"',"+this.entId+","+this.level;
    }
    
    //父节点编码
    public String getParentCodeKeyValue() {
        int superLevel = this.level - Short.valueOf("1");
        return "'"+this.parentCode+"','"+this.erpCode+"',"+this.entId+"," + superLevel;
	}

    private Long categoryId;

    private Long entId;

    private String erpCode;

    private String categoryCode;

    private String categoryName;

    private String categorLevel;

    private Long parentId;

    private String parentCode;

    private Integer categoryStatus;

    private Double dmsValue;

    private Integer saleQuotaDays;

    private Integer standardSaleDays;

    private Integer trySaleDays;

    private Integer safeStockDays;

    private Integer minStockDays;

    private Integer maxStockDays;

    private String remark;

    private Short status;

    private Short level;

    private Boolean leafFlag;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Short license;

    private String manageCategoryCode;

    private String restricted;
    
    private Integer shopSheetType;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getCategorLevel() {
        return categorLevel;
    }

    public void setCategorLevel(String categorLevel) {
        this.categorLevel = categorLevel == null ? null : categorLevel.trim();
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

    public Integer getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(Integer categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Double getDmsValue() {
        return dmsValue;
    }

    public void setDmsValue(Double dmsValue) {
        this.dmsValue = dmsValue;
    }

    public Integer getSaleQuotaDays() {
        return saleQuotaDays;
    }

    public void setSaleQuotaDays(Integer saleQuotaDays) {
        this.saleQuotaDays = saleQuotaDays;
    }

    public Integer getStandardSaleDays() {
        return standardSaleDays;
    }

    public void setStandardSaleDays(Integer standardSaleDays) {
        this.standardSaleDays = standardSaleDays;
    }

    public Integer getTrySaleDays() {
        return trySaleDays;
    }

    public void setTrySaleDays(Integer trySaleDays) {
        this.trySaleDays = trySaleDays;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Short getLicense() {
        return license;
    }

    public void setLicense(Short license) {
        this.license = license;
    }

    public String getManageCategoryCode() {
        return manageCategoryCode;
    }

    public void setManageCategoryCode(String manageCategoryCode) {
        this.manageCategoryCode = manageCategoryCode == null ? null : manageCategoryCode.trim();
    }

    public String getRestricted() {
        return restricted;
    }

    public void setRestricted(String restricted) {
        this.restricted = restricted == null ? null : restricted.trim();
    }

	public Integer getShopSheetType() {
		return shopSheetType;
	}

	public void setShopSheetType(Integer shopSheetType) {
		this.shopSheetType = shopSheetType;
	}
    
}