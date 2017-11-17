//package com.ibm.kstar.action.contract.pi;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.xsnake.xflow.api.IProcessService;
//import org.xsnake.xflow.api.ITaskService;
//import org.xsnake.xflow.api.model.HistoryActivityInstance;
//import org.xsnake.xflow.api.model.ProcessInstance;
//import org.xsnake.xflow.api.model.Task;
//
//import com.ibm.kstar.action.BaseFlowAction;
//import com.ibm.kstar.api.contract.IContractService;
//import com.ibm.kstar.api.product.IAuTestService;
//import com.ibm.kstar.api.product.IDocService;
//import com.ibm.kstar.api.product.IEcrService;
//import com.ibm.kstar.api.product.IProductService;
//import com.ibm.kstar.api.system.lov.entity.LovMember;
//import com.ibm.kstar.api.system.permission.UserObject;
//import com.ibm.kstar.cache.CacheData;
//import com.ibm.kstar.entity.product.KstarEcrBean;
//import com.ibm.kstar.entity.product.KstarProductAuTest;
//import com.ibm.kstar.entity.product.KstarProductDoc;
//import com.ibm.kstar.entity.quot.KstarPrjEvl;
//
//@Controller
//@RequestMapping("/contrpiprocess")
//public class ContrPiProcessAction extends BaseFlowAction {
//
//	@Autowired
//	private IContractService contractService;
//	
//	@Autowired
//	IProcessService processService;
//	
//	@Autowired
//	IEcrService ecrService;
//	
//	@Autowired
//	IDocService docService;
//	
//	@Autowired
//	ITaskService taskService;
//	
//	@Autowired
//	IAuTestService auTestService;
//
//	@RequestMapping("/processcontrpireview")
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
//	@RequestMapping(value="/processcontrpireview",method=RequestMethod.POST)
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
//	@RequestMapping("/procpitrial")
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
//		return forward("process");
//	} 
//	
//}
