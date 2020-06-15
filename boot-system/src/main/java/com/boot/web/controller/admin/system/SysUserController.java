package com.boot.web.controller.admin.system;

import com.boot.common.annotation.Log;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.domain.Ztree;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.core.text.Convert;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.StringUtils;
import com.boot.framework.shiro.service.SysPasswordService;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.*;
import com.boot.system.service.*;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author epl
 */
@Api(value = "/admin/system/user", description = "系统基础用户信息管理")
@Controller
@RequestMapping("/admin/system/user")
public class SysUserController extends BaseController {
	private String prefix = "admin/system/user";

	@Autowired
	private ISysUserService userService;
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private ISysRoleService roleService;
	@Autowired
	private ISysFilesService sysFilesService;
	@Autowired
	private SysPasswordService passwordService;
	@Autowired
	private IAreaService areaService;

	@RequiresPermissions("system:user:list")
	@GetMapping("list")
	public String list() {
		return prefix + "/list";
	}

	@RequiresPermissions("system:user:list")
	@PostMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(SysUser user) {
		startPage();
		List<SysUser> list = userService.findList(user);
		if (StringUtils.isNotEmpty(list)) {
			for (SysUser sysUser : list) {
				List<SysRole> sysRoleList = roleService.getRoleListByUserId(sysUser.getUserId());
				if (StringUtils.isNotEmpty(sysRoleList)) {
					sysUser.setSysRoles(sysRoleList);
				}
			}
		}
		return getDataTable(list);
	}

	@RequestMapping("/findListByIds")
	@ResponseBody
	public TableDataInfo findListByIds(String userIds) {
		List<SysUser> list = userService.findListByIds(userIds);
		for (SysUser sysUser : list) {
			List<SysOrg> orgList = orgService.getOrgListByUserId(sysUser.getUserId());
			sysUser.setSysOrgs(orgList);
		}
		return getDataTable(list);
	}

	/**
	 * 新增用户
	 */
	@GetMapping("/add")
	public String add(ModelMap mMap) {
		SysRole sysRole = new SysRole();
		List<SysRole> list = roleService.findList(sysRole);
		mMap.put("roleList", list);
		return prefix + "/add";
	}

	/**
	 * 新增保存用户
	 */
	@RequiresPermissions("system:user:add")
	@Log(title = "用户管理", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(@Validated SysUser user) {
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		// 通过组织机构获取区域编号
		if (user.getParams().containsKey("orgIds")) {
			String orgId = Convert.toStr(user.getParams().get("orgIds"));
			if (StringUtils.isNotEmpty(orgId)) {
				SysOrg sysOrg = orgService.getById(orgId);
				if (sysOrg != null && StringUtils.isNotEmpty(sysOrg.getAreaId())) {
					user.setAreaStr(sysOrg.getAreaId());
				} else {
					sysOrg = new SysOrg();
					sysOrg.setAreaId(orgId);
					List<SysOrg> list = orgService.findList(sysOrg);
					if (list != null) {
						user.getParams().put("orgIds", list.get(0).getOrgId());
					}
					user.setAreaStr(orgId);
				}
			}
		}
		return toAjax(userService.insert(user));
	}

	/**
	 * 修改用户
	 */
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") String userId, ModelMap mmap) {
		SysUser sysUser = userService.getById(userId);
		if (sysUser != null) {
			// 用户头像
			if (sysUser.getAvatar() != null && !"".equals(sysUser.getAvatar())) {
				SysFiles sysFiles = sysFilesService.selectSysFilesById(sysUser.getAvatar());
				mmap.put("avatar", sysFiles);
			}
			// 用户岗位角色
			List<String> roleIds = new ArrayList<>();
//            List<String> roleNames=new ArrayList<>();
			List<SysUserRole> sysUserRoles = roleService.getUserRoleList(userId);
			if (sysUserRoles.size() > 0) {
				for (SysUserRole sysUserRole : sysUserRoles) {
//                    roleIds.add(String.format("%s/%s",sysUserRole.getOrgId(),sysUserRole.getRoleId()));
					roleIds.add(sysUserRole.getRoleId());
//                    String roleName="";
//                    SysRole sysRole=roleService.getById(sysUserRole.getRoleId());
//                    if(sysRole!=null){
//                        roleName=sysRole.getRoleName();
//                    }
//                    roleNames.add(roleName);
				}
			}
			mmap.put("roleId", StringUtils.join(roleIds, ","));
//            mmap.put("roleName", StringUtils.join(roleNames,","));
			SysRole sysRole = new SysRole();
			List<SysRole> list = roleService.findList(sysRole);
			mmap.put("roleList", list);
			
			// 用户组织机构
			List<String> orgIds = new ArrayList<>();
			List<String> orgNames = new ArrayList<>();
			if ("8".equals(roleService.getById(sysUserRoles.get(0).getRoleId()).getRoleKey())) {
				Area area = areaService.getEntityById(sysUser.getAreaStr());
				orgIds.add(area.getAreaId());
				orgNames.add(area.getAreaName());
			} else {
				List<SysOrg> sysOrgs = orgService.getOrgListByUserId(userId);
				if (sysOrgs.size() > 0) {
					for (SysOrg sysOrg : sysOrgs) {
						orgIds.add(sysOrg.getOrgId());
						orgNames.add(sysOrg.getOrgName());
						mmap.put("orgType", sysOrg.getOrgType());
					}
				}
			}
			mmap.put("orgIds", StringUtils.join(orgIds, ","));
			mmap.put("orgNames", StringUtils.join(orgNames, ","));
		}
		mmap.put("user", sysUser);
		return prefix + "/edit";
	}

