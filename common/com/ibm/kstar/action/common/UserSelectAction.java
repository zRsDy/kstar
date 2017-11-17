package com.ibm.kstar.action.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.action.BaseAction;

import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/user")
public class UserSelectAction extends BaseAction{
	
	@Autowired
	IUserService userService;
	
	@NoRight
	@RequestMapping("/selectList")
	public String selectOrder(String pickerId,String orderType,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("userSelectList");
	}
	
	@NoRight
	@RequestMapping("/multiSelectList")
	public String multiSelectOrder(String pickerId,String orderType,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("userMultiSelectList");
	}
}
