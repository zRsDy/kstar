package com.ibm.kstar.impl.workflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.kstar.action.common.process.ProcessConstants;

/**
 * @author lhm
 * @version 1.0.0 2017-03-23
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class ProductPublishFlowCallBack extends BaseFlowCallBack {
    
	@Override
	public String processPath() {
		return "/product/edit.html";
	}
	
	String getEntityName() {
		return "KstarProduct";
	}
	
	@Override
	String getStatusName() {
		return "publishStatus";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PRODUCT_PUBLISH_PROC;
	}
}
