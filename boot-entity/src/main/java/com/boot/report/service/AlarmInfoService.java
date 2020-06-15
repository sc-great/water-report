package com.boot.report.service;

import com.boot.report.domain.AlarmInfo;
import java.util.List;

/**
 * 报警信息Service接口
 * 
 * @author LM
 * @date 2020-05-10
 */
public interface AlarmInfoService {
	/**
	 * 新增报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	public int add(AlarmInfo alarmInfo);

	/**
	 * 修改报警信息
	 *
	 * @param alarmInfo 报警信息
	 * @return 结果
	 */
	public int update(AlarmInfo alarmInfo);

	/**
	 * 批量删除报警信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 获取报警信息实体对象
	 * 
	 * @param id 报警信息ID
	 * @return 报警信息
	 */
	public AlarmInfo getEntityById(String id);

	/**
	 * 查询报警信息列表
	 * 
	 * @param alarmInfo 报警信息
	 * @return 报警信息集合
	 */
	public List<AlarmInfo> getList(AlarmInfo alarmInfo);
}