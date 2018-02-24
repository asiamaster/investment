package com.artist.investment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.artist.investment.constant.PaymentType;
import com.artist.investment.constant.Yn;
import com.artist.investment.dao.InvestmentMapper;
import com.artist.investment.domain.Investment;
import com.artist.investment.domain.PaymentRecord;
import com.artist.investment.domain.User;
import com.artist.investment.domain.dto.InvestmentDto;
import com.artist.investment.rpc.UserRpc;
import com.artist.investment.service.InvestmentPlatformService;
import com.artist.investment.service.InvestmentService;
import com.artist.investment.service.PaymentRecordService;
import com.artist.sysadmin.sdk.domain.UserTicket;
import com.artist.sysadmin.sdk.exception.NotLoginException;
import com.artist.sysadmin.sdk.session.SessionContext;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
@Service
public class InvestmentServiceImpl extends BaseServiceImpl<Investment, Long> implements InvestmentService {

    @Autowired
    private UserRpc userRpc;

    @Autowired
    private PaymentRecordService paymentRecordService;

    @Autowired
    private InvestmentPlatformService investmentPlatformService;

    public InvestmentMapper getActualDao() {
        return (InvestmentMapper)getDao();
    }

    @Override
    public EasyuiPageOutput listEasyuiPageByExample(InvestmentDto domain, boolean useProvider) throws Exception {
        if(domain.getIsProgressing() != null){
//            if(domain.getIsProgressing().equals(1)){
//                Date now = new Date();
//                //开始时间小于今天
//                domain.setLtStartDate(now);
//                //结束时间大于等于今天
//                domain.setGteEndDate(now);
//            }else if(domain.getIsProgressing().equals(0)){
//                //开始时间大于今天
//                //或
//                //结束时间小于等于今天
//                //所以这里需要自定义Example的and condition expr
//                domain.mset(IDTO.AND_CONDITION_EXPR, " (start_date >= now() or end_date < now()) ");
//            }
            if(domain.getIsProgressing().equals(1)) {
                domain.setIsExpired(Yn.NO.getCode());
            }else if(domain.getIsProgressing().equals(0)){
                domain.setIsExpired(Yn.YES.getCode());
            }
        }
        return super.listEasyuiPageByExample(domain, useProvider);

    }

    @Override
    @Transactional
    public BaseOutput insertSelectiveWithOutput(Investment investment) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        investment.setCreatedId(userTicket.getId());
        investment.setModifiedId(userTicket.getId());
        centToYuanIntrospection(investment);

        //如果要调整余额，还要记录收支明细
        if("1".equals(investment.aget("adjustBalance"))) {
            //调整用户余额(减去当前投资额)
            BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), -investment.getInvestment());
            if (!balanceOutput.isSuccess()) {
                return balanceOutput;
            }
            PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
            paymentRecord.setCreatedName(userTicket.getRealName());
            //设置当前余额
            paymentRecord.setBalance(balanceOutput.getData());
            paymentRecord.setAdjustAmount(investment.getInvestment());
            paymentRecord.setName("新增投资");
            paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
            paymentRecord.setType(PaymentType.EXPENDITURE.getCode());
            paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
            paymentRecord.setNotes(getInsertInvestmentPaymentNotes(investment));
            paymentRecordService.insertSelective(paymentRecord);
        }
        //先新增，再记录
        insertSelective(investment);
        return BaseOutput.success("新增成功");

    }

    @Override
    @Transactional
    public BaseOutput updateSelectiveWithOutput(Investment investment) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        Investment originalInvestment = get(investment.getId());
        //先获取原始投资金额，用于收支记录的初始金额
        Long originalInvestmentAmount = originalInvestment.getInvestment();
        investment.setModifiedId(userTicket.getId());
        investment.setModified(new Date());
        //分转元内省，因为这时入参中的各项资金指标还是有小数点的
        centToYuanIntrospection(investment);
        //这里是为了解决投资人或银行卡为空的时候，只能从数据库获取旧值，然后用新值覆盖后强制更新
