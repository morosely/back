package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "aeon_jdtoken")
public class AeonJDTokenModel {
    /**
     * 公司号
     */
    private String companyCode;

    /**
     * 对应多个Token的ItemCode
     */
    private String itemCode1;

    /**
     * 对应1个Token的ItemCode
     */
    private String itemCode2;

    /**
     * 对应的数量
     */
    private Integer number;

    /**
     * 导入时间
     */
    private Date importDate;

    /**
     * 获取公司号
     *
     * @return companyCode - 公司号
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 设置公司号
     *
     * @param companyCode 公司号
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 获取对应多个Token的ItemCode
     *
     * @return itemCode1 - 对应多个Token的ItemCode
     */
    public String getItemCode1() {
        return itemCode1;
    }

    /**
     * 设置对应多个Token的ItemCode
     *
     * @param itemCode1 对应多个Token的ItemCode
     */
    public void setItemCode1(String itemCode1) {
        this.itemCode1 = itemCode1;
    }

    /**
     * 获取对应1个Token的ItemCode
     *
     * @return itemCode2 - 对应1个Token的ItemCode
     */
    public String getItemCode2() {
        return itemCode2;
    }

    /**
     * 设置对应1个Token的ItemCode
     *
     * @param itemCode2 对应1个Token的ItemCode
     */
    public void setItemCode2(String itemCode2) {
        this.itemCode2 = itemCode2;
    }

    /**
     * 获取对应的数量
     *
     * @return number - 对应的数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置对应的数量
     *
     * @param number 对应的数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取导入时间
     *
     * @return importDate - 导入时间
     */
    public Date getImportDate() {
        return importDate;
    }

    /**
     * 设置导入时间
     *
     * @param importDate 导入时间
     */
    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }
}