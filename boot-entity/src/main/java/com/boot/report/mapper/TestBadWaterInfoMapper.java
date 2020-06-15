package com.boot.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.boot.report.domain.TestBadWaterInfo;

/**
 * 化验污水数据信息Mapper接口
 * 
 * @author yangxiaojun
 * @date 2020-04-22
 */
@Component
@Mapper
public interface TestBadWaterInfoMapper {
    /**
     * 新增化验水质数据信息(污水处理厂/排水厂)
     *
     * @param badWaterQualityInfo 化验污水数据信息(污水处理厂/排水厂)
     * @return 结果
     */
    public int insert(TestBadWaterInfo testBadWaterInfo);
    /**
     * 修改化验污水数据信息(污水处理厂/排水厂)
     *
     * @param badWaterQualityInfo 化验污水数据信息(污水处理厂/排水厂)
     * @return 结果
     */
    public int update(TestBadWaterInfo testBadWaterInfo);
    /**
     * 批量删除化验污水数据信息(污水处理厂/排水厂)
     *
     * @param badWaterQualityInfo 删除条件
     * @return 结果
     */
    public int delete(TestBadWaterInfo testBadWaterInfo);
    /**
     * 获取化验污水数据信息(污水处理厂/排水厂)数量
     *
     * @param badWaterQualityInfo 查询条件
     * @return 结果
     */
    public int getCount(TestBadWaterInfo testBadWaterInfo);
    /**
     * 获取化验污水数据信息(污水处理厂/排水厂)对象
     * 
     * @param badWaterQualityInfo 查询条件
     * @return 化验污水数据信息(污水处理厂/排水厂)
     */
    public TestBadWaterInfo getEntity(TestBadWaterInfo testBadWaterInfo);
    /**
     * 查询化验污水数据信息(污水处理厂/排水厂)列表
     * 
     * @param badWaterQualityInfo 查询条件
     * @return 化验污水数据信息(污水处理厂/排水厂)集合
     */
    public List<TestBadWaterInfo> getList(TestBadWaterInfo testBadWaterInfo);
}