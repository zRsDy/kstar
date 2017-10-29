/** 
 * Project Name:crm 
 * File Name:QuotbidadtFlowCallBack.java 
 * Package Name:com.ibm.kstar.impl.workflow 
 * Date:Mar 16, 20171:46:48 PM 
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
import com.ibm.kstar.entity.quot.KstarBiddcevl;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.entity.quot.KstarQuot;

/** 
 * ClassName:QuotbidadtFlowCallBack <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Mar 16, 2017 1:46:48 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Service
 @Transactional(readOnly = false, rollbackFor = Exception.class) 
public class QuotbidadtFlowCallBack extends IXflowInterface {

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
			quot.setBidAuditStatus("B02");
			//更新技术评审状态
			quot.setTchAdtstatus("S02");
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
		
		KstarQuot quot;
		
		KstarBiddcevl bidevl = quotService.getBiddcevl(processInstanceId);
		
		if(bidevl!=null){
			quot = quotService.getKstarQuot(bidevl.getQuotCode());
			quot.setBidAuditStatus("B04");
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
  
