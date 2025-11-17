package com.efuture.omdmain.model;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
//import javax.persistence.*;

@Table(name = "salegoods")
@Data
public class AeonCitySaleGoodsModel {


    /**
     * 产地,净重,包装含量
     */
    @Transient
    @ReferQuery(table="goods",query="{goodsCode:'$goodsCode',entId:'$entId',erpCode:'$erpCode'}",
            set="{originArea:'originArea',partsNum:'partsNum',partsUnit:'partsUnit',nweight:'nweight',orderNum:'orderNum'}",
            operationFlags={OperationFlag.afterQuery})
    private String originArea;//产地
    @Transient
    private Double nweight;//净重
    @Transient
    private String orderNum;//包装含量

    private String partsUnit;//包装单位(商品表goods)

    private Double partsNum;//包装数量(商品表goods)
	
	//配送方式：0-无需配送，1-行送，2-DC送，3-店铺送，4-自提，5-Coupon配送
	private Short deliveryFlag;
	 /**
     * 是否加工(档口商品) 1:是 0:否
     */
    private Short processFlag;
    
	/**
     * 货号
     */
    private String artNo;
	/**
     * 条码
     */
    private String barNo;

	/**
     * 品牌编码
     */
    private String brandCode;

	/**
     * 品牌ID
     */
   
    private Long brandId;
	 
//	@Transient
//    @ReferQuery(table="brandInfo",query="{brandId:'$brandId', entId:'$entId'}",set="{brandName:'brandName'}",operationFlags={OperationFlag.afterQuery})
//    private String brandName;

	/**
     * 批发价
     */
    private BigDecimal bulkPrice;

	/**
     * 工业分类编码
     */
    private String categoryCode;
	/**
     * 工业分类ID
     */
    private Long categoryId;
	
//	@Transient
//    @ReferQuery(table="category",query="{categoryId:'$categoryId', entId:'$entId'}",set="{categoryName:'categoryName',artCode:'parentCode'}",operationFlags={OperationFlag.afterQuery})
//    private String categoryName;
	
	@Transient
	private String artCode;

	/**
     * 是否冷藏运输
     */
    private Boolean coldTransFlag;
	
	/**
     * 消费税率
     */
    private Float consumpTax;

	/**
     * 是否受管制
     */
    private Boolean controlFlag;

	private Date createDate;
    /**
     * 是否直接来源ERP 0-否/1-是
     */
    private Boolean directFromErp;

	/**
     * 英文全称
     */
    private String enFname;

	/**
     * 英文简称
     */
    private String enSname;
    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

//	@Transient
//    @ReferQuery(table="businesscompany",query="{erpCode:'$erpCode',entId:'$entId'}",set="{erpName:'erpName'}",operationFlags={OperationFlag.afterQuery})
//    private String erpName;

	/**
     * 是否电子称码  0：否/1：是
     */
    private Boolean escaleFlag;
    /**
     * 商品编码
     */
    private String goodsCode;

	/**
     * 商品展示名称
     */
    private String goodsDisplayName;

	/**
     * 来源编码
     */
    private String goodsFromCode;

    /**
     * 来源编码名称
     */
//    @Transient
//    @ReferQuery(table="salegoods",query="{erpCode:'$erpCode',entId:'$entId',goodsCode:'$goodsFromCode'}",set="{goodsFromCodeName:'goodsName'}",operationFlags={OperationFlag.afterQuery})
//    private String goodsFromCodeName;

    /**
     * 商品等级
     */
    private String goodsGrade;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品状态 0 待启用/1新品试销/ 2新品评估/3正常/4暂停订货/5换季处理/6暂停经营/7停止销售/8待清退/9已清退/10暂停订补
     */
    private Short goodsStatus;

    /**
     * 商品类型 0-普通商品/1-子品/2-虚拟母品/3-组包码/4-菜谱/7-分割商品/9-档口套餐/10-黄金商品/11-券商品/12-服务费商品/13-统销码商品
     */
    private Short goodsType;

    private String guadid;

    /**
     * 是否需要输入批次号销售
     */
    private Boolean isBatch;

    /**
     * 是否贵重商品：0=否,1=是
     */
    private Boolean isPercious;

