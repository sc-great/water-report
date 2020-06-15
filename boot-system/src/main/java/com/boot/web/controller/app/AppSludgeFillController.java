package com.boot.web.controller.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.boot.common.utils.DateUtils;
import com.boot.common.utils.InitsUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.TodayWaterYieldInfo;
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
import com.boot.report.domain.MudInfo;
import com.boot.report.service.IMudInfoService;
import com.boot.system.domain.SysUser;

/**
 * 手机端污泥填报Controller
 * @author LM
 *
 */
@Controller
@RequestMapping("/app/sludge")
public class AppSludgeFillController extends APPBaseController {

	private String prefix = "app/pages";
	
	@Autowired
	private IMudInfoService mudInfoService;

	@GetMapping("/toFillPage")
	public String toFillPage(ModelMap mMap) {
		mMap.put("orgName", getLoginUserOrg().getOrgName());
		return prefix + "/sludge_fill";
	}
	
	/**
	 * 查询本水厂下的所有有效信息
	 */
	@RequestMapping("/sludgeFactoryData")
	@ResponseBody
	public AjaxResult sludgeFactoryData(String date) {
		MudInfo mudInfo = new MudInfo();
		mudInfo.setEffectIcon("1");
		mudInfo.setFactoryId(getLoginUserOrg().getOrgId());
		Map<String, Object> params = new HashMap<>();
		params.put("date", date);
		mudInfo.setParams(params);
		return AjaxResult.success(mudInfoService.getList(mudInfo));
	}
	
	@RequestMapping("/getSludgeLatest")
	@ResponseBody
	public AjaxResult getSludgeLatest() {
		MudInfo mudInfo = new MudInfo();
		mudInfo.setEffectIcon("1");
		mudInfo.setFactoryId(getLoginUserOrg().getOrgId());
		Map<String, Object> params = new HashMap<>();
		params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		mudInfo.setParams(params);
		return AjaxResult.success(mudInfoService.getEntity(mudInfo));
	}
	
	@Log(title = "水厂污泥数据信息", businessType = BusinessType.INSERT)
	@PostMapping("/sludgeAdd")
	@ResponseBody
	public AjaxResult sludgeAdd(MudInfo mudInfo) {
		SysUser curUser = getLoginUserInfo();
		mudInfo.setFillUserId(curUser.getUserId());
		mudInfo.setFillUserName(curUser.getUserName());
		mudInfo.setFactoryId(getLoginUserOrg().getOrgId());
		mudInfo.setAreaId(curUser.getAreaStr());
		return AjaxResult.success(mudInfoService.add(mudInfo));
	}

	/**
	 * 新增保存当日污泥数据信息
	 * yangxiaojun
	 * 2020-05-14
	 */

	@Log(title = "新增保存当日污泥数据信息", businessType = BusinessType.INSERT)
	@PostMapping("/doAddMudInfo")
	@ResponseBody
	public AjaxResult doAddMudInfo(MudInfo mudInfo) {
		Map<String, Object> map = new HashMap<String, Object>();

		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {

			mudInfo.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			mudInfo.setFillDate(DateUtils.getDate());
			mudInfo.setEffectIcon("1");
			MudInfo  existEntity=	mudInfoService.getEntity(mudInfo);
			if(existEntity!=null){
				map.put("flag", "-1");
				map.put("mess", "今日已填报！");

			}else{
				mudInfo.setAreaId(sysUser.getAreaStr());
				mudInfo.setFillUserId(sysUser.getUserId());
				mudInfo.setFillUserName(sysUser.getUserName());
				//当日绝干污泥量=污泥日产*(1-污泥含水率)
				Double tempOne=new Double(100);
				Double temptwo = InitsUtils.div(InitsUtils.subtract(tempOne, mudInfo.getWaterIn()), tempOne,2);

				mudInfo.setDryMud( InitsUtils.mul(temptwo,mudInfo.getTodayMud()) );
				//污泥累计、绝干污泥累计
				MudInfo temp = new MudInfo();
				temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
				temp.setFillDate(DateUtils.getDate());
				temp.setEffectIcon("1");
				Map<String, Object> sumMudInfo = mudInfoService.getSumByAppArea(temp);
				Double	 sumTodayMud  = Double.parseDouble(sumMudInfo.get("sumTodayMud").toString())  + mudInfo.getTodayMud() ;
				Double	 sumDryMud  =   Double.parseDouble(sumMudInfo.get("sumDryMud").toString())  +  mudInfo.getDryMud() ;
				mudInfo.setTotalMud(sumTodayMud);
				mudInfo.setDryMudTotal(sumDryMud);
				mudInfoService.add(mudInfo);
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
	 * 修改当日污泥数据信息
	 * yangxiaojun
	 * 2020-05-14
	 */

	@Log(title = "修改当日污泥数据信息", businessType = BusinessType.UPDATE)
	@PostMapping("/doEditMudInfo")
	@ResponseBody
	public AjaxResult doEditMudInfo(MudInfo mudInfo) {
		SysUser sysUser = getLoginUserInfo();
		if (StringUtils.isNotNull(sysUser)) {
			MudInfo oldEntity=new MudInfo();
			oldEntity.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			oldEntity.setFillDate(DateUtils.getDate());
			oldEntity.setEffectIcon("1");

			MudInfo  existEntity=	mudInfoService.getEntity(oldEntity);
			mudInfo.setId(existEntity.getId());

			//当日绝干污泥量=污泥日产*(1-污泥含水率)
			Double tempOne=new Double(100);
			Double temptwo = InitsUtils.div(InitsUtils.subtract(tempOne, mudInfo.getWaterIn()), tempOne,2);

			mudInfo.setDryMud( InitsUtils.mul(temptwo,mudInfo.getTodayMud()) );
			//污泥累计、绝干污泥累计
			MudInfo temp = new MudInfo();
			temp.setFactoryId(sysUser.getSysOrgs().get(0).getOrgId());
			temp.setFillDate(DateUtils.getDate());
			temp.setEffectIcon("1");
			Map<String, Object> sumMudInfo = mudInfoService.getSumByAppArea(temp);
			Double	 sumTodayMud  = Double.parseDouble(sumMudInfo.get("sumTodayMud").toString())  + mudInfo.getTodayMud() ;
			Double	 sumDryMud  =   Double.parseDouble(sumMudInfo.get("sumDryMud").toString())  +  mudInfo.getDryMud() ;
			mudInfo.setTotalMud(sumTodayMud);
			mudInfo.setDryMudTotal(sumDryMud);

			mudInfoService.update(mudInfo);
			return AjaxResult.success("0");
		}else{
			return AjaxResult.error("登录超时,请重新登录！");

		}


	}

}
