package com.artist.investment.provider;

import com.artist.investment.constant.PaymentType;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-30 09:35:46.
 */
@Component
public class PaymentTypeProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        for(PaymentType paymentType : PaymentType.values()){
            buffer.add(new ValuePairImpl(paymentType.getText(), paymentType.getCode()));
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
        for(ValuePair<?> valuePair : buffer){
            if(obj.equals(valuePair.getValue())){
                return valuePair.getText();
            }
        }
        return null;
    }
}