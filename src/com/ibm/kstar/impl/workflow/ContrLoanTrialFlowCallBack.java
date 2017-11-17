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
public class ContrLoanTrialFlowCallBack extends IXflowInterface{


	@Autowired
	private ILovMemberService lovMemberService;

	@Autowired
	private IContractService contractService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {

		// 合同初审状态更新
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//
		contractService.updateContractTrialStatus(businessKey, lovReview.getId());
		// 借货合同审批状态更新
		/*12	审批通过待商务下单
		13	审批通过商务已下单*/

		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "12");//12	审批通过待商务下单
		contractService.updateContractStatus(businessKey, staLov.getId());
		//更新借货合同流程结束时间
		contractService.updateContractProcessCloseDate(businessKey);

		// 取消签订时0单价的价格平摊;2017-10-10  黄奇
		// contractService.share0Price(businessKey);
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
			LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");//03	待评审   
			contractService.updateContractStatus(businessKey, staLov.getId());
		}
	}

	@Override
	public String processPath() {
		return "/loan/tabs.html";
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
