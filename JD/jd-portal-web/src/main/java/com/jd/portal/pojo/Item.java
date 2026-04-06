package com.jd.portal.pojo;

import com.jd.pojo.TbItem;

public class Item extends TbItem {
     
	 private String[] images;
		
	 public void setImages(String[] images) {
		this.images = images;
	}

	public String[] getImages() {
		if (this.getImage()!=null && !"".equals(this.getImage())) {
			String img = this.getImage();
			String[] strings = img.split(",");
			return strings;
		}
		return null;
	}
}
