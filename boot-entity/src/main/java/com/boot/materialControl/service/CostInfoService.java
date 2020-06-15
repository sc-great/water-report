package com.boot.materialControl.service;

import java.util.List;
import java.util.Map;

import com.boot.materialControl.domain.CostInfo;

public interface CostInfoService {

	public List<CostInfo> getList(CostInfo costInfo);

	public int add(CostInfo costInfo);

	public CostInfo getEntityById(String id);

	public int update(CostInfo costInfo);

	public int deleteByIds(String ids);
	// 查询上一条数据
	public CostInfo getLast(CostInfo costInfo);

	public Map<String, Object> getSum(CostInfo costInfo);

	public Map<String, Object> getSumBySubjectCost(CostInfo costInfo);

	public int count(CostInfo costInfo);
	
	public  double  getSumByApp(CostInfo costInfo);


	public double getSumByAppA1(CostInfo costInfo);

	public double getSumByAppA2(CostInfo costInfo);
}
