package com.ibm.kstar.action.custom.customhandover;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomHandoverService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.entity.custom.CustomHandoverList;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/handover")
public class HandoverAction extends BaseAction {

	@Autowired
	ICustomHandoverService service;
	
	@Autowired
	ICustomInfoService customService;
	
	@Autowired
	ICustomNumberService numberService;
	
	@RequestMapping("/list")
	public String list(String id,Model model){
		return forward("list");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：交接一览")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String handoverCode = condition.getStringCondition("handoverCode");
		if(StringUtil.isNotEmpty(handoverCode)){
			condition.getFilterObject().addCondition("handoverCode", "like", "%" + handoverCode + "%");
		}
		
		String dateFrom = condition.getStringCondition("dateFrom");
		if(StringUtil.isNotEmpty(dateFrom)){
			condition.getFilterObject().addCondition("createdAt", ">=", dateFrom);
		}
		
		String dateTo = condition.getStringCondition("dateTo");
		if(StringUtil.isNotEmpty(dateTo)){
			condition.getFilterObject().addCondition("createdAt", "<=", dateTo);
		}
		
		String handoverFrom = condition.getStringCondition("handoverFrom");
		if(StringUtil.isNotEmpty(handoverFrom)){
			condition.getFilterObject().addCondition("handoverFrom", "=", handoverFrom);
		}
		
		String handoverTo = condition.getStringCondition("handoverTo");
		if(StringUtil.isNotEmpty(handoverTo)){
			condition.getFilterObject().addCondition("handoverTo", "=", handoverTo);
		}
		
		String handoverReason = condition.getStringCondition("handoverReason");
		if(StringUtil.isNotEmpty(handoverReason)){
			condition.getFilterObject().addCondition("handoverReason", "=", handoverReason);
		}
		
		String handoverStatus = condition.getStringCondition("handoverStatus");
		if(StringUtil.isNotEmpty(handoverStatus)){
			condition.getFilterObject().addCondition("handoverStatus", "=", handoverStatus);
		}
		
		IPage p = service.queryHandover(condition);
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：交接一览记录新增")
	@RequestMapping("/add")
	public String add(String id, Model model){
		model.addAttribute("id",id);
		
		CustomHandoverList customHandoverList = new CustomHandoverList();
		customHandoverList.setId(null);
		
		customHandoverList.setHandoverCode(numberService.getHandoverNumber());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customHandoverList.setHandoverDate(sdf.format(new Date()));
		
		customHandoverList.setHandoverStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("customHandoverList",customHandoverList);
		model.addAttribute("mode", IConstants.ACTION_TYPE_ADD);
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：交接一览记录新增保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomHandoverList customHandoverList, HttpServletRequest request){
		
//		if (StringUtils.equals(customHandoverList.getHandoverFrom(), customHandoverList.getHandoverTo())) {
//			throw new AnneException("交接人相同，不能交接!");
//		}
		
		customHandoverList.setHandoverFrom(getUserObject().getEmployee().getId());
		customHandoverList.setHandoverFromOrg(getUserObject().getOrg().getId());
		customHandoverList.setHandoverFromPos(getUserObject().getPosition().getId());
		
		service.saveHandoverInfo(customHandoverList, getUserObject());
		return sendSuccessMessage(customHandoverList.getHandoverCode());
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：交接一览记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomHandoverList customHandoverList = service.getHandoverInfo(id);
		if(customHandoverList==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
		if (!StringUtils.isEmpty(customHandoverList.getCustomId())) {
			CustomInfo customInfo = customService.getCustomInfo(customHandoverList.getCustomId());
			model.addAttribute("customInfo",customInfo);
		}
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if (hasPermission("P02HandoverPendingPage")) {
			tabMain.addTab("基础信息", "/custom/handover/base.html?id=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoverCustomerPage")) {
			tabMain.addTab("客户信息交接", "/custom/handover/custom.html?id=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoverBizsPage")) {
			tabMain.addTab("商机信息交接", "/custom/handover/bizopp.html?id=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoverQuotePage")) {
			tabMain.addTab("报价信息交接", "/custom/handover/quot.html?id=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoverContractPage")) {
			tabMain.addTab("合同信息交接", "/custom/handover/contract.html?id=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoveOrderPage")) {
			tabMain.addTab("订单信息交接", "/custom/handover/order.html?id=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoverT7TeamMemberPage")) {
			tabMain.addTab("销售团队", "/team/list.html?businessType=" + IConstants.CUSTOM_HANDOVER_PROC + "&businessId=" + customHandoverList.getId());
		}
		if (hasPermission("P02HandoverT8ProReviewHistoryPage")) {
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+customHandoverList.getId());
		}
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("customHandoverList",customHandoverList);
		model.addAttribute("mode", IConstants.ACTION_TYPE_EDIT);
		return forward("add");
	}
	
	@LogOperate(module = "客户管理模块：交接", notes = "${user}点击基础信息列表")
	@RequestMapping("/base")
	public String base(String id, Model model) {
		CustomHandoverList customHandoverList = new CustomHandoverList();
		if (!StringUtils.isEmpty(id)) {
			customHandoverList = service.getHandoverInfo(id);
		}

		model.addAttribute("customHandoverList", customHandoverList);
		return forward("tabDiv");
	}
	
	@LogOperate(module = "客户管理模块：交接", notes = "${user}点击客户信息交接")
	@RequestMapping("/custom")
	public String custom(String id, Model model) {
		CustomHandoverList customHandoverList = new CustomHandoverList();
		if (!StringUtils.isEmpty(id)) {
			customHandoverList = service.getHandoverInfo(id);
		}
		
		boolean checkDisplay = false;
		
		if (hasPermission("P02HandoverT2CusHandover")) {
			checkDisplay = true;
		}
		
		if (checkDisplay) {
			if (StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, customHandoverList.getHandoverStatus())
					|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, customHandoverList.getHandoverStatus())) {
				checkDisplay = true;
			} else {
				checkDisplay = false;
			}
		}
		model.addAttribute("checkDisplay", String.valueOf(checkDisplay));
		model.addAttribute("customHandoverList", customHandoverList);
		return forward("custom");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：待交接客户一览")
	@ResponseBody
	@RequestMapping("/customPage")
	public String customPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customCode", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("customFullName", "like", "%" + searchKey + "%");
		}
