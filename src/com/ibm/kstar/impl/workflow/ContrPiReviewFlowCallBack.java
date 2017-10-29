package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrPiReviewFlowCallBack extends IXflowInterface{


	@Autowired
	private ILovMemberService lovMemberService;

	@Autowired
	private IContractService contractService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {

		// 合同审批状态
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//已审批
//		contractService.updateContractTrialStatus(businessKey, lovReview.getId());
		// 合同状态更新
//		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");//待评审
//		contractService.updateContractStatus(businessKey, staLov.getId());
//		KstarPrjEvl prje=contractService.getKstarPrjEvl(businessKey);
		KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
		prje.setEvlSt(lovReview.getId());
//		prje.setEvlSg(comment);
		
		contractService.updatePrjEvl(prje);

		// 取消签订时0单价的价格平摊;2017-10-10  黄奇
		// 价格平摊
		// contractService.share0Price(businessKey);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
//		// 合同初审状态更新
//		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//
//		contractService.updateContractTrialStatus(businessKey, lov.getId());
//		// 合同状态更新
//		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");//已废弃
//		contractService.updateContractStatus(businessKey, staLov.getId());
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId,String comment) {
		// 合同初审状态更新
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//04	已驳回
//		contractService.updateContractTrialStatus(businessKey, lov.getId());
		/*
		 * 驳回时， 合同评审的状态 仍为 “待评审”
		 */
		KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
		prje.setEvlSt(lov.getId());
//		prje.setEvlSg(comment);
		
		contractService.updatePrjEvl(prje);

		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");//06	返回修改中
		contractService.updateContractStatus(businessKey, staLov.getId());
	}
	
	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		
	}

	@Override
	public String processPath() {
		return "/pi/tabs.html";
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
