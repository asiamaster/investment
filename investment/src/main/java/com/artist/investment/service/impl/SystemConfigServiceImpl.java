package com.artist.investment.service.impl;

import com.artist.investment.dao.SystemConfigMapper;
import com.artist.investment.service.SystemConfigService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.sdk.domain.SystemConfig;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-12-05 14:23:24.
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, Long> implements SystemConfigService {

    public SystemConfigMapper getActualDao() {
        return (SystemConfigMapper)getDao();
    }
}