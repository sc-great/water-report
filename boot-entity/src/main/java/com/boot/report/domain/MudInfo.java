package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂污泥数据信息对象 r_mud_info
 * 
 * @author EPL
 * @date 2020-03-30
 */
public class MudInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** 污泥日产 */
    private Double todayMud;

    /** 污泥累计 */
    private Double totalMud;
    
    /** 改为污泥含水率 */
    private Double waterIn;
    
    /** 绝干污泥量 */
    private Double dryMud;
    
    /** 绝干污泥量 */
    private Double dryMudTotal;

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

    public void setTodayMud(Double todayMud) {
        this.todayMud = todayMud;
    }

    public Double getTodayMud() {
        return todayMud;
    }

    public void setTotalMud(Double totalMud) {
        this.totalMud = totalMud;
    }

    public Double getTotalMud() {
        return totalMud;
    }

    public Double getDryMud() {
		return dryMud;
	}

	public void setDryMud(Double dryMud) {
		this.dryMud = dryMud;
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

	public Double getWaterIn() {
		return waterIn;
	}

	public void setWaterIn(Double waterIn) {
		this.waterIn = waterIn;
	}

	public Double getDryMudTotal() {
		return dryMudTotal;
	}

	public void setDryMudTotal(Double dryMudTotal) {
		this.dryMudTotal = dryMudTotal;
	}
}
