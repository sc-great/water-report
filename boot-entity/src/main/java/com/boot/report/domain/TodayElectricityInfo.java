package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂当日用电信息对象 TodayElectricityInfo
 * 
 * @author EPL
 * @date 2020-03-24
 */
public class TodayElectricityInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** 水厂当日电耗 */
    private Double todayElectricity;

    /** 水厂累计电耗 */
    private Double totalElectricity;

    /** 泵站当日电耗 */
    private Double pumpTodayEletricity;

    /** 泵站累计电耗 */
    private Double pumpTotalEletricity;

    /** 所属水厂 */
    private String factoryId;

    /** 所属区域 */
    private String areaId;

    /** 填报人编号 */
    private String fillUserId;

    /** 填报人 */
    private String fillUserName;

    /** 有效标识(1-有效 2-无效 ) */
    private String effectIcon;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public void setTodayElectricity(Double todayElectricity) {
        this.todayElectricity = todayElectricity;
    }

    public Double getTodayElectricity() {
        return todayElectricity;
    }

    public void setTotalElectricity(Double totalElectricity) {
        this.totalElectricity = totalElectricity;
    }

    public Double getTotalElectricity() {
        return totalElectricity;
    }

    public void setPumpTodayEletricity(Double pumpTodayEletricity) {
        this.pumpTodayEletricity = pumpTodayEletricity;
    }

    public Double getPumpTodayEletricity() {
        return pumpTodayEletricity;
    }

    public void setPumpTotalEletricity(Double pumpTotalEletricity) {
        this.pumpTotalEletricity = pumpTotalEletricity;
    }

    public Double getPumpTotalEletricity() {
        return pumpTotalEletricity;
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

    public void setFillUserId(String fillUserId) {
        this.fillUserId = fillUserId;
    }

    public String getFillUserId() {
        return fillUserId;
    }

    public void setFillUserName(String fillUserName) {
        this.fillUserName = fillUserName;
    }

    public String getFillUserName() {
        return fillUserName;
    }

    public void setEffectIcon(String effectIcon) {
        this.effectIcon = effectIcon;
    }

    public String getEffectIcon() {
        return effectIcon;
    }
}
