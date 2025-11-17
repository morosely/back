package com.efuture.model;

import java.io.Serializable;
import java.util.Date;

public abstract class CommonModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public abstract String getUniqueKey();
	public abstract String getUniqueKeyValue();
	public abstract Date getCreateDate() ;
	public abstract void setCreateDate(Date createDate);
	public abstract Date getUpdateDate();
	public abstract void setUpdateDate(Date updateDate);
	
	public Date erpUpdateDate;
	public Date getErpUpdateDate() {
		return erpUpdateDate;
	}
	public void setErpUpdateDate(Date erpUpdateDate) {
		this.erpUpdateDate = erpUpdateDate;
	}

	private Long seqNum;

	private String dealType;

	public Long getSeqNum() {return seqNum;}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

}
