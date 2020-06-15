package com.boot.system.service.impl;

import java.util.*;

import com.boot.common.annotation.DataScope;
import com.boot.system.domain.*;
import com.boot.system.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.common.core.text.Convert;
import com.boot.system.service.ISysRoleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色 业务层处理
 * 
 * @author epl
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysRoleOrgMapper sysRoleOrgMapper;
    @Autowired
    private SysRoleOrgScopeMapper sysRoleOrgScopeMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    /**
     * 添加部门岗位角色
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    @Override
    public int insert(SysRole sysRole){
        int result=sysRoleMapper.insert(sysRole);
//        if(result>0){
//            addOrgs(sysRole);
//        }
        return result;
    }
    /**
     * 删除
     *
     * @param sysRole 删除条件
     * @return 影响行数
     */
    @Override
    public int delete(SysRole sysRole){
        int result=sysRoleMapper.delete(sysRole);
        if(result>0){
            //删除组织机构权限
            SysRoleOrgScope sysRoleOrgScope=new SysRoleOrgScope();
            sysRoleOrgScope.setRoleId(sysRole.getRoleId());
            sysRoleOrgScopeMapper.delete(sysRoleOrgScope);
            //删除组织机构岗位角色
//            SysRoleOrg sysRoleOrg=new SysRoleOrg();
//            sysRoleOrg.setRoleId(sysRole.getRoleId());
//            sysRoleOrgMapper.delete(sysRoleOrg);
            //删除菜单
            sysRoleMenuMapper.deleteRoleMenuByRoleId(sysRole.getRoleId());
        }
        return result;
    }
    /**
     * 删除
     *
     * @param id 删除条件
     * @return 影响行数
     */
    @Override
    public int deleteById(String id){
        SysRole sysRole=new SysRole();
        sysRole.setRoleId(id);
        return delete(sysRole);
    }
    /**
     * 修改
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    @Override
    public int update(SysRole sysRole){
        int result=sysRoleMapper.update(sysRole);
        return result;
    }
    /**
     * 修改
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    @Override
    public int updateAndOrgs(SysRole sysRole){
        int result=sysRoleMapper.update(sysRole);
//        if(result>0){
//            addOrgs(sysRole);
//        }
        return result;
    }

    /**
     * 添加角色所属机构
     * */
    private void addOrgs(SysRole sysRole){
        if(sysRole!=null){
            Map<String,Object> params=sysRole.getParams();
            //删除角色部门
            SysRoleOrg sysRoleOrg_params=new SysRoleOrg();
            sysRoleOrg_params.setRoleId(sysRole.getRoleId());
            sysRoleOrgMapper.delete(sysRoleOrg_params);

            String[] orgIds=null;
            if(params.containsKey("orgIds") && !"".equals(params.get("orgIds"))){
                orgIds=params.get("orgIds").toString().split(",");
            }
            if(orgIds!=null && orgIds.length>0){
                List<SysRoleOrg> sysRoleOrgs=new ArrayList<>();
                for(String orgId:orgIds){
                    SysRoleOrg sysRoleOrg=new SysRoleOrg();
                    sysRoleOrg.setOrgId(orgId);
                    sysRoleOrg.setRoleId(sysRole.getRoleId());
                    sysRoleOrgs.add(sysRoleOrg);
                }
                if(sysRoleOrgs.size()>0){
                    sysRoleOrgMapper.batch(sysRoleOrgs);
                }
            }
        }
    }
    /**
     * 修改菜单权限
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    @Override
    public int menusScope(SysRole sysRole){
        return addMenus(sysRole);
    }
    /**
     * 添加角色岗位菜单权限
     * */
    private int addMenus(SysRole sysRole){
        int result = 0;
        if(sysRole!=null){
            Map<String,Object> params=sysRole.getParams();
            //删除角色菜单
            sysRoleMenuMapper.deleteRoleMenuByRoleId(sysRole.getRoleId());
            //添加角色菜单权限
            String[] menuIds=null;
            if(params.containsKey("menuIds") && !"".equals(params.get("menuIds"))){
                menuIds=params.get("menuIds").toString().split(",");
            }
            if(menuIds!=null && menuIds.length>0){
                List<SysRoleMenu> sysRoleMenus=new ArrayList<>();
                for (String menuId:menuIds) {
                    SysRoleMenu sysRoleMenu=new SysRoleMenu();
                    sysRoleMenu.setRoleId(sysRole.getRoleId());
                    sysRoleMenu.setMenuId(Convert.toLong(menuId));
                    sysRoleMenus.add(sysRoleMenu);
                }
                if(sysRoleMenus.size()>0){
                    result=sysRoleMenuMapper.batchRoleMenu(sysRoleMenus);
                }
            }else {
                result = 1;
            }
        }
        return result;
    }
    /**
     * 修改数据权限
     *
     * @param sysRole 角色信息
     * @return 影响行数
     */
    @Override
    public int authDataScope(SysRole sysRole){
        int result=sysRoleMapper.update(sysRole);
        if(result>0){
            addScopeOrg(sysRole);
        }
        return result;
    }
    /**
     * 添加数据权限
     * */
    private void addScopeOrg(SysRole sysRole){
        if(sysRole!=null){
            Map<String,Object> params=sysRole.getParams();
            //删除角色部门权限组织机构
            SysRoleOrgScope sysRoleOrgScope_params=new SysRoleOrgScope();
            sysRoleOrgScope_params.setRoleId(sysRole.getRoleId());
            sysRoleOrgScopeMapper.delete(sysRoleOrgScope_params);

            String[] scopeOrgIds=null;
            if(params.containsKey("scopeOrgIds") && !"".equals(params.get("scopeOrgIds"))){
                scopeOrgIds=params.get("scopeOrgIds").toString().split(",");
            }
            if(scopeOrgIds!=null && scopeOrgIds.length>0){
                List<SysRoleOrgScope> sysRoleOrgScopes=new ArrayList<>();
                for(String orgId:scopeOrgIds){
                    SysRoleOrgScope sysRoleOrgScope=new SysRoleOrgScope();
                    sysRoleOrgScope.setOrgId(orgId);
                    sysRoleOrgScope.setRoleId(sysRole.getRoleId());
                    sysRoleOrgScopes.add(sysRoleOrgScope);
                }
                if(sysRoleOrgScopes.size()>0){
                    sysRoleOrgScopeMapper.batch(sysRoleOrgScopes);
                }
            }
        }
    }

    /**
     * 查询实体对象
     *
     * @param roleId 查询条件
     * @return 实体对象
     */
    @Override
    public SysRole getById(String roleId){
        SysRole sysRole=new SysRole();
        sysRole.setRoleId(roleId);
        return sysRoleMapper.getEntity(sysRole);
    }
    /**
     * 查询实体对象
     *
     * @param sysRole 查询条件
     * @return 实体对象
     */
    @Override
    public SysRole getEntity(SysRole sysRole){
        return sysRoleMapper.getEntity(sysRole);
    }
    /**
     * 查询数量
     *
     * @param sysRole 查询条件
     * @return 数量
     */
    @Override
    @DataScope(orgAlias = "t",userAlias = "t")
    public int getCount(SysRole sysRole){
        return sysRoleMapper.getCount(sysRole);
    }
    /**
     * 查询集合
     *
     * @param sysRole 查询条件
     * @return 集合
     */
    @Override
    @DataScope(orgAlias = "t",userAlias = "t")
    public List<SysRole> findList(SysRole sysRole){
        return sysRoleMapper.findList(sysRole);
    }
    /**
     * 查询用户的角色集合
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    @Override
    public List<SysRole> getRoleListByUserId(String userId){
        return sysRoleMapper.getRoleListByUserId(userId);
    }

    @Override
    public List<SysUserRole> getUserRoleList(String userId){
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(userId);
        return sysUserRoleMapper.findList(sysUserRole);
    }
    /**
     * 查询机构下的角色集合
     *
     * @param orgId 机构ID
     * @return 角色集合
     */
    @Override
    public List<SysRole> getRoleListByOrgId(String orgId){
        return sysRoleMapper.getRoleListByOrgId(orgId);
    }


    /**
     * 导入人力资源岗位信息
     * */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int importHRRoleData(SysRole sysRole){
        int result=0;
        SysRole params=new SysRole();
        params.setRoleId(sysRole.getRoleId());
        int count=sysRoleMapper.getCount(params);
        if(count>0){
            result=sysRoleMapper.update(sysRole);
        }else{
            result=sysRoleMapper.insert(sysRole);
        }
        if(result>0){
            addOrgs(sysRole);
        }
        return result;
    }
}
