package com.artist.investment.component;

import com.artist.investment.constant.PaymentType;
import com.artist.investment.constant.ProjectDurationUnit;
import com.artist.investment.constant.RepaymentMethod;
import com.artist.investment.constant.Yn;
import com.artist.investment.domain.Investment;
import com.artist.investment.domain.PaymentRecord;
import com.artist.investment.rpc.UserRpc;
import com.artist.investment.service.InvestmentPlatformService;
import com.artist.investment.service.InvestmentService;
import com.artist.investment.service.PaymentRecordService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 扫描等额本息和按月付息到帐的投资
 * Created by asiamaster on 2018/1/31 0024.
 */
@Component
public class ScanPayableInvestmentJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(ScanPayableInvestmentJob.class);

	@Autowired
	private UserRpc userRpc;

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private PaymentRecordService paymentRecordService;

	@Autowired
	private InvestmentPlatformService investmentPlatformService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		execute();
	}

	/**
	 * 扫描到帐的投资
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
		execute();
	}

	private void execute(){
		//查询没过期的等额本息或按月付息的到帐投资记录
		Investment investmentCondition = DTOUtils.newDTO(Investment.class);
		investmentCondition.setIsExpired(Yn.NO.getCode());
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("repayment_method in (");
		stringBuilder.append(RepaymentMethod.MONTHLY.getCode());
		stringBuilder.append(",");
		stringBuilder.append(RepaymentMethod.PRINCIPAL_INTEREST.getCode());
		stringBuilder.append(")");
		investmentCondition.mset(IDTO.AND_CONDITION_EXPR, stringBuilder.toString());
		List<Investment> investmentList = investmentService.listByExample(investmentCondition);
		if(CollectionUtils.isEmpty(investmentList)){
			return;
		}
		for(Investment investment : investmentList) {
			//不支持活期
			if(investment.getEndDate() == null){
				return;
			}
			if(investment.getRepaymentMethod().equals(RepaymentMethod.PRINCIPAL_INTEREST.getCode())){
				principalInterest(investment);
			}else if(investment.getRepaymentMethod().equals(RepaymentMethod.MONTHLY.getCode())){
				monthly(investment);
			}
		}
	}

	/**
	 * 按月付息
	 * @param investment
	 */
	private void monthly(Investment investment){
//		贷款本金 = (investment+deducted)
		BigDecimal principal = new BigDecimal(investment.getInvestment() + investment.getDeducted()).divide(BigDecimal.valueOf(100));
		//月利率 = 年利率 / 12 (精确到小数后10位，四舍五入，2.35变成2.4)
		BigDecimal monthlyInterestRate = new BigDecimal(investment.getProfitRatio() / 100).divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);
		//计算当前应还款的月序号 st=======================================
		//获取还款/开始时间是当月几号
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(investment.getStartDate());
		//还款月天数, 如果开始时间的月天数和还款日不一样，则应该是债转项目,第一个月可能下次还款时间少于30天，即实际税率可能高于原利率
		int repaymentDay = investment.getRepaymentDay() == null ? startCalendar.get(Calendar.DAY_OF_MONTH) : investment.getRepaymentDay();
		//还款起始月
		int startMonth = startCalendar.get(Calendar.MONTH) + 1;
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		//获取当月最大天数
		int maxDayOfMonth = nowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//如果还款时间大于本月最大时间，则还款时间改为本月最大时间，例:还款时间是31号，本月只有30天
		if(repaymentDay > maxDayOfMonth){
			repaymentDay = maxDayOfMonth;
		}
		//获取当前时间是当月几号
		int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
		//获取当前时间是几月
		int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
		//计算当前应还款的月序号：如果当前月天数 > 还款月天数 则是当前月份减开始月份， 否则是当前月份减开始月份再减1
		int repaymentIndex = nowDay >= repaymentDay ? nowMonth - startMonth : nowMonth - startMonth - 1;
		//计算当前应还款的月序号 end=======================================
		//如果还没到第一次还款日，则跳过
		if(repaymentIndex <= 0){
			return;
		}
		//还款月数 (开始和结束日期之间相差月数)
		int repaymentMonth = monthsBetween(investment.getStartDate(), investment.getEndDate());
		//已还款月序号
		int monthIndex = investment.getMonthIndex() == null ? 0 : investment.getMonthIndex();
		//如果已还款月序号 大于等于 应该还款的月序号
		if(monthIndex >= repaymentIndex) {
			return;
		}
		//每月利息=贷款本金×月利率(年利率/12)
		BigDecimal monthlyInterest = principal.multiply(monthlyInterestRate).setScale(2, BigDecimal.ROUND_HALF_UP);
		//单独计算加息率部分的(参考团贷网，分开作四舍五入)
		BigDecimal interestCouponRate = new BigDecimal(investment.getInterestCoupon()/100).divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);

		//循环还未还款的月份
		//monthIndex为未还款月序号
		for(++monthIndex; monthIndex <= repaymentIndex; monthIndex++){
			//调整用户余额 ===================================
			//每月基本利息
			Long adjustAmount = monthlyInterest.multiply(BigDecimal.valueOf(100)).longValue();
			//加息率部分利息
			Long interestCoupon = principal.multiply(interestCouponRate).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).longValue();
			adjustAmount = adjustAmount + interestCoupon;
			BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), adjustAmount);

			//记录投资明细 ===================================
			PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
			paymentRecord.setCreatedName("到帐投资扫描器");
			//设置当前余额
			paymentRecord.setBalance(balanceOutput.getData());
			paymentRecord.setAdjustAmount(adjustAmount);
			paymentRecord.setName("每月付息到帐投资");
			String platformName = investmentPlatformService.get(investment.getPlatformId()).getName();
			paymentRecord.setPlatformName(platformName);
			//到期投资算是收入
			paymentRecord.setType(PaymentType.INCOME.getCode());
			paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
			paymentRecord.setNotes(getArriveInvestmentPaymentNotes(investment, adjustAmount, null, platformName));
			paymentRecordService.insertSelective(paymentRecord);

			// 修改投资记录的用户余额和到帐金额 ===================================
			//设置到期时，用户的余额
			investment.setBalanceDue(balanceOutput.getData());
			//设置当前到帐金额
			investment.setArrived(investment.getArrived() + adjustAmount);
			investment.setMonthIndex(monthIndex);
			//如果是最后一个月投资到账
			if(repaymentMonth == repaymentIndex && monthIndex == repaymentIndex){
				investment.setIsExpired(Yn.YES.getCode());
			}
			investmentService.updateSelective(investment);
		}
	}

	/**
	 * 计算等额本息
	 * 等额本息还款法:
	 * 每月月供额=贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
	 * 每月应还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
	 * 每月应还本金=贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕
	 * 总利息=还款月数×每月月供额-贷款本金
	 * @param investment
	 */
	private void principalInterest(Investment investment){
		//贷款本金 = (investment+deducted)
		BigDecimal principal = new BigDecimal(investment.getInvestment() + investment.getDeducted()).divide(BigDecimal.valueOf(100));
		//月利率 = 年利率 / 12 (精确到小数后10位，四舍五入，2.35变成2.4)
		BigDecimal monthlyInterestRate = new BigDecimal((investment.getProfitRatio() + investment.getInterestCoupon())/100).divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);
		//计算当前应还款的月序号 st=======================================
		//获取还款/开始时间是当月几号
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(investment.getStartDate());
		//还款月天数, 如果开始时间的月天数和还款日不一样，则应该是债转项目,第一个月可能下次还款时间少于30天，即实际税率可能高于原利率
		int repaymentDay = investment.getRepaymentDay() == null ? startCalendar.get(Calendar.DAY_OF_MONTH) : investment.getRepaymentDay();
		//还款起始月
		int startMonth = startCalendar.get(Calendar.MONTH) + 1;
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		//获取当月最大天数
		int maxDayOfMonth = nowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//如果还款时间大于本月最大时间，则还款时间改为本月最大时间，例:还款时间是31号，本月只有30天
		if(repaymentDay > maxDayOfMonth){
			repaymentDay = maxDayOfMonth;
		}
		//获取当前时间是当月几号
		int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
		//获取当前时间是几月
		int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
		//计算当前应还款的月序号：如果当前月天数 > 还款月天数 则是当前月份减开始月份， 否则是当前月份减开始月份再减1
		int repaymentIndex = nowDay >= repaymentDay ? nowMonth - startMonth : nowMonth - startMonth - 1;
		//计算当前应还款的月序号 end=======================================
		//如果还没到第一次还款日，则跳过
		if(repaymentIndex <= 0){
			return;
		}
		//还款月数 (开始和结束日期之间相差月数)
		int repaymentMonth = monthsBetween(investment.getStartDate(), investment.getEndDate());
		//已还款月序号
		int monthIndex = investment.getMonthIndex() == null ? 0 : investment.getMonthIndex();
		//如果已还款月序号 大于等于 应该还款的月序号
		if(monthIndex >= repaymentIndex) {
			return;
		}
		//每月月供额(元)=贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
		BigDecimal monthlyRepayment = principal.multiply(monthlyInterestRate).multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth))
				.divide(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth).subtract(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
//		循环还未还款的月份
		//monthIndex为未还款月序号
		for(++monthIndex; monthIndex <= repaymentIndex; monthIndex++){
			//每月应还本金=贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕  用于记录收支日志
			BigDecimal monthlyPrincipal = principal.multiply(monthlyInterestRate).multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(monthIndex-1))
					.divide(BigDecimal.ONE.add(monthlyInterestRate).pow(repaymentMonth).subtract(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
			//调整用户余额 ===================================
			Long adjustAmount = monthlyRepayment.multiply(BigDecimal.valueOf(100)).longValue();
			BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), adjustAmount);

			//记录投资明细 ===================================
			PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
			paymentRecord.setCreatedName("到帐投资扫描器");
			//设置当前余额
			paymentRecord.setBalance(balanceOutput.getData());
			paymentRecord.setAdjustAmount(adjustAmount);
			paymentRecord.setName("等额本息到帐投资");
			String platformName = investmentPlatformService.get(investment.getPlatformId()).getName();
			paymentRecord.setPlatformName(platformName);
			//到期投资算是收入
			paymentRecord.setType(PaymentType.INCOME.getCode());
			paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
			paymentRecord.setNotes(getArriveInvestmentPaymentNotes(investment, adjustAmount, monthlyPrincipal.multiply(BigDecimal.valueOf(100)).longValue(), platformName));
			paymentRecordService.insertSelective(paymentRecord);

			// 修改投资记录的用户余额和到帐金额 ===================================
			//设置到期时，用户的余额
			investment.setBalanceDue(balanceOutput.getData());
			//设置当前到帐金额
			investment.setArrived(investment.getArrived() + adjustAmount);
			investment.setMonthIndex(monthIndex);
			//如果是最后一个月投资到账
			if(repaymentMonth == repaymentIndex && monthIndex == repaymentIndex){
				investment.setIsExpired(Yn.YES.getCode());
			}
			investmentService.updateSelective(investment);
		}
	}

	/**
	 * 构造到期投资备注
	 * @param investment
	 * @param adjustAmount	每月月供额
	 * @param monthlyPrincipal 每月应还本金
	 * @param platformName	平台名称
	 * @return
	 */
	private String getArriveInvestmentPaymentNotes(Investment investment, Long adjustAmount, Long monthlyPrincipal, String platformName){
		StringBuilder stringBuilder = new StringBuilder("["+platformName + "]投资["+investment.getProjectName()+"]到帐:")
				.append(MoneyUtils.centToYuan(adjustAmount))
				.append("元");
		if(monthlyPrincipal != null && monthlyPrincipal > 0){
			stringBuilder.append(",应收本金:").append(MoneyUtils.centToYuan(monthlyPrincipal)).append("元，应收利息:").append(MoneyUtils.centToYuan(adjustAmount-monthlyPrincipal)).append("元");
		}
		stringBuilder.append(",到帐时间:")
				.append(DateUtils.format(investment.getEndDate(), "yyyy-MM-dd"));
		return stringBuilder.toString();
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
