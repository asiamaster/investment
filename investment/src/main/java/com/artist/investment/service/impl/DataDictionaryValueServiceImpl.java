package com.artist.investment.service.impl;

import com.artist.investment.dao.DataDictionaryValueMapper;
import com.artist.investment.service.DataDictionaryService;
import com.artist.investment.service.DataDictionaryValueService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataDictionaryValueServiceImpl extends BaseServiceImpl<DataDictionaryValue, Long> implements DataDictionaryValueService {

	@Autowired
	DataDictionaryService dataDictionaryService;

	public DataDictionaryValueMapper getActualDao() {
		return (DataDictionaryValueMapper) getDao();
	}



}