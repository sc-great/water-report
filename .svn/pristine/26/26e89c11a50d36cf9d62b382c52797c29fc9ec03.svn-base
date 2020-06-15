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
import com.boot.report.domain.TodayMedicineInfo;
import com.boot.report.service.ITodayMedicineInfoService;
import com.boot.system.domain.SysUser;

/**
 * 手机端:  水厂当日用药信息填报表  Controller
 * @author yangxiaojun
 * date:   2020-05-05
 */
@Controller
@RequestMapping("/app")
public class AppTodayMedicineInfoController extends APPBaseController {
	 //private String prefix = "app/pages";

	  @Autowired
	  private ITodayMedicineInfoService todayMedicineInfoService;
	

    /**
     * 新增保存信息
     */
   
    @Log(title = "信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddTodayMedicineInfo")
    @ResponseBody
    public AjaxResult doAdd(TodayMedicineInfo todayMedicineInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
					
			todayMedicineInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());		
			todayMedicineInfo.setFillDate(DateUtils.getDate());
			todayMedicineInfo.setEffectIcon("1");
	    	
	    	TodayMedicineInfo  existEntity=	todayMedicineInfoService.getEntity(todayMedicineInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");
				
			}else{
				todayMedicineInfo.setAreaId(sysUser.getAreaStr());					
				todayMedicineInfo.setFillUserId(sysUser.getUserId());			
				todayMedicineInfo.setFillUserName(sysUser.getUserName());
				//累计当日以前的药量
				TodayMedicineInfo temp = new TodayMedicineInfo();
				temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
				temp.setEffectIcon("1");
				TodayMedicineInfo totalEntity = todayMedicineInfoService.getLatest(temp);	
				Double	 totalPac =new Double(0);
				Double	 totalPamYin=new Double(0);
				Double	 totalPamYang=new Double(0);
				Double	 totalPhosphorus=new Double(0);
				Double	 totalNaclo=new Double(0);
				Double	 totalLime=new Double(0);
				Double	 totalGlucose=new Double(0);
				Double	 totalSc=new Double(0);
				Double	 totalSa=new Double(0);
				Double	 totalHCL=new Double(0);
				
				if (totalEntity!=null){
					totalPac  =  todayMedicineInfo.getTodayPac()+totalEntity.getTotalPac();
					totalPamYin  =  todayMedicineInfo.getTodayPamYin() +totalEntity.getTotalPamYin();
					totalPamYang  =  todayMedicineInfo.getTodayPamYang()+totalEntity.getTotalPamYang();
					totalPhosphorus  =  todayMedicineInfo.getTodayPhosphorus()+totalEntity.getTotalPhosphorus();
					totalNaclo  =  todayMedicineInfo.getTodayNaclo()+totalEntity.getTotalNaclo();
					totalLime  =  todayMedicineInfo.getTodayLime()+totalEntity.getTotalLime();
					totalGlucose  =  todayMedicineInfo.getTodayGlucose()+totalEntity.getTotalGlucose();
					totalSc  =  todayMedicineInfo.getTodaySc()+totalEntity.getTotalSc();
					totalSa  =  todayMedicineInfo.getTodaySa()+totalEntity.getTotalSa();
					totalHCL  =  todayMedicineInfo.getTodayHCL()+totalEntity.getTotalHCL();
					
				}else{
					totalPac  =  todayMedicineInfo.getTodayPac();
					totalPamYin  =  todayMedicineInfo.getTodayPamYin() ;
					totalPamYang  =  todayMedicineInfo.getTodayPamYang();
					totalPhosphorus  =  todayMedicineInfo.getTodayPhosphorus();
					totalNaclo  =  todayMedicineInfo.getTodayNaclo();
					totalLime  =  todayMedicineInfo.getTodayLime();
					totalGlucose  =  todayMedicineInfo.getTodayGlucose();
					totalSc  =  todayMedicineInfo.getTodaySc();
					totalSa  =  todayMedicineInfo.getTodaySa();
					totalHCL  =  todayMedicineInfo.getTodayHCL();
				}
				todayMedicineInfo.setTotalPac(totalPac);
				todayMedicineInfo.setTotalPamYin(totalPamYin);
				todayMedicineInfo.setTotalPamYang(totalPamYang);
				todayMedicineInfo.setTotalPhosphorus(totalPhosphorus);
				todayMedicineInfo.setTotalNaclo(totalNaclo);
				todayMedicineInfo.setTotalLime(totalLime);
				todayMedicineInfo.setTotalGlucose(totalGlucose);
				todayMedicineInfo.setTotalSc(totalSc);
				todayMedicineInfo.setTotalSa(totalSa);
				todayMedicineInfo.setTotalHCL(totalHCL);
								
				todayMedicineInfoService.add(todayMedicineInfo);			    
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
    @PostMapping("/doEditTodayMedicineInfo")
    @ResponseBody
    public AjaxResult doEdit(TodayMedicineInfo todayMedicineInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			TodayMedicineInfo oldEntity=new TodayMedicineInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
	    	TodayMedicineInfo  existEntity=	todayMedicineInfoService.getEntity(oldEntity); 
	    	todayMedicineInfo.setId(existEntity.getId());
	    	
	    	//累计当日以前的药量
			TodayMedicineInfo temp = new TodayMedicineInfo();
			temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			temp.setEffectIcon("1");
			TodayMedicineInfo totalEntity = todayMedicineInfoService.getLatest(temp);	
			Double	 totalPac =new Double(0);
			Double	 totalPamYin=new Double(0);
			Double	 totalPamYang=new Double(0);
			Double	 totalPhosphorus=new Double(0);
			Double	 totalNaclo=new Double(0);
			Double	 totalLime=new Double(0);
			Double	 totalGlucose=new Double(0);
			Double	 totalSc=new Double(0);
			Double	 totalSa=new Double(0);
			Double	 totalHCL=new Double(0);
			
			if (totalEntity!=null){
				totalPac  =  todayMedicineInfo.getTodayPac()+totalEntity.getTotalPac();
				totalPamYin  =  todayMedicineInfo.getTodayPamYin() +totalEntity.getTotalPamYin();
				totalPamYang  =  todayMedicineInfo.getTodayPamYang()+totalEntity.getTotalPamYang();
				totalPhosphorus  =  todayMedicineInfo.getTodayPhosphorus()+totalEntity.getTotalPhosphorus();
				totalNaclo  =  todayMedicineInfo.getTodayNaclo()+totalEntity.getTotalNaclo();
				totalLime  =  todayMedicineInfo.getTodayLime()+totalEntity.getTotalLime();
				totalGlucose  =  todayMedicineInfo.getTodayGlucose()+totalEntity.getTotalGlucose();
				totalSc  =  todayMedicineInfo.getTodaySc()+totalEntity.getTotalSc();
				totalSa  =  todayMedicineInfo.getTodaySa()+totalEntity.getTotalSa();
				totalHCL  =  todayMedicineInfo.getTodayHCL()+totalEntity.getTotalHCL();
				
			}else{
				totalPac  =  todayMedicineInfo.getTodayPac();
				totalPamYin  =  todayMedicineInfo.getTodayPamYin() ;
				totalPamYang  =  todayMedicineInfo.getTodayPamYang();
				totalPhosphorus  =  todayMedicineInfo.getTodayPhosphorus();
				totalNaclo  =  todayMedicineInfo.getTodayNaclo();
				totalLime  =  todayMedicineInfo.getTodayLime();
				totalGlucose  =  todayMedicineInfo.getTodayGlucose();
				totalSc  =  todayMedicineInfo.getTodaySc();
				totalSa  =  todayMedicineInfo.getTodaySa();
				totalHCL  =  todayMedicineInfo.getTodayHCL();
			}
			todayMedicineInfo.setTotalPac(totalPac);
			todayMedicineInfo.setTotalPamYin(totalPamYin);
			todayMedicineInfo.setTotalPamYang(totalPamYang);
			todayMedicineInfo.setTotalPhosphorus(totalPhosphorus);
			todayMedicineInfo.setTotalNaclo(totalNaclo);
			todayMedicineInfo.setTotalLime(totalLime);
			todayMedicineInfo.setTotalGlucose(totalGlucose);
			todayMedicineInfo.setTotalSc(totalSc);
			todayMedicineInfo.setTotalSa(totalSa);
			todayMedicineInfo.setTotalHCL(totalHCL);
			
			todayMedicineInfoService.update(todayMedicineInfo);
	    	return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
	    }
    	
    	
    }
//	
	
	
}
