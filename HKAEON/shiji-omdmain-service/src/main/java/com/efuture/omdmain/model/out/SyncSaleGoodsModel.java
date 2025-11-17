package com.efuture.omdmain.model.out;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import org.springframework.data.annotation.Transient;

@Table(name = "salegoods")
public class SyncSaleGoodsModel {
	private String status4Goods; //商品表状态（同步数据做判断用）
	private String status4Ref; //经营配置商品状态（同步数据做判断用）
	private String status4More; //多条码表条码状态（同步数据做判断用）
	
	public String getStatus4Goods() {
		return status4Goods;
	}

	public void setStatus4Goods(String status4Goods) {
		this.status4Goods = status4Goods;
	}

	public String getStatus4Ref() {
		return status4Ref;
	}

	public void setStatus4Ref(String status4Ref) {
		this.status4Ref = status4Ref;
	}

	public String getStatus4More() {
		return status4More;
	}

	public void setStatus4More(String status4More) {
		this.status4More = status4More;
	}

	/**
     * 货号
     */
    private String artNo;

	/**
	 * 1:主条码 0：从条码
	 */
	private Short barCodeType;

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

    /**
     * 批发价
     */
    private BigDecimal bulkPrice;

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

    /**
     * 是否电子称码
     */
    private Boolean escaleFlag;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品展示名称
     */
    private String goodsDisplayName;

    /**
     * 来源编码
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
     * 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    private Short goodsStatus;

    /**
     * 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品/14-赠品
     */
    private Short goodsType;

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
     * 是否主条码
     */
    private Boolean mainBarcodeFlag;

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
     * 是否多单位
     */
    private Boolean multiUnitFlag;

    /**
     * 柜组编码
     */
    private String orgCode;

    /**
     * 销项税率
     */
    private Float outputTax;

    /**
     * 母品编码
     */
    private String parentGoodsCode;

    /**
     * 包装数量（商品的步长）
     */
    private Double partsNum;

    /**
     * 包装单位
     */
    private String partsUnit;

    /**
     * 最低批发数量
     */
    private Integer pcs;

    /**
     * 四舍五入方式(Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元
     */
    private String prcutMode;

    /**
     * 进货价
     */
    private BigDecimal primeCost;

    /**
     * 是否打印副单 0:否,1:是
     */
    private Boolean prtDuplFlag;

    /**
     * 母品ID
     */
    private Long psgid;

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
     * 柜组ID
     */
    private Long saleOrgId;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

    /**
     * 销售规格
     */
    private String saleSpec;

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
    private Long sgid;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 档口ID
     */
    private Long siid;

    /**
     * 是否母品0-否/1- 是
     */
    private Boolean singleItemFlag;

    /**
     * 可售商品ID
     */
    @Id
    private Long ssgid;

    /**
     * 档口编码
     */
    private String stallCode;

    /**
     * 步长差异范围
     */
    private Float stepDiff;

    /**
     * 时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
     */
    private Short timesFlag;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 主营供应商编码
     */
    private String venderCode;

    /**
     * 供应商ID
     */
    private Long vid;

    /**
     * 获取货号
     *
     * @return artNo - 货号
     */
    public String getArtNo() {
        return artNo;
    }

