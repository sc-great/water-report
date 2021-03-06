package com.boot.report.mapper;

import com.boot.report.domain.ReportDateInfo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂填报日期登记Mapper接口
 * 
 * @author EPL
 * @date 2020-03-26
 */
@Component
@Mapper
public interface ReportDateInfoMapper {
    /**
     * 新增水厂填报日期登记
     *
     * @param reportDateInfo 水厂填报日期登记
     * @return 结果
     */
    public int insert(ReportDateInfo reportDateInfo);
    /**
     * 获取水厂填报日期登记数量
     *
     * @param reportDateInfo 查询条件
     * @return 结果
     */
    public int getCount(ReportDateInfo reportDateInfo);
}