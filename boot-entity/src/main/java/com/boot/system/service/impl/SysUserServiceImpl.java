package com.boot.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.boot.common.annotation.DataScope;
import com.boot.common.constant.UserConstants;
import com.boot.common.core.domain.Ztree;
import com.boot.common.core.text.Convert;
import com.boot.common.utils.StringUtils;
import com.boot.system.domain.SysOrg;
import com.boot.system.domain.SysOrgUser;
import com.boot.system.domain.SysUserRole;
import com.boot.system.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.system.domain.SysUser;
import com.boot.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 业务层处理
 * 
 * @author epl
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysOrgUserMapper sysOrgUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysOrgMapper sysOrgMapper;

    /**
     * 添加
     *
     * @param sysUser 用户信息
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int insert(SysUser sysUser){
        int result=userMapper.insert(sysUser);
        if(result>0){
            addOrgAndRole(sysUser);
        }
        return result;
    }

    /**
     * 修改
     *
     * @param sysUser 用户信息
     * @return 影响行数
     */
    @Override
    public int update(SysUser sysUser){
        return userMapper.update(sysUser);
    }

    /**
     * 修改用户信息和组织机构以及岗位角色
     *
     * @param sysUser 用户信息
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int updateOrgAndRole(SysUser sysUser){
        int result=userMapper.update(sysUser);
        if(result>0){
            addOrgAndRole(sysUser);
        }
        return result;
    }

    /**
     * 添加用户组织机构和岗位角色
     * */
    private void addOrgAndRole(SysUser sysUser){
        if(sysUser!=null){
            Map<String,Object> params=sysUser.getParams();
            //删除用户组织机构
            SysOrgUser sysOrgUser_params=new SysOrgUser();
            sysOrgUser_params.setUserId(sysUser.getUserId());
            sysOrgUserMapper.delete(sysOrgUser_params);

            String[] orgIds=null;
            if(params.containsKey("orgIds") && !"".equals(params.get("orgIds"))){
                orgIds=params.get("orgIds").toString().split(",");
            }
            if(orgIds!=null && orgIds.length>0){
                List<SysOrgUser> sysOrgUsers=new ArrayList<>();
                for(String orgId:orgIds){
                    SysOrgUser sysOrgUser=new SysOrgUser();
                    sysOrgUser.setOrgId(orgId);
                    sysOrgUser.setUserId(sysUser.getUserId());
                    sysOrgUsers.add(sysOrgUser);
                }
                if(sysOrgUsers.size()>0){
                    sysOrgUserMapper.batch(sysOrgUsers);
                }
            }

            //删除用户岗位角色
            SysUserRole sysUserRole_params=new SysUserRole();
            sysUserRole_params.setUserId(sysUser.getUserId());
            sysUserRoleMapper.delete(sysUserRole_params);

            String[] roleIds=null;
            if(params.containsKey("roleIds") && !"".equals(params.get("roleIds"))){
                roleIds=params.get("roleIds").toString().split(",");
            }
            if(roleIds!=null && roleIds.length>0){
                List<SysUserRole> sysUserRoles=new ArrayList<>();
                for(String orgId_roleId_str:roleIds){
                    String[] orgId_roleId=orgId_roleId_str.split("/");
                    SysUserRole sysUserRole=new SysUserRole();
                    sysUserRole.setUserId(sysUser.getUserId());
//                    sysUserRole.setOrgId(orgId_roleId[0]);
                    sysUserRole.setRoleId(orgId_roleId[0]);
                    sysUserRoles.add(sysUserRole);
                }
                if(sysUserRoles.size()>0){
                    sysUserRoleMapper.batch(sysUserRoles);
                }
            }
        }
    }

    /**
     * 查询数量
     *
     * @param sysUser 查询条件
     * @return 数量
     */
    @Override
    @DataScope(orgAlias = "t",userAlias = "t")
    public int getCount(SysUser sysUser){
        return userMapper.getCount(sysUser);
    }

    /**
     * 登录
     *
     * @param loginName 登录帐号
     * @return 实体对象
     */
    @Override
    public SysUser loginName(String loginName){
        SysUser sysUser=new SysUser();
        sysUser.setLoginName(loginName);
        return userMapper.getEntity(sysUser);
    }

    /**
     * 查询实体对象
     *
     * @param userId 查询ID
     * @return 实体对象
     */
    @Override
    public SysUser getById(String userId){
        SysUser sysUser=new SysUser();
        sysUser.setUserId(userId);
        return userMapper.getEntity(sysUser);
    }

    /**
     * 查询实体对象
     *
     * @param sysUser 查询条件
     * @return 实体对象
     */
    @Override
    public SysUser getEntity(SysUser sysUser){
        return userMapper.getEntity(sysUser);
    }

    /**
     * 查询
     *
     * @param sysUser 查询条件
     * @return 集合
     */
    @Override
    @DataScope(orgAlias = "t",userAlias = "t")
    public List<SysUser> findList(SysUser sysUser){
        return userMapper.findList(sysUser);
    }

    /**
     * 查询
     *
     * @param userIds 查询条件
     * @return 集合
     */
    @Override
    public List<SysUser> findListByIds(String userIds){
        return userMapper.findListByIds(Convert.toStrArray(userIds));
    }

    /**
     * 删除
     *
     * @param sysUser 删除条件
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int delete(SysUser sysUser){
        int result=userMapper.delete(sysUser);
        if(result>0){
            //删除用户组织机构
            SysOrgUser sysOrgUser=new SysOrgUser();
            sysOrgUser.setUserId(sysUser.getUserId());
            sysOrgUserMapper.delete(sysOrgUser);
            //删除用户岗位角色
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setUserId(sysUser.getUserId());
            sysUserRoleMapper.delete(sysUserRole);
        }
        return result;
    }

    /**
     * 批量删除
     *
     * @param userIds 删除条件
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(String userIds){
        String[] array = Convert.toStrArray(userIds);
        int result=userMapper.deleteByIds(array);
        if(result>0){
            //删除用户组织机构
            sysOrgUserMapper.deleteByUserIds(array);
            //删除用户岗位角色
            sysUserRoleMapper.deleteByUserIds(array);
        }
        return result;
    }

    /**
     * 查询机构下的用户集合
     *
     * @param orgId 机构ID
     * @return 集合
     */
    @Override
    public List<SysUser> getUserListByOrgId(String orgId){
        return  userMapper.getUserListByOrgId(orgId);
    }

    /**
     * 查询角色下的用户集合
     *
     * @param roleId 角色ID
     * @return 用户集合
     */
    @Override
    public List<SysUser> getUserListByRoleId(String roleId){
        return userMapper.getUserListByRoleId(roleId);
    }

    /**
     * 导入人力资源用户信息
     * */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int importHRUserData(SysUser sysUser){
        int result;
        SysUser params=new SysUser();
        params.setUserId(sysUser.getUserId());
        int count=userMapper.getCount(params);
        if(count>0){
            result=updateOrgAndRole(sysUser);
        }else{
            result=insert(sysUser);
        }
        return result;
    }

    /**
     * 根据条件查询部门用户树形列表
     *
     * @param params 参数集合
     * @return 部门用户树形列表
     */
    @Override
    public List<Ztree> deptUserTreeData(Map<String, Object> params) {
        List<Ztree> zTrees = new ArrayList<>();
        //用户数据(启用)
        List<SysUser> sysUserList = userMapper.getOrgUserList();
        //判断是否有部门信息
        if (StringUtils.isNotEmpty(sysUserList)) {
            //判断是否多选并且回显选中
            Object selValuesObj = params.get("selValues");
            Object selPValuesObj = params.get("selPValues");
            boolean isSel = false;
            ArrayList<String> selUserIdsList = new ArrayList<>();
            ArrayList<String> selOrgIdsList = new ArrayList<>();
            if (StringUtils.isNotNullAndNotEmpty(selValuesObj) && StringUtils.isNotNullAndNotEmpty(selPValuesObj) && ("checkbox").equals(params.get("checkType"))) {
                isSel = true;
                selUserIdsList = new ArrayList<>(Arrays.asList(selValuesObj.toString().split(",")));
                selOrgIdsList = new ArrayList<>(Arrays.asList(selPValuesObj.toString().split(",")));
            }
            //获取包含用户数据的部门及其父类
            ArrayList<String> pOrgIdsList = new ArrayList<>();
            for (SysUser sysUser : sysUserList) {
                List<SysOrg> sysOrgList = sysUser.getSysOrgs();
                for (SysOrg sysOrg : sysOrgList) {
                    //筛选用户所属部门ID及所有父类ID
                    String[] curOrgIdsArr = sysOrg.getOrgIdPath().replaceAll("/", ",").split(",");
                    for (String orgId : curOrgIdsArr) {
                        if (!pOrgIdsList.contains(orgId)) {
                            pOrgIdsList.add(orgId);
                        }
                    }
                    //封装为用户树形对象集合
                    Ztree ztree = new Ztree();
                    ztree.setId(sysUser.getUserId());
                    ztree.setpId(sysOrg.getOrgId());
                    ztree.setName(sysUser.getUserName());
                    ztree.setTitle(sysUser.getUserName());
                    //判断是否选中
                    if (isSel) {
                        if (selUserIdsList.contains(sysUser.getUserId()) && selOrgIdsList.contains(sysOrg.getOrgId())) {
                            ztree.setChecked(true);
                        }
                    }
                    zTrees.add(ztree);
                }
            }
            //判断是否为多选，若是则获取所有需选中的父部门ID
            ArrayList<String> selPIdsList = new ArrayList<>();
            if (isSel) {
                List<SysOrg> selOrgList = sysOrgMapper.findListByIds(selPValuesObj.toString().split(","));
                for (SysOrg sysOrg : selOrgList) {
                    String[] selOrgIdsArr = sysOrg.getOrgIdPath().replaceAll("/", ",").split(",");
                    for (String selOrgId : selOrgIdsArr) {
                        if (!selPIdsList.contains(selOrgId)) {
                            selPIdsList.add(selOrgId);
                        }
                    }
                }
            }
            //根据用户所属部门IDS获取所有部门对象集合
            List<SysOrg> orgList = sysOrgMapper.findListByIds(pOrgIdsList.toArray(new String[0]));
            if (StringUtils.isNotEmpty(orgList)) {
                //封装为部门树形对象集合
                for (SysOrg org : orgList) {
                    if (("1").equals(org.getValidStatus())) {
                        Ztree ztree = new Ztree();
                        ztree.setId(org.getOrgId());
                        ztree.setpId(org.getParentId());
                        ztree.setName(org.getOrgName());
                        ztree.setTitle(org.getOrgName());
                        if (isSel) {
                            if (selPIdsList.contains(org.getOrgId())) {
                                ztree.setChecked(true);
                            }
                        }
                        zTrees.add(ztree);
                    }
                }
            }
        }
        return zTrees;
    }
}