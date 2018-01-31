package com.artist.investment.service.impl;

import com.artist.investment.constant.PaymentType;
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
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            if(domain.getIsProgressing().equals(1)){
                Date now = new Date();
                //开始时间小于今天
                domain.setLtStartDate(now);
                //结束时间大于等于今天
                domain.setGteEndDate(now);
            }else if(domain.getIsProgressing().equals(0)){
                //开始时间大于今天
                //或
                //结束时间小于等于今天
                //所以这里需要自定义Example的and condition expr
                domain.mset(IDTO.AND_CONDITION_EXPR, " (start_date >= now() or end_date < now()) ");
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
        //先新增，再记录
        insertSelective(investment);
        //调整用户余额(减去当前投资额)
        BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), -investment.getInvestment());
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(balanceOutput.getData());
        paymentRecord.setInitialAmount(0L);
        paymentRecord.setTargetAmount(investment.getInvestment());
        paymentRecord.setName("新增投资");
        paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
        paymentRecord.setType(PaymentType.EXPENDITURE.getCode());
        paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
        paymentRecord.setNotes(getInsertInvestmentPaymentNotes(investment));
        paymentRecordService.insertSelective(paymentRecord);
        return BaseOutput.success("新增成功");
    }

    @Override
    @Transactional
    public BaseOutput updateSelectiveWithOutput(Investment investment) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        //先获取原始投资金额，用于收支记录的初始金额
        Long originalInvestment = get(investment.getId()).getInvestment();
        investment.setModifiedId(userTicket.getId());
        investment.setModified(new Date());
        centToYuanIntrospection(investment);
        //这里是为了解决投资人或银行卡为空的时候，只能从数据库获取旧值，然后用新值覆盖后强制更新
        investment = DTOUtils.link(investment, get(investment.getId()), Investment.class);
        update(investment);

        //调整用户余额
        BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), originalInvestment-investment.getInvestment());
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(balanceOutput.getData());
        paymentRecord.setInitialAmount(originalInvestment);
        paymentRecord.setTargetAmount(investment.getInvestment());
        paymentRecord.setName("修改投资");
        paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
        //如果调整金额小于等于调整前的金额，则是收入，否则是支出
        if(originalInvestment-investment.getInvestment() >= 0) {
            paymentRecord.setType(PaymentType.INCOME.getCode());
        }else{
            paymentRecord.setType(PaymentType.EXPENDITURE.getCode());
        }
        paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
        paymentRecord.setNotes(getUpdateInvestmentPaymentNotes(investment, originalInvestment));
        paymentRecordService.insertSelective(paymentRecord);
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
        return convertInvestor(getActualDao().selectInvestorPlatformStats(), "investorId");
    }

    @Override
    public EasyuiPageOutput selectInvestorStats() {
        List<Map> list = convertInvestor(getActualDao().selectInvestorStats(), "investorId");
        EasyuiPageOutput easyuiPageOutput = new EasyuiPageOutput();
        easyuiPageOutput.setRows(list);
        easyuiPageOutput.setTotal(list.size());
        List<Map> summationList = new ArrayList<>();
        Map summation = new HashMap();
        summation.put("investorId", "合计");
        Float dailyProfit = 0f;
        Float investmentAmount = 0f;
        Float investment = 0f;
        Float deducted = 0f;
        Float profit = 0f;
        Float principalAndInterest = 0f;
        Float totalInvestment = 0f;
        Float totalInvestmentAmount = 0f;
        Float totalProfit = 0f;
        for(Map data : list){
            dailyProfit += Float.parseFloat(data.get("dailyProfit").toString());
            investmentAmount += Float.parseFloat(data.get("investmentAmount").toString());
            investment += Float.parseFloat(data.get("investment").toString());
            deducted += Float.parseFloat(data.get("deducted").toString());
            profit += Float.parseFloat(data.get("profit").toString());
            principalAndInterest += Float.parseFloat(data.get("principalAndInterest").toString());
            totalInvestment += Float.parseFloat(data.get("totalInvestment").toString());
            totalInvestmentAmount += Float.parseFloat(data.get("totalInvestmentAmount").toString());
            totalProfit += Float.parseFloat(data.get("totalProfit").toString());
        }
        summation.put("dailyProfit", dailyProfit);
        summation.put("investmentAmount", investmentAmount);
        summation.put("investment", investment);
        summation.put("deducted", deducted);
        summation.put("profit", profit);
        summation.put("principalAndInterest", principalAndInterest);
        summation.put("totalInvestment", totalInvestment);
        summation.put("totalInvestmentAmount", totalInvestmentAmount);
        summation.put("totalProfit", totalProfit);
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
        //调整用户余额
        BaseOutput<Long> balanceOutput = userRpc.adjustBalance(investment.getInvestorId(), investment.getInvestment());
        PaymentRecord paymentRecord = DTOUtils.newDTO(PaymentRecord.class);
        paymentRecord.setCreatedName(userTicket.getRealName());
        //设置当前余额
        paymentRecord.setBalance(balanceOutput.getData());
        paymentRecord.setInitialAmount(investment.getInvestment());
        paymentRecord.setTargetAmount(0L);
        paymentRecord.setName("删除投资");
        paymentRecord.setPlatformName(investmentPlatformService.get(investment.getPlatformId()).getName());
        //删除投资算是收入
        paymentRecord.setType(PaymentType.INCOME.getCode());
        paymentRecord.setUserName(userRpc.get(investment.getInvestorId()).getData().getRealName());
        paymentRecord.setNotes(getDeleteInvestmentPaymentNotes(investment));
        paymentRecordService.insertSelective(paymentRecord);
        return count;
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

}