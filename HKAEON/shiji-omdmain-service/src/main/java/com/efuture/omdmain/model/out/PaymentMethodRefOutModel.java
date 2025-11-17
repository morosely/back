package com.efuture.omdmain.model.out;

import com.product.annotation.NotField;
import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;


public class PaymentMethodRefOutModel {
	
	private  Long entId;
	
	private String erpCode;
	
	private String payCode;
	
	private String payName;
	
	private String pmid;
	
	@ReferQuery(table = "paymentmethod", query = "{pmid:'$pmid'}", 
			set = "{parentCode:'parentCode',payType:'payType',rate:'rate',leafFlag:'leafFlag',virtualPayType:'virtualPayType'}", operationFlags = { OperationFlag.afterQuery })
	private String parentCode;
	
	@NotField(operationFlags = { OperationFlag.Query })
	private String payType;
	
	@NotField(operationFlags = { OperationFlag.Query })
	private double rate;
	
	@NotField(operationFlags = { OperationFlag.Query })
	private String leafFlag;
	
	@NotField(operationFlags = { OperationFlag.Query })
	private Integer virtualPayType;

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

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(String leafFlag) {
		this.leafFlag = leafFlag;
	}

	public Integer getVirtualPayType() {
		return virtualPayType;
	}

	public void setVirtualPayType(Integer virtualPayType) {
		this.virtualPayType = virtualPayType;
	}
	
}
