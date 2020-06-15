package com.boot.web.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.text.Convert;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.materialControl.domain.CostInfo;
import com.boot.materialControl.service.CostInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysDictDataService;

@Controller
@RequestMapping("/app")
public class APPCostInfoController extends APPBaseController {

	private String prefix = "app/pages";
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysDictDataService dictDataService;
	@Autowired
	private CostInfoService costInfoService;

	@GetMapping("/costInfo")
	public String costInfo(ModelMap mMap) {
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
		// 查看是不是财务
		int isFinance = -1;
		if ("8".equals(sysUser.getSysRoles().get(0).getRoleKey()))
			isFinance = 0;
		mMap.put("isFinance", isFinance);

		return prefix + "/costInfo";
	}

	@RequestMapping("/costInfoData")
	@ResponseBody
	public AjaxResult costInfoData(String date) {
		SysUser sysUser = getLoginUserInfo();
		String roleKey = sysUser.getSysRoles().get(0).getRoleKey();
		// 集团领导、系统管理员、区域领导、公司财务
		//if ("1".equals(roleKey) || "2".equals(roleKey) || "6".equals(roleKey) || "8".equals(roleKey)) {
		if ("1".equals(roleKey) || "2".equals(roleKey) || "6".equals(roleKey) ) {
			List<Map<String, Object>> costInfoList = new ArrayList<>();
			// 当前用户管理区域
			String areaIds = sysUser.getAreaStr();
			if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
				String[] areaId_array = areaIds.split(",");
				for (String areaId : areaId_array) {
					if ("".equals(areaId))
						continue;
					CostInfo costInfo = new CostInfo();
					costInfo.setAreaId(areaId);
					costInfo.setFillDate(DateUtils.getMouth(new Date()));
					costInfo.setEffectIcon("1");
					Map<String, Object> map = costInfoService.getSumBySubjectCost(costInfo);
					//  Double.parseDouble(map.get("a1").toString());
					map.put("areaId", areaId);
					costInfoList.add(map);
				}
				return AjaxResult.success(costInfoList);
			}
		}
		return AjaxResult.error("对不起，您并没有区域管理权限，请联系管理员！");
	}

	@GetMapping("/costInfoCompanys/{areaGroupTypeId}")
	public String costInfoCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
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
		return prefix + "/costInfo_companys";
	}

	@RequestMapping("/costInfoCompanysData")
	@ResponseBody
	public AjaxResult costInfoCompanysData(String areaIds, String date) {
		List<Map<String, Object>> costInfoList = new ArrayList<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			String[] areaId_array = areaIds.split(",");
			for (String areaId : areaId_array) {
				if ("".equals(areaId))
					continue;
				CostInfo costInfo = new CostInfo();
				costInfo.setAreaId(areaId);
				costInfo.setFillDate(DateUtils.getMouth(new Date()));
				costInfo.setEffectIcon("1");
				Map<String, Object> map = costInfoService.getSum(costInfo);
				map.put("areaId", areaId);
				costInfoList.add(map);
			}
			return AjaxResult.success(costInfoList);
		}
		return AjaxResult.error();
	}

	/**
	 * 财务级首页数据查询
	 */
	@RequestMapping("/homeFinanceData")
	@ResponseBody
	public AjaxResult homeFinanceData() {
		CostInfo costInfo = new CostInfo();
		costInfo.setAreaId(getLoginUserInfo().getAreaStr());
		costInfo.setEffectIcon("1");
		List<CostInfo> costInfoList = costInfoService.getList(costInfo);

		if (costInfoList != null && costInfoList.size() > 0) {
			CostInfo info = costInfoList.get(0);
			Date date = DateUtils.dateTime("yyyy-MM-dd", info.getFillTime());
			Map<String, Object> params = new HashMap<>();
			if (!DateUtils.isThisMonth(date)) { // 判断数据是不是本月
				params.put("iconMonth", 1);
				if (!DateUtils.isThisYear(date)) { // 判断数据是不是本年
					params.put("iconYear", 1);
				} else {
					params.put("iconYear", 2);
				}
			} else {
				params.put("iconMonth", 2);
			}
			info.setParams(params);
		}
		return AjaxResult.success(costInfoList);
	}

	@GetMapping("/costInfoFinance")
	public String costInfoFinance(ModelMap mMap) {
		SysUser curUser = getLoginUserInfo();
		// 查区域名称
		mMap.put("areaName", areaService.getEntityById(curUser.getAreaStr()).getAreaName());
		// 查最新的那条数据
		CostInfo costInfo = new CostInfo();
		costInfo.setAreaId(curUser.getAreaStr());
		costInfo.setEffectIcon("1");
		CostInfo last = costInfoService.getLast(costInfo);
		if (last != null) {
			Date date = DateUtils.dateTime("yyyy-MM-dd", last.getFillTime());
			if (!DateUtils.isThisMonth(date)) { // 判断数据是不是本月
				last.setCurrentEnter(0.0);
				last.setCurrentTotal(0.0);
				if (!DateUtils.isThisYear(date)) { // 判断数据是不是本年
					last.setLastYear(last.getThisYear());
					last.setThisYear(0.0);
				}
			}
		}
		mMap.put("costInfo", last);
		return prefix + "/costInfo_finance";
	}

	@Log(title = "当月费用填报", businessType = BusinessType.INSERT)
	@RequestMapping("/costInfoAdd")
	@ResponseBody
	public AjaxResult costInfoAdd(CostInfo costInfo) {
		SysUser curUser = getLoginUserInfo();
		costInfo.setFillDate(DateUtils.getMouth(new Date()));
		costInfo.setFillUserId(curUser.getUserId());
		costInfo.setFillUserName(curUser.getUserName());
		costInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		costInfo.setAreaId(curUser.getAreaStr());
		return AjaxResult.success(costInfoService.add(costInfo));
	}

	/**
	 * 财务导航点进去数据查询
	 */
	@RequestMapping("/costInfoFinanceData")
	@ResponseBody
	public AjaxResult costInfoFinanceData(String date) {
		String last = DateUtils.parseDateToStr(DateUtils.YYYY_MM, DateUtils.addYear(DateUtils.dateTime(DateUtils.YYYY_MM, date), -1));
		CostInfo costInfo = new CostInfo();
		costInfo.setAreaId(getLoginUserInfo().getAreaStr());
		Map<String, Object> params = new HashMap<>();
		params.put("dateSta", last);
		params.put("dateEnd", date);
		costInfo.setParams(params);
		return AjaxResult.success(costInfoService.getList(costInfo));
	}





}
