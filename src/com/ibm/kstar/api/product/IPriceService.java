package com.ibm.kstar.api.product;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

public interface IPriceService {
	
	IPage querySql(PageCondition condition) throws Exception ;
}
