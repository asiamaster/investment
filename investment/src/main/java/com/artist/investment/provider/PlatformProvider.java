package com.artist.investment.provider;

import com.artist.investment.domain.InvestmentPlatform;
import com.artist.investment.service.InvestmentPlatformService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.ss.metadata.provider.BatchSqlDisplayTextProviderAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-19 09:40:57.
 */
@Component
public class PlatformProvider extends BatchSqlDisplayTextProviderAdaptor {
    @Autowired
    InvestmentPlatformService investmentPlatformService;
    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        List<InvestmentPlatform> investmentPlatforms = investmentPlatformService.list(null);
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>(investmentPlatforms.size());
        buffer.add(new ValuePairImpl<>(EMPTY_ITEM_TEXT, null));
        for(InvestmentPlatform investmentPlatform : investmentPlatforms){
            buffer.add(new ValuePairImpl(investmentPlatform.getName(), investmentPlatform.getId()));
        }
        return buffer;
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        Map<String, String> map = new HashMap<>();
        map.put(metaMap.get(FIELD_KEY).toString(), "name");
        return map;
    }

    @Override
    protected String getFkField(Map metaMap) {
        return "platformId";
    }

    @Override
    protected String getRelationTable(Map metaMap) {
        return "investment_platform";
    }

    @Override
    protected String getRelationTablePkField(Map metaMap) {
        return "id";
    }
}