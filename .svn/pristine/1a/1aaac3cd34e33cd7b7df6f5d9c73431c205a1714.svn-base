package com.boot.web.controller.app;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.InitsUtils;
import com.boot.common.utils.StringUtils;
import com.boot.materialControl.domain.CostInfo;
import com.boot.materialControl.service.CostInfoService;
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.report.domain.TodayElectricityInfo;
import com.boot.report.domain.TodayMedicineInfo;
import com.boot.report.domain.TodayWaterYieldInfo;
import com.boot.report.domain.UserHealthInfo;
import com.boot.report.service.IBadWaterQualityInfoService;
import com.boot.report.service.IGoodWaterHealthInfoService;
import com.boot.report.service.ITodayElectricityInfoService;
import com.boot.report.service.ITodayMedicineInfoService;
import com.boot.report.service.ITodayWaterYieldInfoService;
import com.boot.report.service.IUserHealthInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.domain.SysFiles;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysDictDataService;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.system.service.ISysFilesService;
import com.boot.system.service.ISysOrgService;

@Controller
@RequestMapping("/app")
public class APPHomeController extends APPBaseController {
	private String prefix = "app/pages";
	@Autowired
	private ISysFilesService sysFilesService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private IUserHealthInfoService userHealthInfoService;
	@Autowired
	private ISysDictDataService dictDataService;
	@Autowired
	private ITodayElectricityInfoService todayElectricityInfoService;
	@Autowired
	private ITodayWaterYieldInfoService todayWaterYieldInfoService;
	@Autowired
	private ITodayMedicineInfoService todayMedicineInfoService;
	@Autowired
	private CostInfoService costInfoService;
	@Autowired
	private ISysFactoryInfoService sysFactoryInfoService;
	@Autowired
	private IBadWaterQualityInfoService badWaterQualityInfoService;
	@Autowired
	private IGoodWaterHealthInfoService goodWaterHealthInfoService;

	/**
	 * 首页（原首页）
	 */
	@GetMapping("/home")
	public String home(ModelMap mMap) {
		return "";
	}

	/**
	 * 领导级首页 homeAdmin
	 */
	@GetMapping("/homeAdmin")
	public String homeAdmin(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {

			// 判断角色，如果是水厂领导，则可以使用“工厂信息”栏目
			String role_code = sysUser.getSysRoles().get(0).getRoleCode();

			if ("7".equals(role_code)) { // 是水厂领导
				mMap.put("currEableFlag", "1");
			}

			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			mMap.put("orgType", sysOrg.getOrgType());
			UserHealthInfo userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setFillDate(DateUtils.getDate());
			userHealthInfo.setWorkType("1");
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				userHealthInfo.getParams().put("areaIds", areaIds);

				// 收费情况
				CostInfo costInfo = new CostInfo();
				costInfo.setFillDate(DateUtils.getDate());
				costInfo.setEffectIcon("1");
				costInfo.getParams().put("areaIds", areaIds);

				// 当年实际收费
				Double costSpeedA1 = costInfoService.getSumByAppA1(costInfo);
			
				mMap.put("costspeedA1", InitsUtils.costInits(costSpeedA1));
				// 当年目标款
				Double costSpeedA2 = costInfoService.getSumByAppA2(costInfo);
				
				mMap.put("costSpeedA2", InitsUtils.costInits(costSpeedA2));
				// 收费进展
				Double costSpeed = costInfoService.getSumByApp(costInfo);
				mMap.put("costspeed", costSpeed);
			}
			// 现场办公人数
			int sceneWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			mMap.put("sceneWorkerCount", sceneWorkerCount);
			userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setFillDate(DateUtils.getDate());
			userHealthInfo.setWorkType("2");
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				userHealthInfo.getParams().put("areaIds", areaIds);
			}
			// 远程办公人数
			int onlineWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			mMap.put("onlineWorkerCount", onlineWorkerCount);
			// 到岗总人数
			int workerCount = sceneWorkerCount + onlineWorkerCount;
			mMap.put("workerCount", workerCount);

