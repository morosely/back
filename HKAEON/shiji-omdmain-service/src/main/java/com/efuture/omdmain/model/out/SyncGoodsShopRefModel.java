package com.efuture.omdmain.model.out;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "goodsshopref")
public class SyncGoodsShopRefModel {
	
	private SyncGoodsModel goods;
	
    public SyncGoodsModel getGoods() {
		return goods;
	}

	public void setGoods(SyncGoodsModel goods) {
		this.goods = goods;
	}

	/**
     * 经营配置ID
     */
    @Id
    private Long gsrid;

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
     * 门店编码
     */
    private String shopCode;

    /**
     * 柜组ID
     */
    private Long saleOrgId;

    /**
     * 柜组编码
     */
    private String orgCode;

    /**
     * 档口ID
     */
    private Long siid;

    /**
     * 档口编码
     */
    private String stallCode;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 主营供应商编码
     */
    private String venderCode;

    /**
     * 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    private Integer goodsStatus;

    /**
     * 执行价（含税）
     */
    private BigDecimal cost;

    /**
     * 合同价（含税）
     */
    private BigDecimal contractCost;

    /**
     * 进项税率
     */
    private Float costTaxRate;

    /**
     * 保底扣点
     */
    private BigDecimal deductRate;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

    /**
     * 会员价
     */
    private BigDecimal customPrice;

    /**
     * 批发价
     */
    private BigDecimal bulkPrice;

    /**
     * 最低批发数量
     */
    private Integer pcs;

    /**
     * 步长
     */
    private Float partsNum;

    /**
     * 步长差异范围
     */
    private Float stepDiff;

    /**
     * 安全库存天数
     */
    private Integer safeStockDays;

    /**
     * 最小库存天数
     */
    private Integer minStockDays;

    /**
     * 最大库存天数
     */
    private Integer maxStockDays;

    /**
     * 门店调价权 0=不允许 1=允许
     */
    private Short priceRight;

    /**
     * 续订标志 0=门店续订 1=采购续订
     */
    private Short continuePurFlag;

    /**
     * 是否重点商品
     */
    private Short importantFlag;

    /**
     * 退货标识 0=合同不可退 1=合同可退 2=特批不可退
     */
    private Short returnFlag;

    /**
     * 经营类型 0=自营 1=联营 2=租赁
     */
    private Short operateFlag;

    /**
     * 订货标识 0=正常 2=暂停订货 3=清场
     */
    private Short orderFlag;

    /**
     * 物流模式  1=直送 2=直通 3=配送
     */
    private Short logistics;

    /**
     * 配送中心
     */
    private String dcshopId;

    /**
     * 法人配送中心
     */
    private String fdcShopId;

    /**
     * 引入时间
     */
    private Date inDate;

    /**
     * 引入人（审核人）
     */
    private String inOper;

    /**
     * 下架日期
     */
    private Date offDate;

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

    private Long vid;

    /**
     * 获取经营配置ID
     *
     * @return gsrid - 经营配置ID
     */
    public Long getGsrid() {
        return gsrid;
    }

