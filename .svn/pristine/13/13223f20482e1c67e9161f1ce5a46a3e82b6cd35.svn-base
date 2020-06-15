package com.boot.web.controller.admin.his;

import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.materialControl.domain.CostInfo;
import com.boot.materialControl.service.CostInfoService;
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.service.IBadWaterQualityInfoService;
import com.boot.report.service.IGoodWaterHealthInfoService;
import com.boot.report.service.ITodayInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.system.service.ISysOrgService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史数据Controller
 *
 * @author LM
 * @date 2020-04-26
 */
@Controller
@RequestMapping("/admin/his")
public class HisInfoController extends BaseController {
	private String prefix = "admin/his";
	
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ITodayInfoService reportService;
	@Autowired
	private CostInfoService costInfoService;
	@Autowired
	private ISysFactoryInfoService factoryService;
	@Autowired
	private IGoodWaterHealthInfoService goodWaterService;
	@Autowired
	private IBadWaterQualityInfoService badWaterService;

	@RequiresPermissions("report:his:info")
	@RequestMapping("/info")
	public String list(ModelMap model) {
		model.put("orgId", ShiroUtils.getSysUser().getSysOrgs().get(0).getOrgId());
		return prefix + "/list";
	}
	
	@RequestMapping("/searchStatisticsReport")
	@ResponseBody
	public TableDataInfo searchStatisticsReport(@RequestParam Map<String, String> map) throws IOException {
		String orgIdOrAreaId = map.get("orgId"); // 可能是orgId，也又可能是areaId
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		if (orgIdOrAreaId == null || "".equals(orgIdOrAreaId) || dateSta == null || "".equals(dateSta) || dateEnd == null || "".equals(dateEnd))
			return getDataTable(new ArrayList<>());
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		String orgType = map.get("orgType");
		// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
		Map<String, Object> params = new HashMap<>();
		params.put("dateSta", dateSta);
		params.put("dateEnd", dateEndReal);
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 该excel表格，以组织机构为循环单位，所以要查出org下所有的子级，这儿混合了区域，所以又要分开查询
		SysOrg org = null;
		Area area = null;
		String sheetName = "统计分析表";
		List<SysOrg> orgList = null;
		List<Area> areaList = null;
		// 用于循环的数值
		int numForCycle = 0;
		// 查集团
		if (orgType.equals("1")) {
			org = orgService.getById(orgIdOrAreaId);
			sheetName = org.getOrgName() + sheetName;
			SysOrg sysOrgParams = new SysOrg();
			sysOrgParams.setParentId(orgIdOrAreaId);
			orgList = orgService.findList(sysOrgParams);
			numForCycle = orgList.size();
		}
		// 查公司（即11个地区）
		if (orgType.equals("2")) {
			org = orgService.getById(orgIdOrAreaId);
			sheetName = org.getOrgName() + sheetName;
			Map<String, Object> p = new HashMap<>();
			p.put("areaIds", org.getAreaId());
			Area areaParams = new Area();
			areaParams.setParams(p);
			areaList = areaService.getList(areaParams);
			numForCycle = areaList.size();
		}
		// 查区域（即公司）
		if (orgType.equals("2.5")) {
			area = areaService.getEntityById(orgIdOrAreaId);
			sheetName = area.getAreaName() + sheetName;
			SysOrg sysOrgParams = new SysOrg();
			sysOrgParams.setOrgType("3");
			sysOrgParams.setAreaId(orgIdOrAreaId);
			orgList = orgService.findList(sysOrgParams);
			numForCycle = orgList.size();
		}
		// 查水厂（即公司）--必定是一条数据
		if (orgType.equals("3")) {
			org = orgService.getById(orgIdOrAreaId);
			sheetName = org.getOrgName() + sheetName;
			numForCycle = 1;
		}
		
		// 用于返回的集合
		List<Map<String, Object>> reList = new ArrayList<>();
		Map<String, Object> reMap = null;
			
		SysOrg sysOrg = null;
			
		for (int i = 0; i < numForCycle; i++) {
			reMap = new HashMap<>();
			String cellName = ""; // 公司、区域、工厂名称
			int orgCount = 0; // 工厂数量——用于平均
			
			if (orgType.equals("1")) { // 查集团
				sysOrg = orgList.get(i); // 当前对象
				params.put("parentId", sysOrg.getOrgId()); // 数据查询条件
				cellName = sysOrg.getOrgName();
				// 用于求平均
				SysOrg o = new SysOrg();
				o.setParentId(sysOrg.getOrgId());
				orgCount = orgService.getCount(sysOrg);
			}
			if (orgType.equals("2")) { // 查公司（即11个地区）
				area = areaList.get(i); // 当前对象
				params.put("areaId", area.getAreaId()); // 数据查询条件
				cellName = area.getAreaName();
				// 用于求平均
				SysOrg o = new SysOrg();
				Map<String, Object> p = new HashMap<>();
				p.put("areaId", area.getAreaId());
				orgCount = orgService.getCount(o);
			}
			if (orgType.equals("2.5")) { // 查区域（即公司）
				sysOrg = orgList.get(i); // 当前对象
				params.put("orgId", sysOrg.getOrgId()); // 数据查询条件
				cellName = sysOrg.getOrgName();
				orgCount = orgList.size(); // 2.5时，是区域，最小单位是水厂了，没有下级，所以就查当前级
			}
			if (orgType.equals("3")) { // 查水厂（即公司）--必定是一条数据
				sysOrg = org; // 当前对象
				params.put("orgId", sysOrg.getOrgId()); // 数据查询条件
				cellName = sysOrg.getOrgName();
				orgCount = 1;
			}

			// 查数据
			ReportDateInfo r = new ReportDateInfo();
			r.setParams(params);
			Map<String, Object> rSumData = reportService.getSumData(r); // 查询在时间段内，当前组织结构下，所有数据的累计
			
			// 区域/项目 公司名称
			reMap.put("orgName", cellName);
			// 达标排放
			// 累计运行日（天）
			reMap.put("iDays", iDays);
			// 累计超标日（天）
			int overNorm = 0, good = 0, bad = 0;
			GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
			BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
			if (orgType.equals("1"))
				params.put("areaIds", sysOrg.getAreaId());
			goodWaterHealthInfo.setParams(params);
			badWaterQualityInfo.setParams(params);
			if (orgType.equals("2.5") || orgType.equals("3")) {
				if ("1".equals(sysOrg.getFactoryType()))
					bad = badWaterService.getOverNorm(badWaterQualityInfo);
				if ("2".equals(sysOrg.getFactoryType()))
					good = goodWaterService.getOverNorm(goodWaterHealthInfo);
			} else {
				good = goodWaterService.getOverNorm(goodWaterHealthInfo);
				bad = badWaterService.getOverNorm(badWaterQualityInfo);
			}
			overNorm = good + bad;
			reMap.put("overNorm", overNorm);
			// 达标率（%）=（累计运行日 - 累计超标日） / 累计运行日 * 100
			int gtotal = 0, galarm = 0, btotal = 0, balarm = 0;
			goodWaterHealthInfo.setEffectIcon("1");
			gtotal = goodWaterService.getCount(goodWaterHealthInfo);
			goodWaterHealthInfo.setPassFlag("2");
			galarm = goodWaterService.getCount(goodWaterHealthInfo);
			badWaterQualityInfo.setEffectIcon("1");
			btotal = badWaterService.getCount(badWaterQualityInfo);
			badWaterQualityInfo.setPassFlag("2");
			balarm = badWaterService.getCount(badWaterQualityInfo);
//			params.put("areaIds", sysOrg.getAreaId());
			double alarm = galarm + balarm;
			double total = gtotal + btotal;
			reMap.put("DBRate", total > 0 ? dataFormat((total - alarm) / total * 100) : 100);
			
			if (rSumData != null) {
				// 污水处理量（很恶心，这个只有污水厂才有）
				// 累计处理量（万吨）--污水厂的累计水量出水（所选日期的当日出水量相加）
				double totalOutTon = Double.parseDouble(rSumData.get("totalOut") == null ? "0" : rSumData.get("totalOut").toString()); // 单位是吨
				double totalOut = totalOutTon / 10000; // 转换为万吨
				reMap.put("totalOut", dataFormat(totalOut));
				// 累计设计处理能力（万吨）
				double ton = Double.parseDouble(rSumData.get("ton") == null ? "0" : rSumData.get("ton").toString()); // 单位是万吨，不用转换
				reMap.put("ton", ton);
				// 负荷率（%） = 累计量 / 累计设计处理能力，取值为百分数，精确小数点后 2 位
				double loadRate = 0;
				if (ton != 0) // 设计处理能力，在实际情况中可能没填，即会为 0，如果为 0，直接把符合率设为 0，以防被 0 除
					loadRate = dataFormat(totalOut / ton * 100);
				reMap.put("loadRate", loadRate);
				// 平均处理能力（万吨/日）= 累计处理量 / 区域水厂（应该四污水厂）个数 / 天数
				reMap.put("pjclnl", dataFormat(totalOut / orgCount / iDays));
				// 收费情况（收费情况的最小单位是areaId）
				// 累计应收水费（万元）= 区域求和：区域内各水厂现执行单价x区域内各水厂实际处理量（侯秋军）
				double should = Double.parseDouble(rSumData.get("account") == null ? "0" : rSumData.get("account").toString());
				reMap.put("should", should);
				// 累计实收取水费（万元）
				double reality = 0;
				if (orgType.equals("1")) { // 11个地区，每个地区的水费累计——水厂没有财务，没有实收水费
					CostInfo c = new CostInfo();
					c.setEffectIcon("1");
					params.put("areaIds", sysOrg.getAreaId());
					c.setParams(params);
					Map<String, Object> ciMap = costInfoService.getSum(c);
					reality = Double.parseDouble(ciMap.get("currentTotal").toString());
				} else if (orgType.equals("2")) {
					CostInfo c = new CostInfo();
					c.setEffectIcon("1");
					c.setAreaId(area.getAreaId());
					reality = Double.parseDouble(costInfoService.getSum(c).get("currentTotal").toString());
				}
				reMap.put("reality", reality);
				// 回款率（%）= 累计实收 / 累计应收，求百分比，保留小数后2位
				reMap.put("HKRate", should == 0 ? 0 : dataFormat(reality / should * 100));
				// 用电量
				// 累计总电量（度）
				double totalElec = Double.parseDouble(rSumData.get("totalElec") == null ? "0" : rSumData.get("totalElec").toString());
				reMap.put("totalElec", totalElec);
				// 吨水电耗（度/吨）= 累计总电量 / 累计处理量
				double totalElecTon = totalOutTon == 0 ? 0 : (totalElec / totalOutTon);
				reMap.put("totalElecTon", dataFormat(totalElecTon));
				// 平均吨水电耗（度/吨）= 区域内各水厂在该时间内吨水单耗求和 / 区域水厂个数
				reMap.put("pjdsdh", dataFormat(totalElecTon / orgCount));
				// 产泥量（这个又是只有污水厂才有的数据）
				// 累计产泥量（吨）
				reMap.put("totalMud", rSumData.get("totalMud") == null ? "0" : rSumData.get("totalMud").toString());
				// 累计绝干泥总量（吨）
				double totalDryMud = rSumData.get("totalDryMud") == null ? 0 : Double.parseDouble(rSumData.get("totalDryMud").toString());
				reMap.put("totalDryMud", totalDryMud);
				// 万吨水绝干污泥量（吨/万吨）= 累计绝干泥总量 / 累计实际处理总量
				reMap.put("wdsjgwnl", totalOut == 0 ? 0 : dataFormat(totalDryMud / totalOut));
				// 水处理药剂
				// PAM(阳）
				// 累计用量(kg)
				double totalPAMYang = Double.parseDouble(rSumData.get("totalPAMYang") == null ? "0" : rSumData.get("totalPAMYang").toString());
				reMap.put("totalPAMYang", totalPAMYang);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonPAMYang", totalOutTon == 0 ? 0 : dataFormat(totalPAMYang / totalOutTon));
				// PAM（阴）
				// 累计用量(kg)
				double totalPAMYin = Double.parseDouble(rSumData.get("totalPAMYin") == null ? "0" : rSumData.get("totalPAMYin").toString());
				reMap.put("totalPAMYin", totalPAMYin);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonPAMYin", totalOutTon == 0 ? 0 : dataFormat(totalPAMYin / totalOutTon));
				// PAC
				// 累计用量(kg)
				double totalPAC = Double.parseDouble(rSumData.get("totalPAC") == null ? "0" : rSumData.get("totalPAC").toString());
				reMap.put("totalPAC", totalPAC);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonPAC", totalOutTon == 0 ? 0 : dataFormat(totalPAC / totalOutTon));
				// 石灰
				// 累计用量(kg)
				double totalLime = Double.parseDouble(rSumData.get("totalLime") == null ? "0" : rSumData.get("totalLime").toString());
				reMap.put("totalLime", totalLime);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonLime", totalOutTon == 0 ? 0 : (Math.round(totalLime / totalOutTon * 100) / 100));
				// 复核除磷剂
				// 累计用量(kg)
				double totalPhosphorus = Double.parseDouble(rSumData.get("totalPhosphorus") == null ? "0" : rSumData.get("totalPhosphorus").toString());
				reMap.put("totalPhosphorus", totalPhosphorus);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonPhosphorus", totalOutTon == 0 ? 0 : dataFormat(totalPhosphorus / totalOutTon));
				// 盐酸（HCL）
				// 累计用量(kg)
				double totalHCL = Double.parseDouble(rSumData.get("totalHCL") == null ? "0" : rSumData.get("totalHCL").toString());
				reMap.put("totalHCL", totalHCL);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonHCL", totalOutTon == 0 ? 0 : dataFormat(totalHCL / totalOutTon));
				// 氯酸钠(NaCL)
				// 累计用量(kg)
				double totalSC = Double.parseDouble(rSumData.get("totalSC") == null ? "0" : rSumData.get("totalSC").toString());
				reMap.put("totalSC", totalSC);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonSC", totalOutTon == 0 ? 0 : dataFormat(totalSC / totalOutTon));
				// 次氯酸钠(NaCLO)
				// 累计用量(kg)
				double totalNaCLO = Double.parseDouble(rSumData.get("totalNaCLO") == null ? "0" : rSumData.get("totalNaCLO").toString());
				reMap.put("totalNaCLO", totalNaCLO);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonNaCLO", totalOutTon == 0 ? 0 : dataFormat(totalNaCLO / totalOutTon));
				// 葡萄糖（C6H12O6）
				// 累计用量(kg)
				double totalGlucose = Double.parseDouble(rSumData.get("totalGlucose") == null ? "0" : rSumData.get("totalGlucose").toString());
				reMap.put("totalGlucose", totalGlucose);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonGlucose", totalOutTon == 0 ? 0 : dataFormat(totalGlucose / totalOutTon));
				// 乙酸钠(C2H3O2Na)
				// 累计用量(kg)
				double totalSA = Double.parseDouble(rSumData.get("totalSA") == null ? "0" : rSumData.get("totalSA").toString());
				reMap.put("totalSA", totalSA);
				// 吨水药剂单耗(KG/吨）
				reMap.put("tonSA", totalOutTon == 0 ? 0 : dataFormat(totalSA / totalOutTon));
			}
			reList.add(reMap);
		}
		return getDataTable(reList);
	}

