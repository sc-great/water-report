package com.boot.web.controller.admin.system;

import com.boot.common.annotation.Log;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.core.text.Convert;
import com.boot.common.enums.BusinessType;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysOrgService;
import com.boot.system.service.ISysRoleService;
import com.boot.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 岗位信息
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/system/role")
public class SysRoleController extends BaseController {
	private String prefix = "admin/system/role";

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private ISysUserService sysUserService;

	@RequiresPermissions("system:role:list")
	@GetMapping("/list")
	public String role() {
		return prefix + "/list";
	}

	@RequiresPermissions("system:role:list")
	@PostMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(SysRole role) {
		startPage();
		List<SysRole> list = roleService.findList(role);
		return getDataTable(list);
	}

	/**
	 * 新增岗位
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		return prefix + "/add";
	}

	/**
	 * 新增保存岗位
	 */
	@RequiresPermissions("system:role:add")
	@Log(title = "岗位管理", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(@Validated SysRole role) {
		return toAjax(roleService.insert(role));
	}

	/**
	 * 修改岗位
	 */
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") String roleId, ModelMap mmap) {
		// 岗位角色所属组织机构
//        List<String> orgIds=new ArrayList<>();
//        List<String> orgNames=new ArrayList<>();
//        List<SysOrg> sysOrgs=orgService.getOrgListByRoleId(roleId);
//        for(SysOrg sysOrg: sysOrgs){
//            orgIds.add(sysOrg.getOrgId());
//            orgNames.add(sysOrg.getOrgName());
//        }
//        mmap.put("orgIds", StringUtils.join(orgIds, ","));
//        mmap.put("orgNames", StringUtils.join(orgNames, ","));
		mmap.put("role", roleService.getById(roleId));
		return prefix + "/edit";
	}

	/**
	 * 修改保存岗位
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@PostMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(@Validated SysRole role) {
		return toAjax(roleService.updateAndOrgs(role));
	}

	/**
	 * 修改保存岗位
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@PostMapping("/doChangeStatus")
	@ResponseBody
	public AjaxResult doChangeStatus(SysRole role) {
		return toAjax(roleService.update(role));
	}

	/**
	 * 岗位分配菜单权限
	 */
	@GetMapping("/menusScope/{roleId}")
	public String menusScope(@PathVariable("roleId") String roleId, ModelMap mmap) {
		mmap.put("roleId", roleId);
		return prefix + "/menusScope";
	}

	/**
	 * 保存角色分配菜单权限
	 */
	@RequiresPermissions("system:role:menusScope")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PostMapping("/doMenusScope")
	@ResponseBody
	public AjaxResult doMenusScope(SysRole role) {
		return toAjax(roleService.menusScope(role));
	}

	/**
	 * 岗位分配数据权限
	 */
	@GetMapping("/authDataScope/{roleId}")
	public String authDataScope(@PathVariable("roleId") String roleId, ModelMap mmap) {
		mmap.put("role", roleService.getById(roleId));
		return prefix + "/dataScope";
	}

	/**
	 * 保存角色分配数据权限
	 */
	@RequiresPermissions("system:role:authDataScope")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PostMapping("/doAuthDataScope")
	@ResponseBody
	public AjaxResult doAuthDataScope(SysRole role) {
		return toAjax(roleService.authDataScope(role));
	}

	/**
	 * 查询数量
	 */
	@RequestMapping("/count")
	@ResponseBody
	public AjaxResult count(SysRole sysRole) {
		AjaxResult ajaxResult;
		int count = roleService.getCount(sysRole);
		if (count > 0) {
			ajaxResult = AjaxResult.success("数量查询成功！", count);
		} else {
			ajaxResult = AjaxResult.warn("数量为0！");
		}
		return ajaxResult;
	}

