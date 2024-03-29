package com.boot.materialControl.service;

import java.util.List;

import com.boot.materialControl.domain.ConsumableType;

public interface ConsumableTypeService {

	public List<ConsumableType> getList(ConsumableType type);

	public int add(ConsumableType type);

	public ConsumableType getById(String id);

	public int update(ConsumableType type);

	public int deleteByIds(String ids);

}
