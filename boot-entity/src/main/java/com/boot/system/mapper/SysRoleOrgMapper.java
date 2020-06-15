package com.boot.system.mapper;

import java.util.List;

import com.boot.system.domain.SysRoleOrg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 角色与部门关联表 数据层
 * 
 * @author epl
 */
@Component
@Mapper
public interface SysRoleOrgMapper
{
    /**
     * 添加
     * 
     * @param sysRoleOrg 机构岗位信息
     * @return 影响行数
     */
    public int insert(SysRoleOrg sysRoleOrg);
    /**
     * 批量添加
     *
     * @param sysRoleOrgs 机构岗位信息
     * @return 影响行数
     */
    public int batch(List<SysRoleOrg> sysRoleOrgs);

    /**
     * 删除
     * 
     * @param sysRoleOrg 删除条件
     * @return 影响行数
     */
    public int delete(SysRoleOrg sysRoleOrg);

    /**
     * 查询集合
     * 
     * @param sysRoleOrg 查询条件
     * @return 集合
     */
    public List<SysRoleOrg> findList(SysRoleOrg sysRoleOrg);

}
