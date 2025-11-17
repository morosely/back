package com.efuture.omdmain.model.hwg;

import java.util.Date;

public class Hwg_FulfillmentShopModel {
    /** 主键ID*/
    private Long hfsid;

    /** 零售商ID*/
    private Long entId;

    /** 经营公司编码*/
    private String erpCode;

    /** 门店ID*/
    private Long shopId;

    /** 门店编码*/
    private String shopCode;

    /** 状态*/
    private Short status;

    /** 语言类型*/
    private String lang;

    /** 创建人*/
    private String creator;

    /** 创建日期*/
    private Date createDate;

    /** 修改人*/
    private String modifier;

    /** 修改日期*/
    private Date updateDate;

    public Long getHfsid() {
        return hfsid;
    }

    public void setHfsid(Long hfsid) {
        this.hfsid = hfsid;
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
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
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