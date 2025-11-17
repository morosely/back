package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "shopgoodsinfoupdate")
public class ShopGoodsInfoUpdateModel {
    /**
     * 门店商品资料修改ID
     */
    @Id
    private Long shopGoodsInfoId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 经营公司名称
     */
    private String erpName;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称(旧)
     */
    private String oldGoodsName;

    /**
     * 商品展示名称(旧)
     */
    private String oldGoodsDisplayName;

    /**
     * 商品价格(旧)
     */
    private BigDecimal oldSalePrice;

    /**
     * 管制商品标签(旧)
     */
    private String oldGoodsFromCode;

    /**
     * 工业分类编码(旧)
     */
    private String oldCategoryCode;

    /**
     * 工业分类名称(旧)
     */
    private String oldCategoryName;

    /**
     * 商品名称(新)
     */
    private String newGoodsName;

    /**
     * 商品展示名称(新)
     */
    private String newGoodsDisplayName;

    /**
     * 商品价格(新)
     */
    private BigDecimal newSalePrice;

    /**
     * 管制商品标签(新)
     */
    private String newGoodsFromCode;

    /**
     * 工业分类编码(新)
     */
    private String newCategoryCode;

    /**
     * 工业分类名称(新)
     */
    private String newCategoryName;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 可售商品ID
     */
    private Long ssgid;

    public Long getSsgid() {
		return ssgid;
	}

	public void setSsgid(Long ssgid) {
		this.ssgid = ssgid;
	}

	/**
     * 获取门店商品资料修改ID
     *
     * @return shopGoodsInfoId - 门店商品资料修改ID
     */
    public Long getShopGoodsInfoId() {
        return shopGoodsInfoId;
    }

