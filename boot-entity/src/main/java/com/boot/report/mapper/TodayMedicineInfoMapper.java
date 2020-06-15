package com.boot.report.mapper;

import com.boot.report.domain.TodayMedicineInfo;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂当日用药信息填报Mapper接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Component
@Mapper
public interface TodayMedicineInfoMapper {
	/**
	 * 新增水厂当日用药信息填报
	 *
	 * @param todayMedicineInfo 水厂当日用药信息填报
	 * @return 结果
	 */
	public int insert(TodayMedicineInfo todayMedicineInfo);

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
	 * @param todayMedicineInfo 删除条件
	 * @return 结果
	 */
	public int delete(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 获取水厂当日用药信息填报数量
	 *
	 * @param todayMedicineInfo 查询条件
	 * @return 结果
	 */
	public int getCount(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 获取水厂当日用药信息填报对象
	 * 
	 * @param todayMedicineInfo 查询条件
	 * @return 水厂当日用药信息填报
	 */
	public TodayMedicineInfo getEntity(TodayMedicineInfo todayMedicineInfo);

	/**
	 * 查询水厂当日用药信息填报列表
	 * 
	 * @param todayMedicineInfo 查询条件
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