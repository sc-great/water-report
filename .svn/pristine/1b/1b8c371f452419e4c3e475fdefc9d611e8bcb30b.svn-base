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
import com.boot.common.core.text.Convert;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.materialControl.domain.MandatoryCheckInfo;
import com.boot.materialControl.service.IMandatoryCheckInfoService;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;
import com.boot.system.service.IAreaService;
import com.boot.system.service.ISysDictDataService;
import com.boot.system.service.ISysOrgService;

/**
 * 手机端强制填报 Controller
 * @author yangxiaojun
 *
 */
@Controller
@RequestMapping("/app")
public class AppMandatoryCheckInfoController extends APPBaseController {
	private String prefix = "app/pages";

	  @Autowired
	  private IMandatoryCheckInfoService mandatoryCheckInfoService;
	  @Autowired
	  private IAreaService areaService;
	  @Autowired
	  private ISysDictDataService dictDataService;
	  @Autowired
	  private ISysOrgService orgService;
		
	 /**
     * 首页栏目：强制检信息
     */
   
    @GetMapping("/addMandatoryCheck")
    public String add(ModelMap mmap)
    {
    	String goPage="mandatory_check_add";

     return prefix + "/"+goPage;
    }

    /**
     * 新增保存信息
     */
   
    @Log(title = "强制检测信息", businessType = BusinessType.INSERT)
    @PostMapping("/doAddMandatoryCheck")
    @ResponseBody
    public AjaxResult doAdd(MandatoryCheckInfo mandatoryCheckInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			mandatoryCheckInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			mandatoryCheckInfo.setAreaId(sysUser.getAreaStr());
						
			mandatoryCheckInfo.setFillUserId(sysUser.getUserId());			
			mandatoryCheckInfo.setFillUserName(sysUser.getUserName());
			
			mandatoryCheckInfoService.add(mandatoryCheckInfo);
		    
		     map.put("flag", "0");
			 map.put("mess", "保存成功！");
		    
		  }else{
			map.put("flag", "1");
		    map.put("mess", "登录超时,请重新登录！");
	     	
		}	
		return AjaxResult.success("0", map);//这里0表示流程执行成功
    }

	@GetMapping("/mandatoryCheckAreaFactory")
	public String mandatoryCheckAreaFactory() {

		return prefix + "/mandatory_check_mana";
	}

	/**
	 * 职工填报时，所在水厂的检测信息列表
	 */
	@RequestMapping("/mandatoryCheckAreaFactoryData")
	@ResponseBody
	public AjaxResult mandatoryCheckAreaFactoryData() {
		MandatoryCheckInfo mandatoryCheckInfo = new MandatoryCheckInfo();
		mandatoryCheckInfo.setEffectIcon("1");
		mandatoryCheckInfo.setFactoryId(getLoginUserOrg().getOrgId());
		List<MandatoryCheckInfo> newList= mandatoryCheckInfoService.getList(mandatoryCheckInfo);
								
		return AjaxResult.success(newList);
	}
	
	
	/**
	 * 领导级——强制检测
	 */
	@GetMapping("/mandatoryCheckArea")
	public String mandatoryCheckArea(ModelMap mMap) {
		SysUser sysUser = getLoginUserInfo();
		// 当前用户管理区域
		String areaIds = sysUser.getAreaStr();
		Map<String, Object> areaTypes = new HashMap<>();
		if (StringUtils.isNotNullAndNotEmpty(areaIds)) {
			Area area = new Area();
			area.getParams().put("ids", Convert.toStrArray(areaIds));
			List<Area> areaList = areaService.getList(area);
			for (Area item : areaList) {
				areaTypes.put(item.getGroupType(),
						dictDataService.selectDictLabel("r_group_type", item.getGroupType()));
			}
		}
		mMap.put("areaTypes", areaTypes);
		return prefix + "/mandatory_check_area";
	}
	
	
	/**
	 * 领导级_强制检测
	 */
		@RequestMapping("/mandatoryCheckData")
		@ResponseBody
		public AjaxResult mandatoryCheckData(String date) {
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
				List<SysOrg> orgList = orgService.findList(sysOrg_params);
				
				List<SysOrg> orgListByNotPass =  new ArrayList<>(); 
				
				for (SysOrg item : orgList) {
					
						// 强制检测填报信息
					    MandatoryCheckInfo mandatoryCheckInfo = new MandatoryCheckInfo();
					    mandatoryCheckInfo.setEffectIcon("1");
					   // mandatoryCheckInfo.setFillDate(date);
					    mandatoryCheckInfo.setFactoryId(item.getOrgId());
					    List<MandatoryCheckInfo>    listCheckInfoList = mandatoryCheckInfoService.getList(mandatoryCheckInfo);
					   // mandatoryCheckInfo.setManList(listCheckInfoList);
						//item.getParams().put("mandatoryCheckInfo", mandatoryCheckInfo);
					    List<MandatoryCheckInfo>  notPassCheckInfoList =  new ArrayList<>(); 
					    //只需要过期的数据
					   // String s1,s2;
					    for (MandatoryCheckInfo manEntity: listCheckInfoList){	
					    	//s1=manEntity.getNextCheckDate();
					    	//s2= DateUtils.getDate();
					    	 
					    	if((manEntity.getNextCheckDate()).compareTo(DateUtils.getDate())<=0){  //下一个检测日期或转期处理日期小于等于当前系统日期
						    	//if (StringUtils.isEmpty(manEntity.getCheckResult())){ //没有结果数据
						    		notPassCheckInfoList.add(manEntity);						    								    	
						    	//}
					    	 }
					    }
					   if (notPassCheckInfoList.size()>0){ 
						  mandatoryCheckInfo.setManList(notPassCheckInfoList);  
						  item.getParams().put("mandatoryCheckInfo", mandatoryCheckInfo);
						  orgListByNotPass.add(item);
					   }
				}
				// ajaxResult = AjaxResult.success(orgList);
				ajaxResult = AjaxResult.success(orgListByNotPass);
			} else {
				ajaxResult = AjaxResult.error();
			}
			return ajaxResult;
		}

		/**
		 * 强制检测：地图查询
		 * @param areaIds
		 * @param date
		 * @return
		 */
		
		@RequestMapping("/mandatoryCheckPictureData")
		@ResponseBody
		public AjaxResult mandatoryCheckPictureData(String areaIds, String date) {
			AjaxResult ajaxResult;
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
			List<SysOrg> orgList = orgService.findList(sysOrg_params);
			List<SysOrg> orgListByNotPass =  new ArrayList<>(); 
			
			for (SysOrg item : orgList) {
				
					// 强制检测填报信息
				    MandatoryCheckInfo mandatoryCheckInfo = new MandatoryCheckInfo();
				    mandatoryCheckInfo.setEffectIcon("1");
				   // mandatoryCheckInfo.setFillDate(date);
				    mandatoryCheckInfo.setFactoryId(item.getOrgId());
				    List<MandatoryCheckInfo>    listCheckInfoList = mandatoryCheckInfoService.getList(mandatoryCheckInfo);
			
				    List<MandatoryCheckInfo>  notPassCheckInfoList =  new ArrayList<>(); 
				    //只需要过期没有检查结果的数据
				    for (MandatoryCheckInfo manEntity: listCheckInfoList){					    	
				    	if((manEntity.getNextCheckDate()).compareTo(DateUtils.getDate())<=0){  //下一个检测日期或转期处理日期小于当前系统日期
					    	//if (StringUtils.isEmpty(manEntity.getCheckResult())){ //没有结果数据
					    		notPassCheckInfoList.add(manEntity);						    								    	
					    	//}
				    	 }
				    }
				   if (notPassCheckInfoList.size()>0){ 
					  mandatoryCheckInfo.setManList(notPassCheckInfoList);  
					  item.getParams().put("mandatoryCheckInfo", mandatoryCheckInfo);
					  orgListByNotPass.add(item);
				   }
			}
			// ajaxResult = AjaxResult.success(orgList);
			ajaxResult = AjaxResult.success(orgListByNotPass);

			return ajaxResult;
		}
				
		
}
