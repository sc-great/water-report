package com.boot.system.service;

import com.boot.system.domain.SysMsg;
import java.util.List;
import java.util.Map;

/**
 * 用户消息Service接口
 * 
 * @author boot
 * @date 2019-12-27
 */
public interface ISysMsgService {
    /**
     * 新增用户消息
     *
     * @param sysMsg 用户消息
     * @return 结果
     */
    public int add(SysMsg sysMsg);
    /**
     * 修改用户消息
     *
     * @param sysMsg 用户消息
     * @return 结果
     */
    public int update(SysMsg sysMsg);
    /**
     * 批量删除用户消息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByIds(String ids);
    /**
     * 修改状态
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int updateRead(String ids);
    /**
     * 查询用户消息数量
     *
     * @param sysMsg 查询条件
     * @return 用户消息数量
     */
    public int getCount(SysMsg sysMsg);
    /**
     * 获取用户消息实体对象
     * 
     * @param id 用户消息ID
     * @return 用户消息
     */
    public SysMsg getEntityById(String id);
    /**
     * 查询用户消息列表
     * 
     * @param sysMsg 用户消息
     * @return 用户消息集合
     */
    public List<SysMsg> getList(SysMsg sysMsg);
}