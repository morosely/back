package com.efuture.model;

import java.math.BigDecimal;
import java.util.Date;

public class VenderModel extends CommonModel {
	
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "venderCode,erpCode,entId";
	}

	public String getUniqueKeyValue() {
		return "'" + this.venderCode + "','" + this.erpCode + "'," + this.entId;
	}
	
    private Long vid;

    private Long entId;

    private String erpCode;

    private String venderCode;

    private String vendersName;

    private String venderName;

    private String venderEnName;

    private Long parentVenderId;

    private Short venderType;

    private Short contractType;

    private Integer majorId;

    private String sShopId;

    private String venderCardId;

    private String taxPayNum;

    private String identityCard;

    private Short localFlag;

    private Short dayPayFlag;

    private Short primaryFlag;

    private Short payMode;

    private Boolean prepayFlag;

    private String address;

    private String telephone;

    private String fax;

    private String contactMan;

    private String contactPhone;

    private String zipCode;

    private String bankName;

    private String bankAccount;

    private String bankAccountName;

    private String bizScope;

    private String bizLevel;

    private String chairMan;

    private Integer zoneId;

    private Integer taxLevel;

    private Integer orderMode;

    private Integer venderStatus;

    private String netAddress;

    private String email;

    private String invoiceAddress;

    private String invoiceHead;

    private BigDecimal registerCap;

    private String entKind;

    private String orderDay;

    private Date inDate;

    private String inDepartment;

    private String inVendee;

    private String endVendee;

    private String endReason;

    private Date endDate;

    private String chnageReason;

    private String inBrand;

    private String shipmentSto;

    private String deliveryType;

    private Integer parentFlag;

    private Integer mallFlag;

    private Integer purType;

    private Integer originType;

    private Integer payLevel;

    private Integer isScm;

    private String str1;

    private String str3;

    private String str4;

    private String str5;

    private Boolean isTrust;

    private Boolean isSpec;

    private Boolean scmCtl;

    private Boolean isSalt;

    private Float trustValueRate;

    private Float payStkValueRate;

    private Boolean isTczs;

    private String venderGrade;

    private String payGrade;

    private BigDecimal n1;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
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

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode == null ? null : venderCode.trim();
    }

    public String getVendersName() {
        return vendersName;
    }

    public void setVendersName(String vendersName) {
        this.vendersName = vendersName == null ? null : vendersName.trim();
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName == null ? null : venderName.trim();
    }

    public String getVenderEnName() {
        return venderEnName;
    }

    public void setVenderEnName(String venderEnName) {
        this.venderEnName = venderEnName == null ? null : venderEnName.trim();
    }

    public Long getParentVenderId() {
        return parentVenderId;
    }

    public void setParentVenderId(Long parentVenderId) {
        this.parentVenderId = parentVenderId;
    }

    public Short getVenderType() {
        return venderType;
    }

    public void setVenderType(Short venderType) {
        this.venderType = venderType;
    }

    public Short getContractType() {
        return contractType;
    }

    public void setContractType(Short contractType) {
        this.contractType = contractType;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getsShopId() {
        return sShopId;
    }

    public void setsShopId(String sShopId) {
        this.sShopId = sShopId == null ? null : sShopId.trim();
    }

    public String getVenderCardId() {
        return venderCardId;
    }

    public void setVenderCardId(String venderCardId) {
        this.venderCardId = venderCardId == null ? null : venderCardId.trim();
    }

    public String getTaxPayNum() {
        return taxPayNum;
    }

    public void setTaxPayNum(String taxPayNum) {
        this.taxPayNum = taxPayNum == null ? null : taxPayNum.trim();
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard == null ? null : identityCard.trim();
    }

    public Short getLocalFlag() {
        return localFlag;
    }

    public void setLocalFlag(Short localFlag) {
        this.localFlag = localFlag;
    }

    public Short getDayPayFlag() {
        return dayPayFlag;
    }

    public void setDayPayFlag(Short dayPayFlag) {
        this.dayPayFlag = dayPayFlag;
    }

    public Short getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(Short primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public Short getPayMode() {
        return payMode;
    }

    public void setPayMode(Short payMode) {
        this.payMode = payMode;
    }

    public Boolean getPrepayFlag() {
        return prepayFlag;
    }

    public void setPrepayFlag(Boolean prepayFlag) {
        this.prepayFlag = prepayFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getContactMan() {
        return contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan == null ? null : contactMan.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    public String getBizScope() {
        return bizScope;
    }

    public void setBizScope(String bizScope) {
        this.bizScope = bizScope == null ? null : bizScope.trim();
    }

    public String getBizLevel() {
        return bizLevel;
    }

    public void setBizLevel(String bizLevel) {
        this.bizLevel = bizLevel == null ? null : bizLevel.trim();
    }

    public String getChairMan() {
        return chairMan;
    }

    public void setChairMan(String chairMan) {
        this.chairMan = chairMan == null ? null : chairMan.trim();
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getTaxLevel() {
        return taxLevel;
    }

    public void setTaxLevel(Integer taxLevel) {
        this.taxLevel = taxLevel;
    }

    public Integer getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(Integer orderMode) {
        this.orderMode = orderMode;
    }

    public Integer getVenderStatus() {
        return venderStatus;
    }

    public void setVenderStatus(Integer venderStatus) {
        this.venderStatus = venderStatus;
    }

    public String getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress == null ? null : netAddress.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress == null ? null : invoiceAddress.trim();
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead == null ? null : invoiceHead.trim();
    }

    public BigDecimal getRegisterCap() {
        return registerCap;
    }

    public void setRegisterCap(BigDecimal registerCap) {
        this.registerCap = registerCap;
    }

    public String getEntKind() {
        return entKind;
    }

    public void setEntKind(String entKind) {
        this.entKind = entKind == null ? null : entKind.trim();
    }

    public String getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(String orderDay) {
        this.orderDay = orderDay == null ? null : orderDay.trim();
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getInDepartment() {
        return inDepartment;
    }

    public void setInDepartment(String inDepartment) {
        this.inDepartment = inDepartment == null ? null : inDepartment.trim();
    }

    public String getInVendee() {
        return inVendee;
    }

    public void setInVendee(String inVendee) {
        this.inVendee = inVendee == null ? null : inVendee.trim();
    }

    public String getEndVendee() {
        return endVendee;
    }

    public void setEndVendee(String endVendee) {
        this.endVendee = endVendee == null ? null : endVendee.trim();
    }

    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason == null ? null : endReason.trim();
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getChnageReason() {
        return chnageReason;
    }

    public void setChnageReason(String chnageReason) {
        this.chnageReason = chnageReason == null ? null : chnageReason.trim();
    }

    public String getInBrand() {
        return inBrand;
    }

    public void setInBrand(String inBrand) {
        this.inBrand = inBrand == null ? null : inBrand.trim();
    }

    public String getShipmentSto() {
        return shipmentSto;
    }

    public void setShipmentSto(String shipmentSto) {
        this.shipmentSto = shipmentSto == null ? null : shipmentSto.trim();
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType == null ? null : deliveryType.trim();
    }

    public Integer getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(Integer parentFlag) {
        this.parentFlag = parentFlag;
    }

    public Integer getMallFlag() {
        return mallFlag;
    }

    public void setMallFlag(Integer mallFlag) {
        this.mallFlag = mallFlag;
    }

    public Integer getPurType() {
        return purType;
    }

    public void setPurType(Integer purType) {
        this.purType = purType;
    }

    public Integer getOriginType() {
        return originType;
    }

    public void setOriginType(Integer originType) {
        this.originType = originType;
    }

    public Integer getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(Integer payLevel) {
        this.payLevel = payLevel;
    }

    public Integer getIsScm() {
        return isScm;
    }

    public void setIsScm(Integer isScm) {
        this.isScm = isScm;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1 == null ? null : str1.trim();
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3 == null ? null : str3.trim();
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4 == null ? null : str4.trim();
    }

    public String getStr5() {
        return str5;
    }

    public void setStr5(String str5) {
        this.str5 = str5 == null ? null : str5.trim();
    }

    public Boolean getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(Boolean isTrust) {
        this.isTrust = isTrust;
    }

    public Boolean getIsSpec() {
        return isSpec;
    }

    public void setIsSpec(Boolean isSpec) {
        this.isSpec = isSpec;
    }

    public Boolean getScmCtl() {
        return scmCtl;
    }

    public void setScmCtl(Boolean scmCtl) {
        this.scmCtl = scmCtl;
    }

    public Boolean getIsSalt() {
        return isSalt;
    }

    public void setIsSalt(Boolean isSalt) {
        this.isSalt = isSalt;
    }

    public Float getTrustValueRate() {
        return trustValueRate;
    }

    public void setTrustValueRate(Float trustValueRate) {
        this.trustValueRate = trustValueRate;
    }

    public Float getPayStkValueRate() {
        return payStkValueRate;
    }

    public void setPayStkValueRate(Float payStkValueRate) {
        this.payStkValueRate = payStkValueRate;
    }

    public Boolean getIsTczs() {
        return isTczs;
    }

    public void setIsTczs(Boolean isTczs) {
        this.isTczs = isTczs;
    }

    public String getVenderGrade() {
        return venderGrade;
    }

    public void setVenderGrade(String venderGrade) {
        this.venderGrade = venderGrade == null ? null : venderGrade.trim();
    }

    public String getPayGrade() {
        return payGrade;
    }

    public void setPayGrade(String payGrade) {
        this.payGrade = payGrade == null ? null : payGrade.trim();
    }

    public BigDecimal getN1() {
        return n1;
    }

    public void setN1(BigDecimal n1) {
        this.n1 = n1;
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