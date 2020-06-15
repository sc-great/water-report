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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.common.annotation.Log;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.report.service.IBadWaterQualityInfoService;
import com.boot.report.service.IGoodWaterHealthInfoService;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysFactoryInfoService;
import com.boot.system.service.ISysOrgService;

/**
 * 手机端:污水水质信息 Controller
 * @author yangxiaojun
 *
 */
@Controller
@RequestMapping("/app")
public class AppBadWaterQualityInfoController extends APPBaseController {

	private String prefix = "app/pages";
	 
	  @Autowired
	  private ISysFactoryInfoService sysFactoryInfoService;

	  @Autowired
	  private ISysOrgService orgService;
	  @Autowired
	  private IBadWaterQualityInfoService badWaterQualityInfoService;
	  @Autowired
	  private IGoodWaterHealthInfoService goodWaterHealthInfoService;
    /**
     * 新增保存信息
     */
   
    @Log(title = "新增污水厂水质信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddBadWaterQualityInfo")
    @ResponseBody
    public AjaxResult doAdd(BadWaterQualityInfo badWaterQualityInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			SysOrg curOrg = sysUser.getSysOrgs().get(0) ;
			if (!(("3").equals(curOrg.getOrgType())&&("1").equals(curOrg.getFactoryType()))) {
				
				 return AjaxResult.warn("您不是污水厂用户，不能进行污水厂水质填报！");
			}
			
			badWaterQualityInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());			
			badWaterQualityInfo.setFillDate(DateUtils.getDate());
			badWaterQualityInfo.setEffectIcon("1");
			BadWaterQualityInfo  existEntity=	badWaterQualityInfoService.getEntity(badWaterQualityInfo); 
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");					
			}else{
				badWaterQualityInfo.setAreaId(sysUser.getAreaStr());				
				badWaterQualityInfo.setFillUserId(sysUser.getUserId());			
				badWaterQualityInfo.setFillUserName(sysUser.getUserName());
							
			   String passFlag = sysFactoryInfoService.badWaterDictDataIsPass(badWaterQualityInfo);		
			   String  alarmNote="";
				if("1".equals(passFlag)){
				  badWaterQualityInfo.setPassFlag(passFlag);	
				}else if(passFlag.indexOf(";")!=-1){		 
				  String[]  resultArray= passFlag.split(";");
				  badWaterQualityInfo.setPassFlag(resultArray[0]);	
				  alarmNote=resultArray[1];
				}	
				
				badWaterQualityInfoService.add(badWaterQualityInfo,alarmNote);			    
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
   
    @Log(title = "修改污水水质填报信息", businessType = BusinessType.UPDATE)
    @PostMapping("/doEditBadWaterQualityInfo")
    @ResponseBody
    public AjaxResult doEdit(BadWaterQualityInfo badWaterQualityInfo) {
    	SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
	    	BadWaterQualityInfo oldEntity=new BadWaterQualityInfo();
	    	oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());	    	
	    	oldEntity.setFillDate(DateUtils.getDate());
	    	oldEntity.setEffectIcon("1");
	    	
			BadWaterQualityInfo  existEntity=	badWaterQualityInfoService.getEntity(oldEntity); 
			badWaterQualityInfo.setId(existEntity.getId());

			
			badWaterQualityInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
		    String passFlag = sysFactoryInfoService.badWaterDictDataIsPass(badWaterQualityInfo);
			String alarmNote = "";
			if ("1".equals(passFlag)) {
				badWaterQualityInfo.setPassFlag(passFlag);
			} else if (passFlag.indexOf(";") != -1) {
				String[] resultArray = passFlag.split(";");
				badWaterQualityInfo.setPassFlag(resultArray[0]);
				alarmNote = resultArray[1];
			}
			badWaterQualityInfo.setFillUserId(sysUser.getUserId());
			badWaterQualityInfo.setFillUserName(sysUser.getUserName());
			badWaterQualityInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			badWaterQualityInfo.setAreaId(sysUser.getAreaStr());
			badWaterQualityInfoService.update(badWaterQualityInfo, alarmNote);
			return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");
			
		}
    	
    	
    }
     /***
      * 运营能力二级页面，列表展示不合格的污水和自来水的单位和填报日期
      */
    
    @RequestMapping("/waterQualityNotPassData")
	@ResponseBody
	public AjaxResult waterQualityNotPassData(String date) {
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
			sysOrg_params.setOrgType("3");
			sysOrg_params.setSortName("factory_type"); //水厂类型排序，污水厂前、自来水后
			List<SysOrg> orgList = orgService.findList(sysOrg_params);
			List<SysOrg> orgListByNotPass =  new ArrayList<>();  //只要有不合格的记录的水厂机构
			for (SysOrg item : orgList) {
				// 污水厂/排水厂
				if ("1".equals(item.getFactoryType())) {
					// 水质数据
					BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
					badWaterQualityInfo.setEffectIcon("1");
					badWaterQualityInfo.setPassFlag("2");
					badWaterQualityInfo.setFactoryId(item.getOrgId());					
					List<BadWaterQualityInfo>   badWaterQualityInfoList=    badWaterQualityInfoService.getList(badWaterQualityInfo);
					badWaterQualityInfo.setBwqiList(badWaterQualityInfoList);
					item.getParams().put("badWaterQualityInfo", badWaterQualityInfo);
					if (badWaterQualityInfoList.size()>0){
						orgListByNotPass.add(item);
					}
				}
				// 自来水厂/给水厂
				else if ("2".equals(item.getFactoryType())) {
					// 水质数据
					GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
					goodWaterHealthInfo.setEffectIcon("1");
					goodWaterHealthInfo.setPassFlag("2");
					goodWaterHealthInfo.setFactoryId(item.getOrgId());
					List<GoodWaterHealthInfo>   goodWaterHealthInfoList=  goodWaterHealthInfoService.getList(goodWaterHealthInfo);
					goodWaterHealthInfo.setGoodWaterList(goodWaterHealthInfoList);
					
					item.getParams().put("goodWaterHealthInfo", goodWaterHealthInfo);
					if (goodWaterHealthInfoList.size()>0){
						orgListByNotPass.add(item);
						//System.out.println("==========GwhiList=========="+goodWaterHealthInfo.getGwhiList());
					}

				}
			}
			ajaxResult = AjaxResult.success(orgListByNotPass);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
	
    @GetMapping("/waterQualityNotPass")
	public String waterQualityNotPass( ModelMap mMap) {		
		return prefix + "/water_quality_not_pass";
	}
}
