package com.boot.report.service;

import com.boot.report.domain.ReportDateInfo;

import java.util.List;
import java.util.Map;

/**
 * 今日数据信息Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface ITodayInfoService {
	/**
	 * 查询今日数据信息列表
	 * 
	 * @param reportDateInfo 查询条件
	 * @return 今日数据信息集合
	 */
	public List<Map<String, Object>> getList(ReportDateInfo reportDateInfo);

	public Map<String, Object> getSumData(ReportDateInfo r);

	public Map<String, Map<String, Object>> getDailyReport(ReportDateInfo r);
}