package com.artist.investment.provider;

import com.artist.investment.domain.Investment;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 距离退出天数提供者
 */
@Component
public class ExitDaysProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Investment investment = (Investment) metaMap.get(ROW_DATA_KEY);
        if (investment.getEndDate() == null) {
            return "";
        }
        return String.valueOf(DateUtils.differentDays(investment.getEndDate(), new Date()));
    }
}