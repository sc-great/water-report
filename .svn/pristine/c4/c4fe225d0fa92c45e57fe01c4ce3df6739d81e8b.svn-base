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
import com.boot.report.domain.TestTapWaterInfo;
import com.boot.report.service.ITestTapWaterInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;

/**
 * 手机端:自来水厂化验数据填报信息 Controller
 * @author yangxiaojun
 *
 */
@Controller
@RequestMapping("/app")
public class AppTestTapWaterInfoController extends APPBaseController {
	 //private String prefix = "app/pages";

	  @Autowired
	  private ITestTapWaterInfoService testTapWaterInfoService;
	

    /**
     * 新增保存信息
     */
   
    @Log(title = "供水厂化验信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddTestTapWaterInfo")
    @ResponseBody
    public AjaxResult doAdd(TestTapWaterInfo testTapWaterInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			
			SysOrg curOrg = sysUser.getSysOrgs().get(0) ;
			if (!(("3").equals(curOrg.getOrgType())&&("2").equals(curOrg.getFactoryType()))) {
				
				 return AjaxResult.warn("您不是供水厂用户，不能进行供水厂化验数据填报！");
			}
			
			
			testTapWaterInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());		
			testTapWaterInfo.setFillDate(DateUtils.getDate());
			testTapWaterInfo.setEffectIcon("1");
			TestTapWaterInfo  existEntity=	testTapWaterInfoService.getEntity(testTapWaterInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");
				
			}else{
				testTapWaterInfo.setAreaId(sysUser.getAreaStr());					
				testTapWaterInfo.setFillUserId(sysUser.getUserId());			
				testTapWaterInfo.setFillUserName(sysUser.getUserName());
				testTapWaterInfoService.add(testTapWaterInfo);			    
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
    @PostMapping("/doEditTestTapWaterInfo")
    @ResponseBody
    public AjaxResult doEdit(TestTapWaterInfo testTapWaterInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			TestTapWaterInfo oldEntity=new TestTapWaterInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
	    	TestTapWaterInfo  existEntity=	testTapWaterInfoService.getEntity(oldEntity); 
	    	testTapWaterInfo.setId(existEntity.getId());
	    	testTapWaterInfoService.update(testTapWaterInfo);
	    	return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
		}
    	
    	
    }
//	
	
	
}
