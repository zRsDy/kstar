package com.ibm.kstar.impl.workflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.kstar.action.common.process.ProcessConstants;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ProductPreSaleFlowCallBack extends BaseFlowCallBack {
	
	@Override
	public String processPath() {
		return "/product/edit.html";
	}
	
	@Override
	String getEntityName() {
		return "KstarProduct";
	}

	@Override
	String getStatusName() {
		return "saleStatus";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PM_PTS_PROC;
	}
}
