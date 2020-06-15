package com.boot.web.controller.admin.his;

/**
 * 用于比较分析的项目
 * @author LM
 *
 */
public class SearchObject {
	
	private String id;
	private String pId;
	private String code;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SearchObject(String id, String pId, String code, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.code = code;
		this.name = name;
	}
}
