package com.artist.investment.controller;

import com.alibaba.fastjson.JSONObject;
import com.artist.investment.service.DataDictionaryService;
import com.artist.investment.service.DataDictionaryValueService;
import com.artist.investment.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api
@Controller
public class IndexController {

    @Autowired
    DataDictionaryService dataDictionaryService;
    @Autowired
    DataDictionaryValueService dataDictionaryValueService;
    @Autowired
    SystemConfigService systemConfigService;


    @ApiOperation("跳转到index页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "index";
    }

    @ApiOperation("跳转到登录页面")
    @RequestMapping(value = "/toLogin.html", method = RequestMethod.GET)
    public String toLogin(ModelMap modelMap) {
        return "toLogin";
    }



    //获取数据字典提供者
    private JSONObject getDDProvider(Long ddId) {
        JSONObject dataDictionaryValueProvider = new JSONObject();
        dataDictionaryValueProvider.put("provider", "dataDictionaryValueProvider");
        dataDictionaryValueProvider.put("queryParams", "{dd_id:" + ddId + "}");
        return dataDictionaryValueProvider;
    }


}
