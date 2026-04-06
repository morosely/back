package com.jd.common.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable {
	
	private String id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String category_name;
	private String item_desc;
	
	public String getCategory_name() {
		return category_name;
	}
	public String getId() {
		return id;
	}
	public String getImage() {
		return image;
	}
	public String getItem_desc() {
		return item_desc;
	}
	public long getPrice() {
		return price;
	}
	public String getSell_point() {
		return sell_point;
	}
	public String getTitle() {
		return title;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
