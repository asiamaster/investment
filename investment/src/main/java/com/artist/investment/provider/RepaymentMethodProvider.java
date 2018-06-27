package com.artist.investment.provider;

import com.artist.investment.constant.RepaymentMethod;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiam on 2018/6/15 0015.
 */
@Component
public class RepaymentMethodProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        buffer.add(new ValuePairImpl(ValueProvider.EMPTY_ITEM_TEXT, null));
        for(RepaymentMethod repaymentMethod : RepaymentMethod.values()){
            buffer.add(new ValuePairImpl(repaymentMethod.getText(), repaymentMethod.getCode()));
        }
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || obj.equals("")) {
            return null;
        }
        return RepaymentMethod.getRepaymentMethodByCode((Integer) obj).getText();
    }
}