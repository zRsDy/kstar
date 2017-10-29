package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.entity.bizopp.SpecialPrice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

/**
 * Created by wangchao on 2017/3/16.
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizOppSpecialPriceFlowCallBack extends IXflowInterface {

    @Autowired
    ProcessStatusService processStatusService;
    
    @Autowired
	IBizBaseService bizService;

    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("SpecialPrice", businessKey, "applyStatus", ProcessConstants.PROCESS_STATUS_Completed);
    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("SpecialPrice", businessKey, "applyStatus", ProcessConstants.PROCESS_STATUS_Destroyed);
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("SpecialPrice", businessKey, "applyStatus", ProcessConstants.PROCESS_STATUS_Rejected);
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant, String comment) {
    	SpecialPrice specialPrice = bizService.getEntity(businessKey,new SpecialPrice());
    	if(specialPrice.getApplyStatus().equals("Rejected")){
    		processStatusService.updateProcessStatus("SpecialPrice", businessKey, "applyStatus", ProcessConstants.PROCESS_STATUS_Processing);
    	}
    }

    @Override
    public String processPath() {
        return "/spePrice/edit.html";
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
