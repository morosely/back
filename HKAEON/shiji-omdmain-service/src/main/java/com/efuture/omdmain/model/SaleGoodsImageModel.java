package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "salegoodsimage")
public class SaleGoodsImageModel {
    /**
     * sku商品图片ID
     */
    @Id
    private Long saleImageId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 图片类型 默认-0/POS主图-1/缩略图-2
     */
    private Integer imageType;

    /**
     * 显示终端
     */
    private Integer showTerm;

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
     * 获取sku商品图片ID
     *
     * @return saleImageId - sku商品图片ID
     */
    public Long getSaleImageId() {
        return saleImageId;
    }

    /**
     * 设置sku商品图片ID
     *
     * @param saleImageId sku商品图片ID
     */
    public void setSaleImageId(Long saleImageId) {
        this.saleImageId = saleImageId;
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
     * 获取文件名称
     *
     * @return fileName - 文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取图片类型 默认-0/POS主图-1/缩略图-2
     *
     * @return imageType - 图片类型 默认-0/POS主图-1/缩略图-2
     */
    public Integer getImageType() {
        return imageType;
    }

    /**
     * 设置图片类型 默认-0/POS主图-1/缩略图-2
     *
     * @param imageType 图片类型 默认-0/POS主图-1/缩略图-2
     */
    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    /**
     * 获取显示终端
     *
     * @return showTerm - 显示终端
     */
    public Integer getShowTerm() {
        return showTerm;
    }

    /**
     * 设置显示终端
     *
     * @param showTerm 显示终端
     */
    public void setShowTerm(Integer showTerm) {
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