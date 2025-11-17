package com.efuture.omdmain.model.out;

import com.product.annotation.NotField;
import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

import java.math.BigDecimal;
import java.util.Date;

public class SaleGoodsOutModel {

	private Long entId;

	private String erpCode;

	private String goodsCode;

	private String barNo;

	private String goodsName;

	private String goodsType;
	private Long categoryId;
	private String categoryCode;
	@ReferQuery(table = "category", query = "{categoryId:'$categoryId'}", set = "{categoryName:'categoryName'}", operationFlags = { OperationFlag.afterQuery })
	private String categoryName;
	private Long brandId;
	private String brandCode;
	@ReferQuery(table = "brandinfo", query = "{brandId:'$brandId'}", set = "{brandName:'brandName'}", operationFlags = { OperationFlag.afterQuery })
	private String brandName;
	private String saleSpec;
	private String saleUnit;
	private String salePrice;
	private Long shopId;
	private String shopCode;
	@ReferQuery(table = "shop", query = "{shopId:'$shopId'}", set = "{shopName:'shopName'}", operationFlags = { OperationFlag.afterQuery })
	private String shopName;
	private Integer goodsStatus;
	private String venderCode;

	@ReferQuery(table = "goodsshopref", query = "{goodsCode:'$goodsCode',shopId:'$shopId',erpCode:'$erpCode',entId:'$entId'}", 
			set = "{contractCost:'contractCost',costTaxRate:'costTaxRate',deductRate:'deductRate',operateFlag:'operateFlag'}",
			operationFlags = { OperationFlag.afterQuery })
	private BigDecimal contractCost;

	@NotField(operationFlags = { OperationFlag.Query })
	private BigDecimal costTaxRate;

	@NotField(operationFlags = { OperationFlag.Query })
	private BigDecimal deductRate;

	@NotField(operationFlags = { OperationFlag.Query })
	private String operateFlag;
	
	private String singleItemFlag;
	private String parentGoodsCode;
	private Long vid;
	@ReferQuery(table = "vender", query = "{vid:'$vid'}", set = "{venderName:'venderName'}", operationFlags = { OperationFlag.afterQuery })
	private String venderName;
	
	private Integer mainBarcodeFlag;

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	private Date updateDate;

	public Integer getMainBarcodeFlag() {
		return mainBarcodeFlag;
	}

	public void setMainBarcodeFlag(Integer mainBarcodeFlag) {
		this.mainBarcodeFlag = mainBarcodeFlag;
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

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getBarNo() {
		return barNo;
	}

	public void setBarNo(String barNo) {
		this.barNo = barNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSaleSpec() {
		return saleSpec;
	}

	public void setSaleSpec(String saleSpec) {
		this.saleSpec = saleSpec;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getVenderCode() {
		return venderCode;
	}

	public void setVenderCode(String venderCode) {
		this.venderCode = venderCode;
	}

	public BigDecimal getContractCost() {
		return contractCost;
	}

	public void setContractCost(BigDecimal contractCost) {
		this.contractCost = contractCost;
	}

	public BigDecimal getCostTaxRate() {
		return costTaxRate;
	}

	public void setCostTaxRate(BigDecimal costTaxRate) {
		this.costTaxRate = costTaxRate;
	}

	public BigDecimal getDeductRate() {
		return deductRate;
	}

	public void setDeductRate(BigDecimal deductRate) {
		this.deductRate = deductRate;
	}

	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

	public String getSingleItemFlag() {
		return singleItemFlag;
	}

	public void setSingleItemFlag(String singleItemFlag) {
		this.singleItemFlag = singleItemFlag;
	}

	public String getParentGoodsCode() {
		return parentGoodsCode;
	}

	public void setParentGoodsCode(String parentGoodsCode) {
		this.parentGoodsCode = parentGoodsCode;
	}

	public Long getVid() {
		return vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

}
