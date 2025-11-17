package com.efuture.omdmain.model;

import java.util.Date;

import javax.validation.constraints.Min;

import com.product.model.BeanConstant;

public class AbstractEntityBean {
	@Min(value = 0)
	protected Long entId; // 企业ID,使用long在where条件时效率更高

	protected String note; // 备注
	protected String baseStatus = BeanConstant.Status.NORMAL; // 状态
	protected String lang; // 语言类型
	protected Date createDate; // 时间戳
	protected Date receiveDate; // 时间戳
	protected Date lastDate; // 时间戳
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getBaseStatus() {
		return baseStatus;
	}
	public void setBaseStatus(String baseStatus) {
		this.baseStatus = baseStatus;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	
	
	

}
