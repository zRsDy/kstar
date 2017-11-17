package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrStdTrialFlowCallBack extends IXflowInterface{


	@Autowired
	private ILovMemberService lovMemberService;

	@Autowired
	private IContractService contractService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {

		// 合同初审状态更新
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//
		contractService.updateContractTrialStatus(businessKey, lovReview.getId());

		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS",  "14");//初审完成

		contractService.updateContractStatus(businessKey, staLov.getId());
		
		//初审完成为 系统自动创建售前、售后、商务评审
		contractService.createPrjevlForContr(businessKey);
		
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		// 合同初审状态更新
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//05	已销毁
		contractService.updateContractTrialStatus(businessKey, lov.getId());
		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");//已废弃
		contractService.updateContractStatus(businessKey, staLov.getId());
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId,String comment) {
		// 合同初审状态更新
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//04	已驳回
		contractService.updateContractTrialStatus(businessKey, lov.getId());
		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");//06	返回修改中
		contractService.updateContractStatus(businessKey, staLov.getId());
	}
	
	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		Contract contr = contractService.get(businessKey);
		String conStatCode= lovMemberService.get(contr.getContrStat()).getCode();	
		String flowStatCode= lovMemberService.get(contr.getTrialStat()).getCode();	
		if( conStatCode.equalsIgnoreCase("06") && flowStatCode.equalsIgnoreCase("04")){
			// 合同初审状态更新
			LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");//02	审批中
			contractService.updateContractTrialStatus(businessKey, lov.getId());

			// 合同状态更新
			LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "02");//02	待初审   
			contractService.updateContractStatus(businessKey, staLov.getId());
		}
	}

	@Override
	public String processPath() {
//		return "/standard/edit.html";
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
