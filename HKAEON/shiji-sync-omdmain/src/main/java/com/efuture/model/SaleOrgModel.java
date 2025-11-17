package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class SaleOrgModel extends CommonModel{
	
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "orgCode,erpCode,entId";
	}

	public String getUniqueKeyValue() {
		return "'" + this.orgCode + "','" + this.erpCode + "'," + this.entId;
	}
	
    private Long saleOrgId;

    private Long entId;

    private String erpCode;

    private Long shopId;

    private String orgCode;

    private String orgName;

    private String orgEnName;

    private String orgSname;

    private Long parentId;

    private String parentCode;

    private String shopForm;

    private String shopTypex;

    private String purchaseTypex;

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

    private String industry;

    private String accountCode;

    private String manageArea;

    private Boolean onlineFlag;

    private Integer wareType;

    private Integer chainType;

    private Boolean upDeliveryFlag;

    private Double raiseRate;

    private Integer defaultSettlebasi;

    private Boolean zssFlag;

    private Boolean multiVendorFlag;

    private String kindPrincipal;

    private String fax;

    private String email;

    private BigDecimal planGrossProfit;

    private BigDecimal flatRate;

    private BigDecimal changeRate;

    private Double realBusinessArea;

    private String postGroup;

    private String businessCompany;

    private Boolean dstoreManageFlag;

    private String categoryCode;

    private Double containShareArea;

    private Float shareRate;

    private BigDecimal pmkcMin;

    private BigDecimal pmkcMax;

    private Integer purchaseCycle;

    private String assistCode;

    private String orientation;

    private String sharp;

    private String length;

    private String rentHardLevel;

    private String advType;

    private String advMedia;

    private String storeType;

    private Double rentArea;

    private String repossessionFlag;

    private BigDecimal unitRent;

    private BigDecimal totalRent;

    private String lesseeCoor;

    private String storeAssistInfo;

    private String contractNo;

    private Short source;

    private String build;

    private String floor;

    private String busiType;

    private String busiPlace;

    private String area;

    private Short level;

    private Boolean leafFlag;

    private Short status;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    public Long getSaleOrgId() {
        return saleOrgId;
    }

    public void setSaleOrgId(Long saleOrgId) {
        this.saleOrgId = saleOrgId;
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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getOrgEnName() {
        return orgEnName;
    }

    public void setOrgEnName(String orgEnName) {
        this.orgEnName = orgEnName == null ? null : orgEnName.trim();
    }

    public String getOrgSname() {
        return orgSname;
    }

    public void setOrgSname(String orgSname) {
        this.orgSname = orgSname == null ? null : orgSname.trim();
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    public String getManageArea() {
        return manageArea;
    }

    public void setManageArea(String manageArea) {
        this.manageArea = manageArea == null ? null : manageArea.trim();
    }

    public Boolean getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Boolean onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public Integer getWareType() {
        return wareType;
    }

    public void setWareType(Integer wareType) {
        this.wareType = wareType;
    }

    public Integer getChainType() {
        return chainType;
    }

    public void setChainType(Integer chainType) {
        this.chainType = chainType;
    }

    public Boolean getUpDeliveryFlag() {
        return upDeliveryFlag;
    }

    public void setUpDeliveryFlag(Boolean upDeliveryFlag) {
        this.upDeliveryFlag = upDeliveryFlag;
    }

    public Double getRaiseRate() {
        return raiseRate;
    }

    public void setRaiseRate(Double raiseRate) {
        this.raiseRate = raiseRate;
    }

    public Integer getDefaultSettlebasi() {
        return defaultSettlebasi;
    }

    public void setDefaultSettlebasi(Integer defaultSettlebasi) {
        this.defaultSettlebasi = defaultSettlebasi;
    }

    public Boolean getZssFlag() {
        return zssFlag;
    }

    public void setZssFlag(Boolean zssFlag) {
        this.zssFlag = zssFlag;
    }

    public Boolean getMultiVendorFlag() {
        return multiVendorFlag;
    }

    public void setMultiVendorFlag(Boolean multiVendorFlag) {
        this.multiVendorFlag = multiVendorFlag;
    }

    public String getKindPrincipal() {
        return kindPrincipal;
    }

    public void setKindPrincipal(String kindPrincipal) {
        this.kindPrincipal = kindPrincipal == null ? null : kindPrincipal.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public BigDecimal getPlanGrossProfit() {
        return planGrossProfit;
    }

    public void setPlanGrossProfit(BigDecimal planGrossProfit) {
        this.planGrossProfit = planGrossProfit;
    }

    public BigDecimal getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(BigDecimal flatRate) {
        this.flatRate = flatRate;
    }

    public BigDecimal getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(BigDecimal changeRate) {
        this.changeRate = changeRate;
    }

    public Double getRealBusinessArea() {
        return realBusinessArea;
    }

    public void setRealBusinessArea(Double realBusinessArea) {
        this.realBusinessArea = realBusinessArea;
    }

    public String getPostGroup() {
        return postGroup;
    }

    public void setPostGroup(String postGroup) {
        this.postGroup = postGroup == null ? null : postGroup.trim();
    }

    public String getBusinessCompany() {
        return businessCompany;
    }

    public void setBusinessCompany(String businessCompany) {
        this.businessCompany = businessCompany == null ? null : businessCompany.trim();
    }

    public Boolean getDstoreManageFlag() {
        return dstoreManageFlag;
    }

    public void setDstoreManageFlag(Boolean dstoreManageFlag) {
        this.dstoreManageFlag = dstoreManageFlag;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public Double getContainShareArea() {
        return containShareArea;
    }

    public void setContainShareArea(Double containShareArea) {
        this.containShareArea = containShareArea;
    }

    public Float getShareRate() {
        return shareRate;
    }

    public void setShareRate(Float shareRate) {
        this.shareRate = shareRate;
    }

    public BigDecimal getPmkcMin() {
        return pmkcMin;
    }

    public void setPmkcMin(BigDecimal pmkcMin) {
        this.pmkcMin = pmkcMin;
    }

    public BigDecimal getPmkcMax() {
        return pmkcMax;
    }

    public void setPmkcMax(BigDecimal pmkcMax) {
        this.pmkcMax = pmkcMax;
    }

    public Integer getPurchaseCycle() {
        return purchaseCycle;
    }

    public void setPurchaseCycle(Integer purchaseCycle) {
        this.purchaseCycle = purchaseCycle;
    }

    public String getAssistCode() {
        return assistCode;
    }

    public void setAssistCode(String assistCode) {
        this.assistCode = assistCode == null ? null : assistCode.trim();
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation == null ? null : orientation.trim();
    }

    public String getSharp() {
        return sharp;
    }

    public void setSharp(String sharp) {
        this.sharp = sharp == null ? null : sharp.trim();
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length == null ? null : length.trim();
    }

    public String getRentHardLevel() {
        return rentHardLevel;
    }

    public void setRentHardLevel(String rentHardLevel) {
        this.rentHardLevel = rentHardLevel == null ? null : rentHardLevel.trim();
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType == null ? null : advType.trim();
    }

    public String getAdvMedia() {
        return advMedia;
    }

    public void setAdvMedia(String advMedia) {
        this.advMedia = advMedia == null ? null : advMedia.trim();
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType == null ? null : storeType.trim();
    }

    public Double getRentArea() {
        return rentArea;
    }

    public void setRentArea(Double rentArea) {
        this.rentArea = rentArea;
    }

    public String getRepossessionFlag() {
        return repossessionFlag;
    }

    public void setRepossessionFlag(String repossessionFlag) {
        this.repossessionFlag = repossessionFlag == null ? null : repossessionFlag.trim();
    }

    public BigDecimal getUnitRent() {
        return unitRent;
    }

    public void setUnitRent(BigDecimal unitRent) {
        this.unitRent = unitRent;
    }

    public BigDecimal getTotalRent() {
        return totalRent;
    }

    public void setTotalRent(BigDecimal totalRent) {
        this.totalRent = totalRent;
    }

    public String getLesseeCoor() {
        return lesseeCoor;
    }

    public void setLesseeCoor(String lesseeCoor) {
        this.lesseeCoor = lesseeCoor == null ? null : lesseeCoor.trim();
    }

    public String getStoreAssistInfo() {
        return storeAssistInfo;
    }

    public void setStoreAssistInfo(String storeAssistInfo) {
        this.storeAssistInfo = storeAssistInfo == null ? null : storeAssistInfo.trim();
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build == null ? null : build.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType == null ? null : busiType.trim();
    }

    public String getBusiPlace() {
        return busiPlace;
    }

    public void setBusiPlace(String busiPlace) {
        this.busiPlace = busiPlace == null ? null : busiPlace.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
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