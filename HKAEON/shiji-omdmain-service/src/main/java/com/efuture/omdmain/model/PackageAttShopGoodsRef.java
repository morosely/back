package com.efuture.omdmain.model;

import java.util.Date;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 套餐分类与门店商品关系表
 */
@Data
public class PackageAttShopGoodsRef {
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

    @Transient
    @ReferQuery(table="packageattcate",query="{erpCode:'$erpCode',pCode:'$pCode'}",set="{pName:'pName'}",operationFlags={OperationFlag.afterQuery})
    private String pName;

    private String cateringTimeCode;

    @Transient
    @ReferQuery(table="cateringtime",query="{erpCode:'$erpCode',cateringTimeCode:'$cateringTimeCode'}",set="{cateringTimeName:'cateringTimeName'}",operationFlags={OperationFlag.afterQuery})
    private String cateringTimeName;

    /**
     * 自助不显示 0:否(显示),1:是(不显示)
     */
    private String selfNotShow;

    /**
    * 属性字典编码
    */
    private String shopCode;

    /**
    * 档口编码
    */
    private String stallCode;

    /**
    * 商品编码
    */
    private String goodsCode;

    /**
    * 条码
    */
    private String barNo;

    /**
    * 状态
    */
    private Short status;

    /**
     * 套餐编码（此字段有值时goodsCode套餐明细的商品编码）
     */
    private String sGoodsCode;
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