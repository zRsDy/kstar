/**
 * 
 */
package com.ibm.kstar.action.contract.standard.member;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.quot.KstarMemInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/member")
//@Scope("prototype")
public class ContractMemberAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";

	@Autowired
	private IContractService contractService;

//	@Autowired
//	private IContrChangeService contrChangeService;
//
//	@Autowired
//	private IContrAddrService contrAddrService;
//
//	@Autowired
//	private IContrPayService contrPayService;
//
//	@Autowired
//	private IContrClauseService contrClauseService;
//
//	@RequestMapping("/index")
//	public String index(String id, Model model) {
//
//		return forward("index");
//	}
//
//	@RequestMapping("/change")
//	public String change(String id, Model model) {
//		return forward("orderList");
//	}
//
//	@RequestMapping("/loan")
//	public String loan(String id, Model model) {
//		return forward("loan");
//	}
//
//	@RequestMapping("/inter")
//	public String inter(String id, Model model) {
//		return forward("inter");
//	}
//
//	@RequestMapping("/candidateTaskList")
//	public String claimTaskList(String id, Model model) {
//		return forward("candidateTaskList");
//	}
//
//	@RequestMapping("/assigneeTaskList")
//	public String completeTaskList(String id, Model model) {
//		return forward("assigneeTaskList");
//	}
//
////	@ResponseBody
////	@RequestMapping("/page")
////	public String page(PageCondition condition, HttpServletRequest request) {
////		ActionUtil.requestToCondition(condition, request);
////		IPage p = contractService.query(condition);
////
////		return sendSuccessMessage(p);
////	}
//
//	@RequestMapping("/tabs")
//	public String tabs(String id, String contrCode, Model model) {
//		model.addAttribute("id", id);
//		model.addAttribute("contrCode", contrCode);
//
//		System.out.println("------------" + id);
//
//		if (id == null) {
//			throw new AnneException("没有找到数据");
//		}
//		Contract contract = contractService.get(id);
//		if (contract == null) {
//			throw new AnneException("没有找到数据");
//		}
//		model.addAttribute("contr", contract);
//
//		return forward("tabs");
//	}
//
////	@RequestMapping("/add")
////	public String add(Model model) {
////		return forward("contract");
////	}
//
//	@RequestMapping("/info1")
//	public String info(String id, Model model) {
//		model.addAttribute("id", id);
//		Contract contr = contractService.get(id);
//		model.addAttribute("contr", contr);
//		return forward("info1");
//	}
//
//	@RequestMapping("/edit")
//	public String edit(String id, Model model) {
//		if (id == null) {
//			throw new AnneException("没有找到需要修改的数据");
//		}
//		Contract contract = contractService.get(id);
//		if (contract == null) {
//			throw new AnneException("没有找到需要修改的数据");
//		}
//		model.addAttribute("contractInfo", contract);
//		return forward("add");
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/edit", method = RequestMethod.POST)
//	public String edit(Contract contract) {
//		contractService.update(contract);
//		return sendSuccessMessage();
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/delete", method = RequestMethod.POST)
//	public String delete(String id) {
//		contractService.delete(id);
//		return sendSuccessMessage();
//	}
//
//	@RequestMapping("/att")
//	public String atts(String id, Model model) {
//		model.addAttribute("id", id);
//		return forward("att");
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/attPage")
//	public String pageAtt(PageCondition condition, HttpServletRequest request) {
//		ActionUtil.requestToCondition(condition, request);
//		condition.setCondition("id", request.getParameter("id"));
//		IPage p = contractService.queryAtt(condition);
//
//		return sendSuccessMessage(p);
//	}
//
//	@RequestMapping("/reviewList")
//	public String reviewList(String processInstanceId, Model model) {
//		model.addAttribute("processInstanceId", processInstanceId);
//		return forward("reviewList");
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/reviewPage")
//	public String reviewPage(String processInstanceId, PageCondition condition, HttpServletRequest request) throws Exception {
//		ActionUtil.requestToCondition(condition, request);	
//		
//		IPage p = WorkflowService.getHistoryById(processInstanceId, condition);
//
//		return sendSuccessMessage(p);
//	}
//
//	/**
//	 * 启动流程
//	 *
//	 * @param contract
//	 * @return
//	 * @throws IOException
//	 * 
//	 */
//	@RequestMapping(value = "/startFlow")
//	public String startflow(String id) throws IOException {
//
//		List<Map<String, Object>> variables = new ArrayList<Map<String, Object>>();
//
//		Map<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("name", "preFlag");
//		map1.put("value", "1");
//		
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("name", "serverFlag");
//		map2.put("value", "1");
//		
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("name", "presale");
//		map3.put("value", "sales");
//				
//		Map<String, Object> map4 = new HashMap<String, Object>();
//		map4.put("name", "server");
//		map4.put("value", "marketing");
//		
//
//		variables.add(map1);
//		variables.add(map2);
//		variables.add(map3);
//		variables.add(map4);
//
//		String processInstanceId = WorkflowService.startWorkflow(CONTRACT_PROCESS_KEY, id, variables);
//
//		if (processInstanceId != null) {
//
//			Contract contract = contractService.get(id);
//			contract.setProcessInstanceId(processInstanceId);
//
//			contract.setCreator("admin");
//
//			// contractService.update(contract);
//
//		}
//
//		return sendSuccessMessage("成功启动合同流程,流程ID是" + processInstanceId);
//	}
//
//	/**
//	 * 任务列表
//	 *
//	 * @param leave
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping("/candidateTaskPage")
//	public String candidateTaskPage(PageCondition condition, HttpServletRequest request) throws Exception {
//
//		ActionUtil.requestToCondition(condition, request);
//
//		List<Contract> contrList = new ArrayList<Contract>();
//
//		String userId = "gonzo";
//
//		PageImpl page = (PageImpl) WorkflowService.getCandidateTasks(CONTRACT_PROCESS_KEY, userId, condition);
//
//		if (page != null) {
//			
//		
//		List<Task> tasks = (List<Task>) page.getList();
//
//		System.out.println("----------" + tasks.size());
//
//		for (Task task : tasks) {
//			String processInstanceId = task.getProcessInstanceId();
//			ProcessInstance processInstance = WorkflowService.getInstanceById(processInstanceId);
//
//			String businessKey = processInstance.getBusinessKey();
//
//			System.out.println("-----------------" + businessKey);
//
//			if (businessKey.equals("null")) {
//				throw new AnneException("没有找到业务数据");
//
//			}
//
//			Contract contract = contractService.get(businessKey);
//			contract.setTask(task);
//			contract.setProcessInstance(processInstance);
//			// contract.setProcessDefinition(WorkflowService.getDefinitionById(processInstance.getProcessDefinitionId()));
//			contrList.add(contract);
//		}
//
//		page.setList(contrList);
//		
//	}
//
//		return sendSuccessMessage(page);
//	}
//
//	/**
//	 * 任务列表
//	 *
//	 * @param leave
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping("/assigneeTaskPage")
//	public String assigneeTaskPage(PageCondition condition, HttpServletRequest request) throws Exception {
//
//		ActionUtil.requestToCondition(condition, request);
//
//		List<Contract> contrList = new ArrayList<Contract>();
//
//		String userId = "fozzie";
//
//		PageImpl page = (PageImpl) WorkflowService.getAssigneeTasks(CONTRACT_PROCESS_KEY, userId, condition);
//
//		if (page != null) {
//
//			List<Task> tasks = (List<Task>) page.getList();
//
//			System.out.println("----------" + tasks.size());
//
//			for (Task task : tasks) {
//				String processInstanceId = task.getProcessInstanceId();
//				ProcessInstance processInstance = WorkflowService.getInstanceById(processInstanceId);
//
//				String businessKey = processInstance.getBusinessKey();
//
//				System.out.println("-----------------" + businessKey);
//
//				if (businessKey.equals("null")) {
//					throw new AnneException("没有找到业务数据");
//
//				}
//
//				Contract contract = contractService.get(businessKey);
//				contract.setTask(task);
//				contract.setProcessInstance(processInstance);
//				// contract.setProcessDefinition(WorkflowService.getDefinitionById(processInstance.getProcessDefinitionId()));
//				contrList.add(contract);
//			}
//
//			page.setList(contrList);
//
//		}
//
//		return sendSuccessMessage(page);
//	}
//
//	/**
//	 * 签收任务
//	 * 
//	 * @throws Exception
//	 */
//	@RequestMapping("/claim")
//	public String claim(String id, String taskId, Model model) throws Exception {
//		model.addAttribute("id", id);
//		model.addAttribute("taskId", taskId);
//		
//		Log.error("-------------"+taskId);
//
//		String userId = "kermit";
//
//		if (taskId == null) {
//			throw new AnneException("没有找到任务");
//		}
//
//		int returnCode = WorkflowService.claim(taskId, userId);
//		String message;
//
//		if (returnCode == 200) {
//			message = "签收成功";
//		} else {
//			message = "签收失败";
//		}
//
//		return sendSuccessMessage(message);
//	}
//
//	/**
//	 * 签收任务
//	 * 
//	 * @throws Exception
//	 */
//	@RequestMapping("/handle")
//	public String handle(String id, String taskId, String taskKey, String processInstanceId, Model model) throws Exception {
//		model.addAttribute("id", id);
//		model.addAttribute("taskId", taskId);
//		model.addAttribute("taskKey", taskKey);
//		model.addAttribute("processInstanceId", processInstanceId);
//
//		String userId = "gonzo";
//
//		if (taskId == null) {
//			throw new AnneException("没有找到任务");
//		}
//
//		if (id == null) {
//			throw new AnneException("没有找到合同ID");
//		}
//
//		Contract contract = contractService.get(id);
//
//		if (contract == null) {
//			throw new AnneException("没有找到合同");
//		}
//
//		model.addAttribute("contr", contract);
//
//		return forward("handle");
//	}
//
//	/**
//	 * 完成任务
//	 *
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/complete")
//	public String complete(String id, String taskId, Model model) throws Exception {
//
//		String userId = "kermit";
//
//		List<Map<String, Object>> variables = new ArrayList<Map<String, Object>>();
//		Map<String, Object> variable = new HashMap<String, Object>();
//		variable.put("name", "isPass");
//		variable.put("value", "true");
//		variables.add(variable);
//
//		int returnCode = WorkflowService.complete(taskId, userId, variables);
//
//		String message;
//
//		if (returnCode == 200) {
//			message = "审批成功";
//		} else {
//			message = "审批失败";
//		}
//
//		return sendSuccessMessage(message);
//
//	}
	
	/**
	 * 团队成员
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/member")
	public String members(String id,Model model) {
		model.addAttribute("id",id);
		return forward("member");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page")
	public String pageMem(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId");  
		condition.getFilterObject().addCondition("contrId", "eq", contrId);
		IPage p = contractService.queryMem(condition);

		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String addMem(String quotCode, Model model){
		String contrId = quotCode; 
		Contract contr= contractService.get(contrId);
		model.addAttribute("contr",contr);
		return forward("contractMember");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addMem(KstarMemInfo mem, HttpServletRequest request) { 
		contractService.saveMem(mem);
		return sendSuccessMessage(mem.getQuotCode());
	}	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String editMem(KstarMemInfo mem){
		contractService.updateMem(mem);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String editMem(String id,Model model){
//		if(id==null){
//			throw new AnneException("没有找到需要修改的数据");
//		}
		KstarMemInfo mem = contractService.getKstarMemInfo(id);
//		if(mem==null){
//			throw new AnneException("没有找到需要修改的数据");
//		}
		Contract contr= contractService.get(mem.getQuotCode()); 
		model.addAttribute("contr",contr);
		model.addAttribute("memInfo",mem);
		return forward("contractMember");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String deleteMem(String id){
		contractService.deleteMem(id);
		return sendSuccessMessage();
	}
	
	
	
	

}
