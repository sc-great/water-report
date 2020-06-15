package com.boot.report.service.impl;

import com.boot.report.domain.ReportDateInfo;
import com.boot.report.mapper.TodayInfoMapper;
import com.boot.report.service.ITodayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TodayInfoServiceImpl
 *
 * @author EPL
 * @date 2020/3/26 0026
 */
@Service
public class TodayInfoServiceImpl implements ITodayInfoService {
	@Autowired
	private TodayInfoMapper todayInfoMapper;

	/**
	 * 查询今日数据信息列表
	 *
	 * @param reportDateInfo 查询条件
	 * @return 今日数据信息集合
	 */
	@Override
	public List<Map<String, Object>> getList(ReportDateInfo reportDateInfo) {
		return todayInfoMapper.getList(reportDateInfo);
	}

	@Override
	public Map<String, Object> getSumData(ReportDateInfo r) {
		return todayInfoMapper.getSumData(r);
	}

	@Override
	public Map<String, Map<String, Object>> getDailyReport(ReportDateInfo r) {
		List<Map<String, Object>> list = todayInfoMapper.getDailyReport(r);
		Map<String, Map<String, Object>> returnMap = new HashMap<>();
		for (Map<String, Object> map : list) {
			returnMap.put(map.get("reportDate").toString(), map);
		}
		return returnMap;
	}
}
