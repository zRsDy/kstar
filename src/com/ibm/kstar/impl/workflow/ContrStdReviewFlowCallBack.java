package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrStdReviewFlowCallBack extends IXflowInterface {


    @Autowired
    private ILovMemberService lovMemberService;

    @Autowired
    private IContractService contractService;
    @Autowired
    IHistoryService historyService;

    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {

        Contract contract = contractService.get(businessKey);

        // 合同审批状态
        LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//已审批
        //		contractService.updateContractTrialStatus(businessKey, lovReview.getId());
        // 合同状态更新
        //		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");//待评审
        //		contractService.updateContractStatus(businessKey, staLov.getId());
        //		KstarPrjEvl prje=contractService.getKstarPrjEvl(businessKey);
        KstarPrjEvl prje = contractService.getKstarPrjevlByProcessId(businessKey, processInstanceId);
        prje.setEvlSt(lovReview.getId());

        // 通过 businessId , module 获取审批历史
        HistoryProcessInstance pi = historyService.get(processInstanceId);
        List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
        if (historyList.size() > 0) {
            for (int i = historyList.size() - 1; i > 0; i--) {
                HistoryActivityInstance lastRec = historyList.get(i);
                if (lastRec != null) {
                    if (lastRec.getComment() == null || lastRec.getComment() == "") {
                        continue;
                    } else {
                        //${(history.operatorName)!(history.assigneeName)!}
                        prje.setEvlMm(!(lastRec.getOperatorName() == null || lastRec.getOperatorName().equalsIgnoreCase("")) ? lastRec.getAssigneeName() : lastRec.getOperatorName());
                        prje.setEvlSg(comment);
                        break;
                    }
                }
            }
        }

        contractService.updatePrjEvl(prje);

        //综合评审
        LovMember e07 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E07");
        if (e07.getId().equals(prje.getEvlTyp())) {
            //待评审 03
            LovMember status03 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");
            if (status03.getId().equals(contract.getContrStat())) {
                //待确认评审完成 15
                LovMember status15 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "15");
                contractService.updateContractStatus(businessKey, status15.getId());
            }
        }

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
        LovMember peLov = lovMemberService.get(prje.getEvlTyp());
        if (!peLov.getCode().equalsIgnoreCase("E07") && !peLov.getCode().equalsIgnoreCase("E08")) {
            LovMember contracttype = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_STAND_0103);
            if (contract != null && Objects.equals(contract.getContrType(), contracttype.getId())) {
                //框架下合同商务评审完成自动开启综合评审流程
                if (peLov.getCode().equalsIgnoreCase("E03")) {
                    contractService.startContrSumReviewFlow(businessKey, IConstants.CONTR_STAND);
                }
            } else {
                contractService.startContrSumReviewFlow(businessKey, IConstants.CONTR_STAND);
            }
        }

    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {

        // 合同审批状态
        LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//05	已销毁
        KstarPrjEvl prje = contractService.getKstarPrjevlByProcessId(businessKey, processInstanceId);
        prje.setEvlSt(lovReview.getId());

        // 通过 businessId , module 获取审批历史
        HistoryProcessInstance pi = historyService.get(processInstanceId);
        List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
        if (historyList.size() > 0) {
            for (int i = historyList.size() - 1; i > 0; i--) {
                HistoryActivityInstance lastRec = historyList.get(i);
                if (lastRec != null) {
                    if (lastRec.getComment() == null || lastRec.getComment() == "") {
                        continue;
                    } else {
                        //${(history.operatorName)!(history.assigneeName)!}
                        prje.setEvlMm(!(lastRec.getOperatorName() == null || lastRec.getOperatorName().equalsIgnoreCase("")) ? lastRec.getAssigneeName() : lastRec.getOperatorName());
                        prje.setEvlSg(comment);
                        break;
                    }
                }
            }
        }

        contractService.updatePrjEvl(prje);
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        // 合同初审状态更新
        LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//04	已驳回
        //		contractService.updateContractTrialStatus(businessKey, lov.getId());
        /*
         * 驳回时， 合同评审的状态 仍为 “待评审”
		 */
        KstarPrjEvl prje = contractService.getKstarPrjevlByProcessId(businessKey, processInstanceId);
        prje.setEvlSt(lov.getId());
        // 通过 businessId , module 获取审批历史
        HistoryProcessInstance pi = historyService.get(processInstanceId);
        List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
        if (historyList.size() > 0) {
            for (int i = historyList.size() - 1; i > 0; i--) {
                HistoryActivityInstance lastRec = historyList.get(i);
                if (lastRec != null) {
                    if (lastRec.getComment() == null || lastRec.getComment() == "") {
                        continue;
                    } else {
                        prje.setEvlMm(!(lastRec.getOperatorName() == null || lastRec.getOperatorName().equalsIgnoreCase("")) ? lastRec.getAssigneeName() : lastRec.getOperatorName());
                        prje.setEvlSg(comment);
                        break;
                    }
                }
            }
        }

        contractService.updatePrjEvl(prje);

        // 合同状态更新
        LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");//06	返回修改中
        contractService.updateContractStatus(businessKey, staLov.getId());
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant, String comment) {
        Contract contr = contractService.get(businessKey);
        String conStatCode = lovMemberService.get(contr.getContrStat()).getCode();
        //		String flowStatCode= lovMemberService.get(contr.getReviewStat()).getCode();
        KstarPrjEvl prje = contractService.getKstarPrjevlByProcessId(businessKey, processInstanceId);
        String flowStatCode = lovMemberService.get(prje.getEvlSt()).getCode();

        if (conStatCode.equalsIgnoreCase("06") && flowStatCode.equalsIgnoreCase("04")) {

            //4、评审流程驳回后再提交后判断是否有完成过综合评审，有则更新合同状态为“待确认评审完成”，无则更新合同状态为“待评审”
            //审批完成的综合评审
            KstarPrjEvl e07 = null;
            List<KstarPrjEvl> evls = contractService.queryContrPrjevlListByContrId(businessKey);
            //评审状态 03:已完成
            LovMember status03 = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");
            LovMember status07 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E07");
            for (KstarPrjEvl evl : evls) {
                // E07 综合评审
                if (status07.getId().equalsIgnoreCase(evl.getEvlTyp()) && status03.getId().equalsIgnoreCase(evl.getEvlSt())) {
                    e07 = evl;
                    break;
                }
            }
            if (e07 != null) {
                //待确认评审完成 15
                LovMember status15 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "15");
                contractService.updateContractStatus(businessKey, status15.getId());
            } else {
                //03	待评审
                LovMember status15 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");
                contractService.updateContractStatus(businessKey, status15.getId());
            }

            LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");//02	审批中
            prje.setEvlSt(lov.getId());
            // 通过 businessId , module 获取审批历史
            HistoryProcessInstance pi = historyService.get(processInstanceId);
            List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
            if (historyList.size() > 0) {
                for (int i = historyList.size() - 1; i > 0; i--) {
                    HistoryActivityInstance lastRec = historyList.get(i);
                    if (lastRec != null) {
                        if (lastRec.getComment() == null || lastRec.getComment() == "") {
                            continue;
                        } else {
                            prje.setEvlMm(!(lastRec.getOperatorName() == null || lastRec.getOperatorName().equalsIgnoreCase("")) ? lastRec.getAssigneeName() : lastRec.getOperatorName());
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
        return "/standard/tabs.html";
    }

    @Override
    public boolean newType() {
        return true;
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

        //合同状态06	返回修改中
        LovMember flowStatus06 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "06");

        KstarPrjEvl prje = contractService.getKstarPrjevlByProcessId(businessKey, processInstanceId);

        if (event.contains("驳回销售")) {
            //评审状态04 已驳回
            LovMember reviewStatus04 = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");
            prje.setEvlSt(reviewStatus04.getId());
            // 通过 businessId , module 获取审批历史
            HistoryProcessInstance pi = historyService.get(processInstanceId);
            List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
            if (historyList.size() > 0) {
                for (int i = historyList.size() - 1; i > 0; i--) {
                    HistoryActivityInstance lastRec = historyList.get(i);
                    if (lastRec != null) {
                        if (lastRec.getComment() == null || lastRec.getComment() == "") {
                            continue;
                        } else {
                            prje.setEvlMm(!(lastRec.getOperatorName() == null || lastRec.getOperatorName().equalsIgnoreCase("")) ? lastRec.getAssigneeName() : lastRec.getOperatorName());
                            prje.setEvlSg(comment);
                            break;
                        }
                    }
                }
            }
            contractService.updatePrjEvl(prje);
            contractService.updateContractStatus(businessKey, flowStatus06.getId());
            return ;
        }

        Contract contract = contractService.get(businessKey);
        if (flowStatus06.getId().equals(contract.getContrStat())) {
            //评审状态02 审批中
            LovMember reviewStatus02 = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
            //合同状态03	待评审
            LovMember flowStatus03 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");

            prje.setEvlSt(reviewStatus02.getId());
            prje.setEvlMm(comment);

            contractService.updatePrjEvl(prje);
            contractService.updateContractStatus(businessKey, flowStatus03.getId());

        }
    }
}