//		condition.getFilterObject().addCondition("businessStatus", "eq", "COMMONYN_N");
		
		condition.getFilterObject().addCondition("businessId", "eq", condition.getStringCondition("id"));
		
		IPage p = service.queryHandoverCustomInfo(condition);
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接客户")
	@RequestMapping("/customHandoverCheck")
	public String customHandoverCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverCustomInfo(getUserObject(), ids, "COMMONYN_Y");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接客户")
	@RequestMapping("/customHandoverUncheck")
	public String customHandoverUncheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverCustomInfo(getUserObject(), ids, "COMMONYN_N");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@LogOperate(module = "客户管理模块：交接", notes = "${user}点击商机信息交接")
	@RequestMapping("/bizopp")
	public String bizopp(String id, Model model) {
		CustomHandoverList customHandoverList = new CustomHandoverList();
		if (!StringUtils.isEmpty(id)) {
			customHandoverList = service.getHandoverInfo(id);
		}
		boolean checkDisplay = false;
		
		if (hasPermission("P02HandoverT3BizOppoHandover")) {
			checkDisplay = true;
		}
		
		if (checkDisplay) {
			if (StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, customHandoverList.getHandoverStatus())
					|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, customHandoverList.getHandoverStatus())) {
				checkDisplay = true;
			} else {
				checkDisplay = false;
			}
		}
		model.addAttribute("checkDisplay", String.valueOf(checkDisplay));
		model.addAttribute("customHandoverList", customHandoverList);
		return forward("bizopp");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：待交接商机一览")
	@ResponseBody
	@RequestMapping("/bizoppPage")
	public String bizoppPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("clientId", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("clientName", "like", "%" + searchKey + "%");
		}
//		condition.getFilterObject().addCondition("businessStatus", "eq", "COMMONYN_N");
		condition.getFilterObject().addCondition("businessId", "eq", condition.getStringCondition("id"));
		IPage p = service.queryHandoverBusinessOpportunity(condition);
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接商机")
	@RequestMapping("/bizoppHandoverCheck")
	public String bizoppHandoverCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverBusinessOpportunity(getUserObject(), ids, "COMMONYN_Y");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接商机")
	@RequestMapping("/bizoppHandoverUnCheck")
	public String bizoppHandoverUnCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverBusinessOpportunity(getUserObject(), ids, "COMMONYN_N");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@LogOperate(module = "客户管理模块：交接", notes = "${user}点击报价信息交接")
	@RequestMapping("/quot")
	public String quot(String id, Model model) {
		CustomHandoverList customHandoverList = new CustomHandoverList();
		if (!StringUtils.isEmpty(id)) {
			customHandoverList = service.getHandoverInfo(id);
		}
		boolean checkDisplay = false;
		
		if (hasPermission("P02HandoverT4QuoteHandover")) {
			checkDisplay = true;
		}
		
		if (checkDisplay) {
			if (StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, customHandoverList.getHandoverStatus())
					|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, customHandoverList.getHandoverStatus())) {
				checkDisplay = true;
			} else {
				checkDisplay = false;
			}
		}
		model.addAttribute("checkDisplay", String.valueOf(checkDisplay));
		model.addAttribute("customHandoverList", customHandoverList);
		return forward("quot");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：待交接报价一览")
	@ResponseBody
	@RequestMapping("/quotPage")
	public String quotPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customerCode", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("customerName", "like", "%" + searchKey + "%");
		}
