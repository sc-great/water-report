package com.boot.web.controller.admin.system;

import com.boot.system.domain.SysFiles;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.service.ISysFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.boot.common.annotation.Log;
import com.boot.common.config.Global;
import com.boot.common.core.controller.BaseController;
import com.boot.common.core.domain.AjaxResult;
import com.boot.common.enums.BusinessType;
import com.boot.common.utils.StringUtils;
import com.boot.common.utils.file.FileUploadUtils;
import com.boot.framework.shiro.service.SysPasswordService;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息 业务处理
 * 
 * @author epl
 */
@Controller
@RequestMapping("/admin/system/user/profile")
public class SysProfileController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

    private String prefix = "admin/system/user/profile";

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysFilesService sysFilesService;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        //用户头像
        if(user.getAvatar()!=null && !"".equals(user.getAvatar())){
            SysFiles sysFiles=sysFilesService.selectSysFilesById(user.getAvatar());
            mmap.put("avatar",sysFiles);
        }
        //组织机构
        List<String> orgs=new ArrayList<>();
        List<SysOrg> sysOrgs=user.getSysOrgs();
        if(sysOrgs.size()>0){
            for(SysOrg sysOrg:sysOrgs){
                orgs.add(sysOrg.getOrgName());
            }
        }
        mmap.put("orgs",StringUtils.join(orgs,","));
        //角色岗位
        List<String> roles=new ArrayList<>();
        List<SysRole> sysRoles=user.getSysRoles();
        if(sysRoles.size()>0){
            for(SysRole sysRole:sysRoles){
                roles.add(sysRole.getRoleName());
            }
        }
        mmap.put("roles",StringUtils.join(roles,","));
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        SysUser user = ShiroUtils.getSysUser();
        return passwordService.matches(user, password);
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/doResetPwd")
    @ResponseBody
    public AjaxResult doResetPwd(String oldPassword, String newPassword)
    {
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword))
        {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
            if (userService.update(user) > 0)
            {
                ShiroUtils.setSysUser(user);
                return success();
            }
            return error();
        }
        else
        {
            return error("修改密码失败，旧密码错误");
        }
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        return prefix + "/avatar";
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/doUpdate")
    @ResponseBody
    public AjaxResult doUpdate(SysUser user)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        currentUser.setAvatar(user.getAvatar());
        currentUser.setUserName(user.getUserName());
        currentUser.setPhone(user.getPhone());
        currentUser.setEmail(user.getEmail());
        currentUser.setCardNo(user.getCardNo());
        currentUser.setBirthday(user.getBirthday());
        currentUser.setSex(user.getSex());
        if (userService.update(currentUser) > 0)
        {
            ShiroUtils.setSysUser(currentUser);
            return success();
        }
        return error();
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        try
        {
            if (!file.isEmpty())
            {
                String avatar = FileUploadUtils.upload(Global.getAvatarPath(), file);
                currentUser.setAvatar(avatar);
                if (userService.update(currentUser) > 0)
                {
                    ShiroUtils.setSysUser(currentUser);
                    return success();
                }
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }
}
