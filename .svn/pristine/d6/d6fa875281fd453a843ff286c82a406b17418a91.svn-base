package com.boot.web.controller.admin.his;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.materialControl.domain.CostInfo;
import com.boot.materialControl.service.CostInfoService;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.service.ITodayInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysOrgService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
import java.util.Arrays;
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
public class CompareController extends BaseController {
	private String prefix = "admin/his";
	
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ITodayInfoService reportService;
	@Autowired
	private CostInfoService costInfoService;

	@RequiresPermissions("report:his:compare")
	@RequestMapping("/compare")
	public String list(ModelMap model) {
		model.put("orgId", ShiroUtils.getSysUser().getSysOrgs().get(0).getOrgId());
		return prefix + "/compare";
	}
	
	@RequestMapping("/loadObjecTree")
	@ResponseBody
	public JSONArray loadObjecTree() {
		JSONArray back = new JSONArray();
		JSONArray sons = new JSONArray();
		JSONObject obj = null;
		
		obj = (JSONObject) JSONObject.toJSON(new SearchObject("0200", "0000", "wscll", "污水处理量"));
		sons = new JSONArray();
		sons.add(JSONObject.toJSON(new SearchObject("0201", "0200", "ljcll", "累计处理量"))); // 万吨
		sons.add(JSONObject.toJSON(new SearchObject("0202", "0200", "sjclnl", "设计处理能力"))); // 万吨/日
		sons.add(JSONObject.toJSON(new SearchObject("0203", "0200", "fhl", "负荷率"))); // 累计量/累计设计处理能力，取值为百分数，精确小数点后2位
		sons.add(JSONObject.toJSON(new SearchObject("0204", "0200", "pjclnl", "平均处理能力"))); // 万吨/日 = 累计处理量/区域水厂个数
		obj.put("obj", sons);
		back.add(obj);
		
		obj = (JSONObject) JSONObject.toJSON(new SearchObject("0300", "0000", "sfqk", "收费情况"));
		sons = new JSONArray();
		sons.add(JSONObject.toJSON(new SearchObject("0301", "0300", "ljyssf", "累计应收水费"))); // 区域/项目公司下属水厂求和：区域/项目公司下属水厂内各水厂现执行单价x区域/项目公司下属水厂内各水厂实际处理量
		sons.add(JSONObject.toJSON(new SearchObject("0302", "0300", "ljssqsf", "累计实收取水费"))); // 来至于区域内各水厂实际到账收入求和
		sons.add(JSONObject.toJSON(new SearchObject("0303", "0300", "hkl", "回款率"))); // 累计实收/累计应收，求百分比，保留小数后2位
		obj.put("obj", sons);
		back.add(obj);
		
		obj = (JSONObject) JSONObject.toJSON(new SearchObject("0400", "0000", "ydl", "用电量"));
		sons = new JSONArray();
		sons.add(JSONObject.toJSON(new SearchObject("0401", "0400", "ljzdl", "累计总电量"))); // 区域/项目公司下属水厂内所有水厂，时间段内电量求和
		sons.add(JSONObject.toJSON(new SearchObject("0402", "0400", "dsdh", "吨水电耗"))); // 累计总电量/累计处理量
		sons.add(JSONObject.toJSON(new SearchObject("0403", "0400", "pjdsdh", "平均吨水电耗"))); // 区域/项目公司下属水厂内各水厂在该时间内吨水单耗求和/区域/项目公司下属水厂水厂个数
		obj.put("obj", sons);
		back.add(obj);
		
		obj = (JSONObject) JSONObject.toJSON(new SearchObject("0500", "0000", "cnl", "产泥量"));
		sons = new JSONArray();
		sons.add(JSONObject.toJSON(new SearchObject("0501", "0500", "ljcnl", "累计产泥量"))); // 区域/项目公司下属水厂/项目公司下属水厂内各厂产泥量求和
		sons.add(JSONObject.toJSON(new SearchObject("0502", "0500", "ljjgnzl", "累计绝干泥总量")));
		sons.add(JSONObject.toJSON(new SearchObject("0503", "0500", "wdsjgwnl", "万吨水绝干污泥量"))); // 吨/万吨 = 累计绝干泥总量/累计实际处理总量
		obj.put("obj", sons);
		back.add(obj);
		
		obj = (JSONObject) JSONObject.toJSON(new SearchObject("0600", "0000", "sclyj", "水处理药剂"));
		sons = new JSONArray();
		sons.add(JSONObject.toJSON(new SearchObject("0601", "0600", "pamylj", "PAM（阳）累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0602", "0600", "pamydh", "PAM（阳）单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0603", "0600", "pamnlj", "PAM（阴）累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0604", "0600", "pamndh", "PAM（阴）单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0605", "0600", "paclj", "PAC累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0606", "0600", "pacdh", "PAC单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0607", "0600", "shlj", "石灰累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0608", "0600", "shdh", "石灰单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0609", "0600", "fhcljlj", "复核除磷剂累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0610", "0600", "fhcljdh", "复核除磷剂单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0611", "0600", "hcllj", "盐酸累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0612", "0600", "hcldh", "盐酸单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0613", "0600", "nacllj", "氯酸钠累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0614", "0600", "nacldh", "氯酸钠单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0615", "0600", "naclolj", "次氯酸钠累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0616", "0600", "naclodh", "次氯酸钠单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0617", "0600", "pttlj", "葡萄糖累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0618", "0600", "pttdh", "葡萄糖单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0619", "0600", "ysnlj", "乙酸钠累计")));
		sons.add(JSONObject.toJSON(new SearchObject("0620", "0600", "ysndh", "乙酸钠单耗")));
		sons.add(JSONObject.toJSON(new SearchObject("0621", "0600", "zhyh", "综合药耗")));
		obj.put("obj", sons);
		back.add(obj);
		
		return back;
	}
	
