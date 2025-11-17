package com.efuture.model;

import java.util.Date;

/**
* @Description: 
* @author: liyongjian
* @date 2022-04-11 02:52:49
*/

public class ExtAmcProfileModel extends CommonExtModel{

    private Long id; // 主键
    private String corporationCode; // 法人公司编码
    private String memberId; // 会员ID
    private String memberCard; // 会员卡号
    private Integer memberSystem; // 会员体系
    private Integer memberLevel; // 会员等级
    private String memberLevelName; // 会员等级名称
    private String memberName; // 会员姓名
    private String memberNameEn; // 会员英文姓名
    private Integer memberGender; // 会员性别
    private Date memberBirthday; // 生日优惠日
    private String effectiveTime; // 等级生效时间
    private String expireTime; // expireTime
    private String erpCode;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorporationCode() {
        return corporationCode;
    }

    public void setCorporationCode(String corporationCode) {
        this.corporationCode = corporationCode;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard;
    }

    public Integer getMemberSystem() {
        return memberSystem;
    }

    public void setMemberSystem(Integer memberSystem) {
        this.memberSystem = memberSystem;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getMemberLevelName() {
        return memberLevelName;
    }

    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberNameEn() {
        return memberNameEn;
    }

    public void setMemberNameEn(String memberNameEn) {
        this.memberNameEn = memberNameEn;
    }

    public Integer getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public Date getMemberBirthday() {
        return memberBirthday;
    }

    public void setMemberBirthday(Date memberBirthday) {
        this.memberBirthday = memberBirthday;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public Integer getDealStatus() {
        return null;
    }

    @Override
    public void setDealStatus(Integer dealStatus) {

    }

    @Override
    public String getUniqueKey() {return memberId;}

    @Override
    public String getUniqueKeyValue() {
        return memberId;
    }

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public void setCreateDate(Date createDate) {

    }

    @Override
    public Date getUpdateDate() {
        return null;
    }

    @Override
    public void setUpdateDate(Date updateDate) {

    }
}