package com.efuture.omdmain.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "processrecipedetail")
public class ProcessRecipeDetailModel {
    /**
     * 加工配方单明细ID
     */
    @Id
    private Long prdid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 加工配方单ID
     */
    private Long prid;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品条码
     */
    private String barNo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 部类编码
     */
    private String categoryCode;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 进价金额
     */
    private BigDecimal purPriceAmount;

    /**
     * 规格
     */
    private String goodsSpec;

    /**
     * 重量
     */
    private Float weight;

    /**
     * 配方比
     */
    private Float recipeRate;

    /**
     * 获取加工配方单明细ID
     *
     * @return prdid - 加工配方单明细ID
     */
    public Long getPrdid() {
        return prdid;
    }

    /**
     * 设置加工配方单明细ID
     *
     * @param prdid 加工配方单明细ID
     */
    public void setPrdid(Long prdid) {
        this.prdid = prdid;
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
     * 获取加工配方单ID
     *
     * @return prid - 加工配方单ID
     */
    public Long getPrid() {
        return prid;
    }

    /**
     * 设置加工配方单ID
     *
     * @param prid 加工配方单ID
     */
    public void setPrid(Long prid) {
        this.prid = prid;
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
     * 获取商品条码
     *
     * @return barCode - 商品条码
     */
    public String getBarNo() {
        return barNo;
    }

    /**
     * 设置商品条码
     *
     * @param barCode 商品条码
     */
    public void setBarCode(String barNo) {
        this.barNo = barNo;
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
     * 设置商品名称
     *
     * @param goodsName 商品名称
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
     * 设置部类编码
     *
     * @param categoryCode 部类编码
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
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
     * 设置分类名称
     *
     * @param categoryName 分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
     * 设置进价金额
     *
     * @param purPriceAmount 进价金额
     */
    public void setPurPriceAmount(BigDecimal purPriceAmount) {
        this.purPriceAmount = purPriceAmount;
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
     * 设置规格
     *
     * @param goodsSpec 规格
     */
    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    /**
     * 获取重量
     *
     * @return weight - 重量
     */
    public Float getWeight() {
        return weight;
    }

    /**
     * 设置重量
     *
     * @param weight 重量
     */
    public void setWeight(Float weight) {
        this.weight = weight;
    }

    /**
     * 获取配方比
     *
     * @return recipeRate - 配方比
     */
    public Float getRecipeRate() {
        return recipeRate;
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