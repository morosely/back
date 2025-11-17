package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;
import com.product.annotation.NotField;
import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

@Table(name = "processrecipe")
public class ProcessRecipeModel {
    @NotField(operationFlags = { OperationFlag.Query })
    private String barNo;

    private String categoryCode;

    @NotField(operationFlags = { OperationFlag.Query })
    private Long categoryId;

    private String categoryName;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 零售商ID
     */
    private Long entId;
    
    /**
     * 商品编码
     */
    private String goodsCode;
    
    @ReferQuery(table="goods",
        query="{sgid:'$sgid'}",
        set="{goodsName:'goodsName',barNo:'barNo', categoryId:'categoryId'}",
        operationFlags={OperationFlag.afterQuery})
    private String goodsName;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 加工配方单ID
     */
    @Id
    private Long prid;
    
    /**
     * 加工类型 0-重量/1-占比
     */
    private Short processType;
    
    /**
     * 商品ID
     */
    private Long sgid;

	/**
     * 单据号
     */
    private String sheetNo;

	/**
     * 修改日期
     */
    private Date updateDate;
    
    public String getBarNo() {
      return barNo;
    }
    
    public String getCategoryCode() {
		return categoryCode;
	}

    public Long getCategoryId() {
      return categoryId;
    }

    public String getCategoryName() {
		return categoryName;
	}

    /**
     * 获取创建日期
     *
     * @return createDate - 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 获取零售商ID
     *
     * @return entId - 零售商ID
     */
    public Long getEntId() {
        return entId;
    }

    /**
     * 获取商品编码
     *
     * @return goodsCode - 商品编码
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    public String getGoodsName() {
      return goodsName;
    }

    /**
     * 获取语言类型
     *
     * @return lang - 语言类型
     */
    public String getLang() {
        return lang;
    }

    /**
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 获取加工配方单ID
     *
     * @return prid - 加工配方单ID
     */
    public Long getPrid() {
        return prid;
    }

    public Short getProcessType() {
		return processType;
	}

    /**
     * 获取商品ID
     *
     * @return sgid - 商品ID
     */
    public Long getSgid() {
        return sgid;
    }

    /**
     * 获取单据号
     *
     * @return sheetNo - 单据号
     */
    public String getSheetNo() {
        return sheetNo;
    }

    /**
     * 获取修改日期
     *
     * @return updateDate - 修改日期
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setBarNo(String barNo) {
      this.barNo = barNo;
    }

    public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

    public void setCategoryId(Long categoryId) {
      this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

    /**
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 设置商品编码
     *
     * @param goodsCode 商品编码
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public void setGoodsName(String goodsName) {
      this.goodsName = goodsName;
    }

    /**
     * 设置语言类型
     *
     * @param lang 语言类型
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 设置加工配方单ID
     *
     * @param prid 加工配方单ID
     */
    public void setPrid(Long prid) {
        this.prid = prid;
    }

    public void setProcessType(Short processType) {
		this.processType = processType;
	}

	/**
     * 设置商品ID
     *
     * @param sgid 商品ID
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

	/**
     * 设置单据号
     *
     * @param sheetNo 单据号
     */
    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

	/**
     * 设置修改日期
     *
     * @param updateDate 修改日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}