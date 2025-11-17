package com.efuture.model;


import java.math.BigDecimal;
import java.util.Date;

/**
* @Description: 
* @author: liyongjian
*/
public class AmcProfileModel extends CommonModel{

    @Override
    public String getUniqueKey() {return "memberId,filler2";}

    @Override
    public String getUniqueKeyValue() { return "'"+this.memberId+"','"+ this.filler2 + "'" ;}

    private Integer memberLevel; // 会员等级(辅助字段)

    /** 主键id */
    private Long id;

    /** 会员卡号 */
    private String memberId;

    /** 会员过期日期 */
    private Date membershipExpireDate;

    /** 上月积分 */
    private BigDecimal bonusPointLastMonth;

    /** 本月积分 */
    private BigDecimal bonusPointThisMonth;

    /** 本月已使用积分 */
    private BigDecimal bonusPointUsed;

    /** 积分到期日期 */
    private Integer bonusPointExpireDate;

    /** 积分 */
    private BigDecimal BonusPoint;

    /** 状态("A" = Active (CAN LOGIN),"E" = Expiry,"C" = Cancel,"P" = Pending) */
    private String status;

    /**  */
    private BigDecimal bonusPointToBeExpired;

    /** 是否启用电子印花 */
    private String estamp;

    /** 出生日期 */
    private String filler1;

    /** 密码 */
    private String filler2;

    /** 虚拟字段3 */
    private String filler3;

    /** 這欄控制會員訊息(包括生日訊息) */
    private String filler4;

    /** 最后一次交易日期 */
    private String filler5;

    /** 卡别 */
    private String filler6;

    /** 虚拟字段7 */
    private String filler7;

    /** 姓名 */
    private String filler8;

    /**  */
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getMembershipExpireDate() {
        return membershipExpireDate;
    }

    public void setMembershipExpireDate(Date membershipExpireDate) {
        this.membershipExpireDate = membershipExpireDate;
    }

    public BigDecimal getBonusPointLastMonth() {
        return bonusPointLastMonth;
    }

    public void setBonusPointLastMonth(BigDecimal bonusPointLastMonth) {
        this.bonusPointLastMonth = bonusPointLastMonth;
    }

    public BigDecimal getBonusPointThisMonth() {
        return bonusPointThisMonth;
    }

    public void setBonusPointThisMonth(BigDecimal bonusPointThisMonth) {
        this.bonusPointThisMonth = bonusPointThisMonth;
    }

    public BigDecimal getBonusPointUsed() {
        return bonusPointUsed;
    }

    public void setBonusPointUsed(BigDecimal bonusPointUsed) {
        this.bonusPointUsed = bonusPointUsed;
    }

    public Integer getBonusPointExpireDate() {
        return bonusPointExpireDate;
    }

    public void setBonusPointExpireDate(Integer bonusPointExpireDate) {
        this.bonusPointExpireDate = bonusPointExpireDate;
    }

    public BigDecimal getBonusPoint() {
        return BonusPoint;
    }

    public void setBonusPoint(BigDecimal bonusPoint) {
        BonusPoint = bonusPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBonusPointToBeExpired() {
        return bonusPointToBeExpired;
    }

    public void setBonusPointToBeExpired(BigDecimal bonusPointToBeExpired) {
        this.bonusPointToBeExpired = bonusPointToBeExpired;
    }

    public String getEstamp() {
        return estamp;
    }

    public void setEstamp(String estamp) {
        this.estamp = estamp;
    }

    public String getFiller1() {
        return filler1;
    }

    public void setFiller1(String filler1) {
        this.filler1 = filler1;
    }

    public String getFiller2() {
        return filler2;
    }

    public void setFiller2(String filler2) {
        this.filler2 = filler2;
    }

    public String getFiller3() {
        return filler3;
    }

    public void setFiller3(String filler3) {
        this.filler3 = filler3;
    }

    public String getFiller4() {
        return filler4;
    }

    public void setFiller4(String filler4) {
        this.filler4 = filler4;
    }

    public String getFiller5() {
        return filler5;
    }

    public void setFiller5(String filler5) {
        this.filler5 = filler5;
    }

    public String getFiller6() {
        return filler6;
    }

    public void setFiller6(String filler6) {
        this.filler6 = filler6;
    }

    public String getFiller7() {
        return filler7;
    }

    public void setFiller7(String filler7) {
        this.filler7 = filler7;
    }

    public String getFiller8() {
        return filler8;
    }

    public void setFiller8(String filler8) {
        this.filler8 = filler8;
    }

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public void setCreateDate(Date createDate) {

    }
    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}