/** 
 * Project Name:crm 
 * File Name:QuotsbmrocFlowCallBack.java 
 * Package Name:com.ibm.kstar.impl.workflow 
 * Date:Apr 12, 20172:24:50 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.entity.quot.KstarQuot;

/** 
 * ClassName:QuotsbmrocFlowCallBack <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Apr 12, 2017 2:24:50 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Service
 @Transactional(readOnly = false, rollbackFor = Exception.class) 
public class QuotsbmrocFlowCallBack extends IXflowInterface {

	@Autowired
	IQuotService quotService;
	 
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		KstarQuot quot = quotService.getKstarQuot(businessKey);
		quot.setProReviewStatus("S02");
		quotService.updateQuot(quot);
		
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
		KstarQuot quot = quotService.getKstarQuot(businessKey);
		quot.setProReviewStatus("S04");
		quotService.updateQuot(quot);
		
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
  