    /**
     * 设置经营配置ID
     *
     * @param gsrid 经营配置ID
     */
    public void setGsrid(Long gsrid) {
        this.gsrid = gsrid;
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
     * 获取门店编码
     *
     * @return shopCode - 门店编码
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * 设置门店编码
     *
     * @param shopCode 门店编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    /**
     * 获取柜组ID
     *
     * @return saleOrgId - 柜组ID
     */
    public Long getSaleOrgId() {
        return saleOrgId;
    }

    /**
     * 设置柜组ID
     *
     * @param saleOrgId 柜组ID
     */
    public void setSaleOrgId(Long saleOrgId) {
        this.saleOrgId = saleOrgId;
    }

    /**
     * 获取柜组编码
     *
     * @return orgCode - 柜组编码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置柜组编码
     *
     * @param orgCode 柜组编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取档口ID
     *
     * @return siid - 档口ID
     */
    public Long getSiid() {
        return siid;
    }

    /**
     * 设置档口ID
     *
     * @param siid 档口ID
     */
    public void setSiid(Long siid) {
        this.siid = siid;
    }

    /**
     * 获取档口编码
     *
     * @return stallCode - 档口编码
     */
    public String getStallCode() {
        return stallCode;
    }

    /**
     * 设置档口编码
     *
     * @param stallCode 档口编码
     */
    public void setStallCode(String stallCode) {
        this.stallCode = stallCode;
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
     * 获取主营供应商编码
     *
     * @return venderCode - 主营供应商编码
     */
    public String getVenderCode() {
        return venderCode;
    }

    /**
     * 设置主营供应商编码
     *
     * @param venderCode 主营供应商编码
     */
    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    /**
     * 获取商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     *
     * @return goodsStatus - 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    /**
     * 设置商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     *
     * @param goodsStatus 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    /**
     * 获取执行价（含税）
     *
     * @return cost - 执行价（含税）
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置执行价（含税）
     *
     * @param cost 执行价（含税）
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取合同价（含税）
     *
     * @return contractCost - 合同价（含税）
     */
    public BigDecimal getContractCost() {
        return contractCost;
    }

    /**
     * 设置合同价（含税）
     *
     * @param contractCost 合同价（含税）
     */
    public void setContractCost(BigDecimal contractCost) {
        this.contractCost = contractCost;
    }

    /**
     * 获取进项税率
     *
     * @return costTaxRate - 进项税率
     */
    public Float getCostTaxRate() {
        return costTaxRate;
    }

    /**
     * 设置进项税率
     *
     * @param costTaxRate 进项税率
     */
    public void setCostTaxRate(Float costTaxRate) {
        this.costTaxRate = costTaxRate;
    }

    /**
     * 获取保底扣点
     *
     * @return deductRate - 保底扣点
     */
    public BigDecimal getDeductRate() {
        return deductRate;
    }

    /**
     * 设置保底扣点
     *
     * @param deductRate 保底扣点
     */
    public void setDeductRate(BigDecimal deductRate) {
        this.deductRate = deductRate;
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
     * 获取会员价
     *
     * @return customPrice - 会员价
     */
    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    /**
     * 设置会员价
     *
     * @param customPrice 会员价
     */
    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    /**
     * 获取批发价
     *
     * @return bulkPrice - 批发价
     */
    public BigDecimal getBulkPrice() {
        return bulkPrice;
    }

    /**
     * 设置批发价
     *
     * @param bulkPrice 批发价
     */
    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
    }

    /**
     * 获取最低批发数量
     *
     * @return pcs - 最低批发数量
     */
    public Integer getPcs() {
        return pcs;
    }

    /**
     * 设置最低批发数量
     *
     * @param pcs 最低批发数量
     */
    public void setPcs(Integer pcs) {
        this.pcs = pcs;
    }

    /**
     * 获取步长
     *
     * @return partsNum - 步长
     */
    public Float getPartsNum() {
        return partsNum;
    }

    /**
     * 设置步长
     *
     * @param partsNum 步长
     */
    public void setPartsNum(Float partsNum) {
        this.partsNum = partsNum;
    }

    /**
     * 获取步长差异范围
     *
     * @return stepDiff - 步长差异范围
     */
    public Float getStepDiff() {
        return stepDiff;
    }

    /**
     * 设置步长差异范围
     *
     * @param stepDiff 步长差异范围
     */
    public void setStepDiff(Float stepDiff) {
        this.stepDiff = stepDiff;
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
     * 获取最小库存天数
     *
     * @return minStockDays - 最小库存天数
     */
    public Integer getMinStockDays() {
        return minStockDays;
    }

    /**
     * 设置最小库存天数
     *
     * @param minStockDays 最小库存天数
     */
    public void setMinStockDays(Integer minStockDays) {
        this.minStockDays = minStockDays;
    }

    /**
     * 获取最大库存天数
     *
     * @return maxStockDays - 最大库存天数
     */
    public Integer getMaxStockDays() {
        return maxStockDays;
    }

    /**
     * 设置最大库存天数
     *
     * @param maxStockDays 最大库存天数
     */
    public void setMaxStockDays(Integer maxStockDays) {
        this.maxStockDays = maxStockDays;
    }

    /**
     * 获取门店调价权 0=不允许 1=允许
     *
     * @return priceRight - 门店调价权 0=不允许 1=允许
     */
    public Short getPriceRight() {
        return priceRight;
    }

    /**
     * 设置门店调价权 0=不允许 1=允许
     *
     * @param priceRight 门店调价权 0=不允许 1=允许
     */
    public void setPriceRight(Short priceRight) {
        this.priceRight = priceRight;
    }

    /**
     * 获取续订标志 0=门店续订 1=采购续订
     *
     * @return continuePurFlag - 续订标志 0=门店续订 1=采购续订
     */
    public Short getContinuePurFlag() {
        return continuePurFlag;
    }

    /**
     * 设置续订标志 0=门店续订 1=采购续订
     *
     * @param continuePurFlag 续订标志 0=门店续订 1=采购续订
     */
    public void setContinuePurFlag(Short continuePurFlag) {
        this.continuePurFlag = continuePurFlag;
    }

    /**
     * 获取是否重点商品
     *
     * @return importantFlag - 是否重点商品
     */
    public Short getImportantFlag() {
        return importantFlag;
    }

    /**
     * 设置是否重点商品
     *
     * @param importantFlag 是否重点商品
     */
    public void setImportantFlag(Short importantFlag) {
        this.importantFlag = importantFlag;
    }

    /**
     * 获取退货标识 0=合同不可退 1=合同可退 2=特批不可退
     *
     * @return returnFlag - 退货标识 0=合同不可退 1=合同可退 2=特批不可退
     */
    public Short getReturnFlag() {
        return returnFlag;
    }

    /**
     * 设置退货标识 0=合同不可退 1=合同可退 2=特批不可退
     *
     * @param returnFlag 退货标识 0=合同不可退 1=合同可退 2=特批不可退
     */
    public void setReturnFlag(Short returnFlag) {
        this.returnFlag = returnFlag;
    }

    /**
     * 获取经营类型 0=自营 1=联营 2=租赁
     *
     * @return operateFlag - 经营类型 0=自营 1=联营 2=租赁
     */
    public Short getOperateFlag() {
        return operateFlag;
    }

    /**
     * 设置经营类型 0=自营 1=联营 2=租赁
     *
     * @param operateFlag 经营类型 0=自营 1=联营 2=租赁
     */
    public void setOperateFlag(Short operateFlag) {
        this.operateFlag = operateFlag;
    }

    /**
     * 获取订货标识 0=正常 2=暂停订货 3=清场
     *
     * @return orderFlag - 订货标识 0=正常 2=暂停订货 3=清场
     */
    public Short getOrderFlag() {
        return orderFlag;
    }

    /**
     * 设置订货标识 0=正常 2=暂停订货 3=清场
     *
     * @param orderFlag 订货标识 0=正常 2=暂停订货 3=清场
     */
    public void setOrderFlag(Short orderFlag) {
        this.orderFlag = orderFlag;
    }

    /**
     * 获取物流模式  1=直送 2=直通 3=配送
     *
     * @return logistics - 物流模式  1=直送 2=直通 3=配送
     */
    public Short getLogistics() {
        return logistics;
    }

    /**
     * 设置物流模式  1=直送 2=直通 3=配送
     *
     * @param logistics 物流模式  1=直送 2=直通 3=配送
     */
    public void setLogistics(Short logistics) {
        this.logistics = logistics;
    }

    /**
     * 获取配送中心
     *
     * @return dcshopId - 配送中心
     */
    public String getDcshopId() {
        return dcshopId;
    }

    /**
     * 设置配送中心
     *
     * @param dcshopId 配送中心
     */
    public void setDcshopId(String dcshopId) {
        this.dcshopId = dcshopId;
    }

    /**
     * 获取法人配送中心
     *
     * @return fdcShopId - 法人配送中心
     */
    public String getFdcShopId() {
        return fdcShopId;
    }

    /**
     * 设置法人配送中心
     *
     * @param fdcShopId 法人配送中心
     */
    public void setFdcShopId(String fdcShopId) {
        this.fdcShopId = fdcShopId;
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
     * 获取引入人（审核人）
     *
     * @return inOper - 引入人（审核人）
     */
    public String getInOper() {
        return inOper;
    }

    /**
     * 设置引入人（审核人）
     *
     * @param inOper 引入人（审核人）
     */
    public void setInOper(String inOper) {
        this.inOper = inOper;
    }

    /**
     * 获取下架日期
     *
     * @return offDate - 下架日期
     */
    public Date getOffDate() {
        return offDate;
    }

    /**
     * 设置下架日期
     *
     * @param offDate 下架日期
     */
    public void setOffDate(Date offDate) {
        this.offDate = offDate;
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
     * @return vid
     */
    public Long getVid() {
        return vid;
    }

    /**
     * @param vid
     */
    public void setVid(Long vid) {
        this.vid = vid;
    }
}