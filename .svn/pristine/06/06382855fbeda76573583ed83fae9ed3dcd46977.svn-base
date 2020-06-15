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
import com.boot.report.domain.TodayWaterYieldInfo;
import com.boot.report.service.ITodayWaterYieldInfoService;
import com.boot.system.domain.SysUser;

/**
 * 手机端:  水厂当日水量消息表 Controller
 * @author yangxiaojun
 *
 */
@Controller
@RequestMapping("/app")
public class AppTodayWaterYieldInfoController extends APPBaseController {
	 //private String prefix = "app/pages";

	  @Autowired
	  private ITodayWaterYieldInfoService todayWaterYieldInfoService;
	

    /**
     * 新增保存信息
     */
   
    @Log(title = "信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddTodayWaterYieldInfo")
    @ResponseBody
    public AjaxResult doAdd(TodayWaterYieldInfo todayWaterYieldInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
					
			todayWaterYieldInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());		
			todayWaterYieldInfo.setFillDate(DateUtils.getDate());
			todayWaterYieldInfo.setEffectIcon("1");
			TodayWaterYieldInfo  existEntity=	todayWaterYieldInfoService.getEntity(todayWaterYieldInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");
				
			}else{
				todayWaterYieldInfo.setAreaId(sysUser.getAreaStr());					
				todayWaterYieldInfo.setFillUserId(sysUser.getUserId());			
				todayWaterYieldInfo.setFillUserName(sysUser.getUserName());
				//累计进出水量
				TodayWaterYieldInfo temp = new TodayWaterYieldInfo();
				temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
				Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getAddOldTotalInOut(temp);				
				Double	 totalIn  =  (Double.parseDouble(sumWaterYield.get("totalTodayIn").toString()) * 10000 + todayWaterYieldInfo.getTodayIn() )/10000 ;
				Double	 totalOut  =  (Double.parseDouble(sumWaterYield.get("totalTodayOut").toString()) * 10000 + todayWaterYieldInfo.getTodayOut() )/10000 ;
				todayWaterYieldInfo.setTotalIn(totalIn);
				todayWaterYieldInfo.setTotalOut(totalOut);
				todayWaterYieldInfoService.add(todayWaterYieldInfo);			    
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
    @PostMapping("/doEditTodayWaterYieldInfo")
    @ResponseBody
    public AjaxResult doEdit(TodayWaterYieldInfo todayWaterYieldInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			TodayWaterYieldInfo oldEntity=new TodayWaterYieldInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
	    	TodayWaterYieldInfo  existEntity=	todayWaterYieldInfoService.getEntity(oldEntity); 
	    	todayWaterYieldInfo.setId(existEntity.getId());
	    	
	    	//累计进出水量
			TodayWaterYieldInfo temp = new TodayWaterYieldInfo();
			temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			Map<String, Object> sumWaterYield = todayWaterYieldInfoService.getAddOldTotalInOut(temp);				
			Double	 totalIn  =  (Double.parseDouble(sumWaterYield.get("totalTodayIn").toString()) * 10000 + todayWaterYieldInfo.getTodayIn() )/10000 ;
			Double	 totalOut  =  (Double.parseDouble(sumWaterYield.get("totalTodayOut").toString()) * 10000 + todayWaterYieldInfo.getTodayOut() )/10000 ;
			todayWaterYieldInfo.setTotalIn(totalIn);
			todayWaterYieldInfo.setTotalOut(totalOut);
	    	todayWaterYieldInfoService.update(todayWaterYieldInfo);
	    	return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
		}
    	
    	
    }
//	
	
	
}
