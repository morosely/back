package com.efuture.omdmain.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "fileupdownlogs")
public class FileUpDownLogsModel {
    /**
     * 主键ID
     */
    @Id
    private Long fid;

    /**
     * 文件类型(1-上传，2-下载)
     */
    private Short udType;

    /**
     * 关联系统（profit，as400，HHT，aeoncity
     */
    private String channel;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件编码
     */
    private String fileCode;

    /**
     * 文件全称（实体文件名）
     */
    private String fileFullName;

    /**
     * 文件类别（1-正常数据，2-纠错数据，3-全量数据）
     */
    private Short fileType;

    /**
     * 文件流水号
     */
    private String fileRowNo;

    /**
     * 同步日期
     */
    private Date syncDate;

    /**
     * 同步时间
     */
    private Date synctime;

    /**
     * 交易结束时间
     */
    private Date endDateTime;

    /**
     * 状态（0成功，1失败，9未知）
     */
    private Short status;

    /**
     * 文件顺序号
     */
    private Integer seqNo;

    /**
     * 重试次数
     */
    private Short retryNum;

    /**
     * 交易时间2
     */
    private Date transDate;

    /**
     * 语言类型
     */
    private String lang;

    /**
     * 文件上传日期
     */
    private Date fileDate;

    /**
     * 获取主键ID
     *
     * @return fid - 主键ID
     */
    public Long getFid() {
        return fid;
    }

    /**
     * 设置主键ID
     *
     * @param fid 主键ID
     */
    public void setFid(Long fid) {
        this.fid = fid;
    }

    /**
     * 获取文件类型(1-上传，2-下载)
     *
     * @return udType - 文件类型(1-上传，2-下载)
     */
    public Short getUdType() {
        return udType;
    }

    /**
     * 设置文件类型(1-上传，2-下载)
     *
     * @param udType 文件类型(1-上传，2-下载)
     */
    public void setUdType(Short udType) {
        this.udType = udType;
    }

    /**
     * 获取关联系统（profit，as400，HHT，aeoncity
     *
     * @return channel - 关联系统（profit，as400，HHT，aeoncity
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置关联系统（profit，as400，HHT，aeoncity
     *
     * @param channel 关联系统（profit，as400，HHT，aeoncity
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 获取文件名
     *
     * @return fileName - 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件编码
     *
     * @return fileCode - 文件编码
     */
    public String getFileCode() {
        return fileCode;
    }

    /**
     * 设置文件编码
     *
     * @param fileCode 文件编码
     */
    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    /**
     * 获取文件全称（实体文件名）
     *
     * @return fileFullName - 文件全称（实体文件名）
     */
    public String getFileFullName() {
        return fileFullName;
    }

    /**
     * 设置文件全称（实体文件名）
     *
     * @param fileFullName 文件全称（实体文件名）
     */
    public void setFileFullName(String fileFullName) {
        this.fileFullName = fileFullName;
    }

    /**
     * 获取文件类别（1-正常数据，2-纠错数据，3-全量数据）
     *
     * @return fileType - 文件类别（1-正常数据，2-纠错数据，3-全量数据）
     */
    public Short getFileType() {
        return fileType;
    }

    /**
     * 设置文件类别（1-正常数据，2-纠错数据，3-全量数据）
     *
     * @param fileType 文件类别（1-正常数据，2-纠错数据，3-全量数据）
     */
    public void setFileType(Short fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取文件流水号
     *
     * @return fileRowNo - 文件流水号
     */
    public String getFileRowNo() {
        return fileRowNo;
    }

    /**
     * 设置文件流水号
     *
     * @param fileRowNo 文件流水号
     */
    public void setFileRowNo(String fileRowNo) {
        this.fileRowNo = fileRowNo;
    }

    /**
     * 获取同步日期
     *
     * @return syncDate - 同步日期
     */
    public Date getSyncDate() {
        return syncDate;
    }

    /**
     * 设置同步日期
     *
     * @param syncDate 同步日期
     */
    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    /**
     * 获取同步时间
     *
     * @return synctime - 同步时间
     */
    public Date getSynctime() {
        return synctime;
    }

    /**
     * 设置同步时间
     *
     * @param synctime 同步时间
     */
    public void setSynctime(Date synctime) {
        this.synctime = synctime;
    }

    /**
     * 获取交易结束时间
     *
     * @return endDateTime - 交易结束时间
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * 设置交易结束时间
     *
     * @param endDateTime 交易结束时间
     */
    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * 获取状态（0成功，1失败，9未知）
     *
     * @return status - 状态（0成功，1失败，9未知）
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 设置状态（0成功，1失败，9未知）
     *
     * @param status 状态（0成功，1失败，9未知）
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 获取文件顺序号
     *
     * @return seqNo - 文件顺序号
     */
    public Integer getSeqNo() {
        return seqNo;
    }

    /**
     * 设置文件顺序号
     *
     * @param seqNo 文件顺序号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    /**
     * 获取重试次数
     *
     * @return retryNum - 重试次数
     */
    public Short getRetryNum() {
        return retryNum;
    }

    /**
     * 设置重试次数
     *
     * @param retryNum 重试次数
     */
    public void setRetryNum(Short retryNum) {
        this.retryNum = retryNum;
    }

    /**
     * 获取交易时间2
     *
     * @return transDate - 交易时间2
     */
    public Date getTransDate() {
        return transDate;
    }

    /**
     * 设置交易时间2
     *
     * @param transDate 交易时间2
     */
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
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
     * 获取文件上传日期
     *
     * @return fileDate - 文件上传日期
     */
    public Date getFileDate() {
        return fileDate;
    }

    /**
     * 设置文件上传日期
     *
     * @param fileDate 文件上传日期
     */
    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }
}