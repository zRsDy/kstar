package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.bizopp.Rebate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import java.util.Date;
import java.util.List;

/**
 * Created by wangchao on 2017/5/15.
 */
@Service
@Transactional
public class BizOppRebatePriceFlowCallBack extends IXflowInterface {
    @Autowired
    ProcessStatusService processStatusService;
    
    @Autowired
    IBizBaseService bizService;
    @Autowired
    ILovMemberService lovMemberService;
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("Rebate", businessKey, "status", ProcessConstants.PROCESS_STATUS_Completed);
        Rebate entity = bizService.getRebate(businessKey);
        boolean specialOffFlag = false; 
        List<LovMember> lovMemberList = lovMemberService.getListByGroupCode("SPECIALOFF");
        for(LovMember lovMember:lovMemberList) {
        	if("02".equals(lovMember.getCode())) {
        		if(lovMember.getId().equals(entity.getSpecialOff())) {
        			specialOffFlag = true;
        		}
        	}
        }
        if(!specialOffFlag) {
        	processStatusService.updateProcessStatus("Rebate", businessKey, "startDate", new Date());
        	processStatusService.updateProcessStatus("Rebate", businessKey, "endDate", DateUtil.getDateAfter(new Date(),60));
        }
        bizService.saveCopyRebatChange(entity);//备份变更数据的初始数据
    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("Rebate", businessKey, "status", ProcessConstants.PROCESS_STATUS_Destroyed);
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("Rebate", businessKey, "status", ProcessConstants.PROCESS_STATUS_Rejected);
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant, String comment) {
    	Rebate entity = bizService.getRebate(businessKey);
    	if(entity.getStatus().equals("Rejected")){
    		processStatusService.updateProcessStatus("Rebate", businessKey, "status", ProcessConstants.PROCESS_STATUS_Processing);
    	}
    }

    @Override
    public String processPath() {
        return "/rebate/edit.html";
    }

    @Override
    public String viewPath() {
        return null;
    }

    @Override
    public String mobileView(String id) {
        return null;
    }

    @Override
    public void onEvent(String event, String businessKey, String flowModule, String processInstanceId, String comment) {

    }
    
    @Override
    public boolean newType() {
    	return true;
    }
}
