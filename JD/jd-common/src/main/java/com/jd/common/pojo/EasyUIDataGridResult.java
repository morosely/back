package com.jd.common.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGridResult implements Serializable {

	private long total;

	private List rows;

	public EasyUIDataGridResult() {
		super();
	}

	public EasyUIDataGridResult(long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public List getRows() {
		return rows;
	}

	public long getTotal() {
		return total;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
