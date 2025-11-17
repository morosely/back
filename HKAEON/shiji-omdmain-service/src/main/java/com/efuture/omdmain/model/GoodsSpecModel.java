package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "goodsspec")
public class GoodsSpecModel {
    /**
     * 商品规格ID
     */
    @Id
    private Long gsid;

    /**
     * 零售商ID
     */
    private Long entId;

    /**
     * 经营公司编码
     */
    private String erpCode;

    /**
     * 组织机构ID
     */
    private Long shopId;

    private String shopName;
    
    /**
     * 商品ID
     */
    private Long sgid;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 小订货含量
     */
    private Integer spurNum;

    /**
     * 小订货规格
     */
    private String spurSpec;

    /**
     * 小订货单位
     */
    private String spurUnit;

    /**
     * 中订货含量
     */
    private Integer mpurNum;

    /**
     * 中订货规格
     */
    private String mpurSpec;

    /**
     * 中订货单位
     */
    private String mpurUnit;

    /**
     * 大订货含量
     */
    private Integer lpurNum;

    /**
     * 大订货规格
     */
    private String lpurSpec;

    /**
     * 大订货单位
     */
    private String lpurUnit;

    /**
     * 订货包装数
     */
    private Integer purNum;

    /**
     * 订货最小起订量
     */
    private Integer minPurNum;

    /**
     * 订货最小起订单位
     */
    private String minPurUnit;

    /**
     * 小配送含量
     */
    private Integer sDeliveryNum;

    /**
     * 小配送规格
     */
    private String sDeliverySpec;

    /**
     * 小配送单位
     */
    private String sDeliveryUnit;

    /**
     * 中配送含量
     */
    private Integer mDeliveryNum;

    /**
     * 中配送规格
     */
    private String mDeliverySpec;

    /**
     * 中配送单位
     */
    private String mDeliveryUnit;

    /**
     * 大配送含量
     */
    private Integer lDeliveryNum;

    /**
     * 大配送规格
     */
    private String lDeliverySpec;

    /**
     * 大配送单位
     */
    private String lDeliveryUnit;

    /**
     * 配送包装数
     */
    private Integer deliveryNum;

    /**
     * 配送最小起订量
     */
    private Integer minDeliveryNum;

    /**
     * 配送最小起订单位
     */
    private String minDeliveryUnit;

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
     * 获取商品规格ID
     *
     * @return gsid - 商品规格ID
     */
    public Long getGsid() {
        return gsid;
    }

