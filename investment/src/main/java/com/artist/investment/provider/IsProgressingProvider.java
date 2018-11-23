package com.artist.investment.provider;

import com.artist.investment.domain.Investment;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 是否投资中提供者
 */
@Component
public class IsProgressingProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        buffer.add(new ValuePairImpl("否", "0"));
        buffer.add(new ValuePairImpl("是", "1"));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Investment investment = (Investment)metaMap.get(ROW_DATA_KEY);
        if(investment.getStartDate() == null || investment.getEndDate() == null){
            return null;
        }
        Date now = new Date();
        return now.after(investment.getStartDate()) && now.before(investment.getEndDate()) ? "是" : "否";
    }
}