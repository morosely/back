package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pricechangebill")
public class PriceChangeBillModel {
    /**
     * 调价单ID
     */
    @Id
    private Long pcbid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 机构编码
     */
    private String shopCode;

    /**
     * 机构名称
     */
    private String shopName;

    /**
     * 机构简称
     */
    private String shopSName;

    /**
     * 调价单号
     */
    private String pcBillno;

    /**
     * 调价开始时间
     */
    private String pcStartDate;

    /**
     * 调价结束时间
     */
    private String pcEndDate;

    /**
     * 生效时间
     */
    private String effDate;

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
     * 状态
     */
    private Short status;

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
     * 获取机构编码
     *
     * @return shopCode - 机构编码
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * 设置机构编码
     *
     * @param shopCode 机构编码
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    /**
     * 获取机构名称
     *
     * @return shopName - 机构名称
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 设置机构名称
     *
     * @param shopName 机构名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 获取机构简称
     *
     * @return shopSName - 机构简称
     */
    public String getShopSName() {
        return shopSName;
    }

    /**
     * 设置机构简称
     *
     * @param shopSName 机构简称
     */
    public void setShopSName(String shopSName) {
        this.shopSName = shopSName;
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
     * 获取调价开始时间
     *
     * @return pcStartDate - 调价开始时间
     */
    public String getPcStartDate() {
        return pcStartDate;
    }

    /**
     * 设置调价开始时间
     *
     * @param pcStartDate 调价开始时间
     */
    public void setPcStartDate(String pcStartDate) {
        this.pcStartDate = pcStartDate;
    }

    /**
     * 获取调价结束时间
     *
     * @return pcEndDate - 调价结束时间
     */
    public String getPcEndDate() {
        return pcEndDate;
    }

    /**
     * 设置调价结束时间
     *
     * @param pcEndDate 调价结束时间
     */
    public void setPcEndDate(String pcEndDate) {
        this.pcEndDate = pcEndDate;
    }

    /**
     * 获取生效时间
     *
     * @return effDate - 生效时间
     */
    public String getEffDate() {
        return effDate;
    }

    /**
     * 设置生效时间
     *
     * @param effDate 生效时间
     */
    public void setEffDate(String effDate) {
        this.effDate = effDate;
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
     * 获取状态
     *
     * @return status - 状态
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Short status) {
        this.status = status;
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