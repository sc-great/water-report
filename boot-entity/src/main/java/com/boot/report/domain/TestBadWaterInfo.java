package com.boot.report.domain;

import com.boot.common.core.domain.BaseEntity;

/**
 * 化验污水数据信息对象 TestBadWaterInfo
 * 
 * @author yangxiaojun
 * @date 2020-04-22
 */
public class TestBadWaterInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;
    

	/** 编号 */
    private String id;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** pH值（进水） */
    private Double phIn;
    /** pH值 （出水)*/
    private Double phOut;
  
    /** 氨氮 （进水）*/
    private Double adanIn;
    /** 氨氮（出水) */
    private Double adanOut;
    
    /** 总氮 （进水）*/
    private Double zdanIn;
    /** 总氮 （出水) */
    private Double zdanOut;    

    /** 总磷（进水） */
    private Double zlinIn;
    /** 总磷（出水)  */
    private Double zlinOut;
    

    /** 化学需氧量COD （进水）*/
    private Integer codIn;
    /** 化学需氧量COD （出水) */
    private Integer codOut;
    

    /** 五日生化需氧量BOD5 （进水）*/
    private Double bod5In;
    /** 五日生化需氧量BOD5 （出水)*/
    private Double bod5Out;

	/** 总悬浮物（进水） */
    private Integer xfuIn;
    /** 总悬浮物 （出水)*/
    private Integer xfuOut;
    
    /**温度（进水）*/
    private Double tempIn;
    /**温度（出水）*/
    private Double tempOut;
    
    /** 粪大肠菌 */
    private Integer fengJun;

    /** SV30 */
    private Double sv30;

    /** SVT */
    private Double svt;

    /** MLSS */
    private Double mlss;

	/** MLVSS */
    private Integer mlvss;

    /** 化验数据-DO */
    private Double testDo;
   
    /**污泥含水率*/
    private Double waterSludge;

    
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

	public Double getPhIn() {
		return phIn;
	}

	public void setPhIn(Double phIn) {
		this.phIn = phIn;
	}

	public Double getPhOut() {
		return phOut;
	}

	public void setPhOut(Double phOut) {
		this.phOut = phOut;
	}

	public Double getAdanIn() {
		return adanIn;
	}

	public void setAdanIn(Double adanIn) {
		this.adanIn = adanIn;
	}

	public Double getAdanOut() {
		return adanOut;
	}

	public void setAdanOut(Double adanOut) {
		this.adanOut = adanOut;
	}

	public Double getZdanIn() {
		return zdanIn;
	}

	public void setZdanIn(Double zdanIn) {
		this.zdanIn = zdanIn;
	}

	public Double getZdanOut() {
		return zdanOut;
	}

	public void setZdanOut(Double zdanOut) {
		this.zdanOut = zdanOut;
	}

	public Double getZlinIn() {
		return zlinIn;
	}

	public void setZlinIn(Double zlinIn) {
		this.zlinIn = zlinIn;
	}

	public Double getZlinOut() {
		return zlinOut;
	}

	public void setZlinOut(Double zlinOut) {
		this.zlinOut = zlinOut;
	}

	public Integer getCodIn() {
		return codIn;
	}

	public void setCodIn(Integer codIn) {
		this.codIn = codIn;
	}

	public Integer getCodOut() {
		return codOut;
	}

	public void setCodOut(Integer codOut) {
		this.codOut = codOut;
	}

	public Double getBod5In() {
		return bod5In;
	}

	public void setBod5In(Double bod5In) {
		this.bod5In = bod5In;
	}

	public Double getBod5Out() {
		return bod5Out;
	}

	public void setBod5Out(Double bod5Out) {
		this.bod5Out = bod5Out;
	}

	public Integer getXfuIn() {
		return xfuIn;
	}

	public void setXfuIn(Integer xfuIn) {
		this.xfuIn = xfuIn;
	}

	public Integer getXfuOut() {
		return xfuOut;
	}

	public void setXfuOut(Integer xfuOut) {
		this.xfuOut = xfuOut;
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

	public Integer getFengJun() {
		return fengJun;
	}

	public void setFengJun(Integer fengJun) {
		this.fengJun = fengJun;
	}

	public Double getSv30() {
		return sv30;
	}

	public void setSv30(Double sv30) {
		this.sv30 = sv30;
	}

	public Double getSvt() {
		return svt;
	}

	public void setSvt(Double svt) {
		this.svt = svt;
	}

	public Double getMlss() {
		return mlss;
	}

	public void setMlss(Double mlss) {
		this.mlss = mlss;
	}

	public Integer getMlvss() {
		return mlvss;
	}

	public void setMlvss(Integer mlvss) {
		this.mlvss = mlvss;
	}

	public Double getTestDo() {
		return testDo;
	}

	public void setTestDo(Double testDo) {
		this.testDo = testDo;
	}

	public Double getWaterSludge() {
		return waterSludge;
	}

	public void setWaterSludge(Double waterSludge) {
		this.waterSludge = waterSludge;
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