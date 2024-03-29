package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 员工健康信息对象 r_user_health_info
 * 
 * @author EPL
 * @date 2020-03-24
 */
public class UserHealthInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 员工姓名 */
    private String userName;

    /** 手机号码 */
    private String mobile;

    /** 办公方式(1-现场 2-远程) */
    private String workType;

    /** 体温 */
    private Double temp;

    /** 当前健康状况(1-健康 2-发热 3-咳嗽 4-其他) */
    private String healthStatus;

    /** 水厂编号 */
    private String factoryId;

    /** 所在区域 */
    private String areaId;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** 是否本场员工(1-是 2-否) */
    private String isInFactory;

    /** 有效标识(1-有效 2-无效 ) */
    private String effectIcon;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkType() {
        return workType;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTemp() {
        return temp;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setFillDate(String fillDate) {
        this.fillDate = fillDate;
    }

    public String getFillDate() {
        return fillDate;
    }

    public void setFillTime(String fillTime) {
        this.fillTime = fillTime;
    }

    public String getFillTime() {
        return fillTime;
    }

    public void setIsInFactory(String isInFactory) {
        this.isInFactory = isInFactory;
    }

    public String getIsInFactory() {
        return isInFactory;
    }

    public void setEffectIcon(String effectIcon) {
        this.effectIcon = effectIcon;
    }

    public String getEffectIcon() {
        return effectIcon;
    }
}
