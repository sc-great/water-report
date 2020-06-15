package com.boot.system.domain;

/**
 * 岗位角色权限部门
 *
 * @author epl
 */
public class SysRoleOrgScope {
    /** 角色ID */
    private String roleId;

    /** 部门ID */
    private String orgId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
