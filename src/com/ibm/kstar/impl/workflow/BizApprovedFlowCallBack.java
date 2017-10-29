package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;

import java.util.Date;

/**
 * Created by wangchao on 2017/3/16.
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizApprovedFlowCallBack extends IXflowInterface {
    @Autowired
    ProcessStatusService processStatusService;
    
    @Autowired
	IBizoppService bizService;

    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "20");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "40");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "successDate", new Date());
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "endDate", DateUtil.getDateAfter(new Date(),90));

    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "30");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "20");
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "30");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "20");
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,String comment) {
    	BusinessOpportunity entity = bizService.getBizOppEntity(businessKey);
    	if(entity.getStatus().equals("30")){
    		processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "10");
            processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "05");
    	}
    }

    @Override
    public String processPath() {
        return "/bizopp/edit.html";
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
