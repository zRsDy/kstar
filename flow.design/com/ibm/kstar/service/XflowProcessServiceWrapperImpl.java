package com.ibm.kstar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.context.MessageContext;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;
import org.xsnake.xflow.api.workflow.IXflowInterface;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.conf.Configuration;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class XflowProcessServiceWrapperImpl implements IXflowProcessServiceWrapper{

	@Autowired
	IProcessService processService;
	
	@Autowired
	IHistoryService historyService;

	@Autowired
	ITaskService taskService; 
	
	@Autowired
	Configuration configuration;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	ProcessStatusService processStatusService;
	
	public ProcessInstance start(String module,String application,String title,String businessKey,UserObject userObject,Map<String,String> varmap){
		if(varmap == null){
			varmap = new HashMap<String,String>();
		}
		varmap.put("__positionId", userObject.getPosition().getId());
		ProcessInstance pi = processService.start(module, application, title, businessKey, userObject.getParticipant(), varmap);
		HistoryProcessInstance hpi = historyService.get(pi.getId());
		if("end".equals(hpi.getStatus())){
			String app = hpi.getApplication();
			String beanName = configuration.getAppMap().get(app);
			IXflowInterface xflowService = (IXflowInterface)MessageContext.getBean(beanName);
			xflowService.onComplete(hpi.getBusinessKey(), hpi.getModule(), hpi.getId(),"");
		}else{
			List<Task> taskList = taskService.getTaskByProcessInstanceId(pi.getId());
			for(Task task : taskList){
				if("Y".equals(task.getMessageFlag())){
					//发送消息
				}
			}
			
		}
		return pi;
	}
	
	public ProcessInstance start(String module,String businessKey,UserObject userObject,Map<String,String> varmap){
		String title = varmap.get("title");
		String application = varmap.get("app");
		Map<String, String> vmap = new HashMap<>();
//		黄奇  207-08-14
//      开启合同评审流程报错,临时注释代码
//		String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
//		vmap.put("SalesCenter", salesCenter);
//		processStatusService.updateProcessStatus("CustomInfoChange", businessKey, "status", IConstants.CUSTOM_NORMAL_STATUS_20);
		return this.start(module, application, title, businessKey, userObject, varmap);
	}

	
}
