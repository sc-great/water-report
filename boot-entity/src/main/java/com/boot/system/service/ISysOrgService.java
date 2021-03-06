package com.boot.system.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysRoleOrgScope;

/**
 * 部门管理 服务层
 * 
 * @author epl
 */
public interface ISysOrgService {
	/**
	 * 新增
	 *
	 * @param sysOrg 机构信息
	 * @return 影响行数
	 */
	public int insert(SysOrg sysOrg);

	/**
	 * 删除
	 *
	 * @param sysOrg 删除条件
	 * @return 影响行数
	 */
	public int delete(SysOrg sysOrg);

	/**
	 * 根据ID删除
	 *
	 * @param orgId 删除机构ID
	 * @return 影响行数
	 */
	public int deleteById(String orgId);

	/**
	 * 修改
	 *
	 * @param sysOrg 修改信息
	 * @return 影响行数
	 */
	public int update(SysOrg sysOrg);

	/**
	 * 查询数量
	 *
	 * @param sysOrg 查询条件
	 * @return 数量
	 */
	public int getCount(SysOrg sysOrg);

	/**
	 * 查询实体对象
	 *
	 * @param id 查询ID
	 * @return 实体对象
	 */
	public SysOrg getById(String id);

	/**
	 * 查询实体对象
	 *
	 * @param sysOrg 查询条件
	 * @return 实体对象
	 */
	public SysOrg getEntity(SysOrg sysOrg);

	/**
	 * 查询集合
	 * 
	 * @param sysOrg 查询条件
	 * @return 集合
	 */
	public List<SysOrg> findList(SysOrg sysOrg);

	public List<SysOrg> findListNoDataScope(SysOrg sysOrg);

	public JSONArray treeList(SysOrg sysOrg);

	public JSONArray treeListArea(SysOrg sysOrg);

	/**
	 * 查询组织机构
	 *
	 * @param orgIds 条件
	 * @return 集合
	 */
	public List<SysOrg> findListByIds(String[] orgIds);

	/**
	 * 获取岗位角色部门树
	 * 
	 * @param roleId 岗位角色ID
	 * @return 树集合
	 */
	public List<SysRoleOrgScope> getOrgScopeByRoleId(String roleId);

	/**
	 * 查询用户所属机构集合
	 *
	 * @param userId 用户ID
	 * @return 机构集合
	 */
	public List<SysOrg> getOrgListByUserId(String userId);

	/**
	 * 查询角色所属机构集合
	 *
	 * @param roleId 角色ID
	 * @return 机构集合
	 */
	public List<SysOrg> getOrgListByRoleId(String roleId);

	/**
	 * 区域下的水厂机构
	 * 
	 * @param sysOrg
	 * @return
	 */
	public List<SysOrg> findListByFactory(SysOrg sysOrg);
}
