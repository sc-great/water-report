package com.boot.web.controller.admin.materialControl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.materialControl.domain.Consumable;
import com.boot.materialControl.domain.ConsumableType;
import com.boot.materialControl.service.ConsumableService;
import com.boot.materialControl.service.ConsumableTypeService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.boot.common.annotation.Log;
import com.boot.common.enums.BusinessType;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;

/**
 * 备品备件Controller
 * 
 * @author LM
 * @date 2020-04-21
 */
@Controller
@RequestMapping("/admin/materialControl/consumable")
public class ConsumableController extends BaseController {
	
	private String prefix = "admin/materialControl/consumable";

	@Autowired
	private ConsumableService consumableService;
	
	@Autowired
	private ConsumableTypeService typeService;
	
	@RequiresPermissions("materialControl:consumable:view")
	@RequestMapping()
	public String list(Consumable consumable, ModelMap mMap) {
		mMap.put("consumable", consumable);
		return prefix + "/list";
	}
	
	/**
	 * 查询备品备件列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(Consumable consumable) {
		SysUser sysUser = ShiroUtils.getSysUser();
		consumable.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				consumable.setParams(params);
			} else {
				consumable.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<Consumable> list = consumableService.getList(consumable);
		return getDataTable(list);
	}
	
	@RequestMapping("/checkIsOver")
	@ResponseBody
	public AjaxResult checkIsOver(@RequestParam("operateIcon") String operateIcon) {
		SysUser curUser = ShiroUtils.getSysUser();
		if ("export".equals(operateIcon)) {
			return success();
		}
		if ("1".equals(curUser.getUserId())) { // 超级管理员
			if (("add").equals(operateIcon)) {
				return warn("超级管理员也不能随便添加数据哟！");
			}
			return success();
		}
		List<SysOrg> sysOrgs = curUser.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgs)) {
			SysOrg curOrg = sysOrgs.get(0);
			if (!("3").equals(curOrg.getOrgType())) {
				return warn("您不是水厂用户，不能进行数据" + (("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报"
						: (("del").equals(operateIcon) ? "删除" : "导出")) + "！");
			}
		} else {
			return warn("您还未分配机构！");
		}
		return success();
	}
	
	/**
	 * 加载物品名称的下拉列表
	 */
	@RequestMapping("/loadOption")
	@ResponseBody
	public AjaxResult loadOption() {
		return success("ok", typeService.getList(new ConsumableType()));
	}
	
	/**
	 * 打开添加页面
	 */
	@RequiresPermissions("materialControl:consumable:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		return prefix + "/add";
	}
	
