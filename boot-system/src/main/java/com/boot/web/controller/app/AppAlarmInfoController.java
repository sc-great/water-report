package com.boot.web.controller.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.AlarmInfo;
import com.boot.report.service.AlarmInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

/**
 * 报警信息Controller
 * 
 * @author LM
 * @date 2020-05-17
 */
@Controller
@RequestMapping("/app/alarmInfo")
public class AppAlarmInfoController extends APPBaseController {

	@Autowired
	private AlarmInfoService alarmService;

	@ResponseBody
	@PostMapping("/list")
	public AjaxResult list(AlarmInfo alarm) {
		SysUser sysUser = getLoginUserInfo();
		alarm.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				alarm.setParams(params);
			} else {
				alarm.setOrgId(sysOrgList.get(0).getOrgId());
			}
		} else {
			AjaxResult.error("您还未分配机构！");
		}
		return AjaxResult.success(alarmService.getList(alarm));
	}
}
