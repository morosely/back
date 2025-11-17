package com.efuture.omdmain.model;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import org.springframework.data.annotation.Transient;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "goods")
public class GoodsModel {

	//配送方式：0-无需配送，1-行送，2-DC送，3-店铺送，4-自提，5-Coupon配送
	private Short deliveryFlag;
	
	//商品门店库存属性 0-单店/1-全店
	private Integer shopSheetType;

	/**
	 * 工业分类名称
	 */
	@Transient
	@ReferQuery(table="category",query="{categoryId:'$categoryId',entId:'$entId'}",set="{categoryName:'categoryName',license:'license'}",operationFlags={OperationFlag.afterQuery})
	private String categoryName;

	/**
	 * 标识商品(是否打印黄色小票0：否1：是)
	 */
	@Transient
	//@ReferQuery(table="category",query="{categoryId:'$categoryId', entId:'$entId'}",set="{license:'license'}",operationFlags={OperationFlag.afterQuery})
	private Short license;

	/**
	 * 货号
	 */
	private String artNo;

	/**
	 * 条码
	 */
	private String barNo;

    /**
     * 品牌编码
     */
    private String brandCode;
    /**
     * 品牌ID
     */
    private Long brandId;
    
    @Transient
    @ReferQuery(table="brandinfo",query="{brandId:'$brandId',entId:'$entId'}",set="{brandName:'brandName'}",operationFlags={OperationFlag.afterQuery})
    private String brandName;

	/**
	 * 能否售卖0-否/1-是（特殊原材料商品不能售卖）
	 */
	private Boolean canSaleFlag;

	/**
	 * 工业分类编码
	 */
	private String categoryCode;

	/**
	 * 工业分类ID
	 */
	private Long categoryId;

	/**
	 * 是否冷藏运输
	 */
	private Boolean coldTransFlag;

	/**
	 * 消费税率
	 */
	private Float consumpTax;

	/**
	 * 是否受管制
	 */
	private Boolean controlFlag;

	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 是否直接来源ERP 0-否/1-是
	 */
	private Boolean directFromErp;

	/**
	 * 英文全称
	 */
	private String enFname;

	/**
	 * 英文简称
	 */
	private String enSname;

	/**
	 * 零售商ID
	 */
	private Long entId;

	/**
	 * 经营公司编码
	 */
	private String erpCode;

    @Transient
    @ReferQuery(table="businesscompany",query="{erpCode:'$erpCode',entId:'$entId'}",set="{erpName:'erpName'}",operationFlags={OperationFlag.afterQuery})
    private String erpName;

	/**
	 * 是否电子称码（这个字段从哪儿来？）
	 */
	private Boolean escaleFlag;

	/**
	 * 长名称
	 */
	private String fullName;

	/**
	 * 商品编码
	 */
	private String goodsCode;

	/**
	 * 商品展示名称
	 */
	private String goodsDisplayName;

	/**
	 * 来源编码（系统切换）默认等于商品编码
	 */
	private String goodsFromCode;

	/**
	 * 商品等级
	 */
	private String goodsGrade;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 商品状态 0-不启用/1-启用
	 */
	private Short goodsStatus;

    /**
     * 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     */
    private Integer goodsType;

	/**
	 * 高
	 */
	private Integer highScale;

	/**
	 * 最高保存温度
	 */
	private Float highTemp;

	/**
	 * 进项税率
	 */
	private Float inputTax;

	/**
	 * 是否需要输入批次号销售
	 */
	private Boolean isBatch;

	/**
	 * 是否贵重商品：0=否,1=是
	 */
	private Boolean isPercious;

	/**
	 * 语言类型
	 */
	private String lang;

	/**
	 * 长
	 */
	private Integer longScale;

	/**
	 * 最低保存温度
	 */
	private Float lowTemp;

	/**
	 * 最大库存天数
	 */
	private Integer maxStockDays;

	/**
	 * 计量单位
	 */
	private String measureUnit;

	/**
	 * 会员价
	 */
	private BigDecimal memberPrice;

	/**
	 * 最低折扣率
	 */
	private Float minDiscount;

	/**
	 * 最低售价
	 */
	private BigDecimal minSalePrice;

	/**
	 * 最小库存天数
	 */
	private Integer minStockDays;

	/**
	 * 修改人
	 */
	private String modifier;

	/**
	 * 是否多单位
	 */
	private Boolean multiUnitFlag;

	/**
	 * 净重
	 */
	private Double nweight;

