package com.boot.web.controller.app;

import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.text.Convert;
import com.boot.common.utils.InitsUtils;
import com.boot.common.utils.StringUtils;
import com.boot.materialControl.domain.MandatoryCheckInfo;
import com.boot.materialControl.service.IMandatoryCheckInfoService;
import com.boot.report.domain.*;
import com.boot.report.service.*;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.system.service.ISysOrgService;
import com.boot.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class AppDataController extends APPBaseController {
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private IUserHealthInfoService userHealthInfoService;
	@Autowired
	private ISysUserService userService;
	@Autowired
	private ITodayWaterYieldInfoService todayWaterYieldInfoService;
	@Autowired
	private ITodayElectricityInfoService todayElectricityInfoService;
	@Autowired
	private ITodayMedicineInfoService todayMedicineInfoService;
	@Autowired
	private IBadWaterQualityInfoService badWaterQualityInfoService;
	@Autowired
	private IGoodWaterHealthInfoService goodWaterHealthInfoService;
	@Autowired
	private IMudInfoService mudInfoService;
	@Autowired
	private ITestBadWaterInfoService testBadWaterInfoService;
	@Autowired
	private ITestTapWaterInfoService testTapWaterInfoService;
	
	@Autowired
	private IMandatoryCheckInfoService mandatoryCheckInfoService;
	@Autowired
	private ISysFactoryInfoService sysFactoryInfoService;

	@RequestMapping("/getAreaList")
	@ResponseBody
	public AjaxResult getAreaList(String groupType) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysUser sysUser = getLoginUserInfo();
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			if (StringUtils.isNotEmpty(groupType)) {
				area.setGroupType(groupType);
			}
			List<Area> areaList = areaService.getList(area);
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	@RequestMapping("/getCompanyList")
	@ResponseBody
	public AjaxResult getCompanyList(String areaId) {
		AjaxResult ajaxResult;
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getParentId());
		} else {
			sysOrg_params.setAreaId(areaId);
		}
		sysOrg_params.setOrgType("2");
		List<SysOrg> list = orgService.findList(sysOrg_params);
		ajaxResult = AjaxResult.success(list);
		return ajaxResult;
	}

	/**
	 * 区域值班数据
	 */
	@RequestMapping("/beOnDutyAreaData")
	@ResponseBody
	public AjaxResult beOnDutyAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				UserHealthInfo userHealthInfo = new UserHealthInfo();
				userHealthInfo.setEffectIcon("1");
				userHealthInfo.setIsInFactory("1");
				userHealthInfo.setFillDate(date);
				userHealthInfo.setWorkType("1");
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					userHealthInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					userHealthInfo.setAreaId(item.getAreaId());
				}
				// 现场办公人数
				int sceneWorkerCount = userHealthInfoService.getCount(userHealthInfo);
				item.getParams().put("sceneWorkerCount", sceneWorkerCount);
				userHealthInfo = new UserHealthInfo();
				userHealthInfo.setEffectIcon("1");
				userHealthInfo.setIsInFactory("1");
				userHealthInfo.setFillDate(date);
				userHealthInfo.setWorkType("2");
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					userHealthInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					userHealthInfo.setAreaId(item.getAreaId());
				}
				// 远程办公人数
				int onlineWorkerCount = userHealthInfoService.getCount(userHealthInfo);
				item.getParams().put("onlineWorkerCount", onlineWorkerCount);
				// 到岗总人数
				int workerCount = sceneWorkerCount + onlineWorkerCount;
				item.getParams().put("workerCount", workerCount);

				SysUser sysUser_params = new SysUser();
				sysUser_params.getParams().put("leader", "1");

				SysFactoryInfo sysFactoryInfo_params = new SysFactoryInfo();
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					sysFactoryInfo_params.setOrgId(sysOrg.getOrgId());
				} else {
					sysFactoryInfo_params.getParams().put("areaIds", item.getAreaId());
				}

				// 总人数
				int workerTotal = sysFactoryInfoService.getCountByAppFactoryPersonNum(sysFactoryInfo_params);

				item.getParams().put("workerTotal", workerTotal);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	/**
	 * 公司值班数据
	 */
	@RequestMapping("/beOnDutyCompanyData")
	@ResponseBody
	public AjaxResult beOnDutyCompanyData(String areaId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getParentId());
		} else {
			sysOrg_params.setAreaId(areaId);
		}
		sysOrg_params.setOrgType("2");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			UserHealthInfo userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setWorkType("1");
			userHealthInfo.setFillDate(date);
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				userHealthInfo.getParams().put("companyId", item.getOrgId());
			}
			// 现场办公人数
			int sceneWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			item.getParams().put("sceneWorkerCount", sceneWorkerCount);
			userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setWorkType("2");
			userHealthInfo.setFillDate(date);
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				userHealthInfo.getParams().put("companyId", item.getOrgId());
			}
			// 远程办公人数
			int onlineWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			item.getParams().put("onlineWorkerCount", onlineWorkerCount);
			// 到岗总人数
			int workerCount = sceneWorkerCount + onlineWorkerCount;
			item.getParams().put("workerCount", workerCount);
			SysUser sysUser_params = new SysUser();
			sysUser_params.getParams().put("leader", "1");
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				sysUser_params.getParams().put("waterWorksId", sysOrg.getOrgId());
			} else {
				sysUser_params.getParams().put("companyId", item.getOrgId());
			}

			// 总人数
			int workerTotal = userService.getCount(sysUser_params);
			item.getParams().put("workerTotal", workerTotal);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 公司值班数据
	 */
	@RequestMapping("/beOnDutyCompanysData")
	@ResponseBody
	public AjaxResult beOnDutyCompanysData(String areaIds, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getParentId());
		} else {
			sysOrg_params.getParams().put("areaIds", areaIds);
		}
		sysOrg_params.setOrgType("2");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			UserHealthInfo userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setWorkType("1");
			userHealthInfo.setFillDate(date);
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				userHealthInfo.getParams().put("companyId", item.getOrgId());
			}
			// 现场办公人数
			int sceneWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			item.getParams().put("sceneWorkerCount", sceneWorkerCount);
			userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setWorkType("2");
			userHealthInfo.setFillDate(date);
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				userHealthInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				userHealthInfo.getParams().put("companyId", item.getOrgId());
			}
			// 远程办公人数
			int onlineWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			item.getParams().put("onlineWorkerCount", onlineWorkerCount);
			// 到岗总人数
			int workerCount = sceneWorkerCount + onlineWorkerCount;
			item.getParams().put("workerCount", workerCount);
			//SysUser sysUser_params = new SysUser();
			//sysUser_params.getParams().put("leader", "1");
			SysFactoryInfo sysFactoryInfo_params = new SysFactoryInfo();
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				sysFactoryInfo_params.setOrgId(sysOrg.getOrgId());
			} else {
				sysFactoryInfo_params.getParams().put("areaIds", item.getAreaId());
			}
			//水厂注册人数
			int workerTotal=sysFactoryInfoService.getCountByAppFactoryPersonNum(sysFactoryInfo_params);

			item.getParams().put("workerTotal", workerTotal);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 水厂值班数据
	 */
	@RequestMapping("/beOnDutyWaterWorksData")
	@ResponseBody
	public AjaxResult beOnDutyWaterWorksData(String areaId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();

		sysOrg_params.setAreaId(areaId);


		List<SysOrg> orgList = orgService.findListByFactory(sysOrg_params);

		// 手机端水厂领导进入查看范围
		if ("3".equals(sysOrg.getOrgType())){
			for (SysOrg orgEntity : orgList) {
				if (sysOrg.getOrgId().equals(orgEntity.getOrgId())){
					List<SysOrg> orgList2= new ArrayList<SysOrg>();
					orgList2.add(orgEntity);
					orgList=orgList2;
					break;
				}
			}
		}
		sysOrg_params.setOrgType("3");

		for (SysOrg item : orgList) {
			UserHealthInfo userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setFillDate(date);
			userHealthInfo.setWorkType("1");
			userHealthInfo.setFactoryId(item.getOrgId());
			// 现场办公人数
			int sceneWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			item.getParams().put("sceneWorkerCount", sceneWorkerCount);
			userHealthInfo = new UserHealthInfo();
			userHealthInfo.setEffectIcon("1");
			userHealthInfo.setIsInFactory("1");
			userHealthInfo.setFillDate(date);
			userHealthInfo.setWorkType("2");
			userHealthInfo.setFactoryId(item.getOrgId());
			// 远程办公人数
			int onlineWorkerCount = userHealthInfoService.getCount(userHealthInfo);
			item.getParams().put("onlineWorkerCount", onlineWorkerCount);
			// 到岗总人数
			int workerCount = sceneWorkerCount + onlineWorkerCount;
			item.getParams().put("workerCount", workerCount);

			SysFactoryInfo sysFactoryInfo_params = new SysFactoryInfo();

			sysFactoryInfo_params.setOrgId(item.getOrgId());

			//水厂注册人数
			int workerTotal=sysFactoryInfoService.getCountByAppFactoryPersonNum(sysFactoryInfo_params);

			item.getParams().put("workerTotal", workerTotal);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 区域水量分析数据
	 */
	@RequestMapping("/waterYieldAreaData")
	@ResponseBody
	public AjaxResult waterYieldAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 水量数据
				TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
				todayWaterYieldInfo.setEffectIcon("1");
				todayWaterYieldInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					todayWaterYieldInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					todayWaterYieldInfo.setAreaId(item.getAreaId());

				}
				Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getSumByAppArea(todayWaterYieldInfo); // 水量区域页面当日水量
				Object totalIn_obj_1 = sumWaterYield.get("totalTodayIn");
				Object totalIn_obj_2 = sumWaterYield.get("totalTodayOut");
				Double temp_a = new Double(0);
				Double temp_b = new Double(0);
				if (totalIn_obj_1 != null) {
					temp_a =Double.parseDouble(sumWaterYield.get("totalTodayIn").toString());
					sumWaterYield.put("totalTodayIn",InitsUtils.waterInits(temp_a));
				}

				if (totalIn_obj_2 != null) {
					
					temp_b = Double.parseDouble(sumWaterYield.get("totalTodayOut").toString());
					sumWaterYield.put("totalTodayOut",InitsUtils.waterInits(temp_b));
				}

				Map<String, Object> sumWaterYieldBefore = todayWaterYieldInfoService
						.getSumByAppAreaBefore(todayWaterYieldInfo); // 水量区域页面当日以前累计水量
                        
				Object totalIn_obj = sumWaterYieldBefore.get("totalTodayIn");
				if (totalIn_obj != null) {

					sumWaterYield.put("totalIn",InitsUtils.waterInits(InitsUtils.add(Double.parseDouble(sumWaterYieldBefore.get("totalTodayIn").toString()),temp_a)) );
				}
				Object totalOut_obj = sumWaterYieldBefore.get("totalTodayOut");
				if (totalOut_obj != null) {
				sumWaterYield.put("totalOut",InitsUtils.waterInits(InitsUtils.add(Double.parseDouble(sumWaterYieldBefore.get("totalTodayOut").toString()),temp_b))  );
				}

				item.getParams().put("sumWaterYield", sumWaterYield);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	/**
	 * 公司水量分析数据
	 */
//	@RequestMapping("/waterYieldCompanyData")
//	@ResponseBody
//	public AjaxResult waterYieldCompanyData(String areaId, String date) {
//		AjaxResult ajaxResult;
//		// 当前用户管理区域
//		SysOrg sysOrg = getLoginUserOrg();
//		SysOrg sysOrg_params = new SysOrg();
//		// 水厂用户
//		if ("3".equals(sysOrg.getOrgType())) {
//			sysOrg_params.setOrgId(sysOrg.getParentId());
//		} else {
//			sysOrg_params.setAreaId(areaId);
//		}
//		sysOrg_params.setOrgType("2");
//		List<SysOrg> orgList = orgService.findList(sysOrg_params);
//		for (SysOrg item : orgList) {
//			// 水量数据
//			TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
//			todayWaterYieldInfo.setEffectIcon("1");
//			todayWaterYieldInfo.setFillDate(date);
//			// 水厂用户
//			if ("3".equals(sysOrg.getOrgType())) {
//				todayWaterYieldInfo.setFactoryId(sysOrg.getOrgId());
//			} else {
//				todayWaterYieldInfo.getParams().put("companyId", item.getOrgId());
//			}
//
//			Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getSum(todayWaterYieldInfo); // 所选 择日的总量
//
//			Object totalIn_obj_1 = sumWaterYield.get("totalTodayIn");
//			Object totalIn_obj_2 = sumWaterYield.get("totalTodayOut");
//			Double temp_a = new Double(0);
//			Double temp_b = new Double(0);
//			if (totalIn_obj_1 != null) {
//				temp_a = (double) Math.round(Double.parseDouble(sumWaterYield.get("totalTodayIn").toString()) * 100)
//						/ 100;
//				// temp_a=temp_a/10000;
//			}
//
//			if (totalIn_obj_2 != null) {
//				temp_b = (double) Math.round(Double.parseDouble(sumWaterYield.get("totalTodayOut").toString()) * 100)
//						/ 100;
//				// temp_b=temp_b/10000;
//			}
//
//			Map<String, Object> sumWaterYieldBefore = todayWaterYieldInfoService
//					.getSumByAppArea_company_Before(todayWaterYieldInfo); // 所选 择日以前的总量
//
//			Object totalIn_obj = sumWaterYieldBefore.get("totalTodayIn");
//			if (totalIn_obj != null) {
//				sumWaterYield.put("totalIn", (double) Math.round(
//						((Double.parseDouble(sumWaterYieldBefore.get("totalTodayIn").toString()) * 10000 + temp_a)
//								/ 10000) * 100)
//						/ 100);
//
//			}
//			Object totalOut_obj = sumWaterYieldBefore.get("totalTodayOut");
//			if (totalOut_obj != null) {
//				sumWaterYield.put("totalOut", (double) Math.round(
//						((Double.parseDouble(sumWaterYieldBefore.get("totalTodayOut").toString()) * 10000 + temp_b)
//								/ 10000) * 100)
//						/ 100);
//
//			}
//
//			item.getParams().put("sumWaterYield", sumWaterYield);
//		}
//		ajaxResult = AjaxResult.success(orgList);
//		return ajaxResult;
//	}
	
/**
 * 点击区域下的水厂水量分析信息
 * yangxiaojun 2020-04-20
 */
	@RequestMapping("/waterYieldFactorySubData")
	@ResponseBody
	public AjaxResult waterYieldFactorySubData(String areaId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();

		sysOrg_params.setAreaId(areaId);
	
		List<SysOrg> orgList = orgService.findListByFactory(sysOrg_params);
		
		// 手机端水厂领导进入查看范围		
		if ("3".equals(sysOrg.getOrgType())){
			for (SysOrg orgEntity : orgList) {
				    if (sysOrg.getOrgId().equals(orgEntity.getOrgId())){
				    	List<SysOrg> orgList2= new ArrayList<SysOrg>();
				    	orgList2.add(orgEntity);
				    	orgList=orgList2;
				    	break;
				    }				
			}
		}
		
		for (SysOrg item : orgList) {
			// 水量数据
			TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
			todayWaterYieldInfo.setEffectIcon("1");
			todayWaterYieldInfo.setFillDate(date);

			todayWaterYieldInfo.setFactoryId(item.getOrgId());

			Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getSum(todayWaterYieldInfo); // 所选 择日的总量

			Object totalIn_obj_1 = sumWaterYield.get("totalTodayIn");
			Object totalIn_obj_2 = sumWaterYield.get("totalTodayOut");
			Double temp_a = new Double(0);
			Double temp_b = new Double(0);
			if (totalIn_obj_1 != null) {
				temp_a =Double.parseDouble(sumWaterYield.get("totalTodayIn").toString());
				sumWaterYield.put("totalTodayIn",InitsUtils.waterInits(temp_a));
			}

			if (totalIn_obj_2 != null) {
				temp_b = Double.parseDouble(sumWaterYield.get("totalTodayOut").toString());
				sumWaterYield.put("totalTodayOut",InitsUtils.waterInits(temp_b));
			}

			Map<String, Object> sumWaterYieldBefore = todayWaterYieldInfoService
					.getSumByAppArea_company_Before(todayWaterYieldInfo); // 所选 择日以前的总量

			Object totalIn_obj = sumWaterYieldBefore.get("totalTodayIn");
			if (totalIn_obj != null) {
			
				sumWaterYield.put("totalIn", InitsUtils.waterInits( InitsUtils.add(Double.parseDouble(sumWaterYieldBefore.get("totalTodayIn").toString()),temp_a)) );
			}
			Object totalOut_obj = sumWaterYieldBefore.get("totalTodayOut");
			if (totalOut_obj != null) {
				
				sumWaterYield.put("totalOut", InitsUtils.waterInits( InitsUtils.add(Double.parseDouble(sumWaterYieldBefore.get("totalTodayOut").toString()),temp_b)) );
			}

			item.getParams().put("sumWaterYield", sumWaterYield);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}
	
	
	
	/**
	 * 公司水量分析数据
	 */
	@RequestMapping("/waterYieldCompanysData")
	@ResponseBody
	public AjaxResult waterYieldCompanysData(String areaIds, String date) {
		AjaxResult ajaxResult;
		
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIdsByUser = sysUser.getAreaStr();

		String[] temp1=   areaIds.split(",");
	
		String tempResult="";
		
		for (int i=0;i<temp1.length;i++){
			
		     if(areaIdsByUser.indexOf(temp1[i])!=-1){
		    	 tempResult=tempResult+temp1[i]+",";
		     }
		}
		if (!"".equals(tempResult)){
			
			areaIds=tempResult.substring(0, tempResult.length()-1);
		}
		
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
		
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
						
			for (Area item : areaList) {
				
			
						// 水量数据
							TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
							todayWaterYieldInfo.setEffectIcon("1");
							todayWaterYieldInfo.setFillDate(date);
							// 水厂用户
							//if ("3".equals(sysOrg.getOrgType())) {
							//	todayWaterYieldInfo.setFactoryId(sysOrg.getOrgId());
							//} else {
								todayWaterYieldInfo.setAreaId(item.getAreaId());
			
						//	}
							Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getSumByAppArea(todayWaterYieldInfo); // 水量区域页面当日水量
							Object totalIn_obj_1 = sumWaterYield.get("totalTodayIn");
							Object totalIn_obj_2 = sumWaterYield.get("totalTodayOut");
							Double temp_a = new Double(0);
							Double temp_b = new Double(0);
							if (totalIn_obj_1 != null) {
								temp_a =Double.parseDouble(sumWaterYield.get("totalTodayIn").toString()) ;
								sumWaterYield.put("totalTodayIn", InitsUtils.waterInits(temp_a));
							}
			
							if (totalIn_obj_2 != null) {
								temp_b = Double.parseDouble(sumWaterYield.get("totalTodayOut").toString());
								sumWaterYield.put("totalTodayOut", InitsUtils.waterInits(temp_b));
							}
			
							Map<String, Object> sumWaterYieldBefore = todayWaterYieldInfoService
									.getSumByAppAreaBefore(todayWaterYieldInfo); // 水量区域页面当日以前累计水量
			
							Object totalIn_obj = sumWaterYieldBefore.get("totalTodayIn");
							if (totalIn_obj != null) {
							
								sumWaterYield.put("totalIn",InitsUtils.waterInits(InitsUtils.add(Double.parseDouble(sumWaterYieldBefore.get("totalTodayIn").toString()), temp_a) ));
			
							}
							Object totalOut_obj = sumWaterYieldBefore.get("totalTodayOut");
							if (totalOut_obj != null) {
								
								sumWaterYield.put("totalOut",InitsUtils.waterInits(InitsUtils.add(Double.parseDouble(sumWaterYieldBefore.get("totalTodayOut").toString()), temp_b) ));

							}
			
							item.getParams().put("sumWaterYield", sumWaterYield);
						
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		
		return ajaxResult;
	}

	/**
	 * 水厂水量分析数据
	 */
//	@RequestMapping("/waterYieldWaterWorksData")
//	@ResponseBody
//	public AjaxResult waterYieldWaterWorksData(String companyId, String date) {
//		AjaxResult ajaxResult;
//		// 当前用户管理区域
//		SysOrg sysOrg = getLoginUserOrg();
//		SysOrg sysOrg_params = new SysOrg();
//		// 水厂用户
//		if ("3".equals(sysOrg.getOrgType())) {
//			sysOrg_params.setOrgId(sysOrg.getOrgId());
//		} else {
//			sysOrg_params.setParentId(companyId);
//		}
//		sysOrg_params.setOrgType("3");
//		List<SysOrg> orgList = orgService.findList(sysOrg_params);
//		for (SysOrg item : orgList) {
//			// 水量数据
//			TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
//			todayWaterYieldInfo.setEffectIcon("1");
//			todayWaterYieldInfo.setFillDate(date);
//			todayWaterYieldInfo.setFactoryId(item.getOrgId());
//			Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getSum(todayWaterYieldInfo);
//
//			Object totalIn_obj_1 = sumWaterYield.get("totalTodayIn");
//			Object totalIn_obj_2 = sumWaterYield.get("totalTodayOut");
//			Double temp_a = new Double(0);
//			Double temp_b = new Double(0);
//			if (totalIn_obj_1 != null) {
//				temp_a = (double)( Math.round(Double.parseDouble(sumWaterYield.get("totalTodayIn").toString()) * 100)/ 100 );
//				
//			}
//
//			if (totalIn_obj_2 != null) {
//				temp_b = (double) (Math.round(Double.parseDouble(sumWaterYield.get("totalTodayOut").toString()) * 100)/ 100 );
//				
//			}
//
//			Map<String, Object> sumWaterYieldBefore = todayWaterYieldInfoService
//					.getSumByAppArea_company_Before(todayWaterYieldInfo); // 所选 择日以前的总量
//
//			Object totalIn_obj = sumWaterYieldBefore.get("totalTodayIn");
//			if (totalIn_obj != null) {
////				sumWaterYield.put("totalIn", (double) Math.round(
////						((Double.parseDouble(sumWaterYieldBefore.get("totalTodayIn").toString()) * 10000 + temp_a)
////								/ 10000) * 100)
////						/ 100);
//				   Double  d1=Double.parseDouble(sumWaterYieldBefore.get("totalTodayIn").toString());
//				   Double  d2=new Double(10000);
//				   Double  t1=   InitsUtils.mul(d1, d2);
//				   sumWaterYield.put("totalIn", InitsUtils.div( InitsUtils.add(t1, temp_a), d2, 2) ) ;
//
//			}
//			Object totalOut_obj = sumWaterYieldBefore.get("totalTodayOut");
//			if (totalOut_obj != null) {
//				sumWaterYield.put("totalOut", (double) Math.round(
//						((Double.parseDouble(sumWaterYieldBefore.get("totalTodayOut").toString()) * 10000 + temp_b)
//								/ 10000) * 100)
//						/ 100);
//
//			}
//
//			item.getParams().put("sumWaterYield", sumWaterYield);
//		}
//		ajaxResult = AjaxResult.success(orgList);
//		return ajaxResult;
//	}

	/**
	 * 区域电耗分析数据
	 */
	@RequestMapping("/electricQuantityAreaData")
	@ResponseBody
	public AjaxResult electricQuantityAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 电耗数据
				TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
				todayElectricityInfo.setEffectIcon("1");
				todayElectricityInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					todayElectricityInfo.setAreaId(item.getAreaId());
				}
				Map<String, Object> sumElectricity = todayElectricityInfoService.getSum(todayElectricityInfo);

				Double tempElectricity1 = new Double(0);
				Double tempElectricity2 = new Double(0);
				if (sumElectricity.get("totalTodayElectricity") != null) {
					tempElectricity1 = Double.parseDouble(sumElectricity.get("totalTodayElectricity").toString());
					sumElectricity.put("totalTodayElectricity",InitsUtils.electricityInits(tempElectricity1));
				}

				if (sumElectricity.get("totalPumpTodayEletricity") != null) {
					tempElectricity2 = Double.parseDouble(sumElectricity.get("totalPumpTodayEletricity").toString());
					sumElectricity.put("totalPumpTodayEletricity",InitsUtils.electricityInits(tempElectricity2));
				}

				Map<String, Object> sumByAppElectricity = todayElectricityInfoService
						.getSumByAppBefore(todayElectricityInfo); // 日前累计的电量

				if (sumByAppElectricity.get("totalBeforeTodayElectricity") != null) {
					//sumElectricity.put("totalTotalElectricity",
					//		Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString())
					//				+ tempElectricity1);
					sumElectricity.put("totalTotalElectricity",InitsUtils.electricityInits(InitsUtils.add(Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString()), tempElectricity1)));
				}

				if (sumByAppElectricity.get("totalPumpBeforeTodayEletricity") != null) {
//					sumElectricity.put("totalPumpTotalEletricity",
//							Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString())
//									+ tempElectricity2);
					sumElectricity.put("totalPumpTotalEletricity",InitsUtils.electricityInits(InitsUtils.add(Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString()), tempElectricity2)));

				}

				item.getParams().put("sumElectricity", sumElectricity);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	/**
	 * 公司电耗分析数据
	 */
	@RequestMapping("/electricQuantityCompanyData")
	@ResponseBody
	public AjaxResult electricQuantityCompanyData(String areaId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getParentId());
		} else {
			sysOrg_params.setAreaId(areaId);
		}
		sysOrg_params.setOrgType("2");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 电耗数据
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			todayElectricityInfo.setFillDate(date);
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
			} else {
				todayElectricityInfo.getParams().put("companyId", item.getOrgId());
			}
			Map<String, Object> sumElectricity = todayElectricityInfoService.getSum(todayElectricityInfo);

			Double tempElectricity1 = new Double(0);
			Double tempElectricity2 = new Double(0);
			if (sumElectricity.get("totalTodayElectricity") != null) {
				tempElectricity1 = Double.parseDouble(sumElectricity.get("totalTodayElectricity").toString());

			}

			if (sumElectricity.get("totalPumpTodayEletricity") != null) {
				tempElectricity2 = Double.parseDouble(sumElectricity.get("totalPumpTodayEletricity").toString());

			}

			Map<String, Object> sumByAppElectricity = todayElectricityInfoService
					.getSumByAppBefore(todayElectricityInfo); // 日前累计的电量

			if (sumByAppElectricity.get("totalBeforeTodayElectricity") != null) {
				sumElectricity.put("totalTotalElectricity",
						Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString())
								+ tempElectricity1);
			}

			if (sumByAppElectricity.get("totalPumpBeforeTodayEletricity") != null) {
				sumElectricity.put("totalPumpTotalEletricity",
						Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString())
								+ tempElectricity2);

			}

			item.getParams().put("sumElectricity", sumElectricity);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

  /**
   *   电量区域分析列表中,点击区域后的水厂分析
   */
		
	@RequestMapping("/electricQuantityFactoryData")
	@ResponseBody
	public AjaxResult electricQuantityFactoryData(String areaId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
	//	if ("3".equals(sysOrg.getOrgType())) {
		//	sysOrg_params.setOrgId(sysOrg.getParentId());
		//} else {
		    sysOrg_params.setAreaId(areaId); //水厂领导手机端查询是自身属于区域下的所有工厂
			
		//}d
		//sysOrg_params.setOrgType("2");
		List<SysOrg> orgList = orgService.findListByFactory(sysOrg_params);
		

		// 手机端水厂领导进入查看范围		
		if ("3".equals(sysOrg.getOrgType())){
			for (SysOrg orgEntity : orgList) {
				    if (sysOrg.getOrgId().equals(orgEntity.getOrgId())){
				    	List<SysOrg> orgList2= new ArrayList<SysOrg>();
				    	orgList2.add(orgEntity);
				    	orgList=orgList2;
				    	break;
				    }				
			}
		}
		
		for (SysOrg item : orgList) {
			// 电耗数据
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			todayElectricityInfo.setFillDate(date);
			// 水厂用户
			//if ("3".equals(sysOrg.getOrgType())) {
				todayElectricityInfo.setFactoryId(item.getOrgId());
			//} else {
			//	todayElectricityInfo.getParams().put("companyId", item.getOrgId());
			//}
			Map<String, Object> sumElectricity = todayElectricityInfoService.getSum(todayElectricityInfo);

			Double tempElectricity1 = new Double(0);
			Double tempElectricity2 = new Double(0);
			if (sumElectricity.get("totalTodayElectricity") != null) {
				tempElectricity1 = Double.parseDouble(sumElectricity.get("totalTodayElectricity").toString());
				sumElectricity.put("totalTodayElectricity",InitsUtils.electricityInits(tempElectricity1));
			}

			if (sumElectricity.get("totalPumpTodayEletricity") != null) {
				tempElectricity2 = Double.parseDouble(sumElectricity.get("totalPumpTodayEletricity").toString());
				sumElectricity.put("totalPumpTodayEletricity",InitsUtils.electricityInits(tempElectricity2));
			}

			Map<String, Object> sumByAppElectricity = todayElectricityInfoService
					.getSumByAppBefore(todayElectricityInfo); // 日前累计的电量

			if (sumByAppElectricity.get("totalBeforeTodayElectricity") != null) {
//				sumElectricity.put("totalTotalElectricity",
//						Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString())
//								+ tempElectricity1);
				sumElectricity.put("totalTotalElectricity",InitsUtils.electricityInits(InitsUtils.add(Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString()), tempElectricity1)));
			}

			if (sumByAppElectricity.get("totalPumpBeforeTodayEletricity") != null) {
//				sumElectricity.put("totalPumpTotalEletricity",
//						Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString())
//								+ tempElectricity2);
				sumElectricity.put("totalPumpTotalEletricity",InitsUtils.electricityInits(InitsUtils.add(Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString()), tempElectricity2)));

			}

			item.getParams().put("sumElectricity", sumElectricity);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}
	
	
	/**
	 * 公司电耗分析数据 (地图一级页面)
	 */
	@RequestMapping("/electricQuantityCompanysData")
	@ResponseBody
	public AjaxResult electricQuantityCompanysData(String areaIds, String date) {
		AjaxResult ajaxResult;

		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIdsByUser = sysUser.getAreaStr();

		String[] temp1=   areaIds.split(",");
	
		String tempResult="";
		
		for (int i=0;i<temp1.length;i++){
			
		     if(areaIdsByUser.indexOf(temp1[i])!=-1){
		    	 tempResult=tempResult+temp1[i]+",";
		     }
		}
		if (!"".equals(tempResult)){
			
			areaIds=tempResult.substring(0, tempResult.length()-1);
		}

		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 电耗数据
				TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
				todayElectricityInfo.setEffectIcon("1");
				todayElectricityInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					todayElectricityInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					todayElectricityInfo.setAreaId(item.getAreaId());
				}
				Map<String, Object> sumElectricity = todayElectricityInfoService.getSum(todayElectricityInfo);

				Double tempElectricity1 = new Double(0);
				Double tempElectricity2 = new Double(0);
				if (sumElectricity.get("totalTodayElectricity") != null) {
					tempElectricity1 = Double.parseDouble(sumElectricity.get("totalTodayElectricity").toString());
					sumElectricity.put("totalTodayElectricity",InitsUtils.electricityInits(tempElectricity1));
				}

				if (sumElectricity.get("totalPumpTodayEletricity") != null) {
					tempElectricity2 = Double.parseDouble(sumElectricity.get("totalPumpTodayEletricity").toString());
					sumElectricity.put("totalPumpTodayEletricity",InitsUtils.electricityInits(tempElectricity2));
				}

				Map<String, Object> sumByAppElectricity = todayElectricityInfoService
						.getSumByAppBefore(todayElectricityInfo); // 日前累计的电量

				if (sumByAppElectricity.get("totalBeforeTodayElectricity") != null) {
//					sumElectricity.put("totalTotalElectricity",
//							Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString())
//									+ tempElectricity1);
					sumElectricity.put("totalTotalElectricity",InitsUtils.electricityInits(InitsUtils.add(Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString()), tempElectricity1)));
				}

				if (sumByAppElectricity.get("totalPumpBeforeTodayEletricity") != null) {
//					sumElectricity.put("totalPumpTotalEletricity",
//							Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString())
//									+ tempElectricity2);
					sumElectricity.put("totalPumpTotalEletricity",InitsUtils.electricityInits(InitsUtils.add(Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString()),tempElectricity2)));
				}

				item.getParams().put("sumElectricity", sumElectricity);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
			
		return ajaxResult;
	}

	/**
	 * 水厂电耗分析数据
	 */
	@RequestMapping("/electricQuantityWaterWorksData")
	@ResponseBody
	public AjaxResult electricQuantityWaterWorksData(String companyId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getOrgId());
		} else {
			sysOrg_params.setParentId(companyId);
		}
		sysOrg_params.setOrgType("3");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 电耗数据
			TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
			todayElectricityInfo.setEffectIcon("1");
			todayElectricityInfo.setFillDate(date);
			todayElectricityInfo.setFactoryId(item.getOrgId());
			Map<String, Object> sumElectricity = todayElectricityInfoService.getSum(todayElectricityInfo);

			Double tempElectricity1 = new Double(0);
			Double tempElectricity2 = new Double(0);
			if (sumElectricity.get("totalTodayElectricity") != null) {
				tempElectricity1 = Double.parseDouble(sumElectricity.get("totalTodayElectricity").toString());

			}

			if (sumElectricity.get("totalPumpTodayEletricity") != null) {
				tempElectricity2 = Double.parseDouble(sumElectricity.get("totalPumpTodayEletricity").toString());

			}

			Map<String, Object> sumByAppElectricity = todayElectricityInfoService
					.getSumByAppBefore(todayElectricityInfo); // 所选日前累计的电量

			if (sumByAppElectricity.get("totalBeforeTodayElectricity") != null) {
				sumElectricity.put("totalTotalElectricity",
						Double.parseDouble(sumByAppElectricity.get("totalBeforeTodayElectricity").toString())
								+ tempElectricity1);
			}

			if (sumByAppElectricity.get("totalPumpBeforeTodayEletricity") != null) {
				sumElectricity.put("totalPumpTotalEletricity",
						Double.parseDouble(sumByAppElectricity.get("totalPumpBeforeTodayEletricity").toString())
								+ tempElectricity2);
			}

			item.getParams().put("sumElectricity", sumElectricity);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 区域药耗分析数据
	 */
	@RequestMapping("/medicationAreaData")
	@ResponseBody
	public AjaxResult medicationAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 药耗数据
				TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
				todayMedicineInfo.setEffectIcon("1");
				todayMedicineInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					todayMedicineInfo.setAreaId(item.getAreaId());
				}
				Map<String, Object> sumMedicine = todayMedicineInfoService.getSum(todayMedicineInfo);

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
					sumMedicine.put("sumTodayPAC",InitsUtils.medicineInits(tempMedicine1));
				}
				if (sumMedicine.get("sumTodayPAMYin") != null) {
					tempMedicine2 = Double.parseDouble(sumMedicine.get("sumTodayPAMYin").toString());
					sumMedicine.put("sumTodayPAMYin",InitsUtils.medicineInits(tempMedicine2));
				}
				if (sumMedicine.get("sumTodayPAMYang") != null) {
					tempMedicine3 = Double.parseDouble(sumMedicine.get("sumTodayPAMYang").toString());
					sumMedicine.put("sumTodayPAMYang",InitsUtils.medicineInits(tempMedicine3));
				}
				if (sumMedicine.get("sumTodayPhosphorus") != null) {
					tempMedicine4 = Double.parseDouble(sumMedicine.get("sumTodayPhosphorus").toString());
					sumMedicine.put("sumTodayPhosphorus",InitsUtils.medicineInits(tempMedicine4));
				}
				if (sumMedicine.get("sumTodayNaCLO") != null) {
					tempMedicine5 = Double.parseDouble(sumMedicine.get("sumTodayNaCLO").toString());
					sumMedicine.put("sumTodayNaCLO",InitsUtils.medicineInits(tempMedicine5));
				}
				if (sumMedicine.get("sumTodayLime") != null) {
					tempMedicine6 = Double.parseDouble(sumMedicine.get("sumTodayLime").toString());
					sumMedicine.put("sumTodayLime",InitsUtils.medicineInits(tempMedicine6));
				}
				if (sumMedicine.get("sumTodayGlucose") != null) {
					tempMedicine7 = Double.parseDouble(sumMedicine.get("sumTodayGlucose").toString());
					sumMedicine.put("sumTodayGlucose",InitsUtils.medicineInits(tempMedicine7));
				}
				if (sumMedicine.get("sumTodaySC") != null) {
					tempMedicine8 = Double.parseDouble(sumMedicine.get("sumTodaySC").toString());
					sumMedicine.put("sumTodaySC",InitsUtils.medicineInits(tempMedicine8));
				}
				if (sumMedicine.get("sumTodaySA") != null) {
					tempMedicine9 = Double.parseDouble(sumMedicine.get("sumTodaySA").toString());
					sumMedicine.put("sumTodaySA",InitsUtils.medicineInits(tempMedicine9));
				}
				if (sumMedicine.get("sumTodayHCL") != null) {
					tempMedicine10 = Double.parseDouble(sumMedicine.get("sumTodayHCL").toString());
					sumMedicine.put("sumTodayHCL",InitsUtils.medicineInits(tempMedicine10));
				}

				Map<String, Object> sumByMedicine = todayMedicineInfoService.getSumByAppBefore(todayMedicineInfo); // 当日前累计的药量

				if (sumByMedicine.get("sumTodayPAC") != null) {
//					sumMedicine.put("sumTotalPAC",
//							Double.parseDouble(sumByMedicine.get("sumTodayPAC").toString()) + tempMedicine1);
					sumMedicine.put("sumTotalPAC",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAC").toString()), tempMedicine1)));
				}

				if (sumByMedicine.get("sumTodayPAMYin") != null) {
//					sumMedicine.put("sumTotalPAMYin",
//							Double.parseDouble(sumByMedicine.get("sumTodayPAMYin").toString()) + tempMedicine2);
					sumMedicine.put("sumTotalPAMYin",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYin").toString()), tempMedicine2)));
				}

				if (sumByMedicine.get("sumTodayPAMYang") != null) {
//					sumMedicine.put("sumTotalPAMYang",
//							Double.parseDouble(sumByMedicine.get("sumTodayPAMYang").toString()) + tempMedicine3);
					sumMedicine.put("sumTotalPAMYang",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYang").toString()), tempMedicine3)));
				}
				if (sumByMedicine.get("sumTodayPhosphorus") != null) {
//					sumMedicine.put("sumTotalPhosphorus",
//							Double.parseDouble(sumByMedicine.get("sumTodayPhosphorus").toString()) + tempMedicine4);
					sumMedicine.put("sumTotalPhosphorus",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPhosphorus").toString()), tempMedicine4)));
				}
				if (sumByMedicine.get("sumTodayNaCLO") != null) {
//					sumMedicine.put("sumTotalNaCLO",
//							Double.parseDouble(sumByMedicine.get("sumTodayNaCLO").toString()) + tempMedicine5);
					sumMedicine.put("sumTotalNaCLO",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayNaCLO").toString()), tempMedicine5)));
				}
				if (sumByMedicine.get("sumTodayLime") != null) {
//					sumMedicine.put("sumTotalLime",
//							Double.parseDouble(sumByMedicine.get("sumTodayLime").toString()) + tempMedicine6);
					sumMedicine.put("sumTotalLime",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayLime").toString()), tempMedicine6)));
				}
				if (sumByMedicine.get("sumTodayGlucose") != null) {
//					sumMedicine.put("sumTotalGlucose",
//							Double.parseDouble(sumByMedicine.get("sumTodayGlucose").toString()) + tempMedicine7);
					sumMedicine.put("sumTotalGlucose",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayGlucose").toString()), tempMedicine7)));
				}
				if (sumByMedicine.get("sumTodaySC") != null) {
//					sumMedicine.put("sumTotalSC",
//							Double.parseDouble(sumByMedicine.get("sumTodaySC").toString()) + tempMedicine8);
					sumMedicine.put("sumTotalSC",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySC").toString()), tempMedicine8)));
				}
				if (sumByMedicine.get("sumTodaySA") != null) {
//					sumMedicine.put("sumTotalSA",
//							Double.parseDouble(sumByMedicine.get("sumTodaySA").toString()) + tempMedicine9);
					sumMedicine.put("sumTotalSA",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySA").toString()), tempMedicine9)));
				}
				if (sumByMedicine.get("sumTodayHCL") != null) {
//					sumMedicine.put("sumTotalHCL",
//							Double.parseDouble(sumByMedicine.get("sumTodayHCL").toString()) + tempMedicine10);
					sumMedicine.put("sumTotalHCL",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayHCL").toString()), tempMedicine10)));
				}

				item.getParams().put("sumMedicine", sumMedicine);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	/**
	 * 公司药耗分析数据(手机端水厂级页面）
	 */
	@RequestMapping("/medicationCompanyData")
	@ResponseBody
	public AjaxResult medicationCompanyData(String areaId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		
		sysOrg_params.setAreaId(areaId);
		
		List<SysOrg> orgList = orgService.findListByFactory(sysOrg_params);
	
		// 手机端水厂领导进入查看范围		
		if ("3".equals(sysOrg.getOrgType())){
			for (SysOrg orgEntity : orgList) {
				    if (sysOrg.getOrgId().equals(orgEntity.getOrgId())){
				    	List<SysOrg> orgList2= new ArrayList<SysOrg>();
				    	orgList2.add(orgEntity);
				    	orgList=orgList2;
				    	break;
				    }				
			}
		}
		for (SysOrg item : orgList) {
			// 药耗数据
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			todayMedicineInfo.setFillDate(date);
			todayMedicineInfo.setFactoryId(item.getOrgId());
			
			
				Map<String, Object> sumMedicine = todayMedicineInfoService.getSum(todayMedicineInfo);
				
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
					sumMedicine.put("sumTodayPAC",InitsUtils.medicineInits(tempMedicine1));
				}
				if (sumMedicine.get("sumTodayPAMYin") != null) {
					tempMedicine2 = Double.parseDouble(sumMedicine.get("sumTodayPAMYin").toString());
					sumMedicine.put("sumTodayPAMYin",InitsUtils.medicineInits(tempMedicine2));
				}
				if (sumMedicine.get("sumTodayPAMYang") != null) {
					tempMedicine3 = Double.parseDouble(sumMedicine.get("sumTodayPAMYang").toString());
					sumMedicine.put("sumTodayPAMYang",InitsUtils.medicineInits(tempMedicine3));
				}
				if (sumMedicine.get("sumTodayPhosphorus") != null) {
					tempMedicine4 = Double.parseDouble(sumMedicine.get("sumTodayPhosphorus").toString());
					sumMedicine.put("sumTodayPhosphorus",InitsUtils.medicineInits(tempMedicine4));
				}
				if (sumMedicine.get("sumTodayNaCLO") != null) {
					tempMedicine5 = Double.parseDouble(sumMedicine.get("sumTodayNaCLO").toString());
					sumMedicine.put("sumTodayNaCLO",InitsUtils.medicineInits(tempMedicine5));
				}
				if (sumMedicine.get("sumTodayLime") != null) {
					tempMedicine6 = Double.parseDouble(sumMedicine.get("sumTodayLime").toString());
					sumMedicine.put("sumTodayLime",InitsUtils.medicineInits(tempMedicine6));
				}
				if (sumMedicine.get("sumTodayGlucose") != null) {
					tempMedicine7 = Double.parseDouble(sumMedicine.get("sumTodayGlucose").toString());
					sumMedicine.put("sumTodayGlucose",InitsUtils.medicineInits(tempMedicine7));
				}
				if (sumMedicine.get("sumTodaySC") != null) {
					tempMedicine8 = Double.parseDouble(sumMedicine.get("sumTodaySC").toString());
					sumMedicine.put("sumTodaySC",InitsUtils.medicineInits(tempMedicine8));
				}
				if (sumMedicine.get("sumTodaySA") != null) {
					tempMedicine9 = Double.parseDouble(sumMedicine.get("sumTodaySA").toString());
					sumMedicine.put("sumTodaySA",InitsUtils.medicineInits(tempMedicine9));
				}
				if (sumMedicine.get("sumTodayHCL") != null) {
					tempMedicine10 = Double.parseDouble(sumMedicine.get("sumTodayHCL").toString());
					sumMedicine.put("sumTodayHCL",InitsUtils.medicineInits(tempMedicine10));
				}

				Map<String, Object> sumByMedicine = todayMedicineInfoService.getSumByAppBefore(todayMedicineInfo); // 当日前累计的药量

				if (sumByMedicine.get("sumTodayPAC") != null) {
//					sumMedicine.put("sumTotalPAC",
//							Double.parseDouble(sumByMedicine.get("sumTodayPAC").toString()) + tempMedicine1);
					sumMedicine.put("sumTotalPAC",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAC").toString()), tempMedicine1)));
				}

				if (sumByMedicine.get("sumTodayPAMYin") != null) {
//					sumMedicine.put("sumTotalPAMYin",
//							Double.parseDouble(sumByMedicine.get("sumTodayPAMYin").toString()) + tempMedicine2);
					sumMedicine.put("sumTotalPAMYin",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYin").toString()), tempMedicine2)));
				}

				if (sumByMedicine.get("sumTodayPAMYang") != null) {
//					sumMedicine.put("sumTotalPAMYang",
//							Double.parseDouble(sumByMedicine.get("sumTodayPAMYang").toString()) + tempMedicine3);
					sumMedicine.put("sumTotalPAMYang",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYang").toString()), tempMedicine3)));
				}
				if (sumByMedicine.get("sumTodayPhosphorus") != null) {
//					sumMedicine.put("sumTotalPhosphorus",
//							Double.parseDouble(sumByMedicine.get("sumTodayPhosphorus").toString()) + tempMedicine4);
					sumMedicine.put("sumTotalPhosphorus",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPhosphorus").toString()), tempMedicine4)));
				}
				if (sumByMedicine.get("sumTodayNaCLO") != null) {
//					sumMedicine.put("sumTotalNaCLO",
//							Double.parseDouble(sumByMedicine.get("sumTodayNaCLO").toString()) + tempMedicine5);
					sumMedicine.put("sumTotalNaCLO",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayNaCLO").toString()), tempMedicine5)));
				}
				if (sumByMedicine.get("sumTodayLime") != null) {
//					sumMedicine.put("sumTotalLime",
//							Double.parseDouble(sumByMedicine.get("sumTodayLime").toString()) + tempMedicine6);
					sumMedicine.put("sumTotalLime",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayLime").toString()), tempMedicine6)));
				}
				
				if (sumByMedicine.get("sumTodayGlucose") != null) {
//					sumMedicine.put("sumTotalGlucose",
//							Double.parseDouble(sumByMedicine.get("sumTodayGlucose").toString()) + tempMedicine7);
					sumMedicine.put("sumTotalGlucose",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayGlucose").toString()), tempMedicine7)));
				}
				if (sumByMedicine.get("sumTodaySC") != null) {
//					sumMedicine.put("sumTotalSC",
//							Double.parseDouble(sumByMedicine.get("sumTodaySC").toString()) + tempMedicine8);
					sumMedicine.put("sumTotalSC",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySC").toString()), tempMedicine8)));
				}
				if (sumByMedicine.get("sumTodaySA") != null) {
//					sumMedicine.put("sumTotalSA",
//							Double.parseDouble(sumByMedicine.get("sumTodaySA").toString()) + tempMedicine9);
					sumMedicine.put("sumTotalSA",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySA").toString()), tempMedicine9)));
				}
				if (sumByMedicine.get("sumTodayHCL") != null) {
//					sumMedicine.put("sumTotalHCL",
//							Double.parseDouble(sumByMedicine.get("sumTodayHCL").toString()) + tempMedicine10);
					sumMedicine.put("sumTotalHCL",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayHCL").toString()), tempMedicine10)));
				}
		
				item.getParams().put("sumMedicine", sumMedicine);

			}
			
		
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
		

	}

	/**
	 * 公司药耗分析数据(地图一级页面)
	 */
	@RequestMapping("/medicationCompanysData")
	@ResponseBody
	public AjaxResult medicationCompanysData(String areaIds, String date) {

		
		AjaxResult ajaxResult;

		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIdsByUser = sysUser.getAreaStr();

		String[] temp1=   areaIds.split(",");
	
		String tempResult="";
		
		for (int i=0;i<temp1.length;i++){
			
		     if(areaIdsByUser.indexOf(temp1[i])!=-1){
		    	 tempResult=tempResult+temp1[i]+",";
		     }
		}
		if (!"".equals(tempResult)){
			
			areaIds=tempResult.substring(0, tempResult.length()-1);
		}
		
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 药耗数据
				TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
				todayMedicineInfo.setEffectIcon("1");
				todayMedicineInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					todayMedicineInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					todayMedicineInfo.setAreaId(item.getAreaId());
				}
				Map<String, Object> sumMedicine = todayMedicineInfoService.getSum(todayMedicineInfo);

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
					sumMedicine.put("sumTodayPAC",InitsUtils.medicineInits(tempMedicine1));
				}
				if (sumMedicine.get("sumTodayPAMYin") != null) {
					tempMedicine2 = Double.parseDouble(sumMedicine.get("sumTodayPAMYin").toString());
					sumMedicine.put("sumTodayPAMYin",InitsUtils.medicineInits(tempMedicine2));
				}
				if (sumMedicine.get("sumTodayPAMYang") != null) {
					tempMedicine3 = Double.parseDouble(sumMedicine.get("sumTodayPAMYang").toString());
					sumMedicine.put("sumTodayPAMYang",InitsUtils.medicineInits(tempMedicine3));
				}
				if (sumMedicine.get("sumTodayPhosphorus") != null) {
					tempMedicine4 = Double.parseDouble(sumMedicine.get("sumTodayPhosphorus").toString());
					sumMedicine.put("sumTodayPhosphorus",InitsUtils.medicineInits(tempMedicine4));
				}
				if (sumMedicine.get("sumTodayNaCLO") != null) {
					tempMedicine5 = Double.parseDouble(sumMedicine.get("sumTodayNaCLO").toString());
					sumMedicine.put("sumTodayNaCLO",InitsUtils.medicineInits(tempMedicine5));
				}
				if (sumMedicine.get("sumTodayLime") != null) {
					tempMedicine6 = Double.parseDouble(sumMedicine.get("sumTodayLime").toString());
					sumMedicine.put("sumTodayLime",InitsUtils.medicineInits(tempMedicine6));
				}
				if (sumMedicine.get("sumTodayGlucose") != null) {
					tempMedicine7 = Double.parseDouble(sumMedicine.get("sumTodayGlucose").toString());
					sumMedicine.put("sumTodayGlucose",InitsUtils.medicineInits(tempMedicine7));
				}
				if (sumMedicine.get("sumTodaySC") != null) {
					tempMedicine8 = Double.parseDouble(sumMedicine.get("sumTodaySC").toString());
					sumMedicine.put("sumTodaySC",InitsUtils.medicineInits(tempMedicine8));
				}
				if (sumMedicine.get("sumTodaySA") != null) {
					tempMedicine9 = Double.parseDouble(sumMedicine.get("sumTodaySA").toString());
					sumMedicine.put("sumTodaySA",InitsUtils.medicineInits(tempMedicine9));
				}
				if (sumMedicine.get("sumTodayHCL") != null) {
					tempMedicine10 = Double.parseDouble(sumMedicine.get("sumTodayHCL").toString());
					sumMedicine.put("sumTodayHCL",InitsUtils.medicineInits(tempMedicine10));
				}
				
				Map<String, Object> sumByMedicine = todayMedicineInfoService.getSumByAppBefore(todayMedicineInfo); // 当日前累计的药量


				if (sumByMedicine.get("sumTodayPAC") != null) {

					sumMedicine.put("sumTotalPAC",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAC").toString()), tempMedicine1)));
				}

				if (sumByMedicine.get("sumTodayPAMYin") != null) {

					sumMedicine.put("sumTotalPAMYin",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYin").toString()), tempMedicine2)));
				}

				if (sumByMedicine.get("sumTodayPAMYang") != null) {

					sumMedicine.put("sumTotalPAMYang",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPAMYang").toString()), tempMedicine3)));
				}
				if (sumByMedicine.get("sumTodayPhosphorus") != null) {

					sumMedicine.put("sumTotalPhosphorus",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayPhosphorus").toString()), tempMedicine4)));
				}
				if (sumByMedicine.get("sumTodayNaCLO") != null) {

					sumMedicine.put("sumTotalNaCLO",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayNaCLO").toString()), tempMedicine5)));
				}
				if (sumByMedicine.get("sumTodayLime") != null) {

					sumMedicine.put("sumTotalLime",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayLime").toString()), tempMedicine6)));
				}
				
				if (sumByMedicine.get("sumTodayGlucose") != null) {

					sumMedicine.put("sumTotalGlucose",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayGlucose").toString()), tempMedicine7)));
				}
				if (sumByMedicine.get("sumTodaySC") != null) {

					sumMedicine.put("sumTotalSC",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySC").toString()), tempMedicine8)));
				}
				if (sumByMedicine.get("sumTodaySA") != null) {

					sumMedicine.put("sumTotalSA",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodaySA").toString()), tempMedicine9)));
				}
				if (sumByMedicine.get("sumTodayHCL") != null) {

					sumMedicine.put("sumTotalHCL",InitsUtils.medicineInits(InitsUtils.add(Double.parseDouble(sumByMedicine.get("sumTodayHCL").toString()), tempMedicine10)));
				}

				item.getParams().put("sumMedicine", sumMedicine);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	/**
	 * 水厂药耗分析数据
	 */
	@RequestMapping("/medicationWaterWorksData")
	@ResponseBody
	public AjaxResult medicationWaterWorksData(String companyId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getOrgId());
		} else {
			sysOrg_params.setParentId(companyId);
		}
		sysOrg_params.setOrgType("3");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 药耗数据
			TodayMedicineInfo todayMedicineInfo = new TodayMedicineInfo();
			todayMedicineInfo.setEffectIcon("1");
			todayMedicineInfo.setFillDate(date);
			todayMedicineInfo.setFactoryId(item.getOrgId());
			Map<String, Object> sumMedicine = todayMedicineInfoService.getSum(todayMedicineInfo);
			item.getParams().put("sumMedicine", sumMedicine);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 区域水质分析数据
	 */
	@RequestMapping("/waterQualityAreaData")
	@ResponseBody
	public AjaxResult waterQualityAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			SysOrg sysOrg_params = new SysOrg();
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				sysOrg_params.setOrgId(sysOrg.getOrgId());
			} else {
				sysOrg_params.getParams().put("areaIds", areaIds);
			}
			sysOrg_params.setOrgType("3");
			List<SysOrg> orgList = orgService.findList(sysOrg_params);
			for (SysOrg item : orgList) {
				// 污水厂/排水厂
				if ("1".equals(item.getFactoryType())) {
					// 水质数据
					BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
					badWaterQualityInfo.setEffectIcon("1");
					badWaterQualityInfo.setFillDate(date);
					badWaterQualityInfo.setFactoryId(item.getOrgId());
					badWaterQualityInfo = badWaterQualityInfoService.getEntity(badWaterQualityInfo);
					item.getParams().put("badWaterQualityInfo", badWaterQualityInfo);
				}
				// 自来水厂/给水厂
				else if ("2".equals(item.getFactoryType())) {
					// 水质数据
					GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
					goodWaterHealthInfo.setEffectIcon("1");
					goodWaterHealthInfo.setFillDate(date);
					goodWaterHealthInfo.setFactoryId(item.getOrgId());
					goodWaterHealthInfo = goodWaterHealthInfoService.getEntity(goodWaterHealthInfo);
					item.getParams().put("goodWaterHealthInfo", goodWaterHealthInfo);
				}
			}
			ajaxResult = AjaxResult.success(orgList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
	/**
	 * 化验数据污水和自来水厂综合展示
	 * @param date
	 * @return
	 */
	
	@RequestMapping("/testBadGoodWaterAreaData")
	@ResponseBody
	public AjaxResult testBadGoodWaterAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			SysOrg sysOrg_params = new SysOrg();
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				sysOrg_params.setOrgId(sysOrg.getOrgId());
			} else {
				sysOrg_params.getParams().put("areaIds", areaIds);
			}
			sysOrg_params.setOrgType("3");
			List<SysOrg> orgList = orgService.findList(sysOrg_params);
			for (SysOrg item : orgList) {
				// 污水厂/排水厂
				if ("1".equals(item.getFactoryType())) {
					// 水质数据
					TestBadWaterInfo testBadWaterInfo = new TestBadWaterInfo();
					testBadWaterInfo.setEffectIcon("1");
					testBadWaterInfo.setFillDate(date);
					testBadWaterInfo.setFactoryId(item.getOrgId());
					testBadWaterInfo = testBadWaterInfoService.getEntity(testBadWaterInfo);
					item.getParams().put("testBadWaterInfo", testBadWaterInfo);
				}
				// 自来水厂/给水厂
				else if ("2".equals(item.getFactoryType())) {
					// 水质数据
					TestTapWaterInfo testTapWaterInfo = new TestTapWaterInfo();
					testTapWaterInfo.setEffectIcon("1");
					testTapWaterInfo.setFillDate(date);
					testTapWaterInfo.setFactoryId(item.getOrgId());
					testTapWaterInfo = testTapWaterInfoService.getEntity(testTapWaterInfo);
					item.getParams().put("testTapWaterInfo", testTapWaterInfo);
				}
			}
			ajaxResult = AjaxResult.success(orgList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}



	/**
	 * 公司水质分析数据
	 */
	@RequestMapping("/waterQualityCompanysData")
	@ResponseBody
	public AjaxResult waterQualityCompanysData(String areaIds, String date) {
		AjaxResult ajaxResult;
		SysOrg sysOrg_params = new SysOrg();
		// 当前用户所属机构
		SysOrg sysOrg = getLoginUserOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getOrgId());
		} else {
			sysOrg_params.getParams().put("areaIds", areaIds);
		}
		sysOrg_params.setOrgType("3");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 污水厂/排水厂
			if ("1".equals(item.getFactoryType())) {
				// 水质数据
				BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
				badWaterQualityInfo.setEffectIcon("1");
				badWaterQualityInfo.setFillDate(date);
				badWaterQualityInfo.setFactoryId(item.getOrgId());
				badWaterQualityInfo = badWaterQualityInfoService.getEntity(badWaterQualityInfo);
				item.getParams().put("badWaterQualityInfo", badWaterQualityInfo);
			}
			// 自来水厂/给水厂
			else if ("2".equals(item.getFactoryType())) {
				// 水质数据
				GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
				goodWaterHealthInfo.setEffectIcon("1");
				goodWaterHealthInfo.setFillDate(date);
				goodWaterHealthInfo.setFactoryId(item.getOrgId());
				goodWaterHealthInfo = goodWaterHealthInfoService.getEntity(goodWaterHealthInfo);
				item.getParams().put("goodWaterHealthInfo", goodWaterHealthInfo);
			}
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}
/**
 * 化验数据：地图查询
 * @param areaIds
 * @param date
 * @return
 */
	
	@RequestMapping("/testWaterPictureData")
	@ResponseBody
	public AjaxResult testWaterPictureData(String areaIds, String date) {
		AjaxResult ajaxResult;
		SysOrg sysOrg_params = new SysOrg();
		// 当前用户所属机构
		SysOrg sysOrg = getLoginUserOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getOrgId());
		} else {
			sysOrg_params.getParams().put("areaIds", areaIds);
		}
		sysOrg_params.setOrgType("3");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 污水厂/排水厂
			if ("1".equals(item.getFactoryType())) {
				// 水质数据
				TestBadWaterInfo testBadWaterInfo = new TestBadWaterInfo();
				testBadWaterInfo.setEffectIcon("1");
				testBadWaterInfo.setFillDate(date);
				testBadWaterInfo.setFactoryId(item.getOrgId());
				testBadWaterInfo = testBadWaterInfoService.getEntity(testBadWaterInfo);
				item.getParams().put("testBadWaterInfo", testBadWaterInfo);
			}
			// 自来水厂/给水厂
			else if ("2".equals(item.getFactoryType())) {
				// 水质数据
				TestTapWaterInfo testTapWaterInfo = new TestTapWaterInfo();
				testTapWaterInfo.setEffectIcon("1");
				testTapWaterInfo.setFillDate(date);
				testTapWaterInfo.setFactoryId(item.getOrgId());
				testTapWaterInfo = testTapWaterInfoService.getEntity(testTapWaterInfo);
				item.getParams().put("testTapWaterInfo", testTapWaterInfo);
			}
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}
	
	/**
	 * 水厂水质分析数据
	 */
	@RequestMapping("/waterQualityWaterWorksData")
	@ResponseBody
	public AjaxResult waterQualityWaterWorksData(String companyId, String date) {
		AjaxResult ajaxResult;
		SysOrg sysOrg_params = new SysOrg();
		// 当前用户所属机构
		SysOrg sysOrg = getLoginUserOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getOrgId());
		} else {
			sysOrg_params.setParentId(companyId);
		}
		sysOrg_params.setOrgType("3");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 污水厂/排水厂
			if ("1".equals(item.getFactoryType())) {
				// 水质数据
				BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
				badWaterQualityInfo.setEffectIcon("1");
				badWaterQualityInfo.setFillDate(date);
				badWaterQualityInfo.setFactoryId(item.getOrgId());
				badWaterQualityInfo = badWaterQualityInfoService.getEntity(badWaterQualityInfo);
				item.getParams().put("badWaterQualityInfo", badWaterQualityInfo);
			}
			// 自来水厂/给水厂
			else if ("2".equals(item.getFactoryType())) {
				// 水质数据
				GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
				goodWaterHealthInfo.setEffectIcon("1");
				goodWaterHealthInfo.setFillDate(date);
				goodWaterHealthInfo.setFactoryId(item.getOrgId());
				goodWaterHealthInfo = goodWaterHealthInfoService.getEntity(goodWaterHealthInfo);
				item.getParams().put("goodWaterHealthInfo", goodWaterHealthInfo);
			}
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 区域污泥分析数据
	 */
	@RequestMapping("/sludgeAreaData")
	@ResponseBody
	public AjaxResult sludgeAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 污泥数据
				MudInfo mudInfo = new MudInfo();
				mudInfo.setEffectIcon("1");
				mudInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					mudInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					mudInfo.setAreaId(item.getAreaId());
				}
				Map<String, Object> sumMud = mudInfoService.getSum(mudInfo); // 单日量

				Double tempMud1 = new Double(0);
				Double tempMud2 = new Double(0);
				if (sumMud.get("sumTodayMud") != null) {
					tempMud1 = Double.parseDouble(sumMud.get("sumTodayMud").toString());
					sumMud.put("sumTodayMud",InitsUtils.mudInits(tempMud1));
				}

				if (sumMud.get("sumDryMud") != null) {
					tempMud2 = Double.parseDouble(sumMud.get("sumDryMud").toString());
					sumMud.put("sumDryMud",InitsUtils.mudInits(tempMud2));
				}

				Map<String, Object> sumByAppMud = mudInfoService.getSumByAppArea(mudInfo); // 日前累计的量

				if (sumByAppMud.get("sumTodayMud") != null) {
					//sumMud.put("sumTotalMud", Double.parseDouble(sumByAppMud.get("sumTodayMud").toString()) + tempMud1);
					sumMud.put("sumTotalMud",InitsUtils.mudInits(InitsUtils.add(Double.parseDouble(sumByAppMud.get("sumTodayMud").toString()), tempMud1)));
				}

				if (sumByAppMud.get("sumDryMud") != null) {
					//sumMud.put("dryMudTotal", Double.parseDouble(sumByAppMud.get("sumDryMud").toString()) + tempMud2);
					sumMud.put("dryMudTotal", InitsUtils.mudInits(InitsUtils.add(Double.parseDouble(sumByAppMud.get("sumDryMud").toString()), tempMud2)));
				}

				item.getParams().put("sumMud", sumMud);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	/**
	 * 公司污泥分析数据 (水厂级页面数据)
	 */
	@RequestMapping("/sludgeCompanyData")
	@ResponseBody
	public AjaxResult sludgeCompanyData(String areaId, String date) {

		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();

		sysOrg_params.setAreaId(areaId);
		
		List<SysOrg> orgList = orgService.findListByFactory(sysOrg_params);
	
		// 手机端水厂领导进入查看范围		
		if ("3".equals(sysOrg.getOrgType())){
			for (SysOrg orgEntity : orgList) {
				    if (sysOrg.getOrgId().equals(orgEntity.getOrgId())){
				    	List<SysOrg> orgList2= new ArrayList<SysOrg>();
				    	orgList2.add(orgEntity);
				    	orgList=orgList2;
				    	break;
				    }				
			}
		}
		for (SysOrg item : orgList) {
			// 污泥数据
			MudInfo mudInfo = new MudInfo();
			mudInfo.setEffectIcon("1");
			mudInfo.setFillDate(date);
		
			mudInfo.setFactoryId(item.getOrgId());
	
			Map<String, Object> sumMud = mudInfoService.getSum(mudInfo); // 单日量

			Double tempMud1 = new Double(0);
			Double tempMud2 = new Double(0);
			if (sumMud.get("sumTodayMud") != null) {
				tempMud1 = Double.parseDouble(sumMud.get("sumTodayMud").toString());
				sumMud.put("sumTodayMud",InitsUtils.mudInits(tempMud1));
			}

			if (sumMud.get("sumDryMud") != null) {
				tempMud2 = Double.parseDouble(sumMud.get("sumDryMud").toString());
				sumMud.put("sumDryMud",InitsUtils.mudInits(tempMud2));
			}

			Map<String, Object> sumByAppMud = mudInfoService.getSumByAppArea(mudInfo); // 日前累计的量

			if (sumByAppMud.get("sumTodayMud") != null) {
				// sumMud.put("sumTotalMud", Double.parseDouble(sumByAppMud.get("sumTodayMud").toString()) + tempMud1);
				sumMud.put("sumTotalMud",InitsUtils.mudInits(InitsUtils.add(Double.parseDouble(sumByAppMud.get("sumTodayMud").toString()), tempMud1)));
			}

			if (sumByAppMud.get("sumDryMud") != null) {
				// sumMud.put("dryMudTotal", Double.parseDouble(sumByAppMud.get("sumDryMud").toString()) + tempMud2);
				sumMud.put("dryMudTotal", InitsUtils.mudInits(InitsUtils.add(Double.parseDouble(sumByAppMud.get("sumDryMud").toString()), tempMud2)));
			}

			item.getParams().put("sumMud", sumMud);
						
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}

	/**
	 * 公司污泥分析数据 (地图一级页面)
	 */
	@RequestMapping("/sludgeCompanysData")
	@ResponseBody
	public AjaxResult sludgeCompanysData(String areaIds, String date) {
		
		AjaxResult ajaxResult;

		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIdsByUser = sysUser.getAreaStr();

		String[] temp1=   areaIds.split(",");
	
		String tempResult="";
		
		for (int i=0;i<temp1.length;i++){
			
		     if(areaIdsByUser.indexOf(temp1[i])!=-1){
		    	 tempResult=tempResult+temp1[i]+",";
		     }
		}
		if (!"".equals(tempResult)){
			
			areaIds=tempResult.substring(0, tempResult.length()-1);
		}
		
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();

			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				// 污泥数据
				MudInfo mudInfo = new MudInfo();
				mudInfo.setEffectIcon("1");
				mudInfo.setFillDate(date);
				// 水厂用户
				if ("3".equals(sysOrg.getOrgType())) {
					mudInfo.setFactoryId(sysOrg.getOrgId());
				} else {
					mudInfo.setAreaId(item.getAreaId());
				}
				Map<String, Object> sumMud = mudInfoService.getSum(mudInfo); // 单日量

				Double tempMud1 = new Double(0);
				Double tempMud2 = new Double(0);
				if (sumMud.get("sumTodayMud") != null) {
					tempMud1 = Double.parseDouble(sumMud.get("sumTodayMud").toString());
					sumMud.put("sumTodayMud",InitsUtils.mudInits(tempMud1));
				}

				if (sumMud.get("sumDryMud") != null) {
					tempMud2 = Double.parseDouble(sumMud.get("sumDryMud").toString());
					sumMud.put("sumDryMud",InitsUtils.mudInits(tempMud2));
				}

				Map<String, Object> sumByAppMud = mudInfoService.getSumByAppArea(mudInfo); // 日前累计的量

				if (sumByAppMud.get("sumTodayMud") != null) {
					// sumMud.put("sumTotalMud", Double.parseDouble(sumByAppMud.get("sumTodayMud").toString()) + tempMud1);
					sumMud.put("sumTotalMud",InitsUtils.mudInits(InitsUtils.add(Double.parseDouble(sumByAppMud.get("sumTodayMud").toString()), tempMud1)));
				}

				if (sumByAppMud.get("sumDryMud") != null) {
					//sumMud.put("dryMudTotal", Double.parseDouble(sumByAppMud.get("sumDryMud").toString()) + tempMud2);
					sumMud.put("dryMudTotal",InitsUtils.mudInits(InitsUtils.add(Double.parseDouble(sumByAppMud.get("sumDryMud").toString()), tempMud2)));
				}

				item.getParams().put("sumMud", sumMud);
			}
			ajaxResult = AjaxResult.success(areaList);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
		
	}

	/**
	 * 水厂污泥分析数据
	 */
	@RequestMapping("/sludgeWaterWorksData")
	@ResponseBody
	public AjaxResult sludgeWaterWorksData(String companyId, String date) {
		AjaxResult ajaxResult;
		// 当前用户管理区域
		SysOrg sysOrg = getLoginUserOrg();
		SysOrg sysOrg_params = new SysOrg();
		// 水厂用户
		if ("3".equals(sysOrg.getOrgType())) {
			sysOrg_params.setOrgId(sysOrg.getOrgId());
		} else {
			sysOrg_params.setParentId(companyId);
		}
		sysOrg_params.setOrgType("3");
		List<SysOrg> orgList = orgService.findList(sysOrg_params);
		for (SysOrg item : orgList) {
			// 污泥数据
			MudInfo mudInfo = new MudInfo();
			mudInfo.setEffectIcon("1");
			mudInfo.setFillDate(date);
			mudInfo.setFactoryId(item.getOrgId());
			Map<String, Object> sumMud = mudInfoService.getSum(mudInfo);
			item.getParams().put("sumMud", sumMud);
		}
		ajaxResult = AjaxResult.success(orgList);
		return ajaxResult;
	}
}
