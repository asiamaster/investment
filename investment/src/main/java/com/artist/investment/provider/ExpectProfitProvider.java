package com.artist.investment.provider;

import com.artist.investment.domain.Investment;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预计收益提供者
 */
@Component
public class ExpectProfitProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Investment investment = (Investment)metaMap.get(ROW_DATA_KEY);
        //计算开始投资时间到现在的收益(单位元)
        //获取投资额，因为这里已经被批量提供者转换过了，所以要取原始值
        Long investment1 = Long.parseLong(investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "investment").toString());
        Long cashCoupon = Long.parseLong(investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "cashCoupon").toString());
        //(投资额 + 抵扣额) * (年化收益率 + 利率加息券) / 100(分)
        Long profit = (investment1+cashCoupon) * (investment.getProfitRatio()+ investment.getInterestCoupon()) / 100;
        return MoneyUtils.centToYuan(profit);
    }
}