package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IAccountsService;

/**
 * 
 * ClassName: 账期申请审核工作流回调 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年3月20日 上午11:10:09 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class OrderAccountsAuditFlowCallBack extends IXflowInterface{

	@Autowired
	IAccountsService accountsService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		accountsService.updateControlStatus(businessKey, IConstants.ORDER_CONTROL_STATUS_30,null);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		
	}

	@Override
	public String processPath() {
		return "/accounts/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		accountsService.updateControlStatus(businessKey, IConstants.ORDER_CONTROL_STATUS_40,null);
	}

	@Override
	public String viewPath() {
		return "/accounts/edit.html";
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
