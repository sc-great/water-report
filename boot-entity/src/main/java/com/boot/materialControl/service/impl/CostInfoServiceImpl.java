package com.boot.materialControl.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boot.common.utils.InitsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.materialControl.domain.CostInfo;
import com.boot.materialControl.service.CostInfoService;
import com.boot.materialControl.mapper.CostInfoMapper;

@Service
public class CostInfoServiceImpl implements CostInfoService {

	@Autowired
	private CostInfoMapper costInfoMapper;

	@Override
	public List<CostInfo> getList(CostInfo costInfo) {
		return costInfoMapper.getList(costInfo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(CostInfo costInfo) {
		// 若是二次填报，则把第一次填报的信息改为不可用标识
		Object iconObj = costInfo.getParams().get("icon");
		if (StringUtils.isNotNullAndNotEmpty(iconObj) && ("2").equals(iconObj.toString())) {
			CostInfo costInfoParam = new CostInfo();
			costInfoParam.setFactoryId(costInfo.getFactoryId());
			costInfoParam.setFillDate(DateUtils.getMouth(new Date()));
			costInfoParam.setEffectIcon("1");
			CostInfo costInfoUpdateParam = costInfoMapper.getEntity(costInfoParam);
			costInfoUpdateParam.setEffectIcon("2");
			costInfoMapper.update(costInfoUpdateParam);
		}
		return costInfoMapper.insert(costInfo);
	}

	@Override
	public CostInfo getEntityById(String id) {
		CostInfo costInfo = new CostInfo();
		costInfo.setId(id);
		return costInfoMapper.getEntity(costInfo);
	}

	@Override
	public int update(CostInfo costInfo) {
		return costInfoMapper.update(costInfo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			CostInfo costInfo = new CostInfo();
			costInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = costInfoMapper.delete(costInfo);
		}
		return result;
	}

	@Override
	public CostInfo getLast(CostInfo costInfo) {
		return costInfoMapper.getLast(costInfo);
	}

	@Override
	public Map<String, Object> getSum(CostInfo costInfo) {
		return costInfoMapper.getSum(costInfo);
	}

	@Override
	public Map<String, Object> getSumBySubjectCost(CostInfo costInfo) {
		return costInfoMapper.getSumBySubjectCost(costInfo);
	}

	@Override
	public int count(CostInfo costInfo) {
		return costInfoMapper.count(costInfo);
	}

	@Override
	public double getSumByApp(CostInfo costInfo) {

		double v1 = costInfoMapper.getSumByApp(costInfo); // 当年实际到帐

		String areaIds = costInfo.getParams().get("areaIds").toString();
		String[] areasArray = areaIds.split(",");
		double v2 = 0;
		for (int i = 0; i < areasArray.length; i++) {
			costInfo.setAreaId(areasArray[i]);
			Map<String, Object> tp = costInfoMapper.getSumByAppYear(costInfo);
			if (tp != null) {
				// 单个区域单位当年收费目标（财务最小是区域单位，没有水厂级的）
				v2 = v2 + Double.parseDouble(tp.get("speed").toString());
			}
		}

		if (v2 == 0) {
			return 0;
		} else {
			double d1 = InitsUtils.div(v1, v2, 2);
			double d2 = 100;

			return InitsUtils.mul(d1, d2);
		}
	}

	@Override
	public double getSumByAppA1(CostInfo costInfo) {
		return costInfoMapper.getSumByApp(costInfo); // 当年实际到帐
	}

	@Override
	public double getSumByAppA2(CostInfo costInfo) { // 多个单位的当年目标和
		String areaIds = costInfo.getParams().get("areaIds").toString();
		String[] areasArray = areaIds.split(",");
		double v2 = 0;
		for (int i = 0; i < areasArray.length; i++) {
			costInfo.setAreaId(areasArray[i]);
			Map<String, Object> tp = costInfoMapper.getSumByAppYear(costInfo);
			if (tp != null) {
				// 单个区域单位当年收费目标（财务最小是区域单位，没有水厂级的）
				v2 = v2 + Double.parseDouble(tp.get("speed").toString());
			}
		}
		return v2;
	}
}
