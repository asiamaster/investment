package com.artist.investment.service.impl;

import com.artist.investment.dao.InvestmentPlatformMapper;
import com.artist.investment.domain.InvestmentPlatform;
import com.artist.investment.service.InvestmentPlatformService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-18 17:11:11.
 */
@Service
public class InvestmentPlatformServiceImpl extends BaseServiceImpl<InvestmentPlatform, Long> implements InvestmentPlatformService {

    public InvestmentPlatformMapper getActualDao() {
        return (InvestmentPlatformMapper)getDao();
    }
}