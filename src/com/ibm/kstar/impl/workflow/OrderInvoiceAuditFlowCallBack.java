package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IInvoiceService;


/**
 * 
 * ClassName: 开票单审核工作流回调 <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年3月20日 上午11:11:04 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class OrderInvoiceAuditFlowCallBack extends IXflowInterface{

	@Autowired
	IInvoiceService invoiceService;
	
	@Autowired
	IProcessService processService;
	
	@Override
	public void onComplete(String businessKey, String flowModule, String processInstanceId,String comment) {
		invoiceService.updateStatus(businessKey, IConstants.ORDER_CONTROL_STATUS_30,null);
		/*
		 * 徐建 2017/11/15增加在回调中判断开票申请的发货行是否可以开票，如果不能开票流程不能结束
		 */
		invoiceService.validateCanInvoice(businessKey);
	}

	@Override
	public void onDestory(String businessKey, String flowModule, String processInstanceId,String comment) {
		
	}
	
	public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant,String comment) {
		ProcessInstance pi = processService.get(processInstanceId);
		if(pi !=null){
			try{
				invoiceService.setBusinessVariable(businessKey,processInstanceId, participant);
			}catch(Exception e){
				//如果流程已经结束会报错，忽略错误
			}
		}
	}

	@Override
	public String processPath() {
		return "/invoice/edit.html";
	}

	@Override
	public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
		invoiceService.updateStatus(businessKey, IConstants.ORDER_CONTROL_STATUS_40,null);
	}

	@Override
	public String viewPath() {
		return "/invoice/edit.html";
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
