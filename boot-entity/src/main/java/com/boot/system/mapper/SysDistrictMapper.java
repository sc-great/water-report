package com.boot.system.mapper;

import com.boot.system.domain.SysDistrict;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SysDistrictMapper
 *
 * @author yipuli
 * @date 2019/9/11 0011
 */
@Component
@Mapper
public interface SysDistrictMapper {
    /**
     * 查询省市县数据
     *
     * @param pid 省市县数据PID
     * @return 省市县数据
     */
    public List<SysDistrict> selectSysDistrictByPid(String pid);

    /**
     * 查询省市县数据
     *
     * @param id 省市县数据ID
     * @return 省市县数据
     */
    public SysDistrict selectSysDistrictById(String id);
}
