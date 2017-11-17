package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;

public interface IRebateDetailService {
	
	IPage queryRebateDetails(PageCondition condition);
	
	void checkDetail(String[] ids, UserObject user);
	
	void closeDetail(String[] ids, UserObject user);
	
	void deleteDetails(String[] ids);
	
	void publishDetail(String[] ids, UserObject user);
	
	IPage queryDeductionDetails(PageCondition condition);
	
	void updateColumnValue(String id,String column,String value, UserObject user);
}
