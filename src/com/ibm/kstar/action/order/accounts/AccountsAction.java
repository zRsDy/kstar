package com.ibm.kstar.action.order.accounts;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IAccountsService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.AccountsMaster;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/accounts")
public class AccountsAction extends BaseAction {
	@Autowired
	IAccountsService accountsService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IOrderService orderService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("accountsCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proposerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("companyName", "like", "%"+searchKey+"%");
			
		}
		String accountsCode = condition.getStringCondition("accountsCode");
		if(StringUtil.isNotEmpty(accountsCode)){
			condition.getFilterObject().addCondition("accountsCode", "like",  "%"+accountsCode+"%");
		}
		String proposerName = condition.getStringCondition("proposerName");
		if(StringUtil.isNotEmpty(proposerName)){
			condition.getFilterObject().addCondition("proposerName", "like",  "%"+proposerName+"%");
		}
		String companyId = condition.getStringCondition("companyId");
		if(StringUtil.isNotEmpty(companyId)){
			condition.getFilterObject().addCondition("companyId", "eq",  companyId);
		}
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "eq",  status);
		}
		String controlStatus = condition.getStringCondition("controlStatus");
		if(StringUtil.isNotEmpty(controlStatus)){
			condition.getFilterObject().addCondition("controlStatus", "eq",  controlStatus);
		}
		String applyDateStart = condition.getStringCondition("applyDateStart");
		if(StringUtil.isNotEmpty(applyDateStart)){
			condition.getFilterObject().addCondition("applyDate", ">=",  applyDateStart);
		}
		String applyDateEnd = condition.getStringCondition("applyDateEnd");
		if(StringUtil.isNotEmpty(applyDateEnd)){
			condition.getFilterObject().addCondition("applyDate", "<=",  applyDateEnd);
		}
		
		
		IPage p = accountsService.queryAccountsMasters(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("accounts_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		accountsService.deleteAccountsMaster(id);
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		AccountsMaster accountsMaster = new AccountsMaster();
		UserObject user = getUserObject();
		
		LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "ACCOUNTS");
		String code = "";
		String prefix = "KST-ZQ-";
		if(lov != null){
			prefix = lov.getName();
		}
		code = orderService.getSequenceCode("gen_accounts_code",prefix);
		
		accountsMaster.setAccountsCode(code);
		accountsMaster.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		accountsMaster.setControlStatus(IConstants.ORDER_CONTROL_STATUS_10);
		accountsMaster.setProposerId(user.getEmployee().getId());
		accountsMaster.setProposerName(user.getEmployee().getNo() + "|" + user.getEmployee().getName());
		accountsMaster.setApplyDate(new Date());
		model.addAttribute("accountsMaster", accountsMaster);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		
		if(hasPermission("P06PaymentDaysT1DetailPage")){
			tabMain.addTab("账期申请明细", "/accounts/accountsDetailList.html?accountsCode="+accountsMaster.getAccountsCode());
		}
		if(hasPermission("P06PaymentDaysT2ProReviewHistoryPage")){
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+accountsMaster.getId());
		}
		model.addAttribute("tabMain",tabMain);
		
		return forward("accounts_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(AccountsMaster accountsMaster) throws Exception {
		accountsService.saveAccounts(accountsMaster,getUserObject());
		return sendSuccessMessage();
	}
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		AccountsMaster accountsMaster = accountsService
				.getAccountsMaster(id);
		if (accountsMaster == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		
		model.addAttribute("accountsMaster", accountsMaster);
		model.addAttribute("accountsCode", accountsMaster.getAccountsCode());
		model.addAttribute("status",accountsMaster.getStatus());
		model.addAttribute("proposerId", accountsMaster.getProposerId());
		model.addAttribute("proposerName", accountsMaster.getProposerName());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(accountsMaster.getApplyDate() != null ){
			model.addAttribute("applyDate",sdf.format(accountsMaster.getApplyDate()));
		}

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		if(hasPermission("P06PaymentDaysT1DetailPage")){
			tabMain.addTab("账期申请明细", "/accounts/accountsDetailList.html?accountsCode="+accountsMaster.getAccountsCode());
		}
		if(hasPermission("P06PaymentDaysT2ProReviewHistoryPage")){
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+accountsMaster.getId());
		}
		model.addAttribute("tabMain",tabMain);
		
		return forward("accounts_info");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(AccountsMaster accountsMaster) throws Exception {
		accountsService.updateAccounts(accountsMaster,getUserObject());
		return sendSuccessMessage();
	}
	
	
	
	@RequestMapping("/accountsDetailList")
	public String accountsDetailList(String accountsCode, Model model) {
		model.addAttribute("accountsCode",accountsCode);
		return forward("accounts_detail_list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/accountsLines")
	public String getAccountsLines(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String accountsCode = condition.getStringCondition("accountsCode");
		condition.getFilterObject().addCondition("accountsCode", "eq", accountsCode);
		IPage p = accountsService.queryAccountsDetail(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping("/updateStatus")
	public String updateStatus(HttpServletRequest request, HttpServletResponse response){
		String accountsId = request.getParameter("id");
		String op = request.getParameter("op");
		UserObject userObject = getUserObject();
		if("submit".equals(op)){
			//更新控制状态
			accountsService.updateControlStatus(accountsId, IConstants.ORDER_CONTROL_STATUS_20,userObject);
		}else if("delay".equals(op)){
			//申请延期
			accountsService.updateStatus(accountsId,IConstants.ACCOUNTS_STATUS_20,userObject);
		}
		return sendSuccessMessage("操作成功");
	}
	
}