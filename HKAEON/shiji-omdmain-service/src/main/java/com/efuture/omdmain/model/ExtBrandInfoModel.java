package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

@Table(name = "extbrandinfo")
public class ExtBrandInfoModel extends BaseDaemonBean{
    /**
     * 品牌ID
     */
    @Id
    private Long ebrandId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌简码
     */
    private String brandSCode;

    /**
     * 品牌编码
     */
    private String brandCode;

    /**
     * 品牌分类编码
     */
    @ReferQuery(table="dictionarydata",query="{dictDataCode:'$brandTypeCode', entId:'$entId'}",set="{brandTypeName:'dictDatacnname'}",operationFlags={OperationFlag.afterQuery})
    private String brandTypeCode;

    /**
     * 品牌等级编码
     */
    @ReferQuery(table="dictionarydata",query="{dictDataCode:'$brandLevelCode', entId:'$entId'}",set="{brandLevelName:'dictDatacnname'}",operationFlags={OperationFlag.afterQuery})
    private String brandLevelCode;

    /**
     * 英文名称
     */
    private String brandEnName;

    /**
     * 设计师
     */
    private String designer;

    /**
     * 品牌描述
     */
    private String brandDesc;

    /**
     * 状态
     */
    private Integer status;

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
     * 获取品牌ID
     *
     * @return ebrandId - 品牌ID
     */
    public Long getEbrandId() {
        return ebrandId;
    }

    /**
     * 设置品牌ID
     *
     * @param ebrandId 品牌ID
     */
    public void setEbrandId(Long ebrandId) {
        this.ebrandId = ebrandId;
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
     * 获取品牌名称
     *
     * @return brandName - 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 获取品牌简码
     *
     * @return brandSCode - 品牌简码
     */
    public String getBrandSCode() {
        return brandSCode;
    }

    /**
     * 设置品牌简码
     *
     * @param brandSCode 品牌简码
     */
    public void setBrandSCode(String brandSCode) {
        this.brandSCode = brandSCode;
    }

    /**
     * 获取品牌编码
     *
     * @return brandCode - 品牌编码
     */
    public String getBrandCode() {
        return brandCode;
    }

    /**
     * 设置品牌编码
     *
     * @param brandCode 品牌编码
     */
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    /**
     * 获取品牌分类编码
     *
     * @return brandTypeCode - 品牌分类编码
     */
    public String getBrandTypeCode() {
        return brandTypeCode;
    }

    /**
     * 设置品牌分类编码
     *
     * @param brandTypeCode 品牌分类编码
     */
    public void setBrandTypeCode(String brandTypeCode) {
        this.brandTypeCode = brandTypeCode;
    }

    /**
     * 获取品牌等级编码
     *
     * @return brandLevelCode - 品牌等级编码
     */
    public String getBrandLevelCode() {
        return brandLevelCode;
    }

    /**
     * 设置品牌等级编码
     *
     * @param brandLevelCode 品牌等级编码
     */
    public void setBrandLevelCode(String brandLevelCode) {
        this.brandLevelCode = brandLevelCode;
    }

    /**
     * 获取英文名称
     *
     * @return brandEnName - 英文名称
     */
    public String getBrandEnName() {
        return brandEnName;
    }

    /**
     * 设置英文名称
     *
     * @param brandEnName 英文名称
     */
    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName;
    }

    /**
     * 获取设计师
     *
     * @return designer - 设计师
     */
    public String getDesigner() {
        return designer;
    }

    /**
     * 设置设计师
     *
     * @param designer 设计师
     */
    public void setDesigner(String designer) {
        this.designer = designer;
    }

    /**
     * 获取品牌描述
     *
     * @return brandDesc - 品牌描述
     */
    public String getBrandDesc() {
        return brandDesc;
    }

    /**
     * 设置品牌描述
     *
     * @param brandDesc 品牌描述
     */
    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
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