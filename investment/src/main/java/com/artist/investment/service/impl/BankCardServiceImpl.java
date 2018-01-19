package com.artist.investment.service.impl;

import com.artist.investment.dao.BankCardMapper;
import com.artist.investment.domain.BankCard;
import com.artist.investment.service.BankCardService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-19 09:40:57.
 */
@Service
public class BankCardServiceImpl extends BaseServiceImpl<BankCard, Long> implements BankCardService {

    public BankCardMapper getActualDao() {
        return (BankCardMapper)getDao();
    }
}