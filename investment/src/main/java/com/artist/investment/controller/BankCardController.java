package com.artist.investment.controller;

import com.artist.investment.domain.BankCard;
import com.artist.investment.service.BankCardService;
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
 * This file was generated on 2018-01-19 14:15:47.
 */
@Api("/bankCard")
@Controller
@RequestMapping("/bankCard")
public class BankCardController {
    @Autowired
    BankCardService bankCardService;

    @ApiOperation("跳转到BankCard页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "bankCard/index";
    }

    @ApiOperation(value="查询BankCard", notes = "查询BankCard，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BankCard", paramType="form", value = "BankCard的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<BankCard> list(BankCard bankCard) {
        return bankCardService.list(bankCard);
    }

    @ApiOperation(value="分页查询BankCard", notes = "分页查询BankCard，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BankCard", paramType="form", value = "BankCard的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(BankCard bankCard) throws Exception {
        return bankCardService.listEasyuiPageByExample(bankCard, true).toString();
    }

    @ApiOperation("新增BankCard")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BankCard", paramType="form", value = "BankCard的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(BankCard bankCard) {
        bankCardService.insertSelective(bankCard);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改BankCard")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BankCard", paramType="form", value = "BankCard的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(BankCard bankCard) {
        bankCardService.updateSelective(bankCard);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除BankCard")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "BankCard的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        bankCardService.delete(id);
        return BaseOutput.success("删除成功");
    }
}