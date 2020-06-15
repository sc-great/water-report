package com.boot.report.mapper;

import com.boot.report.domain.HrHealthInfo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂人力资源健康情况信息Mapper接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Component
@Mapper
public interface HrHealthInfoMapper {
	/**
	 * 新增水厂人力资源健康情况信息
	 *
	 * @param hrHealthInfo 水厂人力资源健康情况信息
	 * @return 结果
	 */
	public int insert(HrHealthInfo hrHealthInfo);

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
	 * @param hrHealthInfo 删除条件
	 * @return 结果
	 */
	public int delete(HrHealthInfo hrHealthInfo);

	/**
	 * 获取水厂人力资源健康情况信息数量
	 *
	 * @param hrHealthInfo 查询条件
	 * @return 结果
	 */
	public int getCount(HrHealthInfo hrHealthInfo);

	/**
	 * 获取水厂人力资源健康情况信息对象
	 * 
	 * @param hrHealthInfo 查询条件
	 * @return 水厂人力资源健康情况信息
	 */
	public HrHealthInfo getEntity(HrHealthInfo hrHealthInfo);

	/**
	 * 查询水厂人力资源健康情况信息列表
	 * 
	 * @param hrHealthInfo 查询条件
	 * @return 水厂人力资源健康情况信息集合
	 */
	public List<HrHealthInfo> getList(HrHealthInfo hrHealthInfo);
}