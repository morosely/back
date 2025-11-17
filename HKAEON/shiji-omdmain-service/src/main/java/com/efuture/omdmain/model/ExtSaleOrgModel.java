package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "extsaleorg")
public class ExtSaleOrgModel {
    /**
     * 柜组ID
     */
    @Id
    private Long esaleOrgId;

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
     * 机构编码
     */
    private String shopCode;

    /**
     * 机构名称
     */
    private String shopName;

    /**
     * 机构英文名称
     */
    private String shopEnName;

    /**
     * 机构简称
     */
    private String shopSName;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 上级代码
     */
    private String parentCode;

    /**
     * 业态
     */
    private String shopForm;

    /**
     * 门店类型
     */
    private String shopTypex;

    /**
     * 订货模式
     */
    private String purchaseTypex;

    /**
     * 商圈
     */
    private String commerceCircle;

    /**
     * 加盟属性
     */
    private String league;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 物价群组
     */
    private String priceGroup;

    /**
     * 营运区域
     */
    private String shopZonex;

    /**
     * 开业时间
     */
    private Date openningTime;

    /**
     * 账套编码
     */
    private String bookNo;

    /**
     * 核算码
     */
    private String subject;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 配送中心编码
     */
    private String deliveryCode;

    /**
     * 配送中心名称
     */
    private String deliveryName;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 营业面积
     */
    private Double busiAreage;

    /**
     * 建筑面积
     */
    private Double buildAreage;

    /**
     * 租金
     */
    private BigDecimal rent;

    /**
     * 送货周期
     */
    private String deliveryCycle;

    /**
     * 安全库存天数
     */
    private Integer safeStockDays;

    /**
     * 订货周期
     */
    private String orderCycle;

    /**
     * 财务入账日期
     */
    private Date recordDate;

    /**
     * 行业
     */
    private String industry;

    /**
     * 核算代码
     */
    private String accountCode;

    /**
     * 经营区域
     */
    private String manageArea;

    /**
     * 是否网上发布
     */
    private Boolean onlineFlag;

    /**
     * 仓柜类型
     */
    private Integer wareType;

    /**
     * 门店连锁方式
     */
    private Integer chainType;

    /**
     * 是否加价配送
     */
    private Boolean upDeliveryFlag;

    /**
     * 加价比率
     */
    private Double raiseRate;

    /**
     * 缺省结算依据(0-柜组/1-mall)
     */
    private Integer defaultSettlebasi;

    /**
     * 是否允许消红
     */
    private Boolean zssFlag;

    /**
     * 专柜是否允许多供应商
     */
    private Boolean multiVendorFlag;

    /**
     * 实物负责人
     */
    private String kindPrincipal;

    /**
     * 传真
     */
    private String fax;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 计划毛利
     */
    private BigDecimal planGrossProfit;

    /**
     * 固定费率
     */
    private BigDecimal flatRate;

    /**
     * 变动费率
     */
    private BigDecimal changeRate;

    /**
     * 营业面积（实测面积）
     */
    private Double realBusinessArea;

    /**
     * 岗组
     */
    private String postGroup;

    /**
     * 经营公司
     */
    private String businessCompany;

    /**
     * 是否百货管理单位
     */
    private Boolean dstoreManageFlag;

    /**
     * 商品类别编码
     */
    private String categoryCode;

    /**
     * 含公摊面积
     */
    private Double containShareArea;

    /**
     * 公摊率
     */
    private Float shareRate;

    /**
     * 最低平米库存金额
     */
    private BigDecimal pmkcMin;

    /**
     * 最高平米库存金额
     */
    private BigDecimal pmkcMax;

    /**
     * 进货周期
     */
    private Integer purchaseCycle;

    /**
     * 辅助码
     */
    private String assistCode;

    /**
     * 方位
     */
    private String orientation;

    /**
     * 形状
     */
    private String sharp;

    /**
     * 长度
     */
    private String length;

    /**
     * 租赁难易
     */
    private String rentHardLevel;

    /**
     * 广告类型
     */
    private String advType;

    /**
     * 广告介质
     */
    private String advMedia;

    /**
     * 商铺类型
     */
    private String storeType;

    /**
     * 租金面积
     */
    private Double rentArea;

