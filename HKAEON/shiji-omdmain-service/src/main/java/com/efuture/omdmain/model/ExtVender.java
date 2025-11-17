package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "extvender")
public class ExtVender {
    /**
     * 供应商ID
     */
    @Id
    private Long evid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 供应商编码
     */
    private String venderCode;

    /**
     * 供应商简称
     */
    private String vendersName;

    /**
     * 供应商全称
     */
    private String venderName;

    /**
     * 供应商英文名
     */
    private String venderEnName;

    /**
     * 上级供应商ID
     */
    private Long parentVenderId;

    /**
     * 供应商类型
     */
    private Integer venderType;

    /**
     * 合同性质
     */
    private Integer contractType;

    /**
     * 主签约大类
     */
    private Integer majorId;

    /**
     * 结算地
     */
    private String sShopId;

    /**
     * 结算卡号
     */
    private String venderCardId;

    /**
     * 纳税号
     */
    private String taxPayNum;

    /**
     * 机构代码证号/身份证号
     */
    @Column(name = "IdentityCard")
    private String identityCard;

    /**
     * 结算标识
     */
    private Integer localFlag;

    /**
     * 天付款标记
     */
    private Integer dayPayFlag;

    /**
     * 重点供应商标记
     */
    private Integer primaryFlag;

    /**
     * 付款方式
     */
    private Integer payMode;

    /**
     * 是否预付款
     */
    private Integer prepayFlag;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 公司传真
     */
    private String fax;

    /**
     * 联系人
     */
    private String contactMan;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 开户银行
     */
    private String bankName;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 银行账户名
     */
    private String bankAccountName;

    /**
     * 经营范围
     */
    private String bizScope;

    /**
     * 代理级别
     */
    private String bizLevel;

    /**
     * 法人代表
     */
    private String chairMan;

    /**
     * 地区编号
     */
    private Integer zoneId;

    /**
     * 纳税级别
     */
    private Integer taxLevel;

    /**
     * 出单方式
     */
    private Integer orderMode;

    /**
     * 供应商状态
     */
    private Integer venderStatus;

    /**
     * 网址
     */
    private String netAddress;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 发票地址
     */
    private String invoiceAddress;

    /**
     * 发票抬头
     */
    private String invoiceHead;

    /**
     * 注册资本
     */
    private BigDecimal registerCap;

    /**
     * 企业性质
     */
    private String entKind;

    /**
     * 供应商出单日
     */
    private String orderDay;

    /**
     * 引入时间
     */
    private Date inDate;

    /**
     * 引入部门
     */
    private String inDepartment;

    /**
     * 引入人
     */
    private String inVendee;

    /**
     * 清理买手
     */
    private String endVendee;

    /**
     * 清理原因
     */
    private String endReason;

    /**
     * 淘汰时间
     */
    private Date endDate;

    /**
     * 上次资料变更原因
     */
    private String chnageReason;

    /**
     * 引进品牌
     */
    private String inBrand;

    /**
     * 发货地
     */
    private String shipmentSto;

    /**
     * 送货方式
     */
    private String deliveryType;

    /**
     * 父子供应商标识
     */
    private Integer parentFlag;

    /**
     * 是否供应商卸货
     */
    private Integer mallFlag;

    /**
     * 供应商分类
     */
    private Integer purType;

    /**
     * 货源属性
     */
    private Integer originType;

    /**
     * 付款级次
     */
    private Integer payLevel;

    /**
     * 是否发送SCM
     */
    private Integer isScm;

    /**
     * 结算主体
     */
    private String str1;

    /**
     * 供应商类型str2
     */
    private String str2;

    /**
     * 采购供应商编码
     */
    private String str3;

    /**
     * 是否合并财务余额
     */
    private String str4;

    /**
     * 到货周期
     */
    private String str5;

    /**
     * 是否信任供应商
     */
    private Boolean isTrust;

    /**
     * 是否烟盐供应商
     */
    private Boolean isSpec;

    /**
     * SCM是否可以查看库存
     */
    private Boolean scmCtl;

    /**
     * 是否分采统销
     */
    private Boolean isSalt;

    /**
     * 退货金额与应付款余额的比例
     */
    private Float trustValueRate;

    /**
     * 已付款与库存金额的比例
     */
    private Float payStkValueRate;

    /**
     * 是否统采供应商
     */
    private Boolean isTczs;

    /**
     * 供应商等级
     */
    private String venderGrade;

    /**
     * 付款优先等级
     */
    private String payGrade;

