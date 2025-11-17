package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "goodspropertyextvalue")
public class GoodsPropertyExtValueModel {
    /**
     * 商品扩展属性值ID
     */
    @Id
    private Long gpevid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 母品ID
     */
    private Long psgid;

    /**
     * 母品编码
     */
    private String pgoodsCode;

    /**
     * 商品扩展属性ID
     */
    private Long gpeid;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private String goodsCode;

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
     * 属性值编码
     */
    private String propertyValueCode;

    /**
     * 属性值
     */
    private String propertyValue;

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
     * 获取商品扩展属性值ID
     *
     * @return gpevid - 商品扩展属性值ID
     */
    public Long getGpevid() {
        return gpevid;
    }

    /**
     * 设置商品扩展属性值ID
     *
     * @param gpevid 商品扩展属性值ID
     */
    public void setGpevid(Long gpevid) {
        this.gpevid = gpevid;
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
     * 获取母品ID
     *
     * @return psgid - 母品ID
     */
    public Long getPsgid() {
        return psgid;
    }

    /**
     * 设置母品ID
     *
     * @param psgid 母品ID
     */
    public void setPsgid(Long psgid) {
        this.psgid = psgid;
    }

    /**
     * 获取母品编码
     *
     * @return pgoodsCode - 母品编码
     */
    public String getPgoodsCode() {
        return pgoodsCode;
    }

    /**
     * 设置母品编码
     *
     * @param pgoodsCode 母品编码
     */
    public void setPgoodsCode(String pgoodsCode) {
        this.pgoodsCode = pgoodsCode;
    }

    /**
     * 获取商品扩展属性ID
     *
     * @return gpeid - 商品扩展属性ID
     */
    public Long getGpeid() {
        return gpeid;
    }

    /**
     * 设置商品扩展属性ID
     *
     * @param gpeid 商品扩展属性ID
     */
    public void setGpeid(Long gpeid) {
        this.gpeid = gpeid;
    }

    /**
     * 获取商品ID
     *
     * @return sgid - 商品ID
     */
    public Long getSgid() {
        return sgid;
    }

    /**
     * 设置商品ID
     *
     * @param sgid 商品ID
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    /**
     * 获取商品编码
     *
     * @return goodsCode - 商品编码
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * 设置商品编码
     *
     * @param goodsCode 商品编码
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
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
     * 获取属性值编码
     *
     * @return propertyValueCode - 属性值编码
     */
    public String getPropertyValueCode() {
        return propertyValueCode;
    }

    /**
     * 设置属性值编码
     *
     * @param propertyValueCode 属性值编码
     */
    public void setPropertyValueCode(String propertyValueCode) {
        this.propertyValueCode = propertyValueCode;
    }

    /**
     * 获取属性值
     *
     * @return propertyValue - 属性值
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * 设置属性值
     *
     * @param propertyValue 属性值
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
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