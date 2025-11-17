package com.efuture.omdmain.model;

import java.util.Date;

import javax.persistence.*;

import com.product.annotation.DefaultValue;
import com.product.annotation.Length;
import com.product.annotation.NotNull;
import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;

@Table(name = "dictionary")
@UniqueKey(table="dictionary",keys = { "entId","dictCode" },primaryKey="dictId",operationFlags={OperationFlag.Insert,OperationFlag.Update},message="字典编码【${dictCode}】必须唯一")
@UniqueKey(table="dictionary",keys = { "entId","cnName" },primaryKey="dictId",operationFlags={OperationFlag.Insert,OperationFlag.Update},message="字典名称【${cnName}】必须唯一")
public class DictionaryModel {
    /**
     * 字典ID
     */
    @Id
    private Long dictId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 字典类别CODE
     */
    private String dictTypeCode;

    /**
     * 字典类别名称
     */
    private String dictTypeName;

    /**
     * 字典代码
     */
//    @NotNull(operationFlags={OperationFlag.Insert})
    @Length(min="1",max="20",operationFlags = { OperationFlag.Insert,OperationFlag.Update },message="字典编码长度必须在1~20之间")
    private String dictCode;

    /**
     * 字典中文名称
     */
//    @NotNull(operationFlags={OperationFlag.Insert})
    @Length(min="1",max="64",operationFlags = { OperationFlag.Insert,OperationFlag.Update },message="字典名称长度必须在1~64之间")
    private String cnName;

    /**
     * 字典英文名称
     */
    private String enName;

    /**
     * 描述
     */
    private String remark;

    /**
     * 代码长度
     */
    private Integer codeLength;

    /**
     * 是否自动编码
     */
    private Boolean autoCode;

    /**
     * 是否系统级
     */
    @DefaultValue(value="true",operationFlags={OperationFlag.Insert})
    private Boolean systemFlag;

    /**
     * 状态
     */
	@DefaultValue(value="1",operationFlags={OperationFlag.Insert})
    private Integer status;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取字典ID
     *
     * @return dictId - 字典ID
     */
    public Long getDictId() {
        return dictId;
    }

    /**
     * 设置字典ID
     *
     * @param dictId 字典ID
     */
    public void setDictId(Long dictId) {
        this.dictId = dictId;
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
     * 获取字典类别CODE
     *
     * @return dictTypeCode - 字典类别CODE
     */
    public String getDictTypeCode() {
        return dictTypeCode;
    }

    /**
     * 设置字典类别CODE
     *
     * @param dictTypeCode 字典类别CODE
     */
    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }

    /**
     * 获取字典类别名称
     *
     * @return dictTypeName - 字典类别名称
     */
    public String getDictTypeName() {
        return dictTypeName;
    }

    /**
     * 设置字典类别名称
     *
     * @param dictTypeName 字典类别名称
     */
    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    /**
     * 获取字典代码
     *
     * @return dictCode - 字典代码
     */
    public String getDictCode() {
        return dictCode;
    }

    /**
     * 设置字典代码
     *
     * @param dictCode 字典代码
     */
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    /**
     * 获取字典中文名称
     *
     * @return cnName - 字典中文名称
     */
    public String getCnName() {
        return cnName;
    }

    /**
     * 设置字典中文名称
     *
     * @param cnName 字典中文名称
     */
    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    /**
     * 获取字典英文名称
     *
     * @return enName - 字典英文名称
     */
    public String getEnName() {
        return enName;
    }

    /**
     * 设置字典英文名称
     *
     * @param enName 字典英文名称
     */
    public void setEnName(String enName) {
        this.enName = enName;
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
     * 获取代码长度
     *
     * @return codeLength - 代码长度
     */
    public Integer getCodeLength() {
        return codeLength;
    }

    /**
     * 设置代码长度
     *
     * @param codeLength 代码长度
     */
    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    /**
     * 获取是否自动编码
     *
     * @return autoCode - 是否自动编码
     */
    public Boolean getAutoCode() {
        return autoCode;
    }

    /**
     * 设置是否自动编码
     *
     * @param autoCode 是否自动编码
     */
    public void setAutoCode(Boolean autoCode) {
        this.autoCode = autoCode;
    }

    /**
     * 获取是否系统级
     *
     * @return systemFlag - 是否系统级
     */
    public Boolean getSystemFlag() {
        return systemFlag;
    }

    /**
     * 设置是否系统级
     *
     * @param systemFlag 是否系统级
     */
    public void setSystemFlag(Boolean systemFlag) {
        this.systemFlag = systemFlag;
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