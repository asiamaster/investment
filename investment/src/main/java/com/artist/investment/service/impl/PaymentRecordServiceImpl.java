package com.artist.investment.service.impl;

import com.artist.investment.dao.PaymentRecordMapper;
import com.artist.investment.domain.PaymentRecord;
import com.artist.investment.service.PaymentRecordService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-30 09:35:46.
 */
@Service
public class PaymentRecordServiceImpl extends BaseServiceImpl<PaymentRecord, Long> implements PaymentRecordService {

    public PaymentRecordMapper getActualDao() {
        return (PaymentRecordMapper)getDao();
    }
}