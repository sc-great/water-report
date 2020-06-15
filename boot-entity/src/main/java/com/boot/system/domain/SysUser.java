package com.boot.system.domain;

import java.util.List;
import com.boot.common.core.domain.BaseEntity;

/**
 * 用户对象 sys_user
 * 
 * @author epl
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private String userId;

    /** 人员编码 */
    private String userCode;

    /** 人员名称 */
    private String userName;

    /** 邮件地址 */
    private String email;

    /** 是否禁用 */
    private String accountIsDisabled;

    /** 手机号 */
    private String phone;

    /** 性别 */
    private String sex;

    /** 生日 */
    private String birthday;

    /** 工号 */
    private String jobNumber;

    /** 身份证号 */
    private String cardNo;

    /** 登录帐号 */
    private String loginName;

    /** 登录密码 */
    private String password;

    /** 盐加密 */
    private String salt;

    /** 头像 */
    private String avatar;

    /** 登录IP地址 */
    private String loginIp;

    /** 登录IP地址 */
    private String loginDate;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 管辖区域字符集 */
    private String areaStr;

    /** 用户机构信息 */
    private List<SysOrg> sysOrgs;

    /** 用户角色岗位信息 */
    private List<SysRole> sysRoles;

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountIsDisabled() {
        return accountIsDisabled;
    }

    public void setAccountIsDisabled(String accountIsDisabled) {
        this.accountIsDisabled = accountIsDisabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public List<SysOrg> getSysOrgs() {
        return sysOrgs;
    }

    public void setSysOrgs(List<SysOrg> sysOrgs) {
        this.sysOrgs = sysOrgs;
    }

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    /**
     *
     * 判断是否是超级管理员
     * */
    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(String userId)
    {
        return userId != null && "1".equals(userId);
    }

    public String getAreaStr() {
        return areaStr;
    }

    public void setAreaStr(String areaStr) {
        this.areaStr = areaStr;
    }
}
