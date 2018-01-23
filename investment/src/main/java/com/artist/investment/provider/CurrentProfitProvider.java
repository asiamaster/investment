package com.artist.investment.provider;

import com.artist.investment.domain.Investment;
import com.dili.ss.metadata.*;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 当前收益提供者
 */
@Component
public class CurrentProfitProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Investment investment = (Investment)metaMap.get(ROW_DATA_KEY);
        if(investment.getStartDate() == null){
            return "0.00";
        }
        //如果还没开始计算收益，则收益为抵扣券，没有抵扣券则收益为0
        if(investment.getStartDate().after(new Date())){
            return investment.getCashCoupon() == null ? "0.00" : investment.getCashCoupon().toString();
        }
        //求开始投资时间到现在的相关天数
        int defDay = DateUtils.differentDays(investment.getStartDate(), new Date());
        //总天数
        int totalDay = DateUtils.differentDays(investment.getStartDate(), investment.getEndDate());
        //计算开始投资时间到现在的收益(单位元)
        //获取投资额，因为这里已经被批量提供者转换过了，所以要取原始值
        Long investment1 = Long.parseLong(investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "investment").toString());
        Long cashCoupon = Long.parseLong(investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "cashCoupon").toString());
        //(投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 已投资天数 / 总天数 / 100(分)
        Long profit = (investment1+cashCoupon) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * defDay / totalDay / 100;
        return MoneyUtils.centToYuan(profit);
    }
}