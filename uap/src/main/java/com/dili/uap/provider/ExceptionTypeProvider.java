package com.dili.uap.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.glossary.ExceptionType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by asiamaster on 2017/5/31 0031.
 */
@Component
public class ExceptionTypeProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER;

    static {
    	BUFFER= Stream.of(ExceptionType.values())
    			//enum 转换为ValuePair
    			.map(e->new ValuePairImpl<String>(e.getName(), e.getCode()))
    			.collect(Collectors.toList());
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || "".equals(obj)){
            return null;
        }
        for(ValuePair<?> valuePair : BUFFER){
            if(obj.toString().equals(valuePair.getValue())){
                return valuePair.getText();
            }
        }
        return null;
    }
}
