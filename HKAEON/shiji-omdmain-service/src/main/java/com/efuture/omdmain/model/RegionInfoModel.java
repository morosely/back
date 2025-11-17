package com.efuture.omdmain.model;

import java.util.Date;

import javax.persistence.Id;

import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;

@UniqueKey(table="regioninfo",keys = { "regionCode","entId" },operationFlags={OperationFlag.Insert},message="企业【${ent_id}】及编码【${regionCode}】数据不唯一")
@UniqueKey(table="regioninfo",keys = { "regionCode","entId" },primaryKey="regionId",operationFlags={OperationFlag.Update},message="企业【${ent_id}】及编码【${regionCode}】数据不唯一")
public class RegionInfoModel {
    /**
     * 行政区域ID
     */
    @Id
    private Long regionId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 行政区域编码
     */
    private String regionCode;

    /**
     * 行政区域名称
     */
    private String regionName;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 上级代码
     */
    private String parentCode;

    /**
     * 描述
     */
    private String remark;

    /**
     * 层级
     */
    private Short level;

    /**
     * 是否叶子结点
     */
    private Boolean leafFlag;

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
     * 获取行政区域ID
     *
     * @return regionId - 行政区域ID
     */
    public Long getRegionId() {
        return regionId;
    }

    /**
     * 设置行政区域ID
     *
     * @param regionId 行政区域ID
     */
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
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
     * 获取行政区域编码
     *
     * @return regionCode - 行政区域编码
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 设置行政区域编码
     *
     * @param regionCode 行政区域编码
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 获取行政区域名称
     *
     * @return regionName - 行政区域名称
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * 设置行政区域名称
     *
     * @param regionName 行政区域名称
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * 获取上级ID
     *
     * @return parentId - 上级ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级ID
     *
     * @param parentId 上级ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取上级代码
     *
     * @return parentCode - 上级代码
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置上级代码
     *
     * @param parentCode 上级代码
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * 获取描述
     *
     * @return remark - 描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置描述
     *
     * @param remark 描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取层级
     *
     * @return level - 层级
     */
    public Short getLevel() {
        return level;
    }

    /**
     * 设置层级
     *
     * @param level 层级
     */
    public void setLevel(Short level) {
        this.level = level;
    }

    /**
     * 获取是否叶子结点
     *
     * @return leafFlag - 是否叶子结点
     */
    public Boolean getLeafFlag() {
        return leafFlag;
    }

    /**
     * 设置是否叶子结点
     *
     * @param leafFlag 是否叶子结点
     */
    public void setLeafFlag(Boolean leafFlag) {
        this.leafFlag = leafFlag;
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