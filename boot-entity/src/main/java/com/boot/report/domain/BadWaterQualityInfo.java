package com.boot.report.domain;

import java.util.List;

import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂水质数据信息(污水处理厂/排水厂)对象 BadWaterQualityInfo
 * 
 * @author EPL
 * @date 2020-03-24
 */
public class BadWaterQualityInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 填报日期 */
    private String fillDate;

    /** 填报时间 */
    private String fillTime;

    /** COD进水 */
    private Double codIn;

    /** COD出水 */
    private Double codOut;

    /** NH3-N进水 */
    private Double nh3NIn;

    /** NH3-N出水 */
    private Double nh3NOut;

    /** SS进水 */
    private Double ssIn;

    /** SS出水 */
    private Double ssOut;

    /** PH值进水 */
    private Double phIn;

    /** PH值出水 */
    private Double phOut;

    /** TP进水 */
    private Double tpIn;

    /** TP出水 */
    private Double tpOut;

    /** TN进水 */
    private Double tnIn;

    /** TN出水 */
    private Double tnOut;

    /** 化验数据-MLSS进水 */
    private Double mlssIn;

    /** 化验数据-MLSS出水 */
    private Double mlssOut;

    /** 化验数据-SV30进水 */
    private Double sv30In;

    /** 化验数据-SV30出水 */
    private Double sv30Out;

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
    
    /** 出水指标  合格标记(1-合格 2-不合格)*/
    private String passFlag;
    
       
	/**手机端运营能力二级页面：不合格列表 (出水)*/
	private  List<BadWaterQualityInfo>  bwqiList;

    public String getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(String passFlag) {
		this.passFlag = passFlag;
	}

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

    public void setCodIn(Double codIn) {
        this.codIn = codIn;
    }

    public Double getCodIn() {
        return codIn;
    }

    public void setCodOut(Double codOut) {
        this.codOut = codOut;
    }

    public Double getCodOut() {
        return codOut;
    }

    public void setNh3NIn(Double nh3NIn) {
        this.nh3NIn = nh3NIn;
    }

    public Double getNh3NIn() {
        return nh3NIn;
    }

    public void setNh3NOut(Double nh3NOut) {
        this.nh3NOut = nh3NOut;
    }

    public Double getNh3NOut() {
        return nh3NOut;
    }

    public void setSsIn(Double ssIn) {
        this.ssIn = ssIn;
    }

    public Double getSsIn() {
        return ssIn;
    }

    public void setSsOut(Double ssOut) {
        this.ssOut = ssOut;
    }

    public Double getSsOut() {
        return ssOut;
    }

    public void setPhIn(Double phIn) {
        this.phIn = phIn;
    }

    public Double getPhIn() {
        return phIn;
    }

    public void setPhOut(Double phOut) {
        this.phOut = phOut;
    }

    public Double getPhOut() {
        return phOut;
    }

    public void setTpIn(Double tpIn) {
        this.tpIn = tpIn;
    }

    public Double getTpIn() {
        return tpIn;
    }

    public void setTpOut(Double tpOut) {
        this.tpOut = tpOut;
    }

    public Double getTpOut() {
        return tpOut;
    }

    public void setTnIn(Double tnIn) {
        this.tnIn = tnIn;
    }

    public Double getTnIn() {
        return tnIn;
    }

    public void setTnOut(Double tnOut) {
        this.tnOut = tnOut;
    }

    public Double getTnOut() {
        return tnOut;
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

    public Double getMlssIn() {
        return mlssIn;
    }

    public void setMlssIn(Double mlssIn) {
        this.mlssIn = mlssIn;
    }

    public Double getMlssOut() {
        return mlssOut;
    }

    public void setMlssOut(Double mlssOut) {
        this.mlssOut = mlssOut;
    }

    public Double getSv30In() {
        return sv30In;
    }

    public void setSv30In(Double sv30In) {
        this.sv30In = sv30In;
    }

    public Double getSv30Out() {
        return sv30Out;
    }

    public void setSv30Out(Double sv30Out) {
        this.sv30Out = sv30Out;
    }
    
    public List<BadWaterQualityInfo> getBwqiList() {
		return bwqiList;
	}

	public void setBwqiList(List<BadWaterQualityInfo> bwqiList) {
		this.bwqiList = bwqiList;
	}
	
}