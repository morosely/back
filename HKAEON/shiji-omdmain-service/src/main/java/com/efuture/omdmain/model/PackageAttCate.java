package com.efuture.omdmain.model;

import java.util.Date;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 套餐属性类别
 */
@Data
public class PackageAttCate {
    /**
    * ID
    */
    private Long id;

    /**
    * 零售商ID
    */
    private Long entId;

    /**
    * 经营公司编码
    */
    private String erpCode;

    /**
    * 属性类别编码
    */
    private String pCode;

    /**
    * 属性类别名称
    */
    private String pName;

    /**
     * 必选数量
     */
    private Integer requiredCount;

    /**
    * 属性类别英文名称
    */
    private String pEnName;

    /**
    * 上级代码
    */
    private String parentCode;

    @Transient
    @ReferQuery(table="packageattcate",query="{erpCode:'$erpCode',pCode:'$parentCode'}",set="{parentName:'pName'}",operationFlags={OperationFlag.afterQuery})
    private String parentName;

    @Transient
    @ReferQuery(table="packageattcate",query="{erpCode:'$erpCode',pCode:'$parentCode'}",set="{parentEnName:'pEnName'}",operationFlags={OperationFlag.afterQuery})
    private String parentEnName;

    /**
    * 状态
    */
    private Short status;

    /**
    * 层级
    */
    private Short level;

    /**
    * 是否叶子结点
    */
    private Boolean leafFlag;

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
}