//        investment = DTOUtils.link(investment, originalInvestment, Investment.class);

        updateExactSimple(investment);
        //如果要调整余额，还要记录收支明细
        if("1".equals(investment.aget("adjustBalance"))) {
            //调整用户余额
            BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), originalInvestmentAmount-investment.getInvestment());
            if(!balanceOutput.isSuccess()){
                return balanceOutput;
            }
            PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
            paymentRecord.setCreatedName(userTicket.getRealName());
            //设置当前余额
            paymentRecord.setBalance(balanceOutput.getData());
            paymentRecord.setAdjustAmount(originalInvestmentAmount - investment.getInvestment());
            paymentRecord.setName("修改投资");
            paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
            //如果调整金额小于等于调整前的金额，则是收入，否则是支出
            if (originalInvestmentAmount - investment.getInvestment() >= 0) {
                paymentRecord.setType(PaymentType.INCOME.getCode());
            } else {
                paymentRecord.setType(PaymentType.EXPENDITURE.getCode());
            }
            paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
            paymentRecord.setNotes(getUpdateInvestmentPaymentNotes(investment, originalInvestmentAmount));
            paymentRecordService.insertSelective(paymentRecord);
        }
        return BaseOutput.success("修改成功");
    }

    @Override
    public List<Map> selectInvestmentDistributionPieChart(Integer isProgressing, Long investorId) {
        Map map = new HashMap(2);
        map.put("isProgressing", isProgressing);
        map.put("investorId", investorId);
        return getActualDao().selectInvestmentDistributionPieChart(map);
    }

    @Override
    public List<Map> selectProfitDistributionPieChart(Integer isProgressing, Long investorId) {
        Map map = new HashMap(2);
        map.put("isProgressing", isProgressing);
        map.put("investorId", investorId);
        return getActualDao().selectProfitDistributionPieChart(map);
    }

    @Override
    public List<Map> selectInvestmentComparisonBarChart(Integer isProgressing) {
        Map map = new HashMap(1);
        map.put("isProgressing", isProgressing);
        return convertInvestor(getActualDao().selectInvestmentComparisonBarChart(map), null);
    }

    @Override
    public List<Map> selectInvestorPlatformStats() {
        try {
            return ValueProviderUtils.buildDataByProvider(getStatsMetadata(), convertInvestor(getActualDao().selectInvestorPlatformStats(), "investorId"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EasyuiPageOutput selectInvestorStats() {
        List<Map> list = convertInvestor(getActualDao().selectInvestorStats(), "investorId");
        EasyuiPageOutput easyuiPageOutput = new EasyuiPageOutput();
        try {
            easyuiPageOutput.setRows(ValueProviderUtils.buildDataByProvider(getStatsMetadata(), list));
        } catch (Exception e) {
            e.printStackTrace();
            easyuiPageOutput.setRows(Lists.newArrayList());
        }
        easyuiPageOutput.setTotal(list.size());
        //求合计
        List<Map> summationList = new ArrayList<>();
        Map summation = new HashMap();
        summation.put("investorId", "合计");
        Float dailyProfit = 0F;
        Float currentProfit = 0F;
        Float totalCurrentProfit = 0F;
        Float investmentAmount = 0F;
        Float investment = 0F;
        Float deducted = 0F;
        Float profit = 0F;
        Float principalAndInterest = 0F;
        Float totalInvestment = 0F;
        Float totalInvestmentAmount = 0F;
        Float totalProfit = 0F;
        Float totalDeducted = 0F;
        Float totalPrincipalAndInterest = 0F;
        for(Map data : list){
            dailyProfit += Float.parseFloat(data.get("dailyProfit").toString());
            currentProfit += Float.parseFloat(data.get("currentProfit").toString());
            totalCurrentProfit += Float.parseFloat(data.get("totalCurrentProfit").toString());
            investmentAmount += Float.parseFloat(data.get("investmentAmount").toString());
            investment += Float.parseFloat(data.get("investment").toString());
            deducted += Float.parseFloat(data.get("deducted").toString());
            profit += Float.parseFloat(data.get("profit").toString());
            principalAndInterest += Float.parseFloat(data.get("principalAndInterest").toString());
            totalInvestment += Float.parseFloat(data.get("totalInvestment").toString());
            totalInvestmentAmount += Float.parseFloat(data.get("totalInvestmentAmount").toString());
            totalProfit += Float.parseFloat(data.get("totalProfit").toString());
            totalDeducted += Float.parseFloat(data.get("totalDeducted").toString());
            totalPrincipalAndInterest += Float.parseFloat(data.get("totalPrincipalAndInterest").toString());
        }
        summation.put("dailyProfit", dailyProfit);
        summation.put("currentProfit", currentProfit);
        summation.put("totalCurrentProfit", totalCurrentProfit);
        summation.put("investmentAmount", investmentAmount);
        summation.put("investment", investment);
        summation.put("deducted", deducted);
        summation.put("profit", profit);
        summation.put("principalAndInterest", principalAndInterest);
        summation.put("totalInvestment", totalInvestment);
        summation.put("totalInvestmentAmount", totalInvestmentAmount);
        summation.put("totalProfit", totalProfit);
        summation.put("totalDeducted", totalDeducted);
        summation.put("totalPrincipalAndInterest", totalPrincipalAndInterest);
        summationList.add(summation);
        easyuiPageOutput.setFooter(summationList);
        return easyuiPageOutput;
    }

    @Override
    @Transactional
    public int delete(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        Investment investment = get(id);
        //先删除，再记录
        int count = super.delete(id);
        Long balance = 0L;
        if(investment.getIsExpired().equals(Yn.YES.getCode())){
            balance = userRpc.get(investment.getInvestorId()).getData().getBalance();
        }else{
            //判断如果是没过期的投资才调整用户余额
            BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), investment.getInvestment());
            if(!balanceOutput.isSuccess()){
                throw new AppException("调整用户余额失败");
            }
            balance = balanceOutput.getData();
        }
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(balance);
        paymentRecord.setAdjustAmount(-investment.getInvestment());
        paymentRecord.setName("删除投资");
        paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
        //删除投资算是收入
        paymentRecord.setType(PaymentType.INCOME.getCode());
        paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
        paymentRecord.setNotes(getDeleteInvestmentPaymentNotes(investment));
        paymentRecordService.insertSelective(paymentRecord);
        return count;
    }

    @Override
    public BaseOutput arrived(Long id){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        Investment investment = get(id);
        BigDecimal bigDecimal = new BigDecimal((investment.getInvestment()+investment.getDeducted()) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(investment.getStartDate(), investment.getEndDate()));
        BigDecimal bigDecimal365 = new BigDecimal(365);
        BigDecimal bigDecimal100 = new BigDecimal(100);
        //精确计算两位小数，并且四舍五入
        Long profit = bigDecimal.divide(bigDecimal365, 0, BigDecimal.ROUND_HALF_DOWN).divide(bigDecimal100, 0, BigDecimal.ROUND_HALF_DOWN).longValue();
        //本息合计
        Long principalAndInterest = investment.getInvestment() + investment.getDeducted() + profit;
        //调整余额量为本息合计 - 部分调整量
        Long arrived = principalAndInterest - investment.getArrived();
        //调整用户余额
        BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), arrived);
        if(!balanceOutput.isSuccess()){
            return balanceOutput;
        }
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(balanceOutput.getData());
        //调整额为本息合计-当前到期额
        paymentRecord.setAdjustAmount(principalAndInterest-investment.getArrived());
        paymentRecord.setName("提前到帐");
        paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
        //到期投资算是收入
        paymentRecord.setType(PaymentType.INCOME.getCode());
        paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
        paymentRecord.setNotes(getArrivedInvestmentPaymentNotes(investment, principalAndInterest));
        paymentRecordService.insertSelective(paymentRecord);
        investment.setIsExpired(Yn.YES.getCode());
        investment.setBalanceDue(balanceOutput.getData());
        //设置当前到帐余额为本息合计
        investment.setArrived(principalAndInterest);
        updateSelective(investment);
        return BaseOutput.success("操作成功");
    }

    //获取投资的本息合计
    private Long getPrincipalAndInterest(Investment investment){
        //计算本息合计收益(分为单位)
        //(投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 收益总天数 / 365 / 100%
        BigDecimal bigDecimal = new BigDecimal((investment.getInvestment()+investment.getDeducted()) * (investment.getProfitRatio()+ investment.getInterestCoupon()) * DateUtils.differentDays(investment.getStartDate(), investment.getEndDate()));
        BigDecimal bigDecimal365 = new BigDecimal(365);
        BigDecimal bigDecimal100 = new BigDecimal(100);
        //精确计算两位小数，并且四舍五入
        Long profit = bigDecimal.divide(bigDecimal365, 0, BigDecimal.ROUND_HALF_DOWN).divide(bigDecimal100, 0, BigDecimal.ROUND_HALF_DOWN).longValue();
        //本息合计
        return investment.getInvestment() + investment.getDeducted() + profit;
    }

    @Override
    public BaseOutput adjustArrived(Long id, String arrived){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        //调整投资记录的到期余额
        Investment investment = get(id);
        //转换前台输入的还款额
        Long arrivedLong = BigDecimal.valueOf(Double.valueOf(arrived)).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).longValue();
        //本息合计
        Long principalAndInterest = getPrincipalAndInterest(investment);

        //调整用户余额
        BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), arrivedLong);
        if(!balanceOutput.isSuccess()){
            return balanceOutput;
        }
        //新增投资记录
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(balanceOutput.getData());
        //设置调整额
        paymentRecord.setAdjustAmount(arrivedLong);
        paymentRecord.setName("部分到帐");
        paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
        //到期投资算是收入
        paymentRecord.setType(PaymentType.INCOME.getCode());
        paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
        paymentRecord.setNotes(getArrivedInvestmentPaymentNotes(investment, arrivedLong));
        paymentRecordService.insertSelective(paymentRecord);
        //如果调整后，当前到帐金额 >= 本息合计，则设置为过期投资
        if((investment.getArrived() + arrivedLong) >= principalAndInterest){
            investment.setIsExpired(Yn.YES.getCode());
        }
        //设置当前到期总额
        investment.setArrived(investment.getArrived() + arrivedLong);
        //设置到期时用户的余额
        investment.setBalanceDue(balanceOutput.getData());
        updateSelective(investment);
        return BaseOutput.success("操作成功");
    }

    /**
     * 构造提前到期投资备注
     * @param investment
     * @return
     */
    private String getArrivedInvestmentPaymentNotes(Investment investment, Long principalAndInterest){
        StringBuilder stringBuilder = new StringBuilder("提前到帐投资:")
                .append(investment.getProjectName())
                .append(",")
                .append(MoneyUtils.centToYuan(principalAndInterest))
                .append("元, 原到期时间:")
                .append(DateUtils.format(investment.getEndDate(), "yyyy-MM-dd"));
        return stringBuilder.toString();
    }

    /**
     * 将Map中的investorId转为投资人真实姓名
     */
    private List<Map> convertInvestor(List<Map> list, String investorIdKey){
        if(StringUtils.isBlank(investorIdKey)){
            investorIdKey = "investorId";
        }
        //这里只查出了投资人id，所以需要转换为投资人真实姓名
        //缓存用户，避免多次远程调用
        Map<Object, String> userCache = new HashMap<>();
        for(Map data : list){
            if(userCache.containsKey(data.get(investorIdKey))){
                data.put(investorIdKey, userCache.get(investorIdKey));
            }else{
                BaseOutput<User> userBaseOutput= userRpc.get(Long.parseLong(data.get(investorIdKey).toString()));
                if(userBaseOutput.isSuccess()){
                    data.put(investorIdKey, userBaseOutput.getData().getRealName());
                    userCache.put(investorIdKey, userBaseOutput.getData().getRealName());
                }else{
                    //远程查询用户失败，则直接显示用户id
                    data.put(investorIdKey, data.get(investorIdKey));
                    userCache.put(investorIdKey, data.get(investorIdKey).toString());
                }
            }
        }
        return list;
    }

    /**
     * 分转元内省,保留两位小数
     * @param investment
     */
    private void centToYuanIntrospection(Investment investment){
        //现金券
        if(investment.aget("cashCoupon") != null) {
            Float cashCoupon = Float.parseFloat(investment.aget("cashCoupon").toString()) * 100;
            investment.aset("cashCoupon", cashCoupon.longValue());
        }
        //已抵扣
        if(investment.aget("deducted") != null) {
            Float cashCoupon = Float.parseFloat(investment.aget("deducted").toString()) * 100;
            investment.aset("deducted", cashCoupon.longValue());
        }
        //实际支付
        if(investment.aget("actualExpenditure") != null) {
            Float cashCoupon = Float.parseFloat(investment.aget("actualExpenditure").toString()) * 100;
            investment.aset("actualExpenditure", cashCoupon.longValue());
        }
        //投资额
        if(investment.aget("investment") != null) {
            Float cashCoupon = Float.parseFloat(investment.aget("investment").toString()) * 100;
            investment.aset("investment", cashCoupon.longValue());
        }
    }

    /**
     * 构造新增投资备注
     * @param investment
     * @return
     */
    private String getInsertInvestmentPaymentNotes(Investment investment){
        StringBuilder stringBuilder = new StringBuilder("新增投资:")
                .append(investment.getProjectName())
                .append(",")
                .append(MoneyUtils.centToYuan(investment.getInvestment()))
                .append("元,从")
                .append(DateUtils.format(investment.getStartDate(), "yyyy-MM-dd"))
                .append("到")
                .append(DateUtils.format(investment.getEndDate(), "yyyy-MM-dd"))
                .append(",利率:")
                .append(investment.getProfitRatio());
        if(investment.getInterestCoupon() != null){
            stringBuilder.append(",加息:").append(investment.getInterestCoupon()).append("%");
        }
        if(investment.getDeducted() != null) {
            stringBuilder.append(",券抵扣:").append(MoneyUtils.centToYuan(investment.getDeducted())).append("元");
        }
        return stringBuilder.toString();
    }

    /**
     * 构造修改投资备注
     * @param investment
     * @return
     */
    private String getUpdateInvestmentPaymentNotes(Investment investment, Long originalAmount){
        StringBuilder stringBuilder = new StringBuilder("修改投资:")
                .append(investment.getProjectName())
                .append(",从")
                .append(MoneyUtils.centToYuan(originalAmount))
                .append("元,")
                .append("到")
                .append(MoneyUtils.centToYuan(investment.getInvestment()))
                .append("元,从")
                .append(DateUtils.format(investment.getStartDate(), "yyyy-MM-dd"))
                .append("到")
                .append(DateUtils.format(investment.getEndDate(), "yyyy-MM-dd"))
                .append(",利率")
                .append(investment.getProfitRatio()).append("%");
        if(investment.getInterestCoupon() != null){
            stringBuilder.append(",加息:").append(investment.getInterestCoupon()).append("%");
        }
        if(investment.getDeducted() != null) {
            stringBuilder.append(",券抵扣:").append(MoneyUtils.centToYuan(investment.getDeducted())).append("元");
        }
        return stringBuilder.toString();
    }

    /**
     * 构造删除投资备注
     * @param investment
     * @return
     */
    private String getDeleteInvestmentPaymentNotes(Investment investment){
        StringBuilder stringBuilder = new StringBuilder("删除投资:")
                .append(investment.getProjectName())
                .append(MoneyUtils.centToYuan(investment.getInvestment()))
                .append("元");
        return stringBuilder.toString();
    }


    /**
     * 获取统计报表提供者，分别是本息合计提供者和累计本息合计提供者
     * 主要是考虑活期的情况，预期收益可能为空
     * @return
     */
    private Map<String, Object> getStatsMetadata(){
        Map<String, Object> metadata = new HashMap<>();
        JSONObject principalAndInterestStatsProvider = new JSONObject();
        principalAndInterestStatsProvider.put("provider", "principalAndInterestStatsProvider");
        metadata.put("principalAndInterest", principalAndInterestStatsProvider);
        JSONObject totalPrincipalAndInterestStatsProvider = new JSONObject();
        totalPrincipalAndInterestStatsProvider.put("provider", "totalPrincipalAndInterestStatsProvider");
        metadata.put("totalPrincipalAndInterest", totalPrincipalAndInterestStatsProvider);
        return metadata;
    }

}