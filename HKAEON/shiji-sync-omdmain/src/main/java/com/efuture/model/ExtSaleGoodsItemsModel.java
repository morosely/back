package com.efuture.model;

import com.efuture.common.CommonExtSyncService;

import java.math.BigDecimal;
import java.util.Date;

public class ExtSaleGoodsItemsModel extends CommonExtModel {
    private Long sgiid;

    private Long entId;

    private String erpCode;

    private Integer goodsType;

    private Long gsgid;

    private String ggoodsCode;

    private Long ssgid;

    private String goodsCode;

    private String barNo;

    private Integer num;

    private Float discountShareRate;

    private BigDecimal salePrice;

    private Integer sortFlag;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Integer dealStatus;
    
    private Short status;

    public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Override
    public String getUniqueKey() {
        return "ggoodsCode,erpCode,entId";
    }

    @Override
    public String getUniqueKeyValue() {
        return "'" + this.ggoodsCode + "'," + "'" + this.erpCode + "'," + this.entId;
    }

    public Long getSgiid() {
        return sgiid;
    }

    public void setSgiid(Long sgiid) {
        this.sgiid = sgiid;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Long getGsgid() {
        return gsgid;
    }

    public void setGsgid(Long gsgid) {
        this.gsgid = gsgid;
    }

    public String getGgoodsCode() {
        return ggoodsCode;
    }

    public void setGgoodsCode(String ggoodsCode) {
        this.ggoodsCode = ggoodsCode == null ? null : ggoodsCode.trim();
    }

    public Long getSsgid() {
        return ssgid;
    }

    public void setSsgid(Long ssgid) {
        this.ssgid = ssgid;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getBarNo() {
        return barNo;
    }

    public void setBarNo(String barNo) {
        this.barNo = barNo == null ? null : barNo.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Float getDiscountShareRate() {
        return discountShareRate;
    }

    public void setDiscountShareRate(Float discountShareRate) {
        this.discountShareRate = discountShareRate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(Integer sortFlag) {
        this.sortFlag = sortFlag;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }
}