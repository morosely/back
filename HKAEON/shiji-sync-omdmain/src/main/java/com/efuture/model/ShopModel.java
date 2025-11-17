package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class ShopModel extends CommonModel{
	
	private static final long serialVersionUID = 1L;
	
	public String getUniqueKey() {
		return "shopCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.shopCode+"','"+this.erpCode+"',"+this.entId;
	}
    
    //父节点编码
    public String getParentCodeKeyValue() {
		return "'"+this.parentCode+"','"+this.erpCode+"',"+this.entId;
	}

	private String address;
    
    private String bookNo;
	
	private String brand;
	
    private Double buildAreage;

	private Double busiAreage;
    
    private String commerceCircle;

    private String companyName;

    private Date createDate;

    private String creator;

    private String deliveryCode;

    private String deliveryCycle;

    private String deliveryName;

    private Long entId;

    private String erpCode;

    private String groupCode;

    private String lang;

    private String latitude;

    private Boolean leafFlag;

    private String league;

    private Short level;

    private String longitude;

    private String modifier;

    private Date openningTime;

    private String orderCycle;

    private String owneShopIdx;

    private String parentCode;

    private Long parentId;

    private String priceGroup;

    private String purchaseTypex;

    private Date recordDate;

    private String regionCode;

    private Long regionId;

    private BigDecimal rent;

    private Integer safeStockDays;

    private Integer serviceRadiu;

    private String shopCode;

    private String shopEnName;

    private String shopForm;

    private Long shopId;

    private String shopLevel;

    private String shopName;

    private String shopSize;

    private String shopSName;

    private String shopTypex;

    private String shopZonex;

    private Short status;

    private String subject;

    private String telephone;

    private Date updateDate;

    private String comCode;

    public String getComCode() { return comCode;}

    public void setComCode(String comCode) { this.comCode = comCode;}

    public String getAddress() {
        return address;
    }

    public String getBookNo() {
        return bookNo;
    }

    public String getBrand() {
        return brand;
    }

    public Double getBuildAreage() {
        return buildAreage;
    }

    public Double getBusiAreage() {
        return busiAreage;
    }

    public String getCommerceCircle() {
        return commerceCircle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreator() {
        return creator;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public Long getEntId() {
        return entId;
    }

    public String getErpCode() {
        return erpCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getLang() {
        return lang;
    }

    public String getLatitude() {
        return latitude;
    }

    public Boolean getLeafFlag() {
        return leafFlag;
    }

    public String getLeague() {
        return league;
    }

    public Short getLevel() {
        return level;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getModifier() {
        return modifier;
    }

    public Date getOpenningTime() {
        return openningTime;
    }

    public String getOrderCycle() {
        return orderCycle;
    }

    public String getOwneShopIdx() {
        return owneShopIdx;
    }

    public String getParentCode() {
        return parentCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getPriceGroup() {
        return priceGroup;
    }

    public String getPurchaseTypex() {
        return purchaseTypex;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public Long getRegionId() {
        return regionId;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public Integer getSafeStockDays() {
        return safeStockDays;
    }

    public Integer getServiceRadiu() {
        return serviceRadiu;
    }

    public String getShopCode() {
        return shopCode;
    }

    public String getShopEnName() {
		return shopEnName;
	}

    public String getShopForm() {
        return shopForm;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getShopLevel() {
        return shopLevel;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopSize() {
        return shopSize;
    }

    public String getShopSName() {
        return shopSName;
    }

    public String getShopTypex() {
        return shopTypex;
    }

    public String getShopZonex() {
        return shopZonex;
    }

    public Short getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
    }

    public String getTelephone() {
        return telephone;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo == null ? null : bookNo.trim();
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public void setBuildAreage(Double buildAreage) {
        this.buildAreage = buildAreage;
    }

    public void setBusiAreage(Double busiAreage) {
        this.busiAreage = busiAreage;
    }

    public void setCommerceCircle(String commerceCircle) {
        this.commerceCircle = commerceCircle == null ? null : commerceCircle.trim();
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode == null ? null : deliveryCode.trim();
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle == null ? null : deliveryCycle.trim();
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName == null ? null : deliveryName.trim();
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
    }

    public void setLeague(String league) {
        this.league = league == null ? null : league.trim();
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public void setOpenningTime(Date openningTime) {
        this.openningTime = openningTime;
    }

    public void setOrderCycle(String orderCycle) {
        this.orderCycle = orderCycle == null ? null : orderCycle.trim();
    }

    public void setOwneShopIdx(String owneShopIdx) {
        this.owneShopIdx = owneShopIdx == null ? null : owneShopIdx.trim();
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setPriceGroup(String priceGroup) {
        this.priceGroup = priceGroup == null ? null : priceGroup.trim();
    }

    public void setPurchaseTypex(String purchaseTypex) {
        this.purchaseTypex = purchaseTypex == null ? null : purchaseTypex.trim();
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
    }

    public void setServiceRadiu(Integer serviceRadiu) {
        this.serviceRadiu = serviceRadiu;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public void setShopEnName(String shopEnName) {
		this.shopEnName = shopEnName;
	}

    public void setShopForm(String shopForm) {
        this.shopForm = shopForm == null ? null : shopForm.trim();
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public void setShopLevel(String shopLevel) {
        this.shopLevel = shopLevel == null ? null : shopLevel.trim();
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public void setShopSize(String shopSize) {
        this.shopSize = shopSize == null ? null : shopSize.trim();
    }

    public void setShopSName(String shopSName) {
        this.shopSName = shopSName == null ? null : shopSName.trim();
    }

    public void setShopTypex(String shopTypex) {
        this.shopTypex = shopTypex == null ? null : shopTypex.trim();
    }

    public void setShopZonex(String shopZonex) {
        this.shopZonex = shopZonex == null ? null : shopZonex.trim();
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}