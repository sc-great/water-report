package com.boot.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.report.domain.BadWaterQualityInfo;
import com.boot.report.domain.GoodWaterHealthInfo;
import com.boot.system.domain.SysFactoryInfo;
import com.boot.system.mapper.SysFactoryInfoMapper;
import com.boot.system.service.ISysFactoryInfoService;

/**
 * 水厂信息Service业务层处理
 * 
 * @author boot
 * @date 2020-04-29
 */
@Service
public class SysFactoryInfoServiceImpl implements ISysFactoryInfoService {
	@Autowired
	private SysFactoryInfoMapper sysFactoryInfoMapper;

	/**
	 * 新增水厂信息
	 *
	 * @param sysFactoryInfo
	 *            水厂信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(SysFactoryInfo sysFactoryInfo) {
		sysFactoryInfo.setCreateTime(DateUtils.getTime());
		return sysFactoryInfoMapper.insert(sysFactoryInfo);
	}

	/**
	 * 修改水厂信息
	 *
	 * @param sysFactoryInfo
	 *            水厂信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(SysFactoryInfo sysFactoryInfo) {
		sysFactoryInfo.setUpdateTime(DateUtils.getTime());
		return sysFactoryInfoMapper.update(sysFactoryInfo);
	}

	/**
	 * 删除水厂信息对象
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
			sysFactoryInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = sysFactoryInfoMapper.delete(sysFactoryInfo);
		}
		return result;
	}

	/**
	 * 查询水厂信息数量
	 *
	 * @param sysFactoryInfo
	 *            查询条件
	 * @return 水厂信息数量
	 */
	@Override
	public int getCount(SysFactoryInfo sysFactoryInfo) {
		return sysFactoryInfoMapper.getCount(sysFactoryInfo);
	}

	/**手机端首页：水厂注册职人数或区域累计职工数
	 *
	 * @param sysFactoryInfo
	 * @return
	 */
	@Override
	public int getCountByAppFactoryPersonNum(SysFactoryInfo sysFactoryInfo) {
		return sysFactoryInfoMapper.getCountByAppFactoryPersonNum(sysFactoryInfo);
	}

	/**
	* 手机端累计设计处理能力
	* @param sysFactoryInfo
	* @return
	*/
	@Override
	public Long getCountByApp(SysFactoryInfo sysFactoryInfo) {
		return sysFactoryInfoMapper.getCountByApp(sysFactoryInfo);
	}

	/**
	 * 获取水厂信息实体对象
	 * 
	 * @param factId
	 *            水厂信息ID
	 * @return 水厂信息
	 */
	@Override
	public SysFactoryInfo getEntityById(String factId) {
		SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
		sysFactoryInfo.setFactId(factId);
		return sysFactoryInfoMapper.getEntity(sysFactoryInfo);
	}

	@Override
	public SysFactoryInfo getEntity(SysFactoryInfo sysFactoryInfo) {
		return sysFactoryInfoMapper.getEntity(sysFactoryInfo);
	}

	/**
	 * 查询水厂信息列表
	 * 
	 * @param sysFactoryInfo
	 *            水厂信息
	 * @return 水厂信息
	 */
	@Override
	public List<SysFactoryInfo> getList(SysFactoryInfo sysFactoryInfo) {
		return sysFactoryInfoMapper.getList(sysFactoryInfo);
	}

	@Override
	public SysFactoryInfo getEntityByOrgId(String orgId) {
		SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
		sysFactoryInfo.setOrgId(orgId);
		return sysFactoryInfoMapper.getEntity(sysFactoryInfo);
	}

