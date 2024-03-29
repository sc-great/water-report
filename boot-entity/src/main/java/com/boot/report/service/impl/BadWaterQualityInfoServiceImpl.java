package com.boot.report.service.impl;

import java.util.List;

import com.boot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.report.mapper.AlarmInfoMapper;
import com.boot.report.mapper.BadWaterQualityInfoMapper;
import com.boot.report.mapper.ReportDateInfoMapper;
import com.boot.report.domain.AlarmInfo;
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.service.IBadWaterQualityInfoService;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 水厂水质数据信息(污水处理厂/排水厂)Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Service
public class BadWaterQualityInfoServiceImpl implements IBadWaterQualityInfoService {
	@Autowired
	private BadWaterQualityInfoMapper badWaterQualityInfoMapper;
	@Autowired
	private ReportDateInfoMapper reportDateInfoMapper;
	@Autowired
	private AlarmInfoMapper alarmInfoMapper;

	/**
	 * 新增水厂水质数据信息(污水处理厂/排水厂)
	 *
	 * @param badWaterQualityInfo 水厂水质数据信息(污水处理厂/排水厂)
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(BadWaterQualityInfo badWaterQualityInfo, String alarmNote) {
		String curDate = DateUtils.getDate();
		// 若是二次填报，则把第一次填报的信息改为不可用标识
		BadWaterQualityInfo badWaterQualityParam = new BadWaterQualityInfo();
		badWaterQualityParam.setFillDate(curDate);
		badWaterQualityParam.setFactoryId(badWaterQualityInfo.getFactoryId());
		badWaterQualityParam.setEffectIcon("1");
		BadWaterQualityInfo originBadWaterQualityInfo = badWaterQualityInfoMapper.getEntity(badWaterQualityParam);

		if (originBadWaterQualityInfo != null) {
			originBadWaterQualityInfo.setEffectIcon("2");
			badWaterQualityInfoMapper.update(originBadWaterQualityInfo);
		}

		// 添加新数据
		int insertRow = badWaterQualityInfoMapper.insert(badWaterQualityInfo);

		// 报警监控
		if (!"".equals(alarmNote)) {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setOrgId(badWaterQualityInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);

			alarmInfo.setAreaId(badWaterQualityInfo.getAreaId());
			alarmInfo.setAlarmItem(alarmNote);
			alarmInfo.setObjId(badWaterQualityInfo.getId());
			alarmInfoMapper.insert(alarmInfo);
		} else {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setOrgId(badWaterQualityInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);
		}

		// 水厂填报日期登记
		if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(curDate);
			reportDateInfoParam.setFactoryId(badWaterQualityInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;
	}

	/**
	 * 修改水厂水质数据信息(污水处理厂/排水厂)
	 *
	 * @param badWaterQualityInfo 水厂水质数据信息(污水处理厂/排水厂)
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(BadWaterQualityInfo badWaterQualityInfo, String alarmNote) {
		BadWaterQualityInfo b = new BadWaterQualityInfo();
		b.setId(badWaterQualityInfo.getId());
		BadWaterQualityInfo bwqi = badWaterQualityInfoMapper.getEntity(b);
		// 报警监控
		AlarmInfo alarmInfo = new AlarmInfo();
		alarmInfo.setFillDate(bwqi.getFillDate());
		if (!"".equals(alarmNote)) {
			alarmInfo.setOrgId(badWaterQualityInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);

			alarmInfo.setAreaId(badWaterQualityInfo.getAreaId());
			alarmInfo.setAlarmItem(alarmNote);
			alarmInfo.setObjId(badWaterQualityInfo.getId());
			alarmInfoMapper.insert(alarmInfo);
		} else {
			alarmInfo.setOrgId(badWaterQualityInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);
		}
		return badWaterQualityInfoMapper.update(badWaterQualityInfo);
	}

	/**
	 * 删除水厂水质数据信息(污水处理厂/排水厂)对象
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
			BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
			badWaterQualityInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = badWaterQualityInfoMapper.delete(badWaterQualityInfo);
		}
		return result;
	}

	/**
	* 查询水厂水质数据信息(污水处理厂/排水厂)数量
	*
	* @param badWaterQualityInfo 查询条件
	* @return 水厂水质数据信息(污水处理厂/排水厂)数量
	*/
	@Override
	public int getCount(BadWaterQualityInfo badWaterQualityInfo) {
		return badWaterQualityInfoMapper.getCount(badWaterQualityInfo);
	}

