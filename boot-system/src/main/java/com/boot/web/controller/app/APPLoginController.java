package com.boot.web.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.boot.common.annotation.Log;
import com.boot.common.constant.Constants;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.enums.BusinessType;
import com.boot.common.enums.OperatorType;
import com.boot.common.utils.AddressUtils;
import com.boot.common.utils.CommonUtils;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.ServletUtils;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.spring.SpringUtils;
import com.boot.framework.shiro.service.SysPasswordService;
import com.boot.framework.util.LogUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.report.domain.UserHealthInfo;
import com.boot.report.service.IUserHealthInfoService;
import com.boot.system.domain.SysLogininfor;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysOrgService;
import com.boot.system.service.ISysRoleService;
import com.boot.system.service.ISysUserService;
import com.boot.system.service.impl.SysLogininforServiceImpl;

import eu.bitwalker.useragentutils.UserAgent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class APPLoginController extends APPBaseController {
	private String prefix = "app";

	@Autowired
	private ISysUserService userService;
	@Autowired
	private SysPasswordService passwordService;
	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private ISysRoleService roleService;
	@Autowired
	private IUserHealthInfoService userHealthInfoService;

	/**
	 * 登录
	 */
	@GetMapping("/index/{url}")
	public String index(@PathVariable("url") String url, ModelMap modelMap) {
		if (!"login".equals(url)) {
			url = CommonUtils.decode(url);
		}
		modelMap.put("url", url);
		return prefix + "/index";
	}

	/**
	 * 登录
	 */
	@GetMapping("/login")
	public String login(ModelMap modelMap) {
		String waterReportUserName = "";
		Cookie[] cookies = getCookie();
		for (Cookie c : cookies) {
			if (c.getName().equals("waterReportUserName"))
				waterReportUserName = c.getValue();
		}
		modelMap.put("waterReportUserName", waterReportUserName);
		return prefix + "/login";
	}

	/**
	 * 登录 (原有登录)
	 */
//	@PostMapping("/doLogin")
//	@ResponseBody
//	public AjaxResult doLogin(String loginName, String password) {
//		AjaxResult ajaxResult;
//		SysUser sysUser = userService.loginName(loginName);
//		if (sysUser != null) {
//			String pwdStr1 = passwordService.encryptPassword(sysUser.getLoginName(), password, sysUser.getSalt());
//			if (pwdStr1.equals(sysUser.getPassword())) {
//				List<SysRole> sysRoles = roleService.getRoleListByUserId(sysUser.getUserId());
//				if (sysRoles.size() > 0) {
//					SysRole sysRole = sysRoles.get(0);
//					if (sysRole != null) {
//						if ("3".equals(sysRole.getRoleCode()) || "4".equals(sysRole.getRoleCode())
//								|| "5".equals(sysRole.getRoleCode())) {
//							ajaxResult = AjaxResult.error("您好，您没有登录权限！");
//						} else {
//							String areaIds = sysUser.getAreaStr();
//							if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
//								List<SysOrg> sysOrgs = orgService.getOrgListByUserId(sysUser.getUserId());
//								sysUser.setSysRoles(sysRoles);
//								sysUser.setSysOrgs(sysOrgs);
//								getSession().setAttribute("loginUserInfo", JSONObject.toJSON(sysUser));
//								ajaxResult = AjaxResult.success("登录成功！");
//							} else {
//								ajaxResult = AjaxResult.error("您好，您没有区域配置！");
//							}
//						}
//					} else {
//						ajaxResult = AjaxResult.error("您好，您没有角色权限！");
//					}
//				} else {
//					ajaxResult = AjaxResult.error("您好，您没有角色权限！");
//				}
//			} else {
//				ajaxResult = AjaxResult.error("密码错误！");
//			}
//		} else {
//			ajaxResult = AjaxResult.error("账号错误！");
//		}
//	 return ajaxResult;
//		
//	}

	/**
	 * 登录
	 */	
	@PostMapping("/doLogin")
	@ResponseBody
	public AjaxResult doLogin(String loginName, String password) {
		String returnHome = "";
		boolean seccess = false;
		Map<String, Object> map = new HashMap<String, Object>();
		SysUser sysUser = userService.loginName(loginName);
		if (sysUser != null) {
			String pwdStr1 = passwordService.encryptPassword(sysUser.getLoginName(), password, sysUser.getSalt());
			if (pwdStr1.equals(sysUser.getPassword())) {
				List<SysRole> sysRoles = roleService.getRoleListByUserId(sysUser.getUserId());
				if (sysRoles.size() > 0) {
					SysRole sysRole = sysRoles.get(0);
					if (sysRole != null) {
						if ("3".equals(sysRole.getRoleCode()) || "4".equals(sysRole.getRoleCode())
								|| "5".equals(sysRole.getRoleCode())) { // 3，4，5：水厂级
							List<SysOrg> sysOrgs = orgService.getOrgListByUserId(sysUser.getUserId());
							sysUser.setSysRoles(sysRoles);
							sysUser.setSysOrgs(sysOrgs);
							getSession().setAttribute("loginUserInfo", JSONObject.toJSON(sysUser));
							returnHome = "homeEmployee";
							map.put("url", returnHome);
							map.put("mess", "ok");
							seccess = true;
						} else if ("1".equals(sysRole.getRoleCode()) || "2".equals(sysRole.getRoleCode())
								|| "6".equals(sysRole.getRoleCode()) || "7".equals(sysRole.getRoleCode())) { // 1，2，6，7
																												// 领导级
							String areaIds = sysUser.getAreaStr();
							if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
								List<SysOrg> sysOrgs = orgService.getOrgListByUserId(sysUser.getUserId());
								sysUser.setSysRoles(sysRoles);
								sysUser.setSysOrgs(sysOrgs);
								getSession().setAttribute("loginUserInfo", JSONObject.toJSON(sysUser));
								// ajaxResult = AjaxResult.success("登录成功！");
								returnHome = "homeAdmin";
								map.put("url", returnHome);
								map.put("mess", "ok");
								seccess = true;
							} else {
								// ajaxResult = AjaxResult.error("您好，您没有区域配置！");

								returnHome = "login";
								map.put("url", returnHome);
								map.put("mess", "您好，您没有区域配置！");
							}
						} else if ("8".equals(sysRole.getRoleCode())) { // 8财务级
							List<SysOrg> sysOrgs = orgService.getOrgListByUserId(sysUser.getUserId());
							sysUser.setSysRoles(sysRoles);
							sysUser.setSysOrgs(sysOrgs);
							getSession().setAttribute("loginUserInfo", JSONObject.toJSON(sysUser));
							returnHome = "homeFinance";
							map.put("url", returnHome);
							map.put("mess", "ok");
							seccess = true;
						} else { // 只许1到8的角色登录
							// mapMode.put("disMess","您好，您没有登录权限！");
							returnHome = "login";
							map.put("url", returnHome);
							map.put("mess", "您好，您不在准许的角色内！");
						}
					} else {
						// mapMode.put("disMess","您好，您没有角色权限！");
						returnHome = "login";
						map.put("url", returnHome);
						map.put("mess", "您好，您没有角色权限！");
					}
				} else {
					// ajaxResult = AjaxResult.error("您好，您没有角色权限！");

					returnHome = "login";
					map.put("url", returnHome);
					map.put("mess", "您好，您没有角色权限！");
				}
			} else {
				// ajaxResult = AjaxResult.error("密码错误！");

				returnHome = "login";
				map.put("url", returnHome);
				map.put("mess", "您好，密码错误！");
			}
		} else {
			// ajaxResult = AjaxResult.error("账号错误！");

			returnHome = "login";
			map.put("url", returnHome);
			map.put("mess", "您好，账号错误！");
		}
		if (seccess) {
			Cookie cookie = new Cookie("waterReportUserName", loginName.trim());
			cookie.setMaxAge(30 * 24 * 60 * 60); // 设置有效期 30 天
			getResponse().addCookie(cookie);
		}
		//插入系统访问记录表
		if ( !"login".equals(map.get("url").toString()) ){
			  final String ip = ShiroUtils.getIp();

			  String address = AddressUtils.getRealAddressByIP(ip);

              // 获取客户端操作系统
			  final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
              String os = userAgent.getOperatingSystem().getName();
              // 获取客户端浏览器
              String browser = userAgent.getBrowser().getName();
              // 封装对象
              SysLogininfor logininfor = new SysLogininfor();
              logininfor.setLoginName(loginName);
              logininfor.setIpaddr(ip);
              logininfor.setLoginLocation(address);
              logininfor.setBrowser(browser);
              logininfor.setOs(os);
              logininfor.setMsg("登录成功");
              // 日志状态             
              logininfor.setStatus(Constants.SUCCESS);
              SpringUtils.getBean(SysLogininforServiceImpl.class).insertLogininfor(logininfor);
		}
		
		return AjaxResult.success("0", map);
	}

	/**
	 * 退出登录
	 */
	@RequestMapping("/signOut")
	public String signOut() {
		getSession().removeAttribute("loginUserInfo");
		return prefix + "/signout";
	}

	/**
	 * 来访人登记
	 */
	@GetMapping("/visitor/{areaId}/{factoryId}")
	public String visitor(@Valid @PathVariable("areaId") String areaId,
			@Valid @PathVariable("factoryId") String factoryId, ModelMap mapMode) {
		mapMode.put("areaId", areaId);
		mapMode.put("factoryId", factoryId);
		return prefix + "/visitor_register";
	}

	/**
	 * 来访人登记
	 */
	@PostMapping("/doVisitor")
	@ResponseBody
	public AjaxResult doVisitor(UserHealthInfo userHealthInfo) {
		AjaxResult ajaxResult;
		UserHealthInfo params = new UserHealthInfo();
		params.getParams().put("effectIcon", "0");
		params.setAreaId(userHealthInfo.getAreaId());
		params.setFactoryId(userHealthInfo.getFactoryId());
		params.setUserName(userHealthInfo.getUserName());
		params.setMobile(userHealthInfo.getMobile());
		params.setFillDate(DateUtils.dateTimeNow("yyyy-MM-dd"));
		userHealthInfoService.updateByWhere(params);

		SysUser sysUser = new SysUser();
	   //sysUser.setUserName(userHealthInfo.getUserName());
		sysUser.setPhone(userHealthInfo.getMobile());
		//sysUser.getParams().put("orgId", userHealthInfo.getFactoryId());
		int count = userService.getCount(sysUser);
		if (count > 0) {
			userHealthInfo.setIsInFactory("1");
		} else {
			userHealthInfo.setIsInFactory("0");
		}
		userHealthInfo.setEffectIcon("1");
		userHealthInfo.setFillDate(DateUtils.dateTimeNow("yyyy-MM-dd"));
		userHealthInfo.setFillTime(DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss"));
		if (userHealthInfoService.add(userHealthInfo) > 0) {
			ajaxResult = AjaxResult.success();
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
}
