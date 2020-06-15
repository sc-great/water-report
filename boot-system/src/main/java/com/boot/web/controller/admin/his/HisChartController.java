package com.boot.web.controller.admin.his;

import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.DateUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.report.domain.MudInfo;
import com.boot.report.domain.TodayElectricityInfo;
import com.boot.report.domain.TodayMedicineInfo;
import com.boot.report.domain.TodayWaterYieldInfo;
import com.boot.report.service.IMudInfoService;
import com.boot.report.service.ITodayElectricityInfoService;
import com.boot.report.service.ITodayMedicineInfoService;
import com.boot.report.service.ITodayWaterYieldInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysOrgService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史数据Controller
 *
 * @author LM
 * @date 2020-05-18
 */
@Controller
@RequestMapping("/admin/his")
public class HisChartController extends BaseController {
	private String prefix = "admin/his";
	
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private ITodayMedicineInfoService todayMedicineInfoService;
	@Autowired
	private ITodayWaterYieldInfoService todayWaterYieldInfoService;
	@Autowired
	private ITodayElectricityInfoService todayElectricityInfoService;
	@Autowired
	private IMudInfoService mudInfoService;

	@RequiresPermissions("report:his:chart")
	@RequestMapping("/chart")
	public String daily(ModelMap model) {
		SysOrg org = ShiroUtils.getSysUser().getSysOrgs().get(0);
		model.put("orgId", org.getOrgId());
		model.put("orgType", org.getOrgType());
		model.put("orgName", org.getOrgName());
		return prefix + "/chart";
	}

	// 药量当日统计
	@RequestMapping("/medicineAreaDataToday")
	@ResponseBody
	public AjaxResult medicineAreaDataToday(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					todayMedicineInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						todayMedicineInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					todayMedicineInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i ++) {
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 药量累计统计
	@RequestMapping("/medicineAreaDataTotal")
	@ResponseBody
	public AjaxResult medicineAreaDataTotal(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					todayMedicineInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						todayMedicineInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					todayMedicineInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 水量当天统计
	@RequestMapping("/waterYieldAreaDataToday")
	@ResponseBody
	public AjaxResult waterYieldAreaDataToday(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			TodayWaterYieldInfo waterYieldInfo = new TodayWaterYieldInfo();
			waterYieldInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					waterYieldInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					waterYieldInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						waterYieldInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						waterYieldInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					waterYieldInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				todayIn = 0;
				todayOut = 0;
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 水量累计统计
	@RequestMapping("/waterYieldAreaDataTotal")
	@ResponseBody
	public AjaxResult waterYieldAreaDataTotal(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			TodayWaterYieldInfo waterYieldInfo = new TodayWaterYieldInfo();
			waterYieldInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					waterYieldInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					waterYieldInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						waterYieldInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						waterYieldInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					waterYieldInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				totalIn = 0;
				totalOut = 0;
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 电量当天统计
	@RequestMapping("/electricQuantityAreaDataToday")
	@ResponseBody
	public AjaxResult electricQuantityAreaDataToday(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					todayElectricityInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						todayElectricityInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					todayElectricityInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				todayElectricity = 0;
				todayPumpEletricity = 0;
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 电量累计统计
	@RequestMapping("/electricQuantityAreaDataTotal")
	@ResponseBody
	public AjaxResult electricQuantityAreaDataTotal(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					todayElectricityInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						todayElectricityInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					todayElectricityInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				totalElectricity = 0;
				totalPumpEletricity = 0;
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 污泥当日统计分析
	@RequestMapping("/sludgeChartToday")
	@ResponseBody
	public AjaxResult sludgeChartToday(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			MudInfo mudInfo = new MudInfo();
			mudInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					mudInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					mudInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						mudInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						mudInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					mudInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				todayMud = 0;
				dryMud = 0;
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
	}

	// 污泥累计统计分析
	@RequestMapping("/sludgeChartDataTotal")
	@ResponseBody
	public AjaxResult sludgeChartDataTotal(String dateSta, String dateEnd, String orgId) {
		try {
			SysUser sysUser = ShiroUtils.getSysUser();
			SysOrg sysOrg = null;
			
			MudInfo mudInfo = new MudInfo();
			mudInfo.setEffectIcon("1");
			if (orgId == null || "".equals(orgId)) {
				sysOrg = sysUser.getSysOrgs().get(0);
				if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
					mudInfo.setFactoryId(sysOrg.getOrgId());
				} else { // 集团领导  区域
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysUser.getAreaStr());
					mudInfo.setParams(params);
				}
			} else {
				sysOrg = orgService.getById(orgId);
				if (sysOrg != null) { // 所选为组织机构
					if ("3".equals(sysOrg.getOrgType())) { // 水厂
						mudInfo.setFactoryId(sysOrg.getOrgId());
					} else {
						mudInfo.setAreaId(sysOrg.getAreaId());
					}
				} else { // 所选为区域
					mudInfo.setAreaId(orgId);
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
			
			// 获取两个时间相差的天数
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateEnd), DateUtils.dateTime(DateUtils.YYYY_MM_DD, dateSta));
			for (int i = 0; i <= iDays; i++) {
				totalMud = 0;
				dryMudTotal = 0;
				new_date = dateUpAndDown(dateSta, i);
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
			
			return AjaxResult.success(mapResult);
		} catch (Exception e) {
			return AjaxResult.error();
		}
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