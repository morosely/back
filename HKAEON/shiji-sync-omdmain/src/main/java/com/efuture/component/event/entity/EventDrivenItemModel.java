package com.efuture.component.event.entity;

import java.util.Date;

public class EventDrivenItemModel {
	String  eventCode;   // 事件代号
	String  eventDesc;   // 事件说明
	Integer eventSeqno;  // 序列号
	Integer actionSeqno; // 处理序列号
	Integer lastSeqno;   // 最后序列号
	Date    lastDate;    // 最后更新时间
	
	public String getEventCode() {
		return eventCode;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public Integer getEventSeqno() {
		return eventSeqno;
	}
	public Integer getActionSeqno() {
		return actionSeqno;
	}
	public Integer getLastSeqno() {
		return lastSeqno;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public void setEventSeqno(Integer eventSeqno) {
		this.eventSeqno = eventSeqno;
	}
	public void setActionSeqno(Integer actionSeqno) {
		this.actionSeqno = actionSeqno;
	}
	public void setLastSeqno(Integer lastSeqno) {
		this.lastSeqno = lastSeqno;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
}
