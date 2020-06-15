package com.boot.web.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.boot.common.annotation.Log;
import com.boot.common.enums.BusinessType;
import com.boot.system.domain.Area;
import com.boot.system.service.IAreaService;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.common.core.page.TableDataInfo;

/**
 * 区域Controller
 * 
 * @author EPL
 * @date 2020-03-23
 */
@Api(value = "/admin/system/area", description = "区域")
@Controller
@RequestMapping("/admin/system/area")
public class AreaController extends BaseController {
	private String prefix = "admin/system/area";

	@Autowired
	private IAreaService areaService;

	@RequiresPermissions("system:area:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询区域列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(Area area) {
		startPage();
		List<Area> list = areaService.getList(area);
		return getDataTable(list);
	}

	/**
	 * 导出区域列表
	 */
	@RequiresPermissions("system:area:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult doExport(Area area) {
		List<Area> list = areaService.getList(area);
		ExcelUtil<Area> util = new ExcelUtil<Area>(Area.class);
		return util.exportExcel(list, "area");
	}

	/**
	 * 新增区域
	 */
	@RequiresPermissions("system:area:add")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存区域
	 */
	@RequiresPermissions("system:area:add")
	@Log(title = "区域", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(Area area) {
		return toAjax(areaService.add(area));
	}

	/**
	 * 修改区域
	 */
	@RequiresPermissions("system:area:edit")
	@GetMapping("/edit/{areaId}")
	public String edit(@PathVariable("areaId") String areaId, ModelMap mmap) {
		Area area = areaService.getEntityById(areaId);
		mmap.put("area", area);
		return prefix + "/edit";
	}

	/**
	 * 修改保存区域
	 */
	@RequiresPermissions("system:area:edit")
	@Log(title = "区域", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(Area area) {
		return toAjax(areaService.update(area));
	}

	/**
	 * 删除区域
	 */
	@RequiresPermissions("system:area:delete")
	@Log(title = "区域", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(areaService.deleteByIds(ids));
	}

	/**
	* 获取数量
	*/
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(Area area) {
		int count = areaService.getCount(area);
		return count;
	}

	/**
	 * 加载部门下拉树
	 */
	@RequestMapping("/treeList")
	@ResponseBody
	public JSONArray treeList() {
		List<Area> areaList = areaService.getList(new Area());
		JSONArray jsonArray = new JSONArray();
		for (Area item : areaList) {
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(item);
			jsonObject.put("childCount", 0);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
}