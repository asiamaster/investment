package com.artist.investment.service;

import com.artist.investment.domain.dto.DataDictionaryDto;
import com.dili.ss.base.BaseService;
import com.dili.uap.sdk.domain.DataDictionary;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-07-12 10:41:18.
 */
public interface DataDictionaryService extends BaseService<DataDictionary, Long> {

	/**
	 * 根据DataDictionary编码查询数据字典和数据字典值的DTO
	 * @param code
	 * @return
	 */
	DataDictionaryDto findByCode(String code);
}