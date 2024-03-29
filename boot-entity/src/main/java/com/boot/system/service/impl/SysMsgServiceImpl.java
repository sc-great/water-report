package com.boot.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.Common;
import com.boot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.system.mapper.SysMsgMapper;
import com.boot.system.domain.SysMsg;
import com.boot.system.service.ISysMsgService;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户消息Service业务层处理
 *
 * @author boot
 * @date 2019-12-27
 */
@Service
public class SysMsgServiceImpl implements ISysMsgService {
    @Autowired
    private SysMsgMapper sysMsgMapper;

    /**
     * 新增用户消息
     *
     * @param sysMsg 用户消息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(SysMsg sysMsg) {
        sysMsg.setCreateTime(DateUtils.getTime());
        return sysMsgMapper.insert(sysMsg);
    }

    /**
     * 修改用户消息
     *
     * @param sysMsg 用户消息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysMsg sysMsg) {
        sysMsg.setUpdateTime(DateUtils.getTime());
        return sysMsgMapper.update(sysMsg);
    }

    /**
     * 删除用户消息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(String ids) {
        int result = 0;
        result = sysMsgMapper.delete(Convert.toStrArray(ids));
        return result;
    }
    /**
     * 删除用户消息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRead(String ids) {
        int result = 0;
        result = sysMsgMapper.updateRead(Convert.toStrArray(ids));
        return result;
    }

    /**
     * 查询用户消息数量
     *
     * @param sysMsg 查询条件
     * @return 用户消息数量
     */
    @Override
    public int getCount(SysMsg sysMsg) {
        return sysMsgMapper.getCount(sysMsg);
    }

    /**
     * 获取用户消息实体对象
     *
     * @param id 用户消息ID
     * @return 用户消息
     */
    @Override
    public SysMsg getEntityById(String id) {
        SysMsg sysMsg = new SysMsg();
        sysMsg.setId(id);
        return sysMsgMapper.getEntity(sysMsg);
    }

    /**
     * 查询用户消息列表
     *
     * @param sysMsg 用户消息
     * @return 用户消息
     */
    @Override
    public List<SysMsg> getList(SysMsg sysMsg) {
        return sysMsgMapper.getList(sysMsg);
    }
}