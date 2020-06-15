package com.boot.report.service.impl;

import java.util.List;
import java.util.Map;

import com.boot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.report.mapper.MudInfoMapper;
import com.boot.report.mapper.ReportDateInfoMapper;
import com.boot.report.domain.MudInfo;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.service.IMudInfoService;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 水厂污泥数据信息Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-30
 */
@Service
public class MudInfoServiceImpl implements IMudInfoService {
	@Autowired
	private MudInfoMapper mudInfoMapper;
	@Autowired
	private ReportDateInfoMapper reportDateInfoMapper;
	/**
	 * 新增水厂污泥数据信息
	 *
	 * @param mudInfo 水厂污泥数据信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(MudInfo mudInfo) {
		// 若是二次填报，则把第一次填报的信息改为不可用标识
		MudInfo mudInfo1Param = new MudInfo();
		mudInfo1Param.setFactoryId(mudInfo.getFactoryId());
		mudInfo1Param.setFillDate(DateUtils.getDate());
		mudInfo1Param.setEffectIcon("1");
		MudInfo originMudInfo = mudInfoMapper.getEntity(mudInfo1Param);
		if (originMudInfo != null) {
			originMudInfo.setEffectIcon("2");
			mudInfoMapper.update(originMudInfo);
		}
		int insertRow = mudInfoMapper.insert(mudInfo);
		
		if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(DateUtils.getDate());
			reportDateInfoParam.setFactoryId(mudInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;
	}

	/**
	 * 修改水厂污泥数据信息
	 *
	 * @param mudInfo 水厂污泥数据信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(MudInfo mudInfo) {
		return mudInfoMapper.update(mudInfo);
	}

	/**
	 * 删除水厂污泥数据信息对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			MudInfo mudInfo = new MudInfo();
			mudInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = mudInfoMapper.delete(mudInfo);
		}
		return result;
	}

	/**
	* 查询水厂污泥数据信息数量
	*
	* @param mudInfo 查询条件
	* @return 水厂污泥数据信息数量
	*/
	@Override
	public int getCount(MudInfo mudInfo) {
		return mudInfoMapper.getCount(mudInfo);
	}

	/**
	 * 获取水厂污泥数据信息实体对象
	 * 
	 * @param id 水厂污泥数据信息ID
	 * @return 水厂污泥数据信息
	 */
	@Override
	public MudInfo getEntityById(String id) {
		MudInfo mudInfo = new MudInfo();
		mudInfo.setId(id);
		return mudInfoMapper.getEntity(mudInfo);
	}

	/**
	 * 查询水厂污泥数据信息列表
	 * 
	 * @param mudInfo 水厂污泥数据信息
	 * @return 水厂污泥数据信息
	 */
	@Override
	public List<MudInfo> getList(MudInfo mudInfo) {
		return mudInfoMapper.getList(mudInfo);
	}

	@Override
	public Map<String, Object> getSum(MudInfo mudInfo) {
		return mudInfoMapper.getSum(mudInfo);
	}

	@Override
	public MudInfo getLatest(MudInfo mudInfo) {
		return mudInfoMapper.getLatest(mudInfo);
	}

	@Override
	public Map<String, Object> getSumForChart(MudInfo mudInfo) {
		return mudInfoMapper.getSumForChart(mudInfo);
	}

	@Override
	public Map<String, Object> getTodayForChart(MudInfo mudInfo) {
		return mudInfoMapper.getTodayForChart(mudInfo);
	}

	/**
	 * 手机端污泥情况：区域、公司、厂区所选页面日期以前的累计量统计
	 */
	@Override
	public Map<String, Object> getSumByAppArea(MudInfo mudInfo) {
		return mudInfoMapper.getSumByAppArea(mudInfo);
	}

	@Override
	public MudInfo getEntity(MudInfo mudInfo) {
		return mudInfoMapper.getEntity(mudInfo);
	}

}