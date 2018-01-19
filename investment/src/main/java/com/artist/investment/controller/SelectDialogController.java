package com.artist.investment.controller;

import com.artist.investment.domain.User;
import com.artist.investment.rpc.UserRpc;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 选择对话框控制器
 */
@RequestMapping("/selectDialog")
@Controller
public class SelectDialogController {

	@Autowired
	private UserRpc userRPC;

	// ================================  用户  ====================================

	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public String user(ModelMap modelMap, @RequestParam("textboxId") String textboxId) {
		modelMap.put("textboxId", textboxId);
		return "controls/user";
	}

	@ResponseBody
	@RequestMapping(value = "/listUser", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> listUser(ModelMap modelMap, User user) {
		BaseOutput<List<User>> output = this.userRPC.listByExample(user);
		if (output.isSuccess()) {
			return output.getData();
		}
		return null;
	}


}
