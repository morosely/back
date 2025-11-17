package com.efuture.omdmain.model.out;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class BusinessCompanyOutModel {
	private Long bcid;

	/**
	 * 零售商ID
	 */
	private Long entId;

	/**
	 * 经营公司编码
	 */
	private String erpCode;

	/**
	 * 经营公司名称
	 */
	private String erpName;

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public String getErpName() {
		return erpName;
	}

	public void setErpName(String erpName) {
		this.erpName = erpName;
	}

	public Long getBcid() {
		return bcid;
	}

	public void setBcid(Long bcid) {
		this.bcid = bcid;
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
