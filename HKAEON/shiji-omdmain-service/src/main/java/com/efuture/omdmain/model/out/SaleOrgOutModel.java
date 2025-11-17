package com.efuture.omdmain.model.out;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

public class SaleOrgOutModel {
	private Long saleOrgId;
	
	private Long entId;
	
	private String erpCode;
	
	private String orgCode;
	
	private String orgName;
	
	private String parentCode;
	
	@ReferQuery(table="shop",query="{shopId:'$shopId',entId:'$entId',erpCode:'$erpCode'}",set="{shopCode:'shopCode'}",operationFlags={OperationFlag.afterQuery})
	private String shopCode;
	
	private String shopId;


	public Long getSaleOrgId() {
		return saleOrgId;
	}


	public void setSaleOrgId(Long saleOrgId) {
		this.saleOrgId = saleOrgId;
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


	public String getOrgCode() {
		return orgCode;
	}


	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


	public String getParentCode() {
		return parentCode;
	}


	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}


	public String getShopCode() {
		return shopCode;
	}


	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}


	public String getShopId() {
		return shopId;
	}


	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}