	/**
	 * 添加备品备件
	 */
	@RequiresPermissions("materialControl:consumable:add")
	@Log(title = "添加备品备件", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(Consumable consumable) {
		SysUser curUser = ShiroUtils.getSysUser();
		consumable.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		consumable.setAreaId(curUser.getAreaStr());
		return toAjax(consumableService.add(consumable, curUser));
	}
	
	/**
	 * 根据id查询物品
	 */
	@RequestMapping("/getConsumableByTypeId")
	@ResponseBody
	public AjaxResult getConsumableByTypeId(String typeId) {
		Consumable con = new Consumable();
		con.setTypeId(typeId);
		con.setFactoryId(ShiroUtils.getSysUser().getSysOrgs().get(0).getOrgId());
		con.setEffectIcon("1");
		Consumable consumable = consumableService.getEntity(con);
		if (consumable == null)
			consumable = con;
		consumable.setType(typeService.getById(typeId));
		return success("ok", consumable);
	}
	
	/**
	 * 修改备品备件信息
	 */
	@RequiresPermissions("materialControl:consumable:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		Consumable consumable = consumableService.getEntityById(id);
		String name = consumable.getName();
		String model = consumable.getModel();
		model = model == null || "".equals(model) ? "" : ("（" + model + "）");
		name += model;
		consumable.setName(name);
		mmap.put("consumable", consumable);
		return prefix + "/edit";
	}
	
	/**
	 * 修改备品备件信息
	 */
	@RequiresPermissions("materialControl:consumable:edit")
	@Log(title = "修改备品备件信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(Consumable consumable) {
		SysUser curUser = ShiroUtils.getSysUser();
		Consumable con = consumableService.getEntityById(consumable.getId());
		con.setTotal(consumable.getTotal());
		con.setLastSetNum(consumable.getLastSetNum());
		con.setLastGetNum(consumable.getLastGetNum());
		con.setUpdateTime(DateUtils.getDateTime());
		con.setUpdateUserId(curUser.getUserId());
		con.setUpdateBy(curUser.getUserName());
		return toAjax(consumableService.update(con));
	}

	/**
	 * 删除水厂备品备件信息
	 */
	@RequiresPermissions("materialControl:consumable:delete")
	@Log(title = "删除水厂备品备件信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(consumableService.deleteByIds(ids));
	}
	
	/**
	 * 领用备品备件页面
	 */
	@RequiresPermissions("materialControl:consumable:get")
	@GetMapping("/get/{id}")
	public String get(@PathVariable("id") String id, ModelMap model) {
		Consumable consumable = consumableService.getEntityById(id);
		String name = consumable.getName();
		String t_model = consumable.getModel();
		t_model = t_model == null || "".equals(t_model) ? "" : ("（" + t_model + "）");
		name += t_model;
		consumable.setName(name);
		model.put("consumable", consumable);
		return prefix + "/get";
	}
	
	/**
	 * 备品备件领用
	 */
	@RequiresPermissions("materialControl:consumable:get")
	@Log(title = "备品备件领用", businessType = BusinessType.INSERT)
	@PostMapping("/doGet")
	@ResponseBody
	public AjaxResult getConsumable(Consumable consumable) {
		SysUser curUser = ShiroUtils.getSysUser();
		int count = consumableService.get(consumable, curUser);
		if (count == -1)
			return error("现有数量不足以领用，请刷新页面再试！");
		else
			return toAjax(count);
	}
	
	@GetMapping("/info/{id}")
	public String info(@PathVariable("id") String id, ModelMap model) {
		model.put("id", id);
		return prefix + "/info";
	}
	
	/**
	 * 查询备品备件列表
	 */
	@RequestMapping("/doListInfo")
	@ResponseBody
	public TableDataInfo doListInfo(Consumable consumable) {
		Consumable con = consumableService.getEntityById(consumable.getId());
		consumable.setTypeId(con.getTypeId());
		consumable.setFactoryId(con.getFactoryId());
		startPage();
		consumable.setId(""); // 把id设为空，否则只有一条数据
		List<Consumable> list = consumableService.getList(consumable);
		return getDataTable(list);
	}
	
	@RequiresPermissions("materialControl:consumable:type")
	@RequestMapping("/typeList")
	public String typeList() {
		return prefix + "/typeList";
	}
	
	@RequestMapping("/doListType")
	@ResponseBody
	public TableDataInfo doListType(ConsumableType type) {
		return getDataTable(typeService.getList(type));
	}
	
	@RequiresPermissions("materialControl:consumable:type")
	@GetMapping("/typeAdd")
	public String typeAdd() {
		return prefix + "/typeAdd";
	}
	
	@RequestMapping("/checkTypeName")
	@ResponseBody
	public AjaxResult checkTypeName(ConsumableType type) {
		String msg = "";
		if (type.getId() != null && !"".equals(type.getId())) { // 修改时的验证
			Map<String, Object> param = new HashMap<>();
			param.put("updateId", type.getId());
			type.setParams(param);
			type.setId("");
		}
		List<ConsumableType> list = typeService.getList(type);
		if (list != null && list.size() > 0) {
			String model = type.getModel();
			msg = "名称：" + type.getName() + (model == null || "".equals(model) ? "" : ("；型号：" + model)) + "  已经存在！";
		}
		return success("", msg);
	}
	
	@RequiresPermissions("materialControl:consumable:type")
	@Log(title = "添加备品备件类型", businessType = BusinessType.INSERT)
	@PostMapping("/doTypeAdd")
	@ResponseBody
	public AjaxResult doTypeAdd(ConsumableType type) {
		return toAjax(typeService.add(type));
	}
	
	@RequiresPermissions("materialControl:consumable:type")
	@GetMapping("/editType/{id}")
	public String editType(@PathVariable("id") String id, ModelMap mmap) {
		mmap.put("type", typeService.getById(id));
		return prefix + "/typeEdit";
	}
	
	@RequiresPermissions("materialControl:consumable:type")
	@Log(title = "修改备品备件类型", businessType = BusinessType.INSERT)
	@PutMapping("/doTypeEdit")
	@ResponseBody
	public AjaxResult doTypeEdit(ConsumableType type) {
		return toAjax(typeService.update(type));
	}
	
	@RequiresPermissions("materialControl:consumable:type")
	@Log(title = "删除备品备件类型", businessType = BusinessType.DELETE)
	@PostMapping("/doTypeDelete")
	@ResponseBody
	public AjaxResult doTypeDelete(String ids) {
		return toAjax(typeService.deleteByIds(ids));
	}
	
	@RequiresPermissions("materialControl:consumable:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult export(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);
		String idsStr = map.get("ids").toString();

		// 获取需要导出的数据信息
		Consumable cParam = new Consumable();
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr))
			cParam.getParams().put("ids", idsStr.split(","));
		else
			return AjaxResult.warn("数据为空！");
		
