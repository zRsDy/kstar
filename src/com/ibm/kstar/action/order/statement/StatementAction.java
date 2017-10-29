package com.ibm.kstar.action.order.statement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IInvoiceService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.order.IStatementService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.StatementMaster;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/statement")
public class StatementAction extends BaseAction {
	@Autowired
	IStatementService statementService ;
	@Autowired
	IOrderService orderService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IInvoiceService invoiceService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("statementCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proposerName", "like", "%"+searchKey+"%");
		}
		String statementCode = condition.getStringCondition("statementCode");
		if(StringUtil.isNotEmpty(statementCode)){
			condition.getFilterObject().addCondition("statementCode", "like",  "%"+statementCode+"%");
		}
		String proposerName = condition.getStringCondition("proposerName");
		if(StringUtil.isNotEmpty(proposerName)){
			condition.getFilterObject().addCondition("proposerName", "like",  "%"+proposerName+"%");
		}
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "eq",  status);
		}
		String applyDateStart = condition.getStringCondition("applyDateStart");
		if(StringUtil.isNotEmpty(applyDateStart)){
			condition.getFilterObject().addCondition("applyDate", ">=",  applyDateStart);
		}
		String applyDateEnd = condition.getStringCondition("applyDateEnd");
		if(StringUtil.isNotEmpty(applyDateEnd)){
			condition.getFilterObject().addCondition("applyDate", "<=",  applyDateEnd);
		}
		
		IPage p = statementService.queryStatementMasters(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("statement_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		statementService.deleteStatementMaster(id);
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		UserObject userObject = getUserObject();
		StatementMaster statementMaster = new StatementMaster();
		
		LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "STATEMENT");
		String code = "";
		String prefix = "KST-DZ-";
		if(lov != null){
			prefix = lov.getName();
		}
		code = orderService.getSequenceCode("gen_statement_code",prefix);
		
		statementMaster.setStatementCode(code);
		statementMaster.setProposerId(userObject.getEmployee().getId());
		statementMaster.setProposerName(userObject.getEmployee().getName());
		statementMaster.setApplyDate(new Date());
		statementMaster.setStatus(IConstants.ORDER_CONTROL_STATUS_10);
		Calendar calendar = Calendar.getInstance();
		String nowMomth = new SimpleDateFormat( "yyyy-MM").format(calendar.getTime());
		calendar.add(Calendar.MONTH, -1);
		String beforeMomth = new SimpleDateFormat( "yyyy-MM").format(calendar.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			statementMaster.setStatementDateBegin(sdf.parse( beforeMomth + "-26"));
			statementMaster.setStatementDateEnd(sdf.parse(nowMomth+"-25"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		statementMaster.setIsPostAccount(IConstants.YES_Yes);
		
		model.addAttribute("statementMaster", statementMaster);
		return forward("statement_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(StatementMaster statementMaster) {
		statementService.saveStatementMaster(statementMaster,getUserObject());
		return sendSuccessMessage();
	}
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		StatementMaster statementMaster = statementService.getStatementMaster(id);
		if (statementMaster == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("statementMaster", statementMaster);
		return forward("statement_info");
	}
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(StatementMaster statementMaster) {
		// 更新字段
		statementMaster.setUpdatedById(getUserObject().getEmployee().getId());
		statementMaster.setUpdatedAt(new Date());
		statementService.updateStatementMaster(statementMaster);
		return sendSuccessMessage();
	}
	
	/**
	 * 
	 * getStatementInvoice:查询对账单发票主表信息. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/statementInvoiceMaster")
	public String getStatementInvoiceMaster(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		IPage p = statementService.queryStatementInvoiceMaster(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/statementInvoiceDetail")
	public String getStatementInvoiceDetail(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		IPage p = statementService.queryStatementInvoiceDetail(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/statementDeliveryLines")
	public String getStatementDeliveryLines(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		IPage p = statementService.queryStatementDeliveryLines(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/statementReceipts")
	public String getStatementReceipts(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		IPage p = statementService.queryStatementReceipts(condition);
		return sendSuccessMessage(p);
	}
	/**
	 * 
	 * publish:发布对账单. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/publish")
	public String publish(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		UserObject userObject = getUserObject();
		statementService.publish(id, userObject);
		return sendSuccessMessage("操作成功");
	}
	/**
	 * 
	 * sumbit:提交对账单. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/sumbit")
	public String sumbit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		UserObject userObject = getUserObject();
		statementService.updateStatus(id,IConstants.ORDER_CONTROL_STATUS_20,userObject);
		return sendSuccessMessage("操作成功");
	}
	
}