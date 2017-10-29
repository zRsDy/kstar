package com.ibm.kstar.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.types.resources.comparators.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.context.MessageContext;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.PropertiesUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.RejectPath;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;
import org.xsnake.xflow.api.workflow.IXflowInterface;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.product.IDemandService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.conf.Configuration;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.pdm.PdmFlowHistory;
import com.ibm.kstar.entity.product.KstarProductDemand;
import com.ibm.kstar.exchange.pdm.ResultBean;
import com.ibm.kstar.exchange.pdm.ServiceUtil;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.service.IDemoService;

@Controller
@RequestMapping("/flow")
public class FlowAction extends BaseFlowAction{
	
	@Autowired
	IDemoService demoService;
	
	@Autowired
	Configuration configuration;
	
	@Autowired
	ITaskService taskService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	IHistoryService historyService;
	
	@Autowired
	IDemandService demandService;
	
	@Autowired
	ICustomInfoService customService;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/mobileProcess")
	public String mobileProcess(String id,String taskId,Model model) throws Exception {
		
		Task task = taskService.getTask(taskId);
		if(task == null){
			throw new AnneException("任务已经不存在");
		}
		ProcessInstance pi = processService.get(task.getProcessInstanceId());
		String app =pi.getApplication();
		String path = configuration.getPath(app);
		if(path == null){
			throw new Exception("应用未配置回调接口类,应用名:"+app);
		}
		IXflowInterface xflowInterface = (IXflowInterface)MessageContext.getBean(path);
		if(xflowInterface == null){
			throw new Exception("未找到对应的接口实现类" + path);
		}
		
		return sendSuccessMessage(xflowInterface.mobileView(id));
	}
	
	@NoRight
	@RequestMapping("/getRejectList")
	public String getRejectList(String taskId){
		Task task = taskService.getTask(taskId);
		List<RejectPath> rejectPathList = null;
		if("Y".equals(task.getRejectFlag())){
			rejectPathList = taskService.getRejectPathList(taskId);
		}else{
			rejectPathList = new ArrayList<RejectPath>();
		}
		return sendSuccessMessage(rejectPathList);
	}
	
	@NoRight
	@RequestMapping("/process")
	public String process(String id,String taskId,Model model) throws Exception {
		Task task = taskService.getTask(taskId);
		if(task == null){
			throw new AnneException("任务已经不存在");
		}
		ProcessInstance pi = processService.get(task.getProcessInstanceId());
		String app =pi.getApplication();
		String path = null;
		if(app == null){
			//throw new Exception("该类型未配置审批地址！");
		}else{
			path = configuration.getPath(app);
			if(path == null){
				throw new AnneException("应用未配置回调接口类,应用名:"+app);
			}
			IXflowInterface xflowInterface = (IXflowInterface)MessageContext.getBean(path);
			if(xflowInterface == null){
				throw new AnneException("未找到对应的接口实现类" + path);
			}
			path = xflowInterface.processPath();
			boolean flag = path.indexOf("?") > -1;
			if(flag){
				path = path + "&";
			} else {
				path = path + "?";
			}
			path = path + "id=" + id + "&taskId="+ taskId + "&typ=" + pi.getApplication() + "&processInstanceId=" + pi.getId();
			
			if(xflowInterface.newType()){
				path = path + "&newProcessType=Y";
				return redirect(path);
			}else{
				path = path + "&newProcessType=N";
			}
		}
		model.addAttribute("path",path);
		model.addAttribute("taskId", taskId);
		getHistory(taskId, model);
		return forward("process");
	}

//	Task task = taskService.getTask(taskId);
//	ProcessInstance pi = processService.get(task.getProcessInstanceId());
//	String path = null;
//	if(pi.getApplication() == null){
//		//throw new Exception("该类型未配置审批地址！");
//	}else{
//		path = configuration.getPath(pi.getApplication());
//		boolean flag = path.indexOf("?") > -1;
//		if(flag){
//			path = path + "&";
//		} else {
//			path = path + "?";
//		}
//		path = path + "id=" + id + "&taskId="+ taskId;
//	}
//	model.addAttribute("path",path);
//	getHistory(taskId, model);
//	return forward("process");
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/process",method=RequestMethod.POST)
	public String process(HttpServletRequest request) throws Exception {
		ProcessForm form = ActionUtil.getProcessForm(request,getUserObject());
		demoService.todoProcess(form);
		return sendSuccessMessage();
	}
	

	@NoRight
	@RequestMapping("/list")
	public String list(){
		return forward("list");
	}
	
	@RequestMapping("/allList")
	public String allList(){
		return forward("allList");
	}
	
