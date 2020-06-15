package com.boot.materialControl.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂费用情况信息对象 m_cost_info
 * 
 * @author LM
 * @date 2020-04-21
 */
public class CostInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// 编号
	private String id;

	// 填报日期
	private String fillDate;

	// 填报时间
	private String fillTime;

	// 填报人编号
	private String fillUserId;

	// 填报人
	private String fillUserName;

	// 当年目标
	private Double thisYear;

	// 去年目标
	private Double lastYear;

	// 本月到账
	private Double currentEnter;

	// 本月累计
	private Double currentTotal;

	// 所属水厂
	private String factoryId;

	// 所属区域
	private String areaId;
	
	// 所属区域
	private String areaName;

	// 有效标识(1-有效 2-无效 )
	private String effectIcon;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFillDate() {
		return fillDate;
	}

	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}

	public String getFillTime() {
		return fillTime;
	}

	public void setFillTime(String fillTime) {
		this.fillTime = fillTime;
	}

	public String getFillUserId() {
		return fillUserId;
	}

	public void setFillUserId(String fillUserId) {
		this.fillUserId = fillUserId;
	}

	public String getFillUserName() {
		return fillUserName;
	}

	public void setFillUserName(String fillUserName) {
		this.fillUserName = fillUserName;
	}

	public Double getThisYear() {
		return thisYear;
	}

	public void setThisYear(Double thisYear) {
		this.thisYear = thisYear;
	}

	public Double getLastYear() {
		return lastYear;
	}

	public void setLastYear(Double lastYear) {
		this.lastYear = lastYear;
	}

	public Double getCurrentEnter() {
		return currentEnter;
	}

	public void setCurrentEnter(Double currentEnter) {
		this.currentEnter = currentEnter;
	}

	public Double getCurrentTotal() {
		return currentTotal;
	}

	public void setCurrentTotal(Double currentTotal) {
		this.currentTotal = currentTotal;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getEffectIcon() {
		return effectIcon;
	}

	public void setEffectIcon(String effectIcon) {
		this.effectIcon = effectIcon;
	}
}
