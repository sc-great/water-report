package com.boot.report.mapper;

import com.boot.report.domain.GoodWaterHealthInfo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂水质数据信息（自来水厂/给水厂）Mapper接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Component
@Mapper
public interface GoodWaterHealthInfoMapper {
	/**
	 * 新增水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 结果
	 */
	public int insert(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 修改水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 结果
	 */
	public int update(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 批量删除水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 删除条件
	 * @return 结果
	 */
	public int delete(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 获取水厂水质数据信息（自来水厂/给水厂）数量
	 *
	 * @param goodWaterHealthInfo 查询条件
	 * @return 结果
	 */
	public int getCount(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 获取水厂水质数据信息（自来水厂/给水厂）对象
	 * 
	 * @param goodWaterHealthInfo 查询条件
	 * @return 水厂水质数据信息（自来水厂/给水厂）
	 */
	public GoodWaterHealthInfo getEntity(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 查询水厂水质数据信息（自来水厂/给水厂）列表
	 * 
	 * @param goodWaterHealthInfo 查询条件
	 * @return 水厂水质数据信息（自来水厂/给水厂）集合
	 */
	public List<GoodWaterHealthInfo> getList(GoodWaterHealthInfo goodWaterHealthInfo);

	public int getOverNorm(GoodWaterHealthInfo goodWaterHealthInfo);
}