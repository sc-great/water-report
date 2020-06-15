package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 报警信息类 （alarm_info）
 * @author LM
 * @date 2020-05-10
 */
public class AlarmInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String id; // 编号
	private String orgId; // 组织机构编号
	private String orgName; // 组织机构名称
	private String areaId; // 区域编号
	private String areaName; // 区域名称
	private String fillDate; // 添加日期
	private String fillTime; // 添加时间
	private String alarmType; // 报警类型（1：备品备件，2：强制检测，3：水质）
	private String alarmItem; // 报警项目（用于显示是哪一项报警，如备品备件的“口罩”、水质的“PH”等）
	private String effectIcon; // 有效标识（1：有效，2：无效）
	private String objId; // 对象id，精确找到报警对象
	
	private String value; // 报警值
	private String paOrgName; // 父级组织机构名称
	private String factoryType; // 父级组织机构名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmItem() {
		return alarmItem;
	}

	public void setAlarmItem(String alarmItem) {
		this.alarmItem = alarmItem;
	}

	public String getEffectIcon() {
		return effectIcon;
	}

	public void setEffectIcon(String effectIcon) {
		this.effectIcon = effectIcon;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPaOrgName() {
		return paOrgName;
	}

	public void setPaOrgName(String paOrgName) {
		this.paOrgName = paOrgName;
	}

	public String getFactoryType() {
		return factoryType;
	}

	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}
}
