package com.boot.report.mapper;

import com.boot.report.domain.TodayElectricityInfo;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂当日用电信息Mapper接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Component
@Mapper
public interface TodayElectricityInfoMapper {
    /**
     * 新增水厂当日用电信息
     *
     * @param todayElectricityInfo 水厂当日用电信息
     * @return 结果
     */
    public int insert(TodayElectricityInfo todayElectricityInfo);
    /**
     * 修改水厂当日用电信息
     *
     * @param todayElectricityInfo 水厂当日用电信息
     * @return 结果
     */
    public int update(TodayElectricityInfo todayElectricityInfo);
    /**
     * 批量删除水厂当日用电信息
     *
     * @param todayElectricityInfo 删除条件
     * @return 结果
     */
    public int delete(TodayElectricityInfo todayElectricityInfo);
    /**
     * 获取水厂当日用电信息数量
     *
     * @param todayElectricityInfo 查询条件
     * @return 结果
     */
    public int getCount(TodayElectricityInfo todayElectricityInfo);
    /**
     * 获取水厂当日用电信息对象
     * 
     * @param todayElectricityInfo 查询条件
     * @return 水厂当日用电信息
     */
    public TodayElectricityInfo getEntity(TodayElectricityInfo todayElectricityInfo);
    /**
     * 查询水厂当日用电信息列表
     * 
     * @param todayElectricityInfo 查询条件
     * @return 水厂当日用电信息集合
     */
    public List<TodayElectricityInfo> getList(TodayElectricityInfo todayElectricityInfo);
    public Map<String,Object> getSum(TodayElectricityInfo todayElectricityInfo);
    
    public Map<String,Object> getSumByApp(TodayElectricityInfo todayElectricityInfo);
    
	public TodayElectricityInfo getLatest(TodayElectricityInfo todayElectricityInfo);
	/**
	 *  手机端电量详情统计分析页面：所 选择日期的以前累计电量
	 * @return
	 */
	
	public Map<String,Object> getSumByAppBefore(TodayElectricityInfo todayElectricityInfo);
	public Map<String, Object> getTodayForChart(TodayElectricityInfo todayElectricityInfo);
	public Map<String, Object> getSumForChart(TodayElectricityInfo todayElectricityInfo);


}


