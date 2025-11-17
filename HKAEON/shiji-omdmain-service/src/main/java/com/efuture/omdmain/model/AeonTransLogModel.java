package com.efuture.omdmain.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import com.product.util.ArithUtils;

@Table(name = "aeon_trans_log")
public class AeonTransLogModel {
	public static Map<Integer,String> statusMap = new HashMap<Integer,String>();
	static{
		statusMap.put(0,"未下载");
		statusMap.put(1,"下载中");
		statusMap.put(2,"已下载未解析");
		statusMap.put(3,"已解析");
	}
	
	//0-未下载，1-下载中，2-以下载未解析，3-已解析
	public String statusValue;
    public String getStatusValue() {
		return statusMap.get(this.getStatus());
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public double sizeToM;
	private double defaultValue = 0.01d;//少于0.01M的数据默认为0.01M
	public double getSizeToM() {
		double sizeToM = ArithUtils.div(this.size, 1024*1024, 8);
		//System.out.println(this.size + " --- "+ sizeToM + " --- "+(sizeToM <= 0.01d)+"&&"+(sizeToM > 0.0d)+" --- "+ (this.size == null ? 0 : (sizeToM <= 0.001d && sizeToM > 0.0d) ? 0.01d : ArithUtils.round(sizeToM, 2)));
		return this.size == null ? 0 : (sizeToM <= defaultValue && sizeToM > 0.0d) ? defaultValue : ArithUtils.round(sizeToM, 2);
	}

	@Id
    private Long id;

    /**
     * 序号
     */
    private Long seq;

    /**
     * 文件名
     */
    private String name;

    /**
     * 英文名
     */
    private String ename;

    /**
     * 门店号
     */
    private String shop;

    /**
     * 文件大小(MB)
     */
    private Long size;

    /**
     * 下载开始时间
     */
    private Date down_start;

    /**
     * 下载完成时间
     */
    private Date down_end;

    /**
     * 载入开始时间
     */
    private Date load_start;

    /**
     * 载入完成时间
     */
    private Date load_end;

    /**
     * 差异数
     */
    private Long diff_count;

    /**
     * 日结日期
     */
    private Date rj_date;

    /**
     * 最后修改时间
     */
    private Date update;

    /**
     * 0-未下载，1-下载中，2-以下载未解析，3-已解析
     */
    private Integer status;
    
    /**
     * ftp上传时间
     */
    private Date ftpUploadTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Date getFtpUploadTime() {
		return ftpUploadTime;
	}

	public void setFtpUploadTime(Date ftpUploadTime) {
		this.ftpUploadTime = ftpUploadTime;
	}

	/**
     * 获取序号
     *
     * @return seq - 序号
     */
    public Long getSeq() {
        return seq;
    }

    /**
     * 设置序号
     *
     * @param seq 序号
     */
    public void setSeq(Long seq) {
        this.seq = seq;
    }

    /**
     * 获取文件名
     *
     * @return name - 文件名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文件名
     *
     * @param name 文件名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取英文名
     *
     * @return ename - 英文名
     */
    public String getEname() {
        return ename;
    }

    /**
     * 设置英文名
     *
     * @param ename 英文名
     */
    public void setEname(String ename) {
        this.ename = ename;
    }

    /**
     * 获取门店号
     *
     * @return shop - 门店号
     */
    public String getShop() {
        return shop;
    }

    /**
     * 设置门店号
     *
     * @param shop 门店号
     */
    public void setShop(String shop) {
        this.shop = shop;
    }

    /**
     * 获取文件大小(MB)
     *
     * @return size - 文件大小(MB)
     */
    public Long getSize() {
        return size;
    }

    /**
     * 设置文件大小(MB)
     *
     * @param size 文件大小(MB)
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 获取下载开始时间
     *
     * @return down_start - 下载开始时间
     */
    public Date getDown_start() {
        return down_start;
    }

    /**
     * 设置下载开始时间
     *
     * @param down_start 下载开始时间
     */
    public void setDown_start(Date down_start) {
        this.down_start = down_start;
    }

    /**
     * 获取下载完成时间
     *
     * @return down_end - 下载完成时间
     */
    public Date getDown_end() {
        return down_end;
    }

    /**
     * 设置下载完成时间
     *
     * @param down_end 下载完成时间
     */
    public void setDown_end(Date down_end) {
        this.down_end = down_end;
    }

    /**
     * 获取载入开始时间
     *
     * @return load_start - 载入开始时间
     */
    public Date getLoad_start() {
        return load_start;
    }

    /**
     * 设置载入开始时间
     *
     * @param load_start 载入开始时间
     */
    public void setLoad_start(Date load_start) {
        this.load_start = load_start;
    }

    /**
     * 获取载入完成时间
     *
     * @return load_end - 载入完成时间
     */
    public Date getLoad_end() {
        return load_end;
    }

    /**
     * 设置载入完成时间
     *
     * @param load_end 载入完成时间
     */
    public void setLoad_end(Date load_end) {
        this.load_end = load_end;
    }

    /**
     * 获取差异数
     *
     * @return diff_count - 差异数
     */
    public Long getDiff_count() {
        return diff_count;
    }

    /**
     * 设置差异数
     *
     * @param diff_count 差异数
     */
    public void setDiff_count(Long diff_count) {
        this.diff_count = diff_count;
    }

    /**
     * 获取日结日期
     *
     * @return rj_date - 日结日期
     */
    public Date getRj_date() {
        return rj_date;
    }

    /**
     * 设置日结日期
     *
     * @param rj_date 日结日期
     */
    public void setRj_date(Date rj_date) {
        this.rj_date = rj_date;
    }

    /**
     * 获取最后修改时间
     *
     * @return update - 最后修改时间
     */
    public Date getUpdate() {
        return update;
    }

    /**
     * 设置最后修改时间
     *
     * @param update 最后修改时间
     */
    public void setUpdate(Date update) {
        this.update = update;
    }

    /**
     * 获取0-未下载，1-下载中，2-以下载未解析，3-已解析
     *
     * @return status - 0-未下载，1-下载中，2-以下载未解析，3-已解析
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0-未下载，1-下载中，2-以下载未解析，3-已解析
     *
     * @param status 0-未下载，1-下载中，2-以下载未解析，3-已解析
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
