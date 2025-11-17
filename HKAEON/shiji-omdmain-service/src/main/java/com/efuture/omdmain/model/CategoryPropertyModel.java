package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "categoryproperty")
public class CategoryPropertyModel {
    /**
     * 品类属性模板ID
     */
    @Id
    private Long cpmid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 工业分类ID
     */
    private Long categoryId;

    /**
     * 工业分类编码
     */
    private String categoryCode;

    /**
     * 属性编码
     */
    private String propertyCode;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性说明
     */
    private String propertyDesc;

    /**
     * 描述
     */
    private String remark;

    /**
     * 是否生成子品
     */
    private Boolean generatesFlag;

    /**
     * 状态
     */
    private Short status;

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
     * 获取品类属性模板ID
     *
     * @return cpmid - 品类属性模板ID
     */
    public Long getCpmid() {
        return cpmid;
    }

    /**
     * 设置品类属性模板ID
     *
     * @param cpmid 品类属性模板ID
     */
    public void setCpmid(Long cpmid) {
        this.cpmid = cpmid;
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
     * 获取工业分类ID
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
     * 获取工业分类编码
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
     * 获取属性编码
     *
     * @return propertyCode - 属性编码
     */
    public String getPropertyCode() {
        return propertyCode;
    }

    /**
     * 设置属性编码
     *
     * @param propertyCode 属性编码
     */
    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    /**
     * 获取属性名称
     *
     * @return propertyName - 属性名称
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 设置属性名称
     *
     * @param propertyName 属性名称
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 获取属性说明
     *
     * @return propertyDesc - 属性说明
     */
    public String getPropertyDesc() {
        return propertyDesc;
    }

    /**
     * 设置属性说明
     *
     * @param propertyDesc 属性说明
     */
    public void setPropertyDesc(String propertyDesc) {
        this.propertyDesc = propertyDesc;
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
     * 设置描述
     *
     * @param remark 描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取是否生成子品
     *
     * @return generatesFlag - 是否生成子品
     */
    public Boolean getGeneratesFlag() {
        return generatesFlag;
    }

    /**
     * 设置是否生成子品
     *
     * @param generatesFlag 是否生成子品
     */
    public void setGeneratesFlag(Boolean generatesFlag) {
        this.generatesFlag = generatesFlag;
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