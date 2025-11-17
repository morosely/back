package com.efuture.omdmain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import org.springframework.data.annotation.Transient;

import com.product.annotation.ReferQuery;
import com.product.service.OperationFlag;

@Table(name = "salegoodsitems")
public class SaleGoodsItemsModel {
	private Short status;

    public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
    /**
     * 商品项ID
     */
    @Id
    private Long sgiid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 组商品ID
     */
    private Long gsgid;

    /**
     * 组商品编码（虚拟母品，组包码，菜谱等）
     */
    private String ggoodsCode;

    /**
     * 可售商品ID
     */
    private Long ssgid;

    //可售商品名称
    @Transient
    @ReferQuery(table="salegoods",query="{ssgid:'$ssgid',entId:'$entId'}",set="{goodsName:'goodsName'}",operationFlags={OperationFlag.afterQuery})
    private String goodsName;
    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 条码
     */
    private String barNo;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 折扣分摊比例
     */
    private Float discountShareRate;

    /**
     * 零售价
     */
    private BigDecimal salePrice;

    /**
     * 排序
     */
    private Integer sortFlag;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 获取商品项ID
     *
     * @return sgiid - 商品项ID
     */
    public Long getSgiid() {
        return sgiid;
    }

    /**
     * 设置商品项ID
     *
     * @param sgiid 商品项ID
     */
    public void setSgiid(Long sgiid) {
        this.sgiid = sgiid;
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
     * 设置零售商ID
     *
     * @param entId 零售商ID
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * 获取经营公司编码
     *
     * @return erpCode - 经营公司编码
     */
    public String getErpCode() {
        return erpCode;
    }

    /**
     * 设置经营公司编码
     *
     * @param erpCode 经营公司编码
     */
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    /**
     * 获取商品类型
     *
     * @return goodsType - 商品类型
     */
    public Integer getGoodsType() {
        return goodsType;
    }

    /**
     * 设置商品类型
     *
     * @param goodsType 商品类型
     */
    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * 获取组商品ID
     *
     * @return gsgid - 组商品ID
     */
    public Long getGsgid() {
        return gsgid;
    }

    /**
     * 设置组商品ID
     *
     * @param gsgid 组商品ID
     */
    public void setGsgid(Long gsgid) {
        this.gsgid = gsgid;
    }

    /**
     * 获取组商品编码（虚拟母品，组包码，菜谱等）
     *
     * @return ggoodsCode - 组商品编码（虚拟母品，组包码，菜谱等）
     */
    public String getGgoodsCode() {
        return ggoodsCode;
    }

    /**
     * 设置组商品编码（虚拟母品，组包码，菜谱等）
     *
     * @param ggoodsCode 组商品编码（虚拟母品，组包码，菜谱等）
     */
    public void setGgoodsCode(String ggoodsCode) {
        this.ggoodsCode = ggoodsCode;
    }

    /**
     * 获取可售商品ID
     *
     * @return ssgid - 可售商品ID
     */
    public Long getSsgid() {
        return ssgid;
    }

    /**
     * 设置可售商品ID
     *
     * @param ssgid 可售商品ID
     */
    public void setSsgid(Long ssgid) {
        this.ssgid = ssgid;
    }

    /**
     * 获取商品编码
     *
     * @return goodsCode - 商品编码
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * 设置商品编码
     *
     * @param goodsCode 商品编码
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * 获取条码
     *
     * @return barNo - 条码
     */
    public String getBarNo() {
        return barNo;
    }

    /**
     * 设置条码
     *
     * @param barNo 条码
     */
    public void setBarNo(String barNo) {
        this.barNo = barNo;
    }

    /**
     * 获取数量
     *
     * @return num - 数量
     */
    public Integer getNum() {
        return num;
    }

    /**
     * 设置数量
     *
     * @param num 数量
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * 获取折扣分摊比例
     *
     * @return discountShareRate - 折扣分摊比例
     */
    public Float getDiscountShareRate() {
        return discountShareRate;
    }

    /**
     * 设置折扣分摊比例
     *
     * @param discountShareRate 折扣分摊比例
     */
    public void setDiscountShareRate(Float discountShareRate) {
        this.discountShareRate = discountShareRate;
    }

    /**
     * 获取零售价
     *
     * @return salePrice - 零售价
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * 设置零售价
     *
     * @param salePrice 零售价
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * 获取排序
     *
     * @return sortFlag - 排序
     */
    public Integer getSortFlag() {
        return sortFlag;
    }

    /**
     * 设置排序
     *
     * @param sortFlag 排序
     */
    public void setSortFlag(Integer sortFlag) {
        this.sortFlag = sortFlag;
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
     * 设置语言类型
     *
     * @param lang 语言类型
     */
    public void setLang(String lang) {
        this.lang = lang;
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
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 获取修改日期
     *
     * @return updateDate - 修改日期
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改日期
     *
     * @param updateDate 修改日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
}