package com.efuture.model;

public abstract class CommonExtModel extends CommonModel{

	
	private static final long serialVersionUID = 1L;
	public String DMLstatus;//增删改状态1:增加,2:删除,3:更新
	private Integer rowNumber;	//行号

	public abstract Integer getDealStatus();
	public String getDMLstatus() {
		return DMLstatus;
	}
	public Integer getRowNumber() {
		return rowNumber;
	}
	
	public abstract void setDealStatus(Integer dealStatus);
	
	public void setDMLstatus(String dMLstatus) {
		DMLstatus = dMLstatus;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
}
