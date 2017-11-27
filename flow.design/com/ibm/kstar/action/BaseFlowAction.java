package com.ibm.kstar.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.kstar.action.flow.design.FlowUtils;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.*;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Controller
public class BaseFlowAction extends BaseAction {

	@Autowired
	IProcessService processService;
	
	@Autowired
	ITaskService taskService;
	
	@Autowired
	IHistoryService historyService;
	
	@Autowired
	IDefinitionService definitionService;
	
	@Autowired
	Configuration configuration;
	
	public void setProcessParam(Model model, HttpServletRequest request) {
		model.addAttribute("newProcessType", request.getParameter("newProcessType"));
        String taskId = request.getParameter("taskId");
        if(StringUtil.isNotEmpty(taskId)){
        	Task task = taskService.getTask(taskId);
        	if(task == null){
        		throw new AnneException("任务已经不存在");
        	}
        	model.addAttribute("task", task);
        	model.addAttribute("taskId", task.getId());
        	try {
				getHistory(taskId, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	public boolean process(HttpServletRequest request,UserObject user) {
//		String submitType = request.getParameter("submitType");
//		String activityId = request.getParameter("activityId");
//		String comment = request.getParameter("comment");
//		String taskId = request.getParameter("taskId");
//		Participant p = new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE");
//		if("1".equals(submitType)){
//			boolean isComplete = taskService.complete(taskId, p, comment);
//			if(isComplete){
//				Task task = taskService.getTask(taskId);
//				ProcessInstance processInstance = processService.get(task.getProcessInstanceId());
//				String app = processInstance.getApplication();
//				String beanName = configuration.getAppMap().get(app);
//				IXflowInterface xflowService = (IXflowInterface)MessageContext.getBean(beanName);
//				xflowService.onComplete(processInstance.getBusinessKey(), processInstance.getModule(), processInstance.getId());
//			}
//			return isComplete;
//		}else{
//			taskService.reject(taskId, activityId, p, comment);
//			return false;
//		}
		return false;
	}
	
	/**
	 * 运行中流程查找
	 * 通过业务主键获取流程历史
	 * @param module
	 * @param businessKey
	 * @return
	 */
//	public List<HistoryActivityInstance> getHistoryByBusinessKey(String module ,String businessKey){
//		ProcessInstance pi = processService.getByBusinessKey(module, businessKey);
//		List<HistoryActivityInstance> list = historyService.findHistoryActivityInstance(pi.getId());
//		return list;
//	}
	
	/**
	 * 所有流程中查找
	 * @param module
	 * @param processInstanceId
	 * @return
	 */
	public List<HistoryActivityInstance> getHistoryByProcessInstanceId(String module ,String processInstanceId){
		ProcessInstance pi = processService.get(processInstanceId);
		List<HistoryActivityInstance> list = historyService.findHistoryActivityInstance(pi.getId());
		return list;
	}
	
//	public void getHistory(String processInstanceId,Model model) throws Exception {
//		ProcessInstance pi = processService.getByBusinessKey(module, businessKey);
//		HistoryProcessInstance p = historyService.get(processInstanceId);
//		history(model, p);
//	}

	public void history(Model model, String processInstanceId) throws Exception {
		HistoryProcessInstance pi = historyService.get(processInstanceId);
		List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
		loadOperateTypeName(historyList);
		model.addAttribute("historyList",historyList);
		JSONObject flow = new JSONObject();
		JSONObject history = new JSONObject();
		JSONObject activeRects = new JSONObject();
		JSONArray hrects = new JSONArray();
		JSONArray arects = new JSONArray();
		history.put("rects", hrects);
		activeRects.put("rects", arects);
		flow.put("historyRects", history);
		flow.put("activeRects", activeRects);
		for(HistoryActivityInstance ha : historyList){
			if("Y".equals(ha.getIsNormalTask()) && "running".equals(ha.getStatus())){
				JSONObject ah = new JSONObject();
				JSONArray apaths = new JSONArray();
				ah.put("paths", apaths);
				ah.put("name", ha.getActivityName());
				arects.add(ah);
			}
		}
		
		for(HistoryActivityInstance hai : historyList) {
			JSONObject h = new JSONObject();
			JSONArray paths = new JSONArray();
			paths.add(hai.getToTransitionName());
			h.put("paths", paths);
			h.put("name", hai.getActivityName());
			hrects.add(h);
		}
		model.addAttribute("flow",JSON.toJSONString(flow));
		
		String xml = definitionService.getXML(pi.getProcessDefinitionId());
		String json = null;
		if(xml!=null){
			json = FlowUtils.xmlToJson(xml);
		}
		model.addAttribute("json",json);
	}

	private void loadOperateTypeName(List<HistoryActivityInstance> historyList) {
		for (HistoryActivityInstance historyActivityInstance : historyList) {
            String operationType = historyActivityInstance.getOperationType();
            if (operationType == null) {
				continue;
			}
            switch (operationType) {
				case ITaskService.OperationType.COMMUNICATION:
					historyActivityInstance.setOperationTypeName("沟通");
					break;
				case ITaskService.OperationType.AGREEMENT:
					historyActivityInstance.setOperationTypeName("同意");
					break;
				case ITaskService.OperationType.DELEGATION:
					historyActivityInstance.setOperationTypeName("委托");
					break;
				case ITaskService.OperationType.AUTO:
					historyActivityInstance.setOperationTypeName("自动");
					break;
				case ITaskService.OperationType.REJECT:
					historyActivityInstance.setOperationTypeName("驳回");
					break;
				case ITaskService.OperationType.CLOSE:
					historyActivityInstance.setOperationTypeName("销毁");
					break;
				default:
					historyActivityInstance.setOperationTypeName("其他");
			}
		}
	}

	public List<HistoryProcessInstance> sequence(List<HistoryProcessInstance> hisList){
		UserObject user = getUserObject();
		String name = user.getEmployee().getName();
		for(HistoryProcessInstance his:hisList) {
			List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(his.getId());
			for(int i = (historyList.size()-1) ;i >= 0;i--) {
				if(name.equals(historyList.get(i).getOperatorName())) {
					his.setLastOperateTime(historyList.get(i).getEndTime());
					break;
				}
			}
		}
		for(int i = 0 ; i < hisList.size(); i++) {
			for(int j = (i+1); j < hisList.size()-1;j++) {
				Date idate = hisList.get(i).getLastOperateTime();
				Date jdate = hisList.get(j).getLastOperateTime();
				boolean flag = DateUtil.compare(idate,jdate);
				if(flag) {
					HistoryProcessInstance temp = hisList.get(i);
					hisList.set(i,hisList.get(j));
					hisList.set(j,temp);
				}
				
			} 
		}
		return hisList;
	}
	
	public void getHistory(String taskId, Model model) throws Exception {
		List<RejectPath> rejectPathList = taskService.getRejectPathList(taskId);
		Task task = taskService.getTask(taskId);
		model.addAttribute("rejectPathList",rejectPathList);
		model.addAttribute("task",task);
		
		HistoryProcessInstance pi = historyService.get(task.getProcessInstanceId());
		history(model, pi.getId());
	}

	
}