	/**
	* 水质（污水）预警指标是否合格
	* 合格返回1，不合格返回2 +指标
	* */
	@Override
	public String badWaterDictDataIsPass(BadWaterQualityInfo badWaterQualityInfo) {
		String returnValue = "1"; // 合格
		String tempStr = "2;"; // 不合格
        Double zero=new Double(0); //上下限都为0时表示没有配置预警值，不参与预警
		
		SysFactoryInfo sysFactoryInfoPara = new SysFactoryInfo();
		sysFactoryInfoPara.setOrgId(badWaterQualityInfo.getFactoryId());
		SysFactoryInfo sysFactoryInfoEntity = sysFactoryInfoMapper.getEntity(sysFactoryInfoPara);
		// 存在水厂信息，才能获取水厂自身的预警指标的上下限，小于下限或者大于上限都是不合格的数据，发出预警
		if (sysFactoryInfoEntity != null) {
			// COD_in
			if (badWaterQualityInfo.getCodIn() != null) {
				if (((badWaterQualityInfo.getCodIn()).compareTo(sysFactoryInfoEntity.getCodInMinBad()) == -1)
						|| ((badWaterQualityInfo.getCodIn()).compareTo(sysFactoryInfoEntity.getCodInMaxBad()) == 1)) {
					if (!((sysFactoryInfoEntity.getCodInMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getCodInMaxBad()).compareTo(zero)==0)){
					 tempStr = tempStr + "COD进水=" + badWaterQualityInfo.getCodIn() + ",";
					}
				}
			}
			// COD_out
			if (badWaterQualityInfo.getCodOut() != null) {
				if (((badWaterQualityInfo.getCodOut()).compareTo(sysFactoryInfoEntity.getCodOutMinBad()) == -1)
						|| ((badWaterQualityInfo.getCodOut()).compareTo(sysFactoryInfoEntity.getCodOutMaxBad()) == 1)) {
					if  (!((sysFactoryInfoEntity.getCodOutMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getCodOutMaxBad()).compareTo(zero)==0)){
					  tempStr = tempStr + "COD出水=" + badWaterQualityInfo.getCodOut() + ",";
					}
				}
			}
			// NH3_N_in
			if (badWaterQualityInfo.getNh3NIn() != null) {
				if (((badWaterQualityInfo.getNh3NIn()).compareTo(sysFactoryInfoEntity.getNh3NInMinBad()) == -1)
						|| ((badWaterQualityInfo.getNh3NIn()).compareTo(sysFactoryInfoEntity.getNh3NInMaxBad()) == 1)) {
					if  (!((sysFactoryInfoEntity.getNh3NInMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getNh3NInMaxBad()).compareTo(zero)==0)){
					  tempStr = tempStr + "NH3-N进水=" + badWaterQualityInfo.getNh3NIn() + ",";
					}					
				}
			}
			// NH3_N_out
			if (badWaterQualityInfo.getNh3NOut() != null) {
				if (((badWaterQualityInfo.getNh3NOut()).compareTo(sysFactoryInfoEntity.getNh3NOutMinBad()) == -1)
						|| ((badWaterQualityInfo.getNh3NOut()).compareTo(sysFactoryInfoEntity.getNh3NOutMaxBad()) == 1)) {
					if  (!((sysFactoryInfoEntity.getNh3NOutMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getNh3NOutMaxBad()).compareTo(zero)==0)){
					  tempStr = tempStr + "NH3-N出水=" + badWaterQualityInfo.getNh3NOut() + ",";
					}
					
				}
			}
			// SS_in
			if (badWaterQualityInfo.getSsIn() != null) {
				if (((badWaterQualityInfo.getSsIn()).compareTo(sysFactoryInfoEntity.getSsInMinBad()) == -1)
						|| ((badWaterQualityInfo.getSsIn()).compareTo(sysFactoryInfoEntity.getSsInMaxBad()) == 1)) {
					if  (!((sysFactoryInfoEntity.getSsInMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getSsInMaxBad()).compareTo(zero)==0)){
				    	tempStr = tempStr + "SS进水=" + badWaterQualityInfo.getSsIn() + ",";
					}
					
				}
			}
			// SS_out
			if (badWaterQualityInfo.getSsOut() != null) {
				if (((badWaterQualityInfo.getSsOut()).compareTo(sysFactoryInfoEntity.getSsOutMinBad()) == -1)
						|| ((badWaterQualityInfo.getSsOut()).compareTo(sysFactoryInfoEntity.getSsOutMaxBad()) == 1)) {
					if  (!((sysFactoryInfoEntity.getSsOutMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getSsOutMaxBad()).compareTo(zero)==0)){
					    tempStr = tempStr + "SS出水=" + badWaterQualityInfo.getSsOut() + ",";
					}
				}
			}
			// PH_in
			if (badWaterQualityInfo.getPhIn() != null) {
				if (((badWaterQualityInfo.getPhIn()).compareTo(sysFactoryInfoEntity.getPhInMinBad()) == -1)
						|| ((badWaterQualityInfo.getPhIn()).compareTo(sysFactoryInfoEntity.getPhInMaxBad()) == 1)) {
				
					if  (!((sysFactoryInfoEntity.getPhInMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getPhInMaxBad()).compareTo(zero)==0)){
					       tempStr = tempStr + "PH进水=" + badWaterQualityInfo.getPhIn() + ",";
					}
				}
			}
			// PH_out
			if (badWaterQualityInfo.getPhOut() != null) {
				if (((badWaterQualityInfo.getPhOut()).compareTo(sysFactoryInfoEntity.getPhOutMinBad()) == -1)
						|| ((badWaterQualityInfo.getPhOut()).compareTo(sysFactoryInfoEntity.getPhOutMaxBad()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getPhOutMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getPhOutMaxBad()).compareTo(zero)==0)){
					   tempStr = tempStr + "PH出水=" + badWaterQualityInfo.getPhOut() + ",";
					}	
				}
			}
			// TP_in
			if (badWaterQualityInfo.getTpIn() != null) {
				if (((badWaterQualityInfo.getTpIn()).compareTo(sysFactoryInfoEntity.getTpInMinBad()) == -1)
						|| ((badWaterQualityInfo.getTpIn()).compareTo(sysFactoryInfoEntity.getTpInMaxBad()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getTpInMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getTpInMaxBad()).compareTo(zero)==0)){
					      tempStr = tempStr + "TP进水=" + badWaterQualityInfo.getTpIn() + ",";
					}
				}
			}
			// TP_out
			if (badWaterQualityInfo.getTpOut() != null) {
				if (((badWaterQualityInfo.getTpOut()).compareTo(sysFactoryInfoEntity.getTpOutMinBad()) == -1)
						|| ((badWaterQualityInfo.getTpOut()).compareTo(sysFactoryInfoEntity.getTpOutMaxBad()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getTpOutMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getTpOutMaxBad()).compareTo(zero)==0)){
					      tempStr = tempStr + "TP出水=" + badWaterQualityInfo.getTpOut() + ",";
					}
				}
			}
			// TN_in
			if (badWaterQualityInfo.getTnIn() != null) {
				if (((badWaterQualityInfo.getTnIn()).compareTo(sysFactoryInfoEntity.getTnInMinBad()) == -1)
						|| ((badWaterQualityInfo.getTnIn()).compareTo(sysFactoryInfoEntity.getTnInMaxBad()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getTnInMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getTnInMaxBad()).compareTo(zero)==0)){
					      tempStr = tempStr + "TN进水=" + badWaterQualityInfo.getTnIn() + ",";
					}
				}
			}
			// TN_out
			if (badWaterQualityInfo.getTnOut() != null) {
				if (((badWaterQualityInfo.getTnOut()).compareTo(sysFactoryInfoEntity.getTnOutMinBad()) == -1)
						|| ((badWaterQualityInfo.getTnOut()).compareTo(sysFactoryInfoEntity.getTnOutMaxBad()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getTnOutMinBad()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getTnOutMaxBad()).compareTo(zero)==0)){
					    tempStr = tempStr + "TN出水=" + badWaterQualityInfo.getTnOut() + ",";
					}
				}
			}
		}
		if (tempStr.indexOf(",") != -1) {
			returnValue = tempStr.substring(0, tempStr.length() - 1);
		}
		return returnValue;
	}

	/**
	 * 水质（自来水/供水）
	 * 合格返回1，不合格返回2+指标
	 *  */
	@Override
	public String goodWaterDictDataIsPass(GoodWaterHealthInfo goodWaterHealthInfo) {
		String returnValue = "1"; // 合格
		String tempStr = "2;"; // 不合格
		Double zero=new Double(0); //上下限都为0时表示没有配置预警值，不参与预警

		SysFactoryInfo sysFactoryInfoPara = new SysFactoryInfo();
		sysFactoryInfoPara.setOrgId(goodWaterHealthInfo.getFactoryId());
		SysFactoryInfo sysFactoryInfoEntity = sysFactoryInfoMapper.getEntity(sysFactoryInfoPara);
		if (sysFactoryInfoEntity != null) {
			// PH值进水
			if (goodWaterHealthInfo.getPhIn() != null) {
				if (((goodWaterHealthInfo.getPhIn()).compareTo(sysFactoryInfoEntity.getPhInMinGood()) == -1)
						|| ((goodWaterHealthInfo.getPhIn()).compareTo(sysFactoryInfoEntity.getPhInMaxGood()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getPhInMinGood()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getPhInMaxGood()).compareTo(zero)==0)){
					    tempStr = tempStr + "PH进水=" + goodWaterHealthInfo.getPhIn() + ",";
					}
					
				}
			}
			// PH值出水
			if (goodWaterHealthInfo.getPhOut() != null) {
				if (((goodWaterHealthInfo.getPhOut()).compareTo(sysFactoryInfoEntity.getPhOutMinGood()) == -1)
						|| ((goodWaterHealthInfo.getPhOut()).compareTo(sysFactoryInfoEntity.getPhOutMaxGood()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getPhOutMinGood()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getPhOutMaxGood()).compareTo(zero)==0)){
					      tempStr = tempStr + "PH出水=" + goodWaterHealthInfo.getPhOut() + ",";
					}
				}
			}
			// COL2出水
			if (goodWaterHealthInfo.getCol2Out() != null) {
				if (((goodWaterHealthInfo.getCol2Out()).compareTo(sysFactoryInfoEntity.getCol2OutMinGood()) == -1)
						|| ((goodWaterHealthInfo.getCol2Out()).compareTo(sysFactoryInfoEntity.getCol2OutMaxGood()) == 1)) {
					if  (!((sysFactoryInfoEntity.getCol2OutMinGood()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getCol2OutMaxGood()).compareTo(zero)==0)){
					     tempStr = tempStr + "COL2出水=" + goodWaterHealthInfo.getCol2Out() + ",";
					}
				}
			}
			// 出水细菌总数
			if (goodWaterHealthInfo.getGermOut() != null) {
				if (((goodWaterHealthInfo.getGermOut()).compareTo(sysFactoryInfoEntity.getGermOutMinGood()) == -1)
						|| ((goodWaterHealthInfo.getGermOut()).compareTo(sysFactoryInfoEntity.getGermOutMaxGood()) == 1)) {
					
					if  (!((sysFactoryInfoEntity.getGermOutMinGood()).compareTo(zero)==0
							&&(sysFactoryInfoEntity.getGermOutMaxGood()).compareTo(zero)==0)){
					     tempStr = tempStr + "COL2出水=" + goodWaterHealthInfo.getGermOut() + ",";
					}
				}
			}
		}
		if (tempStr.indexOf(",") != -1) {
			returnValue = tempStr.substring(0, tempStr.length() - 1);
		}
		return returnValue;
	}
}
