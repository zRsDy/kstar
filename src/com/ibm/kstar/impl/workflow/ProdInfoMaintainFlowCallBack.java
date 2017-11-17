package com.ibm.kstar.impl.workflow;

import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.product.maintain.IProdMaintainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class ProdInfoMaintainFlowCallBack extends IXflowInterface {
	
	@Autowired
	IProdMaintainService prodMaintainService;

	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		prodMaintainService.updateProcessStatus(businessKey,IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_40);
		
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		prodMaintainService.updateProcessStatus(businessKey,IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_30);
	}

	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,
			String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String processPath() {
		// TODO Auto-generated method stub
		return "/product/maintain/process.html";
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