	/**
	 * 修改保存用户
	 */
	@RequiresPermissions("system:user:edit")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@PostMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(@Validated SysUser user) {
		if (user.getPassword() != null) {
			if (!"".equals(user.getPassword())) {
				user.setSalt(ShiroUtils.randomSalt());
				user.setPassword(
						passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
			} else {
				user.setPassword(null);
			}
		}
		// 通过组织机构获取区域编号
		if (user.getParams().containsKey("orgIds")) {
			String orgId = Convert.toStr(user.getParams().get("orgIds"));
			if (StringUtils.isNotEmpty(orgId)) {
				SysOrg sysOrg = orgService.getById(orgId);
				if (sysOrg != null) {
					if ("3".equals(sysOrg.getOrgType()) && StringUtils.isNotEmpty(sysOrg.getAreaId())) {
						user.setAreaStr(sysOrg.getAreaId());
					}
				} else {
					sysOrg = new SysOrg();
					sysOrg.setAreaId(orgId);
					List<SysOrg> list = orgService.findList(sysOrg);
					if (list != null) {
						user.getParams().put("orgIds", list.get(0).getOrgId());
					}
					user.setAreaStr(orgId);
				}
			}
		}
		int result = userService.updateOrgAndRole(user);
		if (result > 0) {
			if (ShiroUtils.getUserId().equals(user.getUserId())) {
				SysUser sysUser = ShiroUtils.getSysUser();
				user.setSysRoles(sysUser.getSysRoles());
				user.setSysOrgs(sysUser.getSysOrgs());
				ShiroUtils.setSysUser(user);
			}
		}
		return toAjax(result);
	}

	/**
	 * 用户状态修改
	 */
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:edit")
	@PostMapping("/doChangeStatus")
	@ResponseBody
	public AjaxResult doChangeStatus(SysUser user) {
		return toAjax(userService.update(user));
	}

	/**
	 * 重置密码
	 */
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") String userId, ModelMap mmap) {
		mmap.put("user", userService.getById(userId));
		return prefix + "/resetPwd";
	}

	/**
	 * 重置密码保存
	 */
	@RequiresPermissions("system:user:resetPwd")
	@Log(title = "重置密码", businessType = BusinessType.UPDATE)
	@PostMapping("/doResetPwd")
	@ResponseBody
	public AjaxResult doResetPwd(SysUser user) {
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		if (userService.update(user) > 0) {
			if (ShiroUtils.getUserId().equals(user.getUserId())) {
				SysUser sysUser = ShiroUtils.getSysUser();
				sysUser.setSalt(user.getSalt());
				sysUser.setPassword(user.getPassword());
				ShiroUtils.setSysUser(sysUser);
			}
			return success();
		}
		return error();
	}

	@RequiresPermissions("system:user:remove")
	@Log(title = "用户管理", businessType = BusinessType.DELETE)
	@PostMapping("/doRemove")
	@ResponseBody
	public AjaxResult doRemove(String ids) {
		try {
			return toAjax(userService.deleteByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 查询数量
	 */
	@RequestMapping("/count")
	@ResponseBody
	public AjaxResult count(SysUser sysUser) {
		AjaxResult ajaxResult;
		int count = userService.getCount(sysUser);
		if (count > 0) {
			ajaxResult = AjaxResult.success("数量查询成功！", count);
		} else {
			ajaxResult = AjaxResult.warn("数量为0！");
		}
		return ajaxResult;
	}

	/**
	 * 选择用户树形页面
	 */
	@RequestMapping("/selTree")
	public String selTree(@RequestParam("type") String type, @RequestParam("checkDiv") String checkDiv,
			@RequestParam(value = "parentDiv", required = false) String parentDiv,
			@RequestParam(value = "parentPreDiv", required = false) String parentPreDiv, ModelMap mmp) {
		mmp.put("type", type);
		mmp.put("checkDiv", checkDiv);
		mmp.put("parentDiv", parentDiv);
		if (StringUtils.isNotNull(parentPreDiv)) {
			mmp.put("parentPreDiv", parentPreDiv);
		}
		return prefix + "/selTree";
	}

	/**
	 * 加载部门用户列表树
	 */
	@PostMapping("/deptUserTreeData")
	@ResponseBody
	public List<Ztree> deptUserTreeData(@RequestParam Map<String, Object> params) {
		return userService.deptUserTreeData(params);
	}

	/**
	 * 分配区域
	 */
	@GetMapping("/appointArea/{userId}")
	public String appointArea(@PathVariable("userId") String userId, ModelMap mMap) {
		SysUser sysUser = userService.getById(userId);
		mMap.put("user", sysUser);
		// 获取所有区域
		Area areaParam = new Area();
		List<Area> allAreaList = areaService.getList(areaParam);
		List<Area> notSelList = new ArrayList<>();
		ArrayList<Area> selList = new ArrayList<>();
		// 排除已选中区域
		if (StringUtils.isNotNullAndNotEmpty(sysUser.getAreaStr())) {
			for (Area area : allAreaList) {
				if (sysUser.getAreaStr().contains(area.getAreaId())) {
					selList.add(area);
				} else {
					notSelList.add(area);
				}
			}
		} else {
			notSelList = allAreaList;
		}
		mMap.put("selAreaList", selList);
		mMap.put("notSelAreaList", notSelList);
		return prefix + "/appointArea";
	}

	/**
	 * 分配区域保存信息
	 */
	@Log(title = "更新用户信息", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:appointArea")
	@PostMapping("appointArea")
	@ResponseBody
	public AjaxResult appointAreaSave(SysUser sysUser) {
		return toAjax(userService.update(sysUser));
	}
}