package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebatePolicy;

public interface IRebatePolicyService {
	
	IPage queryPolicys(PageCondition condition);
	
	RebatePolicy queryPolicy(String id);
	
	void addOrEdit(RebatePolicy rebatePolicy, UserObject user);

	void delete(String id);
	
	void submit(String id, UserObject user);
	
	void compute(String id, UserObject user);

}
