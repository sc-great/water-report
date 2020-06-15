package com.boot.system.mapper;

import com.boot.system.domain.Area;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 区域Mapper接口
 * 
 * @author EPL
 * @date 2020-03-23
 */
@Component
@Mapper
public interface AreaMapper {
    /**
     * 新增区域
     *
     * @param area 区域
     * @return 结果
     */
    public int insert(Area area);
    /**
     * 修改区域
     *
     * @param area 区域
     * @return 结果
     */
    public int update(Area area);
    /**
     * 批量删除区域
     *
     * @param area 删除条件
     * @return 结果
     */
    public int delete(Area area);
    /**
     * 获取区域数量
     *
     * @param area 查询条件
     * @return 结果
     */
    public int getCount(Area area);
    /**
     * 获取区域对象
     * 
     * @param area 查询条件
     * @return 区域
     */
    public Area getEntity(Area area);
    /**
     * 查询区域列表
     * 
     * @param area 查询条件
     * @return 区域集合
     */
    public List<Area> getList(Area area);
}