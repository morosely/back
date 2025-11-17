package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class ExtShopModel extends CommonExtModel{
	
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "shopCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.shopCode+"','"+this.erpCode+"',"+this.entId;
	}

	private String shopEnName;
	
    public String getShopEnName() {
		return shopEnName;
	}

	public void setShopEnName(String shopEnName) {
		this.shopEnName = shopEnName;
	}

	private Long eshopId;

    private Long entId;

    private String erpCode;

    private String shopCode;

    private String shopName;

    private String shopSName;

    private Long parentId;

    private String parentCode;

    private String shopForm;

    private String shopTypex;

    private String groupCode;

    private String purchaseTypex;

    private String owneShopIdx;

    private String shopSize;

    private String shopLevel;

    private String commerceCircle;

    private String league;

    private String brand;

    private String priceGroup;

    private String shopZonex;

    private Date openningTime;

    private String bookNo;

    private String subject;

    private String companyName;

    private String deliveryCode;

    private String deliveryName;

    private String telephone;

    private Double busiAreage;

    private Double buildAreage;

    private BigDecimal rent;

    private String deliveryCycle;

    private Integer safeStockDays;

    private String orderCycle;

    private Date recordDate;

    private Long regionId;

    private String regionCode;

    private String longitude;

    private String latitude;

    private Integer serviceRadiu;

    private String address;

    private Short level;

    private Boolean leafFlag;

    private Short status;

    private String sourceFromCode;

    private Integer dealStatus;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private String comCode;

    public String getComCode() { return comCode;}

    public void setComCode(String comCode) { this.comCode = comCode;}

    public Long getEshopId() {
        return eshopId;
    }

    public void setEshopId(Long eshopId) {
        this.eshopId = eshopId;
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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopSName() {
        return shopSName;
    }

    public void setShopSName(String shopSName) {
        this.shopSName = shopSName == null ? null : shopSName.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }

    public String getShopForm() {
        return shopForm;
    }

    public void setShopForm(String shopForm) {
        this.shopForm = shopForm == null ? null : shopForm.trim();
    }

    public String getShopTypex() {
        return shopTypex;
    }

    public void setShopTypex(String shopTypex) {
        this.shopTypex = shopTypex == null ? null : shopTypex.trim();
    }

    public String getPurchaseTypex() {
        return purchaseTypex;
    }

    public void setPurchaseTypex(String purchaseTypex) {
        this.purchaseTypex = purchaseTypex == null ? null : purchaseTypex.trim();
    }

    public String getOwneShopIdx() {
        return owneShopIdx;
    }

    public void setOwneShopIdx(String owneShopIdx) {
        this.owneShopIdx = owneShopIdx == null ? null : owneShopIdx.trim();
    }

    public String getShopSize() {
        return shopSize;
    }

    public void setShopSize(String shopSize) {
        this.shopSize = shopSize == null ? null : shopSize.trim();
    }

    public String getShopLevel() {
        return shopLevel;
    }

    public void setShopLevel(String shopLevel) {
        this.shopLevel = shopLevel == null ? null : shopLevel.trim();
    }

    public String getCommerceCircle() {
        return commerceCircle;
    }

    public void setCommerceCircle(String commerceCircle) {
        this.commerceCircle = commerceCircle == null ? null : commerceCircle.trim();
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league == null ? null : league.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getPriceGroup() {
        return priceGroup;
    }

    public void setPriceGroup(String priceGroup) {
        this.priceGroup = priceGroup == null ? null : priceGroup.trim();
    }

    public String getShopZonex() {
        return shopZonex;
    }

    public void setShopZonex(String shopZonex) {
        this.shopZonex = shopZonex == null ? null : shopZonex.trim();
    }

    public Date getOpenningTime() {
        return openningTime;
    }

    public void setOpenningTime(Date openningTime) {
        this.openningTime = openningTime;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo == null ? null : bookNo.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode == null ? null : deliveryCode.trim();
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName == null ? null : deliveryName.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public Double getBusiAreage() {
        return busiAreage;
    }

    public void setBusiAreage(Double busiAreage) {
        this.busiAreage = busiAreage;
    }

    public Double getBuildAreage() {
        return buildAreage;
    }

    public void setBuildAreage(Double buildAreage) {
        this.buildAreage = buildAreage;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle == null ? null : deliveryCycle.trim();
    }

    public Integer getSafeStockDays() {
        return safeStockDays;
    }

    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
    }

    public String getOrderCycle() {
        return orderCycle;
    }

    public void setOrderCycle(String orderCycle) {
        this.orderCycle = orderCycle == null ? null : orderCycle.trim();
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public Integer getServiceRadiu() {
        return serviceRadiu;
    }

    public void setServiceRadiu(Integer serviceRadiu) {
        this.serviceRadiu = serviceRadiu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public Boolean getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getSourceFromCode() {
        return sourceFromCode;
    }

    public void setSourceFromCode(String sourceFromCode) {
        this.sourceFromCode = sourceFromCode == null ? null : sourceFromCode.trim();
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
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
}