package com.boot.report.mapper;

import com.boot.report.domain.AlarmInfo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 报警信息Mapper接口
 * 
 * @author LM
 * @date 2020-05-10
 */
@Component
@Mapper
public interface AlarmInfoMapper {
	/**
	 * 新增报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	public int insert(AlarmInfo alarmInfo);

	/**
	 * 修改报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	public int update(AlarmInfo alarmInfo);
	public int updateByOrgIdAndAlarmType(AlarmInfo alarmInfo);
	/**
	 * 修改报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	public int delete(AlarmInfo alarmInfo);

	/**
	 * 获取报警信息对象
	 * 
	 * @param alarmInfo 查询条件
	 * @return 报警信息
	 */
	public AlarmInfo getEntity(AlarmInfo alarmInfo);

	/**
	 * 查询报警信息列表
	 * 
	 * @param alarmInfo 查询条件
	 * @return 报警信息集合
	 */
	public List<AlarmInfo> getList(AlarmInfo alarmInfo);
}