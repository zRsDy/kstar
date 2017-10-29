package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.custom.CustomInfo;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomReportPrmFlowCallBack extends IXflowInterface{

	@Autowired
	ICustomInfoService custom;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		CustomInfo customInfo = custom.getCustomInfo(businessKey);
		customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_40);
		custom.updateCustomInfo(customInfo);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		
	}

	@Override
	public String processPath() {
		return "/custom/prm/baseinfo/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		CustomInfo customInfo = custom.getCustomInfo(businessKey);
		customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_30);
		custom.updateCustomInfo(customInfo);
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
