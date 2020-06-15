package com.boot.web.controller.admin.report;

import com.boot.common.exception.BusinessException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.report.domain.UserHealthInfo;
import com.boot.report.service.IUserHealthInfoService;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysFactoryInfoService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.boot.common.annotation.Log;
import com.boot.common.enums.BusinessType;
import com.boot.report.domain.HrHealthInfo;
import com.boot.report.service.IHrHealthInfoService;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.common.core.page.TableDataInfo;

/**
 * 水厂人力资源健康情况信息Controller
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Controller
@RequestMapping("/admin/report/hrHealthInfo")
public class HrHealthInfoController extends BaseController {
	private String prefix = "admin/report/hrHealthInfo";

	@Autowired
	private IHrHealthInfoService hrHealthInfoService;
	@Autowired
	private IUserHealthInfoService userHealthInfoService;
	@Autowired
	private ISysFactoryInfoService factoryInfoService;

	@RequiresPermissions("report:hrHealthInfo:view")
	@RequestMapping()
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询水厂人力资源健康情况信息列表
	 */
	@RequestMapping("/doList")
	@ResponseBody
	public TableDataInfo doList(HrHealthInfo hrHealthInfo) {
		SysUser sysUser = ShiroUtils.getSysUser();
		hrHealthInfo.setEffectIcon("1");
		List<SysOrg> sysOrgList = sysUser.getSysOrgs();
		SysRole sysRole = sysUser.getSysRoles().get(0);
		if (StringUtils.isNotEmpty(sysOrgList)) {
			if ("1".equals(sysUser.getUserId())) { // 超级管理员查所有
				
			} else if ("1".equals(sysRole.getRoleKey()) || "6".equals(sysRole.getRoleKey())) { // 集团领导 || 区域领导查区域
				Map<String, Object> params = new HashMap<>();
				params.put("areaIds", sysUser.getAreaStr());
				hrHealthInfo.setParams(params);
			} else {
				hrHealthInfo.setFactoryId(sysOrgList.get(0).getOrgId());
			}
		} else {
			throw new BusinessException("您还未分配机构！");
		}
		startPage();
		List<HrHealthInfo> list = hrHealthInfoService.getList(hrHealthInfo);
		return getDataTable(list);
	}

	/**
	 * 导出水厂人力资源健康情况信息列表
	 */
	@RequiresPermissions("report:hrHealthInfo:export")
	@RequestMapping("/export")
	@ResponseBody
	public AjaxResult doExport(@RequestParam Map<String, Object> map) throws IOException {
		SysUser sysUser = ShiroUtils.getSysUser();
		SysOrg sysOrg = sysUser.getSysOrgs().get(0);

		String idsStr = map.get("ids").toString();

		List<HrHealthInfo> list;
		// 获取需要导出的数据信息
		HrHealthInfo hrHealthInfoParam = new HrHealthInfo();
		hrHealthInfoParam.setEffectIcon("1");
		// 判断是否选中数据
		if (StringUtils.isNotEmpty(idsStr)) {
			hrHealthInfoParam.getParams().put("ids", idsStr.split(","));
		}
		list = hrHealthInfoService.getList(hrHealthInfoParam);

		if (list.size() > 0) {
			OutputStream os = null;
			Workbook wb = null; // 工作薄
			String filename = "";

			try {
				// 模板资源路径
				ClassPathResource cpr = new ClassPathResource("static/template/hrHealthInfo.xlsx");
				InputStream inputStream = cpr.getInputStream();
				wb = new XSSFWorkbook(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				String sheetName = sysOrg.getOrgName() + "-人力资源填报数据";

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
					HrHealthInfo hrHealthInfoCell = list.get(i);
					if (StringUtils.isNull(hrHealthInfoCell)) {
						break;
					}
					// Cell赋值开始
					cell = row.createCell(0);
					cell.setCellValue(hrHealthInfoCell.getFillTime());
					cell.setCellStyle(cs);

					cell = row.createCell(1);
					cell.setCellValue(hrHealthInfoCell.getAllUserCount());
					cell.setCellStyle(cs);

					cell = row.createCell(2);
					cell.setCellValue(hrHealthInfoCell.getSiteUserCount());
					cell.setCellStyle(cs);

					cell = row.createCell(3);
					cell.setCellValue(hrHealthInfoCell.getExceptionUserCount());
					cell.setCellStyle(cs);

					cell = row.createCell(4);
					cell.setCellValue(hrHealthInfoCell.getHealthInfo());
					cell.setCellStyle(cs);

					cell = row.createCell(5);
					cell.setCellValue(hrHealthInfoCell.getWearInfo());
					cell.setCellStyle(cs);

					cell = row.createCell(6);
					cell.setCellValue(hrHealthInfoCell.getMaskCount());
					cell.setCellStyle(cs);

					cell = row.createCell(7);
					cell.setCellValue(hrHealthInfoCell.getDisinfectCount());
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
			return AjaxResult.warn("未找到人力资源健康情况信息！");
		}
	}

	/**
	 * 新增水厂人力资源健康情况信息
	 */
	@RequiresPermissions("report:hrHealthInfo:add")
	@GetMapping("/add/{icon}")
	public String add(@PathVariable("icon") String icon, ModelMap mMap) {
		mMap.put("icon", icon);
		// 当前用户信息
		SysUser curUser = ShiroUtils.getSysUser();
		SysOrg curOrg = curUser.getSysOrgs().get(0);
		// 当前水厂下所有人数
		String totalCount = "0";
		SysFactoryInfo factoryInfo = factoryInfoService.getEntityByOrgId(curOrg.getOrgId());
		if (factoryInfo != null) {
			String personNum = factoryInfo.getPersonNum();
			if (personNum != null && !"".equals(personNum))
				totalCount = personNum;
		}
		mMap.put("totalCount", totalCount);
		// 当前水厂现场人数
		String curDate = DateUtils.getDate();
		UserHealthInfo userHealthParam = new UserHealthInfo();
		userHealthParam.setFactoryId(curOrg.getOrgId());
		userHealthParam.setEffectIcon("1");
		userHealthParam.setIsInFactory("1");
		userHealthParam.setWorkType("1");
		userHealthParam.setFillDate(curDate);
		mMap.put("siteCount", userHealthInfoService.getCount(userHealthParam));
		// 当前水厂远程人数
		userHealthParam.setWorkType("2");
		mMap.put("onlineCount", userHealthInfoService.getCount(userHealthParam));
		// 当前水厂异常人数
		userHealthParam.getParams().put("healthStatuses", "2,3,4");
		mMap.put("exceptionCount", userHealthInfoService.getCount(userHealthParam));
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
			if (!("3").equals(curOrg.getOrgType())) {
				return warn("您不是水厂用户，不能进行数据" + (("add").equals(operateIcon) || ("edit").equals(operateIcon) ? "填报"
						: (("del").equals(operateIcon) ? "删除" : "导出")) + "！");
			}
			if (("add").equals(operateIcon)) {
				HrHealthInfo hrHealthParam = new HrHealthInfo();
				hrHealthParam.setFillDate(DateUtils.getDate());
				hrHealthParam.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
				hrHealthParam.setEffectIcon("1");
				List<HrHealthInfo> overList = hrHealthInfoService.getList(hrHealthParam);
				if (StringUtils.isNotEmpty(overList) && overList.size() > 0) {
					return error("当前水厂今日已经填报过信息，是否继续填报？");
				}
			}
		} else {
			return warn("您还未分配机构！");
		}
		return success();
	}

	/**
	 * 新增保存水厂人力资源健康情况信息
	 */
	@RequiresPermissions("report:hrHealthInfo:add")
	@Log(title = "水厂人力资源健康情况信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(HrHealthInfo hrHealthInfo) {
		SysUser curUser = ShiroUtils.getSysUser();
		hrHealthInfo.setFillUserId(curUser.getUserId());
		hrHealthInfo.setFillUserName(curUser.getUserName());
		hrHealthInfo.setFactoryId(curUser.getSysOrgs().get(0).getOrgId());
		hrHealthInfo.setAreaId(curUser.getAreaStr());
		return toAjax(hrHealthInfoService.add(hrHealthInfo));
	}

	/**
	 * 修改水厂人力资源健康情况信息
	 */
	@RequiresPermissions("report:hrHealthInfo:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mMap) {
		HrHealthInfo hrHealthInfo = hrHealthInfoService.getEntityById(id);
		mMap.put("hrHealthInfo", hrHealthInfo);
/*		// 当前用户信息
		SysUser curUser = ShiroUtils.getSysUser();
		SysOrg curOrg = curUser.getSysOrgs().get(0);
		// 当前水厂下所有人数
		String totalCount = "0";
		SysFactoryInfo factoryInfo = factoryInfoService.getEntityByOrgId(curOrg.getOrgId());
		if (factoryInfo != null) {
			String personNum = factoryInfo.getPersonNum();
			if (personNum != null && !"".equals(personNum))
				totalCount = personNum;
		}
		mMap.put("totalCount", totalCount);
		// 当前水厂现场人数
		String curDate = DateUtils.getDate();
		UserHealthInfo userHealthParam = new UserHealthInfo();
		userHealthParam.setFactoryId(curOrg.getOrgId());
		userHealthParam.setEffectIcon("1");
		userHealthParam.setIsInFactory("1");
		userHealthParam.setWorkType("1");
		userHealthParam.setFillDate(curDate);
		mMap.put("siteCount", userHealthInfoService.getCount(userHealthParam));
		// 当前水厂远程人数
		userHealthParam.setWorkType("2");
		mMap.put("onlineCount", userHealthInfoService.getCount(userHealthParam));
		// 当前水厂异常人数
		userHealthParam.getParams().put("healthStatuses", "2,3,4");
		mMap.put("exceptionCount", userHealthInfoService.getCount(userHealthParam));
*/
		return prefix + "/edit";
	}

	/**
	 * 修改保存水厂人力资源健康情况信息
	 */
	@RequiresPermissions("report:hrHealthInfo:edit")
	@Log(title = "水厂人力资源健康情况信息", businessType = BusinessType.UPDATE)
	@PutMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(HrHealthInfo hrHealthInfo) {
		return toAjax(hrHealthInfoService.update(hrHealthInfo));
	}

	/**
	 * 删除水厂人力资源健康情况信息
	 */
	@RequiresPermissions("report:hrHealthInfo:delete")
	@Log(title = "水厂人力资源健康情况信息", businessType = BusinessType.DELETE)
	@PostMapping("/doDelete")
	@ResponseBody
	public AjaxResult doDelete(String ids) {
		return toAjax(hrHealthInfoService.deleteByIds(ids));
	}

	/**
	* 获取数量
	*/
	@RequestMapping("/getCount")
	@ResponseBody
	public int getCount(HrHealthInfo hrHealthInfo) {
		return hrHealthInfoService.getCount(hrHealthInfo);
	}
}