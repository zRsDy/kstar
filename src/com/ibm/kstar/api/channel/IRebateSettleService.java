package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateSettle;

public interface IRebateSettleService {
	
	IPage queryRebateSettles(PageCondition condition);
	
	RebateSettle queryRebateSettle(String id);
	
	void addOrEditSettle(RebateSettle settle, UserObject user);
	
	void checkSettle(String id, UserObject user);
	
	IPage queryRebateSettleDetails(PageCondition condition);
	
	void selectDetails(String[] ids, String settleId);
	
	void deleteDetails(String[] ids);
	
	void updateSettleMoney(String detailId, String money, UserObject user);
}
