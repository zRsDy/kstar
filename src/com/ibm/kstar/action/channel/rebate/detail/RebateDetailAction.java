package com.ibm.kstar.action.channel.rebate.detail;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.channel.IRebateDetailService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/rebateDetail")
public class RebateDetailAction extends BaseAction {
	@Autowired
	IRebateDetailService rebateDetailService ;
	
	@RequestMapping("/list")
	public String list() {
		return forward("rebate_detail_list");
	}
	
	@RequestMapping("/deductionList")
	public String deductionList(String rebateDetailId, Model model) {
		model.addAttribute("rebateDetailId",rebateDetailId);
		return forward("rebate_deduction_list");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = rebateDetailService.queryRebateDetails(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public String publish(@RequestParam(value = "ids[]")String[] ids) {
		rebateDetailService.publishDetail(ids,this.getUserObject());
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public String check(@RequestParam(value = "ids[]")String[] ids) {
		rebateDetailService.checkDetail(ids,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public String close(@RequestParam(value = "ids[]")String[] ids) {
		rebateDetailService.closeDetail(ids,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids[]")String[] ids) {
		rebateDetailService.deleteDetails(ids);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pageDeduction")
	public String pageDeduction(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String rebateDetailId = condition.getStringCondition("rebateDetailId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(rebateDetailId)){	//先判断，提升效率
			p = rebateDetailService.queryDeductionDetails(condition);
		}
		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/submitColumnValue", method = RequestMethod.POST)
	public String submitColumnValue(String detailId,String column,String value) {
		rebateDetailService.updateColumnValue(detailId,column,value,this.getUserObject());
		return sendSuccessMessage();
	}
}