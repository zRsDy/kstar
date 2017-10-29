package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.bizopp.Rebate;
import com.ibm.kstar.entity.bizopp.RebateChange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import java.util.Date;
import java.util.List;

/**
 * 特价变更流程回调
 * Created by wangchao on 2017/5/15.
 */
@Service
@Transactional
public class BizOppRebatePriceChangeFlowCallBack extends IXflowInterface {
    @Autowired
    ProcessStatusService processStatusService;
    @Autowired
    IBizBaseService bizBaseService;
    @Autowired
    IBizBaseService bizService;
    @Autowired
    ILovMemberService lovMemberService;
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("RebateChange", businessKey, "status", ProcessConstants.PROCESS_STATUS_Completed);
        RebateChange entity = bizService.getRebateChangeById(businessKey);
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
        	 processStatusService.updateProcessStatus("RebateChange", businessKey, "startDate", new Date());
             processStatusService.updateProcessStatus("RebateChange", businessKey, "endDate", DateUtil.getDateAfter(new Date(),60));
        }
       
        
        // 审核通过回写数据
        bizBaseService.updatePriceChangeFlowCallBack(businessKey);
    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("RebateChange", businessKey, "status", ProcessConstants.PROCESS_STATUS_Destroyed);
        bizBaseService.destoryRebateChange("RebateChange", businessKey, "status", ProcessConstants.PROCESS_STATUS_Destroyed);
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("RebateChange", businessKey, "status", ProcessConstants.PROCESS_STATUS_Rejected);
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant, String comment) {
    	RebateChange entity = bizBaseService.getEntity(businessKey, new RebateChange());
    	if(entity.getStatus().equals("Rejected")){
    		processStatusService.updateProcessStatus("RebateChange", businessKey, "status", ProcessConstants.PROCESS_STATUS_Processing);
    	}
    }

    @Override
    public String processPath() {
        return "/rebate/change/change.html";
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
