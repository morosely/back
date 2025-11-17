package com.efuture.omdmain.model;

import java.util.Date;

public class PartyTrayGoodsPropertiesModel {
    private Long ptgpid;

    private Long sgid;

    private Long entId;

    private String erpCode;

    private String goodsCode;

    private String proGroup;

    private String proItem;

    private Date updateDate;

    public Long getPtgpid() {
        return ptgpid;
    }

    public void setPtgpid(Long ptgpid) {
        this.ptgpid = ptgpid;
    }

    public Long getSgid() {
        return sgid;
    }

    public void setSgid(Long sgid) {
        this.sgid = sgid;
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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getProGroup() {
        return proGroup;
    }

    public void setProGroup(String proGroup) {
        this.proGroup = proGroup == null ? null : proGroup.trim();
    }

    public String getProItem() {
        return proItem;
    }

    public void setProItem(String proItem) {
        this.proItem = proItem == null ? null : proItem.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}