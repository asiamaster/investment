package com.artist.investment.provider;

import com.artist.investment.domain.Investment;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目期限提供者
 */
@Component
public class ProjectDurationProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        buffer.add(new ValuePairImpl("天", "1"));
        buffer.add(new ValuePairImpl("周", "2"));
        buffer.add(new ValuePairImpl("月", "3"));
        buffer.add(new ValuePairImpl("季", "4"));
        buffer.add(new ValuePairImpl("年", "5"));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || obj.equals("")) return null;
        Investment investment = (Investment)metaMap.get(ROW_DATA_KEY);
        if(investment.getProjectDurationUnit() == null){
            return null;
        }
        //项目期限单位
        String unit = "";
        for(ValuePair<?> valuePair : buffer){
            if(investment.getProjectDurationUnit().toString().equals(valuePair.getValue())){
                unit = valuePair.getText();
                break;
            }
        }
        return obj+unit;
    }
}