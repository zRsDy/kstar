package com.ibm.kstar.impl.workflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContractXxxFlowCallBack extends IXflowInterface{

	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		System.out.println("============流程结束了，更新一下合同业务的信息=============");
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		
	}

	@Override
	public String processPath() {
		return "/order/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String viewPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mobileView(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEvent(String event, String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}

}