    /**
     * 备用S3
     */
    private String s3;

    /**
     * 最小起订金额
     */
    private BigDecimal n1;

    /**
     * 备用N2
     */
    private Double n2;

    /**
     * 备用D1
     */
    private Date d1;

    /**
     * 备用D2
     */
    private Date d2;

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
     * 获取供应商ID
     *
     * @return evid - 供应商ID
     */
    public Long getEvid() {
        return evid;
    }

    /**
     * 设置供应商ID
     *
     * @param evid 供应商ID
     */
    public void setEvid(Long evid) {
        this.evid = evid;
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
     * 获取供应商编码
     *
     * @return venderCode - 供应商编码
     */
    public String getVenderCode() {
        return venderCode;
    }

    /**
     * 设置供应商编码
     *
     * @param venderCode 供应商编码
     */
    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    /**
     * 获取供应商简称
     *
     * @return vendersName - 供应商简称
     */
    public String getVendersName() {
        return vendersName;
    }

    /**
     * 设置供应商简称
     *
     * @param vendersName 供应商简称
     */
    public void setVendersName(String vendersName) {
        this.vendersName = vendersName;
    }

    /**
     * 获取供应商全称
     *
     * @return venderName - 供应商全称
     */
    public String getVenderName() {
        return venderName;
    }

    /**
     * 设置供应商全称
     *
     * @param venderName 供应商全称
     */
    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    /**
     * 获取供应商英文名
     *
     * @return venderEnName - 供应商英文名
     */
    public String getVenderEnName() {
        return venderEnName;
    }

    /**
     * 设置供应商英文名
     *
     * @param venderEnName 供应商英文名
     */
    public void setVenderEnName(String venderEnName) {
        this.venderEnName = venderEnName;
    }

    /**
     * 获取上级供应商ID
     *
     * @return parentVenderId - 上级供应商ID
     */
    public Long getParentVenderId() {
        return parentVenderId;
    }

    /**
     * 设置上级供应商ID
     *
     * @param parentVenderId 上级供应商ID
     */
    public void setParentVenderId(Long parentVenderId) {
        this.parentVenderId = parentVenderId;
    }

    /**
     * 获取供应商类型
     *
     * @return venderType - 供应商类型
     */
    public Integer getVenderType() {
        return venderType;
    }

    /**
     * 设置供应商类型
     *
     * @param venderType 供应商类型
     */
    public void setVenderType(Integer venderType) {
        this.venderType = venderType;
    }

    /**
     * 获取合同性质
     *
     * @return contractType - 合同性质
     */
    public Integer getContractType() {
        return contractType;
    }

    /**
     * 设置合同性质
     *
     * @param contractType 合同性质
     */
    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    /**
     * 获取主签约大类
     *
     * @return majorId - 主签约大类
     */
    public Integer getMajorId() {
        return majorId;
    }

    /**
     * 设置主签约大类
     *
     * @param majorId 主签约大类
     */
    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    /**
     * 获取结算地
     *
     * @return sShopId - 结算地
     */
    public String getsShopId() {
        return sShopId;
    }

    /**
     * 设置结算地
     *
     * @param sShopId 结算地
     */
    public void setsShopId(String sShopId) {
        this.sShopId = sShopId;
    }

    /**
     * 获取结算卡号
     *
     * @return venderCardId - 结算卡号
     */
    public String getVenderCardId() {
        return venderCardId;
    }

    /**
     * 设置结算卡号
     *
     * @param venderCardId 结算卡号
     */
    public void setVenderCardId(String venderCardId) {
        this.venderCardId = venderCardId;
    }

    /**
     * 获取纳税号
     *
     * @return taxPayNum - 纳税号
     */
    public String getTaxPayNum() {
        return taxPayNum;
    }

    /**
     * 设置纳税号
     *
     * @param taxPayNum 纳税号
     */
    public void setTaxPayNum(String taxPayNum) {
        this.taxPayNum = taxPayNum;
    }

    /**
     * 获取机构代码证号/身份证号
     *
     * @return IdentityCard - 机构代码证号/身份证号
     */
    public String getIdentityCard() {
        return identityCard;
    }

    /**
     * 设置机构代码证号/身份证号
     *
     * @param identityCard 机构代码证号/身份证号
     */
    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    /**
     * 获取结算标识
     *
     * @return localFlag - 结算标识
     */
    public Integer getLocalFlag() {
        return localFlag;
    }

    /**
     * 设置结算标识
     *
     * @param localFlag 结算标识
     */
    public void setLocalFlag(Integer localFlag) {
        this.localFlag = localFlag;
    }

    /**
     * 获取天付款标记
     *
     * @return dayPayFlag - 天付款标记
     */
    public Integer getDayPayFlag() {
        return dayPayFlag;
    }

    /**
     * 设置天付款标记
     *
     * @param dayPayFlag 天付款标记
     */
    public void setDayPayFlag(Integer dayPayFlag) {
        this.dayPayFlag = dayPayFlag;
    }

    /**
     * 获取重点供应商标记
     *
     * @return primaryFlag - 重点供应商标记
     */
    public Integer getPrimaryFlag() {
        return primaryFlag;
    }

    /**
     * 设置重点供应商标记
     *
     * @param primaryFlag 重点供应商标记
     */
    public void setPrimaryFlag(Integer primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    /**
     * 获取付款方式
     *
     * @return payMode - 付款方式
     */
    public Integer getPayMode() {
        return payMode;
    }

    /**
     * 设置付款方式
     *
     * @param payMode 付款方式
     */
    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    /**
     * 获取是否预付款
     *
     * @return prepayFlag - 是否预付款
     */
    public Integer getPrepayFlag() {
        return prepayFlag;
    }

    /**
     * 设置是否预付款
     *
     * @param prepayFlag 是否预付款
     */
    public void setPrepayFlag(Integer prepayFlag) {
        this.prepayFlag = prepayFlag;
    }

    /**
     * 获取详细地址
     *
     * @return address - 详细地址
     */
    public String getAddress() {
        return address;
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
     * 获取公司传真
     *
     * @return fax - 公司传真
     */
    public String getFax() {
        return fax;
    }

    /**
     * 设置公司传真
     *
     * @param fax 公司传真
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 获取联系人
     *
     * @return contactMan - 联系人
     */
    public String getContactMan() {
        return contactMan;
    }

    /**
     * 设置联系人
     *
     * @param contactMan 联系人
     */
    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    /**
     * 获取联系人电话
     *
     * @return contactPhone - 联系人电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置联系人电话
     *
     * @param contactPhone 联系人电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * 获取邮政编码
     *
     * @return zipCode - 邮政编码
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 设置邮政编码
     *
     * @param zipCode 邮政编码
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 获取开户银行
     *
     * @return bankName - 开户银行
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置开户银行
     *
     * @param bankName 开户银行
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取银行账户
     *
     * @return bankAccount - 银行账户
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * 设置银行账户
     *
     * @param bankAccount 银行账户
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * 获取银行账户名
     *
     * @return bankAccountName - 银行账户名
     */
    public String getBankAccountName() {
        return bankAccountName;
    }

    /**
     * 设置银行账户名
     *
     * @param bankAccountName 银行账户名
     */
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    /**
     * 获取经营范围
     *
     * @return bizScope - 经营范围
     */
    public String getBizScope() {
        return bizScope;
    }

    /**
     * 设置经营范围
     *
     * @param bizScope 经营范围
     */
    public void setBizScope(String bizScope) {
        this.bizScope = bizScope;
    }

    /**
     * 获取代理级别
     *
     * @return bizLevel - 代理级别
     */
    public String getBizLevel() {
        return bizLevel;
    }

    /**
     * 设置代理级别
     *
     * @param bizLevel 代理级别
     */
    public void setBizLevel(String bizLevel) {
        this.bizLevel = bizLevel;
    }

    /**
     * 获取法人代表
     *
     * @return chairMan - 法人代表
     */
    public String getChairMan() {
        return chairMan;
    }

    /**
     * 设置法人代表
     *
     * @param chairMan 法人代表
     */
    public void setChairMan(String chairMan) {
        this.chairMan = chairMan;
    }

    /**
     * 获取地区编号
     *
     * @return zoneId - 地区编号
     */
    public Integer getZoneId() {
        return zoneId;
    }

    /**
     * 设置地区编号
     *
     * @param zoneId 地区编号
     */
    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * 获取纳税级别
     *
     * @return taxLevel - 纳税级别
     */
    public Integer getTaxLevel() {
        return taxLevel;
    }

    /**
     * 设置纳税级别
     *
     * @param taxLevel 纳税级别
     */
    public void setTaxLevel(Integer taxLevel) {
        this.taxLevel = taxLevel;
    }

    /**
     * 获取出单方式
     *
     * @return orderMode - 出单方式
     */
    public Integer getOrderMode() {
        return orderMode;
    }

    /**
     * 设置出单方式
     *
     * @param orderMode 出单方式
     */
    public void setOrderMode(Integer orderMode) {
        this.orderMode = orderMode;
    }

    /**
     * 获取供应商状态
     *
     * @return venderStatus - 供应商状态
     */
    public Integer getVenderStatus() {
        return venderStatus;
    }

    /**
     * 设置供应商状态
     *
     * @param venderStatus 供应商状态
     */
    public void setVenderStatus(Integer venderStatus) {
        this.venderStatus = venderStatus;
    }

    /**
     * 获取网址
     *
     * @return netAddress - 网址
     */
    public String getNetAddress() {
        return netAddress;
    }

    /**
     * 设置网址
     *
     * @param netAddress 网址
     */
    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
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
     * 获取发票地址
     *
     * @return invoiceAddress - 发票地址
     */
    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    /**
     * 设置发票地址
     *
     * @param invoiceAddress 发票地址
     */
    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    /**
     * 获取发票抬头
     *
     * @return invoiceHead - 发票抬头
     */
    public String getInvoiceHead() {
        return invoiceHead;
    }

    /**
     * 设置发票抬头
     *
     * @param invoiceHead 发票抬头
     */
    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    /**
     * 获取注册资本
     *
     * @return registerCap - 注册资本
     */
    public BigDecimal getRegisterCap() {
        return registerCap;
    }

    /**
     * 设置注册资本
     *
     * @param registerCap 注册资本
     */
    public void setRegisterCap(BigDecimal registerCap) {
        this.registerCap = registerCap;
    }

    /**
     * 获取企业性质
     *
     * @return entKind - 企业性质
     */
    public String getEntKind() {
        return entKind;
    }

    /**
     * 设置企业性质
     *
     * @param entKind 企业性质
     */
    public void setEntKind(String entKind) {
        this.entKind = entKind;
    }

    /**
     * 获取供应商出单日
     *
     * @return orderDay - 供应商出单日
     */
    public String getOrderDay() {
        return orderDay;
    }

    /**
     * 设置供应商出单日
     *
     * @param orderDay 供应商出单日
     */
    public void setOrderDay(String orderDay) {
        this.orderDay = orderDay;
    }

    /**
     * 获取引入时间
     *
     * @return inDate - 引入时间
     */
    public Date getInDate() {
        return inDate;
    }

    /**
     * 设置引入时间
     *
     * @param inDate 引入时间
     */
    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    /**
     * 获取引入部门
     *
     * @return inDepartment - 引入部门
     */
    public String getInDepartment() {
        return inDepartment;
    }

    /**
     * 设置引入部门
     *
     * @param inDepartment 引入部门
     */
    public void setInDepartment(String inDepartment) {
        this.inDepartment = inDepartment;
    }

    /**
     * 获取引入人
     *
     * @return inVendee - 引入人
     */
    public String getInVendee() {
        return inVendee;
    }

    /**
     * 设置引入人
     *
     * @param inVendee 引入人
     */
    public void setInVendee(String inVendee) {
        this.inVendee = inVendee;
    }

    /**
     * 获取清理买手
     *
     * @return endVendee - 清理买手
     */
    public String getEndVendee() {
        return endVendee;
    }

    /**
     * 设置清理买手
     *
     * @param endVendee 清理买手
     */
    public void setEndVendee(String endVendee) {
        this.endVendee = endVendee;
    }

    /**
     * 获取清理原因
     *
     * @return endReason - 清理原因
     */
    public String getEndReason() {
        return endReason;
    }

    /**
     * 设置清理原因
     *
     * @param endReason 清理原因
     */
    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    /**
     * 获取淘汰时间
     *
     * @return endDate - 淘汰时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置淘汰时间
     *
     * @param endDate 淘汰时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取上次资料变更原因
     *
     * @return chnageReason - 上次资料变更原因
     */
    public String getChnageReason() {
        return chnageReason;
    }

    /**
     * 设置上次资料变更原因
     *
     * @param chnageReason 上次资料变更原因
     */
    public void setChnageReason(String chnageReason) {
        this.chnageReason = chnageReason;
    }

    /**
     * 获取引进品牌
     *
     * @return inBrand - 引进品牌
     */
    public String getInBrand() {
        return inBrand;
    }

    /**
     * 设置引进品牌
     *
     * @param inBrand 引进品牌
     */
    public void setInBrand(String inBrand) {
        this.inBrand = inBrand;
    }

    /**
     * 获取发货地
     *
     * @return shipmentSto - 发货地
     */
    public String getShipmentSto() {
        return shipmentSto;
    }

    /**
     * 设置发货地
     *
     * @param shipmentSto 发货地
     */
    public void setShipmentSto(String shipmentSto) {
        this.shipmentSto = shipmentSto;
    }

    /**
     * 获取送货方式
     *
     * @return deliveryType - 送货方式
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * 设置送货方式
     *
     * @param deliveryType 送货方式
     */
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * 获取父子供应商标识
     *
     * @return parentFlag - 父子供应商标识
     */
    public Integer getParentFlag() {
        return parentFlag;
    }

    /**
     * 设置父子供应商标识
     *
     * @param parentFlag 父子供应商标识
     */
    public void setParentFlag(Integer parentFlag) {
        this.parentFlag = parentFlag;
    }

    /**
     * 获取是否供应商卸货
     *
     * @return mallFlag - 是否供应商卸货
     */
    public Integer getMallFlag() {
        return mallFlag;
    }

    /**
     * 设置是否供应商卸货
     *
     * @param mallFlag 是否供应商卸货
     */
    public void setMallFlag(Integer mallFlag) {
        this.mallFlag = mallFlag;
    }

    /**
     * 获取供应商分类
     *
     * @return purType - 供应商分类
     */
    public Integer getPurType() {
        return purType;
    }

    /**
     * 设置供应商分类
     *
     * @param purType 供应商分类
     */
    public void setPurType(Integer purType) {
        this.purType = purType;
    }

    /**
     * 获取货源属性
     *
     * @return originType - 货源属性
     */
    public Integer getOriginType() {
        return originType;
    }

    /**
     * 设置货源属性
     *
     * @param originType 货源属性
     */
    public void setOriginType(Integer originType) {
        this.originType = originType;
    }

    /**
     * 获取付款级次
     *
     * @return payLevel - 付款级次
     */
    public Integer getPayLevel() {
        return payLevel;
    }

    /**
     * 设置付款级次
     *
     * @param payLevel 付款级次
     */
    public void setPayLevel(Integer payLevel) {
        this.payLevel = payLevel;
    }

    /**
     * 获取是否发送SCM
     *
     * @return isScm - 是否发送SCM
     */
    public Integer getIsScm() {
        return isScm;
    }

    /**
     * 设置是否发送SCM
     *
     * @param isScm 是否发送SCM
     */
    public void setIsScm(Integer isScm) {
        this.isScm = isScm;
    }

    /**
     * 获取结算主体
     *
     * @return str1 - 结算主体
     */
    public String getStr1() {
        return str1;
    }

    /**
     * 设置结算主体
     *
     * @param str1 结算主体
     */
    public void setStr1(String str1) {
        this.str1 = str1;
    }

    /**
     * 获取供应商类型str2
     *
     * @return str2 - 供应商类型str2
     */
    public String getStr2() {
        return str2;
    }

    /**
     * 设置供应商类型str2
     *
     * @param str2 供应商类型str2
     */
    public void setStr2(String str2) {
        this.str2 = str2;
    }

    /**
     * 获取采购供应商编码
     *
     * @return str3 - 采购供应商编码
     */
    public String getStr3() {
        return str3;
    }

    /**
     * 设置采购供应商编码
     *
     * @param str3 采购供应商编码
     */
    public void setStr3(String str3) {
        this.str3 = str3;
    }

    /**
     * 获取是否合并财务余额
     *
     * @return str4 - 是否合并财务余额
     */
    public String getStr4() {
        return str4;
    }

    /**
     * 设置是否合并财务余额
     *
     * @param str4 是否合并财务余额
     */
    public void setStr4(String str4) {
        this.str4 = str4;
    }

    /**
     * 获取到货周期
     *
     * @return str5 - 到货周期
     */
    public String getStr5() {
        return str5;
    }

    /**
     * 设置到货周期
     *
     * @param str5 到货周期
     */
    public void setStr5(String str5) {
        this.str5 = str5;
    }

    /**
     * 获取是否信任供应商
     *
     * @return isTrust - 是否信任供应商
     */
    public Boolean getIsTrust() {
        return isTrust;
    }

    /**
     * 设置是否信任供应商
     *
     * @param isTrust 是否信任供应商
     */
    public void setIsTrust(Boolean isTrust) {
        this.isTrust = isTrust;
    }

    /**
     * 获取是否烟盐供应商
     *
     * @return isSpec - 是否烟盐供应商
     */
    public Boolean getIsSpec() {
        return isSpec;
    }

    /**
     * 设置是否烟盐供应商
     *
     * @param isSpec 是否烟盐供应商
     */
    public void setIsSpec(Boolean isSpec) {
        this.isSpec = isSpec;
    }

    /**
     * 获取SCM是否可以查看库存
     *
     * @return scmCtl - SCM是否可以查看库存
     */
    public Boolean getScmCtl() {
        return scmCtl;
    }

    /**
     * 设置SCM是否可以查看库存
     *
     * @param scmCtl SCM是否可以查看库存
     */
    public void setScmCtl(Boolean scmCtl) {
        this.scmCtl = scmCtl;
    }

    /**
     * 获取是否分采统销
     *
     * @return isSalt - 是否分采统销
     */
    public Boolean getIsSalt() {
        return isSalt;
    }

    /**
     * 设置是否分采统销
     *
     * @param isSalt 是否分采统销
     */
    public void setIsSalt(Boolean isSalt) {
        this.isSalt = isSalt;
    }

    /**
     * 获取退货金额与应付款余额的比例
     *
     * @return trustValueRate - 退货金额与应付款余额的比例
     */
    public Float getTrustValueRate() {
        return trustValueRate;
    }

    /**
     * 设置退货金额与应付款余额的比例
     *
     * @param trustValueRate 退货金额与应付款余额的比例
     */
    public void setTrustValueRate(Float trustValueRate) {
        this.trustValueRate = trustValueRate;
    }

    /**
     * 获取已付款与库存金额的比例
     *
     * @return payStkValueRate - 已付款与库存金额的比例
     */
    public Float getPayStkValueRate() {
        return payStkValueRate;
    }

    /**
     * 设置已付款与库存金额的比例
     *
     * @param payStkValueRate 已付款与库存金额的比例
     */
    public void setPayStkValueRate(Float payStkValueRate) {
        this.payStkValueRate = payStkValueRate;
    }

    /**
     * 获取是否统采供应商
     *
     * @return isTczs - 是否统采供应商
     */
    public Boolean getIsTczs() {
        return isTczs;
    }

    /**
     * 设置是否统采供应商
     *
     * @param isTczs 是否统采供应商
     */
    public void setIsTczs(Boolean isTczs) {
        this.isTczs = isTczs;
    }

    /**
     * 获取供应商等级
     *
     * @return venderGrade - 供应商等级
     */
    public String getVenderGrade() {
        return venderGrade;
    }

    /**
     * 设置供应商等级
     *
     * @param venderGrade 供应商等级
     */
    public void setVenderGrade(String venderGrade) {
        this.venderGrade = venderGrade;
    }

    /**
     * 获取付款优先等级
     *
     * @return payGrade - 付款优先等级
     */
    public String getPayGrade() {
        return payGrade;
    }

    /**
     * 设置付款优先等级
     *
     * @param payGrade 付款优先等级
     */
    public void setPayGrade(String payGrade) {
        this.payGrade = payGrade;
    }

    /**
     * 获取备用S3
     *
     * @return s3 - 备用S3
     */
    public String getS3() {
        return s3;
    }

    /**
     * 设置备用S3
     *
     * @param s3 备用S3
     */
    public void setS3(String s3) {
        this.s3 = s3;
    }

    /**
     * 获取最小起订金额
     *
     * @return n1 - 最小起订金额
     */
    public BigDecimal getN1() {
        return n1;
    }

    /**
     * 设置最小起订金额
     *
     * @param n1 最小起订金额
     */
    public void setN1(BigDecimal n1) {
        this.n1 = n1;
    }

    /**
     * 获取备用N2
     *
     * @return n2 - 备用N2
     */
    public Double getN2() {
        return n2;
    }

    /**
     * 设置备用N2
     *
     * @param n2 备用N2
     */
    public void setN2(Double n2) {
        this.n2 = n2;
    }

    /**
     * 获取备用D1
     *
     * @return d1 - 备用D1
     */
    public Date getD1() {
        return d1;
    }

    /**
     * 设置备用D1
     *
     * @param d1 备用D1
     */
    public void setD1(Date d1) {
        this.d1 = d1;
    }

    /**
     * 获取备用D2
     *
     * @return d2 - 备用D2
     */
    public Date getD2() {
        return d2;
    }

    /**
     * 设置备用D2
     *
     * @param d2 备用D2
     */
    public void setD2(Date d2) {
        this.d2 = d2;
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