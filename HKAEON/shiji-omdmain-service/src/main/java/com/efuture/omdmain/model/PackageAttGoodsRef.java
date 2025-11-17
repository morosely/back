package com.efuture.omdmain.model;

import java.util.Date;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 套餐分类与套餐商品关系表
 */
@Data
public class PackageAttGoodsRef {
    /**
    * 工业分类ID
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

    @Transient
    @ReferQuery(table="packageattcate",query="{erpCode:'$erpCode',pCode:'$pCode'}",set="{pName:'pName'}",operationFlags={OperationFlag.afterQuery})
    private String pName;

    /**
    * 商品编码
    */
    private String goodsCode;

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
}