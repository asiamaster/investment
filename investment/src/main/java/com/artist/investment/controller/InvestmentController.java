package com.artist.investment.controller;

import com.artist.investment.constant.Yn;
import com.artist.investment.domain.BankCard;
import com.artist.investment.domain.Investment;
import com.artist.investment.domain.dto.InvestmentDto;
import com.artist.investment.service.BankCardService;
import com.artist.investment.service.InvestmentService;
import com.artist.sysadmin.sdk.domain.UserTicket;
import com.artist.sysadmin.sdk.exception.NotLoginException;
import com.artist.sysadmin.sdk.session.SessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-22 10:24:59.
 */
@Api("/investment")
@Controller
@RequestMapping("/investment")
public class InvestmentController {
    @Autowired
    InvestmentService investmentService;

    @Autowired
    BankCardService bankCardService;

    @ApiOperation("跳转到Investment页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //查询当前用户的默认，非存管银行卡
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        BankCard bankCard = DTOUtils.newDTO(BankCard.class);
        bankCard.setUserId(userTicket.getId());
        bankCard.setIsDefault(1);
        bankCard.setIsDepository(0);
        //查询当前用户的默认非存管银行卡
        List<BankCard> bankCards = bankCardService.list(bankCard);
        if(bankCards!= null && !bankCards.isEmpty()){
            modelMap.put("bankCardId", bankCards.get(0).getId());
            modelMap.put("bankCardNumber", bankCards.get(0).getCardNumber());
        }
        return "investment/index";
    }

    @ApiOperation("跳转到计算页面")
    @RequestMapping(value="/compute.html", method = RequestMethod.GET)
    public String compute(ModelMap modelMap) {
        return "investment/compute";
    }

    @ApiOperation(value="查询Investment", notes = "查询Investment，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Investment", paramType="form", value = "Investment的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Investment> list(Investment investment) {
        return investmentService.list(investment);
    }

    @ApiOperation(value="分页查询Investment", notes = "分页查询Investment，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Investment", paramType="form", value = "Investment的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(InvestmentDto investment) throws Exception {
        return investmentService.listEasyuiPageByExample(investment, true).toString();
    }

    @ApiOperation(value="查询近期到帐10笔投资", notes = "分页查询Investment，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Investment", paramType="form", value = "Investment的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value="/listComingInvestment", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listComingInvestment(InvestmentDto investment) throws Exception {
        investment.setSort("endDate");
        investment.setOrder("asc");
        investment.setPage(1);
        investment.setRows(10);
        Date now = new Date();
        //查询一个月内即将到期的投资
        investment.setLtEndDate(DateUtils.addDays(now, 30));
        investment.setGteEndDate(now);
        investment.setIsExpired(Yn.NO.getCode());
        return investmentService.listEasyuiPageByExample(investment, true).toString();
    }

    @ApiOperation("新增Investment")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Investment", paramType="form", value = "Investment的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Investment investment) {
        return investmentService.insertSelectiveWithOutput(investment);
    }

    @ApiOperation("修改Investment")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Investment", paramType="form", value = "Investment的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Investment investment) {
        return investmentService.updateSelectiveWithOutput(investment);
    }

    @ApiOperation("删除Investment")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Investment的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        return BaseOutput.success("删除成功").setData(investmentService.delete(id));
    }

    @ApiOperation("提前到帐")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", paramType="form", value = "Investment的主键", required = true, dataType = "long")
    })
    @RequestMapping(value="/arrived", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput arrived(Long id) {
        return investmentService.arrived(id);
    }

    @ApiOperation("调整提前到帐")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", paramType="form", value = "Investment的主键", required = true, dataType = "long")
    })
    @RequestMapping(value="/adjustArrived", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput adjustArrived(@RequestParam("id") Long id, @RequestParam("arrived") String arrived) {
        return investmentService.adjustArrived(id, arrived);
    }



    // ==================================   报表  ==================================
    @ApiImplicitParams({
            @ApiImplicitParam(name="Investment", paramType="form", value = "查询投资分布饼图", required = false, dataType = "string")
    })
    @RequestMapping(value="/selectInvestmentDistributionPieChart", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Map> selectInvestmentDistributionPieChart(InvestmentDto investment) {
        return investmentService.selectInvestmentDistributionPieChart(investment.getIsProgressing(), investment.getInvestorId());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="Investment", paramType="form", value = "查询收益分布饼图", required = false, dataType = "string")
    })
    @RequestMapping(value="/selectProfitDistributionPieChart", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Map> selectProfitDistributionPieChart(InvestmentDto investment) {
        return investmentService.selectProfitDistributionPieChart(investment.getIsProgressing(), investment.getInvestorId());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="Investment", paramType="form", value = "查询投资人横向对比柱图", required = false, dataType = "string")
    })
    @RequestMapping(value="/selectInvestmentComparisonBarChart", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Map> selectInvestmentComparisonBarChart(InvestmentDto investment) {
        return investmentService.selectInvestmentComparisonBarChart(investment.getIsProgressing());
    }

    /**
     * 查询投资人平台统计
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/selectInvestorPlatformStats", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Map> selectInvestorPlatformStats() {
        return investmentService.selectInvestorPlatformStats();
    }

    /**
     * 查询投资人统计
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/selectInvestorStats", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String selectInvestorStats(){
        return investmentService.selectInvestorStats().toString();
    }

    @ApiOperation("跳转到投资分布详情报表页面")
    @RequestMapping(value="/investmentDistributionPieChart.html", method = RequestMethod.GET)
    public String investmentDistributionPieChart(ModelMap modelMap) {
        return "investment/investmentDistributionPieChart";
    }

    @ApiOperation("跳转到投资收益分布详情报表页面")
    @RequestMapping(value="/profitDistributionPieChart.html", method = RequestMethod.GET)
    public String profitDistributionPieChart(ModelMap modelMap) {
        return "investment/profitDistributionPieChart";
    }

    @ApiOperation("跳转到投资统计页面")
    @RequestMapping(value="/investmentStats.html", method = RequestMethod.GET)
    public String investmentStats(ModelMap modelMap) {
        return "investment/investmentStats";
    }

}