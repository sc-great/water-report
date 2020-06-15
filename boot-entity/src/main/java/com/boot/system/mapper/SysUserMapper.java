package com.boot.system.mapper;

import java.util.List;
import com.boot.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 用户表 数据层
 * 
 * @author epl
 */
@Component
@Mapper
public interface SysUserMapper
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
     * 查询数量
     * 
     * @param sysUser 查询条件
     * @return 数量
     */
    public int getCount(SysUser sysUser);

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
    public List<SysUser> findListByIds(@Param("userIds") String[] userIds);
    /**
     * 删除
     * 
     * @param sysUser 删除条件
     * @return 影响行数
     */
    public int delete(SysUser sysUser);
    public int deleteByIds(@Param("userIds") String[] userIds);

    /**
     * 查询机构下的用户集合
     *
     * @param orgId 机构ID
     * @return 集合
     */
    public List<SysUser> getUserListByOrgId(@Param("orgId") String orgId);
    /**
     * 查询角色下的用户集合
     *
     * @param roleId 角色ID
     * @return 用户集合
     */
    public List<SysUser> getUserListByRoleId(@Param("roleId") String roleId);

    /**
     * 查询部门用户集合
     *
     * @return 用户集合
     */
    public List<SysUser> getOrgUserList();

    /**
     * 查询部门用户集合
     * @param userIds 用户IDS
     *
     * @return 用户集合
     */
    public List<SysUser> findUserListByIds(@Param("userIds") String[] userIds);
}