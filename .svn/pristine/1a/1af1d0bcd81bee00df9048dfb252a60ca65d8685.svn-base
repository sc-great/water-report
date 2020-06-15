package com.boot.web.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.common.utils.ServletUtils;
import com.boot.common.utils.StringUtils;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysUser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class APPBaseController {
	/**
	 * 获取request
	 */
	public HttpServletRequest getRequest() {
		return ServletUtils.getRequest();
	}

	/**
	 * 获取response
	 */
	public HttpServletResponse getResponse() {
		return ServletUtils.getResponse();
	}

	/**
	 * 获取session
	 */
	public HttpSession getSession() {
		HttpSession session = getRequest().getSession();
		session.setMaxInactiveInterval(60 * 60 * 4);// 单位秒
		return session;
	}
	
	/**
	 * 获取cookie
	 */
	public Cookie[] getCookie() {
		return getRequest().getCookies();
	}

	/**
	 * 获取当前登录信息
	 * */
	public SysUser getLoginUserInfo() {
		SysUser loginUserInfo = null;
		Object obj = getSession().getAttribute("loginUserInfo");
		if (StringUtils.isNotNull(obj)) {
			loginUserInfo = JSON.toJavaObject(JSONObject.parseObject(obj.toString()), SysUser.class);
		}
		return loginUserInfo;
	}

	/**
	 * 获取当前登录用户组织机构
	 * */
	public SysOrg getLoginUserOrg() {
		SysUser sysUser = getLoginUserInfo();
		return sysUser.getSysOrgs().get(0);
	}
}
