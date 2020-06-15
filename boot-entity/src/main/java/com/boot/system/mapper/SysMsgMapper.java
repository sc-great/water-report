package com.boot.system.mapper;

import com.boot.system.domain.SysMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户消息Mapper接口
 *
 * @author boot
 * @date 2019-12-27
 */
@Component
@Mapper
public interface SysMsgMapper {
    /**
     * 新增用户消息
     *
     * @param sysMsg 用户消息
     * @return 结果
     */
    public int insert(SysMsg sysMsg);

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
     * @param ids 删除条件
     * @return 结果
     */
    public int delete(@Param("ids") String[] ids);

    /**
     * 修改状态
     *
     * @param ids 删除条件
     * @return 结果
     */
    public int updateRead(@Param("ids") String[] ids);

    /**
     * 获取用户消息数量
     *
     * @param sysMsg 查询条件
     * @return 结果
     */
    public int getCount(SysMsg sysMsg);

    /**
     * 获取用户消息对象
     *
     * @param sysMsg 查询条件
     * @return 用户消息
     */
    public SysMsg getEntity(SysMsg sysMsg);

    /**
     * 查询用户消息列表
     *
     * @param sysMsg 查询条件
     * @return 用户消息集合
     */
    public List<SysMsg> getList(SysMsg sysMsg);
}