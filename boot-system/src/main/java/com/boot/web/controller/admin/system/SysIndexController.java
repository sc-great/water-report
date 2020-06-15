package com.boot.web.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boot.common.config.Global;
import com.boot.common.core.controller.BaseController;
import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.materialControl.domain.Consumable;
import com.boot.materialControl.domain.MandatoryCheckInfo;
import com.boot.materialControl.service.ConsumableService;
import com.boot.materialControl.service.IMandatoryCheckInfoService;
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
import com.boot.system.domain.*;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.system.service.ISysFilesService;
import com.boot.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页 业务处理
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin")
public class SysIndexController extends BaseController {

	private String prefix = "admin";
	@Autowired
	private ISysMenuService menuService;
	@Autowired
	private ISysFilesService sysFilesService;
	@Autowired
	private ITodayWaterYieldInfoService todayWaterYieldInfoService;
	@Autowired
	private ITodayElectricityInfoService todayElectricityInfoService;
	@Autowired
	private ITodayMedicineInfoService todayMedicineInfoService;
	@Autowired
	private IUserHealthInfoService userHealthInfoService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysFactoryInfoService factoryInfoService;
	@Autowired
	private IBadWaterQualityInfoService badWaterQualityInfoService;
	@Autowired
	private IGoodWaterHealthInfoService goodWaterHealthInfoService;
	@Autowired
	private ConsumableService consumableService;
	@Autowired
	private IMandatoryCheckInfoService mandatoryCheckInfoService;

	/**
	 * 系统首页
	 */
	@GetMapping("/index")
	public String index(ModelMap mmap) {
		// 取身份信息
		SysUser user = ShiroUtils.getSysUser();
		// 用户头像
		if (user.getAvatar() != null && !"".equals(user.getAvatar()))
			mmap.put("avatar", sysFilesService.selectSysFilesById(user.getAvatar()));
		// 根据用户id取出菜单
		List<SysMenu> menus = menuService.selectMenusByUser(user);
		mmap.put("menus", menus);
		mmap.put("user", user);
		mmap.put("copyrightYear", Global.getCopyrightYear());
		mmap.put("demoEnabled", Global.isDemoEnabled());
		// 超级管理员
		if (("1").equals(user.getUserId()))
			return prefix + "/index";
		
		// 当前用户所属区域、公司、水厂
		SysOrg sysOrg = null;
		SysRole sysRole = null;
		try {
			sysOrg = user.getSysOrgs().get(0);
			sysRole = user.getSysRoles().get(0);
		} catch (Exception e) {
			return prefix + "/index";
		}
		
		mmap.put("role", sysRole.getRoleKey());
		
		// 集团领导直接返回
		if ("1".equals(sysRole.getRoleKey()))
			return prefix + "/index";
		
		boolean isAreaUser = false; // 标记用户是否是区域级别的用户
		if ("8".equals(sysRole.getRoleKey())) // 财务单独处理
			isAreaUser = true;
		
		ArrayList<String> headerStrList = new ArrayList<>();
		// 第一级
		String orgNamePath = sysOrg.getOrgNamePath();
		String factoryStr = orgNamePath.substring(orgNamePath.indexOf("/") + 1);
		String[] factoryStrArr = factoryStr.split("/");
		if (factoryStrArr.length > 0)
			headerStrList.add(factoryStrArr[0]);
		// 如果区域领导，直接返回（即11个大区）
		if ("6".equals(sysRole.getRoleKey())) {
			mmap.put("headerStrList", headerStrList);
			return prefix + "/index";
		}
		
		// 第二级——区域
		if (isAreaUser) { // 区域级用户
			headerStrList.add(areaService.getEntityById(user.getAreaStr()).getAreaName());
		} else {
			Area areaParam = new Area();
			areaParam.getParams().put("ids", sysOrg.getAreaId().split(","));
			List<Area> areaList = areaService.getList(areaParam);
			List<String> areaNamesList = new ArrayList<>();
			if (StringUtils.isNotEmpty(areaList)) {
				for (Area area : areaList) {
					areaNamesList.add(area.getAreaName());
				}
			}
			String areaNames = StringUtils.join(areaNamesList, "/");

			if (factoryStrArr.length > 0) {
				if (StringUtils.isNotEmpty(areaNames)) {
					headerStrList.add(areaNames);
				}
				headerStrList.add(factoryStrArr[1]);
			}
		}
		mmap.put("headerStrList", headerStrList);
		return prefix + "/index";
	}

