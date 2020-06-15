package com.boot.web.controller.admin.report;

import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.boot.common.annotation.Log;
import com.boot.common.enums.BusinessType;
import com.boot.report.domain.TodayWaterYieldInfo;
import com.boot.report.service.ITodayWaterYieldInfoService;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;

/**
 * 水厂当日水量信息Controller
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Controller
@RequestMapping("/admin/report/todayWaterYieldInfo")
public class TodayWaterYieldInfoController extends BaseController {
	private String prefix = "admin/report/todayWaterYieldInfo";

	@Autowired
	private ITodayWaterYieldInfoService todayWaterYieldInfoService;

	@RequiresPermissions("report:todayWaterYieldInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询水厂当日水量信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(TodayWaterYieldInfo todayWaterYieldInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		todayWaterYieldInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				todayWaterYieldInfo.setParams(params);
			} else {
				todayWaterYieldInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<TodayWaterYieldInfo> list = todayWaterYieldInfoService.getList(todayWaterYieldInfo);
		List<TodayWaterYieldInfo> twoList = new ArrayList<TodayWaterYieldInfo>();

		for (TodayWaterYieldInfo ty : list) {
			TodayWaterYieldInfo entity = new TodayWaterYieldInfo();
			entity = ty;
			entity.setTotalIn((double) Math.round(ty.getTotalIn() * 100) / 100);
			entity.setTotalOut((double) Math.round(ty.getTotalOut() * 100) / 100);
			// System.out.println("======"+entity.getTodayIn());
			twoList.add(entity);
		}

		return getDataTable(list);
	}

	/**
	 * 新增水厂当日水量信息
	 */
	@RequiresPermissions("report:todayWaterYieldInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		return prefix + "/add";
	}

	/**
	 * 统计新增页面中当前日期前的累计进出水量
	 * @return
	 */
	@RequestMapping("/getSumInOut")
	@ResponseBody
	public AjaxResult getSumInOut() {

		TodayWaterYieldInfo temp = new TodayWaterYieldInfo();
		SysUser sysUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgList)) {
			temp.setFactoryId(sysOrgList.get(0).getOrgId());
		} else {
			throw new BusinessException("您还未分配机构！");
		}

		Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getAddOldTotalInOut(temp);

		return success("0", sumWaterYield);
	}

	/**
	 * 检查当前员工当天是否已经填报过信息
	 */
	@RequestMapping("/checkIsOver")
	@ResponseBody
	public AjaxResult checkIsOver(@RequestParam("operateIcon") String operateIcon) {
		SysUser curUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgs = curUser.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgs)) {
			SysOrg curOrg = sysOrgs.get(0);
			if (!("3").equals(curOrg.getOrgType())) {
				return warn("您不是水厂用户，不能进行数据" + (("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报"
						: (("del").equals(operateIcon) ? "删除" : "导出")) + "！");
			}
			if (("add").equals(operateIcon)) {
				TodayWaterYieldInfo todayWaterYieldParam = new TodayWaterYieldInfo();
				todayWaterYieldParam.setFillDate(DateUtils.getDate());
				todayWaterYieldParam.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				todayWaterYieldParam.setEffectIcon("1");
				List<TodayWaterYieldInfo> overList = todayWaterYieldInfoService.getList(todayWaterYieldParam);
				if (StringUtils.isNotEmpty(overList) && overList.size() > 0) {
					return error("当前水厂今日已经填报过信息，是否继续填报？", overList.get(0));
				}
			}
		} else {
			return warn("您还未分配机构！");
		}
		return success();
	}

	/**
	 * 新增保存水厂当日水量信息
	 */
	@RequiresPermissions("report:todayWaterYieldInfo:add")
	@Log(title = "水厂水量信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(TodayWaterYieldInfo todayWaterYieldInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		todayWaterYieldInfo.setFillUserId(curUser.getUserId());
		todayWaterYieldInfo.setFillUserName(curUser.getUserName());
		todayWaterYieldInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		todayWaterYieldInfo.setAreaId(curUser.getAreaStr());
		return toAjax(todayWaterYieldInfoService.add(todayWaterYieldInfo));
	}

	/**
	 * 修改水厂当日水量信息
	 */
	@RequiresPermissions("report:todayWaterYieldInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		TodayWaterYieldInfo todayWaterYieldInfo = todayWaterYieldInfoService.getEntityById(id);

		mmap.put("todayWaterYieldInfo", todayWaterYieldInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂当日水量信息
	 */
	@RequiresPermissions("report:todayWaterYieldInfo:edit")
	@Log(title = "水厂水量信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(TodayWaterYieldInfo todayWaterYieldInfo) {
		return toAjax(todayWaterYieldInfoService.update(todayWaterYieldInfo));
	}

	/**
	 * 删除水厂当日水量信息
	 */
	@RequiresPermissions("report:todayWaterYieldInfo:delete")
	@Log(title = "水厂水量信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(todayWaterYieldInfoService.deleteByIds(ids));
	}

	/**
	* 获取数量
	*/
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoService.getCount(todayWaterYieldInfo);
	}
}