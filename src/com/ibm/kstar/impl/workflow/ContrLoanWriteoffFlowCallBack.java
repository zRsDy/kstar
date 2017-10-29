package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.api.contract.IContractEliminateService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;

/**
 * 无合同核销
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrLoanWriteoffFlowCallBack extends IXflowInterface{

	@Autowired
	private IContractEliminateService contractEliminateService;	
	
	@Autowired
	private ILovMemberService lovMemberService;

	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "03");//
		contractEliminateService.updateContractTrialStatus(businessKey, lovReview.getId());
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "05");//
		contractEliminateService.updateContractTrialStatus(businessKey, lov.getId());
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "04");//04	已驳回
		contractEliminateService.updateContractTrialStatus(businessKey, lov.getId());
		
	}

	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,
			String comment) {
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "02");//02	审批中
		contractEliminateService.updateContractTrialStatus(businessKey, lov.getId());
	}

	@Override
	public String processPath() {
		return "/loan/eliminate/addtabs.html";
	}

	@Override
	public String viewPath() {
		return "";
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
