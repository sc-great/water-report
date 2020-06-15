package com.boot.web.controller.app;

import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.*;
import com.boot.report.service.*;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/chart")
public class AppChartController extends APPBaseController {

	@Autowired
	private ITodayWaterYieldInfoService todayWaterYieldInfoService;
	@Autowired
	private ITodayElectricityInfoService todayElectricityInfoService;
	@Autowired
	private ITodayMedicineInfoService todayMedicineInfoService;
	@Autowired
	private IMudInfoService mudInfoService;

	// 药量当日统计
	@RequestMapping("/medicineAreaDataToday")
	@ResponseBody
	public AjaxResult medicineAreaDataToday(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				todayMedicineInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					todayMedicineInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_todayPac = new ArrayList<>(); // 累计PAC
			List<String> dataList_todayPamYin = new ArrayList<>(); // 累计PAM阴离子
			List<String> dataList_todayPamYang = new ArrayList<>(); // 累计PAM阳离子
			List<String> dataList_todayPhosphorus = new ArrayList<>(); // 累计复核除磷剂
			List<String> dataList_todayNaclo = new ArrayList<>(); // 累计NaCLO
			List<String> dataList_todayLime = new ArrayList<>(); // 累计石灰
			List<String> dataList_todayGlucose = new ArrayList<>(); // 累计葡萄糖
			List<String> dataList_todaySc = new ArrayList<>(); // 累计氯酸钠
			List<String> dataList_todaySa = new ArrayList<>(); // 累计乙酸钠
			List<String> dataList_todayHCL = new ArrayList<>(); // 累计氯化氢
			String new_date = "";
			Map<String, Object> model = null;
			double todayPac = 0, // 累计PAC
					todayPamYin = 0, // 累计PAM阴离子
					todayPamYang = 0, // 累计PAM阳离子
					todayPhosphorus = 0, // 累计复核除磷剂
					todayNaclo = 0, // 累计NaCLO
					todayLime = 0, // 累计石灰
					todayGlucose = 0, // 累计葡萄糖
					todaySc = 0, // 累计氯酸钠
					todaySa = 0, // 累计乙酸钠
					todayHCL = 0; // 累计氯化氢
			for (int i = 0; i < 7; i++) {
				new_date = dateUpAndDown(date, i - 6);
				todayMedicineInfo.setFillDate(new_date);
				model = todayMedicineInfoService.getTodayForChart(todayMedicineInfo);
				// 数据转换
				todayPac = Double.parseDouble(model.get("todayPac").toString());
				todayPamYin = Double.parseDouble(model.get("todayPamYin").toString());
				todayPamYang = Double.parseDouble(model.get("todayPamYang").toString());
				todayPhosphorus = Double.parseDouble(model.get("todayPhosphorus").toString());
				todayNaclo = Double.parseDouble(model.get("todayNaclo").toString());
				todayLime = Double.parseDouble(model.get("todayLime").toString());
				todayGlucose = Double.parseDouble(model.get("todayGlucose").toString());
				todaySc = Double.parseDouble(model.get("todaySc").toString());
				todaySa = Double.parseDouble(model.get("todaySa").toString());
				todayHCL = Double.parseDouble(model.get("todayHCL").toString());
				// 数据打包
				dataList_todayPac.add(String.format("%.2f", todayPac));
				dataList_todayPamYin.add(String.format("%.2f", todayPamYin));
				dataList_todayPamYang.add(String.format("%.2f", todayPamYang));
				dataList_todayPhosphorus.add(String.format("%.2f", todayPhosphorus));
				dataList_todayNaclo.add(String.format("%.2f", todayNaclo));
				dataList_todayLime.add(String.format("%.2f", todayLime));
				dataList_todayGlucose.add(String.format("%.2f", todayGlucose));
				dataList_todaySc.add(String.format("%.2f", todaySc));
				dataList_todaySa.add(String.format("%.2f", todaySa));
				dataList_todayHCL.add(String.format("%.2f", todayHCL));

				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("todayPac", dataList_todayPac);
			dataMap.put("todayPamYin", dataList_todayPamYin);
			dataMap.put("todayPamYang", dataList_todayPamYang);
			dataMap.put("todayPhosphorus", dataList_todayPhosphorus);
			dataMap.put("todayNaclo", dataList_todayNaclo);
			dataMap.put("todayLime", dataList_todayLime);
			dataMap.put("todayGlucose", dataList_todayGlucose);
			dataMap.put("todaySc", dataList_todaySc);
			dataMap.put("todaySa", dataList_todaySa);
			dataMap.put("todayHCL", dataList_todayHCL);

			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 药量累计统计
	@RequestMapping("/medicineAreaDataTotal")
	@ResponseBody
	public AjaxResult medicineAreaDataTotal(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				todayMedicineInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					todayMedicineInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_totalPac = new ArrayList<>(); // 累计PAC
			List<String> dataList_totalPamYin = new ArrayList<>(); // 累计PAM阴离子
			List<String> dataList_totalPamYang = new ArrayList<>(); // 累计PAM阳离子
			List<String> dataList_totalPhosphorus = new ArrayList<>(); // 累计复核除磷剂
			List<String> dataList_totalNaclo = new ArrayList<>(); // 累计NaCLO
			List<String> dataList_totalLime = new ArrayList<>(); // 累计石灰
			List<String> dataList_totalGlucose = new ArrayList<>(); // 累计葡萄糖
			List<String> dataList_totalSc = new ArrayList<>(); // 累计氯酸钠
			List<String> dataList_totalSa = new ArrayList<>(); // 累计乙酸钠
			List<String> dataList_totalHCL = new ArrayList<>(); // 累计氯化氢
			String new_date = "";
			Map<String, Object> model = null;
			double totalPac = 0, // 累计PAC
					totalPamYin = 0, // 累计PAM阴离子
					totalPamYang = 0, // 累计PAM阳离子
					totalPhosphorus = 0, // 累计复核除磷剂
					totalNaclo = 0, // 累计NaCLO
					totalLime = 0, // 累计石灰
					totalGlucose = 0, // 累计葡萄糖
					totalSc = 0, // 累计氯酸钠
					totalSa = 0, // 累计乙酸钠
					totalHCL = 0; // 累计氯化氢
			for (int i = 0; i < 7; i++) {
				new_date = dateUpAndDown(date, i - 6);
				todayMedicineInfo.setFillDate(new_date);
				model = todayMedicineInfoService.getSumForChart(todayMedicineInfo);
				// 数据转换
				totalPac = Double.parseDouble(model.get("totalPac").toString());
				totalPamYin = Double.parseDouble(model.get("totalPamYin").toString());
				totalPamYang = Double.parseDouble(model.get("totalPamYang").toString());
				totalPhosphorus = Double.parseDouble(model.get("totalPhosphorus").toString());
				totalNaclo = Double.parseDouble(model.get("totalNaclo").toString());
				totalLime = Double.parseDouble(model.get("totalLime").toString());
				totalGlucose = Double.parseDouble(model.get("totalGlucose").toString());
				totalSc = Double.parseDouble(model.get("totalSc").toString());
				totalSa = Double.parseDouble(model.get("totalSa").toString());
				totalHCL = Double.parseDouble(model.get("totalHCL").toString());
				// 数据打包
				dataList_totalPac.add(String.format("%.2f", totalPac));
				dataList_totalPamYin.add(String.format("%.2f", totalPamYin));
				dataList_totalPamYang.add(String.format("%.2f", totalPamYang));
				dataList_totalPhosphorus.add(String.format("%.2f", totalPhosphorus));
				dataList_totalNaclo.add(String.format("%.2f", totalNaclo));
				dataList_totalLime.add(String.format("%.2f", totalLime));
				dataList_totalGlucose.add(String.format("%.2f", totalGlucose));
				dataList_totalSc.add(String.format("%.2f", totalSc));
				dataList_totalSa.add(String.format("%.2f", totalSa));
				dataList_totalHCL.add(String.format("%.2f", totalHCL));

				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("totalPac", dataList_totalPac);
			dataMap.put("totalPamYin", dataList_totalPamYin);
			dataMap.put("totalPamYang", dataList_totalPamYang);
			dataMap.put("totalPhosphorus", dataList_totalPhosphorus);
			dataMap.put("totalNaclo", dataList_totalNaclo);
			dataMap.put("totalLime", dataList_totalLime);
			dataMap.put("totalGlucose", dataList_totalGlucose);
			dataMap.put("totalSc", dataList_totalSc);
			dataMap.put("totalSa", dataList_totalSa);
			dataMap.put("totalHCL", dataList_totalHCL);

			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 水量当天统计
	@RequestMapping("/waterYieldAreaDataToday")
	@ResponseBody
	public AjaxResult waterYieldAreaDataToday(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			TodayWaterYieldInfo waterYieldInfo = new TodayWaterYieldInfo();
			waterYieldInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				waterYieldInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				waterYieldInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					waterYieldInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_todayIn = new ArrayList<>();
			List<String> dataList_todayOut = new ArrayList<>();
			String new_date = "";
			Map<String, Object> model = null;
			double todayIn = 0, todayOut = 0;
			for (int i = 0; i < 7; i++) {
				todayIn = 0;
				todayOut = 0;
				new_date = dateUpAndDown(date, i - 6);
				waterYieldInfo.setFillDate(new_date);
				model = todayWaterYieldInfoService.getTodayForChart(waterYieldInfo);
				// 数据转换
				todayIn = Double.parseDouble(model.get("todayIn").toString());
				todayOut = Double.parseDouble(model.get("todayOut").toString());
				// 数据打包
				dataList_todayIn.add(String.format("%.2f", todayIn));
				dataList_todayOut.add(String.format("%.2f", todayOut));
				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("todayIn", dataList_todayIn);
			dataMap.put("todayOut", dataList_todayOut);
			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 水量累计统计
	@RequestMapping("/waterYieldAreaDataTotal")
	@ResponseBody
	public AjaxResult waterYieldAreaDataTotal(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			TodayWaterYieldInfo waterYieldInfo = new TodayWaterYieldInfo();
			waterYieldInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				waterYieldInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				waterYieldInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					waterYieldInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_totalIn = new ArrayList<>();
			List<String> dataList_totalOut = new ArrayList<>();
			String new_date = "";
			Map<String, Object> model = null;
			double totalIn = 0, totalOut = 0;
			for (int i = 0; i < 7; i++) {
				totalIn = 0;
				totalOut = 0;
				new_date = dateUpAndDown(date, i - 6);
				waterYieldInfo.setFillDate(new_date);
				model = todayWaterYieldInfoService.getSumForChart(waterYieldInfo);
				// 数据转换
				totalIn = Double.parseDouble(model.get("totalIn").toString());
				totalOut = Double.parseDouble(model.get("totalOut").toString());
				// 数据打包
				dataList_totalIn.add(String.format("%.2f", totalIn));
				dataList_totalOut.add(String.format("%.2f", totalOut));
				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("totalIn", dataList_totalIn);
			dataMap.put("totalOut", dataList_totalOut);
			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 电量当天统计
	@RequestMapping("/electricQuantityAreaDataToday")
	@ResponseBody
	public AjaxResult electricQuantityAreaDataToday(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				todayElectricityInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					todayElectricityInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_todayElectricity = new ArrayList<>();
			List<String> dataList_todayPumpEletricity = new ArrayList<>();
			String new_date = "";
			Map<String, Object> model = null;
			double todayElectricity = 0, todayPumpEletricity = 0;
			for (int i = 0; i < 7; i++) {
				todayElectricity = 0;
				todayPumpEletricity = 0;
				new_date = dateUpAndDown(date, i - 6);
				todayElectricityInfo.setFillDate(new_date);
				model = todayElectricityInfoService.getTodayForChart(todayElectricityInfo);
				// 数据转换
				todayElectricity = Double.parseDouble(model.get("todayElectricity").toString());
				todayPumpEletricity = Double.parseDouble(model.get("todayPumpEletricity").toString());
				// 数据打包
				dataList_todayElectricity.add(String.format("%.2f", todayElectricity));
				dataList_todayPumpEletricity.add(String.format("%.2f", todayPumpEletricity));
				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("todayElectricity", dataList_todayElectricity);
			dataMap.put("todayPumpEletricity", dataList_todayPumpEletricity);
			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 电量累计统计
	@RequestMapping("/electricQuantityAreaDataTotal")
	@ResponseBody
	public AjaxResult electricQuantityAreaDataTotal(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				todayElectricityInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					todayElectricityInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_totalElectricity = new ArrayList<>();
			List<String> dataList_totalPumpEletricity = new ArrayList<>();
			String new_date = "";
			Map<String, Object> model = null;
			double totalElectricity = 0, totalPumpEletricity = 0;
			for (int i = 0; i < 7; i++) {
				totalElectricity = 0;
				totalPumpEletricity = 0;
				new_date = dateUpAndDown(date, i - 6);
				todayElectricityInfo.setFillDate(new_date);
				model = todayElectricityInfoService.getSumForChart(todayElectricityInfo);
				// 数据转换
				totalElectricity = Double.parseDouble(model.get("totalElectricity").toString());
				totalPumpEletricity = Double.parseDouble(model.get("totalPumpEletricity").toString());
				// 数据打包
				dataList_totalElectricity.add(String.format("%.2f", totalElectricity));
				dataList_totalPumpEletricity.add(String.format("%.2f", totalPumpEletricity));
				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("totalElectricity", dataList_totalElectricity);
			dataMap.put("totalPumpEletricity", dataList_totalPumpEletricity);
			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 污泥当日统计分析
	@RequestMapping("/sludgeChartToday")
	@ResponseBody
	public AjaxResult sludgeChartToday(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			MudInfo mudInfo = new MudInfo();
			mudInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				mudInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				mudInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					mudInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_todayMud = new ArrayList<>();
			List<String> dataList_dryMud = new ArrayList<>();
			String new_date = "";
			Map<String, Object> sumMud = null;
			double todayMud = 0, dryMud = 0;
			for (int i = 0; i < 7; i++) {
				todayMud = 0;
				dryMud = 0;
				new_date = dateUpAndDown(date, i - 6);
				mudInfo.setFillDate(new_date);
				sumMud = mudInfoService.getTodayForChart(mudInfo);
				// 数据转换
				todayMud = Double.parseDouble(sumMud.get("todayMud").toString());
				dryMud = Double.parseDouble(sumMud.get("dryMud").toString());
				// 数据打包
				dataList_todayMud.add(String.format("%.2f", todayMud));
				dataList_dryMud.add(String.format("%.2f", dryMud));
				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("todayMud", dataList_todayMud);
			dataMap.put("dryMud", dataList_dryMud);
			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 污泥累计统计分析
	@RequestMapping("/sludgeChartDataTotal")
	@ResponseBody
	public AjaxResult sludgeChartDataTotal(String date, String areaIds) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String area_ids = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(area_ids)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			MudInfo mudInfo = new MudInfo();
			mudInfo.setEffectIcon("1");
			if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
				mudInfo.setFactoryId(sysOrg.getOrgId());
			} else if ("2".equals(sysOrg.getOrgType())) { // 区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", area_ids);
				mudInfo.setParams(params);
			} else { // 集团领导
				if (areaIds != null && !"".equals(areaIds)) { // 点击查区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", areaIds);
					mudInfo.setParams(params);
				}
			}

			Map<String, Object> mapResult = new HashMap<>();
			Map<String, Object> dataMap = new HashMap<>();
			List<String> dateList = new ArrayList<>();
			List<String> dataList_totalMud = new ArrayList<>();
			List<String> dataList_dryMudTotal = new ArrayList<>();
			String new_date = "";
			Map<String, Object> sumMud = null;
			double totalMud = 0, dryMudTotal = 0;
			for (int i = 0; i < 7; i++) {
				totalMud = 0;
				dryMudTotal = 0;
				new_date = dateUpAndDown(date, i - 6);
				mudInfo.setFillDate(new_date);
				sumMud = mudInfoService.getSumForChart(mudInfo);
				// 数据转换
				totalMud = Double.parseDouble(sumMud.get("totalMud").toString());
				dryMudTotal = Double.parseDouble(sumMud.get("dryMudTotal").toString());
				// 数据打包
				dataList_totalMud.add(String.format("%.2f", totalMud));
				dataList_dryMudTotal.add(String.format("%.2f", dryMudTotal));
				dateList.add(new_date.substring(new_date.indexOf("-") + 1));
			}
			dataMap.put("totalMud", dataList_totalMud);
			dataMap.put("dryMudTotal", dataList_dryMudTotal);
			mapResult.put("date", dateList);
			mapResult.put("data", dataMap);
			ajaxResult = AjaxResult.success(mapResult);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	// 时间加减n天的方法
	public static String dateUpAndDown(String dateStr, int index) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, index);
			dateStr = sdf.format(calendar.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dateStr;
	}
}
