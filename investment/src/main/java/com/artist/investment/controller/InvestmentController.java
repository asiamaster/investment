package com.artist.investment.controller;

import com.artist.investment.domain.Investment;
import com.artist.investment.service.InvestmentService;
import com.artist.sysadmin.sdk.domain.UserTicket;
import com.artist.sysadmin.sdk.exception.NotLoginException;
import com.artist.sysadmin.sdk.session.SessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @ApiOperation("跳转到Investment页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "investment/index";
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
    public @ResponseBody String listPage(Investment investment) throws Exception {
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

}