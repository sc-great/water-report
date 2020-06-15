package com.boot.materialControl.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.common.utils.DateUtils;
import com.boot.materialControl.domain.Consumable;
import com.boot.materialControl.domain.ConsumableType;
import com.boot.materialControl.service.ConsumableService;
import com.boot.report.domain.AlarmInfo;
import com.boot.report.mapper.AlarmInfoMapper;
import com.boot.system.domain.SysUser;
import com.boot.materialControl.mapper.ConsumableMapper;
import com.boot.materialControl.mapper.ConsumableTypeMapper;

@Service
public class ConsumableServiceImpl implements ConsumableService {

	@Autowired
	private ConsumableMapper consumableMapper;
	@Autowired
	private ConsumableTypeMapper typeMapper;
	@Autowired
	private AlarmInfoMapper alarmInfoMapper;

	@Override
	public List<Consumable> getList(Consumable consumable) {
		return consumableMapper.getList(consumable);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int add(Consumable consumable, SysUser curUser) {
		String nowTime = DateUtils.getDateTime();
		// 如果没有添加过该耗材就直接添加，如果以前添加过，就查出来，整理数据，重新insert
		Consumable consumableOld = consumableMapper.getEntity(consumable);
		int total = consumable.getLastSetNum();
		int row = 0;
				
		if (consumableOld != null) {
			// 备份老数据
			consumableOld.setEffectIcon("2");
			consumableMapper.update(consumableOld);
			// 添加新数据
			consumableOld.setEffectIcon("1");
			total = consumableOld.getTotal() + consumable.getLastSetNum();
			consumableOld.setTotal(total);
			consumableOld.setLastSetTime(nowTime);
			consumableOld.setLastSetNum(consumable.getLastSetNum());
			consumableOld.setLastSetUserId(curUser.getUserId());
			consumableOld.setLastSetUserName(curUser.getUserName());
			consumableOld.setUpdateTime(nowTime);
			consumableOld.setUpdateUserId(curUser.getUserId());
			consumableOld.setUpdateBy(curUser.getUserName());
			row =  consumableMapper.insert(consumableOld);
			
			insertAlarmInfo(consumableOld);
		} else {
			consumable.setTotal(total);
			consumable.setLastSetTime(nowTime);
			consumable.setLastSetUserId(curUser.getUserId());
			consumable.setLastSetUserName(curUser.getUserName());
			consumable.setUpdateTime(nowTime);
			consumable.setUpdateUserId(curUser.getUserId());
			consumable.setUpdateBy(curUser.getUserName());
			row = consumableMapper.insert(consumable);
			
			if (total < 3)
				insertAlarmInfo(consumable);
		}
		return row;
	}
	
	@Override
	public Consumable getEntity(Consumable consumable) {
		return consumableMapper.getEntity(consumable);
	}

	@Override
	public Consumable getEntityById(String id) {
		Consumable consumable = new Consumable();
		consumable.setId(id);
		return consumableMapper.getEntity(consumable);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			// 报警数据删除
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.getParams().put("objIds", Convert.toStrArray(ids));
			alarmInfoMapper.delete(alarmInfo);
			// 备品备件删除
			Consumable consumable = new Consumable();
			consumable.getParams().put("ids", Convert.toStrArray(ids));
			List<Consumable> list = consumableMapper.getList(consumable);
			Consumable con = null;
			for (Consumable c : list) {
				con = new Consumable();
				con.setFactoryId(c.getFactoryId());
				con.setTypeId(c.getTypeId());
				result = consumableMapper.delete(con);
			}
		}
		return result;
	}

	@Override
	public int update(Consumable con) {
		int row = consumableMapper.update(con);
		if (con.getTotal() < 3)
			insertAlarmInfo(con);
		return row;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int get(Consumable consumable, SysUser curUser) {
		String nowTime = DateUtils.getDateTime();
		Consumable consumableOld = consumableMapper.getEntity(consumable);
		Consumable csb = new Consumable();
		csb.setTypeId(consumableOld.getTypeId());
		csb.setFactoryId(consumableOld.getFactoryId());
		consumableOld = consumableMapper.getEntity(csb);
		// 如果不够领用，返回 -1
		if (consumableOld.getTotal() < consumable.getLastGetNum()) {
			return -1;
		}
		// 备份老数据
		consumableOld.setEffectIcon("2");
		consumableMapper.update(consumableOld);
		// 添加新数据
		consumableOld.setEffectIcon("1");
		consumableOld.setTotal(consumableOld.getTotal() - consumable.getLastGetNum());
		consumableOld.setLastGetTime(nowTime);
		consumableOld.setLastGetNum(consumable.getLastGetNum());
		consumableOld.setLastGetUserName(consumable.getLastGetUserName());
		consumableOld.setUpdateTime(nowTime);
		consumableOld.setUpdateUserId(curUser.getUserId());
		consumableOld.setUpdateBy(curUser.getUserName());
		
		int row = consumableMapper.insert(consumableOld);
		// 根据总数判断报警
		if (consumableOld.getTotal() < 3)
			insertAlarmInfo(consumableOld);
		return row;
	}

	@Override
	public List<Map<String, Object>> getSum(Consumable consumable) {
		return consumableMapper.getSum(consumable);
	}
	
	/**
	 * 插入报警信息
	 * @param consumable
	 * @return
	 */
	public int insertAlarmInfo(Consumable consumable) {
		AlarmInfo alarm = new AlarmInfo();
		// 查备品备件类型，用于添加到报警信息的项目
		ConsumableType typeParam = new ConsumableType();
		typeParam.setId(consumable.getTypeId());
		ConsumableType type = typeMapper.getEntity(typeParam);
		String name = "";
		if (type != null) {
			name = type.getName();
			String model = type.getModel();
			name += (model == null || "".equals(model) || "".equals(model.trim()) ? "" : ("（" + model.trim() + "）"));
		} else {
			name = "名称丢失";
		}
		alarm.setAlarmItem(name);
		alarm.setOrgId(consumable.getFactoryId());
		alarm.setAlarmType("1");
		alarm.setEffectIcon("1");
		AlarmInfo alarmOld = alarmInfoMapper.getEntity(alarm);
		if (alarmOld != null) {
			alarmOld.setEffectIcon("2");
			alarmInfoMapper.update(alarmOld);
		}
		if (consumable.getTotal() >= 3) {
			return 0;
		}
		alarm.setAreaId(consumable.getAreaId());
		alarm.setValue(consumable.getTotal() + "");
		alarm.setFillDate(DateUtils.getDate());
		alarm.setFillTime(DateUtils.getDateTime());
		alarm.setObjId(consumable.getId());
		return alarmInfoMapper.insert(alarm);
	}
}