			SysFactoryInfo sysFactoryInfo_params = new SysFactoryInfo();
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				sysFactoryInfo_params.setOrgId(sysOrg.getOrgId());
			} else {
				sysFactoryInfo_params.getParams().put("areaIds", areaIds);
			}
			// 水厂注册人数(区域是累加)
			int workerTotal = sysFactoryInfoService.getCountByAppFactoryPersonNum(sysFactoryInfo_params);
			mMap.put("workerTotal", workerTotal);

			// 运营能力:设计处理能力累计、实际处理累计、达标率
			if ("3".equals(sysOrg.getOrgType())) { // 水厂领导
				SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
				sysFactoryInfo.setOrgId(sysOrg.getOrgId());
				SysFactoryInfo findEntity = sysFactoryInfoService.getEntity(sysFactoryInfo);
				if (findEntity != null) {
					Long sys_factory_ton = findEntity.getTon();
					mMap.put("factoryTon", InitsUtils.waterInits(sys_factory_ton.doubleValue()));

				}
				// 实际处理累计
				TodayWaterYieldInfo todayWaterYieldInfoPara = new TodayWaterYieldInfo();
				todayWaterYieldInfoPara.setFactoryId(sysOrg.getOrgId());
				todayWaterYieldInfoPara.setEffectIcon("1");
				List<TodayWaterYieldInfo> todayWaterYieldList = todayWaterYieldInfoService
						.getList(todayWaterYieldInfoPara);
				if (todayWaterYieldList.size() > 0) {
					Double waterYieldOutSum = todayWaterYieldList.get(0).getTotalOut() * 10000;
					mMap.put("waterYieldOutSum", InitsUtils.waterInits(waterYieldOutSum));
				}

				String reachRate = ""; // 达标率(%)
				if ("1".equals(sysOrg.getFactoryType())) { // 污水
					BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
					badWaterQualityInfo.setEffectIcon("1");
					badWaterQualityInfo.setFactoryId(sysOrg.getOrgId());
					int badWaterNum = badWaterQualityInfoService.getCount(badWaterQualityInfo); // 总量
					if (badWaterNum > 0) {
						badWaterQualityInfo.setPassFlag("1");
						int badWaterPassNum = badWaterQualityInfoService.getCount(badWaterQualityInfo); // 合格量
						DecimalFormat df = new DecimalFormat("0.00");
						mMap.put("reachRate", df.format(((float) badWaterPassNum / badWaterNum) * 100)); // 达标率(%)
					} else {
						mMap.put("reachRate", reachRate);
					}
				} else if ("2".equals(sysOrg.getFactoryType())) { // 供水厂或自来水厂
					GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
					goodWaterHealthInfo.setEffectIcon("1");
					goodWaterHealthInfo.setFactoryId(sysOrg.getOrgId());
					int goodWaterNum = goodWaterHealthInfoService.getCount(goodWaterHealthInfo); // 总量
					if (goodWaterNum > 0) {
						goodWaterHealthInfo.setPassFlag("1");
						int goodWaterPassNum = goodWaterHealthInfoService.getCount(goodWaterHealthInfo); // 合格量
						DecimalFormat df = new DecimalFormat("0.00");
						mMap.put("reachRate", df.format(((float) goodWaterPassNum / goodWaterNum) * 100)); // 达标率(%)
					} else {
						mMap.put("reachRate", reachRate);
					}
				} else {
					mMap.put("reachRate", reachRate);
				}
			} else { // 区域或集团或系统管理级
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", areaIds);
				SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
				sysFactoryInfo.setParams(params);
				Long areaTonSum = sysFactoryInfoService.getCountByApp(sysFactoryInfo);
				mMap.put("factoryTon", InitsUtils.waterInits(Double.parseDouble(String.valueOf(areaTonSum)))); // 设计处理能力累计

				// 实际处理累计
				TodayWaterYieldInfo todayWaterYieldInfoPara = new TodayWaterYieldInfo();
				todayWaterYieldInfoPara.setParams(params);
				todayWaterYieldInfoPara.setEffectIcon("1");
				Double waterYieldOutSum = todayWaterYieldInfoService.getWaterYieldOutSumByApp(todayWaterYieldInfoPara);
				mMap.put("waterYieldOutSum", InitsUtils.waterInits(waterYieldOutSum));

				String reachRate = ""; // 达标率(%)
				// 污水
				BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
				badWaterQualityInfo.setEffectIcon("1");
				badWaterQualityInfo.setParams(params);
				int badWaterNum = badWaterQualityInfoService.getCount(badWaterQualityInfo); // 总量
				badWaterQualityInfo.setPassFlag("1");
				int badWaterPassNum = badWaterQualityInfoService.getCount(badWaterQualityInfo); // 合格量
				// 供水厂或自来水厂
				GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
				goodWaterHealthInfo.setEffectIcon("1");
				goodWaterHealthInfo.setParams(params);
				int goodWaterNum = goodWaterHealthInfoService.getCount(goodWaterHealthInfo); // 总量
				goodWaterHealthInfo.setPassFlag("1");
				int goodWaterPassNum = goodWaterHealthInfoService.getCount(goodWaterHealthInfo); // 合格量
				int badAndGoodWaterNum = badWaterNum + goodWaterNum;
				int badAndGoodWaterPassNum = badWaterPassNum + goodWaterPassNum;

				if (badAndGoodWaterNum > 0) {
					DecimalFormat df = new DecimalFormat("0.00");
					mMap.put("reachRate", df.format(((float) badAndGoodWaterPassNum / badAndGoodWaterNum) * 100)); // 达标率(%)
				} else {
					mMap.put("reachRate", reachRate);
				}
			}

			// 水量数据
			TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
			todayWaterYieldInfo.setEffectIcon("1");
			todayWaterYieldInfo.setFillDate(DateUtils.getDate());
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				todayWaterYieldInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				todayWaterYieldInfo.getParams().put("areaIds", areaIds);
			}

			Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getSum(todayWaterYieldInfo); // 当日水量
			Object totalIn_obj_1 = sumWaterYield.get("totalTodayIn");
			Object totalIn_obj_2 = sumWaterYield.get("totalTodayOut");
			Double temp_a = new Double(0);
			Double temp_b = new Double(0);
			if (totalIn_obj_1 != null) {
				temp_a = Double.parseDouble(sumWaterYield.get("totalTodayIn").toString());
				// 不足万时以吨为单位，上万不足亿时以万吨为单位，上亿以亿吨为单位(当日进水累计电量单位)
				sumWaterYield.put("totalTodayIn", InitsUtils.waterInits(temp_a));
			}
			if (totalIn_obj_2 != null) {
				temp_b = Double.parseDouble(sumWaterYield.get("totalTodayOut").toString());
				// 不足万时以吨为单位，上万不足亿时以万吨为单位，上亿以亿吨为单位(当日出水累计电量单位)
				sumWaterYield.put("totalTodayOut", InitsUtils.waterInits(temp_b));
			}
			Map<String, Object> sumByApp = todayWaterYieldInfoService.getSumByApp(todayWaterYieldInfo); // 当日前累计的进水总量
																										// 和出水总量
			Double temp_aa = new Double(0);
			Double temp_bb = new Double(0);

			Object totalIn_obj = sumByApp.get("totalTodayIn");
			if (totalIn_obj != null) {
				temp_aa = Double.parseDouble(totalIn_obj.toString()) + temp_a;
				sumWaterYield.put("totalIn", InitsUtils.waterInits(temp_aa));
			}
			Object totalOut_obj = sumByApp.get("totalTodayOut");
			if (totalOut_obj != null) {
				temp_bb = Double.parseDouble(totalOut_obj.toString()) + temp_b;
				sumWaterYield.put("totalOut", InitsUtils.waterInits(temp_bb));
			}

			mMap.put("sumWaterYield", sumWaterYield);

			// 电耗数据
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			todayElectricityInfo.setFillDate(DateUtils.getDate());
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				todayElectricityInfo.getParams().put("areaIds", areaIds);
			}

			Map<String, Object> sumElectricity = todayElectricityInfoService.getSum(todayElectricityInfo); // 当日电量

			Double tempElectricity1 = new Double(0);
			Double tempElectricity2 = new Double(0);
			if (sumElectricity.get("totalTodayElectricity") != null) {
				tempElectricity1 = Double.parseDouble(sumElectricity.get("totalTodayElectricity").toString());
				// 万度以下单位为度，超万为万度，超亿为亿度
				sumElectricity.put("totalTodayElectricity", InitsUtils.electricityInits(tempElectricity1));
			}
			if (sumElectricity.get("totalPumpTodayEletricity") != null) {
				tempElectricity2 = Double.parseDouble(sumElectricity.get("totalPumpTodayEletricity").toString());
				sumElectricity.put("totalPumpTodayEletricity", InitsUtils.electricityInits(tempElectricity2));
			}

			Map<String, Object> sumByAppElectricity = todayElectricityInfoService.getSumByApp(todayElectricityInfo); // 当日及前累计的电量
			Double eb1 = new Double(0);
			Double eb2 = new Double(0);

			if (sumByAppElectricity.get("totalBeforeTodayElectricity") != null) {
				eb1 = (Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString()))
						+ tempElectricity1;
				sumElectricity.put("totalTotalElectricity", InitsUtils.electricityInits(eb1));
			}
			if (sumByAppElectricity.get("totalPumpBeforeTodayEletricity") != null) {
				eb2 = Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString())
						+ tempElectricity2;
				sumElectricity.put("totalPumpTotalEletricity", InitsUtils.electricityInits(eb2));
			}
			mMap.put("sumElectricity", sumElectricity);

			// 药耗数据
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			todayMedicineInfo.setFillDate(DateUtils.getDate());
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				todayMedicineInfo.getParams().put("areaIds", areaIds);
			}
			Map<String, Object> sumMedicine = todayMedicineInfoService.getSum(todayMedicineInfo); // 当日药量

			Double tempMedicine1 = new Double(0);
			Double tempMedicine2 = new Double(0);
			Double tempMedicine3 = new Double(0);
			Double tempMedicine4 = new Double(0);
			Double tempMedicine5 = new Double(0);
			Double tempMedicine6 = new Double(0);
			Double tempMedicine7 = new Double(0);
			Double tempMedicine8 = new Double(0);
			Double tempMedicine9 = new Double(0);
			Double tempMedicine10 = new Double(0);

			if (sumMedicine.get("sumTodayPAC") != null) {
				tempMedicine1 = Double.parseDouble(sumMedicine.get("sumTodayPAC").toString());
				sumMedicine.put("sumTodayPAC", InitsUtils.medicineInits(tempMedicine1));
			}
			if (sumMedicine.get("sumTodayPAMYin") != null) {
				tempMedicine2 = Double.parseDouble(sumMedicine.get("sumTodayPAMYin").toString());
				sumMedicine.put("sumTodayPAMYin", InitsUtils.medicineInits(tempMedicine2));
			}
			if (sumMedicine.get("sumTodayPAMYang") != null) {
				tempMedicine3 = Double.parseDouble(sumMedicine.get("sumTodayPAMYang").toString());
				sumMedicine.put("sumTodayPAMYang", InitsUtils.medicineInits(tempMedicine3));
			}
			if (sumMedicine.get("sumTodayPhosphorus") != null) {
				tempMedicine4 = Double.parseDouble(sumMedicine.get("sumTodayPhosphorus").toString());
				sumMedicine.put("sumTodayPhosphorus", InitsUtils.medicineInits(tempMedicine4));
			}
			if (sumMedicine.get("sumTodayNaCLO") != null) {
				tempMedicine5 = Double.parseDouble(sumMedicine.get("sumTodayNaCLO").toString());
				sumMedicine.put("sumTodayNaCLO", InitsUtils.medicineInits(tempMedicine5));
			}
			if (sumMedicine.get("sumTodayLime") != null) {
				tempMedicine6 = Double.parseDouble(sumMedicine.get("sumTodayLime").toString());
				sumMedicine.put("sumTodayLime", InitsUtils.medicineInits(tempMedicine6));
			}
			if (sumMedicine.get("sumTodayGlucose") != null) {
				tempMedicine7 = Double.parseDouble(sumMedicine.get("sumTodayGlucose").toString());
				sumMedicine.put("sumTodayGlucose", InitsUtils.medicineInits(tempMedicine7));
			}
			if (sumMedicine.get("sumTodaySC") != null) {
				tempMedicine8 = Double.parseDouble(sumMedicine.get("sumTodaySC").toString());
				sumMedicine.put("sumTodaySC", InitsUtils.medicineInits(tempMedicine8));
			}
			if (sumMedicine.get("sumTodaySA") != null) {
				tempMedicine9 = Double.parseDouble(sumMedicine.get("sumTodaySA").toString());
				sumMedicine.put("sumTodaySA", InitsUtils.medicineInits(tempMedicine9));
			}
			if (sumMedicine.get("sumTodayHCL") != null) {
				tempMedicine10 = Double.parseDouble(sumMedicine.get("sumTodayHCL").toString());
				sumMedicine.put("sumTodayHCL", InitsUtils.medicineInits(tempMedicine10));
			}
			Map<String, Object> sumByMedicine = todayMedicineInfoService.getSumByApp(todayMedicineInfo); // 当日前累计的药量
			if (sumByMedicine.get("sumTodayPAC") != null) {
				sumMedicine.put("sumTotalPAC", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayPAC").toString()), tempMedicine1)));
			}
			if (sumByMedicine.get("sumTodayPAMYin") != null) {
				sumMedicine.put("sumTotalPAMYin", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYin").toString()), tempMedicine2)));
			}
			if (sumByMedicine.get("sumTodayPAMYang") != null) {
				sumMedicine.put("sumTotalPAMYang", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYang").toString()), tempMedicine3)));
			}
			if (sumByMedicine.get("sumTodayPhosphorus") != null) {
				sumMedicine.put("sumTotalPhosphorus", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayPhosphorus").toString()), tempMedicine4)));
			}
			if (sumByMedicine.get("sumTodayNaCLO") != null) {
				sumMedicine.put("sumTotalNaCLO", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayNaCLO").toString()), tempMedicine5)));
			}
			if (sumByMedicine.get("sumTodayLime") != null) {
				sumMedicine.put("sumTotalLime", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayLime").toString()), tempMedicine6)));
			}
			if (sumByMedicine.get("sumTodayGlucose") != null) {
				sumMedicine.put("sumTotalGlucose", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayGlucose").toString()), tempMedicine7)));
			}
			if (sumByMedicine.get("sumTodaySC") != null) {
				sumMedicine.put("sumTotalSC", InitsUtils.medicineInits(
						InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySC").toString()), tempMedicine8)));
			}
			if (sumByMedicine.get("sumTodaySA") != null) {
				sumMedicine.put("sumTotalSA", InitsUtils.medicineInits(
						InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySA").toString()), tempMedicine9)));
			}
			if (sumByMedicine.get("sumTodayHCL") != null) {
				sumMedicine.put("sumTotalHCL", InitsUtils.medicineInits(InitsUtils
						.add(Double.parseDouble(sumByMedicine.get("sumTodayHCL").toString()), tempMedicine10)));
			}
			mMap.put("sumMedicine", sumMedicine);
		}
		return prefix + "/homeAdmin";
	}

	/**
	 * 员工填报首页级 homeEmployee
	 */
	@GetMapping("/homeEmployee")
	public String homeEmployee(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			UserHealthInfo userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setFillDate(DateUtils.getDate());
			userHealthInfo.setWorkType("1");
			mMap.put("orgType", sysOrg.getOrgType());
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
				mMap.put("factoryType", sysOrg.getFactoryType());
			} else {
				userHealthInfo.getParams().put("areaIds", areaIds);
			}

		}
		return prefix + "/homeEmployee";
	}

	/**
	 * 财务级首页 homeFinance
	 */
	@GetMapping("/homeFinance")
	public String homeFinance(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaId = sysUser.getAreaStr();
		CostInfo costInfo = new CostInfo();
		costInfo.setAreaId(areaId);
		costInfo.setEffectIcon("1");
		mMap.put("factoryName", sysUser.getSysOrgs().get(0).getOrgName());
		mMap.put("costInfoList", costInfoService.getList(costInfo));
		return prefix + "/homeFinance";
	}

	/**
	 * 区域值班统计
	 */
	@GetMapping("/beOnDutyArea")
	public String beOnDutyArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		return prefix + "/be_on_duty_area";
	}

	/**
	 * 公司值班统计
	 */
	@GetMapping("/beOnDutyCompany/{areaId}")
	public String beOnDutyCompany(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/be_on_duty_company";
	}

	/**
	 * 公司值班统计
	 */
	@GetMapping("/beOnDutyCompanys/{areaGroupTypeId}")
	public String beOnDutyCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/be_on_duty_companys";
	}

	/**
	 * 水厂值班统计
	 */
	@GetMapping("/beOnDutyWaterWorks/{areaId}")
	public String beOnDutyWaterWorks(@PathVariable("areaId") String areaId, ModelMap mMap) {
		// SysOrg company = orgService.getById(areaId);
		if (areaId != null) {
			mMap.put("areaId", areaId);
		}
		return prefix + "/be_on_duty_water_works";
	}

	/**
	 * 区域水量分析
	 */
	@GetMapping("/waterYieldArea")
	public String waterYieldArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		SysOrg sysOrg = getLoginUserOrg();
		if ("3".equals(sysOrg.getOrgType())) { // 水厂领导(水厂只会有一个区域)

			Area area = areaService.getEntityById(areaIds);
			if (area != null) {
				mMap.put("area", area);
			}
			return prefix + "/water_yield_factory_sub";
		} else {
			return prefix + "/water_yield_area";
		}
	}

	/**
	 * 公司水量分析
	 */
	@GetMapping("/waterYieldCompany/{areaId}")
	public String waterYieldCompany(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/water_yield_company";
	}

	/**
	 * 区域列表中，点击区域列出该区域下的所有水厂 yangxiaojun 2020-04-20
	 */
	@GetMapping("/waterYieldFactorySub/{areaId}")
	public String waterYieldCompanyBySub(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/water_yield_factory_sub";
	}

	/**
	 * 公司水量分析
	 */
	@GetMapping("/waterYieldCompanys/{areaGroupTypeId}")
	public String waterYieldCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/water_yield_companys";
	}

	/**
	 * 水厂水量分析
	 */
	@GetMapping("/waterYieldWaterWorks/{companyId}")
	public String waterYieldWaterWorks(@PathVariable("companyId") String companyId, ModelMap mMap) {
		SysOrg company = orgService.getById(companyId);
		if (company != null) {
			mMap.put("company", company);
		}
		return prefix + "/water_yield_water_works";
	}

	/**
	 * 区域电量分析
	 */
	@GetMapping("/electricQuantityArea")
	public String electricQuantityArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		// return prefix+"/electric_quantity_area";
		SysOrg sysOrg = getLoginUserOrg();
		if ("3".equals(sysOrg.getOrgType())) { // 水厂领导(水厂只会有一个区域)

			Area area = areaService.getEntityById(areaIds);
			if (area != null) {
				mMap.put("area", area);
			}
			return prefix + "/electric_quantity_factory_sub";
		} else {
			return prefix + "/electric_quantity_area";
		}
	}

	/**
	 * 公司电量分析
	 */
	@GetMapping("/electricQuantityCompany/{areaId}")
	public String electricQuantityCompany(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/electric_quantity_company";
	}

	/**
	 * 电量： 区域下级分析
	 * 
	 * @param areaId
	 * @param mMap
	 * @return
	 */

	@GetMapping("/electricQuantityFactory/{areaId}")
	public String electricQuantityFactory(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/electric_quantity_factory_sub";
	}

	/**
	 * 公司电量分析(地图)
	 */
	@GetMapping("/electricQuantityCompanys/{areaGroupTypeId}")
	public String electricQuantityCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/electric_quantity_companys";
	}

	/**
	 * 水厂电量分析
	 */
	@GetMapping("/electricQuantityWaterWorks/{companyId}")
	public String electricQuantityWaterWorks(@PathVariable("companyId") String companyId, ModelMap mMap) {
		SysOrg company = orgService.getById(companyId);
		if (company != null) {
			mMap.put("company", company);
		}
		return prefix + "/electric_quantity_water_works";
	}

	/**
	 * 区域药耗分析
	 */
	@GetMapping("/medicationArea")
	public String medicationArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);

		SysOrg sysOrg = getLoginUserOrg();
		if ("3".equals(sysOrg.getOrgType())) { // 水厂领导(水厂只会有一个区域)

			Area area = areaService.getEntityById(areaIds);
			if (area != null) {
				mMap.put("area", area);
			}
			return prefix + "/medication_factory_sub";
		} else {
			return prefix + "/medication_area";
		}

	}

	/**
	 * 公司药耗分析 (列表)
	 */
	@GetMapping("/medicationCompany/{areaId}")
	public String medicationCompany(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/medication_factory_sub";
	}

	/**
	 * 公司药耗分析(地图)
	 */
	@GetMapping("/medicationCompanys/{areaGroupTypeId}")
	public String medicationCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/medication_companys";
	}

	/**
	 * 水厂药耗分析
	 */
	@GetMapping("/medicationWaterWorks/{companyId}")
	public String medicationWaterWorks(@PathVariable("companyId") String companyId, ModelMap mMap) {
		SysOrg company = orgService.getById(companyId);
		if (company != null) {
			mMap.put("company", company);
		}
		return prefix + "/medication_water_works";
	}

	/**
	 * 区域水质分析
	 */
	@GetMapping("/waterQualityArea")
	public String waterQualityArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		return prefix + "/water_quality_area";
	}

	/**
	 * 化验数据
	 * 
	 */
	@GetMapping("/testWaterArea")
	public String testWaterArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		return prefix + "/test_water_area";
	}

	/**
	 * 公司水质分析
	 */
	@GetMapping("/waterQualityCompanys/{areaGroupTypeId}")
	public String waterQualityCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		mMap.put("areaGroupTypeId", areaGroupTypeId);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/water_quality_companys";
	}

	/**
	 * 化验数据地图查询
	 */
	@GetMapping("/testWaterPicture/{areaGroupTypeId}")
	public String testWaterPicture(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		mMap.put("areaGroupTypeId", areaGroupTypeId);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/test_water_picture";
	}

	/**
	 * 强制检测:地图查询
	 * 
	 */
	@GetMapping("/mandatoryCheckPicture/{areaGroupTypeId}")
	public String mandatoryCheckPicture(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		mMap.put("areaGroupTypeId", areaGroupTypeId);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/mandatory_check_picture";
	}

	/**
	 * 水厂水质分析
	 */
	@GetMapping("/waterQualityWaterWorks/{companyId}")
	public String waterQualityWaterWorks(@PathVariable("companyId") String companyId, ModelMap mMap) {
		SysOrg company = orgService.getById(companyId);
		if (company != null) {
			mMap.put("company", company);
		}
		return prefix + "/water_quality_water_works";
	}

	/**
	 * 区域污泥分析
	 */
	@GetMapping("/sludgeArea")
	public String sludgeArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);

		SysOrg sysOrg = getLoginUserOrg();
		if ("3".equals(sysOrg.getOrgType())) { // 水厂领导(水厂只会有一个区域)

			Area area = areaService.getEntityById(areaIds);
			if (area != null) {
				mMap.put("area", area);
			}
			return prefix + "/sludge_factory_sub";
		} else {
			return prefix + "/sludge_area";
		}

	}

	/**
	 * 公司污泥分析
	 */
	@GetMapping("/sludgeCompany/{areaId}")
	public String sludgeCompany(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/sludge_company";
	}

	/**
	 * 公司污泥分析：区域下级水厂级
	 * 
	 * @author yangxiaojun
	 * @param areaId
	 * @param mMap
	 * @return
	 */
	@GetMapping("/sludgeFactorySub/{areaId}")
	public String sludgeFactorySub(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/sludge_factory_sub";
	}

	/**
	 * 公司污泥分析(地图)
	 */
	@GetMapping("/sludgeCompanys/{areaGroupTypeId}")
	public String sludgeCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/sludge_companys";
	}

	/**
	 * 水厂污泥分析
	 */
	@GetMapping("/sludgeWaterWorks/{companyId}")
	public String sludgeWaterWorks(@PathVariable("companyId") String companyId, ModelMap mMap) {
		SysOrg company = orgService.getById(companyId);
		if (company != null) {
			mMap.put("company", company);
		}
		return prefix + "/sludge_water_works";
	}

	/**
	 * 登录用户详细信息
	 */
	private void loginUserInfo(ModelMap mmp) {
		// 当前登录供应商信息
		SysUser sysUser = getLoginUserInfo();
		mmp.put("loginUserInfo", sysUser);
		if (sysUser != null) {
			// 用户头像
			if (StringUtils.isNotEmpty(sysUser.getAvatar())) {
				SysFiles sysFiles = sysFilesService.selectSysFilesById(sysUser.getAvatar());
				if (sysFiles != null && !"".equals(sysFiles.getPath())) {
					mmp.put("avatar", sysFiles.getPath());
				}
			}
		}
	}

	/**
	 * 综合统计
	 * 
	 * @return
	 */
	@GetMapping("/comprehensiveArea")
	public String comprehensiveArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		return prefix + "/comprehensive_area";
	}

	/**
	 * 公司综合统计
	 */
	@GetMapping("/comprehensiveCompanys/{areaGroupTypeId}")
	public String comprehensiveCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		for (Area item : areaList) {
			areaIds.add(item.getAreaId());
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/comprehensive_companys";
	}
}
