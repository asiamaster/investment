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
import java.math.MathContext;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 本息合计提供者
 */
@Component
public class PrincipalAndInterestProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Investment investment = (Investment)metaMap.get(ROW_DATA_KEY);
        //收益
        String profit = null;
        //先取预计收益
        if(null != investment.aget("expectProfit")){
            profit = investment.aget("expectProfit").toString();
        }//没有预期收益则为活期，这时取当前收益
        else if(null != investment.aget("currentProfit")){
            profit = investment.aget("currentProfit").toString();
        }else{
            return null;
        }
        //获取当前投资额，单位分
        Object investmentObj = investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "investment");
        if(investmentObj == null){
            investmentObj = investment.aget("investment");
        }
        Long investment1 = Long.parseLong(investmentObj.toString());
        //获取抵扣额，单位分
        Object deductedObj = investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "deducted");
        if(deductedObj == null){
            deductedObj = investment.aget("deducted");
        }
        Long deducted = Long.parseLong(deductedObj.toString());
        //直接使用expectProfitProvider的计算结果*100
        Long expectProfit = new BigDecimal(Float.parseFloat(profit)).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).longValue();
        return MoneyUtils.centToYuan(investment1 + deducted + expectProfit) ;
    }
}