package com.boot.web.controller.admin.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.PostMapping;
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
import com.boot.report.domain.AlarmInfo;
import com.boot.report.domain.BadWaterQualityInfo;
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
@RequestMapping("/admin/report/alarmInfo")
public class AlarmInfoController extends BaseController {

	private String prefix = "admin/report/alarmInfo";

	@Autowired
	private AlarmInfoService alarmService;

	@RequiresPermissions("report:alarmInfo:view")
	@RequestMapping()
	public String list(BadWaterQualityInfo info) {
		return prefix + "/list";
	}

	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(AlarmInfo alarm) {
		SysUser sysUser = ShiroUtils.getSysUser();
		alarm.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				alarm.getParams().put("areaIds", sysUser.getAreaStr());
			} else {
				alarm.setOrgId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<AlarmInfo> list = alarmService.getList(alarm);
		return getDataTable(list);
	}

	@ResponseBody
	@PostMapping("/list")
	public AjaxResult list(AlarmInfo alarm) {
		SysUser sysUser = ShiroUtils.getSysUser();
		alarm.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysRole.getRoleKey())) { // 集团领导查所有

			} else if ("6".equals(sysRole.getRoleKey())) { // 区域领导查区域
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

	@RequiresPermissions("report:alarmInfo:delete")
	@Log(title = "报警信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(alarmService.deleteByIds(ids));
	}

	@RequiresPermissions("report:alarmInfo:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult doExport(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);

		String idsStr = map.get("ids").toString();

		List<AlarmInfo> list;
		// 获取需要导出的数据信息
		AlarmInfo alarm = new AlarmInfo();
		alarm.setEffectIcon("1");
		if ("3".equals(sysOrg.getOrgType())) { // 水厂用户
			alarm.setOrgId(sysOrg.getOrgId());
		} else {
			alarm.getParams().put("areaIds", sysUser.getAreaStr());
		}
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr)) {
			alarm.getParams().put("ids", idsStr.split(","));
		}
		list = alarmService.getList(alarm);
		String dateSta = list.get(list.size() - 1).getFillDate();
		String dateEnd = list.get(0).getFillDate();

		if (StringUtils.isNotEmpty(list)) {
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/alarmInfo.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "报警信息";

				Row row;
				Cell cell;

				// 设置标题名称
				Row titleRow = sheet.getRow(0);
				Cell titleCell = titleRow.getCell(0);
				titleCell.setCellValue(sheetName);
				
				// 第二行
				// 数据时间段
				Row secondRow = sheet.getRow(1);
				Cell periodCell = secondRow.getCell(0);
				String[] dateSta_array = dateSta.split("-");
				String[] dateEnd_array = dateEnd.split("-");
				periodCell.setCellValue("时间：" + dateSta_array[0] + "年" + dateSta_array[1] + "月" + dateSta_array[2] + "日-" + dateEnd_array[0] + "年" +dateEnd_array[1] + "月" + dateEnd_array[2] + "日");
				// 导出时间  操作人员
				Cell exportDateCell = secondRow.getCell(4);
				exportDateCell.setCellValue("日期：" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + " 操作者：" + sysUser.getUserName());

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
					AlarmInfo alarmCell = list.get(i);
					if (StringUtils.isNull(alarmCell)) {
						break;
					}
					// Cell赋值开始
					cell = row.createCell(0);
					cell.setCellValue(i + 1);
					cell.setCellStyle(cs);

					cell = row.createCell(1);
					cell.setCellValue(alarmCell.getFillTime());
					cell.setCellStyle(cs);

					cell = row.createCell(2);
					cell.setCellValue(alarmCell.getAlarmItem());
					cell.setCellStyle(cs);

					cell = row.createCell(3);
					cell.setCellValue(alarmCell.getValue());
					cell.setCellStyle(cs);

					cell = row.createCell(4);
					cell.setCellValue(alarmCell.getPaOrgName() + " " + alarmCell.getAreaName() + " " + alarmCell.getOrgName());
					cell.setCellStyle(cs);

					cell = row.createCell(5);
	                if ("1".equals(alarmCell.getAlarmType()))
	                	cell.setCellValue("低库存");
	                if ("2".equals(alarmCell.getAlarmType()))
	                	cell.setCellValue("待检测");
	                if ("3".equals(alarmCell.getAlarmType()))
	                	cell.setCellValue("不达标");
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
			return AjaxResult.warn("未找到报警信息！");
		}
	}
}
