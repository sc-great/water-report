package com.boot.framework.shiro.service;

import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.service.ISysOrgService;
import com.boot.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.boot.common.constant.Constants;
import com.boot.common.constant.ShiroConstants;
import com.boot.common.constant.UserConstants;
import com.boot.common.enums.UserStatus;
import com.boot.common.exception.user.CaptchaException;
import com.boot.common.exception.user.UserBlockedException;
import com.boot.common.exception.user.UserNotExistsException;
import com.boot.common.exception.user.UserPasswordNotMatchException;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.MessageUtils;
import com.boot.common.utils.ServletUtils;
import com.boot.framework.manager.AsyncManager;
import com.boot.framework.manager.factory.AsyncFactory;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysUserService;

import java.util.List;

/**
 * 登录校验方法
 * 
 * @author epl
 */
@Component
public class SysLoginService
{
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysOrgService orgService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserService userService;

    /**
     * 登录
     */
    public SysUser login(String username, String password)
    {
        // 验证码校验
        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 查询用户信息
        SysUser user = userService.loginName(username);

        if (user == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        
        if (UserStatus.DISABLE.getCode().equals(user.getAccountIsDisabled()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }

        passwordService.validate(user, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(user);
        //获取用户机构和角色
        List<SysOrg> sysOrgs=orgService.getOrgListByUserId(user.getUserId());
        user.setSysOrgs(sysOrgs);
        List<SysRole> sysRoles=roleService.getRoleListByUserId(user.getUserId());
        user.setSysRoles(sysRoles);
        return user;
    }

    private boolean maybeEmail(String username)
    {
        if (!username.matches(UserConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username)
    {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN))
        {
            return false;
        }
        return true;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user)
    {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate().toString());
        userService.update(user);
    }
}
