package com.artist.investment.controller;

import com.artist.investment.domain.PaymentRecord;
import com.artist.investment.service.PaymentRecordService;
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
 * This file was generated on 2018-01-30 09:35:46.
 */
@Api("/paymentRecord")
@Controller
@RequestMapping("/paymentRecord")
public class PaymentRecordController {
    @Autowired
    PaymentRecordService paymentRecordService;

    @ApiOperation("跳转到PaymentRecord页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "paymentRecord/index";
    }

    @ApiOperation(value="查询PaymentRecord", notes = "查询PaymentRecord，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentRecord", paramType="form", value = "PaymentRecord的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<PaymentRecord> list(PaymentRecord paymentRecord) {
        return paymentRecordService.list(paymentRecord);
    }

    @ApiOperation(value="分页查询PaymentRecord", notes = "分页查询PaymentRecord，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentRecord", paramType="form", value = "PaymentRecord的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(PaymentRecord paymentRecord) throws Exception {
        return paymentRecordService.listEasyuiPageByExample(paymentRecord, true).toString();
    }

    @ApiOperation("新增PaymentRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentRecord", paramType="form", value = "PaymentRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(PaymentRecord paymentRecord) {
        paymentRecordService.insertSelective(paymentRecord);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PaymentRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentRecord", paramType="form", value = "PaymentRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(PaymentRecord paymentRecord) {
        paymentRecordService.updateSelective(paymentRecord);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PaymentRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "PaymentRecord的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        paymentRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}