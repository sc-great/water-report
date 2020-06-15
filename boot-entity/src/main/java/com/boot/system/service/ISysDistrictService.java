package com.boot.system.service;

import com.boot.system.domain.SysDistrict;

import java.util.List;

/**
 * 省市县数据Service接口
 *
 * @author EPL
 * @date 2019-09-11
 */
public interface ISysDistrictService {
    /**
     * 根据父ID查询省市县数据
     *
     * @param pid 省市县数据PID
     * @return 省市县数据
     */
    public List<SysDistrict> selectSysDistrictByPid(String pid);

    /**
     * 根据ID查询省市县数据
     *
     * @param id 省市县数据ID
     * @return 省市县数据
     */
    public SysDistrict selectSysDistrictById(String id);
}