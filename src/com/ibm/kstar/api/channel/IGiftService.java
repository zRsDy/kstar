package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.GiftManage;

public interface IGiftService {
	
	IPage queryGifts(PageCondition condition);
	
	void saveOrUpdateGift(GiftManage giftManage, UserObject user);

	GiftManage queryGift(String id);
	
	void delete(GiftManage giftManage);
}
