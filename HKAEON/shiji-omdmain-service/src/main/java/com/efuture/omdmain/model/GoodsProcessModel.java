package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "goodsprocess")
public class GoodsProcessModel {
    /**
     * 商品加工ID
     */
    @Id
    private Long ssgid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 加工方式
     */
    private String processType;

    /**
     * 加工费
     */
    private BigDecimal processFee;

    /**
     * 最低加工费
     */
    private BigDecimal minProcessFee;

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
     * 获取商品加工ID
     *
     * @return ssgid - 商品加工ID
     */
    public Long getSsgid() {
        return ssgid;
    }

    /**
     * 设置商品加工ID
     *
     * @param ssgid 商品加工ID
     */
    public void setSsgid(Long ssgid) {
        this.ssgid = ssgid;
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
     * 获取商品ID
     *
     * @return sgid - 商品ID
     */
    public Long getSgid() {
        return sgid;
    }

    /**
     * 设置商品ID
     *
     * @param sgid 商品ID
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    /**
     * 获取商品编码
     *
     * @return goodsCode - 商品编码
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * 设置商品编码
     *
     * @param goodsCode 商品编码
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * 获取加工方式
     *
     * @return processType - 加工方式
     */
    public String getProcessType() {
        return processType;
    }

    /**
     * 设置加工方式
     *
     * @param processType 加工方式
     */
    public void setProcessType(String processType) {
        this.processType = processType;
    }

    /**
     * 获取加工费
     *
     * @return processFee - 加工费
     */
    public BigDecimal getProcessFee() {
        return processFee;
    }

    /**
     * 设置加工费
     *
     * @param processFee 加工费
     */
    public void setProcessFee(BigDecimal processFee) {
        this.processFee = processFee;
    }

    /**
     * 获取最低加工费
     *
     * @return minProcessFee - 最低加工费
     */
    public BigDecimal getMinProcessFee() {
        return minProcessFee;
    }

    /**
     * 设置最低加工费
     *
     * @param minProcessFee 最低加工费
     */
    public void setMinProcessFee(BigDecimal minProcessFee) {
        this.minProcessFee = minProcessFee;
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