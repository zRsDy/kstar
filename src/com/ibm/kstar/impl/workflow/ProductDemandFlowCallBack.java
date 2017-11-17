package com.ibm.kstar.impl.workflow;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.product.IDemandService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductDemand;

/**
 * @author lhm
 * @version 1.0.0 2017-03-23
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class ProductDemandFlowCallBack extends BaseFlowCallBack {
	
	@Autowired
	BaseDao baseDao;
	@Autowired
	IDemandService demandService;
    
	@Override
	public String processPath() {
		return "/product/demandAdd.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "KstarProductDemand";
	}

	@Override
	String getStatusName() {
		return "demandStatus";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PRODUCT_DEMAND_PROC;
	}
	
    @Override
    @Transactional(readOnly = false,noRollbackFor = Exception.class)
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        /*
         * 更新产品需求单的状态为已审批
         */
    	processStatusService.updateProcessStatus(getEntityName(), businessKey, getStatusName(), getLovId(ProcessConstants.PROCESS_STATUS_03));
        
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserObject user = (UserObject) request.getSession().getAttribute("LOGIN_USER");
        /*
         * 根据需求申请单id获取需求单
         */
        KstarProductDemand demand = baseDao.get(KstarProductDemand.class, businessKey);
        
        
        if(demand.getProductID() == null){
        	demandService.demandInBound(businessKey, user, true);
        }else{
        	demandService.demandInBound(businessKey, user, false);
        }
    }
}