		List<Consumable> list = consumableService.getList(cParam);
		
		if (list.size() > 0) {
			
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/consumable.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-备品备件";

				Row row;
				Cell cell;

				// 设置标题名称
				Row titleRow = sheet.getRow(0);
				Cell titleCell = titleRow.getCell(0);
				titleCell.setCellValue(sheetName);
				// 循环插入数据
				int lastRow = sheet.getLastRowNum(); // 插入数据的数据ROW
				CellStyle cs = ExcelUtil.setSimpleCellStyle(wb); // 获取模板Excel单元格样式
				// 垂直、水平居中
				cs.setAlignment(HorizontalAlignment.CENTER);
				cs.setVerticalAlignment(VerticalAlignment.CENTER);
				
				for (int i = 0; i < list.size(); i ++) {
					row = sheet.createRow(lastRow + i); // 创建新的ROW，用于数据插入
					row.setHeightInPoints(30);

					// 按项目实际需求，在该处将对象数据插入到Excel中
					Consumable mapCell = list.get(i);
					
					if (StringUtils.isNull(mapCell))
						break;

					// Cell赋值开始
					cell = row.createCell(0);
					cell.setCellValue(i + 1);
					cell.setCellStyle(cs);
					// 公司
					cell = row.createCell(1);
					cell.setCellValue(mapCell.getFactoryName());
					cell.setCellStyle(cs);
					// 名称
					cell = row.createCell(2);
					String name = mapCell.getName();
					String model = mapCell.getModel();
					if (model != null && !"".equals(model) && !"".equals(model.trim()))
						name += ("（" + model.trim() + "）");
					cell.setCellValue(name);
					cell.setCellStyle(cs);
					// 库存（PCS）
					cell = row.createCell(3);
					cell.setCellValue(mapCell.getTotal());
					cell.setCellStyle(cs);
					// 最近入库（PCS）
					cell = row.createCell(4);
					cell.setCellValue(mapCell.getLastSetNum());
					cell.setCellStyle(cs);
					// 入库时间
					cell = row.createCell(5);
					cell.setCellValue(mapCell.getLastSetTime());
					cell.setCellStyle(cs);
					// 入库人员
					cell = row.createCell(6);
					cell.setCellValue(mapCell.getLastSetUserName());
					cell.setCellStyle(cs);
					// 最近领用（PCS）
					cell = row.createCell(7);
					cell.setCellValue(mapCell.getLastGetNum());
					cell.setCellStyle(cs);
					// 领用时间
					cell = row.createCell(8);
					cell.setCellValue(mapCell.getLastGetTime());
					cell.setCellStyle(cs);
					// 领用人员
					cell = row.createCell(9);
					cell.setCellValue(mapCell.getLastGetUserName());
					cell.setCellStyle(cs);
					// 填报日期
					cell = row.createCell(10);
					cell.setCellValue(mapCell.getFillDate());
					cell.setCellStyle(cs);
				}
				filename = sheetName + "（" + DateUtils.getDate() + "）.xlsx";
				os = new FileOutputStream(ExcelUtil.getAbsoluteFile(filename));
				wb.write(os);
			} catch (Exception e) {
				e.printStackTrace();
				return AjaxResult.warn(e.toString());
			} finally {
				if (os != null) {
					os.flush();
					os.close();
					wb.close();
				}
			}
			return AjaxResult.success(filename);
		} else {
			return AjaxResult.warn("未找到相关数据信息！");
		}
	}
}