package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.BizOppChange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;


/**
 * Created by wangchao on 2017/3/20.
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizChangeFlowCallBack extends IXflowInterface {

    @Autowired
    ProcessStatusService processStatusService;

    @Autowired
    IBizoppService bizoppService;

    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
//        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "20");
        processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Completed);
        bizoppService.changeBizOpp(businessKey);

    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
//        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "20");
        processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Destroyed);
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
//        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "20");
        processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Rejected);
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,String comment) {
    	BizOppChange bizOppChange = bizoppService.getBizOppChangeById(businessKey);
    	if(bizOppChange.getAuditStatus().equals("Rejected")){
    		processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Processing);
    	}
    	
    }

    @Override
    public String processPath() {
        return "/change/change.html?type=changeId";
    }

    @Override
    public String viewPath() {
        return "/change/change.html?type=changeId";
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
