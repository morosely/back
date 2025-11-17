package com.efuture.omdmain.model.out;

import java.math.BigDecimal;

import javax.persistence.Id;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

public class GoodsOutModel {
    /**
     * 可售商品ID
     */
    @Id
    private Long sgid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    
    /**
     * 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     */
    private Short goodsType;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 条码
     */
    private String barNo;


    /**
     * 零售价
     */
    private BigDecimal salePrice;
    /**
     * 销售单位
     */
    private String saleUnit;

    /**
     * 工业分类ID
     */
    private Long categoryId;

    /**
     * 工业分类编码
     */
    private String categoryCode;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 品牌编码
     */
    private String brandCode;

    /**
     * 销售规格
     */
    private String saleSpec;

    /**
     * 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    private Short goodsStatus;

    /**
     * 主营供应商编码
     */
//    @ReferQuery(table="goodsshopref",query="{sgid:'$sgid'}",set="{venderCode:'venderCode'}",operationFlags={OperationFlag.afterQuery})
//    private String venderCode;
    
    @ReferQuery(table="category",query="{categoryId:'$categoryId'}",set="{categoryName:'categoryName'}",operationFlags={OperationFlag.afterQuery})
    private String categoryName;
    
    @ReferQuery(table="brandInfo",query="{brandId:'$brandId'}",set="{brandName:'brandName'}",operationFlags={OperationFlag.afterQuery})
    private String brandName;
    

	public Long getSgid() {
		return sgid;
	}

	public void setSgid(Long sgid) {
		this.sgid = sgid;
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
		this.erpCode = erpCode;
	}


	public Short getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Short goodsType) {
		this.goodsType = goodsType;
	}


	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBarNo() {
		return barNo;
	}

	public void setBarNo(String barNo) {
		this.barNo = barNo;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSaleSpec() {
		return saleSpec;
	}

	public void setSaleSpec(String saleSpec) {
		this.saleSpec = saleSpec;
	}

	public Short getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Short goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

//	public String getVenderCode() {
//		return venderCode;
//	}
//
//	public void setVenderCode(String venderCode) {
//		this.venderCode = venderCode;
//	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}
