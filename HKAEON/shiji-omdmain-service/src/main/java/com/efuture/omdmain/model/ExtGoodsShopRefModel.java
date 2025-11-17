package com.efuture.omdmain.model;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExtGoodsShopRefModel{
    @org.springframework.data.annotation.Transient
    @ReferQuery(table="shop",query="{shopCode:'$shopCode'}",set="{shopName:'shopName'}",operationFlags={OperationFlag.afterQuery})
    private String shopName;

    private Long batchno;
    
    private Long egsrid;

    private Long entId;

    private String erpCode;

    private Long shopId;

    private String shopCode;

    private Long saleOrgId;

    private String orgCode;

    private Long siid;

    private String stallCode;

    private Long sgid;

    private String goodsCode;

    private String venderCode;

    private Integer goodStatus;

    private BigDecimal cost;

    private BigDecimal contractCost;

    private Float costTaxRate;

    private BigDecimal deductRate;

    private BigDecimal salePrice;

    private BigDecimal customPrice;

    private BigDecimal bulkPrice;

    private Integer pcs;

    private Float partsNum;

    private Float stepDiff;

    private Integer safeStockDays;

    private Integer minStockDays;

    private Integer maxStockDays;

    private Short priceRight;

    private Short continuePurFlag;

    private Short importantFlag;

    private Short returnFlag;

    private Short operateFlag;

    private Short orderFlag;

    private Short logistics;

    private String dcshopId;

    private String fdcShopId;

    private Date inDate;

    private String inOper;

    private Date offDate;

    private String lang;

    private String creator;

    private Date createDate;

    private String modifier;

    private Date updateDate;

    private Long vid;

    private String prcutMode;

    private Integer dealStatus;

    private String goodsName;

}