package com.efuture.model.stock;

import com.efuture.model.CommonExtModel;

import java.util.Date;

public class ExtShelfGoodsRefModel extends CommonExtModel {
    private Long sgridt;

    private String shopCode;

    private String yardCode;

    private String shelfCode;

    private String allocationCode;

    private String goodsCode;

    private Integer alloRow;

    private Integer allocolumn;

    private Integer alloDepth;

    private Long stockNum;

    private String shejfGoodsRefTmp1;

    private String shelfGoodsRefTmp2;

    private Integer status;

    private Integer dealStatus;

    @Override
    public String getUniqueKey() {
        return "shopCode,goodsCode,yardCode,shelfCode,allocationCode";
    }

    @Override
    public String getUniqueKeyValue() {
        /*return  "'" + this.shopCode + "'," +
                "'" + this.goodsCode + "'";*/
    	return "'"+this.shopCode+"','"+this.goodsCode+"','"+this.yardCode+"','"+this.shelfCode+"','"+this.allocationCode+"'";
    }
    
    public Long getSgridt() {
        return sgridt;
    }

    public void setSgridt(Long sgridt) {
        this.sgridt = sgridt;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public String getYardCode() {
        return yardCode;
    }

    public void setYardCode(String yardCode) {
        this.yardCode = yardCode == null ? null : yardCode.trim();
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode == null ? null : shelfCode.trim();
    }

    public String getAllocationCode() {
        return allocationCode;
    }

    public void setAllocationCode(String allocationCode) {
        this.allocationCode = allocationCode == null ? null : allocationCode.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public Integer getAlloRow() {
        return alloRow;
    }

    public void setAlloRow(Integer alloRow) {
        this.alloRow = alloRow;
    }

    public Integer getAllocolumn() {
        return allocolumn;
    }

    public void setAllocolumn(Integer allocolumn) {
        this.allocolumn = allocolumn;
    }

    public Integer getAlloDepth() {
        return alloDepth;
    }

    public void setAlloDepth(Integer alloDepth) {
        this.alloDepth = alloDepth;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public String getShejfGoodsRefTmp1() {
        return shejfGoodsRefTmp1;
    }

    public void setShejfGoodsRefTmp1(String shejfGoodsRefTmp1) {
        this.shejfGoodsRefTmp1 = shejfGoodsRefTmp1 == null ? null : shejfGoodsRefTmp1.trim();
    }

    public String getShelfGoodsRefTmp2() {
        return shelfGoodsRefTmp2;
    }

    public void setShelfGoodsRefTmp2(String shelfGoodsRefTmp2) {
        this.shelfGoodsRefTmp2 = shelfGoodsRefTmp2 == null ? null : shelfGoodsRefTmp2.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public void setCreateDate(Date createDate) {

    }

    @Override
    public Date getUpdateDate() {
        return null;
    }

    @Override
    public void setUpdateDate(Date updateDate) {

    }
}