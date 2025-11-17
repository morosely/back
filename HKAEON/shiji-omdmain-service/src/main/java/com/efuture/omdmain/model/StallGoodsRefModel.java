package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "stallgoodsref")
public class StallGoodsRefModel {
    /**
     * 档口经营商品ID
     */
    @Id
    private Long sgrid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 组织机构ID
     */
    private Long shopId;

    /**
     * 机构编码
     */
    private String shopCode;

    /**
     * 档口ID
     */
    private Long siid;

    /**
     * 档口编号
     */
    private String stallCode;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分割商品/8-箱码商品/0-正常商品
     */
    private Integer goodsType;

    /**
     * 是否直接来源ERP 0-否/1-是
     */
    private Boolean directFromErp;

    /**
     * 是否单品0-否/1- 是
     */
    private Boolean singleItemFlag;

    /**
     * 能否售卖0-否/1-是
     */
    private Boolean canSaleFlag;

    /**
     * 母品编码
     */
    private String parentGoodsCode;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品展示名称
     */
    private String goodsDisplayName;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 条码
     */
    private String barNo;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 长名称
     */
    private String fullName;

    /**
     * 英文简称
     */
    private String enSname;

    /**
     * 英文全称
     */
    private String enFname;

    /**
     * 来源编码
     */
    private String goodsFromCode;

    /**
     * 货号
     */
    private String artNo;

    /**
     * 商品类型名称
     */
    private String goodskindName;

    /**
     * 计量单位
     */
    private String measureUnit;

    /**
     * 包装单位
     */
    private String partsUnit;

    /**
     * 包装数量（商品的步长，箱码商品的包装含量）
     */
    private Float partsNum;

    /**
     * 工业分类ID
     */
    private Long categoryId;

    /**
     * 工业分类名称
     */
    private String categoryName;

    /**
     * 销售规格
     */
    private String saleSpec;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 材质
     */
    private String textture;

    /**
     * 产地
     */
    private String originArea;

    /**
     * 销售单位
     */
    private String saleUnit;

    /**
     * 净重
     */
    private Float nweight;

    /**
     * 保质期
     */
    private Integer qaDays;

    /**
     * 水损
     */
    private BigDecimal waterDamage;

    /**
     * 最低保存温度
     */
    private Float lowTemp;

    /**
     * 最高保存温度
     */
    private Float highTemp;

    /**
     * 进项税
     */
    private BigDecimal inputTax;

    /**
     * 毛重
     */
    private Float rweight;

    /**
     * 长
     */
    private Float longScale;

    /**
     * 宽
     */
    private Float wideScale;

    /**
     * 高
     */
    private Float highScale;

    /**
     * 销项税
     */
    private BigDecimal outputTax;

    /**
     * 开始时间
     */
    private Date timeSdate;

    /**
     * 结束时间
     */
    private Date timeEdate;

    /**
     * 商品等级
     */
    private String goodsGrade;

    /**
     * 价格等级  0=未知;1=高;2=中;3=低
     */
    private Integer priceGrade;

    /**
     * 商品颜色
     */
    private String goodsColor;

    /**
     * 商品尺码
     */
    private String goodsSize;

    /**
     * 生产商号
     */
    private String manuFacture;

    /**
     * 订货规格
     */
    private String orderSpec;

    /**
     * 订货单位
     */
    private String orderUnit;

    /**
     * 质检报告交纳标志:0=否,1=是
     */
    private Integer handinFlag;

    /**
     * 质检报告送检日期
     */
    private Date handinDate;

    /**
     * 保质期单位 0=日 1=月 2=年 
     */
    private Integer qaDaysUnit;

    /**
     * 计划试销天数
     */
    private Integer planTrySaleDays;

    /**
     * 计划试销金额
     */
    private BigDecimal planTrySaleValue;

    /**
     * 是否贵重商品：0=否,1=是
     */
    private Integer isPercious;

    /**
     * 是否服务码：0=否,1=是
     */
    private Integer isService;

    /**
     * 计划试销数量
     */
    private Integer planTrySaleQty;

    /**
     * 引入人
     */
    private String inPerson;

    /**
     * 进货价
     */
    private BigDecimal primeCost;

    /**
     * 参考售价
     */
    private BigDecimal refPrice;

