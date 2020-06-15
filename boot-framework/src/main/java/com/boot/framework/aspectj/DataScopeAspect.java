package com.boot.framework.aspectj;

import java.lang.reflect.Method;
import java.util.List;

import com.boot.system.domain.SysOrg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.boot.common.annotation.DataScope;
import com.boot.common.core.domain.BaseEntity;
import com.boot.common.utils.StringUtils;
import com.boot.framework.util.ShiroUtils;
import com.boot.system.domain.SysRole;
import com.boot.system.domain.SysUser;

/**
 * 数据过滤处理
 * 
 * @author epl
 */
@Aspect
@Component
public class DataScopeAspect
{

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.boot.common.annotation.DataScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
        // 获取当前的用户
        SysUser currentUser = ShiroUtils.getSysUser();
        if (currentUser != null)
        {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin())
            {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.orgAlias(),
                        controllerDataScope.userAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     * 
     * @param joinPoint 切点
     * @param user 用户
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String orgAlias, String userAlias)
    {
        StringBuilder sqlString = new StringBuilder();
        if(user.getSysRoles()!=null){
            for (SysRole role : user.getSysRoles())
            {
                String dataScope = role.getDataScope();
                //全部数据权限
                if (DATA_SCOPE_ALL.equals(dataScope))
                {
                    sqlString = new StringBuilder();
                    break;
                }
                //自定数据权限
                else if (DATA_SCOPE_CUSTOM.equals(dataScope))
                {
                    sqlString.append(StringUtils.format(" OR {}.org_id IN ( SELECT org_id FROM sys_role_org_scope WHERE role_id = '{}' ) ", orgAlias,role.getRoleId()));
                }
                //部门数据权限
                else if (DATA_SCOPE_DEPT.equals(dataScope))
                {
                    List<SysOrg> sysOrgList=user.getSysOrgs();
                    for (SysOrg sysOrg:sysOrgList){
                        sqlString.append(StringUtils.format(" OR {}.org_id = '{}' ", orgAlias, sysOrg.getOrgId()));
                    }
                }
                //部门及以下数据权限
                else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope))
                {
                    List<SysOrg> sysOrgList=user.getSysOrgs();
                    for (SysOrg sysOrg:sysOrgList) {
                        sqlString.append(StringUtils.format(" OR {}.org_id IN ( SELECT org_id FROM sys_org WHERE FIND_IN_SET( '{}' ,REPLACE (org_id_path,'/',',')))",orgAlias, sysOrg.getOrgId()));
                    }
                }
                //仅本人数据权限
                else if (DATA_SCOPE_SELF.equals(dataScope))
                {
                    if (StringUtils.isNotBlank(userAlias))
                    {
                        sqlString.append(StringUtils.format(" OR {}.user_id = '{}' ", userAlias, user.getUserId()));
                    }
                    else
                    {
                        // 数据权限为仅本人且没有userAlias别名不查询任何数据
                        sqlString.append(" OR 1=0 ");
                    }
                }
            }
        }


        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            String where=String.format(" AND (%s)", sqlString.substring(4));
            baseEntity.getParams().put(DATA_SCOPE,where);
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
