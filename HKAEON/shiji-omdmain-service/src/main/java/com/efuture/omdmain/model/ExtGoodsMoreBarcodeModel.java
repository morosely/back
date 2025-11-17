package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "extgoodsmorebarcode")
public class ExtGoodsMoreBarcodeModel {
    /**
     * 多条码表ID
     */
    @Id
    private Long egsid;

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
     * 分拣等级
     */
    private String sortLevel;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

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
     * 价格来源于单品(1:是,0:否)
     */
    private Boolean priceFromGoods;

    /**
     * 是否主条码1：是，0，否
     */
    private Short barCodeType;

    /**
     * 获取多条码表ID
     *
     * @return egsid - 多条码表ID
     */
    public Long getEgsid() {
        return egsid;
    }

    /**
     * 设置多条码表ID
     *
     * @param egsid 多条码表ID
     */
    public void setEgsid(Long egsid) {
        this.egsid = egsid;
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

    /**
     * 获取价格来源于单品(1:是,0:否)
     *
     * @return priceFromGoods - 价格来源于单品(1:是,0:否)
     */
    public Boolean getPriceFromGoods() {
        return priceFromGoods;
    }

    /**
     * 设置价格来源于单品(1:是,0:否)
     *
     * @param priceFromGoods 价格来源于单品(1:是,0:否)
     */
    public void setPriceFromGoods(Boolean priceFromGoods) {
        this.priceFromGoods = priceFromGoods;
    }

    /**
     * 获取是否主条码1：是，0，否
     *
     * @return barCodeType - 是否主条码1：是，0，否
     */
    public Short getBarCodeType() {
        return barCodeType;
    }

    /**
     * 设置是否主条码1：是，0，否
     *
     * @param barCodeType 是否主条码1：是，0，否
     */
    public void setBarCodeType(Short barCodeType) {
        this.barCodeType = barCodeType;
    }
}