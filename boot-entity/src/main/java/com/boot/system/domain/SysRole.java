package com.boot.system.domain;

import javax.validation.constraints.*;

import com.boot.common.core.domain.BaseEntity;


/**
 * 角色岗位表 sys_role
 * 
 * @author epl
 */
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private String roleId;

    /** 角色编号 */
    private String roleCode;

    /** 角色名称 */
    private String roleName;

    /** 角色权限 */
    private String roleKey;

    /** 角色排序 */
    private String roleSort;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
    private String dataScope;

    /** 角色状态（1正常 0停用） */
    private String roleStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    @NotBlank(message = "岗位编号不能为空")
    @Size(min = 0, max = 20, message = "岗位编号长度不能超过20个字符")
    public String getRoleCode()
    {
        return roleCode;
    }

    public void setRoleCode(String roleCode)
    {
        this.roleCode = roleCode;
    }

    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 30, message = "岗位名称长度不能超过30个字符")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "显示顺序不能为空")
    public String getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(String roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getRoleStatus()
    {
        return roleStatus;
    }
    public void setRoleStatus(String roleStatus)
    {
        this.roleStatus = roleStatus;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }
}