    /**
     * 设置商品规格ID
     *
     * @param gsid 商品规格ID
     */
    public void setGsid(Long gsid) {
        this.gsid = gsid;
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
     * 获取组织机构ID
     *
     * @return shopId - 组织机构ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置组织机构ID
     *
     * @param shopId 组织机构ID
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
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
     * 设置商品ID
     *
     * @param sgid 商品ID
     */
    public void setSgid(Long sgid) {
        this.sgid = sgid;
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
     * 获取小订货含量
     *
     * @return spurNum - 小订货含量
     */
    public Integer getSpurNum() {
        return spurNum;
    }

    /**
     * 设置小订货含量
     *
     * @param spurNum 小订货含量
     */
    public void setSpurNum(Integer spurNum) {
        this.spurNum = spurNum;
    }

    /**
     * 获取小订货规格
     *
     * @return spurSpec - 小订货规格
     */
    public String getSpurSpec() {
        return spurSpec;
    }

    /**
     * 设置小订货规格
     *
     * @param spurSpec 小订货规格
     */
    public void setSpurSpec(String spurSpec) {
        this.spurSpec = spurSpec;
    }

    /**
     * 获取小订货单位
     *
     * @return spurUnit - 小订货单位
     */
    public String getSpurUnit() {
        return spurUnit;
    }

    /**
     * 设置小订货单位
     *
     * @param spurUnit 小订货单位
     */
    public void setSpurUnit(String spurUnit) {
        this.spurUnit = spurUnit;
    }

    /**
     * 获取中订货含量
     *
     * @return mpurNum - 中订货含量
     */
    public Integer getMpurNum() {
        return mpurNum;
    }

    /**
     * 设置中订货含量
     *
     * @param mpurNum 中订货含量
     */
    public void setMpurNum(Integer mpurNum) {
        this.mpurNum = mpurNum;
    }

    /**
     * 获取中订货规格
     *
     * @return mpurSpec - 中订货规格
     */
    public String getMpurSpec() {
        return mpurSpec;
    }

    /**
     * 设置中订货规格
     *
     * @param mpurSpec 中订货规格
     */
    public void setMpurSpec(String mpurSpec) {
        this.mpurSpec = mpurSpec;
    }

    /**
     * 获取中订货单位
     *
     * @return mpurUnit - 中订货单位
     */
    public String getMpurUnit() {
        return mpurUnit;
    }

    /**
     * 设置中订货单位
     *
     * @param mpurUnit 中订货单位
     */
    public void setMpurUnit(String mpurUnit) {
        this.mpurUnit = mpurUnit;
    }

    /**
     * 获取大订货含量
     *
     * @return lpurNum - 大订货含量
     */
    public Integer getLpurNum() {
        return lpurNum;
    }

    /**
     * 设置大订货含量
     *
     * @param lpurNum 大订货含量
     */
    public void setLpurNum(Integer lpurNum) {
        this.lpurNum = lpurNum;
    }

    /**
     * 获取大订货规格
     *
     * @return lpurSpec - 大订货规格
     */
    public String getLpurSpec() {
        return lpurSpec;
    }

    /**
     * 设置大订货规格
     *
     * @param lpurSpec 大订货规格
     */
    public void setLpurSpec(String lpurSpec) {
        this.lpurSpec = lpurSpec;
    }

    /**
     * 获取大订货单位
     *
     * @return lpurUnit - 大订货单位
     */
    public String getLpurUnit() {
        return lpurUnit;
    }

    /**
     * 设置大订货单位
     *
     * @param lpurUnit 大订货单位
     */
    public void setLpurUnit(String lpurUnit) {
        this.lpurUnit = lpurUnit;
    }

    /**
     * 获取订货包装数
     *
     * @return purNum - 订货包装数
     */
    public Integer getPurNum() {
        return purNum;
    }

    /**
     * 设置订货包装数
     *
     * @param purNum 订货包装数
     */
    public void setPurNum(Integer purNum) {
        this.purNum = purNum;
    }

    /**
     * 获取订货最小起订量
     *
     * @return minPurNum - 订货最小起订量
     */
    public Integer getMinPurNum() {
        return minPurNum;
    }

    /**
     * 设置订货最小起订量
     *
     * @param minPurNum 订货最小起订量
     */
    public void setMinPurNum(Integer minPurNum) {
        this.minPurNum = minPurNum;
    }

    /**
     * 获取订货最小起订单位
     *
     * @return minPurUnit - 订货最小起订单位
     */
    public String getMinPurUnit() {
        return minPurUnit;
    }

    /**
     * 设置订货最小起订单位
     *
     * @param minPurUnit 订货最小起订单位
     */
    public void setMinPurUnit(String minPurUnit) {
        this.minPurUnit = minPurUnit;
    }

    /**
     * 获取小配送含量
     *
     * @return sDeliveryNum - 小配送含量
     */
    public Integer getsDeliveryNum() {
        return sDeliveryNum;
    }

    /**
     * 设置小配送含量
     *
     * @param sDeliveryNum 小配送含量
     */
    public void setsDeliveryNum(Integer sDeliveryNum) {
        this.sDeliveryNum = sDeliveryNum;
    }

    /**
     * 获取小配送规格
     *
     * @return sDeliverySpec - 小配送规格
     */
    public String getsDeliverySpec() {
        return sDeliverySpec;
    }

    /**
     * 设置小配送规格
     *
     * @param sDeliverySpec 小配送规格
     */
    public void setsDeliverySpec(String sDeliverySpec) {
        this.sDeliverySpec = sDeliverySpec;
    }

    /**
     * 获取小配送单位
     *
     * @return sDeliveryUnit - 小配送单位
     */
    public String getsDeliveryUnit() {
        return sDeliveryUnit;
    }

    /**
     * 设置小配送单位
     *
     * @param sDeliveryUnit 小配送单位
     */
    public void setsDeliveryUnit(String sDeliveryUnit) {
        this.sDeliveryUnit = sDeliveryUnit;
    }

    /**
     * 获取中配送含量
     *
     * @return mDeliveryNum - 中配送含量
     */
    public Integer getmDeliveryNum() {
        return mDeliveryNum;
    }

    /**
     * 设置中配送含量
     *
     * @param mDeliveryNum 中配送含量
     */
    public void setmDeliveryNum(Integer mDeliveryNum) {
        this.mDeliveryNum = mDeliveryNum;
    }

    /**
     * 获取中配送规格
     *
     * @return mDeliverySpec - 中配送规格
     */
    public String getmDeliverySpec() {
        return mDeliverySpec;
    }

    /**
     * 设置中配送规格
     *
     * @param mDeliverySpec 中配送规格
     */
    public void setmDeliverySpec(String mDeliverySpec) {
        this.mDeliverySpec = mDeliverySpec;
    }

    /**
     * 获取中配送单位
     *
     * @return mDeliveryUnit - 中配送单位
     */
    public String getmDeliveryUnit() {
        return mDeliveryUnit;
    }

    /**
     * 设置中配送单位
     *
     * @param mDeliveryUnit 中配送单位
     */
    public void setmDeliveryUnit(String mDeliveryUnit) {
        this.mDeliveryUnit = mDeliveryUnit;
    }

    /**
     * 获取大配送含量
     *
     * @return lDeliveryNum - 大配送含量
     */
    public Integer getlDeliveryNum() {
        return lDeliveryNum;
    }

    /**
     * 设置大配送含量
     *
     * @param lDeliveryNum 大配送含量
     */
    public void setlDeliveryNum(Integer lDeliveryNum) {
        this.lDeliveryNum = lDeliveryNum;
    }

    /**
     * 获取大配送规格
     *
     * @return lDeliverySpec - 大配送规格
     */
    public String getlDeliverySpec() {
        return lDeliverySpec;
    }

    /**
     * 设置大配送规格
     *
     * @param lDeliverySpec 大配送规格
     */
    public void setlDeliverySpec(String lDeliverySpec) {
        this.lDeliverySpec = lDeliverySpec;
    }

    /**
     * 获取大配送单位
     *
     * @return lDeliveryUnit - 大配送单位
     */
    public String getlDeliveryUnit() {
        return lDeliveryUnit;
    }

    /**
     * 设置大配送单位
     *
     * @param lDeliveryUnit 大配送单位
     */
    public void setlDeliveryUnit(String lDeliveryUnit) {
        this.lDeliveryUnit = lDeliveryUnit;
    }

    /**
     * 获取配送包装数
     *
     * @return deliveryNum - 配送包装数
     */
    public Integer getDeliveryNum() {
        return deliveryNum;
    }

    /**
     * 设置配送包装数
     *
     * @param deliveryNum 配送包装数
     */
    public void setDeliveryNum(Integer deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    /**
     * 获取配送最小起订量
     *
     * @return minDeliveryNum - 配送最小起订量
     */
    public Integer getMinDeliveryNum() {
        return minDeliveryNum;
    }

    /**
     * 设置配送最小起订量
     *
     * @param minDeliveryNum 配送最小起订量
     */
    public void setMinDeliveryNum(Integer minDeliveryNum) {
        this.minDeliveryNum = minDeliveryNum;
    }

    /**
     * 获取配送最小起订单位
     *
     * @return minDeliveryUnit - 配送最小起订单位
     */
    public String getMinDeliveryUnit() {
        return minDeliveryUnit;
    }

    /**
     * 设置配送最小起订单位
     *
     * @param minDeliveryUnit 配送最小起订单位
     */
    public void setMinDeliveryUnit(String minDeliveryUnit) {
        this.minDeliveryUnit = minDeliveryUnit;
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

    public String getShopName() {
      return shopName;
    }

    public void setShopName(String shopName) {
      this.shopName = shopName;
    }
}