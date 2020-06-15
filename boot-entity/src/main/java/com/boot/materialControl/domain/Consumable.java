package com.boot.materialControl.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂备品备件信息对象 m_consumable
 * 
 * @author LM
 * @date 2020-04-22
 */
public class Consumable extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// 编号
	private String id;
	
	// 备品备件类型
	private String typeId;
	
	// 名称
	private String name;
	
	// 型号
	private String model;
	
	// 单位
	private String unit;
	
	// 类型
	private ConsumableType type;
	
	// 备品备件总量
	private int total;
	
	// 最近一次入账时间
	private String lastSetTime;
	
	// 最近一次入账数量
	private int lastSetNum;
	
	// 最近一次入账登记者id
	private String lastSetUserId;
	
	// 最近一次入账等记者姓名
	private String lastSetUserName;
	
	// 最近一次领用时间
	private String lastGetTime;
	
	// 最近一次领用数量
	private int lastGetNum;
	
	// 最近一次领用者id
	private String lastGetUserId;
	
	// 最近一次领用者姓名
	private String lastGetUserName;
	
	// 所属区域
	private String areaId;

	// 所属水厂
	private String factoryId;
	
	// 所属水厂
	private String factoryName;

	// 有效标识（1-有效 2-无效 ）
	private String effectIcon;
	
	// 填报日期
	private String fillDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConsumableType getType() {
		return type;
	}

	public void setType(ConsumableType type) {
		this.type = type;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getLastSetTime() {
		return lastSetTime;
	}

	public void setLastSetTime(String lastSetTime) {
		this.lastSetTime = lastSetTime;
	}

	public int getLastSetNum() {
		return lastSetNum;
	}

	public void setLastSetNum(int lastSetNum) {
		this.lastSetNum = lastSetNum;
	}

	public String getLastSetUserId() {
		return lastSetUserId;
	}

	public void setLastSetUserId(String lastSetUserId) {
		this.lastSetUserId = lastSetUserId;
	}

	public String getLastSetUserName() {
		return lastSetUserName;
	}

	public void setLastSetUserName(String lastSetUserName) {
		this.lastSetUserName = lastSetUserName;
	}

	public String getLastGetTime() {
		return lastGetTime;
	}

	public void setLastGetTime(String lastGetTime) {
		this.lastGetTime = lastGetTime;
	}

	public int getLastGetNum() {
		return lastGetNum;
	}

	public void setLastGetNum(int lastGetNum) {
		this.lastGetNum = lastGetNum;
	}

	public String getLastGetUserId() {
		return lastGetUserId;
	}

	public void setLastGetUserId(String lastGetUserId) {
		this.lastGetUserId = lastGetUserId;
	}

	public String getLastGetUserName() {
		return lastGetUserName;
	}

	public void setLastGetUserName(String lastGetUserName) {
		this.lastGetUserName = lastGetUserName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getEffectIcon() {
		return effectIcon;
	}

	public void setEffectIcon(String effectIcon) {
		this.effectIcon = effectIcon;
	}

	public String getFillDate() {
		return fillDate;
	}

	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
