package com.efuture.omdmain.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "versionlist")
public class VersionListModel {
    /**
     * ID
     */
    private Long vlid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 序列号
     */
    private String serialNo;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 行数
     */
    private Long rowsCount;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 状态0-未更新/1-已更新
     */
    private Integer statusFlag;

    /**
     * 获取ID
     *
     * @return vlid - ID
     */
    public Long getVlid() {
        return vlid;
    }

    /**
     * 设置ID
     *
     * @param vlid ID
     */
    public void setVlid(Long vlid) {
        this.vlid = vlid;
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
     * 获取序列号
     *
     * @return serialNo - 序列号
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * 设置序列号
     *
     * @param serialNo 序列号
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * 获取表名
     *
     * @return tableName - 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     *
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取版本号
     *
     * @return versionNo - 版本号
     */
    public String getVersionNo() {
        return versionNo;
    }

    /**
     * 设置版本号
     *
     * @param versionNo 版本号
     */
    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * 获取行数
     *
     * @return rowsCount - 行数
     */
    public Long getRowsCount() {
        return rowsCount;
    }

    /**
     * 设置行数
     *
     * @param rowsCount 行数
     */
    public void setRowsCount(Long rowsCount) {
        this.rowsCount = rowsCount;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取状态0-未更新/1-已更新
     *
     * @return statusFlag - 状态0-未更新/1-已更新
     */
    public Integer getStatusFlag() {
        return statusFlag;
    }

    /**
     * 设置状态0-未更新/1-已更新
     *
     * @param statusFlag 状态0-未更新/1-已更新
     */
    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }
}