package com.artist.investment.service.impl;

import com.artist.investment.dao.InvestmentMapper;
import com.artist.investment.domain.Investment;
import com.artist.investment.service.InvestmentService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
@Service
public class InvestmentServiceImpl extends BaseServiceImpl<Investment, Long> implements InvestmentService {

    public InvestmentMapper getActualDao() {
        return (InvestmentMapper)getDao();
    }
}