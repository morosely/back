package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "extpricechangebilldetail")
public class ExtPriceChangeBillDetailModel {
    /**
     * 调价商品ID
     */
    private Long epcbdid;

    /**
     * 调价单ID
     */
    private Long pcbid;

    /**
     * 调价单号
     */
    private String pcBillno;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 销售商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private Long goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 原售价
     */
    private String originPrice;

    /**
     * 现售价
     */
    private String nowPrice;

    /**
     * 接收时间
     */
    private String recieveDate;

    /**
     * 调价时间
     */
    private String pcDate;

    /**
     * 出错信息
     */
    private String errorMsg;

    /**
     * 数据来源编码
     */
    private String sourceFromCode;

    /**
     * 0-未处理/1-处理中/2-已处理
     */
    private Integer dealStatus;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取调价商品ID
     *
     * @return epcbdid - 调价商品ID
     */
    public Long getEpcbdid() {
        return epcbdid;
    }

    /**
     * 设置调价商品ID
     *
     * @param epcbdid 调价商品ID
     */
    public void setEpcbdid(Long epcbdid) {
        this.epcbdid = epcbdid;
    }

    /**
     * 获取调价单ID
     *
     * @return pcbid - 调价单ID
     */
    public Long getPcbid() {
        return pcbid;
    }

    /**
     * 设置调价单ID
     *
     * @param pcbid 调价单ID
     */
    public void setPcbid(Long pcbid) {
        this.pcbid = pcbid;
    }

    /**
     * 获取调价单号
     *
     * @return pcBillno - 调价单号
     */
    public String getPcBillno() {
        return pcBillno;
    }

    /**
     * 设置调价单号
     *
     * @param pcBillno 调价单号
     */
    public void setPcBillno(String pcBillno) {
        this.pcBillno = pcBillno;
    }

    /**
     * 获取零售商ID
     *
     * @return entId - 零售商ID
     */
    public Long getEntId() {
        return entId;
    }

    /**
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 获取经营公司编码
     *
     * @return erpCode - 经营公司编码
     */
    public String getErpCode() {
        return erpCode;
    }

    /**
     * 设置经营公司编码
     *
     * @param erpCode 经营公司编码
     */
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    /**
     * 获取销售商品ID
     *
     * @return sgid - 销售商品ID
     */
    public Long getSgid() {
        return sgid;
    }

    /**
     * 设置销售商品ID
     *
     * @param sgid 销售商品ID
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    /**
     * 获取商品编码
     *
     * @return goodsCode - 商品编码
     */
    public Long getGoodsCode() {
        return goodsCode;
    }

    /**
     * 设置商品编码
     *
     * @param goodsCode 商品编码
     */
    public void setGoodsCode(Long goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * 获取商品名称
     *
     * @return goodsName - 商品名称
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 设置商品名称
     *
     * @param goodsName 商品名称
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 获取原售价
     *
     * @return originPrice - 原售价
     */
    public String getOriginPrice() {
        return originPrice;
    }

    /**
     * 设置原售价
     *
     * @param originPrice 原售价
     */
    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    /**
     * 获取现售价
     *
     * @return nowPrice - 现售价
     */
    public String getNowPrice() {
        return nowPrice;
    }

    /**
     * 设置现售价
     *
     * @param nowPrice 现售价
     */
    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    /**
     * 获取接收时间
     *
     * @return recieveDate - 接收时间
     */
    public String getRecieveDate() {
        return recieveDate;
    }

    /**
     * 设置接收时间
     *
     * @param recieveDate 接收时间
     */
    public void setRecieveDate(String recieveDate) {
        this.recieveDate = recieveDate;
    }

    /**
     * 获取调价时间
     *
     * @return pcDate - 调价时间
     */
    public String getPcDate() {
        return pcDate;
    }

    /**
     * 设置调价时间
     *
     * @param pcDate 调价时间
     */
    public void setPcDate(String pcDate) {
        this.pcDate = pcDate;
    }

    /**
     * 获取出错信息
     *
     * @return errorMsg - 出错信息
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置出错信息
     *
     * @param errorMsg 出错信息
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * 获取数据来源编码
     *
     * @return sourceFromCode - 数据来源编码
     */
    public String getSourceFromCode() {
        return sourceFromCode;
    }

    /**
     * 设置数据来源编码
     *
     * @param sourceFromCode 数据来源编码
     */
    public void setSourceFromCode(String sourceFromCode) {
        this.sourceFromCode = sourceFromCode;
    }

    /**
     * 获取0-未处理/1-处理中/2-已处理
     *
     * @return dealStatus - 0-未处理/1-处理中/2-已处理
     */
    public Integer getDealStatus() {
        return dealStatus;
    }

    /**
     * 设置0-未处理/1-处理中/2-已处理
     *
     * @param dealStatus 0-未处理/1-处理中/2-已处理
     */
    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * 获取语言类型
     *
     * @return lang - 语言类型
     */
    public String getLang() {
        return lang;
    }

    /**
     * 设置语言类型
     *
     * @param lang 语言类型
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取创建日期
     *
     * @return createDate - 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 获取修改日期
     *
     * @return updateDate - 修改日期
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改日期
     *
     * @param updateDate 修改日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}