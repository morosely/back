package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "errorlog")
public class ErrorLogModel {
	/**
     * 发生错误的类名
     */
    private String className;
    /**
     * 创建时间
     */
    private Date createDate;

	/**
     * 零售商ID
     */
    private Long entId;

	/**
     * 出错编码（自定义编码，方便查询）
     */
    private String errorCode;

    /**
     * 日志ID
     */
    private Long errorId;

    /**
	 * 是否处理出错数据 0：未处理 1：已处理
	 */
	private Boolean isProcessData;

    /**
     * 错误消息描述
     */
    private String message;

    /**
     * 操作类型 I:新增 U:更新 D:删除 S:查询
     */
    private String operateType;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 堆栈信息
     */
    private String stack;

    /**
     * 查询的表名
     */
    private String tableName;

    /**
     * 获取发生错误的类名
     *
     * @return className - 发生错误的类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 获取创建时间
     *
     * @return createDate - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 获取零售商ID
     *
     * @return entId - 零售商ID
     */
    public Long getEntId() {
        return entId;
    }

    /**
     * 获取出错编码（自定义编码，方便查询）
     *
     * @return errorCode - 出错编码（自定义编码，方便查询）
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取日志ID
     *
     * @return errorId - 日志ID
     */
    public Long getErrorId() {
        return errorId;
    }

    public Boolean getIsProcessData() {
		return isProcessData;
	}

    /**
     * 获取错误消息描述
     *
     * @return message - 错误消息描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取操作类型 I:新增 U:更新 D:删除 S:查询
     *
     * @return operateType - 操作类型 I:新增 U:更新 D:删除 S:查询
     */
    public String getOperateType() {
        return operateType;
    }

    /**
     * 获取请求参数
     *
     * @return params - 请求参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 获取堆栈信息
     *
     * @return stack - 堆栈信息
     */
    public String getStack() {
        return stack;
    }

    /**
     * 获取查询的表名
     *
     * @return tableName - 查询的表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置发生错误的类名
     *
     * @param className 发生错误的类名
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 设置出错编码（自定义编码，方便查询）
     *
     * @param errorCode 出错编码（自定义编码，方便查询）
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 设置日志ID
     *
     * @param errorId 日志ID
     */
    public void setErrorId(Long errorId) {
        this.errorId = errorId;
    }

    public void setIsProcessData(Boolean isProcessData) {
		this.isProcessData = isProcessData;
	}

    /**
     * 设置错误消息描述
     *
     * @param message 错误消息描述
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 设置操作类型 I:新增 U:更新 D:删除 S:查询
     *
     * @param operateType 操作类型 I:新增 U:更新 D:删除 S:查询
     */
    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    /**
     * 设置请求参数
     *
     * @param params 请求参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 设置堆栈信息
     *
     * @param stack 堆栈信息
     */
    public void setStack(String stack) {
        this.stack = stack;
    }

    /**
     * 设置查询的表名
     *
     * @param tableName 查询的表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}