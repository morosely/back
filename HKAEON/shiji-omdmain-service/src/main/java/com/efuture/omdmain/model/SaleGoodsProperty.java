package com.efuture.omdmain.model;

import java.util.Date;
import lombok.Data;

/**
 * 可售商品—附属表
 */
@Data
public class SaleGoodsProperty {
    /**
    * 主键ID
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
    * 门店编码
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
    * 售罄标识(1:售罄,0:未售罄)
    */
    private String sellout;

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
    /**
     * 备用字段01
     */
    private String back01;

    /**
     * 备用字段02
     */
    private String back02;

    /**
     * 备用字段03
     */
    private String back03;
}