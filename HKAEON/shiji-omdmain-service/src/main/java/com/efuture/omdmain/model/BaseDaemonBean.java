package com.efuture.omdmain.model;

public class BaseDaemonBean extends AbstractEntityBean{
	
	String  channel;     //渠道名称
    Boolean processed;   //是否已处理
    String  message;     //异常消息
    String  flag;        //增删改状态
    Long    pageNo;
    Long    pageSize;
	Long    requestID;   //请求序列号，用于调度使用，不作为接口字段
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Boolean getProcessed() {
		return processed;
	}
	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Long getPageNo() {
		return pageNo;
	}
	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getRequestID() {
		return requestID;
	}
	public void setRequestID(Long requestID) {
		this.requestID = requestID;
	}
	
	

}
