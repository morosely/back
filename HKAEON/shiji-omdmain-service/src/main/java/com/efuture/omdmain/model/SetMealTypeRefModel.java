package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "setmealtyperef")
public class SetMealTypeRefModel {
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

	private String erpCode;
	/**
     * 套餐编码
     */
    private String goodsCode;
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
     * 可选数量
     */
    private Integer optionNum;

	private String shopCode;

	private Long shopId;

	/**
     * 档口套餐种类关联ID
     */
    @Id
    private Long smtrid;

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

    public String getErpCode() {
		return erpCode;
	}

    /**
     * 获取套餐编码
     *
     * @return goodsCode - 套餐编码
     */
    public String getGoodsCode() {
        return goodsCode;
    }

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
     * 获取可选数量
     *
     * @return optionNum - 可选数量
     */
    public Integer getOptionNum() {
        return optionNum;
    }

    public String getShopCode() {
		return shopCode;
	}

    public Long getShopId() {
		return shopId;
	}

    /**
     * 获取档口套餐种类关联ID
     *
     * @return smtrid - 档口套餐种类关联ID
     */
    public Long getSmtrid() {
        return smtrid;
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

    public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

    /**
     * 设置套餐编码
     *
     * @param goodsCode 套餐编码
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

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
     * 设置可选数量
     *
     * @param optionNum 可选数量
     */
    public void setOptionNum(Integer optionNum) {
        this.optionNum = optionNum;
    }

    public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

    public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

    /**
     * 设置档口套餐种类关联ID
     *
     * @param smtrid 档口套餐种类关联ID
     */
    public void setSmtrid(Long smtrid) {
        this.smtrid = smtrid;
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