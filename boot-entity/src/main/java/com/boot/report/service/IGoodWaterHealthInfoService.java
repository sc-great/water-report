package com.boot.report.service;

import com.boot.report.domain.GoodWaterHealthInfo;
import java.util.List;

/**
 * 水厂水质数据信息（自来水厂/给水厂）Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface IGoodWaterHealthInfoService {
	/**
	 * 新增水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 结果
	 */
	public int add(GoodWaterHealthInfo goodWaterHealthInfo,String alarmNote);

	/**
	 * 修改水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 结果
	 */
	public int update(GoodWaterHealthInfo goodWaterHealthInfo,String alarmNote);

	/**
	 * 批量删除水厂水质数据信息（自来水厂/给水厂）
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询水厂水质数据信息（自来水厂/给水厂）数量
	 *
	 * @param goodWaterHealthInfo 查询条件
	 * @return 水厂水质数据信息（自来水厂/给水厂）数量
	 */
	public int getCount(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 获取水厂水质数据信息（自来水厂/给水厂）实体对象
	 * 
	 * @param id 水厂水质数据信息（自来水厂/给水厂）ID
	 * @return 水厂水质数据信息（自来水厂/给水厂）
	 */
	public GoodWaterHealthInfo getEntityById(String id);

	public GoodWaterHealthInfo getEntity(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 查询水厂水质数据信息（自来水厂/给水厂）列表
	 * 
	 * @param goodWaterHealthInfo 水厂水质数据信息（自来水厂/给水厂）
	 * @return 水厂水质数据信息（自来水厂/给水厂）集合
	 */
	public List<GoodWaterHealthInfo> getList(GoodWaterHealthInfo goodWaterHealthInfo);

	/**
	 * 查询超标天数
	 * @param goodWaterHealthInfo
	 * @return
	 */
	public int getOverNorm(GoodWaterHealthInfo goodWaterHealthInfo);
}