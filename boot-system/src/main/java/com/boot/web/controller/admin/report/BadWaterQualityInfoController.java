package com.boot.web.controller.admin.report;

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
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.service.IBadWaterQualityInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysFactoryInfoService;

/**
 * 水厂水质数据信息(污水处理厂/排水厂)Controller
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Controller
@RequestMapping("/admin/report/badWaterQualityInfo")
public class BadWaterQualityInfoController extends BaseController {
	private String prefix = "admin/report/badWaterQualityInfo";

	@Autowired
	private IBadWaterQualityInfoService badWaterQualityInfoService;

	@Autowired
	private ISysFactoryInfoService sysFactoryInfoService;

	@RequiresPermissions("report:badWaterQualityInfo:view")
	@RequestMapping()
	public String list(BadWaterQualityInfo info, ModelMap mMap) {
		mMap.put("info", info);
		return prefix + "/list";
	}

	/**
	 * 查询水厂水质数据信息(污水处理厂/排水厂)列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(BadWaterQualityInfo badWaterQualityInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		badWaterQualityInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有

			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				badWaterQualityInfo.setParams(params);
			} else {
				badWaterQualityInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<BadWaterQualityInfo> list = badWaterQualityInfoService.getList(badWaterQualityInfo);
		return getDataTable(list);
	}

	/**
	 * 导出水厂水质数据信息(污水处理厂/排水厂)列表
	 */
	@RequiresPermissions("report:badWaterQualityInfo:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult doExport(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);

		String idsStr = map.get("ids").toString();

		List<BadWaterQualityInfo> list;
		// 获取需要导出的数据信息
		BadWaterQualityInfo badWaterQualityInfoParam = new BadWaterQualityInfo();
		badWaterQualityInfoParam.setEffectIcon("1");
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr)) {
			badWaterQualityInfoParam.getParams().put("ids", idsStr.split(","));
		}
		list = badWaterQualityInfoService.getList(badWaterQualityInfoParam);

		if (list.size() > 0) {
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/badWaterInfo.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-污水处理/排水厂填报数据";

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
					BadWaterQualityInfo badWaterQualityInfoCell = list.get(i);
					if (StringUtils.isNull(badWaterQualityInfoCell)) {
						break;
					}
					// Cell赋值开始
					cell = row.createCell(0);
					cell.setCellValue(badWaterQualityInfoCell.getFillTime());
					cell.setCellStyle(cs);

					cell = row.createCell(1);
					cell.setCellValue(badWaterQualityInfoCell.getCodIn());
					cell.setCellStyle(cs);

					cell = row.createCell(2);
					cell.setCellValue(badWaterQualityInfoCell.getCodOut());
					cell.setCellStyle(cs);

					cell = row.createCell(3);
					cell.setCellValue(badWaterQualityInfoCell.getNh3NIn());
					cell.setCellStyle(cs);

					cell = row.createCell(4);
					cell.setCellValue(badWaterQualityInfoCell.getNh3NOut());
					cell.setCellStyle(cs);

					cell = row.createCell(5);
					cell.setCellValue(badWaterQualityInfoCell.getSsIn());
					cell.setCellStyle(cs);

					cell = row.createCell(6);
					cell.setCellValue(badWaterQualityInfoCell.getSsOut());
					cell.setCellStyle(cs);

					cell = row.createCell(7);
					cell.setCellValue(badWaterQualityInfoCell.getPhIn());
					cell.setCellStyle(cs);

					cell = row.createCell(8);
					cell.setCellValue(badWaterQualityInfoCell.getPhOut());
					cell.setCellStyle(cs);

					cell = row.createCell(9);
					cell.setCellValue(badWaterQualityInfoCell.getTpIn());
					cell.setCellStyle(cs);

					cell = row.createCell(10);
					cell.setCellValue(badWaterQualityInfoCell.getTpOut());
					cell.setCellStyle(cs);

					cell = row.createCell(11);
					cell.setCellValue(badWaterQualityInfoCell.getTnIn());
					cell.setCellStyle(cs);

					cell = row.createCell(12);
					cell.setCellValue(badWaterQualityInfoCell.getTnOut());
					cell.setCellStyle(cs);

					cell = row.createCell(13);
					cell.setCellValue(badWaterQualityInfoCell.getMlssIn());
					cell.setCellStyle(cs);

					cell = row.createCell(14);
					cell.setCellValue(badWaterQualityInfoCell.getSv30In());
					cell.setCellStyle(cs);
					
					cell = row.createCell(15);
					cell.setCellValue(badWaterQualityInfoCell.getFillUserName());
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
			return AjaxResult.warn("未找到污水处理/排水厂数据信息！");
		}
	}

	/**
	 * 新增水厂水质数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:badWaterQualityInfo:add")
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
		if (StringUtils.isNotEmpty(sysOrgs)) {
			SysOrg curOrg = sysOrgs.get(0);
			String msg = ("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报" : (("del").equals(operateIcon) ? "删除" : "导出");
			if (!("3").equals(curOrg.getOrgType())) {
				return warn("您不是水厂用户，不能进行数据" + msg + "！");
			}
			if (StringUtils.isNotNull(curOrg.getFactoryType()) && !("1").equals(curOrg.getFactoryType())) {
				return warn("您所在水厂不是污水厂/排水厂，不能进行数据" + msg + "！");
			}
			if (("add").equals(operateIcon)) {
				BadWaterQualityInfo badWaterQualityParam = new BadWaterQualityInfo();
				badWaterQualityParam.setFillDate(DateUtils.getDate());
				badWaterQualityParam.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				badWaterQualityParam.setEffectIcon("1");
				List<BadWaterQualityInfo> overList = badWaterQualityInfoService.getList(badWaterQualityParam);
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
	 * 新增保存水厂水质数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:badWaterQualityInfo:add")
	@Log(title = "水厂水质数据信息(污水处理厂/排水厂)", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(BadWaterQualityInfo badWaterQualityInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		badWaterQualityInfo.setFillUserId(curUser.getUserId());
		badWaterQualityInfo.setFillUserName(curUser.getUserName());
		badWaterQualityInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		badWaterQualityInfo.setAreaId(curUser.getAreaStr());

		String passFlag = sysFactoryInfoService.badWaterDictDataIsPass(badWaterQualityInfo);
		
		String alarmNote = "";
		if ("1".equals(passFlag)) {
			badWaterQualityInfo.setPassFlag(passFlag);
		} else if (passFlag.indexOf(";") != -1) {
			String[] resultArray = passFlag.split(";");
			badWaterQualityInfo.setPassFlag(resultArray[0]);
			alarmNote = resultArray[1];
		}
		return toAjax(badWaterQualityInfoService.add(badWaterQualityInfo, alarmNote));
	}

	/**
	 * 修改水厂水质数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:badWaterQualityInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		BadWaterQualityInfo badWaterQualityInfo = badWaterQualityInfoService.getEntityById(id);
		mmap.put("badWaterQualityInfo", badWaterQualityInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂水质数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:badWaterQualityInfo:edit")
	@Log(title = "水厂水质数据信息(污水处理厂/排水厂)", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(BadWaterQualityInfo badWaterQualityInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		badWaterQualityInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		String passFlag = sysFactoryInfoService.badWaterDictDataIsPass(badWaterQualityInfo);
		
		String alarmNote = "";
		if ("1".equals(passFlag)) {
			badWaterQualityInfo.setPassFlag(passFlag);
		} else if (passFlag.indexOf(";") != -1) {
			String[] resultArray = passFlag.split(";");
			badWaterQualityInfo.setPassFlag(resultArray[0]);
			alarmNote = resultArray[1];
		}

		badWaterQualityInfo.setFillUserId(curUser.getUserId());
		badWaterQualityInfo.setFillUserName(curUser.getUserName());
		badWaterQualityInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		badWaterQualityInfo.setAreaId(curUser.getAreaStr());

		return toAjax(badWaterQualityInfoService.update(badWaterQualityInfo, alarmNote));
	}

	/**
	 * 删除水厂水质数据信息(污水处理厂/排水厂)
	 */
	@RequiresPermissions("report:badWaterQualityInfo:delete")
	@Log(title = "水厂水质数据信息(污水处理厂/排水厂)", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(badWaterQualityInfoService.deleteByIds(ids));
	}

	/**
	 * 获取数量
	 */
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(BadWaterQualityInfo badWaterQualityInfo) {
		return badWaterQualityInfoService.getCount(badWaterQualityInfo);
	}
	
	/**
	 * 
	 * @param icon
	 * @param mMap
	 * @return
	 */
	@GetMapping("/append/{icon}")
	public String append(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		return prefix + "/append";
	}
	
	/**
	 * 追加污水处理厂水质数据信息
	 */
	@Log(title = "水厂水质数据信息(污水处理厂/排水厂)", businessType = BusinessType.INSERT)
	@PostMapping("/doAppend")
	@ResponseBody
	public AjaxResult doAppend(BadWaterQualityInfo badWaterQualityInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		badWaterQualityInfo.setFillUserId(curUser.getUserId());
		badWaterQualityInfo.setFillUserName(curUser.getUserName());
		badWaterQualityInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		badWaterQualityInfo.setAreaId(curUser.getAreaStr());

		String passFlag = sysFactoryInfoService.badWaterDictDataIsPass(badWaterQualityInfo);
		
		String alarmNote = "";
		if ("1".equals(passFlag)) {
			badWaterQualityInfo.setPassFlag(passFlag);
		} else if (passFlag.indexOf(";") != -1) {
			String[] resultArray = passFlag.split(";");
			badWaterQualityInfo.setPassFlag(resultArray[0]);
			alarmNote = resultArray[1];
		}
		return toAjax(badWaterQualityInfoService.append(badWaterQualityInfo, alarmNote));
	}
}