    /**
     * 语言类型
     */
    private String lang;

    /*
	 * 标识商品(是否打印黄色小票0：否1：是)
	 */
//    @Transient
//	@ReferQuery(table="category",query="{categoryId:'$categoryId', entId:'$entId'}",set="{license:'license'}",operationFlags={OperationFlag.afterQuery})
//	private Short license;

    /**
     * 是否主条码
     */
    private Boolean mainBarcodeFlag;

	/*
	 * 管理部类 编码
	 */
//	@Transient
//	private String manageCategoryCode;

	/*
	 * 管理部类名称
	 */
//	@Transient
//    @ReferQuery(table="category",query="{erpCode:'$erpCode', entId:'$entId', categoryCode:'$manageCategoryCode'}",set="{manageCategroyName:'categoryName'}",operationFlags={OperationFlag.afterQuery})
//    private String manageCategroyName;
	
	/**
     * 会员价
     */
    private BigDecimal memberPrice;

    /**
     * 最低折扣率
     */
    private Float minDiscount;

    /**
     * 最低售价
     */
    private BigDecimal minSalePrice;

    /**
     * 是否多单位
     */
    private Boolean multiUnitFlag;

    /**
     * 柜组编码
     */
    private String orgCode;

    /**
     * 产地
     */
//    @Transient
//    @ReferQuery(table="goods",query="{goodsCode:'$goodsCode',entId:'$entId',erpCode:'$erpCode'}",set="{originArea:'originArea'}",operationFlags={OperationFlag.afterQuery})
//    private String originArea;

    /**
     * 销项税率
     */
    private Float outputTax;

    /**
     * 母品编码
     */
    private String parentGoodsCode;

    /**
     * 最低批发数量
     */
    private Integer pcs;

    /**
     * 四舍五入方式
     * (Y:截断 N:四舍五入)0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元'
     * */
    private String prcutMode;

    /**
     * 进货价
     */
    private BigDecimal primeCost;

    /**
     * 是否打印副单
     */
    private Boolean prtDuplFlag;

    /**
     * 母品ID
     */
    private Long psgid;

    /**
     * 回收费用
     */
    private BigDecimal recycleFee;

    /**
     * 参考售价
     */
    private BigDecimal refPrice;

    /**
     * 毛重
     */
    private Double rweight;

    /**
     * 柜组ID
     */
    private Long saleOrgId;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

    /**
     * 销售规格
     */
    private String saleSpec;

    /**
     * 销售单位
     */
    private String saleUnit;

    /**
     * 时令属性
     */
    private String season;

    /**
     * 商品ID
     */
    private Long sgid;
    /**
     * 门店编码
     */
    private String shopCode;
    
    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 门店名称
     */
//    @Transient
//    @ReferQuery(table="shop",query="{shopId:'$shopId'}",set="{shopName:'shopName'}",operationFlags={OperationFlag.afterQuery})
//    private String shopName;

    /**
     * 档口ID
     */
    private Long siid;

    /**
     * 是否单品0-否/1- 是
     */
    private Boolean singleItemFlag;

    /**
     * 分拣等级
     */
    /*@Transient
    @ReferQuery(table="goodsMoreBarCode",query="{entId:'$entId', erpCode:'$erpCode', barNo:'$barNo'}",set="{sortLevel:'sortLevel'}",operationFlags={OperationFlag.afterQuery})*/
    private String sortLevel;

    /**
     * 可售商品ID
     */
    @Id
    private Long ssgid;

    /**
     * 档口编码
     */
    private String stallCode;

    /**
     * 步长差异范围
     */
    private Float stepDiff;

    /**
     * 时令标志（1- 一次性商品/2-季节性商品/3-年节商品）
     */
    private Short timesFlag;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 主营供应商编码
     */
    private String venderCode;

    /**
     * 供应商简称
     */
//    @Transient
//    @ReferQuery(table="vender",query="{vid:'$vid', entId:'$entId'}",set="{venderName:'venderName'}",operationFlags={OperationFlag.afterQuery})
//    private String venderName;

    /**
     * 供应商ID
     */
    private Long vid;

    
}