	/**
	 * 订货包装含量
	 */
	private Integer orderNum;

	/**
	 * 订货规格
	 */
	private String orderSpec;

	/**
	 * 订货单位
	 */
	private String orderUnit;

	/**
	 * 产地
	 */
	private String originArea;

	/**
	 * 销项税率
	 */
	private Float outputTax;

	/**
	 * 母品编码
	 */
	private String parentGoodsCode;

	/**
	 * 包装数量
	 */
	private Double partsNum;

	/**
	 * 包装单位
	 */
	private String partsUnit;

	/**
	 * 进货价
	 */
	private BigDecimal primeCost;

	/**
	 * 保质期
	 */
	private Integer qaDays;

	/**
	 * 保质期单位 0=日 1=月 2=年
	 */
	private Short qaDaysUnit;

	/**
	 * 回收费用
	 */
	private BigDecimal recycleFee;

	/**
	 * 参考售价
	 */
	private BigDecimal refPrice;

	/**
	 * 毛重
	 */
	private Double rweight;

	/**
	 * 安全库存天数
	 */
	private Integer safeStockDays;

	/**
	 * 零售价
	 */
	private BigDecimal salePrice;

	/**
	 * 销售单位
	 */
	private String saleUnit;

	/**
	 * 时令属性
	 */
	private String season;

	/**
	 * 商品ID
	 */
	@Id
	private Long sgid;

	/**
	 * 是否单品0-否/1- 是
	 */
	private Boolean singleItemFlag;

	/**
	 * 材质
	 */
	private String textture;

	/**
	 * 时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
	 */
	private Short timesFlag;

	/**
	 * 修改日期
	 */
	private Date updateDate;

	/**
	 * 水损
	 */
	private Float waterDamage;

	/**
	 * 宽
	 */
	private Integer wideScale;

	/**
	 * 销售规格
	 */
	private String saleSpec;

	/**
	 * 获取货号
	 *
	 * @return artNo - 货号
	 */
	public String getArtNo() {
		return artNo;
	}

	/**
	 * 获取条码
	 *
	 * @return barNo - 条码
	 */
	public String getBarNo() {
		return barNo;
	}

	/**
	 * 获取品牌编码
	 *
	 * @return brandCode - 品牌编码
	 */
	public String getBrandCode() {
		return brandCode;
	}

