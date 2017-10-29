package com.ibm.kstar.action.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;

import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.api.product.IAuTestService;
import com.ibm.kstar.api.product.IDocService;
import com.ibm.kstar.api.product.IEcrService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductDoc;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/productprocess")
public class ProductProcessAction extends BaseFlowAction {

	@Autowired
	IProductService productService;
	
	@Autowired
	IProcessService processService;
	
	@Autowired
	IEcrService ecrService;
	
	@Autowired
	IDocService docService;
	
	@Autowired
	ITaskService taskService;
	
	@Autowired
	IAuTestService auTestService;

	@ResponseBody
	@RequestMapping(value = "/startAuTestProcess", method = RequestMethod.POST)
	public String startAuTestProcess(String id,HttpServletRequest request) throws Exception{
		auTestService.startAuTestApplyrocess(getUserObject(), id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/startDocApplyProcess", method = RequestMethod.POST)
	public String startDocApplyProcess(KstarProductDoc rowData,HttpServletRequest request) throws Exception{
		KstarProductDoc kb = (KstarProductDoc)rowData;
		docService.startDocApplyrocess(getUserObject(), kb.getId());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/startEcrProcess", method = RequestMethod.POST)
	public String startEcrProcess(String id,HttpServletRequest request) throws Exception{
		ecrService.startEcrrocess(getUserObject(), id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/startPreSaleProcess", method = RequestMethod.POST)
	public String startPreSaleProcess(@RequestParam(value = "ids[]")String[] ids) throws Exception{
		productService.startPreSaleProcess(getUserObject(), ids);
		return sendSuccessMessage();
	}
	
	@NoRight
	@RequestMapping("/startCsaleProcess")
	public String startCsaleProcess(String ids, Model model) {
		model.addAttribute("ids", ids);
		return forward("csale_submit");
	}
	
	@ResponseBody
	@RequestMapping(value = "/startCsaleProcess", method = RequestMethod.POST)
	public String startCsaleProcess(String ids, String csaleStatus, String csaleReason) throws Exception{
		String[] productIds = ids.split(",");
		productService.startCsaleProcess(getUserObject(), productIds, csaleStatus, csaleReason);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/startPublishProcess", method = RequestMethod.POST)
	public String startPublishProcess(String ids,HttpServletRequest request) throws Exception{
		String[] _ids = ids.split(",");
		productService.startPublishProcess(getUserObject(), _ids);
		return sendSuccessMessage();
	}
		
	@RequestMapping("/process")
	public String process(String id,String taskId,Model model) throws Exception {
		Task task = taskService.getTask(taskId);
		ProcessInstance pi = processService.get(task.getProcessInstanceId());
		Map<String,String> map = processService.getBusinessVariable(pi.getId());
		model.addAttribute("title",map.get("title"));
		model.addAttribute("processID",pi.getId());
		getHistory(taskId, model);
		return forward("process");
	}
	
	@ResponseBody
	@RequestMapping(value="/process",method=RequestMethod.POST)
	public String process(HttpServletRequest request,Model model){
		UserObject user = getUserObject();;
		boolean flag = process(request,user);
		if(flag){
			//productProcess.delete(request.getParameter("id"));
			productService.endPreSaleProcess(request.getParameter("processID"));
		}
		
		return sendSuccessMessage();
	}
}
