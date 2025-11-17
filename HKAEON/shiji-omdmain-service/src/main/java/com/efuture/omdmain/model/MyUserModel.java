package com.efuture.omdmain.model;

import java.util.Date;

/**
 * 所有在Bean中定义的字段类型，在执行数据库请求时，会根据强类型转换
 */
public class MyUserModel {
	Long ent_id;
	Long seqno;
	String code;
	String name;
	Double pointvalue;
	Date createdate;
	public Long getEnt_id() {
		return ent_id;
	}
	public void setEnt_id(Long ent_id) {
		this.ent_id = ent_id;
	}
	public Long getSeqno() {
		return seqno;
	}
	public void setSeqno(Long seqno) {
		this.seqno = seqno;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPointvalue() {
		return pointvalue;
	}
	public void setPointvalue(Double pointvalue) {
		this.pointvalue = pointvalue;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

}
