package com.efuture.omdmain.model.out;

import java.util.Date;
import javax.persistence.*;

@Table(name = "syncdatatime")
public class SyncDataTimeModel {
	
	private String returnMsg;
    public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	/**
     * 主键
     */
    @Id
    private Long syncId;

    /**
     * 同步开始时间
     */
    private Date startTime;

    /**
     * 同步结束时间
     */
    private Date endTime;

    /**
     * 完成同步标识 0：开始同步 1：完成同步
     */
    private Byte finishFlag;

    /**
     * 模块名称或表名
     */
    private String modelName;

    /**
     * 获取主键
     *
     * @return syncId - 主键
     */
    public Long getSyncId() {
        return syncId;
    }

    /**
     * 设置主键
     *
     * @param syncId 主键
     */
    public void setSyncId(Long syncId) {
        this.syncId = syncId;
    }

    /**
     * 获取同步开始时间
     *
     * @return startTime - 同步开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置同步开始时间
     *
     * @param startTime 同步开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取同步结束时间
     *
     * @return endTime - 同步结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置同步结束时间
     *
     * @param endTime 同步结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取完成同步标识 0：开始同步 1：完成同步
     *
     * @return finishFlag - 完成同步标识 0：开始同步 1：完成同步
     */
    public Byte getFinishFlag() {
        return finishFlag;
    }

    /**
     * 设置完成同步标识 0：开始同步 1：完成同步
     *
     * @param finishFlag 完成同步标识 0：开始同步 1：完成同步
     */
    public void setFinishFlag(Byte finishFlag) {
        this.finishFlag = finishFlag;
    }

    /**
     * 获取模块名称或表名
     *
     * @return modelName - 模块名称或表名
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 设置模块名称或表名
     *
     * @param modelName 模块名称或表名
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}