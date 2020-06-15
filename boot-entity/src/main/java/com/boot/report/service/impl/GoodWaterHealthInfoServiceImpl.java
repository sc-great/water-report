package com.boot.report.service.impl;

import java.util.List;

import com.boot.common.utils.DateUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.report.mapper.AlarmInfoMapper;
import com.boot.report.mapper.GoodWaterHealthInfoMapper;
import com.boot.report.mapper.ReportDateInfoMapper;
import com.boot.report.domain.AlarmInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.service.IGoodWaterHealthInfoService;
import com.boot.system.domain.SysUser;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 水厂水质数据信息（自来水厂/给水厂）Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Service
public class GoodWaterHealthInfoServiceImpl implements IGoodWaterHealthInfoService {
	@Autowired
	private GoodWaterHealthInfoMapper goodWaterHealthInfoMapper;
	@Autowired
	private ReportDateInfoMapper reportDateInfoMapper;
	@Autowired
	private AlarmInfoMapper alarmInfoMapper;

	/**
	 * 新增水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(GoodWaterHealthInfo goodWaterHealthInfo, String alarmNote) {
		// 若是二次填报，则把第一次填报的信息改为不可用标识
		GoodWaterHealthInfo goodWaterHealthParam = new GoodWaterHealthInfo();
		goodWaterHealthParam.setFillDate(DateUtils.getDate());
		goodWaterHealthParam.setFactoryId(goodWaterHealthInfo.getFactoryId());
		goodWaterHealthParam.setEffectIcon("1");
		GoodWaterHealthInfo originGoodWaterHealthInfo = goodWaterHealthInfoMapper.getEntity(goodWaterHealthParam);
		if (originGoodWaterHealthInfo != null) {
			originGoodWaterHealthInfo.setEffectIcon("2");
			goodWaterHealthInfoMapper.update(originGoodWaterHealthInfo);
		}
		// 添加新数据
		int insertRow = goodWaterHealthInfoMapper.insert(goodWaterHealthInfo);

		// 报警监控
		if (!"".equals(alarmNote)) {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setOrgId(goodWaterHealthInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);

			alarmInfo.setAreaId(goodWaterHealthInfo.getAreaId());
			alarmInfo.setAlarmItem(alarmNote);
			alarmInfo.setObjId(goodWaterHealthInfo.getId());
			alarmInfoMapper.insert(alarmInfo);
		}else{
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setOrgId(goodWaterHealthInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);
		}

		// 水厂填报日期登记
		if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(DateUtils.getDate());
			reportDateInfoParam.setFactoryId(goodWaterHealthInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;

	}

	/**
	 * 修改水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(GoodWaterHealthInfo goodWaterHealthInfo, String alarmNote) {

		// 报警监控
		if (!"".equals(alarmNote)) {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setOrgId(goodWaterHealthInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);

			alarmInfo.setAreaId(goodWaterHealthInfo.getAreaId());
			alarmInfo.setAlarmItem(alarmNote);
			alarmInfo.setObjId(goodWaterHealthInfo.getId());
			alarmInfoMapper.insert(alarmInfo);
		}else{
			
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setOrgId(goodWaterHealthInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);
		}
		return goodWaterHealthInfoMapper.update(goodWaterHealthInfo);
	}

	/**
	 * 删除水厂水质数据信息（自来水厂/给水厂）对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			// 报警数据删除
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.getParams().put("objIds", Convert.toStrArray(ids));
			alarmInfoMapper.delete(alarmInfo);
			// 水质数据删除
			GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
			goodWaterHealthInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = goodWaterHealthInfoMapper.delete(goodWaterHealthInfo);
			
		}
		return result;
	}

	/**
	* 查询水厂水质数据信息（自来水厂/给水厂）数量
	*
	* @param goodWaterHealthInfo 查询条件
	* @return 水厂水质数据信息（自来水厂/给水厂）数量
	*/
	@Override
	public int getCount(GoodWaterHealthInfo goodWaterHealthInfo) {
		return goodWaterHealthInfoMapper.getCount(goodWaterHealthInfo);
	}

	/**
	 * 获取水厂水质数据信息（自来水厂/给水厂）实体对象
	 * 
	 * @param id 水厂水质数据信息（自来水厂/给水厂）ID
	 * @return 水厂水质数据信息（自来水厂/给水厂）
	 */
	@Override
	public GoodWaterHealthInfo getEntityById(String id) {
		GoodWaterHealthInfo goodWaterHealthInfo = new GoodWaterHealthInfo();
		goodWaterHealthInfo.setId(id);
		return goodWaterHealthInfoMapper.getEntity(goodWaterHealthInfo);
	}

	@Override
	public GoodWaterHealthInfo getEntity(GoodWaterHealthInfo goodWaterHealthInfo) {
		return goodWaterHealthInfoMapper.getEntity(goodWaterHealthInfo);
	}

	/**
	 * 查询水厂水质数据信息（自来水厂/给水厂）列表
	 * 
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 水厂水质数据信息（自来水厂/给水厂）
	 */
	@Override
	public List<GoodWaterHealthInfo> getList(GoodWaterHealthInfo goodWaterHealthInfo) {
		return goodWaterHealthInfoMapper.getList(goodWaterHealthInfo);
	}

	@Override
	public int getOverNorm(GoodWaterHealthInfo goodWaterHealthInfo) {
		return goodWaterHealthInfoMapper.getOverNorm(goodWaterHealthInfo);
	}
}