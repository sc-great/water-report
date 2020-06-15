package com.boot.web.controller.admin.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.boot.common.annotation.Log;
import com.boot.common.enums.BusinessType;
import com.boot.report.domain.MudInfo;
import com.boot.report.service.IMudInfoService;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;

/**
 * 水厂污泥数据信息Controller
 * 
 * @author EPL
 * @date 2020-03-30
 */
@Controller
@RequestMapping("/admin/report/mudInfo")
public class MudInfoController extends BaseController {
	private String prefix = "admin/report/mudInfo";

	@Autowired
	private IMudInfoService mudInfoService;

	@RequiresPermissions("report:mudInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询水厂污泥数据信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(MudInfo mudInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		mudInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				mudInfo.setParams(params);
			} else {
				mudInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<MudInfo> list = mudInfoService.getList(mudInfo);
		return getDataTable(list);
	}

	/**
	 * 检查当天是否已经填报过信息
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
				MudInfo mudInfoParam = new MudInfo();
				mudInfoParam.setFillDate(DateUtils.getDate());
				mudInfoParam.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				mudInfoParam.setEffectIcon("1");
				List<MudInfo> overList = mudInfoService.getList(mudInfoParam);
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
	 */
	@RequestMapping("/getLatest")
	@ResponseBody
	public AjaxResult getLatest() {
		SysUser curUser = ShiroUtils.getSysUser();
		// 查询最新的统计数据（可能是当天，也可能不是）
		MudInfo mudInfo = new MudInfo();
		mudInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		mudInfo.setEffectIcon("1");
		mudInfo.setFillDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return success("0", mudInfoService.getLatest(mudInfo));
	}

	/**
	 * 新增水厂污泥数据信息
	 */
	@RequiresPermissions("report:mudInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		return prefix + "/add";
	}

	/**
	 * 新增保存水厂污泥数据信息
	 */
	@RequiresPermissions("report:mudInfo:add")
	@Log(title = "水厂污泥数据信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(MudInfo mudInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		mudInfo.setFillUserId(curUser.getUserId());
		mudInfo.setFillUserName(curUser.getUserName());
		mudInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		mudInfo.setAreaId(curUser.getAreaStr());
		return toAjax(mudInfoService.add(mudInfo));
	}

	/**
	 * 修改水厂污泥数据信息
	 */
	@RequiresPermissions("report:mudInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		MudInfo mudInfo = mudInfoService.getEntityById(id);
		mmap.put("mudInfo", mudInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂污泥数据信息
	 */
	@RequiresPermissions("report:mudInfo:edit")
	@Log(title = "水厂污泥数据信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(MudInfo mudInfo) {
		return toAjax(mudInfoService.update(mudInfo));
	}

	/**
	 * 删除水厂污泥数据信息
	 */
	@RequiresPermissions("report:mudInfo:delete")
	@Log(title = "水厂污泥数据信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(mudInfoService.deleteByIds(ids));
	}

	/**
	* 获取数量
	*/
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(MudInfo mudInfo) {
		return mudInfoService.getCount(mudInfo);
	}
}