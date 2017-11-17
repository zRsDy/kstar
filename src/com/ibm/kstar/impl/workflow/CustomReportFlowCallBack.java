package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.JsonResult;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.custom.CustomInfo;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomReportFlowCallBack extends IXflowInterface{

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
		CustomInfo customInfo = custom.getCustomInfo(businessKey);
		if(customInfo.getCustomStatus().equals("CUSTOM_NORMAL_STATUS_30")){
			customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_20);
			custom.updateCustomInfo(customInfo);
		}
	}

	@Override
	public String processPath() {
		return "/custom/baseinfo/edit.html";
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

	public String mobileView(String id){
		CustomInfo customInfo = custom.getCustomInfo(id);
		return JsonResult.toSuccessJson(customInfo);
	}

	@Override
	public void onEvent(String event, String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}
}
