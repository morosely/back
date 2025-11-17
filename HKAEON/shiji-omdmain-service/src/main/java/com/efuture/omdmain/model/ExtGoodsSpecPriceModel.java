package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "extgoodsspecprice")
public class ExtGoodsSpecPriceModel {
    /**
     * 商品多单位价格ID
     */
    private Long egspid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 编码类型  1-条码/2-价格码（PLU）
     */
    private Short codeType;

    /**
     * 条码
     */
    private String barNo;

    /**
     * 包装含量
     */
    private Double partsNum;

    /**
     * 包装单位
     */
    private String partsUnit;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

    /**
     * 会员价
     */
    private BigDecimal customPrice;

    /**
     * 批发价
     */
    private BigDecimal bulkPrice;

    /**
     * 分拣等级
     */
    private String sortLevel;

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
     * 获取商品多单位价格ID
     *
     * @return egspid - 商品多单位价格ID
     */
    public Long getEgspid() {
        return egspid;
    }

    /**
     * 设置商品多单位价格ID
     *
     * @param egspid 商品多单位价格ID
     */
    public void setEgspid(Long egspid) {
        this.egspid = egspid;
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
     * 获取门店ID
     *
     * @return shopId - 门店ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置门店ID
     *
     * @param shopId 门店ID
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
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
     * 获取编码类型  1-条码/2-价格码（PLU）
     *
     * @return codeType - 编码类型  1-条码/2-价格码（PLU）
     */
    public Short getCodeType() {
        return codeType;
    }

    /**
     * 设置编码类型  1-条码/2-价格码（PLU）
     *
     * @param codeType 编码类型  1-条码/2-价格码（PLU）
     */
    public void setCodeType(Short codeType) {
        this.codeType = codeType;
    }

    /**
     * 获取条码
     *
     * @return barNo - 条码
     */
    public String getBarNo() {
        return barNo;
    }

    /**
     * 设置条码
     *
     * @param barNo 条码
     */
    public void setBarNo(String barNo) {
        this.barNo = barNo;
    }

    /**
     * 获取包装含量
     *
     * @return partsNum - 包装含量
     */
    public Double getPartsNum() {
        return partsNum;
    }

    /**
     * 设置包装含量
     *
     * @param partsNum 包装含量
     */
    public void setPartsNum(Double partsNum) {
        this.partsNum = partsNum;
    }

    /**
     * 获取包装单位
     *
     * @return partsUnit - 包装单位
     */
    public String getPartsUnit() {
        return partsUnit;
    }

    /**
     * 设置包装单位
     *
     * @param partsUnit 包装单位
     */
    public void setPartsUnit(String partsUnit) {
        this.partsUnit = partsUnit;
    }

    /**
     * 获取零售价
     *
     * @return salePrice - 零售价
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * 设置零售价
     *
     * @param salePrice 零售价
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * 获取会员价
     *
     * @return customPrice - 会员价
     */
    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    /**
     * 设置会员价
     *
     * @param customPrice 会员价
     */
    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    /**
     * 获取批发价
     *
     * @return bulkPrice - 批发价
     */
    public BigDecimal getBulkPrice() {
        return bulkPrice;
    }

    /**
     * 设置批发价
     *
     * @param bulkPrice 批发价
     */
    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
    }

    /**
     * 获取分拣等级
     *
     * @return sortLevel - 分拣等级
     */
    public String getSortLevel() {
        return sortLevel;
    }

    /**
     * 设置分拣等级
     *
     * @param sortLevel 分拣等级
     */
    public void setSortLevel(String sortLevel) {
        this.sortLevel = sortLevel;
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