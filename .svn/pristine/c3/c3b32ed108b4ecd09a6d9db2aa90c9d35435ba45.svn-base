package com.boot.web.controller.admin.materialControl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.materialControl.domain.CostInfo;
import com.boot.materialControl.service.CostInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;

/**
 * 费用情况Controller
 * 
 * @author LM
 * @date 2020-04-21
 */
@Controller
@RequestMapping("/admin/materialControl/costInfo")
public class CostInfoController extends BaseController {

	private String prefix = "admin/materialControl/costInfo";

	@Autowired
	private CostInfoService costInfoService;
	@Autowired
	private IAreaService areaService;

	@RequiresPermissions("materialControl:costInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 检查当天是否已经填报过信息
	 */
	@RequestMapping("/checkIsOver")
	@ResponseBody
	public AjaxResult checkIsOver(@RequestParam("operateIcon") String operateIcon) {
		SysUser curUser = ShiroUtils.getSysUser();
		boolean isAreaUser = false;
		boolean isAdmin = false;
		Area area = areaService.getEntityById(curUser.getAreaStr());
		if (area != null) {
			isAreaUser = true;
		}
		if ("1".equals(curUser.getUserId())) {
			isAdmin = true;
		}

		switch (operateIcon) {
		case "add":
			if (!isAreaUser)
				return warn("您不是公司用户，不能进行数据填报！");
			if (!"8".equals(curUser.getSysRoles().get(0).getRoleId()))
				return warn("您不是公司财务，不能进行数据填报！");
			break;
		default:
			if (isAdmin) {
				return success();
			}
			if (!isAreaUser) {
				return warn("您不是公司用户，不能进行数据" + (("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报"
						: (("del").equals(operateIcon) ? "删除" : "导出")) + "！");
			}
			if (!"8".equals(curUser.getSysRoles().get(0).getRoleId())) {
				return warn("您不是公司财务，不能进行数据" + (("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报"
						: (("del").equals(operateIcon) ? "删除" : "导出")) + "！");
			}
			break;
		}

		if (("add").equals(operateIcon)) {
			CostInfo costInfo = new CostInfo();
			costInfo.setAreaId(curUser.getAreaStr());
			costInfo.setEffectIcon("1");
			CostInfo last = costInfoService.getLast(costInfo);
			if (last != null) {
				Date date = DateUtils.dateTime("yyyy-MM-dd", last.getFillTime());
				if (!DateUtils.isThisMonth(date)) { // 判断数据是不是本月
					last.setCurrentEnter(0.0);
					last.setCurrentTotal(0.0);
					if (!DateUtils.isThisYear(date)) { // 判断数据是不是本年
						last.setLastYear(last.getThisYear());
						last.setThisYear(0.0);
					}
					// 本月无填报，但是有历史数据
					return success("OK", last);
				}
				return error("当前水厂本月已经填报过信息，是否继续填报？", last);
			}
		}
		// 本月没数据，上月也没有数据
		return success();
	}

	/**
	 * 当月费用填报
	 */
	@RequiresPermissions("materialControl:costInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		return prefix + "/add";
	}

	/**
	 * 新增保存当月费用填报
	 */
	@RequiresPermissions("materialControl:costInfo:add")
	@Log(title = "当月费用填报", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(CostInfo costInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		costInfo.setFillDate(DateUtils.getMouth(new Date()));
		costInfo.setFillUserId(curUser.getUserId());
		costInfo.setFillUserName(curUser.getUserName());
		costInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		costInfo.setAreaId(curUser.getAreaStr());
		
		CostInfo ci = new CostInfo();
		ci.setAreaId(curUser.getAreaStr());
		ci.setEffectIcon("1");
		CostInfo last = costInfoService.getLast(ci);
		if (last != null) {
			Date date = DateUtils.dateTime("yyyy-MM-dd", last.getFillTime());
			if (DateUtils.isThisMonth(date)) { // 判断数据是不是本月
				double currentTotal = Math.round(costInfo.getCurrentEnter() * 100 + last.getCurrentTotal() * 100) / 100;
				costInfo.setCurrentTotal(currentTotal);
			}
		}
		return toAjax(costInfoService.add(costInfo));
	}

	/**
	 * 查询水厂费用填报信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(CostInfo costInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		costInfo.setEffectIcon("1");
		if ("1".equals(sysUser.getUserId())) { // 超级管理员
			return getDataTable(costInfoService.getList(costInfo));
		}
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导和区域领导
			Map<String, Object> params = new HashMap<>();
			params.put("areaIds", sysUser.getAreaStr());
			costInfo.setParams(params);
		} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导和区域领导
			Map<String, Object> params = new HashMap<>();
			params.put("areaIds", sysUser.getAreaStr());
			costInfo.setParams(params);
		} else if ("8".equals(sysRole.getRoleKey())) { // 公司财务
			costInfo.setAreaId(sysUser.getAreaStr());
		} else {
			throw new BusinessException("对不起，您无此权限！");
		}
		startPage();
		List<CostInfo> list = costInfoService.getList(costInfo);
		return getDataTable(list);
	}
	
	@GetMapping("/info/{id}")
	public String info(@PathVariable("id") String id, ModelMap model) {
		model.put("id", id);
		return prefix + "/info";
	}
	
	@RequestMapping("/doListInfo")
	@ResponseBody
	public TableDataInfo doListInfo(CostInfo costInfo) {
		costInfo = costInfoService.getEntityById(costInfo.getId());
		costInfo.setId("");
		costInfo.setEffectIcon("");
		startPage();
		return getDataTable(costInfoService.getList(costInfo));
	}

	/**
	 * 修改水厂费用填报信息
	 */
	@RequiresPermissions("materialControl:costInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		CostInfo costInfo = costInfoService.getEntityById(id);
		mmap.put("costInfo", costInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂费用信息
	 */
	@RequiresPermissions("materialControl:costInfo:edit")
	@Log(title = "水厂费用信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(CostInfo costInfo) {
		return toAjax(costInfoService.update(costInfo));
	}

	/**
	 * 删除水厂费用信息
	 */
	@RequiresPermissions("materialControl:costInfo:delete")
	@Log(title = "水厂费用信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(costInfoService.deleteByIds(ids));
	}
	
	@RequiresPermissions("materialControl:costInfo:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult export(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);
		String idsStr = map.get("ids").toString();

		// 获取需要导出的数据信息
		CostInfo costInfoParam = new CostInfo();
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr))
			costInfoParam.getParams().put("ids", idsStr.split(","));
		else
			return AjaxResult.warn("数据为空！");
		
		List<CostInfo> list = costInfoService.getList(costInfoParam);
		
		if (list.size() > 0) {
			
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/costInfo.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-收费情况";

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
					CostInfo mapCell = list.get(i);
					
					if (StringUtils.isNull(mapCell))
						break;

					// Cell赋值开始
					cell = row.createCell(0);
					cell.setCellValue(i + 1);
					cell.setCellStyle(cs);
					// 日期
					cell = row.createCell(1);
					cell.setCellValue(mapCell.getFillDate());
					cell.setCellStyle(cs);
					// 公司
					cell = row.createCell(2);
					cell.setCellValue(mapCell.getAreaName());
					cell.setCellStyle(cs);
					// 当年目标（万元）
					cell = row.createCell(3);
					cell.setCellValue(mapCell.getThisYear());
					cell.setCellStyle(cs);
					// 去年目标（万元）
					cell = row.createCell(4);
					cell.setCellValue(mapCell.getLastYear());
					cell.setCellStyle(cs);
					// 本月到账（万元）
					cell = row.createCell(5);
					cell.setCellValue(mapCell.getCurrentEnter());
					cell.setCellStyle(cs);
					// 本月累计（万元）
					cell = row.createCell(6);
					cell.setCellValue(mapCell.getCurrentTotal());
					cell.setCellStyle(cs);
					// 填报人
					cell = row.createCell(7);
					cell.setCellValue(mapCell.getFillUserName());
					cell.setCellStyle(cs);
					// 填报时间
					cell = row.createCell(8);
					cell.setCellValue(mapCell.getFillTime());
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
