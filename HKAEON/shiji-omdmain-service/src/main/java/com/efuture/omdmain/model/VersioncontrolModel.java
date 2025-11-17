package com.efuture.omdmain.model;

import javax.persistence.*;

@Table(name = "versioncontrol")
public class VersioncontrolModel {
    /**
     * ID
     */
    @Id
    private Long vcid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 当前版本号
     */
    private String currentVersionNo;

    /**
     * 获取ID
     *
     * @return vcid - ID
     */
    public Long getVcid() {
        return vcid;
    }

    /**
     * 设置ID
     *
     * @param vcid ID
     */
    public void setVcid(Long vcid) {
        this.vcid = vcid;
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
     * 获取当前版本号
     *
     * @return currentVersionNo - 当前版本号
     */
    public String getCurrentVersionNo() {
        return currentVersionNo;
    }

    /**
     * 设置当前版本号
     *
     * @param currentVersionNo 当前版本号
     */
    public void setCurrentVersionNo(String currentVersionNo) {
        this.currentVersionNo = currentVersionNo;
    }
}