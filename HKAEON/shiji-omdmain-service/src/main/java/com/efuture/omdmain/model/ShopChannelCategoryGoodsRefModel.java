package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "shopchannelcategorygoodsref")
public class ShopChannelCategoryGoodsRefModel {
    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 有效状态0:无效1有效
     */
    private String isValid;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 唯一序列号
     */
    @Id
    private Long sccgrid;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 组织机构ID
     */
    private Long shopId;

    /**
     * 展示分类ID
     */
    private Long showCategoryId;
    
    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取渠道ID
     *
     * @return channelId - 渠道ID
     */
    public Long getChannelId() {
        return channelId;
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 获取零售商ID
     *
     * @return entId - 零售商ID
     */
    public Long getEntId() {
        return entId;
    }

    public String getIsValid() {
		return isValid;
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
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 获取唯一序列号
     *
     * @return sccgrid - 唯一序列号
     */
    public Long getSccgrid() {
        return sccgrid;
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
     * 获取组织机构ID
     *
     * @return shopId - 组织机构ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 获取展示分类ID
     *
     * @return showCategoryId - 展示分类ID
     */
    public Long getShowCategoryId() {
        return showCategoryId;
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
     * 设置渠道ID
     *
     * @param channelId 渠道ID
     */
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public void setIsValid(String isValid) {
		this.isValid = isValid;
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
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 设置唯一序列号
     *
     * @param sccgrid 唯一序列号
     */
    public void setSccgrid(Long sccgrid) {
        this.sccgrid = sccgrid;
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
     * 设置组织机构ID
     *
     * @param shopId 组织机构ID
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * 设置展示分类ID
     *
     * @param showCategoryId 展示分类ID
     */
    public void setShowCategoryId(Long showCategoryId) {
        this.showCategoryId = showCategoryId;
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