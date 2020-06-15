package com.boot.web.controller.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.report.service.IGoodWaterHealthInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysFactoryInfoService;

/**
 * 手机端:自来水厂(供水厂) 水质信息 Controller
 * @author yangxiaojun
 *
 */
@Controller
@RequestMapping("/app")
public class AppGoodWaterHealthInfoController extends APPBaseController {
	 //private String prefix = "app/pages";

	  @Autowired
	  private IGoodWaterHealthInfoService goodWaterHealthInfoService;
	  
	 @Autowired
	 private ISysFactoryInfoService sysFactoryInfoService;

    /**
     * 新增保存信息
     */
   
    @Log(title = "供水厂信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddGoodWaterHealthInfo")
    @ResponseBody
    public AjaxResult doAdd(GoodWaterHealthInfo goodWaterHealthInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			
			SysOrg curOrg = sysUser.getSysOrgs().get(0) ;
			if (!(("3").equals(curOrg.getOrgType())&&("2").equals(curOrg.getFactoryType()))) {
				
				 return AjaxResult.warn("您不是供水厂用户，不能进行供水厂水质填报！");
			}
			
			
			goodWaterHealthInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());		
			goodWaterHealthInfo.setFillDate(DateUtils.getDate());
			goodWaterHealthInfo.setEffectIcon("1");
			GoodWaterHealthInfo  existEntity=	goodWaterHealthInfoService.getEntity(goodWaterHealthInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");
				
			}else{
				goodWaterHealthInfo.setAreaId(sysUser.getAreaStr());					
				goodWaterHealthInfo.setFillUserId(sysUser.getUserId());			
				goodWaterHealthInfo.setFillUserName(sysUser.getUserName());				
				
				String passFlag = sysFactoryInfoService.goodWaterDictDataIsPass(goodWaterHealthInfo);	
				String  alarmNote="";
				if("1".equals(passFlag)){
					goodWaterHealthInfo.setPassFlag(passFlag);	
				}else if(passFlag.indexOf(",")!=-1){		 
				  String[]  resultArray= passFlag.split(",");
				  goodWaterHealthInfo.setPassFlag(resultArray[0]);	
				  alarmNote=resultArray[1];
				}			
				
				goodWaterHealthInfoService.add(goodWaterHealthInfo,alarmNote);			    
			    map.put("flag", "0");
				map.put("mess", "保存成功！");
				
			}
		  }else{
			map.put("flag", "1");
		    map.put("mess", "登录超时,请重新登录！");
	     	
		}	
		return AjaxResult.success("0", map);//这里0表示流程执行成功
    }

    /**
     * 修改
     */
   
    @Log(title = "信息", businessType = BusinessType.UPDATE)
    @PostMapping("/doEditGoodWaterHealthInfo")
    @ResponseBody
    public AjaxResult doEdit(GoodWaterHealthInfo goodWaterHealthInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			GoodWaterHealthInfo oldEntity=new GoodWaterHealthInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
	    	GoodWaterHealthInfo  existEntity=	goodWaterHealthInfoService.getEntity(oldEntity); 
			goodWaterHealthInfo.setId(existEntity.getId());
			
			goodWaterHealthInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			String passFlag = sysFactoryInfoService.goodWaterDictDataIsPass(goodWaterHealthInfo);		
			String alarmNote = "";
			if ("1".equals(passFlag)) {
				goodWaterHealthInfo.setPassFlag(passFlag);
			} else if (passFlag.indexOf(";") != -1) {
				String[] resultArray = passFlag.split(";");
				goodWaterHealthInfo.setPassFlag(resultArray[0]);
				alarmNote = resultArray[1];
			}
			goodWaterHealthInfo.setFillUserId(sysUser.getUserId());
			goodWaterHealthInfo.setFillUserName(sysUser.getUserName());
			goodWaterHealthInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			goodWaterHealthInfo.setAreaId(sysUser.getAreaStr());
			
			goodWaterHealthInfoService.update(goodWaterHealthInfo,alarmNote);
	    	return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
		}
    	
    	
    }
//	
	
	
}
