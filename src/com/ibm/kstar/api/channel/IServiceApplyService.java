package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.ApplyEquipment;
import com.ibm.kstar.entity.channel.ServiceApply;

public interface IServiceApplyService {
	
	IPage queryServiceApplys(PageCondition condition, UserObject user);
	
	void saveOrUpdateServiceApply(ServiceApply serviceApply, UserObject user);

	ServiceApply queryServiceApply(String id);
	
	void deleteServiceApply(ServiceApply serviceApply);
	
	void submit(String id, UserObject user);
	
	IPage queryApplyEquipments(PageCondition condition);
	
	void saveOrUpdateApplyEquipment(ApplyEquipment applyEquipment, UserObject user);

	ApplyEquipment queryApplyEquipment(String id);
	
	void deleteApplyEquipment(ApplyEquipment applyEquipment);
	
}
