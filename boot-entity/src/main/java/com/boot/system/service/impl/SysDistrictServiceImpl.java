package com.boot.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.system.mapper.SysDistrictMapper;
import com.boot.system.domain.SysDistrict;
import com.boot.system.service.ISysDistrictService;

import java.util.List;

/**
 * 省市县数据Service业务层处理
 *
 * @author EPL
 * @date 2019-09-11
 */
@Service
public class SysDistrictServiceImpl implements ISysDistrictService {
    @Autowired
    private SysDistrictMapper sysDistrictMapper;

    /**
     * 查询省市县数据
     *
     * @param pid 省市县数据PID
     * @return 省市县数据
     */
    @Override
    public List<SysDistrict> selectSysDistrictByPid(String pid) {
        return sysDistrictMapper.selectSysDistrictByPid(pid);
    }

    /**
     * 根据ID查询省市县数据
     *
     * @param id 省市县数据ID
     * @return 省市县数据
     */
    @Override
    public SysDistrict selectSysDistrictById(String id) {
        return sysDistrictMapper.selectSysDistrictById(id);
    }
}