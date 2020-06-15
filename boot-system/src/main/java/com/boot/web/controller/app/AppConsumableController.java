package com.boot.web.controller.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.text.Convert;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.StringUtils;
import com.boot.materialControl.domain.Consumable;
import com.boot.materialControl.domain.ConsumableType;
import com.boot.materialControl.service.ConsumableService;
import com.boot.materialControl.service.ConsumableTypeService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysDictDataService;
import com.boot.system.service.ISysOrgService;

/**
 * 手机端备品备件Controller
 * @author LM
 *
 */
@Controller
@RequestMapping("/app")
public class AppConsumableController extends APPBaseController {

	private String prefix = "app/pages";
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysDictDataService dictDataService;
	@Autowired
	private ConsumableService consumableService;
	@Autowired
	private ConsumableTypeService consumableTypeService;
	@Autowired
	private ISysOrgService orgService;

	@GetMapping("/consumable")
	public String consumable(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);

		SysOrg sysOrg = getLoginUserOrg();
		if ("3".equals(sysOrg.getOrgType())) { // 水厂领导（水厂只会有一个区域）
			Area area = areaService.getEntityById(areaIds);
			if (area != null) {
				mMap.put("area", area);
			}
			return prefix + "/consumable_companys";
		} else {
			return prefix + "/consumable";
		}
	}

	@RequestMapping("/consumableData")
	@ResponseBody
	public AjaxResult consumableData(String date) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			// 查询条件
			Map<String, Object> params = new HashMap<>();
			params.put("typeList", consumableTypeService.getList(new ConsumableType()));
			Consumable consumable = new Consumable();
			consumable.setParams(params);
			consumable.setEffectIcon("1");
			consumable.setFillDate(date);
			for (Area item : areaList) {
				consumable.setAreaId(item.getAreaId());
				List<Map<String, Object>> sumConsumable = consumableService.getSum(consumable);
				item.getParams().put("sumConsumable", sumConsumable);
			}
			return AjaxResult.success(areaList);
		}
		return AjaxResult.error();
	}
	
	@GetMapping("/consumableArea/{areaGroupTypeId}")
	public String sludgeCompanys(@PathVariable("areaGroupTypeId") String areaGroupTypeId, ModelMap mMap) {
		String areaGroupName = dictDataService.selectDictLabel("r_group_type", areaGroupTypeId);
		mMap.put("areaGroupName", areaGroupName);
		List<String> areaIds = new ArrayList<>();
		Area area = new Area();
		area.setGroupType(areaGroupTypeId);
		List<Area> areaList = areaService.getList(area);
		// 当前用户管理区域
		String user_areaIds = getLoginUserInfo().getAreaStr();
		for (Area item : areaList) {
			if (user_areaIds.indexOf(item.getAreaId()) != -1) {
				areaIds.add(item.getAreaId());
			}
		}
		mMap.put("areaIds", StringUtils.join(areaIds, ","));
		return prefix + "/consumable_area";
	}
	
	@RequestMapping("/consumableAreaData")
	@ResponseBody
	public AjaxResult consumableAreaData(String areaIds, String date) {
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			// 查询条件
			Map<String, Object> params = new HashMap<>();
			params.put("typeList", consumableTypeService.getList(new ConsumableType()));
			Consumable consumable = new Consumable();
			consumable.setParams(params);
			consumable.setEffectIcon("1");
			consumable.setFillDate(date);
			for (Area item : areaList) {
				consumable.setAreaId(item.getAreaId());
				List<Map<String, Object>> sumConsumable = consumableService.getSum(consumable);
				item.getParams().put("sumConsumable", sumConsumable);
			}
			return AjaxResult.success(areaList);
		}
		return AjaxResult.error();
	}
	
	@GetMapping("/consumableCompanys/{areaId}")
	public String consumableCompanys(@PathVariable("areaId") String areaId, ModelMap mMap) {
		Area area = areaService.getEntityById(areaId);
		if (area != null) {
			mMap.put("area", area);
		}
		return prefix + "/consumable_companys";
	}
	
	@RequestMapping("/consumableCompanysData")
	@ResponseBody
	public AjaxResult consumableCompanysData(String areaId, String date) {
		List<SysOrg> orgList = null;
		SysOrg userOrg = getLoginUserOrg();
		if ("3".equals(userOrg.getOrgType())) { // 水厂用户
			orgList = new ArrayList<>();
			orgList.add(userOrg);
		} else {
			// 当前用户管理区域
			SysOrg sysOrg_params = new SysOrg();
			sysOrg_params.setAreaId(areaId);
			orgList = orgService.findListByFactory(sysOrg_params);
		}
		
		if (orgList == null)
			return AjaxResult.error();
		
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("typeList", consumableTypeService.getList(new ConsumableType()));
		Consumable consumable = new Consumable();
		consumable.setParams(params);
		consumable.setEffectIcon("1");
		consumable.setFillDate(date);
		for (SysOrg item : orgList) {
			consumable.setFactoryId(item.getOrgId());
			List<Map<String, Object>> sumConsumable = consumableService.getSum(consumable);
			item.getParams().put("sumConsumable", sumConsumable);
		}
		return AjaxResult.success(orgList);
	}
	
	/**
	 * 填报人员进入填报页面
	 */
	@GetMapping("/consumableFill")
	public String consumableFill() {

		return prefix + "/consumable_fill";
	}
	
	/**
	 * 查询本水厂下的所有有效信息
	 */
	@RequestMapping("/consumableFactoryData")
	@ResponseBody
	public AjaxResult consumableFactoryData() {
		Consumable consumable = new Consumable();
		consumable.setEffectIcon("1");
		consumable.setFactoryId(getLoginUserOrg().getOrgId());
		return AjaxResult.success(consumableService.getList(consumable));
	}
	
	/**
	 * 加载物品名称的下拉列表
	 */
	@RequestMapping("/loadOption")
	@ResponseBody
	public AjaxResult loadOption() {
		return AjaxResult.success("ok", consumableTypeService.getList(new ConsumableType()));
	}
	
	/**
	 * 根据id查询物品
	 */
	@RequestMapping("/getConsumableByTypeId")
	@ResponseBody
	public AjaxResult getConsumableByTypeId(String typeId) {
		Consumable con = new Consumable();
		con.setTypeId(typeId);
		con.setFactoryId(getLoginUserOrg().getOrgId());
		con.setEffectIcon("1");
		Consumable consumable = consumableService.getEntity(con);
		if (consumable == null)
			consumable = con;
		consumable.setType(consumableTypeService.getById(typeId));
		return AjaxResult.success("ok", consumable);
	}
	
	/**
	 * 添加
	 * @param consumable
	 * @return
	 */
	@Log(title = "添加备品备件", businessType = BusinessType.INSERT)
	@PostMapping("/consumableset")
	@ResponseBody
	public AjaxResult doAdd(Consumable consumable) {
		SysUser curUser = getLoginUserInfo();
		consumable.setFactoryId(getLoginUserOrg().getOrgId());
		consumable.setAreaId(curUser.getAreaStr());
		consumable.setEffectIcon("1");
		return AjaxResult.success(consumableService.add(consumable, curUser));
	}
	
	/**
	 * 备品备件领用
	 */
	@Log(title = "备品备件领用", businessType = BusinessType.INSERT)
	@PostMapping("/consumableget")
	@ResponseBody
	public AjaxResult getConsumable(Consumable consumable) {
		SysUser curUser = getLoginUserInfo();
		consumable.setFactoryId(getLoginUserOrg().getOrgId());
		consumable.setAreaId(curUser.getAreaStr());
		consumable.setEffectIcon("1");
		return AjaxResult.success(consumableService.get(consumable, curUser));
	}
}
