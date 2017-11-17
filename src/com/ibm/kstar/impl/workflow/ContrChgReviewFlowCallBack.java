package com.ibm.kstar.impl.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.quot.KstarPrjEvl;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrChgReviewFlowCallBack extends IXflowInterface{


	@Autowired
	private ILovMemberService lovMemberService;

	@Autowired
	private IContractService contractService;
	@Autowired
	private IContrChangeService  changeService ; 
	@Autowired
	IHistoryService historyService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {

		// 合同审批状态
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//已审批
//		contractService.updateContractTrialStatus(businessKey, lovReview.getId());
		// 合同状态更新
//		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");//待评审
//		contractService.updateContractStatus(businessKey, staLov.getId());
		KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
		prje.setEvlSt(lovReview.getId());

		// 通过 businessId , module 获取审批历史
		HistoryProcessInstance pi = historyService.get(processInstanceId);
		List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
		if(historyList.size()>0){
			for(int i= historyList.size()-1;i>0;i--){
				HistoryActivityInstance lastRec = historyList.get(i);
				if(lastRec != null ){
					if(lastRec.getComment() == null || lastRec.getComment()==""){
						continue;
					}else{
						prje.setEvlMm(!(lastRec.getOperatorName()==null || lastRec.getOperatorName().equalsIgnoreCase(""))?lastRec.getAssigneeName():lastRec.getOperatorName());
						prje.setEvlSg(comment);
						break;
					}
				}
			}
		}
		
		contractService.updatePrjEvl(prje);
		

		/*
		 *  若 		专业评审：
		 *  1、售前
			2、售后
			3、商务
			4、法务
			5、风控
			6、价格

			当有这些评审类别时，若上述专业评审状态都为”已审批“时，自动启动综合评审（若无综合评审时发起）。
		 */
		LovMember peLov = lovMemberService.get( prje.getEvlTyp());
		if(!peLov.getCode().equalsIgnoreCase("E07") && !peLov.getCode().equalsIgnoreCase("E08")){
			contractService.startContrSumReviewFlow(businessKey, IConstants.CONTR_CHANGE);
		}
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {

		// 合同审批状态
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//05	已销毁
		KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
		prje.setEvlSt(lovReview.getId());

		// 通过 businessId , module 获取审批历史
		HistoryProcessInstance pi = historyService.get(processInstanceId);
		List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
		if(historyList.size()>0){
			for(int i= historyList.size()-1;i>0;i--){
				HistoryActivityInstance lastRec = historyList.get(i);
				if(lastRec != null ){
					if(lastRec.getComment() == null || lastRec.getComment()==""){
						continue;
					}else{
						//${(history.operatorName)!(history.assigneeName)!}
						prje.setEvlMm(!(lastRec.getOperatorName()==null || lastRec.getOperatorName().equalsIgnoreCase(""))?lastRec.getAssigneeName():lastRec.getOperatorName());
						prje.setEvlSg(comment);
						break;
					}
				}
			}
		}

		contractService.updatePrjEvl(prje);
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId,String comment) {
		// 合同审批状态
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//04	已驳回
		// 合同变更初审状态更新
		KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
		prje.setEvlSt(lovReview.getId());
		// 通过 businessId , module 获取审批历史
		HistoryProcessInstance pi = historyService.get(processInstanceId);
		List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
		if(historyList.size()>0){
			for(int i= historyList.size()-1;i>0;i--){
				HistoryActivityInstance lastRec = historyList.get(i);
				if(lastRec != null ){
					if(lastRec.getComment() == null || lastRec.getComment()==""){
						continue;
					}else{
						prje.setEvlMm(!(lastRec.getOperatorName()==null || lastRec.getOperatorName().equalsIgnoreCase(""))?lastRec.getAssigneeName():lastRec.getOperatorName());
						prje.setEvlSg(comment);
						break;
					}
				}
			}
		}
		contractService.updatePrjEvl(prje);
		// 合同变更状态
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");//06	返回修改中
		changeService.updateContrChgStatus(businessKey,staLov.getId());
	}
	
	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		ContrChange contr = changeService.get(businessKey);
		String conStatCode= lovMemberService.get(contr.getChangeStat()).getCode();

		KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
		String flowStatCode= lovMemberService.get(prje.getEvlSt()).getCode();	
		if( conStatCode.equalsIgnoreCase("06") && flowStatCode.equalsIgnoreCase("04")){
			// 合同变更状态
			LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");//待评审
			changeService.updateContrChgStatus(businessKey,staLov.getId());
			// 合同变更评审状态更新
			LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");//02	审批中
			prje.setEvlSt(lovReview.getId());
			// 通过 businessId , module 获取审批历史
			HistoryProcessInstance pi = historyService.get(processInstanceId);
			List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
			if(historyList.size()>0){
				for(int i= historyList.size()-1;i>0;i--){
					HistoryActivityInstance lastRec = historyList.get(i);
					if(lastRec != null ){
						if(lastRec.getComment() == null || lastRec.getComment()==""){
							continue;
						}else{
							prje.setEvlMm(!(lastRec.getOperatorName()==null || lastRec.getOperatorName().equalsIgnoreCase(""))?lastRec.getAssigneeName():lastRec.getOperatorName());
							prje.setEvlSg(comment);
							break;
						}
					}
				}
			}
			contractService.updatePrjEvl(prje);
			
		}
	}

	@Override
	public String processPath() {
		return "/change/tabs.html";
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
		if(event.contains("驳回销售")){
			// 合同初审状态更新
			LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//04	已驳回
//			contractService.updateContractTrialStatus(businessKey, lov.getId());
			/*
			 * 驳回时， 合同评审的状态 仍为 “待评审”
			 */
			KstarPrjEvl prje=contractService.getKstarPrjevlByProcessId(businessKey,processInstanceId);
			prje.setEvlSt(lov.getId());
			// 通过 businessId , module 获取审批历史
			HistoryProcessInstance pi = historyService.get(processInstanceId);
			List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
			if(historyList.size()>0){
				for(int i= historyList.size()-1;i>0;i--){
					HistoryActivityInstance lastRec = historyList.get(i);
					if(lastRec != null ){
						if(lastRec.getComment() == null || lastRec.getComment()==""){
							continue;
						}else{
							prje.setEvlMm(!(lastRec.getOperatorName()==null || lastRec.getOperatorName().equalsIgnoreCase(""))?lastRec.getAssigneeName():lastRec.getOperatorName());
							prje.setEvlSg(comment);
							break;
						}
					}
				}
			}
			
			contractService.updatePrjEvl(prje);

			// 合同变更状态
			LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");//06	返回修改中
			changeService.updateContrChgStatus(businessKey,staLov.getId());
			
		}
	}
}
