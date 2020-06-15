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
import com.boot.report.domain.TestBadWaterInfo;
import com.boot.report.service.ITestBadWaterInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;

/**
 * 手机端:化验数据_污水 Controller
 * @author yangxiaojun
 *
 */
@Controller
@RequestMapping("/app")
public class AppTestBadWaterInfoController extends APPBaseController {
//	private String prefix = "app/pages";

	  @Autowired
	  private ITestBadWaterInfoService testBadWaterInfoService;


    /**
     * 新增保存信息
     */
   
    @Log(title = "污水厂化验信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddTestBadWaterInfo")
    @ResponseBody
    public AjaxResult doAdd(TestBadWaterInfo testBadWaterInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			SysOrg curOrg = sysUser.getSysOrgs().get(0) ;
			if (!(("3").equals(curOrg.getOrgType())&&("1").equals(curOrg.getFactoryType()))) {
				
				 return AjaxResult.warn("您不是污水厂用户，不能进行污水厂化验数据填报！");
			}
			
			testBadWaterInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());			
			testBadWaterInfo.setFillDate(DateUtils.getDate());
			testBadWaterInfo.setEffectIcon("1");
			TestBadWaterInfo  existEntity=	testBadWaterInfoService.getEntity(testBadWaterInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");
				
			}else{
				testBadWaterInfo.setAreaId(sysUser.getAreaStr());				
				testBadWaterInfo.setFillUserId(sysUser.getUserId());			
				testBadWaterInfo.setFillUserName(sysUser.getUserName());
				testBadWaterInfoService.add(testBadWaterInfo);			    
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
    @PostMapping("/doEditTestBadWaterInfo")
    @ResponseBody
    public AjaxResult doEdit(TestBadWaterInfo testBadWaterInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			TestBadWaterInfo oldEntity=new TestBadWaterInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
	    	TestBadWaterInfo  existEntity=	testBadWaterInfoService.getEntity(oldEntity); 
	    	testBadWaterInfo.setId(existEntity.getId());
	    	testBadWaterInfoService.update(testBadWaterInfo);
	    	return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
		}
    	
    	
    }
//	
	
	
}
