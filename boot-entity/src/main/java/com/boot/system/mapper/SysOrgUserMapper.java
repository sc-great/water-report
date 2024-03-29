package com.boot.system.mapper;

import com.boot.system.domain.SysOrgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门信息表
 * */
@Component
@Mapper
public interface SysOrgUserMapper {
    /**
     * 新增
     *
     * @param sysOrgUser 机构用户信息
     * @return 影响行数
     */
    public int insert(SysOrgUser sysOrgUser);
    /**
     * 批量添加
     *
     * @param sysOrgUsers 用户组织机构信息
     * @return 影响行数
     */
    public int batch(List<SysOrgUser> sysOrgUsers);
    /**
     * 删除
     *
     * @param sysOrgUser 条件
     * @return 结果
     */
    public int delete(SysOrgUser sysOrgUser);
    public int deleteByUserIds(@Param("userIds") String[] userIds);

    /**
     * 查询对象集合
     *
     * @param sysOrgUser 条件
     * @return 集合
     */
    public List<SysOrgUser> findList(SysOrgUser sysOrgUser);
}
