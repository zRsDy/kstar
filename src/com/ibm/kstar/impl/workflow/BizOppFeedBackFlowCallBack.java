package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.BiddingFeedBack;

import javax.swing.plaf.basic.BasicSplitPaneDivider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

/**
 * Created by zhangjunxin on 2017/11/13.
 */
@Service
@Transactional
public class BizOppFeedBackFlowCallBack extends IXflowInterface {

    @Autowired
    IProcessService processService;

    @Autowired
    ProcessStatusService processStatusService;
    
    @Autowired
	IBizoppService iBizoppService;
    
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BiddingFeedBack",businessKey,"status", ProcessConstants.PROCESS_STATUS_Completed);
    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BiddingFeedBack",businessKey,"status", ProcessConstants.PROCESS_STATUS_Destroyed);
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BiddingFeedBack",businessKey,"status", ProcessConstants.PROCESS_STATUS_Rejected);
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,String comment) {
    	BiddingFeedBack bidFeedBack = iBizoppService.getBidFeedBackById(businessKey);
    	if(bidFeedBack.getStatus().equals("Rejected")){
    		processStatusService.updateProcessStatus("BiddingFeedBack",businessKey,"status", ProcessConstants.PROCESS_STATUS_Processing);
    	}
    }

    @Override
    public String processPath() {
        return "/bidFeedBack/add.html";
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
