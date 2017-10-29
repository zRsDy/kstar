package com.ibm.kstar.api.channel;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.RebateProductDetail;

public interface IRebateProductDetailService {

	IPage queryDetails(PageCondition condition);
	
	RebateProductDetail queryDetail(String id);
	
	void addOrEdit(RebateProductDetail detail, UserObject user);

	void delete(String id);
	
}