	/**
	 * 获取品牌ID
	 *
	 * @return brandId - 品牌ID
	 */
	public Long getBrandId() {
		return brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	/**
	 * 获取能否售卖0-否/1-是（特殊原材料商品不能售卖）
	 *
	 * @return canSaleFlag - 能否售卖0-否/1-是（特殊原材料商品不能售卖）
	 */
	public Boolean getCanSaleFlag() {
		return canSaleFlag;
	}

	/**
	 * 获取工业分类编码
	 *
	 * @return categoryCode - 工业分类编码
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * 获取工业分类ID
	 *
	 * @return categoryId - 工业分类ID
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * 获取是否冷藏运输
	 *
	 * @return coldTransFlag - 是否冷藏运输
	 */
	public Boolean getColdTransFlag() {
		return coldTransFlag;
	}

	/**
	 * 获取消费税率
	 *
	 * @return consumpTax - 消费税率
	 */
	public Float getConsumpTax() {
		return consumpTax;
	}

	/**
	 * 获取是否受管制
	 *
	 * @return controlFlag - 是否受管制
	 */
	public Boolean getControlFlag() {
		return controlFlag;
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
	 * 获取是否直接来源ERP 0-否/1-是
	 *
	 * @return directFromErp - 是否直接来源ERP 0-否/1-是
	 */
	public Boolean getDirectFromErp() {
		return directFromErp;
	}

	/**
	 * 获取英文全称
	 *
	 * @return enFname - 英文全称
	 */
	public String getEnFname() {
		return enFname;
	}

	/**
	 * 获取英文简称
	 *
	 * @return enSname - 英文简称
	 */
	public String getEnSname() {
		return enSname;
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

	public String getErpName() {
		return erpName;
	}

	/**
	 * 获取是否电子称码（这个字段从哪儿来？）
	 *
	 * @return escaleFlag - 是否电子称码（这个字段从哪儿来？）
	 */
	public Boolean getEscaleFlag() {
		return escaleFlag;
	}

	/**
	 * 获取长名称
	 *
	 * @return fullName - 长名称
	 */
	public String getFullName() {
		return fullName;
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
	 * 获取商品展示名称
	 *
	 * @return goodsDisplayName - 商品展示名称
	 */
	public String getGoodsDisplayName() {
		return goodsDisplayName;
	}

	/**
	 * 获取来源编码（系统切换）默认等于商品编码
	 *
	 * @return goodsFromCode - 来源编码（系统切换）默认等于商品编码
	 */
	public String getGoodsFromCode() {
		return goodsFromCode;
	}

	/**
	 * 获取商品等级
	 *
	 * @return goodsGrade - 商品等级
	 */
	public String getGoodsGrade() {
		return goodsGrade;
	}

	/**
	 * 获取商品名称
	 *
	 * @return goodsName - 商品名称
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * 获取商品状态 0-不启用/1-启用
	 *
	 * @return goodsStatus - 商品状态 0-不启用/1-启用
	 */
	public Short getGoodsStatus() {
		return goodsStatus;
	}

	/**
	 * 获取商品类型
	 * 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
	 *
	 * @return goodsType - 商品类型
	 *         0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
	 */
	public Integer getGoodsType() {
		return goodsType;
	}

	/**
	 * 获取高
	 *
	 * @return highScale - 高
	 */
	public Integer getHighScale() {
		return highScale;
	}

	/**
	 * 获取最高保存温度
	 *
	 * @return highTemp - 最高保存温度
	 */
	public Float getHighTemp() {
		return highTemp;
	}

	/**
	 * 获取进项税率
	 *
	 * @return inputTax - 进项税率
	 */
	public Float getInputTax() {
		return inputTax;
	}

	/**
	 * 获取是否需要输入批次号销售
	 *
	 * @return isBatch - 是否需要输入批次号销售
	 */
	public Boolean getIsBatch() {
		return isBatch;
	}

	/**
	 * 获取是否贵重商品：0=否,1=是
	 *
	 * @return isPercious - 是否贵重商品：0=否,1=是
	 */
	public Boolean getIsPercious() {
		return isPercious;
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
	 * 获取长
	 *
	 * @return longScale - 长
	 */
	public Integer getLongScale() {
		return longScale;
	}

	/**
	 * 获取最低保存温度
	 *
	 * @return lowTemp - 最低保存温度
	 */
	public Float getLowTemp() {
		return lowTemp;
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
	 * 获取计量单位
	 *
	 * @return measureUnit - 计量单位
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * 获取会员价
	 *
	 * @return memberPrice - 会员价
	 */
	public BigDecimal getMemberPrice() {
		return memberPrice;
	}

	/**
	 * 获取最低折扣率
	 *
	 * @return minDiscount - 最低折扣率
	 */
	public Float getMinDiscount() {
		return minDiscount;
	}

	/**
	 * 获取最低售价
	 *
	 * @return minSalePrice - 最低售价
	 */
	public BigDecimal getMinSalePrice() {
		return minSalePrice;
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
	 * 获取修改人
	 *
	 * @return modifier - 修改人
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * 获取是否多单位
	 *
	 * @return multiUnitFlag - 是否多单位
	 */
	public Boolean getMultiUnitFlag() {
		return multiUnitFlag;
	}

	/**
	 * 获取净重
	 *
	 * @return nweight - 净重
	 */
	public Double getNweight() {
		return nweight;
	}

	/**
	 * 获取订货包装含量
	 *
	 * @return orderNum - 订货包装含量
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * 获取订货规格
	 *
	 * @return orderSpec - 订货规格
	 */
	public String getOrderSpec() {
		return orderSpec;
	}

	/**
	 * 获取订货单位
	 *
	 * @return orderUnit - 订货单位
	 */
	public String getOrderUnit() {
		return orderUnit;
	}

	/**
	 * 获取产地
	 *
	 * @return originArea - 产地
	 */
	public String getOriginArea() {
		return originArea;
	}

	/**
	 * 获取销项税率
	 *
	 * @return outputTax - 销项税率
	 */
	public Float getOutputTax() {
		return outputTax;
	}

	/**
	 * 获取母品编码
	 *
	 * @return parentGoodsCode - 母品编码
	 */
	public String getParentGoodsCode() {
		return parentGoodsCode;
	}

	/**
	 * 获取包装数量
	 *
	 * @return partsNum - 包装数量
	 */
	public Double getPartsNum() {
		return partsNum;
	}

	/**
	 * 获取包装单位
	 *
	 * @return partsUnit - 包装单位
	 */
	public String getPartsUnit() {
		return partsUnit;
	}

	/**
	 * 获取进货价
	 *
	 * @return primeCost - 进货价
	 */
	public BigDecimal getPrimeCost() {
		return primeCost;
	}

	/**
	 * 获取保质期
	 *
	 * @return qaDays - 保质期
	 */
	public Integer getQaDays() {
		return qaDays;
	}

	/**
	 * 获取保质期单位 0=日 1=月 2=年
	 *
	 * @return qaDaysUnit - 保质期单位 0=日 1=月 2=年
	 */
	public Short getQaDaysUnit() {
		return qaDaysUnit;
	}

	/**
	 * 获取回收费用
	 *
	 * @return recycleFee - 回收费用
	 */
	public BigDecimal getRecycleFee() {
		return recycleFee;
	}

	/**
	 * 获取参考售价
	 *
	 * @return refPrice - 参考售价
	 */
	public BigDecimal getRefPrice() {
		return refPrice;
	}

	/**
	 * 获取毛重
	 *
	 * @return rweight - 毛重
	 */
	public Double getRweight() {
		return rweight;
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
	 * 获取零售价
	 *
	 * @return salePrice - 零售价
	 */
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	/**
	 * 获取销售单位
	 *
	 * @return saleUnit - 销售单位
	 */
	public String getSaleUnit() {
		return saleUnit;
	}

	/**
	 * 获取时令属性
	 *
	 * @return season - 时令属性
	 */
	public String getSeason() {
		return season;
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
	 * 获取是否单品0-否/1- 是
	 *
	 * @return singleItemFlag - 是否单品0-否/1- 是
	 */
	public Boolean getSingleItemFlag() {
		return singleItemFlag;
	}

	/**
	 * 获取材质
	 *
	 * @return textture - 材质
	 */
	public String getTextture() {
		return textture;
	}

	/**
	 * 获取时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
	 *
	 * @return timesFlag - 时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
	 */
	public Short getTimesFlag() {
		return timesFlag;
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
	 * 获取水损
	 *
	 * @return waterDamage - 水损
	 */
	public Float getWaterDamage() {
		return waterDamage;
	}

	/**
	 * 获取宽
	 *
	 * @return wideScale - 宽
	 */
	public Integer getWideScale() {
		return wideScale;
	}

	/**
	 * 设置货号
	 *
	 * @param artNo 货号
	 */
	public void setArtNo(String artNo) {
		this.artNo = artNo;
	}

	/**
	 * 设置条码
	 *
	 * @param barNo 条码
	 */
	public void setBarNo(String barNo) {
		this.barNo = barNo;
	}

	/**
	 * 设置品牌编码
	 *
	 * @param brandCode 品牌编码
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	/**
	 * 设置品牌ID
	 *
	 * @param brandId 品牌ID
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * 设置能否售卖0-否/1-是（特殊原材料商品不能售卖）
	 *
	 * @param canSaleFlag 能否售卖0-否/1-是（特殊原材料商品不能售卖）
	 */
	public void setCanSaleFlag(Boolean canSaleFlag) {
		this.canSaleFlag = canSaleFlag;
	}

	/**
	 * 设置工业分类编码
	 *
	 * @param categoryCode 工业分类编码
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * 设置工业分类ID
	 *
	 * @param categoryId 工业分类ID
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * 设置是否冷藏运输
	 *
	 * @param coldTransFlag 是否冷藏运输
	 */
	public void setColdTransFlag(Boolean coldTransFlag) {
		this.coldTransFlag = coldTransFlag;
	}

	/**
	 * 设置消费税率
	 *
	 * @param consumpTax 消费税率
	 */
	public void setConsumpTax(Float consumpTax) {
		this.consumpTax = consumpTax;
	}

	/**
	 * 设置是否受管制
	 *
	 * @param controlFlag 是否受管制
	 */
	public void setControlFlag(Boolean controlFlag) {
		this.controlFlag = controlFlag;
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
	 * 设置是否直接来源ERP 0-否/1-是
	 *
	 * @param directFromErp 是否直接来源ERP 0-否/1-是
	 */
	public void setDirectFromErp(Boolean directFromErp) {
		this.directFromErp = directFromErp;
	}

	/**
	 * 设置英文全称
	 *
	 * @param enFname 英文全称
	 */
	public void setEnFname(String enFname) {
		this.enFname = enFname;
	}

	/**
	 * 设置英文简称
	 *
	 * @param enSname 英文简称
	 */
	public void setEnSname(String enSname) {
		this.enSname = enSname;
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

	public void setErpName(String erpName) {
		this.erpName = erpName;
	}

	/**
	 * 设置是否电子称码（这个字段从哪儿来？）
	 *
	 * @param escaleFlag 是否电子称码（这个字段从哪儿来？）
	 */
	public void setEscaleFlag(Boolean escaleFlag) {
		this.escaleFlag = escaleFlag;
	}

	/**
	 * 设置长名称
	 *
	 * @param fullName 长名称
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	 * 设置商品展示名称
	 *
	 * @param goodsDisplayName 商品展示名称
	 */
	public void setGoodsDisplayName(String goodsDisplayName) {
		this.goodsDisplayName = goodsDisplayName;
	}

	/**
	 * 设置来源编码（系统切换）默认等于商品编码
	 *
	 * @param goodsFromCode 来源编码（系统切换）默认等于商品编码
	 */
	public void setGoodsFromCode(String goodsFromCode) {
		this.goodsFromCode = goodsFromCode;
	}

	/**
	 * 设置商品等级
	 *
	 * @param goodsGrade 商品等级
	 */
	public void setGoodsGrade(String goodsGrade) {
		this.goodsGrade = goodsGrade;
	}

	/**
	 * 设置商品名称
	 *
	 * @param goodsName 商品名称
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * 设置商品状态 0-不启用/1-启用
	 *
	 * @param goodsStatus 商品状态 0-不启用/1-启用
	 */
	public void setGoodsStatus(Short goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	/**
	 * 设置商品类型
	 * 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
	 *
	 * @param goodsType 商品类型
	 *                  0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
	 */
	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	/**
	 * 设置高
	 *
	 * @param highScale 高
	 */
	public void setHighScale(Integer highScale) {
		this.highScale = highScale;
	}

	/**
	 * 设置最高保存温度
	 *
	 * @param highTemp 最高保存温度
	 */
	public void setHighTemp(Float highTemp) {
		this.highTemp = highTemp;
	}

	/**
	 * 设置进项税率
	 *
	 * @param inputTax 进项税率
	 */
	public void setInputTax(Float inputTax) {
		this.inputTax = inputTax;
	}

	/**
	 * 设置是否需要输入批次号销售
	 *
	 * @param isBatch 是否需要输入批次号销售
	 */
	public void setIsBatch(Boolean isBatch) {
		this.isBatch = isBatch;
	}

	/**
	 * 设置是否贵重商品：0=否,1=是
	 *
	 * @param isPercious 是否贵重商品：0=否,1=是
	 */
	public void setIsPercious(Boolean isPercious) {
		this.isPercious = isPercious;
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
	 * 设置长
	 *
	 * @param longScale 长
	 */
	public void setLongScale(Integer longScale) {
		this.longScale = longScale;
	}

	/**
	 * 设置最低保存温度
	 *
	 * @param lowTemp 最低保存温度
	 */
	public void setLowTemp(Float lowTemp) {
		this.lowTemp = lowTemp;
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
	 * 设置计量单位
	 *
	 * @param measureUnit 计量单位
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	/**
	 * 设置会员价
	 *
	 * @param memberPrice 会员价
	 */
	public void setMemberPrice(BigDecimal memberPrice) {
		this.memberPrice = memberPrice;
	}

	/**
	 * 设置最低折扣率
	 *
	 * @param minDiscount 最低折扣率
	 */
	public void setMinDiscount(Float minDiscount) {
		this.minDiscount = minDiscount;
	}

	/**
	 * 设置最低售价
	 *
	 * @param minSalePrice 最低售价
	 */
	public void setMinSalePrice(BigDecimal minSalePrice) {
		this.minSalePrice = minSalePrice;
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
	 * 设置修改人
	 *
	 * @param modifier 修改人
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * 设置是否多单位
	 *
	 * @param multiUnitFlag 是否多单位
	 */
	public void setMultiUnitFlag(Boolean multiUnitFlag) {
		this.multiUnitFlag = multiUnitFlag;
	}

	/**
	 * 设置净重
	 *
	 * @param nweight 净重
	 */
	public void setNweight(Double nweight) {
		this.nweight = nweight;
	}

	/**
	 * 设置订货包装含量
	 *
	 * @param orderNum 订货包装含量
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 设置订货规格
	 *
	 * @param orderSpec 订货规格
	 */
	public void setOrderSpec(String orderSpec) {
		this.orderSpec = orderSpec;
	}

	/**
	 * 设置订货单位
	 *
	 * @param orderUnit 订货单位
	 */
	public void setOrderUnit(String orderUnit) {
		this.orderUnit = orderUnit;
	}

	/**
	 * 设置产地
	 *
	 * @param originArea 产地
	 */
	public void setOriginArea(String originArea) {
		this.originArea = originArea;
	}

	/**
	 * 设置销项税率
	 *
	 * @param outputTax 销项税率
	 */
	public void setOutputTax(Float outputTax) {
		this.outputTax = outputTax;
	}

	/**
	 * 设置母品编码
	 *
	 * @param parentGoodsCode 母品编码
	 */
	public void setParentGoodsCode(String parentGoodsCode) {
		this.parentGoodsCode = parentGoodsCode;
	}

	/**
	 * 设置包装数量
	 *
	 * @param partsNum 包装数量
	 */
	public void setPartsNum(Double partsNum) {
		this.partsNum = partsNum;
	}

	/**
	 * 设置包装单位
	 *
	 * @param partsUnit 包装单位
	 */
	public void setPartsUnit(String partsUnit) {
		this.partsUnit = partsUnit;
	}

	/**
	 * 设置进货价
	 *
	 * @param primeCost 进货价
	 */
	public void setPrimeCost(BigDecimal primeCost) {
		this.primeCost = primeCost;
	}

	/**
	 * 设置保质期
	 *
	 * @param qaDays 保质期
	 */
	public void setQaDays(Integer qaDays) {
		this.qaDays = qaDays;
	}

	/**
	 * 设置保质期单位 0=日 1=月 2=年
	 *
	 * @param qaDaysUnit 保质期单位 0=日 1=月 2=年
	 */
	public void setQaDaysUnit(Short qaDaysUnit) {
		this.qaDaysUnit = qaDaysUnit;
	}

	/**
	 * 设置回收费用
	 *
	 * @param recycleFee 回收费用
	 */
	public void setRecycleFee(BigDecimal recycleFee) {
		this.recycleFee = recycleFee;
	}

	/**
	 * 设置参考售价
	 *
	 * @param refPrice 参考售价
	 */
	public void setRefPrice(BigDecimal refPrice) {
		this.refPrice = refPrice;
	}

	/**
	 * 设置毛重
	 *
	 * @param rweight 毛重
	 */
	public void setRweight(Double rweight) {
		this.rweight = rweight;
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
	 * 设置零售价
	 *
	 * @param salePrice 零售价
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * 设置销售单位
	 *
	 * @param saleUnit 销售单位
	 */
	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	/**
	 * 设置时令属性
	 *
	 * @param season 时令属性
	 */
	public void setSeason(String season) {
		this.season = season;
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
	 * 设置是否单品0-否/1- 是
	 *
	 * @param singleItemFlag 是否单品0-否/1- 是
	 */
	public void setSingleItemFlag(Boolean singleItemFlag) {
		this.singleItemFlag = singleItemFlag;
	}

	/**
	 * 设置材质
	 *
	 * @param textture 材质
	 */
	public void setTextture(String textture) {
		this.textture = textture;
	}

	/**
	 * 设置时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
	 *
	 * @param timesFlag 时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
	 */
	public void setTimesFlag(Short timesFlag) {
		this.timesFlag = timesFlag;
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
	 * 设置水损
	 *
	 * @param waterDamage 水损
	 */
	public void setWaterDamage(Float waterDamage) {
		this.waterDamage = waterDamage;
	}

	/**
	 * 设置宽
	 *
	 * @param wideScale 宽
	 */
	public void setWideScale(Integer wideScale) {
		this.wideScale = wideScale;
	}

	public String getSaleSpec() {
		return saleSpec;
	}

	public void setSaleSpec(String saleSpec) {
		this.saleSpec = saleSpec;
	}

	public Short getLicense() {
		return license;
	}

	public void setLicense(Short license) {
		this.license = license;
	}

	public Short getDeliveryFlag() {
		return deliveryFlag;
	}

	public void setDeliveryFlag(Short deliveryFlag) {
		this.deliveryFlag = deliveryFlag;
	}

	public Integer getShopSheetType() {
		return shopSheetType;
	}

	public void setShopSheetType(Integer shopSheetType) {
		this.shopSheetType = shopSheetType;
	}
}