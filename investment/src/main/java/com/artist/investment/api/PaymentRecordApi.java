package com.artist.investment.api;

import com.artist.investment.domain.PaymentRecord;
import com.artist.investment.service.PaymentRecordService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-01-30 09:35:46.
 */
@Api("/paymentRecord")
@Controller
@RequestMapping("/paymentRecordApi")
public class PaymentRecordApi {
    @Autowired
    PaymentRecordService paymentRecordService;


    @ApiOperation("新增PaymentRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PaymentRecord", paramType="form", value = "PaymentRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<String> insert(@RequestBody PaymentRecord paymentRecord) {
        paymentRecordService.insertSelective(paymentRecord);
        return BaseOutput.success("新增成功");
    }

}