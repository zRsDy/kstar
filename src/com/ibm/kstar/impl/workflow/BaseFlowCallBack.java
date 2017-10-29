package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

/**
 * @author lhm
 * @version 1.0.0 2017-03-16
 */
public abstract class BaseFlowCallBack extends IXflowInterface {

    @Autowired
    ProcessStatusService processStatusService;
    
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus(getEntityName(), businessKey, getStatusName(), getLovId(ProcessConstants.PROCESS_STATUS_03));
    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
    	rejectDeal(businessKey);
        processStatusService.updateProcessStatus(getEntityName(), businessKey, getStatusName(), getLovId(ProcessConstants.PROCESS_STATUS_04));
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
    	rejectDeal(businessKey);
        processStatusService.updateProcessStatus(getEntityName(), businessKey, getStatusName(), getLovId(ProcessConstants.PROCESS_STATUS_04));
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant, String comment) {
    	ownerTask(businessKey);
    }
	
	@Override
	public String viewPath() {
		return null;
	}

	@Override
	public String mobileView(String id) {
		return null;
	}

	@Override
	public void onEvent(String event, String businessKey, String flowModule, String processInstanceId, String comment) {
		
	}
    
    protected String getLovId(String code){
    	LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().getMember(getGroupCode(), code);
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov.getId();
    }

    abstract String getEntityName();
    
    abstract String getStatusName();
    
    abstract String getGroupCode();
    
    protected void rejectDeal(String businessKey){
    	
    }
    
    protected void ownerTask(String businessKey){
    	
    }

}