    /**
     * 设置门店商品资料修改ID
     *
     * @param shopGoodsInfoId 门店商品资料修改ID
     */
    public void setShopGoodsInfoId(Long shopGoodsInfoId) {
        this.shopGoodsInfoId = shopGoodsInfoId;
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
     * 获取经营公司名称
     *
     * @return erpName - 经营公司名称
     */
    public String getErpName() {
        return erpName;
    }

    /**
     * 设置经营公司名称
     *
     * @param erpName 经营公司名称
     */
    public void setErpName(String erpName) {
        this.erpName = erpName;
    }

    /**
     * 获取门店编码
     *
     * @return shopCode - 门店编码
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * 设置门店编码
     *
     * @param shopCode 门店编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    /**
     * 获取门店名称
     *
     * @return shopName - 门店名称
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 设置门店名称
     *
     * @param shopName 门店名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
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
     * 获取商品名称(旧)
     *
     * @return oldGoodsName - 商品名称(旧)
     */
    public String getOldGoodsName() {
        return oldGoodsName;
    }

    /**
     * 设置商品名称(旧)
     *
     * @param oldGoodsName 商品名称(旧)
     */
    public void setOldGoodsName(String oldGoodsName) {
        this.oldGoodsName = oldGoodsName;
    }

    /**
     * 获取商品展示名称(旧)
     *
     * @return oldGoodsDisplayName - 商品展示名称(旧)
     */
    public String getOldGoodsDisplayName() {
        return oldGoodsDisplayName;
    }

    /**
     * 设置商品展示名称(旧)
     *
     * @param oldGoodsDisplayName 商品展示名称(旧)
     */
    public void setOldGoodsDisplayName(String oldGoodsDisplayName) {
        this.oldGoodsDisplayName = oldGoodsDisplayName;
    }

    /**
     * 获取商品价格(旧)
     *
     * @return oldSalePrice - 商品价格(旧)
     */
    public BigDecimal getOldSalePrice() {
        return oldSalePrice;
    }

    /**
     * 设置商品价格(旧)
     *
     * @param oldSalePrice 商品价格(旧)
     */
    public void setOldSalePrice(BigDecimal oldSalePrice) {
        this.oldSalePrice = oldSalePrice;
    }

    /**
     * 获取管制商品标签(旧)
     *
     * @return oldGoodsFromCode - 管制商品标签(旧)
     */
    public String getOldGoodsFromCode() {
        return oldGoodsFromCode;
    }

    /**
     * 设置管制商品标签(旧)
     *
     * @param oldGoodsFromCode 管制商品标签(旧)
     */
    public void setOldGoodsFromCode(String oldGoodsFromCode) {
        this.oldGoodsFromCode = oldGoodsFromCode;
    }

    /**
     * 获取工业分类编码(旧)
     *
     * @return oldCategoryCode - 工业分类编码(旧)
     */
    public String getOldCategoryCode() {
        return oldCategoryCode;
    }

    /**
     * 设置工业分类编码(旧)
     *
     * @param oldCategoryCode 工业分类编码(旧)
     */
    public void setOldCategoryCode(String oldCategoryCode) {
        this.oldCategoryCode = oldCategoryCode;
    }

    /**
     * 获取工业分类名称(旧)
     *
     * @return oldCategoryName - 工业分类名称(旧)
     */
    public String getOldCategoryName() {
        return oldCategoryName;
    }

    /**
     * 设置工业分类名称(旧)
     *
     * @param oldCategoryName 工业分类名称(旧)
     */
    public void setOldCategoryName(String oldCategoryName) {
        this.oldCategoryName = oldCategoryName;
    }

    /**
     * 获取商品名称(新)
     *
     * @return newGoodsName - 商品名称(新)
     */
    public String getNewGoodsName() {
        return newGoodsName;
    }

    /**
     * 设置商品名称(新)
     *
     * @param newGoodsName 商品名称(新)
     */
    public void setNewGoodsName(String newGoodsName) {
        this.newGoodsName = newGoodsName;
    }

    /**
     * 获取商品展示名称(新)
     *
     * @return newGoodsDisplayName - 商品展示名称(新)
     */
    public String getNewGoodsDisplayName() {
        return newGoodsDisplayName;
    }

    /**
     * 设置商品展示名称(新)
     *
     * @param newGoodsDisplayName 商品展示名称(新)
     */
    public void setNewGoodsDisplayName(String newGoodsDisplayName) {
        this.newGoodsDisplayName = newGoodsDisplayName;
    }

    /**
     * 获取商品价格(新)
     *
     * @return newSalePrice - 商品价格(新)
     */
    public BigDecimal getNewSalePrice() {
        return newSalePrice;
    }

    /**
     * 设置商品价格(新)
     *
     * @param newSalePrice 商品价格(新)
     */
    public void setNewSalePrice(BigDecimal newSalePrice) {
        this.newSalePrice = newSalePrice;
    }

    /**
     * 获取管制商品标签(新)
     *
     * @return newGoodsFromCode - 管制商品标签(新)
     */
    public String getNewGoodsFromCode() {
        return newGoodsFromCode;
    }

    /**
     * 设置管制商品标签(新)
     *
     * @param newGoodsFromCode 管制商品标签(新)
     */
    public void setNewGoodsFromCode(String newGoodsFromCode) {
        this.newGoodsFromCode = newGoodsFromCode;
    }

    /**
     * 获取工业分类编码(新)
     *
     * @return newCategoryCode - 工业分类编码(新)
     */
    public String getNewCategoryCode() {
        return newCategoryCode;
    }

    /**
     * 设置工业分类编码(新)
     *
     * @param newCategoryCode 工业分类编码(新)
     */
    public void setNewCategoryCode(String newCategoryCode) {
        this.newCategoryCode = newCategoryCode;
    }

    /**
     * 获取工业分类名称(新)
     *
     * @return newCategoryName - 工业分类名称(新)
     */
    public String getNewCategoryName() {
        return newCategoryName;
    }

    /**
     * 设置工业分类名称(新)
     *
     * @param newCategoryName 工业分类名称(新)
     */
    public void setNewCategoryName(String newCategoryName) {
        this.newCategoryName = newCategoryName;
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
}