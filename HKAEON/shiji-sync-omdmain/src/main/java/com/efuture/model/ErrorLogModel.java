package com.efuture.model;

import java.util.Date;

public class ErrorLogModel {
	
	/**日志id*/
	private Long errorId;
	
	/**表名*/
	private String tableName;
	
	/**参数*/
	private String param;
	
	/**异常信息*/
	private String errorMsg;
	
	/**异常处理状态："1"：未处理，"0"：已处理*/
	private String status;
	
	/**操作类型 I:新增 U:更新 D:删除 S:查询*/
	private String operatorType;
	
	/**参数类型："P":批量，"D":单条*/
	private String paramType;
	
	/**异常创建时间*/
	private Date createDate;

	public ErrorLogModel(Long errorId, String tableName, String param, String errorMsg,
			String operatorType, String paramType) {
		super();
		this.errorId = errorId;
		this.tableName = tableName;
		this.param = param;
		this.errorMsg = errorMsg;
		this.status = "1";
		this.operatorType = operatorType;
		this.paramType = paramType;
		this.createDate = new Date();
	}
	
	public ErrorLogModel() {
		super();
	}

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
