package com.boot.system.mapper;

import java.util.List;
import java.util.Map;

import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysRoleOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 角色表 数据层
 * 
 * @author epl
 */
@Component
@Mapper
public interface SysRoleMapper
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
     * 修改
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    public int update(SysRole sysRole);
    /**
     * 查询数量
     *
     * @param sysRole 查询条件
     * @return 数量
     */
    public int getCount(SysRole sysRole);
    /**
     * 查询实体对象
     *
     * @param sysRole 查询条件
     * @return 实体对象
     */
    public SysRole getEntity(SysRole sysRole);
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
    public List<SysRole> getRoleListByUserId(@Param("userId") String userId);
    /**
     * 查询机构下的角色集合
     *
     * @param orgId 机构ID
     * @return 角色集合
     */
    public List<SysRole> getRoleListByOrgId(@Param("orgId") String orgId);
}
