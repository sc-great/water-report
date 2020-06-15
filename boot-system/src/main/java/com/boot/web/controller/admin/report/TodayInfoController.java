package com.boot.web.controller.admin.report;

import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.page.TableDataInfo;
import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.service.ITodayInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 今日数据信息Controller
 *
 * @author EPL
 * @date 2020-03-24
 */
@Controller
@RequestMapping("/admin/report/todayInfo")
public class TodayInfoController extends BaseController {
	private String prefix = "admin/report/todayInfo";

	@Autowired
	private ITodayInfoService todayInfoService;

	@RequiresPermissions("report:todayInfo:view")
	@RequestMapping()
	public String list() {
		SysUser user = ShiroUtils.getSysUser();
		if ("2".equals(user.getSysRoles().get(0).getRoleKey())) // 超级管理员
			throw new BusinessException("超级管理员不可查看！");
		if ("1".equals(user.getSysRoles().get(0).getRoleKey())) // 集团领导
			throw new BusinessException("集团领导不可查看！");
		if ("6".equals(user.getSysRoles().get(0).getRoleKey())) // 区域领导
			throw new BusinessException("区域领导不可查看！");
		
		List<SysOrg> sysOrgList = user.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgList)) {
			// 污水厂
			if ("1".equals(sysOrgList.get(0).getFactoryType())) {
				return prefix + "/listWS";
			}
			// 供水厂
			if ("2".equals(sysOrgList.get(0).getFactoryType())) {
				return prefix + "/listGS";
			}
		}
		throw new BusinessException("您还未分配机构！");
	}

	/**
	 * 查询今日数据信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(ReportDateInfo reportDateInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgList)) {
			reportDateInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			Map<String, Object> params = new HashMap<>();
			params.put("type", sysOrgList.get(0).getFactoryType());
			reportDateInfo.setParams(params);
			List<Map<String, Object>> list = todayInfoService.getList(reportDateInfo);
			return getDataTable(list);
		} else {
			throw new BusinessException("您还未分配机构！");
		}
	}

	/**
	 * 判断当前员工状态
	 */
	@RequestMapping("/checkIsOver")
	@ResponseBody
	public AjaxResult checkIsOver(@RequestParam("operateIcon") String operateIcon) {
		SysUser curUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgs = curUser.getSysOrgs();
		if (StringUtils.isNotEmpty(sysOrgs)) {
			SysOrg curOrg = sysOrgs.get(0);
			if (!("3").equals(curOrg.getOrgType())) {
				return warn("您不是水厂用户，不能进行数据导出！");
			}
		} else {
			return warn("您还未分配机构！");
		}
		return success();
	}

	/**
	 * 导出今日数据信息——供水厂
	 */
	@RequiresPermissions("report:todayInfo:export")
	@RequestMapping("/exportGS")
	@ResponseBody
	public AjaxResult doExportGS(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);

		String idsStr = map.get("ids").toString();

		List<Map<String, Object>> list;
		// 获取需要导出的数据信息
		ReportDateInfo reportDateInfoParam = new ReportDateInfo();
		reportDateInfoParam.setFactoryId(sysOrg.getOrgId());
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr)) {
			reportDateInfoParam.getParams().put("ids", idsStr.split(","));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("type", sysOrg.getFactoryType());
		reportDateInfoParam.setParams(params);
		list = todayInfoService.getList(reportDateInfoParam);
		if (list.size() > 0) {
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/mixInfoGS.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-水质/污泥量/电量/药量填报数据";

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
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(lastRow + i); // 创建新的ROW，用于数据插入
					row.setHeightInPoints(30);

					// 按项目实际需求，在该处将对象数据插入到Excel中
					Map<String, Object> mapCell = list.get(i);
					if (StringUtils.isNull(mapCell)) {
						break;
					}

					// Cell赋值开始
					cell = row.createCell(0);

					cell.setCellValue(mapCell.get("reportDate").toString());
					cell.setCellStyle(cs);

					cell = row.createCell(1);
					if (mapCell.get("NTU_in") != null) {
						cell.setCellValue(mapCell.get("NTU_in").toString());
					} else {

						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(2);
					if (mapCell.get("NTU_out") != null) {
						cell.setCellValue(mapCell.get("NTU_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(3);
					if (mapCell.get("PH_in") != null) {
						cell.setCellValue(mapCell.get("PH_in").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(4);
					if (mapCell.get("PH_out") != null) {
						cell.setCellValue(mapCell.get("PH_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(5);
					if (mapCell.get("COL2_out") != null) {
						cell.setCellValue(mapCell.get("COL2_out").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(6);
					if (mapCell.get("germ_out") != null) {
						cell.setCellValue(mapCell.get("germ_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(7);
					if (mapCell.get("today_mud") != null) {
						cell.setCellValue(mapCell.get("today_mud").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(8);
					if (mapCell.get("total_mud") != null) {
						cell.setCellValue(mapCell.get("total_mud").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(9);
					if (mapCell.get("todayElectricity") != null) {
						cell.setCellValue(mapCell.get("todayElectricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(10);
					if (mapCell.get("totalElectricity") != null) {
						cell.setCellValue(mapCell.get("totalElectricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(11);
					if (mapCell.get("pumpTodayEletricity") != null) {
						cell.setCellValue(mapCell.get("pumpTodayEletricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(12);
					if (mapCell.get("pumpTotalEletricity") != null) {
						cell.setCellValue(mapCell.get("pumpTotalEletricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(13);
					if (mapCell.get("todayPac") != null) {
						cell.setCellValue(mapCell.get("todayPac").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(14);
					if (mapCell.get("totalPac") != null) {
						cell.setCellValue(mapCell.get("totalPac").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(15);
					if (mapCell.get("todayPamYin") != null) {
						cell.setCellValue(mapCell.get("todayPamYin").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(16);
					if (mapCell.get("totalPamYin") != null) {
						cell.setCellValue(mapCell.get("totalPamYin").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(17);
					if (mapCell.get("todayPamYang") != null) {
						cell.setCellValue(mapCell.get("todayPamYang").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(18);
					if (mapCell.get("totalPamYang") != null) {
						cell.setCellValue(mapCell.get("totalPamYang").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(19);
					if (mapCell.get("todayLime") != null) {
						cell.setCellValue(mapCell.get("todayLime").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(20);
					if (mapCell.get("totalLime") != null) {
						cell.setCellValue(mapCell.get("totalLime").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(21);
					if (mapCell.get("todayPhosphorus") != null) {
						cell.setCellValue(mapCell.get("todayPhosphorus").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(22);
					if (mapCell.get("totalPhosphorus") != null) {
						cell.setCellValue(mapCell.get("totalPhosphorus").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(23);
					if (mapCell.get("todayHCL") != null) {
						cell.setCellValue(mapCell.get("todayHCL").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(24);
					if (mapCell.get("totalHCL") != null) {
						cell.setCellValue(mapCell.get("totalHCL").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(25);
					if (mapCell.get("todaySc") != null) {
						cell.setCellValue(mapCell.get("todaySc").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(26);
					if (mapCell.get("totalSc") != null) {
						cell.setCellValue(mapCell.get("totalSc").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(27);
					if (mapCell.get("todayNaclo") != null) {
						cell.setCellValue(mapCell.get("todayNaclo").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(28);
					if (mapCell.get("totalNaclo") != null) {
						cell.setCellValue(mapCell.get("totalNaclo").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(29);
					if (mapCell.get("todayGlucose") != null) {
						cell.setCellValue(mapCell.get("todayGlucose").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(30);
					if (mapCell.get("totalGlucose") != null) {
						cell.setCellValue(mapCell.get("totalGlucose").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(31);
					if (mapCell.get("todaySa") != null) {
						cell.setCellValue(mapCell.get("todaySa").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(32);
					if (mapCell.get("totalSa") != null) {
						cell.setCellValue(mapCell.get("totalSa").toString());
					} else {
						cell.setCellValue("");
					}

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
	
	/**
	 * 导出今日数据信息——污水厂
	 */
	@RequiresPermissions("report:todayInfo:export")
	@RequestMapping("/exportWS")
	@ResponseBody
	public AjaxResult doExportWS(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);

		String idsStr = map.get("ids").toString();

		List<Map<String, Object>> list;
		// 获取需要导出的数据信息
		ReportDateInfo reportDateInfoParam = new ReportDateInfo();
		reportDateInfoParam.setFactoryId(sysOrg.getOrgId());
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr)) {
			reportDateInfoParam.getParams().put("ids", idsStr.split(","));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("type", sysOrg.getFactoryType());
		reportDateInfoParam.setParams(params);
		list = todayInfoService.getList(reportDateInfoParam);
		if (list.size() > 0) {
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/mixInfoWS.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-水质/污泥量/电量/药量填报数据";

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
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(lastRow + i); // 创建新的ROW，用于数据插入
					row.setHeightInPoints(30);

					// 按项目实际需求，在该处将对象数据插入到Excel中
					Map<String, Object> mapCell = list.get(i);
					if (StringUtils.isNull(mapCell)) {
						break;
					}

					// Cell赋值开始
					cell = row.createCell(0);

					cell.setCellValue(mapCell.get("reportDate").toString());
					cell.setCellStyle(cs);
					
					cell = row.createCell(1);
					if (mapCell.get("COD_in") != null) {
						cell.setCellValue(mapCell.get("COD_in").toString());
					} else {

						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(2);
					if (mapCell.get("COD_out") != null) {
						cell.setCellValue(mapCell.get("COD_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(3);
					if (mapCell.get("NH3_N_in") != null) {
						cell.setCellValue(mapCell.get("NH3_N_in").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(4);
					if (mapCell.get("NH3_N_out") != null) {
						cell.setCellValue(mapCell.get("NH3_N_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(5);
					if (mapCell.get("SS_in") != null) {
						cell.setCellValue(mapCell.get("SS_in").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(6);
					if (mapCell.get("SS_out") != null) {
						cell.setCellValue(mapCell.get("SS_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);
					
					cell = row.createCell(7);
					if (mapCell.get("PH_in") != null) {
						cell.setCellValue(mapCell.get("PH_in").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(8);
					if (mapCell.get("PH_out") != null) {
						cell.setCellValue(mapCell.get("PH_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);
					
					cell = row.createCell(9);
					if (mapCell.get("TP_in") != null) {
						cell.setCellValue(mapCell.get("TP_in").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(10);
					if (mapCell.get("TP_out") != null) {
						cell.setCellValue(mapCell.get("TP_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);
					
					cell = row.createCell(11);
					if (mapCell.get("TN_in") != null) {
						cell.setCellValue(mapCell.get("TN_in").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(12);
					if (mapCell.get("TN_out") != null) {
						cell.setCellValue(mapCell.get("TN_out").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);
					  
					cell = row.createCell(13);
					if (mapCell.get("MLSS_in") != null) {
						cell.setCellValue(mapCell.get("MLSS_in").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(14);
					if (mapCell.get("SV30_in") != null) {
						cell.setCellValue(mapCell.get("SV30_in").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);
					  
					cell = row.createCell(15);
					if (mapCell.get("today_mud") != null) {
						cell.setCellValue(mapCell.get("today_mud").toString());
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(cs);

					cell = row.createCell(16);
					if (mapCell.get("total_mud") != null) {
						cell.setCellValue(mapCell.get("total_mud").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(17);
					if (mapCell.get("todayElectricity") != null) {
						cell.setCellValue(mapCell.get("todayElectricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(18);
					if (mapCell.get("totalElectricity") != null) {
						cell.setCellValue(mapCell.get("totalElectricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(19);
					if (mapCell.get("pumpTodayEletricity") != null) {
						cell.setCellValue(mapCell.get("pumpTodayEletricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(20);
					if (mapCell.get("pumpTotalEletricity") != null) {
						cell.setCellValue(mapCell.get("pumpTotalEletricity").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(21);
					if (mapCell.get("todayPac") != null) {
						cell.setCellValue(mapCell.get("todayPac").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(22);
					if (mapCell.get("totalPac") != null) {
						cell.setCellValue(mapCell.get("totalPac").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(23);
					if (mapCell.get("todayPamYin") != null) {
						cell.setCellValue(mapCell.get("todayPamYin").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(24);
					if (mapCell.get("totalPamYin") != null) {
						cell.setCellValue(mapCell.get("totalPamYin").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(25);
					if (mapCell.get("todayPamYang") != null) {
						cell.setCellValue(mapCell.get("todayPamYang").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(26);
					if (mapCell.get("totalPamYang") != null) {
						cell.setCellValue(mapCell.get("totalPamYang").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(27);
					if (mapCell.get("todayLime") != null) {
						cell.setCellValue(mapCell.get("todayLime").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(28);
					if (mapCell.get("totalLime") != null) {
						cell.setCellValue(mapCell.get("totalLime").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(29);
					if (mapCell.get("todayPhosphorus") != null) {
						cell.setCellValue(mapCell.get("todayPhosphorus").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(30);
					if (mapCell.get("totalPhosphorus") != null) {
						cell.setCellValue(mapCell.get("totalPhosphorus").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(31);
					if (mapCell.get("todayHCL") != null) {
						cell.setCellValue(mapCell.get("todayHCL").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(32);
					if (mapCell.get("totalHCL") != null) {
						cell.setCellValue(mapCell.get("totalHCL").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(33);
					if (mapCell.get("todaySc") != null) {
						cell.setCellValue(mapCell.get("todaySc").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(34);
					if (mapCell.get("totalSc") != null) {
						cell.setCellValue(mapCell.get("totalSc").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(35);
					if (mapCell.get("todayNaclo") != null) {
						cell.setCellValue(mapCell.get("todayNaclo").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(36);
					if (mapCell.get("totalNaclo") != null) {
						cell.setCellValue(mapCell.get("totalNaclo").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(37);
					if (mapCell.get("todayGlucose") != null) {
						cell.setCellValue(mapCell.get("todayGlucose").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(38);
					if (mapCell.get("totalGlucose") != null) {
						cell.setCellValue(mapCell.get("totalGlucose").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(39);
					if (mapCell.get("todaySa") != null) {
						cell.setCellValue(mapCell.get("todaySa").toString());
					} else {
						cell.setCellValue("");
					}

					cell.setCellStyle(cs);

					cell = row.createCell(40);
					if (mapCell.get("totalSa") != null) {
						cell.setCellValue(mapCell.get("totalSa").toString());
					} else {
						cell.setCellValue("");
					}

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