	@RequiresPermissions("system:role:remove")
	@Log(title = "角色管理", businessType = BusinessType.DELETE)
	@PostMapping("/doRemove")
	@ResponseBody
	public AjaxResult doRemove(@RequestParam("ids") String roleId) {
		AjaxResult ajaxResult;
		try {
			List<SysUser> sysUsers = sysUserService.getUserListByRoleId(roleId);
			if (sysUsers.size() > 0) {
				ajaxResult = AjaxResult.warn("该岗位角色存在用户，不允许删除！");
			} else {
				if (roleService.deleteById(roleId) > 0) {
					ajaxResult = success("删除成功！");
				} else {
					ajaxResult = error("删除失败！");
				}
			}
			return success();
		} catch (Exception e) {
			ajaxResult = error(e.getMessage());
		}
		return ajaxResult;
	}

	/**
	 * 获取岗位角色集合
	 * */
	@RequestMapping("/getListByOrgId/{orgId}")
	@ResponseBody
	public AjaxResult getListByOrgId(@PathVariable String orgId) {
		AjaxResult ajaxResult;
		List<SysRole> sysRoles = roleService.getRoleListByOrgId(orgId);
		ajaxResult = success("岗位角色获取成功！", sysRoles);
		return ajaxResult;
	}

	/**
	 * 获取用户岗位角色集合
	 * */
	@RequestMapping("/getListByUserId/{userId}")
	@ResponseBody
	public AjaxResult getListByUserId(@PathVariable String userId) {
		AjaxResult ajaxResult;
		List<SysRole> sysRoles = roleService.getRoleListByUserId(userId);
		if (sysRoles.size() > 0) {
			ajaxResult = success("岗位角色获取成功！", sysRoles);
		} else {
			ajaxResult = error("未找到角色信息！");
		}
		return ajaxResult;
	}

	/**
	 *
	 * 获取组织机构下的角色树
	 * */
	@RequestMapping("/getRoleTreeDataByOrgIds")
	@ResponseBody
	public AjaxResult getRoleTreeDataByOrgIds(String orgIds) {
		AjaxResult ajaxResult;
		if (!"".equals(orgIds)) {
			String orgIdStr = "";
			List<SysOrg> sysOrgs = orgService.findListByIds(Convert.toStrArray(orgIds));
			if (sysOrgs.size() > 0) {
				for (SysOrg sysOrg : sysOrgs) {
					if ("".equals(orgIdStr)) {
						orgIdStr = sysOrg.getOrgIdPath().replaceAll("/", ",");
					} else {
						orgIdStr += "," + sysOrg.getOrgIdPath().replaceAll("/", ",");
					}
				}
			}
			sysOrgs = orgService.findListByIds(Convert.toStrArray(orgIdStr));
			ajaxResult = success("机构查询成功！", getJsonTreeData(sysOrgs, "0"));
		} else {
			ajaxResult = error();
		}
		return ajaxResult;
	}

	/**
	 * 封装角色机构树
	 * */
	private List<Map<String, Object>> getJsonTreeData(List<SysOrg> sysOrgs, String parentId) {
		List<Map<String, Object>> mapList = new LinkedList<>();

		if (sysOrgs.size() > 0) {
			for (SysOrg sysOrg : sysOrgs) {
				if (parentId.equals(sysOrg.getParentId())) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", sysOrg.getOrgId());
					map.put("name", sysOrg.getOrgName());
					SysOrg sysOrg_params = new SysOrg();
					sysOrg_params.setParentId(sysOrg.getOrgId());
					sysOrg_params.setValidStatus("1");
					int count = orgService.getCount(sysOrg_params);
					if (count > 0) {
						List<Map<String, Object>> children = getJsonTreeData(sysOrgs, sysOrg.getOrgId());
						map.put("children", children);
					} else {
						List<SysRole> sysRoles = roleService.getRoleListByOrgId(sysOrg.getOrgId());
						map.put("children", toMapByFiled(sysOrg.getOrgId(), sysRoles));
					}
					map.put("childCount", 1);
					mapList.add(map);
				}
			}
		}
		return mapList;
	}

	private List<Map<String, Object>> toMapByFiled(String orgId, List<SysRole> sysRoles) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (SysRole sysRole : sysRoles) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", String.format("%s/%s", orgId, sysRole.getRoleId()));
			map.put("name", sysRole.getRoleName());
			map.put("childCount", 0);
			mapList.add(map);
		}
		return mapList;
	}
}