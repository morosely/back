package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

import org.springframework.data.annotation.Transient;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

@Table(name = "category")
public class CategoryModel {
	
	// 门店库存属性0-单店/1-全店
	private Integer shopSheetType;

	public Integer getShopSheetType() {
		return shopSheetType;
	}

	public void setShopSheetType(Integer shopSheetType) {
		this.shopSheetType = shopSheetType;
	}
	
	//受限标识(0:否,1:是)
	private String restricted;
	
	public String getRestricted() {
		return restricted;
	}

	public void setRestricted(String restricted) {
		this.restricted = restricted;
	}

	//管理部类编码
	private String manageCategoryCode;

	public String getManageCategoryCode() {
		return manageCategoryCode;
	}

	public void setManageCategoryCode(String manageCategoryCode) {
		this.manageCategoryCode = manageCategoryCode;
	}

	/**
     * 工业分类ID
     */
    @Id
    private Long categoryId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;
    
    @Transient
    @ReferQuery(table="businesscompany",query="{erpCode:'$erpCode',entId:'$entId'}",set="{erpName:'erpName'}",operationFlags={OperationFlag.afterQuery})
    private String erpName;

    public String getErpName() {
		return erpName;
	}

	public void setErpName(String erpName) {
		this.erpName = erpName;
	}

	/**
     * 工业分类编码
     */
    private String categoryCode;

    /**
     * 工业分类名称
     */
    private String categoryName;

    /**
     * 工业分类级别
     */
    private String categorLevel;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 上级代码
     */
    private String parentCode;

    /**
     * 工业分类状态
     */
    private Integer categoryStatus;

    /**
     * 标准DMS值
     */
    private Double dmsValue;

    /**
     * 不动销天数
     */
    private Integer saleQuotaDays;

    /**
     * 标准可销天数
     */
    private Integer standardSaleDays;

    /**
     * 试销天数
     */
    private Integer trySaleDays;

    /**
     * 安全库存天数
     */
    private Integer safeStockDays;

    /**
     * 最小库存天数
     */
    private Integer minStockDays;

    /**
     * 最大库存天数
     */
    private Integer maxStockDays;

    /**
     * 描述
     */
    private String remark;

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
     * 语言类�?�
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
     * 是否打印黄色小票
     */
    private Short license;
    
    /**
     * �?�取工业分类ID
     *
     * @return categoryId - 工业分类ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置工业分类ID
     *
     * @param categoryId 工业分类ID
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * �?�取零售商ID
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
     * �?�取经营公司编码
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
     * �?�取工业分类编码
     *
     * @return categoryCode - 工业分类编码
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * 设置工业分类编码
     *
     * @param categoryCode 工业分类编码
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * �?�取工业分类名称
     *
     * @return categoryName - 工业分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置工业分类名称
     *
     * @param categoryName 工业分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * �?�取工业分类级别
     *
     * @return categorLevel - 工业分类级别
     */
    public String getCategorLevel() {
        return categorLevel;
    }

    /**
     * 设置工业分类级别
     *
     * @param categorLevel 工业分类级别
     */
    public void setCategorLevel(String categorLevel) {
        this.categorLevel = categorLevel;
    }

    /**
     * �?�取上级ID
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
     * �?�取上级代码
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
     * �?�取工业分类状态
     *
     * @return categoryStatus - 工业分类状态
     */
    public Integer getCategoryStatus() {
        return categoryStatus;
    }

    /**
     * 设置工业分类状态
     *
     * @param categoryStatus 工业分类状态
     */
    public void setCategoryStatus(Integer categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    /**
     * �?�取标准DMS值
     *
     * @return dmsValue - 标准DMS值
     */
    public Double getDmsValue() {
        return dmsValue;
    }

    /**
     * 设置标准DMS值
     *
     * @param dmsValue 标准DMS值
     */
    public void setDmsValue(Double dmsValue) {
        this.dmsValue = dmsValue;
    }

    /**
     * �?�取不动销天数
     *
     * @return saleQuotaDays - 不动销天数
     */
    public Integer getSaleQuotaDays() {
        return saleQuotaDays;
    }

    /**
     * 设置不动销天数
     *
     * @param saleQuotaDays 不动销天数
     */
    public void setSaleQuotaDays(Integer saleQuotaDays) {
        this.saleQuotaDays = saleQuotaDays;
    }

    /**
     * �?�取标准可销天数
     *
     * @return standardSaleDays - 标准可销天数
     */
    public Integer getStandardSaleDays() {
        return standardSaleDays;
    }

    /**
     * 设置标准可销天数
     *
     * @param standardSaleDays 标准可销天数
     */
    public void setStandardSaleDays(Integer standardSaleDays) {
        this.standardSaleDays = standardSaleDays;
    }

    /**
     * �?�取试销天数
     *
     * @return trySaleDays - 试销天数
     */
    public Integer getTrySaleDays() {
        return trySaleDays;
    }

    /**
     * 设置试销天数
     *
     * @param trySaleDays 试销天数
     */
    public void setTrySaleDays(Integer trySaleDays) {
        this.trySaleDays = trySaleDays;
    }

    /**
     * �?�取安全库存天数
     *
     * @return safeStockDays - 安全库存天数
     */
    public Integer getSafeStockDays() {
        return safeStockDays;
    }

    /**
     * 设置安全库存天数
     *
     * @param safeStockDays 安全库存天数
     */
    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
    }

    /**
     * �?�取最小库存天数
     *
     * @return minStockDays - 最小库存天数
     */
    public Integer getMinStockDays() {
        return minStockDays;
    }

    /**
     * 设置最小库存天数
     *
     * @param minStockDays 最小库存天数
     */
    public void setMinStockDays(Integer minStockDays) {
        this.minStockDays = minStockDays;
    }

    /**
     * �?�取最大库存天数
     *
     * @return maxStockDays - 最大库存天数
     */
    public Integer getMaxStockDays() {
        return maxStockDays;
    }

    /**
     * 设置最大库存天数
     *
     * @param maxStockDays 最大库存天数
     */
    public void setMaxStockDays(Integer maxStockDays) {
        this.maxStockDays = maxStockDays;
    }

    /**
     * �?�取描述
     *
     * @return remark - 描述
     */
    public String getRemark() {
        return remark;
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
     * �?�取状态
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
     * �?�取层级
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
     * �?�取是否叶子结点
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
     * �?�取语言类�?�
     *
     * @return lang - 语言类�?�
     */
    public String getLang() {
        return lang;
    }

    /**
     * 设置语言类�?�
     *
     * @param lang 语言类�?�
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * �?�取创建人
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
     * �?�取创建日期
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
     * �?�取修改人
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
     * �?�取修改日期
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

	public Short getLicense() {
		return license;
	}

	public void setLicense(Short license) {
		this.license = license;
	}
}