    /**
     * 收楼标志
     */
    private String repossessionFlag;

    /**
     * 单位租金
     */
    private BigDecimal unitRent;

    /**
     * 总租金
     */
    private BigDecimal totalRent;

    /**
     * 租户事务协调
     */
    private String lesseeCoor;

    /**
     * a-自持商铺b-返租商铺c-代租商铺d-自有商铺
     */
    private String storeAssistInfo;

    /**
     * 合约号
     */
    private String contractNo;

    /**
     * 来源（0：定义 1:拆分 2：合并）
     */
    private Integer source;

    /**
     * 楼栋
     */
    private String build;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 经营方式
     */
    private String busiType;

    /**
     * 经营位置
     */
    private String busiPlace;

    /**
     * 区域
     */
    private String area;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 是否叶子结点
     */
    private Boolean leafFlag;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 数据来源编码
     */
    private String sourceFromCode;

    /**
     * 0-未处理/1-处理中/2-已处理
     */
    private Integer dealStatus;

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
     * 获取柜组ID
     *
     * @return esaleOrgId - 柜组ID
     */
    public Long getEsaleOrgId() {
        return esaleOrgId;
    }

    /**
     * 设置柜组ID
     *
     * @param esaleOrgId 柜组ID
     */
    public void setEsaleOrgId(Long esaleOrgId) {
        this.esaleOrgId = esaleOrgId;
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
     * 获取机构编码
     *
     * @return shopCode - 机构编码
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * 设置机构编码
     *
     * @param shopCode 机构编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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
     * 设置机构名称
     *
     * @param shopName 机构名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 获取机构英文名称
     *
     * @return shopEnName - 机构英文名称
     */
    public String getShopEnName() {
        return shopEnName;
    }

    /**
     * 设置机构英文名称
     *
     * @param shopEnName 机构英文名称
     */
    public void setShopEnName(String shopEnName) {
        this.shopEnName = shopEnName;
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
     * 设置机构简称
     *
     * @param shopSName 机构简称
     */
    public void setShopSName(String shopSName) {
        this.shopSName = shopSName;
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
     * 设置上级ID
     *
     * @param parentId 上级ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
     * 设置上级代码
     *
     * @param parentCode 上级代码
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
     * 设置业态
     *
     * @param shopForm 业态
     */
    public void setShopForm(String shopForm) {
        this.shopForm = shopForm;
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
     * 设置门店类型
     *
     * @param shopTypex 门店类型
     */
    public void setShopTypex(String shopTypex) {
        this.shopTypex = shopTypex;
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
     * 设置订货模式
     *
     * @param purchaseTypex 订货模式
     */
    public void setPurchaseTypex(String purchaseTypex) {
        this.purchaseTypex = purchaseTypex;
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
     * 设置商圈
     *
     * @param commerceCircle 商圈
     */
    public void setCommerceCircle(String commerceCircle) {
        this.commerceCircle = commerceCircle;
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
     * 设置加盟属性
     *
     * @param league 加盟属性
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     * 获取品牌
     *
     * @return brand - 品牌
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 设置品牌
     *
     * @param brand 品牌
     */
    public void setBrand(String brand) {
        this.brand = brand;
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
     * 设置物价群组
     *
     * @param priceGroup 物价群组
     */
    public void setPriceGroup(String priceGroup) {
        this.priceGroup = priceGroup;
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
     * 设置营运区域
     *
     * @param shopZonex 营运区域
     */
    public void setShopZonex(String shopZonex) {
        this.shopZonex = shopZonex;
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
     * 设置开业时间
     *
     * @param openningTime 开业时间
     */
    public void setOpenningTime(Date openningTime) {
        this.openningTime = openningTime;
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
     * 设置账套编码
     *
     * @param bookNo 账套编码
     */
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
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
     * 设置核算码
     *
     * @param subject 核算码
     */
    public void setSubject(String subject) {
        this.subject = subject;
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
     * 设置公司名称
     *
     * @param companyName 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
     * 设置配送中心编码
     *
     * @param deliveryCode 配送中心编码
     */
    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
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
     * 设置配送中心名称
     *
     * @param deliveryName 配送中心名称
     */
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
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
     * 设置联系电话
     *
     * @param telephone 联系电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
     * 设置营业面积
     *
     * @param busiAreage 营业面积
     */
    public void setBusiAreage(Double busiAreage) {
        this.busiAreage = busiAreage;
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
     * 设置建筑面积
     *
     * @param buildAreage 建筑面积
     */
    public void setBuildAreage(Double buildAreage) {
        this.buildAreage = buildAreage;
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
     * 设置租金
     *
     * @param rent 租金
     */
    public void setRent(BigDecimal rent) {
        this.rent = rent;
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
     * 设置送货周期
     *
     * @param deliveryCycle 送货周期
     */
    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
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
     * 设置安全库存天数
     *
     * @param safeStockDays 安全库存天数
     */
    public void setSafeStockDays(Integer safeStockDays) {
        this.safeStockDays = safeStockDays;
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
     * 设置订货周期
     *
     * @param orderCycle 订货周期
     */
    public void setOrderCycle(String orderCycle) {
        this.orderCycle = orderCycle;
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
     * 设置财务入账日期
     *
     * @param recordDate 财务入账日期
     */
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * 获取行业
     *
     * @return industry - 行业
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * 设置行业
     *
     * @param industry 行业
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     * 获取核算代码
     *
     * @return accountCode - 核算代码
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * 设置核算代码
     *
     * @param accountCode 核算代码
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    /**
     * 获取经营区域
     *
     * @return manageArea - 经营区域
     */
    public String getManageArea() {
        return manageArea;
    }

    /**
     * 设置经营区域
     *
     * @param manageArea 经营区域
     */
    public void setManageArea(String manageArea) {
        this.manageArea = manageArea;
    }

    /**
     * 获取是否网上发布
     *
     * @return onlineFlag - 是否网上发布
     */
    public Boolean getOnlineFlag() {
        return onlineFlag;
    }

    /**
     * 设置是否网上发布
     *
     * @param onlineFlag 是否网上发布
     */
    public void setOnlineFlag(Boolean onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    /**
     * 获取仓柜类型
     *
     * @return wareType - 仓柜类型
     */
    public Integer getWareType() {
        return wareType;
    }

    /**
     * 设置仓柜类型
     *
     * @param wareType 仓柜类型
     */
    public void setWareType(Integer wareType) {
        this.wareType = wareType;
    }

    /**
     * 获取门店连锁方式
     *
     * @return chainType - 门店连锁方式
     */
    public Integer getChainType() {
        return chainType;
    }

    /**
     * 设置门店连锁方式
     *
     * @param chainType 门店连锁方式
     */
    public void setChainType(Integer chainType) {
        this.chainType = chainType;
    }

    /**
     * 获取是否加价配送
     *
     * @return upDeliveryFlag - 是否加价配送
     */
    public Boolean getUpDeliveryFlag() {
        return upDeliveryFlag;
    }

    /**
     * 设置是否加价配送
     *
     * @param upDeliveryFlag 是否加价配送
     */
    public void setUpDeliveryFlag(Boolean upDeliveryFlag) {
        this.upDeliveryFlag = upDeliveryFlag;
    }

    /**
     * 获取加价比率
     *
     * @return raiseRate - 加价比率
     */
    public Double getRaiseRate() {
        return raiseRate;
    }

    /**
     * 设置加价比率
     *
     * @param raiseRate 加价比率
     */
    public void setRaiseRate(Double raiseRate) {
        this.raiseRate = raiseRate;
    }

    /**
     * 获取缺省结算依据(0-柜组/1-mall)
     *
     * @return defaultSettlebasi - 缺省结算依据(0-柜组/1-mall)
     */
    public Integer getDefaultSettlebasi() {
        return defaultSettlebasi;
    }

    /**
     * 设置缺省结算依据(0-柜组/1-mall)
     *
     * @param defaultSettlebasi 缺省结算依据(0-柜组/1-mall)
     */
    public void setDefaultSettlebasi(Integer defaultSettlebasi) {
        this.defaultSettlebasi = defaultSettlebasi;
    }

    /**
     * 获取是否允许消红
     *
     * @return zssFlag - 是否允许消红
     */
    public Boolean getZssFlag() {
        return zssFlag;
    }

    /**
     * 设置是否允许消红
     *
     * @param zssFlag 是否允许消红
     */
    public void setZssFlag(Boolean zssFlag) {
        this.zssFlag = zssFlag;
    }

    /**
     * 获取专柜是否允许多供应商
     *
     * @return multiVendorFlag - 专柜是否允许多供应商
     */
    public Boolean getMultiVendorFlag() {
        return multiVendorFlag;
    }

    /**
     * 设置专柜是否允许多供应商
     *
     * @param multiVendorFlag 专柜是否允许多供应商
     */
    public void setMultiVendorFlag(Boolean multiVendorFlag) {
        this.multiVendorFlag = multiVendorFlag;
    }

    /**
     * 获取实物负责人
     *
     * @return kindPrincipal - 实物负责人
     */
    public String getKindPrincipal() {
        return kindPrincipal;
    }

    /**
     * 设置实物负责人
     *
     * @param kindPrincipal 实物负责人
     */
    public void setKindPrincipal(String kindPrincipal) {
        this.kindPrincipal = kindPrincipal;
    }

    /**
     * 获取传真
     *
     * @return fax - 传真
     */
    public String getFax() {
        return fax;
    }

    /**
     * 设置传真
     *
     * @param fax 传真
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取计划毛利
     *
     * @return planGrossProfit - 计划毛利
     */
    public BigDecimal getPlanGrossProfit() {
        return planGrossProfit;
    }

    /**
     * 设置计划毛利
     *
     * @param planGrossProfit 计划毛利
     */
    public void setPlanGrossProfit(BigDecimal planGrossProfit) {
        this.planGrossProfit = planGrossProfit;
    }

    /**
     * 获取固定费率
     *
     * @return flatRate - 固定费率
     */
    public BigDecimal getFlatRate() {
        return flatRate;
    }

    /**
     * 设置固定费率
     *
     * @param flatRate 固定费率
     */
    public void setFlatRate(BigDecimal flatRate) {
        this.flatRate = flatRate;
    }

    /**
     * 获取变动费率
     *
     * @return changeRate - 变动费率
     */
    public BigDecimal getChangeRate() {
        return changeRate;
    }

    /**
     * 设置变动费率
     *
     * @param changeRate 变动费率
     */
    public void setChangeRate(BigDecimal changeRate) {
        this.changeRate = changeRate;
    }

    /**
     * 获取营业面积（实测面积）
     *
     * @return realBusinessArea - 营业面积（实测面积）
     */
    public Double getRealBusinessArea() {
        return realBusinessArea;
    }

    /**
     * 设置营业面积（实测面积）
     *
     * @param realBusinessArea 营业面积（实测面积）
     */
    public void setRealBusinessArea(Double realBusinessArea) {
        this.realBusinessArea = realBusinessArea;
    }

    /**
     * 获取岗组
     *
     * @return postGroup - 岗组
     */
    public String getPostGroup() {
        return postGroup;
    }

    /**
     * 设置岗组
     *
     * @param postGroup 岗组
     */
    public void setPostGroup(String postGroup) {
        this.postGroup = postGroup;
    }

    /**
     * 获取经营公司
     *
     * @return businessCompany - 经营公司
     */
    public String getBusinessCompany() {
        return businessCompany;
    }

    /**
     * 设置经营公司
     *
     * @param businessCompany 经营公司
     */
    public void setBusinessCompany(String businessCompany) {
        this.businessCompany = businessCompany;
    }

    /**
     * 获取是否百货管理单位
     *
     * @return dstoreManageFlag - 是否百货管理单位
     */
    public Boolean getDstoreManageFlag() {
        return dstoreManageFlag;
    }

    /**
     * 设置是否百货管理单位
     *
     * @param dstoreManageFlag 是否百货管理单位
     */
    public void setDstoreManageFlag(Boolean dstoreManageFlag) {
        this.dstoreManageFlag = dstoreManageFlag;
    }

    /**
     * 获取商品类别编码
     *
     * @return categoryCode - 商品类别编码
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * 设置商品类别编码
     *
     * @param categoryCode 商品类别编码
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * 获取含公摊面积
     *
     * @return containShareArea - 含公摊面积
     */
    public Double getContainShareArea() {
        return containShareArea;
    }

    /**
     * 设置含公摊面积
     *
     * @param containShareArea 含公摊面积
     */
    public void setContainShareArea(Double containShareArea) {
        this.containShareArea = containShareArea;
    }

    /**
     * 获取公摊率
     *
     * @return shareRate - 公摊率
     */
    public Float getShareRate() {
        return shareRate;
    }

    /**
     * 设置公摊率
     *
     * @param shareRate 公摊率
     */
    public void setShareRate(Float shareRate) {
        this.shareRate = shareRate;
    }

    /**
     * 获取最低平米库存金额
     *
     * @return pmkcMin - 最低平米库存金额
     */
    public BigDecimal getPmkcMin() {
        return pmkcMin;
    }

    /**
     * 设置最低平米库存金额
     *
     * @param pmkcMin 最低平米库存金额
     */
    public void setPmkcMin(BigDecimal pmkcMin) {
        this.pmkcMin = pmkcMin;
    }

    /**
     * 获取最高平米库存金额
     *
     * @return pmkcMax - 最高平米库存金额
     */
    public BigDecimal getPmkcMax() {
        return pmkcMax;
    }

    /**
     * 设置最高平米库存金额
     *
     * @param pmkcMax 最高平米库存金额
     */
    public void setPmkcMax(BigDecimal pmkcMax) {
        this.pmkcMax = pmkcMax;
    }

    /**
     * 获取进货周期
     *
     * @return purchaseCycle - 进货周期
     */
    public Integer getPurchaseCycle() {
        return purchaseCycle;
    }

    /**
     * 设置进货周期
     *
     * @param purchaseCycle 进货周期
     */
    public void setPurchaseCycle(Integer purchaseCycle) {
        this.purchaseCycle = purchaseCycle;
    }

    /**
     * 获取辅助码
     *
     * @return assistCode - 辅助码
     */
    public String getAssistCode() {
        return assistCode;
    }

    /**
     * 设置辅助码
     *
     * @param assistCode 辅助码
     */
    public void setAssistCode(String assistCode) {
        this.assistCode = assistCode;
    }

    /**
     * 获取方位
     *
     * @return orientation - 方位
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * 设置方位
     *
     * @param orientation 方位
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * 获取形状
     *
     * @return sharp - 形状
     */
    public String getSharp() {
        return sharp;
    }

    /**
     * 设置形状
     *
     * @param sharp 形状
     */
    public void setSharp(String sharp) {
        this.sharp = sharp;
    }

    /**
     * 获取长度
     *
     * @return length - 长度
     */
    public String getLength() {
        return length;
    }

    /**
     * 设置长度
     *
     * @param length 长度
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * 获取租赁难易
     *
     * @return rentHardLevel - 租赁难易
     */
    public String getRentHardLevel() {
        return rentHardLevel;
    }

    /**
     * 设置租赁难易
     *
     * @param rentHardLevel 租赁难易
     */
    public void setRentHardLevel(String rentHardLevel) {
        this.rentHardLevel = rentHardLevel;
    }

    /**
     * 获取广告类型
     *
     * @return advType - 广告类型
     */
    public String getAdvType() {
        return advType;
    }

    /**
     * 设置广告类型
     *
     * @param advType 广告类型
     */
    public void setAdvType(String advType) {
        this.advType = advType;
    }

    /**
     * 获取广告介质
     *
     * @return advMedia - 广告介质
     */
    public String getAdvMedia() {
        return advMedia;
    }

    /**
     * 设置广告介质
     *
     * @param advMedia 广告介质
     */
    public void setAdvMedia(String advMedia) {
        this.advMedia = advMedia;
    }

    /**
     * 获取商铺类型
     *
     * @return storeType - 商铺类型
     */
    public String getStoreType() {
        return storeType;
    }

    /**
     * 设置商铺类型
     *
     * @param storeType 商铺类型
     */
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    /**
     * 获取租金面积
     *
     * @return rentArea - 租金面积
     */
    public Double getRentArea() {
        return rentArea;
    }

    /**
     * 设置租金面积
     *
     * @param rentArea 租金面积
     */
    public void setRentArea(Double rentArea) {
        this.rentArea = rentArea;
    }

    /**
     * 获取收楼标志
     *
     * @return repossessionFlag - 收楼标志
     */
    public String getRepossessionFlag() {
        return repossessionFlag;
    }

    /**
     * 设置收楼标志
     *
     * @param repossessionFlag 收楼标志
     */
    public void setRepossessionFlag(String repossessionFlag) {
        this.repossessionFlag = repossessionFlag;
    }

    /**
     * 获取单位租金
     *
     * @return unitRent - 单位租金
     */
    public BigDecimal getUnitRent() {
        return unitRent;
    }

    /**
     * 设置单位租金
     *
     * @param unitRent 单位租金
     */
    public void setUnitRent(BigDecimal unitRent) {
        this.unitRent = unitRent;
    }

    /**
     * 获取总租金
     *
     * @return totalRent - 总租金
     */
    public BigDecimal getTotalRent() {
        return totalRent;
    }

    /**
     * 设置总租金
     *
     * @param totalRent 总租金
     */
    public void setTotalRent(BigDecimal totalRent) {
        this.totalRent = totalRent;
    }

    /**
     * 获取租户事务协调
     *
     * @return lesseeCoor - 租户事务协调
     */
    public String getLesseeCoor() {
        return lesseeCoor;
    }

    /**
     * 设置租户事务协调
     *
     * @param lesseeCoor 租户事务协调
     */
    public void setLesseeCoor(String lesseeCoor) {
        this.lesseeCoor = lesseeCoor;
    }

    /**
     * 获取a-自持商铺b-返租商铺c-代租商铺d-自有商铺
     *
     * @return storeAssistInfo - a-自持商铺b-返租商铺c-代租商铺d-自有商铺
     */
    public String getStoreAssistInfo() {
        return storeAssistInfo;
    }

    /**
     * 设置a-自持商铺b-返租商铺c-代租商铺d-自有商铺
     *
     * @param storeAssistInfo a-自持商铺b-返租商铺c-代租商铺d-自有商铺
     */
    public void setStoreAssistInfo(String storeAssistInfo) {
        this.storeAssistInfo = storeAssistInfo;
    }

    /**
     * 获取合约号
     *
     * @return contractNo - 合约号
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * 设置合约号
     *
     * @param contractNo 合约号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * 获取来源（0：定义 1:拆分 2：合并）
     *
     * @return source - 来源（0：定义 1:拆分 2：合并）
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置来源（0：定义 1:拆分 2：合并）
     *
     * @param source 来源（0：定义 1:拆分 2：合并）
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * 获取楼栋
     *
     * @return build - 楼栋
     */
    public String getBuild() {
        return build;
    }

    /**
     * 设置楼栋
     *
     * @param build 楼栋
     */
    public void setBuild(String build) {
        this.build = build;
    }

    /**
     * 获取楼层
     *
     * @return floor - 楼层
     */
    public String getFloor() {
        return floor;
    }

    /**
     * 设置楼层
     *
     * @param floor 楼层
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * 获取经营方式
     *
     * @return busiType - 经营方式
     */
    public String getBusiType() {
        return busiType;
    }

    /**
     * 设置经营方式
     *
     * @param busiType 经营方式
     */
    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    /**
     * 获取经营位置
     *
     * @return busiPlace - 经营位置
     */
    public String getBusiPlace() {
        return busiPlace;
    }

    /**
     * 设置经营位置
     *
     * @param busiPlace 经营位置
     */
    public void setBusiPlace(String busiPlace) {
        this.busiPlace = busiPlace;
    }

    /**
     * 获取区域
     *
     * @return area - 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area;
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
     * 设置层级
     *
     * @param level 层级
     */
    public void setLevel(Integer level) {
        this.level = level;
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
     * 设置是否叶子结点
     *
     * @param leafFlag 是否叶子结点
     */
    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
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
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 设置数据来源编码
     *
     * @param sourceFromCode 数据来源编码
     */
    public void setSourceFromCode(String sourceFromCode) {
        this.sourceFromCode = sourceFromCode;
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
     * 设置0-未处理/1-处理中/2-已处理
     *
     * @param dealStatus 0-未处理/1-处理中/2-已处理
     */
    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
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