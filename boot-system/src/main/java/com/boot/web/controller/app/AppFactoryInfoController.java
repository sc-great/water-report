package com.boot.web.controller.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.enums.BusinessType;
import com.boot.common.enums.OperatorType;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.system.domain.SysDictType;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysDictTypeService;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.system.service.ISysOrgService;

/**
 * 手机端工厂信息Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app")
public class AppFactoryInfoController extends APPBaseController {
	private String prefix = "app/pages";

	  @Autowired
	  private ISysFactoryInfoService sysFactoryInfoService;
	  @Autowired
	  private ISysDictTypeService sysDictTypeService;
	  @Autowired
	  private ISysOrgService orgService;
	  
	 /**
     * 首页栏目：工厂信息
     */
   
    @GetMapping("/addFactory")
    public String add(ModelMap mmap)
    {
    	String goPage="";

    	//如果已存在，则去修改页;否则新增页面
    	SysUser sysUser = getLoginUserInfo();
    	if (StringUtils.isNotNull(sysUser)) {
    	  SysFactoryInfo entity=new SysFactoryInfo();
    	  entity.setOrgId(sysUser.getSysOrgs().get(0).getOrgId());
          SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.getEntity(entity);
         
          if(sysFactoryInfo!=null){
             mmap.put("sysFactoryInfo", sysFactoryInfo);
            // List<SysDictType> list = sysDictTypeService.selectDictTypeByFactory();          
 			// mmap.put("standardList", list); 	
             if ( "1".equals( sysUser.getSysOrgs().get(0).getFactoryType())){ //污水厂
               goPage="factory_edit_bad";
             }else{
    	       goPage="factory_edit_good";
             }
          }else{
        	  if ( "1".equals( sysUser.getSysOrgs().get(0).getFactoryType())){ //污水厂
        		  goPage="factory_add_bad";
        	  }else{
        		  goPage="factory_add_good";        		  
        	  }
        	  
          }
        
    	}
     return prefix + "/"+goPage;
    }

    /**
     * 新增保存水厂信息
     */
   
    @Log(title = "新增水厂信息", businessType = BusinessType.INSERT ,operatorType = OperatorType.MOBILE)
    @PostMapping("/doAddFactory")
    @ResponseBody
    public AjaxResult doAdd(SysFactoryInfo sysFactoryInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			sysFactoryInfo.setOrgId(sysUser.getSysOrgs().get(0).getOrgId());
			sysFactoryInfo.setAreaId(sysUser.getAreaStr());
						
			sysFactoryInfo.setCreateBy(sysUser.getUserName());			
			sysFactoryInfo.setCreateTime(DateUtils.getDate());
			
		    sysFactoryInfoService.add(sysFactoryInfo);
		    
		     map.put("flag", "0");
			 map.put("mess", "保存成功！");
		    
		  }else{
			map.put("flag", "1");
		    map.put("mess", "登录超时,请重新登录！");
	     	
		}	
		return AjaxResult.success("0", map);//这里0表示流程执行成功
    }

    /**
     * 修改保存水厂信息
     */
   
    @Log(title = "修改水厂信息", businessType = BusinessType.UPDATE)
    @PostMapping("/doEdit")
    @ResponseBody
    public AjaxResult doEdit(SysFactoryInfo sysFactoryInfo) {
    	sysFactoryInfoService.update(sysFactoryInfo);
    	return AjaxResult.success("0");
    	
    }
	
	/**
	 * 非水厂领导：集团、区域、水厂职员都可以看
	 * @param mmap
	 * @return
	 */
    @GetMapping("/factoryArea")
    public String factoryArea(ModelMap mmap)
    {    	   
     return prefix + "/factory_area";
    }

	/**
	 * 非水厂领导：集团、区域、水厂职员都可以看,集团区域看所管理范围内的水厂，职工只能看自己所在的水厂信息
	 *
	 * @return
	 */
    @RequestMapping("/factoryAreaAreaData")
	@ResponseBody
	public AjaxResult waterQualityAreaData(String date) {
		AjaxResult ajaxResult;
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			SysOrg sysOrg_params = new SysOrg();
			// 当前用户所属机构
			SysOrg sysOrg = getLoginUserOrg();
			// 水厂用户
			if ("3".equals(sysOrg.getOrgType())) {
				sysOrg_params.setOrgId(sysOrg.getOrgId());
			} else {
				sysOrg_params.getParams().put("areaIds", areaIds);
			}
			sysOrg_params.setOrgType("3");//只要水厂单位
			List<SysOrg> orgList = orgService.findList(sysOrg_params);
			List<SysOrg> orgListByExist =new ArrayList<SysOrg>(); //存在的工厂信息记录
			for (SysOrg item : orgList) {
			
					// 水厂信息
				    SysFactoryInfo sysFactoryInfoPara = new SysFactoryInfo();				   				
				   	sysFactoryInfoPara.setOrgId(item.getOrgId());				   
				   	SysFactoryInfo	   sysFactoryInfo = sysFactoryInfoService.getEntity(sysFactoryInfoPara);
				   	//处理字典
					if (sysFactoryInfo != null) {
						if ("r_bad_water_check_a".equals(sysFactoryInfo.getStandard())) {
							sysFactoryInfo.setStandard("污水检测排放标准A");
						} else if ("r_bad_water_check_b".equals(sysFactoryInfo.getStandard())) {
							sysFactoryInfo.setStandard("污水检测排放标准B");
						} else if ("r_good_water_check_a".equals(sysFactoryInfo.getStandard())) {
							sysFactoryInfo.setStandard("自来水出厂标准");
						}
						
						
						item.getParams().put("sysFactoryInfo", sysFactoryInfo);
						orgListByExist.add(item);
						
					}
					
				
			}
			ajaxResult = AjaxResult.success(orgListByExist);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
    
}
