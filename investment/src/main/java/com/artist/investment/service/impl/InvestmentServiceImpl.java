package com.artist.investment.service.impl;

import com.artist.investment.dao.InvestmentMapper;
import com.artist.investment.domain.Investment;
import com.artist.investment.domain.User;
import com.artist.investment.domain.dto.InvestmentDto;
import com.artist.investment.rpc.UserRpc;
import com.artist.investment.service.InvestmentService;
import com.artist.sysadmin.sdk.domain.UserTicket;
import com.artist.sysadmin.sdk.exception.NotLoginException;
import com.artist.sysadmin.sdk.session.SessionContext;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
@Service
public class InvestmentServiceImpl extends BaseServiceImpl<Investment, Long> implements InvestmentService {

    @Autowired
    private UserRpc userRpc;

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
    public BaseOutput insertSelectiveWithOutput(Investment investment) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        investment.setCreatedId(userTicket.getId());
        investment.setModifiedId(userTicket.getId());
        centToYuanIntrospection(investment);
        insertSelective(investment);
        return BaseOutput.success("新增成功");
    }

    @Override
    public BaseOutput updateSelectiveWithOutput(Investment investment) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        investment.setModifiedId(userTicket.getId());
        investment.setModified(new Date());
        centToYuanIntrospection(investment);
        //这里是为了解决投资人或银行卡为空的时候，只能从数据库获取旧值，然后用新值覆盖后强制更新
        investment = DTOUtils.link(investment, get(investment.getId()), Investment.class);
        update(investment);
        return BaseOutput.success("修改成功");
    }

    @Override
    public List<Map> selectDistributionPieChart(Integer isProgressing, Long investorId) {
//        if(investorId == null) {
//            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
//            if (userTicket == null) {
//                throw new NotLoginException();
//            }
//            investorId = userTicket.getId();
//        }
        Map map = new HashMap(2);
        map.put("isProgressing", isProgressing);
        map.put("investorId", investorId);
        return getActualDao().selectDistributionPieChart(map);
    }

    @Override
    public List<Map> selectProfitPieChart(Integer isProgressing, Long investorId) {
        if(investorId == null) {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket == null) {
                throw new NotLoginException();
            }
            investorId = userTicket.getId();
        }
        Map map = new HashMap(2);
        map.put("isProgressing", isProgressing);
        map.put("investorId", investorId);
        return getActualDao().selectProfitPieChart(map);
    }

    @Override
    public List<Map> selectInvestmentComparisonBarChart(Integer isProgressing) {
        Map map = new HashMap(1);
        map.put("isProgressing", isProgressing);
        List<Map> list = getActualDao().selectInvestmentComparisonBarChart(map);
        //这里只查出了投资人id，所以需要转换为投资人真实姓名
        //缓存用户，避免多次远程调用
        Map<Object, String> userCache = new HashMap<>();
        for(Map data : list){
            if(userCache.containsKey(data.get("investorId"))){
                data.put("investorId", userCache.get("investorId"));
            }else{
                BaseOutput<User> userBaseOutput= userRpc.get(Long.parseLong(data.get("investorId").toString()));
                if(userBaseOutput.isSuccess()){
                    data.put("investorId", userBaseOutput.getData().getRealName());
                    userCache.put("investorId", userBaseOutput.getData().getRealName());
                }else{
                    //远程查询用户失败，则直接显示用户id
                    data.put("investorId", data.get("investorId"));
                    userCache.put("investorId", data.get("investorId").toString());
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

}