package com.boot.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.boot.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 用户与角色关联表 数据层
 * 
 * @author epl
 */
@Component
@Mapper
public interface SysUserRoleMapper
{
    /**
     * 新增
     *
     * @param sysUserRole 实体对象
     * @return 影响行数
     */
    public int insert(SysUserRole sysUserRole);
    /**
     * 批量添加
     *
     * @param sysUserRoles 用户岗位角色信息
     * @return 影响行数
     */
    public int batch(List<SysUserRole> sysUserRoles);
    /**
     * 删除
     * 
     * @param sysUserRole 删除条件
     * @return 影响行数
     */
    public int delete(SysUserRole sysUserRole);
    public int deleteByUserIds(@Param("userIds") String[] userIds);
    /**
     * 查询集合
     * 
     * @param sysUserRole 查询条件
     * @return 集合
     */
    public List<SysUserRole> findList(SysUserRole sysUserRole);

}
