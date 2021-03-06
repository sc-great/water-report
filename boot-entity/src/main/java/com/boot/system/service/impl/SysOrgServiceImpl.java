package com.boot.system.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boot.system.domain.SysRoleOrgScope;
import com.boot.system.mapper.SysRoleOrgScopeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.boot.common.annotation.DataScope;
import com.boot.system.domain.Area;
import com.boot.system.domain.SysOrg;
import com.boot.system.mapper.AreaMapper;
import com.boot.system.mapper.SysOrgMapper;
import com.boot.system.service.ISysOrgService;

/**
 * 组织机构管理 服务实现
 * 
 * @author epl
 */
@Service
public class SysOrgServiceImpl implements ISysOrgService {
	@Autowired
	private SysOrgMapper sysOrgMapper;
	@Autowired
	private SysRoleOrgScopeMapper sysRoleOrgScopeMapper;
	@Autowired
	private AreaMapper areaMapper;
	/**
	 * 新增
	 *
	 * @param sysOrg 机构信息
	 * @return 影响行数
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insert(SysOrg sysOrg) {
		int result = sysOrgMapper.insert(sysOrg);
		if (result > 0) {
			update(sysOrg);
		}
		return result;
	}

	/**
	 * 修改
	 *
	 * @param sysOrg 机构信息
	 * @return 影响行数
	 */
	@Override
	public int update(SysOrg sysOrg) {
		setFullPath(sysOrg);
		int result = sysOrgMapper.update(sysOrg);
		if (result > 0) {
			updateSubset(sysOrg.getOrgId());
		}
		return result;
	}

	/**
	 * 设置全路径
	 */
	private void setFullPath(SysOrg sysOrg) {
		Map<String, Object> map = getFullPath(sysOrg.getParentId());
		String org_id_path = map.containsKey("org_id_path") ? map.get("org_id_path") + "/" + sysOrg.getOrgId()
				: sysOrg.getOrgId();
		String org_code_path = map.containsKey("org_code_path") ? map.get("org_code_path") + "/" + sysOrg.getOrgCode()
				: sysOrg.getOrgCode();
		String org_name_path = map.containsKey("org_name_path") ? map.get("org_name_path") + "/" + sysOrg.getOrgName()
				: sysOrg.getOrgName();
		sysOrg.setOrgIdPath(org_id_path);
		sysOrg.setOrgCodePath(org_code_path);
		sysOrg.setOrgNamePath(org_name_path);
	}

	/**
	 * 获取机构全路径
	 */
	private Map<String, Object> getFullPath(String orgId) {
		Map<String, Object> map = new HashMap<>();
		SysOrg sysOrg = getById(orgId);
		if (sysOrg != null) {
			if (!"0".equals(sysOrg.getParentId())) {
				map = getFullPath(sysOrg.getParentId());
			}
			map.put("org_id_path", "0".equals(sysOrg.getParentId()) ? sysOrg.getOrgId()
					: map.get("org_id_path") + "/" + sysOrg.getOrgId());
			map.put("org_code_path", "0".equals(sysOrg.getParentId()) ? sysOrg.getOrgCode()
					: map.get("org_code_path") + "/" + sysOrg.getOrgCode());
			map.put("org_name_path", "0".equals(sysOrg.getParentId()) ? sysOrg.getOrgName()
					: map.get("org_name_path") + "/" + sysOrg.getOrgName());
		}
		return map;
	}

	/**
	 * 修改子集的全路径
	 */
	private void updateSubset(String parentId) {
		SysOrg sysOrg_params = new SysOrg();
		sysOrg_params.setParentId(parentId);
		List<SysOrg> sysOrgs = sysOrgMapper.findList(sysOrg_params);
		if (sysOrgs.size() > 0) {
			for (SysOrg sysOrg : sysOrgs) {
				if (update(sysOrg) > 0) {
					updateSubset(sysOrg.getOrgId());
				}
			}
		}
	}

	/**
	 * 删除
	 *
	 * @param sysOrg 删除条件
	 * @return 影响行数
	 */
	@Override
	public int delete(SysOrg sysOrg) {
		return sysOrgMapper.delete(sysOrg);
	}

	/**
	 * 根据ID删除
	 *
	 * @param orgId 删除机构ID
	 * @return 影响行数
	 */
	@Override
	public int deleteById(String orgId) {
		SysOrg sysOrg = new SysOrg();
		sysOrg.setOrgId(orgId);
		return sysOrgMapper.delete(sysOrg);
	}

	/**
	 * 查询数量
	 *
	 * @param sysOrg 查询条件
	 * @return 数量
	 */
	@Override
	@DataScope(orgAlias = "t", userAlias = "t")
	public int getCount(SysOrg sysOrg) {
		return sysOrgMapper.getCount(sysOrg);
	}

	/**
	 * 查询实体对象
	 *
	 * @param orgId 查询ID
	 * @return 实体对象
	 */
	@Override
	public SysOrg getById(String orgId) {
		SysOrg sysOrg = new SysOrg();
		sysOrg.setOrgId(orgId);
		return sysOrgMapper.getEntity(sysOrg);
	}

	/**
	 * 查询实体对象
	 *
	 * @param sysOrg 查询条件
	 * @return 实体对象
	 */
	@Override
	public SysOrg getEntity(SysOrg sysOrg) {
		return sysOrgMapper.getEntity(sysOrg);
	}