	/**
	 * 获取水厂水质数据信息(污水处理厂/排水厂)实体对象
	 * 
	 * @param id 水厂水质数据信息(污水处理厂/排水厂)ID
	 * @return 水厂水质数据信息(污水处理厂/排水厂)
	 */
	@Override
	public BadWaterQualityInfo getEntityById(String id) {
		BadWaterQualityInfo badWaterQualityInfo = new BadWaterQualityInfo();
		badWaterQualityInfo.setId(id);
		return badWaterQualityInfoMapper.getEntity(badWaterQualityInfo);
	}

	@Override
	public BadWaterQualityInfo getEntity(BadWaterQualityInfo badWaterQualityInfo) {
		return badWaterQualityInfoMapper.getEntity(badWaterQualityInfo);
	}

	/**
	 * 查询水厂水质数据信息(污水处理厂/排水厂)列表
	 * 
	 * @param badWaterQualityInfo 水厂水质数据信息(污水处理厂/排水厂)
	 * @return 水厂水质数据信息(污水处理厂/排水厂)
	 */
	@Override
	public List<BadWaterQualityInfo> getList(BadWaterQualityInfo badWaterQualityInfo) {
		return badWaterQualityInfoMapper.getList(badWaterQualityInfo);
	}

	/**
	 * 查询超标天数
	 */
	@Override
	public int getOverNorm(BadWaterQualityInfo badWaterQualityInfo) {
		return badWaterQualityInfoMapper.getOverNorm(badWaterQualityInfo);
	}

	@Override
	public int append(BadWaterQualityInfo badWaterQualityInfo, String alarmNote) {
		String curDate = badWaterQualityInfo.getFillDate();
		// 若是二次填报，则把第一次填报的信息改为不可用标识
		BadWaterQualityInfo badWaterQualityParam = new BadWaterQualityInfo();
		badWaterQualityParam.setFillDate(curDate);
		badWaterQualityParam.setFactoryId(badWaterQualityInfo.getFactoryId());
		badWaterQualityParam.setEffectIcon("1");
		BadWaterQualityInfo originBadWaterQualityInfo = badWaterQualityInfoMapper.getEntity(badWaterQualityParam);

		if (originBadWaterQualityInfo != null) {
			originBadWaterQualityInfo.setEffectIcon("2");
			badWaterQualityInfoMapper.update(originBadWaterQualityInfo);
		}

		// 添加新数据
		int insertRow = badWaterQualityInfoMapper.insert(badWaterQualityInfo);

		// 报警监控
		AlarmInfo alarmInfo = new AlarmInfo();
		alarmInfo.setFillDate(curDate);
		if (!"".equals(alarmNote)) {
			alarmInfo.setOrgId(badWaterQualityInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);

			alarmInfo.setAreaId(badWaterQualityInfo.getAreaId());
			alarmInfo.setAlarmItem(alarmNote);
			alarmInfo.setObjId(badWaterQualityInfo.getId());
			alarmInfoMapper.insert(alarmInfo);
		} else {
			alarmInfo.setOrgId(badWaterQualityInfo.getFactoryId());
			alarmInfo.setAlarmType("3");
			alarmInfo.setEffectIcon("1");
			alarmInfoMapper.updateByOrgIdAndAlarmType(alarmInfo);
		}

		// 水厂填报日期登记
		if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(curDate);
			reportDateInfoParam.setFactoryId(badWaterQualityInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;
	}
}