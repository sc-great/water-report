package com.boot.materialControl.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂备品备件类型对象 m_consumable_type
 * 
 * @author LM
 * @date 2020-04-24
 */
public class ConsumableType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// 编号
	private String id;

	// 名称
	private String name;

	// 型号
	private String model;
	
	// 单位
	private String unit;

	// 信息
	private String info;
	
	// 备注
	private String note;
	
	// 用于模糊查询
	private String param;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
