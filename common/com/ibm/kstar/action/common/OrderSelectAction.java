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

import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/order")
public class OrderSelectAction extends BaseAction{
	
	@Autowired
	IOrderService orderService;
	
	@NoRight
	@RequestMapping("/selectList")
	public String selectOrder(String pickerId,String orderType,Model model,HttpServletRequest request){
		model.addAttribute("pickerId",pickerId);
		String customerId = request.getParameter("customerId");
		String customerName = request.getParameter("customerName");
		model.addAttribute("customerId",customerId);
		model.addAttribute("customerName",customerName);
		String isAdvanceBilling = request.getParameter("isAdvanceBilling");
		model.addAttribute("isAdvanceBilling",isAdvanceBilling);
		//行状态
		String lineStatus = request.getParameter("lineStatus");
		model.addAttribute("lineStatus",lineStatus);
		//订单头执行状态
		String executeStatus = request.getParameter("executeStatus");
		model.addAttribute("executeStatus",executeStatus);
		//合同CODE
		String contractCode = request.getParameter("contractCode");
		model.addAttribute("contractCode",contractCode);
		//业务实体
		String businessEntity = request.getParameter("businessEntity");
		model.addAttribute("businessEntity",businessEntity);
		//ERP是否发货
		String isErpDelivery =request.getParameter("isErpDelivery");
		model.addAttribute("isErpDelivery",isErpDelivery);
		//币种
		String currency = request.getParameter("currency");
		model.addAttribute("currency",currency);
		//我司合同编号
		String sourceCode = request.getParameter("sourceCode");
		model.addAttribute("sourceCode",sourceCode);
		//ERP订单编号
		String erpOrderCode = request.getParameter("erpOrderCode");
		model.addAttribute("erpOrderCode",erpOrderCode);
		
		return forward("orderSelectList");
	}
	
	@NoRight
	@RequestMapping("/multiSelectList")
	public String multiSelectOrder(String pickerId,String orderType,Model model,HttpServletRequest request){
		model.addAttribute("pickerId",pickerId);
		String customerId = request.getParameter("customerId");
		String customerName = request.getParameter("customerName");
		model.addAttribute("customerId",customerId);
		model.addAttribute("customerName",customerName);
		String isAdvanceBilling = request.getParameter("isAdvanceBilling");
		model.addAttribute("isAdvanceBilling",isAdvanceBilling);
		String lineStatus = request.getParameter("lineStatus");
		model.addAttribute("lineStatus",lineStatus);
		//订单头执行状态
		String executeStatus = request.getParameter("executeStatus");
		model.addAttribute("executeStatus",executeStatus);
		//合同CODE
		String contractCode = request.getParameter("contractCode");
		model.addAttribute("contractCode",contractCode);
		//业务实体
		String businessEntity = request.getParameter("businessEntity");
		model.addAttribute("businessEntity",businessEntity);
		//ERP是否发货
		String isErpDelivery =request.getParameter("isErpDelivery");
		model.addAttribute("isErpDelivery",isErpDelivery);
		//币种
		String currency = request.getParameter("currency");
		model.addAttribute("currency",currency);
		//我司合同编号
		String sourceCode = request.getParameter("sourceCode");
		model.addAttribute("sourceCode",sourceCode);
		//ERP订单编号
		String erpOrderCode = request.getParameter("erpOrderCode");
		model.addAttribute("erpOrderCode",erpOrderCode);
		
		

		return forward("orderMultiSelectList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		IPage p = orderService.queryOrderSelectList(condition);
		return sendSuccessMessage(p);
	}
}
