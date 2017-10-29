package com.ibm.kstar.api.price;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.price.BatchProductPrice;

public interface IBatchProductPriceService {
	
	IPage queryBatchProductPrices(PageCondition condition, UserObject user);
	
	void saveOrUpdateBatchProductPrice(BatchProductPrice batchProductPrice, UserObject user);
	
	BatchProductPrice queryBatchProductPrice(String id);

	BatchProductPrice query(String id);
	
	void deleteBatchProductPrice(BatchProductPrice batchProductPrice);
	
	void submitBatchProductPrice(UserObject user,String id);
}
