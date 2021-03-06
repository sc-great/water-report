package com.boot.report.service;

import com.boot.report.domain.UserHealthInfo;
import java.util.List;

/**
 * 员工健康信息Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface IUserHealthInfoService {
	/**
	 * 新增员工健康信息
	 *
	 * @param userHealthInfo 员工健康信息
	 * @return 结果
	 */
	public int add(UserHealthInfo userHealthInfo);

	/**
	 * 修改员工健康信息
	 *
	 * @param userHealthInfo 员工健康信息
	 * @return 结果
	 */
	public int update(UserHealthInfo userHealthInfo);

	public int updateByWhere(UserHealthInfo userHealthInfo);

	/**
	 * 批量删除员工健康信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询员工健康信息数量
	 *
	 * @param userHealthInfo 查询条件
	 * @return 员工健康信息数量
	 */
	public int getCount(UserHealthInfo userHealthInfo);

	/**
	 * 获取员工健康信息实体对象
	 * 
	 * @param id 员工健康信息ID
	 * @return 员工健康信息
	 */
	public UserHealthInfo getEntityById(String id);

	/**
	 * 查询员工健康信息列表
	 * 
	 * @param userHealthInfo 员工健康信息
	 * @return 员工健康信息集合
	 */
	public List<UserHealthInfo> getList(UserHealthInfo userHealthInfo);
}