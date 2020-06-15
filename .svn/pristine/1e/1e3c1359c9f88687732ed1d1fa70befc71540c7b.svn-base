package com.boot.web.controller.admin.system;

import com.alibaba.fastjson.JSONArray;
import com.boot.common.annotation.Log;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.core.domain.Ztree;
import com.boot.common.core.text.Convert;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.CommonUtils;
import com.boot.common.utils.QRCodeUtils;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.*;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysOrgService;
import com.boot.system.service.ISysRoleService;
import com.boot.system.service.ISysUserService;
import com.google.zxing.WriterException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.*;

/**
 * 机构信息
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/system/org")
public class SysOrgController extends BaseController {
	private String prefix = "admin/system/org";

	@Value("${boot.serviceHttpUrl}")
	public String serviceHttpUrl;

	@Autowired
	private ISysOrgService orgService;
	@Autowired
	private ISysRoleService roleService;
	@Autowired
	private ISysUserService userService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private Configuration configuration;

	@RequiresPermissions("system:org:list")
	@GetMapping("/list")
	public String list() {
		return prefix + "/list";
	}

	/**
	 * 查询机构
	 */
	@RequiresPermissions("system:org:list")
	@PostMapping("/doList")
	@ResponseBody
	public List<SysOrg> doList(SysOrg org) {
		return orgService.findList(org);
	}

	/**
	 * 新增机构
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") String parentId, ModelMap mmap) {
		mmap.put("parentId", parentId);
		//
		Area area = new Area();
		List<Area> list = areaService.getList(area);
		mmap.put("areaList", list);
		return prefix + "/add";
	}

	/**
	 * 新增保存机构
	 */
	@Log(title = "机构管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:org:add")
	@PostMapping("/doAdd")
	@ResponseBody
	public AjaxResult doAdd(@Validated SysOrg org) {
		org.setUserId(ShiroUtils.getUserId());
		return toAjax(orgService.insert(org));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{orgId}")
	public String edit(@PathVariable("orgId") String orgId, ModelMap mmap) {
		SysOrg org = orgService.getById(orgId);
		mmap.put("org", org);
		//
		Area area = new Area();
		List<Area> list = areaService.getList(area);
		mmap.put("areaList", list);
		if ("2".equals(org.getOrgType()) && StringUtils.isNotEmpty(org.getAreaId())) {
			List<String> areaIds = new ArrayList<>();
			List<String> areaNames = new ArrayList<>();
			area = new Area();
			area.getParams().put("ids", Convert.toStrArray(org.getAreaId()));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaIds.add(item.getAreaId());
				areaNames.add(item.getAreaName());
			}
			mmap.put("areaIds", StringUtils.join(areaIds, ","));
			mmap.put("areaNames", StringUtils.join(areaNames, ","));
		}
		return prefix + "/edit";
	}

	/**
	 * 修改
	 */
	@Log(title = "机构管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:org:edit")
	@PostMapping("/doEdit")
	@ResponseBody
	public AjaxResult doEdit(@Validated SysOrg org) {
		return toAjax(orgService.update(org));
	}

	/**
	 * 删除
	 */
	@Log(title = "机构管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:org:remove")
	@GetMapping("/doRemove/{orgId}")
	@ResponseBody
	public AjaxResult doRemove(@PathVariable("orgId") String orgId) {
		SysOrg sysOrg = new SysOrg();
		sysOrg.setParentId(orgId);
		int count = orgService.getCount(sysOrg);
		if (count > 0) {
			return AjaxResult.warn("存在下级机构,不允许删除");
		}
		List<SysRole> sysRoles = roleService.getRoleListByOrgId(orgId);
		if (sysRoles.size() > 0) {
			return AjaxResult.warn("机构存在岗位,不允许删除");
		}
		List<SysUser> sysUsers = userService.getUserListByOrgId(orgId);
		if (sysUsers.size() > 0) {
			return AjaxResult.warn("机构存在用户,不允许删除");
		}
		return toAjax(orgService.deleteById(orgId));
	}

	/**
	 * 查询用户所属机构
	 */
	@RequestMapping("/getOrgsByUserId/{userId}")
	@ResponseBody
	public AjaxResult getOrgsByUserId(@PathVariable String userId) {
		AjaxResult ajaxResult;
		List<SysOrg> list = orgService.getOrgListByUserId(userId);
		ajaxResult = success("用户所属机构查询成功！", list);
		return ajaxResult;
	}

	/**
	 * 查询用户所属机构
	 */
	@RequestMapping("/getOrgsByUserIdForList/{userId}")
	@ResponseBody
	public AjaxResult getOrgsByUserIdForList(@PathVariable String userId) {
		AjaxResult ajaxResult;
		List<SysOrg> list = null;
		SysRole sysRole = roleService.getRoleListByUserId(userId).get(0);
		if ("8".equals(sysRole.getRoleKey())) {
			list = new ArrayList<>();
			Area area = areaService.getEntityById(userService.getById(userId).getAreaStr());
			SysOrg sysOrg = new SysOrg();
			sysOrg.setOrgName(area.getAreaName());
			sysOrg.setOrgNamePath(area.getAreaName());
			list.add(sysOrg);
		} else {
			list = orgService.getOrgListByUserId(userId);
		}
		ajaxResult = success("用户所属机构查询成功！", list);
		return ajaxResult;
	}

	/**
	 * 查询岗位所属机构
	 */
	@RequestMapping("/getOrgsByRoleId/{roleId}")
	@ResponseBody
	public AjaxResult getOrgsByRoleId(@PathVariable String roleId) {
		AjaxResult ajaxResult;
		List<SysOrg> list = orgService.getOrgListByRoleId(roleId);
		ajaxResult = success("岗位所属机构查询成功！", list);
		return ajaxResult;
	}

	/**
	 * 查询数量
	 */
	@RequestMapping("/count")
	@ResponseBody
	public AjaxResult count(SysOrg sysOrg) {
		AjaxResult ajaxResult;
		int count = orgService.getCount(sysOrg);
		if (count > 0) {
			ajaxResult = AjaxResult.success("数量查询成功！", count);
		} else {
			ajaxResult = AjaxResult.warn("数量为0！");
		}
		return ajaxResult;
	}

	/**
	 * 加载角色部门（数据权限）列表树
	 */
	@RequestMapping("/getOrgTreeByRoleId")
	@ResponseBody
	public List<Ztree> getOrgTreeByRoleId(String roleId) {
		SysOrg sysOrg = new SysOrg();
		sysOrg.setValidStatus("1");
		List<SysOrg> sysOrgs = orgService.findList(sysOrg);
		List<SysRoleOrgScope> sysRoleOrgScopes = orgService.getOrgScopeByRoleId(roleId);
		List<String> roleScopeOrgIds = new ArrayList<>();
		for (SysRoleOrgScope sysRoleOrgScope1 : sysRoleOrgScopes) {
			roleScopeOrgIds.add(sysRoleOrgScope1.getOrgId());
		}
		return initZtree(sysOrgs, roleScopeOrgIds);
	}

	/**
	 * 根据ID获取机构信息
	 */
	@GetMapping("/get/{id}")
	@ResponseBody
	public AjaxResult getById(@PathVariable("id") String id) {
		SysOrg org = orgService.getById(id);
		if (StringUtils.isNotNull(org)) {
			return success("success", org);
		} else {
			Area area = areaService.getEntityById(id);
			if (StringUtils.isNotNull(area)) {
				org = new SysOrg();
				org.setOrgId(area.getAreaId());
				org.setOrgType("2.5");
				org.setOrgName(area.getAreaName());
				return success("success", org);
			} else {
				return error("error");
			}
		}
	}

	/**
	 * 加载部门列表树
	 */
	@RequestMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData() {
		SysOrg sysOrg = new SysOrg();
		sysOrg.setValidStatus("1");
		List<SysOrg> sysOrgs = orgService.findList(sysOrg);
		return initZtree(sysOrgs, null);
	}

	/**
	 * 加载部门列表树
	 */
	@RequestMapping("/treeDataArea")
	@ResponseBody
	public List<Ztree> treeDataArea() {
		// 组织机构
		// //////////////////////////////////////////////////////////////////////////////
		SysOrg sysOrg = new SysOrg();
		sysOrg.setValidStatus("1");
		sysOrg.setOrgType("2");
		List<SysOrg> sysOrgs = orgService.findList(sysOrg);

		List<Ztree> zTrees = new ArrayList<>();
		Ztree ztree = new Ztree();
		ztree.setId("1");
		ztree.setpId("0");
		ztree.setName("中国水环境集团");
		ztree.setTitle("中国水环境集团");
		zTrees.add(ztree);

		Area area = null;
		Map<String, Object> areaParams = null;
		for (SysOrg org : sysOrgs) {
			ztree = new Ztree();
			ztree.setId(org.getOrgId());
			ztree.setpId(org.getParentId());
			ztree.setName(org.getOrgName());
			ztree.setTitle(org.getOrgName());
			ztree.setChecked(false);
			zTrees.add(ztree);

			// 区域
			// //////////////////////////////////////////////////////////////////////////////
			area = new Area();
			areaParams = new HashMap<>();
			areaParams.put("areaIds", org.getAreaId());
			area.setParams(areaParams);
			List<Area> areaList = areaService.getList(area);
			for (Area a : areaList) {
				ztree = new Ztree();
				ztree.setId(a.getAreaId());
				ztree.setpId(org.getOrgId());
				ztree.setName(a.getAreaName());
				ztree.setTitle(a.getAreaName());
				ztree.setChecked(false);
				zTrees.add(ztree);
			}
		}

		return zTrees;
	}

	/**
	 * 加载部门下拉树
	 */
	@RequestMapping("/treeList")
	@ResponseBody
	public JSONArray treeList(SysOrg sysOrg) {
		return orgService.treeList(sysOrg);
	}

	/**
	 * 加载部门下拉树
	 */
	@RequestMapping("/treeListArea")
	@ResponseBody
	public JSONArray treeListArea(SysOrg sysOrg) {
		return orgService.treeListArea(sysOrg);
	}

	/**
	 * 对象转组织机构树
	 *
	 * @param sysOrgList 组织机构列表
	 * @param roleScopeOrgIds 角色已存在菜单列表
	 * @return 树结构列表
	 */
	private List<Ztree> initZtree(List<SysOrg> sysOrgList, List<String> roleScopeOrgIds) {
		List<Ztree> zTrees = new ArrayList<>();
		boolean isCheck = StringUtils.isNotEmpty(roleScopeOrgIds);
		for (SysOrg sysOrg : sysOrgList) {
			Ztree ztree = new Ztree();
			ztree.setId(sysOrg.getOrgId());
			ztree.setpId(sysOrg.getParentId());
			ztree.setName(sysOrg.getOrgName());
			ztree.setTitle(sysOrg.getOrgName());
			if (isCheck) {
				ztree.setChecked(roleScopeOrgIds.contains(sysOrg.getOrgId()));
			}
			zTrees.add(ztree);
		}
		return zTrees;
	}

	/**
	 *
	 * 获取组织机构树数据源
	 * */
	@RequestMapping("/getTreeData/{parentId}")
	@ResponseBody
	public AjaxResult getTreeData(@PathVariable String parentId) {
		return success("树数据源封装成功！", getJson(parentId));
	}

	private List<Map<String, Object>> getJson(String parentId) {
		List<Map<String, Object>> mapList = new LinkedList<>();
		SysOrg sysOrg_params = new SysOrg();
		sysOrg_params.setParentId(parentId);
		sysOrg_params.setValidStatus("1");
		List<SysOrg> sysOrgs = orgService.findList(sysOrg_params);
		if (sysOrgs.size() > 0) {
			for (SysOrg sysOrg : sysOrgs) {
				Map<String, Object> map = new HashMap<>();
				map.put("orgId", sysOrg.getOrgId());
				map.put("orgName", sysOrg.getOrgName());
				sysOrg_params = new SysOrg();
				sysOrg_params.setParentId(sysOrg.getOrgId());
				sysOrg_params.setValidStatus("1");
				int count = orgService.getCount(sysOrg_params);
				if (count > 0) {
					List<Map<String, Object>> children = getJson(sysOrg.getOrgId());
					map.put("children", children);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 *
	 * 获取组织机构所有上级
	 * */
	@RequestMapping("/getAllParentOrgs")
	@ResponseBody
	public AjaxResult getAllParentOrgs(String orgIds) {
		AjaxResult ajaxResult;
		if (!"".equals(orgIds)) {
			String orgIdStr = "";
			List<SysOrg> sysOrgs = orgService.findListByIds(Convert.toStrArray(orgIds));
			if (sysOrgs.size() > 0) {
				for (SysOrg sysOrg : sysOrgs) {
					if ("".equals(orgIdStr)) {
						orgIdStr = sysOrg.getOrgIdPath().replaceAll("/", ",");
					} else {
						orgIdStr += "," + sysOrg.getOrgIdPath().replaceAll("/", ",");
					}
				}
			}
			sysOrgs = orgService.findListByIds(Convert.toStrArray(orgIdStr));
			ajaxResult = success("机构查询成功！", sysOrgs);
		} else {
			ajaxResult = error();
		}
		return ajaxResult;
	}

	/**
	 * 选择部门树形页面
	 */
	@RequestMapping("/selCompanyTree")
	public String selTree(@RequestParam("type") String type, @RequestParam("checkDiv") String checkDiv, ModelMap mmp) {
		mmp.put("type", type);
		mmp.put("checkDiv", checkDiv);
		return prefix + "/selCompanyTree";
	}

	/**
	 * 加载部门公司列表树
	 */
	@RequestMapping("/companyTreeData")
	@ResponseBody
	public List<Ztree> companyTreeData() {
		SysOrg sysOrg = new SysOrg();
		sysOrg.setValidStatus("1");
		List<SysOrg> sysOrgList = orgService.findListNoDataScope(sysOrg);
		return initZtree(sysOrgList, null);
	}

	/**
	 * 获取水厂二维码
	 * */
	@Log(title = "机构管理", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:org:exportQrCode")
	@RequestMapping("/createQrCode")
	@ResponseBody
	public void createQrCode(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TemplateException {
		SysOrg sysOrgParam = new SysOrg();
		sysOrgParam.setOrgType("3");
		List<SysOrg> list = orgService.findList(sysOrgParam);

		Map<String, Object> freemarkerMap = new HashMap<String, Object>();
		freemarkerMap.put("title", "水厂员工健康信息填报二维码");
		List<Map<String, Object>> orgList = new ArrayList<>();

		for (SysOrg sysOrg : list) {
			Map<String, Object> orgInfo = new HashMap<>();
			orgInfo.put("orgName", sysOrg.getOrgName());
			orgInfo.put("orgId", sysOrg.getOrgId());

			// 获取区域信息
			Area orgArea = areaService.getEntityById(sysOrg.getAreaId());
			orgInfo.put("areaName", orgArea.getAreaName());
			// 加密
			String encodeParams = String.format("/app/visitor/%s/%s", sysOrg.getAreaId(), sysOrg.getOrgId());
			String qrCodeStr = serviceHttpUrl + CommonUtils.encode(encodeParams);
			orgInfo.put("serviceHttpUrl", qrCodeStr);
			try {
				byte[] qrCodeImage = QRCodeUtils.getQRCodeImage(qrCodeStr, 300, 300);
				// 转换为base64
				BASE64Encoder base64Encoder = new BASE64Encoder();
				String encodeImage = base64Encoder.encode(qrCodeImage);
				orgInfo.put("urlImg", encodeImage);
			} catch (WriterException | IOException e) {
				e.printStackTrace();
			}
			orgList.add(orgInfo);
		}
		freemarkerMap.put("orgList", orgList);
		// 告诉浏览器用什么软件可以打开此文件(.doc)
		// 若是下载.docx
		// 则为application/vnd.openxmlformats-officedocument.wordprocessingml.document
		response.setHeader("content-Type", "application/msword");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode("水厂员工健康信息填报二维码", "UTF-8") + ".doc");
		// 默认模版地址为classpath:/templates,若要你的模版不想放在默认地址，可以使用上面注释掉的代码读取模版 or
		// 在application.properties中
		// 设置编码格式，后三处UTF-8！！！
		configuration.setDefaultEncoding("UTF-8");
		// 添加配置spring.freemarker.template-loader-path=你的模版地址（例如获取根目录下所有模版，配置如下spring.freemarker.template-loader-path=classpath:/)
		Template template = configuration.getTemplate("qcode.ftl", "UTF-8");
		template.process(freemarkerMap, new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
	}
}
