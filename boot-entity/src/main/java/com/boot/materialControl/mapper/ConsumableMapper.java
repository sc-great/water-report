package com.boot.materialControl.mapper;

import java.util.List;
import java.util.Map;

import com.boot.materialControl.domain.Consumable;

public interface ConsumableMapper {

	public List<Consumable> getList(Consumable consumable);

	public int insert(Consumable consumable);

	public Consumable getEntity(Consumable consumable);

	public int delete(Consumable consumable);

	public int update(Consumable con);

	public List<Map<String, Object>> getSum(Consumable consumable);
}
