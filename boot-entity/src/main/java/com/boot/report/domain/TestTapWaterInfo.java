package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 自来水厂化验数据对象 GoodWaterHealthInfo
 * 
 * @author yangxiaojun
 * @date 2020-04-26
 */
public class TestTapWaterInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;
    
    /** 编号 */
    private String id;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** 浊度(进水) */
    private Double ntuIn;
    /** 浊度 (出水)*/
    private Double ntuOut;
    
    /**色度(进水)(度） */
    private Double colourIn;
    /**色度(出水)(度） */
    private Double colourOut;
        
    /** 臭和味(进水) */
    private String cwIn;
    /** 臭和味(出水) */
    private String cwOut;
    
    /** 肉眼可见物(进水) */
    private String eyeIn;    
    /** 肉眼可见物(出水) */
    private String eyeOut;

    /** 耗氧量(进水）mg/L */
    private Double hyIn;
    /** 耗氧量(出水）mg/L */
    private Double hyOut;
 
    /**氨氮(进水)mg/L */
    private Double adanIn;
 
    /** 细菌总数(进水)CFU/ml */
    private Double xjzsIn;
    /** 细菌总数(出水)CFU/ml */
    private Double xjzsOut;

    /**总大肠菌群(进水)CFU/ml*/
    private Double zdjIn;
    /**总大肠菌群(出水)CFU/ml*/
    private Double zdjOut;
    
    /**耐热大肠菌群(进水)CFU/ml */
    private Double nrjIn;
    /**耐热大肠菌群(出水)CFU/ml */
    private Double nrjOut;
    
    /**二氧化氯(出水)mg/L*/
    private Double twoOut;
    
    /**温度（进水)(度） */
    private Double tempIn;
    /**温度（出水)(度） */
    private Double tempOut;
    
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

	public Double getNtuIn() {
		return ntuIn;
	}

	public void setNtuIn(Double ntuIn) {
		this.ntuIn = ntuIn;
	}

	public Double getNtuOut() {
		return ntuOut;
	}

	public void setNtuOut(Double ntuOut) {
		this.ntuOut = ntuOut;
	}

	public Double getColourIn() {
		return colourIn;
	}

	public void setColourIn(Double colourIn) {
		this.colourIn = colourIn;
	}

	public Double getColourOut() {
		return colourOut;
	}

	public void setColourOut(Double colourOut) {
		this.colourOut = colourOut;
	}

	public String getCwIn() {
		return cwIn;
	}

	public void setCwIn(String cwIn) {
		this.cwIn = cwIn;
	}

	public String getCwOut() {
		return cwOut;
	}

	public void setCwOut(String cwOut) {
		this.cwOut = cwOut;
	}

	public String getEyeIn() {
		return eyeIn;
	}

	public void setEyeIn(String eyeIn) {
		this.eyeIn = eyeIn;
	}

	public String getEyeOut() {
		return eyeOut;
	}

	public void setEyeOut(String eyeOut) {
		this.eyeOut = eyeOut;
	}

	public Double getHyIn() {
		return hyIn;
	}

	public void setHyIn(Double hyIn) {
		this.hyIn = hyIn;
	}

	public Double getHyOut() {
		return hyOut;
	}

	public void setHyOut(Double hyOut) {
		this.hyOut = hyOut;
	}

	public Double getAdanIn() {
		return adanIn;
	}

	public void setAdanIn(Double adanIn) {
		this.adanIn = adanIn;
	}

	public Double getXjzsIn() {
		return xjzsIn;
	}

	public void setXjzsIn(Double xjzsIn) {
		this.xjzsIn = xjzsIn;
	}

	public Double getXjzsOut() {
		return xjzsOut;
	}

	public void setXjzsOut(Double xjzsOut) {
		this.xjzsOut = xjzsOut;
	}

	public Double getZdjIn() {
		return zdjIn;
	}

	public void setZdjIn(Double zdjIn) {
		this.zdjIn = zdjIn;
	}

	public Double getZdjOut() {
		return zdjOut;
	}

	public void setZdjOut(Double zdjOut) {
		this.zdjOut = zdjOut;
	}

	public Double getNrjIn() {
		return nrjIn;
	}

	public void setNrjIn(Double nrjIn) {
		this.nrjIn = nrjIn;
	}

	public Double getNrjOut() {
		return nrjOut;
	}

	public void setNrjOut(Double nrjOut) {
		this.nrjOut = nrjOut;
	}

	public Double getTwoOut() {
		return twoOut;
	}

	public void setTwoOut(Double twoOut) {
		this.twoOut = twoOut;
	}

	public Double getTempIn() {
		return tempIn;
	}

	public void setTempIn(Double tempIn) {
		this.tempIn = tempIn;
	}

	public Double getTempOut() {
		return tempOut;
	}

	public void setTempOut(Double tempOut) {
		this.tempOut = tempOut;
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

	public String getEffectIcon() {
		return effectIcon;
	}

	public void setEffectIcon(String effectIcon) {
		this.effectIcon = effectIcon;
	}

	

   

 
	

   
}