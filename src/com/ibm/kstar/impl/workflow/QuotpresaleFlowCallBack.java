/** 
 * Project Name:crm 
 * File Name:QuotpresaleFlowCallBack.java 
 * Package Name:com.ibm.kstar.impl.workflow 
 * Date:Mar 15, 201710:31:54 AM 
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
 * ClassName:QuotpresaleFlowCallBack <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Mar 15, 2017 10:31:54 AM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
 
 @Service
 @Transactional(readOnly = false, rollbackFor = Exception.class) 
public class QuotpresaleFlowCallBack extends IXflowInterface {

		@Autowired
		IProductProcesService productProcess;
	 
		@Autowired
		IQuotService quotService;
	 
		@Override
		public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
			//System.out.println("============quot presale process complete=============");
			
			List<KstarProductWorkFlow> processworkflows = productProcess.getList(processInstanceId);
			
			for(KstarProductWorkFlow processwk : processworkflows){
				
				String prjevlID = processwk.getProductId();
				KstarPrjEvl prjevl = quotService.getKstarPrjEvl(prjevlID);
				prjevl.setEvlSt("S02");
				quotService.updatePrjEvl(prjevl);
				
//				String quotID = prjevl.getQuotCode();
//				KstarQuot quot = quotService.getKstarQuot(quotID);
//				quot.setProReviewStatus("S02");
//				quotService.updateQuot(quot);
				
				

			}
			
		}

		@Override
		public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
			
		}
		
		public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
			
		}

		@Override
		public String processPath() {
			return "/quotprocess/processtabs.html";
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
		public void onEvent(String event, String businessKey, String flowModule, String processInstanceId,
				String comment) {
			// TODO Auto-generated method stub
			
		}

	 
}
  
