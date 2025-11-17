package com.efuture.omdmain.model;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExtGoodsModel{

    //接口表的品类只能通过categoryCode去查询标识商品，但是categoryCode四，五级会有重复
    @Transient //修改基类查询，强制注入level = 5
    @ReferQuery(table="category",query="{categoryCode:'$categoryCode',level:'$level'}",set="{categoryName:'categoryName',license:'license'}",operationFlags={OperationFlag.afterQuery})
    private String categoryName;
    @Transient
    private Short license;//标识商品(是否打印黄色小票0：否1：是)
    @Transient
    private Integer level;

    private Long egid;

    private Long entId;

    private String erpCode;

    private Integer goodsType;

    private Boolean directFromErp;

    private Boolean singleItemFlag;

    private Boolean canSaleFlag;

    private String goodsCode;

    private String parentGoodsCode;

    private String goodsName;

    private String goodsDisplayName;

    private String fullName;

    private String enSname;

    private String enFname;

    private String barNo;

    private BigDecimal salePrice;

    private BigDecimal refPrice;

    private BigDecimal minSalePrice;

    private Float minDiscount;

    private BigDecimal memberPrice;

    private BigDecimal primeCost;

    private String goodsFromCode;

    private String measureUnit;

    private String saleUnit;

    private Double partsNum;

    private String partsUnit;

    private Long categoryId;

    private String categoryCode;

    private Long brandId;

    private String brandCode;

    private String artNo;

    private String textture;

    private String originArea;

    private Double nweight;

    private Double rweight;

    private Integer qaDays;

    private Short qaDaysUnit;

    private Float waterDamage;

    private Float lowTemp;

    private Float highTemp;

    private Float inputTax;

    private Float outputTax;

    private Float consumpTax;

    private Integer longScale;

    private Integer wideScale;

    private Integer highScale;

    private String goodsGrade;

    private String orderSpec;

    private String orderUnit;

    private Integer orderNum;

    private Integer safeStockDays;

    private Integer minStockDays;

    private Integer maxStockDays;

    private Boolean isPercious;

    private Boolean coldTransFlag;

    private Boolean escaleFlag;

    private Short timesFlag;

    private String season;

    private Boolean isBatch;

    private Boolean controlFlag;

    private BigDecimal recycleFee;

    private Boolean multiUnitFlag;

    private Short goodsStatus;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Integer dealStatus;

    private String venderCode;
    
    private Short deliveryFlag;
}