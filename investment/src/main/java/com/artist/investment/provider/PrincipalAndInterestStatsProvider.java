package com.artist.investment.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 本息合计统计提供者
 * Created by asiam on 2018/2/9 0009.
 */
@Component
public class PrincipalAndInterestStatsProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Map investmentStats = (Map) metaMap.get(ROW_DATA_KEY);
        if(investmentStats.get("principalAndInterest") == null){
            return new BigDecimal(investmentStats.get("investment").toString()).add(new BigDecimal(investmentStats.get("currentProfit").toString())).toPlainString();
        }else{
            return investmentStats.get("principalAndInterest").toString();
        }
    }
}