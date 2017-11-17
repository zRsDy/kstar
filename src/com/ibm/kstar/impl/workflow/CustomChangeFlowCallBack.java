package com.ibm.kstar.impl.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.custom.CustomInfoChange;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomChangeFlowCallBack extends IXflowInterface{

	@Autowired
	ICustomInfoService custom;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		List<CustomInfoChange> entitys = custom.getCustomInfoChangeByCustomId(businessKey);
		
		CustomInfoChange customInfoChange = custom.getCustomInfoChange(entitys.get(0).getId());
		customInfoChange.setStatus(IConstants.CUSTOM_NORMAL_STATUS_40);
		custom.updateCustomInfoChange(customInfoChange, IConstants.CUSTOM_NORMAL_STATUS_40);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		List<CustomInfoChange> entitys = custom.getCustomInfoChangeByCustomId(businessKey);
		CustomInfoChange customInfoChange = custom.getCustomInfoChange(entitys.get(0).getId());
		if(customInfoChange.getStatus().equals("CUSTOM_NORMAL_STATUS_30")){
			customInfoChange.setStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
			custom.updateCustomInfoChange(customInfoChange, IConstants.CUSTOM_NORMAL_STATUS_20);
		}
	}

	@Override
	public String processPath() {
		return "/custom/change/change.html?flg=CUSTOM_CHANGE_PROC";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		List<CustomInfoChange> entitys = custom.getCustomInfoChangeByCustomId(businessKey);
		
		CustomInfoChange customInfoChange = custom.getCustomInfoChange(entitys.get(0).getId());
		customInfoChange.setStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		custom.updateCustomInfoChange(customInfoChange, IConstants.CUSTOM_NORMAL_STATUS_30);
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
