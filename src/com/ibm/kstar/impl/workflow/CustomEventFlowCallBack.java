package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomEventService;
import com.ibm.kstar.entity.custom.CustomEventInfo;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomEventFlowCallBack extends IXflowInterface{
	@Autowired
    IProcessService processService;
	
	@Autowired
	ICustomEventService event;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		CustomEventInfo customEventInfo = event.getEventInfo(businessKey);
		customEventInfo.setEventStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		event.updateEventInfo(customEventInfo);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		CustomEventInfo customEventInfo = event.getEventInfo(businessKey);
		if(customEventInfo.getEventStatus().equals("CUSTOM_NORMAL_STATUS_30")){
			customEventInfo.setEventStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
			event.updateEventInfo(customEventInfo);
		}
	}

	@Override
	public String processPath() {
		return "/custom/event/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		CustomEventInfo customEventInfo = event.getEventInfo(businessKey);
		customEventInfo.setEventStatus(IConstants.CUSTOM_NORMAL_STATUS_30);
		event.updateEventInfo(customEventInfo);
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
