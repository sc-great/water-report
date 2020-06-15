package com.boot.system.service;

import java.util.List;
import java.util.Map;

import com.boot.common.core.domain.Ztree;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

/**
 * 用户 业务层
 * 
 * @author epl
 */
public interface ISysUserService
{
    /**
     * 添加
     *
     * @param sysUser 用户信息
     * @return 影响行数
     */
    public int insert(SysUser sysUser);

    /**
     * 修改
     *
     * @param sysUser 用户信息
     * @return 影响行数
     */
    public int update(SysUser sysUser);
    /**
     * 修改用户信息和组织机构以及岗位角色
     *
     * @param sysUser 用户信息
     * @return 影响行数
     */
    public int updateOrgAndRole(SysUser sysUser);
    /**
     * 查询数量
     *
     * @param sysUser 查询条件
     * @return 数量
     */
    public int getCount(SysUser sysUser);

    /**
     * 登录
     *
     * @param loginName 登录帐号
     * @return 实体对象
     */
    public SysUser loginName(String loginName);
    /**
     * 查询实体对象
     *
     * @param id 查询ID
     * @return 实体对象
     */
    public SysUser getById(String id);
    /**
     * 查询实体对象
     *
     * @param sysUser 查询条件
     * @return 实体对象
     */
    public SysUser getEntity(SysUser sysUser);

    /**
     * 查询
     *
     * @param sysUser 查询条件
     * @return 集合
     */
    public List<SysUser> findList(SysUser sysUser);
    /**
     * 查询
     *
     * @param userIds 查询条件
     * @return 集合
     */
    public List<SysUser> findListByIds(String userIds);
    /**
     * 删除
     *
     * @param sysUser 删除条件
     * @return 影响行数
     */
    public int delete(SysUser sysUser);
    /**
     * 批量删除
     *
     * @param userIds 删除条件
     * @return 影响行数
     */
    public int deleteByIds(String userIds);
    /**
     * 查询机构下的用户集合
     *
     * @param orgId 机构ID
     * @return 集合
     */
    public List<SysUser> getUserListByOrgId(String orgId);
    /**
     * 查询角色下的用户集合
     *
     * @param roleId 角色ID
     * @return 用户集合
     */
    public List<SysUser> getUserListByRoleId(String roleId);

    /**
     * 导入人力资源用户信息
     *
     * @param sysUser 用户
     * @return int
     * */
    public int importHRUserData(SysUser sysUser);

    /**
     * 根据条件查询部门用户树形列表
     *
     * @param params 参数集合
     * @return 部门用户树形列表
     */
    public List<Ztree> deptUserTreeData(Map<String,Object> params);
}