	/**
	 * 系统主页
	 */
	@GetMapping("/main")
	public String main(ModelMap mMap) {
		SysUser curUser = ShiroUtils.getSysUser();
		// 判断当前用户是否是管理员
		// ////////////////////////////////////////////////////////////////////////////////////////
		boolean isAdmin = false;
		if (("1").equals(curUser.getUserId()))
			isAdmin = true;
		mMap.put("isAdmin", isAdmin);
		if (isAdmin)
			return prefix + "/main";
		// 判断当前用户是不是财务
		// ////////////////////////////////////////////////////////////////////////////////////////
		boolean isAccounting = false;
		if ("8".equals(curUser.getSysRoles().get(0).getRoleCode()))
			isAccounting = true;
		mMap.put("isAccounting", isAccounting);
		if (isAccounting)
			return prefix + "/main";
		// ////////////////////////////////////////////////////////////////////////////////////////
		SysOrg curOrg = curUser.getSysOrgs().get(0);
		String orgId = curOrg.getOrgId();
		mMap.put("orgType", curOrg.getOrgType());
		mMap.put("factoryType", curOrg.getFactoryType());
		// 判断是不是填报人员
		// ////////////////////////////////////////////////////////////////////////////////////////
		String today = DateUtils.getDate();
		SysRole role = curUser.getSysRoles().get(0);
		if ("3".equals(role.getRoleKey()) || "4".equals(role.getRoleKey()) || "5".equals(role.getRoleKey())) {
			mMap.put("isLeader", false);
			return prefix + "/main";
		}
		if (curUser.getAreaStr() == null || "".equals(curUser.getAreaStr())) {
			throw new BusinessException("您还未分配区域，请联系管理员！");
		}
		mMap.put("isLeader", true);
		// 现场办公人数
		UserHealthInfo uhi_live = new UserHealthInfo();
		uhi_live.setEffectIcon("1");
		uhi_live.setIsInFactory("1");
		uhi_live.setFillDate(today);
		uhi_live.setWorkType("1");
		// 远程办公人数
		UserHealthInfo uhi_line = new UserHealthInfo();
		uhi_line.setEffectIcon("1");
		uhi_line.setIsInFactory("1");
		uhi_line.setFillDate(today);
		uhi_line.setWorkType("2");
		// 总人数
		SysFactoryInfo factoryInfoParam = new SysFactoryInfo();
		// ----------------------- 统计数据 -------------------------------------------
		// 今日水量数据信息
		TodayWaterYieldInfo todayWaterYieldInfoParam = new TodayWaterYieldInfo();
		todayWaterYieldInfoParam.setEffectIcon("1");
		todayWaterYieldInfoParam.setFillDate(today);
		// 今日电量数据信息
		TodayElectricityInfo todayElectricityInfoParam = new TodayElectricityInfo();
		todayElectricityInfoParam.setEffectIcon("1");
		todayElectricityInfoParam.setFillDate(today);
		// 今日药量数据信息
		TodayMedicineInfo todayMedicineInfoParam = new TodayMedicineInfo();
		todayMedicineInfoParam.setEffectIcon("1");
		todayMedicineInfoParam.setFillDate(today);
		
		if ("1".equals(role.getRoleKey()) || "6".equals(role.getRoleKey())) { // 集团领导和区域领导
			Map<String, Object> params = new HashMap<>();
			params.put("areaIds", curUser.getAreaStr());
			// 现场办公人数，根据管理区域查询
			uhi_live.setParams(params);
			// 远程办公人数，根据管理区域查询
			uhi_line.setParams(params);
			// 总人数
			factoryInfoParam.setParams(params);
			// 今日水量数据信息
			todayWaterYieldInfoParam.setParams(params);
			// 今日电量数据信息
			todayElectricityInfoParam.setParams(params);
			// 今日药量数据信息
			todayMedicineInfoParam.setParams(params);
		}
		if ("7".equals(role.getRoleKey())) { // 水厂领导
			// 现场办公人数，查询水厂
			uhi_live.setFactoryId(orgId);
			// 远程办公人数，查询水厂
			uhi_line.setFactoryId(orgId);
			// 总人数
			factoryInfoParam.setOrgId(orgId);
			// 今日水量数据信息
			todayWaterYieldInfoParam.setFactoryId(orgId);
			// 今日电量数据信息
			todayElectricityInfoParam.setFactoryId(orgId);
			// 今日药量数据信息
			todayMedicineInfoParam.setFactoryId(orgId);
		}
		// 现场办公人数
		int sceneWorkerCount = userHealthInfoService.getCount(uhi_live);
		mMap.put("sceneWorkerCount", sceneWorkerCount);
		// 远程办公人数
		int onlineWorkerCount = userHealthInfoService.getCount(uhi_line);
		mMap.put("onlineWorkerCount", onlineWorkerCount);
		// 到岗总人数
		int workerCount = sceneWorkerCount + onlineWorkerCount;
		mMap.put("workerCount", workerCount);
		// 总人数--------------------根据工厂信息来查询--------------------------------------
		int workerTotal = factoryInfoService.getCountByAppFactoryPersonNum(factoryInfoParam);
		mMap.put("workerTotal", workerTotal);
		// 到岗百分比
		DecimalFormat df = new DecimalFormat("0.00");
		mMap.put("workerPer", workerTotal == 0 ? "0" : df.format((float) workerCount / workerTotal * 100) + "%");
		// ----------------------- 统计数据 -------------------------------------------
		// 是否正常及错误信息
		boolean isWaterNormal = true;
		boolean isElectricNormal = true;
		boolean isMedicineNormal = true;
		// 查看条件
		// 今日水量数据信息
		try {
			TodayWaterYieldInfo TodayWaterYieldInfoReturn = new TodayWaterYieldInfo();
			Map<String, Object> todayWaterYieldInfoMap = todayWaterYieldInfoService.getTodayForChart(todayWaterYieldInfoParam);
			Map<String, Object> totalWaterYieldInfoMap = todayWaterYieldInfoService.getSumForChart(todayWaterYieldInfoParam);
			TodayWaterYieldInfoReturn.setTodayIn(Double.parseDouble(todayWaterYieldInfoMap.get("todayIn").toString()));
			TodayWaterYieldInfoReturn.setTodayOut(Double.parseDouble(todayWaterYieldInfoMap.get("todayOut").toString()));
			TodayWaterYieldInfoReturn.setTotalIn(Double.parseDouble(totalWaterYieldInfoMap.get("totalIn").toString()));
			TodayWaterYieldInfoReturn.setTotalOut(Double.parseDouble(totalWaterYieldInfoMap.get("totalOut").toString()));
			mMap.put("todayWaterYieldInfo", TodayWaterYieldInfoReturn);
		} catch (Exception e) {
			isWaterNormal = false;
		}
		// 今日电量数据信息
		try {
			TodayElectricityInfo todayElectricityInfoReturn = new TodayElectricityInfo();
			Map<String, Object> todayElectricityInfoMap = todayElectricityInfoService.getTodayForChart(todayElectricityInfoParam);
			Map<String, Object> totalElectricityInfoMap = todayElectricityInfoService.getSumForChart(todayElectricityInfoParam);
			todayElectricityInfoReturn.setTodayElectricity(Double.parseDouble(todayElectricityInfoMap.get("todayElectricity").toString()));
			todayElectricityInfoReturn.setPumpTodayEletricity(Double.parseDouble(todayElectricityInfoMap.get("todayPumpEletricity").toString()));
			todayElectricityInfoReturn.setTotalElectricity(Double.parseDouble(totalElectricityInfoMap.get("totalElectricity").toString()));
			todayElectricityInfoReturn.setPumpTotalEletricity(Double.parseDouble(totalElectricityInfoMap.get("totalPumpEletricity").toString()));
			mMap.put("todayElectricityInfo", todayElectricityInfoReturn);
		} catch (Exception e) {
			isElectricNormal = false;
		}
		// 今日药量数据信息
		try {
			TodayMedicineInfo todayMedicineInfoReturn = new TodayMedicineInfo();
			// 当日
			Map<String, Object> todayMedicineInfoMap = todayMedicineInfoService.getTodayForChart(todayMedicineInfoParam);
			todayMedicineInfoReturn.setTodayPac(Double.parseDouble(todayMedicineInfoMap.get("todayPac").toString()));
			todayMedicineInfoReturn.setTodayPamYin(Double.parseDouble(todayMedicineInfoMap.get("todayPamYin").toString()));
			todayMedicineInfoReturn.setTodayPamYang(Double.parseDouble(todayMedicineInfoMap.get("todayPamYang").toString()));
			todayMedicineInfoReturn.setTodayPhosphorus(Double.parseDouble(todayMedicineInfoMap.get("todayPhosphorus").toString()));
			todayMedicineInfoReturn.setTodayNaclo(Double.parseDouble(todayMedicineInfoMap.get("todayNaclo").toString()));
			todayMedicineInfoReturn.setTodayLime(Double.parseDouble(todayMedicineInfoMap.get("todayLime").toString()));
			todayMedicineInfoReturn.setTodayGlucose(Double.parseDouble(todayMedicineInfoMap.get("todayGlucose").toString()));
			todayMedicineInfoReturn.setTodaySc(Double.parseDouble(todayMedicineInfoMap.get("todaySc").toString()));
			todayMedicineInfoReturn.setTodaySa(Double.parseDouble(todayMedicineInfoMap.get("todaySa").toString()));
			todayMedicineInfoReturn.setTodayHCL(Double.parseDouble(todayMedicineInfoMap.get("todayHCL").toString()));
			// 累计
			Map<String, Object> totalMedicineInfoMap = todayMedicineInfoService.getSumForChart(todayMedicineInfoParam);
			todayMedicineInfoReturn.setTotalPac(Double.parseDouble(totalMedicineInfoMap.get("totalPac").toString()));
			todayMedicineInfoReturn.setTotalPamYin(Double.parseDouble(totalMedicineInfoMap.get("totalPamYin").toString()));
			todayMedicineInfoReturn.setTotalPamYang(Double.parseDouble(totalMedicineInfoMap.get("totalPamYang").toString()));
			todayMedicineInfoReturn.setTotalPhosphorus(Double.parseDouble(totalMedicineInfoMap.get("totalPhosphorus").toString()));
			todayMedicineInfoReturn.setTotalNaclo(Double.parseDouble(totalMedicineInfoMap.get("totalNaclo").toString()));
			todayMedicineInfoReturn.setTotalLime(Double.parseDouble(totalMedicineInfoMap.get("totalLime").toString()));
			todayMedicineInfoReturn.setTotalGlucose(Double.parseDouble(totalMedicineInfoMap.get("totalGlucose").toString()));
			todayMedicineInfoReturn.setTotalSc(Double.parseDouble(totalMedicineInfoMap.get("totalSc").toString()));
			todayMedicineInfoReturn.setTotalSa(Double.parseDouble(totalMedicineInfoMap.get("totalSa").toString()));
			todayMedicineInfoReturn.setTotalHCL(Double.parseDouble(totalMedicineInfoMap.get("totalHCL").toString()));
			mMap.put("todayMedicineInfo", todayMedicineInfoReturn);
		} catch (Exception e) {
			isMedicineNormal = false;
		}
		mMap.put("isWaterNormal", isWaterNormal);
		mMap.put("isElectricNormal", isElectricNormal);
		mMap.put("isMedicineNormal", isMedicineNormal);
		
		// 是不是水厂领导（以下数据只有水厂领导显示）
		if (!"7".equals(role.getRoleKey())) { // 水厂领导
			mMap.put("shiBuShiShuiChangLingDao", false);
			return prefix + "/main_old";
		}
		mMap.put("shiBuShiShuiChangLingDao", true);
		// 水质数据 ------------------------------------------------------------------
		// 污水厂
		boolean isBadWaterNormal = true;
		if ("1".equals(curOrg.getFactoryType())) {
			BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
			badWaterQualityInfo.setFactoryId(orgId);
			badWaterQualityInfo.setFillDate(today);
			badWaterQualityInfo.setEffectIcon("1");
			BadWaterQualityInfo bwqi = badWaterQualityInfoService.getEntity(badWaterQualityInfo);
			if (bwqi != null) {
				mMap.put("badWaterQualityInfo", bwqi);
			} else {
				isBadWaterNormal = false;
			}
		}
		mMap.put("isBadWaterNormal", isBadWaterNormal);
		// 供水厂
		boolean isGoodWaterNormal = true;
		if ("2".equals(curOrg.getFactoryType())) {
			GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
			goodWaterHealthInfo.setFactoryId(orgId);
			goodWaterHealthInfo.setFillDate(today);
			goodWaterHealthInfo.setEffectIcon("1");
			GoodWaterHealthInfo gwhi = goodWaterHealthInfoService.getEntity(goodWaterHealthInfo);
			if (gwhi != null) {
				mMap.put("goodWaterHealthInfo", gwhi);
			} else {
				isGoodWaterNormal = false;
			}
		}
		mMap.put("isGoodWaterNormal", isGoodWaterNormal);
		// 备品备件 ------------------------------------------------------------------
		Consumable consumable = new Consumable();
		consumable.setFactoryId(orgId);
		consumable.setEffectIcon("1");
		List<Consumable> consumableList = consumableService.getList(consumable);
		if (StringUtils.isNotEmpty(consumableList)) {
			JSONArray JA = new JSONArray();
			JSONObject JO = null;
			for (Consumable c : consumableList) {
				JO = new JSONObject();
				String name = c.getName();
				String model = c.getModel();
				name = name + (model == null || "".equals(model.trim()) ? "" : ("（" + model.trim() + "）"));
				int total = c.getTotal();
				String unit = c.getUnit();
				String total_str = total + "（" + unit + "）";
				
				String lastSetTime = c.getLastSetTime();
				String lastGetTime = c.getLastGetTime();
				
				boolean isGet = false;
				if (lastGetTime != null && lastGetTime != "" && (DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", lastGetTime).after(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", lastSetTime))))
					isGet = true;
				
				String info = "";
				if (isGet)
					info = lastGetTime + "  " + c.getLastGetUserName() + " 领用 " + c.getLastGetNum();
				else
					info = lastSetTime + "  " + c.getLastSetUserName() + " 添加 " + c.getLastSetNum();	
				
				JO.put("name", name);
				JO.put("total", total_str);
				JO.put("info", info);
				JA.add(JO);
			}
			mMap.put("consumableList", JA);
			mMap.put("isConsumableNormal", true);
		} else {
			mMap.put("isConsumableNormal", false);
		}
		// 强检设备 ------------------------------------------------------------------
		MandatoryCheckInfo mandatoryCheckInfo = new MandatoryCheckInfo();
		mandatoryCheckInfo.setFactoryId(orgId);
		mandatoryCheckInfo.setEffectIcon("1");
		List<MandatoryCheckInfo> mandatoryCheckInfoList = mandatoryCheckInfoService.getList(mandatoryCheckInfo);
		if (StringUtils.isNotEmpty(mandatoryCheckInfoList)) {
			mMap.put("mandatoryCheckInfoList", mandatoryCheckInfoList);
			mMap.put("isMandatoryCheckNormal", true);
		} else {
			mMap.put("isMandatoryCheckNormal", false);
		}
		// 水质预警信息？？不用显示了吧，有报警窗口了
		
		return prefix + "/main_old";
	}
}