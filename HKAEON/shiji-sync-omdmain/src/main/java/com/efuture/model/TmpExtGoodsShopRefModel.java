package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class TmpExtGoodsShopRefModel extends CommonExtModel{
	
	private static final long serialVersionUID = 1L;
	
	public String getUniqueKey() {
		return "goodsCode,shopCode,entId,erpCode";
	}
    
    public String getUniqueKeyValue() {
//		return "'"+this.goodsCode+"','"+this.shopCode+"','"+this.erpCode+"',"+this.entId;
		return "'"+this.goodsCode+"','"+this.shopCode+"',"+this.entId+",'"+this.erpCode+"'";
	}

    private Long gsrid;

	public Long getGsrid() {
		return gsrid;
	}

	public void setGsrid(Long gsrid) {
		this.gsrid = gsrid;
	}

	private Long egsrid;

    private Long entId;

    private String erpCode;

    private Long shopId;

    private String shopCode;

    private Long saleOrgId;

    private String orgCode;

    private Long siid;

    private String stallCode;

    private Long sgid;

    private String goodsCode;

    private String venderCode;

    private Integer goodStatus;

    private BigDecimal cost;

    private BigDecimal contractCost;

    private Float costTaxRate;

    private BigDecimal deductRate;

    private BigDecimal salePrice;

    private BigDecimal customPrice;

    private BigDecimal bulkPrice;

    private Integer pcs;

    private Float partsNum;

    private Float stepDiff;

    private Integer safeStockDays;

    private Integer minStockDays;

    private Integer maxStockDays;

    private Short priceRight;

    private Short continuePurFlag;

    private Short importantFlag;

    private Short returnFlag;

    private Short operateFlag;

    private Short orderFlag;

    private Short logistics;

    private String dcshopId;

    private String fdcShopId;

    private Date inDate;

    private String inOper;

    private Date offDate;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Long vid;

    private String prcutMode;

    private Integer dealStatus;

    private String goodsName;

    public Long getEgsrid() {
        return egsrid;
    }

    public void setEgsrid(Long egsrid) {
        this.egsrid = egsrid;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public Long getSaleOrgId() {
        return saleOrgId;
    }

    public void setSaleOrgId(Long saleOrgId) {
        this.saleOrgId = saleOrgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Long getSiid() {
        return siid;
    }

    public void setSiid(Long siid) {
        this.siid = siid;
    }

    public String getStallCode() {
        return stallCode;
    }

    public void setStallCode(String stallCode) {
        this.stallCode = stallCode == null ? null : stallCode.trim();
    }

    public Long getSgid() {
        return sgid;
    }

    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode == null ? null : venderCode.trim();
    }

    public Integer getGoodStatus() {
        return goodStatus;
    }

    public void setGoodStatus(Integer goodStatus) {
        this.goodStatus = goodStatus;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getContractCost() {
        return contractCost;
    }

    public void setContractCost(BigDecimal contractCost) {
        this.contractCost = contractCost;
    }

    public Float getCostTaxRate() {
        return costTaxRate;
    }

    public void setCostTaxRate(Float costTaxRate) {
        this.costTaxRate = costTaxRate;
    }

    public BigDecimal getDeductRate() {
        return deductRate;
    }

    public void setDeductRate(BigDecimal deductRate) {
        this.deductRate = deductRate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    public BigDecimal getBulkPrice() {
        return bulkPrice;
    }

    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
    }

    public Integer getPcs() {
        return pcs;
    }

    public void setPcs(Integer pcs) {
        this.pcs = pcs;
    }

    public Float getPartsNum() {
        return partsNum;
    }

    public void setPartsNum(Float partsNum) {
        this.partsNum = partsNum;
    }

    public Float getStepDiff() {
        return stepDiff;
    }

    public void setStepDiff(Float stepDiff) {
        this.stepDiff = stepDiff;
    }

    public Integer getSafeStockDays() {
        return safeStockDays;
    }

    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
    }

    public Integer getMinStockDays() {
        return minStockDays;
    }

    public void setMinStockDays(Integer minStockDays) {
        this.minStockDays = minStockDays;
    }

    public Integer getMaxStockDays() {
        return maxStockDays;
    }

    public void setMaxStockDays(Integer maxStockDays) {
        this.maxStockDays = maxStockDays;
    }

    public Short getPriceRight() {
        return priceRight;
    }

    public void setPriceRight(Short priceRight) {
        this.priceRight = priceRight;
    }

    public Short getContinuePurFlag() {
        return continuePurFlag;
    }

    public void setContinuePurFlag(Short continuePurFlag) {
        this.continuePurFlag = continuePurFlag;
    }

    public Short getImportantFlag() {
        return importantFlag;
    }

    public void setImportantFlag(Short importantFlag) {
        this.importantFlag = importantFlag;
    }

    public Short getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(Short returnFlag) {
        this.returnFlag = returnFlag;
    }

    public Short getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(Short operateFlag) {
        this.operateFlag = operateFlag;
    }

    public Short getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(Short orderFlag) {
        this.orderFlag = orderFlag;
    }

    public Short getLogistics() {
        return logistics;
    }

    public void setLogistics(Short logistics) {
        this.logistics = logistics;
    }

    public String getDcshopId() {
        return dcshopId;
    }

    public void setDcshopId(String dcshopId) {
        this.dcshopId = dcshopId == null ? null : dcshopId.trim();
    }

    public String getFdcShopId() {
        return fdcShopId;
    }

    public void setFdcShopId(String fdcShopId) {
        this.fdcShopId = fdcShopId == null ? null : fdcShopId.trim();
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getInOper() {
        return inOper;
    }

    public void setInOper(String inOper) {
        this.inOper = inOper == null ? null : inOper.trim();
    }

    public Date getOffDate() {
        return offDate;
    }

    public void setOffDate(Date offDate) {
        this.offDate = offDate;
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

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public String getPrcutMode() {
        return prcutMode;
    }

    public void setPrcutMode(String prcutMode) {
        this.prcutMode = prcutMode == null ? null : prcutMode.trim();
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }
}