	/**
	 * 查询集合
	 * 
	 * @param sysOrg 查询条件
	 * @return 集合
	 */
	@Override
	@DataScope(orgAlias = "t", userAlias = "t")
	public List<SysOrg> findList(SysOrg sysOrg) {
		return sysOrgMapper.findList(sysOrg);
	}

	/**
	 * 查询集合
	 *
	 * @param sysOrg 查询条件
	 * @return 集合
	 */
	@Override
	public List<SysOrg> findListNoDataScope(SysOrg sysOrg) {
		return sysOrgMapper.findList(sysOrg);
	}

	@Override
	public JSONArray treeList(SysOrg sysOrg) {
		JSONArray jsonArray = new JSONArray();
		List<SysOrg> list = findList(sysOrg);
		for (SysOrg item : list) {
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(item);
			sysOrg = new SysOrg();
			sysOrg.setParentId(item.getOrgId());
			int child_count = getCount(sysOrg);
			jsonObject.put("childCount", child_count);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	@Override
	public JSONArray treeListArea(SysOrg sysOrg) {
		JSONArray jsonArray = new JSONArray();
		if (sysOrg.getParentId().indexOf("MARK") != -1) {
			String id = sysOrg.getParentId();
			id = id.replace("MARK", "");
			SysOrg org = getById(id);
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(org);
			sysOrg = new SysOrg();
			sysOrg.setAreaId(org.getAreaId());
			jsonObject.put("childCount", getCount(sysOrg));
			jsonArray.add(jsonObject);
			return jsonArray;
		}
		if ("0".equals(sysOrg.getParentId()) || "1".equals(sysOrg.getParentId())) {
			
		} else {
			SysOrg sysOrg_old = getById(sysOrg.getParentId());
			if (sysOrg_old != null) {
				if ("2".equals(sysOrg_old.getOrgType())) {
					Area area = new Area();
					Map<String, Object> params = new HashMap<>();
					params.put("areaIds", sysOrg_old.getAreaId());
					area.setParams(params);
					List<Area> areaList = areaMapper.getList(area);
					for (Area a : areaList) {
						SysOrg sysOrg_new = new SysOrg();
						sysOrg_new.setOrgId(a.getAreaId());
						sysOrg_new.setOrgName(a.getAreaName());
						sysOrg_new.setParentId(sysOrg_old.getOrgId());
						sysOrg_new.setValidStatus("1");
						sysOrg_new.setOrgType("2.5");
						JSONObject jsonObject = (JSONObject) JSONObject.toJSON(sysOrg_new);
						sysOrg = new SysOrg();
						sysOrg.setAreaId(a.getAreaId());
						jsonObject.put("childCount", getCount(sysOrg));
						jsonArray.add(jsonObject);
					}
				}
			} else {
				SysOrg sysOrg_param = new SysOrg();
				sysOrg_param.setAreaId(sysOrg.getParentId());
				sysOrg_param.setOrgType("3");
				List<SysOrg> list = findList(sysOrg_param);
				for (SysOrg s : list) {
					s.setParentId(sysOrg_param.getAreaId());
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(s);
					sysOrg = new SysOrg();
					sysOrg.setParentId(s.getOrgId());
					jsonObject.put("childCount", getCount(sysOrg));
					jsonArray.add(jsonObject);
				}
			}
			return jsonArray;
		}
		
		List<SysOrg> list = findList(sysOrg);
		for (SysOrg item : list) {
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(item);
			sysOrg = new SysOrg();
			sysOrg.setParentId(item.getOrgId());
			int child_count = getCount(sysOrg);
			jsonObject.put("childCount", child_count);
			jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}

	/**
	 * 查询组织机构
	 *
	 * @param orgIds 条件
	 * @return 集合
	 */
	public List<SysOrg> findListByIds(String[] orgIds) {
		return sysOrgMapper.findListByIds(orgIds);
	}

	/**
	 * 获取角色数据权限机构
	 *
	 * @param roleId 组角色ID
	 * @return 角色数据权限组织机构集合
	 */
	@Override
	public List<SysRoleOrgScope> getOrgScopeByRoleId(String roleId) {
		SysRoleOrgScope sysRoleOrgScope = new SysRoleOrgScope();
		sysRoleOrgScope.setRoleId(roleId);
		List<SysRoleOrgScope> sysRoleOrgScopes = sysRoleOrgScopeMapper.findList(sysRoleOrgScope);
		return sysRoleOrgScopes;
	}

	/**
	 * 查询用户所属机构集合
	 *
	 * @param userId 用户ID
	 * @return 机构集合
	 */
	@Override
	public List<SysOrg> getOrgListByUserId(String userId) {
		return sysOrgMapper.getOrgListByUserId(userId);
	}

	/**
	 * 查询角色所属机构集合
	 *
	 * @param roleId 角色ID
	 * @return 机构集合
	 */
	@Override
	public List<SysOrg> getOrgListByRoleId(String roleId) {
		return sysOrgMapper.getOrgListByRoleId(roleId);
	}

	/**
	 * 区域下的水厂机构
	 * 
	 * @param sysOrg
	 * @return
	 */
	public List<SysOrg> findListByFactory(SysOrg sysOrg) {
		return sysOrgMapper.findListByFactory(sysOrg);
	}
}
