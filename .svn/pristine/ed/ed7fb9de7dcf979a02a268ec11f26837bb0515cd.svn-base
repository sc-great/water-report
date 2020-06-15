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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.enums.BusinessType;
import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.report.domain.TestBadWaterInfo;
import com.boot.report.service.ITestBadWaterInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

/**
 * 化验数据信息(污水)Controller
 * 
 * @author yangxiaojun
 * @date 2020-04-23
 */
@Controller
@RequestMapping("/admin/report/testBadWaterInfo")
public class TestBadWaterInfoController extends BaseController {
	private String prefix = "admin/report/testBadWaterInfo";

	@Autowired
	private ITestBadWaterInfoService testBadWaterInfofoService;

	@RequiresPermissions("report:testBadWaterInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询化验数据信息(污水处理厂/排水厂)列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(TestBadWaterInfo testBadWaterInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		testBadWaterInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有
		
			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				testBadWaterInfo.setParams(params);
			} else {
				testBadWaterInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<TestBadWaterInfo> list = testBadWaterInfofoService.getList(testBadWaterInfo);
		return getDataTable(list);
	}

	/**
	 * 新增化验数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:testBadWaterInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		TestBadWaterInfo testBadWaterInfo = new TestBadWaterInfo();
		testBadWaterInfo.setFillDate(DateUtils.getDate());
		testBadWaterInfo.setFactoryId(ShiroUtils.getSysUser().getSysOrgs().get(0).getOrgId());
		testBadWaterInfo.setEffectIcon("1");
		List<TestBadWaterInfo> testBadWaterInfoList = testBadWaterInfofoService.getList(testBadWaterInfo);
		if (StringUtils.isNotEmpty(testBadWaterInfoList) && testBadWaterInfoList.size() > 0)
			mMap.put("testBadWaterInfo", testBadWaterInfoList.get(0));
		else
			mMap.put("testBadWaterInfo", new TestBadWaterInfo());
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
			String msg = ("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报"
					: (("del").equals(operateIcon) ? "删除" : "导出");
			if (!("3").equals(curOrg.getOrgType())) {
				return warn("您不是水厂用户，不能进行数据" + msg + "！");
			}
			if (StringUtils.isNotNull(curOrg.getFactoryType()) && !("1").equals(curOrg.getFactoryType())) {
				return warn("您所在水厂不是污水厂/排水厂，不能进行数据" + msg + "！");
			}
			if (("add").equals(operateIcon)) {
				TestBadWaterInfo testBadWaterInfo = new TestBadWaterInfo();
				testBadWaterInfo.setFillDate(DateUtils.getDate());
				testBadWaterInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				testBadWaterInfo.setEffectIcon("1");
				List<TestBadWaterInfo> overList = testBadWaterInfofoService.getList(testBadWaterInfo);
				if (StringUtils.isNotEmpty(overList) && overList.size() > 0) {
					return error("今日已经填报过信息，是否继续填报？", overList.get(0));
				}
			}
		} else {
			return warn("您还未分配机构！");
		}
		return success();
	}

	/**
	 * 新增保存(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:testBadWaterInfo:add")
	@Log(title = "化验数据信息(污水处理厂/排水厂)", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(TestBadWaterInfo testBadWaterInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		testBadWaterInfo.setFillUserId(curUser.getUserId());
		testBadWaterInfo.setFillUserName(curUser.getUserName());
		testBadWaterInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		testBadWaterInfo.setAreaId(curUser.getAreaStr());
		return toAjax(testBadWaterInfofoService.add(testBadWaterInfo));
	}

	/**
	 * 修改化验数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:testBadWaterInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		TestBadWaterInfo testBadWaterInfo = testBadWaterInfofoService.getEntityById(id);
		mmap.put("testBadWaterInfo", testBadWaterInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存化验数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:testBadWaterInfo:edit")
	@Log(title = "化验数据信息(污水处理厂/排水厂)", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(TestBadWaterInfo testBadWaterInfo) {
		return toAjax(testBadWaterInfofoService.update(testBadWaterInfo));
	}

	/**
	 * 删除化验数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:testBadWaterInfo:delete")
	@Log(title = "化验数据信息(污水处理厂/排水厂)", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(testBadWaterInfofoService.deleteByIds(ids));
	}

	/**
	 * 获取数量
	 */
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(TestBadWaterInfo testBadWaterInfo) {
		return testBadWaterInfofoService.getCount(testBadWaterInfo);
	}
}