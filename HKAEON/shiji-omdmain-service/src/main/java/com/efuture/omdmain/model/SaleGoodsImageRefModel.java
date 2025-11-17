package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "salegoodsimageref")
public class SaleGoodsImageRefModel {
    /**
     * 商品图片关联ID
     */
    @Id
    private Long sgirid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 图片类型 默认-0/POS主图-1/缩略图-2
     */
    private Short imageType;

    /**
     * 显示终端0-PC端/1-移动端
     */
    private Short showTerm;

    /**
     * 图片url
     */
    private String imageUrl;

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
     * 获取商品图片关联ID
     *
     * @return sgirid - 商品图片关联ID
     */
    public Long getSgirid() {
        return sgirid;
    }

    /**
     * 设置商品图片关联ID
     *
     * @param sgirid 商品图片关联ID
     */
    public void setSgirid(Long sgirid) {
        this.sgirid = sgirid;
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
     * 获取图片类型 默认-0/POS主图-1/缩略图-2
     *
     * @return imageType - 图片类型 默认-0/POS主图-1/缩略图-2
     */
    public Short getImageType() {
        return imageType;
    }

    /**
     * 设置图片类型 默认-0/POS主图-1/缩略图-2
     *
     * @param imageType 图片类型 默认-0/POS主图-1/缩略图-2
     */
    public void setImageType(Short imageType) {
        this.imageType = imageType;
    }

    /**
     * 获取显示终端0-PC端/1-移动端
     *
     * @return showTerm - 显示终端0-PC端/1-移动端
     */
    public Short getShowTerm() {
        return showTerm;
    }

    /**
     * 设置显示终端0-PC端/1-移动端
     *
     * @param showTerm 显示终端0-PC端/1-移动端
     */
    public void setShowTerm(Short showTerm) {
        this.showTerm = showTerm;
    }

    /**
     * 获取图片url
     *
     * @return imageUrl - 图片url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置图片url
     *
     * @param imageUrl 图片url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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