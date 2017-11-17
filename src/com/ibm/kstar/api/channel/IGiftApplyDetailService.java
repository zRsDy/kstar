package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.GiftApplyDetail;

public interface IGiftApplyDetailService {
	
	IPage queryDetails(PageCondition condition);
	
	GiftApplyDetail queryDetail(String id);
	
	void addOrEdit(GiftApplyDetail detail, UserObject user);
	
	void delete(String[] ids);
	
	void giveDetails(String[] ids, UserObject user);
	
	void getDetails(String[] ids, UserObject user);
	
	void updateColumnValue(String id,String column,String value, UserObject user);

}
