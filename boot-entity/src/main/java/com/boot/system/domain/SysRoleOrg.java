package com.boot.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和机构关联 sys_role_org
 * 
 * @author epl
 */
public class SysRoleOrg
{
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
