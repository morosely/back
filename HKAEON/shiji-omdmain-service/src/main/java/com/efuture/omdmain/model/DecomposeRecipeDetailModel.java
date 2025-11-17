package com.efuture.omdmain.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "decomposerecipedetail")
public class DecomposeRecipeDetailModel {
    /**
     * 商品条码
     */
    private String barNo;

    /**
     * 部类编码
     */
    private String categoryCode;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分解配方单明细ID
     */
    @Id
    private Long drdid;

    /**
     * 分解配方单ID
     */
    private Long drid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String goodsSpec;

    /**
     * 进价金额
     */
    private BigDecimal purPriceAmount;

    /**
     * 配方比
     */
    private Float recipeRate;

    public String getBarNo() {
		return barNo;
	}

    /**
     * 获取部类编码
     *
     * @return categoryCode - 部类编码
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * 获取分类名称
     *
     * @return categoryName - 分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 获取分解配方单明细ID
     *
     * @return drdid - 分解配方单明细ID
     */
    public Long getDrdid() {
        return drdid;
    }

    /**
     * 获取分解配方单ID
     *
     * @return drid - 分解配方单ID
     */
    public Long getDrid() {
        return drid;
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
     * 获取商品编码
     *
     * @return goodsCode - 商品编码
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * 获取商品名称
     *
     * @return goodsName - 商品名称
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 获取规格
     *
     * @return goodsSpec - 规格
     */
    public String getGoodsSpec() {
        return goodsSpec;
    }

	/**
     * 获取进价金额
     *
     * @return purPriceAmount - 进价金额
     */
    public BigDecimal getPurPriceAmount() {
        return purPriceAmount;
    }

	/**
     * 获取配方比
     *
     * @return recipeRate - 配方比
     */
    public Float getRecipeRate() {
        return recipeRate;
    }

    public void setBarNo(String barNo) {
		this.barNo = barNo;
	}

    /**
     * 设置部类编码
     *
     * @param categoryCode 部类编码
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * 设置分类名称
     *
     * @param categoryName 分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 设置分解配方单明细ID
     *
     * @param drdid 分解配方单明细ID
     */
    public void setDrdid(Long drdid) {
        this.drdid = drdid;
    }

    /**
     * 设置分解配方单ID
     *
     * @param drid 分解配方单ID
     */
    public void setDrid(Long drid) {
        this.drid = drid;
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
     * 设置商品编码
     *
     * @param goodsCode 商品编码
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * 设置商品名称
     *
     * @param goodsName 商品名称
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 设置规格
     *
     * @param goodsSpec 规格
     */
    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    /**
     * 设置进价金额
     *
     * @param purPriceAmount 进价金额
     */
    public void setPurPriceAmount(BigDecimal purPriceAmount) {
        this.purPriceAmount = purPriceAmount;
    }

    /**
     * 设置配方比
     *
     * @param recipeRate 配方比
     */
    public void setRecipeRate(Float recipeRate) {
        this.recipeRate = recipeRate;
    }
}