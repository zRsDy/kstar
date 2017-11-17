package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateClause;

public interface IRebateClauseService {
	
	IPage queryClauses(PageCondition condition);
	
	RebateClause queryClause(String id);
	
	void addOrEdit(RebateClause clause, UserObject user);
	
	void delete(String id);

}
