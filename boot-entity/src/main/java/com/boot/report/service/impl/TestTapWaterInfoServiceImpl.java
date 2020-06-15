package com.boot.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.domain.TestTapWaterInfo;
import com.boot.report.mapper.ReportDateInfoMapper;
import com.boot.report.mapper.TestTapWaterInfoMapper;
import com.boot.report.service.ITestTapWaterInfoService;

/**
 * 水厂化验数据信息（自来水厂/给水厂）Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Service
public class TestTapWaterInfoServiceImpl implements ITestTapWaterInfoService {
    @Autowired
    private TestTapWaterInfoMapper testTapWaterInfoMapper;
    @Autowired
  	private ReportDateInfoMapper reportDateInfoMapper;

    /**
     * 新增水厂化验数据信息（自来水厂/给水厂）
     *
     * @param goodWaterHealthInfo 水厂化验数据信息（自来水厂/给水厂）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(TestTapWaterInfo testTapWaterInfo) {
        //若是二次填报，则把第一次填报的信息改为不可用标识
        Object iconObj = testTapWaterInfo.getParams().get("icon");
        if (StringUtils.isNotNullAndNotEmpty(iconObj) && ("2").equals(iconObj.toString())) {
        	TestTapWaterInfo TestInfo = new TestTapWaterInfo();
        	TestInfo.setFillDate(DateUtils.getDate());
        	TestInfo.setFactoryId(testTapWaterInfo.getFactoryId());
        	TestInfo.setEffectIcon("1");
        	TestTapWaterInfo originWaterInfo = testTapWaterInfoMapper.getEntity(TestInfo);
        	originWaterInfo.setEffectIcon("2");
        	testTapWaterInfoMapper.update(originWaterInfo);
        }
        //添加新数据
        int insertRow =  testTapWaterInfoMapper.insert(testTapWaterInfo);
        if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(DateUtils.getDate());
			reportDateInfoParam.setFactoryId(testTapWaterInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;
    }
    /**
     * 修改水厂化验数据信息（自来水厂/给水厂）
     *
     * @param goodWaterHealthInfo 水厂化验数据信息（自来水厂/给水厂）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TestTapWaterInfo testTapWaterInfo) {
        return testTapWaterInfoMapper.update(testTapWaterInfo);
    }
    /**
     * 删除水厂化验数据信息（自来水厂/给水厂）对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(String ids) {
        int result=0;
        if(!ids.isEmpty()){
        	TestTapWaterInfo testInfo=new TestTapWaterInfo();
        	testInfo.getParams().put("ids",Convert.toStrArray(ids));
            result=testTapWaterInfoMapper.delete(testInfo);
        }
        return result;
    }
    /**
    * 查询水厂化验数据信息（自来水厂/给水厂）数量
    *
    * @param goodWaterHealthInfo 查询条件
    * @return 水厂化验数据信息（自来水厂/给水厂）数量
    */
    @Override
    public int getCount(TestTapWaterInfo testTapWaterInfo){
        return testTapWaterInfoMapper.getCount(testTapWaterInfo);
    }
    /**
     * 获取水厂化验数据信息（自来水厂/给水厂）实体对象
     * 
     * @param id 水厂化验数据信息（自来水厂/给水厂）ID
     * @return 水厂化验数据信息（自来水厂/给水厂）
     */
    @Override
    public TestTapWaterInfo getEntityById(String id) {
    	TestTapWaterInfo testInfo=new TestTapWaterInfo();
    	testInfo.setId(id);
        return testTapWaterInfoMapper.getEntity(testInfo);
    }
    @Override
    public TestTapWaterInfo getEntity(TestTapWaterInfo testTapWaterInfo){
        return testTapWaterInfoMapper.getEntity(testTapWaterInfo);
    }
    /**
     * 查询水厂化验数据信息（自来水厂/给水厂）列表
     * 
     * @param goodWaterHealthInfo 水厂化验数据信息（自来水厂/给水厂）
     * @return 水厂化验数据信息（自来水厂/给水厂）
     */
    @Override
        public List<TestTapWaterInfo> getList(TestTapWaterInfo testTapWaterInfo) {
        return testTapWaterInfoMapper.getList(testTapWaterInfo);
    }
}