    /**
     * 会员价
     */
    private BigDecimal memberPrice;

    /**
     * 价格档
     */
    private Integer priceLevel;

    /**
     * 价格档名称
     */
    private String priceLevelName;

    /**
     * 最大安全库存
     */
    private Integer maxSafeStock;

    /**
     * 最小安全库存
     */
    private Integer minSafeStock;

    /**
     * 日均销量
     */
    private Integer dailySales;

    /**
     * 商品来源状态
     */
    private Integer goodsFromStatus;

    /**
     * 是否冷藏运输
     */
    private Boolean coldTransFlag;

    /**
     * 编码类型
     */
    private Integer goodsTypeId;

    /**
     * 编码类型名称
     */
    private String goodsTypeName;

    /**
     * 称重类型
     */
    private Integer weightType;

    /**
     * 称重类型名称
     */
    private String weightTypeName;

    /**
     * 行政区域ID
     */
    private Long regionId;

    /**
     * 统计分类
     */
    private Integer goodsClass;

    /**
     * 统计分类名称
     */
    private String goodsClassName;

    /**
     * 时令属性
     */
    private String season;

    /**
     * 服务码类型
     */
    private Integer serviceType;

    /**
     * 服务码类型名称
     */
    private String serviceTypeName;

    /**
     * 最低折扣率
     */
    private Float minDiscount;

    /**
     * 商品状态
     */
    private Integer goodsStatus;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 经营公司名称
     */
    private String erpName;

    /**
     * 供应商ID
     */
    private Long vid;

    /**
     * 供应商简称
     */
    private String vendersName;

    /**
     * 供应商全称
     */
    private String venderName;

    /**
     * 步长差异范围
     */
    private String stepDiff;

    /**
     * 分拣等级
     */
    private Integer sortLevel;

    /**
     * 分拣等级名称
     */
    private String sortLevelName;

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
     * 商品描述
     */
    private String goodsDesc;

    /**
     * 获取档口经营商品ID
     *
     * @return sgrid - 档口经营商品ID
     */
    public Long getSgrid() {
        return sgrid;
    }

