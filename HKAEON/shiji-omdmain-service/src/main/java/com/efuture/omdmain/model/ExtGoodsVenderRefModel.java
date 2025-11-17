package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

@Table(name = "extgoodsvenderref")
public class ExtGoodsVenderRefModel {
    /**
     * 供应商商品ID
     */
    @Id
    private Long egvrid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 组织机构ID
     */
    private Long shopId;

    /**
     * 机构编码
     */
    private String shopCode;

    /**
     * 供应商编码
     */
    @ReferQuery(table="extVender",query="{venderCode:'$venderCode', entId:'$entId' , erpCode:'$erpCode'}",set="{venderName:'venderName'}",operationFlags={OperationFlag.afterQuery})
    private String venderCode;

    /**
     * 商品编码
     */
    @ReferQuery(table="extGoods",query="{goodsCode:'$goodsCode', entId:'$entId' , erpCode:'$erpCode'}",set="{goodsName:'goodsName'}",operationFlags={OperationFlag.afterQuery})
    private String goodsCode;

    /**
     * 执行价（含税）
     */
    private BigDecimal cost;

    /**
     * 合同价（含税）
     */
    private BigDecimal contractCost;

    /**
     * 进项税率
     */
    private Float costTaxRate;

    /**
     * 保底扣点
     */
    private BigDecimal deductRate;

    /**
     * 退货标识
     */
    private Integer returnFlag;

    /**
     * 订货标识
     */
    private Integer orderFlag;

    /**
     * 物流模式
     */
    private Integer logistics;

    /**
     * 下架日期
     */
    private Date offDate;

    /**
     * 默认顺序
     */
    private Integer defaultSort;

    /**
     * 引入日期
     */
    private Date inDate;

    /**
     * 引入人（审核人）
     */
    private String inOper;

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
     * 获取供应商商品ID
     *
     * @return egvrid - 供应商商品ID
     */
    public Long getEgvrid() {
        return egvrid;
    }

    /**
     * 设置供应商商品ID
     *
     * @param egvrid 供应商商品ID
     */
    public void setEgvrid(Long egvrid) {
        this.egvrid = egvrid;
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
     * 获取组织机构ID
     *
     * @return shopId - 组织机构ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置组织机构ID
     *
     * @param shopId 组织机构ID
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
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
     * 获取供应商编码
     *
     * @return venderCode - 供应商编码
     */
    public String getVenderCode() {
        return venderCode;
    }

    /**
     * 设置供应商编码
     *
     * @param venderCode 供应商编码
     */
    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
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
     * 获取执行价（含税）
     *
     * @return cost - 执行价（含税）
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置执行价（含税）
     *
     * @param cost 执行价（含税）
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取合同价（含税）
     *
     * @return contractCost - 合同价（含税）
     */
    public BigDecimal getContractCost() {
        return contractCost;
    }

    /**
     * 设置合同价（含税）
     *
     * @param contractCost 合同价（含税）
     */
    public void setContractCost(BigDecimal contractCost) {
        this.contractCost = contractCost;
    }

    /**
     * 获取进项税率
     *
     * @return costTaxRate - 进项税率
     */
    public Float getCostTaxRate() {
        return costTaxRate;
    }

    /**
     * 设置进项税率
     *
     * @param costTaxRate 进项税率
     */
    public void setCostTaxRate(Float costTaxRate) {
        this.costTaxRate = costTaxRate;
    }

    /**
     * 获取保底扣点
     *
     * @return deductRate - 保底扣点
     */
    public BigDecimal getDeductRate() {
        return deductRate;
    }

    /**
     * 设置保底扣点
     *
     * @param deductRate 保底扣点
     */
    public void setDeductRate(BigDecimal deductRate) {
        this.deductRate = deductRate;
    }

    /**
     * 获取退货标识
     *
     * @return returnFlag - 退货标识
     */
    public Integer getReturnFlag() {
        return returnFlag;
    }

    /**
     * 设置退货标识
     *
     * @param returnFlag 退货标识
     */
    public void setReturnFlag(Integer returnFlag) {
        this.returnFlag = returnFlag;
    }

    /**
     * 获取订货标识
     *
     * @return orderFlag - 订货标识
     */
    public Integer getOrderFlag() {
        return orderFlag;
    }

    /**
     * 设置订货标识
     *
     * @param orderFlag 订货标识
     */
    public void setOrderFlag(Integer orderFlag) {
        this.orderFlag = orderFlag;
    }

    /**
     * 获取物流模式
     *
     * @return logistics - 物流模式
     */
    public Integer getLogistics() {
        return logistics;
    }

    /**
     * 设置物流模式
     *
     * @param logistics 物流模式
     */
    public void setLogistics(Integer logistics) {
        this.logistics = logistics;
    }

    /**
     * 获取下架日期
     *
     * @return offDate - 下架日期
     */
    public Date getOffDate() {
        return offDate;
    }

    /**
     * 设置下架日期
     *
     * @param offDate 下架日期
     */
    public void setOffDate(Date offDate) {
        this.offDate = offDate;
    }

    /**
     * 获取默认顺序
     *
     * @return defaultSort - 默认顺序
     */
    public Integer getDefaultSort() {
        return defaultSort;
    }

    /**
     * 设置默认顺序
     *
     * @param defaultSort 默认顺序
     */
    public void setDefaultSort(Integer defaultSort) {
        this.defaultSort = defaultSort;
    }

    /**
     * 获取引入日期
     *
     * @return inDate - 引入日期
     */
    public Date getInDate() {
        return inDate;
    }

    /**
     * 设置引入日期
     *
     * @param inDate 引入日期
     */
    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    /**
     * 获取引入人（审核人）
     *
     * @return inOper - 引入人（审核人）
     */
    public String getInOper() {
        return inOper;
    }

    /**
     * 设置引入人（审核人）
     *
     * @param inOper 引入人（审核人）
     */
    public void setInOper(String inOper) {
        this.inOper = inOper;
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