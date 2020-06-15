package com.boot.report.service;

import com.boot.report.domain.TodayWaterYieldInfo;
import java.util.List;
import java.util.Map;

/**
 * 水厂当日水量信息Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface ITodayWaterYieldInfoService {
	/**
	 * 新增水厂当日水量信息
	 *
	 * @param todayWaterYieldInfo 水厂当日水量信息
	 * @return 结果
	 */
	public int add(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 * 修改水厂当日水量信息
	 *
	 * @param todayWaterYieldInfo 水厂当日水量信息
	 * @return 结果
	 */
	public int update(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 * 批量删除水厂当日水量信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询水厂当日水量信息数量
	 *
	 * @param todayWaterYieldInfo 查询条件
	 * @return 水厂当日水量信息数量
	 */
	public int getCount(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 * 获取水厂当日水量信息实体对象
	 * 
	 * @param id 水厂当日水量信息ID
	 * @return 水厂当日水量信息
	 */
	public TodayWaterYieldInfo getEntityById(String id);
	public TodayWaterYieldInfo getEntity(TodayWaterYieldInfo todayWaterYieldInfo);
	/**
	 * 查询水厂当日水量信息列表
	 * 
	 * @param todayWaterYieldInfo 水厂当日水量信息
	 * @return 水厂当日水量信息集合
	 */
	public List<TodayWaterYieldInfo> getList(TodayWaterYieldInfo todayWaterYieldInfo);

	public Map<String, Object> getSum(TodayWaterYieldInfo todayWaterYieldInfo);

	public Map<String, Object> getSumByApp(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 *  添加页面中显示的当日前累计的进水总量 和出水总量
	 */
	public Map<String, Object> getAddOldTotalInOut(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 * 手机端水量统计详情页面：区域分析当日量
	 */
	public Map<String, Object> getSumByAppArea(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 * 手机端水量统计详情页面：区域分析 当日以前累计量 ,单位是 :万吨
	 * @param todayWaterYieldInfo
	 * @return
	 */
	public Map<String, Object> getSumByAppAreaBefore(TodayWaterYieldInfo todayWaterYieldInfo);

	/**
	 *  手机端水量统计：公司级水量分析页面的当前所选日期以前的累计量
	 */
	public Map<String, Object> getSumByAppArea_company_Before(TodayWaterYieldInfo todayWaterYieldInfo);

	public Map<String, Object> getSumForChart(TodayWaterYieldInfo waterYieldInfo);

	public Map<String, Object> getTodayForChart(TodayWaterYieldInfo waterYieldInfo);

	/**
	 * 手机端实际处理累计
	 * 
	 * @param todayWaterYieldInfo
	 * @return
	 */
	public Double getWaterYieldOutSumByApp(TodayWaterYieldInfo todayWaterYieldInfo);
}