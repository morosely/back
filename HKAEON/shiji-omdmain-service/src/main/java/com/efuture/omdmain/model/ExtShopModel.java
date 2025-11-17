package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "extshop")
public class ExtShopModel {
	/**
     * 详细地址
     */
    private String address;
    /**
     * 账套编码
     */
    private String bookNo;

	/**
     * 门店品牌
     */
    private String brand;
    /**
     * 建筑面积
     */
    private Double buildAreage;

    /**
     * 营业面积
     */
    private Double busiAreage;

    /**
     * 商圈
     */
    private String commerceCircle;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 0-未处理/1-处理中/2-已处理
     */
    private Integer dealStatus;

    /**
     * 配送中心编码
     */
    private String deliveryCode;

    /**
     * 送货周期
     */
    private String deliveryCycle;

    /**
     * 配送中心名称
     */
    private String deliveryName;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 组织机构ID
     */
    @Id
    private Long eshopId;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 是否叶子结点
     */
    private Boolean leafFlag;

    /**
     * 加盟属性
     */
    private String league;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 精度
     */
    private String longitude;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 开业时间
     */
    private Date openningTime;

    /**
     * 订货周期
     */
    private String orderCycle;

    /**
     * 所属中心店
     */
    private String owneShopIdx;

    /**
     * 上级代码
     */
    private String parentCode;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 物价群组
     */
    private String priceGroup;

    /**
     * 订货模式
     */
    private String purchaseTypex;

    /**
     * 财务入账日期
     */
    private Date recordDate;

    /**
     * 行政区域编码
     */
    private String regionCode;

    /**
     * 行政区域ID
     */
    private Long regionId;

    /**
     * 租金
     */
    private BigDecimal rent;

    /**
     * 安全库存天数
     */
    private Integer safeStockDays;

    /**
     * 服务半径
     */
    private Integer serviceRadiu;

    /**
     * 机构编码
     */
    private String shopCode;

    /**
	 * 门店英文名称
	 */
	private String shopEnName;

    /**
     * 业态
     */
    private String shopForm;

    /**
     * 门店等级
     */
    private String shopLevel;

    /**
     * 机构名称
     */
    private String shopName;

    /**
     * 门店规模
     */
    private String shopSize;

    /**
     * 机构简称
     */
    private String shopSName;

    /**
     * 门店类型
     */
    private String shopTypex;

    /**
     * 营运区域
     */
    private String shopZonex;

    /**
     * 数据来源编码
     */
    private String sourceFromCode;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 核算码
     */
    private String subject;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取详细地址
     *
     * @return address - 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 获取账套编码
     *
     * @return bookNo - 账套编码
     */
    public String getBookNo() {
        return bookNo;
    }

    /**
     * 获取门店品牌
     *
     * @return brand - 门店品牌
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 获取建筑面积
     *
     * @return buildAreage - 建筑面积
     */
    public Double getBuildAreage() {
        return buildAreage;
    }

    /**
     * 获取营业面积
     *
     * @return busiAreage - 营业面积
     */
    public Double getBusiAreage() {
        return busiAreage;
    }

    /**
     * 获取商圈
     *
     * @return commerceCircle - 商圈
     */
    public String getCommerceCircle() {
        return commerceCircle;
    }

