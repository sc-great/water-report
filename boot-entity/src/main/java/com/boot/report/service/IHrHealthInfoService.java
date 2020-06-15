package com.boot.report.service;

import com.boot.report.domain.HrHealthInfo;
import java.util.List;

/**
 * 水厂人力资源健康情况信息Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface IHrHealthInfoService {
	/**
	 * 新增水厂人力资源健康情况信息
	 *
	 * @param hrHealthInfo 水厂人力资源健康情况信息
	 * @return 结果
	 */
	public int add(HrHealthInfo hrHealthInfo);

	/**
	 * 修改水厂人力资源健康情况信息
	 *
	 * @param hrHealthInfo 水厂人力资源健康情况信息
	 * @return 结果
	 */
	public int update(HrHealthInfo hrHealthInfo);

	/**
	 * 批量删除水厂人力资源健康情况信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询水厂人力资源健康情况信息数量
	 *
	 * @param hrHealthInfo 查询条件
	 * @return 水厂人力资源健康情况信息数量
	 */
	public int getCount(HrHealthInfo hrHealthInfo);

	/**
	 * 获取水厂人力资源健康情况信息实体对象
	 * 
	 * @param id 水厂人力资源健康情况信息ID
	 * @return 水厂人力资源健康情况信息
	 */
	public HrHealthInfo getEntityById(String id);

	/**
	 * 查询水厂人力资源健康情况信息列表
	 * 
	 * @param hrHealthInfo 水厂人力资源健康情况信息
	 * @return 水厂人力资源健康情况信息集合
	 */
	public List<HrHealthInfo> getList(HrHealthInfo hrHealthInfo);
}