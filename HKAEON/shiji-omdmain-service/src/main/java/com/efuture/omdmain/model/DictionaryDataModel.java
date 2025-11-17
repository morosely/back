package com.efuture.omdmain.model;

import java.util.Date;

import javax.persistence.Id;

import com.product.annotation.Length;
import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;

//@UniqueKey(table="dictionarydata",keys = { "entId","dictCode","dictDatacnname" },operationFlags={OperationFlag.Insert},message="字典数据不唯一")
@UniqueKey(table="dictionarydata",keys = { "entId","dictCode","dictDataCode"},primaryKey="dictDataId",operationFlags={OperationFlag.Insert,OperationFlag.Update},message="字典值编码【${dictDataCode}】必须唯一")
@UniqueKey(table="dictionarydata",keys = { "entId","dictCode","dictDatacnname" },primaryKey="dictDataId",operationFlags={OperationFlag.Insert,OperationFlag.Update},message="字典值【${dictDatacnname}】必须唯一")
public class DictionaryDataModel {
    /**
     * 字典数据ID
     */
    @Id
    private Long dictDataId;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 字典ID
     */
    private Long dictId;

    /**
     * 字典代码
     */
    private String dictCode;

    /**
     * 字典数据代码
     */
    @Length(operationFlags={OperationFlag.Insert,OperationFlag.Update},message="字典值编码不能为空且长度不超过20",min="1",max="20")
    private String dictDataCode;

    /**
     * 字典数据英文名称
     */
    private String dictDataenname;

    /**
     * 字典数据中文名称
     */
    @Length(operationFlags={OperationFlag.Insert,OperationFlag.Update},message="字典值不能为空且长度不超过20",min="1",max="32")
    private String dictDatacnname;

    /**
     * 描述
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序序号
     */
    private Short orderNum;

    /**
     * 层级
     */
    private Integer levelNum;

    /**
     * 是否叶子结点
     */
    private Boolean leafFlag;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 是否系统级
     */
    private Boolean systemFlag;

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
     * 获取字典数据ID
     *
     * @return dictDataId - 字典数据ID
     */
    public Long getDictDataId() {
        return dictDataId;
    }

    /**
     * 设置字典数据ID
     *
     * @param dictDataId 字典数据ID
     */
    public void setDictDataId(Long dictDataId) {
        this.dictDataId = dictDataId;
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
     * 获取字典数据代码
     *
     * @return dictDataCode - 字典数据代码
     */
    public String getDictDataCode() {
        return dictDataCode;
    }

    /**
     * 设置字典数据代码
     *
     * @param dictDataCode 字典数据代码
     */
    public void setDictDataCode(String dictDataCode) {
        this.dictDataCode = dictDataCode;
    }

    /**
     * 获取字典数据英文名称
     *
     * @return dictDataenname - 字典数据英文名称
     */
    public String getDictDataenname() {
        return dictDataenname;
    }

    /**
     * 设置字典数据英文名称
     *
     * @param dictDataenname 字典数据英文名称
     */
    public void setDictDataenname(String dictDataenname) {
        this.dictDataenname = dictDataenname;
    }

    /**
     * 获取字典数据中文名称
     *
     * @return dictDatacnname - 字典数据中文名称
     */
    public String getDictDatacnname() {
        return dictDatacnname;
    }

    /**
     * 设置字典数据中文名称
     *
     * @param dictDatacnname 字典数据中文名称
     */
    public void setDictDatacnname(String dictDatacnname) {
        this.dictDatacnname = dictDatacnname;
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
     * 获取排序序号
     *
     * @return orderNum - 排序序号
     */
    public Short getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排序序号
     *
     * @param orderNum 排序序号
     */
    public void setOrderNum(Short orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取层级
     *
     * @return levelNum - 层级
     */
    public Integer getLevelNum() {
        return levelNum;
    }

    /**
     * 设置层级
     *
     * @param levelNum 层级
     */
    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
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