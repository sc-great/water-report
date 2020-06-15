package com.boot.web.controller.admin.report;

import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.boot.report.domain.TodayElectricityInfo;
import com.boot.report.service.ITodayElectricityInfoService;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;

/**
 * 水厂当日用电信息Controller
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Controller
@RequestMapping("/admin/report/todayElectricityInfo")
public class TodayElectricityInfoController extends BaseController {
	private String prefix = "admin/report/todayElectricityInfo";

	@Autowired
	private ITodayElectricityInfoService todayElectricityInfoService;

	@RequiresPermissions("report:todayElectricityInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询水厂当日用电信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(TodayElectricityInfo todayElectricityInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		todayElectricityInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				todayElectricityInfo.setParams(params);
			} else {
				todayElectricityInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<TodayElectricityInfo> list = todayElectricityInfoService.getList(todayElectricityInfo);
		return getDataTable(list);
	}

	/**
	 * 新增水厂当日用电信息
	 */
	@RequiresPermissions("report:todayElectricityInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		return prefix + "/add";
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
				TodayElectricityInfo todayElectricityParam = new TodayElectricityInfo();
				todayElectricityParam.setFillDate(DateUtils.getDate());
				todayElectricityParam.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				todayElectricityParam.setEffectIcon("1");
				List<TodayElectricityInfo> overList = todayElectricityInfoService.getList(todayElectricityParam);
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
	 * 查最新的统计数据
	 * 
	 * @param operateIcon
	 * @return
	 */
	@RequestMapping("/getLatest")
	@ResponseBody
	public AjaxResult getLatest() {
		SysUser curUser = ShiroUtils.getSysUser();
		// 查询最新的统计数据（可能是当天，也可能不是）
		TodayElectricityInfo todayElectricityInfo = new TodayElectricityInfo();
		todayElectricityInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		todayElectricityInfo.setEffectIcon("1");
		todayElectricityInfo.setFillDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return success("0", todayElectricityInfoService.getLatest(todayElectricityInfo));
	}

	/**
	 * 新增保存水厂当日用电信息
	 */
	@RequiresPermissions("report:todayElectricityInfo:add")
	@Log(title = "水厂电耗信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(TodayElectricityInfo todayElectricityInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		todayElectricityInfo.setFillUserId(curUser.getUserId());
		todayElectricityInfo.setFillUserName(curUser.getUserName());
		todayElectricityInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		todayElectricityInfo.setAreaId(curUser.getAreaStr());
		return toAjax(todayElectricityInfoService.add(todayElectricityInfo));
	}

	/**
	 * double相加
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public double doubleAdd(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 修改水厂当日用电信息
	 */
	@RequiresPermissions("report:todayElectricityInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		TodayElectricityInfo todayElectricityInfo = todayElectricityInfoService.getEntityById(id);
		mmap.put("todayElectricityInfo", todayElectricityInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂当日用电信息
	 */
	@RequiresPermissions("report:todayElectricityInfo:edit")
	@Log(title = "水厂电耗信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(TodayElectricityInfo todayElectricityInfo) {
		return toAjax(todayElectricityInfoService.update(todayElectricityInfo));
	}

	/**
	 * 删除水厂当日用电信息
	 */
	@RequiresPermissions("report:todayElectricityInfo:delete")
	@Log(title = "水厂电耗信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(todayElectricityInfoService.deleteByIds(ids));
	}
}