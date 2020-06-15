package com.boot.materialControl.service;

import java.util.List;

import com.boot.materialControl.domain.MandatoryCheckInfo;

/**
 * 化验数据信息Service接口
 * 
 * @author EPL
 * @date 2020-03-24
 */
public interface IMandatoryCheckInfoService {
	/**
	 * 新增化验水质数据信息
	 *
	 * @param testBadWaterInfo
	 * @return 结果
	 */
	public int add(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 修改化验数据信息(污水处理厂/排水厂)
	 *
	 * @param testBadWaterInfo 水厂水质数据信息(污水处理厂/排水厂)
	 * @return 结果
	 */
	public int update(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 批量删除化验数据信息(污水处理厂/排水厂)
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询化验数据信息(污水处理厂/排水厂)数量
	 *
	 * @param testBadWaterInfo 查询条件
	 * @return 化验数据信息(污水处理厂/排水厂)数量
	 */
	public int getCount(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 获取化验数据信息(污水处理厂/排水厂)实体对象
	 * 
	 * @param id 化验数据信息(污水处理厂/排水厂)ID
	 * @return 化验数据信息(污水处理厂/排水厂)
	 */
	public MandatoryCheckInfo getEntityById(String id);

	public MandatoryCheckInfo getEntity(MandatoryCheckInfo mandatoryCheckInfo);

	/**
	 * 查询化验数据信息(污水处理厂/排水厂)列表
	 * 
	 * @param testBadWaterInfo 化验数据信息(污水处理厂/排水厂)
	 * @return 化验数据信息(污水处理厂/排水厂)集合
	 */
	public List<MandatoryCheckInfo> getList(MandatoryCheckInfo mandatoryCheckInfo);

	public int doDispose(MandatoryCheckInfo mandatoryCheckInfo);
}