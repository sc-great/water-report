package com.boot.web.controller.admin.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.enums.BusinessType;
import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.report.domain.UserHealthInfo;
import com.boot.report.service.IUserHealthInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysOrgService;

/**
 * 员工健康信息Controller
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Controller
@RequestMapping("/admin/report/userHealthInfo")
public class UserHealthInfoController extends BaseController {
	private String prefix = "admin/report/userHealthInfo";

	@Autowired
	private IUserHealthInfoService userHealthInfoService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysOrgService orgService;

	@RequiresPermissions("report:userHealthInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询员工健康信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(UserHealthInfo userHealthInfo) {
		userHealthInfo.setEffectIcon("1");
		SysUser sysUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有
				
			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				userHealthInfo.setParams(params);
			} else {
				userHealthInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<UserHealthInfo> list = userHealthInfoService.getList(userHealthInfo);
		if (StringUtils.isNotEmpty(list)) {
			for (UserHealthInfo userHealth : list) {
				SysOrg org = orgService.getById(userHealth.getFactoryId());
				if (StringUtils.isNotNull(org)) {
					userHealth.getParams().put("factoryName", org.getOrgName());
				}
				Area area = areaService.getEntityById(userHealth.getAreaId());
				if (StringUtils.isNotNull(area)) {
					userHealth.getParams().put("areaName", area.getAreaName());
				}
			}
		}
		return getDataTable(list);
	}

	/**
	 * 导出员工健康信息列表
	 */
	@RequiresPermissions("report:userHealthInfo:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult doExport(UserHealthInfo userHealthInfo) {
		List<UserHealthInfo> list = userHealthInfoService.getList(userHealthInfo);
		ExcelUtil<UserHealthInfo> util = new ExcelUtil<UserHealthInfo>(UserHealthInfo.class);
		return util.exportExcel(list, "userHealthInfo");
	}

	/**
	 * 新增员工健康信息
	 */
	@RequiresPermissions("report:userHealthInfo:add")
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
	public AjaxResult checkIsOver() {
		SysUser curUser = ShiroUtils.getSysUser();
		UserHealthInfo userHealthParam = new UserHealthInfo();
		userHealthParam.setMobile(curUser.getPhone());
		userHealthParam.setFillDate(DateUtils.getDate());
		userHealthParam.setEffectIcon("1");
		List<UserHealthInfo> overList = userHealthInfoService.getList(userHealthParam);
		if (StringUtils.isNotEmpty(overList) && overList.size() > 0) {
			return error("您当天已经填报过信息，是否继续填报？");
		}
		return success();
	}

	/**
	 * 新增保存员工健康信息
	 */
	@RequiresPermissions("report:userHealthInfo:add")
	@Log(title = "员工健康信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(UserHealthInfo userHealthInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		userHealthInfo.setUserName(curUser.getUserName());
		userHealthInfo.setMobile(curUser.getPhone());
		return toAjax(userHealthInfoService.add(userHealthInfo));
	}

	/**
	 * 修改员工健康信息
	 */
	@RequiresPermissions("report:userHealthInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		UserHealthInfo userHealthInfo = userHealthInfoService.getEntityById(id);
		mmap.put("userHealthInfo", userHealthInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存员工健康信息
	 */
	@RequiresPermissions("report:userHealthInfo:edit")
	@Log(title = "员工健康信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(UserHealthInfo userHealthInfo) {
		return toAjax(userHealthInfoService.update(userHealthInfo));
	}

	/**
	 * 删除员工健康信息
	 */
	@RequiresPermissions("report:userHealthInfo:delete")
	@Log(title = "员工健康信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(userHealthInfoService.deleteByIds(ids));
	}

	/**
	* 获取数量
	*/
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(UserHealthInfo userHealthInfo) {
		return userHealthInfoService.getCount(userHealthInfo);
	}
}