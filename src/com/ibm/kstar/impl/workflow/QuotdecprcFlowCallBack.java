/** 
 * Project Name:crm 
 * File Name:QuotdecprcFlowCallBack.java 
 * Package Name:com.ibm.kstar.impl.workflow 
 * Date:Mar 16, 201711:00:04 AM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.impl.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.entity.quot.KstarQuot;

/** 
 * ClassName:QuotdecprcFlowCallBack <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Mar 16, 2017 11:00:04 AM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Service
 @Transactional(readOnly = false, rollbackFor = Exception.class) 
public class QuotdecprcFlowCallBack extends IXflowInterface {

	@Autowired
	IProductProcesService productProcess;

	@Autowired
	IQuotService quotService;
	 
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		List<KstarProductWorkFlow> processworkflows = productProcess.getList(processInstanceId);
		
		for(KstarProductWorkFlow processwk : processworkflows){
			
			String prjevlID = processwk.getProductId();
			KstarPrjEvl prjevl = quotService.getKstarPrjEvl(prjevlID);
			prjevl.setEvlSt("S02");
			quotService.updatePrjEvl(prjevl);
			
//			String quotID = prjevl.getQuotCode();
//			KstarQuot quot = quotService.getKstarQuot(quotID);
//			quot.setProReviewStatus("S02");
//			quotService.updateQuot(quot);

		}
		
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		List<KstarProductWorkFlow> processworkflows = productProcess.getList(processInstanceId);
		
		for(KstarProductWorkFlow processwk : processworkflows){
			
			String prjevlID = processwk.getProductId();
			KstarPrjEvl prjevl = quotService.getKstarPrjEvl(prjevlID);
			prjevl.setEvlSt("S04");
			quotService.updatePrjEvl(prjevl);
			
			String quotID = prjevl.getQuotCode();
			KstarQuot quot = quotService.getKstarQuot(quotID);
			quot.setProReviewStatus("S04");
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
  
