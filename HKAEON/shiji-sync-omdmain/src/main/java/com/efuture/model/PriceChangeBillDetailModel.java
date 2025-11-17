package com.efuture.model;

import java.util.Date;

public class PriceChangeBillDetailModel extends CommonModel{
	
	private static final long serialVersionUID = 1L;

	public String getUniqueKey() {
		return "pcBillno,goodsCode,erpCode,entId";
	}
    
    public String getUniqueKeyValue() {
		return "'"+this.pcBillno+"','"+this.goodsCode+"','"+this.erpCode+"',"+this.entId;
	}
	
    /**调价商品ID*/
    private Long pcbdid;

    /**调价单ID*/
    private Long pcbid;

    /**调价单号*/
    private String pcBillno;

    /**零售商ID*/
    private Long entId;
    
    /**经营公司编码*/
    private String erpCode;

    /**销售商品ID*/
    private Long sgid;

    /**商品编码*/
    private String goodsCode;

    /**商品名称*/
    private String goodsName;
    
    /**商品条码*/
    private String barNo;

    /**原售价*/
    private String originPrice;

    /**现售价*/
    private String nowPrice;

    /**接收时间*/
    private String recieveDate;

    /**调价时间*/
    private String pcDate;

    /**出错信息*/
    private String errorMsg;

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

    public Long getPcbdid() {
        return pcbdid;
    }

    public void setPcbdid(Long pcbdid) {
        this.pcbdid = pcbdid;
    }

    public Long getPcbid() {
        return pcbid;
    }

    public void setPcbid(Long pcbid) {
        this.pcbid = pcbid;
    }

    public String getPcBillno() {
        return pcBillno;
    }

    public void setPcBillno(String pcBillno) {
        this.pcBillno = pcBillno == null ? null : pcBillno.trim();
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
		this.erpCode = erpCode == null ? null : pcBillno.trim();
	}

	public Long getSgid() {
        return sgid;
    }

    public void setSgid(Long sgid) {
        this.sgid = sgid;
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
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }
    
    public String getBarNo() {
		return barNo;
	}

	public void setBarNo(String barNo) {
		this.barNo = barNo == null ? null : goodsName.trim();
	}

	public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice == null ? null : originPrice.trim();
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice == null ? null : nowPrice.trim();
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