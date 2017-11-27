/** 
 * Project Name:crm 
 * File Name:QuotspproctFlowCallBack.java 
 * Package Name:com.ibm.kstar.impl.workflow 
 * Date:Mar 16, 20173:29:15 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.impl.workflow;

import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.entity.quot.KstarBiddcevl;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import java.util.List;

/** 
 * ClassName:QuotspproctFlowCallBack <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Mar 16, 2017 3:29:15 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Service
 @Transactional(readOnly = false, rollbackFor = Exception.class) 
public class QuotspproctFlowCallBack extends IXflowInterface {

	@Autowired
	IProductProcesService productProcess;

	@Autowired
	IQuotService quotService;
	 
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		KstarQuot quot;
		KstarBiddcevl bidevl = quotService.getBiddcevl(processInstanceId);

		if(bidevl!=null){
			quot = quotService.getKstarQuot(bidevl.getQuotCode());
			quot.setSpAuditStatus("P02");
			quotService.updateQuot(quot);

            PageCondition condition = new PageCondition();
            condition.setCondition("qid", quot.getId());
            condition.setCondition("typ", "003");
            IPage p = quotService.queryPrjLst(condition);

            List<KstarPrjLst> kstarPrjLsts = (List<KstarPrjLst>) p.getList();
			// 判断所有行是否都有批复折扣
            boolean hasApprovalDiscount = true;
            for (KstarPrjLst kstarPrjLst : kstarPrjLsts) {
				if (kstarPrjLst.getApplyDiscount() == null || kstarPrjLst.getApprovePrc() == null) {
                    hasApprovalDiscount = false;
                    break;
				}

                if (!(kstarPrjLst.getApproveDiscount() > 0 && kstarPrjLst.getApproveDiscount() < 100)) {
                    hasApprovalDiscount = false;
                    break;
                }
			}

            if (!hasApprovalDiscount) {
                throw new RuntimeException("存在批复折扣和价格不正确的产品，批复价格和批复折扣不能为空，并且批复折扣大于0%小于100%");
            }
		}



	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		KstarQuot quot;
		
		KstarBiddcevl bidevl = quotService.getBiddcevl(processInstanceId);
		
		if(bidevl!=null){
			quot = quotService.getKstarQuot(bidevl.getQuotCode());
			quot.setSpAuditStatus("P04");
			quotService.updateQuot(quot);
		}
		
	}

	@Override
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String processPath() {
		// TODO Auto-generated method stub
		return "/quotprocess/processtabs.html";
	}

	@Override
	public String viewPath() {
		// TODO Auto-generated method stub
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
  
