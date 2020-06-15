package com.boot.system.mapper;

import com.boot.system.domain.SysFactoryInfo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂信息Mapper接口
 * 
 * @author boot
 * @date 2020-04-29
 */
@Component
@Mapper
public interface SysFactoryInfoMapper {
	/**
	 * 新增水厂信息
	 *
	 * @param sysFactoryInfo 水厂信息
	 * @return 结果
	 */
	public int insert(SysFactoryInfo sysFactoryInfo);

	/**
	 * 修改水厂信息
	 *
	 * @param sysFactoryInfo 水厂信息
	 * @return 结果
	 */
	public int update(SysFactoryInfo sysFactoryInfo);

	/**
	 * 批量删除水厂信息
	 *
	 * @param sysFactoryInfo 删除条件
	 * @return 结果
	 */
	public int delete(SysFactoryInfo sysFactoryInfo);

	/**
	 * 获取水厂信息数量
	 *
	 * @param sysFactoryInfo 查询条件
	 * @return 结果
	 */
	public int getCount(SysFactoryInfo sysFactoryInfo);

	/**手机端首页：水厂注册职人数或区域累计职工数
	 *
	 * @param sysFactoryInfo
	 * @return
	 */
	public int getCountByAppFactoryPersonNum(SysFactoryInfo sysFactoryInfo);

	/**
	 *  手机端设计处理能力累计
	 * @param sysFactoryInfo
	 * @return
	 */
	public Long getCountByApp(SysFactoryInfo sysFactoryInfo);

	/**
	 * 获取水厂信息对象
	 * 
	 * @param sysFactoryInfo 查询条件
	 * @return 水厂信息
	 */
	public SysFactoryInfo getEntity(SysFactoryInfo sysFactoryInfo);

	/**
	 * 查询水厂信息列表
	 * 
	 * @param sysFactoryInfo 查询条件
	 * @return 水厂信息集合
	 */
	public List<SysFactoryInfo> getList(SysFactoryInfo sysFactoryInfo);
}