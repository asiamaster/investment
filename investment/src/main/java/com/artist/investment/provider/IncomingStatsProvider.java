package com.artist.investment.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 待收合计提供者
 * Created by asiam on 2018/6/26 0026.
 */
@Component
public class IncomingStatsProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Map investmentStats = (Map) metaMap.get(ROW_DATA_KEY);
        if(investmentStats.get("arrived") == null){
            return investmentStats.get("principalAndInterest").toString();
        }
        return new BigDecimal(investmentStats.get("principalAndInterest").toString()).subtract(new BigDecimal(investmentStats.get("arrived").toString())).toPlainString();
    }
}