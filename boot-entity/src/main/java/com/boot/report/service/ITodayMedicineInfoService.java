package com.boot.report.service;

import com.boot.report.domain.TodayMedicineInfo;
import java.util.List;
import java.util.Map;

/**
 * 水厂当日用药信息填报Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface ITodayMedicineInfoService {
	/**
	 * 新增水厂当日用药信息填报
	 *
	 * @param todayMedicineInfo 水厂当日用药信息填报
	 * @return 结果
	 */
	public int add(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 修改水厂当日用药信息填报
	 *
	 * @param todayMedicineInfo 水厂当日用药信息填报
	 * @return 结果
	 */
	public int update(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 批量删除水厂当日用药信息填报
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询水厂当日用药信息填报数量
	 *
	 * @param todayMedicineInfo 查询条件
	 * @return 水厂当日用药信息填报数量
	 */
	public int getCount(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 获取水厂当日用药信息填报实体对象
	 * 
	 * @param id 水厂当日用药信息填报ID
	 * @return 水厂当日用药信息填报
	 */
	public TodayMedicineInfo getEntityById(String id);
	public TodayMedicineInfo getEntity(TodayMedicineInfo todayMedicineInfo);
	/**
	 * 查询水厂当日用药信息填报列表
	 * 
	 * @param todayMedicineInfo 水厂当日用药信息填报
	 * @return 水厂当日用药信息填报集合
	 */
	public List<TodayMedicineInfo> getList(TodayMedicineInfo todayMedicineInfo);

	public Map<String, Object> getSum(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 手机端登录后的统计 :当日前累计的药量
	 */
	public Map<String, Object> getSumByApp(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 查最新数据
	 * 
	 * @param todayMedicineInfo
	 * @return
	 */
	public TodayMedicineInfo getLatest(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 手机端药量统计:区域、公司、工厂所选日期前的累计药量
	 */
	public Map<String, Object> getSumByAppBefore(TodayMedicineInfo todayMedicineInfo);

	public Map<String, Object> getSumForChart(TodayMedicineInfo todayMedicineInfo);

	public Map<String, Object> getTodayForChart(TodayMedicineInfo todayMedicineInfo);

}