    public Short getBarCodeType() {
		return barCodeType;
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

    /**
     * 获取批发价
     *
     * @return bulkPrice - 批发价
     */
    public BigDecimal getBulkPrice() {
        return bulkPrice;
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

    /**
     * 获取是否电子称码
     *
     * @return escaleFlag - 是否电子称码
     */
    public Boolean getEscaleFlag() {
        return escaleFlag;
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
     * 获取来源编码
     *
     * @return goodsFromCode - 来源编码
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
     * 获取商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     *
     * @return goodsStatus - 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    public Short getGoodsStatus() {
        return goodsStatus;
    }

    /**
     * 获取商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品/14-赠品
     *
     * @return goodsType - 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品/14-赠品
     */
    public Short getGoodsType() {
        return goodsType;
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
     * 获取是否主条码
     *
     * @return mainBarcodeFlag - 是否主条码
     */
    public Boolean getMainBarcodeFlag() {
        return mainBarcodeFlag;
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
     * 获取是否多单位
     *
     * @return multiUnitFlag - 是否多单位
     */
    public Boolean getMultiUnitFlag() {
        return multiUnitFlag;
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
     * 获取包装数量（商品的步长）
     *
     * @return partsNum - 包装数量（商品的步长）
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
     * 获取最低批发数量
     *
     * @return pcs - 最低批发数量
     */
    public Integer getPcs() {
        return pcs;
    }

    /**
     * 获取四舍五入方式(Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元
     *
     * @return prcutMode - 四舍五入方式(Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元
     */
    public String getPrcutMode() {
        return prcutMode;
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
     * 获取是否打印副单 0:否,1:是
     *
     * @return prtDuplFlag - 是否打印副单 0:否,1:是
     */
    public Boolean getPrtDuplFlag() {
        return prtDuplFlag;
    }

    /**
     * 获取母品ID
     *
     * @return psgid - 母品ID
     */
    public Long getPsgid() {
        return psgid;
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
     * 获取柜组ID
     *
     * @return saleOrgId - 柜组ID
     */
    public Long getSaleOrgId() {
        return saleOrgId;
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
     * 获取销售规格
     *
     * @return saleSpec - 销售规格
     */
    public String getSaleSpec() {
        return saleSpec;
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
     * 获取门店编码
     *
     * @return shopCode - 门店编码
     */
    public String getShopCode() {
        return shopCode;
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
     * 获取档口ID
     *
     * @return siid - 档口ID
     */
    public Long getSiid() {
        return siid;
    }

    /**
     * 获取是否母品0-否/1- 是
     *
     * @return singleItemFlag - 是否母品0-否/1- 是
     */
    public Boolean getSingleItemFlag() {
        return singleItemFlag;
    }

    /**
     * 获取可售商品ID
     *
     * @return ssgid - 可售商品ID
     */
    public Long getSsgid() {
        return ssgid;
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
     * 获取步长差异范围
     *
     * @return stepDiff - 步长差异范围
     */
    public Float getStepDiff() {
        return stepDiff;
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
     * 获取主营供应商编码
     *
     * @return venderCode - 主营供应商编码
     */
    public String getVenderCode() {
        return venderCode;
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
     * 设置货号
     *
     * @param artNo 货号
     */
    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }

    public void setBarCodeType(Short barCodeType) {
		this.barCodeType = barCodeType;
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

    /**
     * 设置批发价
     *
     * @param bulkPrice 批发价
     */
    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
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

    /**
     * 设置是否电子称码
     *
     * @param escaleFlag 是否电子称码
     */
    public void setEscaleFlag(Boolean escaleFlag) {
        this.escaleFlag = escaleFlag;
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
     * 设置来源编码
     *
     * @param goodsFromCode 来源编码
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
     * 设置商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     *
     * @param goodsStatus 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    public void setGoodsStatus(Short goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    /**
     * 设置商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品/14-赠品
     *
     * @param goodsType 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品/14-赠品
     */
    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
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
     * 设置是否主条码
     *
     * @param mainBarcodeFlag 是否主条码
     */
    public void setMainBarcodeFlag(Boolean mainBarcodeFlag) {
        this.mainBarcodeFlag = mainBarcodeFlag;
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
     * 设置是否多单位
     *
     * @param multiUnitFlag 是否多单位
     */
    public void setMultiUnitFlag(Boolean multiUnitFlag) {
        this.multiUnitFlag = multiUnitFlag;
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
     * 设置包装数量（商品的步长）
     *
     * @param partsNum 包装数量（商品的步长）
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
     * 设置最低批发数量
     *
     * @param pcs 最低批发数量
     */
    public void setPcs(Integer pcs) {
        this.pcs = pcs;
    }

    /**
     * 设置四舍五入方式(Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元
     *
     * @param prcutMode 四舍五入方式(Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元
     */
    public void setPrcutMode(String prcutMode) {
        this.prcutMode = prcutMode;
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
     * 设置是否打印副单 0:否,1:是
     *
     * @param prtDuplFlag 是否打印副单 0:否,1:是
     */
    public void setPrtDuplFlag(Boolean prtDuplFlag) {
        this.prtDuplFlag = prtDuplFlag;
    }

    /**
     * 设置母品ID
     *
     * @param psgid 母品ID
     */
    public void setPsgid(Long psgid) {
        this.psgid = psgid;
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
     * 设置柜组ID
     *
     * @param saleOrgId 柜组ID
     */
    public void setSaleOrgId(Long saleOrgId) {
        this.saleOrgId = saleOrgId;
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
     * 设置销售规格
     *
     * @param saleSpec 销售规格
     */
    public void setSaleSpec(String saleSpec) {
        this.saleSpec = saleSpec;
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
     * 设置门店编码
     *
     * @param shopCode 门店编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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
     * 设置档口ID
     *
     * @param siid 档口ID
     */
    public void setSiid(Long siid) {
        this.siid = siid;
    }

    /**
     * 设置是否母品0-否/1- 是
     *
     * @param singleItemFlag 是否母品0-否/1- 是
     */
    public void setSingleItemFlag(Boolean singleItemFlag) {
        this.singleItemFlag = singleItemFlag;
    }

    /**
     * 设置可售商品ID
     *
     * @param ssgid 可售商品ID
     */
    public void setSsgid(Long ssgid) {
        this.ssgid = ssgid;
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
     * 设置步长差异范围
     *
     * @param stepDiff 步长差异范围
     */
    public void setStepDiff(Float stepDiff) {
        this.stepDiff = stepDiff;
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
     * 设置主营供应商编码
     *
     * @param venderCode 主营供应商编码
     */
    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    /**
     * 设置供应商ID
     *
     * @param vid 供应商ID
     */
    public void setVid(Long vid) {
        this.vid = vid;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artNo == null) ? 0 : artNo.hashCode());
		result = prime * result + ((barCodeType == null) ? 0 : barCodeType.hashCode());
		result = prime * result + ((barNo == null) ? 0 : barNo.hashCode());
		result = prime * result + ((brandCode == null) ? 0 : brandCode.hashCode());
		result = prime * result + ((brandId == null) ? 0 : brandId.hashCode());
		result = prime * result + ((bulkPrice == null) ? 0 : bulkPrice.hashCode());
		result = prime * result + ((categoryCode == null) ? 0 : categoryCode.hashCode());
		result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
		result = prime * result + ((coldTransFlag == null) ? 0 : coldTransFlag.hashCode());
		result = prime * result + ((consumpTax == null) ? 0 : consumpTax.hashCode());
		result = prime * result + ((controlFlag == null) ? 0 : controlFlag.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((directFromErp == null) ? 0 : directFromErp.hashCode());
		result = prime * result + ((enFname == null) ? 0 : enFname.hashCode());
		result = prime * result + ((enSname == null) ? 0 : enSname.hashCode());
		result = prime * result + ((entId == null) ? 0 : entId.hashCode());
		result = prime * result + ((erpCode == null) ? 0 : erpCode.hashCode());
		result = prime * result + ((escaleFlag == null) ? 0 : escaleFlag.hashCode());
		result = prime * result + ((goodsCode == null) ? 0 : goodsCode.hashCode());
		result = prime * result + ((goodsDisplayName == null) ? 0 : goodsDisplayName.hashCode());
		result = prime * result + ((goodsFromCode == null) ? 0 : goodsFromCode.hashCode());
		result = prime * result + ((goodsGrade == null) ? 0 : goodsGrade.hashCode());
		result = prime * result + ((goodsName == null) ? 0 : goodsName.hashCode());
		result = prime * result + ((goodsStatus == null) ? 0 : goodsStatus.hashCode());
		result = prime * result + ((goodsType == null) ? 0 : goodsType.hashCode());
		result = prime * result + ((isBatch == null) ? 0 : isBatch.hashCode());
		result = prime * result + ((isPercious == null) ? 0 : isPercious.hashCode());
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		result = prime * result + ((mainBarcodeFlag == null) ? 0 : mainBarcodeFlag.hashCode());
		result = prime * result + ((memberPrice == null) ? 0 : memberPrice.hashCode());
		result = prime * result + ((minDiscount == null) ? 0 : minDiscount.hashCode());
		result = prime * result + ((minSalePrice == null) ? 0 : minSalePrice.hashCode());
		result = prime * result + ((multiUnitFlag == null) ? 0 : multiUnitFlag.hashCode());
		result = prime * result + ((orgCode == null) ? 0 : orgCode.hashCode());
		result = prime * result + ((outputTax == null) ? 0 : outputTax.hashCode());
		result = prime * result + ((parentGoodsCode == null) ? 0 : parentGoodsCode.hashCode());
		result = prime * result + ((partsNum == null) ? 0 : partsNum.hashCode());
		result = prime * result + ((partsUnit == null) ? 0 : partsUnit.hashCode());
		result = prime * result + ((pcs == null) ? 0 : pcs.hashCode());
		result = prime * result + ((prcutMode == null) ? 0 : prcutMode.hashCode());
		result = prime * result + ((primeCost == null) ? 0 : primeCost.hashCode());
		result = prime * result + ((prtDuplFlag == null) ? 0 : prtDuplFlag.hashCode());
		result = prime * result + ((psgid == null) ? 0 : psgid.hashCode());
		result = prime * result + ((recycleFee == null) ? 0 : recycleFee.hashCode());
		result = prime * result + ((refPrice == null) ? 0 : refPrice.hashCode());
		result = prime * result + ((rweight == null) ? 0 : rweight.hashCode());
		result = prime * result + ((saleOrgId == null) ? 0 : saleOrgId.hashCode());
		result = prime * result + ((salePrice == null) ? 0 : salePrice.hashCode());
		result = prime * result + ((saleSpec == null) ? 0 : saleSpec.hashCode());
		result = prime * result + ((saleUnit == null) ? 0 : saleUnit.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result + ((sgid == null) ? 0 : sgid.hashCode());
		result = prime * result + ((shopCode == null) ? 0 : shopCode.hashCode());
		result = prime * result + ((shopId == null) ? 0 : shopId.hashCode());
		result = prime * result + ((siid == null) ? 0 : siid.hashCode());
		result = prime * result + ((singleItemFlag == null) ? 0 : singleItemFlag.hashCode());
		result = prime * result + ((ssgid == null) ? 0 : ssgid.hashCode());
		result = prime * result + ((stallCode == null) ? 0 : stallCode.hashCode());
		result = prime * result + ((status4Goods == null) ? 0 : status4Goods.hashCode());
		result = prime * result + ((status4More == null) ? 0 : status4More.hashCode());
		result = prime * result + ((status4Ref == null) ? 0 : status4Ref.hashCode());
		result = prime * result + ((stepDiff == null) ? 0 : stepDiff.hashCode());
		result = prime * result + ((timesFlag == null) ? 0 : timesFlag.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((venderCode == null) ? 0 : venderCode.hashCode());
		result = prime * result + ((vid == null) ? 0 : vid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SyncSaleGoodsModel other = (SyncSaleGoodsModel) obj;
		if (artNo == null) {
			if (other.artNo != null)
				return false;
		} else if (!artNo.equals(other.artNo))
			return false;
		if (barCodeType == null) {
			if (other.barCodeType != null)
				return false;
		} else if (!barCodeType.equals(other.barCodeType))
			return false;
		if (barNo == null) {
			if (other.barNo != null)
				return false;
		} else if (!barNo.equals(other.barNo))
			return false;
		if (brandCode == null) {
			if (other.brandCode != null)
				return false;
		} else if (!brandCode.equals(other.brandCode))
			return false;
		if (brandId == null) {
			if (other.brandId != null)
				return false;
		} else if (!brandId.equals(other.brandId))
			return false;
		if (bulkPrice == null) {
			if (other.bulkPrice != null)
				return false;
		} else if (!bulkPrice.equals(other.bulkPrice))
			return false;
		if (categoryCode == null) {
			if (other.categoryCode != null)
				return false;
		} else if (!categoryCode.equals(other.categoryCode))
			return false;
		if (categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!categoryId.equals(other.categoryId))
			return false;
		if (coldTransFlag == null) {
			if (other.coldTransFlag != null)
				return false;
		} else if (!coldTransFlag.equals(other.coldTransFlag))
			return false;
		if (consumpTax == null) {
			if (other.consumpTax != null)
				return false;
		} else if (!consumpTax.equals(other.consumpTax))
			return false;
		if (controlFlag == null) {
			if (other.controlFlag != null)
				return false;
		} else if (!controlFlag.equals(other.controlFlag))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (directFromErp == null) {
			if (other.directFromErp != null)
				return false;
		} else if (!directFromErp.equals(other.directFromErp))
			return false;
		if (enFname == null) {
			if (other.enFname != null)
				return false;
		} else if (!enFname.equals(other.enFname))
			return false;
		if (enSname == null) {
			if (other.enSname != null)
				return false;
		} else if (!enSname.equals(other.enSname))
			return false;
		if (entId == null) {
			if (other.entId != null)
				return false;
		} else if (!entId.equals(other.entId))
			return false;
		if (erpCode == null) {
			if (other.erpCode != null)
				return false;
		} else if (!erpCode.equals(other.erpCode))
			return false;
		if (escaleFlag == null) {
			if (other.escaleFlag != null)
				return false;
		} else if (!escaleFlag.equals(other.escaleFlag))
			return false;
		if (goodsCode == null) {
			if (other.goodsCode != null)
				return false;
		} else if (!goodsCode.equals(other.goodsCode))
			return false;
		if (goodsDisplayName == null) {
			if (other.goodsDisplayName != null)
				return false;
		} else if (!goodsDisplayName.equals(other.goodsDisplayName))
			return false;
		if (goodsFromCode == null) {
			if (other.goodsFromCode != null)
				return false;
		} else if (!goodsFromCode.equals(other.goodsFromCode))
			return false;
		if (goodsGrade == null) {
			if (other.goodsGrade != null)
				return false;
		} else if (!goodsGrade.equals(other.goodsGrade))
			return false;
		if (goodsName == null) {
			if (other.goodsName != null)
				return false;
		} else if (!goodsName.equals(other.goodsName))
			return false;
		if (goodsStatus == null) {
			if (other.goodsStatus != null)
				return false;
		} else if (!goodsStatus.equals(other.goodsStatus))
			return false;
		if (goodsType == null) {
			if (other.goodsType != null)
				return false;
		} else if (!goodsType.equals(other.goodsType))
			return false;
		if (isBatch == null) {
			if (other.isBatch != null)
				return false;
		} else if (!isBatch.equals(other.isBatch))
			return false;
		if (isPercious == null) {
			if (other.isPercious != null)
				return false;
		} else if (!isPercious.equals(other.isPercious))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (mainBarcodeFlag == null) {
			if (other.mainBarcodeFlag != null)
				return false;
		} else if (!mainBarcodeFlag.equals(other.mainBarcodeFlag))
			return false;
		if (memberPrice == null) {
			if (other.memberPrice != null)
				return false;
		} else if (!memberPrice.equals(other.memberPrice))
			return false;
		if (minDiscount == null) {
			if (other.minDiscount != null)
				return false;
		} else if (!minDiscount.equals(other.minDiscount))
			return false;
		if (minSalePrice == null) {
			if (other.minSalePrice != null)
				return false;
		} else if (!minSalePrice.equals(other.minSalePrice))
			return false;
		if (multiUnitFlag == null) {
			if (other.multiUnitFlag != null)
				return false;
		} else if (!multiUnitFlag.equals(other.multiUnitFlag))
			return false;
		if (orgCode == null) {
			if (other.orgCode != null)
				return false;
		} else if (!orgCode.equals(other.orgCode))
			return false;
		if (outputTax == null) {
			if (other.outputTax != null)
				return false;
		} else if (!outputTax.equals(other.outputTax))
			return false;
		if (parentGoodsCode == null) {
			if (other.parentGoodsCode != null)
				return false;
		} else if (!parentGoodsCode.equals(other.parentGoodsCode))
			return false;
		if (partsNum == null) {
			if (other.partsNum != null)
				return false;
		} else if (!partsNum.equals(other.partsNum))
			return false;
		if (partsUnit == null) {
			if (other.partsUnit != null)
				return false;
		} else if (!partsUnit.equals(other.partsUnit))
			return false;
		if (pcs == null) {
			if (other.pcs != null)
				return false;
		} else if (!pcs.equals(other.pcs))
			return false;
		if (prcutMode == null) {
			if (other.prcutMode != null)
				return false;
		} else if (!prcutMode.equals(other.prcutMode))
			return false;
		if (primeCost == null) {
			if (other.primeCost != null)
				return false;
		} else if (!primeCost.equals(other.primeCost))
			return false;
		if (prtDuplFlag == null) {
			if (other.prtDuplFlag != null)
				return false;
		} else if (!prtDuplFlag.equals(other.prtDuplFlag))
			return false;
		if (psgid == null) {
			if (other.psgid != null)
				return false;
		} else if (!psgid.equals(other.psgid))
			return false;
		if (recycleFee == null) {
			if (other.recycleFee != null)
				return false;
		} else if (!recycleFee.equals(other.recycleFee))
			return false;
		if (refPrice == null) {
			if (other.refPrice != null)
				return false;
		} else if (!refPrice.equals(other.refPrice))
			return false;
		if (rweight == null) {
			if (other.rweight != null)
				return false;
		} else if (!rweight.equals(other.rweight))
			return false;
		if (saleOrgId == null) {
			if (other.saleOrgId != null)
				return false;
		} else if (!saleOrgId.equals(other.saleOrgId))
			return false;
		if (salePrice == null) {
			if (other.salePrice != null)
				return false;
		} else if (!salePrice.equals(other.salePrice))
			return false;
		if (saleSpec == null) {
			if (other.saleSpec != null)
				return false;
		} else if (!saleSpec.equals(other.saleSpec))
			return false;
		if (saleUnit == null) {
			if (other.saleUnit != null)
				return false;
		} else if (!saleUnit.equals(other.saleUnit))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (sgid == null) {
			if (other.sgid != null)
				return false;
		} else if (!sgid.equals(other.sgid))
			return false;
		if (shopCode == null) {
			if (other.shopCode != null)
				return false;
		} else if (!shopCode.equals(other.shopCode))
			return false;
		if (shopId == null) {
			if (other.shopId != null)
				return false;
		} else if (!shopId.equals(other.shopId))
			return false;
		if (siid == null) {
			if (other.siid != null)
				return false;
		} else if (!siid.equals(other.siid))
			return false;
		if (singleItemFlag == null) {
			if (other.singleItemFlag != null)
				return false;
		} else if (!singleItemFlag.equals(other.singleItemFlag))
			return false;
		if (ssgid == null) {
			if (other.ssgid != null)
				return false;
		} else if (!ssgid.equals(other.ssgid))
			return false;
		if (stallCode == null) {
			if (other.stallCode != null)
				return false;
		} else if (!stallCode.equals(other.stallCode))
			return false;
		if (status4Goods == null) {
			if (other.status4Goods != null)
				return false;
		} else if (!status4Goods.equals(other.status4Goods))
			return false;
		if (status4More == null) {
			if (other.status4More != null)
				return false;
		} else if (!status4More.equals(other.status4More))
			return false;
		if (status4Ref == null) {
			if (other.status4Ref != null)
				return false;
		} else if (!status4Ref.equals(other.status4Ref))
			return false;
		if (stepDiff == null) {
			if (other.stepDiff != null)
				return false;
		} else if (!stepDiff.equals(other.stepDiff))
			return false;
		if (timesFlag == null) {
			if (other.timesFlag != null)
				return false;
		} else if (!timesFlag.equals(other.timesFlag))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (venderCode == null) {
			if (other.venderCode != null)
				return false;
		} else if (!venderCode.equals(other.venderCode))
			return false;
		if (vid == null) {
			if (other.vid != null)
				return false;
		} else if (!vid.equals(other.vid))
			return false;
		return true;
	}
    
    
}