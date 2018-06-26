package com.artist.investment.provider;

import com.artist.investment.constant.RepaymentMethod;
import com.artist.investment.domain.Investment;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.MoneyUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 每月还款提供者
 * 用于投资记录表
 * Created by asiam on 2018/6/25 0025.
 */
@Component
public class MonthlyRepaymentProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        Investment investment = (Investment) metaMap.get(ROW_DATA_KEY);
        //到期还款每月还款额为0
        if(investment.getRepaymentMethod().equals(RepaymentMethod.DUE.getCode())){
            return "0";
        }
        //计算开始投资时间到现在的收益(单位元)
        //获取投资额，因为这里可能已经被批量提供者转换过了，所以要取原始值，如果没有原始值才取字段的值
        Object investmentObj = investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "investment");
        if(investmentObj == null){
            investmentObj = investment.aget("investment");
        }
        //投资额，单位分
        Long investmentAmount = Long.parseLong(investmentObj.toString());
        Object deductedObj = investment.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX + "deducted");
        if(deductedObj == null){
            deductedObj = investment.aget("deducted");
        }
        Long deducted = Long.parseLong(deductedObj.toString());
        Investment clone = DTOUtils.clone(investment, Investment.class);
        clone.setInvestment(investmentAmount);
        clone.setDeducted(deducted);
        //等额本息
        if(investment.getRepaymentMethod().equals(RepaymentMethod.PRINCIPAL_INTEREST.getCode())) {
            return MoneyUtils.centToYuan(getExpectProfitByPrincipalInterest(clone));
        }else if(investment.getRepaymentMethod().equals(RepaymentMethod.MONTHLY.getCode())) {
            //贷款本金(元) = (investment+deducted)
            BigDecimal principal = new BigDecimal(clone.getInvestment() + clone.getDeducted()).divide(BigDecimal.valueOf(100));
            //月利率 = 年利率 / 12 (精确到小数后10位，四舍五入，2.35变成2.4)
            //每月利息(元) = 贷款本金×月利率(年利率/12)
            BigDecimal monthlyInterest = principal.multiply(new BigDecimal(clone.getProfitRatio()/100)).divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
            if(clone.getInterestCoupon() == 0){
                return monthlyInterest.toPlainString();
            }
            //单独计算加息率部分的(参考团贷网，分开作四舍五入)
            BigDecimal interestCouponRate = new BigDecimal(clone.getInterestCoupon()/100).divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);
            BigDecimal interestCoupon = principal.multiply(interestCouponRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            return monthlyInterest.toPlainString() +"+"+interestCoupon.toPlainString();
        }
        return "0";
    }

    /**
     * 获取等额本息每月收益(分)
     * @param investment
     * @return
     */
    private Long getExpectProfitByPrincipalInterest(Investment investment){
        //贷款本金 = (investment+deducted)
        BigDecimal principal = new BigDecimal(investment.getInvestment() + investment.getDeducted()).divide(BigDecimal.valueOf(100));
        //月利率 = 年利率 / 12 (精确到小数后10位，四舍五入，2.35变成2.4)
        BigDecimal monthlyInterestRate = new BigDecimal((investment.getProfitRatio()+investment.getInterestCoupon())/100).divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);
        //还款月数 (开始和结束日期之间相差月数)
        int repaymentMonth = monthsBetween(investment.getStartDate(), investment.getEndDate());
        //每月月供额(元)=贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
        BigDecimal monthlyRepayment = principal.multiply(monthlyInterestRate).multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth))
                .divide(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth).subtract(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
//      返回合计还款额
        return monthlyRepayment.multiply(BigDecimal.valueOf(100)).longValue();
    }

    /**
     * 计算两个日期相差月数
     * @param start
     * @param end
     * @return
     */
    private int monthsBetween(Date start, Date end) {
        return Months.monthsBetween(new DateTime(start), new DateTime(end)).getMonths();
    }
}