	@RequestMapping("/exportStatisticsReport")
	@ResponseBody
	public AjaxResult exportStatisticsReport(@RequestParam Map<String, String> map) throws IOException {
		String orgIdOrAreaId = map.get("orgId"); // 可能是orgId，也又可能是areaId
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		String orgType = map.get("orgType");
		// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
		Map<String, Object> params = new HashMap<>();
		params.put("dateSta", dateSta);
		params.put("dateEnd", dateEndReal);
		// 获取当前用户
		SysUser sysUser = ShiroUtils.getSysUser();
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 该excel表格，以组织机构为循环单位，所以要查出org下所有的子级，这儿混合了区域，所以又要分开查询
		SysOrg org = null;
		Area area = null;
		String sheetName = "统计分析表";
		List<SysOrg> orgList = null;
		List<Area> areaList = null;
		// 用于循环的数值
		int numForCycle = 0;
		// 查集团
		if (orgType.equals("1")) {
			org = orgService.getById(orgIdOrAreaId);
			sheetName = org.getOrgName() + sheetName;
			SysOrg sysOrgParams = new SysOrg();
			sysOrgParams.setParentId(orgIdOrAreaId);
			orgList = orgService.findList(sysOrgParams);
			numForCycle = orgList.size();
		}
		// 查公司（即11个地区）
		if (orgType.equals("2")) {
			org = orgService.getById(orgIdOrAreaId);
			sheetName = org.getOrgName() + sheetName;
			Map<String, Object> p = new HashMap<>();
			p.put("areaIds", org.getAreaId());
			Area areaParams = new Area();
			areaParams.setParams(p);
			areaList = areaService.getList(areaParams);
			numForCycle = areaList.size();
		}
		// 查区域（即公司）
		if (orgType.equals("2.5")) {
			area = areaService.getEntityById(orgIdOrAreaId);
			sheetName = area.getAreaName() + sheetName;
			SysOrg sysOrgParams = new SysOrg();
			sysOrgParams.setOrgType("3");
			sysOrgParams.setAreaId(orgIdOrAreaId);
			orgList = orgService.findList(sysOrgParams);
			numForCycle = orgList.size();
		}
		// 查水厂（即公司）--必定是一条数据
		if (orgType.equals("3")) {
			org = orgService.getById(orgIdOrAreaId);
			sheetName = org.getOrgName() + sheetName;
			numForCycle = 1;
		}
		
		OutputStream os = null;
		Workbook wb = null; // 工作薄
		String filename = "";

		try {
			// 模板资源路径
			ClassPathResource cpr = new ClassPathResource("static/template/report/statisticsReport.xlsx");
			InputStream inputStream = cpr.getInputStream();
			wb = new XSSFWorkbook(inputStream);
			Sheet sheet = wb.getSheetAt(0);

			Row row;
			Cell cell;

			// 设置标题名称
			Row titleRow = sheet.getRow(0);
			Cell titleCell = titleRow.getCell(0);
			titleCell.setCellValue(sheetName);
			
			// 第二行
			// 数据时间段
			Row secondRow = sheet.getRow(1);
			Cell periodCell = secondRow.getCell(0);
			String[] dateSta_array = dateSta.split("-");
			String[] dateEnd_array = dateEnd.split("-");
			periodCell.setCellValue("时间段：" + dateSta_array[0] + "年" + dateSta_array[1] + "月" + dateSta_array[2] + "日-" + dateEnd_array[0] + "年" +dateEnd_array[1] + "月" + dateEnd_array[2] + "日");
			// 导出时间
			Cell exportDateCell = secondRow.getCell(30);
			exportDateCell.setCellValue("日期：" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			// 操作人员
			Cell userCell = secondRow.getCell(34);
			userCell.setCellValue("操作者：" + sysUser.getUserName());
			
			// 循环插入数据
			int lastRow = sheet.getLastRowNum(); // 插入数据的数据ROW
			CellStyle cs = ExcelUtil.setSimpleCellStyle(wb); // 获取模板Excel单元格样式
			// 垂直、水平居中
			cs.setAlignment(HorizontalAlignment.CENTER);
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
			
			SysOrg sysOrg = null;
			
			for (int i = 0; i < numForCycle; i++) {
				int index = i + 1; // 序号
				String cellName = ""; // 公司、区域、工厂名称
				int orgCount = 0; // 工厂数量——用于平均
				
				if (orgType.equals("1")) { // 查集团
					sysOrg = orgList.get(i); // 当前对象
					params.put("parentId", sysOrg.getOrgId()); // 数据查询条件
					cellName = sysOrg.getOrgName();
					// 用于求平均
					SysOrg o = new SysOrg();
					o.setParentId(sysOrg.getOrgId());
					orgCount = orgService.getCount(sysOrg);
				}
				if (orgType.equals("2")) { // 查公司（即11个地区）
					area = areaList.get(i); // 当前对象
					params.put("areaId", area.getAreaId()); // 数据查询条件
					cellName = area.getAreaName();
					// 用于求平均
					SysOrg o = new SysOrg();
					Map<String, Object> p = new HashMap<>();
					p.put("areaId", area.getAreaId());
					orgCount = orgService.getCount(o);
				}
				if (orgType.equals("2.5")) { // 查区域（即公司）
					sysOrg = orgList.get(i); // 当前对象
					params.put("orgId", sysOrg.getOrgId()); // 数据查询条件
					cellName = sysOrg.getOrgName();
					orgCount = orgList.size(); // 2.5时，是区域，最小单位是水厂了，没有下级，所以就查当前级
				}
				if (orgType.equals("3")) { // 查水厂（即公司）--必定是一条数据
					sysOrg = org; // 当前对象
					params.put("orgId", sysOrg.getOrgId()); // 数据查询条件
					cellName = sysOrg.getOrgName();
					orgCount = 1;
				}
				row = sheet.createRow(lastRow + index); // 创建新的ROW，用于数据插入
				// 查数据
				ReportDateInfo r = new ReportDateInfo();
				r.setParams(params);
				Map<String, Object> rSumData = reportService.getSumData(r); // 查询在时间段内，当前组织结构下，所有数据的累计
				Object obj = null; // 用于获取map里面的值
				// Cell赋值开始
				// 序号
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(cs);
				// 区域/项目 公司名称
				cell = row.createCell(1);
				cell.setCellValue(cellName);
				cell.setCellStyle(cs);
				// 达标排放
				// 累计运行日（天）
				cell = row.createCell(2);
				cell.setCellValue(iDays);
				cell.setCellStyle(cs);
				// 累计超标日（天）
				int overNorm = 0, good = 0, bad = 0;
				GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
				BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
				if (orgType.equals("1"))
					params.put("areaIds", sysOrg.getAreaId());
				goodWaterHealthInfo.setParams(params);
				badWaterQualityInfo.setParams(params);
				if (orgType.equals("2.5") || orgType.equals("3")) {
					if ("1".equals(sysOrg.getFactoryType()))
						bad = badWaterService.getOverNorm(badWaterQualityInfo);
					if ("2".equals(sysOrg.getFactoryType()))
						good = goodWaterService.getOverNorm(goodWaterHealthInfo);
				} else {
					good = goodWaterService.getOverNorm(goodWaterHealthInfo);
					bad = badWaterService.getOverNorm(badWaterQualityInfo);
				}
				overNorm = good + bad;
				cell = row.createCell(3);
				cell.setCellValue(overNorm);
				cell.setCellStyle(cs);
				// 达标率（%）=（累计运行日 - 累计超标日） / 累计运行日 * 100
				int gtotal = 0, galarm = 0, btotal = 0, balarm = 0;
				goodWaterHealthInfo.setEffectIcon("1");
				gtotal = goodWaterService.getCount(goodWaterHealthInfo);
				goodWaterHealthInfo.setPassFlag("2");
				galarm = goodWaterService.getCount(goodWaterHealthInfo);
				badWaterQualityInfo.setEffectIcon("1");
				btotal = badWaterService.getCount(badWaterQualityInfo);
				badWaterQualityInfo.setPassFlag("2");
				balarm = badWaterService.getCount(badWaterQualityInfo);
//				params.put("areaIds", sysOrg.getAreaId());
				double alarm = galarm + balarm;
				double total = gtotal + btotal;
				cell = row.createCell(4);
				cell.setCellValue(total > 0 ? dataFormat((total - alarm) / total * 100) : 100);
				cell.setCellStyle(cs);
				// 如果查出来的数据为空，就全部写 0
				if (rSumData == null) {
					for (int c = 5; c < 38; c ++) {
						cell = row.createCell(c);
						cell.setCellValue(0);
						cell.setCellStyle(cs);
					}
					continue;
				}
				// 污水处理量（很恶心，这个只有污水厂才有）
				// 累计处理量（万吨）--污水厂的累计水量出水（所选日期的当日出水量相加）
				cell = row.createCell(5);
				obj = rSumData.get("totalOut");
				obj = obj == null ? 0 : obj;
				double totalOutTon = Double.parseDouble(obj.toString()); // 单位是吨
				double totalOut = totalOutTon / 10000; // 转换为万吨
				cell.setCellValue(dataFormat(totalOut));
				cell.setCellStyle(cs);
				// 累计设计处理能力（万吨）
				cell = row.createCell(6);
				obj = rSumData.get("ton");
				obj = obj == null ? 0 : obj;
				double ton = Double.parseDouble(obj.toString()); // 单位是万吨，不用转换
				cell.setCellValue(ton);
				cell.setCellStyle(cs);
				// 负荷率（%） = 累计量 / 累计设计处理能力，取值为百分数，精确小数点后 2 位
				cell = row.createCell(7);
				double LoadRate = 0;
				if (ton != 0) // 设计处理能力，在实际情况中可能没填，即会为 0，如果为 0，直接把符合率设为 0，以防被 0 除
					LoadRate = dataFormat(totalOut / ton * 100);
				cell.setCellValue(LoadRate);
				cell.setCellStyle(cs);
				// 平均处理能力（万吨/日）= 累计处理量 / 区域水厂（应该四污水厂）个数 / 天数
				cell = row.createCell(8);
				cell.setCellValue(dataFormat(totalOut / orgCount / iDays));
				cell.setCellStyle(cs);
				// 收费情况（收费情况的最小单位是areaId）
				// 累计应收水费（万元）= 区域求和：区域内各水厂现执行单价x区域内各水厂实际处理量（侯秋军）
				cell = row.createCell(9);
				obj = rSumData.get("account");
				obj = obj == null ? 0 : obj;
				double should = Double.parseDouble(obj.toString());
				cell.setCellValue(should);
				cell.setCellStyle(cs);
				// 累计实收取水费（万元）
				cell = row.createCell(10);
				double reality = 0;
				if (orgType.equals("1")) { // 11个地区，每个地区的水费累计——水厂没有财务，没有实收水费
					CostInfo c = new CostInfo();
					c.setEffectIcon("1");
					params.put("areaIds", sysOrg.getAreaId());
					c.setParams(params);
					Map<String, Object> ciMap = costInfoService.getSum(c);
					reality = Double.parseDouble(ciMap.get("currentTotal").toString());
				} else if (orgType.equals("2")) {
					CostInfo c = new CostInfo();
					c.setEffectIcon("1");
					c.setAreaId(area.getAreaId());
					reality = Double.parseDouble(costInfoService.getSum(c).get("currentTotal").toString());
				}
				cell.setCellValue(reality);
				cell.setCellStyle(cs);
				// 回款率（%）= 累计实收 / 累计应收，求百分比，保留小数后2位
				cell = row.createCell(11);
				cell.setCellValue(should == 0 ? 0 : dataFormat(reality / should * 100));
				cell.setCellStyle(cs);
				// 用电量
				// 累计总电量（度）
				cell = row.createCell(12);
				obj = rSumData.get("totalElec");
				obj = obj == null ? 0 : obj;
				double totalElec = Double.parseDouble(obj.toString());
				cell.setCellValue(totalElec);
				cell.setCellStyle(cs);
				// 吨水电耗（度/吨）= 累计总电量 / 累计处理量
				cell = row.createCell(13);
				double totalElecTon = totalOutTon == 0 ? 0 : (totalElec / totalOutTon);
				cell.setCellValue(dataFormat(totalElecTon));
				cell.setCellStyle(cs);
				// 平均吨水电耗（度/吨）= 区域内各水厂在该时间内吨水单耗求和 / 区域水厂个数
				cell = row.createCell(14);
				cell.setCellValue(dataFormat(totalElecTon / orgCount));
				cell.setCellStyle(cs);
				// 产泥量（这个又是只有污水厂才有的数据）
				// 累计产泥量（吨）
				cell = row.createCell(15);
				obj = rSumData.get("totalMud");
				cell.setCellValue(dataFormat(obj == null ? 0 : obj));
				cell.setCellStyle(cs);
				// 累计绝干泥总量（吨）
				cell = row.createCell(16);
				obj = rSumData.get("totalDryMud");
				obj = obj == null ? 0 : obj;
				double totalDryMud = Double.parseDouble(obj.toString());
				cell.setCellValue(totalDryMud);
				cell.setCellStyle(cs);
				// 万吨水绝干污泥量（吨/万吨）= 累计绝干泥总量 / 累计实际处理总量
				cell = row.createCell(17);
				cell.setCellValue(totalOut == 0 ? 0 : dataFormat(totalDryMud / totalOut));
				cell.setCellStyle(cs);
				// 水处理药剂
				// PAM(阳）
				// 累计用量(kg)
				cell = row.createCell(18);
				obj = rSumData.get("totalPAMYang");
				obj = obj == null ? 0 : obj;
				double totalPAMYang = Double.parseDouble(obj.toString());
				cell.setCellValue(totalPAMYang);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(19);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPAMYang / totalOutTon));
				cell.setCellStyle(cs);
				// PAM（阴）
				// 累计用量(kg)
				cell = row.createCell(20);
				obj = rSumData.get("totalPAMYin");
				obj = obj == null ? 0 : obj;
				double totalPAMYin = Double.parseDouble(obj.toString());
				cell.setCellValue(totalPAMYin);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(21);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPAMYin / totalOutTon));
				cell.setCellStyle(cs);
				// PAC
				// 累计用量(kg)
				cell = row.createCell(22);
				obj = rSumData.get("totalPAC");
				obj = obj == null ? 0 : obj;
				double totalPAC = Double.parseDouble(obj.toString());
				cell.setCellValue(totalPAC);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(23);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPAC / totalOutTon));
				cell.setCellStyle(cs);
				// 石灰
				// 累计用量(kg)
				cell = row.createCell(24);
				obj = rSumData.get("totalLime");
				obj = obj == null ? 0 : obj;
				double totalLime = Double.parseDouble(obj.toString());
				cell.setCellValue(totalLime);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(25);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalLime / totalOutTon));
				cell.setCellStyle(cs);
				// 复核除磷剂
				// 累计用量(kg)
				cell = row.createCell(26);
				obj = rSumData.get("totalPhosphorus");
				obj = obj == null ? 0 : obj;
				double totalPhosphorus = Double.parseDouble(obj.toString());
				cell.setCellValue(totalPhosphorus);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(27);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPhosphorus / totalOutTon));
				cell.setCellStyle(cs);
				// 盐酸（HCL）
				// 累计用量(kg)
				cell = row.createCell(28);
				obj = rSumData.get("totalHCL");
				obj = obj == null ? 0 : obj;
				double totalHCL = Double.parseDouble(obj.toString());
				cell.setCellValue(totalHCL);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(29);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalHCL / totalOutTon));
				cell.setCellStyle(cs);
				// 氯酸钠(NaCL)
				// 累计用量(kg)
				cell = row.createCell(30);
				obj = rSumData.get("totalSC");
				obj = obj == null ? 0 : obj;
				double totalSC = Double.parseDouble(obj.toString());
				cell.setCellValue(totalSC);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(31);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalSC / totalOutTon));
				cell.setCellStyle(cs);
				// 次氯酸钠(NaCLO)
				// 累计用量(kg)
				cell = row.createCell(32);
				obj = rSumData.get("totalNaCLO");
				obj = obj == null ? 0 : obj;
				double totalNaCLO = Double.parseDouble(obj.toString());
				cell.setCellValue(totalNaCLO);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(33);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalNaCLO / totalOutTon));
				cell.setCellStyle(cs);
				// 葡萄糖（C6H12O6）
				// 累计用量(kg)
				cell = row.createCell(34);
				obj = rSumData.get("totalGlucose");
				obj = obj == null ? 0 : obj;
				double totalGlucose = Double.parseDouble(obj.toString());
				cell.setCellValue(totalGlucose);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(35);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalGlucose / totalOutTon));
				cell.setCellStyle(cs);
				// 乙酸钠(C2H3O2Na)
				// 累计用量(kg)
				cell = row.createCell(36);
				obj = rSumData.get("totalSA");
				obj = obj == null ? 0 : obj;
				double totalSA = Double.parseDouble(obj.toString());
				cell.setCellValue(totalSA);
				cell.setCellStyle(cs);
				// 吨水药剂单耗(KG/吨）
				cell = row.createCell(37);
				cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalSA / totalOutTon));
				cell.setCellStyle(cs);
			}
			
			filename = sheetName + "（" + DateUtils.getDate() + "）.xlsx";
			os = new FileOutputStream(ExcelUtil.getAbsoluteFile(filename));
			wb.write(os);
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
			return AjaxResult.warn(e.toString());
		} finally {
			if (os != null) {
				os.flush();
				os.close();
				wb.close();
			}
		}
		return AjaxResult.success(filename);
	}
	
	@RequiresPermissions("report:his:daily")
	@RequestMapping("/daily")
	public String daily(ModelMap model) {
		SysOrg org = ShiroUtils.getSysUser().getSysOrgs().get(0);
		model.put("orgId", org.getOrgId());
		model.put("type", org.getOrgType());
		model.put("factoryType", org.getFactoryType());
		model.put("orgName", org.getOrgName());
		return prefix + "/daily";
	}
	
	@RequestMapping("/exportDailyReport")
	@ResponseBody
	public AjaxResult exportDailyReport(@RequestParam Map<String, String> map) throws IOException {
		SysOrg org = orgService.getById(map.get("orgId"));
		if ("1".equals(org.getFactoryType())) {
			return sewagePlantDailyReport(map);
		}
		if ("2".equals(org.getFactoryType())) {
			return waterWorksDailyReport(map);
		}
		return AjaxResult.warn("奇了一个怪");
	}
	
	@RequestMapping("/searchDailyReport")
	@ResponseBody
	public TableDataInfo searchDailyReport(@RequestParam Map<String, String> map) throws IOException {
		SysOrg org = orgService.getById(map.get("orgId"));
		if ("1".equals(org.getFactoryType())) {
			return getDataTable(searchDailyReportWS(map));
		}
		if ("2".equals(org.getFactoryType())) {
			return getDataTable(searchDailyReportGS(map));
		}
		return getDataTable(new ArrayList<>());
	}
	
	public List<Map<String, Object>> searchDailyReportWS(Map<String, String> map) throws IOException {
		String orgId = map.get("orgId");
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		if (orgId == null || "".equals(orgId) || dateSta == null || "".equals(dateSta) || dateEnd == null || "".equals(dateEnd))
			return new ArrayList<>();
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 当前组织机构
		SysOrg org = orgService.getById(orgId);
		// 当前水厂信息
		SysFactoryInfo factoryParam = new SysFactoryInfo();
		factoryParam.setOrgId(orgId);
		SysFactoryInfo factory = factoryService.getEntity(factoryParam);
		
		// 用于返回的集合
		List<Map<String, Object>> reList = new ArrayList<>();
		Map<String, Object> reMap = null;
		
		Date sta = DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta);
		// 查数据
		ReportDateInfo r = new ReportDateInfo();
		// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
		Map<String, Object> params = new HashMap<>();
		r.setFactoryId(orgId);
		params.put("dateSta", dateSta);
		params.put("dateEnd", dateEndReal);
		params.put("type", org.getFactoryType());
		r.setParams(params);
		Map<String, Map<String, Object>> rDailyDataMap = reportService.getDailyReport(r); // 查询在时间段内，当前组织结构下，所有数据的累计
		Map<String, Object> rDailyData = null;
		for (int i = 0; i < iDays; i++) {
			reMap = new HashMap<>();
			// 日期
			reMap.put("date", DateUtils.parseDateToStr("yyyy/MM/dd", DateUtils.addDay(sta, i)));
			// 找数据
			rDailyData = rDailyDataMap.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDay(sta, i)));
			if (rDailyData == null) {
				reList.add(reMap);
				continue;
			}
			// 超标——_如果超标为1，未超标为 0
			int overNorm = 0;
			if (rDailyData.get("overNorm") != null && rDailyData.get("overNorm").equals("2")) {
				overNorm = 1;
			}
			reMap.put("overNorm", overNorm);
			// 处理水量（万吨/日）
			double todayOut = Double.parseDouble(rDailyData.get("todayOut") == null ? "0" : rDailyData.get("todayOut").toString()); // 单位吨
			reMap.put("todayOut", dataFormat(todayOut / 10000)); // 转万吨
			// 污泥量（吨）
			reMap.put("todayMud", Double.parseDouble(rDailyData.get("todayMud") == null ? "0" : rDailyData.get("todayMud").toString()));
			// 绝干污泥量（吨）
			reMap.put("dryMud", Double.parseDouble(rDailyData.get("dryMud") == null ? "0" : rDailyData.get("dryMud").toString()));
			// 工时情况
			// 水厂工作总人次
			int personNum = 0;
			if (factory != null && factory.getPersonNum() != null)
				personNum = Integer.parseInt(factory.getPersonNum());
			reMap.put("personNum", personNum);
			// 吨水人工（小时/吨）= 人工总人次 * 8 / 处理水量
			reMap.put("dsrg", todayOut == 0 ? 0 : dataFormat(personNum * 8 / todayOut));
			// 能耗数据（度）
			// 污水处理电量
			double todayElec = Double.parseDouble(rDailyData.get("todayElec") == null ? "0" : rDailyData.get("todayElec").toString());
			reMap.put("todayElec", todayElec);
			// 泵站/其它电量
			double pumpElec = Double.parseDouble(rDailyData.get("pumpElec") == null ? "0" : rDailyData.get("pumpElec").toString());
			reMap.put("pumpElec", pumpElec);
			// 总计电量
			reMap.put("totalElec", dataFormat(todayElec + pumpElec));
			// 能耗（度/吨）
			reMap.put("dynamic", todayOut == 0 ? 0 : dataFormat((todayElec + pumpElec) / todayOut));
			// 药耗数据
			double todayPAMYang = Double.parseDouble(rDailyData.get("todayPAMYang").toString()); // PAM(阳）
			double todayPAMYin = Double.parseDouble(rDailyData.get("todayPAMYin").toString()); // PAM（阴）
			double todayPAC = Double.parseDouble(rDailyData.get("todayPAC").toString()); // PAC
			double todayLime = Double.parseDouble(rDailyData.get("todayLime").toString()); // 石灰
			double todayPhosphorus = Double.parseDouble(rDailyData.get("todayPhosphorus").toString()); // 复核除磷剂
			double todayHCL = Double.parseDouble(rDailyData.get("todayHCL").toString()); // 盐酸（HCL）
			double todaySC = Double.parseDouble(rDailyData.get("todaySC").toString()); // 氯酸钠（NaCL）
			double todayNaCLO = Double.parseDouble(rDailyData.get("todayNaCLO").toString()); // 次氯酸钠（NaCLO）
			double todayGlucose = Double.parseDouble(rDailyData.get("todayGlucose").toString()); // 葡萄糖（C6H12O6）
			double todaySA = Double.parseDouble(rDailyData.get("todaySA").toString()); // 乙酸钠(C2H3O2Na)
			// 综合药耗（KG/吨）= 所有药品用量求和 / 实际处理量
			reMap.put("composite", todayOut == 0 ? 0 : dataFormat((todayPAMYang + todayPAMYin + todayPAC + todayLime + todayPhosphorus + todayHCL + todaySC + todayNaCLO + todayGlucose + todaySA) / todayOut));
			// PAM(阳）
			// 用量（kg）
			reMap.put("todayPAMYang", todayPAMYang);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPAMYang", todayOut == 0 ? 0 : dataFormat(todayPAMYang / todayOut));
			// PAM（阴）
			// 用量（kg）
			reMap.put("todayPAMYin", todayPAMYin);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPAMYin", todayOut == 0 ? 0 : dataFormat(todayPAMYin / todayOut));
			// PAC
			// 用量（kg）
			reMap.put("todayPAC", todayPAC);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPAC", todayOut == 0 ? 0 : dataFormat(todayPAC / todayOut));
			// 石灰
			// 用量（kg）
			reMap.put("todayLime", todayLime);
			// 药剂单耗 （KG/吨）
			reMap.put("tonLime", todayOut == 0 ? 0 : dataFormat(todayLime / todayOut));
			// 复核除磷剂
			// 用量（kg）
			reMap.put("todayPhosphorus", todayPhosphorus);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPhosphorus", todayOut == 0 ? 0 : dataFormat(todayPhosphorus / todayOut));
			// 盐酸（HCL）
			// 用量（kg）
			reMap.put("todayHCL", todayHCL);
			// 药剂单耗 （KG/吨）
			reMap.put("tonHCL", todayOut == 0 ? 0 : dataFormat(todayHCL / todayOut));
			// 氯酸钠（NaCL）
			// 用量（kg）
			reMap.put("todaySC", todaySC);
			// 药剂单耗 （KG/吨）
			reMap.put("tonSC", todayOut == 0 ? 0 : dataFormat(todaySC / todayOut));
			// 次氯酸钠（NaCLO）
			// 用量（kg）
			reMap.put("todayNaCLO", todayNaCLO);
			// 药剂单耗 （KG/吨）
			reMap.put("tonNaCLO", todayOut == 0 ? 0 : dataFormat(todayNaCLO / todayOut));
			// 葡萄糖（C6H12O6）
			// 用量（kg）
			reMap.put("todayGlucose", todayGlucose);
			// 药剂单耗 （KG/吨）
			reMap.put("tonGlucose", todayOut == 0 ? 0 : dataFormat(todayGlucose / todayOut));
			// 乙酸钠 （C2H3O2Na）
			// 用量（kg）
			reMap.put("todaySA", todaySA);
			// 药剂单耗 （KG/吨）
			reMap.put("tonSA", todayOut == 0 ? 0 : dataFormat(todaySA / todayOut));
			// 进水化验数据
			// COD（mg/L）
			reMap.put("codIn", rDailyData.get("codIn") == null ? 0 : Integer.parseInt(rDailyData.get("codIn").toString()));
			// BOD5（mg/L）
			reMap.put("bod5In", Double.parseDouble(rDailyData.get("bod5In") == null ? "0" : rDailyData.get("bod5In").toString()));
			// NH3-N（mg/L）
			reMap.put("adanIn", Double.parseDouble(rDailyData.get("adanIn") == null ? "0" : rDailyData.get("adanIn").toString()));
			// T-N（mg/L）
			reMap.put("zdanIn", Double.parseDouble(rDailyData.get("zdanIn") == null ? "0" : rDailyData.get("zdanIn").toString()));
			// T-P（mg/L）
			reMap.put("zlinIn", Double.parseDouble(rDailyData.get("zlinIn") == null ? "0" : rDailyData.get("zlinIn").toString()));
			// SS（mg/L）
			reMap.put("xfuIn", Double.parseDouble(rDailyData.get("xfuIn") == null ? "0" : rDailyData.get("xfuIn").toString()));
			// PH值
			reMap.put("phIn", Double.parseDouble(rDailyData.get("phIn") == null ? "0" : rDailyData.get("phIn").toString()));
			// 温度（℃）
			reMap.put("tempIn", Double.parseDouble(rDailyData.get("tempIn") == null ? "0" : rDailyData.get("tempIn").toString()));
			// 出水化验数据
			// COD（mg/L）
			reMap.put("codOut", Double.parseDouble(rDailyData.get("codOut") == null ? "0" : rDailyData.get("codOut").toString()));
			// BOD5（mg/L）
			reMap.put("bod5Out", Double.parseDouble(rDailyData.get("bod5Out") == null ? "0" : rDailyData.get("bod5Out").toString()));
			// NH3-N（mg/L）
			reMap.put("adanOut", Double.parseDouble(rDailyData.get("adanOut") == null ? "0" : rDailyData.get("adanOut").toString()));
			// T-N（mg/L）
			reMap.put("zdanOut", Double.parseDouble(rDailyData.get("zdanOut") == null ? "0" : rDailyData.get("zdanOut").toString()));
			// T-P（mg/L）
			reMap.put("zlinOut", Double.parseDouble(rDailyData.get("zlinOut") == null ? "0" : rDailyData.get("zlinOut").toString()));
			// SS（mg/L）
			reMap.put("xfuOut", Double.parseDouble(rDailyData.get("xfuOut") == null ? "0" : rDailyData.get("xfuOut").toString()));
			// PH值
			reMap.put("phOut", Double.parseDouble(rDailyData.get("phOut") == null ? "0" : rDailyData.get("phOut").toString()));
			// 温度（℃）
			reMap.put("tempOut", Double.parseDouble(rDailyData.get("tempOut") == null ? "0" : rDailyData.get("tempOut").toString()));
			// 粪大肠菌群数（个/L）
			reMap.put("fengJun", Double.parseDouble(rDailyData.get("fengJun") == null ? "0" : rDailyData.get("fengJun").toString()));
			// 化验室生化池数据
			// 生化池  MLSS（mg/L）
			reMap.put("mlss", Double.parseDouble(rDailyData.get("mlss") == null ? "0" : rDailyData.get("mlss").toString()));
			// 生化池  MLVSS（mg/L）
			reMap.put("mlvss", Double.parseDouble(rDailyData.get("mlvss") == null ? "0" : rDailyData.get("mlvss").toString()));
			// 生化池出水口/曝气末  DO（mg/L）
			reMap.put("testDo", Double.parseDouble(rDailyData.get("testDo") == null ? "0" : rDailyData.get("testDo").toString()));
			// 生化池  SV30（%）
			reMap.put("sv30", Double.parseDouble(rDailyData.get("sv30") == null ? "0" : rDailyData.get("sv30").toString()));
			// 污泥含水率（%）
			reMap.put("waterSludge", Double.parseDouble(rDailyData.get("waterSludge") == null ? "0" : rDailyData.get("waterSludge").toString()));
			// 进出口在线数据（中控数值）（日平均值）
			// 进水
			// COD (mg/L)
			reMap.put("CODIn", Double.parseDouble(rDailyData.get("CODIn") == null ? "0" : rDailyData.get("CODIn").toString()));
			// NH3-N (mg/L)
			reMap.put("NH3NIn", Double.parseDouble(rDailyData.get("NH3NIn") == null ? "0" : rDailyData.get("NH3NIn").toString()));
			// TP  (mg/L)
			reMap.put("TPIn", Double.parseDouble(rDailyData.get("TPIn") == null ? "0" : rDailyData.get("TPIn").toString()));
			// TN  (mg/L)
			reMap.put("TNIn", Double.parseDouble(rDailyData.get("TNIn") == null ? "0" : rDailyData.get("TNIn").toString()));
			// SS  (mg/L)
			reMap.put("SSIn", Double.parseDouble(rDailyData.get("SSIn") == null ? "0" : rDailyData.get("SSIn").toString()));
			// 出水
			// COD (mg/L)
			reMap.put("CODOut", Double.parseDouble(rDailyData.get("CODOut") == null ? "0" : rDailyData.get("CODOut").toString()));
			// NH3-N (mg/L)
			reMap.put("NH3NOut", Double.parseDouble(rDailyData.get("NH3NOut") == null ? "0" : rDailyData.get("NH3NOut").toString()));
			// TP  (mg/L)
			reMap.put("TPOut", Double.parseDouble(rDailyData.get("TPOut") == null ? "0" : rDailyData.get("TPOut").toString()));
			// TN  (mg/L)
			reMap.put("TNOut", Double.parseDouble(rDailyData.get("TNOut") == null ? "0" : rDailyData.get("TNOut").toString()));
			// SS  (mg/L)
			reMap.put("SSOut", Double.parseDouble(rDailyData.get("SSOut") == null ? "0" : rDailyData.get("SSOut").toString()));
			
			reList.add(reMap);
		}
		return reList;
	}
	
	public List<Map<String, Object>> searchDailyReportGS(Map<String, String> map) throws IOException {
		String orgId = map.get("orgId");
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		if (orgId == null || "".equals(orgId) || dateSta == null || "".equals(dateSta) || dateEnd == null || "".equals(dateEnd))
			return new ArrayList<>();
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 当前组织机构
		SysOrg org = orgService.getById(orgId);
		// 当前水厂信息
		SysFactoryInfo factoryParam = new SysFactoryInfo();
		factoryParam.setOrgId(orgId);
		SysFactoryInfo factory = factoryService.getEntity(factoryParam);

		// 用于返回的集合
		List<Map<String, Object>> reList = new ArrayList<>();
		Map<String, Object> reMap = null;
			
		Date sta = DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta);
		// 查数据
		ReportDateInfo r = new ReportDateInfo();
		// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
		Map<String, Object> params = new HashMap<>();
		r.setFactoryId(orgId);
		params.put("dateSta", dateSta);
		params.put("dateEnd", dateEndReal);
		params.put("type", org.getFactoryType());
		r.setParams(params);
		Map<String, Map<String, Object>> rDailyDataMap = reportService.getDailyReport(r); // 查询在时间段内，当前组织结构下，所有数据的累计
		Map<String, Object> rDailyData = null;
		for (int i = 0; i < iDays; i++) {
			reMap = new HashMap<>();
			// 日期
			reMap.put("date", DateUtils.parseDateToStr("yyyy/MM/dd", DateUtils.addDay(sta, i)));
			// 找数据
			rDailyData = rDailyDataMap.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDay(sta, i)));
			if (rDailyData == null) {
				reList.add(reMap);
				continue;
			}
			// 超标——_如果超标为1，未超标为 0
			int overNorm = 0;
			if (rDailyData.get("overNorm") != null && rDailyData.get("overNorm").equals("2")) {
				overNorm = 1;
			}
			reMap.put("overNorm", overNorm);
			// 产水量（万吨/日）
			double todayOut = Double.parseDouble(rDailyData.get("todayOut") == null ? "0" : rDailyData.get("todayOut").toString()); // 单位吨
			reMap.put("todayOut", todayOut);
			// 工时情况
			// 水厂工作总人次
			int personNum = 0;
			if (factory != null && factory.getPersonNum() != null)
				personNum = Integer.parseInt(factory.getPersonNum());
			reMap.put("personNum", personNum);
			// 吨水人工（小时/吨）= 人工总人次 * 8 / 处理水量
			reMap.put("dsrg", todayOut == 0 ? 0 : dataFormat(personNum * 8 / todayOut));
			// 能耗数据（度）
			// 水厂电量
			double todayElec = Double.parseDouble(rDailyData.get("todayElec") == null ? "0" : rDailyData.get("todayElec").toString());
			reMap.put("todayElec", todayElec);
			// 泵站/其它电量
			double pumpElec = Double.parseDouble(rDailyData.get("pumpElec") == null ? "0" : rDailyData.get("pumpElec").toString());
			reMap.put("pumpElec", pumpElec);
			// 总计电量
			reMap.put("totalElec", dataFormat(todayElec + pumpElec));
			// 能耗（度/吨）
			reMap.put("dynamic", todayOut == 0 ? 0 : dataFormat((todayElec + pumpElec) / todayOut));
			// 药耗数据
			double todayPAMYang = Double.parseDouble(rDailyData.get("todayPAMYang").toString()); // PAM(阳）
			double todayPAMYin = Double.parseDouble(rDailyData.get("todayPAMYin").toString()); // PAM（阴）
			double todayPAC = Double.parseDouble(rDailyData.get("todayPAC").toString()); // PAC
			double todayLime = Double.parseDouble(rDailyData.get("todayLime").toString()); // 石灰
			double todayPhosphorus = Double.parseDouble(rDailyData.get("todayPhosphorus").toString()); // 复核除磷剂
			double todayHCL = Double.parseDouble(rDailyData.get("todayHCL").toString()); // 盐酸（HCL）
			double todaySC = Double.parseDouble(rDailyData.get("todaySC").toString()); // 氯酸钠（NaCL）
			double todayNaCLO = Double.parseDouble(rDailyData.get("todayNaCLO").toString()); // 次氯酸钠（NaCLO）
			double todayGlucose = Double.parseDouble(rDailyData.get("todayGlucose").toString()); // 葡萄糖（C6H12O6）
			double todaySA = Double.parseDouble(rDailyData.get("todaySA").toString()); // 乙酸钠(C2H3O2Na)
			// 综合药耗（KG/吨）= 所有药品用量求和 / 实际处理量
			reMap.put("composite", todayOut == 0 ? 0 : dataFormat((todayPAMYang + todayPAMYin + todayPAC + todayLime + todayPhosphorus + todayHCL + todaySC + todayNaCLO + todayGlucose + todaySA) / todayOut));
			// PAM(阳）
			// 用量（kg）
			reMap.put("todayPAMYang", todayPAMYang);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPAMYang", todayOut == 0 ? 0 : dataFormat(todayPAMYang / todayOut));
			// PAM（阴）
			// 用量（kg）
			reMap.put("todayPAMYin", todayPAMYin);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPAMYin", todayOut == 0 ? 0 : dataFormat(todayPAMYin / todayOut));
			// PAC
			// 用量（kg）
			reMap.put("todayPAC", todayPAC);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPAC", todayOut == 0 ? 0 : dataFormat(todayPAC / todayOut));
			// 石灰
			// 用量（kg）
			reMap.put("todayLime", todayLime);
			// 药剂单耗 （KG/吨）
			reMap.put("tonLime", todayOut == 0 ? 0 : dataFormat(todayLime / todayOut));
			// 复核除磷剂
			// 用量（kg）
			reMap.put("todayPhosphorus", todayPhosphorus);
			// 药剂单耗 （KG/吨）
			reMap.put("tonPhosphorus", todayOut == 0 ? 0 : dataFormat(todayPhosphorus / todayOut));
			// 盐酸（HCL）
			// 用量（kg）
			reMap.put("todayHCL", todayHCL);
			// 药剂单耗 （KG/吨）
			reMap.put("tonHCL", todayOut == 0 ? 0 : dataFormat(todayHCL / todayOut));
			// 氯酸钠（NaCL）
			// 用量（kg）
			reMap.put("todaySC", todaySC);
			// 药剂单耗 （KG/吨）
			reMap.put("tonSC", todayOut == 0 ? 0 : dataFormat(todaySC / todayOut));
			// 次氯酸钠（NaCLO）
			// 用量（kg）
			reMap.put("todayNaCLO", todayNaCLO);
			// 药剂单耗 （KG/吨）
			reMap.put("tonNaCLO", todayOut == 0 ? 0 : dataFormat(todayNaCLO / todayOut));
			// 葡萄糖（C6H12O6）
			// 用量（kg）
			reMap.put("todayGlucose", todayGlucose);
			// 药剂单耗 （KG/吨）
			reMap.put("tonGlucose", todayOut == 0 ? 0 : dataFormat(todayGlucose / todayOut));
			// 乙酸钠 （C2H3O2Na）
			// 用量（kg）
			reMap.put("todaySA", todaySA);
			// 药剂单耗 （KG/吨）
			reMap.put("tonSA", todayOut == 0 ? 0 : dataFormat(todaySA / todayOut));
			// 进水化验数据
			// 肉眼可见物
			reMap.put("eyeOut", rDailyData.get("eyeOut").toString());
			// 色度
			reMap.put("colourOut", Double.parseDouble(rDailyData.get("colourOut").toString()));
			// 嗅和味
			reMap.put("cwOut", rDailyData.get("cwOut").toString());
			// 出水CLO2
			reMap.put("twoOut", Double.parseDouble(rDailyData.get("twoOut").toString()));
			// 浊度
			reMap.put("ntuOut", Double.parseDouble(rDailyData.get("ntuOut").toString()));
			// 温度（℃）
			reMap.put("tempOut", Double.parseDouble(rDailyData.get("tempOut").toString()));
			// 总大肠菌群
			reMap.put("zdjOut", Double.parseDouble(rDailyData.get("zdjOut").toString()));
			// 细菌总数 
			reMap.put("xjzsOut", Double.parseDouble(rDailyData.get("xjzsOut").toString()));
			// 进出口在线数据（中控数值）（日平均值）
			// 进水
			// PH值
			reMap.put("PHIn", Double.parseDouble(rDailyData.get("PHIn").toString()));
			// 浊度
			reMap.put("NTUIn", Double.parseDouble(rDailyData.get("NTUIn").toString()));
			// 出水
			// PH值
			reMap.put("PHOut", Double.parseDouble(rDailyData.get("PHOut").toString()));
			// 浊度
			reMap.put("NTUOut", Double.parseDouble(rDailyData.get("NTUOut").toString()));
			// 后置加氯/出水CL02
			reMap.put("COL2Out", Double.parseDouble(rDailyData.get("COL2Out").toString()));
			// 细菌总数
			reMap.put("germOut", Double.parseDouble(rDailyData.get("germOut").toString()));
			
			reList.add(reMap);
		}
		return reList;
	}
	
	// 污水厂日报表导出
	public AjaxResult sewagePlantDailyReport(Map<String, String> map) throws IOException {
		String orgId = map.get("orgId");
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 获取当前用户
		SysUser sysUser = ShiroUtils.getSysUser();
		// 当前组织机构
		SysOrg org = orgService.getById(orgId);
		// 当前水厂信息
		SysFactoryInfo factoryParam = new SysFactoryInfo();
		factoryParam.setOrgId(orgId);
		SysFactoryInfo factory = factoryService.getEntity(factoryParam);
		
		String sheetName = org.getOrgName() + "运行日报表";
		
		OutputStream os = null;
		Workbook wb = null; // 工作薄
		String filename = "";

		try {
			// 模板资源路径
			ClassPathResource cpr = new ClassPathResource("static/template/report/sewagePlantDailyReport.xlsx");
			InputStream inputStream = cpr.getInputStream();
			wb = new XSSFWorkbook(inputStream);
			Sheet sheet = wb.getSheetAt(0);

			Row row;
			Cell cell;

			// 设置标题名称
			Row titleRow = sheet.getRow(0);
			Cell titleCell = titleRow.getCell(0);
			titleCell.setCellValue(sheetName);
			
			// 第二行
			// 数据时间段
			Row secondRow = sheet.getRow(1);
			Cell periodCell = secondRow.getCell(0);
			String[] dateSta_array = dateSta.split("-");
			String[] dateEnd_array = dateEnd.split("-");
			periodCell.setCellValue("时间段：" + dateSta_array[0] + "年" + dateSta_array[1] + "月" + dateSta_array[2] + "日-" + dateEnd_array[0] + "年" +dateEnd_array[1] + "月" + dateEnd_array[2] + "日");
			// 设计处理能力
			Cell capacityCell = secondRow.getCell(8);
			capacityCell.setCellValue("设计能力：" + (factory != null && factory.getTon() != null ? factory.getTon() : "0") + "万吨/日");
			// 导出时间
			Cell exportDateCell = secondRow.getCell(55);
			exportDateCell.setCellValue("日期：" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			// 操作人员
			Cell userCell = secondRow.getCell(60);
			userCell.setCellValue("操作者：" + sysUser.getUserName());
			
			// 循环插入数据
			int lastRow = sheet.getLastRowNum(); // 插入数据的数据ROW
			CellStyle cs = ExcelUtil.setSimpleCellStyle(wb); // 获取模板Excel单元格样式
			// 垂直、水平居中
			cs.setAlignment(HorizontalAlignment.CENTER);
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
			
			int index = 0;
			Date sta = DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta);
			// 查数据
			ReportDateInfo r = new ReportDateInfo();
			// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
			Map<String, Object> params = new HashMap<>();
			r.setFactoryId(orgId);
			params.put("dateSta", dateSta);
			params.put("dateEnd", dateEndReal);
			params.put("type", org.getFactoryType());
			r.setParams(params);
			Map<String, Map<String, Object>> rDailyDataMap = reportService.getDailyReport(r); // 查询在时间段内，当前组织结构下，所有数据的累计
			Map<String, Object> rDailyData = null;
			for (int i = 0; i < iDays; i++) {
				index = i + 1;
				row = sheet.createRow(lastRow + index); // 创建新的ROW，用于数据插入
				// 用于查数据
				Object obj = null;
				// Cell赋值开始
				// 序号
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(cs);
				// 日期
				cell = row.createCell(1);
				cell.setCellValue(DateUtils.parseDateToStr("yyyy/MM/dd", DateUtils.addDay(sta, i)));
				cell.setCellStyle(cs);
				// 找数据
				rDailyData = rDailyDataMap.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDay(sta, i)));
				if (rDailyData == null) { // 无数据，全部填0
					for (int n = 2; n < 65; n ++) {
						cell = row.createCell(n);
						cell.setCellValue(0);
						cell.setCellStyle(cs);
					}
					continue;
				}
				// 超标——_如果超标为1，未超标为 0
				cell = row.createCell(2);
				int overNorm = 0;
				if (rDailyData.get("overNorm") != null && rDailyData.get("overNorm").equals("2")) {
					overNorm = 1;
				}
				cell.setCellValue(overNorm);
				cell.setCellStyle(cs);
				// 处理水量（万吨/日）
				cell = row.createCell(3);
				obj = rDailyData.get("todayOut");
				obj = obj == null ? 0 : obj;
				double todayOut = Double.parseDouble(obj.toString()); // 单位吨
				cell.setCellValue(dataFormat(todayOut / 10000)); // 转万吨
				cell.setCellStyle(cs);
				// 污泥量（吨）
				cell = row.createCell(4);
				obj = rDailyData.get("todayMud");
				obj = obj == null ? 0 : obj;
				cell.setCellValue(Double.parseDouble(obj.toString()));
				cell.setCellStyle(cs);
				// 绝干污泥量（吨）
				cell = row.createCell(5);
				obj = rDailyData.get("dryMud");
				obj = obj == null ? 0 : obj;
				cell.setCellValue(Double.parseDouble(obj.toString()));
				cell.setCellStyle(cs);
				// 工时情况
				// 水厂工作总人次
				cell = row.createCell(6);
				int personNum = 0;
				if (factory != null && factory.getPersonNum() != null)
					personNum = Integer.parseInt(factory.getPersonNum());
				cell.setCellValue(personNum);
				cell.setCellStyle(cs);
				// 吨水人工（小时/吨）= 人工总人次 * 8 / 处理水量
				cell = row.createCell(7);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(personNum * 8 / todayOut));
				cell.setCellStyle(cs);
				// 能耗数据（度）
				// 污水处理电量
				cell = row.createCell(8);
				obj = rDailyData.get("todayElec");
				obj = obj == null ? 0 : obj;
				double todayElec = Double.parseDouble(obj.toString());
				cell.setCellValue(todayElec);
				cell.setCellStyle(cs);
				// 泵站/其它电量
				cell = row.createCell(9);
				obj = rDailyData.get("pumpElec");
				obj = obj == null ? 0 : obj;
				double pumpElec = Double.parseDouble(obj.toString());
				cell.setCellValue(pumpElec);
				cell.setCellStyle(cs);
				// 总计电量
				cell = row.createCell(10);
				cell.setCellValue(dataFormat(todayElec + pumpElec));
				cell.setCellStyle(cs);
				// 能耗（度/吨）
				cell = row.createCell(11);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat((todayElec + pumpElec) / todayOut));
				cell.setCellStyle(cs);
				// 药耗数据
				double todayPAMYang = Double.parseDouble(rDailyData.get("todayPAMYang").toString()); // PAM(阳）
				double todayPAMYin = Double.parseDouble(rDailyData.get("todayPAMYin").toString()); // PAM（阴）
				double todayPAC = Double.parseDouble(rDailyData.get("todayPAC").toString()); // PAC
				double todayLime = Double.parseDouble(rDailyData.get("todayLime").toString()); // 石灰
				double todayPhosphorus = Double.parseDouble(rDailyData.get("todayPhosphorus").toString()); // 复核除磷剂
				double todayHCL = Double.parseDouble(rDailyData.get("todayHCL").toString()); // 盐酸（HCL）
				double todaySC = Double.parseDouble(rDailyData.get("todaySC").toString()); // 氯酸钠（NaCL）
				double todayNaCLO = Double.parseDouble(rDailyData.get("todayNaCLO").toString()); // 次氯酸钠（NaCLO）
				double todayGlucose = Double.parseDouble(rDailyData.get("todayGlucose").toString()); // 葡萄糖（C6H12O6）
				double todaySA = Double.parseDouble(rDailyData.get("todaySA").toString()); // 乙酸钠(C2H3O2Na)
				// 综合药耗（KG/吨）= 所有药品用量求和 / 实际处理量
				cell = row.createCell(12);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat((todayPAMYang + todayPAMYin + todayPAC + todayLime + todayPhosphorus + todayHCL + todaySC + todayNaCLO + todayGlucose + todaySA) / todayOut));
				cell.setCellStyle(cs);
				// PAM(阳）
				// 用量（kg）
				cell = row.createCell(13);
				cell.setCellValue(todayPAMYang);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(14);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPAMYang / todayOut));
				cell.setCellStyle(cs);
				// PAM（阴）
				// 用量（kg）
				cell = row.createCell(15);
				cell.setCellValue(todayPAMYin);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(16);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPAMYin / todayOut));
				cell.setCellStyle(cs);
				// PAC
				// 用量（kg）
				cell = row.createCell(17);
				cell.setCellValue(todayPAC);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(18);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPAC / todayOut));
				cell.setCellStyle(cs);
				// 石灰
				// 用量（kg）
				cell = row.createCell(19);
				cell.setCellValue(todayLime);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(20);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayLime / todayOut));
				cell.setCellStyle(cs);
				// 复核除磷剂
				// 用量（kg）
				cell = row.createCell(21);
				cell.setCellValue(todayPhosphorus);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(22);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPhosphorus / todayOut));
				cell.setCellStyle(cs);
				// 盐酸（HCL）
				// 用量（kg）
				cell = row.createCell(23);
				cell.setCellValue(todayHCL);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(24);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayHCL / todayOut));
				cell.setCellStyle(cs);
				// 氯酸钠（NaCL）
				// 用量（kg）
				cell = row.createCell(25);
				cell.setCellValue(todaySC);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(26);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todaySC / todayOut));
				cell.setCellStyle(cs);
				// 次氯酸钠（NaCLO）
				// 用量（kg）
				cell = row.createCell(27);
				cell.setCellValue(todayNaCLO);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(28);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayNaCLO / todayOut));
				cell.setCellStyle(cs);
				// 葡萄糖（C6H12O6）
				// 用量（kg）
				cell = row.createCell(29);
				cell.setCellValue(todayGlucose);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(30);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayGlucose / todayOut));
				cell.setCellStyle(cs);
				// 乙酸钠 （C2H3O2Na）
				// 用量（kg）
				cell = row.createCell(31);
				cell.setCellValue(todaySA);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(32);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todaySA / todayOut));
				cell.setCellStyle(cs);
				// 进水化验数据
				// COD（mg/L）
				cell = row.createCell(33);
				cell.setCellValue(Integer.parseInt(rDailyData.get("codIn").toString()));
				cell.setCellStyle(cs);
				// BOD5（mg/L）
				cell = row.createCell(34);
				cell.setCellValue(Double.parseDouble(rDailyData.get("bod5In").toString()));
				cell.setCellStyle(cs);
				// NH3-N（mg/L）
				cell = row.createCell(35);
				cell.setCellValue(Double.parseDouble(rDailyData.get("adanIn").toString()));
				cell.setCellStyle(cs);
				// T-N（mg/L）
				cell = row.createCell(36);
				cell.setCellValue(Double.parseDouble(rDailyData.get("zdanIn").toString()));
				cell.setCellStyle(cs);
				// T-P（mg/L）
				cell = row.createCell(37);
				cell.setCellValue(Double.parseDouble(rDailyData.get("zlinIn").toString()));
				cell.setCellStyle(cs);
				// SS（mg/L）
				cell = row.createCell(38);
				cell.setCellValue(Integer.parseInt(rDailyData.get("xfuIn").toString()));
				cell.setCellStyle(cs);
				// PH值
				cell = row.createCell(39);
				cell.setCellValue(Double.parseDouble(rDailyData.get("phIn").toString()));
				cell.setCellStyle(cs);
				// 温度（℃）
				cell = row.createCell(40);
				cell.setCellValue(Double.parseDouble(rDailyData.get("tempIn").toString()));
				cell.setCellStyle(cs);
				// 出水化验数据
				// COD（mg/L）
				cell = row.createCell(41);
				cell.setCellValue(Integer.parseInt(rDailyData.get("codOut").toString()));
				cell.setCellStyle(cs);
				// BOD5（mg/L）
				cell = row.createCell(42);
				cell.setCellValue(Double.parseDouble(rDailyData.get("bod5Out").toString()));
				cell.setCellStyle(cs);
				// NH3-N（mg/L）
				cell = row.createCell(43);
				cell.setCellValue(Double.parseDouble(rDailyData.get("adanOut").toString()));
				cell.setCellStyle(cs);
				// T-N（mg/L）
				cell = row.createCell(44);
				cell.setCellValue(Double.parseDouble(rDailyData.get("zdanOut").toString()));
				cell.setCellStyle(cs);
				// T-P（mg/L）
				cell = row.createCell(45);
				cell.setCellValue(Double.parseDouble(rDailyData.get("zlinOut").toString()));
				cell.setCellStyle(cs);
				// SS（mg/L）
				cell = row.createCell(46);
				cell.setCellValue(Integer.parseInt(rDailyData.get("xfuOut").toString()));
				cell.setCellStyle(cs);
				// PH值
				cell = row.createCell(47);
				cell.setCellValue(Double.parseDouble(rDailyData.get("phOut").toString()));
				cell.setCellStyle(cs);
				// 温度（℃）
				cell = row.createCell(48);
				cell.setCellValue(Double.parseDouble(rDailyData.get("tempOut").toString()));
				cell.setCellStyle(cs);
				// 粪大肠菌群数（个/L）
				cell = row.createCell(49);
				cell.setCellValue(Integer.parseInt(rDailyData.get("fengJun").toString()));
				cell.setCellStyle(cs);
				// 化验室生化池数据
				// 生化池  MLSS（mg/L）
				cell = row.createCell(50);
				cell.setCellValue(Double.parseDouble(rDailyData.get("mlss").toString()));
				cell.setCellStyle(cs);
				// 生化池  MLVSS（mg/L）
				cell = row.createCell(51);
				cell.setCellValue(Integer.parseInt(rDailyData.get("mlvss").toString()));
				cell.setCellStyle(cs);
				// 生化池出水口/曝气末  DO（mg/L）
				cell = row.createCell(52);
				cell.setCellValue(Double.parseDouble(rDailyData.get("testDo").toString()));
				cell.setCellStyle(cs);
				// 生化池  SV30（%）
				cell = row.createCell(53);
				cell.setCellValue(Double.parseDouble(rDailyData.get("sv30").toString()));
				cell.setCellStyle(cs);
				// 污泥含水率（%）
				cell = row.createCell(54);
				cell.setCellValue(Double.parseDouble(rDailyData.get("waterSludge").toString()));
				cell.setCellStyle(cs);
				// 进出口在线数据（中控数值）（日平均值）
				// 进水
				// COD (mg/L)
				cell = row.createCell(55);
				cell.setCellValue(Double.parseDouble(rDailyData.get("CODIn").toString()));
				cell.setCellStyle(cs);
				// NH3-N (mg/L)
				cell = row.createCell(56);
				cell.setCellValue(Double.parseDouble(rDailyData.get("NH3NIn").toString()));
				cell.setCellStyle(cs);
				// TP  (mg/L)
				cell = row.createCell(57);
				cell.setCellValue(Double.parseDouble(rDailyData.get("TPIn").toString()));
				cell.setCellStyle(cs);
				// TN  (mg/L)
				cell = row.createCell(58);
				cell.setCellValue(Double.parseDouble(rDailyData.get("TNIn").toString()));
				cell.setCellStyle(cs);
				// SS  (mg/L)
				cell = row.createCell(59);
				cell.setCellValue(Double.parseDouble(rDailyData.get("SSIn").toString()));
				cell.setCellStyle(cs);
				// 出水
				// COD (mg/L)
				cell = row.createCell(60);
				cell.setCellValue(Double.parseDouble(rDailyData.get("CODOut").toString()));
				cell.setCellStyle(cs);
				// NH3-N (mg/L)
				cell = row.createCell(61);
				cell.setCellValue(Double.parseDouble(rDailyData.get("NH3NOut").toString()));
				cell.setCellStyle(cs);
				// TP  (mg/L)
				cell = row.createCell(62);
				cell.setCellValue(Double.parseDouble(rDailyData.get("TPOut").toString()));
				cell.setCellStyle(cs);
				// TN  (mg/L)
				cell = row.createCell(63);
				cell.setCellValue(Double.parseDouble(rDailyData.get("TNOut").toString()));
				cell.setCellStyle(cs);
				// SS  (mg/L)
				cell = row.createCell(64);
				cell.setCellValue(Double.parseDouble(rDailyData.get("SSOut").toString()));
				cell.setCellStyle(cs);
			}
			
			filename = sheetName + "（" + DateUtils.getDate() + "）.xlsx";
			os = new FileOutputStream(ExcelUtil.getAbsoluteFile(filename));
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.warn(e.toString());
		} finally {
			if (os != null) {
				os.flush();
				os.close();
				wb.close();
			}
		}
		return AjaxResult.success(filename);
	}
	
	// 自来水厂日报表导出
	public AjaxResult waterWorksDailyReport(Map<String, String> map) throws IOException {
		String orgId = map.get("orgId");
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 获取当前用户
		SysUser sysUser = ShiroUtils.getSysUser();
		// 当前组织机构
		SysOrg org = orgService.getById(orgId);
		// 当前水厂信息
		SysFactoryInfo factoryParam = new SysFactoryInfo();
		factoryParam.setOrgId(orgId);
		SysFactoryInfo factory = factoryService.getEntity(factoryParam);
		
		String sheetName = org.getOrgName() + "运行日报表";
		
		OutputStream os = null;
		Workbook wb = null; // 工作薄
		String filename = "";

		try {
			// 模板资源路径
			ClassPathResource cpr = new ClassPathResource("static/template/report/waterWorksDailyReport.xlsx");
			InputStream inputStream = cpr.getInputStream();
			wb = new XSSFWorkbook(inputStream);
			Sheet sheet = wb.getSheetAt(0);

			Row row;
			Cell cell;

			// 设置标题名称
			Row titleRow = sheet.getRow(0);
			Cell titleCell = titleRow.getCell(0);
			titleCell.setCellValue(sheetName);
			
			// 第二行
			// 数据时间段
			Row secondRow = sheet.getRow(1);
			Cell periodCell = secondRow.getCell(0);
			String[] dateSta_array = dateSta.split("-");
			String[] dateEnd_array = dateEnd.split("-");
			periodCell.setCellValue("时间段：" + dateSta_array[0] + "年" + dateSta_array[1] + "月" + dateSta_array[2] + "日-" + dateEnd_array[0] + "年" +dateEnd_array[1] + "月" + dateEnd_array[2] + "日");
			// 设计处理能力
			Cell capacityCell = secondRow.getCell(6);
			capacityCell.setCellValue("设计能力：" + (factory != null && factory.getTon() != null ? factory.getTon() : "0") + "万吨/日");
			// 导出时间
			Cell exportDateCell = secondRow.getCell(35);
			exportDateCell.setCellValue("日期：" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			// 操作人员
			Cell userCell = secondRow.getCell(38);
			userCell.setCellValue("操作者：" + sysUser.getUserName());
			
			// 循环插入数据
			int lastRow = sheet.getLastRowNum(); // 插入数据的数据ROW
			CellStyle cs = ExcelUtil.setSimpleCellStyle(wb); // 获取模板Excel单元格样式
			// 垂直、水平居中
			cs.setAlignment(HorizontalAlignment.CENTER);
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
			
			int index = 0;
			Date sta = DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta);
			// 查数据
			ReportDateInfo r = new ReportDateInfo();
			// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
			Map<String, Object> params = new HashMap<>();
			r.setFactoryId(orgId);
			params.put("dateSta", dateSta);
			params.put("dateEnd", dateEndReal);
			params.put("type", org.getFactoryType());
			r.setParams(params);
			Map<String, Map<String, Object>> rDailyDataMap = reportService.getDailyReport(r); // 查询在时间段内，当前组织结构下，所有数据的累计
			Map<String, Object> rDailyData = null;
			for (int i = 0; i < iDays; i++) {
				index = i + 1;
				row = sheet.createRow(lastRow + index); // 创建新的ROW，用于数据插入
				// 用于查数据
				Object obj = null;
				// Cell赋值开始
				// 序号
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(cs);
				// 日期
				cell = row.createCell(1);
				cell.setCellValue(DateUtils.parseDateToStr("yyyy/MM/dd", DateUtils.addDay(sta, i)));
				cell.setCellStyle(cs);
				// 找数据
				rDailyData = rDailyDataMap.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDay(sta, i)));
				if (rDailyData == null) { // 无数据，全部填0
					for (int n = 2; n <= 44; n ++) {
						cell = row.createCell(n);
						cell.setCellValue(0);
						cell.setCellStyle(cs);
					}
					continue;
				}
				// 超标——_如果超标为1，未超标为 0
				cell = row.createCell(2);
				int overNorm = 0;
				if (rDailyData.get("overNorm") != null && rDailyData.get("overNorm").equals("2")) {
					overNorm = 1;
				}
				cell.setCellValue(overNorm);
				cell.setCellStyle(cs);
				// 产水量（万吨/日）
				cell = row.createCell(3);
				obj = rDailyData.get("todayOut");
				obj = obj == null ? 0 : obj;
				double todayOut = Double.parseDouble(obj.toString()); // 单位吨
				cell.setCellValue(dataFormat(todayOut / 10000)); // 转万吨
				cell.setCellStyle(cs);
				// 工时情况
				// 水厂工作总人次
				cell = row.createCell(4);
				int personNum = 0;
				if (factory != null && factory.getPersonNum() != null)
					personNum = Integer.parseInt(factory.getPersonNum());
				cell.setCellValue(personNum);
				cell.setCellStyle(cs);
				// 吨水人工（小时/吨）= 人工总人次 * 8 / 处理水量
				cell = row.createCell(5);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(personNum * 8 / todayOut));
				cell.setCellStyle(cs);
				// 能耗数据（度）
				// 水厂电量
				cell = row.createCell(6);
				obj = rDailyData.get("todayElec");
				obj = obj == null ? 0 : obj;
				double todayElec = Double.parseDouble(obj.toString());
				cell.setCellValue(todayElec);
				cell.setCellStyle(cs);
				// 泵站/其它电量
				cell = row.createCell(7);
				obj = rDailyData.get("pumpElec");
				obj = obj == null ? 0 : obj;
				double pumpElec = Double.parseDouble(obj.toString());
				cell.setCellValue(pumpElec);
				cell.setCellStyle(cs);
				// 总计电量
				cell = row.createCell(8);
				cell.setCellValue(dataFormat(todayElec + pumpElec));
				cell.setCellStyle(cs);
				// 能耗（度/吨）
				cell = row.createCell(9);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat((todayElec + pumpElec) / todayOut));
				cell.setCellStyle(cs);
				// 药耗数据
				double todayPAMYang = Double.parseDouble(rDailyData.get("todayPAMYang").toString()); // PAM(阳）
				double todayPAMYin = Double.parseDouble(rDailyData.get("todayPAMYin").toString()); // PAM（阴）
				double todayPAC = Double.parseDouble(rDailyData.get("todayPAC").toString()); // PAC
				double todayLime = Double.parseDouble(rDailyData.get("todayLime").toString()); // 石灰
				double todayPhosphorus = Double.parseDouble(rDailyData.get("todayPhosphorus").toString()); // 复核除磷剂
				double todayHCL = Double.parseDouble(rDailyData.get("todayHCL").toString()); // 盐酸（HCL）
				double todaySC = Double.parseDouble(rDailyData.get("todaySC").toString()); // 氯酸钠（NaCL）
				double todayNaCLO = Double.parseDouble(rDailyData.get("todayNaCLO").toString()); // 次氯酸钠（NaCLO）
				double todayGlucose = Double.parseDouble(rDailyData.get("todayGlucose").toString()); // 葡萄糖（C6H12O6）
				double todaySA = Double.parseDouble(rDailyData.get("todaySA").toString()); // 乙酸钠(C2H3O2Na)
				// 综合药耗（KG/吨）= 所有药品用量求和 / 实际处理量
				cell = row.createCell(10);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat((todayPAMYang + todayPAMYin + todayPAC + todayLime + todayPhosphorus + todayHCL + todaySC + todayNaCLO + todayGlucose + todaySA) / todayOut));
				cell.setCellStyle(cs);
				// PAM(阳）
				// 用量（kg）
				cell = row.createCell(11);
				cell.setCellValue(todayPAMYang);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(12);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPAMYang / todayOut));
				cell.setCellStyle(cs);
				// PAM（阴）
				// 用量（kg）
				cell = row.createCell(13);
				cell.setCellValue(todayPAMYin);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(14);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPAMYin / todayOut));
				cell.setCellStyle(cs);
				// PAC
				// 用量（kg）
				cell = row.createCell(15);
				cell.setCellValue(todayPAC);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(16);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPAC / todayOut));
				cell.setCellStyle(cs);
				// 石灰
				// 用量（kg）
				cell = row.createCell(17);
				cell.setCellValue(todayLime);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(18);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayLime / todayOut));
				cell.setCellStyle(cs);
				// 复核除磷剂
				// 用量（kg）
				cell = row.createCell(19);
				cell.setCellValue(todayPhosphorus);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(20);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayPhosphorus / todayOut));
				cell.setCellStyle(cs);
				// 盐酸（HCL）
				// 用量（kg）
				cell = row.createCell(21);
				cell.setCellValue(todayHCL);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(22);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayHCL / todayOut));
				cell.setCellStyle(cs);
				// 氯酸钠（NaCL）
				// 用量（kg）
				cell = row.createCell(23);
				cell.setCellValue(todaySC);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(24);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todaySC / todayOut));
				cell.setCellStyle(cs);
				// 次氯酸钠（NaCLO）
				// 用量（kg）
				cell = row.createCell(25);
				cell.setCellValue(todayNaCLO);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(26);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayNaCLO / todayOut));
				cell.setCellStyle(cs);
				// 葡萄糖（C6H12O6）
				// 用量（kg）
				cell = row.createCell(27);
				cell.setCellValue(todayGlucose);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(28);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todayGlucose / todayOut));
				cell.setCellStyle(cs);
				// 乙酸钠 （C2H3O2Na）
				// 用量（kg）
				cell = row.createCell(29);
				cell.setCellValue(todaySA);
				cell.setCellStyle(cs);
				// 药剂单耗 （KG/吨）
				cell = row.createCell(30);
				cell.setCellValue(todayOut == 0 ? 0 : dataFormat(todaySA / todayOut));
				cell.setCellStyle(cs);
				// 进水化验数据
				// 肉眼可见物
				cell = row.createCell(31);
				cell.setCellValue(Double.parseDouble(rDailyData.get("eyeOut").toString()));
				cell.setCellStyle(cs);
				// 色度
				cell = row.createCell(32);
				cell.setCellValue(Double.parseDouble(rDailyData.get("colourOut").toString()));
				cell.setCellStyle(cs);
				// 嗅和味
				cell = row.createCell(33);
				cell.setCellValue(Double.parseDouble(rDailyData.get("cwOut").toString()));
				cell.setCellStyle(cs);
				// 出水CLO2
				cell = row.createCell(34);
				cell.setCellValue(Double.parseDouble(rDailyData.get("twoOut").toString()));
				cell.setCellStyle(cs);
				// 浊度
				cell = row.createCell(35);
				cell.setCellValue(Double.parseDouble(rDailyData.get("ntuOut").toString()));
				cell.setCellStyle(cs);
				// 温度（℃）
				cell = row.createCell(36);
				cell.setCellValue(Double.parseDouble(rDailyData.get("tempOut").toString()));
				cell.setCellStyle(cs);
				// 总大肠菌群
				cell = row.createCell(37);
				cell.setCellValue(Double.parseDouble(rDailyData.get("zdjOut").toString()));
				cell.setCellStyle(cs);
				// 细菌总数 
				cell = row.createCell(38);
				cell.setCellValue(Double.parseDouble(rDailyData.get("xjzsOut").toString()));
				cell.setCellStyle(cs);
				// 进出口在线数据（中控数值）（日平均值）
				// 进水
				// PH值
				cell = row.createCell(39);
				cell.setCellValue(Double.parseDouble(rDailyData.get("PHIn").toString()));
				cell.setCellStyle(cs);
				// 浊度
				cell = row.createCell(40);
				cell.setCellValue(Double.parseDouble(rDailyData.get("NTUIn").toString()));
				cell.setCellStyle(cs);
				// 出水
				// PH值
				cell = row.createCell(41);
				cell.setCellValue(Double.parseDouble(rDailyData.get("PHOut").toString()));
				cell.setCellStyle(cs);
				// 浊度
				cell = row.createCell(42);
				cell.setCellValue(Double.parseDouble(rDailyData.get("NTUOut").toString()));
				cell.setCellStyle(cs);
				// 后置加氯/出水CL02
				cell = row.createCell(43);
				cell.setCellValue(Double.parseDouble(rDailyData.get("COL2Out").toString()));
				cell.setCellStyle(cs);
				// 细菌总数
				cell = row.createCell(44);
				cell.setCellValue(Double.parseDouble(rDailyData.get("germOut").toString()));
				cell.setCellStyle(cs);
			}
			
			filename = sheetName + "（" + DateUtils.getDate() + "）.xlsx";
			os = new FileOutputStream(ExcelUtil.getAbsoluteFile(filename));
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.warn(e.toString());
		} finally {
			if (os != null) {
				os.flush();
				os.close();
				wb.close();
			}
		}
		return AjaxResult.success(filename);
	}
	
	// 格式换单元格，目的：
	// 1、保留 2 位小数
	// 2、去除小数点后面多余的 0
	// 3、去除Excel单元格的小三角
	public double dataFormat(Object obj) {
		obj = obj == null ? 0 : obj;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		String numStr = nf.format(Double.parseDouble(obj.toString()));
		numStr = numStr.indexOf(",") != -1 ? numStr.replace(",", "") : numStr;
		return Double.parseDouble(numStr);
	}
}