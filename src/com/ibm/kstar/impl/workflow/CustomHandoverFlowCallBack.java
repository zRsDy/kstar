package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomHandoverService;
import com.ibm.kstar.entity.custom.CustomHandoverList;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomHandoverFlowCallBack extends IXflowInterface{
	@Autowired
	ICustomHandoverService handover;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		CustomHandoverList customHandoverList = handover.getHandoverInfo(businessKey);
		customHandoverList.setHandoverStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		handover.updateHandoverInfo(customHandoverList, IConstants.CUSTOM_NORMAL_STATUS_40);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		CustomHandoverList customHandoverList = handover.getHandoverInfo(businessKey);
		if(customHandoverList.getHandoverStatus().equals("CUSTOM_NORMAL_STATUS_30")){
			customHandoverList.setHandoverStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
			handover.updateHandoverInfo(customHandoverList, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
	}

	@Override
	public String processPath() {
		return "/custom/handover/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		CustomHandoverList customHandoverList = handover.getHandoverInfo(businessKey);
		customHandoverList.setHandoverStatus(IConstants.CUSTOM_NORMAL_STATUS_30);
		handover.updateHandoverInfo(customHandoverList, IConstants.CUSTOM_NORMAL_STATUS_30);
	}

	@Override
	public String viewPath() {
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
