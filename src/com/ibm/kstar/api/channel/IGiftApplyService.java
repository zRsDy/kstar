package com.ibm.kstar.api.channel;

import java.math.BigDecimal;
import java.util.Date;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.GiftApply;

public interface IGiftApplyService {
	
	IPage queryApplys(PageCondition condition, UserObject user);
	
	GiftApply queryApply(String id);
	
	void addOrEdit(GiftApply giftApply, UserObject user);
	
	void delete(String id);
	
	void submit(String id,UserObject user);
	
	BigDecimal getAvailableLimit(String currencyId,Date applyDate);
	
	/**
	 * @param giftApplyId
	 * @param type 0:流程提交；1:流程驳回或销毁
	 */
	void dealInventoryQuantity(String giftApplyId,int type);

}
