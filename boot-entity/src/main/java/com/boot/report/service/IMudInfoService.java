package com.boot.report.service;

import com.boot.report.domain.MudInfo;
import java.util.List;
import java.util.Map;

/**
 * 水厂污泥数据信息Service接口
 * 
 * @author EPL
 * @date 2020-03-30
 */
public interface IMudInfoService {
	/**
	 * 新增水厂污泥数据信息
	 *
	 * @param mudInfo 水厂污泥数据信息
	 * @return 结果
	 */
	public int add(MudInfo mudInfo);

	/**
	 * 修改水厂污泥数据信息
	 *
	 * @param mudInfo 水厂污泥数据信息
	 * @return 结果
	 */
	public int update(MudInfo mudInfo);

	/**
	 * 批量删除水厂污泥数据信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询水厂污泥数据信息数量
	 *
	 * @param mudInfo 查询条件
	 * @return 水厂污泥数据信息数量
	 */
	public int getCount(MudInfo mudInfo);

	/**
	 * 获取水厂污泥数据信息实体对象
	 * 
	 * @param id 水厂污泥数据信息ID
	 * @return 水厂污泥数据信息
	 */
	public MudInfo getEntityById(String id);

	/**
	 * 查询水厂污泥数据信息列表
	 * 
	 * @param mudInfo 水厂污泥数据信息
	 * @return 水厂污泥数据信息集合
	 */
	public List<MudInfo> getList(MudInfo mudInfo);

	public Map<String, Object> getSum(MudInfo mudInfo);

	/**
	 * 查昨天的最后一条有用数据，用于累加
	 * @param mudInfo
	 * @return
	 */
	public MudInfo getLatest(MudInfo mudInfo);

	public Map<String, Object> getSumForChart(MudInfo mudInfo);

	public Map<String, Object> getTodayForChart(MudInfo mudInfo);

	public Map<String, Object> getSumByAppArea(MudInfo mudInfo);

	public MudInfo getEntity(MudInfo mudInfo);
}