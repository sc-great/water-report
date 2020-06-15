package com.boot.web.controller.admin.materialControl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.boot.materialControl.domain.MandatoryCheckInfo;
import com.boot.materialControl.service.IMandatoryCheckInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

/**
 * 强制检测填报信息Controller
 * 
 * @author yangxiaojun
 * @date 2020-04-24
 */
@Controller
@RequestMapping("/admin/materialControl/mandatoryCheckInfo")
public class MandatoryCheckInfoController extends BaseController {
	private String prefix = "admin/materialControl/mandatoryCheckInfo";

	@Autowired
	private IMandatoryCheckInfoService mandatoryCheckInfoService;

	@RequiresPermissions("materialControl:mandatoryCheckInfo:view")
	@RequestMapping()
	public String list(MandatoryCheckInfo info, ModelMap mMap) {
		mMap.put("info", info);
		return prefix + "/list";
	}

	/**
	 * 查询强制检测信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(MandatoryCheckInfo mandatoryCheckInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		mandatoryCheckInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				mandatoryCheckInfo.setParams(params);
			} else {
				mandatoryCheckInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<MandatoryCheckInfo> list = mandatoryCheckInfoService.getList(mandatoryCheckInfo);
		return getDataTable(list);
	}

	/**
	 * 新增强制检测信息
	 */
	@RequiresPermissions("materialControl:mandatoryCheckInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
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
		if ("export".equals(operateIcon)) {
			return success();
		}
		if (StringUtils.isNotEmpty(sysOrgs)) {
			String msg = ("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报" : (("del").equals(operateIcon) ? "删除" : "导出");
			if (!("3").equals(sysOrgs.get(0).getOrgType()))
				return warn("您不是水厂用户，不能进行数据" + msg + "！");
		} else {
			return warn("您还未分配机构！");
		}
		return success();
	}

	/**
	 * 新增保存
	 */
	@RequiresPermissions("materialControl:mandatoryCheckInfo:add")
	@Log(title = "强制检测信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(MandatoryCheckInfo mandatoryCheckInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		mandatoryCheckInfo.setFillUserId(curUser.getUserId());
		mandatoryCheckInfo.setFillUserName(curUser.getUserName());
		mandatoryCheckInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		mandatoryCheckInfo.setAreaId(curUser.getAreaStr());
		return toAjax(mandatoryCheckInfoService.add(mandatoryCheckInfo));
	}

	/**
	 * 修改强制检测信息
	 */
	@RequiresPermissions("materialControl:mandatoryCheckInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		MandatoryCheckInfo mandatoryCheckInfo = mandatoryCheckInfoService.getEntityById(id);
		mmap.put("mandatoryCheckInfo", mandatoryCheckInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存强制检测信息
	 */
	@Log(title = "强制检测信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(MandatoryCheckInfo mandatoryCheckInfo) {
		return toAjax(mandatoryCheckInfoService.update(mandatoryCheckInfo));
	}

	/**
	 * 删除强制检测信息
	 */
	@RequiresPermissions("materialControl:mandatoryCheckInfo:delete")
	@Log(title = "强制检测信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(mandatoryCheckInfoService.deleteByIds(ids));
	}

	/**
	 * 获取数量
	 */
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(MandatoryCheckInfo mandatoryCheckInfo) {
		return mandatoryCheckInfoService.getCount(mandatoryCheckInfo);
	}
	
	/**
	 * 弹出延期操作框
	 */
	@RequiresPermissions("materialControl:mandatoryCheckInfo:delay")
	@GetMapping("/delay/{id}")
	public String delay(@PathVariable("id") String id, ModelMap mmap) {
		mmap.put("mandatoryCheckInfo", mandatoryCheckInfoService.getEntityById(id));
		return prefix + "/delay";
	}
	
	/**
	 * 处理事件的检查
	 */
	@RequestMapping("/checkForDispose")
	@ResponseBody
	public AjaxResult checkForDispose(@RequestParam("id") String id) {
		SysUser curUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgs = curUser.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgs)) {
			if (!("3").equals(sysOrgs.get(0).getOrgType()))
				return warn("您不是水厂用户，不能处理该数据！");
			
			MandatoryCheckInfo info = mandatoryCheckInfoService.getEntityById(id);
			int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime("yyyy-MM-dd", info.getNextCheckDate()), DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
			if (iDays > 0)
				return error("距离下次检测时间还有" + iDays + "天，是否您确定现在处理吗？");
			return success("OK");
		} else {
			return warn("您还未分配机构！");
		}
	}
	
	/**
	 * 弹出处理操作框
	 */
	@RequiresPermissions("materialControl:mandatoryCheckInfo:dispose")
	@GetMapping("/dispose/{id}")
	public String dispose(@PathVariable("id") String id, ModelMap mmap) {
		MandatoryCheckInfo info = mandatoryCheckInfoService.getEntityById(id);
		info.setCheckDate(DateUtils.getDate());
		mmap.put("mandatoryCheckInfo", info);
		return prefix + "/dispose";
	}
	
	@Log(title = "强制检测信息", businessType = BusinessType.INSERT)
	@PutMapping("/doDispose")
	@ResponseBody
	public AjaxResult doDispose(MandatoryCheckInfo mandatoryCheckInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		mandatoryCheckInfo.setFillUserId(curUser.getUserId());
		mandatoryCheckInfo.setFillUserName(curUser.getUserName());
		mandatoryCheckInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		mandatoryCheckInfo.setAreaId(curUser.getAreaStr());
		return toAjax(mandatoryCheckInfoService.doDispose(mandatoryCheckInfo));
	}
	
	@RequiresPermissions("materialControl:mandatoryCheckInfo:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult export(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);
		String idsStr = map.get("ids").toString();

		// 获取需要导出的数据信息
		MandatoryCheckInfo infoParam = new MandatoryCheckInfo();
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr))
			infoParam.getParams().put("ids", idsStr.split(","));
		else
			return AjaxResult.warn("数据为空！");
		
