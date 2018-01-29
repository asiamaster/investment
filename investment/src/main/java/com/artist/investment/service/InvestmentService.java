package com.artist.investment.service;

import com.artist.investment.domain.Investment;
import com.artist.investment.domain.dto.InvestmentDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
public interface InvestmentService extends BaseService<Investment, Long> {

    EasyuiPageOutput listEasyuiPageByExample(InvestmentDto domain, boolean useProvider) throws Exception;

    BaseOutput insertSelectiveWithOutput(Investment investment);

    BaseOutput updateSelectiveWithOutput(Investment investment);

    /**
     * 查询投资分布饼图
     * @param isProgressing 是否进行中(0,1或null) 和 investorId 投资人id
     * @return
     */
    List<Map> selectInvestmentDistributionPieChart(Integer isProgressing, Long investorId);

    /**
     * 查询收益分布饼图
     * (投资额 + 抵扣额) * (年化收益率 + 利率加息券) * 收益总天数 / 365 / 100% /100(分)， 保留两位小数并四舍五入
     * @param isProgressing 是否进行中(0,1或null) 和 investorId 投资人id
     * @return
     */
    List<Map> selectProfitDistributionPieChart(Integer isProgressing, Long investorId);

    /**
     * 查询投资横比柱图
     * @param isProgressing 是否进行中(0,1或null)
     * @return
     */
    List<Map> selectInvestmentComparisonBarChart(Integer isProgressing);

    /**
     * 查询投资人平台统计
     * @return
     */
    List<Map> selectInvestorPlatformStats();

    /**
     * 查询投资人统计
     * @return
     */
    EasyuiPageOutput selectInvestorStats();
}