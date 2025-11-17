package com.efuture.omdmain.model;

import javax.persistence.Transient;

/** 
* @author yihaitao
* @time 2018年5月26日 下午6:23:10 
* 
*/
public class CategoryPropertyBean extends CategoryPropertyModel {
	 /**
     * 属性值编码 辅助字段，无需数据库映射
     */
	@Transient
    private String propertyValueCode = "0";
    
    public String getPropertyValueCode() {
		return propertyValueCode;
	}

	public void setPropertyValueCode(String propertyValueCode) {
		this.propertyValueCode = propertyValueCode;
	}
}
