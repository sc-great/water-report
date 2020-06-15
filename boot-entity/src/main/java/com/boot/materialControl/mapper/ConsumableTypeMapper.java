package com.boot.materialControl.mapper;

import java.util.List;

import com.boot.materialControl.domain.ConsumableType;

public interface ConsumableTypeMapper {

	public List<ConsumableType> getList(ConsumableType type);

	public int insert(ConsumableType type);
	
	public ConsumableType getEntity(ConsumableType type);
	
	public ConsumableType getById(String id);
	
	public int update(ConsumableType type);
	
	public int delete(ConsumableType type);
}
