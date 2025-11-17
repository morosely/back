package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dictionarytype")
public class DictionaryTypeModel {
    /**
     * 字典类别ID
     */
    @Id
    private Long dtid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 字典类别CODE
     */
    private String dictTypeCode;

    /**
     * 字典类别名称
     */
    private String dictTypeName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 是否自动编码
     */
    private Boolean autoCode;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取字典类别ID
     *
     * @return dtid - 字典类别ID
     */
    public Long getDtid() {
        return dtid;
    }

    /**
     * 设置字典类别ID
     *
     * @param dtid 字典类别ID
     */
    public void setDtid(Long dtid) {
        this.dtid = dtid;
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
     * 获取字典类别CODE
     *
     * @return dictTypeCode - 字典类别CODE
     */
    public String getDictTypeCode() {
        return dictTypeCode;
    }

    /**
     * 设置字典类别CODE
     *
     * @param dictTypeCode 字典类别CODE
     */
    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }

    /**
     * 获取字典类别名称
     *
     * @return dictTypeName - 字典类别名称
     */
    public String getDictTypeName() {
        return dictTypeName;
    }

    /**
     * 设置字典类别名称
     *
     * @param dictTypeName 字典类别名称
     */
    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
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
     * 获取是否自动编码
     *
     * @return autoCode - 是否自动编码
     */
    public Boolean getAutoCode() {
        return autoCode;
    }

    /**
     * 设置是否自动编码
     *
     * @param autoCode 是否自动编码
     */
    public void setAutoCode(Boolean autoCode) {
        this.autoCode = autoCode;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
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