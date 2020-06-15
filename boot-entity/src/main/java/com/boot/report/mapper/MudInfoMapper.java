package com.boot.report.mapper;

import com.boot.report.domain.MudInfo;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 水厂污泥数据信息Mapper接口
 * 
 * @author EPL
 * @date 2020-03-30
 */
@Component
@Mapper
public interface MudInfoMapper {
    /**
     * 新增水厂污泥数据信息
     *
     * @param mudInfo 水厂污泥数据信息
     * @return 结果
     */
    public int insert(MudInfo mudInfo);
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
     * @param mudInfo 删除条件
     * @return 结果
     */
    public int delete(MudInfo mudInfo);
    /**
     * 获取水厂污泥数据信息数量
     *
     * @param mudInfo 查询条件
     * @return 结果
     */
    public int getCount(MudInfo mudInfo);
    /**
     * 获取水厂污泥数据信息对象
     * 
     * @param mudInfo 查询条件
     * @return 水厂污泥数据信息
     */
    public MudInfo getEntity(MudInfo mudInfo);
    /**
     * 查询水厂污泥数据信息列表
     * 
     * @param mudInfo 查询条件
     * @return 水厂污泥数据信息集合
     */
    public List<MudInfo> getList(MudInfo mudInfo);
    public Map<String,Object> getSum(MudInfo mudInfo);
    
	public MudInfo getLatest(MudInfo mudInfo);
	public Map<String, Object> getSumForChart(MudInfo mudInfo);
	public Map<String, Object> getTodayForChart(MudInfo mudInfo);
	
	/**
	 * 手机端污泥情况：区域、公司、厂区所选页面日期以前的累计量统计
	 * @param mudInfo
	 * @return
	 */
	 public Map<String,Object> getSumByAppArea(MudInfo mudInfo);
}