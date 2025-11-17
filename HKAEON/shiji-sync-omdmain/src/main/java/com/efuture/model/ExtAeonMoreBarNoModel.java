package com.efuture.model;

import java.util.Date;

public class ExtAeonMoreBarNoModel extends CommonExtModel {

	private static final long serialVersionUID = 1L;

	@Override
    public String getUniqueKey() {
        return "goodsCode,barNo,groupCode";
    }

    @Override
    public String getUniqueKeyValue() {
        return "'"+this.goodsCode+"','"+ this.barNo + "','" + this.groupCode + "'";
    }

    private Long mbid;

    private String goodsCode;

    private String barNo;

    private String groupCode;

    private Integer dealStatus;
    
    private Date createDate;
    
    private Date updateDate;

    private Short mainBarcodeFlag;
    
    private Short barNoStatus;
    
    public Short getBarNoStatus() {
		return barNoStatus;
	}

	public void setBarNoStatus(Short barNoStatus) {
		this.barNoStatus = barNoStatus;
	}

	public Short getMainBarcodeFlag() {
		return mainBarcodeFlag;
	}

	public void setMainBarcodeFlag(Short mainBarcodeFlag) {
		this.mainBarcodeFlag = mainBarcodeFlag;
	}

	public Long getMbid() {
        return mbid;
    }

    public void setMbid(Long mbid) {
        this.mbid = mbid;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getBarNo() {
        return barNo;
    }

    public void setBarNo(String barNo) {
        this.barNo = barNo == null ? null : barNo.trim();
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}