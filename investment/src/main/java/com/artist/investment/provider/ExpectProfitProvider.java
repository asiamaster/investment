package com.artist.investment.provider;

import com.artist.investment.constant.RepaymentMethod;
import com.artist.investment.domain.Investment;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
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
        Date startDate = investment.getStartDate();
        Date endDate = investment.getEndDate();
        if(startDate == null || endDate == null){
            return null;
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

        //单独处理等额本息
        if(investment.getRepaymentMethod().equals(RepaymentMethod.PRINCIPAL_INTEREST.getCode())){
            Investment clone = DTOUtils.clone(investment, Investment.class);
            clone.setInvestment(investmentAmount);
            clone.setDeducted(deducted);
            return MoneyUtils.centToYuan(getExpectProfitByPrincipalInterest(clone));
        }
        //单独处理按月付息，可能和到期还款有细微的差别(精确到分的话，每个月有四舍五入的差别)
        if(investment.getRepaymentMethod().equals(RepaymentMethod.MONTHLY.getCode())){
            Investment clone = DTOUtils.clone(investment, Investment.class);
            clone.setInvestment(investmentAmount);
            clone.setDeducted(deducted);
            //贷款本金 = (investment+deducted)
            BigDecimal principal = new BigDecimal(investment.getInvestment() + investment.getDeducted()).divide(BigDecimal.valueOf(100));
            //月利率 = 年利率 / 12 (精确到小数后10位，四舍五入，2.35变成2.4)
            BigDecimal monthlyInterestRate = new BigDecimal(investment.getProfitRatio()/100).divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);
            //每月利息=贷款本金×月利率(年利率/12)
            BigDecimal monthlyInterest = principal.multiply(monthlyInterestRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            //单独计算加息率部分的(参考团贷网，分开作四舍五入)
            BigDecimal interestCouponRate = new BigDecimal(investment.getInterestCoupon()/100).divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);
            BigDecimal interestCoupon = principal.multiply(interestCouponRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            return monthlyInterest.add(interestCoupon).multiply(BigDecimal.valueOf(12)).toPlainString();
        }

        //(投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 收益总天数 / 365 / 100%
        BigDecimal bigDecimal = new BigDecimal((investmentAmount+deducted) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(startDate, endDate));
        BigDecimal bigDecimal365 = new BigDecimal(365);
        BigDecimal bigDecimal100 = new BigDecimal(100);
        //下面的方式不精确
//        Long profit = (investment1+deducted) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(startDate, endDate) / 365 / 100;
        //精确计算两位小数，并且四舍五入
        Long profit = bigDecimal.divide(bigDecimal365, 0, BigDecimal.ROUND_HALF_UP).divide(bigDecimal100, 0, BigDecimal.ROUND_HALF_UP).longValue();
        return MoneyUtils.centToYuan(profit);
    }

    /**
     * 获取等额本息预计收益
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
        BigDecimal monthlyPayment = principal.multiply(monthlyInterestRate).multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth))
                .divide(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth).subtract(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
//      返回合计还款额
        return monthlyPayment.multiply(BigDecimal.valueOf(repaymentMonth)).subtract(principal).multiply(BigDecimal.valueOf(100)).longValue();
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