package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "setmealdetail")
public class SetMealDetailModel {
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
     * 经营公司编码
     */
    private String erpCode;

	/**
     * 商品编码
     */
    private String goodsCode;

	/**
     * 商品ID
     */
    private Long goodsId;

	/**
     * 语言类型
     */
    private String lang;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 套餐编码
     */
    private String sgoodsCode;

    /**
     * 套餐商品ID
     */
    private Long sgoodsId;

    private String shopCode;

    private Long shopId;

    /**
     * 套餐商品明细ID
     */
    @Id
    private Long smdid;

    /**
     * 种类名称
     */
    private String typeName;

    /**
     * 修改日期
     */
    private Date updateDate;

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

    /**
     * 获取经营公司编码
     *
     * @return erpCode - 经营公司编码
     */
    public String getErpCode() {
        return erpCode;
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
     * 获取商品ID
     *
     * @return goodsId - 商品ID
     */
    public Long getGoodsId() {
        return goodsId;
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
     * 获取套餐编码
     *
     * @return sgoodsCode - 套餐编码
     */
    public String getSgoodsCode() {
        return sgoodsCode;
    }

    /**
     * 获取套餐商品ID
     *
     * @return sgoodsId - 套餐商品ID
     */
    public Long getSgoodsId() {
        return sgoodsId;
    }

    public String getShopCode() {
		return shopCode;
	}

    public Long getShopId() {
		return shopId;
	}

    /**
     * 获取套餐商品明细ID
     *
     * @return smdid - 套餐商品明细ID
     */
    public Long getSmdid() {
        return smdid;
    }

    /**
     * 获取种类名称
     *
     * @return typeName - 种类名称
     */
    public String getTypeName() {
        return typeName;
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

    /**
     * 设置经营公司编码
     *
     * @param erpCode 经营公司编码
     */
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
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
     * 设置商品ID
     *
     * @param goodsId 商品ID
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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
     * 设置套餐编码
     *
     * @param sgoodsCode 套餐编码
     */
    public void setSgoodsCode(String sgoodsCode) {
        this.sgoodsCode = sgoodsCode;
    }

    /**
     * 设置套餐商品ID
     *
     * @param sgoodsId 套餐商品ID
     */
    public void setSgoodsId(Long sgoodsId) {
        this.sgoodsId = sgoodsId;
    }

    public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

    public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

    /**
     * 设置套餐商品明细ID
     *
     * @param smdid 套餐商品明细ID
     */
    public void setSmdid(Long smdid) {
        this.smdid = smdid;
    }

    /**
     * 设置种类名称
     *
     * @param typeName 种类名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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