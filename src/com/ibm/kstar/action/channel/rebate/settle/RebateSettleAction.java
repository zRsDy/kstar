package com.ibm.kstar.action.channel.rebate.settle;


import java.util.Date;

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

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.channel.IRebateSettleService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.channel.RebateSettle;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/rebateSettle")
public class RebateSettleAction extends BaseAction {
	
	@Autowired
	IRebateSettleService rebatesettleService ;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	SerialNumberService serialNumberService;
	
	@RequestMapping("/list")
	public String list() {
		return forward("rebate_settle_list");
	}
	
	@NoRight
	@RequestMapping("/selectDetails")
	public String selectDetails(String pickerId,String settleId,String customId, Model model){
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("settleId",settleId);
		model.addAttribute("customId",customId);
		return forward("select_detail");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = rebatesettleService.queryRebateSettles(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/addOrEdit")
	public String addOrEdit(String id, Model model) {
		if(id != null){
			RebateSettle settle = rebatesettleService.queryRebateSettle(id);
			model.addAttribute("settle",settle);
			CustomInfo customInfo = customService.getCustomInfo(settle.getCustomId());
			model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		}else{
			RebateSettle settle =  new RebateSettle();
			settle.setDeductionCode(serialNumberService.getSerialNumber3("rebateSettle"));
			settle.setOrganization(this.getUserObject().getOrg().getId());
			settle.setCreateDate(new Date());
			settle.setCurrency(settle.getLovMember("CURRENCY", "CNY").getId());
			settle.setStatus(settle.getLovMember("REBATESETTLESTATUS", "01").getId());
			model.addAttribute("settle",settle);
		}
		return forward("rebate_settle_add");
	}

	@ResponseBody
	@RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
	public String addOrEdit(RebateSettle settle)  {
		rebatesettleService.addOrEditSettle(settle,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkSettle", method = RequestMethod.POST)
	public String checkSettle(String id) {
		rebatesettleService.checkSettle(id,this.getUserObject());
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/pageDetail")
	public String pageDetail(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String rebateSettleId = condition.getStringCondition("rebateSettleId");
		IPage p = new PageImpl(null,condition.getPage(),condition.getRows(),0);
		if(StringUtil.isNotEmpty(rebateSettleId)){
			condition.getFilterObject().addCondition("rebateSettleId", "=", rebateSettleId);
			p = rebatesettleService.queryRebateSettleDetails(condition);
		}
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/selectDetails", method = RequestMethod.POST)
	public String selectDetails(@RequestParam(value = "ids[]")String[] ids, String settleId) {
		rebatesettleService.selectDetails(ids,settleId);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/submitSettleMoney", method = RequestMethod.POST)
	public String submitSettleMoney(String detailId,String settleMoney) {
		rebatesettleService.updateSettleMoney(detailId,settleMoney,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDetails", method = RequestMethod.POST)
	public String deleteDetails(@RequestParam(value = "ids[]")String[] ids, String settleId) {
		rebatesettleService.deleteDetails(ids);
		return sendSuccessMessage();
	}
}