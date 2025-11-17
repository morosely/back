package com.efuture.component.event.entity;

import java.util.Date;

public class EventDrivenModel {
	String  eventCode;   // 事件代号
	Integer eventSeqno;  // 序列号
	Date    lastDate;    // 最后更新时间
	
	public String getEventCode() {
		return eventCode;
	}
	public Integer getEventSeqno() {
		return eventSeqno;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public void setEventSeqno(Integer eventSeqno) {
		this.eventSeqno = eventSeqno;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
}