	@NoRight
	@RequestMapping("/hisProcess")
	public String hisProcess(){
		return forward("hisProcess");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/hisProcessPage")
	public String hisProcessPage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		UserObject user = getUserObject();
		Map<String,Object> map = new HashMap<String,Object>();
		String searchKey = condition.getStringCondition("keyword");
		if(StringUtil.isNullOrEmpty(searchKey)) {
			searchKey = condition.getStringCondition("searchKey");
		}
		map.put("searchKey", searchKey);
		IPage p = historyService.history(user.getParticipant(),map,condition.getRows(),condition.getPage());
		/*List<HistoryProcessInstance> hisListTemp = sequence((List<HistoryProcessInstance>) p.getList());
		List<HistoryProcessInstance> hisList = new ArrayList<HistoryProcessInstance>();
		
		for(int i = 0;i<hisListTemp.size();i++) {
			hisList.add(hisListTemp.get(i));
		}
		PageImpl page = new PageImpl(hisList, condition.getPage(),condition.getRows(), p.getCount());*/
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/page")
	public String page(PageCondition condition,Model model,HttpServletRequest request){
		UserObject user = getUserObject();
		List<LovMember> positions = (List<LovMember>)getSession().getAttribute("positions");
		return sendSuccessMessage(demoService.taskList(user,positions,condition.getRows(),condition.getPage()));
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/crmOrPdmPage")
	public String crmOrPdmPage(PageCondition condition,Model model,HttpServletRequest request){
		UserObject user = getUserObject();
		List<LovMember> positions = (List<LovMember>)getSession().getAttribute("positions");
		IPage page = demoService.taskList(user,positions,condition.getRows(),condition.getPage());
		IPage p = demoService.pdmTaskList(user,condition.getRows(),condition.getPage());
		List<Object> list = new ArrayList<Object>();
		if(page.getList()!=null&&page.getList().size()>0){			
			for(Object obj:page.getList()){
				list.add(obj);
			}
		}
		if(p != null){			
			if(p.getList()!=null&&p.getList().size()>0){			
				for(Object obj:p.getList()){
					list.add(obj);
				}
			}	
		}
		IPage newPage = new PageImpl(list, condition.getPage(), condition.getRows(), list.size());
		
		return sendSuccessMessage(newPage);
	}
	
	/**
	 * 非标PDM待办流程处理
	 * @param condition
	 * @param model
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/pdmPage")
	public String pdmPage(PageCondition condition,Model model,HttpServletRequest request){
		UserObject user = getUserObject();
		IPage page = demoService.pdmTaskList(user,condition.getRows(),condition.getPage());
		
//		List<PdmFlowHistory> list = new ArrayList<PdmFlowHistory>();
//		PdmFlowHistory pdmFlowHistory = new PdmFlowHistory();
//		pdmFlowHistory.setId(1l);
//		pdmFlowHistory.setName("测试");
//		pdmFlowHistory.setTempId(2l);
//		pdmFlowHistory.setProcId(3l);
//		pdmFlowHistory.setProcName("测试111111");
//		pdmFlowHistory.setStatus(4l);
//		pdmFlowHistory.setStartTime("2017-09-22");
//		pdmFlowHistory.setEndTime("2017-09-23");
//		pdmFlowHistory.setProUsers("张三");
//		pdmFlowHistory.setOpinions("测试意见建议");
//		pdmFlowHistory.setNeedconfirm(2l);
//		list.add(pdmFlowHistory);
//		IPage page = new PageImpl(list, condition.getPage(), condition.getRows(), 0);
		return sendSuccessMessage(page);
	}
	
	/**
	 * 非标PDM待办流程处理
	 * @param condition
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@NoRight
	@RequestMapping(value="/pdmProcess")
	public String pdmProcess(String id,String procId,String formNo,Model model) throws Exception{
		KstarProductDemand client =  demandService.queryDemandByCode(formNo);
		CustomInfo customInfo = customService.getCustomInfo(client.getClientCode());
		model.addAttribute("demand", client);
		model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("PDM审批历史", "/pdm/flow/history.html?no=" + client.getDemandCode() + "&type=" + "PRODUCT_DEMAND");
		model.addAttribute("tabMainInfo",tabMainInfo);
		
		ResultBean res = ServiceUtil.getToken();
		String loginUid = ServiceUtil.encodeStr(res.getLoginguid());
		model.addAttribute("id", id);
		model.addAttribute("procId", procId);
		model.addAttribute("loginguid", loginUid);
		return forward("pdmProcess");
	}
	
	@NoRight
	@RequestMapping("/doPdmProcess")
	public String doPdmProcess(String id,String procId,String opinions,
			String loginguid,String type){
		String serKey = "getwfinfo";
		String submitOrReject = new StringBuilder().append(PropertiesUtil.getStringByKey(serKey))
				.append("&wfid=").append(id)
				.append("&procid=").append(procId)
				.append("&operation=").append(type)
				.append("&loginguid=").append(loginguid)
				.append("&opinion=").append(opinions).toString();
		ResultBean cssRs = ServiceUtil.callService(submitOrReject, null);
		return sendSuccessMessage(cssRs);
	}
	
	@ResponseBody
	@RequestMapping(value="/allTaskPage")
	public String allTask(PageCondition condition,Model model,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return sendSuccessMessage(taskService.allTask(condition.toMap(),condition.getRows(),condition.getPage()));
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/allPage")
	public String allPage(PageCondition condition,Model model,HttpServletRequest request){
		UserObject user = getUserObject();
		List<LovMember> positions = (List<LovMember>)getSession().getAttribute("positions");
		return sendSuccessMessage(demoService.taskList(user,positions,condition.getRows(),condition.getPage()));
	}
	
	@NoRight
	@RequestMapping("/history")
	public String myHistory(String taskId,Model model) throws Exception{
		getHistory(taskId, model);
		return forward("history");
	}
	
//	@ResponseBody
//	@RequestMapping(value="/historyPage")
//	public String historyPage(Model model,HttpServletRequest request){
//		UserObject user = getUserObject();
//		return sendSuccessMessage(demoService.taskList(user));
//	}
	
	/**
	 * 
	 * transfer:转交,转办任务. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param model
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/transfer")
	public String transfer(PageCondition condition,Model model,HttpServletRequest request){
		UserObject user = getUserObject();
		String taskId = request.getParameter("taskId");
		String userId = request.getParameter("userId");
		String comment = request.getParameter("comment");
		
		Employee employee = employeeService.get(userId);
		Participant assignee = new Participant(employee.getId(),employee.getName(),"EMPLOYEE");
		
		taskService.transfer(taskId,user.getParticipant(), assignee,comment);
		return sendSuccessMessage("操作成功");
	}
	/**
	 * 
	 * support:支持,沟通任务. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param model
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/support")
	public String support(PageCondition condition,Model model,HttpServletRequest request){
		UserObject user = getUserObject();
		String taskId = request.getParameter("taskId");
		String userIds = request.getParameter("userIds");
		String comment = request.getParameter("comment");
		String[] ids = userIds.split(",");
		List<Participant> participantList = new ArrayList<Participant>();
		for(String userId : ids){
			Employee employee = employeeService.get(userId);
			Participant assignee = new Participant(employee.getId(),employee.getName(),"EMPLOYEE");
			participantList.add(assignee);
		}
		taskService.support(taskId,user.getParticipant(),participantList,comment);
		return sendSuccessMessage("操作成功");
	}

	/**
	 * 
	 * support:审批历史history, flowdesign. <br/> 
	 * 
	 * @author joe 
	 * @param condition
	 * @param model 
	 */
	@NoRight
	@RequestMapping("/flowHisProcess")
	public String flowHisProcess(String id,Model model) throws Exception {
//		history(model,id);
//		return forward("flowHistory");
		
		HistoryProcessInstance pi = historyService.get(id);
		String app =pi.getApplication();
		String path = null;
		if(app == null){
			throw new Exception("该类型未配置地址！");
		}else{
			path = configuration.getPath(app);
			if(path == null){
				throw new Exception("应用未配置回调接口类,应用名:"+app);
			}
			IXflowInterface xflowInterface = (IXflowInterface)MessageContext.getBean(path);
			if(xflowInterface == null){
				throw new Exception("未找到对应的接口实现类" + path);
			}
			path = xflowInterface.processPath();
			boolean flag = path.indexOf("?") > -1;
			if(flag){
				path = path + "&";
			} else {
				path = path + "?";
			}
			path = path + "id=" + pi.getBusinessKey() + "&typ=" + pi.getApplication() + "&processInstanceId=" + pi.getId();
			return redirect(path);
		}
		
	}

	@NoRight
	@RequestMapping("/flowHistory")
	public String flowHistory(String id,Model model) throws Exception{
		history(model,id);
		return forward("history");
	}
	
	@NoRight
	@RequestMapping("/assignee")
	public String assignee(String taskId,Model model) throws Exception{
		model.addAttribute("taskId",taskId);
		return forward("assignee");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/assignee",method=RequestMethod.POST)
	public String assignee(String taskId,String assigneeId,String comment,HttpServletRequest request){
		UserObject user = getUserObject();
		
		Employee employee = employeeService.get(assigneeId);
		Participant assignee = new Participant(employee.getId(),employee.getName(),"EMPLOYEE");
		
		taskService.transfer(taskId,user.getParticipant(), assignee,comment);
		return sendSuccessMessage("操作成功");
	}
	
	@ResponseBody
	@RequestMapping(value="/closeProcess",method=RequestMethod.POST)
	public String closeProcess(Task task,HttpServletRequest request){
		UserObject user = getUserObject();
		processService.close(task.getProcessInstanceId(), user.getParticipant(), "");;
		return sendSuccessMessage();
	}
	
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	@Autowired
	IProcessService processService;
	
	@RequestMapping("/test")
	public String test() throws Exception{
//		String id = UUID.randomUUID().toString();
//		xflowProcessServiceWrapper.start("jerryTest", "jerryTest", "jerryTest", id, getUserObject(), null);
//		return "OK";
		return forward("test");
	}
	 

}
