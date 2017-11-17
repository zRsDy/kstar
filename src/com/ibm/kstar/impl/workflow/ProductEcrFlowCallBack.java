package com.ibm.kstar.impl.workflow;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.product.IEcrService;
import com.ibm.kstar.api.system.permission.UserObject;

/**
 * @author lhm
 * @version 1.0.0 2017-03-23
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class ProductEcrFlowCallBack extends BaseFlowCallBack {
	
	@Autowired
	IEcrService ecrService;
    
	@Override
	public String processPath() {
		return "/product/ecrEdit.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "KstarEcrBean";
	}

	@Override
	String getStatusName() {
		return "ecrStatus";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PRODUCT_ECR_PROC;
	}
	
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus(getEntityName(), businessKey, getStatusName(), getLovId(ProcessConstants.PROCESS_STATUS_03));
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserObject user = (UserObject) request.getSession().getAttribute("LOGIN_USER");
        ecrService.ecrInBound(businessKey, user);
    }
}
