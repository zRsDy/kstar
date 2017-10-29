package com.ibm.kstar.action.common.outteam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.common.outteam.ISalesOutService;
import com.ibm.kstar.entity.common.outteam.SalesTeamOut;
import com.ibm.kstar.interceptor.system.permission.NoRight;


@Controller
@RequestMapping("/salesOut")
public class SalesOutInfoAction extends BaseAction {

	@Autowired
	ISalesOutService service;

	//外部团队
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		
//		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("systemId", "eq", (String)condition.getCondition("systemId"));
//		condition.getFilterObject().addCondition("customId", "eq", customCode);
		condition.getFilterObject().addCondition("systemCode", "eq", (String)condition.getCondition("systemCode"));
		
		IPage p = service.querySalesOut(condition);
		
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String salesOutAdd(String systemId,String systemCode, Model model){
		return forward("add");
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(SalesTeamOut salesTeamOut, HttpServletRequest request){
		service.saveSalesOutInfo(salesTeamOut);
		return sendSuccessMessage(salesTeamOut.getSystemId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		SalesTeamOut salesTeamOut = service.getSalesOutInfo(id);
		if(salesTeamOut==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("salesTeamOut",salesTeamOut);
		return forward("add");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(SalesTeamOut salesTeamOut){
		service.updateSalesOutInfo(salesTeamOut);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteSalesOutInfo(id);
		return sendSuccessMessage();
	}
	
}
