package com.boot.materialControl.mapper;

import java.util.List;
import java.util.Map;

import com.boot.materialControl.domain.CostInfo;

public interface CostInfoMapper {

	public List<CostInfo> getList(CostInfo costInfo);

	public int insert(CostInfo costInfo);

	public int update(CostInfo costInfoUpdateParam);

	public CostInfo getEntity(CostInfo costInfoParam);

	public int delete(CostInfo costInfo);

	public CostInfo getLast(CostInfo costInfo);

	public Map<String, Object> getSum(CostInfo costInfo);

	public Map<String, Object> getSumBySubjectCost(CostInfo costInfo);

	public int count(CostInfo costInfo);
	
	public  double  getSumByApp(CostInfo costInfo);

	public  Map<String, Object>  getSumByAppYear(CostInfo costInfo);


}
