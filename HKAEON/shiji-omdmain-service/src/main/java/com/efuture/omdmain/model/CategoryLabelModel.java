package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "categorylabel")
public class CategoryLabelModel {
    /**
     * 类别标签ID
     */
    @Id
    private Long clid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 类别标签CODE
     */
    private String catLabelCode;

    /**
     * 类别标签名称
     */
    private String catLabelName;

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
     * 获取类别标签ID
     *
     * @return clid - 类别标签ID
     */
    public Long getClid() {
        return clid;
    }

    /**
     * 设置类别标签ID
     *
     * @param clid 类别标签ID
     */
    public void setClid(Long clid) {
        this.clid = clid;
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
     * 获取类别标签CODE
     *
     * @return catLabelCode - 类别标签CODE
     */
    public String getCatLabelCode() {
        return catLabelCode;
    }

    /**
     * 设置类别标签CODE
     *
     * @param catLabelCode 类别标签CODE
     */
    public void setCatLabelCode(String catLabelCode) {
        this.catLabelCode = catLabelCode;
    }

    /**
     * 获取类别标签名称
     *
     * @return catLabelName - 类别标签名称
     */
    public String getCatLabelName() {
        return catLabelName;
    }

    /**
     * 设置类别标签名称
     *
     * @param catLabelName 类别标签名称
     */
    public void setCatLabelName(String catLabelName) {
        this.catLabelName = catLabelName;
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