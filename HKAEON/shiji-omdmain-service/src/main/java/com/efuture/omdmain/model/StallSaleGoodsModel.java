package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

@Data
public class StallSaleGoodsModel {

	@Transient
    @ReferQuery(table="category",query="{categoryId:'$categoryId', entId:'$entId'}",set="{categoryName:'categoryName',artCode:'parentCode'}",operationFlags={OperationFlag.afterQuery})
    private String categoryName;

	private Long ssgid;

    private Long entId;

    private String erpCode;

    private Long shopId;

    private String shopCode;

    private Long saleOrgId;

    private String orgCode;

    private Long siid;

    private String stallCode;

    private Short goodsType;

    private Boolean directFromErp;

    private Boolean singleItemFlag;

    private Long sgid;

    private String goodsCode;

    private Long psgid;

    private String parentGoodsCode;

    private String goodsName;

    private String goodsDisplayName;

    private String enSname;

    private String barNo;

    private Boolean mainBarcodeFlag;

    private BigDecimal salePrice;

    private BigDecimal refPrice;

    private BigDecimal memberPrice;

    private Float minDiscount;

    private BigDecimal minSalePrice;

    private BigDecimal bulkPrice;

    private Integer pcs;

    private BigDecimal primeCost;

    private String goodsFromCode;

    private String artNo;

    private String saleUnit;

    private Double partsNum;

    private Double rweight;

    private Long categoryId;

    private String categoryCode;

    private Long brandId;

    private String brandCode;

    private String saleSpec;

    private Float outputTax;

    private Float consumpTax;

    private String goodsGrade;

    private Boolean isPercious;

    private Boolean coldTransFlag;

    private Boolean escaleFlag;

    private Short timesFlag;

    private String season;

    private Boolean isBatch;

    private Boolean multiUnitFlag;

    private Short goodsStatus;

    private Long vid;

    private String venderCode;

    private Float stepDiff;

    private Boolean controlFlag;

    private BigDecimal recycleFee;

    private String lang;

    private Date createDate;

    private Date updateDate;

    private Boolean prtDuplFlag;

    private String enFname;

    private String partsUnit;

    private String prcutMode;

    private Short processFlag;

    private Short deliveryFlag;
}