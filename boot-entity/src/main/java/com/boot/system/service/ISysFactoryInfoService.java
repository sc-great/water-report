package com.boot.system.service;

import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.system.domain.SysFactoryInfo;
import java.util.List;

/**
 * 水厂信息Service接口
 * 
 * @author boot
 * @date 2020-04-29
 */
public interface ISysFactoryInfoService {
	/**
	 * 新增水厂信息
	 *
	 * @param sysFactoryInfo 水厂信息
	 * @return 结果
	 */
	public int add(SysFactoryInfo sysFactoryInfo);

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
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteByIds(String ids);

	/**
	 * 查询水厂信息数量
	 *
	 * @param sysFactoryInfo 查询条件
	 * @return 水厂信息数量
	 */
	public int getCount(SysFactoryInfo sysFactoryInfo);

	/**手机端首页：水厂注册职人数或区域累计职工数
	 *
	 * @param sysFactoryInfo
	 * @return
	 */
	public int getCountByAppFactoryPersonNum(SysFactoryInfo sysFactoryInfo);

	/**
	 * 手机端累计设计处理能力
	 * @param sysFactoryInfo
	 * @return
	 */
	public Long getCountByApp(SysFactoryInfo sysFactoryInfo);

	/**
	 * 获取水厂信息实体对象
	 * 
	 * @param factId 水厂信息ID
	 * @return 水厂信息
	 */
	public SysFactoryInfo getEntityById(String factId);

	/**
	 * 获取水厂信息实体对象
	 * 
	 * @param orgId 水厂ID
	 * @return 水厂信息
	 */
	public SysFactoryInfo getEntityByOrgId(String orgId);

	public SysFactoryInfo getEntity(SysFactoryInfo sysFactoryInfo);

	/**
	 * 查询水厂信息列表
	 * 
	 * @param sysFactoryInfo 水厂信息
	 * @return 水厂信息集合
	 */
	public List<SysFactoryInfo> getList(SysFactoryInfo sysFactoryInfo);
	/**
	 *  
     * 水质（污水）预警指标是否合格
     * 合格返回1，不合格返回2 
	 * @param badWaterQualityInfo
	 * @return
	 */
   public String badWaterDictDataIsPass(BadWaterQualityInfo badWaterQualityInfo);
   /**
    * 水质（自来水/供水）预警指标是否合格
    */
   public String goodWaterDictDataIsPass(GoodWaterHealthInfo goodWaterHealthInfo);
}