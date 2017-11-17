/** 
 * Project Name:crm 
 * File Name:QuotmrprocFlowCallBack.java 
 * Package Name:com.ibm.kstar.impl.workflow 
 * Date:May 15, 201711:34:06 AM 
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
import com.ibm.kstar.entity.quot.KstarQuot;

/** 
 * ClassName:QuotmrprocFlowCallBack <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     May 15, 2017 11:34:06 AM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Service
 @Transactional(readOnly = false, rollbackFor = Exception.class) 
public class QuotmrprocFlowCallBack extends IXflowInterface {

		@Autowired
		IProductProcesService productProcess;

		@Autowired
		IQuotService quotService;
	 
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		List<KstarProductWorkFlow> processworkflows = productProcess.getList(processInstanceId);
		
		for(KstarProductWorkFlow processwk : processworkflows){
			
			String quotID;
			
			quotID = processwk.getProductId();
			

			KstarQuot quot = quotService.getKstarQuot(quotID);
			quot.setMtrReqstatus("S02");
			quotService.updateQuot(quot);

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
						
			String quotID = processwk.getProductId();
			KstarQuot quot = quotService.getKstarQuot(quotID);
			quot.setMtrReqstatus("S04");
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
  