//		condition.getFilterObject().addCondition("businessStatus", "eq", "COMMONYN_N");
		condition.getFilterObject().addCondition("businessId", "eq", condition.getStringCondition("id"));
		IPage p = service.queryHandoverQuot(condition);
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接报价")
	@RequestMapping("/quotHandoverCheck")
	public String quotHandoverCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverQuot(getUserObject(), ids, "COMMONYN_Y");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接报价")
	@RequestMapping("/quotHandoverUnCheck")
	public String quotHandoverUnCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverQuot(getUserObject(), ids, "COMMONYN_N");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@LogOperate(module = "客户管理模块：交接", notes = "${user}点击合同信息交接")
	@RequestMapping("/contract")
	public String contract(String id, Model model) {
		CustomHandoverList customHandoverList = new CustomHandoverList();
		if (!StringUtils.isEmpty(id)) {
			customHandoverList = service.getHandoverInfo(id);
		}
		boolean checkDisplay = false;
		
		if (hasPermission("P02HandoverT5ConHandover")) {
			checkDisplay = true;
		}
		
		if (checkDisplay) {
			if (StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, customHandoverList.getHandoverStatus())
					|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, customHandoverList.getHandoverStatus())) {
				checkDisplay = true;
			} else {
				checkDisplay = false;
			}
		}
		model.addAttribute("checkDisplay", String.valueOf(checkDisplay));
		model.addAttribute("customHandoverList", customHandoverList);
		return forward("contract");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：待交接合同一览")
	@ResponseBody
	@RequestMapping("/contractPage")
	public String contractPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("custCode", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("custName", "like", "%" + searchKey + "%");
		}
//		condition.getFilterObject().addCondition("businessStatus", "eq", "COMMONYN_N");
		condition.getFilterObject().addCondition("businessId", "eq", condition.getStringCondition("id"));
		IPage p = service.queryHandoverContract(condition);
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接合同")
	@RequestMapping("/contractHandoverCheck")
	public String contractHandoverCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverContract(getUserObject(), ids, "COMMONYN_Y");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择交接合同")
	@RequestMapping("/contractHandoverUnCheck")
	public String contractHandoverUnCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverContract(getUserObject(), ids, "COMMONYN_N");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@LogOperate(module = "客户管理模块：交接", notes = "${user}点击订单信息交接")
	@RequestMapping("/order")
	public String order(String id, Model model) {
		CustomHandoverList customHandoverList = new CustomHandoverList();
		if (!StringUtils.isEmpty(id)) {
			customHandoverList = service.getHandoverInfo(id);
		}
		boolean checkDisplay = false;
		
		if (hasPermission("P02HandoverT6OrderHandover")) {
			checkDisplay = true;
		}
		
		if (checkDisplay) {
			if (StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_10, customHandoverList.getHandoverStatus())
					|| StringUtil.equals(IConstants.CUSTOM_NORMAL_STATUS_30, customHandoverList.getHandoverStatus())) {
				checkDisplay = true;
			} else {
				checkDisplay = false;
			}
		}
		model.addAttribute("checkDisplay", String.valueOf(checkDisplay));
		model.addAttribute("customHandoverList", customHandoverList);
		return forward("order");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：待交接订单一览")
	@ResponseBody
	@RequestMapping("/orderPage")
	public String orderPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customerCode", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("customerName", "like", "%" + searchKey + "%");
		}
//		condition.getFilterObject().addCondition("businessStatus", "eq", "COMMONYN_N");
		condition.getFilterObject().addCondition("businessId", "eq", condition.getStringCondition("id"));
		IPage p = service.queryHandoverOrderHeader(condition);
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择订单报价")
	@RequestMapping("/orderHandoverCheck")
	public String orderHandoverCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverOrderHeader(getUserObject(), ids, "COMMONYN_Y");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@ResponseBody
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：选择订单报价")
	@RequestMapping("/orderHandoverUnCheck")
	public String orderHandoverUnCheck(@RequestParam(value = "ids[]")String[] ids){
		service.updateHandoverOrderHeader(getUserObject(), ids, "COMMONYN_N");
		return sendSuccessMessage("交接状态更新成功！");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：交接一览记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomHandoverList customHandoverList){
		
//		if (StringUtils.equals(customHandoverList.getHandoverFrom(), customHandoverList.getHandoverTo())) {
//			throw new AnneException("交接人相同，不能合并!");
//		}
		
		// 功能字段设值
		// 更新字段
		customHandoverList.setUpdatedById(getUserObject().getEmployee().getId());
		customHandoverList.setUpdatedAt(new Date());
		service.updateHandoverInfo(customHandoverList ,IConstants.CUSTOM_NORMAL_STATUS_10);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：交接",notes="${user}页面：交接一览记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteHandoverInfo(id);
		return sendSuccessMessage();
	}
	
	@LogOperate(module = "客户管理模块：客户交接", notes = "${user}工作流：客户交接申请")
	@ResponseBody
	@RequestMapping("/handoverInfo")
	public String handoverInfo(CustomHandoverList customHandoverList, String customCode , String id) {
		
		
		long cnt = service.queryReceiptList(getUserObject(), id, customHandoverList.getCustomId(), customCode, customHandoverList.getHandoverFrom());
		
		if (cnt == 0) {
			return sendSuccessMessage("没有待交接数据");
		} else {
			return sendSuccessMessage("抽取操作成功");
		}
		
	}
	
}
