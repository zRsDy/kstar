package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomCreditAdjustService;
import com.ibm.kstar.entity.custom.CustomCreditAdjustment;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomAdjustFlowCallBack extends IXflowInterface{

	@Autowired
	ICustomCreditAdjustService adjust;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		CustomCreditAdjustment customCreditAdjustment = adjust.getAdjustmentInfo(businessKey);
		customCreditAdjustment.setStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		adjust.updateAdjustmentInfo(customCreditAdjustment, IConstants.CUSTOM_NORMAL_STATUS_40);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		CustomCreditAdjustment customCreditAdjustment = adjust.getAdjustmentInfo(businessKey);
		if(customCreditAdjustment.getStatus().equals("CUSTOM_NORMAL_STATUS_30")){
			customCreditAdjustment.setStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
			adjust.updateAdjustmentInfo(customCreditAdjustment, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
	}

	@Override
	public String processPath() {
		return "/custom/adjust/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		CustomCreditAdjustment customCreditAdjustment = adjust.getAdjustmentInfo(businessKey);
		customCreditAdjustment.setStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		adjust.updateAdjustmentInfo(customCreditAdjustment, IConstants.CUSTOM_NORMAL_STATUS_30);
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
