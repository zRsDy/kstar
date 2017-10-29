package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.Contract;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrStdFinalFlowCallBack extends IXflowInterface{


	@Autowired
	private ILovMemberService lovMemberService;

	@Autowired
	private IContractService contractService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {

		// 合同书评审状态更新
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//已审批
		contractService.updateContractFinalStatus(businessKey, lovReview.getId());
		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "05");//05	待签订   
		contractService.updateContractStatus(businessKey, staLov.getId());
		
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		// 合同初审状态更新
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//
		contractService.updateContractTrialStatus(businessKey, lov.getId());
		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");//已废弃
		contractService.updateContractStatus(businessKey, staLov.getId());
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId,String comment) {
		// 合同初审状态更新
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//04	已驳回
		contractService.updateContractFinalStatus(businessKey, lov.getId());
		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");//06	返回修改中
		contractService.updateContractStatus(businessKey, staLov.getId());
	}
	
	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		Contract contr = contractService.get(businessKey);
		String conStatCode= lovMemberService.get(contr.getContrStat()).getCode();	
		String flowStatCode= lovMemberService.get(contr.getFinalReviewStat()).getCode();	
		if( conStatCode.equalsIgnoreCase("06") && flowStatCode.equalsIgnoreCase("04")){
			// 合同初审状态更新
			LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");//02	审批中
			contractService.updateContractFinalStatus(businessKey, lov.getId());
			// 合同状态更新
			LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "04");//04	待合同书评审  
			contractService.updateContractStatus(businessKey, staLov.getId());
		}
	}

	@Override
	public String processPath() {
		return "/standard/tabs.html";
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
	
	@Override
	public boolean newType() {
		return true;
	}
}
