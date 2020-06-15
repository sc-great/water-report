package com.boot.system.domain;

import com.boot.common.annotation.Excel;
import com.boot.common.core.domain.BaseEntity;

/**
 * 水厂信息对象 sys_factory_info
 * 
 * @author boot
 * @date 2020-04-29
 */
public class SysFactoryInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 编号 */
	private String factId;

	/** 组织机构编号 */
	@Excel(name = "组织机构编号")
	private String orgId;

	/** 所在区域 */
	@Excel(name = "所在区域")
	private String areaId;

	/** 水厂名称 */
	@Excel(name = "水厂名称")
	private String factName;

	/** 竣工日期 */
	@Excel(name = "竣工日期")
	private String finiDate;

	/** 开始运营日期 */
	@Excel(name = "开始运营日期")
	private String startDate;

	/** 设计处理能力（吨/天） */
	@Excel(name = "设计处理能力", readConverterExp = "吨=/天")
	private Long ton;

	/** 人员配置（人） */
	@Excel(name = "人员配置", readConverterExp = "人=")
	private String personNum;

	/** 工艺 */
	@Excel(name = "工艺")
	private String technology;

	/** 排放标准(字典类型） */
	@Excel(name = "排放标准(字典类型）")
	private String standard;

	/** 现执行单价(元/吨) */
	@Excel(name = "现执行单价(元/吨)")
	private Double price;

	/** COD进水预警值下限 */
	@Excel(name = "COD进水预警值下限")
	private Double codInMinBad;

	/** COD进水预警值上限 */
	@Excel(name = "COD进水预警值上限")
	private Double codInMaxBad;

	/** COD出水预警值下限 */
	@Excel(name = "COD出水预警值下限")
	private Double codOutMinBad;

	/** COD出水预警值上限 */
	@Excel(name = "COD出水预警值上限")
	private Double codOutMaxBad;

	/** NH3-N进水预警值下限 */
	@Excel(name = "NH3-N进水预警值下限")
	private Double nh3NInMinBad;

	/** NH3-N进水预警值上限 */
	@Excel(name = "NH3-N进水预警值上限")
	private Double nh3NInMaxBad;

	/** NH3-N出水预警值下限 */
	@Excel(name = "NH3-N出水预警值下限")
	private Double nh3NOutMinBad;

	/** NH3-N出水预警值上限 */
	@Excel(name = "NH3-N出水预警值上限")
	private Double nh3NOutMaxBad;

	/** SS进水预警值下限 */
	@Excel(name = "SS进水预警值下限")
	private Double ssInMinBad;

	/** SS进水预警值上限 */
	@Excel(name = "SS进水预警值上限")
	private Double ssInMaxBad;

	/** SS出水预警值下限 */
	@Excel(name = "SS出水预警值下限")
	private Double ssOutMinBad;

	/** SS出水预警值上限 */
	@Excel(name = "SS出水预警值上限")
	private Double ssOutMaxBad;

	/** PH值进水预警值下限 */
	@Excel(name = "PH值进水预警值下限")
	private Double phInMinBad;

	/** PH值进水预警值上限 */
	@Excel(name = "PH值进水预警值上限")
	private Double phInMaxBad;

	/** PH值出水预警值下限 */
	@Excel(name = "PH值出水预警值下限")
	private Double phOutMinBad;

	/** PH值出水预警值上限 */
	@Excel(name = "PH值出水预警值上限")
	private Double phOutMaxBad;

	/** TP进水预警值下限 */
	@Excel(name = "TP进水预警值下限")
	private Double tpInMinBad;

	/** TP进水预警值上限 */
	@Excel(name = "TP进水预警值上限")
	private Double tpInMaxBad;

	/** TP出水预警值下限 */
	@Excel(name = "TP出水预警值下限")
	private Double tpOutMinBad;

	/** TP出水预警值上限 */
	@Excel(name = "TP出水预警值上限")
	private Double tpOutMaxBad;

	/** TN进水预警值下限 */
	@Excel(name = "TN进水预警值下限")
	private Double tnInMinBad;

	/** TN进水预警值上限 */
	@Excel(name = "TN进水预警值上限")
	private Double tnInMaxBad;

	/** TN出水预警值下限 */
	@Excel(name = "TN出水预警值下限")
	private Double tnOutMinBad;

	/** TN出水预警值上限 */
	@Excel(name = "TN出水预警值上限")
	private Double tnOutMaxBad;

	/** PH值进水预警值下限 */
	@Excel(name = "PH值进水预警值下限")
	private Double phInMinGood;

	/** PH值进水预警值上限 */
	@Excel(name = "PH值进水预警值上限")
	private Double phInMaxGood;

	/** PH值出水预警值下限 */
	@Excel(name = "PH值出水预警值下限")
	private Double phOutMinGood;

	/** PH值出水预警值下限 */
	@Excel(name = "PH值出水预警值下限")
	private Double phOutMaxGood;

	/** COL2出水预警值下限 */
	@Excel(name = "COL2出水预警值下限")
	private Double col2OutMinGood;

	/** COL2出水预警值上限 */
	@Excel(name = "COL2出水预警值上限")
	private Double col2OutMaxGood;

	/** 出水细菌总数预警值下限 */
	@Excel(name = "出水细菌总数预警值下限")
	private Double germOutMinGood;

	/** 出水细菌总数预警值上限 */
	@Excel(name = "出水细菌总数预警值上限")
	private Double germOutMaxGood;

	public void setFactId(String factId) {
		this.factId = factId;
	}

	public String getFactId() {
		return factId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setFactName(String factName) {
		this.factName = factName;
	}

	public String getFactName() {
		return factName;
	}

	public void setFiniDate(String finiDate) {
		this.finiDate = finiDate;
	}

	public String getFiniDate() {
		return finiDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setTon(Long ton) {
		this.ton = ton;
	}

	public Long getTon() {
		return ton;
	}

	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}

	public String getPersonNum() {
		return personNum;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTechnology() {
		return technology;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getStandard() {
		return standard;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPrice() {
		return price;
	}

	public void setCodInMinBad(Double codInMinBad) {
		this.codInMinBad = codInMinBad;
	}

	public Double getCodInMinBad() {
		return codInMinBad;
	}

	public void setCodInMaxBad(Double codInMaxBad) {
		this.codInMaxBad = codInMaxBad;
	}

	public Double getCodInMaxBad() {
		return codInMaxBad;
	}

	public void setCodOutMinBad(Double codOutMinBad) {
		this.codOutMinBad = codOutMinBad;
	}

	public Double getCodOutMinBad() {
		return codOutMinBad;
	}

	public void setCodOutMaxBad(Double codOutMaxBad) {
		this.codOutMaxBad = codOutMaxBad;
	}

	public Double getCodOutMaxBad() {
		return codOutMaxBad;
	}

	public void setNh3NInMinBad(Double nh3NInMinBad) {
		this.nh3NInMinBad = nh3NInMinBad;
	}

	public Double getNh3NInMinBad() {
		return nh3NInMinBad;
	}

	public void setNh3NInMaxBad(Double nh3NInMaxBad) {
		this.nh3NInMaxBad = nh3NInMaxBad;
	}

	public Double getNh3NInMaxBad() {
		return nh3NInMaxBad;
	}

	public void setNh3NOutMinBad(Double nh3NOutMinBad) {
		this.nh3NOutMinBad = nh3NOutMinBad;
	}

	public Double getNh3NOutMinBad() {
		return nh3NOutMinBad;
	}

	public void setNh3NOutMaxBad(Double nh3NOutMaxBad) {
		this.nh3NOutMaxBad = nh3NOutMaxBad;
	}

	public Double getNh3NOutMaxBad() {
		return nh3NOutMaxBad;
	}

	public void setSsInMinBad(Double ssInMinBad) {
		this.ssInMinBad = ssInMinBad;
	}

	public Double getSsInMinBad() {
		return ssInMinBad;
	}

	public void setSsInMaxBad(Double ssInMaxBad) {
		this.ssInMaxBad = ssInMaxBad;
	}

	public Double getSsInMaxBad() {
		return ssInMaxBad;
	}

	public void setSsOutMinBad(Double ssOutMinBad) {
		this.ssOutMinBad = ssOutMinBad;
	}

	public Double getSsOutMinBad() {
		return ssOutMinBad;
	}

	public void setSsOutMaxBad(Double ssOutMaxBad) {
		this.ssOutMaxBad = ssOutMaxBad;
	}

	public Double getSsOutMaxBad() {
		return ssOutMaxBad;
	}

	public void setPhInMinBad(Double phInMinBad) {
		this.phInMinBad = phInMinBad;
	}

	public Double getPhInMinBad() {
		return phInMinBad;
	}

	public void setPhInMaxBad(Double phInMaxBad) {
		this.phInMaxBad = phInMaxBad;
	}

	public Double getPhInMaxBad() {
		return phInMaxBad;
	}

	public void setPhOutMinBad(Double phOutMinBad) {
		this.phOutMinBad = phOutMinBad;
	}

	public Double getPhOutMinBad() {
		return phOutMinBad;
	}

	public void setPhOutMaxBad(Double phOutMaxBad) {
		this.phOutMaxBad = phOutMaxBad;
	}

	public Double getPhOutMaxBad() {
		return phOutMaxBad;
	}

	public void setTpInMinBad(Double tpInMinBad) {
		this.tpInMinBad = tpInMinBad;
	}

	public Double getTpInMinBad() {
		return tpInMinBad;
	}

	public void setTpInMaxBad(Double tpInMaxBad) {
		this.tpInMaxBad = tpInMaxBad;
	}

	public Double getTpInMaxBad() {
		return tpInMaxBad;
	}

	public void setTpOutMinBad(Double tpOutMinBad) {
		this.tpOutMinBad = tpOutMinBad;
	}

	public Double getTpOutMinBad() {
		return tpOutMinBad;
	}

	public void setTpOutMaxBad(Double tpOutMaxBad) {
		this.tpOutMaxBad = tpOutMaxBad;
	}

	public Double getTpOutMaxBad() {
		return tpOutMaxBad;
	}

	public void setTnInMinBad(Double tnInMinBad) {
		this.tnInMinBad = tnInMinBad;
	}

	public Double getTnInMinBad() {
		return tnInMinBad;
	}

	public void setTnInMaxBad(Double tnInMaxBad) {
		this.tnInMaxBad = tnInMaxBad;
	}

	public Double getTnInMaxBad() {
		return tnInMaxBad;
	}

	public void setTnOutMinBad(Double tnOutMinBad) {
		this.tnOutMinBad = tnOutMinBad;
	}

	public Double getTnOutMinBad() {
		return tnOutMinBad;
	}

	public void setTnOutMaxBad(Double tnOutMaxBad) {
		this.tnOutMaxBad = tnOutMaxBad;
	}

	public Double getTnOutMaxBad() {
		return tnOutMaxBad;
	}

	public void setPhInMinGood(Double phInMinGood) {
		this.phInMinGood = phInMinGood;
	}

	public Double getPhInMinGood() {
		return phInMinGood;
	}

	public void setPhInMaxGood(Double phInMaxGood) {
		this.phInMaxGood = phInMaxGood;
	}

	public Double getPhInMaxGood() {
		return phInMaxGood;
	}

	public void setPhOutMinGood(Double phOutMinGood) {
		this.phOutMinGood = phOutMinGood;
	}

	public Double getPhOutMinGood() {
		return phOutMinGood;
	}

	public void setPhOutMaxGood(Double phOutMaxGood) {
		this.phOutMaxGood = phOutMaxGood;
	}

	public Double getPhOutMaxGood() {
		return phOutMaxGood;
	}

	public void setCol2OutMinGood(Double col2OutMinGood) {
		this.col2OutMinGood = col2OutMinGood;
	}

	public Double getCol2OutMinGood() {
		return col2OutMinGood;
	}

	public void setCol2OutMaxGood(Double col2OutMaxGood) {
		this.col2OutMaxGood = col2OutMaxGood;
	}

	public Double getCol2OutMaxGood() {
		return col2OutMaxGood;
	}

	public void setGermOutMinGood(Double germOutMinGood) {
		this.germOutMinGood = germOutMinGood;
	}

	public Double getGermOutMinGood() {
		return germOutMinGood;
	}

	public void setGermOutMaxGood(Double germOutMaxGood) {
		this.germOutMaxGood = germOutMaxGood;
	}

	public Double getGermOutMaxGood() {
		return germOutMaxGood;
	}
}
