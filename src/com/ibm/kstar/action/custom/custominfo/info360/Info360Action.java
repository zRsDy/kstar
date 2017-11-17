package com.ibm.kstar.action.custom.custominfo.info360;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomEventService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/info360")
public class Info360Action extends BaseAction {

	@Autowired
	ICustomInfoService service;
	
	/**
	 * 360查询
	 */
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户活动")
	@RequestMapping("/event360")
	public String event360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}
		model.addAttribute("customInfo", customInfo);
		return forward("event360");
	}

	@Autowired
	ICustomEventService eventService;

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户活动一览")
	@ResponseBody
	@RequestMapping("/event360Show")
	public String event360Show(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("customCode");
		condition.getFilterObject().addCondition("customCode", "eq", customCode);
		IPage p = eventService.queryEvent(condition);
		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户商机")
	@RequestMapping("/bizopp360")
	public String bizOp360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}
		model.addAttribute("customInfo", customInfo);
		return forward("bizopp360");
	}

	@Autowired
	IBizoppService bizService;

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户商机一览")
	@ResponseBody
	@RequestMapping("/bizopp360Show")
	public String bizopp360Show(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customId = condition.getStringCondition("customId");
		condition.getFilterObject().addCondition("clientId", "eq", customId);
		IPage p = bizService.query(condition);
		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-报价合同")
	@RequestMapping("/quot360")
	public String quot360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}
		model.addAttribute("customInfo", customInfo);
		return forward("quot360");
	}

	@Autowired
	IQuotService quotService;

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户报价一览")
	@ResponseBody
	@RequestMapping("/quot360Show")
	public String quot360Show(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("customCode");
		condition.getFilterObject().addCondition("customerCode", "eq", customCode);
		IPage p = quotService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@Autowired
	private IContractService contractService;

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户合同")
	@RequestMapping("/contract360")
	public String contract360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}
		model.addAttribute("customInfo", customInfo);
		return forward("contract360");
	}
	
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户合同一览")
	@ResponseBody
	@RequestMapping("/contrat360Show")
	public String contrat360Show(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("customCode");
		condition.getFilterObject().addCondition("custCode", "eq", customCode);
		IPage p = contractService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户订单")
	@RequestMapping("/order360")
	public String order360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}
		model.addAttribute("customInfo", customInfo);
		return forward("order360");
	}

	@Autowired
	IOrderService orderService;

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户订单一览")
	@ResponseBody
	@RequestMapping("/order360Show")
	public String order360Show(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("customCode");
		condition.getFilterObject().addCondition("customerCode", "eq", customCode);
		IPage p = orderService.queryOrderHeaders(condition);
		return sendSuccessMessage(p);
	}
	
	@Autowired
	IDeliveryService deliveryService;

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-客户应收")
	@RequestMapping("/receive360")
	public String receive360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}
		model.addAttribute("customInfo", customInfo);
		return forward("receive360");
	}
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}360查询-应收一览")
	@ResponseBody
	@RequestMapping("/receive360Show")
	public String receive360Show(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		
		IPage p = deliveryService.queryDeliveryHeadersTtl(condition, getUserObject());
		return sendSuccessMessage(p);
	}


}
