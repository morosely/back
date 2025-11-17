package com.efuture.omdmain.model;

import java.util.Date;
import lombok.Data;

/**
 * 订单叫号状态历史表
 */
@Data
public class OrderCallNoHis {
    /**
    * 主键ID
    */
    private Long id;

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
    * 档口名称
    */
    private String stallName;

    /**
    * 叫餐号
    */
    private String callNo;

    /**
    * 订单号
    */
    private String orderId;

    /**
    * 订单叫号状态 0-已取消/1-制作中/2-待取餐/3-已取餐
    */
    private Short status;

    /**
    * 零售商ID
    */
    private Long entId;

    /**
    * 语言类型
    */
    private String lang;

    /**
    * 创建人(收银机号)
    */
    private String creator;

    /**
    * 创建日期
    */
    private Date createDate;

    /**
    * 修改人(收银机号)
    */
    private String modifier;

    /**
    * 修改日期
    */
    private Date updateDate;
}