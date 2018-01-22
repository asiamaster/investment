package com.artist.investment.provider;

import com.artist.investment.domain.InvestmentPlatform;
import com.artist.investment.service.BankCardService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchSqlDisplayTextProviderAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 09:40:57.
 */
@Component
public class BankCardProvider extends BatchSqlDisplayTextProviderAdaptor {
    @Autowired
    BankCardService bankCardService;

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        Map<String, String> map = new HashMap<>();
        map.put(metaMap.get(FIELD_KEY).toString(), "card_number");
        return map;
    }

    @Override
    protected String getFkField(Map metaMap) {
        return "bankCardId";
    }

    @Override
    protected String getRelationTable(Map metaMap) {
        return "bank_card";
    }

    @Override
    protected String getRelationTablePkField(Map metaMap) {
        return "id";
    }
}