    /**
     * 设置档口经营商品ID
     *
     * @param sgrid 档口经营商品ID
     */
    public void setSgrid(Long sgrid) {
        this.sgrid = sgrid;
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
     * 获取组织机构ID
     *
     * @return shopId - 组织机构ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置组织机构ID
     *
     * @param shopId 组织机构ID
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
     * 获取档口编号
     *
     * @return stallCode - 档口编号
     */
    public String getStallCode() {
        return stallCode;
    }

    /**
     * 设置档口编号
     *
     * @param stallCode 档口编号
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
     * 获取商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分割商品/8-箱码商品/0-正常商品
     *
     * @return goodsType - 商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分割商品/8-箱码商品/0-正常商品
     */
    public Integer getGoodsType() {
        return goodsType;
    }

    /**
     * 设置商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分割商品/8-箱码商品/0-正常商品
     *
     * @param goodsType 商品类型 1-子品/2-虚拟母品/3-组包码/4-菜谱/5-步长商品/6-生鲜商品等级/7-分割商品/8-箱码商品/0-正常商品
     */
    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
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
     * 设置是否直接来源ERP 0-否/1-是
     *
     * @param directFromErp 是否直接来源ERP 0-否/1-是
     */
    public void setDirectFromErp(Boolean directFromErp) {
        this.directFromErp = directFromErp;
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
     * 设置是否单品0-否/1- 是
     *
     * @param singleItemFlag 是否单品0-否/1- 是
     */
    public void setSingleItemFlag(Boolean singleItemFlag) {
        this.singleItemFlag = singleItemFlag;
    }

    /**
     * 获取能否售卖0-否/1-是
     *
     * @return canSaleFlag - 能否售卖0-否/1-是
     */
    public Boolean getCanSaleFlag() {
        return canSaleFlag;
    }

    /**
     * 设置能否售卖0-否/1-是
     *
     * @param canSaleFlag 能否售卖0-否/1-是
     */
    public void setCanSaleFlag(Boolean canSaleFlag) {
        this.canSaleFlag = canSaleFlag;
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
     * 设置母品编码
     *
     * @param parentGoodsCode 母品编码
     */
    public void setParentGoodsCode(String parentGoodsCode) {
        this.parentGoodsCode = parentGoodsCode;
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
     * 获取商品名称
     *
     * @return goodsName - 商品名称
     */
    public String getGoodsName() {
        return goodsName;
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
     * 获取商品展示名称
     *
     * @return goodsDisplayName - 商品展示名称
     */
    public String getGoodsDisplayName() {
        return goodsDisplayName;
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
     * 获取SKU编码
     *
     * @return skuCode - SKU编码
     */
    public String getSkuCode() {
        return skuCode;
    }

    /**
     * 设置SKU编码
     *
     * @param skuCode SKU编码
     */
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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
     * 设置条码
     *
     * @param barNo 条码
     */
    public void setBarNo(String barNo) {
        this.barNo = barNo;
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
     * 获取长名称
     *
     * @return fullName - 长名称
     */
    public String getFullName() {
        return fullName;
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
     * 获取英文简称
     *
     * @return enSname - 英文简称
     */
    public String getEnSname() {
        return enSname;
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
     * 获取英文全称
     *
     * @return enFname - 英文全称
     */
    public String getEnFname() {
        return enFname;
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
     * 获取来源编码
     *
     * @return goodsFromCode - 来源编码
     */
    public String getGoodsFromCode() {
        return goodsFromCode;
    }

    /**
     * 设置来源编码
     *
     * @param goodsFromCode 来源编码
     */
    public void setGoodsFromCode(String goodsFromCode) {
        this.goodsFromCode = goodsFromCode;
    }

    /**
     * 获取货号
     *
     * @return artNo - 货号
     */
    public String getArtNo() {
        return artNo;
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
     * 获取商品类型名称
     *
     * @return goodskindName - 商品类型名称
     */
    public String getGoodskindName() {
        return goodskindName;
    }

    /**
     * 设置商品类型名称
     *
     * @param goodskindName 商品类型名称
     */
    public void setGoodskindName(String goodskindName) {
        this.goodskindName = goodskindName;
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
     * 设置计量单位
     *
     * @param measureUnit 计量单位
     */
    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
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
     * 设置包装单位
     *
     * @param partsUnit 包装单位
     */
    public void setPartsUnit(String partsUnit) {
        this.partsUnit = partsUnit;
    }

    /**
     * 获取包装数量（商品的步长，箱码商品的包装含量）
     *
     * @return partsNum - 包装数量（商品的步长，箱码商品的包装含量）
     */
    public Float getPartsNum() {
        return partsNum;
    }

    /**
     * 设置包装数量（商品的步长，箱码商品的包装含量）
     *
     * @param partsNum 包装数量（商品的步长，箱码商品的包装含量）
     */
    public void setPartsNum(Float partsNum) {
        this.partsNum = partsNum;
    }

    /**
     * 获取工业分类ID
     *
     * @return categoryId - 工业分类ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置工业分类ID
     *
     * @param categoryId 工业分类ID
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取工业分类名称
     *
     * @return categoryName - 工业分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置工业分类名称
     *
     * @param categoryName 工业分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取销售规格
     *
     * @return saleSpec - 销售规格
     */
    public String getSaleSpec() {
        return saleSpec;
    }

    /**
     * 设置销售规格
     *
     * @param saleSpec 销售规格
     */
    public void setSaleSpec(String saleSpec) {
        this.saleSpec = saleSpec;
    }

    /**
     * 获取品牌ID
     *
     * @return brandId - 品牌ID
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * 设置品牌ID
     *
     * @param brandId 品牌ID
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * 获取品牌名称
     *
     * @return brandName - 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
     * 设置材质
     *
     * @param textture 材质
     */
    public void setTextture(String textture) {
        this.textture = textture;
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
     * 设置产地
     *
     * @param originArea 产地
     */
    public void setOriginArea(String originArea) {
        this.originArea = originArea;
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
     * 设置销售单位
     *
     * @param saleUnit 销售单位
     */
    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    /**
     * 获取净重
     *
     * @return nweight - 净重
     */
    public Float getNweight() {
        return nweight;
    }

    /**
     * 设置净重
     *
     * @param nweight 净重
     */
    public void setNweight(Float nweight) {
        this.nweight = nweight;
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
     * 设置保质期
     *
     * @param qaDays 保质期
     */
    public void setQaDays(Integer qaDays) {
        this.qaDays = qaDays;
    }

    /**
     * 获取水损
     *
     * @return waterDamage - 水损
     */
    public BigDecimal getWaterDamage() {
        return waterDamage;
    }

    /**
     * 设置水损
     *
     * @param waterDamage 水损
     */
    public void setWaterDamage(BigDecimal waterDamage) {
        this.waterDamage = waterDamage;
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
     * 设置最低保存温度
     *
     * @param lowTemp 最低保存温度
     */
    public void setLowTemp(Float lowTemp) {
        this.lowTemp = lowTemp;
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
     * 设置最高保存温度
     *
     * @param highTemp 最高保存温度
     */
    public void setHighTemp(Float highTemp) {
        this.highTemp = highTemp;
    }

    /**
     * 获取进项税
     *
     * @return inputTax - 进项税
     */
    public BigDecimal getInputTax() {
        return inputTax;
    }

    /**
     * 设置进项税
     *
     * @param inputTax 进项税
     */
    public void setInputTax(BigDecimal inputTax) {
        this.inputTax = inputTax;
    }

    /**
     * 获取毛重
     *
     * @return rweight - 毛重
     */
    public Float getRweight() {
        return rweight;
    }

    /**
     * 设置毛重
     *
     * @param rweight 毛重
     */
    public void setRweight(Float rweight) {
        this.rweight = rweight;
    }

    /**
     * 获取长
     *
     * @return longScale - 长
     */
    public Float getLongScale() {
        return longScale;
    }

    /**
     * 设置长
     *
     * @param longScale 长
     */
    public void setLongScale(Float longScale) {
        this.longScale = longScale;
    }

    /**
     * 获取宽
     *
     * @return wideScale - 宽
     */
    public Float getWideScale() {
        return wideScale;
    }

    /**
     * 设置宽
     *
     * @param wideScale 宽
     */
    public void setWideScale(Float wideScale) {
        this.wideScale = wideScale;
    }

    /**
     * 获取高
     *
     * @return highScale - 高
     */
    public Float getHighScale() {
        return highScale;
    }

    /**
     * 设置高
     *
     * @param highScale 高
     */
    public void setHighScale(Float highScale) {
        this.highScale = highScale;
    }

    /**
     * 获取销项税
     *
     * @return outputTax - 销项税
     */
    public BigDecimal getOutputTax() {
        return outputTax;
    }

    /**
     * 设置销项税
     *
     * @param outputTax 销项税
     */
    public void setOutputTax(BigDecimal outputTax) {
        this.outputTax = outputTax;
    }

    /**
     * 获取开始时间
     *
     * @return timeSdate - 开始时间
     */
    public Date getTimeSdate() {
        return timeSdate;
    }

    /**
     * 设置开始时间
     *
     * @param timeSdate 开始时间
     */
    public void setTimeSdate(Date timeSdate) {
        this.timeSdate = timeSdate;
    }

    /**
     * 获取结束时间
     *
     * @return timeEdate - 结束时间
     */
    public Date getTimeEdate() {
        return timeEdate;
    }

    /**
     * 设置结束时间
     *
     * @param timeEdate 结束时间
     */
    public void setTimeEdate(Date timeEdate) {
        this.timeEdate = timeEdate;
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
     * 设置商品等级
     *
     * @param goodsGrade 商品等级
     */
    public void setGoodsGrade(String goodsGrade) {
        this.goodsGrade = goodsGrade;
    }

    /**
     * 获取价格等级  0=未知;1=高;2=中;3=低
     *
     * @return priceGrade - 价格等级  0=未知;1=高;2=中;3=低
     */
    public Integer getPriceGrade() {
        return priceGrade;
    }

    /**
     * 设置价格等级  0=未知;1=高;2=中;3=低
     *
     * @param priceGrade 价格等级  0=未知;1=高;2=中;3=低
     */
    public void setPriceGrade(Integer priceGrade) {
        this.priceGrade = priceGrade;
    }

    /**
     * 获取商品颜色
     *
     * @return goodsColor - 商品颜色
     */
    public String getGoodsColor() {
        return goodsColor;
    }

    /**
     * 设置商品颜色
     *
     * @param goodsColor 商品颜色
     */
    public void setGoodsColor(String goodsColor) {
        this.goodsColor = goodsColor;
    }

    /**
     * 获取商品尺码
     *
     * @return goodsSize - 商品尺码
     */
    public String getGoodsSize() {
        return goodsSize;
    }

    /**
     * 设置商品尺码
     *
     * @param goodsSize 商品尺码
     */
    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    /**
     * 获取生产商号
     *
     * @return manuFacture - 生产商号
     */
    public String getManuFacture() {
        return manuFacture;
    }

    /**
     * 设置生产商号
     *
     * @param manuFacture 生产商号
     */
    public void setManuFacture(String manuFacture) {
        this.manuFacture = manuFacture;
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
     * 设置订货规格
     *
     * @param orderSpec 订货规格
     */
    public void setOrderSpec(String orderSpec) {
        this.orderSpec = orderSpec;
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
     * 设置订货单位
     *
     * @param orderUnit 订货单位
     */
    public void setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
    }

    /**
     * 获取质检报告交纳标志:0=否,1=是
     *
     * @return handinFlag - 质检报告交纳标志:0=否,1=是
     */
    public Integer getHandinFlag() {
        return handinFlag;
    }

    /**
     * 设置质检报告交纳标志:0=否,1=是
     *
     * @param handinFlag 质检报告交纳标志:0=否,1=是
     */
    public void setHandinFlag(Integer handinFlag) {
        this.handinFlag = handinFlag;
    }

    /**
     * 获取质检报告送检日期
     *
     * @return handinDate - 质检报告送检日期
     */
    public Date getHandinDate() {
        return handinDate;
    }

    /**
     * 设置质检报告送检日期
     *
     * @param handinDate 质检报告送检日期
     */
    public void setHandinDate(Date handinDate) {
        this.handinDate = handinDate;
    }

    /**
     * 获取保质期单位 0=日 1=月 2=年 
     *
     * @return qaDaysUnit - 保质期单位 0=日 1=月 2=年 
     */
    public Integer getQaDaysUnit() {
        return qaDaysUnit;
    }

    /**
     * 设置保质期单位 0=日 1=月 2=年 
     *
     * @param qaDaysUnit 保质期单位 0=日 1=月 2=年 
     */
    public void setQaDaysUnit(Integer qaDaysUnit) {
        this.qaDaysUnit = qaDaysUnit;
    }

    /**
     * 获取计划试销天数
     *
     * @return planTrySaleDays - 计划试销天数
     */
    public Integer getPlanTrySaleDays() {
        return planTrySaleDays;
    }

    /**
     * 设置计划试销天数
     *
     * @param planTrySaleDays 计划试销天数
     */
    public void setPlanTrySaleDays(Integer planTrySaleDays) {
        this.planTrySaleDays = planTrySaleDays;
    }

    /**
     * 获取计划试销金额
     *
     * @return planTrySaleValue - 计划试销金额
     */
    public BigDecimal getPlanTrySaleValue() {
        return planTrySaleValue;
    }

    /**
     * 设置计划试销金额
     *
     * @param planTrySaleValue 计划试销金额
     */
    public void setPlanTrySaleValue(BigDecimal planTrySaleValue) {
        this.planTrySaleValue = planTrySaleValue;
    }

    /**
     * 获取是否贵重商品：0=否,1=是
     *
     * @return isPercious - 是否贵重商品：0=否,1=是
     */
    public Integer getIsPercious() {
        return isPercious;
    }

    /**
     * 设置是否贵重商品：0=否,1=是
     *
     * @param isPercious 是否贵重商品：0=否,1=是
     */
    public void setIsPercious(Integer isPercious) {
        this.isPercious = isPercious;
    }

    /**
     * 获取是否服务码：0=否,1=是
     *
     * @return isService - 是否服务码：0=否,1=是
     */
    public Integer getIsService() {
        return isService;
    }

    /**
     * 设置是否服务码：0=否,1=是
     *
     * @param isService 是否服务码：0=否,1=是
     */
    public void setIsService(Integer isService) {
        this.isService = isService;
    }

    /**
     * 获取计划试销数量
     *
     * @return planTrySaleQty - 计划试销数量
     */
    public Integer getPlanTrySaleQty() {
        return planTrySaleQty;
    }

    /**
     * 设置计划试销数量
     *
     * @param planTrySaleQty 计划试销数量
     */
    public void setPlanTrySaleQty(Integer planTrySaleQty) {
        this.planTrySaleQty = planTrySaleQty;
    }

    /**
     * 获取引入人
     *
     * @return inPerson - 引入人
     */
    public String getInPerson() {
        return inPerson;
    }

    /**
     * 设置引入人
     *
     * @param inPerson 引入人
     */
    public void setInPerson(String inPerson) {
        this.inPerson = inPerson;
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
     * 设置进货价
     *
     * @param primeCost 进货价
     */
    public void setPrimeCost(BigDecimal primeCost) {
        this.primeCost = primeCost;
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
     * 设置参考售价
     *
     * @param refPrice 参考售价
     */
    public void setRefPrice(BigDecimal refPrice) {
        this.refPrice = refPrice;
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
     * 设置会员价
     *
     * @param memberPrice 会员价
     */
    public void setMemberPrice(BigDecimal memberPrice) {
        this.memberPrice = memberPrice;
    }

    /**
     * 获取价格档
     *
     * @return priceLevel - 价格档
     */
    public Integer getPriceLevel() {
        return priceLevel;
    }

    /**
     * 设置价格档
     *
     * @param priceLevel 价格档
     */
    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    /**
     * 获取价格档名称
     *
     * @return priceLevelName - 价格档名称
     */
    public String getPriceLevelName() {
        return priceLevelName;
    }

    /**
     * 设置价格档名称
     *
     * @param priceLevelName 价格档名称
     */
    public void setPriceLevelName(String priceLevelName) {
        this.priceLevelName = priceLevelName;
    }

    /**
     * 获取最大安全库存
     *
     * @return maxSafeStock - 最大安全库存
     */
    public Integer getMaxSafeStock() {
        return maxSafeStock;
    }

    /**
     * 设置最大安全库存
     *
     * @param maxSafeStock 最大安全库存
     */
    public void setMaxSafeStock(Integer maxSafeStock) {
        this.maxSafeStock = maxSafeStock;
    }

    /**
     * 获取最小安全库存
     *
     * @return minSafeStock - 最小安全库存
     */
    public Integer getMinSafeStock() {
        return minSafeStock;
    }

    /**
     * 设置最小安全库存
     *
     * @param minSafeStock 最小安全库存
     */
    public void setMinSafeStock(Integer minSafeStock) {
        this.minSafeStock = minSafeStock;
    }

    /**
     * 获取日均销量
     *
     * @return dailySales - 日均销量
     */
    public Integer getDailySales() {
        return dailySales;
    }

    /**
     * 设置日均销量
     *
     * @param dailySales 日均销量
     */
    public void setDailySales(Integer dailySales) {
        this.dailySales = dailySales;
    }

    /**
     * 获取商品来源状态
     *
     * @return goodsFromStatus - 商品来源状态
     */
    public Integer getGoodsFromStatus() {
        return goodsFromStatus;
    }

    /**
     * 设置商品来源状态
     *
     * @param goodsFromStatus 商品来源状态
     */
    public void setGoodsFromStatus(Integer goodsFromStatus) {
        this.goodsFromStatus = goodsFromStatus;
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
     * 设置是否冷藏运输
     *
     * @param coldTransFlag 是否冷藏运输
     */
    public void setColdTransFlag(Boolean coldTransFlag) {
        this.coldTransFlag = coldTransFlag;
    }

    /**
     * 获取编码类型
     *
     * @return goodsTypeId - 编码类型
     */
    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    /**
     * 设置编码类型
     *
     * @param goodsTypeId 编码类型
     */
    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    /**
     * 获取编码类型名称
     *
     * @return goodsTypeName - 编码类型名称
     */
    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    /**
     * 设置编码类型名称
     *
     * @param goodsTypeName 编码类型名称
     */
    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    /**
     * 获取称重类型
     *
     * @return weightType - 称重类型
     */
    public Integer getWeightType() {
        return weightType;
    }

    /**
     * 设置称重类型
     *
     * @param weightType 称重类型
     */
    public void setWeightType(Integer weightType) {
        this.weightType = weightType;
    }

    /**
     * 获取称重类型名称
     *
     * @return weightTypeName - 称重类型名称
     */
    public String getWeightTypeName() {
        return weightTypeName;
    }

    /**
     * 设置称重类型名称
     *
     * @param weightTypeName 称重类型名称
     */
    public void setWeightTypeName(String weightTypeName) {
        this.weightTypeName = weightTypeName;
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
     * 设置行政区域ID
     *
     * @param regionId 行政区域ID
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取统计分类
     *
     * @return goodsClass - 统计分类
     */
    public Integer getGoodsClass() {
        return goodsClass;
    }

    /**
     * 设置统计分类
     *
     * @param goodsClass 统计分类
     */
    public void setGoodsClass(Integer goodsClass) {
        this.goodsClass = goodsClass;
    }

    /**
     * 获取统计分类名称
     *
     * @return goodsClassName - 统计分类名称
     */
    public String getGoodsClassName() {
        return goodsClassName;
    }

    /**
     * 设置统计分类名称
     *
     * @param goodsClassName 统计分类名称
     */
    public void setGoodsClassName(String goodsClassName) {
        this.goodsClassName = goodsClassName;
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
     * 设置时令属性
     *
     * @param season 时令属性
     */
    public void setSeason(String season) {
        this.season = season;
    }

    /**
     * 获取服务码类型
     *
     * @return serviceType - 服务码类型
     */
    public Integer getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务码类型
     *
     * @param serviceType 服务码类型
     */
    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * 获取服务码类型名称
     *
     * @return serviceTypeName - 服务码类型名称
     */
    public String getServiceTypeName() {
        return serviceTypeName;
    }

    /**
     * 设置服务码类型名称
     *
     * @param serviceTypeName 服务码类型名称
     */
    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
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
     * 设置最低折扣率
     *
     * @param minDiscount 最低折扣率
     */
    public void setMinDiscount(Float minDiscount) {
        this.minDiscount = minDiscount;
    }

    /**
     * 获取商品状态
     *
     * @return goodsStatus - 商品状态
     */
    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    /**
     * 设置商品状态
     *
     * @param goodsStatus 商品状态
     */
    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
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
     * 获取经营公司名称
     *
     * @return erpName - 经营公司名称
     */
    public String getErpName() {
        return erpName;
    }

    /**
     * 设置经营公司名称
     *
     * @param erpName 经营公司名称
     */
    public void setErpName(String erpName) {
        this.erpName = erpName;
    }

    /**
     * 获取供应商ID
     *
     * @return vid - 供应商ID
     */
    public Long getVid() {
        return vid;
    }

    /**
     * 设置供应商ID
     *
     * @param vid 供应商ID
     */
    public void setVid(Long vid) {
        this.vid = vid;
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
     * 获取步长差异范围
     *
     * @return stepDiff - 步长差异范围
     */
    public String getStepDiff() {
        return stepDiff;
    }

    /**
     * 设置步长差异范围
     *
     * @param stepDiff 步长差异范围
     */
    public void setStepDiff(String stepDiff) {
        this.stepDiff = stepDiff;
    }

    /**
     * 获取分拣等级
     *
     * @return sortLevel - 分拣等级
     */
    public Integer getSortLevel() {
        return sortLevel;
    }

    /**
     * 设置分拣等级
     *
     * @param sortLevel 分拣等级
     */
    public void setSortLevel(Integer sortLevel) {
        this.sortLevel = sortLevel;
    }

    /**
     * 获取分拣等级名称
     *
     * @return sortLevelName - 分拣等级名称
     */
    public String getSortLevelName() {
        return sortLevelName;
    }

    /**
     * 设置分拣等级名称
     *
     * @param sortLevelName 分拣等级名称
     */
    public void setSortLevelName(String sortLevelName) {
        this.sortLevelName = sortLevelName;
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
     * 获取商品描述
     *
     * @return goodsDesc - 商品描述
     */
    public String getGoodsDesc() {
        return goodsDesc;
    }

    /**
     * 设置商品描述
     *
     * @param goodsDesc 商品描述
     */
    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }
}