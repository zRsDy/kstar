package com.ibm.kstar.action.common;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
@Controller
@RequestMapping("/common/customer")
public class CustomerSelectAction extends BaseAction{
	
	@Autowired
	ICustomInfoService customInfoService;
	
	@NoRight
	@RequestMapping("/selectList")
	public String selectOrder(String pickerId,String orderType,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("customerSelectList");
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if(StringUtil.isNotEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customFullName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customAliasName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customWebAddress", "like", "%"+searchKey+"%");
		}
		IPage p = customInfoService.query(condition);
		return sendSuccessMessage(p);
	}
}
