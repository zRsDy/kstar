/**
 * 
 */
package com.ibm.kstar.action.contract.standard.history;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.xflow.api.IDefinitionService;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/history")
//@Scope("prototype")
public class ContractStandardHistoryAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";

	@Autowired
	IDefinitionService definitionService;
	
	@Autowired
	IProcessService processService;
	
	@Autowired
	ITaskService taskService;
	
	@Autowired
	IHistoryService historyService;

//	@ResponseBody
//	@RequestMapping(value = "/page")
//	public String page(PageCondition condition, HttpServletRequest request) {
//		ActionUtil.requestToCondition(condition, request); 
//		condition.setCondition("contrId", request.getParameter("contrId")); 
//		String contrId = condition.getStringCondition("contrId");  
//		condition.getFilterObject().addCondition("contrId", "eq", contrId);
//		
////		IPage p = contractAddrService.query(condition);
//		List<HistoryActivityInstance> list = contractService.getContrFinReviewHisLst("aa", contrId);
//		PageImpl p  = new PageImpl(list, 1,30,0); 
//		return sendSuccessMessage(p);
//	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/contrStdFlowHisPage")
	public String contrStdFlowHisPage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String contrId = condition.getStringCondition("contrId");
		UserObject user = getUserObject();
		Map<String,String> varmap = new HashMap<>();
		varmap.put("businessKey", contrId);
//		varmap.put("module", "CONTR_STAND_TRIAL_PROC");
//		IPage p = historyService.history(user.getParticipant(),varmap,condition.getRows(),condition.getPage());
		// 修复业务单据非发起人看不到审批历史的问题
		IPage p = historyService.findProcessInstance(varmap,condition.getRows(),condition.getPage());
		return sendSuccessMessage(p);
	}
	
	
	

}
