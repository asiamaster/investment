package com.artist.investment.service;

import com.artist.investment.domain.Investment;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
public interface InvestmentService extends BaseService<Investment, Long> {

    BaseOutput insertSelectiveWithOutput(Investment investment);

    BaseOutput updateSelectiveWithOutput(Investment investment);
}