    /**
     * 获取公司名称
     *
     * @return companyName - 公司名称
     */
    public String getCompanyName() {
        return companyName;
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 获取0-未处理/1-处理中/2-已处理
     *
     * @return dealStatus - 0-未处理/1-处理中/2-已处理
     */
    public Integer getDealStatus() {
        return dealStatus;
    }

    /**
     * 获取配送中心编码
     *
     * @return deliveryCode - 配送中心编码
     */
    public String getDeliveryCode() {
        return deliveryCode;
    }

    /**
     * 获取送货周期
     *
     * @return deliveryCycle - 送货周期
     */
    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    /**
     * 获取配送中心名称
     *
     * @return deliveryName - 配送中心名称
     */
    public String getDeliveryName() {
        return deliveryName;
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
     * 获取经营公司编码
     *
     * @return erpCode - 经营公司编码
     */
    public String getErpCode() {
        return erpCode;
    }

    /**
     * 获取组织机构ID
     *
     * @return eshopId - 组织机构ID
     */
    public Long getEshopId() {
        return eshopId;
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
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 获取是否叶子结点
     *
     * @return leafFlag - 是否叶子结点
     */
    public Boolean getLeafFlag() {
        return leafFlag;
    }

    /**
     * 获取加盟属性
     *
     * @return league - 加盟属性
     */
    public String getLeague() {
        return league;
    }

    /**
     * 获取层级
     *
     * @return level - 层级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 获取精度
     *
     * @return longitude - 精度
     */
    public String getLongitude() {
        return longitude;
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
     * 获取开业时间
     *
     * @return openningTime - 开业时间
     */
    public Date getOpenningTime() {
        return openningTime;
    }

    /**
     * 获取订货周期
     *
     * @return orderCycle - 订货周期
     */
    public String getOrderCycle() {
        return orderCycle;
    }

    /**
     * 获取所属中心店
     *
     * @return owneShopIdx - 所属中心店
     */
    public String getOwneShopIdx() {
        return owneShopIdx;
    }

    /**
     * 获取上级代码
     *
     * @return parentCode - 上级代码
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 获取上级ID
     *
     * @return parentId - 上级ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 获取物价群组
     *
     * @return priceGroup - 物价群组
     */
    public String getPriceGroup() {
        return priceGroup;
    }

    /**
     * 获取订货模式
     *
     * @return purchaseTypex - 订货模式
     */
    public String getPurchaseTypex() {
        return purchaseTypex;
    }

    /**
     * 获取财务入账日期
     *
     * @return recordDate - 财务入账日期
     */
    public Date getRecordDate() {
        return recordDate;
    }

    /**
     * 获取行政区域编码
     *
     * @return regionCode - 行政区域编码
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 获取行政区域ID
     *
     * @return regionId - 行政区域ID
     */
    public Long getRegionId() {
        return regionId;
    }

    /**
     * 获取租金
     *
     * @return rent - 租金
     */
    public BigDecimal getRent() {
        return rent;
    }

    /**
     * 获取安全库存天数
     *
     * @return safeStockDays - 安全库存天数
     */
    public Integer getSafeStockDays() {
        return safeStockDays;
    }

    /**
     * 获取服务半径
     *
     * @return serviceRadiu - 服务半径
     */
    public Integer getServiceRadiu() {
        return serviceRadiu;
    }

    /**
     * 获取机构编码
     *
     * @return shopCode - 机构编码
     */
    public String getShopCode() {
        return shopCode;
    }

    public String getShopEnName() {
		return shopEnName;
	}

    /**
     * 获取业态
     *
     * @return shopForm - 业态
     */
    public String getShopForm() {
        return shopForm;
    }

    /**
     * 获取门店等级
     *
     * @return shopLevel - 门店等级
     */
    public String getShopLevel() {
        return shopLevel;
    }

    /**
     * 获取机构名称
     *
     * @return shopName - 机构名称
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 获取门店规模
     *
     * @return shopSize - 门店规模
     */
    public String getShopSize() {
        return shopSize;
    }

    /**
     * 获取机构简称
     *
     * @return shopSName - 机构简称
     */
    public String getShopSName() {
        return shopSName;
    }

    /**
     * 获取门店类型
     *
     * @return shopTypex - 门店类型
     */
    public String getShopTypex() {
        return shopTypex;
    }

    /**
     * 获取营运区域
     *
     * @return shopZonex - 营运区域
     */
    public String getShopZonex() {
        return shopZonex;
    }

    /**
     * 获取数据来源编码
     *
     * @return sourceFromCode - 数据来源编码
     */
    public String getSourceFromCode() {
        return sourceFromCode;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 获取核算码
     *
     * @return subject - 核算码
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 获取联系电话
     *
     * @return telephone - 联系电话
     */
    public String getTelephone() {
        return telephone;
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
     * 设置详细地址
     *
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 设置账套编码
     *
     * @param bookNo 账套编码
     */
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    /**
     * 设置门店品牌
     *
     * @param brand 门店品牌
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 设置建筑面积
     *
     * @param buildAreage 建筑面积
     */
    public void setBuildAreage(Double buildAreage) {
        this.buildAreage = buildAreage;
    }

    /**
     * 设置营业面积
     *
     * @param busiAreage 营业面积
     */
    public void setBusiAreage(Double busiAreage) {
        this.busiAreage = busiAreage;
    }

    /**
     * 设置商圈
     *
     * @param commerceCircle 商圈
     */
    public void setCommerceCircle(String commerceCircle) {
        this.commerceCircle = commerceCircle;
    }

    /**
     * 设置公司名称
     *
     * @param companyName 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 设置0-未处理/1-处理中/2-已处理
     *
     * @param dealStatus 0-未处理/1-处理中/2-已处理
     */
    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * 设置配送中心编码
     *
     * @param deliveryCode 配送中心编码
     */
    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    /**
     * 设置送货周期
     *
     * @param deliveryCycle 送货周期
     */
    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    /**
     * 设置配送中心名称
     *
     * @param deliveryName 配送中心名称
     */
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
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
     * 设置经营公司编码
     *
     * @param erpCode 经营公司编码
     */
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    /**
     * 设置组织机构ID
     *
     * @param eshopId 组织机构ID
     */
    public void setEshopId(Long eshopId) {
        this.eshopId = eshopId;
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
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 设置是否叶子结点
     *
     * @param leafFlag 是否叶子结点
     */
    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
    }

    /**
     * 设置加盟属性
     *
     * @param league 加盟属性
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     * 设置层级
     *
     * @param level 层级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 设置精度
     *
     * @param longitude 精度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
     * 设置开业时间
     *
     * @param openningTime 开业时间
     */
    public void setOpenningTime(Date openningTime) {
        this.openningTime = openningTime;
    }

    /**
     * 设置订货周期
     *
     * @param orderCycle 订货周期
     */
    public void setOrderCycle(String orderCycle) {
        this.orderCycle = orderCycle;
    }

    /**
     * 设置所属中心店
     *
     * @param owneShopIdx 所属中心店
     */
    public void setOwneShopIdx(String owneShopIdx) {
        this.owneShopIdx = owneShopIdx;
    }

    /**
     * 设置上级代码
     *
     * @param parentCode 上级代码
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * 设置上级ID
     *
     * @param parentId 上级ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 设置物价群组
     *
     * @param priceGroup 物价群组
     */
    public void setPriceGroup(String priceGroup) {
        this.priceGroup = priceGroup;
    }

    /**
     * 设置订货模式
     *
     * @param purchaseTypex 订货模式
     */
    public void setPurchaseTypex(String purchaseTypex) {
        this.purchaseTypex = purchaseTypex;
    }

    /**
     * 设置财务入账日期
     *
     * @param recordDate 财务入账日期
     */
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * 设置行政区域编码
     *
     * @param regionCode 行政区域编码
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 设置行政区域ID
     *
     * @param regionId 行政区域ID
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * 设置租金
     *
     * @param rent 租金
     */
    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    /**
     * 设置安全库存天数
     *
     * @param safeStockDays 安全库存天数
     */
    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
    }

    /**
     * 设置服务半径
     *
     * @param serviceRadiu 服务半径
     */
    public void setServiceRadiu(Integer serviceRadiu) {
        this.serviceRadiu = serviceRadiu;
    }

    /**
     * 设置机构编码
     *
     * @param shopCode 机构编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public void setShopEnName(String shopEnName) {
		this.shopEnName = shopEnName;
	}

    /**
     * 设置业态
     *
     * @param shopForm 业态
     */
    public void setShopForm(String shopForm) {
        this.shopForm = shopForm;
    }

    /**
     * 设置门店等级
     *
     * @param shopLevel 门店等级
     */
    public void setShopLevel(String shopLevel) {
        this.shopLevel = shopLevel;
    }

    /**
     * 设置机构名称
     *
     * @param shopName 机构名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 设置门店规模
     *
     * @param shopSize 门店规模
     */
    public void setShopSize(String shopSize) {
        this.shopSize = shopSize;
    }

    /**
     * 设置机构简称
     *
     * @param shopSName 机构简称
     */
    public void setShopSName(String shopSName) {
        this.shopSName = shopSName;
    }

    /**
     * 设置门店类型
     *
     * @param shopTypex 门店类型
     */
    public void setShopTypex(String shopTypex) {
        this.shopTypex = shopTypex;
    }

    /**
     * 设置营运区域
     *
     * @param shopZonex 营运区域
     */
    public void setShopZonex(String shopZonex) {
        this.shopZonex = shopZonex;
    }

    /**
     * 设置数据来源编码
     *
     * @param sourceFromCode 数据来源编码
     */
    public void setSourceFromCode(String sourceFromCode) {
        this.sourceFromCode = sourceFromCode;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 设置核算码
     *
     * @param subject 核算码
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 设置联系电话
     *
     * @param telephone 联系电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
