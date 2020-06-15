package com.boot.materialControl.service;

import java.util.List;
import java.util.Map;

import com.boot.materialControl.domain.Consumable;
import com.boot.system.domain.SysUser;

public interface ConsumableService {

	public List<Consumable> getList(Consumable consumable);

	public int add(Consumable consumable, SysUser curUser);

	public Consumable getEntityById(String id);

	public int deleteByIds(String ids);

	public Consumable getEntity(Consumable consumable);

	public int update(Consumable con);

	public int get(Consumable consumable, SysUser curUser);

	public List<Map<String, Object>> getSum(Consumable consumable);
}
