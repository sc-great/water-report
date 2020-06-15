package com.boot.web.controller.admin.system;

import io.swagger.annotations.Api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.boot.common.annotation.Log;
import com.boot.common.enums.BusinessType;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.poi.ExcelUtil;
import com.boot.framework.util.ShiroUtils;
import com.boot.common.core.page.TableDataInfo;

/**
 * 水厂信息Controller
 * 
 * @author boot
 * @date 2020-05-18
 */
@Api(value = "/admin/system/factory",description = "水厂信息")
@Controller
@RequestMapping("/admin/system/factory")
public class SysFactoryInfoController extends BaseController {
    private String prefix = "admin/system/factory";

    @Autowired
    private ISysFactoryInfoService sysFactoryInfoService;

   
    @RequestMapping()
    public String list() {
        return prefix + "/list";
    }

    /**
     * 查询水厂信息列表
     */
   
    @RequestMapping("/doList")
    @ResponseBody
    public TableDataInfo doList(SysFactoryInfo sysFactoryInfo) {
        startPage();
        SysUser curUser = ShiroUtils.getSysUser();
        Map<String, Object> params = new HashMap<>();
		params.put("areaIds", curUser.getAreaStr());
		sysFactoryInfo.setParams(params);
        List<SysFactoryInfo> list = sysFactoryInfoService.getList(sysFactoryInfo);
        return getDataTable(list);
    }

    /**
     * 导出水厂信息列表
     */
    @RequiresPermissions("system:factory:export")
    @RequestMapping("/export")
    @ResponseBody
    public AjaxResult doExport(SysFactoryInfo sysFactoryInfo) {
        List<SysFactoryInfo> list = sysFactoryInfoService.getList(sysFactoryInfo);
        ExcelUtil<SysFactoryInfo> util = new ExcelUtil<SysFactoryInfo>(SysFactoryInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 新增水厂信息
     */
    @RequiresPermissions("system:factory:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
    	String returnValue="/editView";
    	
    	SysUser curUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgs = curUser.getSysOrgs();
		
    	if(StringUtils.isNotEmpty(sysOrgs)){
    		SysOrg curOrg = sysOrgs.get(0);    		
		    if (!"3".equals(curOrg.getOrgType())){ //非工厂用户仅查看管辖内的工厂信息
		    	returnValue="/list";
		    }else{
		    	SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.getEntityByOrgId(curOrg.getOrgId());
	    		if (StringUtils.isNotNull(sysFactoryInfo)){  //修改
	    			 mmap.put("sysFactoryInfo", sysFactoryInfo);
	    			 if("1".equals(curOrg.getFactoryType())){
	    		         returnValue="/editBad";
	    			 }else {
	    				 returnValue="/editGood";
	    			 }
	    		}else{
	    			 if("1".equals(curOrg.getFactoryType())){
	    		         returnValue="/addBad";
	    			 }else {
	    				 returnValue="/addGood";
	    			 }
	    			
	    		}		    	
		    }    		
    		
    	}
    	    	
        return prefix + returnValue;
    }

    /**
     * 新增保存水厂信息
     */
    @RequiresPermissions("system:factory:add")
    @Log(title = "水厂信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAdd")
    @ResponseBody
    public AjaxResult doAdd(SysFactoryInfo sysFactoryInfo) {
    	SysUser curUser = ShiroUtils.getSysUser();
		List<SysOrg> sysOrgs = curUser.getSysOrgs();
		if(StringUtils.isNotEmpty(sysOrgs)){
			sysFactoryInfo.setOrgId(sysOrgs.get(0).getOrgId());
			sysFactoryInfo.setAreaId(curUser.getAreaStr());
		}   
                return toAjax(sysFactoryInfoService.add(sysFactoryInfo));
    }

    /**
     * 修改水厂信息
     */
   
    @GetMapping("/edit/{factId}")
    public String edit(@PathVariable("factId") String factId, ModelMap mmap) {
        SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.getEntityById(factId);
        if(sysFactoryInfo==null){
        	sysFactoryInfo=new SysFactoryInfo();
        }
        mmap.put("sysFactoryInfo", sysFactoryInfo);
        return prefix + "/editView";
    }

    /**
     * 修改保存水厂信息
     */
    @Log(title = "水厂信息", businessType = BusinessType.UPDATE)
    @PostMapping("/doEdit")
    @ResponseBody
    public AjaxResult doEdit(SysFactoryInfo sysFactoryInfo) {
                return toAjax(sysFactoryInfoService.update(sysFactoryInfo));
    }

    /**
     * 删除水厂信息
     */
    @RequiresPermissions("system:factory:delete")
    @Log(title = "水厂信息", businessType = BusinessType.DELETE)
    @PostMapping( "/doDelete")
    @ResponseBody
    public AjaxResult doDelete(String ids) {
        return toAjax(sysFactoryInfoService.deleteByIds(ids));
    }
    /**
    * 获取数量
    */
    @RequestMapping( "/getCount")
    @ResponseBody
    public int getCount(SysFactoryInfo sysFactoryInfo){
        int count=sysFactoryInfoService.getCount(sysFactoryInfo);
        return count;
    }
}