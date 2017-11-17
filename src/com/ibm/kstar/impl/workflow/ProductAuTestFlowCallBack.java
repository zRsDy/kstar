package com.ibm.kstar.impl.workflow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.Participant;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.entity.product.KstarProductAuTest;

/**
 * @author lhm
 * @version 1.0.0 2017-03-20
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class ProductAuTestFlowCallBack extends BaseFlowCallBack {
	
	@Autowired
	BaseDao baseDao;
	@Autowired
	IProcessService processService;
    
	@Override
	public String processPath() {
		return "/product/auTestAdd.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "KstarProductAuTest";
	}

	@Override
	String getStatusName() {
		return "status";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PRODUCT_AU_TEST_PROC;
	}
	
    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant, String comment) {
    	Map<String,String> varmap = new HashMap<>();
    	KstarProductAuTest kd = baseDao.get(KstarProductAuTest.class, businessKey);
    	varmap.put("CEO_APPROVE", kd.getIsCeoApprove());
    	processService.setBusinessVariable(processInstanceId, participant, varmap);
    }

}