	@RequestMapping("/searchCompare")
	@ResponseBody
	public TableDataInfo searchCompare(@RequestParam Map<String, String> map) throws IOException {
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		String orgIds = map.get("orgIds");
		String objIds = map.get("objIds");
		if (dateSta == null || "".equals(dateSta) || dateEnd == null || "".equals(dateEnd) 
				|| orgIds == null || "".equals(orgIds) || objIds == null || "".equals(objIds))
			return getDataTable(new ArrayList<>());
		// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
		Map<String, Object> params = new HashMap<>();
		params.put("dateSta", dateSta);
		params.put("dateEnd", dateEndReal);
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 该excel表格，以所选组织机构为循环单位，所选组织机构可能为区域
		String orgType = map.get("orgType");

		// 用于返回的集合
		List<Map<String, Object>> reList = new ArrayList<>();
		Map<String, Object> reMap = null;

		String[] orgIdArray = orgIds.split(",");

		for (int i = 0; i < orgIdArray.length; i++) {
			if ("".equals(orgIdArray[i]))
				continue;
			reMap = new HashMap<>();
			String orgId = orgIdArray[i];
			String cellName = ""; // 公司、区域、工厂名称
			int orgCount = 0; // 工厂数量——用于平均
			SysOrg org = null;
			Area area = null;
			if (orgType.equals("2.5")) { // 查区域
				area = areaService.getEntityById(orgId);
				cellName = area.getAreaName();
			} else { // 组织机构
				org = orgService.getById(orgId); // 当前对象
				cellName = org.getOrgName();
			}

			SysOrg op = new SysOrg();
			op.setOrgType("3");
			if (orgType.equals("1")) { // 查集团
				orgCount = orgService.getCount(op);
				params.put("admin", true);
			}
			if (orgType.equals("2")) { // 查公司（即11个地区）
				op.setParentId(orgId);
				orgCount = orgService.getCount(op);
				params.put("parentId", orgId);
			}
			if (orgType.equals("2.5")) { // 查区域（即公司）
				op.setAreaId(orgId);
				orgCount = orgService.getCount(op);
				params.put("areaId", orgId);
			}
			if (orgType.equals("3")) { // 查水厂（即公司）--必定是一条数据
				orgCount = 1;
				params.put("orgId", orgId);
			}

			// 查数据
			ReportDateInfo r = new ReportDateInfo();
			r.setParams(params);
			Map<String, Object> rSumData = reportService.getSumData(r); // 查询在时间段内，当前组织结构下，所有数据的累计
			// map赋值开始
			// 区域/项目 公司名称
			reMap.put("orgName", cellName);

			// 如果查出来的数据为空，就全部写 0
			if (rSumData == null) {
				reList.add(reMap);
				continue;
			}

			// 需要计算的值，先取出来
			double totalOutTon = Double.parseDouble((rSumData.get("totalOut") == null ? 0 : rSumData.get("totalOut")).toString()); // 累计处理量（单位是吨）
			double ton = Double.parseDouble((rSumData.get("ton") == null ? 0 : rSumData.get("ton")).toString()); // 设计处理能力（万吨/日）
			double should = Double.parseDouble((rSumData.get("account") == null ? 0 : rSumData.get("account")).toString()); // 累计应收水费（万元）
			double reality = 0; // 实收水费（万元）
			if (orgType.equals("1") || orgType.equals("2")) { // 11个地区，每个地区的水费累计——水厂没有财务，没有实收水费
				CostInfo c = new CostInfo();
				c.setEffectIcon("1");
				params.put("areaIds", org.getAreaId());
				c.setParams(params);
				reality = Double.parseDouble(costInfoService.getSum(c).get("currentTotal").toString());
			} else if (orgType.equals("2.5")) {
				CostInfo c = new CostInfo();
				c.setEffectIcon("1");
				c.setAreaId(orgId);
				reality = Double.parseDouble(costInfoService.getSum(c).get("currentTotal").toString());
			}
			double totalElec = Double.parseDouble((rSumData.get("totalElec") == null ? 0 : rSumData.get("totalElec")).toString()); // 累计总电量（度）
			double totalDryMud = Double.parseDouble((rSumData.get("totalDryMud") == null ? 0 : rSumData.get("totalDryMud")).toString()); // 累计绝干泥总量（吨）
			double totalPAMYang = Double.parseDouble((rSumData.get("totalPAMYang") == null ? 0 : rSumData.get("totalPAMYang")).toString()); // PAM（阳）累计（KG）
			double totalPAMYin = Double.parseDouble((rSumData.get("totalPAMYin") == null ? 0 : rSumData.get("totalPAMYin")).toString()); // PAM（阴）累计（KG）
			double totalPAC = Double.parseDouble((rSumData.get("totalPAC") == null ? 0 : rSumData.get("totalPAC")).toString()); // PAC累计（KG）
			double totalLime = Double.parseDouble((rSumData.get("totalLime") == null ? 0 : rSumData.get("totalLime")).toString()); // 石灰累计（KG）
			double totalPhosphorus = Double.parseDouble((rSumData.get("totalPhosphorus") == null ? 0 : rSumData.get("totalPhosphorus")).toString()); // 复核除磷剂累计（KG）
			double totalHCL = Double.parseDouble((rSumData.get("totalHCL") == null ? 0 : rSumData.get("totalHCL")).toString()); // 盐酸累计（KG）
			double totalSC = Double.parseDouble((rSumData.get("totalSC") == null ? 0 : rSumData.get("totalSC")).toString()); // 氯酸钠累计（KG）
			double totalNaCLO = Double.parseDouble((rSumData.get("totalNaCLO") == null ? 0 : rSumData.get("totalNaCLO")).toString()); // 次氯酸钠累计（KG）
			double totalGlucose = Double.parseDouble((rSumData.get("totalGlucose") == null ? 0 : rSumData.get("totalGlucose")).toString()); // 葡萄糖累计（KG）
			double totalSA = Double.parseDouble((rSumData.get("totalSA") == null ? 0 : rSumData.get("totalSA")).toString()); // 乙酸钠累计（KG）

			reMap.put("totalOut", dataFormat(totalOutTon / 10000)); // 转换为万吨
			reMap.put("ton", ton); // 设计处理能力（万吨/日）
			reMap.put("loadRate", ton == 0 ? 0 : dataFormat(totalOutTon / 10000 / ton * 100)); // 负荷率（%）
			reMap.put("avgCapacity", dataFormat(totalOutTon / 10000 / orgCount / iDays)); // 平均处理能力（万吨/日）
			
			reMap.put("should", should); // 累计应收水费（万元）
			reMap.put("reality", reality); // 累计实收取水费（万元）
			reMap.put("backRate", should == 0 ? 0 : dataFormat(reality / should * 100)); // 回款率（%）
			
			reMap.put("totalElec", totalElec); // 累计总电量（度）
			reMap.put("tonElec", totalOutTon == 0 ? 0 : dataFormat(totalElec / totalOutTon)); // 吨水电耗（度/吨）
			reMap.put("avgTonElec", totalOutTon == 0 || orgCount == 0 ? 0 : dataFormat(totalElec / totalOutTon / orgCount)); // 平均吨水电耗（度/吨）

			reMap.put("totalMud", dataFormat(rSumData.get("totalMud"))); // 累计产泥量（吨）
			reMap.put("totalDryMud", totalDryMud); // 累计绝干泥总量（吨）
			reMap.put("avgTotalDryMud", totalOutTon == 0 ? 0 : dataFormat(totalDryMud / totalOutTon * 10000)); // 万吨水绝干污泥量（吨/万吨）

			reMap.put("totalPAMYang", totalPAMYang); // PAM（阳）累计（KG）
			reMap.put("tonPAMYang", totalOutTon == 0 ? 0 : dataFormat(totalPAMYang / totalOutTon)); // PAM（阳）单耗（KG/吨）
			reMap.put("totalPAMYin", totalPAMYin); // PAM（阴）累计（KG）
			reMap.put("tonPAMYin", totalOutTon == 0 ? 0 : dataFormat(totalPAMYin / totalOutTon)); // PAM（阴）单耗（KG/吨）

			reMap.put("totalPAC", totalPAC); // PAC累计（KG）
			reMap.put("tonPAC", totalOutTon == 0 ? 0 : dataFormat(totalPAC / totalOutTon)); // PAC单耗（KG/吨）
			reMap.put("totalLime", totalLime); // 石灰累计（KG）
			reMap.put("tonLime", totalOutTon == 0 ? 0 : dataFormat(totalLime / totalOutTon)); // 石灰单耗（KG/吨）

			reMap.put("totalPhosphorus", totalPhosphorus); // 复核除磷剂累计（KG）
			reMap.put("tonPhosphorus", totalOutTon == 0 ? 0 : dataFormat(totalPhosphorus / totalOutTon)); // 复核除磷剂单耗（KG/吨）
			reMap.put("totalHCL", totalHCL); // 盐酸累计（KG）
			reMap.put("tonHCL", totalOutTon == 0 ? 0 : dataFormat(totalHCL / totalOutTon)); // 盐酸单耗（KG/吨）

			reMap.put("totalSC", totalSC); // 氯酸钠累计（KG）
			reMap.put("tonSC", totalOutTon == 0 ? 0 : dataFormat(totalSC / totalOutTon)); // 氯酸钠单耗（KG/吨）
			reMap.put("totalNaCLO", totalNaCLO); // 次氯酸钠累计（KG）
			reMap.put("tonNaCLO", totalOutTon == 0 ? 0 : dataFormat(totalNaCLO / totalOutTon)); // 次氯酸钠单耗（KG/吨）

			reMap.put("totalGlucose", totalGlucose); // 葡萄糖累计（KG）
			reMap.put("tonGlucose", totalOutTon == 0 ? 0 : dataFormat(totalGlucose / totalOutTon)); // 葡萄糖单耗（KG/吨）
			reMap.put("totalSA", totalSA); // 乙酸钠累计（KG）
			reMap.put("tonSA", totalOutTon == 0 ? 0 : dataFormat(totalSA / totalOutTon)); // 乙酸钠单耗（KG/吨）

			reMap.put("composite",
					totalOutTon == 0 ? 0
							: dataFormat((totalPAMYang + totalPAMYin + totalPAC + totalLime + totalPhosphorus + totalHCL
									+ totalSC + totalNaCLO + totalGlucose + totalSA) / totalOutTon)); // 综合药耗（KG/吨）
			reList.add(reMap);
		}
		return getDataTable(reList);
	}

