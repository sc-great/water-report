package com.boot.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.report.mapper.AlarmInfoMapper;
import com.boot.report.domain.AlarmInfo;
import com.boot.report.service.AlarmInfoService;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报警信息Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-30
 */
@Service
public class AlarmInfoServiceImpl implements AlarmInfoService {
	@Autowired
	private AlarmInfoMapper alarmInfoMapper;

	/**
	 * 新增报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(AlarmInfo alarmInfo) {
		return alarmInfoMapper.insert(alarmInfo);
	}

	/**
	 * 修改报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(AlarmInfo alarmInfo) {
		return alarmInfoMapper.update(alarmInfo);
	}

	/**
	 * 删除报警信息对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = alarmInfoMapper.delete(alarmInfo);
		}
		return result;
	}

	/**
	 * 获取报警信息实体对象
	 * 
	 * @param id 报警信息ID
	 * @return 报警信息
	 */
	@Override
	public AlarmInfo getEntityById(String id) {
		AlarmInfo alarmInfo = new AlarmInfo();
		alarmInfo.setId(id);
		return alarmInfoMapper.getEntity(alarmInfo);
	}

	/**
	 * 查询报警信息列表
	 * 
	 * @param alarmInfo 报警信息
	 * @return 报警信息
	 */
	@Override
	public List<AlarmInfo> getList(AlarmInfo alarmInfo) {
		return alarmInfoMapper.getList(alarmInfo);
	}
}