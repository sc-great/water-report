package com.boot.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.report.mapper.UserHealthInfoMapper;
import com.boot.report.domain.UserHealthInfo;
import com.boot.report.service.IUserHealthInfoService;
import com.boot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工健康信息Service业务层处理
 * 
 * @author EPL
 * @date 2020-03-24
 */
@Service
public class UserHealthInfoServiceImpl implements IUserHealthInfoService {
	@Autowired
	private UserHealthInfoMapper userHealthInfoMapper;

	/**
	 * 新增员工健康信息
	 *
	 * @param userHealthInfo 员工健康信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(UserHealthInfo userHealthInfo) {
		return userHealthInfoMapper.insert(userHealthInfo);
	}

	/**
	 * 修改员工健康信息
	 *
	 * @param userHealthInfo 员工健康信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(UserHealthInfo userHealthInfo) {
		return userHealthInfoMapper.update(userHealthInfo);
	}

	@Override
	public int updateByWhere(UserHealthInfo userHealthInfo) {
		return userHealthInfoMapper.updateByWhere(userHealthInfo);
	}

	/**
	 * 删除员工健康信息对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			UserHealthInfo userHealthInfo = new UserHealthInfo();
			userHealthInfo.getParams().put("ids", Convert.toStrArray(ids));
			result = userHealthInfoMapper.delete(userHealthInfo);
		}
		return result;
	}

	/**
	* 查询员工健康信息数量
	*
	* @param userHealthInfo 查询条件
	* @return 员工健康信息数量
	*/
	@Override
	public int getCount(UserHealthInfo userHealthInfo) {
		return userHealthInfoMapper.getCount(userHealthInfo);
	}

	/**
	 * 获取员工健康信息实体对象
	 * 
	 * @param id 员工健康信息ID
	 * @return 员工健康信息
	 */
	@Override
	public UserHealthInfo getEntityById(String id) {
		UserHealthInfo userHealthInfo = new UserHealthInfo();
		userHealthInfo.setId(id);
		return userHealthInfoMapper.getEntity(userHealthInfo);
	}

	/**
	 * 查询员工健康信息列表
	 * 
	 * @param userHealthInfo 员工健康信息
	 * @return 员工健康信息
	 */
	@Override
	public List<UserHealthInfo> getList(UserHealthInfo userHealthInfo) {
		return userHealthInfoMapper.getList(userHealthInfo);
	}
}