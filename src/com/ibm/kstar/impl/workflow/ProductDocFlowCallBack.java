package com.ibm.kstar.impl.workflow;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;

/**
 * @author lhm
 * @version 1.0.0 2017-03-23
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class) 
public class ProductDocFlowCallBack extends BaseFlowCallBack {
	
	@Autowired
	BaseDao baseDao;
	@Autowired
	protected ITeamService teamService;
    
	@Override
	public String processPath() {
		return "/product/docAdd.html?ableEdit=true";
	}
	
	@Override
	String getEntityName() {
		return "KstarProductDoc";
	}

	@Override
	String getStatusName() {
		return "applyStatus";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PRODUCT_DOC_PROC;
	}
	
    public void ownerTask(String businessKey) {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	UserObject user = (UserObject) request.getSession().getAttribute("LOGIN_USER");
    	//加销售团队
    	teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),"productDoc",businessKey);
    }
}
