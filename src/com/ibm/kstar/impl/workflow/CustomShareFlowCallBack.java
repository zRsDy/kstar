package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomShareService;
import com.ibm.kstar.entity.custom.CustomShareInfo;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomShareFlowCallBack extends IXflowInterface{

	@Autowired
	ICustomShareService share;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		CustomShareInfo customShareInfo = share.getShareInfo(businessKey);
		customShareInfo.setShareStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		share.updateShareInfo(customShareInfo, IConstants.CUSTOM_NORMAL_STATUS_40);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		CustomShareInfo customShareInfo = share.getShareInfo(businessKey);
		if(customShareInfo.getShareStatus().equals("CUSTOM_NORMAL_STATUS_30")){
			customShareInfo.setShareStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
			share.updateShareInfo(customShareInfo, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
	}

	@Override
	public String processPath() {
		return "/custom/share/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		CustomShareInfo customShareInfo = share.getShareInfo(businessKey);
		customShareInfo.setShareStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		share.updateShareInfo(customShareInfo, IConstants.CUSTOM_NORMAL_STATUS_10);
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
