package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.product.annotation.ReferQuery;
import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;
//import javax.persistence.*;

/**
 * 修改salegoods时，为了规避UniqueKey的影响(主要是同一条码对应多个商品的问题)而新建一个model
 */
@Table(name = "salegoods")
@UniqueKey(table="saleGoods",keys = { "barNo","entId","shopId"},operationFlags={OperationFlag.Insert},message="箱码、条码、PLU码:${barNo}重复")
//@UniqueKey(table="saleGoods",keys = { "entId","barNo","shopId"},primaryKey="ssgid",operationFlags={OperationFlag.Update},message="企业：${entId},条码：${barNo},门店：${shopId} 的商品有重复")
public class SaleGoodsModel2 {
	
	//配送方式：0-无需配送，1-行送，2-DC送，3-店铺送，4-自提，5-Coupon配送
	private Short deliveryFlag;
	
	public Short getDeliveryFlag() {
		return deliveryFlag;
	}

	public void setDeliveryFlag(Short deliveryFlag) {
		this.deliveryFlag = deliveryFlag;
	}
	/**
	 * 是否加工(档口商品) 1:是 0:否
	 */
    private Short processFlag;
    
	public Short getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(Short processFlag) {
		this.processFlag = processFlag;
	}
	
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
    @ReferQuery(table="brandInfo",query="{brandId:'$brandId', entId:'$entId'}",set="{brandName:'brandName'}",operationFlags={OperationFlag.afterQuery})
    private String brandName;
	
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

	@Transient
    @ReferQuery(table="category",query="{categoryId:'$categoryId', entId:'$entId'}",set="{categoryName:'categoryName'}",operationFlags={OperationFlag.afterQuery})
    private String categoryName;

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

	@Transient
    @ReferQuery(table="businesscompany",query="{erpCode:'$erpCode',entId:'$entId'}",set="{erpName:'erpName'}",operationFlags={OperationFlag.afterQuery})
    private String erpName;

	/**
     * 是否电子称码  0：否/1：是
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
     * 来源编码名称
     */
    @Transient
    @ReferQuery(table="salegoods",query="{erpCode:'$erpCode',entId:'$entId',goodsCode:'$goodsFromCode'}",set="{goodsFromCodeName:'goodsName'}",operationFlags={OperationFlag.afterQuery})
    private String goodsFromCodeName;

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
     * 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     */
    private Short goodsType;

    private String guadid;

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

	/*
	 * 标识商品(是否打印黄色小票0：否1：是)
	 */
    @Transient
	@ReferQuery(table="category",query="{categoryId:'$categoryId', entId:'$entId'}",set="{license:'license'}",operationFlags={OperationFlag.afterQuery})
	private Short license;
    
    /**
     * 是否主条码
     */
    private Boolean mainBarcodeFlag;

    /*
	 * 管理部类 编码
	 */
	@Transient
	private String manageCategoryCode;

    /*
	 * 管理部类名称
	 */
	@Transient
    @ReferQuery(table="category",query="{erpCode:'$erpCode', entId:'$entId', categoryCode:'$manageCategoryCode'}",set="{manageCategroyName:'categoryName'}",operationFlags={OperationFlag.afterQuery})
    private String manageCategroyName;

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
     * 包装数量（商品的步长，箱码商品的包装含量）
     */
    private Double partsNum;

    private String partsUnit;

    /**
     * 最低批发数量
     */
    private Integer pcs;

    /**
     * 四舍五入方式
     * (Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元'
     * */
    private String prcutMode;

    /**
     * 进货价
     */
    private BigDecimal primeCost;

    /**
     * 是否打印副单
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
     * 门店名称
     */
    @Transient
    @ReferQuery(table="shop",query="{shopId:'$shopId'}",set="{shopName:'shopName'}",operationFlags={OperationFlag.afterQuery})
    private String shopName;

    /**
     * 档口ID
     */
    private Long siid;

    /**
     * 是否单品0-否/1- 是
     */
    private Boolean singleItemFlag;

    /**
     * 分拣等级
     */
    /*@Transient
    @ReferQuery(table="goodsMoreBarCode",query="{entId:'$entId', erpCode:'$erpCode', barNo:'$barNo'}",set="{sortLevel:'sortLevel'}",operationFlags={OperationFlag.afterQuery})*/
    private String sortLevel;

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
     * 供应商简称
     */
    @Transient
    @ReferQuery(table="vender",query="{vid:'$vid', entId:'$entId'}",set="{venderName:'venderName'}",operationFlags={OperationFlag.afterQuery})
    private String venderName;

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

    public String getGoodsFromCodeName() {
		return goodsFromCodeName;
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
     * 获取商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     *
     * @return goodsType - 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     */
    public Short getGoodsType() {
        return goodsType;
    }

    public String getGuadid() {
        return guadid;
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

    public Short getLicense() {
		return license;
	}

    /**
     * 获取是否主条码
     *
     * @return mainBarcodeFlag - 是否主条码
     */
    public Boolean getMainBarcodeFlag() {
        return mainBarcodeFlag;
    }

    public String getManageCategoryCode() {
		return manageCategoryCode;
	}

    public String getManageCategroyName() {
		return manageCategroyName;
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
     * 获取包装数量（商品的步长，箱码商品的包装含量）
     *
     * @return partsNum - 包装数量（商品的步长，箱码商品的包装含量）
     */
    public Double getPartsNum() {
        return partsNum;
    }

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

    public String getShopName() {
		return shopName;
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
     * 获取是否单品0-否/1- 是
     *
     * @return singleItemFlag - 是否单品0-否/1- 是
     */
    public Boolean getSingleItemFlag() {
        return singleItemFlag;
    }

    public String getSortLevel() {
        return sortLevel;
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

    public String getVenderName() {
        return venderName;
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

    public void setGoodsFromCodeName(String goodsFromCodeName) {
		this.goodsFromCodeName = goodsFromCodeName;
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
     * 设置商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     *
     * @param goodsType 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     */
    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
    }

    public void setGuadid(String guadid) {
        this.guadid = guadid;
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

    public void setLicense(Short license) {
		this.license = license;
	}

    /**
     * 设置是否主条码
     *
     * @param mainBarcodeFlag 是否主条码
     */
    public void setMainBarcodeFlag(Boolean mainBarcodeFlag) {
        this.mainBarcodeFlag = mainBarcodeFlag;
    }

    public void setManageCategoryCode(String manageCategoryCode) {
		this.manageCategoryCode = manageCategoryCode;
	}

    public void setManageCategroyName(String manageCategroyName) {
		this.manageCategroyName = manageCategroyName;
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
     * 设置包装数量（商品的步长，箱码商品的包装含量）
     *
     * @param partsNum 包装数量（商品的步长，箱码商品的包装含量）
     */
    public void setPartsNum(Double partsNum) {
        this.partsNum = partsNum;
    }

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
  
    public void setShopName(String shopName) {
		this.shopName = shopName;
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
     * 设置是否单品0-否/1- 是
     *
     * @param singleItemFlag 是否单品0-否/1- 是
     */
    public void setSingleItemFlag(Boolean singleItemFlag) {
        this.singleItemFlag = singleItemFlag;
    }

	public void setSortLevel(String sortLevel) {
        this.sortLevel = sortLevel;
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

	public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

	/**
     * 设置供应商ID
     *
     * @param vid 供应商ID
     */
    public void setVid(Long vid) {
        this.vid = vid;
    }

    
}