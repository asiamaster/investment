package com.artist.investment.controller;

import com.artist.investment.domain.InvestmentPlatform;
import com.artist.investment.service.InvestmentPlatformService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-18 17:11:11.
 */
@Api("/investmentPlatform")
@Controller
@RequestMapping("/investmentPlatform")
public class InvestmentPlatformController {
    @Autowired
    InvestmentPlatformService investmentPlatformService;

    @ApiOperation("跳转到InvestmentPlatform页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "investmentPlatform/index";
    }

    @ApiOperation(value="查询InvestmentPlatform", notes = "查询InvestmentPlatform，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="InvestmentPlatform", paramType="form", value = "InvestmentPlatform的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<InvestmentPlatform> list(InvestmentPlatform investmentPlatform) {
        return investmentPlatformService.list(investmentPlatform);
    }

    @ApiOperation(value="分页查询InvestmentPlatform", notes = "分页查询InvestmentPlatform，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="InvestmentPlatform", paramType="form", value = "InvestmentPlatform的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(InvestmentPlatform investmentPlatform) throws Exception {
        return investmentPlatformService.listEasyuiPageByExample(investmentPlatform, true).toString();
    }

    @ApiOperation("新增InvestmentPlatform")
    @ApiImplicitParams({
		@ApiImplicitParam(name="InvestmentPlatform", paramType="form", value = "InvestmentPlatform的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(InvestmentPlatform investmentPlatform) {
        investmentPlatformService.insertSelective(investmentPlatform);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改InvestmentPlatform")
    @ApiImplicitParams({
		@ApiImplicitParam(name="InvestmentPlatform", paramType="form", value = "InvestmentPlatform的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(InvestmentPlatform investmentPlatform) {
        investmentPlatformService.updateSelective(investmentPlatform);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除InvestmentPlatform")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "InvestmentPlatform的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        investmentPlatformService.delete(id);
        return BaseOutput.success("删除成功");
    }
}