		List<MandatoryCheckInfo> list = mandatoryCheckInfoService.getList(infoParam);
		
		if (list.size() > 0) {
			
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/mandatoryCheckInfo.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-强制检测";

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
					MandatoryCheckInfo mapCell = list.get(i);
					
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
					// 填报日期
					cell = row.createCell(2);
					cell.setCellValue(mapCell.getFillDate());
					cell.setCellStyle(cs);
					// 检测日期
					cell = row.createCell(3);
					cell.setCellValue(mapCell.getCheckDate());
					cell.setCellStyle(cs);
					// 检测周期（天）
					cell = row.createCell(4);
					cell.setCellValue(mapCell.getCheckCycle());
					cell.setCellStyle(cs);
					// 下次检测日期
					cell = row.createCell(5);
					int iDays = DateUtils.getDateIntervalDays(DateUtils.dateTime(DateUtils.YYYY_MM_DD, mapCell.getCheckDate()),
							DateUtils.dateTime(DateUtils.YYYY_MM_DD, mapCell.getNextCheckDate()));
					String nextCheckDate = mapCell.getNextCheckDate();
					if (iDays > 0)
						nextCheckDate += ("（延期 " + iDays + " 天）");
					cell.setCellValue(nextCheckDate);
					cell.setCellStyle(cs);
					// 检测项目
					cell = row.createCell(6);
					cell.setCellValue(mapCell.getCheckProject());
					cell.setCellStyle(cs);
					// 检测内容
					cell = row.createCell(7);
					cell.setCellValue(mapCell.getCheckInfo());
					cell.setCellStyle(cs);
					// 检测结果
					cell = row.createCell(8);
					cell.setCellValue(mapCell.getCheckResult());
					cell.setCellStyle(cs);
					// 备注
					cell = row.createCell(9);
					cell.setCellValue(mapCell.getNote());
					cell.setCellStyle(cs);
					// 填报人
					cell = row.createCell(10);
					cell.setCellValue(mapCell.getFillUserName());
					cell.setCellStyle(cs);
					// 填报时间
					cell = row.createCell(11);
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