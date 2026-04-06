package com.shiji.model;

import lombok.Data;
import org.apache.ibatis.annotations.Options;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "order_tbl")
public class Order implements Serializable {

	@Id
	@Column(name = "id")
	public Long id;

	@Column(name = "user_id")
	public String userId;

	@Column(name = "commodity_code")
	public String commodityCode;

	@Column(name = "count")
	public Integer count;

	@Column(name = "money")
	public Integer money;

	@Override
	public String toString() {
		return "Order{" + "id=" + id + ", userId='" + userId + '\'' + ", commodityCode='"
				+ commodityCode + '\'' + ", count=" + count + ", money=" + money + '}';
	}

}

