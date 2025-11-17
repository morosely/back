package com.efuture.omdmain.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import com.product.annotation.Length;
import com.product.annotation.NotNull;
import com.product.annotation.UniqueKey;
import com.product.service.OperationFlag;

@Table(name = "channelinfo")
@UniqueKey(table="channelInfo",keys = { "entId","channelCode" },operationFlags={OperationFlag.Insert},message="企业【${entId}】及编码【${channelCode}】数据不唯一")
public class ChannelInfoModel {
    /**
     * 渠道ID
     */
    @Id
    private Long channelId;

    /**
     * 是否营销应用渠道1:是,0:否
     */
    private Short isMarketChannel;

	/**
     * 零售商ID
     */
    private Long entId;

    /**
     * 渠道编码
     */
//    @NotNull(operationFlags = { OperationFlag.Insert },message="渠道编码不能为空")
    @Length(min="1",max="20",operationFlags = { OperationFlag.Insert,OperationFlag.Update },message="渠道编码长度必须在1~20之间")
    private String channelCode;

    /**
     * 渠道名称
     */
//    @NotNull(operationFlags = { OperationFlag.Insert },message="渠道名称不能为空")
    @Length(min="1",max="100",operationFlags = { OperationFlag.Insert,OperationFlag.Update },message="渠道名称长度必须在1~100之间")
    private String channelName;

    /**
     * 是否自建展示分类
     */
    private Boolean selfBuildFlag;
    
    /**
     * 渠道类型 0-线下渠道/1-线上渠道
     */
    private Integer channelType;

    /**
     * 描述
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

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
     * 款机类型 1-普通收银,  2-快捷收银, 3-餐饮区收银
     */
    private String posmode;

    public String getPosmode() {
        return posmode;
    }

    public void setPosmode(String posmode) {
        this.posmode = posmode;
    }

    /**
     * 获取渠道ID
     *
     * @return channelId - 渠道ID
     */
    public Long getChannelId() {
        return channelId;
    }

    /**
     * 设置渠道ID
     *
     * @param channelId 渠道ID
     */
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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
     * 获取渠道编码
     *
     * @return channelCode - 渠道编码
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * 设置渠道编码
     *
     * @param channelCode 渠道编码
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    /**
     * 获取渠道名称
     *
     * @return channelName - 渠道名称
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * 设置渠道名称
     *
     * @param channelName 渠道名称
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * 获取是否自建展示分类
     *
     * @return selfBuildFlag - 是否自建展示分类
     */
    public Boolean getSelfBuildFlag() {
        return selfBuildFlag;
    }

    /**
     * 设置是否自建展示分类
     *
     * @param selfBuildFlag 是否自建展示分类
     */
    public void setSelfBuildFlag(Boolean selfBuildFlag) {
        this.selfBuildFlag = selfBuildFlag;
    }
    
    /**
     * 渠道类型 0-线下渠道/1-线上渠道
     * 
     * @param channelType 渠道类型 0-线下渠道/1-线上渠道
     */
    public Integer getChannelType() {
		return channelType;
	}
    
    /**
     * 渠道类型 0-线下渠道/1-线上渠道
     * 
     * @param channelType 渠道类型 0-线下渠道/1-线上渠道
     */
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

	/**
     * 获取描述
     *
     * @return remark - 描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置描述
     *
     * @param remark 描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
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
    
    public Short getIsMarketChannel() {
		return isMarketChannel;
	}

	public void setIsMarketChannel(Short isMarketChannel) {
		this.isMarketChannel = isMarketChannel;
	}
}