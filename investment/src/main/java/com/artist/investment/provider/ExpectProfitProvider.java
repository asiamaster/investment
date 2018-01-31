package com.artist.investment.provider;

import com.artist.investment.domain.Investment;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        //获取投资额，因为这里可能已经被批量提供者转换过了，所以要取原始值，如果没有原始值才取字段的值
        Object investmentObj = investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "investment");
        if(investmentObj == null){
            investmentObj = investment.aget("investment");
        }
        Long investment1 = Long.parseLong(investmentObj.toString());
        Object deductedObj = investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "deducted");
        if(deductedObj == null){
            deductedObj = investment.aget("deducted");
        }
        Long deducted = Long.parseLong(deductedObj.toString());
        Date startDate = investment.getStartDate();
        Date endDate = investment.getEndDate();
        if(startDate == null || endDate == null){
            return null;
        }
        //(投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 收益总天数 / 365 / 100%
        BigDecimal bigDecimal = new BigDecimal((investment1+deducted) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(startDate, endDate));
        BigDecimal bigDecimal365 = new BigDecimal(365);
        BigDecimal bigDecimal100 = new BigDecimal(100);
        //下面的方式不精确
//        Long profit = (investment1+deducted) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(startDate, endDate) / 365 / 100;
        //精确计算两位小数，并且四舍五入
        Long profit = bigDecimal.divide(bigDecimal365, 0, BigDecimal.ROUND_HALF_DOWN).divide(bigDecimal100, 0, BigDecimal.ROUND_HALF_DOWN).longValue();
        return MoneyUtils.centToYuan(profit);
    }
}