	@RequestMapping("/exportCompare")
	@ResponseBody
	public AjaxResult exportCompare(@RequestParam Map<String, String> map) throws IOException {
		String dateSta = map.get("dateSta");
		String dateEnd = map.get("dateEnd");
		// 实际查询的结束日期，因为查询的日期是没有时分秒的，即为0点，所以实际的结束时间应该为第二天的0点
		String dateEndReal = DateUtils.dateTime(DateUtils.addDay(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), 1));
		String orgIds = map.get("orgIds");
		String objIds = map.get("objIds");
		// 把起始日期和实际查询的结束日期，重新放会map，用于查询的条件
		Map<String, Object> params = new HashMap<>();
		params.put("dateSta", dateSta);
		params.put("dateEnd", dateEndReal);
		// 根据选择的比较项目，判断需要查询哪些表 =====================暂时不考虑，直接查所有============================================================
		// 获取当前用户
		SysUser sysUser = ShiroUtils.getSysUser();
		// 获取两个时间实际相差的天数
		int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEndReal), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
		// 该excel表格，以所选组织机构为循环单位，所选组织机构可能为区域
		String orgType = map.get("orgType");
		String sheetName = "比较分析表";
		
		OutputStream os = null;
		Workbook wb = null; // 工作薄
		String filename = "";

		try {
			// 模板资源路径
			ClassPathResource cpr = new ClassPathResource("static/template/report/compare.xlsx");
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
			Cell exportDateCell = secondRow.getCell(5);
			exportDateCell.setCellValue("日期：" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			// 操作人员
			Cell userCell = secondRow.getCell(8);
			userCell.setCellValue("操作者：" + sysUser.getUserName());
			
			// 获取模板Excel单元格样式
			CellStyle cs = ExcelUtil.setSimpleCellStyle(wb);
			// 垂直、水平居中
			cs.setAlignment(HorizontalAlignment.CENTER);
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 插入表头
			String[] objIdArray = objIds.split(",");
			// 排序
			Arrays.sort(objIdArray);
			// 第四行
			Row fourthRow = sheet.getRow(3);
			CellStyle fourthCS = fourthRow.getCell(0).getCellStyle();
			int m = 2;
			int o2 = 0, o3 = 0, o4 = 0, o5 = 0, o6 = 0;
			for (String item : objIdArray) {
				if ("".equals(item))
					continue;
				String value = "";
				// "0200", "0000", "wscll", "污水处理量"
				if ("0201".equals(item)) {
					value = "累计处理量（万吨）";
					o2 ++;
				}
				if ("0202".equals(item)) {
					value = "设计处理能力（万吨/日）";
					o2 ++;
				}
				if ("0203".equals(item)) {
					value = "负荷率（%）";
					o2 ++;
				}
				if ("0204".equals(item)) {
					value = "平均处理能力（万吨/日）";
					o2 ++;
				}
				// "0300", "0000", "sfqk", "收费情况"
				if ("0301".equals(item)) {
					value = "累计应收水费（万元）";
					o3 ++;
				}
				if ("0302".equals(item)) {
					value = "累计实收取水费（万元）";
					o3 ++;
				}
				if ("0303".equals(item)) {
					value = "回款率（%）";
					o3 ++;
				}
				// "0400", "0000", "ydl", "用电量"
				if ("0401".equals(item)) {
					value = "累计总电量（度）";
					o4 ++;
				}
				if ("0402".equals(item)) {
					value = "吨水电耗（度/吨）";
					o4 ++;
				}
				if ("0403".equals(item)) {
					value = "平均吨水电耗（度/吨）";
					o4 ++;
				}
				// "0500", "0000", "cnl", "产泥量"
				if ("0501".equals(item)) {
					value = "累计产泥量（吨）";
					o5 ++;
				}
				if ("0502".equals(item)) {
					value = "累计绝干泥总量（吨）";
					o5 ++;
				}
				if ("0503".equals(item)) {
					value = "万吨水绝干污泥量（吨/万吨）";
					o5 ++;
				}
				// "0600", "0000", "sclyj", "水处理药剂"
				if ("0601".equals(item)) {
					value = "PAM（阳）累计（KG）";
					o6 ++;
				}
				if ("0602".equals(item)) {
					value = "PAM（阳）单耗（KG/吨）";
					o6 ++;
				}
				if ("0603".equals(item)) {
					value = "PAM（阴）累计（KG）";
					o6 ++;
				}
				if ("0604".equals(item)) {
					value = "PAM（阴）单耗（KG/吨）";
					o6 ++;
				}
				if ("0605".equals(item)) {
					value = "PAC累计（KG）";
					o6 ++;
				}
				if ("0606".equals(item)) {
					value = "PAC单耗（KG/吨）";
					o6 ++;
				}
				if ("0607".equals(item)) {
					value = "石灰累计（KG）";
					o6 ++;
				}
				if ("0608".equals(item)) {
					value = "石灰单耗（KG/吨）";
					o6 ++;
				}
				if ("0609".equals(item)) {
					value = "复核除磷剂累计（KG）";
					o6 ++;
				}
				if ("0610".equals(item)) {
					value = "复核除磷剂单耗（KG/吨）";
					o6 ++;
				}
				if ("0611".equals(item)) {
					value = "盐酸累计（KG）";
					o6 ++;
				}
				if ("0612".equals(item)) {
					value = "盐酸单耗（KG/吨）";
					o6 ++;
				}
				if ("0613".equals(item)) {
					value = "氯酸钠累计（KG）";
					o6 ++;
				}
				if ("0614".equals(item)) {
					value = "氯酸钠单耗（KG/吨）";
					o6 ++;
				}
				if ("0615".equals(item)) {
					value = "次氯酸钠累计（KG）";
					o6 ++;
				}
				if ("0616".equals(item)) {
					value = "次氯酸钠单耗（KG/吨）";
					o6 ++;
				}
				if ("0617".equals(item)) {
					value = "葡萄糖累计（KG）";
					o6 ++;
				}
				if ("0618".equals(item)) {
					value = "葡萄糖单耗（KG/吨）";
					o6 ++;
				}
				if ("0619".equals(item)) {
					value = "乙酸钠累计（KG）";
					o6 ++;
				}
				if ("0620".equals(item)) {
					value = "乙酸钠单耗（KG/吨）";
					o6 ++;
				}
				if ("0621".equals(item)) {
					value = "综合药耗（KG/吨）";
					o6 ++;
				}
				cell = fourthRow.createCell(m);
				cell.setCellValue(value);
				fourthCS.setWrapText(true); // 自动换行
			    cell.setCellStyle(fourthCS);
			    cell.getCellStyle().setBorderTop(BorderStyle.THIN);//上边框
				m ++;
			}
			
			// 第三行
			int no = 2;
			Row thirdRow = sheet.getRow(2);
			if (o2 != 0) {
				// 合并单元格
				CellRangeAddress cra = new CellRangeAddress(2, 2, (short) no, (short) no + o2 - 1);
				RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
				sheet.addMergedRegion(cra);
				cell = thirdRow.createCell(no);
				cell.setCellValue("污水处理量");
				cell.setCellStyle(fourthCS);
				no += o2;
			}
			if (o3 != 0) {
				CellRangeAddress cra = new CellRangeAddress(2, 2, (short) no, (short) no + o3 - 1);
				RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
				sheet.addMergedRegion(cra);
				cell = thirdRow.createCell(no);
				cell.setCellValue("收费情况");
				cell.setCellStyle(fourthCS);
				no += o3;
			}
			if (o4 != 0) {
				CellRangeAddress cra = new CellRangeAddress(2, 2, (short) no, (short) no + o4 - 1);
				RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
				sheet.addMergedRegion(cra);
				cell = thirdRow.createCell(no);
				cell.setCellValue("用电量");
				cell.setCellStyle(fourthCS);
				no += o4;
			}
			if (o5 != 0) {
				CellRangeAddress cra = new CellRangeAddress(2, 2, (short) no, (short) no + o5 - 1);
				RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
				sheet.addMergedRegion(cra);
				cell = thirdRow.createCell(no);
				cell.setCellValue("产泥量");
				cell.setCellStyle(fourthCS);
				no += o5;
			}
			if (o6 != 0) {
				CellRangeAddress cra = new CellRangeAddress(2, 2, (short) no, (short) no + o6 - 1);
				RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
				sheet.addMergedRegion(cra);
				cell = thirdRow.createCell(no);
				cell.setCellValue("水处理药剂");
				cell.setCellStyle(fourthCS);
				no += o6;
			}
			
			// 循环插入数据
			int lastRow = sheet.getLastRowNum(); // 插入数据的数据ROW
			String[] orgIdArray = orgIds.split(",");
			
			for (int i = 0; i < orgIdArray.length; i++) {
				if ("".equals(orgIdArray[i]))
					continue;
				int index = i + 1; // 序号
				String orgId = orgIdArray[i];
				String cellName = ""; // 公司、区域、工厂名称
				int orgCount = 0; // 工厂数量——用于平均
				SysOrg org = null;
				Area area = null;
				if (orgType.equals("2.5")) { // 查区域
					area = areaService.getEntityById(orgId);
					cellName = area.getAreaName();
				} else { // 组织机构
					org = orgService.getById(orgId); // 当前对象
					cellName = org.getOrgName();
				}
				
				SysOrg op = new SysOrg();
				op.setOrgType("3");
				if (orgType.equals("1")) { // 查集团
					orgCount = orgService.getCount(op);
					params.put("admin", true);
				}
				if (orgType.equals("2")) { // 查公司（即11个地区）
					op.setParentId(orgId);
					orgCount = orgService.getCount(op);
					params.put("parentId", orgId);
				}
				if (orgType.equals("2.5")) { // 查区域（即公司）
					op.setAreaId(orgId);
					orgCount = orgService.getCount(op);
					params.put("areaId", orgId);
				}
				if (orgType.equals("3")) { // 查水厂（即公司）--必定是一条数据
					orgCount = 1;
					params.put("orgId", orgId);
				}
				
				row = sheet.createRow(lastRow + index); // 创建新的ROW，用于数据插入
				// 查数据
				ReportDateInfo r = new ReportDateInfo();
				r.setParams(params);
				Map<String, Object> rSumData = reportService.getSumData(r); // 查询在时间段内，当前组织结构下，所有数据的累计
				// Cell赋值开始
				// 序号
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(cs);
				// 区域/项目 公司名称
				cell = row.createCell(1);
				cell.setCellValue(cellName);
				cell.setCellStyle(cs);
				
				// 如果查出来的数据为空，就全部写 0
				if (rSumData == null) {
					for (int c = 2; c < m; c ++) {
						cell = row.createCell(c);
						cell.setCellValue(0);
						cell.setCellStyle(cs);
					}
					continue;
				}
				
				// 需要计算的值，先取出来
				double totalOutTon = Double.parseDouble((rSumData.get("totalOut") == null ? 0 : rSumData.get("totalOut")).toString()); // 累计处理量（单位是吨）
				double ton = Double.parseDouble((rSumData.get("ton") == null ? 0 : rSumData.get("ton")).toString()); // 设计处理能力（万吨/日）
				double should = Double.parseDouble((rSumData.get("account") == null ? 0 : rSumData.get("account")).toString()); // 累计应收水费（万元）
				double reality = 0; // 实收水费（万元）
				if (orgType.equals("1") || orgType.equals("2")) { // 11个地区，每个地区的水费累计——水厂没有财务，没有实收水费
					CostInfo c = new CostInfo();
					c.setEffectIcon("1");
					params.put("areaIds", org.getAreaId());
					c.setParams(params);
					reality = Double.parseDouble(costInfoService.getSum(c).get("currentTotal").toString());
				} else if (orgType.equals("2.5")) {
					CostInfo c = new CostInfo();
					c.setEffectIcon("1");
					c.setAreaId(orgId);
					reality = Double.parseDouble(costInfoService.getSum(c).get("currentTotal").toString());
				}
				double totalElec = Double.parseDouble((rSumData.get("totalElec") == null ? 0 : rSumData.get("totalElec")).toString()); // 累计总电量（度）
				double totalDryMud = Double.parseDouble((rSumData.get("totalDryMud") == null ? 0 : rSumData.get("totalDryMud")).toString()); // 累计绝干泥总量（吨）
				double totalPAMYang = Double.parseDouble((rSumData.get("totalPAMYang") == null ? 0 : rSumData.get("totalPAMYang")).toString()); // PAM（阳）累计（KG）
				double totalPAMYin = Double.parseDouble((rSumData.get("totalPAMYin") == null ? 0 : rSumData.get("totalPAMYin")).toString()); // PAM（阴）累计（KG）
				double totalPAC = Double.parseDouble((rSumData.get("totalPAC") == null ? 0 : rSumData.get("totalPAC")).toString()); // PAC累计（KG）
				double totalLime = Double.parseDouble((rSumData.get("totalLime") == null ? 0 : rSumData.get("totalLime")).toString()); // 石灰累计（KG）
				double totalPhosphorus = Double.parseDouble((rSumData.get("totalPhosphorus") == null ? 0 : rSumData.get("totalPhosphorus")).toString()); // 复核除磷剂累计（KG）
				double totalHCL = Double.parseDouble((rSumData.get("totalHCL") == null ? 0 : rSumData.get("totalHCL")).toString()); // 盐酸累计（KG）
				double totalSC = Double.parseDouble((rSumData.get("totalSC") == null ? 0 : rSumData.get("totalSC")).toString()); // 氯酸钠累计（KG）
				double totalNaCLO = Double.parseDouble((rSumData.get("totalNaCLO") == null ? 0 : rSumData.get("totalNaCLO")).toString()); // 次氯酸钠累计（KG）
				double totalGlucose = Double.parseDouble((rSumData.get("totalGlucose") == null ? 0 : rSumData.get("totalGlucose")).toString()); // 葡萄糖累计（KG）
				double totalSA = Double.parseDouble((rSumData.get("totalSA") == null ? 0 : rSumData.get("totalSA")).toString()); // 乙酸钠累计（KG）
				
				int n = 2;
				for (String item : objIdArray) {
					if ("".equals(item))
						continue;
					
					cell = row.createCell(n);
					cell.setCellStyle(cs);
					n ++;

					// "0200", "0000", "wscll", "污水处理量"
					if ("0201".equals(item)) { // 累计处理量（万吨）
						cell.setCellValue(dataFormat(totalOutTon / 10000)); // 转换为万吨
						continue;
					}
					if ("0202".equals(item)) { // 设计处理能力（万吨/日）
						cell.setCellValue(ton);
						continue;
					}
					if ("0203".equals(item)) { // 负荷率（%）
						cell.setCellValue(ton == 0 ? 0 : dataFormat(totalOutTon / 10000 / ton * 100));
						continue;
					}
					if ("0204".equals(item)) { // 平均处理能力（万吨/日）
						cell.setCellValue(dataFormat(totalOutTon / 10000 / orgCount / iDays));
						continue;
					}
					// "0300", "0000", "sfqk", "收费情况"
					if ("0301".equals(item)) { // 累计应收水费（万元）
						cell.setCellValue(should);
						continue;
					}
					if ("0302".equals(item)) { // 累计实收取水费（万元）
						cell.setCellValue(reality);
						continue;
					}
					if ("0303".equals(item)) { // 回款率（%）
						cell.setCellValue(should == 0 ? 0 : dataFormat(reality / should * 100));
						continue;
					}
					// "0400", "0000", "ydl", "用电量"
					if ("0401".equals(item)) { // 累计总电量（度）
						cell.setCellValue(totalElec);
						continue;
					}
					if ("0402".equals(item)) { // 吨水电耗（度/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalElec / totalOutTon));
						continue;
					}
					if ("0403".equals(item)) { // 平均吨水电耗（度/吨）
						cell.setCellValue(totalOutTon == 0 || orgCount == 0 ? 0 : dataFormat(totalElec / totalOutTon / orgCount));
						continue;
					}
					// "0500", "0000", "cnl", "产泥量"
					if ("0501".equals(item)) { // 累计产泥量（吨）
						cell.setCellValue(dataFormat(rSumData.get("totalMud")));
						continue;
					}
					if ("0502".equals(item)) { // 累计绝干泥总量（吨）
						cell.setCellValue(totalDryMud);
						continue;
					}
					if ("0503".equals(item)) { // 万吨水绝干污泥量（吨/万吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalDryMud / totalOutTon * 10000));
						continue;
					}
					// "0600", "0000", "sclyj", "水处理药剂"
					if ("0601".equals(item)) { // PAM（阳）累计（KG）
						cell.setCellValue(totalPAMYang);
						continue;
					}
					if ("0602".equals(item)) { // PAM（阳）单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPAMYang / totalOutTon));
						continue;
					}
					if ("0603".equals(item)) { // PAM（阴）累计（KG）
						cell.setCellValue(totalPAMYin);
						continue;
					}
					if ("0604".equals(item)) { // PAM（阴）单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPAMYin / totalOutTon));
						continue;
					}
					if ("0605".equals(item)) { // PAC累计（KG）
						cell.setCellValue(totalPAC);
						continue;
					}
					if ("0606".equals(item)) { // PAC单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPAC / totalOutTon));
						continue;
					}
					if ("0607".equals(item)) { // 石灰累计（KG）
						cell.setCellValue(totalLime);
						continue;
					}
					if ("0608".equals(item)) { // 石灰单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalLime / totalOutTon));
						continue;
					}
					if ("0609".equals(item)) { // 复核除磷剂累计（KG）
						cell.setCellValue(totalPhosphorus);
						continue;
					}
					if ("0610".equals(item)) { // 复核除磷剂单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalPhosphorus / totalOutTon));
						continue;
					}
					if ("0611".equals(item)) { // 盐酸累计（KG）
						cell.setCellValue(totalHCL);
						continue;
					}
					if ("0612".equals(item)) { // 盐酸单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalHCL / totalOutTon));
						continue;
					}
					if ("0613".equals(item)) { // 氯酸钠累计（KG）
						cell.setCellValue(totalSC);
						continue;
					}
					if ("0614".equals(item)) { // 氯酸钠单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalSC / totalOutTon));
						continue;
					}
					if ("0615".equals(item)) { // 次氯酸钠累计（KG）
						cell.setCellValue(totalNaCLO);
						continue;
					}
					if ("0616".equals(item)) { // 次氯酸钠单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalNaCLO / totalOutTon));
						continue;
					}
					if ("0617".equals(item)) { // 葡萄糖累计（KG）
						cell.setCellValue(totalGlucose);
						continue;
					}
					if ("0618".equals(item)) { // 葡萄糖单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalGlucose / totalOutTon));
						continue;
					}
					if ("0619".equals(item)) { // 乙酸钠累计（KG）
						cell.setCellValue(totalSA);
						continue;
					}
					if ("0620".equals(item)) { // 乙酸钠单耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat(totalSA / totalOutTon));
						continue;
					}
					if ("0621".equals(item)) { // 综合药耗（KG/吨）
						cell.setCellValue(totalOutTon == 0 ? 0 : dataFormat((totalPAMYang + totalPAMYin + totalPAC + totalLime + totalPhosphorus + totalHCL + totalSC + totalNaCLO + totalGlucose + totalSA) / totalOutTon));
						continue;
					}
				}
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