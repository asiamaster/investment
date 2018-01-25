package com.artist.investment.dao;

import com.artist.investment.domain.Investment;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;

public interface InvestmentMapper extends MyMapper<Investment> {

    /**
     * 查询投资分布饼图
     * @param params, isProgressing 是否进行中(0,1或null) 和 investorId 投资人id
     * @return
     */
    List<Map> selectInvestmentDistributionPieChart(Map params);

    /**
     * 查询收益分布饼图
     * (投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 收益总天数 / 365 / 100% /100(分)， 保留两位小数并四舍五入
     * @param params, isProgressing 是否进行中(0,1或null) 和 investorId 投资人id
     * @return
     */
    List<Map> selectProfitDistributionPieChart(Map params);

    /**
     * 查询投资横比柱图
     * @param params, isProgressing 是否进行中(0,1或null) 和 investorId 投资人id
     * @return
     */
    List<Map> selectInvestmentComparisonBarChart(Map params);

    /**
     * 查询投资人平台统计
     * @return
     */
    List<Map> selectInvestorPlatformStats();

    /**
     * 查询投资人统计
     * @return
     */
    List<Map> selectInvestorStats();
}