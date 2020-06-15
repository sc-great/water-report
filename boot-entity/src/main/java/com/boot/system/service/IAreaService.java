package com.boot.system.service;

import com.boot.system.domain.Area;
import java.util.List;
import java.util.Map;

/**
 * 区域Service接口
 * 
 * @author EPL
 * @date 2020-03-23
 */
public interface IAreaService {
    /**
     * 新增区域
     *
     * @param area 区域
     * @return 结果
     */
    public int add(Area area);
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
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteByIds(String ids);
    /**
     * 查询区域数量
     *
     * @param area 查询条件
     * @return 区域数量
     */
    public int getCount(Area area);
    /**
     * 获取区域实体对象
     * 
     * @param areaId 区域ID
     * @return 区域
     */
    public Area getEntityById(String areaId);
    /**
     * 查询区域列表
     * 
     * @param area 区域
     * @return 区域集合
     */
    public List<Area> getList(Area area);
}