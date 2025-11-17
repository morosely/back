package com.efuture.omdmain.model;

import java.util.Date;

public class MinDiscountModel {
    private Long minDisId;

    private Long entId;

    private String erpCode;

    private String categoryCode;

    private Float minDiscount;

    private Short level;

    private Date updateDate;
    
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getMinDisId() {
        return minDisId;
    }

    public void setMinDisId(Long minDisId) {
        this.minDisId = minDisId;
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
        this.erpCode = erpCode == null ? null : erpCode.trim();
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public Float getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Float minDiscount) {
        this.minDiscount = minDiscount;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }
}