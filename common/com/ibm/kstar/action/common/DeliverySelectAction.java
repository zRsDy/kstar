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

import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/delivery")
public class DeliverySelectAction extends BaseAction{
	
	@Autowired
	IDeliveryService deliveryService;
	
	@Autowired
	IOrderService orderService;
	
	@NoRight
	@RequestMapping("/selectList")
	public String selectDelivery(String pickerId,Model model,HttpServletRequest request){
		model.addAttribute("pickerId",pickerId);
		String customerCode = request.getParameter("customerCode");
		String customerName = request.getParameter("customerName");
		model.addAttribute("customerCode",customerCode);
		model.addAttribute("customerName",customerName);
		String contractCode = request.getParameter("contractCode");
		model.addAttribute("contractCode",contractCode);
		//业务实体
		String businessEntity = request.getParameter("businessEntity");
		model.addAttribute("businessEntity",businessEntity);
		//是否提前开票
		String isAdvanceBilling = request.getParameter("isAdvanceBilling");
		model.addAttribute("isAdvanceBilling",isAdvanceBilling);
		
		return forward("deliverySelectList");
	}
	
	@NoRight
	@RequestMapping("/multiSelect")
	public String multiDeliveryOrder(String pickerId,Model model,HttpServletRequest request){
		model.addAttribute("pickerId",pickerId);
		String customerCode = request.getParameter("customerCode");
		String customerName = request.getParameter("customerName");
		model.addAttribute("customerCode",customerCode);
		model.addAttribute("customerName",customerName);
		String contractCode = request.getParameter("contractCode");
		model.addAttribute("contractCode",contractCode);
		//业务实体
		String businessEntity = request.getParameter("businessEntity");
		model.addAttribute("businessEntity",businessEntity);
		
		//是否提前开票
		String isAdvanceBilling = request.getParameter("isAdvanceBilling");
		model.addAttribute("isAdvanceBilling",isAdvanceBilling);
		
		return forward("deliveryMultiSelectList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		IPage p = deliveryService.getDeliveryLinesByHQL(condition);
		return sendSuccessMessage(p);
	}
}
