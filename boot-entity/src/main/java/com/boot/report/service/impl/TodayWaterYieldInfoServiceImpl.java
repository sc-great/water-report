package com.boot.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.domain.TodayWaterYieldInfo;
import com.boot.report.mapper.ReportDateInfoMapper;
import com.boot.report.mapper.TodayWaterYieldInfoMapper;
import com.boot.report.service.ITodayWaterYieldInfoService;

/**
 * 水厂当日水量信息Service业务层处理
 *
 * @author EPL
 * @date 2020-03-24
 */
@Service
public class TodayWaterYieldInfoServiceImpl implements ITodayWaterYieldInfoService {
	@Autowired
	private TodayWaterYieldInfoMapper todayWaterYieldInfoMapper;
	@Autowired
	private ReportDateInfoMapper reportDateInfoMapper;

	/**
	 * 新增水厂当日水量信息
	 *
	 * @param todayWaterYieldInfo 水厂当日水量信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(TodayWaterYieldInfo todayWaterYieldInfo) {
		// 若是二次填报，则把第一次填报的信息改为不可用标识
		String curDate = DateUtils.getDate();
		TodayWaterYieldInfo todayWaterYieldParam = new TodayWaterYieldInfo();
		todayWaterYieldParam.setFactoryId(todayWaterYieldInfo.getFactoryId());
		todayWaterYieldParam.setFillDate(curDate);
		todayWaterYieldParam.setEffectIcon("1");
		TodayWaterYieldInfo origintodayWaterYieldInfo = todayWaterYieldInfoMapper.getEntity(todayWaterYieldParam);
		if (origintodayWaterYieldInfo != null) {
			origintodayWaterYieldInfo.setEffectIcon("2");
			todayWaterYieldInfoMapper.update(origintodayWaterYieldInfo);
		}
		// 添加新数据
		int insertRow = todayWaterYieldInfoMapper.insert(todayWaterYieldInfo);
		if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(curDate);
			reportDateInfoParam.setFactoryId(todayWaterYieldInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;
	}

	/**
	 * 修改水厂当日水量信息
	 *
	 * @param todayWaterYieldInfo 水厂当日水量信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.update(todayWaterYieldInfo);
	}

	/**
	 * 删除水厂当日水量信息对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
			todayWaterYieldInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = todayWaterYieldInfoMapper.delete(todayWaterYieldInfo);
		}
		return result;
	}

	/**
	 * 查询水厂当日水量信息数量
	 *
	 * @param todayWaterYieldInfo 查询条件
	 * @return 水厂当日水量信息数量
	 */
	@Override
	public int getCount(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getCount(todayWaterYieldInfo);
	}
	
	/**
	 * 手机端实际处理累计
	 * @param todayWaterYieldInfo
	 * @return
	 */
	@Override
	public Double getWaterYieldOutSumByApp(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getWaterYieldOutSumByApp(todayWaterYieldInfo);
	}

	/**
	 * 获取水厂当日水量信息实体对象
	 *
	 * @param id 水厂当日水量信息ID
	 * @return 水厂当日水量信息
	 */
	@Override
	public TodayWaterYieldInfo getEntityById(String id) {
		TodayWaterYieldInfo todayWaterYieldInfo = new TodayWaterYieldInfo();
		todayWaterYieldInfo.setId(id);
		return todayWaterYieldInfoMapper.getEntity(todayWaterYieldInfo);
	}
	
	@Override
	public TodayWaterYieldInfo getEntity(TodayWaterYieldInfo todayWaterYieldInfo) {
		
		return todayWaterYieldInfoMapper.getEntity(todayWaterYieldInfo);
	}
	/**
	 * 查询水厂当日水量信息列表
	 *
	 * @param todayWaterYieldInfo 水厂当日水量信息
	 * @return 水厂当日水量信息
	 */
	@Override
	public List<TodayWaterYieldInfo> getList(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getList(todayWaterYieldInfo);
	}

	@Override
	public Map<String, Object> getSum(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getSum(todayWaterYieldInfo);
	}

	/**
	 * <!-- 手机端登录后的水量统计 :当日前累计的进水总量 和出水总量 -->
	 */
	@Override
	public Map<String, Object> getSumByApp(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getSumByApp(todayWaterYieldInfo);
	}

	/**
	 * 添加页面中显示的当日前累计的进水总量 和出水总量
	 */
	public Map<String, Object> getAddOldTotalInOut(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getSumAddInOut(todayWaterYieldInfo);
	}

	/**
	 * 手机端水量统计详情页面：区域分析当日量
	 */

	@Override
	public Map<String, Object> getSumByAppArea(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getSumByAppArea(todayWaterYieldInfo);
	}

	/**
	 * 手机端水量统计详情页面：区域分析 当日以前累计量 ,单位是 :万吨
	 * 
	 * @param todayWaterYieldInfo
	 * @return
	 */

	@Override
	public Map<String, Object> getSumByAppAreaBefore(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getSumByAppAreaBefore(todayWaterYieldInfo);
	}

	/**
	 * 手机端水量统计：公司级水量分析页面的当前所选日期以前的累计量
	 */
	@Override
	public Map<String, Object> getSumByAppArea_company_Before(TodayWaterYieldInfo todayWaterYieldInfo) {
		return todayWaterYieldInfoMapper.getSumByAppArea_company_Before(todayWaterYieldInfo);
	}

	@Override
	public Map<String, Object> getSumForChart(TodayWaterYieldInfo waterYieldInfo) {
		return todayWaterYieldInfoMapper.getSumForChart(waterYieldInfo);
	}

	@Override
	public Map<String, Object> getTodayForChart(TodayWaterYieldInfo waterYieldInfo) {
		return todayWaterYieldInfoMapper.getTodayForChart(waterYieldInfo);
	}
}