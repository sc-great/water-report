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
import com.boot.common.utils.InitsUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.TodayElectricityInfo;
import com.boot.report.service.ITodayElectricityInfoService;
import com.boot.system.domain.SysUser;

/**
 * 手机端:  水厂当日用电信息表  Controller
 * @author yangxiaojun
 * date:   2020-05-05
 */
@Controller
@RequestMapping("/app")
public class AppTodayElectricityInfoController extends APPBaseController {
	 //private String prefix = "app/pages";

	  @Autowired
	  private ITodayElectricityInfoService todayElectricityInfoService;
	

    /**
     * 新增保存信息
     */
   
    @Log(title = "信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddTodayElectricityInfo")
    @ResponseBody
    public AjaxResult doAdd(TodayElectricityInfo todayElectricityInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
					
			todayElectricityInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());		
			todayElectricityInfo.setFillDate(DateUtils.getDate());
			todayElectricityInfo.setEffectIcon("1");
			TodayElectricityInfo  existEntity=	todayElectricityInfoService.getEntity(todayElectricityInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");
				
			}else{
				todayElectricityInfo.setAreaId(sysUser.getAreaStr());					
				todayElectricityInfo.setFillUserId(sysUser.getUserId());			
				todayElectricityInfo.setFillUserName(sysUser.getUserName());
				//累计当日以前的电量
				TodayElectricityInfo temp = new TodayElectricityInfo();
				temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
				temp.setEffectIcon("1");
				TodayElectricityInfo totalEntity = todayElectricityInfoService.getLatest(temp);	
				Double	 totalElectricity =new Double(0);
				Double	 pumpTotalEletricity=new Double(0);
				//System.out.println("======totalEntity.getPumpTotalEletricity()===="+totalEntity.getPumpTotalEletricity()+"====totalEntity.getTodayElectricity()===="+totalEntity.getTodayElectricity());
				if (totalEntity!=null){
					// totalElectricity  =  todayElectricityInfo.getTodayElectricity()+totalEntity.getTodayElectricity();
					// pumpTotalEletricity  =todayElectricityInfo.getPumpTodayEletricity()+totalEntity.getPumpTotalEletricity() ;
					totalElectricity  =InitsUtils.add(todayElectricityInfo.getTodayElectricity(), totalEntity.getTodayElectricity());
					pumpTotalEletricity  =InitsUtils.add(todayElectricityInfo.getPumpTodayEletricity(), totalEntity.getPumpTotalEletricity());
				}else{
					
					 totalElectricity  = todayElectricityInfo.getTodayElectricity();
				     pumpTotalEletricity  =todayElectricityInfo.getPumpTodayEletricity();
				}
				todayElectricityInfo.setTotalElectricity(totalElectricity);
				todayElectricityInfo.setPumpTotalEletricity(pumpTotalEletricity);
				todayElectricityInfoService.add(todayElectricityInfo);			    
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
    @PostMapping("/doEditTodayElectricityInfo")
    @ResponseBody
    public AjaxResult doEdit(TodayElectricityInfo todayElectricityInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			TodayElectricityInfo oldEntity=new TodayElectricityInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
	    	TodayElectricityInfo  existEntity=	todayElectricityInfoService.getEntity(oldEntity); 
	    	todayElectricityInfo.setId(existEntity.getId());
	    	
	    	//累计进出水量
	    	//累计当日以前的电量
			TodayElectricityInfo temp = new TodayElectricityInfo();
			temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			temp.setEffectIcon("1");
			TodayElectricityInfo totalEntity = todayElectricityInfoService.getLatest(temp);	
			Double	 totalElectricity =new Double(0);
			Double	 pumpTotalEletricity=new Double(0);
			if (totalEntity!=null){
//				 totalElectricity  =  todayElectricityInfo.getTodayElectricity()+totalEntity.getTodayElectricity();
//				 pumpTotalEletricity  =todayElectricityInfo.getPumpTodayEletricity()+totalEntity.getPumpTotalEletricity() ;
				totalElectricity  =InitsUtils.add(todayElectricityInfo.getTodayElectricity(), totalEntity.getTodayElectricity());
				pumpTotalEletricity  =InitsUtils.add(todayElectricityInfo.getPumpTodayEletricity(), totalEntity.getPumpTotalEletricity());
			}else{
				 totalElectricity  = todayElectricityInfo.getTodayElectricity();
			     pumpTotalEletricity  =todayElectricityInfo.getPumpTodayEletricity();
			}
			todayElectricityInfo.setTotalElectricity(totalElectricity);
			todayElectricityInfo.setPumpTotalEletricity(pumpTotalEletricity);
			
			todayElectricityInfoService.update(todayElectricityInfo);
	    	return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
		}
    	
    	
    }
//	
	
	
}
