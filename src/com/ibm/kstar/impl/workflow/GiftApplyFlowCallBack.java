package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.channel.IGiftApplyService;

/**
 * @author lhm
 * @version 1.0.0 2017-03-16
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class GiftApplyFlowCallBack extends BaseFlowCallBack {
	
	@Autowired
	IGiftApplyService giftApplyService;
    
	@Override
	public String processPath() {
		return "/giftApply/addOrEdit.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "GiftApply";
	}

	@Override
	String getStatusName() {
		return "status";
	}
   
	@Override
	String getGroupCode() {
		return ProcessConstants.GIFT_APPLY_PROC;
	}
	
	@Override
	protected void rejectDeal(String businessKey){
		giftApplyService.dealInventoryQuantity(businessKey, 1);
    }
}
