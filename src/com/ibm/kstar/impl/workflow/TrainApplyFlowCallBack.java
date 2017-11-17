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
public class TrainApplyFlowCallBack extends BaseFlowCallBack {
    
	@Override
	public String processPath() {
		return "/activityApply/addOrEditTrain.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "ActivityApply";
	}

	@Override
	String getStatusName() {
		return "status";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.ACTIVITY_APPLY_PROC;
	}
}
