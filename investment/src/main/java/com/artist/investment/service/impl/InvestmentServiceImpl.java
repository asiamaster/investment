package com.artist.investment.service.impl;

import com.artist.investment.dao.InvestmentMapper;
import com.artist.investment.domain.Investment;
import com.artist.investment.service.InvestmentService;
import com.artist.sysadmin.sdk.domain.UserTicket;
import com.artist.sysadmin.sdk.exception.NotLoginException;
import com.artist.sysadmin.sdk.session.SessionContext;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
@Service
public class InvestmentServiceImpl extends BaseServiceImpl<Investment, Long> implements InvestmentService {

    public InvestmentMapper getActualDao() {
        return (InvestmentMapper)getDao();
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