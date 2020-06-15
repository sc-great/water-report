package com.boot.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.common.utils.StringUtils;
import com.boot.report.domain.ReportDateInfo;
import com.boot.report.domain.TestBadWaterInfo;
import com.boot.report.mapper.ReportDateInfoMapper;
import com.boot.report.mapper.TestBadWaterInfoMapper;
import com.boot.report.service.ITestBadWaterInfoService;

/**
 * 化验数据信息(污水)Service业务层处理
 * 
 * @author yangxiaojun
 * @date 2020-04-22
 */
@Service
public class TestBadWaterInfoServiceImpl implements ITestBadWaterInfoService {
    @Autowired
    private TestBadWaterInfoMapper testBadWaterInfoMapper;
    @Autowired
	private ReportDateInfoMapper reportDateInfoMapper;
    /**
     * 新增数据信息(污水)
     *
     * @param TestBadWaterInfo  化验数据信息(污水)
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(TestBadWaterInfo testBadWaterInfo) {
        //若是二次填报，则把第一次填报的信息改为不可用标识
        Object iconObj = testBadWaterInfo.getParams().get("icon");
        if (StringUtils.isNotNullAndNotEmpty(iconObj) && ("2").equals(iconObj.toString())) {
            TestBadWaterInfo waterQualityParam = new TestBadWaterInfo();
            waterQualityParam.setFillDate(DateUtils.getDate());
            waterQualityParam.setFactoryId(testBadWaterInfo.getFactoryId());
            waterQualityParam.setEffectIcon("1");
            TestBadWaterInfo originTestBadWaterInfo = testBadWaterInfoMapper.getEntity(waterQualityParam);
            originTestBadWaterInfo.setEffectIcon("2");
            testBadWaterInfoMapper.update(originTestBadWaterInfo);
        }
        //添加新数据
        int insertRow = testBadWaterInfoMapper.insert(testBadWaterInfo);
        if (insertRow > 0) {
			ReportDateInfo reportDateInfoParam = new ReportDateInfo();
			reportDateInfoParam.setReportDate(DateUtils.getDate());
			reportDateInfoParam.setFactoryId(testBadWaterInfo.getFactoryId());
			int count = reportDateInfoMapper.getCount(reportDateInfoParam);
			if (count == 0) {
				reportDateInfoMapper.insert(reportDateInfoParam);
			}
		}
		return insertRow;
    }
    /**
     * 修改数据信息(污水)
     *
     * @param TestBadWaterInfo 数据信息(污水)
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TestBadWaterInfo testBadWaterInfo) {
        return testBadWaterInfoMapper.update(testBadWaterInfo);
    }
    /**
     * 删除数据信息(污水)对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(String ids) {
        int result=0;
        if(!ids.isEmpty()){
            TestBadWaterInfo testBadWaterInfo=new TestBadWaterInfo();
            testBadWaterInfo.getParams().put("ids",Convert.toStrArray(ids));
            result=testBadWaterInfoMapper.delete(testBadWaterInfo);
        }
        return result;
    }
    /**
    * 查询数据信息(污水)数量
    *
    * @param TestBadWaterInfo 查询条件
    * @return 数据信息(污水)数量
    */
    @Override
    public int getCount(TestBadWaterInfo testBadWaterInfo){
        return testBadWaterInfoMapper.getCount(testBadWaterInfo);
    }
    /**
     * 获取化验数据信息(污水)实体对象
     * 
     * @param id 化验数据信息(污水)ID
     * @return 化验数据信息(污水)
     */
    @Override
    public TestBadWaterInfo getEntityById(String id) {
        TestBadWaterInfo testBadWaterInfo=new TestBadWaterInfo();
        testBadWaterInfo.setId(id);
        return testBadWaterInfoMapper.getEntity(testBadWaterInfo);
    }
    @Override
    public TestBadWaterInfo getEntity(TestBadWaterInfo testBadWaterInfo){
        return testBadWaterInfoMapper.getEntity(testBadWaterInfo);
    }
    /**
     * 查询化验数据信息(污水)列表
     * 
     * @param TestBadWaterInfo 化验数据信息(污水)
     * @return 化验数据信息(污水)
     */
    @Override
        public List<TestBadWaterInfo> getList(TestBadWaterInfo testBadWaterInfo) {
        return testBadWaterInfoMapper.getList(testBadWaterInfo);
    }
}