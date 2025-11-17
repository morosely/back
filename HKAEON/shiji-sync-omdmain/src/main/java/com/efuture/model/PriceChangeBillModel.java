package com.efuture.model;

import java.util.Date;

public class PriceChangeBillModel extends CommonModel{
	
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "pcBillno,shopCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.pcBillno+"',+'"+this.shopCode+"','"+this.erpCode+"',"+this.entId;
	}
	
    /**调价单ID*/
    private Long pcbid;

    /**零售商ID*/
    private Long entId;
    
    /**经营公司编码*/
    private String erpCode;

    /**机构编码*/
    private String shopCode;

    /**机构名称*/
    private String shopName;

    /**机构简称*/
    private String shopSName;

    /**调价单号*/
    private String pcBillno;

    /**调价开始时间*/
    private String pcStartDate;

    /**调价结束时间*/
    private String pcEndDate;

    /**生效时间*/
    private String effDate;

    /**接收时间*/
    private String recieveDate;

    /**调价时间*/
    private String pcDate;

    /**出错信息*/
    private String errorMsg;

    /**状态*/
    private Integer status;

    /**语言类型*/
    private String lang;

    /**创建人*/
    private String creator;

    /**创建日期*/
    private Date createDate;

    /**修改人*/
    private String modifier;

    /**修改日期*/
    private Date updateDate;

    public Long getPcbid() {
        return pcbid;
    }

    public void setPcbid(Long pcbid) {
        this.pcbid = pcbid;
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

	public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopSName() {
        return shopSName;
    }

    public void setShopSName(String shopSName) {
        this.shopSName = shopSName == null ? null : shopSName.trim();
    }

    public String getPcBillno() {
        return pcBillno;
    }

    public void setPcBillno(String pcBillno) {
        this.pcBillno = pcBillno == null ? null : pcBillno.trim();
    }

    public String getPcStartDate() {
        return pcStartDate;
    }

    public void setPcStartDate(String pcStartDate) {
        this.pcStartDate = pcStartDate == null ? null : pcStartDate.trim();
    }

    public String getPcEndDate() {
        return pcEndDate;
    }

    public void setPcEndDate(String pcEndDate) {
        this.pcEndDate = pcEndDate == null ? null : pcEndDate.trim();
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate == null ? null : effDate.trim();
    }

    public String getRecieveDate() {
        return recieveDate;
    }

    public void setRecieveDate(String recieveDate) {
        this.recieveDate = recieveDate == null ? null : recieveDate.trim();
    }

    public String getPcDate() {
        return pcDate;
    }

    public void setPcDate(String pcDate) {
        this.pcDate = pcDate == null ? null : pcDate.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}