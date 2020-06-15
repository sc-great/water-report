package com.boot.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUserRole;

/**
 * 角色业务层
 * 
 * @author epl
 */
public interface ISysRoleService
{
    /**
     * 新增
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    public int insert(SysRole sysRole);
    /**
     * 删除
     *
     * @param sysRole 删除条件
     * @return 影响行数
     */
    public int delete(SysRole sysRole);
    /**
     * 删除编号
     *
     * @param id 删除条件
     * @return 影响行数
     */
    public int deleteById(String id);

    /**
     * 修改
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    public int update(SysRole sysRole);
    /**
     * 修改岗位角色和所属组织机构以及菜单权限
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    public int updateAndOrgs(SysRole sysRole);
    /**
     * 修改菜单权限
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    public int menusScope(SysRole sysRole);
    /**
     * 修改数据权限
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    public int authDataScope(SysRole sysRole);
    /**
     * 查询实体对象
     *
     * @param id 查询ID
     * @return 实体对象
     */
    public SysRole getById(String id);
    /**
     * 查询实体对象
     *
     * @param sysRole 查询条件
     * @return 实体对象
     */
    public SysRole getEntity(SysRole sysRole);
    /**
     * 查询数量
     *
     * @param sysRole 查询条件
     * @return 数量
     */
    public int getCount(SysRole sysRole);
    /**
     * 查询集合
     * 
     * @param sysRole 查询条件
     * @return 集合
     */
    public List<SysRole> findList(SysRole sysRole);
    /**
     * 查询用户的角色集合
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    public List<SysRole> getRoleListByUserId(String userId);
    public List<SysUserRole> getUserRoleList(String userId);
    /**
     * 查询机构下的角色集合
     *
     * @param orgId 机构ID
     * @return 角色集合
     */
    public List<SysRole> getRoleListByOrgId(String orgId);


    /**
     * 导入人力资源岗位信息
     * */
    public int importHRRoleData(SysRole sysRole);
}
