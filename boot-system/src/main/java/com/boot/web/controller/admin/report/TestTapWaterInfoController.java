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
import com.boot.report.domain.TestTapWaterInfo;
import com.boot.report.service.ITestTapWaterInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

/**
 * 水厂化验数据信息（自来水厂/给水厂）Controller
 * 
 * @author yangxiaojun
 * @date 2020-04-26
 */
@Controller
@RequestMapping("/admin/report/testTapWaterInfo")
public class TestTapWaterInfoController extends BaseController {
	private String prefix = "admin/report/testTapWaterInfo";

	@Autowired
	private ITestTapWaterInfoService testTapWaterInfoService;

	@RequiresPermissions("report:testTapWaterInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询水厂化验数据信息（自来水厂/给水厂）列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(TestTapWaterInfo TestTapWaterInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		TestTapWaterInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				TestTapWaterInfo.setParams(params);
			} else {
				TestTapWaterInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<TestTapWaterInfo> list = testTapWaterInfoService.getList(TestTapWaterInfo);
		return getDataTable(list);
	}

	/**
	 * 新增水厂化验数据信息（自来水厂/给水厂）
	 */
	@RequiresPermissions("report:testTapWaterInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		TestTapWaterInfo goodWaterHealthParam = new TestTapWaterInfo();
		goodWaterHealthParam.setFillDate(DateUtils.getDate());
		goodWaterHealthParam.setFactoryId(ShiroUtils.getSysUser().getSysOrgs().get(0).getOrgId());
		goodWaterHealthParam.setEffectIcon("1");
		List<TestTapWaterInfo> testTapWaterInfoList = testTapWaterInfoService.getList(goodWaterHealthParam);
		if (StringUtils.isNotEmpty(testTapWaterInfoList))
			mMap.put("testTapWaterInfo", testTapWaterInfoList.get(0));
		else
			mMap.put("testTapWaterInfo", new TestTapWaterInfo());
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
			if (StringUtils.isNotNull(curOrg.getFactoryType()) && !("2").equals(curOrg.getFactoryType())) {
				return warn("您所在水厂不是自来水厂/给水厂，不能进行数据" + msg + "！");
			}
			if (("add").equals(operateIcon)) {
				TestTapWaterInfo goodWaterHealthParam = new TestTapWaterInfo();
				goodWaterHealthParam.setFillDate(DateUtils.getDate());
				goodWaterHealthParam.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				goodWaterHealthParam.setEffectIcon("1");
				List<TestTapWaterInfo> overList = testTapWaterInfoService.getList(goodWaterHealthParam);
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
	 * 新增保存水厂化验数据信息（自来水厂/给水厂）
	 */
	@RequiresPermissions("report:testTapWaterInfo:add")
	@Log(title = "水厂化验数据信息（自来水厂/给水厂）", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(TestTapWaterInfo testTapWaterInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		testTapWaterInfo.setFillUserId(curUser.getUserId());
		testTapWaterInfo.setFillUserName(curUser.getUserName());
		testTapWaterInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		testTapWaterInfo.setAreaId(curUser.getAreaStr());
		return toAjax(testTapWaterInfoService.add(testTapWaterInfo));
	}

	/**
	 * 修改水厂化验数据信息（自来水厂/给水厂）
	 */
	@RequiresPermissions("report:testTapWaterInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		TestTapWaterInfo testTapWaterInfo = testTapWaterInfoService.getEntityById(id);
		mmap.put("testTapWaterInfo", testTapWaterInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂化验数据信息（自来水厂/给水厂）
	 */
	@RequiresPermissions("report:testTapWaterInfo:edit")
	@Log(title = "水厂化验数据信息（自来水厂/给水厂）", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(TestTapWaterInfo testTapWaterInfo) {
		return toAjax(testTapWaterInfoService.update(testTapWaterInfo));
	}

	/**
	 * 删除水厂化验数据信息（自来水厂/给水厂）
	 */
	@RequiresPermissions("report:testTapWaterInfo:delete")
	@Log(title = "水厂化验数据信息（自来水厂/给水厂）", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(testTapWaterInfoService.deleteByIds(ids));
	}

	/**
	 * 获取数量
	 */
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(TestTapWaterInfo testTapWaterInfo) {
		return testTapWaterInfoService.getCount(testTapWaterInfo);
	}
}