package com.artist.investment.component;

import com.artist.investment.constant.PaymentType;
import com.artist.investment.constant.Yn;
import com.artist.investment.dao.BankCardMapper;
import com.artist.investment.domain.BankCard;
import com.artist.investment.domain.Investment;
import com.artist.investment.domain.PaymentRecord;
import com.artist.investment.rpc.UserRpc;
import com.artist.investment.service.BankCardService;
import com.artist.investment.service.InvestmentPlatformService;
import com.artist.investment.service.InvestmentService;
import com.artist.investment.service.PaymentRecordService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描到期投资
 * Created by asiamaster on 2018/1/31 0024.
 */
@Component
public class ScanExpiredInvestmentJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(ScanExpiredInvestmentJob.class);

	@Autowired
	private UserRpc userRpc;

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private PaymentRecordService paymentRecordService;

	@Autowired
	private InvestmentPlatformService investmentPlatformService;

	@Autowired
	private BankCardService bankCardService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//		BankCard card = DTOUtils.newDTO(BankCard.class);
//		card.setId(6L);
//		Map setParams = new HashMap();
//		setParams.put("account_name", null);
//		setParams.put("is_default", null);
//		System.out.println(setParams.containsKey("account_name"));
//		card.setSetForceParams(setParams);
//
//		BankCard condition = DTOUtils.newDTO(BankCard.class);
//		condition.setId(6L);
//		bankCardService.updateExactByExample(card, condition);
		execute();
	}

	/**
	 * 扫描到期投资
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
		execute();
	}

	private void execute(){
		//查询没过期的投资记录
		Investment investmentCondition = DTOUtils.newDTO(Investment.class);
		investmentCondition.setIsExpired(Yn.NO.getCode());
		investmentCondition.mset(IDTO.AND_CONDITION_EXPR, "end_date <='" + DateUtils.format(new Date())+"'");
		List<Investment> investmentList = investmentService.listByExample(investmentCondition);
		if(CollectionUtils.isEmpty(investmentList)){
			return;
		}
		for(Investment investment : investmentList) {
			//计算本息合计收益(分为单位)
			//(投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 收益总天数 / 365 / 100%
			BigDecimal bigDecimal = new BigDecimal((investment.getInvestment()+investment.getDeducted()) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(investment.getStartDate(), investment.getEndDate()));
			BigDecimal bigDecimal365 = new BigDecimal(365);
			BigDecimal bigDecimal100 = new BigDecimal(100);
			//精确计算两位小数，并且四舍五入
			Long profit = bigDecimal.divide(bigDecimal365, 0, BigDecimal.ROUND_HALF_DOWN).divide(bigDecimal100, 0, BigDecimal.ROUND_HALF_DOWN).longValue();
			Long principalAndInterest = investment.getInvestment() + investment.getDeducted() + profit;
			//调整用户余额
			BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), principalAndInterest);
			PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
			paymentRecord.setCreatedName("过期投资扫描器");
			//设置当前余额
			paymentRecord.setBalance(balanceOutput.getData());
			paymentRecord.setInitialAmount(principalAndInterest);
			paymentRecord.setTargetAmount(0L);
			paymentRecord.setName("到期投资");
			paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
			//到期投资算是收入
			paymentRecord.setType(PaymentType.INCOME.getCode());
			paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
			paymentRecord.setNotes(getExpiredInvestmentPaymentNotes(investment, principalAndInterest));
			paymentRecordService.insertSelective(paymentRecord);
			investment.setIsExpired(Yn.YES.getCode());
			investment.setBalanceDue(balanceOutput.getData());
			investmentService.updateSelective(investment);
		}
	}

	/**
	 * 构造到期投资备注
	 * @param investment
	 * @return
	 */
	private String getExpiredInvestmentPaymentNotes(Investment investment, Long principalAndInterest){
		StringBuilder stringBuilder = new StringBuilder("到期投资:")
				.append(investment.getProjectName())
				.append(",")
				.append(MoneyUtils.centToYuan(principalAndInterest))
				.append("元, 到期时间:")
				.append(DateUtils.format(investment.getEndDate(), "yyyy-MM-dd"));
		return stringBuilder.toString();
	}

}
