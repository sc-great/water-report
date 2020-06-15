package com.boot.materialControl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.common.core.text.Convert;
import com.boot.materialControl.domain.ConsumableType;
import com.boot.materialControl.service.ConsumableTypeService;
import com.boot.materialControl.mapper.ConsumableTypeMapper;

@Service
public class ConsumableTypeServiceImpl implements ConsumableTypeService {

	@Autowired
	private ConsumableTypeMapper typeMapper;

	@Override
	public List<ConsumableType> getList(ConsumableType type) {
		return typeMapper.getList(type);
	}

	@Override
	public int add(ConsumableType type) {
		return typeMapper.insert(type);
	}

	@Override
	public ConsumableType getById(String id) {
		ConsumableType type = new ConsumableType();
		type.setId(id);
		return typeMapper.getEntity(type);
	}

	@Override
	public int update(ConsumableType type) {
		return typeMapper.update(type);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByIds(String ids) {
		int result = 0;
		if (!ids.isEmpty()) {
			ConsumableType type = new ConsumableType();
			type.getParams().put("ids", Convert.toStrArray(ids));
			result = typeMapper.delete(type);
		}
		return result;
	}
}
