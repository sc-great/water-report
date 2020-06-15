package com.boot.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.system.mapper.AreaMapper;
import com.boot.system.domain.Area;
import com.boot.system.service.IAreaService;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 区域Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-23
 */
@Service
public class AreaServiceImpl implements IAreaService {
    @Autowired
    private AreaMapper areaMapper;

    /**
     * 新增区域
     *
     * @param area 区域
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Area area) {
                                                                            return areaMapper.insert(area);
    }
    /**
     * 修改区域
     *
     * @param area 区域
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Area area) {
                                                                            return areaMapper.update(area);
    }
    /**
     * 删除区域对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(String ids) {
        int result=0;
        if(!ids.isEmpty()){
            Area area=new Area();
            area.getParams().put("ids",Convert.toStrArray(ids));
            result=areaMapper.delete(area);
        }
        return result;
    }
    /**
    * 查询区域数量
    *
    * @param area 查询条件
    * @return 区域数量
    */
    @Override
    public int getCount(Area area){
        return areaMapper.getCount(area);
    }
    /**
     * 获取区域实体对象
     * 
     * @param areaId 区域ID
     * @return 区域
     */
    @Override
    public Area getEntityById(String areaId) {
        Area area=new Area();
        area.setAreaId(areaId);
        return areaMapper.getEntity(area);
    }

    /**
     * 查询区域列表
     * 
     * @param area 区域
     * @return 区域
     */
    @Override
        public List<Area> getList(Area area) {
        return areaMapper.getList(area);
    }
}