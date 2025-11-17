package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "goodsupanddown")
public class GoodsUpAndDownModel {
    /**
     * 唯一序列号
     */
    @Id
    private Long guadid;
    
    /**
     * 有效状态0:无效1有效
     */
    private String isValid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 组织机构ID
     */
    private Long shopId;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 商品ID
     */
//    private Long sgid;

    public Long getSsgid() {
        return ssgid;
    }

    public void setSsgid(Long ssgid) {
        this.ssgid = ssgid;
    }

    public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	private Long ssgid;

    /**
     * 商品上下架状态 0-下架 1-上架
     */
    private Short updownStatus;

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
     * 获取唯一序列号
     *
     * @return guadid - 唯一序列号
     */
    public Long getGuadid() {
        return guadid;
    }

    /**
     * 设置唯一序列号
     *
     * @param guadid 唯一序列号
     */
    public void setGuadid(Long guadid) {
        this.guadid = guadid;
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
     * 获取组织机构ID
     *
     * @return shopId - 组织机构ID
     */
    public Long getShopId() {
        return shopId;
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
     * 获取渠道ID
     *
     * @return channelId - 渠道ID
     */
    public Long getChannelId() {
        return channelId;
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
     * 获取商品ID
     *
     * @return sgid - 商品ID
     */
//    public Long getSgid() {
//        return sgid;
//    }

    /**
     * 设置商品ID
     *
     * @param sgid 商品ID
     */
//    public void setSgid(Long sgid) {
//        this.sgid = sgid;
//    }

    /**
     * 获取商品上下架状态 0-下架 1-上架
     *
     * @return updownStatus - 商品上下架状态 0-下架 1-上架
     */
    public Short getUpdownStatus() {
        return updownStatus;
    }

    /**
     * 设置商品上下架状态 0-下架 1-上架
     *
     * @param updownStatus 商品上下架状态 0-下架 1-上架
     */
    public void setUpdownStatus(Short updownStatus) {
        this.updownStatus = updownStatus;
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