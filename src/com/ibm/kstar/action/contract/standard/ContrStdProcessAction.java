package com.ibm.kstar.action.contract.standard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.model.HistoryProcessInstance;

import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.entity.quot.KstarPrjEvl;

@Controller
@RequestMapping("/contrstdprocess")
public class ContrStdProcessAction extends BaseFlowAction {

	@Autowired
	private IContractService contractService;
	@Autowired
	private ILovMemberService lovMemberService;
	@Autowired
	IHistoryService historyService;

//	@RequestMapping("/processcontrstdreview")
//	public String process(String id,String taskId,Model model) throws Exception {
//		Task task = taskService.getTask(taskId);
//		ProcessInstance pi = processService.get(task.getProcessInstanceId());
//		Map<String,String> map = processService.getBusinessVariable(pi.getId());
//		model.addAttribute("title",map.get("title"));
//		model.addAttribute("processID",pi.getId());
//		
//		//***********************common**************************************
//		getHistory(taskId, model);
//		//***********************common**************************************
//		return forward("process");
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/processcontrstdreview",method=RequestMethod.POST)
//	public String process(HttpServletRequest request,Model model){
//		UserObject user = getUserObject();
//		boolean flag = process(request,user);
//		String taskId = request.getParameter("taskId");
//		String comment = request.getParameter("comment");
//		if(flag){
//			//productProcess.delete(request.getParameter("id"));
////			productService.endPreSaleProcess(request.getParameter("processID"));
//			String bizId=request.getParameter("id");
//			List<HistoryActivityInstance> list = getHistoryByBusinessKey("bb",bizId);
//			HistoryActivityInstance hai = list.get(list.size()-1);
//			KstarPrjEvl prjevl = contractService.getKstarPrjEvl(bizId);
////			prjevl.setEvlMm(hai.getOperatorName());
//			prjevl.setEvlSt(hai.getStatus());
////			prjevl.setSbmDt(hai.getEndTime());
////			prjevl.setEvlRs(hai.getActivityName());
//			prjevl.setEvlMm(user.getEmployee().getName());
//			prjevl.setSbmDt(new Date());	
//			prjevl.setEvlSg(comment);		
//
//			String submitType = request.getParameter("submitType");
//			if("1".equals(submitType)){
////				prjevl.setEvlSt("已审批");
//				prjevl.setEvlRs("同意");
//			}else if ("2".equals(submitType)){
////				prjevl.setEvlSt("");
//				prjevl.setEvlRs("建议带条件通过");
//			}else if ("3".equals(submitType)){
//				prjevl.setEvlRs("建议不通过");
//			}else if ("4".equals(submitType)){
//				prjevl.setEvlRs("不涉及");
//			}else if ("5".equals(submitType)){
//				prjevl.setEvlRs("不同意");
//			}
//			
//			contractService.updatePrjEvl(prjevl);
//			
//		}
//		
//		return sendSuccessMessage();
//	}
//	
//	@RequestMapping("/proctrial")
//	public String processTrial(String id,String taskId,Model model) throws Exception {
//		Task task = taskService.getTask(taskId);
//		ProcessInstance pi = processService.get(task.getProcessInstanceId());
//		Map<String,String> map = processService.getBusinessVariable(pi.getId());
//		model.addAttribute("title",map.get("title"));
//		model.addAttribute("processID",pi.getId());
//		
//		//***********************common**************************************
//		getHistory(taskId, model);
//		//***********************common**************************************
//			
//		
//		return forward("process");
//	} 
//	
//	@ResponseBody
//	@RequestMapping(value="/proctrial",method=RequestMethod.POST)
//	public String processTrial(HttpServletRequest request,Model model){
//		UserObject user = getUserObject();;
//		boolean flag = process(request,user);
//		if(flag){
//			//productProcess.delete(request.getParameter("id"));
////			productService.endPreSaleProcess(request.getParameter("processID"));
//		}
//		
//		return sendSuccessMessage();
//	}
	
	@RequestMapping(value = "/historyView")
	public String getHistoryView(String id,Model model) throws Exception{
//		String businessKey=id;
		String module = "";	//"cc"	;
		KstarPrjEvl prje = contractService.getKstarPrjEvl(id);
		String evlTp = prje.getEvlTyp(); // 合同评审类型
		String bizTp = prje.getCType(); //
		String businessKey=prje.getQuotCode();
		String appName=contractService.getAppnameByType(evlTp,bizTp);
		module= lovMemberService.getFlowCodeByAppCode(appName);
		
		// 通过 businessId , module 获取审批历史
		HistoryProcessInstance pi = historyService.findLastProcessInstance(module, businessKey);
		
		history(model, pi.getId());
//		com/ibm/kstar/action/flowHistory
		return forward("../../flowHistory");	
	}
	
	@RequestMapping(value = "/flowDesign")
	public String getFlowDesign(String id,Model model) throws Exception{
//		String businessKey=id;
		String module = "";	//"cc"	;
		KstarPrjEvl prje = contractService.getKstarPrjEvl(id);
		String evlTp = prje.getEvlTyp(); // 合同评审类型
		String bizTp = prje.getCType(); //
		String businessKey=prje.getQuotCode();
		String appName=contractService.getAppnameByType(evlTp,bizTp);
		module= lovMemberService.getFlowCodeByAppCode(appName);
		
		// 通过 businessId , module 获取审批历史
		HistoryProcessInstance pi = historyService.findLastProcessInstance(module, businessKey);
		
		history(model, pi.getId());
		return forward("../../history");	
	}
	
}
