package com.efuture.omdmain.model;


import java.sql.Time;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 餐饮时段表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CateringTime {
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
     * 餐饮时段编码
     */
    private String cateringTimeCode;

    /**
     * 餐饮时段名称
     */
    private String cateringTimeName;

    /**
     * 开始时间
     */
    private Time startTime;

    /**
     * 结束时间
     */
    private Time endTime;

    /**
     * 状态1:有效,0:无效
     */
    private String status;

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