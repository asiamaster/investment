package com.artist.investment.service;

import java.util.List;

import com.artist.investment.domain.DataDictionaryValue;
import com.artist.investment.domain.dto.DataDictionaryValueTreeView;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

public interface DataDictionaryValueService extends BaseService<DataDictionaryValue, Long> {

	BaseOutput<Object> insertAndGet(DataDictionaryValue dataDictionaryValue);

	List<DataDictionaryValueTreeView> listTree(Long ddId);
	
	String findChartServer();
}