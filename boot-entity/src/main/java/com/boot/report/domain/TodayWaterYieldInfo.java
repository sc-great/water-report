package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂当日水量信息对象 TodayWaterYieldInfo
 * 
 * @author EPL
 * @date 2020-03-24
 */
public class TodayWaterYieldInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** 当日水量进水 */
    private Double todayIn;

    /** 当日水量出水 */
    private Double todayOut;

    /** 累计水量进水 */
    private Double totalIn;

    /** 累计水量出水 */
    private Double totalOut;

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

    public void setTodayIn(Double todayIn) {
        this.todayIn = todayIn;
    }

    public Double getTodayIn() {
        return todayIn;
    }

    public void setTodayOut(Double todayOut) {
        this.todayOut = todayOut;
    }

    public Double getTodayOut() {
        return todayOut;
    }

    public void setTotalIn(Double totalIn) {
        this.totalIn = totalIn;
    }

    public Double getTotalIn() {
        return totalIn;
    }

    public void setTotalOut(Double totalOut) {
        this.totalOut = totalOut;
    }

    public Double getTotalOut() {
        return totalOut;
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
