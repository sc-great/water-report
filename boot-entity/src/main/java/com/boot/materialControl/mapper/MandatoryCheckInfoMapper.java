package com.boot.materialControl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.boot.materialControl.domain.MandatoryCheckInfo;

/**
 * 强制检测填报信息Mapper接口
 * 
 * @author yangxiaojun
 * @date 2020-04-22
 */
@Component
@Mapper
public interface MandatoryCheckInfoMapper {
	/**
	 * 新增强制检测数据信息(污水处理厂/排水厂)
	 *
	 * @param badWaterQualityInfo 强制检测数据信息(污水处理厂/排水厂)
	 * @return 结果
	 */
	public int insert(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 修改强制检测数据信息(污水处理厂/排水厂)
	 *
	 * @param badWaterQualityInfo 强制检测数据信息(污水处理厂/排水厂)
	 * @return 结果
	 */
	public int update(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 批量删除强制检测数据信息(污水处理厂/排水厂)
	 *
	 * @param badWaterQualityInfo 删除条件
	 * @return 结果
	 */
	public int delete(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 获取强制检测数据信息(污水处理厂/排水厂)数量
	 *
	 * @param badWaterQualityInfo 查询条件
	 * @return 结果
	 */
	public int getCount(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 获取强制检测数据信息(污水处理厂/排水厂)对象
	 * 
	 * @param badWaterQualityInfo 查询条件
	 * @return 强制检测数据信息(污水处理厂/排水厂)
	 */
	public MandatoryCheckInfo getEntity(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 查询强制检测数据信息(污水处理厂/排水厂)列表
	 * 
	 * @param badWaterQualityInfo 查询条件
	 * @return 强制检测数据信息(污水处理厂/排水厂)集合
	 */
	public List<MandatoryCheckInfo> getList(MandatoryCheckInfo mandatoryCheckInfo);
}