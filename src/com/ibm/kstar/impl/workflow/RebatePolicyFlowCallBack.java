package com.ibm.kstar.impl.workflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.kstar.action.common.process.ProcessConstants;

/**
 * @author lhm
 * @version 1.0.0 2017-03-16
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class RebatePolicyFlowCallBack extends BaseFlowCallBack {
    
	@Override
	public String processPath() {
		return "/rebatePolicy/addOrEdit.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "RebatePolicy";
	}

	@Override
	String getStatusName() {
		return "status";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.REBATE_POLICY_PROC;
	}
}
